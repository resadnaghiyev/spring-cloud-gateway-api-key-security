package com.rashad.cloudgateway;

import com.rashad.cloudgateway.config.RedisHashComponent;
import com.rashad.cloudgateway.dto.ApiKey;
import com.rashad.cloudgateway.util.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
public class CloudGatewayApplication {

    @Autowired
    private RedisHashComponent redisHashComponent;

    @PostConstruct
    public void initKeysToRedis() {
        List<ApiKey> apiKeys = new ArrayList<>();
        apiKeys.add(new ApiKey("343C-ED0B-4137-B27E", Stream.of(AppConstants.STUDENT_SERVICE_KEY,
                AppConstants.COURSE_SERVICE_KEY).collect(Collectors.toList())));
        apiKeys.add(new ApiKey("FA48-EF0C-427E-8CCF", Stream.of(AppConstants.COURSE_SERVICE_KEY)
                .collect(Collectors.toList())));
        List<Object> lists = redisHashComponent.hValues(AppConstants.RECORD_KEY);
        if (lists.isEmpty()) {
            apiKeys.forEach(k -> redisHashComponent.hSet(AppConstants.RECORD_KEY, k.getKey(), k));
        }
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(AppConstants.STUDENT_SERVICE_KEY,
                        r -> r.path("/api/student-service/**")
                                .filters(f -> f.stripPrefix(2)).uri("http://localhost:8081"))
                .route(AppConstants.COURSE_SERVICE_KEY,
                        r -> r.path("/api/course-service/**")
                                .filters(f -> f.stripPrefix(2)).uri("http://localhost:8082"))
                .route(AppConstants.COURSE_SERVICE_KEY,
                        r -> r.path("/api/new/secure/**")
                                .filters(f -> f.stripPrefix(3)).uri("http://localhost:8083"))
                .build();
    }

    public static void main(String[] args) {
        SpringApplication.run(CloudGatewayApplication.class, args);
    }
}