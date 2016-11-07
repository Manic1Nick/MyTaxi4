package ua.artcode.taxi.model;

public interface DriverActive {

    Long getId();
    void setId(Long id);

    UserIdentifier getIdentifier();
    void setIdentifier(UserIdentifier identifier);

    String getUserphone();
    void setUserphone(String phone);

    String getPassword();
    void setPassword(String pass);

    String getUsername();
    void setUsername(String name);

    Car getCar();
    void setCar(Car car);

    String toString();
}
