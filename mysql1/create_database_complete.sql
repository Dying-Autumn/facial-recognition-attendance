-- ============================================
-- 人脸识别考勤系统数据库创建脚本（完整版）
-- 数据库名称：facial_recognition
-- 创建日期：2025-01-XX
-- 说明：此脚本与Java实体类完全匹配
-- ============================================

-- 创建数据库
DROP DATABASE IF EXISTS facial_recognition;
CREATE DATABASE facial_recognition 
    DEFAULT CHARACTER SET utf8mb4 
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE facial_recognition;

-- ============================================
-- 1. 角色表 (roles) - 对应Role实体
-- ============================================
CREATE TABLE `roles` (
    `RoleID` INT PRIMARY KEY AUTO_INCREMENT COMMENT '角色ID',
    `roleName` VARCHAR(50) NOT NULL COMMENT '角色名称',
    `description` VARCHAR(200) COMMENT '角色描述',
    `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `status` VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '状态(ACTIVE/INACTIVE)',
    UNIQUE KEY `uk_role_name` (`roleName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- ============================================
-- 2. 用户表 (User) - 对应User实体
-- ============================================
CREATE TABLE `User` (
    `UserID` INT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    `Username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `Password` VARCHAR(255) NOT NULL COMMENT '密码',
    `RealName` VARCHAR(50) NOT NULL COMMENT '真实姓名',
    `PhoneNumber` VARCHAR(20) COMMENT '手机号',
    `Email` VARCHAR(100) COMMENT '邮箱',
    `RoleID` INT NOT NULL COMMENT '角色ID',
    `IsActive` TINYINT(1) DEFAULT 1 COMMENT '账号状态(1:启用 0:禁用)',
    `CreatedDate` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY `uk_username` (`Username`),
    KEY `idx_role_id` (`RoleID`),
    CONSTRAINT `fk_user_role` FOREIGN KEY (`RoleID`) REFERENCES `roles` (`RoleID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- ============================================
-- 3. 功能权限表 (function_entities) - 对应Function实体
-- ============================================
CREATE TABLE `function_entities` (
    `FunctionID` INT PRIMARY KEY AUTO_INCREMENT COMMENT '功能ID',
    `functionName` VARCHAR(100) NOT NULL COMMENT '功能名称',
    `description` VARCHAR(200) COMMENT '功能描述',
    `function_code` VARCHAR(100) NOT NULL COMMENT '功能代码/标识符',
    `url` VARCHAR(255) COMMENT '功能URL',
    `method` VARCHAR(20) COMMENT 'HTTP方法',
    `parent_id` INT COMMENT '父功能ID',
    `sortOrder` INT DEFAULT 0 COMMENT '排序顺序',
    `icon` VARCHAR(100) COMMENT '图标',
    `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `status` VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '状态(ACTIVE/INACTIVE)',
    UNIQUE KEY `uk_function_code` (`function_code`),
    KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='功能权限表';

-- ============================================
-- 4. 角色功能分配表 (role_function_permissions) - 对应RoleFunctionPermission实体
-- ============================================
CREATE TABLE `role_function_permissions` (
    `RoleID` INT NOT NULL COMMENT '角色ID',
    `FunctionID` INT NOT NULL COMMENT '功能ID',
    `permission_type` VARCHAR(20) DEFAULT 'READ' COMMENT '权限类型(READ/WRITE/DELETE/EXECUTE)',
    `granted` TINYINT(1) DEFAULT 1 COMMENT '是否授权',
    `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`RoleID`, `FunctionID`),
    KEY `idx_function_id` (`FunctionID`),
    CONSTRAINT `fk_rfp_role` FOREIGN KEY (`RoleID`) REFERENCES `roles` (`RoleID`),
    CONSTRAINT `fk_rfp_function` FOREIGN KEY (`FunctionID`) REFERENCES `function_entities` (`FunctionID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色功能分配表';

-- ============================================
-- 5. 人脸数据表 (face_data) - 对应FaceData实体
-- ============================================
CREATE TABLE `face_data` (
    `FaceDataID` INT PRIMARY KEY AUTO_INCREMENT COMMENT '人脸数据ID',
    `face_template` TEXT NOT NULL COMMENT '人脸特征模板(文本数据)',
    `created_date` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `UserID` INT NOT NULL COMMENT '用户ID',
    UNIQUE KEY `uk_user_id` (`UserID`),
    CONSTRAINT `fk_facedata_user` FOREIGN KEY (`UserID`) REFERENCES `User` (`UserID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='人脸数据表';

-- ============================================
-- 6. 课程表 (courses) - 对应Course实体
-- ============================================
CREATE TABLE `courses` (
    `CourceID` INT PRIMARY KEY AUTO_INCREMENT COMMENT '课程ID',
    `courseName` VARCHAR(100) NOT NULL COMMENT '课程名称',
    `courseCode` VARCHAR(50) NOT NULL COMMENT '课程编号',
    `credits` DECIMAL(3,1) DEFAULT 0 COMMENT '学分',
    `semester` VARCHAR(20) COMMENT '开课学期',
    UNIQUE KEY `uk_course_code` (`courseCode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程表';

-- ============================================
-- 7. 教师表 (teachers) - 对应Teacher实体
-- ============================================
-- 注意：实体类中userId是String类型，但这里使用INT以保持外键约束
-- 如果实体类需要String类型，请修改实体类或移除外键约束
CREATE TABLE `teachers` (
    `teacherId` INT PRIMARY KEY AUTO_INCREMENT COMMENT '教师ID',
    `title` VARCHAR(50) COMMENT '职称',
    `department` VARCHAR(100) COMMENT '所属部门',
    `userId` VARCHAR(50) NOT NULL COMMENT '用户ID（注意：实体类中为String类型）',
    UNIQUE KEY `uk_user_id` (`userId`)
    -- 注意：由于userId是VARCHAR类型，无法直接创建外键约束
    -- 建议修改实体类中的userId为Integer类型以保持数据完整性
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='教师表';

-- ============================================
-- 8. 学生表 (student) - 对应Student实体
-- ============================================
CREATE TABLE `student` (
    `studentid` INT PRIMARY KEY AUTO_INCREMENT COMMENT '学生ID',
    `student_number` VARCHAR(50) UNIQUE COMMENT '学号',
    `student_name` VARCHAR(50) NOT NULL COMMENT '姓名',
    `major_class` VARCHAR(100) COMMENT '所属班级',
    `userid` INT NOT NULL COMMENT '用户ID',
    `created_date` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY `uk_user_id` (`userid`),
    CONSTRAINT `fk_student_user` FOREIGN KEY (`userid`) REFERENCES `User` (`UserID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学生表';

-- ============================================
-- 9. 课程班级表 (CourseClass) - 对应CourseClass实体
-- ============================================
CREATE TABLE `CourseClass` (
    `ClassID` INT PRIMARY KEY AUTO_INCREMENT COMMENT '班级ID',
    `ClassName` VARCHAR(100) NOT NULL COMMENT '班级名称/代号',
    `CourseID` INT NOT NULL COMMENT '课程ID',
    `TeacherID` INT NOT NULL COMMENT '教师ID',
    `Semester` VARCHAR(20) COMMENT '学期',
    `ClassTime` VARCHAR(100) COMMENT '上课时间',
    `ClassLocation` VARCHAR(100) COMMENT '上课地点',
    `CreatedDate` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    KEY `idx_course_id` (`CourseID`),
    KEY `idx_teacher_id` (`TeacherID`),
    CONSTRAINT `fk_class_course` FOREIGN KEY (`CourseID`) REFERENCES `courses` (`CourceID`),
    CONSTRAINT `fk_class_teacher` FOREIGN KEY (`TeacherID`) REFERENCES `teachers` (`teacherId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程班级表';

-- ============================================
-- 10. 学生课程班级关联表 (student_course_classes) - 对应StudentCourseClass实体
-- ============================================
CREATE TABLE `student_course_classes` (
    `StudentID` INT NOT NULL COMMENT '学生ID',
    `ClassID` INT NOT NULL COMMENT '班级ID',
    `enrollment_date` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '选课时间',
    `status` VARCHAR(20) DEFAULT 'ENROLLED' COMMENT '状态(ENROLLED/DROPPED/COMPLETED)',
    `finalGrade` DECIMAL(5,2) COMMENT '最终成绩',
    `gradeLevel` VARCHAR(10) COMMENT '成绩等级(A/B/C/D/F)',
    PRIMARY KEY (`StudentID`, `ClassID`),
    KEY `idx_class_id` (`ClassID`),
    CONSTRAINT `fk_scc_student` FOREIGN KEY (`StudentID`) REFERENCES `student` (`studentid`),
    CONSTRAINT `fk_scc_class` FOREIGN KEY (`ClassID`) REFERENCES `CourseClass` (`ClassID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学生课程班级关联表';

-- ============================================
-- 11. 考勤任务表 (AttendanceTask) - 对应AttendanceTask实体
-- ============================================
CREATE TABLE `AttendanceTask` (
    `TaskID` INT PRIMARY KEY AUTO_INCREMENT COMMENT '任务ID',
    `StartTime` DATETIME NOT NULL COMMENT '开始时间',
    `EndTime` DATETIME NOT NULL COMMENT '结束时间',
    `LocationRange` VARCHAR(200) COMMENT '定位范围(GPS坐标/半径)',
    `Latitude` DECIMAL(10, 7) COMMENT '纬度',
    `Longitude` DECIMAL(10, 7) COMMENT '经度',
    `Radius` INT DEFAULT 30 COMMENT '有效范围半径(米)',
    `ClassID` INT NOT NULL COMMENT '班级ID',
    `CreatedDate` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    KEY `idx_class_id` (`ClassID`),
    KEY `idx_start_time` (`StartTime`),
    CONSTRAINT `fk_task_class` FOREIGN KEY (`ClassID`) REFERENCES `CourseClass` (`ClassID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='考勤任务表';

-- ============================================
-- 12. 考勤记录表 (AttendanceRecord) - 对应AttendanceRecord实体
-- ============================================
CREATE TABLE `AttendanceRecord` (
    `RecordID` INT PRIMARY KEY AUTO_INCREMENT COMMENT '记录ID',
    `CheckInTime` DATETIME NOT NULL COMMENT '签到时间',
    `ActualLatitude` DECIMAL(10, 7) COMMENT '实际签到纬度',
    `ActualLongitude` DECIMAL(10, 7) COMMENT '实际签到经度',
    `VerificationPhotoURL` VARCHAR(255) COMMENT '人脸识别照片证据',
    `ConfidenceScore` DECIMAL(5, 2) COMMENT '识别可信度(0-100)',
    `AttendanceResult` VARCHAR(20) NOT NULL COMMENT '签到结果(正常/迟到/早退/缺勤/请假)',
    `TaskID` INT NOT NULL COMMENT '任务ID',
    `StudentID` INT NOT NULL COMMENT '学生ID',
    `Remark` VARCHAR(200) COMMENT '备注',
    `CreatedDate` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    KEY `idx_task_id` (`TaskID`),
    KEY `idx_student_id` (`StudentID`),
    KEY `idx_checkin_time` (`CheckInTime`),
    CONSTRAINT `fk_record_task` FOREIGN KEY (`TaskID`) REFERENCES `AttendanceTask` (`TaskID`),
    CONSTRAINT `fk_record_student` FOREIGN KEY (`StudentID`) REFERENCES `student` (`studentid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='考勤记录表';

-- ============================================
-- 插入初始数据
-- ============================================

-- 插入默认角色
INSERT INTO `roles` (`roleName`, `description`, `status`) VALUES
('系统管理员', '拥有系统所有权限', 'ACTIVE'),
('教师', '可以管理课程、发布考勤任务、查看考勤统计', 'ACTIVE'),
('学生', '可以进行签到、查看个人考勤记录', 'ACTIVE');

-- 插入默认管理员账号 (密码: admin123，实际应用需要加密)
INSERT INTO `User` (`Username`, `Password`, `RealName`, `PhoneNumber`, `Email`, `RoleID`) VALUES
('admin', 'admin123', '系统管理员', '13800138000', 'admin@example.com', 1);

-- 插入测试教师账号
INSERT INTO `User` (`Username`, `Password`, `RealName`, `PhoneNumber`, `Email`, `RoleID`) VALUES
('teacher_zhang', 'teacher123', '张老师', '13900001001', 'zhang@example.com', 2),
('teacher_li', 'teacher123', '李老师', '13900001002', 'li@example.com', 2);

-- 插入测试学生账号
INSERT INTO `User` (`Username`, `Password`, `RealName`, `PhoneNumber`, `Email`, `RoleID`) VALUES
('student001', 'student123', '王小明', '13900002001', 'wangxm@example.com', 3),
('student002', 'student123', '李华', '13900002002', 'lihua@example.com', 3),
('student003', 'student123', '张三', '13900002003', 'zhangsan@example.com', 3),
('student004', 'student123', '李四', '13900002004', 'lisi@example.com', 3),
('student005', 'student123', '王五', '13900002005', 'wangwu@example.com', 3),
('student006', 'student123', '赵六', '13900002006', 'zhaoliu@example.com', 3);

-- 插入教师信息
INSERT INTO `teachers` (`title`, `department`, `userId`) VALUES
('教授', '计算机学院', '2'),
('副教授', '计算机学院', '3');

-- 插入学生信息
INSERT INTO `student` (`student_number`, `student_name`, `major_class`, `userid`) VALUES
('2021001001', '王小明', '计算机2101班', 4),
('2021001002', '李华', '计算机2101班', 5),
('2021001003', '张三', '计算机2102班', 6),
('2021001004', '李四', '计算机2102班', 7),
('2021001005', '王五', '计算机2103班', 8),
('2021001006', '赵六', '计算机2103班', 9);

-- 插入测试课程
INSERT INTO `courses` (`courseName`, `courseCode`, `credits`, `semester`) VALUES
('Java程序设计', 'CS101', 3.0, '2025-春'),
('数据库原理', 'CS102', 3.0, '2025-春'),
('Web开发技术', 'CS103', 2.5, '2025-春');

-- 插入课程班级
INSERT INTO `CourseClass` (`ClassName`, `CourseID`, `TeacherID`, `Semester`, `ClassTime`, `ClassLocation`) VALUES
('Java程序设计-01班', 1, 1, '2025-春', '周一 8:00-10:00', '教学楼A101'),
('数据库原理-01班', 2, 1, '2025-春', '周三 10:00-12:00', '教学楼A102'),
('Web开发技术-01班', 3, 2, '2025-春', '周五 14:00-16:00', '教学楼B201');

-- 学生选课
INSERT INTO `student_course_classes` (`StudentID`, `ClassID`, `status`) VALUES
(1, 1, 'ENROLLED'),
(2, 1, 'ENROLLED'),
(3, 2, 'ENROLLED'),
(4, 2, 'ENROLLED'),
(5, 3, 'ENROLLED'),
(6, 3, 'ENROLLED');

-- 插入功能权限数据（基础功能）
INSERT INTO `function_entities` (`functionName`, `function_code`, `description`, `url`, `method`, `sortOrder`, `status`) VALUES
-- 仪表板模块
('访问仪表板', 'dashboard.view', '查看系统仪表板统计信息', '/api/dashboard', 'GET', 1, 'ACTIVE'),

-- 课程管理模块
('查看课程信息', 'course.view', '查看课程列表和详情', '/api/courses', 'GET', 10, 'ACTIVE'),
('新增课程信息', 'course.add', '创建新课程', '/api/courses', 'POST', 11, 'ACTIVE'),
('编辑课程信息', 'course.edit', '修改课程信息', '/api/courses/{id}', 'PUT', 12, 'ACTIVE'),
('删除课程信息', 'course.delete', '删除课程', '/api/courses/{id}', 'DELETE', 13, 'ACTIVE'),

-- 教师管理模块
('查看教师信息', 'teacher.view', '查看教师列表和详情', '/api/teachers', 'GET', 20, 'ACTIVE'),
('新增教师信息', 'teacher.add', '创建新教师账号', '/api/teachers', 'POST', 21, 'ACTIVE'),
('编辑教师信息', 'teacher.edit', '修改教师信息', '/api/teachers/{id}', 'PUT', 22, 'ACTIVE'),
('删除教师信息', 'teacher.delete', '删除教师账号', '/api/teachers/{id}', 'DELETE', 23, 'ACTIVE'),

-- 学生管理模块
('查看学生信息', 'student.view', '查看学生列表和详情', '/api/students', 'GET', 30, 'ACTIVE'),
('新增学生信息', 'student.add', '创建新学生账号', '/api/students', 'POST', 31, 'ACTIVE'),
('编辑学生信息', 'student.edit', '修改学生信息', '/api/students/{id}', 'PUT', 32, 'ACTIVE'),
('删除学生信息', 'student.delete', '删除学生账号', '/api/students/{id}', 'DELETE', 33, 'ACTIVE'),

-- 课程班级管理模块
('查看班级信息', 'class.view', '查看课程班级列表', '/api/classes', 'GET', 40, 'ACTIVE'),
('创建课程班级', 'class.add', '创建新的课程班级', '/api/classes', 'POST', 41, 'ACTIVE'),
('编辑班级信息', 'class.edit', '修改班级信息', '/api/classes/{id}', 'PUT', 42, 'ACTIVE'),
('删除课程班级', 'class.delete', '删除课程班级', '/api/classes/{id}', 'DELETE', 43, 'ACTIVE'),

-- 考勤任务模块
('查看考勤任务', 'attendance_task.view', '查看考勤任务列表', '/api/attendance-tasks', 'GET', 50, 'ACTIVE'),
('创建考勤任务', 'attendance_task.add', '创建新的考勤任务', '/api/attendance-tasks', 'POST', 51, 'ACTIVE'),
('编辑考勤任务', 'attendance_task.edit', '修改考勤任务', '/api/attendance-tasks/{id}', 'PUT', 52, 'ACTIVE'),
('删除考勤任务', 'attendance_task.delete', '删除考勤任务', '/api/attendance-tasks/{id}', 'DELETE', 53, 'ACTIVE'),

-- 考勤签到模块
('进行考勤签到', 'attendance.checkin', '学生进行考勤签到', '/api/attendance/checkin', 'POST', 60, 'ACTIVE'),
('查看个人考勤', 'attendance.view_own', '查看个人考勤记录', '/api/attendance/own', 'GET', 61, 'ACTIVE'),
('查看全部考勤', 'attendance.view_all', '查看所有学生考勤记录', '/api/attendance', 'GET', 62, 'ACTIVE'),

-- 角色管理模块
('查看角色信息', 'role.view', '查看角色列表', '/api/roles', 'GET', 90, 'ACTIVE'),
('新增角色', 'role.add', '创建新角色', '/api/roles', 'POST', 91, 'ACTIVE'),
('编辑角色', 'role.edit', '修改角色信息', '/api/roles/{id}', 'PUT', 92, 'ACTIVE'),
('删除角色', 'role.delete', '删除角色', '/api/roles/{id}', 'DELETE', 93, 'ACTIVE');

-- 为系统管理员分配所有权限
INSERT INTO `role_function_permissions` (`RoleID`, `FunctionID`, `granted`)
SELECT 1, `FunctionID`, 1 FROM `function_entities`;

-- 为教师角色分配权限
INSERT INTO `role_function_permissions` (`RoleID`, `FunctionID`, `granted`)
SELECT 2, `FunctionID`, 1 FROM `function_entities` 
WHERE `function_code` IN (
    'dashboard.view',
    'course.view',
    'student.view',
    'class.view',
    'attendance_task.view', 'attendance_task.add', 'attendance_task.edit',
    'attendance.view_all'
);

-- 为学生角色分配权限
INSERT INTO `role_function_permissions` (`RoleID`, `FunctionID`, `granted`)
SELECT 3, `FunctionID`, 1 FROM `function_entities` 
WHERE `function_code` IN (
    'dashboard.view',
    'attendance.checkin',
    'attendance.view_own'
);

-- ============================================
-- 完成
-- ============================================
SELECT '数据库创建完成！' AS Message;

