package com.xitong.springboot_xitong.pojo;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "student_course_classes")
@IdClass(StudentCourseClassId.class)
public class StudentCourseClass implements Serializable {
    
    @Id
    @Column(name = "StudentID", nullable = false)
    private Long studentId; // 主键，外键

    @Id
    @Column(name = "ClassID", nullable = false)
    private Long classId; // 主键，外键

    @Column(name = "enrollment_date")
    private LocalDateTime enrollmentDate; // 选课时间

    private String status; // 状态：ENROLLED, DROPPED, COMPLETED

    private Double finalGrade; // 最终成绩

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

// 复合主键类
class StudentCourseClassId implements Serializable {
    private Long studentId;
    private Long classId;

    public StudentCourseClassId() {}

    public StudentCourseClassId(Long studentId, Long classId) {
        this.studentId = studentId;
        this.classId = classId;
    }

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public Long getClassId() { return classId; }
    public void setClassId(Long classId) { this.classId = classId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentCourseClassId that = (StudentCourseClassId) o;
        return studentId.equals(that.studentId) && classId.equals(that.classId);
    }

    @Override
    public int hashCode() {
        return studentId.hashCode() + classId.hashCode();
    }
}
