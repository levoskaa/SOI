package rest_w9hl9h;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "movie")
@XmlType(propOrder = { "title", "year", "director", "actor" })
@XmlAccessorType(XmlAccessType.FIELD)
public class Movie {
    @XmlTransient
    private int id;

    @XmlElement(name = "title")
    private String title;

    @XmlElement(name = "year")
    private int year;

    @XmlElement(name = "director")
    private String director;

    @XmlElement(name = "actor")
    private List<String> actor;

    public Movie() {
	title = "";
	year = 0;
	director = "";
	actor = new ArrayList<String>();
    }

    public int getId() {
	return id;
    }

    public void setId(int id) {
	if (id >= 0) {
	    this.id = id;
	}
    }

    public String getTitle() {
	return title;
    }

    public int getYear() {
	return year;
    }

    public String getDirector() {
	return director;
    }

    public List<String> getActor() {
	return actor;
    }
}
