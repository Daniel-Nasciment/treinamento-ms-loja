package br.com.alura.microservice.loja;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableHystrix
@EnableResourceServer
public class LojaFloriculturaApplication {

	@Bean
	@LoadBalanced // ESSE CARA VAI DAR A INTELIGENCIA AO REST TEMPLATE PARA RESOLVER A URL EM COMPRASERVICE
	// LOAD BALANCE DISTRIBUI A CARGA, OU SEJA, A CADA REUISICAO ELE VAI PROCURAR POR UM IP E PORTA DIFERENTE
	public RestTemplate getRestTemplte() {
		return new RestTemplate();
	}
	
	public static void main(String[] args) {
		SpringApplication.run(LojaFloriculturaApplication.class, args);
	}

}
