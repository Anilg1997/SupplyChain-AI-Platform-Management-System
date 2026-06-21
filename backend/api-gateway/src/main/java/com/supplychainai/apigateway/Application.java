package com.supplychainai.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public RouteLocator customRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth-service", r -> r.path("/api/v1/auth/**")
                        .filters(f -> f.stripPrefix(0))
                        .uri("http://auth-service:8081"))
                .route("user-service", r -> r.path("/api/v1/users/**")
                        .filters(f -> f.stripPrefix(0))
                        .uri("http://user-service:8082"))
                .route("supplier-service", r -> r.path("/api/v1/suppliers/**")
                        .filters(f -> f.stripPrefix(0))
                        .uri("http://supplier-service:8083"))
                .route("pr-service", r -> r.path("/api/v1/pr/**")
                        .filters(f -> f.stripPrefix(0))
                        .uri("http://purchase-requisition-service:8084"))
                .route("po-service", r -> r.path("/api/v1/po/**")
                        .filters(f -> f.stripPrefix(0))
                        .uri("http://purchase-order-service:8085"))
                .route("rfq-service", r -> r.path("/api/v1/rfq/**")
                        .filters(f -> f.stripPrefix(0))
                        .uri("http://rfq-service:8086"))
                .route("contract-service", r -> r.path("/api/v1/contracts/**")
                        .filters(f -> f.stripPrefix(0))
                        .uri("http://contract-service:8087"))
                .route("product-catalog", r -> r.path("/api/v1/products/**")
                        .filters(f -> f.stripPrefix(0))
                        .uri("http://product-catalog-service:8088"))
                .route("inventory-service", r -> r.path("/api/v1/inventory/**")
                        .filters(f -> f.stripPrefix(0))
                        .uri("http://inventory-service:8089"))
                .route("warehouse-service", r -> r.path("/api/v1/warehouse/**")
                        .filters(f -> f.stripPrefix(0))
                        .uri("http://warehouse-service:8900"))
                .route("order-service", r -> r.path("/api/v1/orders/**")
                        .filters(f -> f.stripPrefix(0))
                        .uri("http://order-service:8091"))
                .route("return-service", r -> r.path("/api/v1/returns/**")
                        .filters(f -> f.stripPrefix(0))
                        .uri("http://return-service:8092"))
                .route("shipment-service", r -> r.path("/api/v1/shipments/**")
                        .filters(f -> f.stripPrefix(0))
                        .uri("http://shipment-service:8093"))
                .route("route-service", r -> r.path("/api/v1/routes/**")
                        .filters(f -> f.stripPrefix(0))
                        .uri("http://route-service:8094"))
                .route("tracking-service", r -> r.path("/api/v1/tracking/**")
                        .filters(f -> f.stripPrefix(0))
                        .uri("http://tracking-service:8095"))
                .route("quality-service", r -> r.path("/api/v1/quality/**")
                        .filters(f -> f.stripPrefix(0))
                        .uri("http://quality-service:8096"))
                .route("quarantine-service", r -> r.path("/api/v1/quarantine/**")
                        .filters(f -> f.stripPrefix(0))
                        .uri("http://quarantine-service:8097"))
                .route("forecast-service", r -> r.path("/api/v1/forecast/**")
                        .filters(f -> f.stripPrefix(0))
                        .uri("http://forecast-service:8098"))
                .route("planning-service", r -> r.path("/api/v1/planning/**")
                        .filters(f -> f.stripPrefix(0))
                        .uri("http://planning-service:8099"))
                .route("invoice-service", r -> r.path("/api/v1/invoices/**")
                        .filters(f -> f.stripPrefix(0))
                        .uri("http://invoice-service:8100"))
                .route("payment-service", r -> r.path("/api/v1/payments/**")
                        .filters(f -> f.stripPrefix(0))
                        .uri("http://payment-service:8101"))
                .route("cost-service", r -> r.path("/api/v1/costs/**")
                        .filters(f -> f.stripPrefix(0))
                        .uri("http://cost-service:8102"))
                .route("supplier-portal", r -> r.path("/api/v1/supplier-portal/**")
                        .filters(f -> f.stripPrefix(0))
                        .uri("http://supplier-portal-service:8103"))
                .route("report-service", r -> r.path("/api/v1/reports/**")
                        .filters(f -> f.stripPrefix(0))
                        .uri("http://report-service:8104"))
                .route("analytics-service", r -> r.path("/api/v1/analytics/**")
                        .filters(f -> f.stripPrefix(0))
                        .uri("http://analytics-service:8105"))
                .route("notification-service", r -> r.path("/api/v1/notifications/**")
                        .filters(f -> f.stripPrefix(0))
                        .uri("http://notification-service:8106"))
                .route("search-service", r -> r.path("/api/v1/search/**")
                        .filters(f -> f.stripPrefix(0))
                        .uri("http://search-service:8108"))
                .build();
    }

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://localhost:4200");
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsWebFilter(source);
    }
}
