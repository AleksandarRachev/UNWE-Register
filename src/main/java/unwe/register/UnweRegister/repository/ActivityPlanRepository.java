package unwe.register.UnweRegister.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unwe.register.UnweRegister.entity.ActivityPlan;

@Repository
public interface ActivityPlanRepository extends JpaRepository<ActivityPlan, String> {
}
