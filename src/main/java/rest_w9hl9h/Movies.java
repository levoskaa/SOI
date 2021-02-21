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
    public List<Movie> getMovies() {
	// TODO Auto-generated method stub
	return null;
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
	movie.setId(nextId);
	movies.add(movie);
	IdResult result = new IdResult();
	result.setId(nextId);
	++nextId;
	return result;
    }

    @Override
    public void upsertMovie(int id, Movie movie) {
	// TODO Auto-generated method stub

    }

    @Override
    public void deleteMovie(int id) {
	// TODO Auto-generated method stub

    }

}
