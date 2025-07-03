# 🚀 自动提醒打卡系统

一个基于Java微服务架构的自动提醒系统，支持个人用户登录后自定义提醒事项，通过邮箱定时发送提醒通知。

## 🏗️ 架构设计

### 微服务组件
- **用户服务** (User Service) - 用户认证、JWT令牌管理
- **提醒服务** (Reminder Service) - 提醒事项CRUD、定时任务
- **通知服务** (Notification Service) - 邮件发送、通知日志
- **网关服务** (Gateway Service) - 统一入口、路由转发、认证过滤
- **服务发现** (Eureka Server) - 微服务注册中心

### 技术栈
#### 后端
- **Spring Boot 3.1.5** - 微服务框架
- **Spring Cloud 2022.0.4** - 微服务生态
- **Spring Security + JWT** - 身份认证
- **MySQL** - 用户数据存储
- **MongoDB** - 提醒数据存储
- **Redis** - 通知日志缓存
- **Eureka** - 服务发现

#### 前端
- **Vue.js 3** - 前端框架
- **Element Plus** - UI组件库
- **Vuex** - 状态管理
- **Vue Router** - 路由管理
- **Axios** - HTTP客户端

## 🚀 快速开始

### 环境要求
- Java 17+
- Node.js 16+
- MySQL 8.0+
- MongoDB 4.4+
- Redis 6.0+
- Maven 3.8+

### 启动步骤

#### 1. 数据库准备
```sql
-- 创建MySQL数据库
CREATE DATABASE reminder_user_db;

-- MongoDB和Redis无需特殊配置，使用默认设置即可
```

#### 2. 启动微服务(按顺序)
```bash
# 1. 启动服务发现
cd discovery-service
mvn spring-boot:run

# 2. 启动用户服务
cd user-service
mvn spring-boot:run

# 3. 启动提醒服务
cd reminder-service
mvn spring-boot:run

# 4. 启动通知服务
cd notification-service
mvn spring-boot:run

# 5. 启动网关服务
cd gateway-service
mvn spring-boot:run
```

#### 3. 启动前端
```bash
cd frontend
npm install
npm run serve
```

### 📱 访问地址
- **前端应用**: http://localhost:3000
- **API网关**: http://localhost:8080
- **服务发现控制台**: http://localhost:8761
- **用户服务**: http://localhost:8081
- **提醒服务**: http://localhost:8082
- **通知服务**: http://localhost:8083

## 🔧 配置说明

### 邮件服务配置
在 `notification-service/src/main/resources/application.yml` 中配置：

```yaml
spring:
  mail:
    host: smtp.gmail.com
    port: 587
    username: your-email@gmail.com
    password: your-app-password
```

### 数据库连接
各服务的数据库连接配置在对应的 `application.yml` 文件中。

## 🎯 功能特性

### ✅ 已实现功能
- 👤 用户注册、登录、JWT认证
- 📝 提醒事项的增删改查
- ⏰ 多种提醒频率(一次性、每日、每周、每月)
- 📧 邮件通知发送
- 🎨 现代化Web界面
- 🔐 API网关统一认证
- 📊 服务监控和发现

### 🚧 待开发功能
- 📱 移动端适配
- 🔔 浏览器推送通知
- 📈 数据统计和报表
- 👥 团队协作功能
- 🔄 提醒模板系统

## 📋 API接口

### 用户服务 API
- `POST /api/users/register` - 用户注册
- `POST /api/users/login` - 用户登录
- `GET /api/users/{id}` - 获取用户信息
- `POST /api/users/validate` - 验证Token

### 提醒服务 API
- `GET /api/reminders/user/{userId}` - 获取用户提醒列表
- `POST /api/reminders` - 创建提醒
- `PUT /api/reminders/{id}` - 更新提醒
- `DELETE /api/reminders/{id}` - 删除提醒
- `GET /api/reminders/pending` - 获取待发送提醒

### 通知服务 API
- `POST /api/notifications/send-reminder` - 发送提醒通知
- `GET /api/notifications/log/{logId}` - 获取通知日志
- `POST /api/notifications/retry/{logId}` - 重试失败通知

## 🤝 贡献

欢迎提交Issue和Pull Request来改进这个项目！

## 📄 许可证

MIT License

---

🤖 Generated with [Claude Code](https://claude.ai/code)

Co-Authored-By: Claude <noreply@anthropic.com>