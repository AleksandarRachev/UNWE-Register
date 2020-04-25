package unwe.register.UnweRegister.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unwe.register.UnweRegister.entities.Event;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, String> {

    List<Event> findAllByTitleContainingOrActivityPlanAgreementEmployerCompanyNameContainingOrderByMadeOnDesc(
            Pageable pageable, String title, String companyName);

    List<Event> findAllByTitleContainingOrActivityPlanAgreementEmployerCompanyNameContainingOrderByMadeOnAsc(
            Pageable pageable, String title, String companyName);

    long countByTitleContainingOrActivityPlanAgreementEmployerCompanyNameContaining(String title, String companyName);

}
