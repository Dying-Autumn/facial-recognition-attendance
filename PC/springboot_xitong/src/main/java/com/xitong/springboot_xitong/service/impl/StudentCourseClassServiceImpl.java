package com.xitong.springboot_xitong.service.impl;

import com.xitong.springboot_xitong.pojo.StudentCourseClass;
import com.xitong.springboot_xitong.repository.StudentCourseClassRepository;
import com.xitong.springboot_xitong.service.StudentCourseClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StudentCourseClassServiceImpl implements StudentCourseClassService {

    @Autowired
    private StudentCourseClassRepository studentCourseClassRepository;

    @Override
    public StudentCourseClass enrollStudent(Long studentId, Long classId) {
        // 检查是否已经选课
        Optional<StudentCourseClass> existing = studentCourseClassRepository
            .findByStudentIdAndClassId(studentId, classId);
        
        if (existing.isPresent()) {
            throw new RuntimeException("Student is already enrolled in this course class");
        }
        
        StudentCourseClass enrollment = new StudentCourseClass(studentId, classId);
        return studentCourseClassRepository.save(enrollment);
    }

    @Override
    public StudentCourseClass dropCourse(Long studentId, Long classId) {
        Optional<StudentCourseClass> enrollment = studentCourseClassRepository
            .findByStudentIdAndClassId(studentId, classId);
        
        if (enrollment.isPresent()) {
            enrollment.get().setStatus("DROPPED");
            return studentCourseClassRepository.save(enrollment.get());
        }
        throw new RuntimeException("Enrollment not found");
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<StudentCourseClass> findById(Long id) {
        return studentCourseClassRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<StudentCourseClass> findByStudentIdAndClassId(Long studentId, Long classId) {
        return studentCourseClassRepository.findByStudentIdAndClassId(studentId, classId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentCourseClass> findByStudentId(Long studentId) {
        return studentCourseClassRepository.findByStudentId(studentId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentCourseClass> findByClassId(Long classId) {
        return studentCourseClassRepository.findByClassId(classId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentCourseClass> findByStatus(String status) {
        return studentCourseClassRepository.findByStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentCourseClass> findByStudentIdAndStatus(Long studentId, String status) {
        return studentCourseClassRepository.findByStudentIdAndStatus(studentId, status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentCourseClass> findByClassIdAndStatus(Long classId, String status) {
        return studentCourseClassRepository.findByClassIdAndStatus(classId, status);
    }

    @Override
    public StudentCourseClass updateStatus(Long id, String status) {
        Optional<StudentCourseClass> enrollment = studentCourseClassRepository.findById(id);
        if (enrollment.isPresent()) {
            enrollment.get().setStatus(status);
            return studentCourseClassRepository.save(enrollment.get());
        }
        throw new RuntimeException("Enrollment not found with id: " + id);
    }

    @Override
    public StudentCourseClass updateGrade(Long id, Double finalGrade, String gradeLevel) {
        Optional<StudentCourseClass> enrollment = studentCourseClassRepository.findById(id);
        if (enrollment.isPresent()) {
            enrollment.get().setFinalGrade(finalGrade);
            enrollment.get().setGradeLevel(gradeLevel);
            return studentCourseClassRepository.save(enrollment.get());
        }
        throw new RuntimeException("Enrollment not found with id: " + id);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countEnrolledStudentsByClassId(Long classId) {
        return studentCourseClassRepository.countEnrolledStudentsByClassId(classId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentCourseClass> findCompletedCoursesByStudentId(Long studentId) {
        return studentCourseClassRepository.findCompletedCoursesByStudentId(studentId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentCourseClass> findEnrolledCoursesByStudentId(Long studentId) {
        return studentCourseClassRepository.findEnrolledCoursesByStudentId(studentId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isStudentEnrolled(Long studentId, Long classId) {
        Optional<StudentCourseClass> enrollment = studentCourseClassRepository
            .findByStudentIdAndClassId(studentId, classId);
        return enrollment.isPresent() && "ENROLLED".equals(enrollment.get().getStatus());
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentCourseClass> getStudentEnrollmentHistory(Long studentId) {
        return studentCourseClassRepository.findByStudentId(studentId);
    }

    @Override
    public Optional<StudentCourseClass> findByStudentIdAndCourseClassId(Long studentId, Long courseClassId) {
        return Optional.empty();
    }

    @Override
    public List<StudentCourseClass> findByCourseClassId(Long courseClassId) {
        return List.of();
    }

    @Override
    public List<StudentCourseClass> findByCourseClassIdAndStatus(Long courseClassId, String status) {
        return List.of();
    }

    @Override
    public Long countEnrolledStudentsByCourseClassId(Long courseClassId) {
        return 0L;
    }
}
