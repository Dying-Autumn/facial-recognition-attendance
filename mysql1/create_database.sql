-- ============================================
-- 人脸识别考勤系统数据库创建脚本
-- 数据库名称：facial_recognition
-- 创建日期：2025-10-29
-- ============================================

-- 创建数据库
DROP DATABASE IF EXISTS facial_recognition;
CREATE DATABASE facial_recognition 
    DEFAULT CHARACTER SET utf8mb4 
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE facial_recognition;

-- ============================================
-- 1. 角色表 (Role)
-- ============================================
CREATE TABLE `Role` (
    `RoleID` INT PRIMARY KEY AUTO_INCREMENT COMMENT '角色ID',
    `RoleName` VARCHAR(50) NOT NULL COMMENT '角色名称',
    `RoleDescription` VARCHAR(200) COMMENT '角色描述',
    `CreatedDate` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY `uk_role_name` (`RoleName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- ============================================
-- 2. 用户表 (User)
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
    CONSTRAINT `fk_user_role` FOREIGN KEY (`RoleID`) REFERENCES `Role` (`RoleID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- ============================================
-- 3. 功能权限表 (Function)
-- ============================================
CREATE TABLE `Function` (
    `FunctionID` INT PRIMARY KEY AUTO_INCREMENT COMMENT '功能ID',
    `FunctionName` VARCHAR(100) NOT NULL COMMENT '功能名称(如:查看学生信息、新增学生信息)',
    `FunctionCode` VARCHAR(100) NOT NULL COMMENT '功能代码/标识符',
    `ModuleName` VARCHAR(50) COMMENT '所属模块(如:学生管理、课程管理)',
    `Description` VARCHAR(200) COMMENT '功能描述',
    `SortOrder` INT DEFAULT 0 COMMENT '排序顺序',
    `IsActive` TINYINT(1) DEFAULT 1 COMMENT '是否启用',
    `CreatedDate` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY `uk_function_code` (`FunctionCode`),
    KEY `idx_module` (`ModuleName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='功能权限表';

-- ============================================
-- 4. 角色功能分配表 (RoleFunctionPermission)
-- ============================================
CREATE TABLE `RoleFunctionPermission` (
    `RoleID` INT NOT NULL COMMENT '角色ID',
    `FunctionID` INT NOT NULL COMMENT '功能ID',
    `AssignedDate` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '分配时间',
    PRIMARY KEY (`RoleID`, `FunctionID`),
    KEY `idx_function_id` (`FunctionID`),
    CONSTRAINT `fk_rfp_role` FOREIGN KEY (`RoleID`) REFERENCES `Role` (`RoleID`),
    CONSTRAINT `fk_rfp_function` FOREIGN KEY (`FunctionID`) REFERENCES `Function` (`FunctionID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色功能分配表';

-- ============================================
-- 5. 人脸数据表 (FaceData)
-- ============================================
CREATE TABLE `FaceData` (
    `FaceDataID` INT PRIMARY KEY AUTO_INCREMENT COMMENT '人脸数据ID',
    `FaceTemplate` BLOB NOT NULL COMMENT '人脸特征模板(二进制数据)',
    `FaceImageURL` VARCHAR(255) COMMENT '人脸照片存储路径',
    `CreatedDate` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `UserID` INT NOT NULL COMMENT '用户ID',
    UNIQUE KEY `uk_user_id` (`UserID`),
    CONSTRAINT `fk_facedata_user` FOREIGN KEY (`UserID`) REFERENCES `User` (`UserID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='人脸数据表';

-- ============================================
-- 6. 课程表 (Course)
-- ============================================
CREATE TABLE `Course` (
    `CourseID` INT PRIMARY KEY AUTO_INCREMENT COMMENT '课程ID',
    `CourseName` VARCHAR(100) NOT NULL COMMENT '课程名称',
    `CourseCode` VARCHAR(50) NOT NULL COMMENT '课程编号',
    `Credits` DECIMAL(3,1) DEFAULT 0 COMMENT '学分',
    `Semester` VARCHAR(20) COMMENT '开课学期',
    `Description` TEXT COMMENT '课程描述',
    `CreatedDate` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY `uk_course_code` (`CourseCode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程表';

-- ============================================
-- 7. 教师表 (Teacher)
-- ============================================
CREATE TABLE `Teacher` (
    `TeacherID` INT PRIMARY KEY AUTO_INCREMENT COMMENT '教师ID',
    `JobTitle` VARCHAR(50) COMMENT '职称',
    `Department` VARCHAR(100) COMMENT '所属部门',
    `UserID` INT NOT NULL COMMENT '用户ID',
    `CreatedDate` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY `uk_user_id` (`UserID`),
    CONSTRAINT `fk_teacher_user` FOREIGN KEY (`UserID`) REFERENCES `User` (`UserID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='教师表';

-- ============================================
-- 8. 学生表 (Student)
-- ============================================
CREATE TABLE `Student` (
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
-- 9. 课程班级表 (CourseClass)
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
    CONSTRAINT `fk_class_course` FOREIGN KEY (`CourseID`) REFERENCES `Course` (`CourseID`),
    CONSTRAINT `fk_class_teacher` FOREIGN KEY (`TeacherID`) REFERENCES `Teacher` (`TeacherID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程班级表';

-- ============================================
-- 10. 学生课程班级关联表 (StudentCourseClass)
-- ============================================
CREATE TABLE `StudentCourseClass` (
    `StudentID` INT NOT NULL COMMENT '学生ID',
    `ClassID` INT NOT NULL COMMENT '班级ID',
    `EnrollmentDate` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '选课时间',
    `Status` TINYINT(1) DEFAULT 1 COMMENT '状态(1:正常 0:退课)',
    PRIMARY KEY (`StudentID`, `ClassID`),
    KEY `idx_class_id` (`ClassID`),
    CONSTRAINT `fk_scc_student` FOREIGN KEY (`StudentID`) REFERENCES `Student` (`studentid`),
    CONSTRAINT `fk_scc_class` FOREIGN KEY (`ClassID`) REFERENCES `CourseClass` (`ClassID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学生课程班级关联表';

-- ============================================
-- 11. 考勤任务表 (AttendanceTask)
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
-- 12. 考勤记录表 (AttendanceRecord)
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
    CONSTRAINT `fk_record_student` FOREIGN KEY (`StudentID`) REFERENCES `Student` (`studentid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='考勤记录表';

-- ============================================
-- 插入初始数据
-- ============================================

-- 插入默认角色
INSERT INTO `Role` (`RoleName`, `RoleDescription`) VALUES
('系统管理员', '拥有系统所有权限'),
('教师', '可以管理课程、发布考勤任务、查看考勤统计'),
('学生', '可以进行签到、查看个人考勤记录');

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
INSERT INTO `Teacher` (`JobTitle`, `Department`, `UserID`) VALUES
('教授', '计算机学院', 2),
('副教授', '计算机学院', 3);

-- 插入学生信息
INSERT INTO `Student` (`student_number`, `student_name`, `major_class`, `userid`) VALUES
('2021001001', '王小明', '计算机2101班', 4),
('2021001002', '李华', '计算机2101班', 5),
('2021001003', '张三', '计算机2102班', 6),
('2021001004', '李四', '计算机2102班', 7),
('2021001005', '王五', '计算机2103班', 8),
('2021001006', '赵六', '计算机2103班', 9);

-- 插入功能权限数据（细粒度权限）
INSERT INTO `Function` (`FunctionName`, `FunctionCode`, `ModuleName`, `Description`, `SortOrder`) VALUES
-- 仪表板模块
('访问仪表板', 'dashboard.view', '仪表板', '查看系统仪表板统计信息', 1),

-- 课程管理模块
('查看课程信息', 'course.view', '课程管理', '查看课程列表和详情', 10),
('新增课程信息', 'course.add', '课程管理', '创建新课程', 11),
('编辑课程信息', 'course.edit', '课程管理', '修改课程信息', 12),
('删除课程信息', 'course.delete', '课程管理', '删除课程', 13),
('导出课程信息', 'course.export', '课程管理', '导出课程数据', 14),

-- 教师管理模块
('查看教师信息', 'teacher.view', '教师管理', '查看教师列表和详情', 20),
('新增教师信息', 'teacher.add', '教师管理', '创建新教师账号', 21),
('编辑教师信息', 'teacher.edit', '教师管理', '修改教师信息', 22),
('删除教师信息', 'teacher.delete', '教师管理', '删除教师账号', 23),
('导出教师信息', 'teacher.export', '教师管理', '导出教师数据', 24),

-- 学生管理模块
('查看学生信息', 'student.view', '学生管理', '查看学生列表和详情', 30),
('新增学生信息', 'student.add', '学生管理', '创建新学生账号', 31),
('编辑学生信息', 'student.edit', '学生管理', '修改学生信息', 32),
('删除学生信息', 'student.delete', '学生管理', '删除学生账号', 33),
('导出学生信息', 'student.export', '学生管理', '导出学生数据', 34),

-- 课程班级管理模块
('查看班级信息', 'class.view', '班级管理', '查看课程班级列表', 40),
('创建课程班级', 'class.add', '班级管理', '创建新的课程班级', 41),
('编辑班级信息', 'class.edit', '班级管理', '修改班级信息', 42),
('删除课程班级', 'class.delete', '班级管理', '删除课程班级', 43),
('管理班级学生', 'class.manage_students', '班级管理', '添加或移除班级学生', 44),

-- 考勤任务模块
('查看考勤任务', 'attendance_task.view', '考勤任务', '查看考勤任务列表', 50),
('创建考勤任务', 'attendance_task.add', '考勤任务', '创建新的考勤任务', 51),
('编辑考勤任务', 'attendance_task.edit', '考勤任务', '修改考勤任务', 52),
('删除考勤任务', 'attendance_task.delete', '考勤任务', '删除考勤任务', 53),

-- 考勤签到模块
('进行考勤签到', 'attendance.checkin', '考勤签到', '学生进行考勤签到', 60),
('查看个人考勤', 'attendance.view_own', '考勤记录', '查看个人考勤记录', 61),
('查看全部考勤', 'attendance.view_all', '考勤记录', '查看所有学生考勤记录', 62),
('修改考勤记录', 'attendance.edit', '考勤记录', '修改考勤记录', 63),
('删除考勤记录', 'attendance.delete', '考勤记录', '删除考勤记录', 64),

-- 考勤统计模块
('查看考勤统计', 'statistics.view', '考勤统计', '查看考勤统计报表', 70),
('导出考勤统计', 'statistics.export', '考勤统计', '导出考勤统计数据', 71),

-- 人脸数据管理模块
('查看人脸数据', 'facedata.view', '人脸数据', '查看人脸数据信息', 80),
('录入人脸数据', 'facedata.add', '人脸数据', '录入新的人脸数据', 81),
('更新人脸数据', 'facedata.edit', '人脸数据', '更新人脸数据', 82),
('删除人脸数据', 'facedata.delete', '人脸数据', '删除人脸数据', 83),

-- 角色管理模块
('查看角色信息', 'role.view', '角色管理', '查看角色列表', 90),
('新增角色', 'role.add', '角色管理', '创建新角色', 91),
('编辑角色', 'role.edit', '角色管理', '修改角色信息', 92),
('删除角色', 'role.delete', '角色管理', '删除角色', 93),

-- 功能权限管理模块
('查看功能权限', 'function.view', '功能管理', '查看功能权限列表', 100),
('新增功能权限', 'function.add', '功能管理', '创建新的功能权限', 101),
('编辑功能权限', 'function.edit', '功能管理', '修改功能权限', 102),
('删除功能权限', 'function.delete', '功能管理', '删除功能权限', 103),

-- 权限分配模块
('查看权限分配', 'permission.view', '权限分配', '查看角色权限分配情况', 110),
('分配角色权限', 'permission.assign', '权限分配', '为角色分配功能权限', 111),
('撤销角色权限', 'permission.revoke', '权限分配', '撤销角色的功能权限', 112),

-- 用户管理模块
('查看用户信息', 'user.view', '用户管理', '查看用户列表', 120),
('新增用户', 'user.add', '用户管理', '创建新用户', 121),
('编辑用户', 'user.edit', '用户管理', '修改用户信息', 122),
('删除用户', 'user.delete', '用户管理', '删除用户', 123),
('重置用户密码', 'user.reset_password', '用户管理', '重置用户密码', 124),
('分配用户角色', 'user.assign_role', '用户管理', '为用户分配角色', 125);

-- 为系统管理员分配所有权限
INSERT INTO `RoleFunctionPermission` (`RoleID`, `FunctionID`)
SELECT 1, `FunctionID` FROM `Function`;

-- 为教师角色分配权限
INSERT INTO `RoleFunctionPermission` (`RoleID`, `FunctionID`)
SELECT 2, `FunctionID` FROM `Function` 
WHERE `FunctionCode` IN (
    'dashboard.view',
    'course.view',
    'student.view', 'student.export',
    'class.view', 'class.manage_students',
    'attendance_task.view', 'attendance_task.add', 'attendance_task.edit',
    'attendance.view_all', 'attendance.edit',
    'statistics.view', 'statistics.export',
    'facedata.view'
);

-- 为学生角色分配权限
INSERT INTO `RoleFunctionPermission` (`RoleID`, `FunctionID`)
SELECT 3, `FunctionID` FROM `Function` 
WHERE `FunctionCode` IN (
    'dashboard.view',
    'attendance.checkin',
    'attendance.view_own',
    'facedata.view', 'facedata.add', 'facedata.edit'
);

-- ============================================
-- 创建视图
-- ============================================

-- 视图1: 角色权限查询视图
CREATE VIEW `v_role_function_detail` AS
SELECT 
    r.RoleID,
    r.RoleName,
    r.RoleDescription,
    f.FunctionID,
    f.FunctionName,
    f.FunctionCode,
    f.ModuleName,
    f.Description AS FunctionDescription,
    rfp.AssignedDate
FROM `Role` r
LEFT JOIN `RoleFunctionPermission` rfp ON r.RoleID = rfp.RoleID
LEFT JOIN `Function` f ON rfp.FunctionID = f.FunctionID AND f.IsActive = 1
ORDER BY r.RoleID, f.ModuleName, f.SortOrder;

-- 视图2: 用户权限查询视图
CREATE VIEW `v_user_permissions` AS
SELECT 
    u.UserID,
    u.Username,
    u.RealName,
    r.RoleID,
    r.RoleName,
    f.FunctionID,
    f.FunctionName,
    f.FunctionCode,
    f.ModuleName
FROM `User` u
JOIN `Role` r ON u.RoleID = r.RoleID
JOIN `RoleFunctionPermission` rfp ON r.RoleID = rfp.RoleID
JOIN `Function` f ON rfp.FunctionID = f.FunctionID
WHERE u.IsActive = 1 AND f.IsActive = 1
ORDER BY u.UserID, f.ModuleName, f.SortOrder;

-- 视图3: 学生考勤统计视图
CREATE VIEW `v_student_attendance_stats` AS
SELECT 
    s.studentid AS StudentID,
    s.student_number AS StudentNumber,
    s.student_name AS StudentName,
    s.major_class AS MajorClass,
    cc.ClassID,
    cc.ClassName,
    c.CourseName,
    COUNT(ar.RecordID) AS TotalRecords,
    SUM(CASE WHEN ar.AttendanceResult = '正常' THEN 1 ELSE 0 END) AS NormalCount,
    SUM(CASE WHEN ar.AttendanceResult = '迟到' THEN 1 ELSE 0 END) AS LateCount,
    SUM(CASE WHEN ar.AttendanceResult = '早退' THEN 1 ELSE 0 END) AS EarlyLeaveCount,
    SUM(CASE WHEN ar.AttendanceResult = '缺勤' THEN 1 ELSE 0 END) AS AbsentCount,
    SUM(CASE WHEN ar.AttendanceResult = '请假' THEN 1 ELSE 0 END) AS LeaveCount
FROM `Student` s
JOIN `User` u ON s.userid = u.UserID
LEFT JOIN `StudentCourseClass` scc ON s.studentid = scc.StudentID
LEFT JOIN `CourseClass` cc ON scc.ClassID = cc.ClassID
LEFT JOIN `Course` c ON cc.CourseID = c.CourseID
LEFT JOIN `AttendanceTask` at ON cc.ClassID = at.ClassID
LEFT JOIN `AttendanceRecord` ar ON at.TaskID = ar.TaskID AND s.studentid = ar.StudentID
GROUP BY s.studentid, cc.ClassID;

-- 完成
SELECT '数据库创建完成！' AS Message;

