package com.sysoev.t1_openschool.kafka;

import com.sysoev.t1_openschool.dto.KafkaUpdatingDto;
import com.sysoev.t1_openschool.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class KafkaTaskConsumer {
    private final NotificationService notificationService;
    private final Logger log = LoggerFactory.getLogger(KafkaTaskConsumer.class);

    public KafkaTaskConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }


    @KafkaListener(
            topics = "${kafka.topics.task-updating}",
            groupId = "${kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listen(List<KafkaUpdatingDto> messages, Acknowledgment acknowledgment) {
        try {
            for (KafkaUpdatingDto dto : messages) {
                notificationService.sendTaskStatusChangeEmail(dto);
            }
            acknowledgment.acknowledge();
        } catch (Exception e) {
            log.error("Ошибка чтения {}", e.getMessage());
        }
    }
}