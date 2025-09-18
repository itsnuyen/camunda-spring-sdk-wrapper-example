package com.camunda.demo.parent.client.engine;

import io.camunda.client.api.response.ProcessInstanceEvent;

import java.util.Map;

/**
 * Custom Engine Client interface for deploying processes and starting process instances.
 * Abstracts the interaction with the Camunda Engine.
 */

public interface CustomEngineClient {
    void deployProcess(byte[] bpmnXml, String processFileName);
    ProcessInstanceEvent startProcessInstance(String processId, Map<String, Object> variables);
}
