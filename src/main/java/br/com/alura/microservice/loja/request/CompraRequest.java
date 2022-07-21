package br.com.alura.microservice.loja.request;

import java.util.List;

public class CompraRequest {

	private List<ItemCompraRequest> itens;

	private EnderecoRequest endereco;

	public List<ItemCompraRequest> getItens() {
		return itens;
	}

	public void setItens(List<ItemCompraRequest> itens) {
		this.itens = itens;
	}

	public EnderecoRequest getEndereco() {
		return endereco;
	}

	public void setEndereco(EnderecoRequest endereco) {
		this.endereco = endereco;
	}

}
