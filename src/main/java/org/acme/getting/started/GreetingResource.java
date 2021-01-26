package org.acme.getting.started;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/hello")
public class GreetingResource {

  @Inject
  App app;

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public List<String> hello(@QueryParam("message") String message) {
    if (message != null && !message.isBlank()) {
      app.data().add(message);
    }

    return app.data().messages();
  }

}