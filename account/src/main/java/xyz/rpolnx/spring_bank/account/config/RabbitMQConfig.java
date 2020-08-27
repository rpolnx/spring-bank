package xyz.rpolnx.spring_bank.account.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Value("${customer-queue-name}")
    private String customerQueueName;
    @Value("${credit-card-queue-name}")
    private String creditCardQueueName;
    @Value("${overdraft-queue-name}")
    private String overdraftQueueName;

    @Value("${service-exchange-name}")
    private String servicesExchange;

    @Value("${routing-credit-card}")
    private String creditCardRoutingKey;
    @Value("${routing-key-overdraft}")
    private String overdraftRoutingKey;

    @Bean
    public Queue customerQueue() {
        return new Queue(customerQueueName);
    }

    @Bean
    public Queue creditCardQueue() {
        return new Queue(creditCardQueueName);
    }

    @Bean
    public Queue overdraftQueue() {
        return new Queue(overdraftQueueName);
    }

    @Bean
    public TopicExchange accountExchange() {
        return new TopicExchange(servicesExchange);
    }

    @Bean
    public Binding creditCardBinding(TopicExchange servicesExchange, Queue creditCardQueue) {
        return BindingBuilder.bind(creditCardQueue)
                .to(servicesExchange)
                .with(creditCardRoutingKey);
    }

    @Bean
    public Binding overdraftBinding(TopicExchange servicesExchange, Queue overdraftQueue) {
        return BindingBuilder.bind(overdraftQueue)
                .to(servicesExchange)
                .with(overdraftRoutingKey);
    }
}
