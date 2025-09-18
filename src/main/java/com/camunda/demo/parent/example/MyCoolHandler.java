package com.camunda.demo.parent.example;

import com.camunda.demo.parent.client.worker.CustomJobContext;
import com.camunda.demo.parent.client.worker.CustomJobHandlerInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MyCoolHandler implements CustomJobHandlerInterface {

    @Override
    public void handleJob(CustomJobContext context) {
        log.info("Handling job with context info: {}", context.getActivatedJob().toString());
        var variables = context.getActivatedJob().getVariablesAsMap();
        variables.put("jobworkerVariable", "somecustomValue" + System.currentTimeMillis());
        context.getJobClient()
                .newCompleteCommand(context.getActivatedJob())
                .variables(variables)
                .send()
                .join();
    }

    @Override
    public String getType() {
        return "myWorker";
    }

    @Override
    public int getMaxJobsActive() {
        return CustomJobHandlerInterface.super.getMaxJobsActive();
    }
}
