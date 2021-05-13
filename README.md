## 本地环境准备
docker-compose up -d
ps：
- 进入容器 例如：docker exec -it demo-mysql bash 
 
## 结构
- admin 提供给管理面的 API
    - rest
    - service
    - usecase
- business 提供给用户面使用的 API
- open
- domain 领域
    - user 
        - modal
        - repository
        - service
    - auth
    - article
- bootstrap 项目启动

## 待实现
- user 的role区分（普通用户与管理员）
- admin 对 article 的管理
- exception体系
- 软删除（查询过滤，关联信息处理）
- 测试规范（api测试，单元测试）
- 外部系统对接（如微信/支付宝）
- 数据库规范：字段命名...
- 数据库性能：索引
- 缓存
- 安全审计（重要操作记录）
- 代码规范：checkstyle
- 异步任务

## 命名规则
- 聚合内的对象都以聚合根名为前缀

## 测试策略
#### application 集成测试
- happy path
#### domain 单元测试（mock数据库）
- 测试业务逻辑异常

## 数据库规范
  - id UUID
  - 默认加上外键索引
  - 默认不要外键约束
  - created_by, created_at, updated_at必有，updated_by可选
  - 大数据量查询排序，加索引
  ```sql
  CREATE TABLE `article`
  (
  `id`         varchar(36)  not null primary key,
  `title`      varchar(100) not null,
  `content`    text         not null,
  `created_by` varchar(36)  not null,
  `updated_by` varchar(36)  not null,
  `created_at` timestamp(3) not null,
  `updated_at` timestamp(3) not null
  );
  ```

## 想讨论的问题
- 统一拦截authority到上下文中是否合理？
- application service中只有domain service，封装查询到domain service
- 数据库字符集设置(set names)
- 管理员添加方式？管理员可添加其他管理员？
- 在什么情况下使用id，articleId，tagId？
- request和response中的时间用时间戳(配置格式)
- 增删改查权限
