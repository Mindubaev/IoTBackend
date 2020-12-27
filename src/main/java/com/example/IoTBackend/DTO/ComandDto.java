package com.example.IoTBackend.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ComandDto {

    public String name;
    public Integer min;
    public Integer max;

    public ComandDto(String comand){
        this.name=comand;
    }

}
