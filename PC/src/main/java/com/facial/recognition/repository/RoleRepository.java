package com.facial.recognition.repository;

import com.facial.recognition.pojo.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    
    // 閺嶈宓佺憴鎺曞閸氬秶袨閺屻儲澹樼憴鎺曞
    Optional<Role> findByRoleName(String roleName);
    
    // 閺嶈宓侀悩鑸碘偓浣圭叀閹垫崘顫楅敓?
    List<Role> findByStatus(String status);
    
    // 閺屻儲澹樺ú鏄忕┈閻樿埖鈧胶娈戠憴鎺曞
    List<Role> findByStatusOrderByRoleNameAsc(String status);
    
    // 閺嶈宓佺憴鎺曞閸氬秶袨濡紕纭﹂弻銉嚄
    List<Role> findByRoleNameContaining(String roleName);
    
    // 閺嶈宓侀幓蹇氬牚濡紕纭﹂弻銉嚄
    List<Role> findByDescriptionContaining(String description);
    
    // 閼奉亜鐣炬稊澶嬬叀鐠囶澁绱伴弻銉﹀閹稿洤鐣鹃悽銊﹀煕ID閻ㄥ嫯顫楅敓?
    @Query("SELECT r FROM Role r JOIN User u ON r.roleId = u.RoleID WHERE u.UserID = :userId")
    Optional<Role> findRoleByUserId(@Param("userId") Integer userId);
    
    // 缂佺喕顓哥憴鎺曞閺佷即鍣?
    @Query("SELECT COUNT(r) FROM Role r WHERE r.status = 'ACTIVE'")
    Long countActiveRoles();
}




