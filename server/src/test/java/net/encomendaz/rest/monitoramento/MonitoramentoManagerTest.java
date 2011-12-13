/*
 * EncomendaZ
 * 
 * Copyright (c) 2011, EncomendaZ <http://encomendaz.net>.
 * All rights reserved.
 * 
 * EncomendaZ is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation; either version 3 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, see <http://gnu.org/licenses>
 * or write to the Free Software Foundation, Inc., 51 Franklin Street,
 * Fifth Floor, Boston, MA  02110-1301, USA.
 */
package net.encomendaz.rest.monitoramento;

import static net.encomendaz.rest.monitoramento.MonitoramentoManager.cadastrar;
import static net.encomendaz.rest.monitoramento.MonitoramentoManager.existe;
import static net.encomendaz.rest.monitoramento.MonitoramentoManager.remover;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import net.encomendaz.rest.AlreadyExistsException;
import net.encomendaz.rest.DoesNotExistException;

import org.junit.Before;
import org.junit.Test;

public class MonitoramentoManagerTest {

	private static final String EMAIL_TEST = "fake@fake.com";

	private static final Monitoramento MONITORAMENTO_TEST = new Monitoramento(EMAIL_TEST, "PB848981335BR");

	@Before
	public void before() {
		try {
			remover(EMAIL_TEST);
		} catch (DoesNotExistException e) {
		}
	}

	@Test
	public void cadastrarComSucesso() throws AlreadyExistsException {
		cadastrar(MONITORAMENTO_TEST);

		assertTrue(existe(MONITORAMENTO_TEST));
	}

	@Test
	public void removerComSucesso() throws AlreadyExistsException, DoesNotExistException {
		cadastrar(MONITORAMENTO_TEST);
		remover(MONITORAMENTO_TEST);

		assertFalse(existe(MONITORAMENTO_TEST));
	}

	@Test
	public void falhaAoCadastrarDuplicado() throws AlreadyExistsException {
		cadastrar(MONITORAMENTO_TEST);

		try {
			cadastrar(MONITORAMENTO_TEST);
			fail();

		} catch (AlreadyExistsException cause) {
			assertTrue(existe(MONITORAMENTO_TEST));
		}
	}
}
