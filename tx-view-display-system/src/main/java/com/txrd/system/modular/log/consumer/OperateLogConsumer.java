package com.txrd.system.modular.log.consumer;

import com.rabbitmq.client.Channel;
import com.txrd.base.constant.KafkaConstants;
import com.txrd.common.config.RabbitMQConfig;
import com.txrd.common.pojo.OperateLogDTO;
import com.txrd.system.modular.log.service.IOperateLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OperateLogConsumer {

    @Autowired
    private IOperateLogService operateLogService;

    /**
     * 监听操作日志 Topic
     * concurrency = 3 表示启动3个线程并发消费，提升吞吐量
     */
//    @KafkaListener(topics = KafkaConstants.TOPIC_OPERATE_LOG, concurrency = "3")
    public void consumeLog(OperateLogDTO logDTO) {
        try {
            log.debug("接收到操作日志: traceId={}", logDTO.getTraceId());

            // 调用 Service 保存数据库
            // 注意：这里不需要 @Async，因为 Kafka 监听器本身就是异步线程池执行的
            operateLogService.saveLog(logDTO);

        } catch (Exception e) {
            // 消费失败处理策略：
            // 1. 记录错误日志，人工排查
            // 2. 发送到死信队列 (DLQ)
            log.error("消费操作日志失败，traceId: {}, error: {}", logDTO.getTraceId(), e.getMessage(), e);
        }
    }

    @RabbitListener(queues = RabbitMQConfig.BUSINESS_QUEUE)
    public void processLog(OperateLogDTO operateLogDTO, Channel channel, Message message)  {
        try {
            log.debug("接收到操作日志: traceId={}", operateLogDTO.getTraceId());
            operateLogService.saveLog(operateLogDTO);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            // 消费失败处理策略：
            // 1. 记录错误日志，人工排查
            // 2. 发送到死信队列 (DLQ)
            log.error("消费操作日志失败，traceId: {}, error: {}", operateLogDTO.getTraceId(), e.getMessage(), e);
        }
    }

    @RabbitListener(queues = RabbitMQConfig.DEAD_LETTER_QUEUE)
    public void processDeadLog(OperateLogDTO operateLogDTO, Channel channel, Message message)  {
        try {
            log.debug("接收到死信操作日志: traceId={}", operateLogDTO.getTraceId());
            operateLogService.saveLog(operateLogDTO);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            // 消费失败处理策略：
            // 1. 记录错误日志，人工排查
            // 2. 发送到死信队列 (DLQ)
            log.error("消费死信操作日志失败，traceId: {}, error: {}", operateLogDTO.getTraceId(), e.getMessage(), e);
        }
    }
}
