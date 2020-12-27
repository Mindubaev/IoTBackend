package com.example.IoTBackend.gRPC.impl;

import com.example.IoTBackend.Service.api.SensorService;
import com.example.iot.gRPC.api.*;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
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
        Long id=request.getId();
        Boolean authenticated=sensorService.authentication(id,secret);
        AuthResponse.Builder builder=AuthResponse
                .newBuilder()
                .setAuthorized(authenticated);
        sensorService.getSensorInfo(id).ifPresent(sensorShortInfo -> builder
                .setMaxTemp(sensorShortInfo.getMaxTemp())
                .setMinTemp(sensorShortInfo.getMinTemp())
        );
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

}
