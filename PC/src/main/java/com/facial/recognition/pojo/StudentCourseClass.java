package com.facial.recognition.pojo;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "StudentCourseClass")
@IdClass(StudentCourseClassId.class)
public class StudentCourseClass implements Serializable {
    
    @Id
    @Column(name = "StudentID", nullable = false)
    private Long studentId; // 主键，外键

    @Id
    @Column(name = "ClassID", nullable = false)
    private Long classId; // 主键，外键

    @Column(name = "EnrollmentDate")
    private LocalDateTime enrollmentDate; // 选课时间

    @Column(name = "Status")
    private String status; // 状态：ENROLLED, DROPPED, COMPLETED

    @Column(name = "FinalGrade")
    private Double finalGrade; // 最终成绩

    @Column(name = "GradeLevel")
    private String gradeLevel; // 成绩等级：A, B, C, D, F

    public StudentCourseClass() {}

    public StudentCourseClass(Long studentId, Long classId) {
        this.studentId = studentId;
        this.classId = classId;
        this.enrollmentDate = LocalDateTime.now();
        this.status = "ENROLLED";
    }

    // Getters and Setters
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public Long getClassId() { return classId; }
    public void setClassId(Long classId) { this.classId = classId; }

    public LocalDateTime getEnrollmentDate() { return enrollmentDate; }
    public void setEnrollmentDate(LocalDateTime enrollmentDate) { this.enrollmentDate = enrollmentDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Double getFinalGrade() { return finalGrade; }
    public void setFinalGrade(Double finalGrade) { this.finalGrade = finalGrade; }

    public String getGradeLevel() { return gradeLevel; }
    public void setGradeLevel(String gradeLevel) { this.gradeLevel = gradeLevel; }
}

