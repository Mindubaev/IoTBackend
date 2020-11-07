package com.example.IoTBackend.Entity;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Table(name = "person")
@Entity
public class Person implements Serializable,UserDetails{
    
    @Id
    @org.springframework.data.annotation.Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String username;
    
    private String password;
    
    @OneToMany(
        mappedBy = "person",
        fetch = FetchType.LAZY,
        cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH},
        orphanRemoval = true
    )
    private List<Setting> settings;
    
    
    @ManyToMany(mappedBy = "persons", 
            fetch = FetchType.LAZY,
            cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH}
    )
    private List<Kettle> kettles;

    public Person() {
    }

    public Person(String username, String password, List<Setting> settings, List<Kettle> kettles) {
        this.username = username;
        this.password = password;
        this.settings = settings;
        this.kettles = kettles;
    }

    public Person(Long id, String Username, String password, List<Setting> settings, List<Kettle> kettles) {
        this.id = id;
        this.username = Username;
        this.password = password;
        this.settings = settings;
        this.kettles = kettles;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public List<Setting> getSettings() {
        return settings;
    }

    public List<Kettle> getKettles() {
        return kettles;
    }

    public void setSettings(List<Setting> settings) {
        this.settings = settings;
    }

    public void setKettles(List<Kettle> kettles) {
        this.kettles = kettles;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority("USER"));
    }

    @Override
    public boolean isAccountNonExpired() {
        return  true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return  true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return  true;
    }

    @Override
    public boolean isEnabled() {
        return  true;
    }
    
    
    
}
