package com.example.ayusidehiddengemsplaylistback.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.List;

@Configuration
public class SwaggerConfig {
    private static final String REFERENCE = "Bearer 헤더 값";

    @Bean
    public Docket api(){
//        return new Docket(DocumentationType.SWAGGER_2)
        return new Docket(DocumentationType.OAS_30) //OpenAPI 3.0
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .securityContexts(List.of(securityContext()))
                .securitySchemes(List.of(bearerAuthSecurityScheme()));
//                .securitySchemes(List.of(securityScheme()))
//                .securitySchemes(Arrays.asList(apiKey(), anotherApiKey()));

    }


//    // accessToken 입력 칸
//    private ApiKey apiKey() {
//        return new ApiKey("Authorization", "Bearer", "header");
//    }
//
//    // refreshToken 입력 칸
//    private ApiKey anotherApiKey(){
//        return new ApiKey(REAUTHORIZATION_HEADER, REAUTHORIZATION_HEADER, "header");
//    }
//
//
    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(securityReferences())
                .operationSelector(operationContext -> true)
//                .securityReferences(securityReferences()).forPaths(PathSelectors.any())
                .build();
    }

    private List<SecurityReference> securityReferences() {
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = new AuthorizationScope("global", "accessEverything");
        return List.of(new SecurityReference(REFERENCE, authorizationScopes));
    }


    private HttpAuthenticationScheme bearerAuthSecurityScheme() {
        return HttpAuthenticationScheme.JWT_BEARER_BUILDER
                .name(REFERENCE)
                .build();
    }

}