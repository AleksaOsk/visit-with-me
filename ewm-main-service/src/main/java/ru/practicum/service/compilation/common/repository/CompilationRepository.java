package ru.practicum.service.compilation.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.service.compilation.common.entity.Compilation;

import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    @Query(value = "SELECT c.* FROM compilations c WHERE c.pinned = ?1 LIMIT ?2 OFFSET ?3", nativeQuery = true)
    List<Compilation> findAllByParamsWithPinned(Boolean pinned, int limit, int offset);

    @Query(value = "SELECT c.* FROM compilations c LIMIT ?1 OFFSET ?2", nativeQuery = true)
    List<Compilation> findAllByParams(int limit, int offset);
}
