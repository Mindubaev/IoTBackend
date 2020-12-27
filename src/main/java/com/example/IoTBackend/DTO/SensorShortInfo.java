package com.example.IoTBackend.DTO;

import com.example.IoTBackend.Entity.Sensor;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class SensorShortInfo {

    private Long id;
    private String name;
    private Integer minTemp;
    private Integer maxTemp;

    public static SensorShortInfo fromSensor(Sensor sensor){
        SensorShortInfo shortInfo=new SensorShortInfo();
        shortInfo.setId(sensor.getId());
        shortInfo.setName(sensor.getName());
        shortInfo.setMinTemp(sensor.getMinTemp());
        shortInfo.setMaxTemp(sensor.getMaxTemp());
        return shortInfo;
    }

}
