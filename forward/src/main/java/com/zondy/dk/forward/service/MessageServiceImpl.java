package com.zondy.dk.forward.service;

/**
 * @author: zj
 * @date: 2020/7/27
 */

import com.forwardrest.entity.RequestRabitMessage;
import com.zondy.dk.forward.conf.DirectRabbitMqConfig;
import com.zondy.dk.forward.taskmgr.TaskFuture;
import com.zondy.dk.forward.taskmgr.TaskManager;
import com.zondy.dk.forward.util.GlobalIdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: zj
 * @date: 2020/7/27
 */
@Service
public class MessageServiceImpl implements IMessageServcie {
    private static Logger log = LoggerFactory.getLogger(MessageServiceImpl.class);

    @Value("${exchange.directInExchange.name}")
    private String directExchangeName;

    @Value("${exchange.directInExchange.routingKey}")
    private String directExchangeRoutingKey;

    @Autowired
    private TaskManager taskManager;

    @Override
    public Object sendMessage(HttpServletRequest request) {
        //资源路径
        String uri = request.getRequestURI();
        //请求方式
        String method = request.getMethod();
        //请求头
        Map<String, String> headerMap = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String element = headerNames.nextElement();
            String header = request.getHeader(element);
            headerMap.put(element, header);
        }
        //请求参数
        Map<String, String[]> parameterMap = request.getParameterMap();
        //请求体
        StringBuilder body = new StringBuilder();
        try (BufferedReader br = request.getReader()) {
            String str;
            while ((str = br.readLine()) != null) {
                body.append(str);
            }
        } catch (Exception e) {
            log.info("body无数据!");
        }
        String bodyJson = body.toString();

        RequestRabitMessage requestRabitMessage = new RequestRabitMessage();
        requestRabitMessage.setRequestUrl(uri);
        requestRabitMessage.setRequestType(method);
        requestRabitMessage.setParameterMap(parameterMap);
        requestRabitMessage.setBody(bodyJson);
        requestRabitMessage.setHeaderMap(headerMap);

        requestRabitMessage.setUniqueId(GlobalIdGenerator.generate());
        //执行请求
        TaskFuture<RequestRabitMessage> taskFuture = taskManager.executeTask(directExchangeName, directExchangeRoutingKey, requestRabitMessage);
        taskFuture.waitTaskFinish();
        return taskFuture.getResult();
    }
}
