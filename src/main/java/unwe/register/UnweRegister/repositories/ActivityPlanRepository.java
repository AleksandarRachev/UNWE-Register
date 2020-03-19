package unwe.register.UnweRegister.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unwe.register.UnweRegister.entities.ActivityPlan;

@Repository
public interface ActivityPlanRepository extends JpaRepository<ActivityPlan, String> {
}
