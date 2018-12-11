package com.wxy.bigdata.interfaceApi;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Created with IntelliJ IDEA.
 * User: WangYN
 * Date: 2018/7/14
 * Time: 11:10
 * To change this template use File | Settings | File Templates.
 * Description: 访问路径 http://localhost:9090/swagger-ui.html
 */
@Configuration
public class SwaggerConfiguration {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.wxy.bigdata.pc"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("springboot利用swagger构建api文档-wxy")
                .description("王小一教你简单优雅的使用restfun风格")
                .termsOfServiceUrl("")
                .version("1.0")
                .build();
    }

}