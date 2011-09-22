import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.alfredlibrary.utilitarios.correios.Rastreamento;
import org.alfredlibrary.utilitarios.correios.RegistroRastreamento;

@Path("/rastramento")
public class RastreamentoRest {

	@GET
	@Path("/{codigo}")
	public String buscar(@PathParam("codigo") String codigo) {
		for (RegistroRastreamento registro : Rastreamento.rastrear(codigo)) {
			System.out.println(registro.toString());
		}

		return "achou!!";
	}

}
