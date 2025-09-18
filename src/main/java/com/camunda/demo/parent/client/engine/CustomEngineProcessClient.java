package com.camunda.demo.parent.client.engine;


import io.camunda.client.CamundaClient;
import io.camunda.client.api.response.ProcessInstanceEvent;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CustomEngineProcessClient implements CustomEngineClient {
    private CamundaClient camundaClient;

    @Override
    public void deployProcess(byte[] bpmnXml, String processFileName) {
        camundaClient.newDeployResourceCommand()
                .addResourceBytes(bpmnXml, processFileName + ".bpmn")
                .send()
                .join();
    }

    @Override
    public ProcessInstanceEvent startProcessInstance(String processId, Map<String, Object> variables) {
        return camundaClient.newCreateInstanceCommand()
                .bpmnProcessId(processId)
                .latestVersion() // or .version("0.0.1") for a specific version
                .variables(variables)
                .send()
                .join();
    }
}
