package com.forwardrest.entity;

import java.io.Serializable;
import java.util.Map;

/**
 * @author: zj
 * @date: 2020/7/27
 */
public class RequestRabitMessage implements Serializable {
    private static final long serialVersionUID = 7162868914896201493L;
    private String uniqueId;
    private String requestType;
    private String requestUrl;
    private Map<String, String[]> parameterMap;
    private String body;
    private Map<String, String> headerMap;

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public Map<String, String[]> getParameterMap() {
        return parameterMap;
    }

    public void setParameterMap(Map<String, String[]> parameterMap) {
        this.parameterMap = parameterMap;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Map<String, String> getHeaderMap() {
        return headerMap;
    }

    public void setHeaderMap(Map<String, String> headerMap) {
        this.headerMap = headerMap;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }
}
