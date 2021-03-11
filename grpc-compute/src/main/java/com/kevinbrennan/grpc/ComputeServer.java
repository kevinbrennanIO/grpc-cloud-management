package com.kevinbrennan.grpc;
import io.grpc.*;
import java.util.ArrayList;

public class ComputeServer
{
    public static void main( String[] args ) throws Exception
    {
        // Create a new server to listen on port 2410
        Server computeServer = ServerBuilder.forPort(2410)
                .addService(new ComputeServiceImpl())
                .build();

        // Start the server
        computeServer.start();

        // Server threads are running in the background.
        System.out.println("Compute Server Started...");

        // Don't exit the main thread. Wait until server is terminated.
        computeServer.awaitTermination();
    }
}