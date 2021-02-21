package rest_w9hl9h;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("movies")
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
public interface IMovieDatabase {
    @GET
    List<Movie> getMovies();

    @GET
    @Path("{id}")
    Movie getMovieById(@PathParam("id") int id);

    @GET
    @Path("find")
    List<Movie> findMovie(@QueryParam("year") int year,
	    @QueryParam("orderby") String orderingField);

    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    int createMovie(final Movie movie);

    @PUT
    @Path("{id}")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    void upsertMovie(@PathParam("id") int id, final Movie movie);

    @DELETE
    @Path("{id}")
    void deleteMovie(@PathParam("id") int id);
}
