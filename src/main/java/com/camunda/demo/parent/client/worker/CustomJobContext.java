package com.camunda.demo.parent.client.worker;

import io.camunda.client.api.response.ActivatedJob;
import io.camunda.client.api.worker.JobClient;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CustomJobContext {
    private Map<String, Object> contextInfo;
    private ActivatedJob activatedJob;
    private JobClient jobClient;

    public ActivatedJob getActivatedJob() {
        return activatedJob;
    }

    public void setActivatedJob(ActivatedJob activatedJob) {
        this.activatedJob = activatedJob;
    }

    public JobClient getJobClient() {
        return jobClient;
    }

    public void setJobClient(JobClient jobClient) {
        this.jobClient = jobClient;
    }

    public void setContextInfo(Map<String, Object> contextInfo) {
        this.contextInfo = contextInfo;
    }

    public Map<String, Object> getContextInfo() {
        return contextInfo;
    }
}
