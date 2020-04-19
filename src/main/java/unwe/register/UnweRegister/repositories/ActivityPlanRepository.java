package unwe.register.UnweRegister.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unwe.register.UnweRegister.entities.ActivityPlan;

import java.util.List;

@Repository
public interface ActivityPlanRepository extends JpaRepository<ActivityPlan, String> {

    List<ActivityPlan> findAllByAgreementEmployerUidAndUidContainingOrAgreementEmployerUidAndAgreementNumberOrderByMadeOnDesc(
            Pageable pageable, String userId, String uid, String userId2, Long agreementNumber);

    Long countByAgreementEmployerUidAndUidContainingOrAgreementEmployerUidAndAgreementNumber(
            String userId, String uid, String userId2, Long agreementNumber);

    List<ActivityPlan> findAllByUidContainingOrAgreementNumberOrderByMadeOnDesc(
            Pageable pageable, String uid, Long agreementNumber);

    Long countByUidContainingOrAgreementNumber(String uid, Long agreementNumber);

}
