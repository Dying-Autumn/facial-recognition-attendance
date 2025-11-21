package com.facial.recognition.service.impl;

import com.facial.recognition.pojo.CourseClass;
import com.facial.recognition.repository.CourseClassRepository;
import com.facial.recognition.service.CourseClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            // transient fields are not updated
            return courseClassRepository.save(updatedClass);
        }
        throw new RuntimeException("CourseClass not found with id: " + courseClassId);
    }

    @Override
    public void deleteCourseClass(Long courseClassId) {
        courseClassRepository.deleteById(courseClassId);
    }
}
