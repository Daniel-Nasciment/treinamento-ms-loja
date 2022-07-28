package br.com.alura.microservice.loja;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.client.RestTemplate;

import feign.RequestInterceptor;
import feign.RequestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableHystrix
@EnableResourceServer
public class LojaFloriculturaApplication {

	// ATRAVEZ DO SPRING É INJETADO INSTANCIAS DE RequestInterceptor DENTRO DO FEIGN CLIENT
	// DESSA FORMA FORNECEMOS UMA INSTANCIA PARA O SPRING QUE SERA INJETADA NO FEIGN
	
	@Bean
	public RequestInterceptor getInterceptorToken() {
		return new RequestInterceptor() {
			
			// DA FORMA QUE ESTÁ O VALOR DE authentication ESTÁ VINDO NULL POR CONTA DO PROCESSAMENTO ESTAR OCORRENDO EM THREADS DIFERENTES
			// POIS O PROCESSAMENTO DO MÉTODO REALIZA COMPRA ESTA ASSOCIADO AO POOL DE THREADS DO HYSTRIX
			// É NECESSARIO CONFIGURAR ISSO NO APP PROPERTIES
			
			@Override
			public void apply(RequestTemplate template) {
				Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
				if(authentication == null) {
					return;
				}
				
				OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();
				
				String tokenValue = details.getTokenValue();
			
				template.header("Authorization", "Bearer" + tokenValue);
			}
		};
	}
	
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
