package com.uchk.app.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
@Getter @Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String name;
    
    @Column
    private String description;
    
    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();
    
    // Constructor with name only
    public Role(String name) {
        this.name = name;
    }
    
    // Constructor with name and description
    public Role(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
