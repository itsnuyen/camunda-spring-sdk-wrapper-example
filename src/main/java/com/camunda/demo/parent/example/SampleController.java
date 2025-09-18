package com.camunda.demo.parent.example;

import io.camunda.client.api.search.response.ProcessInstance;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class SampleController {

    private SampleServices sampleServices;

    @GetMapping("/process-instances/{processKey}")
    public List<ProcessInstance> getProcessInstances(@PathVariable String processKey) {
        log.debug("Getting process instances");
        return sampleServices.getAllActiveProcessInstances(processKey);
    }

    @GetMapping("/process-instance/{processInstanceId}")
    public ProcessInstance getProcessInstance(@PathVariable Long processInstanceId) {
        log.debug("Getting process instance for id: {}", processInstanceId);
        return sampleServices.getProcessInstance(processInstanceId);
    }

    @PostMapping("/start-process/{processKey}")
    public void startProcessInstance(String processKey, SampleRecord sample) {
        log.debug("Starting process instance for processKey: {} with businessKey: {}", processKey, sample);
        sampleServices.startProcessInstance(processKey, sample);
    }
}
