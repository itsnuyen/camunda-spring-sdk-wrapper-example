package com.camunda.demo.parent.client.operate;

import io.camunda.client.api.search.response.ProcessInstance;

import java.util.List;

public interface OperateCustomClient {
    List<ProcessInstance> getActiveProcessInstanceWithSearchQuery(String processKey);
    ProcessInstance getProcessInstance(Long processInstanceId);
}
