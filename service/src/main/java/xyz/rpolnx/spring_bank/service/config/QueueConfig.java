package xyz.rpolnx.spring_bank.service.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class QueueConfig {
    @Value("${credit-card-queue-name}")
    private String creditCardQueueName;
    @Value("${overdraft-queue-name}")
    private String overdraftQueueName;

    @Bean
    public Queue creditCardQueue() {
        return new Queue(creditCardQueueName);
    }

    @Bean
    public Queue overdraftQueue() {
        return new Queue(overdraftQueueName);
    }
}
