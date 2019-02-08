package eu.agilejava.dukes.weather;

import javax.enterprise.context.Dependent;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/weather")
@Dependent
@RegisterRestClient
public interface WeatherService {

    @GET
    @Produces("application/json")
    public Response weather(@QueryParam("zip") String zipcode,
                            @QueryParam("units") String units,
                            @QueryParam("appid") String appid
                            );
}
