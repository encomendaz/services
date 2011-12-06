package net.encomendaz.rest.monitoramento;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import net.encomendaz.rest.rastreamento.RastreamentoManager;

public class MonitoramentoTask extends TimerTask {

	private MonitoramentoManager monitoramentoManager;

	private RastreamentoManager rastreamentoManager;

	public MonitoramentoTask() {
		monitoramentoManager = new MonitoramentoManager();
		rastreamentoManager = new RastreamentoManager();
	}

	@Override
	public void run() {
		System.out.println("verificando... " + new Date().toString());

		List<String> emails = monitoramentoManager.obter();
		List<Monitoramento> monitoramentos;
		List<Monitoramento> atualizados;

		for (String email : emails) {
			atualizados = new ArrayList<Monitoramento>();
			monitoramentos = monitoramentoManager.obter(email);

			for (Monitoramento monitoramento : monitoramentos) {
				if (atualizou(monitoramento)) {
					atualizados.add(monitoramento);
				}
			}

			for (Monitoramento atualizado : atualizados) {
				notificar(email, atualizado);
			}
		}
	}

	private boolean atualizou(Monitoramento monitoramento) {
		String md5 = rastreamentoManager.hash(monitoramento.getId());
		boolean result = !md5.equals(monitoramento.getHash());

		monitoramento.setHash(md5);
		monitoramento.setUpdated(new Date());

		return result;
	}

	private void notificar(String email, Monitoramento atualizado) {
		System.out.println("notificando " + email + ", " + atualizado.toString());
	}
}
