package com.xitong.springboot_xitong.repository;

import com.xitong.springboot_xitong.pojo.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    
    // 根据角色名称查找角色
    Optional<Role> findByRoleName(String roleName);
    
    // 根据状态查找角色
    List<Role> findByStatus(String status);
    
    // 查找活跃状态的角色
    List<Role> findByStatusOrderByRoleNameAsc(String status);
    
    // 根据角色名称模糊查询
    List<Role> findByRoleNameContaining(String roleName);
    
    // 根据描述模糊查询
    List<Role> findByDescriptionContaining(String description);
    
    // 自定义查询：查找指定用户ID的角色
    @Query("SELECT r FROM Role r JOIN User u ON r.roleId = u.roleID WHERE u.userID = :userId")
    Optional<Role> findRoleByUserId(@Param("userId") Integer userId);
    
    // 统计角色数量
    @Query("SELECT COUNT(r) FROM Role r WHERE r.status = 'ACTIVE'")
    Long countActiveRoles();
}
