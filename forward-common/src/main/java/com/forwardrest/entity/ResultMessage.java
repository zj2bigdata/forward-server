package com.forwardrest.entity;

import java.io.Serializable;

/**
 * @author: zj
 * @date: 2020/7/28
 */
public class ResultMessage<T> implements Serializable {

    private static final long serialVersionUID = 5716675872705338507L;
    private String sessionId;

    private T data;

    public ResultMessage() {
    }

    public ResultMessage(String sessionId, T data) {
        this.sessionId = sessionId;
        this.data = data;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
