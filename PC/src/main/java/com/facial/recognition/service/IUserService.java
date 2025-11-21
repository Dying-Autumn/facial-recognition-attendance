package com.facial.recognition.service;

import com.facial.recognition.pojo.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    // 插入用户
    User add(User user);
    
    // 获取所有用户
    List<User> findAll();
    
    // 根据ID获取用户
    Optional<User> findById(Integer id);
    
    // 根据用户名获取用户
    Optional<User> findByUsername(String username);
    
    // 保存用户（新增或更新）
    User save(User user);
    
    // 删除用户
    void delete(Integer id);
}
