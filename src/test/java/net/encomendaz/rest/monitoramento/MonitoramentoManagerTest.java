package net.encomendaz.rest.monitoramento;

import org.junit.Test;

public class MonitoramentoManagerTest {

	private MonitoramentoManager manager = new MonitoramentoManager();

	@Test
	public void cadastrarComSucesso() {
		manager.cadastrar("xxxx", "cleverson.sacramento@gmail.com");
	}
}
