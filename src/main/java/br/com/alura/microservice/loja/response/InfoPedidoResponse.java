package br.com.alura.microservice.loja.response;

public class InfoPedidoResponse {

	private Long id;

	private Integer tempoDePreparo;

	private String endereco;

	public InfoPedidoResponse(Long idPedido, Integer tempoDePreparo, String endereco) {
		this.id = idPedido;
		this.tempoDePreparo = tempoDePreparo;
		this.endereco = endereco;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long idPedido) {
		this.id = idPedido;
	}

	public Integer getTempoDePreparo() {
		return tempoDePreparo;
	}

	public void setTempoDePreparo(Integer tempoDePreparo) {
		this.tempoDePreparo = tempoDePreparo;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

}
