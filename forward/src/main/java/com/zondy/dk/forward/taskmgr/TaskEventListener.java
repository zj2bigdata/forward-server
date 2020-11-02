package com.zondy.dk.forward.taskmgr;


import com.forwardrest.entity.ResultMessage;

/**
 * @author: zj
 * @date: 2020/7/27
 */
public interface TaskEventListener {
	public String getName();
	public void handle(ResultMessage task);
}
