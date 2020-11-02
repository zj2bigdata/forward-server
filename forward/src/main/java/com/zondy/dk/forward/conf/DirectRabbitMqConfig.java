package com.zondy.dk.forward.conf;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;



/**
 * rabbitmq 配置项
 * @author 7651
 *
 */
@SpringBootConfiguration
public class DirectRabbitMqConfig {

    public static final String directQueueName = "forwardOut";


    /**
     * directExchange  properties
     */
    @Value("${exchange.directOutExchange.name}")
    private String directExchangeName;

    @Value("${exchange.directOutExchange.durable}")
    private boolean directExchangeDurable;

    @Value("${exchange.directOutExchange.autoDelete}")
    private boolean directExchangeAutoDelete;

    @Value("${exchange.directOutExchange.routingKey}")
    private String directExchangeRoutingKey;

    /**
     * 申明directExchange(直连交换机，根据路由键完全匹配进行路由消息队列)
     * @return
     */
    @Bean(name="getDirectExchange")
    public DirectExchange getDirectExchange(){
        //申明并创建直连交换机
        return new DirectExchange(directExchangeName,
                directExchangeDurable,
                directExchangeAutoDelete);
    }
    /**
     * 申明direct交换器使用的队列
     * @return
     */
    @Bean(name="getDirectQueue")
    public Queue getDirectQueue(){
        //Queue(String name, boolean durable, boolean exclusive, boolean autoDelete)
        return new Queue(directQueueName,true,false,false);
    }

    @Bean
    public Binding bindExchangeAndQueue(@Qualifier(value="getDirectExchange") DirectExchange getDirectExchange,
                                        @Qualifier(value="getDirectQueue") Queue getDirectQueue){
        return BindingBuilder.bind(getDirectQueue).to(getDirectExchange).with(directExchangeRoutingKey);
    }
}
