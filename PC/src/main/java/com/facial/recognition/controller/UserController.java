package com.facial.recognition.controller;

import com.facial.recognition.pojo.User;
import com.facial.recognition.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        if (id == null || id <= 0) {
            return ResponseEntity.badRequest().build();
        }
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
        if (id == null || id <= 0) {
            return ResponseEntity.badRequest().build();
        }
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
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable Integer id) {
        if (id == null || id <= 0) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "无效的用户ID");
            return ResponseEntity.badRequest().body(response);
        }
        
        return userService.findById(id)
                .map(user -> {
                    try {
                        userService.delete(id);
                        Map<String, Object> response = new HashMap<>();
                        response.put("success", true);
                        response.put("message", "用户删除成功");
                        return ResponseEntity.ok(response);
                    } catch (IllegalStateException e) {
                        Map<String, Object> response = new HashMap<>();
                        response.put("success", false);
                        response.put("message", e.getMessage());
                        return ResponseEntity.status(409).body(response); // Conflict
                    } catch (Exception e) {
                        Map<String, Object> response = new HashMap<>();
                        response.put("success", false);
                        response.put("message", "删除失败: " + e.getMessage());
                        return ResponseEntity.status(500).body(response);
                    }
                })
                .orElseGet(() -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", false);
                    response.put("message", "用户不存在");
                    return ResponseEntity.status(404).body(response);
                });
    }
}

