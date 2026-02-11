# 一句话小部件 - 开发文档

## 项目概述
这是一个为小米手机（Android系统通用）开发的桌面小部件（Widget），用于展示随机的一句话名言。

## 功能特性
- ✅ 4x2 大小的桌面小部件
- ✅ 每小时自动更新一次内容
- ✅ 点击小部件可立即刷新
- ✅ 展示一句话和作者信息
- ✅ 优雅的圆角卡片设计

## 项目结构

```
hitokoto-widget/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/hitokotowidget/
│   │   │   │   ├── HitokotoWidget.java          # Widget主类
│   │   │   │   └── HitokotoService.java         # 网络请求服务
│   │   │   ├── res/
│   │   │   │   ├── layout/
│   │   │   │   │   └── widget_layout.xml        # Widget布局
│   │   │   │   ├── drawable/
│   │   │   │   │   └── widget_background.xml    # 背景样式
│   │   │   │   ├── xml/
│   │   │   │   │   └── widget_info.xml          # Widget配置
│   │   │   │   └── values/
│   │   │   │       └── strings.xml              # 字符串资源
│   │   │   └── AndroidManifest.xml              # 应用清单
│   │   └── build.gradle                         # 构建配置
```

## 核心组件说明

### 1. HitokotoWidget.java
AppWidgetProvider的实现类，负责：
- Widget的生命周期管理
- 设置定时更新（AlarmManager）
- 处理用户点击事件
- 触发数据更新

### 2. HitokotoService.java
IntentService实现，负责：
- 在后台线程中请求API
- 解析JSON数据
- 更新Widget界面

### 3. widget_layout.xml
Widget的UI布局：
- 使用LinearLayout垂直排列
- TextView显示一句话内容
- TextView显示作者信息

### 4. widget_info.xml
Widget配置信息：
- 尺寸：4x2（250dp x 110dp）
- 更新频率：1小时（3600000ms）
- 支持水平和垂直调整大小

## 开发步骤

### 1. 创建Android Studio项目
```bash
# 使用Android Studio创建新项目
# 选择 "Empty Activity"
# 包名：com.example.hitokotowidget
# 最低SDK：API 21 (Android 5.0)
```

### 2. 文件放置位置
将上述文件按以下路径放置：

```
app/src/main/
├── java/com/example/hitokotowidget/
│   ├── HitokotoWidget.java
│   └── HitokotoService.java
├── res/
│   ├── layout/
│   │   └── widget_layout.xml
│   ├── drawable/
│   │   └── widget_background.xml
│   ├── xml/
│   │   └── widget_info.xml
│   └── values/
│       └── strings.xml
└── AndroidManifest.xml
```

### 3. 添加权限
AndroidManifest.xml中已包含必要权限：
- `INTERNET` - 访问网络
- `ACCESS_NETWORK_STATE` - 检查网络状态
- `SCHEDULE_EXACT_ALARM` - 精确定时更新

### 4. 编译和安装
```bash
# 在Android Studio中
# 点击 Run -> Run 'app'
# 或使用命令行
./gradlew assembleDebug
adb install app/build/outputs/apk/debug/app-debug.apk
```

### 5. 添加Widget到桌面
1. 长按桌面空白处
2. 选择"小部件"或"Widget"
3. 找到"一句话小部件"
4. 拖动到桌面

## API接口说明

### 接口地址
```
https://api.codelife.cc/yiyan/random?lang=cn
```

### 响应格式
```json
{
    "code": 200,
    "data": {
        "hitokoto": "想一个人有多想念，那又是文字失效瞬间。",
        "from": "-五月天-"
    },
    "msg": "加载成功"
}
```

### 字段说明
- `code`: 状态码（200表示成功）
- `data.hitokoto`: 一句话内容
- `data.from`: 作者或来源
- `msg`: 消息提示

## 技术要点

### 1. 定时更新机制
使用`AlarmManager`实现每小时更新：
```java
alarmManager.setRepeating(
    AlarmManager.ELAPSED_REALTIME,
    firstTime,
    UPDATE_INTERVAL,  // 3600000ms = 1小时
    pendingIntent
);
```

### 2. 网络请求
使用`HttpURLConnection`进行GET请求：
- 设置连接超时：5秒
- 设置读取超时：5秒
- 在IntentService中执行，避免阻塞主线程

### 3. 更新Widget
通过`RemoteViews`更新UI：
```java
RemoteViews views = new RemoteViews(packageName, R.layout.widget_layout);
views.setTextViewText(R.id.tv_hitokoto, hitokoto);
appWidgetManager.updateAppWidget(appWidgetId, views);
```

### 4. 错误处理
- 网络错误：显示"网络错误"
- 解析错误：显示"解析错误"
- API错误：显示"加载失败"

## 优化建议

### 1. 网络优化
```java
// 使用OkHttp替代HttpURLConnection
implementation 'com.squareup.okhttp3:okhttp:4.12.0'
```

### 2. JSON解析优化
```java
// 使用Gson替代手动解析
implementation 'com.google.code.gson:gson:2.10.1'
```

### 3. 添加缓存
```java
// 使用SharedPreferences缓存最后一次的内容
SharedPreferences prefs = context.getSharedPreferences("widget_cache", MODE_PRIVATE);
prefs.edit()
     .putString("last_hitokoto", hitokoto)
     .putString("last_from", from)
     .apply();
```

### 4. 主题适配
支持深色模式，根据系统主题切换文字颜色：
```xml
<TextView
    android:textColor="?android:textColorPrimary" />
```

### 5. 网络检测
更新前检查网络连接：
```java
ConnectivityManager cm = (ConnectivityManager) 
    context.getSystemService(Context.CONNECTIVITY_SERVICE);
NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
```

## 测试要点

### 1. 功能测试
- ✅ Widget正常显示
- ✅ 点击刷新功能
- ✅ 定时更新（等待1小时）
- ✅ 网络异常处理
- ✅ 重启手机后Widget恢复

### 2. 兼容性测试
- ✅ 小米MIUI系统
- ✅ Android 5.0+
- ✅ 不同屏幕尺寸

### 3. 性能测试
- ✅ 内存占用
- ✅ 电池消耗
- ✅ 网络流量

## 常见问题

### Q1: Widget不更新？
**A**: 检查以下几点：
1. 确认网络权限已授予
2. 查看Logcat日志
3. 确认API接口可访问
4. 重启手机后重新添加Widget

### Q2: 小米手机特殊处理？
**A**: 小米MIUI系统需要：
1. 允许应用自启动
2. 允许后台运行
3. 在"省电优化"中设置为无限制

### Q3: 定时更新不准确？
**A**: Android 6.0+对定时任务有限制：
- 使用`setExactAndAllowWhileIdle()`更精确
- 或使用WorkManager替代AlarmManager

### Q4: HTTP请求失败？
**A**: 确保在AndroidManifest.xml中添加：
```xml
android:usesCleartextTraffic="true"
```

## 发布准备

### 1. 混淆配置（proguard-rules.pro）
```proguard
-keepclassmembers class * extends android.appwidget.AppWidgetProvider {
    public <methods>;
}
```

### 2. 签名配置
```gradle
signingConfigs {
    release {
        storeFile file("keystore.jks")
        storePassword "your_password"
        keyAlias "your_alias"
        keyPassword "your_password"
    }
}
```

### 3. 生成发布版本
```bash
./gradlew assembleRelease
```

## 许可证
MIT License

## 联系方式
如有问题，请提交Issue或Pull Request。
