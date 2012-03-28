package net.encomendaz.services;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import net.encomendaz.services.monitoramento.Monitoramento;
import net.encomendaz.services.util.Configuration;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

public class ConfigurationTest {

	private static final String EMPTY_AWS_KEY_MESSAGE = "Defina as variáveis de ambiente AWS_ACCESS_KEY e AWS_SECRET_KEY no Sistema Operacional";

	@Test
	public void notEmptyAwsAccessKey() {
		assertNotNull(EMPTY_AWS_KEY_MESSAGE, Configuration.awsAccessKey());
		assertFalse(EMPTY_AWS_KEY_MESSAGE, Configuration.awsAccessKey().trim().equals(""));
	}

	@Test
	public void notEmptyAwsSecretKey() {
		assertNotNull(EMPTY_AWS_KEY_MESSAGE, Configuration.awsSecretKey());
		assertFalse(EMPTY_AWS_KEY_MESSAGE, Configuration.awsSecretKey().trim().equals(""));
	}

	@Test
	public void x() throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();

		Monitoramento monitoramento = new Monitoramento();
		monitoramento.setId("asdasdas");

		String x = mapper.writeValueAsString(monitoramento);

		System.out.println(x);
	}
}
