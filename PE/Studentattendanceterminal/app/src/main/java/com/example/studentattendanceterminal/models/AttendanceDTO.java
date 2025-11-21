package com.example.studentattendanceterminal.models;

public class AttendanceDTO {
    private Long studentId;
    private Long courseId; // 如果没有具体任务，关联到课程
    private Long taskId;   // 如果有具体任务
    private Double latitude;
    private Double longitude;
    private String photoBase64; // 图片Base64字符串
    private Long checkInTime;
    private String status; // "正常" 或 "缺勤"

    public AttendanceDTO(Long studentId, Long courseId, Double latitude, Double longitude, String photoBase64, Long checkInTime, String status) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.photoBase64 = photoBase64;
        this.checkInTime = checkInTime;
        this.status = status;
    }
    
    // Getters and Setters (省略，Gson会自动序列化字段)
}

