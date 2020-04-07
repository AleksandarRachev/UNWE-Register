package unwe.register.UnweRegister.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unwe.register.UnweRegister.dtos.activityPlan.ActivityPlanResponse;
import unwe.register.UnweRegister.entities.ActivityPlan;

import java.util.List;

@Repository
public interface ActivityPlanRepository extends JpaRepository<ActivityPlan, String> {

    List<ActivityPlan> findAllByAgreementEmployerUid(Pageable pageable, String userId);

    Long countByAgreementEmployerUid(String userId);

}
