package ru.practicum.service.event.common;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.service.event.common.entity.Event;
import ru.practicum.service.event.common.entity.State;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findByIdAndInitiatorId(Long id, Long initiatorId);

    @Query(value = "SELECT e.* FROM events e WHERE e.initiator_id = ?1 LIMIT ?2 OFFSET ?3", nativeQuery = true)
    List<Event> findAllByInitiatorId(Long initiatorId, int limit, int offset);

    Optional<Event> findByIdAndState(Long id, State state);

    Optional<Event> findByCategoryId(Long categoryId);

    List<Event> findAllByIdIn(List<Long> ids);
}
