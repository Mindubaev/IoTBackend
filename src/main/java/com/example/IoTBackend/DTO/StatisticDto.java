package com.example.IoTBackend.DTO;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StatisticDto {

    private double temp;
    private LocalDateTime date;
    private Long sensorId;

}
