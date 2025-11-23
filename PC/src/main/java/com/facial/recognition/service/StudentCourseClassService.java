package com.facial.recognition.service;

import com.facial.recognition.pojo.StudentCourseClass;

import java.util.List;
import java.util.Optional;

public interface StudentCourseClassService {
    
    // 学生选课
    StudentCourseClass enrollStudent(Long studentId, Long classId);
    
    // 学生退�?
    StudentCourseClass dropCourse(Long studentId, Long classId);
    
    // 根据ID查找选课记录
    Optional<StudentCourseClass> findById(Long id);
    
    // 根据学生ID和班级ID查找选课记录
    Optional<StudentCourseClass> findByStudentIdAndClassId(Long studentId, Long classId);
    
    // 根据学生ID查找选课记录
    List<StudentCourseClass> findByStudentId(Long studentId);
    
    // 根据班级ID查找选课记录
    List<StudentCourseClass> findByClassId(Long classId);
    
    // 根据状态查找选课记录
    List<StudentCourseClass> findByStatus(String status);
    
    // 根据学生ID和状态查找选课记录
    List<StudentCourseClass> findByStudentIdAndStatus(Long studentId, String status);
    
    // 根据班级ID和状态查找选课记录
    List<StudentCourseClass> findByClassIdAndStatus(Long classId, String status);
    
    // 更新选课状�?
    StudentCourseClass updateStatus(Long id, String status);
    
    // 更新成绩
    StudentCourseClass updateGrade(Long id, Double finalGrade, String gradeLevel);
    
    // 统计班级中的学生数量
    Long countEnrolledStudentsByClassId(Long classId);
    
    // 查找学生已完成的课程
    List<StudentCourseClass> findCompletedCoursesByStudentId(Long studentId);
    
    // 查找学生正在进行的课�?
    List<StudentCourseClass> findEnrolledCoursesByStudentId(Long studentId);
    
    // 检查学生是否已选课
    boolean isStudentEnrolled(Long studentId, Long classId);
    
    // 获取学生选课历史
    List<StudentCourseClass> getStudentEnrollmentHistory(Long studentId);

    Optional<StudentCourseClass> findByStudentIdAndCourseClassId(Long studentId, Long courseClassId);

    List<StudentCourseClass> findByCourseClassId(Long courseClassId);

    List<StudentCourseClass> findByCourseClassIdAndStatus(Long courseClassId, String status);

    Long countEnrolledStudentsByCourseClassId(Long courseClassId);
    
    // 获取学生选修的课程列表（包含课程信息）
    List<com.facial.recognition.dto.StudentCourseDTO> getStudentCourses(Long studentId);
}
