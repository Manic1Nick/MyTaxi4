package ua.artcode.taxi.model;

import ua.artcode.taxi.exception.InputDataWrongException;
import ua.artcode.taxi.utils.geolocation.GoogleMapsAPI;

import javax.persistence.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    private Long id;
    private String userphone;
    private String password;
    private String passwordConfirm;
    private String username;
    private Address homeAddress;
    private Car car;

    private Set<Role> roles = new HashSet<>();

    private Long lastOrderId;
    private int quantityOrders;
    private boolean active;

    private Address currentAddress;

    public User() {
    }

    //for passenger
    public User(String userphone, String password, String username, Address homeAddress) {
        this.userphone = userphone;
        this.password = password;
        this.username = username;
        this.homeAddress = homeAddress;
    }

    //for driver
    public User(String userphone, String password, String username, Car car) {
        this.userphone = userphone;
        this.password = password;
        this.username = username;
        this.car = car;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public int getQuantityOrders() {
        return quantityOrders;
    }

    public void setQuantityOrders(int quantityOrders) {
        this.quantityOrders = quantityOrders;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Transient
    public Address getCurrentAddress() {

        return currentAddress;
    }

    public void setCurrentAddress(Address currentAddress) {

        this.currentAddress = currentAddress;
    }

    public String toShortViewJS() {

        String nameAndPhone = " name: " + username + ",<br /> phone: " + userphone;

        return car != null ? nameAndPhone + ",<br /> car: " + car.separateByCommas() : nameAndPhone;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userphone='" + userphone + '\'' +
                ", username='" + username + '\'' +
                ", homeAddress=" + homeAddress +
                ", car=" + car +
                ", lastOrderId=" + lastOrderId +
                ", quantityOrders=" + quantityOrders +
                ", active=" + active +
                '}';
    }
}
