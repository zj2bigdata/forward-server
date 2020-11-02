package com.zondy.dk.forward.taskmgr;

import com.alibaba.fastjson.JSONObject;
import com.forwardrest.entity.RequestRabitMessage;
import com.forwardrest.entity.ResultMessage;
import com.rabbitmq.client.Channel;
import com.zondy.dk.forward.conf.DirectRabbitMqConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author: zj
 * @date: 2020/7/27
 */
@Component
public class TaskManager implements RabbitTemplate.ConfirmCallback {

    private static Logger log = LoggerFactory.getLogger(TaskManager.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    List<TaskEventListener> listeners = new CopyOnWriteArrayList<TaskEventListener>();
    private Map<String, TaskFuture<?>> tasks = new ConcurrentHashMap<String, TaskFuture<?>>();

    public TaskManager() {
        addListener(new TaskFutureHandler());
    }

    public boolean addListener(TaskEventListener lis) {
        return this.listeners.add(lis);
    }

    /**
     * 监听指定的队列名称
     */

    @RabbitListener(queues = DirectRabbitMqConfig.directQueueName)
    @RabbitHandler
    public void process(String msg, Channel channel, org.springframework.amqp.core.Message message) throws IOException {
        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
            ResultMessage resultMessage = JSONObject.parseObject(msg, ResultMessage.class);
            if (Objects.nonNull(resultMessage)) {
                for (TaskEventListener listener : listeners) {
                    listener.handle(resultMessage);
                }
            }
        } catch (Exception e) {
            //消费者处理出了问题，需要告诉队列信息消费失败
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            log.info("RABBITMQ : get msg failed msg = " + msg);
        }
    }

    /**
     * 发送消息并等待回执
     *
     * @param exchange
     * @param routingKey
     * @param msg
     * @return com.zondy.dk.forward.taskmgr.TaskFuture<com.forwardrest.entity.RequestRabitMessage>
     * @author zj
     * @date 2020/7/28
     */
    public TaskFuture<RequestRabitMessage> executeTask(String exchange, String routingKey, RequestRabitMessage msg) {
        MessageProperties messageProperties = new MessageProperties();
        //设置消息是否持久化。Persistent表示持久化，Non-persistent表示不持久化
        messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
        messageProperties.setContentType(StandardCharsets.UTF_8.name());
        org.springframework.amqp.core.Message message = new Message(JSONObject.toJSONString(msg).getBytes(), messageProperties);
        //消息发送失败返回到队列中, yml需要配置 publisher-returns: true
        rabbitTemplate.setMandatory(true);
        //消息消费者确认收到消息后，手动ack回执
        rabbitTemplate.setConfirmCallback(this);
        CorrelationData correlationData = new CorrelationData(msg.getUniqueId());
        //发送消息
        rabbitTemplate.convertAndSend(exchange, routingKey, message, correlationData);
        TaskFuture<RequestRabitMessage> taskFuture = new TaskFuture<>(msg);
        tasks.put(taskFuture.getT().getUniqueId(), taskFuture);
        return taskFuture;
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
//        log.info("RABBITMQ : 内网已接收到外网请求消息,唯一标识：" + correlationData);
//        log.info("RABBITMQ : 确认结果：" + ack);
//        log.info("失败原因：" + cause);
    }

    public class TaskFutureHandler implements TaskEventListener {
        @Override
        public String getName() {
            return TaskFutureHandler.class.getName();
        }

        @Override
        public void handle(ResultMessage task) {
            String taskID = task.getSessionId();
            TaskFuture<?> tf = tasks.remove(taskID);
            if (tf != null) {
                tf.setResult(task.getData());
                tf.setFinished(true);
            }
        }
    }
}
