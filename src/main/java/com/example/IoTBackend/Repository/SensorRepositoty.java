package com.example.IoTBackend.Repository;

import com.example.IoTBackend.Entity.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SensorRepositoty extends JpaRepository<Sensor,Long> {

    Optional<Sensor> findByIdAndSecret(Long id, UUID secret);

}
