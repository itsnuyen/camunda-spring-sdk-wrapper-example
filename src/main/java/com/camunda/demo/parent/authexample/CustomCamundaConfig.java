package com.camunda.demo.parent.authexample;

import io.camunda.client.CamundaClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/*
    This configuration class defines a custom CamundaClient bean with specific gRPC and HTTP interceptors.
    It ensures that this custom client is used throughout the application by marking it as the primary bean.
    Each intercept will add some custom headers to the requests.
*/
@Slf4j
@Configuration
public class CustomCamundaConfig {

    @Autowired
    private GrpcClientInterceptor grpcClientInterceptor;

    @Autowired
    private HttpClientIntercepter httpClientIntercepter;

    // This completely replaces the auto-configured bean
    @Bean("myCustomClient")
    @Primary
    public CamundaClient camundaClient() {
        log.info("Creating REPLACEMENT CamundaClient (auto-config excluded)");
        var client = CamundaClient.newClientBuilder()
                .withInterceptors(grpcClientInterceptor)
                .withChainHandlers(httpClientIntercepter);
        return client.build();
    }
}
