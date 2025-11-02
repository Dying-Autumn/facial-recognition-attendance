package com.facial.recognition.repository;

import com.facial.recognition.pojo.CourseClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseClassRepository extends JpaRepository<CourseClass, Long> {
    
    // 鏍规嵁璇剧▼ID鏌ユ壘鐝骇
    List<CourseClass> findByCourseId(Long courseId);
    
    // 鏍规嵁鏁欏笀ID鏌ユ壘鐝骇
    List<CourseClass> findByTeacherId(Long teacherId);
    
    // 鏍规嵁鐝骇鍚嶇О鏌ユ壘鐝骇
    List<CourseClass> findByClassName(String className);
    
    // 鏍规嵁鐘舵€佹煡鎵剧彮锟?
    List<CourseClass> findByStatus(String status);
    
    // 鏍规嵁璇剧▼ID鍜屾暀甯圛D鏌ユ壘鐝骇
    List<CourseClass> findByCourseIdAndTeacherId(Long courseId, Long teacherId);
    
    // 鏌ユ壘娲昏穬鐘舵€佺殑鐝骇
    List<CourseClass> findByStatusOrderByStartDateDesc(String status);
    
    // 鏍规嵁鏁欏鏌ユ壘鐝骇
    List<CourseClass> findByClassroom(String classroom);
    
    // 鑷畾涔夋煡璇細鏌ユ壘鎸囧畾鏃堕棿鑼冨洿鍐呯殑鐝骇
    @Query("SELECT cc FROM CourseClass cc WHERE cc.startDate <= :endDate AND cc.endDate >= :startDate")
    List<CourseClass> findClassesInDateRange(@Param("startDate") java.time.LocalDateTime startDate, 
                                           @Param("endDate") java.time.LocalDateTime endDate);
}


