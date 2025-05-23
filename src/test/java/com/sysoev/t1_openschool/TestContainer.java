package com.sysoev.t1_openschool;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public class TestContainer {

    @Container
    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17");

    @DynamicPropertySource
    public static void setup(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Container
    static KafkaContainer kafkaProducerContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.2.1"));

    @DynamicPropertySource
    static void kafkaProperties(DynamicPropertyRegistry registry) {
        String kafkaUrl = kafkaProducerContainer.getBootstrapServers();

        registry.add("spring.kafka.bootstrap-servers", () -> kafkaUrl);
        registry.add("kafka.bootstrap-servers", () -> kafkaUrl);
        registry.add("kafka.consumer.group-id", () -> "test-group");
        registry.add("kafka.topics.task-updating", () -> "test-topic");
    }

}
