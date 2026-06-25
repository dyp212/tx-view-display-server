package com.txrd.common.annotation.runner;

import com.txrd.base.constant.KafkaConstants;
import com.txrd.common.config.RabbitMQConfig;
import com.txrd.common.pojo.OperateLogDTO;
import com.txrd.common.util.RabbitMQUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class OperateLogProducer {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * kafka发送操作日志消息
     */
    public void sendLog2Kafka(OperateLogDTO logDTO) {
        try {
            // 使用 userId 作为 Key，保证同一用户的日志有序性（可选）
            String key = logDTO.getUserId() != null ? logDTO.getUserId() : "unknown";

//            kafkaTemplate.send(KafkaConstants.TOPIC_OPERATE_LOG, key, logDTO);
            log.debug("操作日志已发送至 Kafka: traceId={}", logDTO.getTraceId());
        } catch (Exception e) {
            // 生产环境建议记录到本地文件或监控报警，不要抛出异常影响主业务
            log.error("发送操作日志到 Kafka 失败", e);
        }
    }

    /**
     * rabbit发送操作日志消息
     */
    public void sendLog2Rabbit(OperateLogDTO logDTO) {
        // 使用 userId 作为 Key，保证同一用户的日志有序性（可选）
//            CorrelationData correlationData = new CorrelationData(logDTO.getUserId() != null ? logDTO.getUserId() : "unknown");
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        try {
            rabbitTemplate.convertAndSend(RabbitMQConfig.BUSINESS_EXCHANGE, RabbitMQConfig.BUSINESS_KEY, logDTO, correlationData);
            log.info("消息发送rabbit成功: message={}, exchange={}, routingKey={}, correlationData={}",
                    logDTO, RabbitMQConfig.BUSINESS_EXCHANGE, RabbitMQConfig.BUSINESS_KEY, correlationData);
        } catch (Exception e) {
            // 生产环境建议记录到本地文件或监控报警，不要抛出异常影响主业务
            log.error("发送操作日志到 rabbit 失败: message={}, exchange={}, routingKey={}, correlationData={}, error={}",
                    logDTO, RabbitMQConfig.BUSINESS_EXCHANGE, RabbitMQConfig.BUSINESS_KEY, correlationData, e.getMessage());
        }
    }
}
