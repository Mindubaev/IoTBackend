package com.example.IoTBackend.Controller;

import com.example.IoTBackend.DTO.StatisticDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

public class StatisticStreamTask implements Runnable{

    private static final int TIMEOUT=10;

    private volatile boolean isStreaming;
    private volatile WebSocketSession session;
    private volatile Thread thread;
    private LinkedBlockingDeque<StatisticDto> messages;
    private CountDownLatch latch;

    public StatisticStreamTask(WebSocketSession session, LinkedBlockingDeque<StatisticDto> messages){
        this.session=session;
        this.isStreaming=false;
        this.messages=messages;
    }

    public synchronized void start() throws InterruptedException{
        stop();
        latch = new CountDownLatch(1);
        isStreaming = true;
        thread = new Thread(this);
        thread.start();
    }

    public synchronized void stop() throws InterruptedException{
        isStreaming=false;
        if (latch != null && latch.getCount() != 0) {
            synchronized (latch) {
                latch.wait(TIMEOUT * 1000 + 200);
            }
        }
    }

        @Override
    public void run() {
        try {
            ObjectMapper mapper= new ObjectMapper();
            while (isStreaming && session.isOpen()) {
                StatisticDto statisticDto = messages.poll(TIMEOUT, TimeUnit.SECONDS);
                String json = mapper.writeValueAsString(statisticDto);

                synchronized (session) {
                    if (session.isOpen())
                        session.sendMessage(new TextMessage(json));
                }

            }
        }catch (InterruptedException | IOException e){
            e.printStackTrace();
            try {

                synchronized (session) {
                    if (session.isOpen())
                        session.close();
                }

            }catch (IOException ex){
                ex.printStackTrace();
            }
        }finally {
            synchronized (latch) {
                latch.countDown();
            }
        }
    }

}
