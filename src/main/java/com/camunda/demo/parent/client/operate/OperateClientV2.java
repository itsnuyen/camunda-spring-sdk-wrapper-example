package com.camunda.demo.parent.client.operate;

import io.camunda.client.CamundaClient;
import io.camunda.client.api.search.enums.ProcessInstanceState;
import io.camunda.client.api.search.response.ProcessInstance;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * v2 https://docs.camunda.io/docs/next/apis-tools/orchestration-cluster-api-rest/specifications/search-process-instances/
 * This client uses the Camunda Client to interact with Camunda Operate's process instance search API.
 * It provides methods to retrieve active process instances based on a search query and to get details of
 * a specific process instance by its ID.
 * Is use as an example of how to abstract the Camunda Client functionality into a custom client for easier use in services.
 *
 */
@Service
@AllArgsConstructor
public class OperateClientV2 implements OperateCustomClient {

    CamundaClient camundaClient;

    // TODO adding filter parameter, now its static
    @Override
    public List<ProcessInstance> getActiveProcessInstanceWithSearchQuery(String processKey) {
        // search filter possible with values: https://github.com/camunda/camunda/blob/d7fda10921f95ad194b688d32b613557f3122049/clients/java/src/main/java/io/camunda/client/api/search/filter/ProcessInstanceFilter.java#L31
        var processInstances = camundaClient.newProcessInstanceSearchRequest()
                .filter(fn -> fn.state(ProcessInstanceState.ACTIVE)
                        .processDefinitionId(processKey))
                .sort(processInstanceSort -> processInstanceSort.startDate())
                .send()
                .join()
                .items();
        return processInstances;
    }

    @Override
    public ProcessInstance getProcessInstance(Long processInstanceId) {
        return camundaClient.newProcessInstanceGetRequest(processInstanceId)
                .send()
                .join();
    }
}
