package xyz.rpolnx.spring_bank.account.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.FanoutExchange;
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
    public FanoutExchange accountExchange() {
        return new FanoutExchange(servicesExchange);
    }

    @Bean
    public Binding creditCardBinding(FanoutExchange servicesExchange, Queue creditCardQueue) {
        return BindingBuilder.bind(creditCardQueue)
                .to(servicesExchange);
    }

    @Bean
    public Binding overdraftBinding(FanoutExchange servicesExchange, Queue overdraftQueue) {
        return BindingBuilder.bind(overdraftQueue)
                .to(servicesExchange);
    }
}
