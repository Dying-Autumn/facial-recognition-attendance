package com.xitong.springboot_xitong.repository;

import com.xitong.springboot_xitong.pojo.AttendanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<AttendanceRecord, Long> {
    List<AttendanceRecord> findByUserId(String userId);
    List<AttendanceRecord> findByUserIdAndCourseId(String userId, String courseId);
}
