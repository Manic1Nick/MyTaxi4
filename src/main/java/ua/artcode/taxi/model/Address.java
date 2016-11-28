package ua.artcode.taxi.model;

import ua.artcode.taxi.exception.InputDataWrongException;
import ua.artcode.taxi.utils.geolocation.GoogleMapsAPI;
import ua.artcode.taxi.utils.geolocation.GoogleMapsAPIImpl;
import ua.artcode.taxi.utils.geolocation.Location;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "addresses")
public class Address {

    private Long id;

    private String country;
    private String city;
    private String street;
    private String houseNum;

    //Set<Order> orders;
    //private User user;

    private GoogleMapsAPI googleMapsAPI;
    private Location location;
    private double lat;
    private double lon;

    public Address(String country, String city, String street, String houseNum) {
        this.city = city;
        this.street = street;
        this.houseNum = houseNum;
        this.country = country;
        googleMapsAPI = new GoogleMapsAPIImpl();
    }

    public Address(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
        googleMapsAPI = new GoogleMapsAPIImpl();
    }

    public Address() {
        googleMapsAPI = new GoogleMapsAPIImpl();
    }

    public Address(String line){

        String[] address = line.split(",");

        if (address.length >= 4) {
            this.country = address[0].trim();
            this.city = address[1].trim();
            this.street = address[2].trim();
            this.houseNum = address[3].trim();
        } else {
            this.country = line;
            this.city = "";
            this.street = "";
            this.houseNum = "";
        }
        googleMapsAPI = new GoogleMapsAPIImpl();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouseNum() {
        return houseNum;
    }

    public void setHouseNum(String houseNum) {
        this.houseNum = houseNum;
    }

    @Transient
    public Location getLocation() throws InputDataWrongException {

        return googleMapsAPI.findLocation(
                this.getCountry(),
                this.getCity(),
                this.getStreet(),
                this.getHouseNum());
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public double getLat() throws InputDataWrongException {

        Location current = this.getLocation();

        return current.getLat();
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() throws InputDataWrongException {

        Location current = this.getLocation();

        return current.getLon();
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    /*@OneToMany(cascade=CascadeType.ALL)
    @JoinColumn(referencedColumnName = "id")
    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }*/

    /*@OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(referencedColumnName = "id")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }*/

    @Override
    public String toString() {
        return "Address{" +
                "country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", street='" + street + '\'' +
                ", houseNum='" + houseNum + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    public String separateByCommas() {

        return String.format("%s,%s,%s,%s",
                this.getCountry(),
                this.getCity(),
                this.getStreet(),
                this.getHouseNum());
    }

    /*private Location getLocationFromAddress(Address address) throws InputDataWrongException {

        return googleMapsAPI.findLocation(
                address.getCountry(),
                address.getCity(),
                address.getStreet(),
                address.getHouseNum()
        );
    }*/
}
