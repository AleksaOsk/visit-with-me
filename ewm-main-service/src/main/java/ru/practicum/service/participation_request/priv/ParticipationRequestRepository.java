package ru.practicum.service.participation_request.priv;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.service.participation_request.priv.entity.ParticipationRequest;

import java.util.List;
import java.util.Optional;

interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {

    List<ParticipationRequest> findAllByEventId(Long eventId);

    Optional<ParticipationRequest> findByIdAndRequesterId(Long id, Long requesterId);

    Optional<ParticipationRequest> findByEventIdAndRequesterId(Long eventId, Long requesterId);

    List<ParticipationRequest> findAllByRequesterId(Long requesterId);
}
