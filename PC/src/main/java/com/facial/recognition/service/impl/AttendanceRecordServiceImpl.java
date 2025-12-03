package com.facial.recognition.service.impl;

import com.facial.recognition.pojo.AttendanceRecord;
import com.facial.recognition.repository.AttendanceRecordRepository;
import com.facial.recognition.service.AttendanceRecordService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AttendanceRecordServiceImpl implements AttendanceRecordService {
    
    private final AttendanceRecordRepository recordRepository;
    
    public AttendanceRecordServiceImpl(AttendanceRecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }
    
    @Override
    public List<AttendanceRecord> findByStudentId(Long studentId) {
        return recordRepository.findByStudentId(studentId);
    }
}
