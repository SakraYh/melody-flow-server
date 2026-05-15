# MelodyFlow Server

基于 Spring Boot 3 构建的在线音乐流媒体平台后端服务，为 MelodyFlow 客户端和管理端提供 RESTful API 支持。

## 技术栈

| 类别 | 技术 |
|------|------|
| 后端框架 | Spring Boot 3.3.7 |
| 开发语言 | Java 17 |
| 构建工具 | Maven |
| ORM | MyBatis-Plus 3.5.9 |
| 数据库 | MySQL 8.0 |
| 缓存 | Redis |
| 对象存储 | MinIO |
| 认证鉴权 | JWT (java-jwt 4.4.0) + RBAC |
| 连接池 | Druid 1.2.18 |

## 核心功能

### 用户模块
- 邮箱验证码注册 / 登录 / 登出
- JWT 令牌认证 + Redis 令牌管理
- 个人信息编辑、头像上传、密码修改/重置
- 管理员对用户的 CRUD、状态启用/禁用、批量删除

### 内容管理
- **歌手管理**：歌手信息 CRUD、头像上传、作品聚合查询
- **歌曲管理**：歌曲 CRUD、音频/封面上传、分类检索、歌词存储
- **歌单管理**：歌单 CRUD、歌单-歌曲多对多绑定、精选推荐
- **Banner 管理**：首页轮播图 CRUD、启用/禁用控制

### 社交互动
- 歌曲与歌单的评论发表/查看/删除
- 用户收藏（歌曲/歌单）及取消收藏
- 用户反馈提交与管理员处理

### 全文搜索
- MySQL FULLTEXT INDEX 倒排索引
- 跨表联合搜索（歌曲/歌手/歌单）
- BOOLEAN MODE 多关键词 AND 匹配
- Redis 搜索结果缓存

### 文件服务
- MinIO 对象存储管理音频、封面、头像等静态资源
- 支持文件上传、删除、预签名访问

### 系统架构
- RBAC 角色权限控制（Admin / User），接口级别拦截
- 全局异常统一处理
- CORS 跨域配置
- 声明式缓存（`@Cacheable` / `@CacheEvict`）

## 数据库设计

12 张数据表：

| 表名 | 说明 |
|------|------|
| tb_admin | 管理员账户 |
| tb_user | 用户账户 |
| tb_artist | 歌手信息 |
| tb_song | 歌曲信息（含 FULLTEXT INDEX） |
| tb_playlist | 歌单信息（含 FULLTEXT INDEX） |
| tb_playlist_binding | 歌单-歌曲绑定关系 |
| tb_genre | 歌曲-风格关联 |
| tb_style | 音乐风格字典 |
| tb_comment | 评论数据 |
| tb_user_favorite | 用户收藏关系 |
| tb_banner | 首页轮播图 |
| tb_feedback | 用户反馈 |

## 环境要求

- JDK 17+
- Maven 3.6+
- MySQL 8.0+
- Redis 6.0+
- MinIO（最新稳定版）

## 快速开始

### 1. 环境准备

确保已安装并启动 MySQL、Redis、MinIO 服务。

在 MySQL 中创建数据库：

```sql
CREATE DATABASE melody_flow CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

在 MinIO 中创建 Bucket（名称与配置文件一致，默认为 `melody-flow-data`）。

### 2. 配置

编辑 `src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://YOUR_HOST:3306/melody_flow?useUnicode=true&characterEncoding=utf-8&useSSL=false
    username: YOUR_USERNAME
    password: YOUR_PASSWORD

  data:
    redis:
      host: YOUR_REDIS_HOST
      port: 6379
      password: YOUR_REDIS_PASSWORD  # 如无密码则留空

  mail:
    host: smtp.example.com
    username: your-email@example.com
    password: YOUR_APP_PASSWORD

minio:
  endpoint: http://YOUR_MINIO_HOST:9000
  accessKey: YOUR_ACCESS_KEY
  secretKey: YOUR_SECRET_KEY
  bucket: melody-flow-data
```

### 3. 运行数据库迁移

执行 SQL 迁移脚本创建全文索引：

```sql
-- 见 sql/migration_fulltext_search.sql
ALTER TABLE tb_song ADD FULLTEXT INDEX ft_song_name_album (name, album);
ALTER TABLE tb_artist ADD FULLTEXT INDEX ft_artist_name (name);
ALTER TABLE tb_playlist ADD FULLTEXT INDEX ft_playlist_title (title);
```

### 4. 构建与启动

```bash
# 构建
mvn clean package -DskipTests

# 启动
java -jar target/melody-flow-server-*.jar
```

服务默认启动在 `8080` 端口。

## 项目结构

```
src/main/java/cn/edu/seig/vibemusic/
├── config/          # 配置类（CORS、Redis、MinIO、Web、RBAC）
├── constant/        # 常量定义
├── controller/      # REST 控制器（14个）
├── enumeration/     # 枚举类
├── handler/         # 全局异常处理
├── interceptor/     # 拦截器（登录鉴权）
├── mapper/          # MyBatis Mapper 接口
├── model/
│   ├── dto/         # 数据传输对象
│   ├── entity/      # 数据库实体
│   └── vo/          # 视图对象
├── result/          # 统一响应模型
├── service/         # 业务逻辑接口与实现
└── util/            # 工具类
```

## Maven 脚本

| 命令 | 说明 |
|------|------|
| `mvn clean` | 清理构建产物 |
| `mvn compile` | 编译源代码 |
| `mvn test` | 运行单元测试 |
| `mvn package -DskipTests` | 跳过测试打包 |
| `mvn spring-boot:run` | 开发模式启动 |

## API 接口

接口定义位于 `controller/` 目录下，按模块划分：

- `/user/**` — 用户认证与信息管理
- `/admin/**` — 管理员操作
- `/song/**` — 歌曲管理
- `/artist/**` — 歌手管理
- `/playlist/**` — 歌单管理
- `/comment/**` — 评论管理
- `/favorite/**` — 收藏管理
- `/banner/**` — 轮播图管理
- `/feedback/**` — 反馈管理
- `/search` — 全文搜索

## 常见问题

**数据库连接失败？**
- 检查 `application.yml` 中数据库 URL、用户名、密码是否正确
- 确认 MySQL 服务已启动且网络可达
- 确认数据库 `melody_flow` 已创建

**Redis 连接失败？**
- 检查 Redis 配置的 host、port、password
- 确认 Redis 服务已启动

**文件上传失败？**
- 检查 MinIO 配置的 endpoint、accessKey、secretKey、bucket
- 确认 MinIO 服务已启动且 Bucket 已创建
- 检查 `spring.servlet.multipart` 中的文件大小限制

**端口冲突？**
- 在 `application.yml` 中添加 `server.port` 修改默认端口

## 许可证

MIT License

## 免责声明

本项目仅供学习和技术研究使用，请勿用于商业用途。使用本项目所产生的任何数据安全风险、版权纠纷或经济损失，项目作者不承担任何责任。
