# Spring Cloud Alibaba Example

在微服务架构中，`Spring Cloud Alibaba`提供了一些优秀的组件来解决微服务场景下的问题，例如`PRC调用`、服务注册发现、配置管理、流控容错、分布式事务等。本项目以`Spring Cloud Alibaba`提供的组件为基础，简单介绍它们在微服务场景下的应用。

## 环境

-   `java 8`
-   `spring boot 2.3.7.RELEASE`
-   `spring cloud alibaba 2.2.2.RELEASE`
-   `nacos 1.4.1`
-   `seata 1.4.1`

## 服务注册与发现

使用`nacos`来做注册中心和配置中心，首先要下载最新的`nacos release 文件`，具体参考[官方nacos快速开始文档](https://nacos.io/zh-cn/docs/quick-start.html)。然后在应用用加入依赖

```xml
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
</dependency>
```

并用`@@EnableDiscoveryClient`标注启动类，`spring`发现该注解以及对应配置属性后，将会自动向`nacos`服务器注册，配置文件如下

```properties
# Nacos注册中心
spring.cloud.nacos.discovery.server-addr=localhost:8848
spring.cloud.nacos.discovery.namespace=public
spring.cloud.nacos.discovery.username=nacos
spring.cloud.nacos.discovery.password=nacos
```

启动应用后，登陆http://localhost:8848/nacos 默认用户名与密码为`nacos`:`nacos`，可以看到应用已经正常注册进来。

![1616227429501](README/1616227429501.png)

## 配置中心

`nacos`同样有配置中心的作用，使用时先添加依赖

```xml
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
</dependency>
```

使用`spring`原生注解`@Value`即可获取到配置中心对应的值，同时可以使用`@RefreshScope`打开配置的自动刷新功能，如下

```java
@RefreshScope
@RestController
public class TestController {
    @Value("${test.value:rain}")
    private String testValue;

    @GetMapping("")
    public String test() {
        return testValue;
    }
}
```

`nacos`配置中心的相关配置需写在`bootstrap.properties`中

```properties
# Nacos配置中心
spring.cloud.nacos.config.username=nacos
spring.cloud.nacos.config.password=nacos
spring.cloud.nacos.config.contextPath=/nacos
spring.cloud.nacos.config.server-addr=localhost:8848
# Nacos 配置中心的namespace。需要注意，如果使用public的namespace，请不要填写这个值，直接留空即可
# spring.cloud.nacos.config.namespace=
```

在`nacos`控制台添加相应配置后，访问应用即可观察到对应值。

## RPC调用

`Apache Dubbo`是一款高性能、轻量级的开源`java`服务框架。提供了诸如面向接口代理的高性能`RPC调用`、智能容错和负载均衡、服务自动注册和发现、高度可扩展、运行期流量调度、可视化服务治理和运维等功能。

下面以简单的生产者-消费者模型来说明其`RPC调用`的功能。

### 依赖

```xml
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
</dependency>
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-dubbo</artifactId>
</dependency>
```

### 公共接口

```java
public interface HelloService {
    String sayHello();
}
```

### 生产者

使用`@DubboService`注解标识服务接口的实现，更详细的场景及使用方式见[官方Dubbo用法示例](https://dubbo.apache.org/zh/docs/v2.7/user/examples/)

```java
@DubboService(interfaceName = "hello.service")
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello() {
        return "hello world 3";
    }
}
```

配置

```properties
spring.application.name=service-provider
# Dubbo
dubbo.protocol.id=dubbo
dubbo.protocol.name=dubbo
dubbo.protocol.port=-1
dubbo.cloud.subscribed-services=service-provider
dubbo.scan.base-packages=com.rainlf.spring.cloud.alibaba.example.serviceprovider
# Nacos
spring.cloud.nacos.discovery.server-addr=10.2.62.5:8848
spring.cloud.nacos.discovery.namespace=public
spring.cloud.nacos.discovery.username=nacos
spring.cloud.nacos.discovery.password=nacos
```

### 消费者

使用`@RestController`注解来注入服务接口，使用`, check = false`属性来用懒加载的方式避免`Bean`生成时的检测（例如此刻服务并不可用）。

```java
@RestController
public class ConsumerController {

    @DubboReference(interfaceName = "hello.service", check = false)
    private HelloService helloService;

    @GetMapping("")
    public String sayHello() {
        return helloService.sayHello();
    }
}
```

配置，订阅的服务名为生成者的应用名称，订阅多个服务方时，使用逗号隔开

```properties
spring.application.name=service-consumer
server.port=8080
# Dubbo
dubbo.protocol.id=dubbo
dubbo.protocol.name=dubbo
dubbo.protocol.port=-1
dubbo.cloud.subscribed-services=service-provider
dubbo.scan.base-packages=com.rainlf.spring.cloud.alibaba.example.serviceconsumer
# Nacos
spring.cloud.nacos.discovery.server-addr=10.2.62.5:8848
spring.cloud.nacos.discovery.namespace=public
spring.cloud.nacos.discovery.username=nacos
spring.cloud.nacos.discovery.password=nacos
```

## 流控保护



## 分布式事务

### Seata TC 服务器搭建()