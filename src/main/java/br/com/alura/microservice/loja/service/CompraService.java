package br.com.alura.microservice.loja.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.alura.microservice.loja.feign.FornecedorClient;
import br.com.alura.microservice.loja.request.CompraRequest;
import br.com.alura.microservice.loja.response.InfoFornecedorResponse;

@Service
public class CompraService {

	@Autowired
	private  FornecedorClient fornecedorClient;
	
	public void realizaCompra(CompraRequest request) {

		InfoFornecedorResponse info = fornecedorClient.getInfoPorEstado(request.getEndereco().getEstado());
		
		System.out.println(info.getEndereco());

	}

}
