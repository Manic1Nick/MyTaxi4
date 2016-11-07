package ua.artcode.taxi.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "user")
@NamedQueries({@NamedQuery(name = "getAllUsers", query = "SELECT c FROM User c")})
public class User implements PassengerActive, DriverActive {

    /*@Id
    @GeneratedValue(strategy = GenerationType.AUTO)*/
    private Long id;

    //@Enumerated(EnumType.STRING)
    private UserIdentifier identifier;

    //@Column(name = "phone", nullable = false)
    private String userphone;

    //@Column(name = "password", nullable = false)
    private String password;

    private String passwordConfirm;

    //@Column(name = "username", nullable = false)
    private String username;

    /*@OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(referencedColumnName = "id")*/
    private Address homeAddress;

    /*@OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(referencedColumnName = "id")*/
    private Car car;

    @Transient
    private String userCurrentLocation;

    private Set<Role> roles;

    public User() {
    }

    //for passenger
    public User(UserIdentifier identifier, String userphone, String password, String username, Address homeAddress) {
        this.identifier = identifier;
        this.userphone = userphone;
        this.password = password;
        this.username = username;
        this.homeAddress = homeAddress;
    }

    //for driver
    public User(UserIdentifier identifier, String userphone, String password, String username, Car car) {
        this.identifier = identifier;
        this.userphone = userphone;
        this.password = password;
        this.username = username;
        this.car = car;
    }

    //for anonymous
    public User(UserIdentifier identifier, String userphone, String username) {
        this.identifier = identifier;
        this.userphone = userphone;
        this.username = username;
    }

    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public UserIdentifier getIdentifier() {
        return identifier;
    }

    @Override
    public void setIdentifier(UserIdentifier identifier) {
        this.identifier = identifier;
    }

    public String getUserphone() {
        return userphone;
    }

    public void setUserphone(String phone) {
        this.userphone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String pass) {
        this.password = pass;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String name) {
        this.username = name;
    }

    @Override
    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(referencedColumnName = "id")
    public Address getHomeAddress() {
        return homeAddress;
    }

    @Override
    public void setHomeAddress(Address homeAddress) {
        this.homeAddress = homeAddress;
    }

    @Override
    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(referencedColumnName = "id")
    public Car getCar() {
        return car;
    }

    @Override
    public void setCar(Car car) {
        this.car = car;
    }

    //todo get current user location
    public String getUserCurrentLocation() {

        return Constants.USER_LOCATION_PATH;
    }

    public void setUserCurrentLocation(String userCurrentLocation) {

        this.userCurrentLocation = userCurrentLocation;
    }

    @Transient
    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    @ManyToMany
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", identifier=" + identifier +
                ", userphone='" + userphone + '\'' +
                ", password='" + password + '\'' +
                ", passwordConfirm='" + passwordConfirm + '\'' +
                ", username='" + username + '\'' +
                ", homeAddress=" + homeAddress +
                ", car=" + car +
                ", userCurrentLocation='" + userCurrentLocation + '\'' +
                ", roles=" + roles +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    /*@Override
    public boolean equals(Object obj) {

        if (obj instanceof User) {

            if (((User)obj).identifier.equals(UserIdentifier.P)) {
                return  id == (((User)obj).id) &&
                        identifier.equals(((User)obj).identifier) &&
                        phone.equals(((User)obj).phone) &&
                        name.equals(((User)obj).name) &&
                        pass.equals(((User)obj).pass) &&
                        homeAddress.equals(((User)obj).homeAddress);

            } else if (((User)obj).identifier.equals(UserIdentifier.D)) {
                return  id == (((User)obj).id) &&
                        identifier.equals(((User)obj).identifier) &&
                        phone.equals(((User)obj).phone) &&
                        name.equals(((User)obj).name) &&
                        pass.equals(((User)obj).pass) &&
                        car.equals(((User)obj).car);
            }
        }

        return false;
    }*/

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
