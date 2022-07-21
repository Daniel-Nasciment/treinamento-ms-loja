package br.com.alura.microservice.loja.service;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.com.alura.microservice.loja.request.CompraRequest;
import br.com.alura.microservice.loja.response.InfoFornecedorResponse;

@Service
public class CompraService {

	
	
	
	public void realizaCompra(CompraRequest request) {

		// NOS PERMITE FAZER REQUISIÇÕES
		RestTemplate client = new RestTemplate();
		
		// URL
		String urlInfo = "http://localhost:8081/info/" + request.getEndereco().getEstado();

		// DESSA FORMA VAMOS NOS COMUNICAR ATRAVES DE UM REQUEST HTTP SINCRONA 
		// (SÓ PODEMOS MANDAR FAZER UMA REQUEST QUANDO NÃO TIVER NENHUMA OUTRA SENDO PROCESSADA)
		ResponseEntity<InfoFornecedorResponse> exchange = client.exchange(urlInfo, 
				// MÉTODO
				HttpMethod.GET, 
				// O QUE VAMOS ENVIAR
				null,
				// O QUE VAMOS RECEBER
				InfoFornecedorResponse.class);
		
		System.out.println(exchange.getBody().getEndereco());
		
	}

}
