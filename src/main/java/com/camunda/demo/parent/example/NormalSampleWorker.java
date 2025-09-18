package com.camunda.demo.parent.example;

import io.camunda.client.api.response.ActivatedJob;
import io.camunda.client.api.worker.JobClient;
import io.camunda.spring.client.annotation.JobWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NormalSampleWorker {

    @JobWorker(type = "fooWorker", autoComplete = false)
    public void handleFoo2(JobClient jobClient, ActivatedJob activatedJob) {
        log.info("Handling foo2 task {}", activatedJob.toString());
        var jobVars = activatedJob.getVariablesAsMap();
        jobVars.put("results", "resultsOfMyService");
        jobClient.newCompleteCommand(activatedJob) // Manual completion
                .variables(jobVars)
                .send()
                .join();
    }
}
