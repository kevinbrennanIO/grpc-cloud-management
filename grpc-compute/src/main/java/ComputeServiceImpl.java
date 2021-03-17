import io.grpc.stub.StreamObserver;

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
                    .setResponseMessage("Error: " + request.getName() + " is already taken. Please choose a different name.")
                    .setResponseCode(6) // ALREADY_EXISTS
                    .build();
        } else {
            response = Compute.NameResponse.newBuilder()
                    .setValidNameChoice(true)
                    .setResponseMessage(request.getName() + " is available for selection.")
                    .setResponseCode(0) // OK
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
        String region = request.getRegionName();

        if (db.regions.contains(region.toLowerCase())) {
            // construct new Proto buffer object
            for (String image : db.availableImages) {
                response = Compute.ImageResponse.newBuilder()
                        .setImageName(image)
                        .setResponseMessage("No Errors")
                        .setResponseCode(0) // OK
                        .build();

                // send single response back per iteration
                responseObserver.onNext(response);
            }
        } else {
            response = Compute.ImageResponse.newBuilder()
                    .setImageName("")
                    .setResponseMessage("Invalid region specified in the request")
                    .setResponseCode(3) // INVALID_ARGUMENT
                    .build();

            // send single response back per iteration
            responseObserver.onNext(response);
        }
        responseObserver.onCompleted();
    }

    @Override
    public void validateCapacity(Compute.CapacityRequest request, StreamObserver<Compute.CapacityResponse> responseObserver) {

        Compute.CapacityResponse response;

        // compute available cores in specified region
        boolean coresAvailable = false;
        String region = request.getRegion();
        int requestedCpuCores = request.getCpuCount();
        int availableCpuCores = getRegionalCpuCores(region);
        if (availableCpuCores > requestedCpuCores) {
            coresAvailable = true;
        }

        if (coresAvailable) {
            response = Compute.CapacityResponse.newBuilder()
                    .setCapacityAvailability(coresAvailable)
                    .setCpuCoresAvailable(availableCpuCores)
                    .setResponseMessage("Region contains enough CPU cores to satisfy the request.")
                    .setResponseCode(0) // OK
                    .build();

        } else {
            response = Compute.CapacityResponse.newBuilder()
                    .setCapacityAvailability(coresAvailable)
                    .setCpuCoresAvailable(availableCpuCores)
                    .setResponseMessage("Not enough CPU cores available to satisfy the request.")
                    .setResponseCode(8) // RESOURCE_EXHAUSTED
                    .build();

        }
        responseObserver.onNext(response);
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