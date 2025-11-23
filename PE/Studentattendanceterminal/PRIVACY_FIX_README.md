# 高德地图SDK隐私合规修复

## 问题描述

在高德地图定位SDK 6.x版本中，必须在使用SDK任何接口前先调用隐私合规接口，否则会出现崩溃。

错误信息：
```
***确保调用SDK任何接口前先调用更新隐私合规updatePrivacyShow、updatePrivacyAgree两个接口并且参数值都为true，若未正确设置有崩溃风险***
```

## 解决方案

### 1. 创建自定义Application类

在 `AttendanceApplication.java` 中，在应用启动时设置隐私合规：

```java
// 设置隐私权政策是否弹窗告知用户
AMapLocationClient.updatePrivacyShow(this, true, true);
// 设置隐私权政策是否取得用户同意
AMapLocationClient.updatePrivacyAgree(this, true);
```

### 2. 注册Application类

在 `AndroidManifest.xml` 中注册自定义Application：

```xml
<application
    android:name=".AttendanceApplication"
    ...>
```

### 3. 初始化顺序

确保隐私合规设置在任何SDK调用之前完成：
1. Application.onCreate() - 设置隐私合规
2. Activity/Fragment中初始化SDK

## 代码变更

### 新增文件：
- `AttendanceApplication.java` - 自定义Application类

### 修改文件：
- `AndroidManifest.xml` - 注册Application类
- `MainActivity.java` - 移除重复初始化
- `HomeFragment.java` - 简化初始化逻辑

## 测试验证

运行应用后，查看日志文件确认：
```
[时间戳] [INFO] [AttendanceApplication] 高德地图SDK隐私合规设置完成
[时间戳] [INFO] [HomeFragment] 高德地图定位客户端初始化成功
```

如果没有看到错误日志 `[ERROR] [HomeFragment] 初始化高德定位SDK失败`，说明修复成功。

## 注意事项

1. **设置时机**：必须在Application.onCreate()中设置，不能在Activity中
2. **参数说明**：
   - `updatePrivacyShow(context, show, agree)` - 是否显示隐私政策弹窗
   - `updatePrivacyAgree(context, agree)` - 用户是否同意隐私政策
3. **版本兼容**：这个要求从高德地图SDK v3.7.0开始实施，6.x版本强制要求

## 相关文档

- [高德地图SDK隐私合规说明](https://lbs.amap.com/api/android-location-sdk/guide/utilities/privacy)
- [Android Application类](https://developer.android.com/reference/android/app/Application)
