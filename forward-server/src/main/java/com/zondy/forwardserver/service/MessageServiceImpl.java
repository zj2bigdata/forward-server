package com.zondy.forwardserver.service;

/**
 * @author: zj
 * @date: 2020/7/27
 */

import com.zondy.forwardserver.util.GlobalIdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ConfirmCallback;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ReturnCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageServiceImpl implements IMessageServcie, RabbitTemplate.ConfirmCallback {
    private static Logger log = LoggerFactory.getLogger(MessageServiceImpl.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void sendMessage(String exchange, String routingKey, Object msg) {
        //消息发送失败返回到队列中, yml需要配置 publisher-returns: true
        rabbitTemplate.setMandatory(true);
        //消息消费者确认收到消息后，手动ack回执
        rabbitTemplate.setConfirmCallback(this);
        //发送消息
        CorrelationData correlationData = new CorrelationData(GlobalIdGenerator.generate());
        rabbitTemplate.convertAndSend(exchange, routingKey, msg, correlationData);
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
//        log.info("RABBITMQ : 外网forward服务已接收请求响应消息：" + correlationData);
//        log.info("RABBITMQ : 确认结果：" + ack);
//        log.info("失败原因：" + cause);
    }
}
