package br.com.alura.microservice.loja.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import br.com.alura.microservice.loja.response.InfoFornecedorResponse;

@FeignClient("FORNECEDOR") // FEIGN JA E INTEGRADO COM RIBBON, COM EUREKA, COM LOAD BALANCE
public interface FornecedorClient {
	
	@GetMapping(value = "/info/{estado}")
	public InfoFornecedorResponse getInfoPorEstado(@PathVariable String estado); 

}
