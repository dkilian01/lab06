import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class User {

    private int id;
    private String name;
    private String surname;
    private Integer addressId;


    public User(int id, String name, String surname, int address) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.addressId = address;
    }

    public User(String name, String surname, int address) {
        this.name = name;
        this.surname = surname;
        this.addressId = address;
    }

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Integer getAddress() {
        return addressId;
    }

    public void setAddress(int address) {
        this.addressId = address;
    }

    @Override
    public String toString(){
        return "User[id=" + id
                + ", name=" + name
                + ", surname=" + surname
                + ", addressId=" + addressId + "]";
    }
}
