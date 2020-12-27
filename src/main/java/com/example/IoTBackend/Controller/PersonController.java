/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.IoTBackend.Controller;

import com.example.IoTBackend.DTO.SensorShortInfo;
import com.example.IoTBackend.Entity.Person;
import com.example.IoTBackend.Repository.PersonRepository;
import com.example.IoTBackend.Repository.SensorRepositoty;
import com.example.IoTBackend.Service.api.PersonService;
import com.example.IoTBackend.Validation.LoginConstraint;
import com.example.IoTBackend.Validation.PasswordConstraint;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/user")
@Validated
@RestController
@CrossOrigin(origins = "*",allowCredentials = "true",allowedHeaders = "*",methods = {RequestMethod.GET,RequestMethod.POST,RequestMethod.PATCH,RequestMethod.DELETE,RequestMethod.OPTIONS})
public class PersonController {

    private final TransactionTemplate transactionTemplate;

    @Autowired
    private PersonService personService;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private SensorRepositoty sensorRepositoty;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public PersonController(PlatformTransactionManager transactionManager){
        this.transactionTemplate=new TransactionTemplate(transactionManager);
    }
    
    private boolean isAlreadyExist(String str) {
        return personService.findByUsername(str) != null;
    }
    
    @PostMapping
    public ResponseEntity<Person> registration(@RequestBody @Valid LoginForm form) {
        if (!isAlreadyExist(form.getUsername())) {
            Person person = personService.save(form.toPerson(passwordEncoder));
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(form.getUsername(), form.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return new ResponseEntity<Person>(person, HttpStatus.OK);
        } else {
            return new ResponseEntity<Person>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Person> login(@Valid @RequestBody LoginForm form) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(form.getUsername(), form.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            Person person=personService.findByUsername(form.getUsername());
            return new ResponseEntity<Person>(person, HttpStatus.OK);
        } catch (AuthenticationException ex) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<Person> logout() {
        SecurityContextHolder.getContext().setAuthentication(null);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/sensors")
    public ResponseEntity<List<SensorShortInfo>> getPersonSensors(@AuthenticationPrincipal Person currentUser){
        List<SensorShortInfo> shortInfoList=transactionTemplate.execute(transactionStatus -> {
            return personRepository.findById(currentUser.getId())
                    .map(person -> person.getSensors()).stream()
                    .flatMap(Collection::stream)
                    .map(SensorShortInfo::fromSensor)
                    .collect(Collectors.toList());
        });
        return new ResponseEntity<>(shortInfoList,HttpStatus.OK);
    }

    @PostMapping("/sensors")
    public ResponseEntity<SensorShortInfo> postSensorToPerson(@Valid @RequestBody SensorForm sensorForm,@AuthenticationPrincipal Person currentPerson){
       return transactionTemplate.execute(transactionStatus -> {

           return sensorRepositoty.findByIdAndSecret(sensorForm.getId(),sensorForm.getSecret()).map(sensor -> {

               return personRepository.findById(currentPerson.getId()).map(person -> {
                   if (person.getSensors().contains(sensor) || sensor.getPersons().contains(person))
                       return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                   person.getSensors().add(sensor);
                   sensor.getPersons().add(person);
                   personRepository.save(person);
                   sensorRepositoty.save(sensor);
                   return new ResponseEntity(SensorShortInfo.fromSensor(sensor),HttpStatus.OK);
               }).orElse(new ResponseEntity(HttpStatus.BAD_REQUEST));

           }).orElse(new ResponseEntity(HttpStatus.BAD_REQUEST));

       });
    }

    @DeleteMapping("/sensors/{id}")
    public ResponseEntity deleteSensorFromPerson(@PathVariable Long id,@AuthenticationPrincipal Person currentPerson){
        return transactionTemplate.execute(transactionStatus -> {

            return sensorRepositoty.findById(id).map(sensor -> {

                return personRepository.findById(currentPerson.getId()).map(person -> {
                    if (!person.getSensors().contains(sensor) || !sensor.getPersons().contains(person))
                        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                    person.getSensors().remove(sensor);
                    sensor.getPersons().remove(person);
                    personRepository.save(person);
                    sensorRepositoty.save(sensor);
                    return new ResponseEntity(HttpStatus.OK);
                }).orElse(new ResponseEntity(HttpStatus.BAD_REQUEST));

            }).orElse(new ResponseEntity(HttpStatus.BAD_REQUEST));

        });
    }
    
    public static class LoginForm {

        @LoginConstraint(max = 30, min = 0, maxNumOfSpecChar = 0)
        private String username;
        @PasswordConstraint(max = 50, min = 0, minNumOfDigits = 0, minNumOfLetters = 0, maxNumOfSpecChar = 0)
        private String password;

        public LoginForm(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public Person toPerson(PasswordEncoder encoder) {
            return new Person(getUsername(),encoder.encode(getPassword()),new ArrayList<>());
        }

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SensorForm{

        @NotNull
        private Long id;
        @NotNull
        private UUID secret;

    }
    
}
