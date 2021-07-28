# 版本选型

*****

首先贴出SpringCloud官方网站：https://spring.io/projects/spring-cloud

以下是截止到2021年6月17日，spring官网上最新的SpringCloud与SpringBoot依赖图

![image-20210617132925340](https://quanjichao.oss-cn-beijing.aliyuncs.com/images/image-20210617132925340.png)

可以看到最新版本是2020年更新的“I”版

![image-20210617134213146](https://quanjichao.oss-cn-beijing.aliyuncs.com/images/image-20210617134213146.png)

点击右侧Reference Doc.按钮查看推荐版本

![image-20210617134333142](https://quanjichao.oss-cn-beijing.aliyuncs.com/images/image-20210617134333142.png)

可以看到当前最新的2020.0.3版本对应推荐的boot版本是2.4.6

github上，推荐使用版本选型：

https://github.com/alibaba/spring-cloud-alibaba/wiki/

![image-20210721101712697](https://quanjichao.oss-cn-beijing.aliyuncs.com/images/image-20210721101712697.png)

# 组件选型

![image-20210617135637876](https://quanjichao.oss-cn-beijing.aliyuncs.com/images/image-20210617135637876.png)

NAcos是重中之重

# 父工程

## 准备工作

约定>配置>编码

选择maven骨架

![image-20210617144614327](https://quanjichao.oss-cn-beijing.aliyuncs.com/images/image-20210617144614327.png)

首先统一编码格式：setting-editor-file encodings: UTF-8

![image-20210617142556608](https://quanjichao.oss-cn-beijing.aliyuncs.com/images/image-20210617142556608.png)

开启lombok注解支持：setting->build,Execution,Deployment->Compiler->Annotation Processors->Enable annotation processing按钮

java编译版本：setting->build,Execution,Deployment->Compiler->java Compiler

![image-20210617143712239](https://quanjichao.oss-cn-beijing.aliyuncs.com/images/image-20210617143712239.png)

## 主POM文件

使用聚合工程形式创建项目方便版本管理，引入spring-cloud、spring-boot、spring-cloud-alibaba以及一些常用的jar包

```xml
<!--  统一管理jar包版本-->
<properties>
  <junit.version>4.12</junit.version>
  <log4j.version>1.2.17</log4j.version>
  <lombok.version>1.16.18</lombok.version>
  <mysql.version>5.1.47</mysql.version>
  <druid.version>1.1.18</druid.version>
  <mybatis.version>1.3.0</mybatis.version>
  <lombok.version>1.18.20</lombok.version>
</properties>
<!--  子模块继承后，无需再写version和artifactId-->
<dependencyManagement>
  <dependencies>
    <!--spring-boot-->
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-dependencies</artifactId>
      <version>2.2.2.RELEASE</version>
      <type>pom</type>
      <scope>import</scope>
    </dependency>
    <!--spring-cloud-->
    <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-dependencies</artifactId>
      <version>Hoxton.SR1</version>
      <type>pom</type>
      <scope>import</scope>
    </dependency>
    <!--spring-cloud-alibaba-->
    <dependency>
      <groupId>com.alibaba.cloud</groupId>
      <artifactId>spring-cloud-alibaba-dependencies</artifactId>
      <version>2.1.0.RELEASE</version>
      <type>pom</type>
      <scope>import</scope>
    </dependency>
    <!--mybatis-->
    <dependency>
      <groupId>org.mybatis.spring.boot</groupId>
      <artifactId>mybatis-spring-boot-starter</artifactId>
      <version>${mybatis.version}</version>
    </dependency>
    <!--druid-->
    <dependency>
      <groupId>com.alibaba</groupId>
      <artifactId>druid-spring-boot-starter</artifactId>
      <version>${druid.version}</version>
    </dependency>
    <!--mysql-->
    <dependency>
      <groupId>mysql</groupId>
      <artifactId>mysql-connector-java</artifactId>
      <version>${mysql.version}</version>
    </dependency>
    <!--lombok-->
    <dependency>
      <groupId>org.projectlombok</groupId>
      <artifactId>lombok</artifactId>
      <version>${lombok.version}</version>
      <!--provided表示参与项目的编译、运行和测试，但在打包的时候不用打包进去-->
      <scope>provided</scope>
    </dependency>
  </dependencies>
</dependencyManagement>
```

## 创建一个微服务

1. 新建module

2. 引入pom（不写版本号的情况下，子模块会自动到父模块寻找版本号）

   ```xml
   <dependencies>
       <!--web模块-->
       <dependency>
           <groupId>org.springframework.boot</groupId>
           <artifactId>spring-boot-starter-web</artifactId>
       </dependency>
       <!--actuator程序健康检查-->
       <dependency>
           <groupId>org.springframework.boot</groupId>
           <artifactId>spring-boot-starter-actuator</artifactId>
       </dependency>
       <!--mybatis-->
       <dependency>
           <groupId>org.mybatis.spring.boot</groupId>
           <artifactId>mybatis-spring-boot-starter</artifactId>
       </dependency>
       <!--durid-->
       <dependency>
           <groupId>com.alibaba</groupId>
           <artifactId>druid-spring-boot-starter</artifactId>
       </dependency>
       <!--mysql-->
       <dependency>
           <groupId>mysql</groupId>
           <artifactId>mysql-connector-java</artifactId>
       </dependency>
       <!--devtools热部署-->
       <dependency>
           <groupId>org.springframework.boot</groupId>
           <artifactId>spring-boot-devtools</artifactId>
           <!--runtime表示不参与项目编译，参与测试和运行周期-->
           <scope>runtime</scope>
           <!--true表示接受依赖传递-->
           <optional>true</optional>
       </dependency>
       <!--lombok-->
       <dependency>
           <groupId>org.projectlombok</groupId>
           <artifactId>lombok</artifactId>
           <optional>true</optional>
       </dependency>
   </dependencies>
   ```

3. 编写yml配置

   ```yml
   server:
     port: 8081
   spring:
     application:
       #微服务名称
       name: cloud-provider-8081
     datasource:
       type: com.alibaba.druid.pool.DruidDataSource
       driver-class-name: org.gjt.mm.mysql.Driver
       url: jdbc:mysql://47.100.42.118:3306/payment?useUnicode=true&characterEncoding=UTF-8
       username: root
       password: Root123456
   mybatis:
     mapper-locations: classpath:mapper/*.xml
     #所有entity类所在包
     type-aliases-package: com.dxc.entity
   ```

4. 配置主启动类

   ```java
   @SpringBootApplication
   @MapperScan("com.dxc.mapper")
   public class Application_8081 {
       public static void main(String[] args) {
           SpringApplication.run(Application_8081.class,args);
       }
   }
   ```

5. 编写业务类

   如果dao层用的是@Repository注解，不要忘记在启动类上加包扫描注解@MapperScan

## 工程重构

业务场景：现在项目A要使用项目B提供的微服务，那么A就必然会使用到项目B的entity实体类，显然在项目A与项目B中构建相同的两个实体类是一种效率低下的办法。

解决办法：

1. 创建一个公用模块，用来提供实体类，以及一些常用工具

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <project xmlns="http://maven.apache.org/POM/4.0.0"
            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
       <parent>
           <artifactId>spring-cloud-qjc</artifactId>
           <groupId>spring-cloud-qjc</groupId>
           <version>1.0-SNAPSHOT</version>
       </parent>
       <modelVersion>4.0.0</modelVersion>
   
       <artifactId>cloud-api-tools</artifactId>
   
       <dependencies>
           <dependency>
               <groupId>org.projectlombok</groupId>
               <artifactId>lombok</artifactId>
               <optional>true</optional>
           </dependency>
           <!--一个非常好用的java工具类库-->
           <dependency>
               <groupId>cn.hutool</groupId>
               <artifactId>hutool-all</artifactId>
               <version>5.1.0</version>
           </dependency>
       </dependencies>
   
   </project>
   ```

2. 在最外层主pom文件的 dependencyManagement 标签中引入这个公用模块的坐标

   ```xml
   <dependencyManagement>
     <dependencies>
       <dependency>
         <groupId>spring-cloud-qjc</groupId>
         <artifactId>cloud-api-tools</artifactId>
         <version>${project.version}</version>
       </dependency>
         ....
       </dependencies>
   </dependencyManagement>
   ```

3.  最后在其他模块中引入这个坐标即可(版本自动继承父工程)

   ```xml
   <dependencies>
       <dependency>
           <groupId>spring-cloud-qjc</groupId>
           <artifactId>cloud-api-tools</artifactId>
       </dependency>
   </dependencies>
   ```

   其他模块所需的工具类、entity实体类都可以在这个公用模块中共享

# Nacos版本依赖

![image-20210621150410979](https://quanjichao.oss-cn-beijing.aliyuncs.com/images/image-20210621150410979.png)

![image-20210621150510002](https://quanjichao.oss-cn-beijing.aliyuncs.com/images/image-20210621150510002.png)

# Nacos配置中心

微服务主流配置中心对比

![image-20210621093659484](https://quanjichao.oss-cn-beijing.aliyuncs.com/images/image-20210621093659484.png)

![image-20210621093812785](https://quanjichao.oss-cn-beijing.aliyuncs.com/images/image-20210621093812785.png)

## 启动naocs

docker命令启动nacos

```
docker run --restart=always --name nacos -p 8848:8848 --env MODE=standalone --env SPRING_DATASOURCE_PLATFORM=mysql --env MYSQL_SERVICE_HOST=${ip地址} --env MYSQL_SERVICE_PORT=3306 --env MYSQL_SERVICE_DB_NAME=nacos   --env MYSQL_SERVICE_USER=root --env MYSQL_SERVICE_PASSWORD=${密码}  --env JVM_XMS=512m --env JVM_XMX=512m --env JVM_XMN=256m -d nacos/nacos-server:1.4.0
```

mysql数据库导入初始化sql：

https://quanjichao.oss-cn-beijing.aliyuncs.com/images/nacos_config.sql

访问地址：http://host:8848/nacos进入登录页面

默认用户名密码：nacos     nacos

## 配置模型

对于nacos,通过namespace、group、data id能够定位一个配置集

![image-20210624161538848](https://quanjichao.oss-cn-beijing.aliyuncs.com/images/image-20210624161538848.png)

![image-20210624161736436](https://quanjichao.oss-cn-beijing.aliyuncs.com/images/image-20210624161736436.png)

![image-20210624161908876](https://quanjichao.oss-cn-beijing.aliyuncs.com/images/image-20210624161908876.png)

![image-20210624162005210](https://quanjichao.oss-cn-beijing.aliyuncs.com/images/image-20210624162005210.png)

默认命名空间是 public

![image-20210624162042685](https://quanjichao.oss-cn-beijing.aliyuncs.com/images/image-20210624162042685.png)

## 远程获取配置

nacos配置中心新建配置：

![image-20210626113528431](https://quanjichao.oss-cn-beijing.aliyuncs.com/images/image-20210626113528431.png)

resource包下新建bootstarp.yml文件：

```yml
server:
  port: 8081
spring:
  application:
    name: provider8081 # 微服务名称，也是配置中心里的Data id
  cloud:
    nacos:
      config:
        server-addr: host:8848 # IP地址 
        file-extension: yaml # 配置文件类型
        namespace: test # 命名空间-环境
        group: STORE_GROUP # 组-项目
```

PS：如遇到无法读取bootstrap.yml的情况，有可能是因为中文注释的原因。解决办法：Settings---Editor---File Encodings中将所有编码格式改为utf-8（先删除所有中文注释）

点击监听查询，可以看到哪个IP从注册中心获取了配置

![image-20210626115655622](https://quanjichao.oss-cn-beijing.aliyuncs.com/images/image-20210626115655622.png)

## 动态更新配置

1.在需要动态更新配置的类上加上```@RefreshScope```注解

2.```@Value```注解属于spring框架，用于读取配置文件的信息

```java
//Value注解的作用：获取配置文件中的信息
    @Value("${server.port}")
    public String port;
```

3.PS：修改数据源后，配置文件会及时更新，但数据库连接不会变，因为数据库连接只在项目启动时加载一次。

## 多配置文件

业务场景：服务A和服务B的配置文件中有一部分是一样的，我想提取到一个公共的配置文件中，那么服务A和B就要同时读取两个以上配置文件

解决方案1：

​			spring.cloud.nacos.config.ext-config

```yaml
server:
  port: 8081
spring:
  application:
    name: provider8081 # 微服务名称，也是配置中心里的Data id
  cloud:
    nacos:
      config:
        server-addr: host:8848 # IP地址
        file-extension: yaml # 配置文件类型
        namespace: test # 命名空间-环境
        group: STORE_GROUP # 组-项目
        ext-config:
          - data-id: test.yaml # dataId
            group: STORE_GROUP # 组
            refresh: true # 是否动态刷新
          - data-id: test02.yaml # dataId
            group: STORE_GROUP # 组
            refresh: true # 是否动态刷新
```

# Nacos注册中心

## 主流服务中心与配置中心对比

![image-20210701132355620](https://quanjichao.oss-cn-beijing.aliyuncs.com/images/image-20210701132355620.png)

## 多实例负载均衡

- 微服务的多实例配置：服务名称、组、命名空间一致，访问地址不一样

- nacos默认负载均衡策略是轮询

- nacos控制面板--服务列表 可以配置多实例权重配置、服务下线等功能

- 客户端与服务端负载均衡

  ![image-20210705172919580](https://quanjichao.oss-cn-beijing.aliyuncs.com/images/image-20210705172919580.png)

feign底层调用ribbon实现负载均衡策略：

```java
@Configuration
@RibbonClient(name = "RibbonConfig",configuration = RibbonAutoConfiguration.class)
public class RibbonConfig {
    @Bean
    public IRule RibbonConfiguretion(){
        return new RandomRule();
    }
}
```

## nacos注册中心使用步骤

pom依赖

```xml
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
        </dependency>
```

配置文件

```yaml
server:
  port: 8081
spring:
  application:
    name: provider
  cloud:
    nacos:
      discovery:
        server-addr: 47.100.42.118:8848
        namespace: spring-cloud-alibaba
        group: TEST_GROUP
```

nacos服务器过载解决办法：

```
删除nacos文件夹下的data文件夹，并重启 即可解决问题
```

# Feign

## 什么是feign

![image-20210702142630385](https://quanjichao.oss-cn-beijing.aliyuncs.com/images/image-20210702142630385.png)

## Feign解决了什么问题

![image-20210702142936949](https://quanjichao.oss-cn-beijing.aliyuncs.com/images/image-20210702142936949.png)

## Feign与openFeign的区别

![image-20210702143345593](https://quanjichao.oss-cn-beijing.aliyuncs.com/images/image-20210702143345593.png)

feign是由Netflix开发出来的另外一种实现负载均衡的开源框架，openfeign是springcloud基于feign开发的拓展

## Feign的使用步骤

![image-20210702144406285](https://quanjichao.oss-cn-beijing.aliyuncs.com/images/image-20210702144406285.png)

pom文件：

```xml
		<!--nacos-openfeign-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-openfeign</artifactId>
        </dependency>
        <!--nacos-discovery-->
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
        </dependency>
```

业务层接口：

```java
import entity.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@FeignClient("provider8081")
public interface OrderService {
    @GetMapping("/user/get-users")
    public List<UserDTO> getUsers();
}
```

启动类：

```java
@SpringBootApplication
@EnableFeignClients
public class Application_80 {
    public static void main(String[] args) {
        SpringApplication.run(Application_80.class, args);
    }
}
```

bootstrap.yaml

```yaml
server:
  port: 80
spring:
  application:
    name: consumer80 # 微服务名称，也是配置中心里的Data id
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848
```

# Nacos高可用集群

机器跑不动，先搁置😓以后再更新

# GateWay

![image-20210706130441151](https://quanjichao.oss-cn-beijing.aliyuncs.com/images/image-20210706130441151.png)

网关主要作用：

![image-20210706132500649](https://quanjichao.oss-cn-beijing.aliyuncs.com/images/image-20210706132500649.png)

## 使用步骤

1.引入依赖（引入gateway后不需要再引入web模块，会造成冲突）

```xml
<dependencies>
        <!--nacos-discovery-->
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-gateway</artifactId>
        </dependency>
</dependencies>
```

2.修改配置文件

```yaml
server:
  port: 80
uri:
  demo: lb://consumer70
spring:
  application:
    name: gateway
  cloud:
    gateway:
      discovery:
        locator:
          enabled: true # 开启网关自动映射
          lowerCaseServiceId: true # 开启服务名称小写转换
      routes:
        - id: api             # id唯一
          uri: ${uri.demo}    # 本地负载均衡策略
          predicates:         # 断言（判断条件）
            - Path=/api01/wdw/**
          filters:            # 配置过滤器集合
            - StripPrefix=2
        - id: api02
          uri: ${uri.demo}
          predicates:
            - Path=/api02/wdw/**
          filters:
            - StripPrefix=2					
```



自定义路由规则中：配置filters: StripPrefix的=2  与 lb 一起使用的作用

http://api01/test/**    转发到    http://api01/test/consumer70/**  ,再通过过滤器规则，最终的uri是http://consumer70/**

**代表具体的api接口，例如/user/get-user?userid=123456

## 路由规则

### PAth

```yaml
routes:
	- id: 163
       uri: http://163.com
       predicates:
       	- Path=/163/**    # 将匹配到的请求追加到目标URL后
```

### Query

```yaml
- id: 164
  uri: http://www.163.com
  predicates:
  	# - Query=token       # 包含参数tokenen
  	- Query=token,abc.    # 包含参数token，且满足正则表达式"abc."
```

### Method

```yaml
- id: 165
          uri: http://www.163.com
          predicates:
            - Method=GET   # 只匹配get请求
```

### Datetime

```yaml
- id: 165
          uri: http://www.163.com
          predicates:
              # 匹配中国上海时间 2021-07-22 11:20:20之后的请求
            - After=2021-07-22T11:20:20.000+08:00[Asia/Shanghai]
```

### RemoteAddr

```yaml
- id: 165
          uri: http://www.163.com
          predicates:
            - RemoteAddr=192.168.0.252 # 匹配远程请求地址是RemoteAddr的请求
```

### Header

```yaml
- id: 165
          uri: http://www.163.com
          predicates:
            # 匹配请求头包含X-Request-Id 并且其值匹配正则表达式 "\d+" 的请求
            - Header=X-Request-Id, \d+
```

## 网关过滤器

参考官方文档

https://docs.spring.io/spring-cloud-gateway/docs/3.0.3/reference/html/#gatewayfilter-factories

### 自定义全局过滤器

![image-20210722153054071](https://quanjichao.oss-cn-beijing.aliyuncs.com/images/image-20210722153054071.png)

使用步骤如下：

自定义全局过滤器需要实现以下两个接口：GlobalFilter, Ordered。通过全局过滤器可以实现权限校验，安全性验证等功能

实现指定接口，添加@Component注解

```java
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import org.springframework.cloud.gateway.filter.GlobalFilter;
@Component
public class SpringCloudGlobalFilter implements GlobalFilter, Ordered {
    /**过滤业务逻辑*/
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        System.out.println("自定义全局过滤器执行");
        return chain.filter(exchange);
    }
    /**过滤器执行顺序，数值越小，优先级越高*/
    public int getOrder() {
        return 0;
    }
}
```

### 全局过滤器统一鉴权

```java
@Component
public class SpringCloudGlobalFilter implements GlobalFilter, Ordered {
    /**过滤业务逻辑*/
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //获取请求参数
        String token = exchange.getRequest().getQueryParams().getFirst("token");
        if (token == null){
            //获取响应对象
            ServerHttpResponse response = exchange.getResponse();
            //响应类型
            response.getHeaders().add("Content-Type","application/json; charset=utf-8");
            //响应状态码，HTTP 401 错误代表用户没有访问权限
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            //响应内容
            String message = "{\"message\":\""+HttpStatus.UNAUTHORIZED.getReasonPhrase()+"\"}";
            DataBuffer buffer = response.bufferFactory().wrap(message.getBytes());
            //请求结束，不再继续向下请求
            return response.writeWith(Mono.just(buffer));
        }
        return chain.filter(exchange);
    }
    /**过滤器执行顺序，数值越小，优先级越高*/
    public int getOrder() {
        return 0;
    }
}
```

## Sentinel

### 为什么要限流

Sentinel项目地址：https://github.com/alibaba/Sentinel/wiki

![image-20210722162647766](https://quanjichao.oss-cn-beijing.aliyuncs.com/images/image-20210722162647766.png)

### 令牌桶算法

![image-20210722172121556](https://quanjichao.oss-cn-beijing.aliyuncs.com/images/image-20210722172121556.png)

优点：

1.可以限制请求速度

2.可以处理突发大批量请求

### 一个Demo

创建springboot项目，引入pom依赖

```xml
<dependency>
    <groupId>com.alibaba.csp</groupId>
    <artifactId>sentinel-core</artifactId>
</dependency>
```

controller层写法

```java
@RestController
@RequestMapping("/user")
public class UserController {
    @RequestMapping("/get-user")
    public String getUSer(){
        try (Entry entry = SphU.entry("MyFlowRule")) {
            // do something here (your business logic)...
            return "老大爷辛苦了！！！"+System.currentTimeMillis();
        } catch (BlockException ex) {
            // Here to handle the rejection
            return "系统繁忙，老大爷请排队！";
        }
    }

    /**定义限流规则*/
    @PostConstruct  //构造方法执行完后，执行该方法 定义和加载规则集合
    public void initFlowRules(){
        System.out.println("我执行了哦");
        List<FlowRule> rules = new ArrayList<FlowRule>(); //定义限流规则集合
        FlowRule rule = new FlowRule(); // 定义限流规则
        rule.setResource("MyFlowRule"); //定义限流资源
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS); // 定义限流规则类型   qps:每秒查询率
        rule.setCount(2); // 定义qps阈值 每秒最多通过的请求个数
        rule.setLimitApp("default");
        rules.add(rule); // 添加规则到集合
        FlowRuleManager.loadRules(rules); //加载规则集合
    }

}
```

详细用法可参考官方演示demo：https://github.com/alibaba/Sentinel/wiki/How-to-Use

### SpringCloud整合sentinel

下载sentinel 项目jar包

https://github.com/alibaba/Sentinel/releases/tag/1.8.1

启动运行

```bash
java -Dserver.port=8080 -Dcsp.sentinel.dashboard.server=localhost:8080 -Dproject.name=sentinel-dashboard -jar sentinel-dashboard.jar
```

引入依赖

```xml
<dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency> 
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-sentinel</artifactId>
        </dependency>
    </dependencies>
```

定义资源（指定限流资源名称）

```java
@RequestMapping("/get-user")
public String getUSer(){
    try (Entry entry = SphU.entry("MyFlowRule")) {
        // do something here (your business logic)...
        return "老大爷辛苦了！！！"+System.currentTimeMillis();
    } catch (BlockException ex) {
        // Here to handle the rejection
        return "系统繁忙，老大爷请排队！";
    }
}
```

配置文件

```yaml
server:
  port: 60
spring:
  cloud:
    sentinel:
      transport:
        dashboard: localhost:8080 # 控制台的地址
```

访问 localhost:XX/user/get-user后，进入sentinel 控制台页面(localhost:8080    默认用户名密码 sentinel : sentinel)，可以看到项目已被检测到

 ![image-20210726160739279](https://quanjichao.oss-cn-beijing.aliyuncs.com/images/image-20210726160739279.png)

配置限流规则

![image-20210726161250939](https://quanjichao.oss-cn-beijing.aliyuncs.com/images/image-20210726161250939.png)

MyFlowRule是 controller中定义的资源名称

```java
SphU.entry("MyFlowRule")
```

### 定义资源方式

方式一

```java
@RequestMapping("/get-user")
    public String getUSer(){
        try (Entry entry = SphU.entry("MyFlowRule")) {
            // do something here (your business logic)...
            return "老大爷辛苦了！！！"+System.currentTimeMillis();
        } catch (BlockException ex) {
            // Here to handle the rejection
            return "系统繁忙，老大爷请排队！";
        }
    }
```

方式二

````java
@RequestMapping("/get-user")
    public String getda(){
        if (SphO.entry("MyFlowRule")){
            //务必保证finally会被执行
            try{
                return "大爷你好";
            }finally {
                SphO.exit();
            }
        }else {
            return "系统繁忙";
        }
    }
````

方式三 (注解方式，推荐使用)

```java
@RequestMapping("/get-user")
    @SentinelResource(value = "MyFlowRule",blockHandler = "Method02")
    public String getUSer(){
        return "老大爷好啊！活得好呀";
    }

    public String Method02(BlockException e){
        e.printStackTrace();
        return "系统繁忙，没空搭理你！";
    }
```

方式四（异步调用支持，仅作了解）

### 流量控制规则

![image-20210726171211810](https://quanjichao.oss-cn-beijing.aliyuncs.com/images/image-20210726171211810.png)

### sentinel整合nacos实现配置持久化

引入pom依赖

```xml
<dependency>
	<groupId>com.alibaba.csp</groupId>
	<artifactId>sentinel-datasource-nacos</artifactId>
</dependency>
```

nacos配置中心新建配置文件（格式：json）

![image-20210727133944426](https://quanjichao.oss-cn-beijing.aliyuncs.com/images/image-20210727133944426.png)

![image-20210727133811865](https://quanjichao.oss-cn-beijing.aliyuncs.com/images/image-20210727133811865.png)

修改配置文件

```yaml
server:
  port: 60
spring:
  cloud:
    sentinel:
      transport:
        dashboard: localhost:8080 # 控制台的地址
      datasource:
        ds:
          nacos:
            server-addr: localhost:8848  # nacos连接地址
            group-id: DEFAULT_GROUP  # nacos连接的分组
            rule-type: flow  # 流控规则  rule-type 配置表示该数据源中的规则属于哪种类型的规则(flow，degrade，authority，system, param-flow, gw-flow, gw-api-group)
            data-id: sentinel_config  # 读取配置文件的 data-id
            data-type: json #  读取培训文件类型为json
```

项目启动时会自动到 nacos 配置中心寻找配置文件，并上传到 sentinel 控制台
