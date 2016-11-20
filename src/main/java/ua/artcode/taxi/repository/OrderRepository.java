package ua.artcode.taxi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.artcode.taxi.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long>{
    Order findById(Long id);

}
