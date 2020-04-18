package unwe.register.UnweRegister.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unwe.register.UnweRegister.entities.Event;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, String> {

    List<Event> findAllByTitleContainingOrderByMadeOnDesc(Pageable pageable, String title);

    long countByTitleContaining(String title);

}
