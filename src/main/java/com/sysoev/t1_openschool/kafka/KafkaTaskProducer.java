package com.sysoev.t1_openschool.kafka;

import com.sysoev.t1_openschool.dto.KafkaUpdatingDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaTaskProducer {
    @Value("${kafka.topics.task-updating}")
    private String taskTopic;
    private final KafkaTemplate<String, KafkaUpdatingDto> kafkaTemplate;

    public KafkaTaskProducer(KafkaTemplate<String, KafkaUpdatingDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendChangedTaskStatus(KafkaUpdatingDto task) {
        kafkaTemplate.send(taskTopic, task);
    }
}
