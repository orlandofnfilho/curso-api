import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;


public class Principal {

	public static void main(String[] args) {
		
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("https://ficr-store.herokuapp.com/api/v1");
		String resposta = target.path("/products").request().get(String.class);
		System.out.println(resposta);
	}	
}
