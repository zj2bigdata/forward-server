package com.forwardrest.entity;

import java.io.Serializable;

/**
 * Created by ll on 2018/5/15.
 */
public class Ret<T> implements Serializable{
    private static final long serialVersionUID = 1L;
    private Integer status;
    private String error;
    private T t;

    public Ret(){}

    public Ret(T t){
        this.status= Status.OK.getStatus();
        this.error = Status.OK.getError();
        this.t = t;
    }

    public Ret(Status status){
        this.status = status.getStatus();
        this.error = status.getError();
    }

    public Ret(Status status, T t){
        this.status = status.getStatus();
        this.error = status.getError();
        this.t = t;
    }

    public Ret(Integer status, String error) {
        this.status = status;
        this.error = error;
    }

    public Ret(Integer status, String error, T t) {
        this.status = status;
        this.error = error;
        this.t = t;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }
}
