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
- bootstrap 项目启动

## 依赖关系
- application service 只依赖domain service，封装查询到domain service
- domain service 依赖聚合内的repository

## 命名规则
- 聚合内的对象都以聚合根名为前缀
- Response 使用 from 方法生成输出 
- domain service 内方法使用get，create，update，delete...，省略聚合主体名
- public方法不在类内被使用，抽取同名加下划线的private方法，如：get，_get

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

## 风格说明

1. 命名风格避免冗余，例如 domainService.action(String id, ...., String operatorId)，连起来需要形成一句话
2. 如果注解为默认行为，不显示添加，比如 @Column 如果满足字段规，则省略
3. ApplicationServe 形参顺序为，被操作对象的 id、request、operatorId

## redis 使用规范

1. 禁止使用 scan\keys 会造成性能低下 http://doc.redisfans.com/key/scan.html
2. 禁止存放需要持久化的数据，因为可能需要清理 redis 数据

## 待实现
- exception体系
- 测试规范（api测试，单元测试）
- 缓存
- 安全审计（重要操作记录）
- 代码规范：checkstyle
- 外部系统对接（如微信/支付宝）
- 异步任务

## 待讨论
- UserRole里的role：USER歧义。GroupMemberRole里的role：MEMBER歧义
- 命名：Answer or QuestionAnswer
- 数据库时间字段使用TIMESTAMP，或者时间戳数字
- 软删除（查询过滤，关联信息处理）
- 测试命名规范



## 业务
问答社区
核心：问题，回答
支撑：圈子

## 测试策略

1. 单元测试。覆盖基础的组件、工具类和领域服务，单元测试需要验证各种分支条件。
2. API 测试。使用 E2E 测试实现 API 测试，API 测试的目标只是验证整体流程是否通畅，只测试正常流程即可。

