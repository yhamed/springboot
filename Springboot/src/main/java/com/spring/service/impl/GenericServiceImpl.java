package com.spring.service.impl;

import com.spring.domain.User;
import com.spring.repository.UserRepository;
import com.spring.service.GenericService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class GenericServiceImpl implements GenericService {
    @Autowired
    private UserRepository userRepository;


    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> findAllUsers() {
        return (List<User>)userRepository.findAll();
    }

    @Override
    public void updateUser(User user) {
        userRepository.save(user);
    }

    @Override
    public User findUserByID(Long id) {
        return userRepository.findOne(id);
    }

    @Override
    public Boolean isUserExist(User user) {
        return userRepository.exists(user.getId());
    }


}
