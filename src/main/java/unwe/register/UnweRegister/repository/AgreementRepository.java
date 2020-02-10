package unwe.register.UnweRegister.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unwe.register.UnweRegister.entity.Agreement;

@Repository
public interface AgreementRepository extends JpaRepository<Agreement, String> {
}
