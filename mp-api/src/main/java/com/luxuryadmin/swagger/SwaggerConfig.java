package com.luxuryadmin.swagger;

import com.luxuryadmin.common.constant.ConstantCommon;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author monkey king
 * @date 2019-12-12 14:13:06
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
        //开发环境和测试环境
        boolean isShow = ConstantCommon.DEV.equals(ConstantCommon.springProfilesActive) || ConstantCommon.TEST.equals(ConstantCommon.springProfilesActive);
        if (isShow) {
            return new Docket(DocumentationType.SWAGGER_2)
                    .apiInfo(apiInfo())
                    .select()
                    .apis(RequestHandlerSelectors.basePackage("com.luxuryadmin"))
                    //.apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                    .paths(PathSelectors.any())
                    .build();
        } else {
            //非开发环境,不开放接口文档
            return new Docket(DocumentationType.SWAGGER_2)
                    .select()
                    .paths(PathSelectors.none())
                    .build();
        }
    }

    private ApiInfo apiInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("APP初始化参数,请求此接口,判断接下来的请求是否需要加密或者校验;<br/>");
        sb.append("APP初始化参数接口: /sys/getAppSysConfig<br/>");
        sb.append("该接口返回数据为加密;请解密data值后;再进行取值判断;<br/>");
        sb.append("<h1>如需解密校验,则为以下步骤:</h1><br/>");
        sb.append("1.校验sign值是否通过;<br/>");
        sb.append("2.sign值通过,data值解密后,再进业务逻辑参数填充;<br/>");
        sb.append("3.sign值通过后,取data值进行解密;<br/>");
        sb.append("================<br/>");
        sb.append("当返回的code是\"ok\"或者是\"ok_no_data\"这两种状态时,流程是走通且正常的.<br/>");
        sb.append("123456：e10adc3949ba59abbe56e057f20f883e<br/>");
        sb.append("000000：670b14728ad9902aecba32e22fa4f6bd");



        return new ApiInfoBuilder()
                .title("=====接口说明文档(必读)全局说明=====")
                .description(sb.toString())
                .termsOfServiceUrl("http://www.baidu.com")
                .version("2.0")
                .build();
    }

}

