package com.spring.repository;


import com.spring.domain.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.io.Serializable;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {
}
