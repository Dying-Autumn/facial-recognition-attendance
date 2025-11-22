package com.facial.recognition.controller;

import com.facial.recognition.pojo.RoleFunctionPermission;
import com.facial.recognition.service.RoleFunctionPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/role-function-permissions")
@CrossOrigin(origins = "*")
public class RoleFunctionPermissionController {

    @Autowired
    private RoleFunctionPermissionService permissionService;

    // 创建权限
    @PostMapping
    public ResponseEntity<RoleFunctionPermission> createPermission(@RequestBody RoleFunctionPermission permission) {
        try {
            RoleFunctionPermission createdPermission = permissionService.createPermission(permission);
            return ResponseEntity.ok(createdPermission);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 根据角色ID和功能ID获取权限
    @GetMapping("/role/{roleId}/function/{functionId}")
    public ResponseEntity<RoleFunctionPermission> getPermission(
            @PathVariable Integer roleId, 
            @PathVariable Integer functionId) {
        Optional<RoleFunctionPermission> permission = permissionService.findByRoleIdAndFunctionId(roleId, functionId);
        return permission.map(ResponseEntity::ok)
                        .orElse(ResponseEntity.notFound().build());
    }

    // 根据角色ID获取所有权限
    @GetMapping("/role/{roleId}")
    public ResponseEntity<List<RoleFunctionPermission>> getPermissionsByRoleId(@PathVariable Integer roleId) {
        List<RoleFunctionPermission> permissions = permissionService.findByRoleId(roleId);
        return ResponseEntity.ok(permissions);
    }

    // 根据功能ID获取所有权限
    @GetMapping("/function/{functionId}")
    public ResponseEntity<List<RoleFunctionPermission>> getPermissionsByFunctionId(@PathVariable Integer functionId) {
        List<RoleFunctionPermission> permissions = permissionService.findByFunctionId(functionId);
        return ResponseEntity.ok(permissions);
    }

    // 根据权限类型获取权限
    @GetMapping("/type/{permissionType}")
    public ResponseEntity<List<RoleFunctionPermission>> getPermissionsByType(@PathVariable String permissionType) {
        List<RoleFunctionPermission> permissions = permissionService.findByPermissionType(permissionType);
        return ResponseEntity.ok(permissions);
    }

    // 根据授权状态获取权限
    @GetMapping("/granted/{granted}")
    public ResponseEntity<List<RoleFunctionPermission>> getPermissionsByGranted(@PathVariable Boolean granted) {
        List<RoleFunctionPermission> permissions = permissionService.findByGranted(granted);
        return ResponseEntity.ok(permissions);
    }

    // 根据角色ID和授权状态获取权限
    @GetMapping("/role/{roleId}/granted/{granted}")
    public ResponseEntity<List<RoleFunctionPermission>> getPermissionsByRoleIdAndGranted(
            @PathVariable Integer roleId, 
            @PathVariable Boolean granted) {
        List<RoleFunctionPermission> permissions = permissionService.findByRoleIdAndGranted(roleId, granted);
        return ResponseEntity.ok(permissions);
    }

    // 更新权限信息
    @PutMapping("/role/{roleId}/function/{functionId}")
    public ResponseEntity<RoleFunctionPermission> updatePermission(
            @PathVariable Integer roleId,
            @PathVariable Integer functionId,
            @RequestBody RoleFunctionPermission permission) {
        try {
            RoleFunctionPermission updatedPermission = permissionService.updatePermission(roleId, functionId, permission);
            return ResponseEntity.ok(updatedPermission);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 更新权限授权状态
    @PatchMapping("/role/{roleId}/function/{functionId}/granted")
    public ResponseEntity<RoleFunctionPermission> updateGranted(
            @PathVariable Integer roleId,
            @PathVariable Integer functionId,
            @RequestParam Boolean granted) {
        try {
            RoleFunctionPermission permission = permissionService.updateGranted(roleId, functionId, granted);
            return ResponseEntity.ok(permission);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 删除权限
    @DeleteMapping("/role/{roleId}/function/{functionId}")
    public ResponseEntity<Void> deletePermission(
            @PathVariable Integer roleId,
            @PathVariable Integer functionId) {
        try {
            permissionService.deletePermission(roleId, functionId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 删除角色的所有权限
    @DeleteMapping("/role/{roleId}")
    public ResponseEntity<Void> deletePermissionsByRoleId(@PathVariable Integer roleId) {
        try {
            permissionService.deleteByRoleId(roleId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 删除功能的所有权限
    @DeleteMapping("/function/{functionId}")
    public ResponseEntity<Void> deletePermissionsByFunctionId(@PathVariable Integer functionId) {
        try {
            permissionService.deleteByFunctionId(functionId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 批量创建权限
    @PostMapping("/batch")
    public ResponseEntity<List<RoleFunctionPermission>> batchCreatePermissions(@RequestBody List<RoleFunctionPermission> permissions) {
        try {
            List<RoleFunctionPermission> createdPermissions = permissionService.batchCreatePermissions(permissions);
            return ResponseEntity.ok(createdPermissions);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 批量更新权限（为角色批量分配权限）
    @PutMapping("/role/{roleId}/batch")
    public ResponseEntity<List<RoleFunctionPermission>> batchUpdatePermissions(
            @PathVariable Integer roleId,
            @RequestBody List<RoleFunctionPermission> permissions) {
        try {
            List<RoleFunctionPermission> updatedPermissions = permissionService.batchUpdatePermissions(roleId, permissions);
            return ResponseEntity.ok(updatedPermissions);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 根据用户ID和功能ID获取用户权限
    @GetMapping("/user/{userId}/function/{functionId}")
    public ResponseEntity<List<RoleFunctionPermission>> getUserPermissionsForFunction(
            @PathVariable Integer userId,
            @PathVariable Integer functionId) {
        List<RoleFunctionPermission> permissions = permissionService.findUserPermissionsForFunction(userId, functionId);
        return ResponseEntity.ok(permissions);
    }

    // 根据角色ID获取已授权的权限
    @GetMapping("/role/{roleId}/granted")
    public ResponseEntity<List<RoleFunctionPermission>> getGrantedPermissionsByRoleId(@PathVariable Integer roleId) {
        List<RoleFunctionPermission> permissions = permissionService.findGrantedPermissionsByRoleId(roleId);
        return ResponseEntity.ok(permissions);
    }
}

