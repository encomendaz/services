package net.encomendaz.services.tracking;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;

import org.alfredlibrary.utilitarios.correios.RegistroRastreamento;
import org.junit.Test;

public class CorreiosTraceTest {

	private Trace getInstance(String local) {
		RegistroRastreamento reg = createMock(RegistroRastreamento.class);
		expect(reg.getLocal()).andReturn(local).anyTimes();
		expect(reg.getDetalhe()).andReturn("").anyTimes();
		expect(reg.getAcao()).andReturn("").anyTimes();
		replay(reg);
		return new CorreiosTrace(reg);
	}

	@Test
	public void getCityAndState() {
		String local;
		Trace trace;

		// SI154843991BR

		local = "ACF IMBUI                                - SALVADOR                      /BA";
		trace = getInstance(local);
		assertEquals("Salvador", trace.getCity());
		assertEquals("BA", trace.getState());

		// CP804408596US

		local = "AVALIACAO - EXERCITO";
		trace = getInstance(local);
		assertEquals(null, trace.getCity());
		assertEquals(null, trace.getState());

		local = "CTCI RIO DE JANEIRO/GEARA - RIO DE JANEIRO/RJ";
		trace = getInstance(local);
		assertEquals("Rio de Janeiro", trace.getCity());
		assertEquals("RJ", trace.getState());

		// SI154847154BR

		local = "CTCE ARACAJU - ARACAJU/SE";
		trace = getInstance(local);
		assertEquals("Aracaju", trace.getCity());
		assertEquals("SE", trace.getState());

		local = "CDD ARACAJU CENTRO - ARACAJU/SE";
		trace = getInstance(local);
		assertEquals("Aracaju", trace.getCity());
		assertEquals("SE", trace.getState());

		// SI154846091BR

		local = "CTE SALVADOR - SALVADOR/BA";
		trace = getInstance(local);
		assertEquals("Salvador", trace.getCity());
		assertEquals("BA", trace.getState());

		local = "CDD PRACA MAUA - RIO DE JANEIRO/RJ";
		trace = getInstance(local);
		assertEquals("Rio de Janeiro", trace.getCity());
		assertEquals("RJ", trace.getState());

		// PB882615209BR

		local = "AC GARIBALDI - GARIBALDI/RS";
		trace = getInstance(local);
		assertEquals("Garibaldi", trace.getCity());
		assertEquals("RS", trace.getState());

		local = "UD GARIBALDI - GARIBALDI/RS";
		trace = getInstance(local);
		assertEquals("Garibaldi", trace.getCity());
		assertEquals("RS", trace.getState());

		local = "CTE PORTO ALEGRE - PORTO ALEGRE/RS";
		trace = getInstance(local);
		assertEquals("Porto Alegre", trace.getCity());
		assertEquals("RS", trace.getState());

		// SI154847168BR

		local = "CDD CAPUCHINHOS/BA";
		trace = getInstance(local);
		assertEquals("Capuchinhos", trace.getCity());
		assertEquals("BA", trace.getState());

		local = "CEE FEIRA DE SANTANA - FEIRA DE SANTANA/BA";
		trace = getInstance(local);
		assertEquals("Feira de Santana", trace.getCity());
		assertEquals("BA", trace.getState());
	}
}
