package unwe.register.UnweRegister.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unwe.register.UnweRegister.entities.Agreement;

import java.util.List;
import java.util.Optional;

@Repository
public interface AgreementRepository extends JpaRepository<Agreement, String> {

    List<Agreement> findAllByUidContainingOrTitleContainingOrCoordinatorFirstNameContainingOrCoordinatorLastNameContainingOrEmployerFirstNameContainingOrEmployerLastNameContainingOrNumberOrderByDateDesc(
            Pageable pageable, String uid, String title, String coordinatorFirstName, String coordinatorLastName,
            String employerFirstName, String employerLastName, Long number);

    List<Agreement> findAllByUidContainingOrTitleContainingOrCoordinatorFirstNameContainingOrCoordinatorLastNameContainingOrEmployerFirstNameContainingOrEmployerLastNameContainingOrNumberOrderByDateAsc(
            Pageable pageable, String uid, String title, String coordinatorFirstName, String coordinatorLastName,
            String employerFirstName, String employerLastName, Long number);

    Long countByUidContainingOrTitleContainingOrCoordinatorFirstNameContainingOrCoordinatorLastNameContainingOrEmployerFirstNameContainingOrEmployerLastNameContainingOrNumber(
            String uid, String title, String coordinatorFirstName, String coordinatorLastName,
            String employerFirstName, String employerLastName, Long number);

    Optional<Agreement> findByNumber(Long agreementNumber);

}
