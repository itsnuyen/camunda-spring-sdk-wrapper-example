package com.camunda.demo.parent.client.worker;

public interface CustomJobHandlerInterface {
    void handleJob(CustomJobContext context);

    String getType();

    default int getMaxJobsActive() {
        return 10;
    }

}