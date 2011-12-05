package net.encomendaz.rest.monitoramento;

import java.util.Date;
import java.util.TimerTask;

public class MonitoramentoTask extends TimerTask {

	@Override
	public void run() {
		System.out.println("rodando... " + new Date().toString());
	}
}
