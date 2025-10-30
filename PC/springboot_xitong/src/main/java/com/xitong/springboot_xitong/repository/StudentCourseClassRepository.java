package com.xitong.springboot_xitong.repository;

import com.xitong.springboot_xitong.pojo.StudentCourseClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentCourseClassRepository extends JpaRepository<StudentCourseClass, Long> {
    
    // 根据学生ID查找选课记录
    List<StudentCourseClass> findByStudentId(Long studentId);
    
    // 根据班级ID查找选课记录
    List<StudentCourseClass> findByClassId(Long classId);
    
    // 根据学生ID和班级ID查找选课记录
    Optional<StudentCourseClass> findByStudentIdAndClassId(Long studentId, Long classId);
    
    // 根据状态查找选课记录
    List<StudentCourseClass> findByStatus(String status);
    
    // 根据学生ID和状态查找选课记录
    List<StudentCourseClass> findByStudentIdAndStatus(Long studentId, String status);
    
    // 根据班级ID和状态查找选课记录
    List<StudentCourseClass> findByClassIdAndStatus(Long classId, String status);
    
    // 统计班级中的学生数量
    @Query("SELECT COUNT(scc) FROM StudentCourseClass scc WHERE scc.classId = :classId AND scc.status = 'ENROLLED'")
    Long countEnrolledStudentsByClassId(@Param("classId") Long classId);
    
    // 查找学生已完成的课程
    @Query("SELECT scc FROM StudentCourseClass scc WHERE scc.studentId = :studentId AND scc.status = 'COMPLETED'")
    List<StudentCourseClass> findCompletedCoursesByStudentId(@Param("studentId") Long studentId);
    
    // 查找学生正在进行的课程
    @Query("SELECT scc FROM StudentCourseClass scc WHERE scc.studentId = :studentId AND scc.status = 'ENROLLED'")
    List<StudentCourseClass> findEnrolledCoursesByStudentId(@Param("studentId") Long studentId);
}
