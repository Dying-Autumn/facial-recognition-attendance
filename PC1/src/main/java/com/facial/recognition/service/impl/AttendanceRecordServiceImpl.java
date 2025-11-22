package com.facial.recognition.service.impl;

import com.facial.recognition.dto.AttendanceRecordDTO;
import com.facial.recognition.pojo.AttendanceRecord;
import com.facial.recognition.repository.AttendanceRepository;
import com.facial.recognition.service.AttendanceRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
public class AttendanceRecordServiceImpl implements AttendanceRecordService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Override
    public AttendanceRecord uploadRecord(AttendanceRecordDTO recordDTO) {
        AttendanceRecord record = new AttendanceRecord();
        
        // 转换时间戳
        if (recordDTO.getCheckInTime() != null) {
            record.setCheckInTime(LocalDateTime.ofInstant(
                Instant.ofEpochMilli(recordDTO.getCheckInTime()), 
                ZoneId.systemDefault()));
        } else {
            record.setCheckInTime(LocalDateTime.now());
        }
        
        record.setStudentId(recordDTO.getStudentId());
        // 如果前端没传 TaskID (例如只选了课程)，这里为了演示防止报错，设为 1
        // 实际业务逻辑应该根据当前时间和课程查找对应的 Task
        record.setTaskId(recordDTO.getTaskId() != null ? recordDTO.getTaskId() : 1L);
        
        record.setActualLatitude(recordDTO.getLatitude());
        record.setActualLongitude(recordDTO.getLongitude());
        record.setAttendanceResult(recordDTO.getStatus());
        record.setConfidenceScore(100.0); // 模拟置信度
        record.setCreatedDate(LocalDateTime.now());
        
        // 处理图片 (这里模拟保存路径，不实际写入文件系统以免复杂化)
        if (recordDTO.getPhotoBase64() != null && !recordDTO.getPhotoBase64().isEmpty()) {
            String fileName = "att_" + recordDTO.getStudentId() + "_" + System.currentTimeMillis() + ".jpg";
            record.setVerificationPhotoUrl("/uploads/" + fileName);
        }
        
        return attendanceRepository.save(record);
    }

    @Override
    public List<AttendanceRecord> findAll() {
        return attendanceRepository.findAll();
    }

    @Override
    public List<AttendanceRecord> findByStudentId(Long studentId) {
        return attendanceRepository.findByStudentId(studentId);
    }
}

