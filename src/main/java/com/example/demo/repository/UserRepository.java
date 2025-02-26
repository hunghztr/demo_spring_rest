package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User save(User user);

    Page<User> findAll(Specification<User> spec, Pageable pageable);

    Optional<User> findById(long id);

    User deleteById(long id);

    Optional<User> findByName(String name);

    long count();

    boolean existsByName(String name);
}
