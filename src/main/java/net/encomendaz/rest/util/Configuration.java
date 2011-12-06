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
package net.encomendaz.rest.util;

import java.util.ResourceBundle;

public class Configuration {

	private static ResourceBundle resourceBundle() {
		return ResourceBundle.getBundle("META-INF/configuration");
	}

	public static String awsAccessKey() {
		return resourceBundle().getString("aws.access.key");
	}

	public static String awsSecretKey() {
		return resourceBundle().getString("aws.secret.key");
	}

	public static Integer intervaloMonitoramento() {
		String value = resourceBundle().getString("monitoramento.intervalo");
		return Integer.parseInt(value) * 1000 * 60;
	}
}
