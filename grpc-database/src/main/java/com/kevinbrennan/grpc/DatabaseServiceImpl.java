package com.kevinbrennan.grpc;
import io.grpc.stub.StreamObserver;

//public class DatabaseServiceImpl extends DatabaseServiceGrpc.DatabaseServiceImplBase {
//
//    // create in-memory testing data
//    Datastore ds = new Datastore();
//
//    @Override
//    public void listDatabases(Database.dbListRequest request, StreamObserver<Database.dbListResponse> responseObserver) {
//
//        Database.dbListResponse response;
//
//
//
//        response = Database.dbListResponse.newBuilder()
//                .setRespMessage()
//                .setStatusCode(Errors.Code.OK)
//                .build();
//
//        responseObserver.onNext(response);
//        responseObserver.onCompleted();
//    }
//}
