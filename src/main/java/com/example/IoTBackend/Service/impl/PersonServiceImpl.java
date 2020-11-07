/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.IoTBackend.Service.impl;

import com.example.IoTBackend.Entity.Person;
import com.example.IoTBackend.Repository.PersonRepository;
import com.example.IoTBackend.Service.api.PersonService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Service
@Transactional
public class PersonServiceImpl implements PersonService,UserDetailsService{
    
    @Autowired
    private PersonRepository personRepository;

    public PersonServiceImpl() {
    }

    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public Person save(Person peron) {
        return personRepository.save(peron);
    }

    @Override
    public void delete(Long id) {
        personRepository.deleteById(id);
    }

    @Override
    public Person findById(Long id) {
         return personRepository.findById(id).orElse(null);
    }

    @Override
    public Person findByUsername(String username) {
        return personRepository.findByUsername(username).orElse(null);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Person person=findByUsername(username);
        if (person!=null)
            return person;
        else
            throw new UsernameNotFoundException("Person with login '"+username+"' not found");
    }
    
}
