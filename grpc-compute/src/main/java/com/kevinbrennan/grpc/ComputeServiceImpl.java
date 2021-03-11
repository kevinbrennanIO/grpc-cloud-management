package com.kevinbrennan.grpc;
import io.grpc.stub.StreamObserver;

import java.util.ArrayList;
import java.util.List;

public class ComputeServiceImpl extends ComputeServiceGrpc.ComputeServiceImplBase {

    @Override
    public void validateName(Compute.NameRequest request, StreamObserver<Compute.NameResponse> responseObserver) {

        Compute.NameResponse response;
        Database db = new Database();

        // construct new Proto buffer object
        if(db.existingNames.contains(request.getName())) {
            response = Compute.NameResponse.newBuilder()
                    .setValidNameChoice(false)
                    .setError("Error: " + request.getName() + " is already taken. Please choose a different name.")
                    .build();
        } else {
            response = Compute.NameResponse.newBuilder()
                    .setValidNameChoice(true)
                    .setError("No error here!!" + request.getName() + " is a valid name that you can choose!!")
                    .build();
        }

        // Use responseObserver to send a single response back
        responseObserver.onNext(response);

        // When you are done, you must call onCompleted.
        responseObserver.onCompleted();
    }
}