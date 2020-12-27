package com.example.IoTBackend.Service.api;

import com.example.IoTBackend.DTO.SensorShortInfo;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

public interface SensorService {

    Long registrate(UUID secret);
    boolean authentication(Long id,UUID secret);
    Optional<SensorShortInfo> getSensorInfo(Long id);
}
