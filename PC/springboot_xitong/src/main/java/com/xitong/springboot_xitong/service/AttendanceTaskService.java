package com.xitong.springboot_xitong.service;

import com.xitong.springboot_xitong.pojo.AttendanceTask;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AttendanceTaskService {
    
    // 创建考勤任务
    AttendanceTask createAttendanceTask(AttendanceTask attendanceTask);
    
    // 根据ID查找考勤任务
    Optional<AttendanceTask> findById(Long taskId);
    
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
    List<AttendanceTask> findTasksInTimeRange(LocalDateTime startTime, LocalDateTime endTime);
    
    // 查找当前活跃的考勤任务
    List<AttendanceTask> findActiveTasksAtTime(LocalDateTime currentTime);
    
    // 根据二维码查找考勤任务
    Optional<AttendanceTask> findByQrCode(String qrCode);
    
    // 查找即将开始的考勤任务
    List<AttendanceTask> findUpcomingTasks(LocalDateTime currentTime);
    
    // 更新考勤任务
    AttendanceTask updateAttendanceTask(Long taskId, AttendanceTask attendanceTask);
    
    // 更新考勤任务状态
    AttendanceTask updateStatus(Long taskId, String status);
    
    // 删除考勤任务
    void deleteAttendanceTask(Long taskId);
    
    // 生成二维码
    String generateQrCode(Long taskId);
    
    // 检查考勤任务是否正在进行
    boolean isTaskActive(Long taskId, LocalDateTime currentTime);
    
    // 检查考勤任务是否已过期
    boolean isTaskExpired(Long taskId, LocalDateTime currentTime);
}
