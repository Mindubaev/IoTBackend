package com.example.IoTBackend.Controller;

import com.example.IoTBackend.DTO.ComandDto;
import com.example.IoTBackend.KafkaListener.StatisticListener;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.server.PathContainer;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.util.pattern.PathPatternParser;

import java.util.concurrent.LinkedBlockingDeque;

public class StatisticHandler extends TextWebSocketHandler implements DisposableBean {

    @Value("${kafka.topics.sensor.prefix}")
    private String prefix;

    private StatisticStreamTask statisticStreamTask;
    private Long sensorId;
    private String urlPattern;

    @Autowired
    private KafkaTemplate<String, ComandDto> kafkaTemplate;

    public StatisticHandler(String urlPattern) {
        this.urlPattern = urlPattern;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String rawPath = session.getUri().getPath();
        PathPatternParser parser = new PathPatternParser();
        String id = parser.parse(urlPattern)
                .matchAndExtract(PathContainer.parsePath(rawPath))
                .getUriVariables().get("id");
        this.sensorId = Long.valueOf(id);
        LinkedBlockingDeque messagesQueue = new LinkedBlockingDeque(3);
        StatisticListener.messages.putIfAbsent(this.sensorId, messagesQueue);

        kafkaTemplate.send(prefix + id, new ComandDto("START_STREAMING"));

        this.statisticStreamTask = new StatisticStreamTask(session, messagesQueue);
        statisticStreamTask.start();
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        kafkaTemplate.send(prefix + this.sensorId, new ComandDto("STOP_STREAMING"));
        statisticStreamTask.stop();
        StatisticListener.messages.computeIfPresent(sensorId, (aLong, statisticDtos) -> {
            statisticDtos.clear();
            return null;
        });
    }

    @Override
    public void destroy() throws Exception {
        if (statisticStreamTask != null)
            statisticStreamTask.stop();
    }
}
