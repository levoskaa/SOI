package rest_w9hl9h;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "movies")
@XmlAccessorType(XmlAccessType.FIELD)
public class MoviesResult {
    @XmlElement(name = "movie")
    private List<Movie> movies = new ArrayList<Movie>();

    public List<Movie> getMovies() {
	return movies;
    }

    public void setMovies(List<Movie> movies) {
	this.movies = movies;
    }
}
