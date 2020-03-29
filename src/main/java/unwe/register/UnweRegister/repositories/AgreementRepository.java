package unwe.register.UnweRegister.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unwe.register.UnweRegister.entities.Agreement;

import java.util.List;

@Repository
public interface AgreementRepository extends JpaRepository<Agreement, String> {

    long count();

    List<Agreement> findAllByOrderByDateDesc(Pageable pageable);

}
