package com.facial.recognition.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.facial.recognition.pojo.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    // 根据用户名查找用户
    Optional<User> findByUserName(String userName);
    
    // 根据角色ID查找用户列表
    @Query("SELECT u FROM User u WHERE u.RoleID = :roleId")
    List<User> findByRoleId(@Param("roleId") Integer roleId);
}
