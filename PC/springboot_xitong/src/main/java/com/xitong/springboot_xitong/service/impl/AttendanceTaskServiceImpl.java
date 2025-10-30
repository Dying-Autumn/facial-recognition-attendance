package com.xitong.springboot_xitong.service.impl;

import com.xitong.springboot_xitong.pojo.AttendanceTask;
import com.xitong.springboot_xitong.repository.AttendanceTaskRepository;
import com.xitong.springboot_xitong.service.AttendanceTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class AttendanceTaskServiceImpl implements AttendanceTaskService {

    @Autowired
    private AttendanceTaskRepository attendanceTaskRepository;

    @Override
    public AttendanceTask createAttendanceTask(AttendanceTask attendanceTask) {
        // 生成二维码
        String qrCode = generateQrCode(attendanceTask.getTaskId());
        attendanceTask.setQrCode(qrCode);
        return attendanceTaskRepository.save(attendanceTask);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AttendanceTask> findById(Long taskId) {
        return attendanceTaskRepository.findById(taskId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttendanceTask> findByCourseClassId(Long courseClassId) {
        return attendanceTaskRepository.findByCourseClassId(courseClassId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttendanceTask> findByTeacherId(Long teacherId) {
        return attendanceTaskRepository.findByTeacherId(teacherId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttendanceTask> findByStatus(String status) {
        return attendanceTaskRepository.findByStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttendanceTask> findByCourseClassIdAndStatus(Long courseClassId, String status) {
        return attendanceTaskRepository.findByCourseClassIdAndStatus(courseClassId, status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttendanceTask> findByTeacherIdAndStatus(Long teacherId, String status) {
        return attendanceTaskRepository.findByTeacherIdAndStatus(teacherId, status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttendanceTask> findByTaskNameContaining(String taskName) {
        return attendanceTaskRepository.findByTaskNameContaining(taskName);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttendanceTask> findTasksInTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        return attendanceTaskRepository.findTasksInTimeRange(startTime, endTime);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttendanceTask> findActiveTasksAtTime(LocalDateTime currentTime) {
        return attendanceTaskRepository.findActiveTasksAtTime(currentTime);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AttendanceTask> findByQrCode(String qrCode) {
        return attendanceTaskRepository.findByQrCode(qrCode);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttendanceTask> findUpcomingTasks(LocalDateTime currentTime) {
        return attendanceTaskRepository.findUpcomingTasks(currentTime);
    }

    @Override
    public AttendanceTask updateAttendanceTask(Long taskId, AttendanceTask attendanceTask) {
        Optional<AttendanceTask> existingTask = attendanceTaskRepository.findById(taskId);
        if (existingTask.isPresent()) {
            AttendanceTask updatedTask = existingTask.get();
            updatedTask.setTaskName(attendanceTask.getTaskName());
            updatedTask.setDescription(attendanceTask.getDescription());
            updatedTask.setStartTime(attendanceTask.getStartTime());
            updatedTask.setEndTime(attendanceTask.getEndTime());
            updatedTask.setLocation(attendanceTask.getLocation());
            updatedTask.setStatus(attendanceTask.getStatus());
            return attendanceTaskRepository.save(updatedTask);
        }
        throw new RuntimeException("AttendanceTask not found with id: " + taskId);
    }

    @Override
    public AttendanceTask updateStatus(Long taskId, String status) {
        Optional<AttendanceTask> task = attendanceTaskRepository.findById(taskId);
        if (task.isPresent()) {
            task.get().setStatus(status);
            return attendanceTaskRepository.save(task.get());
        }
        throw new RuntimeException("AttendanceTask not found with id: " + taskId);
    }

    @Override
    public void deleteAttendanceTask(Long taskId) {
        attendanceTaskRepository.deleteById(taskId);
    }

    @Override
    public String generateQrCode(Long taskId) {
        // 生成唯一的二维码内容
        String qrContent = "ATTENDANCE_TASK_" + taskId + "_" + UUID.randomUUID().toString();
        return qrContent;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isTaskActive(Long taskId, LocalDateTime currentTime) {
        Optional<AttendanceTask> task = attendanceTaskRepository.findById(taskId);
        if (task.isPresent()) {
            AttendanceTask attendanceTask = task.get();
            return "ACTIVE".equals(attendanceTask.getStatus()) &&
                   currentTime.isAfter(attendanceTask.getStartTime()) &&
                   currentTime.isBefore(attendanceTask.getEndTime());
        }
        return false;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isTaskExpired(Long taskId, LocalDateTime currentTime) {
        Optional<AttendanceTask> task = attendanceTaskRepository.findById(taskId);
        if (task.isPresent()) {
            return currentTime.isAfter(task.get().getEndTime());
        }
        return true;
    }
}
