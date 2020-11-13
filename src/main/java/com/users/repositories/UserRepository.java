package com.users.repositories;

import com.users.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findById(long id);
    Optional<User> findByEmail(String mail);
    User findByHash(String hash);

    @Query(value = "select users.hash from users where users.email = :email", nativeQuery = true)
    String findHashByEmail(@Param("email") String email);

    @Query(value = "select u.email from users u where u.email = :email", nativeQuery = true)
    String findMail(@Param("email") String email);

    Boolean existsByEmail(String email);
}