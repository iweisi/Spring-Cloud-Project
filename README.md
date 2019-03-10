

## Spring Cloud

### 搭建环境：

| 工具    | 版本            |
| ----- | ------------- |
| JDK   | 1.8           |
| Maven | 3.X           |
| IDE   | IntelliJ IDEA |



### 关于我：

- 本人联系QQ：200538725 ，入门教程边学边写出来的，也搭建成功，主要目的为了准备学习Springcloud的朋友一些帮助
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

### 一、Eureka 注册中心 (重点使用)

> **Eureka是Spring Cloud Netflix微服务套件中的一部分，可以与Springboot构建的微服务很容易的整合起来。Eureka包含了服务器端和客户端组件。服务器端，也被称作是服务注册中心，用于提供服务的注册与发现。Eureka支持高可用的配置，当集群中有分片出现故障时，Eureka就会转入自动保护模式，它允许分片故障期间继续提供服务的发现和注册，当故障分片恢复正常时，集群中其他分片会把他们的状态再次同步回来。客户端组件包含服务消费者与服务生产者。在应用程序运行时，Eureka客户端向注册中心注册自身提供的服务并周期性的发送心跳来更新它的服务租约。同时也可以从服务端查询当前注册的服务信息并把他们缓存到本地并周期性的刷新服务状态。**

#### 1.1 Pom 配置

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

#### 1.2 启动类 配置

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
#### 1.3 Yml 配置

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

> **Spring Cloud Ribbon是一个基于HTTP和TCP的客户端负载均衡工具，它基于Netflix Ribbon实现。通过Spring Cloud的封装，可以让我们轻松地将面向服务的REST模版请求自动转换成客户端负载均衡的服务调用。Spring Cloud Ribbon虽然只是一个工具类框架，它不像服务注册中心、配置中心、API网关那样需要独立部署，但是它几乎存在于每一个Spring Cloud构建的微服务和基础设施中。因为微服务间的调用，API网关的请求转发等内容，实际上都是通过Ribbon来实现的，包括后续我们将要介绍的Feign，它也是基于Ribbon实现的工具。所以，对Spring Cloud Ribbon的理解和使用，对于我们使用Spring Cloud来构建微服务非常重要。**

#### 3.1 Bean 配置

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
#### 3.2 Eureka 服务
- 服务端若是集群，默认是轮询调用
  ![0](https://github.com/xr2117/Spring-Cloud-Project/blob/master/images/0.png)

#### 3.3 调用 配置 
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
>**Feign是一个声明式的Web服务客户端。这使得Web服务客户端的写入更加方便 要使用Feign创建一个界面并对其进行注释。它具有可插入注释支持，包括Feign注释和JAX-RS注释。Feign还支持可插拔编码器和解码器。Spring Cloud增加了对Spring MVC注释的支持，并使用Spring Web中默认使用的HttpMessageConverters。Spring Cloud集成Ribbon和Eureka以在使用Feign时提供负载均衡的http客户端。**

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
> **在一个分布式系统里，许多依赖不可避免的会调用失败，比如超时、异常等，如何能够保证在一个依赖出问题的情况下，不会导致整体服务失败，这个就是Hystrix需要  做 的事情。Hystrix提供了熔断、隔离、Fallback、cache、监控等功能，能够在一 个、或多个依赖同时出现问题时保证系统依然可用。**

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
##### 5.7.3 提供方主启动类
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
### 五、HystrixDashboard 服务监控

> Hystrix Dashboard是Hystrix的仪表盘组件，主要用来实时监控Hystrix的各项指标信息，通过界面反馈的信息可以快速发现系统中存在的问题。

#### 5.1 Pom 配置
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
        <!-- 同样使用 Feign -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-openfeign</artifactId>
        </dependency>
```
#### 5.2 启动类 配置
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
#### 5.3 需要监控的服务 pom配置
> 所有需要被监控的微服务都要引入该依赖


```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
```
#### 5.3 需要监控的服务 pom配置
> 所有需要被监控的微服务都要引入该依赖


```xml
        <!-- actuator 组件 健康检查、审计、统计和监控 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
```

#### 5.4 监控步骤

2.X的版本 好像有bug 笔者后续会补 在找原因



### 六、Zuul 网关
> Zuul 是 Netflix 开源的微服务网关，Spring Cloud 对 Zuul 进行了整合和增强。在 SpringCloud 体系中，Zuul 担任着网关的角色，对发送到服务端的请求进行一些预处理，比如安全验证、动态路由、负载分配等。

#### 6.1 Pom 配置
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
#### 6.2 启动类 配置
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
#### 6.3 Yml 配置
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
#### 6.4 使用网关访问
##### 6.4.1 controller 接口
```java
    @GetMapping("/zuul")
    public String zuul() {
        return "hello zuul";
    }
}
```
##### 6.4.2 不使用 zuul 访问
```yaml
#　http:// + 服务地址 + 服务端口+　控制器
http://localhost:9001/zuul
```

##### 6.4.3 使用 zuul 访问
```yaml
#　http:// + 网关地址 + 网关端口 + 访问的微服务名　+　控制器
http://localhost:9527/spring-cloud-provider/zuul
```
##### 6.4.4 路由访问映射
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
### 七、Config 配置中心

> **Spring Cloud Config为分布式系统中的外部配置提供服务器和客户端支持。使用Config Server，您可以为所有环境中的应用程序管理其外部属性。它非常适合spring应用，也可以使用在其他语言的应用上。随着应用程序通过从开发到测试和生产的部署流程，您可以管理这些环境之间的配置，并确定应用程序具有迁移时需要运行的一切。服务器存储后端的默认实现使用git，因此它轻松支持标签版本的配置环境，以及可以访问用于管理内容的各种工具。**

#### 7.1 Pom 配置 
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
#### 7.2 启动类 配置
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
#### 7.3 yml 配置
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
#### 7.4 Github 创建远程仓库

- 创建仓库

![8](https://github.com/xr2117/Spring-Cloud-Project/blob/master/images/8.png)

- 2. 拷贝地址

![3](https://github.com/xr2117/Spring-Cloud-Project/blob/master/images/3.png)

- 3. 复制cloen

![4](https://github.com/xr2117/Spring-Cloud-Project/blob/master/images/4.png)

##### 7.4.1 创建 Yml push到Github

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

#### 7.5 搭建config clinet端

##### 7.5.1 Pom 配置
```xml
        <!-- SpringCloud Config 客户端 -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-config-client</artifactId>
            <version>2.1.1.RELEASE</version>
        </dependency>
```
##### 7.5.2 启动类 配置
```java
@EnableDiscoveryClient
@SpringBootApplication
public class Client3355Application {

    public static void main(String[] args) {
        SpringApplication.run(Client3355Application.class, args);
    }

}

```
##### 7.5.3 (bootstrap.yml) 配置

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
#### 7.6 运行测试 + 运行流程介绍
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
### 八、Spring Cldou Bus（用于Spring Cloud Server）
> 使用Cloud Config 配置中心，如果实施动态的配置更新，需要使用Bus组件，
> 该组件需要配置好消息队列（RabbitMq），启动项目后会自动创建一个消息队列。

#### 8.1 Config Server 端
##### 8.1.1 Pom 配置
```xml
        <!-- Bus -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-bus-amqp</artifactId>
            <version>2.1.0.RC3</version>
        </dependency>
```
##### 8.1.2 启动类配置
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
##### 8.1.2 Yml 配置
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
#### 8.2 Config Clinet 端
##### 8.2.1 Pom 配置
```xml
        <!-- Bus -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-bus-amqp</artifactId>
            <version>2.1.0.RC3</version>
        </dependency>
```
##### 8.2.2 启动类配置
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
##### 8.2.3 Yml 配置
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
#### 8.3 测试动态刷新配置

##### 8.3.1 修改github 配置文件（修改前）
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
##### 8.3.2 访问client测试接口
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
##### 8.3.3. 修改后 (并且push到 github)
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
##### 8.3.4 访问Config server暴露的接口 进行刷新配置（消息队列自动实现）
`POST 请求 http://localhost:3344/actuator/bus-refresh`

##### 8.3.5.  访问client测试接口
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
##### 8.4 实现Bus 修改文件自动更，需要内网映射这里不做演示

> 实现需要内网映射，笔者没有做内网映射，这个很简单我就不做演示了

#### 8.4.1 Github Webhooks功能!

![8](https://github.com/xr2117/Spring-Cloud-Project/blob/master/images/7.png)

### 九、Cloud GateWay
> **在Spring boot2.0版本发布之后，spring cloud又集成和开发了很多新的模块和功能；spring boot2.0和spring boot1.5的差别可以说是很大了。今天介绍一些spring cloud gateway，spring cloud自己的网关，在1.x版本中，大家使用的较多的是Zuul网关，但Zuul只是Spring cloud整合Netflix的，并不是它自己的。今天大家一起来感受一下Spring cloud gateway的强大之处吧！**
>
> **网关的功能和作用，在我看来大概分为两块：无外乎就是路由转发和过滤功能！在实际使用时，其实spring cloud gateway很简单，不过就是配置文件和配置类，但实际上Spring cloud gateway比Zuul实现了更强大的过滤器功能，而且支持WebSocket长连接，可以说是非常强大的功能了。**


未完待续。
