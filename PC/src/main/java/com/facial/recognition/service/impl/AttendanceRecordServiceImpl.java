package com.facial.recognition.service.impl;

import com.facial.recognition.pojo.AttendanceRecord;
import com.facial.recognition.pojo.AttendanceTask;
import com.facial.recognition.pojo.StudentCourseClass;
import com.facial.recognition.repository.AttendanceRecordRepository;
import com.facial.recognition.repository.AttendanceTaskRepository;
import com.facial.recognition.repository.StudentCourseClassRepository;
import com.facial.recognition.service.AttendanceRecordService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AttendanceRecordServiceImpl implements AttendanceRecordService {
    
    private final AttendanceRecordRepository recordRepository;
    private final AttendanceTaskRepository attendanceTaskRepository;
    private final StudentCourseClassRepository studentCourseClassRepository;
    
    public AttendanceRecordServiceImpl(AttendanceRecordRepository recordRepository,
                                       AttendanceTaskRepository attendanceTaskRepository,
                                       StudentCourseClassRepository studentCourseClassRepository) {
        this.recordRepository = recordRepository;
        this.attendanceTaskRepository = attendanceTaskRepository;
        this.studentCourseClassRepository = studentCourseClassRepository;
    }
    
    @Override
    public List<AttendanceRecord> findByStudentId(Long studentId) {
        return recordRepository.findByStudentId(studentId);
    }

    @Override
    public AttendanceRecord save(AttendanceRecord record) {
        return recordRepository.save(record);
    }

    @Override
    public List<AttendanceRecord> findFullRecordsWithAbsent(Long studentId) {
        List<AttendanceRecord> existing = recordRepository.findByStudentId(studentId);
        Set<Long> attendedTaskIds = existing.stream()
                .map(AttendanceRecord::getTaskId)
                .filter(id -> id != null)
                .collect(Collectors.toSet());

        // 取当前学生所在的班级
        List<StudentCourseClass> enrollments = studentCourseClassRepository.findByStudentId(studentId);
        Set<Long> classIds = enrollments.stream()
                .map(StudentCourseClass::getClassId)
                .collect(Collectors.toSet());

        LocalDateTime now = LocalDateTime.now();
        List<AttendanceTask> endedTasks = new ArrayList<>();
        for (Long classId : classIds) {
            List<AttendanceTask> tasks = attendanceTaskRepository.findByCourseClassId(classId);
            if (tasks != null) {
                tasks.stream()
                        .filter(t -> t.getEndTime() != null && t.getEndTime().isBefore(now))
                        .forEach(endedTasks::add);
            }
        }

        List<AttendanceRecord> missing = new ArrayList<>();
        for (AttendanceTask task : endedTasks) {
            Long tid = task.getTaskId();
            if (tid == null) continue;
            if (attendedTaskIds.contains(tid)) continue;
            AttendanceRecord r = new AttendanceRecord();
            r.setTaskId(tid);
            r.setStudentId(studentId);
            r.setCheckInTime(task.getEndTime());
            r.setCreatedDate(task.getEndTime());
            r.setAttendanceResult("缺勤");
            r.setRemark("任务已结束未签到");
            missing.add(r);
        }

        List<AttendanceRecord> all = new ArrayList<>(existing);
        all.addAll(missing);

        // 按打卡时间倒序（缺勤按任务结束时间）
        all.sort(Comparator.comparing(AttendanceRecord::getCheckInTime,
                Comparator.nullsLast(LocalDateTime::compareTo)).reversed());
        return all;
    }
}
