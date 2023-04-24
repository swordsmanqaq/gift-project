package com.heng.config;/**
 * @author shkstart
 * @create 2023-04-24 13:39
 */

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 *@Auther:Jarvis
 *@Date:2023年04月2023/4/24日13:39
 *@Description:
 */
@Configuration
@EnableSwagger2
public class Knife4jConfig {
    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("系统中心接口文档")
                .description("系统中心接口文档，仅限内部使用")
                .termsOfServiceUrl("http://www.heng.cn")
                .contact(new Contact("jarvis", "www.heng.cn", "swords_man12@163.com"))
                .version("1.0")
                .build();
    }

    @Bean
    public Docket defaultApi2() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                //分组名称
                //.groupName("系统管理接口")
                .select()
                //指定controller（接口）扫描的包路径
                .apis(RequestHandlerSelectors.basePackage("com.heng.controller"))
                .paths(PathSelectors.any())
                .build();
    }

}
