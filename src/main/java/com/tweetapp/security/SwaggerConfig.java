package com.tweetapp.security;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.google.common.collect.Lists;


import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.paths.RelativePathProvider;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private static final String AUTHORIZATION_HEADER = "Authorization";
	private static final String DEFAULT_INCLUDE_PATTERN = "/.*";

	@Bean
	public Docket api(Environment env, ServletContext servletContext) {
		String appBasePath = env.getProperty("server.servlet.context-path");
		this.logger.info(" Path {}", appBasePath);

		return new Docket(DocumentationType.SWAGGER_2).pathProvider(new RelativePathProvider(servletContext) {
			@Override
			public String getApplicationBasePath() {
				return appBasePath;
			}
		}).select().apis(RequestHandlerSelectors.basePackage("com.tweetapp.controller"))
				.paths(PathSelectors.any()).build().apiInfo(this.apiInfo())
				.securityContexts(Lists.newArrayList(this.securityContext()))
				.securitySchemes(Lists.newArrayList(this.apiKey()));

	}

	private ApiInfo apiInfo() {
		return new ApiInfo("Tweet API", "REST API to be consumed by UI for Tweet App project.",
				"Tweet API Documentation", "Terms of service", new Contact("Cognizant", "www.cts.com", ""), "",
				"", Collections.emptyList());
	}

	private ApiKey apiKey() {
		return new ApiKey("JWT", AUTHORIZATION_HEADER, "header");
	}

	private SecurityContext securityContext() {
		return SecurityContext.builder().securityReferences(this.defaultAuth())
				.forPaths(PathSelectors.regex(DEFAULT_INCLUDE_PATTERN)).build();
	}

	private List<SecurityReference> defaultAuth() {
		AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
		AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
		authorizationScopes[0] = authorizationScope;
		return Lists.newArrayList(new SecurityReference("JWT", authorizationScopes));
	}
}
