package com.sysoev.t1_openschool.service;

import com.sysoev.t1_openschool.dto.TaskResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String sender;

    @Value("${spring.mail.recipient}")
    private String recipient;

    public NotificationService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendTaskStatusChangeEmail(TaskResponseDto task) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipient);
        message.setSubject("Изменение статуса задачи");
        message.setText("Задача с ID: " + task.getId() + " теперь имеет статус: " + task.getStatus());

        //Дублировние, но без явного указания отправителя mail не отправляет сообщения
        message.setFrom(sender);

        mailSender.send(message);
    }
}