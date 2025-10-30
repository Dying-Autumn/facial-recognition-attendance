package com.xitong.springboot_xitong.controller;

import com.xitong.springboot_xitong.pojo.Role;
import com.xitong.springboot_xitong.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/roles")
@CrossOrigin(origins = "*")
public class RoleController {

    @Autowired
    private RoleService roleService;

    // 创建角色
    @PostMapping
    public ResponseEntity<Role> createRole(@RequestBody Role role) {
        try {
            Role createdRole = roleService.createRole(role);
            return ResponseEntity.ok(createdRole);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 根据ID获取角色
    @GetMapping("/{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable Integer id) {
        Optional<Role> role = roleService.findById(id);
        return role.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }

    // 根据角色名称获取角色
    @GetMapping("/name/{roleName}")
    public ResponseEntity<Role> getRoleByName(@PathVariable String roleName) {
        Optional<Role> role = roleService.findByRoleName(roleName);
        return role.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }

    // 获取所有角色
    @GetMapping
    public ResponseEntity<List<Role>> getAllRoles() {
        List<Role> roles = roleService.findAll();
        return ResponseEntity.ok(roles);
    }

    // 根据状态获取角色
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Role>> getRolesByStatus(@PathVariable String status) {
        List<Role> roles = roleService.findByStatus(status);
        return ResponseEntity.ok(roles);
    }

    // 获取活跃状态的角色
    @GetMapping("/active")
    public ResponseEntity<List<Role>> getActiveRoles() {
        List<Role> roles = roleService.findActiveRoles();
        return ResponseEntity.ok(roles);
    }

    // 根据角色名称搜索
    @GetMapping("/search/name")
    public ResponseEntity<List<Role>> searchRolesByName(@RequestParam String roleName) {
        List<Role> roles = roleService.findByRoleNameContaining(roleName);
        return ResponseEntity.ok(roles);
    }

    // 根据描述搜索
    @GetMapping("/search/description")
    public ResponseEntity<List<Role>> searchRolesByDescription(@RequestParam String description) {
        List<Role> roles = roleService.findByDescriptionContaining(description);
        return ResponseEntity.ok(roles);
    }

    // 更新角色信息
    @PutMapping("/{id}")
    public ResponseEntity<Role> updateRole(@PathVariable Integer id, @RequestBody Role role) {
        try {
            Role updatedRole = roleService.updateRole(id, role);
            return ResponseEntity.ok(updatedRole);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 更新角色状态
    @PatchMapping("/{id}/status")
    public ResponseEntity<Role> updateRoleStatus(@PathVariable Integer id, @RequestParam String status) {
        try {
            Role role = roleService.updateStatus(id, status);
            return ResponseEntity.ok(role);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 删除角色
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Integer id) {
        try {
            roleService.deleteRole(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 根据用户ID获取角色
    @GetMapping("/user/{userId}")
    public ResponseEntity<Role> getRoleByUserId(@PathVariable Integer userId) {
        Optional<Role> role = roleService.findRoleByUserId(userId);
        return role.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }

    // 统计活跃角色数量
    @GetMapping("/count/active")
    public ResponseEntity<Long> countActiveRoles() {
        Long count = roleService.countActiveRoles();
        return ResponseEntity.ok(count);
    }

    // 检查角色名称是否存在
    @GetMapping("/exists/{roleName}")
    public ResponseEntity<Boolean> checkRoleNameExists(@PathVariable String roleName) {
        boolean exists = roleService.existsByRoleName(roleName);
        return ResponseEntity.ok(exists);
    }
}
