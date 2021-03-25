package main

import (
	"google.golang.org/grpc"
	"io"
	"log"
	"math/rand"
	"net"
	"strconv"
	"sync"
	"time"

	pb "grpcBillingServer/billingserver"
)

// billingserverServer defines how our billing server object
// should look like
type billingserverServer struct {
	pb.UnimplementedBillingServer
	mu              sync.Mutex
}

// SubscriptionCost receives a stream of subscriptionIDs, and responds with a stream
// of current cost for the subscription (which is randomly generated for demo purpose)
func (b *billingserverServer) SubscriptionCost(stream pb.Billing_SubscriptionCostServer) error {
	for {
		in, err := stream.Recv()
		if err == io.EOF {
			return nil
		}
		if err != nil {
			return err
		}

		// lock transaction so no other client is processed
		b.mu.Lock()

		// define a seed for random number generation
		s1 := rand.NewSource(time.Now().UnixNano())

		// initialize CostUsageResponse object and populate with
		// the servers response data
		resp := &pb.CostUsageResponse{
			SubscriptionID:  in.SubscriptionID,
			AccumulatedCost: strconv.Itoa(rand.New(s1).Intn(1000)),
		}

		if err := stream.Send(resp); err != nil {
			return err
		}
		log.Printf("Response sent to client: %v", resp)

		// unlock the transaction
		b.mu.Unlock()
	}
}

// newServer returns a server of type 'billingserverServer'
func newServer() *billingserverServer {
	s := &billingserverServer{}
	return s
}

func main() {

	//initialize tcp listener that will server the gRPC server
	lis, err := net.Listen("tcp", "localhost:2412")
	if err != nil {
		log.Fatalf("failed to listen on 2412: %v", err)
	}
	log.Println("Server running OK on port 2412")

	// instantiate a new gRPC server
	grpcServer := grpc.NewServer()

	// register/serve gRPC service
	pb.RegisterBillingServer(grpcServer, newServer())
	if err := grpcServer.Serve(lis); err != nil {
		log.Fatalf("Error serving gRPC: %v ", err)
	}
}
