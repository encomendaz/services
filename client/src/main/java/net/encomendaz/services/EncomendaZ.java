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
package net.encomendaz.services;

import java.util.ResourceBundle;

import net.encomendaz.services.tracking.TrackingService;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

public class EncomendaZ {

	/**
	 * Serviço de rastreamento de encomendas.
	 */
	public static TrackingService tracking = initTrackingService();

	private static ResourceBundle bundle;

	private static boolean initialized = false;

	private EncomendaZ() {
	}

	private static void initProviders() {
		if (!initialized) {
			ResteasyProviderFactory.setRegisterBuiltinByDefault(false);
			ResteasyProviderFactory.getInstance().registerProvider(JacksonJsonProvider.class);

			initialized = true;
		}
	}

	private static TrackingService initTrackingService() {
		initProviders();
		return ProxyFactory.create(TrackingService.class, getBaseURL());
	}

	private static ResourceBundle getBundle() {
		if (bundle == null) {
			bundle = ResourceBundle.getBundle("encomendaz");
		}

		return bundle;
	}

	private static String getBaseURL() {
		return getBundle().getString("base-url");
	}
}
