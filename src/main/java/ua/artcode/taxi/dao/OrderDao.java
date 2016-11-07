package ua.artcode.taxi.dao;

import ua.artcode.taxi.model.Order;
import ua.artcode.taxi.model.OrderStatus;

import java.util.Collection;
import java.util.List;

public interface OrderDao {

    Order create(Order order);
    Collection<Order> getAllOrders();
    Order update(Order newOrder);
    Order delete(Long id);

    Order findById(Long id);
    List<Order> getOrdersByStatus(OrderStatus status);
    Order getLastOrderOfUser(Long userId);
}
