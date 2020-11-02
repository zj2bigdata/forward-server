package com.zondy.forwardserver.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.forwardrest.entity.RequestRabitMessage;
import com.forwardrest.entity.ResultMessage;
import com.forwardrest.entity.Ret;
import com.forwardrest.entity.Status;
import com.rabbitmq.client.Channel;
import com.zondy.forwardserver.conf.DirectRabbitMqConfig;
import com.zondy.forwardserver.rest.RestManager;
import com.zondy.forwardserver.util.GlobalIdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;


/**
 * @author: zj
 * @date: 2020/7/27
 */
@Component
public class MessageConsumer {

    private static Logger log = LoggerFactory.getLogger(MessageConsumer.class);

    @Autowired
    IMessageServcie messageServcie;

    @Value("${exchange.directOutExchange.name}")
    private String directOutExchangeName;

    @Value("${exchange.directOutExchange.routingKey}")
    private String directOutExchangeRoutingKey;

    @Autowired
    RestManager restManager;

    //监听指定的队列名称
    @RabbitListener(queues = DirectRabbitMqConfig.directQueueName)
    @RabbitHandler
    public void process(byte[] msgBody, Channel channel, Message message) throws IOException {
        RequestRabitMessage msg = JSONObject.parseObject(new String(msgBody, StandardCharsets.UTF_8), RequestRabitMessage.class);
        try {
            if (Objects.nonNull(msg)) {
//                log.info("RABBITMQ : correlationData={}", message.getMessageProperties().getHeaders().get("spring_returned_message_correlation"));
                String result;
                switch (msg.getRequestType()) {
                    case "POST":
                        result = restManager.restPostRabitMessage(msg);
                        break;
                    case "DELETE":
                        result = restManager.restDeleteRabitMessage(msg);
                        break;
                    case "PUT":
                        result = restManager.restPutRabitMessage(msg);
                        break;
                    case "PATCH":
                        result = restManager.restPatchRabitMessage(msg);
                        break;
                    default:
                        result = restManager.restGetRabitMessage(msg);
                        break;
                }
                messageServcie.sendMessage(directOutExchangeName, directOutExchangeRoutingKey, JSON.toJSONString(new ResultMessage<>(msg.getUniqueId(), result)));
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
//                log.info("RABBITMQ : 外网CorrelationData [id={}] 的请求消息已经处理!", message.getMessageProperties().getHeaders().get("spring_returned_message_correlation"));
            } else {
                log.error("RABBITMQ : msg is null!CorrelationData [id={}]", message.getMessageProperties().getHeaders().get("spring_returned_message_correlation"));
            }
        } catch (Exception e) {
            //rest请求失败发送失败原因
            messageServcie.sendMessage(directOutExchangeName, directOutExchangeRoutingKey,
                    JSON.toJSONString(new ResultMessage<>(msg.getUniqueId(), new Ret<>(Status.Error, e.getMessage()))));
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
            //消费者处理出了问题，需要告诉队列信息消费失败
//            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            log.error("RABBITMQ : get msg failed msg ={}", msg, e);
        }
    }


}
