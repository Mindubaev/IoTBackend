package com.example.IoTBackend.Config;

import com.example.IoTBackend.gRPC.impl.AuthServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.IOException;

@Component
public class GrpcServerConfig implements DisposableBean,Runnable {

    private Server server;
    @Value("${gRPC.server.port}")
    private String port;
    private Thread thread;
    @Autowired
    private AuthServiceImpl authServiceImpl;

    public void start(){
        this.thread=new Thread(this);
        this.thread.start();
    }

    @Override
    public void run() {
        try {
            Server server= ServerBuilder.forPort(Integer.valueOf(port))
                    .addService(authServiceImpl)
                    .build();
            server.start();
            server.awaitTermination();
        }catch (IOException | InterruptedException e){
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() throws Exception {
        server.shutdownNow();
    }
}
