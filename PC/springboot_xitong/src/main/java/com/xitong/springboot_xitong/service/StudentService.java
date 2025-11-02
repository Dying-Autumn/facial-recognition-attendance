package com.xitong.springboot_xitong.service;

import com.xitong.springboot_xitong.pojo.Student;
import com.xitong.springboot_xitong.pojo.AttendanceRecord;

import java.util.List;
import java.util.Optional;

public interface StudentService {
    
    // 创建学生
    Student createStudent(Student student);
    
    // 根据ID查找学生
    Optional<Student> findById(Long studentId);
    
    // 根据UserID查找学生
    Optional<Student> findByUserId(Integer userId);
    
    // 根据学号查找学生
    Optional<Student> findByStudentNumber(String studentNumber);
    
    // 获取所有学生
    List<Student> findAll();
    
    // 根据所属班级查找学生
    List<Student> findByClassName(String className);
    
    // 根据入学年份查找学生
    List<Student> findByEnrollmentYear(Integer enrollmentYear);
    
    // 根据班级和入学年份查找学生
    List<Student> findByClassNameAndEnrollmentYear(String className, Integer enrollmentYear);
    
    // 根据班级名称模糊查询
    List<Student> findByClassNameContaining(String className);
    
    // 查找指定年份范围的学生
    List<Student> findByEnrollmentYearBetween(Integer startYear, Integer endYear);
    
    // 更新学生信息
    Student updateStudent(Long studentId, Student student);
    
    // 根据UserID更新学生信息
    Student updateStudentByUserId(Integer userId, Student student);
    
    // 删除学生
    void deleteStudent(Long studentId);
    
    // 根据UserID删除学生
    void deleteByUserId(Integer userId);
    
    // 根据学号删除学生
    void deleteByStudentNumber(String studentNumber);
    
    // 考勤相关方法
    List<AttendanceRecord> getAttendance(String userId, String courseId);
    AttendanceRecord upsertAttendance(String userId, AttendanceRecord rec);
}
