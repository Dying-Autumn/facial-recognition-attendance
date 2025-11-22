package com.facial.recognition.pojo;
import jakarta.persistence.*;

@Entity
@Table(name = "teachers")
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teacherId;

    private String title; // 职称

    private String department; // 所属部门

    @Column(nullable = false)
    private String userId; // 关联 User 的 userId（外键）

    public Teacher() {}

    public Teacher(String title, String department, String userId) {
        this.title = title;
        this.department = department;
        this.userId = userId;
    }

    public Long getTeacherId() { return teacherId; }
    public void setTeacherId(Long teacherId) { this.teacherId = teacherId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
}


