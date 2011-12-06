package net.encomendaz.rest;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import net.encomendaz.rest.monitoramento.Monitoramento;
import net.encomendaz.rest.util.Configuration;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
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
	
	@Test
	public void x() throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		
		Monitoramento  monitoramento= new Monitoramento();
		monitoramento.setId("asdasdas");
		
		String x = mapper.writeValueAsString(monitoramento);
		
		System.out.println(x);
	}
}
