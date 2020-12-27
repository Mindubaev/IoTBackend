package com.example.IoTBackend.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "sensor")
public class Sensor implements Serializable{
    
    @Id
    @org.springframework.data.annotation.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    
    private UUID secret;
    
    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH}
    )
    @JoinTable(name = "sensor_person_relation",
            joinColumns = @JoinColumn(name = "sensor_id"),
            inverseJoinColumns = @JoinColumn(name="person_id")
    )
    private List<Person> persons;

    @Column(name = "minTemp")
    private Integer minTemp;

    @Column(name = "maxTemp")
    private Integer maxTemp;

}
