package net.encomendaz.rest.monitoramento;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import net.encomendaz.rest.rastreamento.RastreamentoManager;
import net.encomendaz.rest.util.Mailer;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceAsync;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;

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
				notificar(atualizado);
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

	private void notificar(Monitoramento monitoramento) {
		Destination to = new Destination().withToAddresses(monitoramento.getEmail());

		Content subject = new Content();
		subject.setCharset("UTF-8");
		subject.setData("EncomendaZ: status atualizado");

		Content content = new Content();
		content.setCharset("UTF-8");
		content.setData(String
				.format("O status do item %s mudou!\n\nClique aqui e confira: http://rest.encomendaz.net/rastreamento.json?id=%s",
						monitoramento.getId(), monitoramento.getId()));

		Body body = new Body();
		body.setText(content);

		Message message = new Message(subject, body);

		SendEmailRequest request = new SendEmailRequest("no-reply@rasea.org", to, message);

		AmazonSimpleEmailServiceAsync client = Mailer.client();
		client.sendEmailAsync(request);

		System.out.println("notificando " + monitoramento.getEmail() + ", " + monitoramento.toString());
	}
}
