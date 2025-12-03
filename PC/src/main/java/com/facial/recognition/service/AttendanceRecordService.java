package com.facial.recognition.service;

import com.facial.recognition.pojo.AttendanceRecord;
import java.util.List;

public interface AttendanceRecordService {
    List<AttendanceRecord> findByStudentId(Long studentId);
}
