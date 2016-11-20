package ua.artcode.taxi.model;

import javax.persistence.*;

@Entity
@Table(name = "cars")
public class Car {

    private Long id;

    private String type;
    private String model;
    private String number;

    //private User user;

    public Car() {

    }

    public Car(String type, String model, String number) {
        this.number = number;
        this.model = model;
        this.type = type;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    /*@OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(referencedColumnName = "id")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }*/

    @Override
    public boolean equals(Object obj) {

        if (obj instanceof Car) {
            return  type.equals(((Car) obj).type) &&
                    model.equals(((Car) obj).model) &&
                    number.equals(((Car) obj).number);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
