package com.example.IoTBackend.Service.impl;

import com.example.IoTBackend.Entity.Sensor;
import com.example.IoTBackend.Repository.SensorRepositoty;
import com.example.IoTBackend.Service.api.SensorService;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Service
@Transactional
public class SensorServiceImpl implements SensorService {

    @Value("{kafka.topics.sensor.prefix}")
    private String prefix;

    @Autowired
    private KafkaAdmin kafkaAdmin;

    @Autowired
    public SensorRepositoty sensorRepositoty;

    @Override
    public Long registrate(UUID secret) {
        Sensor sensor=new Sensor();
        sensor.setSecret(secret);
        sensor.setMaxTemp(0);
        sensor.setMinTemp(0);
        sensor=sensorRepositoty.save(sensor);

        NewTopic topic=TopicBuilder.name(prefix+sensor.getId())
                .partitions(1)
                .build();
        AdminClient client=AdminClient.create(kafkaAdmin.getConfig());
        client.createTopics(List.of(topic));
        client.close();

        return sensor.getId();
    }

    @Override
    public boolean authentication(Long id, UUID secret) {
        Optional<Sensor> optional=sensorRepositoty.findByIdAndSecret(id,secret);
        return optional.isPresent();
    }
}
