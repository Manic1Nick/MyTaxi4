package ua.artcode.taxi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.artcode.taxi.model.Address;
import ua.artcode.taxi.model.Car;
import ua.artcode.taxi.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByUserphone(String userphone);
    User findById(Long id);
    List<User> findByActiveAndHomeAddress(boolean active, Address homeAddress);
    List<User> findByActiveAndCar(boolean active, Car car);
}
