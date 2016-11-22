package ua.artcode.taxi.service;

import ua.artcode.taxi.exception.InputDataWrongException;
import ua.artcode.taxi.model.Address;
import ua.artcode.taxi.model.Order;
import ua.artcode.taxi.model.OrderStatus;
import ua.artcode.taxi.model.User;

import java.util.List;
import java.util.Map;

public interface UserService {


    void saveNewUser(User user);
    void saveNewOrder(Order order, User user)
            throws InputDataWrongException;

    User getByUsername(String username);
    User getByUserphone(String userphone);
    User getById(Long id);
    User updateUser(Long id, User newUser);

    Order getLastOrder(String userphone);
    Order getOrderById(Long id);
    Order updateOrder(Long id, Order newOrder);

    List<Order> getListOrdersOfUser(String userphone);
    List<Order> getListOrdersByOrderStatus(OrderStatus orderStatus);

    Map<Long, Double> createMapOrdersWithDistancesToDriver(
            List<Order> orders, Address addressDriver)
            throws InputDataWrongException;

    Address getUserLocation();

    Order takeOrderByDriver(Long orderId, User driver);
}
