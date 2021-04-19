## 本地环境准备
docker-compose up -d
ps：
- 进入容器 例如：docker exec -it demo-mysql bash 
 
## 结构
- admin 提供给管理面的 API
    - rest
    - service
    - usecase
- forntend 提供给用户面使用的 API
- domain 领域
    - user 
        - modal
        - repository
        - service
    - auth
    - article
- bootstrap 项目启动

## 待实现
- Authority
- user 的role区分（普通用户与管理员）
- user 对 article 的管理
- admin 对 article 的管理
- article 与 tag （多对多）
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

## 概念
实体：可见资源。当一个实体属于另一个实体，生命周期随拥有者
关系证明：实体间关系的证明，生命周期彼此独立

resource
    - ...
    - ...
    - resource
relation
    - resource(identify)
    - resource(identify)
    - resource对该relation权限（查看，撤销，创建）
    - ...
