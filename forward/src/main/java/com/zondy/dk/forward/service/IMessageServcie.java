package com.zondy.dk.forward.service;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: zj
 * @date: 2020/7/27
 */
public interface IMessageServcie {
     /**
      * 发送消息
      *
      * @param request
      * @author zj
      * @date 2020/7/28
      * @return java.lang.Object
      */
     Object sendMessage(HttpServletRequest request);
}
