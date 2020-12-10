package com.example.IoTBackend.Controller;

import com.example.IoTBackend.DTO.SensorShortInfo;
import com.example.IoTBackend.Repository.SensorRepositoty;
import org.apache.kafka.clients.consumer.Consumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@RequestMapping("/sensor")
public class SensorController {

    @Autowired
    private SensorRepositoty repositoty;

    @GetMapping("/{id}/info")
    public ResponseEntity<SensorShortInfo> getSensorShortInfo(@PathVariable Long id){
         return repositoty.findById(id).flatMap(sensor -> {
             SensorShortInfo shortInfo=new SensorShortInfo(sensor.getId(),sensor.getMinTemp(),sensor.getMaxTemp());
             return Optional.of(new ResponseEntity<SensorShortInfo>(shortInfo, HttpStatus.OK));
         }).orElse(new ResponseEntity<SensorShortInfo>(HttpStatus.BAD_REQUEST));
    }

}
