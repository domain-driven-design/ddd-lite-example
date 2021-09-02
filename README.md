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

1. 命名风格避免冗余，例如 domainService.action(String id, ...., Object operator)，连起来需要形成一句话
2. 如果注解为默认行为，不显示添加，比如 @Column 如果满足字段规，则省略
3. ApplicationServe 形参顺序为，被操作对象的 id、request、operator
4. url 中使用 /me 来默认使用当前用户凭证

## redis 使用规范

1. 禁止使用 scan\keys 会造成性能低下 http://doc.redisfans.com/key/scan.html
2. 禁止存放需要持久化的数据，因为可能需要清理 redis 数据

## 其他规范
1. repository的save操作对象，必须从repository中重新查询后更新，避免使用缓存等对象存在的潜在问题
2. 业务逻辑在application service中编排，一个对外公开的方法是一个完整的业务流程
3. 切面使用，不影响当前业务流程的操作，可随时关闭打开
4. 资源的操作者是该资源/传递下来的上层资源的成员，如：question属于group，question的operator为groupMember
5. 在统一理解上，User也是一种成员，是站点成员

## 测试策略

1. 单元测试。覆盖基础的组件、工具类和领域服务，单元测试需要验证各种分支条件。仅测试业务异常，不需要happy path。
2. API 测试。使用 E2E 测试实现 API 测试，API 测试的目标只是验证整体流程是否通畅，只测试正常流程即可。

## 业务
问答社区
核心：问题，回答
支撑：圈子
ps：
- 可直接提出问题，此时问题属于默认圈子
- 圈子内的问题属于圈子，那么创建者不再是圈子成员时，就无权对问题进行操作，问题的管理维度只能是以圈子成员的身份。

## 待实现
- 区分User UserOperator，GroupMember GroupMemberOperator
- 缓存
- 安全审计（重要操作记录）
- 代码规范：checkstyle
- rest 接口自动生成
- application service和usecase放在一起
- 外部系统对接（如微信/支付宝）
- 异步任务

