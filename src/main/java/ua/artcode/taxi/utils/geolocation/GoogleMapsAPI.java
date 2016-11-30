package ua.artcode.taxi.utils.geolocation;

import ua.artcode.taxi.exception.InputDataWrongException;
import ua.artcode.taxi.model.Address;

import java.io.IOException;

public interface GoogleMapsAPI {

    Location findLocation(String unformatted) throws InputDataWrongException;

    Location findLocation(String country, String city, String street, String houseNum) throws InputDataWrongException;

    double getDistance(Location pointA, Location pointB) throws InputDataWrongException;

    Address getCurrentLocation()  throws IOException;

}