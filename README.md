# gift-project

## master分支：
### 使用的是SpringBoot Cloud netflix微服务架构进行开发，其中包含一下组件：
Eureka	    注册中心，进行服务注册、服务发现、服务续约；
Ribbon	    负载均衡，多服务时做负载；
OpenFeign	  服务间内部调用；
Hystrix	    熔断器，服务熔断、降级；
Zuul        网关，统一的访问入口；

### 
SpringBoot Cloud netflix这套架构只是搭建了简单几个服务中心，系统服务中心，授权服务中心，前端服务中心、通过将一些公共都会依赖的基础模块抽出来弄成一个服务等。
