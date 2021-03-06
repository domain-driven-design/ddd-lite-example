## 写在最前面
仅供参考，如何使用，而非建模。

免责声明：
- 建立在已经是由 DDD 良好建模过的模型基础上
- 各团队对 DDD 的解读不同，团队内的统一认知就是最好的理解
- 各项目使用不同的 DDD 实践规范，项目的规范就是最好的规范

## 何为架构？
为什么需要架构？总而言之，架构就是为了**设置限制**。

架构是前人基于成千上万的项目经验总结出来的一套规范，通过遵循这套规范，你就可以在未来尽可能少的去踩坑。当你所编写的代码变得庞大时，使用合适的架构就可以一定程度上避免项目的代码结构需要做出巨大调整的风险。

## DDD 是什么？
Deadline-Driven Design？

Domain-Driven Design

## 为什么要用 DDD？
解决复杂问题？

将实现对应到持续进化的模型。并且让开发和业务合作，共同来完善模型构建。

## DDD 怎么用？
如果说之前的 MVC 是个层，那 DDD 就是块。

横着切完，竖着切。domain 不关注某个具体的业务流程，关注某个业务功能。

一言而概之，关注点分离。

## 几个有趣的概念
#### 通用语言（Ubiquitous）
- 业务人员和开发人员需要使用无歧义的统一语言来对话。这些语言包括对概念的统一理解和定义。
- 一个简单的方式：coding 中的关键命名 call 业务人员一起定。

#### 领域（Domain）
- 业务相关知识的集合。
- 通俗来说，领域就是圈了些业务知识，计算机作为业务规则的自动化。
- 同一个领域中的内容，可以认为是紧耦合的，没啥关系的内容，也没有必要放在一起。

#### 核心域（Core Domain）
- 领域中最核心的部分。
- 换个说法，用来赚钱的那部分。

#### 支撑域（Support Domain）
- 为了实现核心业务而不得不开发的业务所对应的相关知识的集合。
- 如果说核心域是目的，那么支撑域就是手段。

#### 通用域（General Domain）
- 业界已经有成熟方案的业务，可以使用标准部件来实现，短信通知、邮件等。
- 专业的事情交给专业的人。

#### 限界上下文（ Bounded Context）
- 有明确边界的上下文，强调概念的一致性。
- 就是一个上下文。买菜，点菜，我好菜，菜的各不相同。

#### 领域模型（Model）
- 业务概念在程序中的一种表达方式。
- 一个还不错的抽象。

#### 实体（Entity）
- 在相同限界上下文中具有唯一标识的领域模型。
- 有身份！

#### 值对象（Value Object）
- 用属性标识，任何属性的变化都视为新的值对象。
- 一个简单的验证方法，可以被拆分为几个平铺的属性。
- 值对象存在的意义，是帮助人更好的理解，一个🍔？还是🍞🥬🧀️🐂🍞？

#### 聚合（Aggregate）
- 一组生命周期强一致，修改规则强关联的实体和值对象的集合，表达统一的业务意义。
- 目的强一致的组合体。

#### 聚合根（ Aggregate Root）
- 聚合中最核心的实体，其他的实体和值对象都从属于这个实体。
- 决策者。

## 值得注意的几个使用点
#### 维护 Domain Model
- 充血还是贫血？
- 充血的主体是是什么？
- 贫的血去了哪？

#### 维护 Service
- Service 的服务粒度是多大？
- Service 调度了什么？
- Application Sercive or Domain Service？

#### Repository 实现
- JPA 是具体实现？还是抽象接口？
- 尝试下内存实现下数据库接口，是否真正做到屏蔽具体实现？
- 面向领域建模 or 面向数据库建模？

## 测试测什么？
这里不扯 TDD，但 TDD 确实是一种非常好的进行思路辅助方式，要做什么比怎么做更值得关注。

#### 领域逻辑
所有的领域逻辑都应当被保护，至于领域逻辑是什么？这是一个说不清道不明的事情了。

所幸，对于使用而言，有一个非常简单的准则，对于所有 domain modal 和 domain service 中的逻辑，尤其是异常逻辑，检查它。除了定义该怎么做，限制也是业务逻辑的一个重要组成。

#### 领域调度逻辑
当领域逻辑已经被充分测试后，我们就应当信任领域服务。对于领域调度逻辑而言，关注点就在于期望被调度的领域服务被正确调度了。这部分通常会被体现为对外接口测试。

## 写在最后面
闲谈写就，欢迎来拍！

