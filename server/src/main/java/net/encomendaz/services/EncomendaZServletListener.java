package net.encomendaz.services;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import net.encomendaz.services.monitoring.MonitoringException;
import net.encomendaz.services.monitoring.MonitoringManager;

import com.google.apphosting.api.ApiProxy.OverQuotaException;

public class EncomendaZServletListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent event) {
		try {
			MonitoringManager.findAll();

		} catch (MonitoringException cause) {
			throw new RuntimeException(cause);

		} catch (OverQuotaException cause) {
			cause.printStackTrace();
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
	}
}
