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

import movies.Movies.Movie;
import movies.Movies.MovieId;
import movies.Movies.MovieIdList;
import movies.Movies.MovieList;

@Path("movies")
@Consumes({ "application/json", "application/x-protobuf" })
@Produces({ "application/json", "application/x-protobuf" })
public interface IMoviesService {
    @GET
    MovieList getMovies();

    @GET
    @Path("{id}")
    Movie getMovieById(@PathParam("id") int id);

    @GET
    @Path("find")
    MovieIdList findMovie(@QueryParam("year") int year,
	    @QueryParam("orderby") String orderingField);

    @POST
    MovieId createMovie(Movie movie);

    @PUT
    @Path("{id}")
    void upsertMovie(@PathParam("id") int id, Movie movie);

    @DELETE
    @Path("{id}")
    void deleteMovie(@PathParam("id") int id);
}