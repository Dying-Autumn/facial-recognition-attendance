package com.xitong.springboot_xitong.service.impl;

import com.xitong.springboot_xitong.pojo.AttendanceRecord;
import com.xitong.springboot_xitong.pojo.Student;
import com.xitong.springboot_xitong.repository.StudentRepository;
import com.xitong.springboot_xitong.repository.AttendanceRepository;
import com.xitong.springboot_xitong.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StudentServiceImpl implements StudentService {
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private AttendanceRepository attendanceRepository;

    @Override
    public Student createStudent(Student student) {
        // 检查学号是否已存在
        studentRepository.findByStudentNumber(student.getStudentNumber())
            .ifPresent(s -> { throw new IllegalArgumentException("Student number already exists"); });
        return studentRepository.save(student);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Student> findById(Long studentId) {
        return studentRepository.findById(studentId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Student> findByUserId(Integer userId) {
        return studentRepository.findByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Student> findByStudentNumber(String studentNumber) {
        return studentRepository.findByStudentNumber(studentNumber);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Student> findByClassName(String className) {
        return studentRepository.findByClassName(className);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Student> findByEnrollmentYear(Integer enrollmentYear) {
        return studentRepository.findByEnrollmentYear(enrollmentYear);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Student> findByClassNameAndEnrollmentYear(String className, Integer enrollmentYear) {
        return studentRepository.findByClassNameAndEnrollmentYear(className, enrollmentYear);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Student> findByClassNameContaining(String className) {
        return studentRepository.findByClassNameContaining(className);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Student> findByEnrollmentYearBetween(Integer startYear, Integer endYear) {
        return studentRepository.findByEnrollmentYearBetween(startYear, endYear);
    }

    @Override
    public Student updateStudent(Long studentId, Student student) {
        Optional<Student> existingStudent = studentRepository.findById(studentId);
        if (existingStudent.isPresent()) {
            Student updatedStudent = existingStudent.get();
            updatedStudent.setStudentNumber(student.getStudentNumber());
            updatedStudent.setClassName(student.getClassName());
            updatedStudent.setEnrollmentYear(student.getEnrollmentYear());
            updatedStudent.setUserId(student.getUserId());
            return studentRepository.save(updatedStudent);
        }
        throw new RuntimeException("Student not found with id: " + studentId);
    }

    @Override
    public Student updateStudentByUserId(Integer userId, Student student) {
        Optional<Student> existingStudent = studentRepository.findByUserId(userId);
        if (existingStudent.isPresent()) {
            Student updatedStudent = existingStudent.get();
            updatedStudent.setStudentNumber(student.getStudentNumber());
            updatedStudent.setClassName(student.getClassName());
            updatedStudent.setEnrollmentYear(student.getEnrollmentYear());
            return studentRepository.save(updatedStudent);
        }
        throw new RuntimeException("Student not found with userId: " + userId);
    }

    @Override
    public void deleteStudent(Long studentId) {
        studentRepository.deleteById(studentId);
    }

    @Override
    public void deleteByUserId(Integer userId) {
        studentRepository.deleteByUserId(userId);
    }

    @Override
    public void deleteByStudentNumber(String studentNumber) {
        studentRepository.deleteByStudentNumber(studentNumber);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttendanceRecord> getAttendance(String userId, String courseId) {
        return (courseId == null) ? 
            attendanceRepository.findByUserId(userId) : 
            attendanceRepository.findByUserIdAndCourseId(userId, courseId);
    }

    @Override
    public AttendanceRecord upsertAttendance(String userId, AttendanceRecord rec) {
        rec.setUserId(userId);
        if (rec.getDate() ==Boolean.parseBoolean(null)) rec.setDate(LocalDate.now());
        return attendanceRepository.save(rec);
    }
}
