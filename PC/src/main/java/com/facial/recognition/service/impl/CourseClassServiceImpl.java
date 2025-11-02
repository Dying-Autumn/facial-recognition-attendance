package com.facial.recognition.service.impl;

import com.facial.recognition.pojo.CourseClass;
import com.facial.recognition.repository.CourseClassRepository;
import com.facial.recognition.service.CourseClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CourseClassServiceImpl implements CourseClassService {

    @Autowired
    private CourseClassRepository courseClassRepository;

    @Override
    public CourseClass createCourseClass(CourseClass courseClass) {
        return courseClassRepository.save(courseClass);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CourseClass> findById(Long courseClassId) {
        return courseClassRepository.findById(courseClassId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseClass> findByClassName(String className) {
        return courseClassRepository.findByClassName(className);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseClass> findAll() {
        return courseClassRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseClass> findByCourseId(Long courseId) {
        return courseClassRepository.findByCourseId(courseId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseClass> findByTeacherId(Long teacherId) {
        return courseClassRepository.findByTeacherId(teacherId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseClass> findByStatus(String status) {
        return courseClassRepository.findByStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseClass> findByCourseIdAndTeacherId(Long courseId, Long teacherId) {
        return courseClassRepository.findByCourseIdAndTeacherId(courseId, teacherId);
    }

    @Override
    public CourseClass updateCourseClass(Long courseClassId, CourseClass courseClass) {
        Optional<CourseClass> existingClass = courseClassRepository.findById(courseClassId);
        if (existingClass.isPresent()) {
            CourseClass updatedClass = existingClass.get();
            updatedClass.setClassName(courseClass.getClassName());
            updatedClass.setCourseId(courseClass.getCourseId());
            updatedClass.setTeacherId(courseClass.getTeacherId());
            updatedClass.setClassroom(courseClass.getClassroom());
            updatedClass.setSchedule(courseClass.getSchedule());
            updatedClass.setMaxStudents(courseClass.getMaxStudents());
            updatedClass.setStartDate(courseClass.getStartDate());
            updatedClass.setEndDate(courseClass.getEndDate());
            updatedClass.setStatus(courseClass.getStatus());
            return courseClassRepository.save(updatedClass);
        }
        throw new RuntimeException("CourseClass not found with id: " + courseClassId);
    }

    @Override
    public void deleteCourseClass(Long courseClassId) {
        courseClassRepository.deleteById(courseClassId);
    }

    @Override
    public CourseClass updateStatus(Long courseClassId, String status) {
        Optional<CourseClass> courseClass = courseClassRepository.findById(courseClassId);
        if (courseClass.isPresent()) {
            courseClass.get().setStatus(status);
            return courseClassRepository.save(courseClass.get());
        }
        throw new RuntimeException("CourseClass not found with id: " + courseClassId);
    }

    @Override
    public CourseClass updateStudentCount(Long courseClassId, Integer currentStudents) {
        Optional<CourseClass> courseClass = courseClassRepository.findById(courseClassId);
        if (courseClass.isPresent()) {
            courseClass.get().setCurrentStudents(currentStudents);
            return courseClassRepository.save(courseClass.get());
        }
        throw new RuntimeException("CourseClass not found with id: " + courseClassId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseClass> findClassesInDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return courseClassRepository.findClassesInDateRange(startDate, endDate);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isClassFull(Long courseClassId) {
        Optional<CourseClass> courseClass = courseClassRepository.findById(courseClassId);
        if (courseClass.isPresent()) {
            Integer maxStudents = courseClass.get().getMaxStudents();
            Integer currentStudents = courseClass.get().getCurrentStudents();
            return maxStudents != null && currentStudents != null && currentStudents >= maxStudents;
        }
        return false;
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getAvailableSeats(Long courseClassId) {
        Optional<CourseClass> courseClass = courseClassRepository.findById(courseClassId);
        if (courseClass.isPresent()) {
            Integer maxStudents = courseClass.get().getMaxStudents();
            Integer currentStudents = courseClass.get().getCurrentStudents();
            if (maxStudents != null && currentStudents != null) {
                return maxStudents - currentStudents;
            }
        }
        return 0;
    }
}
