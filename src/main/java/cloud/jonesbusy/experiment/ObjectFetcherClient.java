package cloud.jonesbusy.experiment;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("objects")
@RegisterRestClient(baseUri = "https://api.restful-api.dev")
public interface ObjectFetcherClient {

    @Path("{id}")
    @GET
    APIModel getObject(String id);
}
