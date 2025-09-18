package com.camunda.demo.parent.authexample;

import org.apache.hc.client5.http.async.AsyncExecCallback;
import org.apache.hc.client5.http.async.AsyncExecChain;
import org.apache.hc.client5.http.async.AsyncExecChainHandler;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.HttpRequest;
import org.apache.hc.core5.http.nio.AsyncEntityProducer;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Base64;

@Component
public class HttpClientIntercepter implements AsyncExecChainHandler {
    @Override
    public void execute(HttpRequest httpRequest, AsyncEntityProducer asyncEntityProducer, AsyncExecChain.Scope scope, AsyncExecChain asyncExecChain, AsyncExecCallback asyncExecCallback) throws HttpException, IOException {
        final String base64EncodedCredentials =
                Base64.getEncoder().encodeToString(String.format("%s:%s", "demo", "demo").getBytes());
        var authHeaderValue = String.format("Basic %s", base64EncodedCredentials);
        httpRequest.addHeader("Authorization", authHeaderValue);
        httpRequest.addHeader("my-custom-header", java.util.UUID.randomUUID().toString());

        asyncExecChain.proceed(httpRequest, asyncEntityProducer, scope, asyncExecCallback);

    }

}
