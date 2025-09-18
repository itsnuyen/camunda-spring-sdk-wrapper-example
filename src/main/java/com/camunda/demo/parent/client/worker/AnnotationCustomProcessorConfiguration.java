package com.camunda.demo.parent.client.worker;

import io.camunda.spring.client.jobhandling.JobWorkerManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
public class AnnotationCustomProcessorConfiguration {

    @Bean
    public CustomJobworkerLifecycle defaultCustomLifeCylce(final Set<CustomJobHandlerInterface> jobHandlers, JobWorkerManager jobWorkerManager) {
        return new CustomJobworkerLifecycle(jobHandlers, jobWorkerManager);
    }
}
