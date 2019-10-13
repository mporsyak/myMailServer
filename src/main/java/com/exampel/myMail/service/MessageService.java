package com.exampel.myMail.service;


import com.exampel.myMail.model.MessageDto;
import com.exampel.myMail.model.Message;
import com.exampel.myMail.model.User;
import com.exampel.myMail.repository.MessageRepository;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class MessageService {
    @Autowired
    MessageRepository messageRepository;

    private RestTemplate restTemplate;

    public MessageService(RestTemplateBuilder templateBuilder){
        restTemplate = templateBuilder.build();
    }

    public List<MessageDto> getAllIncomeMessages(String authUserLogin){
        return getAllDirectMessages(false, authUserLogin);
    }

    public List<MessageDto> getAllOutcomeMessages(String authUserLogin){
        return getAllDirectMessages(true, authUserLogin);
    }

    public void serverAddMessage(Message message){
        messageRepository.save(message);
    }

    public List<Message> serverGetAllMessageByUser(User userSender, User userRecip){
        return messageRepository.findByUserSenderAndUserRecip(userSender, userRecip);
    }

    private List<MessageDto> getAllDirectMessages(boolean isOutcomeDirect, String authUserLogin){
        List<Message> allMessageList = getAllMessage();

        List<MessageDto> messages = new ArrayList();
        for (int i = 0; i < allMessageList.size(); i++) {
            Message currentMessage = allMessageList.get(i);

            if (isOutcomeDirect ? currentMessage.getUserSender().getLogin().equals(authUserLogin) : currentMessage.getUserRecip().getLogin().equals(authUserLogin)){
                MessageDto messageInfo = new MessageDto();
                messageInfo.setContent(currentMessage.getContent());
                messageInfo.setGoal((isOutcomeDirect ? ("Получатель: " + currentMessage.getUserRecip().getLogin()) : ("Отправитель: " + currentMessage.getUserSender().getLogin())));
                messages.add(messageInfo);
            }
        }

        return messages;
    }





    public void addMessage(Message message){
        messageRepository.save(message);}

    public Message getMessage(String content){return messageRepository.findByContent(content);}


    public List<Message> getAllMessage(){
        return (List<Message>) messageRepository.findAll();
    }

    public List<Message> getAllMessageByUser(User userSender, User userRecip){
        return messageRepository.findByUserSenderAndUserRecip(userSender, userRecip);
    }

}
