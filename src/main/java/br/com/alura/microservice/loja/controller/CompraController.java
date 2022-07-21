package br.com.alura.microservice.loja.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.alura.microservice.loja.request.CompraRequest;
import br.com.alura.microservice.loja.service.CompraService;

@RestController
@RequestMapping(value = "/compra")
public class CompraController {

	
	@Autowired
	private CompraService compraService;
	
	@PostMapping
	public ResponseEntity<?> postMethodName(@RequestBody CompraRequest request) {
		
		compraService.realizaCompra(request);
		
		return ResponseEntity.ok().build();
	}

	
}
