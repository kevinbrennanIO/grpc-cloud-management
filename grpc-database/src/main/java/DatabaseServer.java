import io.grpc.*;

import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseServer {

    private static final Logger logger = Logger.getLogger(DatabaseServer.class.getName());
    private static final int port = 2411;

    public static void main(String[] args) throws Exception {

        // Create a new server to listen on port 2411
        Server databaseServer = ServerBuilder.forPort(2411)
                .addService(new DatabaseServiceImpl())
                .build();

        // Start the server
        databaseServer.start();

        // Server threads are running in the background.
        logger.log(Level.INFO, "Database server started, listening on port: " + port);

        // Wait until server is terminated.
        databaseServer.awaitTermination();
    }
}