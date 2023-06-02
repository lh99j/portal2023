package com.example.potalend;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//User를 쓸꺼고 Primary키는 Long이야.
public interface UserDao extends JpaRepository<User, Long> {
    @Override
    Optional<User> findById(Long id);
}
