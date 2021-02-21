package rest_w9hl9h;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "movies")
@XmlAccessorType(XmlAccessType.FIELD)
public class MovieIdsResult {
    @XmlElement(name = "id")
    private List<Integer> id = new ArrayList<Integer>();

    public List<Integer> getId() {
	return id;
    }

    public void setId(List<Integer> id) {
	this.id = id;
    }
}
