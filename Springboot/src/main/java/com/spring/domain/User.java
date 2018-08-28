package com.spring.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import javax.persistence.*;
import java.util.List;




public class User {

    public User() {}
    @Id
    private Long id;

    private String email;


    private String password;
    private String firstName;
    private String lastName;

    /**
     * Roles are being eagerly loaded here because
     * they are a fairly small collection of items for this example.
     */
//    @JsonIgnore
//    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(name = "user_role", joinColumns
//            = @JoinColumn(name = "user_id",
//            referencedColumnName = "id"),
//            inverseJoinColumns = @JoinColumn(name = "role_id",
//                    referencedColumnName = "id"))

    @DBRef
    @JsonIgnore
    private Role role;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    @JsonIgnore
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}

