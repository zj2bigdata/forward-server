package com.zondy.forwardserver.rest;

import com.forwardrest.entity.RequestRabitMessage;
import com.zondy.forwardserver.service.MessageConsumer;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

/**
 * @author: zj
 * @date: 2020/7/28
 */
@Component
public class RestManager {

    private static Logger log = LoggerFactory.getLogger(RestManager.class);


    @Autowired
    RestTemplate restTemplate;

    @Value("${zuulUrl}")
    private String zuulUrl;

    private String transRestUrl(String apiUrl, Map<String, String[]> paramsMap) {
        String resultUrl;
        StringBuilder paramsStr = new StringBuilder();
        paramsMap.forEach((k, v) -> paramsStr.append(k).append("=").append(v[0]).append("&"));
        if (StringUtils.isNotBlank(paramsStr.toString())) {
            resultUrl = zuulUrl + apiUrl + "?" + paramsStr.substring(0, paramsStr.lastIndexOf("&"));
        } else {
            resultUrl = zuulUrl + apiUrl;
        }
        try {
            //对路径上含有中文的进行解码
            resultUrl = URLDecoder.decode(resultUrl, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if(!resultUrl.contains("api/msg/msg/unread")){
            log.info("REST : URL={}", resultUrl);
        }
        return resultUrl;
    }

    public String restGetRabitMessage(RequestRabitMessage restMessage) {
        String apiUrl = restMessage.getRequestUrl();
        //处理请求头
        Map<String, String> headerMap = restMessage.getHeaderMap();
        HttpHeaders requestHeaders = new HttpHeaders();
        headerMap.forEach(requestHeaders::add);
        HttpEntity<String> requestEntity = new HttpEntity<>(null, requestHeaders);
        ResponseEntity<String> exchange = restTemplate.exchange(transRestUrl(apiUrl, restMessage.getParameterMap()), HttpMethod.GET, requestEntity, String.class);
        return exchange.getBody();
    }

    public String restPostRabitMessage(RequestRabitMessage restMessage) {
        String apiUrl = restMessage.getRequestUrl();
        //处理请求头
        Map<String, String> headerMap = restMessage.getHeaderMap();
        HttpHeaders requestHeaders = new HttpHeaders();
        headerMap.forEach(requestHeaders::add);
        HttpEntity<String> requestEntity = new HttpEntity<>(restMessage.getBody(), requestHeaders);
        ResponseEntity<String> exchange = restTemplate.exchange(transRestUrl(apiUrl, restMessage.getParameterMap()), HttpMethod.POST, requestEntity, String.class);
        return exchange.getBody();
    }

    public String restPutRabitMessage(RequestRabitMessage restMessage) {
        String apiUrl = restMessage.getRequestUrl();
        //处理请求头
        Map<String, String> headerMap = restMessage.getHeaderMap();
        HttpHeaders requestHeaders = new HttpHeaders();
        headerMap.forEach(requestHeaders::add);
        HttpEntity<String> requestEntity = new HttpEntity<>(restMessage.getBody(), requestHeaders);
        ResponseEntity<String> exchange = restTemplate.exchange(transRestUrl(apiUrl, restMessage.getParameterMap()), HttpMethod.PUT, requestEntity, String.class);
        return exchange.getBody();
    }

    public String restPatchRabitMessage(RequestRabitMessage restMessage) {
        String apiUrl = restMessage.getRequestUrl();
        //处理请求头
        Map<String, String> headerMap = restMessage.getHeaderMap();
        HttpHeaders requestHeaders = new HttpHeaders();
        headerMap.forEach(requestHeaders::add);
        HttpEntity<String> requestEntity = new HttpEntity<>(restMessage.getBody(), requestHeaders);
        ResponseEntity<String> exchange = restTemplate.exchange(transRestUrl(apiUrl, restMessage.getParameterMap()), HttpMethod.PATCH, requestEntity, String.class);
        return exchange.getBody();
    }

    public String restDeleteRabitMessage(RequestRabitMessage restMessage) {
        String apiUrl = restMessage.getRequestUrl();
        //处理请求头
        Map<String, String> headerMap = restMessage.getHeaderMap();
        HttpHeaders requestHeaders = new HttpHeaders();
        headerMap.forEach(requestHeaders::add);
        HttpEntity<String> requestEntity = new HttpEntity<>(restMessage.getBody(), requestHeaders);
        ResponseEntity<String> exchange = restTemplate.exchange(transRestUrl(apiUrl, restMessage.getParameterMap()), HttpMethod.DELETE, requestEntity, String.class);
        return exchange.getBody();
    }
}
