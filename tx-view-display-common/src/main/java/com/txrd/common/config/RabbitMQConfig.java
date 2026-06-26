package com.txrd.common.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableRabbit
public class RabbitMQConfig {
    // 交换机名称
    public static final String BUSINESS_EXCHANGE = "business.exchange";
    public static final String DEAD_LETTER_EXCHANGE = "dead.letter.exchange";
    // 队列名称
    public static final String BUSINESS_QUEUE = "business.queue";
    public static final String DEAD_LETTER_QUEUE = "dead.letter.queue";
    // 路由键
    public static final String BUSINESS_KEY = "business.key";
    public static final String DEAD_LETTER_KEY = "dead.letter.key";

    // 业务交换机
    @Bean
    public DirectExchange businessExchange() {
        return ExchangeBuilder.directExchange(BUSINESS_EXCHANGE)
                .durable(true)
                .build();
    }
    // 死信交换机
    @Bean
    public DirectExchange deadLetterExchange() {
        return ExchangeBuilder.directExchange(DEAD_LETTER_EXCHANGE)
                .durable(true)
                .build();
    }
    // 业务队列
    @Bean
    public Queue businessQueue() {
        Map<String, Object> args = new HashMap<>(3);
        // 消息过期时间
        args.put("x-message-ttl", 60000);
        // 队列最大长度
        args.put("x-max-length", 1000);
        // 死信交换机
        args.put("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE);
        args.put("x-dead-letter-routing-key", DEAD_LETTER_KEY);
        return QueueBuilder.durable(BUSINESS_QUEUE)
                .withArguments(args)
                .build();
    }
    // 死信队列
    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable(DEAD_LETTER_QUEUE).build();
    }
    // 业务绑定
    @Bean
    public Binding businessBinding() {
        return BindingBuilder.bind(businessQueue())
                .to(businessExchange())
                .with(BUSINESS_KEY);
    }
    // 死信绑定
    @Bean
    public Binding deadLetterBinding() {
        return BindingBuilder.bind(deadLetterQueue())
                .to(deadLetterExchange())
                .with(DEAD_LETTER_KEY);
    }
    // 消息转换器
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    // RabbitTemplate配置
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }
}
