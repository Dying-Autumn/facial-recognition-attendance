package com.xitong.springboot_xitong.service;

import com.xitong.springboot_xitong.pojo.CourseClass;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CourseClassService {
    
    // 创建班级
    CourseClass createCourseClass(CourseClass courseClass);
    
    // 根据ID查找班级
    Optional<CourseClass> findById(Long courseClassId);
    
    // 根据班级名称查找班级
    List<CourseClass> findByClassName(String className);
    
    // 获取所有班级
    List<CourseClass> findAll();
    
    // 根据课程ID查找班级
    List<CourseClass> findByCourseId(Long courseId);
    
    // 根据教师ID查找班级
    List<CourseClass> findByTeacherId(Long teacherId);
    
    // 根据状态查找班级
    List<CourseClass> findByStatus(String status);
    
    // 根据课程ID和教师ID查找班级
    List<CourseClass> findByCourseIdAndTeacherId(Long courseId, Long teacherId);
    
    // 更新班级信息
    CourseClass updateCourseClass(Long courseClassId, CourseClass courseClass);
    
    // 删除班级
    void deleteCourseClass(Long courseClassId);
    
    // 更新班级状态
    CourseClass updateStatus(Long courseClassId, String status);
    
    // 更新班级学生数量
    CourseClass updateStudentCount(Long courseClassId, Integer currentStudents);
    
    // 查找指定时间范围内的班级
    List<CourseClass> findClassesInDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    // 检查班级是否已满
    boolean isClassFull(Long courseClassId);
    
    // 获取班级可用座位数
    Integer getAvailableSeats(Long courseClassId);
}
