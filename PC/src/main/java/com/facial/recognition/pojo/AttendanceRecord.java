package com.facial.recognition.pojo;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "attendance_records")
public class AttendanceRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RecordID")
    private Long recordId; // 主键

    @Column(name = "checkin_time", nullable = false)
    private LocalDateTime checkinTime; // 签到时间

    @Column(name = "actual_coordinates")
    private String actualCoordinates; // 实际签到坐标

    @Column(name = "verification_photo_url")
    private String verificationPhotoUrl; // 验证照片URL

    @Column(name = "confidence_score")
    private Double confidenceScore; // 置信度分数

    @Column(name = "checkin_result", nullable = false)
    private String checkinResult; // 签到结果：SUCCESS, FAILED, PENDING

    @Column(name = "TaskID", nullable = false)
    private Long taskId; // 外键

    @Column(name = "StudentID", nullable = false)
    private Long studentId; // 外键

    public AttendanceRecord() {}

    public AttendanceRecord(LocalDateTime checkinTime, String checkinResult, Long taskId, Long studentId) {
        this.checkinTime = checkinTime;
        this.checkinResult = checkinResult;
        this.taskId = taskId;
        this.studentId = studentId;
    }

    // Getters and Setters
    public Long getRecordId() { return recordId; }
    public void setRecordId(Long recordId) { this.recordId = recordId; }

    public LocalDateTime getCheckinTime() { return checkinTime; }
    public void setCheckinTime(LocalDateTime checkinTime) { this.checkinTime = checkinTime; }

    public String getActualCoordinates() { return actualCoordinates; }
    public void setActualCoordinates(String actualCoordinates) { this.actualCoordinates = actualCoordinates; }

    public String getVerificationPhotoUrl() { return verificationPhotoUrl; }
    public void setVerificationPhotoUrl(String verificationPhotoUrl) { this.verificationPhotoUrl = verificationPhotoUrl; }

    public Double getConfidenceScore() { return confidenceScore; }
    public void setConfidenceScore(Double confidenceScore) { this.confidenceScore = confidenceScore; }

    public String getCheckinResult() { return checkinResult; }
    public void setCheckinResult(String checkinResult) { this.checkinResult = checkinResult; }

    public Long getTaskId() { return taskId; }
    public void setTaskId(Long taskId) { this.taskId = taskId; }

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public void setUserId(String userId) {
    }



    public void setDate(LocalDate now) {
    }

    public boolean getDate() {
        boolean Date = false;
        return Date;}
}

