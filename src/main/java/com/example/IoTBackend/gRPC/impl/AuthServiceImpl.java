package com.example.IoTBackend.gRPC.impl;

import com.example.IoTBackend.Service.api.SensorService;
import com.example.IoTBackend.gRPC.api.*;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

public class AuthServiceImpl extends AuthServiceGrpc.AuthServiceImplBase {

    @Autowired
    private SensorService sensorService;

    @Override
    public void registrate(RegRequest request, StreamObserver<RegResponse> responseObserver) {
        UUID secret=UUID.fromString(request.getSecret());
        Long id=sensorService.registrate(secret);
        RegResponse response=RegResponse
                .newBuilder()
                .setId(id)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void login(AuthRequest request, StreamObserver<AuthResponse> responseObserver) {
        UUID secret=UUID.fromString(request.getSecret());
        Boolean authenticated=sensorService.authentication(request.getId(),secret);
        AuthResponse response=AuthResponse
                .newBuilder()
                .setAuthorized(authenticated)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

}
