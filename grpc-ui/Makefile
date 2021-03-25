# build the proto definitions
proto:
	@echo "Compiling proto files"
	python -m grpc_tools.protoc \
	-I ./protobufs --python_out=./gui \
	--grpc_python_out=./gui \
	./protobufs/*.proto

# clean up generated proto files
clean:
	@echo "Cleaning proto files"
	rm ./clients/*.py
