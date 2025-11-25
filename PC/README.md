在当前文件夹下 打开终端输入mvn spring-boot:run 启动 并在浏览器8080端口访问
需要电脑具有Maven

### 权限校验
- 所有与课程查看相关的接口需在请求头携带 `X-User-Id`，值为当前登录用户的 `UserID`
- 角色通过 `User.RoleID -> Role.RoleName` 识别，系统内置：系统管理员、教师、学生
- 学生仅能查看自己的课程/课表；教师仅能查看自己授课的班级；管理员可查看全部

### 数据库升级
- 若已初始化数据库，请手动执行：
  - `ALTER TABLE Function ADD COLUMN Url VARCHAR(255) NULL AFTER ModuleName;`
  - `ALTER TABLE Function ADD COLUMN Method VARCHAR(20) NULL AFTER Url;`
- 新库可直接使用 `mysql/create_database.sql` 自动创建包含上述字段的 `Function` 表