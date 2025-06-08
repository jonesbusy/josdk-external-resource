package cloud.jonesbusy.experiment;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

/**
 * Client interface for fetching objects from a RESTful API.
 * This interface uses MicroProfile Rest Client to define the endpoint and method for fetching an object by its ID.
 */
@Path("users")
@RegisterRestClient(baseUri = "https://jsonplaceholder.typicode.com")
public interface ObjectFetcherClient {

    /**
     * Fetches an object by its ID from the external API.
     * @param id The ID of the object to be fetched.
     * @return An instance of {@link ObjectModel} representing the fetched object.
     */
    @Path("{id}")
    @GET
    ObjectModel getObject(@PathParam("id") String id);
}
