package ua.artcode.taxi.model;

import javax.persistence.*;

@Entity
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "street", nullable = false)
    private String street;

    @Column(name = "houseNum", nullable = false)
    private String houseNum;

    // google api
    @Transient
    private double lat;
    @Transient
    private double lon;

    public Address(String country, String city, String street, String houseNum) {
        this.city = city;
        this.street = street;
        this.houseNum = houseNum;
        this.country = country;
    }

    public Address(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public Address() {
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
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

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

    /*@Override
    public boolean equals(Object obj) {

        if (obj instanceof Address) {
            return country.equals(((Address) obj).country) &&
                    city.equals(((Address) obj).city) &&
                    street.equals(((Address) obj).street) &&
                    houseNum.equals(((Address) obj).houseNum);
        }

        return false;
    }*/

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    public String separateByCommas(Address address) {

        return String.format("%s,%s,%s,%s",
                address.getCountry(),
                address.getCity(),
                address.getStreet(),
                address.getHouseNum());
    }
}
