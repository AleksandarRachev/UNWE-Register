package unwe.register.UnweRegister.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unwe.register.UnweRegister.entities.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, String> {
}
