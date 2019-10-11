package com.exampel.myMail.controller;

import com.exampel.myMail.model.MessageDto;
import com.exampel.myMail.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ZaglushkaController {
    @Autowired
   private MessageService messageService;

    @GetMapping(value = "register")
    public String register(){
        return "Заглушка";
    }

    @GetMapping(value = "login")
    public String login(){
        return "Заглушка";
    }
}
