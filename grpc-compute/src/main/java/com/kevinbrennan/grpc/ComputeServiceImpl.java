package com.kevinbrennan.grpc;

import io.grpc.stub.StreamObserver;

import java.util.HashMap;
import java.util.logging.Logger;

import java.util.ArrayList;
import java.util.List;

public class ComputeServiceImpl extends ComputeServiceGrpc.ComputeServiceImplBase {

    // create in-memory testing data
    Database db = new Database();

    @Override
    public void validateName(Compute.NameRequest request, StreamObserver<Compute.NameResponse> responseObserver) {

        Compute.NameResponse response;

        // construct new Proto buffer object
        if (db.existingNames.contains(request.getName())) {
            response = Compute.NameResponse.newBuilder()
                    .setValidNameChoice(false)
                    .setError("Error: " + request.getName() + " is already taken. Please choose a different name.")
                    .build();
        } else {
            response = Compute.NameResponse.newBuilder()
                    .setValidNameChoice(true)
                    .setError(request.getName() + " is available for selection.")
                    .build();
        }

        // Use responseObserver to send a single response back
        responseObserver.onNext(response);

        // When you are done, you must call onCompleted.
        responseObserver.onCompleted();
    }

    @Override
    public void retrieveImages(Compute.ImageRequest request, StreamObserver<Compute.ImageResponse> responseObserver) {

        Compute.ImageResponse response;

        // construct new Proto buffer object
        for (String image : db.availableImages) {
            response = Compute.ImageResponse.newBuilder()
                    .setImageName(image)
                    .build();

            // send single response back per iteration
            responseObserver.onNext(response);
        }
        // When you are done, you must call onCompleted.
        responseObserver.onCompleted();
    }

    @Override
    public void validateCapacity(Compute.CapacityRequest request, StreamObserver<Compute.CapacityResponse> responseObserver) {

        Compute.CapacityResponse response;

        // compute available cores in specified region
        String region = request.getRegion();
        int requestedCpuCores = request.getCpuCount();
        int availableCpuCores = getRegionalCpuCores(region);
        boolean boolAvailable = !(availableCpuCores > 0);

        response = Compute.CapacityResponse.newBuilder()
                .setCapacityAvailability(boolAvailable)
                .setCpuCoresAvailable(availableCpuCores)
                .build();

        // send single response back per iteration
        responseObserver.onNext(response);

        // When you are done, you must call onCompleted.
        responseObserver.onCompleted();
    }

    public int getRegionalCpuCores(String region) {
        String r = region.toLowerCase();
        if (r.contains("uk")) {
            return db.ukWest;
        }
        if (r.contains("india")) {
            return db.centralIndia;
        }
        if (r.contains("japan")) {
            return db.japanEast;
        }
        return 0;
    }
}