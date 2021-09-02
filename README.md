
# ddd-lite-example

This is an example of a Java project with limited use of DDD style.

## local environment for starting

>  docker-compose up -d

## structure
- admin // API for Admin 
    - rest
    - service
    - usecase
- business // API for user 
- open
- domain 
    - user 
        - modal
        - repository
        - service
    - auth
- bootstrap 

## dependence

- application service -> domain service
- domain service -> repository

## naming rule

- Objects in the aggregation are prefixed with the `aggregation root`
- Response Generates output using the `from` 
- Domain service uses get, create, update, delete... , omits the aggregation principal name
- the public method is not used within the same class, extract the private method with the same name and underline, such as: get, _get
- Naming style To avoid redundancy, for example, domainservice. action(String ID,.... , Object operator), which should form a sentence
- If the annotation is the default behavior, do not add, for example, @column is omitted if it meets the field specification
- ApplicationServe Parameter sequence is the ID of the object to be operated, request, and operator

## database rule
- id by UUID
- Add foreign key index by default (name: i_xxx)
- No foreign key constraints by default
- Database unique constraint is required
- audit log fields such as: created_by, created_at, updated_at are required, updated_by is optional
- need index for large data volume querying or sorting when necessary


## redis rule

- Disabling scan\keys results in poor performance http://doc.redisfans.com/key/scan.html
- Disallow persistent data because redis data may need to be cleaned

## other tips 

- The save operation object of repository must be queried from the Repository and updated to avoid the potential problems of using caches and other objects
-  Business logic is choreographed in Application Service, and an open method is a complete business process
- Section use, does not affect the operation of the current business process, can be closed and opened at any time
- The operator of a resource is a member of the resource or upper-layer resource. For example, a question belongs to a group, and the operator of a question is groupMember
- In the unified understanding, User is also a kind of member, is a site member

## The test strategy

- Unit tests. Covering the underlying components, utility classes, and domain services, unit tests need to validate various branching conditions. Only service exceptions are tested. Happy Path is not required.
- API tests. API testing is implemented using E2E tests. The goal of API testing is to verify that the overall process is smooth, and only test the normal process.

## TODO

- Distinguish User UserOperator, GroupMember GroupMemberOperator
- the cache
- Security audit (Important operation records)
- Code specification: checkStyle
- The REST interface is automatically generated
- Application Service and usecase are stored together
- External system connection (e.g. Wechat/Alipay)
- Asynchronous task
