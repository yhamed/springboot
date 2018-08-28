package com.spring.domain;

import javax.persistence.*;

import org.springframework.data.annotation.Id;
public class Role {
    private static final long serialVersionUID = 1L;
    @Id
    private Long id;

    private String roleName;

    private String description;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
