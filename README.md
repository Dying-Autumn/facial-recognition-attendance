# facial-recognition-attendance

人脸识别考勤系统

---

## 使用说明汇总

### Web 端

- **进入 `PC` 文件夹**，终端执行：`mvn spring-boot:run`，浏览器访问 **8080** 端口（需已安装 Maven）。
- 启动前请确保数据库满足以下要求：
  - 数据库类型：**MySQL 8.x**
  - 库名：`facial_recognition`
  - 连接参数：`jdbc:mysql://localhost:3306/facial_recognition?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true`
  - 账号密码：默认 `root / 123456`（与 `PC/src/main/resources/application.yml` 中保持一致，若修改请同步配置）
  - 初始化脚本：`mysql/create_database.sql`
- 使用管理员账户登录 Web 后，依次点击各栏目检查是否有显式报错，确认无误即可正常使用。

### 人脸识别服务

- 进入 `FaceMind` 文件夹，使用 **Python 3.10** 安装依赖（`requirements.txt`）。
- 在 `face_process` 目录下运行：
  - `uvicorn face_api:app --host 0.0.0.0 --port 8000`
- 服务启动后，Web 端人脸识别功能可用；提供测试入口可在本地验证刚上传的人脸图片是否可识别。

---

### PE（手机端）

- 安装 **Android Studio**，导入 `PE` 文件夹下的子目录 **StudentXXX**（首次导入初始化时间较长）。
- 用 USB 连接真机调试。手机需与数据库所在电脑处于同一网络。
- 在电脑终端 (`cmd`) 执行 `ipconfig`，找到 **IPv4 地址**，修改 `ApiClient.java` 中的网络地址（有注释提示），**端口号无需修改**。
- 安装 APK 后即可测试。若已执行过 `create_database.sql`，默认存在学生账号：学号 `20210001`，密码 `student123`。
- 先在电脑端发布考勤（管理员可见班级），选择学生所在班级；选地图坐标时直接在地图上点击即可（可搜索“华东理工大学 奉贤”再选宿舍/图书馆等）。
