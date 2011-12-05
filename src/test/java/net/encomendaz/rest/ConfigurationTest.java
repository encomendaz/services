package net.encomendaz.rest;

import net.encomendaz.rest.config.Configuration;

import org.junit.Test;

public class ConfigurationTest {

	private Configuration configuration = Configuration.getInstance();

	@Test
	public void test() {
		System.out.println(configuration.getAwsAccessKey());
		System.out.println(configuration.getAwsSecretKey());
	}
}
