package xyz.rpolnx.spring_bank.account.config;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueueConfig {
    @Value("${customer-queue-name:customer-queue}")
    private String queueName;

    @Bean
    public Queue customerQueue() {
        return new Queue(queueName);
    }
}
