package ru.practicum.service.category.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.service.category.common.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT c FROM Category c WHERE LOWER(c.name) = LOWER(:name)")
    Optional<Category> findByNameIgnoreCase(String name);

    @Query(value = "SELECT c.* FROM categories c LIMIT ?1 OFFSET ?2", nativeQuery = true)
    List<Category> findAllWithLimitAndOffset(int limit, int offset);
}
