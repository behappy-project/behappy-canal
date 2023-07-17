> 本家：https://github.com/NormanGyllenhaal/canal-client

## 介绍
本项目以学习为初衷，本家链接地址在上方，关于具体介绍和特性可以进入链接看下

主要修改部分：
1. 修改了一些问题，相关issue可见release history
2. 支持jdk17/springboot3.x（不支持jdk8）
3. 支持mybatis plus注解
4. 升级canal-client版本

## TODO
- [ ] 批量数据处理，见【https://github.com/NormanGyllenhaal/canal-client/issues/6】
- [x] rabbitmq对接支持

## 使用方式
> 要求
> 
> java17/springboot3.x

### 搭建canal server/mysql示例
git clone https://github.com/behappy-hospital/behappy-docker-application.git \
&& docker-compose -f mysql/docker-compose.yml up -d \
&& {执行canal/下的sql文件}
&& docker-compose -f canal/docker-compose.yml up -d

### 搭建canal client

java 方式
```xml
<dependency>
    <groupId>io.github.behappy-project</groupId>
    <artifactId>behappy-canal-client</artifactId>
    <version>3.0.3</version>
</dependency>
```

spring boot 方式 maven 依赖
```xml
<dependency>
    <groupId>io.github.behappy-project</groupId>
    <artifactId>behappy-canal-spring-boot-starter</artifactId>
    <version>3.0.3</version>
</dependency>
```

## 配置说明

| 属性              | 描述                                                                     | 默认值    |
| ----------------- |------------------------------------------------------------------------|--------|
| canal.mode        | canal 客户端类型 目前支持4种类型 simple,cluster,zk,kafka(kafka 目前支持flatMessage 格式) | simple |
| canal.filter      | canal过滤的表名称，如配置则只订阅配置的表                                                | ""     |
| canal.batch-size  | 消息的数量，超过该次数将进行一次消费                                                     | 1(个)   |
| canal.timeout     | 消费的时间间隔(s)                                                             | 1s     |
| canal.server      | 服务地址,多个地址以,分隔 格式 host:{port}                                           | null   |
| canal.destination | canal 的instance 名称,kafka模式为topic 名称,rabbitMQ模式为queueName               | null   |
| canal.user-name   | canal/rabbitMQ 的用户名                                                    | null   |
| canal.password    | canal/rabbitMQ 的密码                                                              | null   |
| canal.group-id    | kafka groupId 消费者订阅消息时可使用，kafka canal 客户端                              | null   |
| canal.async       | 是否是异步消费，异步消费时，消费时异常将导致消息不会回滚，也不保证顺序性                                   | true   |
| canal.partition   | kafka partition                                                        | null   |
| canal.vhost   | rabbitMQ vhost                                                     | "/"    |
| canal.accessKey   | rabbitMQ accessKey                                                     | ""   |
| canal.secretKey   | rabbitMQ secretKey                                                     | ""   |
| canal.resourceOwnerId   | rabbitMQ resourceOwnerId                                                     | null   |

## 订阅数据库的增删改操作
实现EntryHandler<T> 接口，泛型为想要订阅的数据库表的实体对象，
该接口的方法为 java 8 的 default 方法，方法可以不实现，如果只要监听增加操作，只实现增加方法即可  
下面以一个t_user表的user实体对象为例,
默认情况下，将使用实体对象的jpa 注解 @Table中的表名来转换为EntryHandler中的泛型对象，
```java
public class UserHandler implements EntryHandler<User>{
}
```
如果实体类没有使用jpa @Table的注解，也可以使用@CanalTable 注解在EntryHandler来标记表名，例如
```java
@CanalTable(value = "t_user")
@Component
public class UserHandler implements EntryHandler<User>{
   /**
   *  新增操作
    */
    @Override
    public void insert(User user) {
    }
    /**
    * 对于更新操作来讲，before 中的属性只包含变更的属性，after 包含所有属性，通过对比可发现那些属性更新了
    */
    @Override
    public void update(User before, User after) {
    }
    /**
    *  删除操作
    */
    @Override
    public void delete(User user) {
   }
}
```
另外也支持统一的处理@CanalTable(value="all"),这样除去存在EntryHandler的表以外，其他所有表的处理将通过该处理器,统一转为Map<String, String>对象
```java
@CanalTable(value = "all")
@Component
public class DefaultEntryHandler implements EntryHandler<Map<String, String>> {
        @Override
        public void insert(Map<String, String> map) {
        }
        @Override
        public void update(Map<String, String> before, Map<String, String> after) {
        }
        @Override
        public void delete(Map<String, String> map) {
        }
}
```
如果你想获取除实体类信息外的其他信息，可以使用
```java
CanalModel canal = CanalContext.getModel();
```

## 具体使用可以查询项目 demo 示例
[behappy-canal-example](behappy-canal-example)

## 参考
创建canal-server容器报错Exited 139
https://github.com/alibaba/canal/issues/4026

canal1.1.6版本，启动后，提示'Base Table' doesn't exist
https://github.com/alibaba/canal/issues/4291

canal HA
https://github.com/alibaba/canal/wiki/AdminGuide#ha%E6%A8%A1%E5%BC%8F%E9%85%8D%E7%BD%AE

docker搭建canal以及配置集群
https://juejin.cn/post/7106769991722074120

canal demo学习
https://juejin.cn/post/6945784603508637709#heading-0

Message和FlatMessage
https://blog.csdn.net/l714417743/article/details/120836263

canal 详解
https://blog.csdn.net/ma15732625261/article/details/88368373

canal + kafka 事务一致性问题
https://www.jianshu.com/p/1ecfafbd6237

canal 整合RabbitMQ
https://www.jianshu.com/p/60a9176a8825

Could not find first log file name in binary log index file
https://github.com/alibaba/canal/issues/156
