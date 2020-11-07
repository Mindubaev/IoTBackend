/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.IoTBackend.Entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "statistic")
public class Statistic implements Serializable{
    
    @Id
    @org.springframework.data.annotation.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private float temp;
    
    private LocalDateTime date;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kettle_id")
    private Kettle kettle;

    public Statistic() {
    }

    public Statistic(Long id, float temp, LocalDateTime date, Kettle kettle) {
        this.id = id;
        this.temp = temp;
        this.date = date;
        this.kettle = kettle;
    }

    public Long getId() {
        return id;
    }

    public float getTemp() {
        return temp;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public Kettle getKettle_id() {
        return kettle;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTemp(float temp) {
        this.temp = temp;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void setKettle(Kettle kettle) {
        this.kettle = kettle;
    }
    
    
}
