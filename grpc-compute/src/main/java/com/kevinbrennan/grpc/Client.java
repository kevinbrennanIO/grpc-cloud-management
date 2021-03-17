package com.kevinbrennan.grpc;

import io.grpc.*;
import io.grpc.stub.StreamObserver;

public class Client {
    public static void main(String[] args) throws Exception {
        // Channel is the abstraction to connect to a service endpoint
        // Let's use plaintext communication because we don't have certs
        final ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:2410")
                .usePlaintext()
                .build();

        // It is up to the client to determine whether to block the call
        // Here we create a blocking stub, but an async stub,
        // or an async stub with Future are always possible.

        // CLIENT BLOCKING STUB
        ComputeServiceGrpc.ComputeServiceBlockingStub stub = ComputeServiceGrpc.newBlockingStub(channel);
        // Construct a request
//        Compute.NameRequest nameRequest =
//                Compute.NameRequest.newBuilder()
//                        .setName("testingV")
//                        .build();

        // CLIENT NON-BLOCKING STUB
        ComputeServiceGrpc.ComputeServiceStub nbStub = ComputeServiceGrpc.newStub(channel);
        // Construct a request
        Compute.ImageRequest imageRequest =
                Compute.ImageRequest.newBuilder()
                        .setRegionName("Ireland")
                        .build();

        // Finally, make the call using the stub
//        Compute.NameResponse response =
//                stub.validateName(nameRequest);

        // Make an Asynchronous call. Listen to responses w/ StreamObserver
        nbStub.retrieveImages(imageRequest, new StreamObserver<Compute.ImageResponse>() {
            public void onNext(Compute.ImageResponse imgResp) {
                System.out.println(imgResp);
            }

            public void onError(Throwable t) {
            }

            public void onCompleted() {
                // Typically you'll shutdown the channel somewhere else.
                // But for the purpose of the lab, we are only making a single
                // request. We'll shutdown as soon as this request is done.
                channel.shutdownNow();
            }
        });

//        System.out.println(response);

        // A Channel should be shutdown before stopping the process.

//        channel.shutdownNow();
    }
}