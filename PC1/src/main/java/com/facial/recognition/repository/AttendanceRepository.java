package com.facial.recognition.repository;

import com.facial.recognition.pojo.AttendanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<AttendanceRecord, Long> {
    List<AttendanceRecord> findByStudentId(Long studentId);
    List<AttendanceRecord> findByTaskId(Long taskId);
    List<AttendanceRecord> findByStudentIdAndTaskId(Long studentId, Long taskId);
}
