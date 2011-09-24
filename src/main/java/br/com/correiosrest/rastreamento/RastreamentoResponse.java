package br.com.correiosrest.rastreamento;

import static javax.xml.bind.annotation.XmlAccessType.FIELD;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(FIELD)
@XmlRootElement(name = "rastreamento")
public class RastreamentoResponse {

	@XmlElement(name = "registro")
	@XmlElementWrapper(name = "registros")
	private List<OcorrenciaResponse> registros;

	public List<OcorrenciaResponse> getRegistros() {
		return registros;
	}

	public void setRegistros(List<OcorrenciaResponse> registros) {
		this.registros = registros;
	}

}
