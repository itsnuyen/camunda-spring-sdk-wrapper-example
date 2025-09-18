package com.camunda.demo.parent.authexample;

import io.grpc.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.UUID;

@Slf4j
@Component
public class GrpcClientInterceptor implements ClientInterceptor {
    private static final Metadata.Key<String> X_AUTH_HEADER =
            Metadata.Key.of("x-tracking-id", Metadata.ASCII_STRING_MARSHALLER);

    private static final Metadata.Key<String> X_AUTH_BASIC =
            Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER);
    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> methodDescriptor,
                                                               CallOptions callOptions, Channel channel) {
        return new ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(channel.newCall(methodDescriptor, callOptions)) {
            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                String uuid = UUID.randomUUID().toString();

                // Add custom headers to gRPC metadata
                headers.put(X_AUTH_HEADER, uuid);

                // You can add more headers as needed
                // headers.put(Metadata.Key.of("x-service-name", Metadata.ASCII_STRING_MARSHALLER),
                //            "your-service-name");

                final String base64EncodedCredentials =
                        Base64.getEncoder().encodeToString(String.format("%s:%s", "demo", "demo").getBytes());
                var authHeaderValue = String.format("Basic %s", base64EncodedCredentials);
                headers.put(X_AUTH_BASIC, authHeaderValue);

                log.debug("Added gRPC headers - x-auth: {} for method: {}",
                        uuid, methodDescriptor.getFullMethodName());

                super.start(responseListener, headers);
            }
        };
    }
}
