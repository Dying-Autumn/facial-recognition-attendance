package com.xitong.springboot_xitong.repository;

import com.xitong.springboot_xitong.pojo.Function;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FunctionRepository extends JpaRepository<Function, Integer> {
    
    // 根据功能代码查找功能
    Optional<Function> findByFunctionCode(String functionCode);
    
    // 根据功能名称查找功能
    List<Function> findByFunctionName(String functionName);
    
    // 根据状态查找功能
    List<Function> findByStatus(String status);
    
    // 根据父功能ID查找子功能
    List<Function> findByParentId(Integer parentId);
    
    // 查找顶级功能（没有父功能的功能）
    List<Function> findByParentIdIsNull();
    
    // 根据URL查找功能
    List<Function> findByUrl(String url);
    
    // 根据HTTP方法查找功能
    List<Function> findByMethod(String method);
    
    // 根据功能名称模糊查询
    List<Function> findByFunctionNameContaining(String functionName);
    
    // 根据状态和父功能ID查找功能
    List<Function> findByStatusAndParentId(String status, Integer parentId);
    
    // 按排序顺序查找功能
    List<Function> findByStatusOrderBySortOrderAsc(String status);
    
    // 自定义查询：查找用户有权限的功能
    @Query("SELECT DISTINCT fe FROM FunctionEntity fe " +
           "JOIN RoleFunctionPermission rfp ON fe.functionId = rfp.functionId " +
           "JOIN Role r ON rfp.roleId = r.roleId " +
           "JOIN User u ON r.roleId = u.roleID " +
           "WHERE u.userID = :userId AND rfp.granted = true AND fe.status = 'ACTIVE'")
    List<Function> findFunctionsByUserId(@Param("userId") Integer userId);
    
    // 自定义查询：查找角色的功能权限
    @Query("SELECT fe FROM FunctionEntity fe " +
           "JOIN RoleFunctionPermission rfp ON fe.functionId = rfp.functionId " +
           "WHERE rfp.roleId = :roleId AND rfp.granted = true AND fe.status = 'ACTIVE'")
    List<Function> findFunctionsByRoleId(@Param("roleId") Integer roleId);
}
