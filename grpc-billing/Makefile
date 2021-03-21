# build the proto definitions
proto:
	@echo "Compiling proto files"
	protoc --go_out=. \
	--go_opt=paths=source_relative \
	--go-grpc_out=. \
	--go-grpc_opt=paths=source_relative \
	billingserver/billing.proto

# clean up generated proto files
clean:
	@echo "Cleaning proto files"
	rm ./billingserver/*.pb.go
