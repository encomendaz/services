package net.encomendaz.rest.monitoramento;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import net.encomendaz.rest.rastreamento.RastreamentoManager;

public class MonitoramentoTask extends TimerTask {

	@Override
	public void run() {
		List<String> emails = MonitoramentoManager.getInstance().obter();
		List<Monitoramento> monitoramentos;
		List<Monitoramento> atualizados;

		for (String email : emails) {
			atualizados = new ArrayList<Monitoramento>();
			monitoramentos = MonitoramentoManager.getInstance().obter(email);

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
		String hash = RastreamentoManager.getInstance().hash(monitoramento.getId());
		boolean result = !hash.equals(monitoramento.getHash());

		System.out.print(monitoramento.toString() + " ");
		
		if (result) {
			monitoramento.setHash(hash);
			monitoramento.setUpdated(new Date());
			
			System.out.println("mudou");
		} else {
			System.out.println("não mudou");
		}

		return result;
	}

	private void notificar(String email, Monitoramento atualizado) {
		System.out.println("notificando " + email + ", " + atualizado.toString());
	}
}
