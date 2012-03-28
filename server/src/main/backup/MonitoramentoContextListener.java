package net.encomendaz.services.monitoramento;

import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import net.encomendaz.services.util.Configuration;

public final class MonitoramentoContextListener implements ServletContextListener {

	private Timer timer;

	public void contextInitialized(ServletContextEvent event) {
		TimerTask task = new MonitoramentoTask();
		Integer period = Configuration.intervaloMonitoramento();

		timer = new Timer(true);
		timer.schedule(task, period, period);
	}

	public void contextDestroyed(ServletContextEvent event) {
		timer.cancel();
	}
}
