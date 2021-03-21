import io.grpc.*;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TestClient {

    //== TEST CLIENT USED FOR TESTING CLIENT SIDE STREAMING ==//

    // Configure logger
    private static final Logger logger = Logger.getLogger(TestClient.class.getName());

    // Define Client Stubs
    private final DatabaseServiceGrpc.DatabaseServiceBlockingStub blockingStub;
    private final DatabaseServiceGrpc.DatabaseServiceStub asyncStub;

    // Construct a client object when called
    public TestClient(Channel channel) {
        blockingStub = DatabaseServiceGrpc.newBlockingStub(channel);
        asyncStub = DatabaseServiceGrpc.newStub(channel);
    }

    // Upload to database
    public void dbUpload() throws InterruptedException {

        logger.log(Level.INFO, "Database upload method invoked");

        StreamObserver<Database.DbUploadResponse> responseObserver = new StreamObserver<Database.DbUploadResponse>() {
            @Override
            public void onNext(Database.DbUploadResponse value) {
                // TODO : Change what is returned
                logger.log(Level.INFO, "Upload Complete... Returning some made up data object!!");
            }

            @Override
            public void onError(Throwable t) {
                logger.log(Level.WARNING, "Upload failed");
            }

            @Override
            public void onCompleted() {
                logger.log(Level.INFO, "Upload complete");
            }
        };

        StreamObserver<Database.DbUploadRequest> requestObserver = asyncStub.databaseUpload(responseObserver);

        Database.DbUploadRequest clientRequest = Database.DbUploadRequest.newBuilder()
                .setPayloadChunk("01101101")
                .setDatabaseName("testDatabase")
                .setDatabaseToken("databaseToken")
                .build();

        for (int i = 0; i < 10; i++) {
            requestObserver.onNext(clientRequest);
            // Sleep for one second
            Thread.sleep(1000);
        }

        // Mark the end of requests
        requestObserver.onCompleted();
    }


    // Main method - runs the client
    public static void main(String[] args) throws Exception {

        // Instantiate communication channel with :2411
        ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:2411")
                .usePlaintext()
                .build();

        // Instantiate Test Client
        TestClient testClient = new TestClient(channel);

        testClient.dbUpload();
        channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);

    }
}