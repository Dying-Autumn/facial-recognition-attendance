package com.facial.recognition.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.facial.recognition.pojo.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    // 根据用户名查找用户
    Optional<User> findByUserName(String userName);
}
