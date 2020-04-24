package unwe.register.UnweRegister.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unwe.register.UnweRegister.entities.Chat;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, String> {

    Optional<Chat> findByEmployerUidAndCoordinatorUid(String employerId, String coordinatorId);

    List<Chat> findAllByCoordinatorUid(String coordinatorId);

    List<Chat> findAllByEmployerUid(String employerId);

}
