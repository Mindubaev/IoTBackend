/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.IoTBackend.Entity;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "kettle")
public class Kettle implements Serializable{
    
    @Id
    @org.springframework.data.annotation.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    
    private UUID secret;
    
    @OneToMany(
        mappedBy = "kettle",
        fetch=FetchType.LAZY,
        cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH},
        orphanRemoval = true
    )
    private List<Statistic> statistics;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "setting_id")
    private Setting setting;
    
    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH}
    )
    @JoinTable(name = "kettle_person_relation", 
            joinColumns = @JoinColumn(name = "kettle_id"),
            inverseJoinColumns = @JoinColumn(name="person_id")
    )
    private List<Person> persons;

    public Kettle() {
    }

    public Kettle(Long id, String name, UUID secret, List<Statistic> statistics, Setting setting, List<Person> persons) {
        this.id = id;
        this.name = name;
        this.secret = secret;
        this.statistics = statistics;
        this.setting = setting;
        this.persons = persons;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public UUID getSecret() {
        return secret;
    }

    public List<Statistic> getStatistics() {
        return statistics;
    }

    public Setting getSetting() {
        return setting;
    }

    public List<Person> getPersons() {
        return persons;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSecret(UUID secret) {
        this.secret = secret;
    }

    public void setStatistics(List<Statistic> statistics) {
        this.statistics = statistics;
    }

    public void setSetting(Setting setting) {
        this.setting = setting;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }
    
}
