package com.facial.recognition.pojo;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "AttendanceRecord")
public class AttendanceRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RecordID")
    private Long recordId; // 主键

    @Column(name = "CheckInTime", nullable = false)
    private LocalDateTime checkInTime; // 签到时间

    @Column(name = "ActualLatitude")
    private Double actualLatitude; // 实际签到纬度

    @Column(name = "ActualLongitude")
    private Double actualLongitude; // 实际签到经度

    @Column(name = "VerificationPhotoURL")
    private String verificationPhotoUrl; // 验证照片URL

    @Column(name = "ConfidenceScore")
    private Double confidenceScore; // 置信度分数

    @Column(name = "AttendanceResult", nullable = false)
    private String attendanceResult; // 签到结果

    @Column(name = "TaskID", nullable = false)
    private Long taskId; // 外键

    @Column(name = "StudentID", nullable = false)
    private Long studentId; // 外键

    @Column(name = "Remark")
    private String remark; // 备注

    @Column(name = "CreatedDate")
    private LocalDateTime createdDate; // 创建时间

    public AttendanceRecord() {}

    public AttendanceRecord(LocalDateTime checkInTime, String attendanceResult, Long taskId, Long studentId) {
        this.checkInTime = checkInTime;
        this.attendanceResult = attendanceResult;
        this.taskId = taskId;
        this.studentId = studentId;
        this.createdDate = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getRecordId() { return recordId; }
    public void setRecordId(Long recordId) { this.recordId = recordId; }

    public LocalDateTime getCheckInTime() { return checkInTime; }
    public void setCheckInTime(LocalDateTime checkInTime) { this.checkInTime = checkInTime; }

    public Double getActualLatitude() { return actualLatitude; }
    public void setActualLatitude(Double actualLatitude) { this.actualLatitude = actualLatitude; }

    public Double getActualLongitude() { return actualLongitude; }
    public void setActualLongitude(Double actualLongitude) { this.actualLongitude = actualLongitude; }

    public String getVerificationPhotoUrl() { return verificationPhotoUrl; }
    public void setVerificationPhotoUrl(String verificationPhotoUrl) { this.verificationPhotoUrl = verificationPhotoUrl; }

    public Double getConfidenceScore() { return confidenceScore; }
    public void setConfidenceScore(Double confidenceScore) { this.confidenceScore = confidenceScore; }

    public String getAttendanceResult() { return attendanceResult; }
    public void setAttendanceResult(String attendanceResult) { this.attendanceResult = attendanceResult; }

    public Long getTaskId() { return taskId; }
    public void setTaskId(Long taskId) { this.taskId = taskId; }

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }
}

