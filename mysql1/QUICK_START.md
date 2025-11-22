# 数据库快速开始指南

## 一键创建数据库

```bash
mysql -uroot -p123456 < mysql/create_database_complete.sql
```

## 验证数据库

```bash
# 查看所有表
mysql -uroot -p123456 -e "USE facial_recognition; SHOW TABLES;"

# 查看数据统计
mysql -uroot -p123456 -e "USE facial_recognition; SELECT 'Roles' as TableName, COUNT(*) as Count FROM roles UNION SELECT 'Users', COUNT(*) FROM User UNION SELECT 'Students', COUNT(*) FROM student;"
```

## 默认账号

### 管理员
- 用户名: `admin`
- 密码: `admin123`

### 教师
- 用户名: `teacher_zhang` 或 `teacher_li`
- 密码: `teacher123`

### 学生
- 用户名: `student001` ~ `student006`
- 密码: `student123`

## 数据库连接信息

- 数据库名: `facial_recognition`
- 主机: `localhost`
- 端口: `3306`
- 用户名: `root`
- 密码: `123456`

## 注意事项

⚠️ **重要提示**：
1. Teacher实体类中的`userId`字段是String类型，数据库中也使用VARCHAR类型以保持匹配
2. Course实体类中的主键字段名是`CourceID`（拼写错误），数据库脚本中保持一致
3. 生产环境请修改默认密码并启用密码加密

