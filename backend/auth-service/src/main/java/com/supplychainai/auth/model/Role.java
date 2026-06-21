package com.supplychainai.auth.model;

import jakarta.persistence.*;

@Entity
@Table(name = "roles", schema = "auth_schema")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private java.util.UUID id;

    @Column(unique = true, nullable = false, length = 50)
    private String name;

    public Role() {}

    public Role(String name) {
        this.name = name;
    }

    public java.util.UUID getId() { return id; }
    public void setId(java.util.UUID id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
