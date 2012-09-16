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
package net.encomendaz.services.monitoring;

import static net.encomendaz.services.Response.MEDIA_TYPE;
import static net.encomendaz.services.Response.Status.OK;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import net.encomendaz.services.Response;
import net.encomendaz.services.notification.NotificationManager;
import net.encomendaz.services.util.Serializer;
import net.encomendaz.services.util.Strings;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

@Path("/monitoring.json")
@Produces(MEDIA_TYPE)
public class MonitoringService {

	@GET
	@Path("/test")
	public String test() {
		Query query = new Query("Monitoring");

		List<Entity> entities = new ArrayList<Entity>();

		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		PreparedQuery preparedQuery = datastore.prepare(query);

		for (Entity entity : preparedQuery.asIterable()) {
			String clientId = (String) entity.getProperty("clientId");
			String trackId = (String) entity.getProperty("trackId");
			Monitoring monitoring = new Monitoring(clientId, trackId);

			entity.setProperty("old", true);
			entity.setProperty("newKey", monitoring.hashCode());
			entity.removeProperty("monitored");

			entities.add(entity);
		}

		datastore.put(entities);

		return "ok";
	}

	@GET
	public String search(@QueryParam("clientId") String clientId, @QueryParam("trackId") String trackId,
			@QueryParam("callback") String callback) throws MonitoringException {

		validateClientId(clientId);
		List<Monitoring> list;

		if (clientId.equalsIgnoreCase("<all>")) {
			list = MonitoringManager.findAll();
		} else {
			list = MonitoringManager.find(clientId);
		}

		Response<List<Monitoring>> response = new Response<List<Monitoring>>();
		response.setStatus(OK);
		response.setData(list);

		return Serializer.json(response, callback);
	}

	@PUT
	public String register(@FormParam("clientId") String clientId, @FormParam("trackId") String trackId,
			@FormParam("label") String label, @FormParam("callback") String callback) throws MonitoringException {

		validateClientId(clientId);
		validateTrackId(trackId);

		String deviceToken = null;
		int i;
		if ((i = clientId.indexOf(":")) > 0) {
			deviceToken = clientId.substring(i + 1, clientId.length());
			clientId = clientId.substring(0, i);

			NotificationManager.register(deviceToken, clientId);
		}

		Monitoring monitoring = MonitoringManager.load(clientId, trackId);
		Response<String> response = new Response<String>();

		if (monitoring == null) {
			monitoring = new Monitoring(clientId, trackId);
			monitoring.setLabel("".equals(label) ? null : label);
			MonitoringManager.insert(monitoring);

			response.setStatus(OK);

		} else if (!(monitoring.getLabel() == null ? "" : monitoring.getLabel()).equals(label == null ? "" : label)) {
			monitoring.setLabel(label);
			MonitoringManager.update(monitoring);

			response.setStatus(OK);

		} else {
			throw new MonitoringException("Duplicado");
		}

		return Serializer.json(response, callback);
	}

	@DELETE
	public String delete(@QueryParam("clientId") String clientId, @QueryParam("trackId") String trackId,
			@QueryParam("callback") String callback) throws MonitoringException {

		validateClientId(clientId);
		validateTrackId(trackId);

		Monitoring monitoring = MonitoringManager.load(clientId, trackId);

		if (monitoring == null) {
			throw new MonitoringException("Não existe monitoramento para o clientId=" + clientId + " e trackId="
					+ trackId);

		} else {
			MonitoringManager.delete(monitoring);
		}

		Response<String> response = new Response<String>();
		response.setStatus(OK);

		return Serializer.json(response, callback);
	}

	private void validateClientId(String clientId) throws MonitoringException {
		if (Strings.isEmpty(clientId)) {
			throw new MonitoringException("O parâmetro clientId é obrigatório");
		}
	}

	private void validateTrackId(String trackId) throws MonitoringException {
		if (Strings.isEmpty(trackId)) {
			throw new MonitoringException("O parâmetro trackId é obrigatório");
		}
	}
}
