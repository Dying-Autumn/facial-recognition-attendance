package com.xitong.springboot_xitong.repository;

import com.xitong.springboot_xitong.pojo.RoleFunctionPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleFunctionPermissionRepository extends JpaRepository<RoleFunctionPermission, Long> {
    
    // 根据角色ID查找权限
    List<RoleFunctionPermission> findByRoleId(Integer roleId);
    
    // 根据功能ID查找权限
    List<RoleFunctionPermission> findByFunctionId(Integer functionId);
    
    // 根据角色ID和功能ID查找权限
    Optional<RoleFunctionPermission> findByRoleIdAndFunctionId(Integer roleId, Integer functionId);
    
    // 根据权限类型查找权限
    List<RoleFunctionPermission> findByPermissionType(String permissionType);
    
    // 根据授权状态查找权限
    List<RoleFunctionPermission> findByGranted(Boolean granted);
    
    // 根据角色ID和授权状态查找权限
    List<RoleFunctionPermission> findByRoleIdAndGranted(Integer roleId, Boolean granted);
    
    // 根据功能ID和授权状态查找权限
    List<RoleFunctionPermission> findByFunctionIdAndGranted(Integer functionId, Boolean granted);
    
    // 根据角色ID、功能ID和权限类型查找权限
    Optional<RoleFunctionPermission> findByRoleIdAndFunctionIdAndPermissionType(
        Integer roleId, Integer functionId, String permissionType);
    
    // 自定义查询：检查用户是否有特定功能的权限
    @Query("SELECT rfp FROM RoleFunctionPermission rfp " +
           "JOIN User u ON rfp.roleId = u.roleID " +
           "WHERE u.userID = :userId AND rfp.functionId = :functionId AND rfp.granted = true")
    List<RoleFunctionPermission> findUserPermissionsForFunction(
        @Param("userId") Integer userId, @Param("functionId") Integer functionId);
    
    // 自定义查询：获取角色的所有权限
    @Query("SELECT rfp FROM RoleFunctionPermission rfp " +
           "WHERE rfp.roleId = :roleId AND rfp.granted = true")
    List<RoleFunctionPermission> findGrantedPermissionsByRoleId(@Param("roleId") Integer roleId);
    
    // 自定义查询：获取功能的所有权限分配
    @Query("SELECT rfp FROM RoleFunctionPermission rfp " +
           "WHERE rfp.functionId = :functionId")
    List<RoleFunctionPermission> findAllPermissionsForFunction(@Param("functionId") Integer functionId);
    
    // 删除角色的所有权限
    void deleteByRoleId(Integer roleId);
    
    // 删除功能的所有权限
    void deleteByFunctionId(Integer functionId);
}
