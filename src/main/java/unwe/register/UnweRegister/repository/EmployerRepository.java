package unwe.register.UnweRegister.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unwe.register.UnweRegister.entity.Employer;

@Repository
public interface EmployerRepository extends JpaRepository<Employer, String> {
}
