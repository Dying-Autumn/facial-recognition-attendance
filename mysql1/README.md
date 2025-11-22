# 数据库创建说明

## 数据库信息

- **数据库名称**: `facial_recognition`
- **字符集**: `utf8mb4`
- **排序规则**: `utf8mb4_unicode_ci`

## 快速开始

### 方法一：使用命令行执行SQL脚本

```bash
mysql -uroot -p123456 < mysql/create_database_complete.sql
```

### 方法二：在MySQL客户端中执行

1. 登录MySQL：
```bash
mysql -uroot -p123456
```

2. 执行SQL脚本：
```sql
source /path/to/mysql/create_database_complete.sql
```

或者直接复制脚本内容到MySQL客户端执行。

## 数据库表结构

本脚本创建了以下12个表，与Java实体类完全匹配：

1. **roles** - 角色表
2. **User** - 用户表
3. **function_entities** - 功能权限表
4. **role_function_permissions** - 角色功能分配表
5. **face_data** - 人脸数据表
6. **courses** - 课程表
7. **teachers** - 教师表
8. **student** - 学生表
9. **CourseClass** - 课程班级表
10. **student_course_classes** - 学生课程班级关联表
11. **AttendanceTask** - 考勤任务表
12. **AttendanceRecord** - 考勤记录表

## 初始数据

脚本会自动插入以下初始数据：

### 角色
- 系统管理员 (RoleID: 1)
- 教师 (RoleID: 2)
- 学生 (RoleID: 3)

### 用户账号

**管理员账号：**
- 用户名: `admin`
- 密码: `admin123`
- 角色: 系统管理员

**教师账号：**
- 用户名: `teacher_zhang` / `teacher_li`
- 密码: `teacher123`
- 角色: 教师

**学生账号：**
- 用户名: `student001` ~ `student006`
- 密码: `student123`
- 角色: 学生

### 测试数据
- 3个测试课程
- 3个课程班级
- 6个学生选课记录
- 基础功能权限数据

## 注意事项

1. **密码安全**: 初始密码为明文存储，生产环境请使用密码加密
2. **表名大小写**: MySQL在Linux/Unix系统上区分表名大小写，请确保与实体类中的`@Table`注解一致
3. **字段映射**: 所有表名和字段名都与Java实体类中的`@Table`和`@Column`注解完全匹配
4. **外键约束**: 删除数据时请注意外键约束，建议先删除子表数据再删除父表数据

## 验证数据库

执行以下命令验证数据库是否创建成功：

```bash
mysql -uroot -p123456 -e "USE facial_recognition; SHOW TABLES;"
```

应该看到12个表。

检查初始数据：

```bash
mysql -uroot -p123456 -e "USE facial_recognition; SELECT COUNT(*) FROM roles; SELECT COUNT(*) FROM User; SELECT COUNT(*) FROM student;"
```

## 重新创建数据库

如果需要重新创建数据库（会删除所有现有数据），直接执行脚本即可：

```bash
mysql -uroot -p123456 < mysql/create_database_complete.sql
```

脚本会自动删除旧数据库并创建新的。
