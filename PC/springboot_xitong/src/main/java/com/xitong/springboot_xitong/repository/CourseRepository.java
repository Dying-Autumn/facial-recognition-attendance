package com.xitong.springboot_xitong.repository;

import com.xitong.springboot_xitong.pojo.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findByCourseCode(String courseCode);
    List<Course> findBySemester(String semester);
}
