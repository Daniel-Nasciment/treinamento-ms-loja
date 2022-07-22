package br.com.alura.microservice.loja.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.com.alura.microservice.loja.request.CompraRequest;
import br.com.alura.microservice.loja.response.InfoFornecedorResponse;

@Service
public class CompraService {

	@Autowired
	private RestTemplate client;
	
	@Autowired // PEGAR AS INFORMACOES QUE VIERAM DO EUREKA
	private DiscoveryClient eurekaClient;

	
	
	public void realizaCompra(CompraRequest request) {

		// URL
		// Ribbon QUE DECIDE QUAL INSTANCIA DE FORNECEDOR ELE VAI ACIONAR
		// ELE VAI FAZER O BALANCEAMENTO DE CARGA
		String urlInfo = "http://fornecedor/info/" + request.getEndereco().getEstado();

		// DESSA FORMA VAMOS NOS COMUNICAR ATRAVES DE UM REQUEST HTTP SINCRONA
		// (SÓ PODEMOS MANDAR FAZER UMA REQUEST QUANDO NÃO TIVER NENHUMA OUTRA SENDO
		// PROCESSADA)
		ResponseEntity<InfoFornecedorResponse> exchange = client.exchange(urlInfo,
				// MÉTODO
				HttpMethod.GET,
				// O QUE VAMOS ENVIAR
				null,
				// O QUE VAMOS RECEBER
				InfoFornecedorResponse.class);
		
		// PRECISA SER DA FORMA QUE É RETORNADO PELO EUREKA "FORNECEDOR" EM MAIUSCULO
		eurekaClient.getInstances("FORNECEDOR").stream()
		.forEach(f -> {
			System.out.println("localhost:"+f.getPort());
		});
		
		// https://cloud.spring.io/spring-cloud-static/spring-cloud-netflix/2.2.0.M3/reference/html/#customizing-the-default-for-all-ribbon-clients
		// LINK PARA CUSTOMIZAÇÃO DA ROTACAO DO LOAD BALANCER

		System.out.println(exchange.getBody().getEndereco());

	}

}