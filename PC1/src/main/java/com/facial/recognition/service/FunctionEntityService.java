package com.facial.recognition.service;

import com.facial.recognition.pojo.FunctionEntity;

import java.util.List;
import java.util.Optional;

public interface FunctionEntityService {
    
    // 创建功能
    FunctionEntity createFunction(FunctionEntity functionEntity);
    
    // 根据ID查找功能
    Optional<FunctionEntity> findById(Integer functionId);
    
    // 根据功能路径查找功能
    Optional<FunctionEntity> findByFunctionPath(String functionPath);
    
    // 获取所有功能
    List<FunctionEntity> findAll();
    
    // 根据状态查找功能
    List<FunctionEntity> findByStatus(String status);
    
    // 查找活跃状态的功能
    List<FunctionEntity> findActiveFunctions();
    
    // 根据子系统查找功能
    List<FunctionEntity> findBySubsystem(String subsystem);
    
    // 根据功能名称模糊查询
    List<FunctionEntity> findByFunctionNameContaining(String functionName);
    
    // 根据功能路径模糊查询
    List<FunctionEntity> findByFunctionPathContaining(String functionPath);
    
    // 根据子系统和状态查找功能
    List<FunctionEntity> findBySubsystemAndStatus(String subsystem, String status);
    
    // 更新功能信息
    FunctionEntity updateFunction(Integer functionId, FunctionEntity functionEntity);
    
    // 更新功能状态
    FunctionEntity updateStatus(Integer functionId, String status);
    
    // 删除功能
    void deleteFunction(Integer functionId);
    
    // 根据用户ID查找用户有权限的功能
    List<FunctionEntity> findFunctionsByUserId(Integer userId);
    
    // 根据角色ID查找角色有权限的功能
    List<FunctionEntity> findFunctionsByRoleId(Integer roleId);
    
    // 统计活跃功能数量
    Long countActiveFunctions();
    
    // 根据子系统统计功能数量
    Long countBySubsystem(String subsystem);
    
    // 检查功能路径是否存在
    boolean existsByFunctionPath(String functionPath);
}

