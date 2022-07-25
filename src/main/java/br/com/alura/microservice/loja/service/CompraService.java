package br.com.alura.microservice.loja.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import br.com.alura.microservice.loja.feign.FornecedorClient;
import br.com.alura.microservice.loja.model.Compra;
import br.com.alura.microservice.loja.request.CompraRequest;
import br.com.alura.microservice.loja.response.InfoFornecedorResponse;
import br.com.alura.microservice.loja.response.InfoPedidoResponse;

@Service
public class CompraService {

	private static final Logger LOG = LoggerFactory.getLogger(CompraService.class);
	
	@Autowired
	private  FornecedorClient fornecedorClient;
	
	// HYSTRIX MONITORA A EXECUCAO, CASO TENHA MUITOS ERROS NA EXECUCAO DO METODO realizaCompra
	// ELE EXECUTA DIRETAMENTE O METODO DE FALLBACK, E DEPOIS DE ALGUM TEMPO ELE RETORNA AO METODO realizaCompra
	// PARA VERIFICAR SE O MESMO VOLTOU A FUNCIONAR NORMALMENTE
	@HystrixCommand(fallbackMethod = "realizaCompraFallback")
	public Compra realizaCompra(CompraRequest request) {

		LOG.info("Buscando informações do fornecedor de {}", request.getEndereco().getEstado());
		InfoFornecedorResponse info = fornecedorClient.getInfoPorEstado(request.getEndereco().getEstado());
		
		// SPRING SLEUTH AJUDA  AGENTE A ACOMPANHAR LOGS, ELE CARREGA UM ID DA TRANSACAO POR TODOS OS DEMAIS MICRO SERVICOS
		// ATE COMPLETAR A TRANSACAO
		
		LOG.info("Realizando um pedido");
		InfoPedidoResponse pedido = fornecedorClient.realizarPedido(request.getItens());
		
		// ESSE CARA PARA A THREAD SEM A NECESSIDADE DO BREAK POINT
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(info.getEndereco());
		
		return new Compra(pedido.getId(), pedido.getTempoDePreparo(), request.getEndereco().toString());

	}
	
	// BASICAMENTE ESSE É UM MÉTODO PARA TRATAMENTO DE ERRO NO CASO DE UM CRICUIT BREAKER
	public Compra realizaCompraFallback(CompraRequest request) {
		
		return new Compra(1L, 1, "Teste fallback");
		
	}

}
