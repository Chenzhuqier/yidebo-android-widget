# 一嘚啵 YiDeBo

<p align="center">
  <img src="screenshots/widget-preview.png" width="300">
</p>

<p align="center">
  一个简洁优雅的Android桌面小部件，每小时自动更新一句话名言
</p>

<p align="center">
  <a href="#功能特性">功能特性</a> •
  <a href="#下载安装">下载安装</a> •
  <a href="#使用说明">使用说明</a> •
  <a href="#开发">开发</a> •
  <a href="#许可证">许可证</a>
</p>

---

## ✨ 功能特性

- 🎨 简洁优雅的卡片设计
- 🔄 每小时自动更新内容
- 👆 点击立即刷新
- 📱 支持4x2桌面小部件尺寸
- 🌐 接入一言API，海量名言警句
- ⚡ 轻量级，低功耗

## 📱 下载安装

### 从Release下载

前往 [Releases](https://github.com/你的用户名/yidebo-android-widget/releases) 页面下载最新版APK

### 要求

- Android 5.0 (API 21) 及以上
- 支持所有Android系统（小米MIUI、华为EMUI等）

## 🚀 使用说明

1. 安装APK
2. 长按桌面空白处
3. 选择"添加小部件/工具"
4. 找到"一嘚啵"
5. 拖动到桌面合适位置

### ⚙️ 小米手机特别设置

为确保正常使用，请进行以下设置：

- ✅ 允许自启动
- ✅ 允许联网
- ✅ 省电策略设为"无限制"

## 🛠️ 开发

### 克隆项目
```bash
git clone https://github.com/你的用户名/yidebo-android-widget.git
cd yidebo-android-widget
```

### 构建

使用Android Studio打开项目，然后：
```bash
# Debug版本
./gradlew assembleDebug

# Release版本
./gradlew assembleRelease
```

### 技术栈

- Java
- Android SDK
- AppWidget
- HttpURLConnection
- AlarmManager

## 📸 截图

<p align="center">
  <img src="screenshots/widget-preview.png" width="250">
  <img src="screenshots/widget-demo.gif" width="250">
</p>

## 🤝 贡献

欢迎提交Issue和Pull Request！

## 📝 更新日志

### v1.0.0 (2026-02-11)
- 🎉 首次发布
- ✨ 支持每小时自动更新
- ✨ 点击刷新功能

## 📄 许可证

[MIT License](LICENSE)

## 🙏 致谢

- 数据来源：[一言API](https://hitokoto.cn/)
- 图标设计：...

---

<p align="center">
  Made with ❤️ by 你的名字
</p>
```

---

## 📷 需要准备的素材

### 1. 截图

用手机截图Widget的效果：
```
screenshots/
├── widget-preview.png     # Widget预览图
├── widget-demo.gif        # 演示GIF（可选）
└── settings.png          # 设置页面（如果有）