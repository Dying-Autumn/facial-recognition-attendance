package com.facial.recognition.service;

import com.facial.recognition.pojo.RoleFunctionPermission;

import java.util.List;
import java.util.Optional;

public interface RoleFunctionPermissionService {
    
    // 创建权限
    RoleFunctionPermission createPermission(RoleFunctionPermission permission);
    
    // 根据角色ID和功能ID查找权限
    Optional<RoleFunctionPermission> findByRoleIdAndFunctionId(Integer roleId, Integer functionId);
    
    // 根据角色ID查找所有权限
    List<RoleFunctionPermission> findByRoleId(Integer roleId);
    
    // 根据功能ID查找所有权限
    List<RoleFunctionPermission> findByFunctionId(Integer functionId);
    
    // 根据权限类型查找权限
    List<RoleFunctionPermission> findByPermissionType(String permissionType);
    
    // 根据授权状态查找权限
    List<RoleFunctionPermission> findByGranted(Boolean granted);
    
    // 根据角色ID和授权状态查找权限
    List<RoleFunctionPermission> findByRoleIdAndGranted(Integer roleId, Boolean granted);
    
    // 根据功能ID和授权状态查找权限
    List<RoleFunctionPermission> findByFunctionIdAndGranted(Integer functionId, Boolean granted);
    
    // 更新权限信息
    RoleFunctionPermission updatePermission(Integer roleId, Integer functionId, RoleFunctionPermission permission);
    
    // 更新权限授权状态
    RoleFunctionPermission updateGranted(Integer roleId, Integer functionId, Boolean granted);
    
    // 删除权限
    void deletePermission(Integer roleId, Integer functionId);
    
    // 删除角色的所有权限
    void deleteByRoleId(Integer roleId);
    
    // 删除功能的所有权限
    void deleteByFunctionId(Integer functionId);
    
    // 批量创建权限
    List<RoleFunctionPermission> batchCreatePermissions(List<RoleFunctionPermission> permissions);
    
    // 批量更新权限
    List<RoleFunctionPermission> batchUpdatePermissions(Integer roleId, List<RoleFunctionPermission> permissions);
    
    // 根据用户ID和功能ID查找用户权限
    List<RoleFunctionPermission> findUserPermissionsForFunction(Integer userId, Integer functionId);
    
    // 根据角色ID查找已授权的权限
    List<RoleFunctionPermission> findGrantedPermissionsByRoleId(Integer roleId);
}

