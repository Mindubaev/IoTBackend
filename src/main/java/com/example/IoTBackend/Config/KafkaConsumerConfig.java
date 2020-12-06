package com.example.IoTBackend.Config;

import com.example.IoTBackend.DTO.StatisticDto;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    @Value(value = "${kafka.bootstrap-server}")
    private String BOOTSTRAP_SERVER;
    @Value(value = "${kafka.topics.statistic.group.id}")
    private String GROUP_ID;

    @Bean
    public Map<String, Object> deserializerProps(){
        Map<String,Object> props=new HashMap<>();
        props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS,false);
//        props.put(JsonDeserializer.TRUSTED_PACKAGES,"*");
        return props;
    }

    @Bean
    public ConsumerFactory<String, StatisticDto> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                BOOTSTRAP_SERVER);
        props.put(
                ConsumerConfig.GROUP_ID_CONFIG,
                GROUP_ID);
        JsonDeserializer<StatisticDto> jsonDeserializer=new JsonDeserializer<>(StatisticDto.class);
        jsonDeserializer.addTrustedPackages("*");
        jsonDeserializer.configure(deserializerProps(),false);
        return new DefaultKafkaConsumerFactory<>(props,new StringDeserializer(),jsonDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, StatisticDto> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, StatisticDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

}
