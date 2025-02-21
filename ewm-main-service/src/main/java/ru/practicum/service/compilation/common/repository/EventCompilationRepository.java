package ru.practicum.service.compilation.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.service.compilation.common.entity.EventCompilation;

import java.util.List;
import java.util.Optional;

public interface EventCompilationRepository extends JpaRepository<EventCompilation, Long> {

    Optional<EventCompilation> findByEventIdAndCompilationId(Long eventId, Long compilationId);

    @Query(value = "SELECT ec.event_id FROM event_compilation ec WHERE ec.compilation_id = ?1", nativeQuery = true)
    List<Long> findAllEventIdByCompilationId(Long compilationId);
}
