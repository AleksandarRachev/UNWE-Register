package unwe.register.UnweRegister.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unwe.register.UnweRegister.entities.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, String> {
}
