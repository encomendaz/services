package net.encomendaz.rest.rastreamento;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.alfredlibrary.utilitarios.correios.RegistroRastreamento;

public class RastreamentoManager {

	private void validarParametrosPesquisar(String id, String ordem) {
		if (id == null || id.isEmpty()) {
			throw new IllegalArgumentException("É necessário informar a identificação do objeto via parâmetro \"id\"");
		}

		if (ordem != null && !("asc".equals(ordem) || "desc".equals(ordem))) {
			throw new IllegalArgumentException("O parâmetro \"ordem\" só aceita os valores \"asc\" ou \"desc\"");
		}
	}

	public List<Rastreamento> pesquisar(String id, Integer inicio, Integer fim, String ordem) {

		validarParametrosPesquisar(id, ordem);

		List<Rastreamento> response = new ArrayList<Rastreamento>();
		List<RegistroRastreamento> registros = org.alfredlibrary.utilitarios.correios.Rastreamento.rastrear(id);
		Collections.reverse(registros);

		int _ini = (inicio == null || inicio < 1 ? 1 : inicio);
		int _fim = (fim == null || fim > registros.size() ? registros.size() : fim);

		for (int i = _ini; i <= _fim; i++) {
			response.add(Rastreamento.parse(registros.get(i - 1)));
		}

		if ("desc".equals(ordem)) {
			Collections.reverse(response);
		}

		return response;
	}
}
