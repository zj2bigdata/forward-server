package com.zondy.forwardserver.service;

/**
 * @author: zj
 * @date: 2020/7/27
 */
public interface IMessageServcie {
     void sendMessage(String exchange, String routingKey, Object msg);
}
