package ua.artcode.taxi.model;

public interface PassengerActive {

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

    Address getHomeAddress();
    void setHomeAddress(Address homeAddress);

    String toString();
}
