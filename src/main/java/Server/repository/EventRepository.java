package Server.repository;

import Server.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByTitle(String title);

    List<Event> findByTitleContaining(String title);
}
