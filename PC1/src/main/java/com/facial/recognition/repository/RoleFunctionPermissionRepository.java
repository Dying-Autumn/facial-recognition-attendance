package com.facial.recognition.repository;

import com.facial.recognition.pojo.RoleFunctionPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleFunctionPermissionRepository extends JpaRepository<RoleFunctionPermission, Long> {
    
    // 閺嶈宓佺憴鎺曞ID閺屻儲澹橀弶鍐
    List<RoleFunctionPermission> findByRoleId(Integer roleId);
    
    // 閺嶈宓侀崝鐔诲厴ID閺屻儲澹橀弶鍐
    List<RoleFunctionPermission> findByFunctionId(Integer functionId);
    
    // 閺嶈宓佺憴鎺曞ID閸滃苯濮涢懗绲€D閺屻儲澹橀弶鍐
    Optional<RoleFunctionPermission> findByRoleIdAndFunctionId(Integer roleId, Integer functionId);
    
    // 閺嶈宓侀弶鍐缁鐎烽弻銉﹀閺夊啴妾?
    List<RoleFunctionPermission> findByPermissionType(String permissionType);
    
    // 閺嶈宓侀幒鍫熸綀閻樿埖鈧焦鐓￠幍鐐綀閿?
    List<RoleFunctionPermission> findByGranted(Boolean granted);
    
    // 閺嶈宓佺憴鎺曞ID閸滃本宸块弶鍐Ц閹焦鐓￠幍鐐綀閿?
    List<RoleFunctionPermission> findByRoleIdAndGranted(Integer roleId, Boolean granted);
    
    // 閺嶈宓侀崝鐔诲厴ID閸滃本宸块弶鍐Ц閹焦鐓￠幍鐐綀閿?
    List<RoleFunctionPermission> findByFunctionIdAndGranted(Integer functionId, Boolean granted);
    
    // 閺嶈宓佺憴鎺曞ID閵嗕礁濮涢懗绲€D閸滃本娼堥梽鎰閸ㄥ鐓￠幍鐐綀閿?
    Optional<RoleFunctionPermission> findByRoleIdAndFunctionIdAndPermissionType(
        Integer roleId, Integer functionId, String permissionType);
    
    // 閼奉亜鐣炬稊澶嬬叀鐠囶澁绱板Λ鈧弻銉ф暏閹撮攱妲搁崥锔芥箒閻楃懓鐣鹃崝鐔诲厴閻ㄥ嫭娼堥敓?
    @Query("SELECT rfp FROM RoleFunctionPermission rfp " +
           "JOIN User u ON rfp.roleId = u.RoleID " +
           "WHERE u.UserID = :userId AND rfp.functionId = :functionId AND rfp.granted = true")
    List<RoleFunctionPermission> findUserPermissionsForFunction(
        @Param("userId") Integer userId, @Param("functionId") Integer functionId);
    
    // 閼奉亜鐣炬稊澶嬬叀鐠囶澁绱伴懢宄板絿鐟欐帟澹婇惃鍕閺堝娼堥敓?
    @Query("SELECT rfp FROM RoleFunctionPermission rfp " +
           "WHERE rfp.roleId = :roleId AND rfp.granted = true")
    List<RoleFunctionPermission> findGrantedPermissionsByRoleId(@Param("roleId") Integer roleId);
    
    // 閼奉亜鐣炬稊澶嬬叀鐠囶澁绱伴懢宄板絿閸旂喕鍏橀惃鍕閺堝娼堥梽鎰瀻閿?
    @Query("SELECT rfp FROM RoleFunctionPermission rfp " +
           "WHERE rfp.functionId = :functionId")
    List<RoleFunctionPermission> findAllPermissionsForFunction(@Param("functionId") Integer functionId);
    
    // 閸掔娀娅庣憴鎺曞閻ㄥ嫭澧嶉張澶嬫綀閿?
    void deleteByRoleId(Integer roleId);
    
    // 閸掔娀娅庨崝鐔诲厴閻ㄥ嫭澧嶉張澶嬫綀閿?
    void deleteByFunctionId(Integer functionId);
}
