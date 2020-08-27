package xyz.rpolnx.spring_bank.customer.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueueConfig {
    @Value("${customer-queue-name}")
    private String queueName;
    @Value("${customer-exchange-name}")
    private String exchangeName;

    @Bean
    public Queue customerQueue() {
        return new Queue(queueName);
    }

    @Bean
    public DirectExchange customerExchange() {
        return new DirectExchange(exchangeName);
    }

    @Bean
    public Binding customerBinding(DirectExchange customerExchange, Queue customerQueue) {
        return BindingBuilder.bind(customerQueue)
                .to(customerExchange)
                .withQueueName();
    }
}
