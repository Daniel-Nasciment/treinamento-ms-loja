package br.com.alura.microservice.loja.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class CompraRequest {

	// CASO ESSA INFORMACAO VENHA NO REQUEST, VAI SER IGNORADA
	// ISSO PARA SER CRIADO A LOGICA DE SEMPRE SALVAR A COMPRA A CADA PROCESSO
	@JsonIgnore
	private Long idCompra;

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

	public Long getIdCompra() {
		return idCompra;
	}

	public void setIdCompra(Long idCompra) {
		this.idCompra = idCompra;
	}

}
