# Android应用数据存储和日志查看说明

## 📁 本地数据存储位置

### 应用包名
```
com.example.studentattendanceterminal
```

### 数据库文件位置

**数据库名称：** `attendance.db`

**存储位置：** Android应用的私有数据库目录

**完整路径：**
```
/data/data/com.example.studentattendanceterminal/databases/attendance.db
```

**代码位置：**
- 文件：`app/src/main/java/com/example/studentattendanceterminal/db/AttendanceDbHelper.java`
- 数据库名称定义：第11行 `public static final String DB_NAME = "attendance.db";`
- 数据库创建：第38-40行，使用 `SQLiteOpenHelper` 创建

**数据库表结构：**
- `AttendanceRecord` - 考勤记录表
- `Student` - 学生表
- `Course` - 课程表

### 日志文件位置

**日志文件名：** `attendance_log.txt`

**存储位置：** Android应用的私有文件目录下的 `logs` 文件夹

**完整路径：**
```
/data/data/com.example.studentattendanceterminal/files/logs/attendance_log.txt
```

**代码位置：**
- 文件：`app/src/main/java/com/example/studentattendanceterminal/utils/LogUtil.java`
- 日志目录创建：第34行 `File logDir = new File(appContext.getFilesDir(), "logs");`
- 日志文件创建：第38行 `logFile = new File(logDir, LOG_FILE_NAME);`
- 日志初始化：`app/src/main/java/com/example/studentattendanceterminal/AttendanceApplication.java` 第17行

**日志文件特点：**
- 最大文件大小：5MB（超过后自动备份并创建新文件）
- 日志格式：`[时间戳] [级别] [标签] 消息内容`
- 日志级别：DEBUG、INFO、WARN、ERROR

### SharedPreferences 存储位置

**存储位置：** Android应用的私有SharedPreferences目录

**完整路径：**
```
/data/data/com.example.studentattendanceterminal/shared_prefs/auth_prefs.xml
```

**存储内容：**
- `student_id` - 学生ID
- `student_number` - 学号
- `student_name` - 学生姓名

**代码位置：**
- `app/src/main/java/com/example/studentattendanceterminal/ui/MeFragment.java` 第185-191行

---

## 📱 使用ADB命令在电脑端查看日志和数据

### 前置准备

1. **安装ADB工具**
   - Windows: 下载 [Android Platform Tools](https://developer.android.com/studio/releases/platform-tools)
   - Mac/Linux: `brew install android-platform-tools` 或通过Android Studio安装

2. **启用USB调试**
   - 在手机上：设置 → 关于手机 → 连续点击版本号7次启用开发者选项
   - 设置 → 开发者选项 → 启用USB调试

3. **连接设备**
   ```bash
   # 检查设备是否连接
   adb devices
   
   # 应该显示类似：
   # List of devices attached
   # ABC123456789    device
   ```

### 1. 查看实时日志（Logcat）

**基本命令：**
```bash
adb logcat
```

**过滤特定标签：**
```bash
# 查看应用日志
adb logcat -s AttendanceApp

# 查看HomeFragment日志
adb logcat -s HomeFragment

# 查看所有应用日志
adb logcat | grep "com.example.studentattendanceterminal"
```

**按日志级别过滤：**
```bash
# 只显示ERROR级别
adb logcat *:E

# 只显示WARN和ERROR
adb logcat *:W

# 显示INFO及以上级别
adb logcat *:I
```

**清除日志并重新开始：**
```bash
adb logcat -c && adb logcat
```

**保存日志到文件：**
```bash
# 保存到当前目录
adb logcat > logcat.txt

# 保存并实时查看
adb logcat | tee logcat.txt

# 保存特定标签的日志
adb logcat -s AttendanceApp > app_log.txt
```

### 2. 查看应用日志文件

**查看日志文件内容：**
```bash
adb shell run-as com.example.studentattendanceterminal cat files/logs/attendance_log.txt
```

**查看日志文件目录：**
```bash
adb shell run-as com.example.studentattendanceterminal ls -la files/logs/
```

**将日志文件导出到电脑：**
```bash
# 方法1：先复制到sdcard，再拉取（推荐）
adb shell run-as com.example.studentattendanceterminal cp files/logs/attendance_log.txt /sdcard/attendance_log.txt
adb pull /sdcard/attendance_log.txt ./
adb shell rm /sdcard/attendance_log.txt

# 方法2：直接使用exec-out（Windows PowerShell）
adb exec-out run-as com.example.studentattendanceterminal cat files/logs/attendance_log.txt > attendance_log.txt

# 方法3：使用adb shell + 重定向（Linux/Mac）
adb shell "run-as com.example.studentattendanceterminal cat files/logs/attendance_log.txt" > attendance_log.txt
```

**实时监控日志文件变化（Linux/Mac）：**
```bash
adb shell run-as com.example.studentattendanceterminal tail -f files/logs/attendance_log.txt
```

### 3. 查看数据库文件

**查看数据库文件列表：**
```bash
adb shell run-as com.example.studentattendanceterminal ls -la databases/
```

**导出数据库文件到电脑：**
```bash
# 先复制到sdcard
adb shell run-as com.example.studentattendanceterminal cp databases/attendance.db /sdcard/attendance.db

# 拉取到电脑当前目录
adb pull /sdcard/attendance.db ./

# 清理sdcard上的临时文件
adb shell rm /sdcard/attendance.db
```

**使用SQLite查看数据库（需要安装sqlite3）：**
```bash
# 导出数据库后，使用sqlite3查看
sqlite3 attendance.db

# 在sqlite3中执行SQL命令
.tables                    # 查看所有表
.schema AttendanceRecord   # 查看表结构
SELECT * FROM AttendanceRecord;  # 查看所有记录
SELECT * FROM AttendanceRecord WHERE StudentID = 1;  # 查看特定学生的记录
.quit                      # 退出sqlite3
```

**直接在设备上查询数据库（无需导出）：**
```bash
# 查看表结构
adb shell run-as com.example.studentattendanceterminal sqlite3 databases/attendance.db ".schema AttendanceRecord"

# 查看所有记录
adb shell run-as com.example.studentattendanceterminal sqlite3 databases/attendance.db "SELECT * FROM AttendanceRecord;"

# 查看特定学生的记录
adb shell run-as com.example.studentattendanceterminal sqlite3 databases/attendance.db "SELECT * FROM AttendanceRecord WHERE StudentID = 1;"
```

### 4. 查看SharedPreferences

**查看SharedPreferences文件内容：**
```bash
adb shell run-as com.example.studentattendanceterminal cat shared_prefs/auth_prefs.xml
```

**导出SharedPreferences到电脑：**
```bash
adb shell run-as com.example.studentattendanceterminal cp shared_prefs/auth_prefs.xml /sdcard/auth_prefs.xml
adb pull /sdcard/auth_prefs.xml ./
adb shell rm /sdcard/auth_prefs.xml
```

### 5. 常用组合命令

**查看应用的所有文件：**
```bash
adb shell run-as com.example.studentattendanceterminal ls -la
```

**查看应用数据目录结构：**
```bash
adb shell run-as com.example.studentattendanceterminal find . -type f
```

**一键导出所有应用数据到电脑：**
```bash
# 创建导出目录
mkdir -p app_data_export

# 导出日志
adb exec-out run-as com.example.studentattendanceterminal cat files/logs/attendance_log.txt > app_data_export/attendance_log.txt

# 导出数据库
adb shell run-as com.example.studentattendanceterminal cp databases/attendance.db /sdcard/attendance.db
adb pull /sdcard/attendance.db app_data_export/
adb shell rm /sdcard/attendance.db

# 导出SharedPreferences
adb shell run-as com.example.studentattendanceterminal cp shared_prefs/auth_prefs.xml /sdcard/auth_prefs.xml
adb pull /sdcard/auth_prefs.xml app_data_export/
adb shell rm /sdcard/auth_prefs.xml

echo "所有数据已导出到 app_data_export 目录"
```

**Windows PowerShell版本（一键导出）：**
```powershell
# 创建导出目录
New-Item -ItemType Directory -Force -Path app_data_export

# 导出日志
adb exec-out run-as com.example.studentattendanceterminal cat files/logs/attendance_log.txt | Out-File -FilePath app_data_export\attendance_log.txt -Encoding utf8

# 导出数据库
adb shell run-as com.example.studentattendanceterminal cp databases/attendance.db /sdcard/attendance.db
adb pull /sdcard/attendance.db app_data_export/
adb shell rm /sdcard/attendance.db

# 导出SharedPreferences
adb shell run-as com.example.studentattendanceterminal cp shared_prefs/auth_prefs.xml /sdcard/auth_prefs.xml
adb pull /sdcard/auth_prefs.xml app_data_export/
adb shell rm /sdcard/auth_prefs.xml

Write-Host "所有数据已导出到 app_data_export 目录"
```

---

## 🔍 调试技巧

### 1. 过滤特定时间段的日志
```bash
# 查看最近10分钟的日志
adb logcat -t 10

# 查看最近100行日志
adb logcat -t 100
```

### 2. 查看崩溃日志
```bash
# 查看ANR（应用无响应）日志
adb shell cat /data/anr/traces.txt

# 查看崩溃日志
adb logcat | grep -i "fatal\|exception\|crash"

# 查看应用崩溃堆栈
adb logcat *:E | grep "com.example.studentattendanceterminal"
```

### 3. 监控特定进程
```bash
# 先获取应用进程ID
adb shell pidof com.example.studentattendanceterminal

# 然后监控该进程
adb logcat --pid=<进程ID>
```

### 4. 清除应用数据（调试用）
```bash
# 清除应用所有数据（包括数据库、SharedPreferences、日志等）
adb shell pm clear com.example.studentattendanceterminal

# 只清除应用缓存
adb shell pm clear --cache-only com.example.studentattendanceterminal
```

### 5. 查看应用信息
```bash
# 查看应用版本信息
adb shell dumpsys package com.example.studentattendanceterminal | grep version

# 查看应用安装路径
adb shell pm path com.example.studentattendanceterminal

# 查看应用权限
adb shell dumpsys package com.example.studentattendanceterminal | grep permission
```

---

## 📝 注意事项

1. **权限要求：**
   - 使用 `run-as` 命令需要设备已root或使用debug签名
   - 如果无法使用 `run-as`，需要root权限才能访问应用私有目录
   - Debug版本的应用可以直接使用 `run-as`

2. **设备连接：**
   - 确保设备已通过USB连接并启用USB调试
   - 使用 `adb devices` 检查设备连接状态
   - 如果设备未显示，尝试：`adb kill-server && adb start-server`

3. **日志文件大小：**
   - 日志文件超过5MB会自动备份
   - 备份文件名格式：`attendance_log_<时间戳>.txt`
   - 可以通过 `ls -la files/logs/` 查看所有日志文件

4. **数据库版本：**
   - 当前数据库版本：4
   - 升级时会自动删除旧表并重建（见 `AttendanceDbHelper.java` 第94-103行）
   - 生产环境建议实现数据迁移逻辑

5. **数据安全：**
   - 所有数据存储在应用私有目录，其他应用无法访问
   - 卸载应用时，所有数据会被删除
   - 建议定期备份重要数据

6. **Windows PowerShell注意事项：**
   - 使用 `exec-out` 而不是 `shell` + 重定向
   - 文件路径使用反斜杠 `\` 或正斜杠 `/` 都可以
   - 输出重定向使用 `>` 或 `Out-File`

---

## 🛠️ 快速参考

| 数据类型 | 文件路径 | 查看命令 | 导出命令 |
|---------|---------|---------|---------|
| 日志文件 | `/data/data/com.example.studentattendanceterminal/files/logs/attendance_log.txt` | `adb shell run-as com.example.studentattendanceterminal cat files/logs/attendance_log.txt` | `adb exec-out run-as com.example.studentattendanceterminal cat files/logs/attendance_log.txt > log.txt` |
| 数据库 | `/data/data/com.example.studentattendanceterminal/databases/attendance.db` | `adb shell run-as com.example.studentattendanceterminal sqlite3 databases/attendance.db ".tables"` | `adb shell run-as com.example.studentattendanceterminal cp databases/attendance.db /sdcard/attendance.db && adb pull /sdcard/attendance.db ./` |
| SharedPreferences | `/data/data/com.example.studentattendanceterminal/shared_prefs/auth_prefs.xml` | `adb shell run-as com.example.studentattendanceterminal cat shared_prefs/auth_prefs.xml` | `adb shell run-as com.example.studentattendanceterminal cp shared_prefs/auth_prefs.xml /sdcard/auth_prefs.xml && adb pull /sdcard/auth_prefs.xml ./` |
| 实时日志 | Logcat | `adb logcat -s AttendanceApp` | `adb logcat > logcat.txt` |

---

## 📚 相关代码文件

- **数据库操作：** `app/src/main/java/com/example/studentattendanceterminal/db/AttendanceDbHelper.java`
- **日志工具：** `app/src/main/java/com/example/studentattendanceterminal/utils/LogUtil.java`
- **应用初始化：** `app/src/main/java/com/example/studentattendanceterminal/AttendanceApplication.java`
- **登录信息存储：** `app/src/main/java/com/example/studentattendanceterminal/ui/MeFragment.java`

---

## 💡 实用脚本示例

### 快速查看最新日志（Linux/Mac）
```bash
#!/bin/bash
# 保存为 view_log.sh
adb shell run-as com.example.studentattendanceterminal tail -n 50 files/logs/attendance_log.txt
```

### 快速导出所有数据（Linux/Mac）
```bash
#!/bin/bash
# 保存为 export_data.sh
OUTPUT_DIR="app_data_$(date +%Y%m%d_%H%M%S)"
mkdir -p "$OUTPUT_DIR"

echo "导出日志..."
adb exec-out run-as com.example.studentattendanceterminal cat files/logs/attendance_log.txt > "$OUTPUT_DIR/attendance_log.txt"

echo "导出数据库..."
adb shell run-as com.example.studentattendanceterminal cp databases/attendance.db /sdcard/attendance.db
adb pull /sdcard/attendance.db "$OUTPUT_DIR/"
adb shell rm /sdcard/attendance.db

echo "导出SharedPreferences..."
adb shell run-as com.example.studentattendanceterminal cp shared_prefs/auth_prefs.xml /sdcard/auth_prefs.xml
adb pull /sdcard/auth_prefs.xml "$OUTPUT_DIR/"
adb shell rm /sdcard/auth_prefs.xml

echo "数据已导出到: $OUTPUT_DIR"
```

### 快速查看最新日志（Windows PowerShell）
```powershell
# 保存为 view_log.ps1
adb exec-out run-as com.example.studentattendanceterminal cat files/logs/attendance_log.txt | Select-Object -Last 50
```

