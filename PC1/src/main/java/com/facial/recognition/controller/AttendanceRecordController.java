package com.facial.recognition.controller;

import com.facial.recognition.dto.AttendanceRecordDTO;
import com.facial.recognition.pojo.AttendanceRecord;
import com.facial.recognition.service.AttendanceRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attendance")
@CrossOrigin(origins = "*")
public class AttendanceRecordController {

    @Autowired
    private AttendanceRecordService attendanceRecordService;

    // 上传考勤记录
    @PostMapping("/record")
    public ResponseEntity<AttendanceRecord> uploadRecord(@RequestBody AttendanceRecordDTO recordDTO) {
        try {
            AttendanceRecord record = attendanceRecordService.uploadRecord(recordDTO);
            return ResponseEntity.ok(record);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    // 获取所有考勤记录
    @GetMapping("/records")
    public ResponseEntity<List<AttendanceRecord>> getAllRecords() {
        List<AttendanceRecord> records = attendanceRecordService.findAll();
        return ResponseEntity.ok(records);
    }

    // 根据学生ID获取考勤记录
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<AttendanceRecord>> getStudentRecords(@PathVariable Long studentId) {
        List<AttendanceRecord> records = attendanceRecordService.findByStudentId(studentId);
        return ResponseEntity.ok(records);
    }
}

