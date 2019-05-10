package pl.niewiemmichal.underhiseye.web.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.collect.Lists;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage("pl.niewiemmichal.underhiseye.web.endpoints"))
                .paths(PathSelectors.regex("/.*")).build()
                .apiInfo(apiEndPointsInfo())
                .securitySchemes(Lists.newArrayList(securityScheme()))
                .securityContexts(Lists.newArrayList(securityContext()));
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(Lists.newArrayList(securityReference()))
                .forPaths(PathSelectors.any()).build();
    }

    private SecurityReference securityReference() {
        AuthorizationScope authorizationScope
                = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return new SecurityReference("basicAuth", authorizationScopes);
    }

    private SecurityScheme securityScheme() {
        return new BasicAuth("basicAuth");
    }

    private ApiInfo apiEndPointsInfo() {
        return new ApiInfoBuilder().title("Under His Eye - Your Clinic Software").build();
    }
}
