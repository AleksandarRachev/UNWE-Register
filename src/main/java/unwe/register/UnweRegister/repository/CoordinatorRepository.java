package unwe.register.UnweRegister.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unwe.register.UnweRegister.entity.Coordinator;

@Repository
public interface CoordinatorRepository extends JpaRepository<Coordinator, String> {
}
