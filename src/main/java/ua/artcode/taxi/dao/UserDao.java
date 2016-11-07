package ua.artcode.taxi.dao;

import ua.artcode.taxi.model.Order;
import ua.artcode.taxi.model.User;

import java.util.Collection;
import java.util.List;

// CRUD, Create, Read, Update, Delete
public interface UserDao {

    User createUser(User user);
    Collection<User> getAllUsers();
    User updateUser(User newUser);
    User deleteUser(Long id);

    User findByPhone(String phone);
    User findById(Long id);
    List<Order> getOrdersOfUser(Long userId, int from, int to);
    int getQuantityOrdersOfUser(Long userId);
}