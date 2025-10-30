package com.xitong.springboot_xitong.repository;

import com.xitong.springboot_xitong.pojo.FunctionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FunctionEntityRepository extends JpaRepository<FunctionEntity, Integer> {
    
    // 根据功能名称查找功能
    List<FunctionEntity> findByFunctionName(String functionName);
    
    // 根据功能路径查找功能
    Optional<FunctionEntity> findByFunctionPath(String functionPath);
    
    // 根据所属子系统查找功能
    List<FunctionEntity> findBySubsystem(String subsystem);
    
    // 根据状态查找功能
    List<FunctionEntity> findByStatus(String status);
    
    // 根据功能名称模糊查询
    List<FunctionEntity> findByFunctionNameContaining(String functionName);
    
    // 根据功能路径模糊查询
    List<FunctionEntity> findByFunctionPathContaining(String functionPath);
    
    // 根据所属子系统和状态查找功能
    List<FunctionEntity> findBySubsystemAndStatus(String subsystem, String status);
    
    // 根据功能名称和所属子系统查找功能
    List<FunctionEntity> findByFunctionNameAndSubsystem(String functionName, String subsystem);
    
    // 自定义查询：查找用户有权限的功能
    @Query("SELECT DISTINCT fe FROM FunctionEntity fe " +
           "JOIN RoleFunctionPermission rfp ON fe.functionId = rfp.functionId " +
           "JOIN Role r ON rfp.roleId = r.roleId " +
           "JOIN User u ON r.roleId = u.roleID " +
           "WHERE u.userID = :userId AND rfp.granted = true AND fe.status = 'ACTIVE'")
    List<FunctionEntity> findFunctionsByUserId(@Param("userId") Integer userId);
    
    // 自定义查询：查找角色的功能权限
    @Query("SELECT fe FROM FunctionEntity fe " +
           "JOIN RoleFunctionPermission rfp ON fe.functionId = rfp.functionId " +
           "WHERE rfp.roleId = :roleId AND rfp.granted = true AND fe.status = 'ACTIVE'")
    List<FunctionEntity> findFunctionsByRoleId(@Param("roleId") Integer roleId);
    
    // 自定义查询：根据子系统统计功能数量
    @Query("SELECT COUNT(fe) FROM FunctionEntity fe WHERE fe.subsystem = :subsystem AND fe.status = 'ACTIVE'")
    Long countBySubsystem(@Param("subsystem") String subsystem);
}