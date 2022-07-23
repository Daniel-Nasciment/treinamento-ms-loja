package br.com.alura.microservice.loja.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.alura.microservice.loja.model.Compra;
import br.com.alura.microservice.loja.request.CompraRequest;
import br.com.alura.microservice.loja.service.CompraService;

@RestController
@RequestMapping(value = "/compra")
public class CompraController {

	private static final Logger LOG = LoggerFactory.getLogger(CompraController.class);
	
	@Autowired
	private CompraService compraService;
	
	@PostMapping
	public ResponseEntity<?> postMethodName(@RequestBody CompraRequest request) {
		
		LOG.info("Entrando no endpoint /compra");
		
		Compra compra = compraService.realizaCompra(request);
		
		LOG.info("Saindo do endpoint /compra");
		return ResponseEntity.ok(compra);
	}

	
}
