package com.facial.recognition.service;

import com.facial.recognition.dto.AttendanceRecordDTO;
import com.facial.recognition.pojo.AttendanceRecord;

import java.util.List;

public interface AttendanceRecordService {
    // 上传考勤记录
    AttendanceRecord uploadRecord(AttendanceRecordDTO recordDTO);
    
    // 获取所有考勤记录
    List<AttendanceRecord> findAll();
    
    // 根据学生ID获取考勤记录
    List<AttendanceRecord> findByStudentId(Long studentId);
}

