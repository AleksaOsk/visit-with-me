package ru.practicum.service.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.service.comment.entity.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findByIdAndAuthorId(Long id, Long authorId);

    List<Comment> findAllByEventId(Long eventId);

    List<Comment> findAllByCompilationId(Long compilationId);
}
