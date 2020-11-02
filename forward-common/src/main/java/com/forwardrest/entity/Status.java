package com.forwardrest.entity;

import java.io.Serializable;

/**
 *后台处理结果统一 状态信息 Ret使用
 */
public enum Status implements Serializable{
	OK(200, "OK"), Create(201, "Create"),NoContent(204, "No Content"),
	Reset(205, "Reset Content"),
	BadReq(400,"Bad Request"), Unauth(401, "Unauthorized"),Forbidden(403,"Forbidden"),
	NotFound(404, "Not Found"),Error(500,"Internal Server Error"),
	ErrorAccount(400, "Account Error"),
	Conflict(409,"Conflict");

	private Integer status;
	private String error;

	private Status(Integer status, String error) {
		this.status = status;
		this.error = error;
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
}
