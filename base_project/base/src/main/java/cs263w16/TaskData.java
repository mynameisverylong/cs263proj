package cs263w16;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.*;

@XmlRootElement
// JAX-RS supports an automatic mapping from JAXB annotated class to XML and JSON
public class TaskData {
  private String keyname;
  private String value;
  private Date date;

  //add constructors (default () and (String,String,Date))
  //add getters and setters for all fields
  public TaskData() {
      date = new Date();
  }

  public TaskData(String k, String v, Date d) {
      keyname = k;
      value = v;
      date = d;
  }

  public String getKeyname() {
      return keyname;
  }

  public String getValue() {
      return value;
  }

  public Date getDate() {
      return date;
  }

  public void setKeyname(String keyname) {
      this.keyname = keyname;
  }

  public void setValue(String value) {
      this.value = value;
  }

  public void setDate(Date date) {
      this.date = date;
  }
} 

