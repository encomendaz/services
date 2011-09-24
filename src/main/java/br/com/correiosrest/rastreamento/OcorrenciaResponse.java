package br.com.correiosrest.rastreamento;

import static javax.xml.bind.annotation.XmlAccessType.FIELD;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(FIELD)
@XmlRootElement(name = "registro")
@XmlType(propOrder = { "data", "local", "situacao", "descricao" })
public class OcorrenciaResponse {

	@XmlElement(required = true)
	private Date data;

	@XmlElement
	private String local;

	@XmlElement
	private String situacao;

	@XmlElement
	private String descricao;

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}
}
