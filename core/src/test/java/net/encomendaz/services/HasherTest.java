package net.encomendaz.services;

import net.encomendaz.services.util.Hasher;

import org.junit.Test;

public class HasherTest {

	@Test
	public void md5() {
		String hash1;

		hash1 = Hasher.md5("91448300404063076307502904506675" + "DF061940228BR");

		System.out.println(hash1);
	}
}
