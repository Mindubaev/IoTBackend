/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.IoTBackend.Controller;

import com.example.IoTBackend.Entity.Kettle;
import com.example.IoTBackend.Entity.Person;
import com.example.IoTBackend.Entity.Setting;
import com.example.IoTBackend.Service.api.PersonService;
import com.example.IoTBackend.Validation.LoginConstraint;
import com.example.IoTBackend.Validation.NameConstraint;
import com.example.IoTBackend.Validation.PasswordConstraint;
import java.util.ArrayList;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/user")
@Validated
@RestController
@CrossOrigin(origins = "*",allowCredentials = "true",allowedHeaders = "*",methods = {RequestMethod.GET,RequestMethod.POST,RequestMethod.PATCH,RequestMethod.DELETE,RequestMethod.OPTIONS})
public class PersonController {
    
    @Autowired
    private PersonService personService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    private boolean isAlreadyExist(String str) {
        return personService.findByUsername(str) != null;
    }
    
    @PostMapping("/person")
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
            return new ResponseEntity<Person>(personService.findByUsername(form.getUsername()), HttpStatus.OK);
        } catch (AuthenticationException ex) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/person/logout")
    public ResponseEntity<Person> logout() {
        SecurityContextHolder.getContext().setAuthentication(null);
        return new ResponseEntity(HttpStatus.OK);
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
            return new Person(getUsername(),encoder.encode(getPassword()),new ArrayList<>(),new ArrayList<>());
        }

    }
    
}
