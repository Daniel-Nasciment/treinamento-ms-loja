package br.com.alura.microservice.loja.service;

import java.time.LocalDate;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import br.com.alura.microservice.loja.feign.FornecedorClient;
import br.com.alura.microservice.loja.feign.TransportadorClient;
import br.com.alura.microservice.loja.model.Compra;
import br.com.alura.microservice.loja.model.CompraState;
import br.com.alura.microservice.loja.repository.CompraRepository;
import br.com.alura.microservice.loja.request.CompraRequest;
import br.com.alura.microservice.loja.request.InfoEntregaRequest;
import br.com.alura.microservice.loja.response.InfoFornecedorResponse;
import br.com.alura.microservice.loja.response.InfoPedidoResponse;
import br.com.alura.microservice.loja.response.VoucherResponse;

@Service
public class CompraService {

	private static final Logger LOG = LoggerFactory.getLogger(CompraService.class);
	
	@Autowired
	private CompraRepository compraRepository;
	
	@Autowired
	private  FornecedorClient fornecedorClient;
	
	@Autowired
	private  TransportadorClient transportadorClient;
	
	// HYSTRIX MONITORA A EXECUCAO, CASO TENHA MUITOS ERROS NA EXECUCAO DO METODO realizaCompra
	// ELE EXECUTA DIRETAMENTE O METODO DE FALLBACK, E DEPOIS DE ALGUM TEMPO ELE RETORNA AO METODO realizaCompra
	// PARA VERIFICAR SE O MESMO VOLTOU A FUNCIONAR NORMALMENTE
	@HystrixCommand(fallbackMethod = "realizaCompraFallback", threadPoolKey = "realizaCompraThreadPool")
	public Compra realizaCompra(CompraRequest request) {

		Compra compra = new Compra(request.getEndereco().toString(), CompraState.RECEBIDO);
		compraRepository.save(compra);
		// DESSA FORMA EU PASSO O ID DA COMPRA AO FALLBACK CASO ACONTEÇA ALGUMA EXCEPTION
		request.setIdCompra(compra.getId());
		
		LOG.info("Buscando informações do fornecedor de {}", request.getEndereco().getEstado());
		InfoFornecedorResponse info = fornecedorClient.getInfoPorEstado(request.getEndereco().getEstado());
		LOG.info("Endereco info fornecedor: {}", info.getEndereco());
		
		// SPRING SLEUTH AJUDA  AGENTE A ACOMPANHAR LOGS, ELE CARREGA UM ID DA TRANSACAO POR TODOS OS DEMAIS MICRO SERVICOS
		// ATE COMPLETAR A TRANSACAO
		
		
		LOG.info("Realizando um pedido");
		InfoPedidoResponse pedido = fornecedorClient.realizarPedido(request.getItens());
		
		compra.setIdPedido(pedido.getId());
		compra.setTempoDePreparo(pedido.getTempoDePreparo());
		compra.setState(CompraState.COMPRA_FORNECEDOR_REALIZADA);
		compraRepository.save(compra);

		// TESTE DA LÓGICA ONDE A LOJA NÃO TEM QUE SE DESDOBRAR PARA TRATAR ERROS GERADOS POR OUTROS MS
		// O MS LOJA TEM SEU FLUXO A SER SEGUIDO
		//if (1 == 1) throw new RuntimeException();
		
		VoucherResponse reserva = transportadorClient.reservarEntrega(prepareRequestTransportador(request, info, pedido));
		
		compra.setPrevisaoParaEntrega(reserva.getPrevisaoParaEntrega());
		compra.setVocher(reserva.getNumero());
		compra.setState(CompraState.COMPRA_PROCESSADA_ENTREGA_TRANSPORTADOR);
		compraRepository.save(compra);
		
		return compra;

	}

	private InfoEntregaRequest prepareRequestTransportador(CompraRequest request, InfoFornecedorResponse info, InfoPedidoResponse pedido) {
		
		InfoEntregaRequest entregaRequest = new InfoEntregaRequest();
		
		entregaRequest.setPedidoId(pedido.getId());
		entregaRequest.setDataParaEntrega(LocalDate.now().plusDays(pedido.getTempoDePreparo()));
		entregaRequest.setEnderecoOrigem(info.getEndereco());
		entregaRequest.setEnderecoDestino(request.getEndereco().toString());
		
		return entregaRequest;
	}
	
	// BASICAMENTE ESSE É UM MÉTODO PARA TRATAMENTO DE ERRO NO CASO DE UM CRICUIT BREAKER
	public Compra realizaCompraFallback(CompraRequest request) {
		
		LOG.info("Entrando no fallback");
		
		Compra compra = null;
		
		if(request.getIdCompra() != null) {
			compra = compraRepository.findById(request.getIdCompra()).get();
		}
		
		return compra;
		
	}

	@HystrixCommand(threadPoolKey = "buscarCompraThreadPool")
	public Compra buscarCompra(Long id) {

		// threadPoolKey É A FORMA DE CONFIGURAR O BULKHEAD PARA DIVISÃO DE THREADS 
		// DADO O EXEMPLO DE QUE UM MS NÃO ESTEJA FUNCIONANDO COMO ESPERADO
		// FARIAMOS 10 REQUISIÇÕES QUE FALHARIAM/DEMORARIAM E O HYSTRIX GUARDARIA ESSAS REQUISIÇÕES CONSUMINDO TODAS AS THREADS
		// CONDIFURANDO DESSA FORMA O HYSTRIX BULKHEAD AGRUPA AS THREADS COM A CONFIGURAÇÃO threadPoolKey
		
		Optional<Compra> compra = compraRepository.findById(id);
		
		if(compra.isPresent()) {
			return compra.get();
		}
		
		return null;
	}

}
