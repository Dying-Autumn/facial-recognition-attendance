package com.facial.recognition.controller;

import com.facial.recognition.pojo.User;
import com.facial.recognition.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {
    
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    
    @Autowired
    private IUserService userService;
    
    // 获取所有用户
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAll();
        return ResponseEntity.ok(users);
    }
    
    // 根据ID获取用户
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        return userService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    // 根据用户名获取用户
    @GetMapping("/username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        return userService.findByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 登录接口
    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody com.facial.recognition.dto.LoginDTO loginDTO) {
        logger.info("收到登录请求: 用户名 [{}]", loginDTO.getUsername());
        
        return userService.findByUsername(loginDTO.getUsername())
                .map(user -> {
                    // 注意：生产环境不建议在日志中打印密码，这里仅为了调试保留部分脱敏信息或不打印
                    logger.info("数据库找到用户: ID={}", user.getUserID());
                    if (user.getPassword().equals(loginDTO.getPassword())) {
                        logger.info("密码匹配成功！用户: {}", user.getUserName());
                        return ResponseEntity.ok(user);
                    } else {
                        logger.warn("密码不匹配！尝试登录用户: {}", loginDTO.getUsername());
                        return ResponseEntity.status(401).<User>build();
                    }
                })
                .orElseGet(() -> {
                    logger.warn("数据库未找到用户: {}", loginDTO.getUsername());
                    return ResponseEntity.notFound().build();
                });
    }
    
    // 创建用户
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        try {
            User savedUser = userService.save(user);
            return ResponseEntity.ok(savedUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // 更新用户
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Integer id, @RequestBody User user) {
        return userService.findById(id)
                .map(existingUser -> {
                    user.setUserID(id);
                    User updatedUser = userService.save(user);
                    return ResponseEntity.ok(updatedUser);
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    // 删除用户
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        return userService.findById(id)
                .map(user -> {
                    userService.delete(id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    // 为用户分配角色
    @PatchMapping("/{id}/role")
    public ResponseEntity<User> assignRole(@PathVariable Integer id, @RequestParam Integer roleId) {
        return userService.findById(id)
                .map(user -> {
                    user.setRoleID(roleId);
                    User updatedUser = userService.save(user);
                    return ResponseEntity.ok(updatedUser);
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    // 根据角色ID获取用户列表
    @GetMapping("/role/{roleId}")
    public ResponseEntity<List<User>> getUsersByRoleId(@PathVariable Integer roleId) {
        List<User> users = userService.findAll().stream()
                .filter(user -> user.getRoleID() != null && user.getRoleID().equals(roleId))
                .toList();
        return ResponseEntity.ok(users);
    }
}

