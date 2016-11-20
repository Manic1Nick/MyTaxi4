package ua.artcode.taxi.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
//@NamedQueries({@NamedQuery(name = "getAllUsers", query = "SELECT c FROM User c")})
public class User {

    private Long id;
    private UserIdentifier identifier;
    private String userphone;
    private String password;
    private String passwordConfirm;
    private String username;
    private Address homeAddress;
    private Car car;
    private Address currentAddress;
    private Set<Role> roles = new HashSet<>();

    private Long lastOrderId;

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

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserIdentifier getIdentifier() {
        return identifier;
    }

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

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(referencedColumnName = "id")
    public Address getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(Address homeAddress) {
        this.homeAddress = homeAddress;
    }

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(referencedColumnName = "id")
    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    //todo get current user location
    @Transient
    public Address getCurrentAddress() {

        return new Address(Constants.USER_LOCATION_PATH);
    }

    public void setCurrentAddress(Address currentAddress) {

        this.currentAddress = currentAddress;
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

    public Long getLastOrderId() {
        return lastOrderId;
    }

    public void setLastOrderId(Long lastOrderId) {
        this.lastOrderId = lastOrderId;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
