package ru.practicum.service.user.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.service.user.admin.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE LOWER(u.email) = LOWER(:email)")
    Optional<User> findByEmailIgnoreCase(String email);

    @Query(value = "SELECT u.* FROM users u WHERE u.id IN ?1 LIMIT ?2 OFFSET ?3", nativeQuery = true)
    List<User> findAllWithListOfIds(List<Integer> ids, int limit, int offset);

    @Query(value = "SELECT u.* FROM users u LIMIT ?1 OFFSET ?2", nativeQuery = true)
    List<User> findAllWithOutListOfIds(int limit, int offset);
}
