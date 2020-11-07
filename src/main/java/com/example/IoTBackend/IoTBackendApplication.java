package com.example.IoTBackend;

import com.example.IoTBackend.Entity.Statistic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.IoTBackend"})
public class IoTBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(IoTBackendApplication.class, args);
	}

}
