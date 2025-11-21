package com.facial.recognition.controller;

import com.facial.recognition.pojo.AttendanceTask;
import com.facial.recognition.service.AttendanceTaskService;
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
            // 如果前端传了 taskName 但没传 description，或者想把 taskName 拼接到 description
            if (attendanceTask.getTaskName() != null && !attendanceTask.getTaskName().isEmpty()) {
                String desc = attendanceTask.getDescription() == null ? "" : attendanceTask.getDescription();
                // 如果 description 不包含 taskName，则拼按
                if (!desc.contains(attendanceTask.getTaskName())) {
                    attendanceTask.setDescription(attendanceTask.getTaskName() + (desc.isEmpty() ? "" : " - " + desc));
                }
            }
            
            AttendanceTask createdTask = attendanceTaskService.createAttendanceTask(attendanceTask);
            return ResponseEntity.ok(createdTask);
        } catch (Exception e) {
            e.printStackTrace();
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

    // 根据描述搜索考勤任务
    @GetMapping("/search")
    public ResponseEntity<List<AttendanceTask>> searchTasksByDescription(@RequestParam String description) {
        List<AttendanceTask> tasks = attendanceTaskService.findByTaskDescriptionContaining(description);
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
