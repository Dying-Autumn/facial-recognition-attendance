package com.facial.recognition.pojo;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "attendance_tasks")
public class AttendanceTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskId;

    @Column(name = "CourseClassID", nullable = false)
    private Long courseClassId;

    @Column(name = "TeacherID", nullable = false)
    private Long teacherId;

    @Column(nullable = false)
    private String taskName; // 考勤任务名称

    private String description; // 任务描述

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime; // 考勤开始时间

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime; // 考勤结束时间

    @Column(name = "created_time")
    private LocalDateTime createdTime; // 创建时间

    private String status; // 状态：ACTIVE, INACTIVE, COMPLETED

    private String location; // 考勤地点

    private String qrCode; // 二维码内容

    public AttendanceTask() {}

    public AttendanceTask(Long courseClassId, Long teacherId, String taskName, 
                         LocalDateTime startTime, LocalDateTime endTime) {
        this.courseClassId = courseClassId;
        this.teacherId = teacherId;
        this.taskName = taskName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.createdTime = LocalDateTime.now();
        this.status = "ACTIVE";
    }

    // Getters and Setters
    public Long getTaskId() { return taskId; }
    public void setTaskId(Long taskId) { this.taskId = taskId; }

    public Long getCourseClassId() { return courseClassId; }
    public void setCourseClassId(Long courseClassId) { this.courseClassId = courseClassId; }

    public Long getTeacherId() { return teacherId; }
    public void setTeacherId(Long teacherId) { this.teacherId = teacherId; }

    public String getTaskName() { return taskName; }
    public void setTaskName(String taskName) { this.taskName = taskName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public LocalDateTime getCreatedTime() { return createdTime; }
    public void setCreatedTime(LocalDateTime createdTime) { this.createdTime = createdTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getQrCode() { return qrCode; }
    public void setQrCode(String qrCode) { this.qrCode = qrCode; }
}

