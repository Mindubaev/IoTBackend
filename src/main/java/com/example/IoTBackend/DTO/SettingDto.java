package com.example.IoTBackend.DTO;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class SettingDto {

    @NotNull
    private Integer maxTemp;
    @NotNull
    private Integer minTemp;

}
