package com.zondy.dk.forward.taskmgr;

/**
 * @author: zj
 * @date: 2020/7/27
 */
public class TaskFuture<T> {
	private Object lock = new Object();
	private boolean finished;
	private T t;
	private Object result;
	
	TaskFuture(T t){
		this.t = t;
	}
	
	public void waitTaskFinish() {
		if(finished) {
			return ;
		}
		synchronized(lock) {
			try {
				lock.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public boolean isFinished() {
		return finished;
	}
	
	protected void setFinished(boolean finished) {
		this.finished = finished;
		synchronized(lock) {
			lock.notifyAll();
		}
	}

	public T getT() {
		return t;
	}

	public Object getResult() {
		return result;
	}

	protected void setResult(Object result) {
		this.result = result;
	}
}
