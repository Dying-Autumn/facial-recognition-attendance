package com.xitong.springboot_xitong.pojo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity       //实体类
@Table(name = "student")       //建立表格
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "StudentID")
    private Long studentId; // 主键

    @Column(unique = true, nullable = false)
    private String studentNumber; // 学号

    @Column(nullable = false)
    private String className; // 所属班级

    @Column(nullable = false)
    private Integer enrollmentYear; // 入学年份

    @Column(name = "UserID", nullable = false)
    private Integer userId; // 外键，关联User表

    public Student() {}

    public Student(String studentNumber, String className, Integer enrollmentYear, Integer userId) {
        this.studentNumber = studentNumber;
        this.className = className;
        this.enrollmentYear = enrollmentYear;
        this.userId = userId;
    }

    // Getters and Setters
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public String getStudentNumber() { return studentNumber; }
    public void setStudentNumber(String studentNumber) { this.studentNumber = studentNumber; }

    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }

    public Integer getEnrollmentYear() { return enrollmentYear; }
    public void setEnrollmentYear(Integer enrollmentYear) { this.enrollmentYear = enrollmentYear; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
}

