package com.xitong.springboot_xitong.repository;

import com.xitong.springboot_xitong.pojo.CourseClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseClassRepository extends JpaRepository<CourseClass, Long> {
    
    // 根据课程ID查找班级
    List<CourseClass> findByCourseId(Long courseId);
    
    // 根据教师ID查找班级
    List<CourseClass> findByTeacherId(Long teacherId);
    
    // 根据班级名称查找班级
    List<CourseClass> findByClassName(String className);
    
    // 根据状态查找班级
    List<CourseClass> findByStatus(String status);
    
    // 根据课程ID和教师ID查找班级
    List<CourseClass> findByCourseIdAndTeacherId(Long courseId, Long teacherId);
    
    // 查找活跃状态的班级
    List<CourseClass> findByStatusOrderByCreatedTimeDesc(String status);
    
    // 根据教室查找班级
    List<CourseClass> findByClassroom(String classroom);
    
    // 自定义查询：查找指定时间范围内的班级
    @Query("SELECT cc FROM CourseClass cc WHERE cc.startDate <= :endDate AND cc.endDate >= :startDate")
    List<CourseClass> findClassesInDateRange(@Param("startDate") java.time.LocalDateTime startDate, 
                                           @Param("endDate") java.time.LocalDateTime endDate);
}
