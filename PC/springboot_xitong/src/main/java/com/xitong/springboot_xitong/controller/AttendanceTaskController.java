package com.xitong.springboot_xitong.controller;

import com.xitong.springboot_xitong.pojo.AttendanceTask;
import com.xitong.springboot_xitong.service.AttendanceTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/attendance-tasks")
@CrossOrigin(origins = "*")
public class AttendanceTaskController {

    @Autowired
    private AttendanceTaskService attendanceTaskService;

    // 创建考勤任务
    @PostMapping
    public ResponseEntity<AttendanceTask> createAttendanceTask(@RequestBody AttendanceTask attendanceTask) {
        try {
            AttendanceTask createdTask = attendanceTaskService.createAttendanceTask(attendanceTask);
            return ResponseEntity.ok(createdTask);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 根据ID获取考勤任务
    @GetMapping("/{id}")
    public ResponseEntity<AttendanceTask> getAttendanceTaskById(@PathVariable Long id) {
        Optional<AttendanceTask> task = attendanceTaskService.findById(id);
        return task.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }

    // 根据班级ID获取考勤任务
    @GetMapping("/class/{courseClassId}")
    public ResponseEntity<List<AttendanceTask>> getTasksByCourseClassId(@PathVariable Long courseClassId) {
        List<AttendanceTask> tasks = attendanceTaskService.findByCourseClassId(courseClassId);
        return ResponseEntity.ok(tasks);
    }

    // 根据教师ID获取考勤任务
    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<AttendanceTask>> getTasksByTeacherId(@PathVariable Long teacherId) {
        List<AttendanceTask> tasks = attendanceTaskService.findByTeacherId(teacherId);
        return ResponseEntity.ok(tasks);
    }

    // 根据状态获取考勤任务
    @GetMapping("/status/{status}")
    public ResponseEntity<List<AttendanceTask>> getTasksByStatus(@PathVariable String status) {
        List<AttendanceTask> tasks = attendanceTaskService.findByStatus(status);
        return ResponseEntity.ok(tasks);
    }

    // 根据班级ID和状态获取考勤任务
    @GetMapping("/class/{courseClassId}/status/{status}")
    public ResponseEntity<List<AttendanceTask>> getTasksByClassAndStatus(
            @PathVariable Long courseClassId, @PathVariable String status) {
        List<AttendanceTask> tasks = attendanceTaskService
            .findByCourseClassIdAndStatus(courseClassId, status);
        return ResponseEntity.ok(tasks);
    }

    // 根据教师ID和状态获取考勤任务
    @GetMapping("/teacher/{teacherId}/status/{status}")
    public ResponseEntity<List<AttendanceTask>> getTasksByTeacherAndStatus(
            @PathVariable Long teacherId, @PathVariable String status) {
        List<AttendanceTask> tasks = attendanceTaskService
            .findByTeacherIdAndStatus(teacherId, status);
        return ResponseEntity.ok(tasks);
    }

    // 根据任务名称搜索考勤任务
    @GetMapping("/search")
    public ResponseEntity<List<AttendanceTask>> searchTasksByName(@RequestParam String taskName) {
        List<AttendanceTask> tasks = attendanceTaskService.findByTaskNameContaining(taskName);
        return ResponseEntity.ok(tasks);
    }

    // 根据时间范围获取考勤任务
    @GetMapping("/time-range")
    public ResponseEntity<List<AttendanceTask>> getTasksInTimeRange(
            @RequestParam String startTime, @RequestParam String endTime) {
        try {
            LocalDateTime start = LocalDateTime.parse(startTime);
            LocalDateTime end = LocalDateTime.parse(endTime);
            List<AttendanceTask> tasks = attendanceTaskService.findTasksInTimeRange(start, end);
            return ResponseEntity.ok(tasks);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 获取当前活跃的考勤任务
    @GetMapping("/active")
    public ResponseEntity<List<AttendanceTask>> getActiveTasks() {
        LocalDateTime currentTime = LocalDateTime.now();
        List<AttendanceTask> tasks = attendanceTaskService.findActiveTasksAtTime(currentTime);
        return ResponseEntity.ok(tasks);
    }

    // 根据二维码获取考勤任务
    @GetMapping("/qr/{qrCode}")
    public ResponseEntity<AttendanceTask> getTaskByQrCode(@PathVariable String qrCode) {
        Optional<AttendanceTask> task = attendanceTaskService.findByQrCode(qrCode);
        return task.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }

    // 获取即将开始的考勤任务
    @GetMapping("/upcoming")
    public ResponseEntity<List<AttendanceTask>> getUpcomingTasks() {
        LocalDateTime currentTime = LocalDateTime.now();
        List<AttendanceTask> tasks = attendanceTaskService.findUpcomingTasks(currentTime);
        return ResponseEntity.ok(tasks);
    }

    // 更新考勤任务
    @PutMapping("/{id}")
    public ResponseEntity<AttendanceTask> updateAttendanceTask(
            @PathVariable Long id, @RequestBody AttendanceTask attendanceTask) {
        try {
            AttendanceTask updatedTask = attendanceTaskService.updateAttendanceTask(id, attendanceTask);
            return ResponseEntity.ok(updatedTask);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 更新考勤任务状态
    @PatchMapping("/{id}/status")
    public ResponseEntity<AttendanceTask> updateTaskStatus(
            @PathVariable Long id, @RequestParam String status) {
        try {
            AttendanceTask task = attendanceTaskService.updateStatus(id, status);
            return ResponseEntity.ok(task);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 删除考勤任务
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAttendanceTask(@PathVariable Long id) {
        try {
            attendanceTaskService.deleteAttendanceTask(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 生成二维码
    @PostMapping("/{id}/qr-code")
    public ResponseEntity<String> generateQrCode(@PathVariable Long id) {
        try {
            String qrCode = attendanceTaskService.generateQrCode(id);
            return ResponseEntity.ok(qrCode);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 检查考勤任务是否正在进行
    @GetMapping("/{id}/active")
    public ResponseEntity<Boolean> isTaskActive(@PathVariable Long id) {
        LocalDateTime currentTime = LocalDateTime.now();
        boolean isActive = attendanceTaskService.isTaskActive(id, currentTime);
        return ResponseEntity.ok(isActive);
    }

    // 检查考勤任务是否已过期
    @GetMapping("/{id}/expired")
    public ResponseEntity<Boolean> isTaskExpired(@PathVariable Long id) {
        LocalDateTime currentTime = LocalDateTime.now();
        boolean isExpired = attendanceTaskService.isTaskExpired(id, currentTime);
        return ResponseEntity.ok(isExpired);
    }
}
