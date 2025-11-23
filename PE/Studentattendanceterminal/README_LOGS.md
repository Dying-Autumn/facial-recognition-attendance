# 日志文件说明

## 日志文件位置

应用会在设备上创建日志文件，保存在以下位置：

```
/data/data/com.example.studentattendanceterminal/files/logs/attendance_log.txt
```

## 查看日志文件的方法

### 方法1：通过ADB命令查看（推荐）

```bash
# 1. 查看日志目录内容
adb shell run-as com.example.studentattendanceterminal ls -la files/logs/

# 2. 查看日志文件内容
adb shell run-as com.example.studentattendanceterminal cat files/logs/attendance_log.txt

# 3. 将日志文件复制到电脑
adb shell run-as com.example.studentattendanceterminal cat files/logs/attendance_log.txt > attendance_log.txt
```

### 方法2：通过应用界面查看

1. 在应用中点击"开始签到"
2. 获取位置信息后，会弹出位置信息对话框
3. 点击"查看日志"按钮，可以看到日志文件路径信息

### 方法3：通过文件管理器（需要root权限）

如果设备已root，可以直接通过文件管理器访问上述路径。

## 日志文件格式

每条日志记录包含：
- 时间戳：[2025-11-23 12:30:45.123]
- 日志级别：[ERROR] [INFO] [WARN] [DEBUG]
- 标签：[HomeFragment]
- 消息内容

示例：
```
[2025-11-23 12:30:45.123] [ERROR] [HomeFragment] 高德定位客户端未初始化
[2025-11-23 12:30:46.456] [INFO] [MainActivity] 应用启动，日志系统已初始化
```

## 日志轮转

当日志文件超过5MB时，会自动备份为带时间戳的文件，并创建新的日志文件。

## 调试建议

1. 如果遇到定位问题，首先查看日志中的错误信息
2. 检查权限是否正常授予
3. 确认高德地图API Key是否正确配置
4. 查看网络连接状态

## 常见错误排查

- **"高德定位客户端未初始化"**: 检查高德地图SDK是否正确导入
- **"定位失败，错误码：..."**: 查看高德地图定位错误码说明
- **权限相关错误**: 检查AndroidManifest.xml中的权限配置
