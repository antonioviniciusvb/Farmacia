package com.generation.farmacia.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "tb_produto")
public class Produto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "O atributo [nome] é obrigatório!")
	@Size(min = 3, max = 200, message = "O [nome] deve conter no mínimo 3 e no máximo 200 caracteres.")
	@Column(length = 200, nullable = false)
	private String nome;

	@Size(min = 10, max = 1000, message = "A [descricao] deve conter no mínimo 10 e no máximo 1000 caracteres.")
	@Column(length = 1000, nullable = true)
	private String descricao;

	@NotNull(message = "O [preco] não pode ser vazio.")
	@Positive(message = "O [preco] deve ser maior que 0")
	@Digits(integer = 10, fraction = 2)
	private BigDecimal preco;

	@NotBlank(message = "A [marca] é obrigatória!")
	@Size(min = 2, max = 200, message = "A [marca] deve conter no mínimo 2 e no máximo 200 caracteres.")
	@Column(length = 200)
	private String marca;

	@NotBlank(message = "A [fabricante] é obrigatória!")
	@Size(min = 2, max = 200, message = "A [fabricante] deve conter no mínimo 10 e no máximo 200 caracteres.")
	private String fabricante;

	@Future(message = "Não é possivel adicionar datas do passado ou presente para [data_vencimento]")
	@NotNull(message = "A [data_vencimento] é obrigatória!")
	@Column(name = "data_vencimento")
	private LocalDate dataVencimento;

	@Size(min = 10, max = 500, message = "A [foto] deve conter no mínimo 10 e no máximo 500 caracteres.")
	@Column(length = 500, nullable = true)
	private String foto;

	@Min(value = 1, message = "A [quantidade] deve ser maior que 0")
	@NotNull(message = "A [quantidade] é obrigatória!")
	private Integer quantidade;

	@NotNull(message = "A [retencao_receita] é obrigatória!")
	private Boolean retencao_receita;

	@ManyToOne
	@JsonIgnoreProperties("produto")
	private Categoria categoria;

	@ManyToOne
	@JsonIgnoreProperties("produto")
	private Usuario usuario;

	public Produto(Long id, String nome, String descricao, BigDecimal preco, String marca, String fabricante,
			LocalDate dataVencimento, String foto, Integer quantidade, Boolean retencao_receita) {
		this.id = id;
		this.nome = nome;
		this.descricao = descricao;
		this.preco = preco;
		this.marca = marca;
		this.fabricante = fabricante;
		this.dataVencimento = dataVencimento;
		this.foto = foto;
		this.quantidade = quantidade;
		this.retencao_receita = retencao_receita;
	}

	public Produto() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public BigDecimal getPreco() {
		return preco;
	}

	public void setPreco(BigDecimal preco) {
		this.preco = preco;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public String getFabricante() {
		return fabricante;
	}

	public void setFabricante(String fabricante) {
		this.fabricante = fabricante;
	}

	public LocalDate getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(LocalDate dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public Boolean getRetencao_receita() {
		return retencao_receita;
	}

	public void setRetencao_receita(Boolean retencao_receita) {
		this.retencao_receita = retencao_receita;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}
