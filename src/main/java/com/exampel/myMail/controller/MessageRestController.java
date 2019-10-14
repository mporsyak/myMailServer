package com.exampel.myMail.controller;

import com.exampel.myMail.model.Message;
import com.exampel.myMail.model.MessageDto;
import com.exampel.myMail.model.NewMessageDto;
import com.exampel.myMail.model.User;
import com.exampel.myMail.service.MessageService;
import com.exampel.myMail.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class MessageRestController {
    @Autowired
   private MessageService messageService;

    @Autowired
    private UserService userService;

    @GetMapping(value = "server/showIncomeMessages", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MessageDto> showIncomeMessages(Principal principal){
        return messageService.getAllIncomeMessages(principal.getName());
    }

    @GetMapping(value = "server/showOutcomeMessages", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MessageDto> showOutcomeMessages(Principal principal){
        return messageService.getAllOutcomeMessages(principal.getName());
    }

    @PostMapping(value = "server/sendMessage", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> serverSendMessage(@RequestBody NewMessageDto newMessage, Principal principal) {
        User userSender = userService.findByLogin(principal.getName());
        User userRecipient = userService.findByLogin(newMessage.getUserRecip());

        if(userRecipient != null){
            Message message = new Message();
            message.setContent(newMessage.getContent());
            message.setUserSender(userSender);
            message.setUserRecip(userRecipient);
            message.setMsgTime(new Date());
            messageService.serverAddMessage(message);

            return new ResponseEntity<>("Сообщение успешно отправлено", HttpStatus.OK);
        }

        return new ResponseEntity<>("Пользователь " + newMessage.getUserRecip() + " не найден", HttpStatus.BAD_REQUEST);
    }

    @GetMapping (path = "server/allMessages/{user}")
    public List<MessageDto> serverAllMessages(@PathVariable("user") String userRecipParam, Principal principal) {
        User userSender = userService.findByLogin(principal.getName());
        User userRecip = userService.findByLogin(userRecipParam);

        List<Message> senderMessages = messageService.serverGetAllMessageByUser(userSender, userRecip);
        List<Message> recipMessages = messageService.serverGetAllMessageByUser(userRecip, userSender);

        List<MessageDto> result = new ArrayList<>();

        for (Message message : senderMessages){
            MessageDto  messageDto = new MessageDto();
            messageDto.setContent(message.getContent());
            messageDto.setMyMsg(true);
            messageDto.setCreateMsgTime(message.getMsgTime());

            result.add(messageDto);
        }

        for (Message message : recipMessages){
            MessageDto  messageDto = new MessageDto();
            messageDto.setContent(message.getContent());
            messageDto.setMyMsg(false);
            messageDto.setCreateMsgTime(message.getMsgTime());

            result.add(messageDto);
        }

        result.sort((a,b) -> a.getCreateMsgTime().compareTo(b.getCreateMsgTime()));
        return result;
    }
}
