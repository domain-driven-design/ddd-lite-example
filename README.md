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

## 数据库规范
  - id UUID
  - 默认加上外键索引（命名：i_xxx）
  - 默认不要外键约束
  - 数据库唯一约束，兜底（命名：uni_xxx_xxx）
  - created_by, created_at, updated_at必有，updated_by可选
  - 大数据量查询排序，加索引，需要时再加

## 风格说明

1. 命名风格避免冗余，例如 domainService.action(String id, ...., String operatorId)，连起来需要形成一句话
2. 如果注解为默认行为，不显示添加，比如 @Column 如果满足字段规，则省略
3. ApplicationServe 形参顺序为，被操作对象的 id、request、operatorId
4. url 中使用 /me 来默认使用当前用户凭证

## redis 使用规范

1. 禁止使用 scan\keys 会造成性能低下 http://doc.redisfans.com/key/scan.html
2. 禁止存放需要持久化的数据，因为可能需要清理 redis 数据

## 测试策略

1. 单元测试。覆盖基础的组件、工具类和领域服务，单元测试需要验证各种分支条件。仅测试业务异常，不需要happy path。
2. API 测试。使用 E2E 测试实现 API 测试，API 测试的目标只是验证整体流程是否通畅，只测试正常流程即可。

## 业务
问答社区
核心：问题，回答
支撑：圈子

## 待实现
- rest 接口自动生成
- 缓存
- 安全审计（重要操作记录）
- 代码规范：checkstyle
- rest 接口自动生成
- application service和usecase放在一起
- 外部系统对接（如微信/支付宝）
- 异步任务

## 待讨论
- 前端如何判断一个question，一个user只能有一个answer？性能考虑
- 对于查询，查询的维度和筛选条件，比如：对answer的查询，以question，以group，以group member
- 权衡，在仅需要operator的最终传递的userId，而不需要对应role权限check，这样的方法是否需要查询出operator并传入？
- API测试覆盖所有happy path，包括不同角色权限对同一资源的同一操作，单元测试专注check各种业务异常
- 查看某位用户创建的问题，无需权限校验，直接在getByPage加查询条件？查询场景不同，但都是对question基本属性的筛选
- 写接口，遵循/groups/groupId/questions，逐层check权限。读接口，不需要check权限，或者有时需要跳过圈子这个筛选项
- api测试，是一个接口统一准备数据，然后分不同的happy path测试？还是不同happy path分别准备数据，在不同的test中测试？
- getByPage和query是否要通过keyword可为空来统一使用？使用场景感觉略微不同
- 如果有全局查询Question的业务需求，如何设计接口？现在接口设计为/groups/groupId/questions（是不是可以用类似ALL来跳过group筛选？）
- 数据查询，是否检查权限
- api测试，仅对默认圈子进行测试？自定义圈子的权限check测试在哪里做？api测试？applicationService测试？
- 聚合根下的实体命名，比如：Group下的GroupMemberList是命名为groupMembers还是members？（具体情况待定）
- 软删除（查询过滤，关联信息处理）
- 测试命名规范

