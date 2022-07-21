package br.com.alura.microservice.loja.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.alura.microservice.loja.request.CompraRequest;

@RestController
@RequestMapping(value = "/compra")
public class CompraController {

	
	@PostMapping
	public ResponseEntity<?> postMethodName(@RequestBody CompraRequest request) {
		
		
		return ResponseEntity.ok().build();
	}

	
}
