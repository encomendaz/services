package net.encomendaz.rest.conf;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import net.encomendaz.rest.config.Configuration;

import org.junit.Test;

public class ConfigurationTest {

	private static final String EMPTY_AWS_KEY_MESSAGE = "Defina as variáveis de ambiente AWS_ACCESS_KEY e AWS_SECRET_KEY no Sistema Operacional";

	private Configuration configuration = Configuration.getInstance();

	@Test
	public void notEmptyAwsAccessKey() {
		assertNotNull(EMPTY_AWS_KEY_MESSAGE, configuration.getAwsAccessKey());
		assertFalse(EMPTY_AWS_KEY_MESSAGE, configuration.getAwsAccessKey().trim().equals(""));
	}

	@Test
	public void notEmptyAwsSecretKey() {
		assertNotNull(EMPTY_AWS_KEY_MESSAGE, configuration.getAwsSecretKey());
		assertFalse(EMPTY_AWS_KEY_MESSAGE, configuration.getAwsSecretKey().trim().equals(""));
	}
}
