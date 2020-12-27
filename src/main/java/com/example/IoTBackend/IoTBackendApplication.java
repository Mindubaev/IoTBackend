package com.example.IoTBackend;

import com.example.IoTBackend.Config.GrpcServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example"})
public class IoTBackendApplication  implements CommandLineRunner{

	@Autowired
	private GrpcServerConfig grpcServerConfig;

	public static void main(String[] args) {
		SpringApplication.run(IoTBackendApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		grpcServerConfig.start();
	}
}
