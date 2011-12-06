/*
 * Rasea Server
 * 
 * Copyright (c) 2008, Rasea <http://rasea.org>. All rights reserved.
 *
 * Rasea Server is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License.
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

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hasher {

	private static Hasher instance;

	private Hasher() {
	}

	public static synchronized Hasher getInstance() {
		if (instance == null) {
			instance = new Hasher();
		}

		return instance;
	}

	public String md5(final String message) {
		String hash = null;

		if (message != null) {
			try {
				MessageDigest md = MessageDigest.getInstance("MD5");
				hash = hex(md.digest(message.getBytes("UTF-8")));

			} catch (NoSuchAlgorithmException cause) {
				cause.printStackTrace();

			} catch (UnsupportedEncodingException cause) {
				cause.printStackTrace();
			}
		}

		return hash;
	}

	private String hex(final byte[] array) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < array.length; ++i)
			sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
		return sb.toString();
	}
}
