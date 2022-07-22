package br.com.alura.microservice.loja.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	
	public Compra realizaCompra(CompraRequest request) {

		LOG.info("Buscando informações do fornecedor de {}", request.getEndereco().getEstado());
		InfoFornecedorResponse info = fornecedorClient.getInfoPorEstado(request.getEndereco().getEstado());
		
		// SPRING SLEUTH AJUDA  AGENTE A ACOMPANHAR LOGS, ELE CARREGA UM ID DA TRANSACAO POR TODOS OS DEMAIS MICRO SERVICOS
		// ATE COMPLETAR A TRANSACAO
		
		LOG.info("Realizando um pedido");
		InfoPedidoResponse pedido = fornecedorClient.realizarPedido(request.getItens());
		
		System.out.println(info.getEndereco());
		
		return new Compra(pedido.getId(), pedido.getTempoDePreparo(), request.getEndereco().toString());

	}

}
