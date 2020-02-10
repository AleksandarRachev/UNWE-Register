package unwe.register.UnweRegister.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unwe.register.UnweRegister.entity.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, String> {
}
