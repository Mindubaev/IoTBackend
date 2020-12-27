package com.example.IoTBackend.Config;

import com.example.IoTBackend.Controller.StatisticHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private static final String STATISTIC_ENDPOINT="/sensor/{id}/statistic";

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(getWebSocketHandler(),STATISTIC_ENDPOINT)
        .setAllowedOrigins("*");
    }

    @Bean
    public TextWebSocketHandler getWebSocketHandler(){
        return new StatisticHandler(STATISTIC_ENDPOINT);
    }

}
