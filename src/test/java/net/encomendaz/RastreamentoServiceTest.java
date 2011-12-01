package net.encomendaz;

import net.encomendaz.rest.rastreamento.RastreamentoService;

import org.alfredlibrary.AlfredException;
import org.junit.Test;

public class RastreamentoServiceTest {

	private RastreamentoService service = new RastreamentoService();

	@Test(expected = AlfredException.class)
	public void falhaAoPesquisarObjetoInvalido() {
		service.pesquisar("XX012345678XX", null, null, null);
	}
	
	@Test
	public void pesquisarObjetoEntregue() {
		service.pesquisar("PB755604756BR", null, null, null);
	}

}
