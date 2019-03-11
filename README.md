## Spring Cloud

### 搭建环境：

| 工具           | 版本                |
| ------------ | ----------------- |
| JDK          | 1.8               |
| Maven        | 3.X               |
| IDE          | IntelliJ IDEA     |
| Spring Boot  | 2.1.3.RELEASE     |
| Spring Cloud | Greenwich.RELEASE |



### 关于我：

- 本人联系QQ：200538725 ，若发现问题或者有问题请教可以随时加我，主要目的为了准备学习SpringCloud的朋友一些帮助。
- 后续会更新其他得 如您觉得该项目对您有用，欢迎点击右上方的Star按钮，给予支持！！

### 模块介绍

| 服务名                             | 简介                   |
| ------------------------------- | -------------------- |
| spring-cloud-eureka             | Eureka注册中心 ，提供注册服务   |
| spring-cloud-consumer           | Eureka 客户端，消费者       |
| spring-cloud-provider-9001      | Eureka 客户端，提供者（负载均衡） |
| spring-cloud-provider-9002      | Eureka 客户端，提供者（负载均衡） |
| spring-cloud-hystrix-dashboard  | Eureka 客户端，服务监控      |
| spring-cloud-zuul               | Eureka 网关，用于路由       |
| spring-cloud-config             | Config 配置中心          |
| Spring-Cloud-Project-client3355 | config 客户端，实现远程配置    |

###内容介绍

| 序号   | 组件        | 简介           |
| ---- | --------- | ------------ |
| 一    | Eureka    | 注册中心         |
| 二    | Client    | 微服务客户端模板     |
| 三    | Ribbon    | 负载均衡         |
| 四    | Feign     | 负载均衡         |
| 五    | Hystrix   | 断路器          |
| 六    | Dashboard | Hystrix仪表盘组件 |
| 七    | Zuul      | 服务网关         |
| 八    | Config    | 配置中心         |
| 九    | Bus       | 消息总线         |
| 十    | GateWay   | 服务网关（Zuul升级） |



### 一、Eureka 注册中心 (重点使用)

#### 1.1 简介

​	**Eureka是Spring Cloud Netflix微服务套件中的一部分，可以与Springboot构建的微服务很容易的整合起来。**
Eureka包含了服务器端和客户端组件。服务器端，也被称作是服务注册中心，用于提供服务的注册与发现。	

​	 **Eureka支持高可用的配置，当集群中有分片出现故障时，Eureka就会转入自动保护模式，它允许分片故障期间继续提供服务的发现和注册，当故障分片恢复正常时，集群中其他分片会把他们的状态再次同步回来。客户端组件包含服务消费者与服务生产者。在应用程序运行时，Eureka客户端向注册中心注册自身提供的服务并周期性的发送心跳来更新它的服务租约。同时也可以从服务端查询当前注册的服务信息并把他们缓存到本地并周期性的刷新服务状态。**

#### 1.2 Pom 配置

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.1.3.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.cloud</groupId>
    <artifactId>eureka</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>eureka</name>
    <description>Demo project for Spring Boot</description>

    <properties>
        <java.version>1.8</java.version>
        <spring-cloud.version>Greenwich.RELEASE</spring-cloud.version>
    </properties>

    <dependencies>
        <!-- Eureka Server 服务端-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

    <repositories>
        <repository>
            <id>spring-milestones</id>
            <name>Spring Milestones</name>
            <url>https://repo.spring.io/milestone</url>
        </repository>
    </repositories>

</project>


```

#### 1.3 启动类 配置

```java

/**
 * @EnableEurekaServer
 * 声明该服务是Eureka注册中心
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaApplication.class, args);
    }

}

```
#### 1.4 Yml 配置

```yaml
eureka:
  client:
    service-url:
      # 注册中心地址
      defaultZone: http://localhost:8761/eureka/

      # Eureka高可用，如果有多个Eureka，将采用互相注册法，若是2个以上Eureka 使用','分割
      # 两个 Eureka 互相注册
      # 8761 注册到 8762
      # Eureka 8761 ：defaultZone: http://localhost:8762/eureka/
      # 8762 注册到 8761
      # Eureka 8762 ：defaultZone: http://localhost:8761/eureka/
      # 多个Eureka 以','分割  8761 8762 8763 以8762为例，以此类推
      # defaultZone: http://localhost:8761/eureka/，http://localhost:8763/eureka/

    # 禁止eureka向自己注册
    register-with-eureka: false
    fetchRegistry: false
  # 关闭eureka心跳警告(不建议):
  server:
    enable-self-preservation: false
# eureka 应用名称
spring:
  application:
    name: eureka
#tomcat端口（8761也是默认端口）
server:
  port: 8761
```
### 二、Client 客户端（所有Client模板）

#### 2.1 Pom 配置
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.1.3.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.cloud</groupId>
    <artifactId>clinet</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>clinet</name>
    <description>Demo project for Spring Boot</description>

    <properties>
        <java.version>1.8</java.version>
        <spring-cloud.version>Greenwich.RELEASE</spring-cloud.version>
    </properties>

    <dependencies>
        <!--  Spring boot web 模块-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <!--  Spring boot client 客户端-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

    <repositories>
        <repository>
            <id>spring-milestones</id>
            <name>Spring Milestones</name>
            <url>https://repo.spring.io/milestone</url>
        </repository>
    </repositories>

</project>

```
#### 2.2 启动类 配置
```java

/**
 * 表示该服务是Client客户端，
 * 并向Eureka注册
 */
@EnableDiscoveryClient
@SpringBootApplication
public class ClinetApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClinetApplication.class, args);
    }

}

```
#### 2.3 Yml 配置
```yaml
eureka:
  client:
    service-url:
      # 注册中心地址 向Eureka注册
      # Eureka如果是集群的话，注册到Eureka集群使用“,”分割
      # defaultZone: http://localhost:8761/eureka/，http://localhost:8762/eureka/
      defaultZone: http://localhost:8761/eureka/
    # 注册到Eureka Status自定义名称
  instance:
    instance-id: spring-cloud-client-8001
    # 注册到Eureka Status自定义IP
    prefer-ip-address: true
# client 应用名称
spring:
  application:
    name: spring-cloud-provider
#tomcat端口
server:
  port: 8001
```
> 带有数据库的 yml
```yaml
eureka:
  client:
    service-url:
      # 注册中心地址 向Eureka注册
      # Eureka如果是集群的话，注册到Eureka集群使用“,”分割
      # defaultZone: http://localhost:8761/eureka/，http://localhost:8762/eureka/
      defaultZone: http://localhost:8761/eureka/
    # 注册到Eureka Status自定义名称
  instance:
    instance-id: spring-cloud-client-8001
    # 注册到Eureka Status自定义IP
    prefer-ip-address: true
# client 应用名称
spring:
  application:
    name: spring-cloud-provider
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://106.13.66.245:3306/demo
    username: root
    password: 123456
#tomcat端口
server:
  port: 8001

```
### 三、Ribbon 负载均衡

### 3.1 简介

​	**Spring Cloud Ribbon是一个基于HTTP和TCP的客户端负载均衡工具，它基于Netflix Ribbon实现。通过Spring Cloud的封装，可以让我们轻松地将面向服务的REST模版请求自动转换成客户端负载均衡的服务调用。Spring Cloud Ribbon虽然只是一个工具类框架，它不像服务注册中心、配置中心、API网关那样需要独立部署，但是它几乎存在于每一个Spring Cloud构建的微服务和基础设施中。因为微服务间的调用，API网关的请求转发等内容，实际上都是通过Ribbon来实现的，包括后续我们将要介绍的Feign，它也是基于Ribbon实现的工具。所以，对Spring Cloud Ribbon的理解和使用，对于我们使用Spring Cloud来构建微服务非常重要。**

#### 3.2 Bean 配置

```java
@Configuration
public class CloudConfig {

    /**
     * Ribbon是基础Netflix Ribbon实现的一套客户端 负载均衡工具
     */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
```
#### 3.3 Eureka 服务
- 服务端若是集群，默认是轮询调用
  ![0](https://github.com/xr2117/Spring-Cloud-Project/blob/master/images/0.png)

#### 3.4 调用 配置 
```java
@RestController
public class TestController {

    @Autowired
    RestTemplate restTemplate;

    /**
     * 注册到Eureka的 http:// + 服务名称 + 控制器
     * @return
     */
    @GetMapping("/test")
    public String hello() {
        return restTemplate.getForObject("http://SPRING-CLOUD-CONSUMER/hello", String.class, String.class);
    }

}
```
### 四、Feign 负载均衡（重点使用）

#### 4.1 简介

​	**Feign是一个声明式的伪Http客户端，它使得写Http客户端变得更简单。使用Feign，只需要创建一个接口并注解。它具有可插拔的注解特性，可使用Feign 注解和JAX-RS注解。Feign支持可插拔的编码器和解码器。Feign默认集成了Ribbon，并和Eureka结合，默认实现了负载均衡的效果。**

**简而言之：**

- **Feign 采用的是基于接口的注解**

- **Feign 整合了ribbon，具有负载均衡的能力**

- **整合了Hystrix，具有熔断的能力**

  ​


#### 4.2 Pom 配置
```xml
        <!-- openfeign 组件 -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-openfeign</artifactId>
        </dependency>
```
#### 4.3 Provider 服务提供者(采用多模块)

> 提供者采用多模块对外暴露提供者要调用的接口以及entity，将被调用的接口打成jar包，提供给消费端（附一张代码结构图）

![1](https://github.com/xr2117/Spring-Cloud-Project/blob/master/images/1.png)

##### 4.3.1 Provider 启动类

```java
/**
 * 开启Feign，若是多模块需要扫描指定存放Feign接口的的路径
 */
@EnableFeignClients(basePackages = "com.cloud.api.entity.feign")
@EnableDiscoveryClient
@SpringBootApplication
public class Provider9001Application {

    public static void main(String[] args) {
        SpringApplication.run(Provider9001Application.class, args);
    }

}
```
##### 4.3.2 Feign 接口
```java
/**
 * name调用哪个服务的那个接口(注册到Eureka的服务)
 *
 */
@FeignClient(name = "SPRING-CLOUD-PROVIDER")
public interface ProviderFeign {

    /**
     * 提供的服务接口
     * findUserAll 接口名字可以不一致，只是为了理解接口作用
     * @GetMapping("/cloud") 为Controller 请求路径
     */
    @GetMapping("/cloud")
    User findUserAll();
}


```
##### 4.3.3 Prodvider Feign接口对应的Controller
```java
/**
 * Feign接口对应的Controller
 */
@RestController
public class ProviderController {

    @Autowired
    ProviderService providerService;

    @GetMapping("/cloud")
    public User cloud() {
        System.out.println("9001 控制器");
        return providerService.findUserAll();
    }
}
```
#### 4.4 Consumer 服务消费者

> 消费者进行调用提供者的接口（附一张结构图）

![2](https://github.com/xr2117/Spring-Cloud-Project/blob/master/images/2.png)

##### 4.4.1 Consumer 引入提供方提供的接口依赖
```xml
        <dependency>
            <groupId>com.cloud</groupId>
            <artifactId>api</artifactId>
            <version>0.0.1-SNAPSHOT</version>
        </dependency>
```
##### 4.4.2 Consumer 启动类
```java
/**
 * 引入提供方的接口jar包，同样要根据包路径进行扫描
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients("com.cloud.api.entity.feign")
public class ConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }

}
```
##### 4.4.3 注入接口进行调用
```java
/**
 * ProviderFeign 提供方的jar包提供的Feign接口，进行调用服务
 */
@RestController
public class ConsumerController {

    @Autowired
    ProviderFeign providerFeign;

    @RequestMapping("/hello")
    public User hello() {
        System.out.println("consumer 控制器");
        return providerFeign.findUserAll();
    }
}
```
### 五、Hystrix 断路器

#### 5.1 简介
​	**在微服务架构中，根据业务来拆分成一个个的服务，服务与服务之间可以相互调用（RPC），在Spring Cloud可以用RestTemplate+Ribbon和Feign来调用。为了保证其高可用，单个服务通常会集群部署。由于网络原因或者自身的原因，服务并不能保证100%可用，如果单个服务出现问题，调用这个服务就会出现线程阻塞，此时若有大量的请求涌入，Servlet容器的线程资源会被消耗完毕，导致服务瘫痪。服务与服务之间的依赖性，故障会传播，会对整个微服务系统造成灾难性的严重后果，这就是服务故障的“雪崩”效应。**

#### 5.2 Pom 配置
```xml
            <!-- hystrix 断路器 -->
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
            </dependency>
```
#### 5.3 启动类 配置
```java
/**
 * 开启Feign，若是多模块需要扫描指定存放Feign接口的的路径
 */
@EnableFeignClients(basePackages = "com.cloud.api.entity.feign")
@EnableDiscoveryClient
// 开启Hystrix
@EnableCircuitBreaker
@SpringBootApplication
public class Provider9001Application {

    public static void main(String[] args) {
        SpringApplication.run(Provider9001Application.class, args);
    }

}
```
#### 5.4 Yml 配置
```yaml
eureka:
  client:
    service-url:
      # 注册中心地址 向Eureka注册
      # Eureka如果是集群的话，注册到Eureka集群使用“,”分割
      # defaultZone: http://localhost:8761/eureka/，http://localhost:8762/eureka/
      defaultZone: http://localhost:8761/eureka/
    # 注册到Eureka Status自定义名称 
  instance:
    # 表明是增加hystrix的服务
    instance-id: spring-cloud-client-9002-hystrix
    # 注册到Eureka Status自定义IP
    prefer-ip-address: true
# client 应用名称
spring:
  application:
    name: spring-cloud-provider
#tomcat端口
server:
  port: 9002
```
#### 5.5 使用Hystrix 服务熔断
```java
/**
 * Feign接口对应的Controller
 */
@RestController
public class ProviderController {

    @Autowired
    ProviderService providerService;

    /**
     * 弱抛出异常，将会引发Hystrix机制，fallback方法
     */
    @HystrixCommand(fallbackMethod = "fallback")
    @GetMapping("/cloud/{num}")
    public User cloud(@PathVariable Integer num) {
        System.out.println("9001 控制器");
        if (num > 10) {
            throw new RuntimeException("num 不能大于0 ");
        }
        return providerService.findUserAll();
    }


    public User fallback(@PathVariable Integer num) {
        System.out.println("hystrix 介入..");
        return new User("hystrix", "123456");
    }
}
```
#### 5.6 使用Hystrix 服务降级 提供者（重点使用）
> **使用场景：类似天猫双11，并发量高会关掉某些查询数据库的服务进行降级，返回一个预处理结果**
> **提供者、消费者都要引入相同依赖**

##### 5.6.1 Pom 配置
```xml
        <!-- Hystrix -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
        </dependency>
```
##### 5.6.2 提供方启动类 配置
```java

/**
 * 开启Feign，若是多模块需要扫描指定存放Feign接口的的路径(必须)
 */
@EnableFeignClients("com.cloud.api.feign")
@SpringCloudApplication
public class Provider9001Application {

    public static void main(String[] args) {
        SpringApplication.run(Provider9001Application.class, args);
    }

}

```
##### 5.6.3 提供方使用Feign 接口通信

```java
/**
 * fallback 回退类，这里不要命名错误，是fallback，不是fallbackFactory
 */
@FeignClient(name = "SPRING-CLOUD-PROVIDER",fallback = ProviderFeignFallback.class)
public interface ProviderFeign {

    @GetMapping("/cloud/{num}")
    User findUserAll(@PathVariable Integer num);
}

```
##### 5.6.4 Feign对应的Controller

```java
    @GetMapping("/cloud/{num}")
    public User cloud(@PathVariable Integer num) {
        System.out.println("9001 控制器");
        if (num > 10) {
            throw new RuntimeException("num 不能大于0 ");
        }
        return providerService.findUserAll();
    }
```
##### 5.6.5 Hystrix 回退类
```java
@Component
public class ProviderFeignFallback implements ProviderFeign {

    @Override
    public User findUserAll(Integer num) {
        return new User("系统网络堵塞", "500");
    }
}
```

#### 5.7 使用Hystrix 服务降级 消费者（重点使用）
> 提供者、消费者都要引入相同依赖

##### 5.7.1 Pom 依赖配置
```xml
        <!-- Hystrix -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
        </dependency>
```

##### 5.7.2 Yml 依赖配置
```yaml
eureka:
  client:
    service-url:
      # 注册中心地址 向Eureka注册
      # Eureka如果是集群的话，注册到Eureka集群使用“,”分割
      # defaultZone: http://localhost:8761/eureka/，http://localhost:8762/eureka/
      defaultZone: http://localhost:8761/eureka/
    # 注册到Eureka Status自定义名称 hystrix表示该服务带熔断
  instance:
    instance-id: spring-cloud-client-8001
    # 注册到Eureka Status自定义IP
    prefer-ip-address: true
# client 应用名称
spring:
  application:
    name: spring-cloud-consumer
#tomcat端口
server:
  port: 8001
# 开启hystrix 熔断降级（这里如果不配置，将无效）
feign:
  hystrix:
    enabled: true
```
##### 5.7.3 消费方主启动类
```java
// 引入提供方的接口jar包，Feign 同样要根据包路径进行扫描
@EnableFeignClients("com.cloud.api.feign")
@SpringCloudApplication
// 扫描引入jar包，将@Component 注入容器
@ComponentScan(basePackages = "com.cloud")
public class ConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }

}
```
##### 5.7.4 消费者 Controller
```java
@RestController
public class ConsumerController {

    @Autowired
    ProviderFeign providerFeign;

    @RequestMapping("/hello/{num}")
    public User hello(@PathVariable Integer num) {
        /**
         * 调用引入提供方pom接口的方法 实现远程调用
         */
        return providerFeignFallback.findUserAll(num);
    }
}
```
### 六、HystrixDashboard 服务监控

### 6.1 简介

​	**Hystrix Dashboard是Hystrix的仪表盘组件，主要用来实时监控Hystrix的各项指标信息，通过界面反馈的信息可以快速发现系统中存在的问题。**

​	**使用Hystrixdashboard 必须在提供者提供的接口上使用@HystrixCommand注解,可以不提供熔断函数，但是不提供也将出现异常无回调函数，导致系统出现雪崩。**

#### 6.2 Pom 配置
```xml
        <!-- Cleint 客户端 -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
        <!-- hystrix 组件-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
        </dependency>
        <!-- dashboard 进行监控-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-hystrix-dashboard</artifactId>
        </dependency>
        <!-- actuator 组件 健康检查、审计、统计和监控 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
```

#### 6.3 启动类 配置
```java
/**
 * 开启EnableHystrixDashboard
 */
@EnableHystrixDashboard
@SpringBootApplication
public class HystrixdashboardApplication {

    public static void main(String[] args) {
        SpringApplication.run(HystrixdashboardApplication.class, args);
    }

}

```

#### 6.4 需要监控的服务 （Client端）

##### 6.4.1 Pom 配置
> 所有需要被监控的微服务都要引入该依赖(必须)

```xml
        <!-- actuator 组件 健康检查、审计、统计和监控 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
```

##### 6.4.2 启动类配置 
```java

/**
 * 开启Feign，若是多模块需要扫描指定存放Feign接口的的路径
 * @SpringCloudApplication是个组合注解包括的是下面三个,所有我用它代替
 * 1.@SpringBootApplication
 * 2.@EnableDiscoveryClient
 * 3.@EnableCircuitBreaker
 */
@EnableFeignClients("com.cloud.api.feign")
@SpringCloudApplication
public class Provider9001Application {

    public static void main(String[] args) {
        SpringApplication.run(Provider9001Application.class, args);
    }

}
```


##### 6.4.3 Yml 配置
```yaml
eureka:
  client:
    service-url:
      # 注册中心地址 向Eureka注册
      # Eureka如果是集群的话，注册到Eureka集群使用“,”分割
      # defaultZone: http://localhost:8761/eureka/，http://localhost:8762/eureka/
      defaultZone: http://localhost:8761/eureka/
    # 注册到Eureka Status自定义名称
  instance:
    instance-id: spring-cloud-client-9001-hystrix
    # 注册到Eureka Status自定义IP
    prefer-ip-address: true
# client 应用名称
spring:
  application:
    name: spring-cloud-provider
#tomcat端口
server:
  port: 9001
# 使用服务监控释放的访问路径,后续的配置中心bus刷新也需要释放
management:
  endpoints:
    web:
      exposure:
        include: "*"
```

##### 6.4.4 提供方 接口

```java
/**
 * Feign接口对应的Controller
 */
@RestController
public class ProviderController {

    @Autowired
    ProviderService providerService;

    /**
     * 弱抛出异常，将会引发Hystrix机制，fallback方法
     */
    @HystrixCommand(fallbackMethod = "fallbackMethod")
    @GetMapping("/cloud/{num}")
    public User cloud(@PathVariable Integer num) {
        System.out.println("9001 控制器");
        if (num > 10) {
            throw new RuntimeException("num 不能大于 10");
        }
        return providerService.findUserAll();
    }

    /**
     * cloud方法的熔断函数
     */
    private User fallbackMethod(@PathVariable Integer num) {
        return new User("回调函数", "123");
    }
}
```
##### 6.4.5 监控步骤
###### 6.4.5.1 启动HystrixDashdoard服务

> 运行Hystrix微服务 访问  `http://localhost:8000/hystrix` 会出现以下页面，也就代表服务运行成功（图片示例）

![6](https://github.com/xr2117/Spring-Cloud-Project/blob/master/images/6.png)

###### 6.4.5.2 使用监控
 - 在路径填写要被监控的服务即可
    `http://localhost:9001/actuator/hystrix.stream `
 - Delay：2000
 - Title：标题
     ![9](https://github.com/xr2117/Spring-Cloud-Project/blob/master/images/9.png)


### 七、Zuul 网关

### 7.1 简介

​	**Zuul的主要功能是路由转发和过滤器。路由功能是微服务的一部分，比如／api/user转发到到user服务，/api/shop转发到到shop服务。zuul默认和Ribbon结合实现了负载均衡的功能。Zuul 是 Netflix 开源的微服务网关，Spring Cloud 对 Zuul 进行了整合和增强。在 SpringCloud 体系中，Zuul 担任着网关的角色，对发送到服务端的请求进行一些预处理，比如安全验证、动态路由、负载分配等。**

- **Authentication**
- **Insights**
- **Stress Testing**
- **Canary Testing**
- **Dynamic Routing**
- **Service Migration**
- **Load Shedding**
- **Security**
- **Static Response handling**

#### 7.2 Pom 配置
```xml
        <!-- Eureka 客户端-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
        <!-- Zuul 网关-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-zuul</artifactId>
        </dependency>
```
#### 7.3 启动类 配置
```java
/**
 * 开启zuul网关
 */
@EnableZuulProxy
@SpringBootApplication
public class ZuulApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZuulApplication.class, args);
    }

}
```
#### 7.4 Yml 配置
```yaml
eureka:
  client:
    service-url:
      # 注册中心地址 向Eureka注册
      # Eureka如果是集群的话，注册到Eureka集群使用“,”分割
      # defaultZone: http://localhost:8761/eureka/，http://localhost:8762/eureka/
      defaultZone: http://localhost:8761/eureka/
    # 注册到Eureka Status自定义名称 hystrix表示该服务带熔断
  instance:
    instance-id: spring-cloud-zuul-9527
    # 注册到Eureka Status自定义IP
    prefer-ip-address: true
# client 应用名称
spring:
  application:
    name: spring-cloud-zuul
#tomcat端口
server:
  port: 9527
```
#### 7.5 使用网关访问
##### 7.5.1 controller 接口
```java
    @GetMapping("/zuul")
    public String zuul() {
        return "hello zuul";
    }
}
```
##### 7.5.2 不使用 zuul 访问
```yaml
#　http:// + 服务地址 + 服务端口+　控制器
http://localhost:9001/zuul
```

##### 7.5.3 使用 zuul 访问
```yaml
#　http:// + 网关地址 + 网关端口 + 访问的微服务名　+　控制器
http://localhost:9527/spring-cloud-provider/zuul
```
##### 7.5.4 路由访问映射
> - yml 配置

```yml
zuul:
  # 可以设置公共前缀（可选）
  prefix: /crazy
  # 隐藏 原服务地址  http://localhost:9527/spring-cloud-provider/zuul
  # 单独或多个地址以 ， 分割  ignored-services:  spring-cloud-provider,spring-cloud-consumer
  # "*" 所有 通配符
  ignored-services:  "*"
  # 路由映射
  routes:
    # provider. 自定义的
    provider.serviceId: spring-cloud-provider
    provider.path: /provider/**
    consumer.serviceId: spring-cloud-consumer
    consumer.path: /consumer/**
```
> - 访问地址
```yaml
# 带前缀 
http://localhost:9527/consumer/hello/1
# 不带前缀
http://localhost:9527/crazy/consumer/hello/1
```
### 八、Config 配置中心

### 8.1 简介

​	**Spring Cloud Config为分布式系统中的外部配置提供服务器和客户端支持。使用Config Server，您可以为所有环境中的应用程序管理其外部属性。它非常适合spring应用，也可以使用在其他语言的应用上。随着应用程序通过从开发到测试和生产的部署流程，您可以管理这些环境之间的配置，并确定应用程序具有迁移时需要运行的一切。服务器存储后端的默认实现使用git，因此它轻松支持标签版本的配置环境，以及可以访问用于管理内容的各种工具。**

#### 8.2 Pom 配置 
```xml
        <!-- SpringCloud Config server端 -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-config-server</artifactId>
            <version>2.1.1.RELEASE</version>
        </dependency>
        <!-- Eureka client 端-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
```
#### 8.3 启动类 配置
```java
/**
 * 声明该服务 是cloud config server
 */
@SpringBootApplication
@EnableConfigServer
@EnableDiscoveryClient
public class ConfigApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigApplication.class, args);
    }

}
```
#### 8.4 yml 配置
```yaml
eureka:
  client:
    service-url:
      # 注册中心地址 向Eureka注册
      # Eureka如果是集群的话，注册到Eureka集群使用“,”分割
      # defaultZone: http://localhost:8761/eureka/，http://localhost:8762/eureka/
      defaultZone: http://localhost:8761/eureka/
    # 注册到Eureka Status自定义名称
  instance:
    instance-id: spring-cloud-config-server-3344
    # 注册到Eureka Status自定义IP
    prefer-ip-address: true
# client 应用名称
spring:
  application:
    name: spring-cloud-config-server
    # Github远程仓库地址
  cloud:
    config:
      server:
        git:
          uri: git@github.com:xr2117/Spring-Cloud-Yml.git
#tomcat端口
server:
  port: 3344
```
#### 8.5 Github 创建远程仓库

- 创建仓库

![8](https://github.com/xr2117/Spring-Cloud-Project/blob/master/images/8.png)

- 2. 拷贝地址

![3](https://github.com/xr2117/Spring-Cloud-Project/blob/master/images/3.png)

- 3. 复制cloen

![4](https://github.com/xr2117/Spring-Cloud-Project/blob/master/images/4.png)

##### 8.5.1 创建 Yml push到Github

> - 1. 新建yml文件（重点：格式一定为UTF-8）
```yaml
eureka:
  client:
    service-url:
      # 注册中心地址 向Eureka注册
      # Eureka如果是集群的话，注册到Eureka集群使用“,”分割
      # defaultZone: http://localhost:8761/eureka/，http://localhost:8762/eureka/
      defaultZone: http://localhost:8761/eureka/
    # 注册到Eureka Status自定义名称
  instance:
    instance-id: spring-cloud-github-config-client-3355
    # 注册到Eureka Status自定义IP
    prefer-ip-address: true
# client 应用名称
spring:
  application:
    name: spring-cloud-github-config-client
#tomcat端口
server:
  port: 3355
env:
  dev 
# spring-cloud 配置中心（UTF-8格式保存）
```
> - 2. push 到github 远程仓库

![5](https://github.com/xr2117/Spring-Cloud-Project/blob/master/images/5.png)

### 8.6 搭建config clinet端

#### 8.6.1 Pom 配置
```xml
        <!-- SpringCloud Config 客户端 -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-config-client</artifactId>
            <version>2.1.1.RELEASE</version>
        </dependency>
```
#### 8.6.2 启动类 配置
```java
@EnableDiscoveryClient
@SpringBootApplication
public class Client3355Application {

    public static void main(String[] args) {
        SpringApplication.run(Client3355Application.class, args);
    }

}

```
#### 8.6.3 (bootstrap.yml) 配置

- 使用Spring Cloud 配置中心，配置文件名字为 bootstrap.yml (重点：命名规范)

```yaml
# 这次不是应用名称，而是github对应的.yml配置文件名的前缀
spring:
  application:
    name: spring-cloud-client
  cloud:
    config:
      discovery:
        # 开启cloud配置
        enabled: true
        # config 配置中心 服务名称 SPRING-CLOUD-CONFIG-SERVER
        service-id: SPRING-CLOUD-CONFIG-SERVER
      # 本次访问的配置项
      profile: dev
```
### 8.7 运行测试 + 运行流程介绍
> clinet端通过yml配置找到对应注册中心的server服务，server服务配置好了github仓库进行拉取配置，clinet读取server拉取下来的配置进行启动
>
```java
@RestController
public class ConfigClientRest {

    @Value("${spring.application.name}")
    private String applicationName;


    @Value("${server.port}")
    private String port;

    @Value("${env}")
    private String env;

    @RequestMapping("/config")
    public void getConfig() {
        System.out.println("applicationName : " + applicationName);
        System.out.println("port : " + port);
        System.out.println("env : " + env);
    }
}


```
### 九、Spring Cldou Bus（用于Spring Cloud Server）

### 9.1 简介

​	**使用Cloud Config 配置中心，如果实施动态的配置更新，需要使用Bus组件，该组件需要配置好消息队列（RabbitMq），启动项目后会自动创建一个消息队列。**

#### 9.2 Config Server 端
##### 9.2.1 Pom 配置
```xml
        <!-- Bus -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-bus-amqp</artifactId>
            <version>2.1.0.RC3</version>
        </dependency>
```
##### 9.2.2 启动类配置
```java
/**
 * 该服务是个cloud config server
 */
@SpringBootApplication
@EnableConfigServer
@EnableDiscoveryClient
public class ConfigApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigApplication.class, args);
    }

}
```
##### 9.2.2 Yml 配置
```yaml
  # bus 配置rabbitmq
  rabbitmq:
    host: 106.13.66.245
    port: 5672
    username: guest
    password: guest
#  暴露接口 http://localhost:3344/actuator/bus-refresh 刷新配置，cloud默认关闭改接口
management:
  endpoints:
    web:
      exposure:
        include: "*"
```
#### 9.3 Config Clinet 端
##### 9.3.1 Pom 配置
```xml
        <!-- Bus -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-bus-amqp</artifactId>
            <version>2.1.0.RC3</version>
        </dependency>
```
##### 9.3.2 启动类配置
```java

/**
 * 该服务是cloud client
 */
@EnableDiscoveryClient
@SpringBootApplication
public class Client3355Application {

    public static void main(String[] args) {
        SpringApplication.run(Client3355Application.class, args);
    }

}

```
##### 9.3.3 Yml 配置
```yaml
# 这次不是应用名称，而是github对应的.yml配置文件名的前缀
spring:
  application:
    name: spring-cloud-client
  cloud:
    config:
      discovery:
        # 开启cloud配置
        enabled: true
        # config 配置中心 服务名称 SPRING-CLOUD-CONFIG-SERVER
        service-id: SPRING-CLOUD-CONFIG-SERVER
      # 本次访问的配置项
      profile: dev
```
#### 9.4 测试动态刷新配置

##### 9.4.1 修改github 配置文件（修改前）
```yaml
# 这次不是应用名称，而是github对应的.yml配置文件名的前缀
spring:
  application:
    name: spring-cloud-client
  cloud:
    config:
      discovery:
        # 开启cloud配置
        enabled: true
        # config 配置中心 服务名称 SPRING-CLOUD-CONFIG-SERVER
        service-id: SPRING-CLOUD-CONFIG-SERVER
      # 本次访问的配置项
      profile: dev
```
##### 9.4.2 访问client测试接口
```java
@RestController
// 实时刷新
@RefreshScope
public class ConfigClientRest {

    @Value("${spring.application.name}")
    private String applicationName;


    @Value("${server.port}")
    private String port;

    @Value("${env}")
    private String env;

    @RequestMapping("/config")
    public void getConfig() {
        System.out.println("applicationName : " + applicationName);
        System.out.println("port : " + port);
        System.out.println("env : " + env);
    }
}
```
##### 9.4.3. 修改后 (并且push到 github)
```yaml
# 这次不是应用名称，而是github对应的.yml配置文件名的前缀
spring:
  application:
    name: spring-cloud-client
  cloud:
    config:
      discovery:
        # 开启cloud配置
        enabled: true
        # config 配置中心 服务名称 SPRING-CLOUD-CONFIG-SERVER
        service-id: SPRING-CLOUD-CONFIG-SERVER
      # 本次访问的配置项
      profile: dev测试自动刷新配置bus
```
##### 9.4.4 访问Config server暴露的接口 进行刷新配置（消息队列自动实现）
`POST 请求 http://localhost:3344/actuator/bus-refresh`

##### 9.4.5.  访问client测试接口
```java
@RestController
// 实时刷新
@RefreshScope
public class ConfigClientRest {

    @Value("${spring.application.name}")
    private String applicationName;


    @Value("${server.port}")
    private String port;

    @Value("${env}")
    private String env;

    @RequestMapping("/config")
    public void getConfig() {
        System.out.println("applicationName : " + applicationName);
        System.out.println("port : " + port);
        System.out.println("env : " + env);
    }
}
```
##### 9.5 实现Bus 修改文件自动更，需要内网映射这里不做演示

> 实现需要内网映射，笔者没有做内网映射，这个很简单我就不做演示了

#### 9.5.1 Github Webhooks功能!

![8](https://github.com/xr2117/Spring-Cloud-Project/blob/master/images/7.png)

### 十、Cloud GateWay
​	**在Spring boot2.0版本发布之后，spring cloud又集成和开发了很多新的模块和功能；spring boot2.0和spring boot1.5的差别可以说是很大了。今天介绍一些spring cloud gateway，spring cloud自己的网关，在1.x版本中，大家使用的较多的是Zuul网关，但Zuul只是Spring cloud整合Netflix的，并不是它自己的。**
**网关的功能和作用，在我看来大概分为两块：无外乎就是路由转发和过滤功能！在实际使用时，其实spring cloud gateway很简单，不过就是配置文件和配置类，但实际上Spring cloud gateway比Zuul实现了更强大的过滤器功能，而且支持WebSocket长连接，可以说是非常强大的功能了。**


未完待续。
