package com.facial.recognition.pojo;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "course_classes")
public class CourseClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ClassID")
    private Long classId; // 主键

    @Column(name = "class_name", nullable = false)
    private String className; // 班级名称/代号

    @Column(name = "CourseID", nullable = false)
    private Long courseId; // 外键

    @Column(name = "TeacherID", nullable = false)
    private Long teacherId; // 外键

    private String classroom; // 教室

    private String schedule; // 上课时间安排

    private Integer maxStudents; // 最大学生数

    private Integer currentStudents; // 当前学生数

    @Column(name = "start_date")
    private LocalDateTime startDate; // 开课时间

    @Column(name = "end_date")
    private LocalDateTime endDate; // 结课时间

    private String status; // 状态：ACTIVE, INACTIVE, COMPLETED

    public CourseClass() {}

    public CourseClass(String className, Long courseId, Long teacherId) {
        this.className = className;
        this.courseId = courseId;
        this.teacherId = teacherId;
        this.currentStudents = 0;
        this.status = "ACTIVE";
    }

    // Getters and Setters
    public Long getClassId() { return classId; }
    public void setClassId(Long classId) { this.classId = classId; }

    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }

    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }

    public Long getTeacherId() { return teacherId; }
    public void setTeacherId(Long teacherId) { this.teacherId = teacherId; }

    public String getClassroom() { return classroom; }
    public void setClassroom(String classroom) { this.classroom = classroom; }

    public String getSchedule() { return schedule; }
    public void setSchedule(String schedule) { this.schedule = schedule; }

    public Integer getMaxStudents() { return maxStudents; }
    public void setMaxStudents(Integer maxStudents) { this.maxStudents = maxStudents; }

    public Integer getCurrentStudents() { return currentStudents; }
    public void setCurrentStudents(Integer currentStudents) { this.currentStudents = currentStudents; }

    public LocalDateTime getStartDate() { return startDate; }
    public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }

    public LocalDateTime getEndDate() { return endDate; }
    public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

