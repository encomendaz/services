package net.encomendaz.services.tracking;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;
import net.encomendaz.services.tracking.CorreiosTrackingData;

import org.alfredlibrary.utilitarios.correios.RegistroRastreamento;
import org.junit.Assert;
import org.junit.Test;

public class CorreiosTrackingDataTest {

	@Test
	public void y() {
		String text;
		String result;

		text = "ACF IMBUI                                - SALVADOR                      /BA";
		result = text.replaceAll(" +", " ").trim();

		Assert.assertEquals(result, "ACF IMBUI - SALVADOR /BA");

	}

	private CorreiosTrackingData getMockForLocalAttr(String local) {
		RegistroRastreamento reg = createMock(RegistroRastreamento.class);
		expect(reg.getLocal()).andReturn(local).anyTimes();
		expect(reg.getDetalhe()).andReturn("").anyTimes();
		expect(reg.getAcao()).andReturn("").anyTimes();
		replay(reg);
		return new CorreiosTrackingData(reg);
	}

	@Test
	public void x() {
		// SI154843991BR
		// ACF IMBUI - SALVADOR /BA
		// CTE BENFICA - RIO DE JANEIRO/RJ
		// CEE LARANJEIRAS - RIO DE JANEIRO/RJ

		CorreiosTrackingData trace;
		String local;

		local = "ACF IMBUI                                - SALVADOR                      /BA";
		//local = "ACF IMBUI                                - SALVADOR                      /BA";
		trace = getMockForLocalAttr(local);
		assertEquals("Salvador", trace.getCity());
		assertEquals("BA", trace.getState());

		// CP804408596US
		// AVALIACAO - EXERCITO
		// CTCI RIO DE JANEIRO/GEARA - RIO DE JANEIRO/RJ

		local = "AVALIACAO - EXERCITO";
		trace = getMockForLocalAttr(local);
		assertEquals(null, trace.getCity());
		assertEquals(null, trace.getState());

		local = "CTCI RIO DE JANEIRO/GEARA - RIO DE JANEIRO/RJ";
		trace = getMockForLocalAttr(local);
		assertEquals("Rio de Janeiro", trace.getCity());
		assertEquals("RJ", trace.getState());

		// SI154847154BR
		// CTCE ARACAJU - ARACAJU/SE
		// CDD ARACAJU CENTRO - ARACAJU/SE

		local = "CTCE ARACAJU - ARACAJU/SE";
		trace = getMockForLocalAttr(local);
		assertEquals("Aracaju", trace.getCity());
		assertEquals("SE", trace.getState());

		local = "CDD ARACAJU CENTRO - ARACAJU/SE";
		trace = getMockForLocalAttr(local);
		assertEquals("Aracaju", trace.getCity());
		assertEquals("SE", trace.getState());

		// SI154846091BR
		// CTE SALVADOR - SALVADOR/BA
		// CDD PRACA MAUA - RIO DE JANEIRO/RJ

		local = "CTE SALVADOR - SALVADOR/BA";
		trace = getMockForLocalAttr(local);
		assertEquals("Salvador", trace.getCity());
		assertEquals("BA", trace.getState());

		local = "CDD PRACA MAUA - RIO DE JANEIRO/RJ";
		trace = getMockForLocalAttr(local);
		assertEquals("Rio de Janeiro", trace.getCity());
		assertEquals("RJ", trace.getState());

		// PB882615209BR
		// AC GARIBALDI - GARIBALDI/RS
		// UD GARIBALDI - GARIBALDI/RS
		// CTE PORTO ALEGRE - PORTO ALEGRE/RS

		local = "AC GARIBALDI - GARIBALDI/RS";
		trace = getMockForLocalAttr(local);
		assertEquals("Garibaldi", trace.getCity());
		assertEquals("RS", trace.getState());

		local = "UD GARIBALDI - GARIBALDI/RS";
		trace = getMockForLocalAttr(local);
		assertEquals("Garibaldi", trace.getCity());
		assertEquals("RS", trace.getState());

		local = "CTE PORTO ALEGRE - PORTO ALEGRE/RS";
		trace = getMockForLocalAttr(local);
		assertEquals("Porto Alegre", trace.getCity());
		assertEquals("RS", trace.getState());

		// SI154847168BR
		// CDD CAPUCHINHOS/BA
		// CEE FEIRA DE SANTANA - FEIRA DE SANTANA/BA

		/*
		 */
		local = "CDD CAPUCHINHOS/BA";
		trace = getMockForLocalAttr(local);
		assertEquals("Capuchinhos", trace.getCity());
		assertEquals("BA", trace.getState());

		local = "CEE FEIRA DE SANTANA - FEIRA DE SANTANA/BA";
		trace = getMockForLocalAttr(local);
		assertEquals("Feira de Santana", trace.getCity());
		assertEquals("BA", trace.getState());
	}
}
