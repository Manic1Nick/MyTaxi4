package ua.artcode.taxi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.artcode.taxi.model.Order;
import ua.artcode.taxi.model.OrderStatus;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long>{
    Order findById(Long id);
    List<Order> findByIdPassenger(Long id);
    List<Order> findByIdDriver(Long id);
    List<Order> findByOrderStatus(OrderStatus orderStatus);
}
