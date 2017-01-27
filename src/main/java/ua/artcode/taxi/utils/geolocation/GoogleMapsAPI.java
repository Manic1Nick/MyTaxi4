package ua.artcode.taxi.utils.geolocation;

import ua.artcode.taxi.exception.InputDataWrongException;

import java.io.IOException;

public interface GoogleMapsAPI {

    Location findLocation(String unformatted) throws InputDataWrongException;

    Location findLocation(String country, String city, String street, String houseNum) throws InputDataWrongException;

    Location findPlaceID(Location location) throws InputDataWrongException;

    double getDistance(Location pointA, Location pointB) throws InputDataWrongException;

    Location getCurrentLocation() throws IOException, InputDataWrongException, NullPointerException;

}