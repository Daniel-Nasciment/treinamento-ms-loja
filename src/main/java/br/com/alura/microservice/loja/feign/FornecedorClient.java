package br.com.alura.microservice.loja.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.alura.microservice.loja.request.ItemCompraRequest;
import br.com.alura.microservice.loja.response.InfoFornecedorResponse;
import br.com.alura.microservice.loja.response.InfoPedidoResponse;

@FeignClient("FORNECEDOR") // FEIGN JA E INTEGRADO COM RIBBON, COM EUREKA, COM LOAD BALANCE
public interface FornecedorClient {
	
	@GetMapping(value = "/info/{estado}")
	public InfoFornecedorResponse getInfoPorEstado(@PathVariable String estado); 
	
	@PostMapping(value = "/pedido")
	public InfoPedidoResponse realizarPedido(@RequestBody List<ItemCompraRequest> produtos);

}
