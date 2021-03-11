package com.kevinbrennan.grpc;
import io.grpc.*;

public class Client
{
    public static void main( String[] args ) throws Exception
    {
        // Channel is the abstraction to connect to a service endpoint
        // Let's use plaintext communication because we don't have certs
        final ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:2410")
                .usePlaintext()
                .build();

        // It is up to the client to determine whether to block the call
        // Here we create a blocking stub, but an async stub,
        // or an async stub with Future are always possible.
        ComputeServiceGrpc.ComputeServiceBlockingStub stub = ComputeServiceGrpc.newBlockingStub(channel);
        Compute.NameRequest request =
                Compute.NameRequest.newBuilder()
                        .setName("testingV")
                        .build();

        // Finally, make the call using the stub
        Compute.NameResponse response =
                stub.validateName(request);

        System.out.println(response);

        // A Channel should be shutdown before stopping the process.
        channel.shutdownNow();
    }
}