package com.sysoev.t1_openschool.kafka;

import com.sysoev.t1_openschool.dto.TaskResponseDto;
import com.sysoev.t1_openschool.service.NotificationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
public class KafkaTaskConsumer {
    private final NotificationService notificationService;


    public KafkaTaskConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(id ="${spring.kafka.consumer.group-id}",
            topics = "${spring.kafka.topic}",
            containerFactory = "kafkaListenerContainerFactory")
    public void listen(TaskResponseDto task) {
        notificationService.sendTaskStatusChangeEmail(task);
    }
}