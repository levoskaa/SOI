package movies;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import movies.Movies.Movie;
import movies.Movies.MovieId;
import movies.Movies.MovieIdList;
import movies.Movies.MovieList;

public class MoviesService implements IMoviesService {
    private static int nextId = 0;
    private static List<MyMovie> movies = new ArrayList<>();

    @Override
    public MovieList getMovies() {
	return MovieList.newBuilder().addAllMovie(movies.stream()
		.map(movie -> movie.getMovie()).collect(Collectors.toList()))
		.build();
    }

    @Override
    public Movie getMovieById(int id) {
	Optional<Movie> result = movies.stream()
		.filter(movie -> movie.getId() == id)
		.map(movie -> movie.getMovie())
		.collect(Collectors.reducing((a, b) -> null));
	if (result.isEmpty()) {
	    throw new WebApplicationException(Response.Status.NOT_FOUND);
	}
	return result.get();
    }

    @Override
    public MovieIdList findMovie(int year, String orderingField) {
	if (!orderingField.toUpperCase().equals("DIRECTOR")
		&& !orderingField.toUpperCase().equals("TITLE")) {
	    throw new WebApplicationException(Response.Status.BAD_REQUEST);
	}
	List<MyMovie> searchedMovies = movies.stream()
		.filter(movie -> movie.getMovie().getYear() == year)
		.collect(Collectors.toList());
	Collections.sort(searchedMovies, (a, b) -> {
	    if (orderingField.toUpperCase().equals("DIRECTOR")) {
		return a.getMovie().getDirector()
			.compareTo(b.getMovie().getDirector());
	    }
	    return a.getMovie().getTitle().compareTo(b.getMovie().getTitle());
	});
	List<Integer> searchedIds = searchedMovies.stream()
		.map(movie -> movie.getId()).collect(Collectors.toList());
	return MovieIdList.newBuilder().addAllId(searchedIds).build();
    }

    @Override
    public MovieId createMovie(Movie movie) {
	boolean isIdTaken = movies.stream().anyMatch(m -> m.getId() == nextId);
	while (isIdTaken) {
	    ++nextId;
	    isIdTaken = movies.stream().anyMatch(m -> m.getId() == nextId);
	}
	MyMovie myMovie = new MyMovie();
	myMovie.setId(nextId);
	myMovie.setMovie(movie);
	movies.add(myMovie);
	++nextId;
	return MovieId.newBuilder().setId(myMovie.getId()).build();
    }

    @Override
    public void upsertMovie(int id, Movie movie) {
	if (id < 0) {
	    throw new WebApplicationException(Response.Status.BAD_REQUEST);
	}
	Optional<MyMovie> result = movies.stream().filter(m -> m.getId() == id)
		.collect(Collectors.reducing((a, b) -> null));
	MyMovie myMovie = new MyMovie();
	myMovie.setId(id);
	myMovie.setMovie(movie);
	if (result.isEmpty()) {
	    movies.add(myMovie);
	} else {
	    int index = movies.indexOf(result.get());
	    movies.set(index, myMovie);
	}
    }

    @Override
    public void deleteMovie(int id) {
	Optional<MyMovie> movieToDelete = movies.stream()
		.filter(m -> m.getId() == id)
		.collect(Collectors.reducing((a, b) -> null));
	if (movieToDelete.isPresent()) {
	    movies.remove(movieToDelete.get());
	}
    }
}
