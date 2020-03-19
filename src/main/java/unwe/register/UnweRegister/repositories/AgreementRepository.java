package unwe.register.UnweRegister.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unwe.register.UnweRegister.entities.Agreement;

@Repository
public interface AgreementRepository extends JpaRepository<Agreement, String> {
}
