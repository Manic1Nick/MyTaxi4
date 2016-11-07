package ua.artcode.taxi.service;

public interface SecurityService {
    String findLoggedInUsername();

    void autologin(String username, String password);
}
