package unwe.register.UnweRegister.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unwe.register.UnweRegister.entity.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, String> {
}
