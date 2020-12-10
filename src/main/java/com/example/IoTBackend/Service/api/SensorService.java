package com.example.IoTBackend.Service.api;

import com.example.IoTBackend.Entity.Sensor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SensorService {

    Long registrate(UUID secret);
    boolean authentication(Long id,UUID secret);

}
