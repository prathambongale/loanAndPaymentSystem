package com.bancx.lnpsystem.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI loanAndPaymentSystemManagementOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                    .title("Loan and Payment System")
                    .description("The system should expose REST APIs for both domains Loan and Payment domain")
                    .version("v1.0")
                    .contact(new Contact().name("BancX").email("info@bancx.com").url("https://www.bancx.com/")))
                .servers(List.of(
                    new Server().url("http://localhost:8080").description("Local Development")
                ));
    }
}
