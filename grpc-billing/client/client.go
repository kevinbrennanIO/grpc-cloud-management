package main

import (
	"context"
	"google.golang.org/grpc"
	pb "grpcBillingServer/billingserver"
	"io"
	"log"
	"time"
)

const (
	// subscriptionID
	subID = "div4-dock34-veggie34rf-buggy654-right87"
)

// biDirectionalReq sends a stream of requests to the server file
// simultaneously processing responses from the server
func biDirectionalReq(client pb.BillingClient) {

	// define request object
	req := &pb.CostUsageRequest{SubscriptionID: subID}

	// run client for 10 seconds and exit
	ctx, cancel := context.WithTimeout(context.Background(), 10*time.Second)

	defer cancel()
	// instantiate client stream
	stream, err := client.SubscriptionCost(ctx)
	if err != nil {
		log.Fatalf("%v.SubscriptionCost(_) = _, %v", client, err)
	}
	waitChannel := make(chan struct{})

	// client receives responses from the server
	go func() {
		for {
			in, err := stream.Recv()
			if err == io.EOF {
				// read done.
				close(waitChannel)
				return
			}
			if err != nil {
				log.Fatalf("Failed to receive a response: %v", err)
			}
			log.Printf("Response received %s:", in.AccumulatedCost)
		}
	}()

	// client sends 10 requests to the server (every 2 seconds)
	for i := 0; i < 10; i++ {
		if err := stream.Send(req); err != nil {
			log.Fatalf("Failed to send a request: %v", err)
		}
		// sleep
		time.Sleep(2 * time.Second)
	}

	stream.CloseSend()
	<-waitChannel
}
func main() {

	// define client dialing options
	var opts []grpc.DialOption
	opts = append(opts, grpc.WithInsecure())
	opts = append(opts, grpc.WithBlock())

	// define the connection object
	conn, err := grpc.Dial("localhost:5000", opts...)
	if err != nil {
		log.Fatalf("Error while dialing: %v", err)
	}

	// defer closing the connection until complete
	defer conn.Close()

	// instantiate a new billing client with connection details
	client := pb.NewBillingClient(conn)

	// send the streaming request
	biDirectionalReq(client)
}
