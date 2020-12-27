package com.example.IoTBackend.KafkaListener;

import com.example.IoTBackend.DTO.StatisticDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingDeque;

@Service
public class StatisticListener {

    public static ConcurrentMap<Long, LinkedBlockingDeque<StatisticDto>> messages=new ConcurrentHashMap<>();

    @KafkaListener(topics = "statistic",groupId = "${kafka.topics.statistic.group.id}")
    public void consumeStatisticTopic(@Payload StatisticDto statisticDto){
        if (statisticDto!=null && statisticDto.getSensorId()!=null)
            messages.computeIfPresent(statisticDto.getSensorId(),(aLong, statisticDtos) -> {
                statisticDtos.add(statisticDto);
                return statisticDtos;
            });
    }

}
