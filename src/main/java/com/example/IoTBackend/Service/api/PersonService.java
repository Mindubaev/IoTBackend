/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.IoTBackend.Service.api;

import com.example.IoTBackend.Entity.Person;
import java.util.List;

public interface PersonService {
    
    Person save(Person peron);
    void delete(Long id);
    Person findById(Long id);
    Person findByUsername(String username);
    
}
