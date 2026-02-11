# 快速开始指南

## 5分钟快速部署

### 第一步：准备环境
1. 安装 Android Studio（最新版本）
2. 配置 Android SDK（API 21+）
3. 连接小米手机或启动模拟器

### 第二步：创建项目
```bash
# 打开 Android Studio
# File -> New -> New Project
# 选择 "Empty Views Activity"
# 配置：
#   Name: HitokotoWidget
#   Package name: com.example.hitokotowidget
#   Minimum SDK: API 21
#   Language: Java
```

### 第三步：复制文件

#### 1. Java代码文件
将以下文件复制到 `app/src/main/java/com/example/hitokotowidget/`：
- ✅ HitokotoWidget.java
- ✅ HitokotoService.java
- ✅ HitokotoData.java

#### 2. 布局文件
将以下文件复制到 `app/src/main/res/layout/`：
- ✅ widget_layout.xml

#### 3. Drawable文件
将以下文件复制到 `app/src/main/res/drawable/`：
- ✅ widget_background.xml

#### 4. XML配置文件
将以下文件复制到 `app/src/main/res/xml/`（如果没有xml文件夹，需要手动创建）：
- ✅ widget_info.xml

#### 5. 资源文件
将 strings.xml 的内容添加到 `app/src/main/res/values/strings.xml`

#### 6. 清单文件
用提供的 AndroidManifest.xml 替换 `app/src/main/AndroidManifest.xml`

#### 7. Gradle文件
用提供的 build.gradle 替换 `app/build.gradle`

### 第四步：文件结构检查
确保项目结构如下：
```
HitokotoWidget/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       │   └── com/
│   │       │       └── example/
│   │       │           └── hitokotowidget/
│   │       │               ├── HitokotoWidget.java       ✅
│   │       │               ├── HitokotoService.java      ✅
│   │       │               └── HitokotoData.java         ✅
│   │       ├── res/
│   │       │   ├── drawable/
│   │       │   │   └── widget_background.xml            ✅
│   │       │   ├── layout/
│   │       │   │   └── widget_layout.xml                ✅
│   │       │   ├── values/
│   │       │   │   └── strings.xml                      ✅
│   │       │   └── xml/
│   │       │       └── widget_info.xml                  ✅
│   │       └── AndroidManifest.xml                       ✅
│   └── build.gradle                                      ✅
```

### 第五步：同步和构建
1. 点击 "Sync Project with Gradle Files" 按钮
2. 等待依赖下载完成
3. 确保没有编译错误

### 第六步：运行应用
1. 连接小米手机（开启USB调试）
2. 点击运行按钮 ▶️
3. 应用会自动安装到手机

### 第七步：添加Widget
1. 长按桌面空白区域
2. 选择"添加小部件"或"工具"
3. 找到"一句话小部件"
4. 拖动到桌面合适位置

### 第八步：测试功能
- ✅ Widget显示一句话
- ✅ 点击Widget刷新内容
- ✅ 等待1小时验证自动更新

## 小米手机特殊设置

### 允许自启动
1. 打开"安全中心"
2. 进入"应用管理"
3. 找到"一句话小部件"
4. 开启"自启动"权限

### 允许后台运行
1. 设置 -> 应用设置 -> 应用管理
2. 找到"一句话小部件"
3. 省电策略 -> 选择"无限制"

### 网络权限
1. 设置 -> 应用设置 -> 应用管理
2. 找到"一句话小部件"
3. 权限管理 -> 允许"网络"权限

## 常见问题排查

### 问题1：Widget不显示
**解决方案：**
1. 检查AndroidManifest.xml中的receiver配置
2. 确保widget_info.xml在res/xml目录下
3. 重新编译安装应用

### 问题2：显示"加载中..."不更新
**解决方案：**
1. 检查网络权限
2. 查看Logcat日志（过滤：HitokotoService）
3. 确认API接口可访问（在浏览器测试）
4. 检查是否开启了后台运行权限

### 问题3：点击无响应
**解决方案：**
1. 检查HitokotoWidget.java中的PendingIntent配置
2. 确认添加了FLAG_IMMUTABLE标志
3. 重启手机后重新测试

### 问题4：1小时后不自动更新
**解决方案：**
1. 小米手机需要允许自启动
2. 关闭省电模式
3. 在"设置-省电与电池"中将应用设置为"无限制"

## 调试技巧

### 查看日志
```bash
# 过滤Widget相关日志
adb logcat | grep HitokotoWidget

# 查看Service日志
adb logcat | grep HitokotoService
```

### 手动触发更新
```bash
# 通过adb发送广播
adb shell am broadcast -a com.example.hitokotowidget.UPDATE
```

### 检查Widget是否注册
```bash
# 查看已注册的Widget
adb shell dumpsys appwidget
```

## 优化建议

### 1. 添加加载动画
在widget_layout.xml中添加ProgressBar

### 2. 支持深色模式
根据系统主题自动切换颜色

### 3. 添加多种尺寸
支持2x1、4x1、4x2等多种尺寸

### 4. 本地缓存
使用SharedPreferences缓存上一次内容

### 5. 网络检测
更新前先检查网络连接状态

## 下一步

### 功能扩展
- [ ] 添加配置界面（选择更新频率）
- [ ] 支持手动选择主题颜色
- [ ] 添加收藏功能
- [ ] 分享一句话到社交媒体
- [ ] 支持多个API源切换

### 性能优化
- [ ] 使用WorkManager替代AlarmManager
- [ ] 使用Retrofit简化网络请求
- [ ] 添加错误重试机制
- [ ] 优化电池消耗

### UI美化
- [ ] 添加更多主题样式
- [ ] 支持自定义字体
- [ ] 添加背景图片选项
- [ ] 动画效果优化

## 获取帮助
- 查看完整文档：README.md
- 遇到问题请检查Logcat日志
- 确保API接口可访问：https://api.codelife.cc/yiyan/random?lang=cn
