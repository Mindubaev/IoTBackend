package com.example.IoTBackend.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class SensorShortInfo {

    private Long id;
    private Integer minTemperature;
    private Integer maxTemperature;

}
