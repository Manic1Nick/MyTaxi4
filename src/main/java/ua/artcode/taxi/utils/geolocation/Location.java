package ua.artcode.taxi.utils.geolocation;

public class Location {

    private String formattedAddress;
    private double lat;
    private double lon;
    private String placeId;

    public Location(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public Location(String formattedAddress, double lat, double lon) {
        this.formattedAddress = formattedAddress;
        this.lat = lat;
        this.lon = lon;
    }

    public Location(String formattedAddress, double lat, double lon, String placeId) {
        this.formattedAddress = formattedAddress;
        this.lat = lat;
        this.lon = lon;
        this.placeId = placeId;
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
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

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    @Override
    public String toString() {
        return "Location{" +
                "formattedAddress='" + formattedAddress + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                ", placeId='" + placeId + '\'' +
                '}';
    }

}