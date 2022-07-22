package br.com.alura.microservice.loja.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.alura.microservice.loja.feign.FornecedorClient;
import br.com.alura.microservice.loja.model.Compra;
import br.com.alura.microservice.loja.request.CompraRequest;
import br.com.alura.microservice.loja.response.InfoFornecedorResponse;
import br.com.alura.microservice.loja.response.InfoPedidoResponse;

@Service
public class CompraService {

	@Autowired
	private  FornecedorClient fornecedorClient;
	
	public Compra realizaCompra(CompraRequest request) {

		InfoFornecedorResponse info = fornecedorClient.getInfoPorEstado(request.getEndereco().getEstado());
		
		InfoPedidoResponse pedido = fornecedorClient.realizarPedido(request.getItens());
		
		System.out.println(info.getEndereco());
		
		return new Compra(pedido.getId(), pedido.getTempoDePreparo(), request.getEndereco().toString());

	}

}
