package com.example.IoTBackend.KafkaListener;

import com.example.IoTBackend.DTO.StatisticDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class StatisticListener {

    @KafkaListener(topics = "statistic",groupId = "${kafka.topics.statistic.group.id}")
    public void consumeStatisticTopic(@Payload StatisticDto statisticDto){
        System.out.println(statisticDto);
    }

}
