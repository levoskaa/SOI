package rest_w9hl9h;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class Movies implements IMovies {
    private static int nextId = 0;
    private static List<Movie> movies = new ArrayList<Movie>();

    @Override
    public MoviesResult getMovies() {
	MoviesResult result = new MoviesResult();
	result.setMovies(movies);
	return result;
    }

    @Override
    public Movie getMovieById(int id) {
	Optional<Movie> result = movies.stream()
		.filter(movie -> movie.getId() == id)
		.collect(Collectors.reducing((a, b) -> null));
	if (result.isEmpty()) {
	    throw new WebApplicationException(Response.Status.NOT_FOUND);
	}
	return result.get();
    }

    @Override
    public List<Movie> findMovie(int year, String orderingField) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public IdResult createMovie(Movie movie) {
	boolean isIdTaken = movies.stream().anyMatch(m -> m.getId() == nextId);
	while (isIdTaken) {
	    ++nextId;
	    isIdTaken = movies.stream().anyMatch(m -> m.getId() == nextId);
	}
	movie.setId(nextId);
	movies.add(movie);
	IdResult result = new IdResult();
	result.setId(movie.getId());
	++nextId;
	return result;
    }

    @Override
    public Response upsertMovie(int id, Movie movie) {
	if (id < 0) {
	    throw new WebApplicationException(Response.Status.BAD_REQUEST);
	}
	Optional<Movie> result = movies.stream().filter(m -> m.getId() == id)
		.collect(Collectors.reducing((a, b) -> null));
	Response response;
	if (result.isEmpty()) {
	    movie.setId(id);
	    movies.add(movie);
	    response = Response.status(Response.Status.CREATED).build();
	} else {
	    int index = movies.indexOf(result.get());
	    movie.setId(id);
	    movies.set(index, movie);
	    response = Response.status(Response.Status.OK).build();
	}
	return response;
    }

    @Override
    public Response deleteMovie(int id) {
	Optional<Movie> movieToDelete = movies.stream()
		.filter(m -> m.getId() == id)
		.collect(Collectors.reducing((a, b) -> null));
	if (movieToDelete.isPresent()) {
	    movies.remove(movieToDelete.get());
	}
	return Response.status(Response.Status.NO_CONTENT).build();
    }
}
