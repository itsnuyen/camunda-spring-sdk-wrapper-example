package com.camunda.demo.parent.client.worker;

import io.camunda.client.CamundaClient;
import io.camunda.client.api.worker.JobHandler;
import io.camunda.spring.client.annotation.processor.CamundaClientLifecycleAware;
import io.camunda.spring.client.annotation.value.JobWorkerValue;
import io.camunda.spring.client.jobhandling.JobWorkerManager;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@Slf4j
public class CustomJobworkerLifecycle implements CamundaClientLifecycleAware {
    private final Set<CustomJobHandlerInterface> jobHandlers;
    private final JobWorkerManager jobWorkerManager;

    public CustomJobworkerLifecycle(Set<CustomJobHandlerInterface> jobHandlers, JobWorkerManager jobWorkerManager) {
        this.jobHandlers = jobHandlers;
        this.jobWorkerManager = jobWorkerManager;
    }

    @Override
    public void onStart(CamundaClient client) {
        log.debug("Starting Jobworker Lifecycle");
        jobHandlers.forEach(handler -> {
            log.info("Registering job handler: {}", handler.getClass().getName());
            registerJobWorker(handler, client);
        });
    }


    private void registerJobWorker(CustomJobHandlerInterface handler, CamundaClient camundaClient) {
        JobWorkerValue myWorker = createWorkerValue(handler);
        JobHandler jobHandler = createJobHandler(handler);

        log.info("Opening worker '{}' for type '{}'", myWorker.getName(), myWorker.getType());
        jobWorkerManager.openWorker(camundaClient, myWorker, jobHandler);
    }

    private JobWorkerValue createWorkerValue(CustomJobHandlerInterface handler) {
        JobWorkerValue value = new JobWorkerValue();
        value.setName("Custom Test Client");
        value.setType(handler.getType());
        value.setMaxJobsActive(handler.getMaxJobsActive());
        return value;
    }

    private JobHandler createJobHandler(CustomJobHandlerInterface handler) {
        return (jobClient, activatedJob) -> {
            log.info("Executing job handler for type={} key={}", activatedJob.getType(), activatedJob.getKey());
            try {
                CustomJobContext ctx = new CustomJobContext();
                ctx.setActivatedJob(activatedJob);
                ctx.setJobClient(jobClient);

                // add context metadata
                var variables = activatedJob.getVariablesAsMap();
                variables.put("contextInfo", "extra context from create Job handler");
                ctx.setContextInfo(variables);

                handler.handleJob(ctx);

                log.info("Job {} executed successfully", activatedJob.getKey());
            } catch (Exception e) {
                log.error("Error while handling job {}", activatedJob.getKey(), e);
                throw e;
            }
        };
    }

    @Override
    public void onStop(CamundaClient client) {
        log.info("Stopping Custom Jobworker Lifecycle");
        jobWorkerManager.closeAllOpenWorkers();
    }
}
