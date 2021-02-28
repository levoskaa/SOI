package movies;

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
import javax.ws.rs.core.Response;

import movies.Movies.Movie;

@Path("hello")
@Consumes("application/x-protobuf")
@Produces("application/x-protobuf")
public interface IMoviesService {
    @GET
    Response getMovies();

    @GET
    @Path("{id}")
    Response getMovieById(@PathParam("id") int id);

    @GET
    @Path("find")
    Response findMovie(@QueryParam("year") int year,
	    @QueryParam("orderby") String orderingField);

    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    Response createMovie(Movie movie);

    @PUT
    @Path("{id}")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    Response upsertMovie(@PathParam("id") int id, Movie movie);

    @DELETE
    @Path("{id}")
    Response deleteMovie(@PathParam("id") int id);
}