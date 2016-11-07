package ua.artcode.taxi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.artcode.taxi.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
