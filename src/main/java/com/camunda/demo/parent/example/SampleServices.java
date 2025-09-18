package com.camunda.demo.parent.example;

import com.camunda.demo.parent.client.engine.CustomEngineProcessClient;
import com.camunda.demo.parent.client.operate.OperateClientV2;
import io.camunda.client.api.response.ProcessInstanceEvent;
import io.camunda.client.api.search.response.ProcessInstance;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor
public class SampleServices {
    private OperateClientV2 operateClient;
    private CustomEngineProcessClient customEngineProcessClient;

    public List<ProcessInstance> getAllActiveProcessInstances(String processKey) {
        return operateClient.getActiveProcessInstanceWithSearchQuery(processKey);
    }

    public ProcessInstance getProcessInstance(Long processInstanceId) {
        return operateClient.getProcessInstance(processInstanceId);
    }

    public void startProcessInstance(String processId, SampleRecord sample) {
        log.info("Starting process instance for processId: {} with businessKey: {}", processId, sample);
        Map<String, Object> variables = (Map<String, Object>) new HashMap<>().put("sample", sample);
        ProcessInstanceEvent processInstanceEvent = customEngineProcessClient.startProcessInstance(processId, variables);
        log.info("Started process instance with id: {}", processInstanceEvent.getProcessInstanceKey());
    }
}
