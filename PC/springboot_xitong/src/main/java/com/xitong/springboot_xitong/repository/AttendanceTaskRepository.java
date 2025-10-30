package com.xitong.springboot_xitong.repository;

import com.xitong.springboot_xitong.pojo.AttendanceTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceTaskRepository extends JpaRepository<AttendanceTask, Long> {
    
    // 根据班级ID查找考勤任务
    List<AttendanceTask> findByCourseClassId(Long courseClassId);
    
    // 根据教师ID查找考勤任务
    List<AttendanceTask> findByTeacherId(Long teacherId);
    
    // 根据状态查找考勤任务
    List<AttendanceTask> findByStatus(String status);
    
    // 根据班级ID和状态查找考勤任务
    List<AttendanceTask> findByCourseClassIdAndStatus(Long courseClassId, String status);
    
    // 根据教师ID和状态查找考勤任务
    List<AttendanceTask> findByTeacherIdAndStatus(Long teacherId, String status);
    
    // 根据任务名称查找考勤任务
    List<AttendanceTask> findByTaskNameContaining(String taskName);
    
    // 查找指定时间范围内的考勤任务
    @Query("SELECT at FROM AttendanceTask at WHERE at.startTime <= :endTime AND at.endTime >= :startTime")
    List<AttendanceTask> findTasksInTimeRange(@Param("startTime") LocalDateTime startTime, 
                                            @Param("endTime") LocalDateTime endTime);
    
    // 查找当前活跃的考勤任务
    @Query("SELECT at FROM AttendanceTask at WHERE at.status = 'ACTIVE' AND at.startTime <= :currentTime AND at.endTime >= :currentTime")
    List<AttendanceTask> findActiveTasksAtTime(@Param("currentTime") LocalDateTime currentTime);
    
    // 根据二维码查找考勤任务
    Optional<AttendanceTask> findByQrCode(String qrCode);
    
    // 查找即将开始的考勤任务
    @Query("SELECT at FROM AttendanceTask at WHERE at.status = 'ACTIVE' AND at.startTime > :currentTime ORDER BY at.startTime ASC")
    List<AttendanceTask> findUpcomingTasks(@Param("currentTime") LocalDateTime currentTime);
}
