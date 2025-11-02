package com.facial.recognition.repository;

import com.facial.recognition.pojo.Function;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FunctionRepository extends JpaRepository<Function, Integer> {
    
    // 鏍规嵁鍔熻兘浠ｇ爜鏌ユ壘鍔熻兘
    Optional<Function> findByFunctionCode(String functionCode);
    
    // 鏍规嵁鍔熻兘鍚嶇О鏌ユ壘鍔熻兘
    List<Function> findByFunctionName(String functionName);
    
    // 鏍规嵁鐘舵€佹煡鎵惧姛锟?
    List<Function> findByStatus(String status);
    
    // 鏍规嵁鐖跺姛鑳絀D鏌ユ壘瀛愬姛锟?
    List<Function> findByParentId(Integer parentId);
    
    // 鏌ユ壘椤剁骇鍔熻兘锛堟病鏈夌埗鍔熻兘鐨勫姛鑳斤級
    List<Function> findByParentIdIsNull();
    
    // 鏍规嵁URL鏌ユ壘鍔熻兘
    List<Function> findByUrl(String url);
    
    // 鏍规嵁HTTP鏂规硶鏌ユ壘鍔熻兘
    List<Function> findByMethod(String method);
    
    // 鏍规嵁鍔熻兘鍚嶇О妯＄硦鏌ヨ
    List<Function> findByFunctionNameContaining(String functionName);
    
    // 鏍规嵁鐘舵€佸拰鐖跺姛鑳絀D鏌ユ壘鍔熻兘
    List<Function> findByStatusAndParentId(String status, Integer parentId);
    
    // 鎸夋帓搴忛『搴忔煡鎵惧姛锟?
    List<Function> findByStatusOrderBySortOrderAsc(String status);
    
    // 鑷畾涔夋煡璇細鏌ユ壘鐢ㄦ埛鏈夋潈闄愮殑鍔熻兘
    @Query("SELECT DISTINCT fe FROM FunctionEntity fe " +
           "JOIN RoleFunctionPermission rfp ON fe.functionId = rfp.functionId " +
           "JOIN Role r ON rfp.roleId = r.roleId " +
           "JOIN User u ON r.roleId = u.RoleID " +
           "WHERE u.UserID = :userId AND rfp.granted = true AND fe.status = 'ACTIVE'")
    List<Function> findFunctionsByUserId(@Param("userId") Integer userId);
    
    // 鑷畾涔夋煡璇細鏌ユ壘瑙掕壊鐨勫姛鑳芥潈锟?
    @Query("SELECT fe FROM FunctionEntity fe " +
           "JOIN RoleFunctionPermission rfp ON fe.functionId = rfp.functionId " +
           "WHERE rfp.roleId = :roleId AND rfp.granted = true AND fe.status = 'ACTIVE'")
    List<Function> findFunctionsByRoleId(@Param("roleId") Integer roleId);
}
