package ua.artcode.taxi.model;

import javax.persistence.*;

@Entity
@Table(name = "car")
public class Car {

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(referencedColumnName = "id")
    private User user;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "type", nullable = false)
    String type;

    @Column(name = "model", nullable = false)
    String model;

    @Column(name = "number", nullable = false)
    String number;

    public Car() {

    }

    public Car(String type, String model, String number) {
        this.number = number;
        this.model = model;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

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
