package com.sysoev.t1_openschool.kafka;

import com.sysoev.t1_openschool.dto.TaskResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaTaskProducer {
    @Value("${spring.kafka.topic}")
    private String taskTopic;
    private final KafkaTemplate kafkaTemplate;

    public KafkaTaskProducer(KafkaTemplate<String, TaskResponseDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendChangedTaskStatus(TaskResponseDto Task){
        try {
            kafkaTemplate.send(taskTopic, Task);
            kafkaTemplate.flush();
        }catch (Exception ex){
            log.error(ex.getMessage(), ex);
        }
    }
}
