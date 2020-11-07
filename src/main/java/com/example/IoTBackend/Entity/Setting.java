/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.IoTBackend.Entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Table(name = "setting")
@Entity
public class Setting implements Serializable{
    
    @Id
    @org.springframework.data.annotation.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private float minTemp;
    
    private float maxTemp;
    
    private String name;
    
    @OneToMany(mappedBy = "setting",
            fetch = FetchType.LAZY,
            orphanRemoval = true,
            cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH}
    )
    private List<Kettle> kettles;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id")
    private Person person;

    public Setting() {
    }

    public Setting(Long id, float minTemp, float maxTemp, String name, List<Kettle> kettles, Person person) {
        this.id = id;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.name = name;
        this.kettles = kettles;
        this.person = person;
    }

    public Long getId() {
        return id;
    }

    public float getMinTemp() {
        return minTemp;
    }

    public float getMaxTemp() {
        return maxTemp;
    }

    public String getName() {
        return name;
    }

    public List<Kettle> getKettles() {
        return kettles;
    }

    public Person getPerson() {
        return person;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMinTemp(float minTemp) {
        this.minTemp = minTemp;
    }

    public void setMaxTemp(float maxTemp) {
        this.maxTemp = maxTemp;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setKettles(List<Kettle> kettles) {
        this.kettles = kettles;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
    
}
