package rest_w9hl9h;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "result")
@XmlAccessorType(XmlAccessType.FIELD)
public class IdResult {
    @XmlElement(name = "id")
    private int id;

    public int getId() {
	return id;
    }

    public void setId(int id) {
	this.id = id;
    }
}
