import io.grpc.stub.StreamObserver;

import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseServiceImpl extends DatabaseServiceGrpc.DatabaseServiceImplBase {

    // Configure Logger
    private static final Logger logger = Logger.getLogger(DatabaseServiceImpl.class.getName());

    // Create in-memory testing data
    Datastore db = new Datastore();

    // Valid region helper function
    public boolean validRegion(String region) {
        return db.regions.contains(region);
    }

    // List all databases for specified region
    @Override
    public void listDatabases(Database.DbListRequest request, StreamObserver<Database.DbListResponse> responseObserver) {

        // Define response object
        Database.DbListResponse response;

        String region = request.getRegion().toLowerCase();

        // Validation logic
        if (validRegion(region)) {
            if (region.contains("india")) {
                for (String db : db.centralindia) {
                    response = Database.DbListResponse.newBuilder()
                            .setDatabaseName(db)
                            .setResponseMessage("No Errors")
                            .setResponseCode(0) // OK
                            .build();
                    // Send single response back per iteration
                    responseObserver.onNext(response);
                }
            }
            if (region.contains("japan")) {
                for (String db : db.japaneast) {
                    response = Database.DbListResponse.newBuilder()
                            .setDatabaseName(db)
                            .setResponseMessage("No Errors")
                            .setResponseCode(0) // OK
                            .build();
                    // Send single response back per iteration
                    responseObserver.onNext(response);
                }
            }
            if (region.contains("uk")) {
                for (String db : db.ukwest) {
                    response = Database.DbListResponse.newBuilder()
                            .setDatabaseName(db)
                            .setResponseMessage("No Errors")
                            .setResponseCode(0) // OK
                            .build();
                    // Send single response back per iteration
                    responseObserver.onNext(response);
                }
            }
        } else {
            response = Database.DbListResponse.newBuilder()
                    .setDatabaseName("")
                    .setResponseMessage("Invalid region specified")
                    .setResponseCode(3) // INVALID_ARGUMENT
                    .build();

            responseObserver.onNext(response);
        }
        // Complete once finished
        responseObserver.onCompleted();
    }

    // Login to database and retrieve token needed for DB Upload
    @Override
    public void databaseLogin(Database.DbAuthRequest request, StreamObserver<Database.DbAuthResponse> responseObserver) {

        // Define response object
        Database.DbAuthResponse response;

        String username = request.getUserName();
        String password = request.getPassword();

        // Validation logic
        if (username.equals(db.userName) && password.equals(db.password)) {
            response = Database.DbAuthResponse.newBuilder()
                    .setDatabaseToken(db.dummyToken)
                    .setResponseMessage("Valid credentials provided.")
                    .setResponseCode(0) // OK
                    .build();

        } else {
            response = Database.DbAuthResponse.newBuilder()
                    .setDatabaseToken("")
                    .setResponseMessage("Invalid credentials provided. Try again")
                    .setResponseCode(16) // UNAUTHENTICATED
                    .build();

        }

        // Respond and end communication with client
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    // Upload stream dummy binary data to database (DB token required - from previous request)
    @Override
    public StreamObserver<Database.DbUploadRequest> databaseUpload(StreamObserver<Database.DbUploadResponse> responseObserver) {

        return new StreamObserver<Database.DbUploadRequest>() {

            // Response message overwritten on failure
            String responseMessage = "Upload successful: Data -> ";

            // dummy data string to append incoming data to
            String dataBinary = "";

            @Override
            public void onNext(Database.DbUploadRequest request) {

                // Validation logic
                if (request.getDatabaseToken().equals(db.dummyToken)) {

                    // append dummy payloadChuck to existing binary data
                    dataBinary = dataBinary.concat(request.getPayloadChunk());

                    // debugging output
                    logger.log(Level.INFO, "DATA OBJECT: " + dataBinary.concat(request.getPayloadChunk()));
                    logger.log(Level.INFO, "DATABASE NAME: " + request.getDatabaseName());
                    logger.log(Level.INFO, "DATABASE TOKEN: " + request.getDatabaseToken());
                }
                else {
                    responseMessage = "ERROR: invalid DB token provided.";
                }
            }

            @Override
            public void onError(Throwable t) {
                // log error
                logger.log(Level.WARNING, "Encountered error in database upload", t);
            }

            @Override
            public void onCompleted() {
                // server responds once client has finished uploading
                responseObserver.onNext(
                        Database.DbUploadResponse.newBuilder()
                                .setResponseMessage(responseMessage + dataBinary)
                                .setResponseCode(0).build()); // OK

                // server ends the communication
                responseObserver.onCompleted();
            }
        };
    }
}
