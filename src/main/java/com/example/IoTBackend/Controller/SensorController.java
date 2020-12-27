package com.example.IoTBackend.Controller;

import com.example.IoTBackend.DTO.ComandDto;
import com.example.IoTBackend.DTO.SensorShortInfo;
import com.example.IoTBackend.DTO.SettingDto;
import com.example.IoTBackend.Entity.Person;
import com.example.IoTBackend.Repository.PersonRepository;
import com.example.IoTBackend.Repository.SensorRepositoty;
import com.example.IoTBackend.Service.api.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Validated
@RestController
@RequestMapping("/sensor")
@CrossOrigin(origins = "*",allowCredentials = "true",allowedHeaders = "*",methods = {RequestMethod.GET,RequestMethod.POST,RequestMethod.PATCH,RequestMethod.DELETE,RequestMethod.OPTIONS})
public class SensorController {

    public static final String SETTINGS="SETTINGS";

    private final TransactionTemplate transactionTemplate;

    @Autowired
    private SensorRepositoty sensorRepositoty;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private SensorService sensorService;
    @Autowired
    private KafkaTemplate<String, ComandDto> kafkaTemplate;

    @Value("${kafka.topics.sensor.prefix}")
    private String prefix;

    public SensorController(PlatformTransactionManager transactionManager){
        this.transactionTemplate=new TransactionTemplate(transactionManager);
    }

    @GetMapping("/{id}/info")
    public ResponseEntity<SensorShortInfo> getSensorShortInfo(@PathVariable Long id,@AuthenticationPrincipal Person currentPerson){
        if (hasAccessToSensor(id,currentPerson.getId()))
            return new ResponseEntity(HttpStatus.BAD_REQUEST);

        return sensorService.getSensorInfo(id)
                .map(sensorShortInfo -> new ResponseEntity<SensorShortInfo>(sensorShortInfo,HttpStatus.OK))
                .orElse(new ResponseEntity<SensorShortInfo>(HttpStatus.BAD_REQUEST));
    }

    @PostMapping("/{id}/settings")
    public ResponseEntity<SensorShortInfo> setSettings(@PathVariable Long id, @Valid @RequestBody SettingDto settingDto, @AuthenticationPrincipal Person currentPerson){
        Boolean hasAccess=hasAccessToSensor(id,currentPerson.getId());
        if (Boolean.FALSE.equals(hasAccess) || settingDto.getMaxTemp()==null || settingDto.getMinTemp()==null)
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        try {
            System.out.println(settingDto.getMinTemp()+" "+settingDto.getMaxTemp());
            kafkaTemplate.send(
                    prefix + id.toString(),
                    new ComandDto(SETTINGS, settingDto.getMinTemp(), settingDto.getMaxTemp())
            ).get(10, TimeUnit.SECONDS);
            return transactionTemplate.execute(transactionStatus -> {

                return sensorRepositoty.findById(id).map(sensor -> {
                    sensor.setMinTemp(settingDto.getMinTemp());
                    sensor.setMaxTemp(settingDto.getMaxTemp());
                    sensor=sensorRepositoty.save(sensor);
                    return new ResponseEntity(SensorShortInfo.fromSensor(sensor),HttpStatus.OK);
                }).orElse(new ResponseEntity(HttpStatus.BAD_REQUEST));

            });
        }catch (InterruptedException | ExecutionException exception){
            exception.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }catch (TimeoutException exception){
            exception.printStackTrace();
            return new ResponseEntity(HttpStatus.EXPECTATION_FAILED);
        }
    }

    private Boolean hasAccessToSensor(Long sensorId,Long personId){
        return transactionTemplate.execute(transactionStatus -> {
            return personRepository.findById(personId)
                    .map(Person::getSensors).stream()
                    .flatMap(Collection::stream)
                    .anyMatch(sensor -> sensor.getId().equals(sensorId));
        });
    }

}
