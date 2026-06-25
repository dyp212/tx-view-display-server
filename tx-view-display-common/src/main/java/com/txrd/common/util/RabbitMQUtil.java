package com.txrd.common.util;

import com.txrd.common.pojo.OperateLogDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class RabbitMQUtil {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     *
     */
    public void sendMessage(Object message, String exchange, String routingKey) {
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        try {
            rabbitTemplate.convertAndSend(exchange, routingKey, message, correlationData);
            log.info("消息发送成功: message={}, exchange={}, routingKey={}, correlationData={}",
                    message, exchange, routingKey, correlationData);
        } catch (Exception e) {
            log.error("消息发送异常: message={}, exchange={}, routingKey={}, correlationData={}, error={}",
                    message, exchange, routingKey, correlationData, e.getMessage());
            throw new RuntimeException("消息发送失败", e);
        }
    }

    /**
     *
     */
    public void sendDelayMessage(Object message, String exchange, String routingKey, long delayMillis) {
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        MessagePostProcessor messagePostProcessor = msg -> {
            msg.getMessageProperties().setDelayLong(delayMillis);
            return msg;
        };
        try {
            rabbitTemplate.convertAndSend(exchange, routingKey, message, messagePostProcessor, correlationData);
            log.info("延迟消息发送成功: message={}, exchange={}, routingKey={}, delay={}, correlationData={}",
                    message, exchange, routingKey, delayMillis, correlationData);
        } catch (Exception e) {
            log.error("延迟消息发送异常: message={}, exchange={}, routingKey={}, delay={}, correlationData={}, error={}",
                    message, exchange, routingKey, delayMillis, correlationData, e.getMessage());
            throw new RuntimeException("延迟消息发送失败", e);
        }
    }
}
