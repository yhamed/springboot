package com.spring.service;



import com.spring.domain.User;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface GenericService {
    User findByEmail(String username);

    List<User> findAllUsers();

    void updateUser(User user);

    User findUserByID(Long id);

    Boolean isUserExist(User user);

}
