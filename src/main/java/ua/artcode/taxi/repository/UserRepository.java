package ua.artcode.taxi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.artcode.taxi.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByUserphone(String userphone);
}
