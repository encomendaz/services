package net.encomendaz.rest.monitoramento;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

public class MonitoramentoTask extends TimerTask {

	private MonitoramentoManager manager;

	public MonitoramentoTask() {
		manager = new MonitoramentoManager();
	}

	@Override
	public void run() {
		System.out.println("verificando... " + new Date().toString());

		List<String> emails = manager.obter();
		List<Monitoramento> monitoramentos;
		List<Monitoramento> atualizados;

		for (String email : emails) {
			atualizados = new ArrayList<Monitoramento>();
			monitoramentos = manager.obter(email);

			for (Monitoramento monitoramento : monitoramentos) {
				if (atualizado(monitoramento)) {
					atualizados.add(monitoramento);
				}
			}

			for (Monitoramento atualizado : atualizados) {
				notificar(email, atualizado);
			}
		}
	}

	private boolean atualizado(Monitoramento monitoramento) {
		System.out.println(monitoramento.toString());
		return true;
	}

	private void notificar(String email, Monitoramento atualizado) {
		System.out.println("notificando " + email + ", " + atualizado.toString());
	}
}
