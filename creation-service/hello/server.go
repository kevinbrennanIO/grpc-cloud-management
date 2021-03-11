package main

import (
	"context"
	"fmt"
)


func (s *HelloServer) SayHello(ctx context.Context, user *pb.User) (*pb.Response, error) {

	resp := user.name
	// No feature was found, return an unnamed feature
	return "kevin", nil
  }



