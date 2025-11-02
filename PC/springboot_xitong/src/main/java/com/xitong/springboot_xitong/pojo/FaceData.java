package com.xitong.springboot_xitong.pojo;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "face_data")
public class FaceData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FaceDataID")
    private Long faceDataId; // 主键

    @Column(name = "face_template", nullable = false, columnDefinition = "TEXT")
    private String faceTemplate; // 人脸模板数据

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate; // 创建日期

    @Column(name = "UserID", nullable = false, unique = true)
    private Integer userId; // 外键，唯一

    public FaceData() {}

    public FaceData(String faceTemplate, Integer userId) {
        this.faceTemplate = faceTemplate;
        this.userId = userId;
        this.createdDate = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getFaceDataId() { return faceDataId; }
    public void setFaceDataId(Long faceDataId) { this.faceDataId = faceDataId; }

    public String getFaceTemplate() { return faceTemplate; }
    public void setFaceTemplate(String faceTemplate) { this.faceTemplate = faceTemplate; }

    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
}
