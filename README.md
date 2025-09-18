# Custom Worker and extends Camunda Client Functionality

## Purpose
Demonstration project showing how to wrap the Camunda Spring SDK client to add enterprise extensions (custom workers, interceptors, wrapper interfaces) for internal reuse and distribution.

## Structure
- `ParentApplication.java` Spring Boot entry point.
- `authexample/` Example custom config and client interceptors (gRPC / HTTP).
- `client/engine/` Wrapper interfaces for engine operations (`CustomEngineClient`, `CustomEngineProcessClient`).
- `client/operate/` Wrapper classes for Operate API access (`OperateClientV2`, `OperateCustomClient`).
- `client/worker/` Custom worker infrastructure (annotation processor config, lifecycle, handler interface, job context).
- `example/` Sample job handlers, REST controller, services, and a record (`MyCoolHandler`, `MySecondCoolHandler`, `NormalSampleWorker`, `SampleController`, `SampleServices`, `SampleRecord`).

## Custom Worker Lifecycle
`CustomJobworkerLifecycle` implements `CamundaClientLifecycleAware` and:
- On start: iterates registered `CustomJobHandlerInterface` implementations and registers job workers through the `JobWorkerManager`.
- On stop: closes all open workers.

Handlers implement `CustomJobHandlerInterface` and may still use standard Camunda worker annotations.

### Job Worker Customization

```java
public class CustomJobworkerLifecycle implements CamundaClientLifecycleAware {
    private final Set<CustomJobHandlerInterface> jobHandlers;
    private final JobWorkerManager jobWorkerManager;
```

We will implement the CamundaClientLifecycleAware interface to hook into the lifecycle of the client and will customize the job worker registration.


```java 
    @Override
    public void onStart(CamundaClient client) {
        log.debug("Starting Jobworker Lifecycle");
        jobHandlers.forEach(handler -> {
            log.info("Registering job handler: {}", handler.getClass().getName());
            registerJobWorker(handler, client);
        });
    }
    @Override
    public void onStop(CamundaClient client) {
        log.info("Stopping Custom Jobworker Lifecycle");
        jobWorkerManager.closeAllOpenWorkers();
    }
}

```

override the onStart and onStop methods to register and unregister the job workers.
the registerJobWorker method will create the job worker based on our custom interface and register it with the job worker manager.
inside these methods you can add additional logic as needed.

To use this custom worker, you will need to define beans for your custom job handlers and the job worker lifecycle in your Spring configuration.

```java
@Bean
public CustomJobworkerLifecycle customJobworkerLifecycle(Set<CustomJobHandlerInterface> jobHandlers,
                                                         JobWorkerManager jobWorkerManager) {
    return new CustomJobworkerLifecycle(jobHandlers, jobWorkerManager);
}
```

and define your custom job handlers implementing the CustomJobHandlerInterface.

```java
@Component
public class MyCustomJobHandler implements CustomJobHandlerInterface {
    @Override
    public String getType() {
        return "my-custom-job-type";
    }
    @Override
    public void handle(Mycustominterface job) {
        // Custom job handling logic
    }  
}
```

On top these kind of handlers still offer the possiblities to use the standard camunda job worker annotation.

### Custom Camunda Client Extension

to extend the camunda client functionality, we can define interfaces and implementations that wrap around the camunda client.

```java
public interface CustomCamundaClient {
    void deployProcess(String bpmnFilePath);
```

This interface can define custom methods for deploying processes, starting process instances, and searching for process instances.

```java
    String startProcessInstance(String processDefinitionKey, Map<String, Object> variables);
    List<ProcessInstance> searchProcessInstances(Map<String, Object> searchCriteria);
}
```

Then we can implement this interface in a class that uses the CamundaClient to perform these operations.
A new class will use the corresponding camunda client apis to implement the custom methods.

### Custom Interceptors

To implement custom interceptors, we can define a custom interceptor class that implements the `AsyncExecChainHandler` or `ClientInterceptor` interface.
In each one we can add custom logic to modify requests or responses.

An example will be found here [authexample](src/main/java/com/camunda/demo/parent/authexample)

## Topics to consider
- Add custom auth / metrics / tracing in interceptors.
- Enforce naming and variable conventions in wrapper layer.
- Expand search / operate abstractions.

## This project is an example how to write a custom wrapper, based on the camunda 8 client and worker.