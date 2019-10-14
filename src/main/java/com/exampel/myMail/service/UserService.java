package com.exampel.myMail.service;

import com.exampel.myMail.model.User;

import com.exampel.myMail.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public User serverFindByLogin(String login){
        return userRepository.findByLogin(login);
    }

    public void serverAddUser(User user) {
        userRepository.save(user);
    }

    public List<String> serverGetAllUser(String authUser) {
        List<User> users = (List<User>) userRepository.findAll();
        users.removeIf(e -> e.getLogin().equals(authUser));

        List<String> allLogins = users.stream().map(e -> e.getLogin()).collect(Collectors.toList());
        return allLogins;
    }

    public User findByLogin(String login){
        return userRepository.findByLogin(login);
    }

}
