package com.exampel.myMail.controller;

import com.exampel.myMail.model.User;
import com.exampel.myMail.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
public class UserRestController {

    @Autowired
    private UserService userService;

    @PostMapping(path = "server/addUser")
    public String serverAddNewUser(@RequestBody User addNewUser){
        String newLogin = addNewUser.getLogin();
        if (newLogin != null){
            User user = userService.serverFindByLogin(newLogin);
            if (user != null){
                return "Пользователь " + newLogin + " уже существует";
            }
            else{
                user = new User();
                user.setLogin(newLogin);
                user.setPassword(addNewUser.getPassword());
                userService.serverAddUser(user);

                return "Добавлен новый пользователь";
            }
        }
        return "Нужен логин";
    }

    @GetMapping (path = "server/allUsers")
    public List<String> serverAllUsers(Principal principal) {
        return userService.serverGetAllUser(principal.getName());
    }

    @GetMapping (path = "/")
    public String home() {
        return "To use this API you should authorize";
    }
}
