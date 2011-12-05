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
package net.encomendaz.rest.config;

import java.util.ResourceBundle;

public class Configuration {

	private String awsAccessKey;

	private String awsSecretKey;

	private ResourceBundle resourceBundle;

	private static Configuration instance;

	private Configuration() {
	}

	public static Configuration getInstance() {
		if (instance == null) {
			instance = new Configuration();
		}

		return instance;
	}

	public String getAwsAccessKey() {
		if (awsAccessKey == null) {
			awsAccessKey = getResourceBundle().getString("aws.access.key");
		}

		return awsAccessKey;
	}

	public String getAwsSecretKey() {
		if (awsSecretKey == null) {
			awsSecretKey = getResourceBundle().getString("aws.secret.key");
		}

		return awsSecretKey;
	}

	private ResourceBundle getResourceBundle() {
		if (resourceBundle == null) {
			resourceBundle = ResourceBundle.getBundle("META-INF/configuration");
		}

		return resourceBundle;
	}
}
