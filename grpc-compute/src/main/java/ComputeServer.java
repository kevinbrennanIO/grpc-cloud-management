import io.grpc.*;
import java.util.logging.Logger;

public class ComputeServer {

    // Configure logging
    private static final Logger logger = Logger.getLogger(ComputeServer.class.getName());

    // set server port number
    private static final int port = 2410;

    public static void main(String[] args) throws Exception {

        // Create a new server to listen on port 2410
        Server computeServer = ServerBuilder.forPort(port)
                .addService(new ComputeServiceImpl())
                .build();

        // Start the server
        computeServer.start();

        // Server threads are running in the background.
        logger.info("Compute server started, listening on port: " + port);

        // Wait until server is terminated.
        computeServer.awaitTermination();
    }
}