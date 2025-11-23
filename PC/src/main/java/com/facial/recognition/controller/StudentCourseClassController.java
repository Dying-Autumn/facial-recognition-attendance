package com.facial.recognition.controller;

import com.facial.recognition.pojo.StudentCourseClass;
import com.facial.recognition.service.StudentCourseClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/student-course-classes")
@CrossOrigin(origins = "*")
public class StudentCourseClassController {

    @Autowired
    private StudentCourseClassService studentCourseClassService;

    // 学生选课
    @PostMapping("/enroll")
    public ResponseEntity<StudentCourseClass> enrollStudent(
            @RequestParam Long studentId, @RequestParam Long courseClassId) {
        try {
            StudentCourseClass enrollment = studentCourseClassService.enrollStudent(studentId, courseClassId);
            return ResponseEntity.ok(enrollment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 学生退课
    @PostMapping("/drop")
    public ResponseEntity<StudentCourseClass> dropCourse(
            @RequestParam Long studentId, @RequestParam Long courseClassId) {
        try {
            StudentCourseClass enrollment = studentCourseClassService.dropCourse(studentId, courseClassId);
            return ResponseEntity.ok(enrollment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 根据ID获取选课记录
    @GetMapping("/{id}")
    public ResponseEntity<StudentCourseClass> getEnrollmentById(@PathVariable Long id) {
        Optional<StudentCourseClass> enrollment = studentCourseClassService.findById(id);
        return enrollment.map(ResponseEntity::ok)
                        .orElse(ResponseEntity.notFound().build());
    }

    // 根据学生ID和班级ID获取选课记录
    @GetMapping("/student/{studentId}/class/{courseClassId}")
    public ResponseEntity<StudentCourseClass> getEnrollmentByStudentAndClass(
            @PathVariable Long studentId, @PathVariable Long courseClassId) {
        Optional<StudentCourseClass> enrollment = studentCourseClassService
            .findByStudentIdAndCourseClassId(studentId, courseClassId);
        return enrollment.map(ResponseEntity::ok)
                        .orElse(ResponseEntity.notFound().build());
    }

    // 根据学生ID获取选课记录
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<StudentCourseClass>> getEnrollmentsByStudentId(@PathVariable Long studentId) {
        List<StudentCourseClass> enrollments = studentCourseClassService.findByStudentId(studentId);
        return ResponseEntity.ok(enrollments);
    }

    // 根据班级ID获取选课记录
    @GetMapping("/class/{courseClassId}")
    public ResponseEntity<List<StudentCourseClass>> getEnrollmentsByCourseClassId(@PathVariable Long courseClassId) {
        List<StudentCourseClass> enrollments = studentCourseClassService.findByCourseClassId(courseClassId);
        return ResponseEntity.ok(enrollments);
    }

    // 根据状态获取选课记录
    @GetMapping("/status/{status}")
    public ResponseEntity<List<StudentCourseClass>> getEnrollmentsByStatus(@PathVariable String status) {
        List<StudentCourseClass> enrollments = studentCourseClassService.findByStatus(status);
        return ResponseEntity.ok(enrollments);
    }

    // 根据学生ID和状态获取选课记录
    @GetMapping("/student/{studentId}/status/{status}")
    public ResponseEntity<List<StudentCourseClass>> getEnrollmentsByStudentAndStatus(
            @PathVariable Long studentId, @PathVariable String status) {
        List<StudentCourseClass> enrollments = studentCourseClassService
            .findByStudentIdAndStatus(studentId, status);
        return ResponseEntity.ok(enrollments);
    }

    // 根据班级ID和状态获取选课记录
    @GetMapping("/class/{courseClassId}/status/{status}")
    public ResponseEntity<List<StudentCourseClass>> getEnrollmentsByClassAndStatus(
            @PathVariable Long courseClassId, @PathVariable String status) {
        List<StudentCourseClass> enrollments = studentCourseClassService
            .findByCourseClassIdAndStatus(courseClassId, status);
        return ResponseEntity.ok(enrollments);
    }

    // 更新选课状态
    @PatchMapping("/{id}/status")
    public ResponseEntity<StudentCourseClass> updateEnrollmentStatus(
            @PathVariable Long id, @RequestParam String status) {
        try {
            StudentCourseClass enrollment = studentCourseClassService.updateStatus(id, status);
            return ResponseEntity.ok(enrollment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 更新成绩
    @PatchMapping("/{id}/grade")
    public ResponseEntity<StudentCourseClass> updateGrade(
            @PathVariable Long id, @RequestParam Double finalGrade, @RequestParam String gradeLevel) {
        try {
            StudentCourseClass enrollment = studentCourseClassService.updateGrade(id, finalGrade, gradeLevel);
            return ResponseEntity.ok(enrollment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 统计班级中的学生数量
    @GetMapping("/class/{courseClassId}/count")
    public ResponseEntity<Long> countEnrolledStudents(@PathVariable Long courseClassId) {
        Long count = studentCourseClassService.countEnrolledStudentsByCourseClassId(courseClassId);
        return ResponseEntity.ok(count);
    }

    // 获取学生已完成的课程
    @GetMapping("/student/{studentId}/completed")
    public ResponseEntity<List<StudentCourseClass>> getCompletedCourses(@PathVariable Long studentId) {
        List<StudentCourseClass> enrollments = studentCourseClassService
            .findCompletedCoursesByStudentId(studentId);
        return ResponseEntity.ok(enrollments);
    }

    // 获取学生正在进行的课程
    @GetMapping("/student/{studentId}/enrolled")
    public ResponseEntity<List<StudentCourseClass>> getEnrolledCourses(@PathVariable Long studentId) {
        List<StudentCourseClass> enrollments = studentCourseClassService
            .findEnrolledCoursesByStudentId(studentId);
        return ResponseEntity.ok(enrollments);
    }

    // 检查学生是否已选课
    @GetMapping("/student/{studentId}/class/{courseClassId}/enrolled")
    public ResponseEntity<Boolean> isStudentEnrolled(
            @PathVariable Long studentId, @PathVariable Long courseClassId) {
        boolean isEnrolled = studentCourseClassService.isStudentEnrolled(studentId, courseClassId);
        return ResponseEntity.ok(isEnrolled);
    }

    // 获取学生选课历史
    @GetMapping("/student/{studentId}/history")
    public ResponseEntity<List<StudentCourseClass>> getStudentEnrollmentHistory(@PathVariable Long studentId) {
        List<StudentCourseClass> enrollments = studentCourseClassService
            .getStudentEnrollmentHistory(studentId);
        return ResponseEntity.ok(enrollments);
    }
    
    // 获取学生选修的课程列表（包含课程信息）
    @GetMapping("/student/{studentId}/courses")
    public ResponseEntity<List<com.facial.recognition.dto.StudentCourseDTO>> getStudentCourses(@PathVariable Long studentId) {
        List<com.facial.recognition.dto.StudentCourseDTO> courses = studentCourseClassService.getStudentCourses(studentId);
        return ResponseEntity.ok(courses);
    }
}

