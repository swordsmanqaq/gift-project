package com.heng.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

@Component
@Primary
public class SwaggerResourceConfig implements SwaggerResourcesProvider {

    @Autowired
    private RouteLocator routeLocator;

    private SwaggerResource swaggerResource(String name, String location) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        //swaggerResource.setLocation(location+"?group=系统管理接口");
        swaggerResource.setLocation(location);
        //这个配置影响不大
        swaggerResource.setSwaggerVersion("3.0");
        return swaggerResource;
    }

    @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> resourceList = new ArrayList<>();
        //获取所有router
        List<Route> routeList = routeLocator.getRoutes();
        for (Route route:routeList) {
            //排除不需要参与文档聚合的服务
            if (!route.getId().equals("monitor-server")){
                //第一个参数指定服务名，id是服务名 eg. order-server
                //第二个参数指定该服务的文档访问接口 eg. /order-server/v2/api-docs。fullPath是该服务的路由地址 eg. /order-server/**
                resourceList.add(swaggerResource(route.getId(), route.getFullPath().replace("/**", "/v2/api-docs")));
            }
        }
        return resourceList;
    }

}