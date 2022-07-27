package br.com.alura.microservice.loja.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.alura.microservice.loja.request.InfoEntregaRequest;
import br.com.alura.microservice.loja.response.VoucherResponse;

@FeignClient("TRANSPORTADOR") 
public interface TransportadorClient {
	
	@PostMapping(value = "/entrega")
	public VoucherResponse reservarEntrega(@RequestBody InfoEntregaRequest request);

}
