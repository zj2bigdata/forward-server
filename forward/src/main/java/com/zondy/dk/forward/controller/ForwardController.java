package com.zondy.dk.forward.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.forwardrest.entity.Ret;
import com.zondy.dk.forward.service.IMessageServcie;
import com.zondy.dk.forward.taskmgr.TaskManager;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: zj
 * @date: 2020/7/27
 */
@RestController
public class ForwardController {

    private static Logger log = LoggerFactory.getLogger(TaskManager.class);

    @Autowired
    private IMessageServcie messageServcie;

    //    @RequestMapping(value = "/**//{path:.+}")
    @RequestMapping(value = "/api/**//{path:.+}")
    @ResponseBody
    public JSONObject forwardHttpURL(HttpServletRequest request, @PathVariable String path) {
        //屏蔽内网跳转和登录接口
//        if (StringUtils.equals("forward", path) || StringUtils.equals("login", path)) {
        if (StringUtils.equals("forward", path)) {
            return JSONObject.parseObject(JSON.toJSONString(new Ret<>(404, "无访问权限！")));
        }
        long start = System.currentTimeMillis();
        JSONObject jsonObject = JSONObject.parseObject(messageServcie.sendMessage(request).toString());
        if (StringUtils.equals("unread", path)) {
            return jsonObject;
        }
        long end = System.currentTimeMillis();
        log.info("URL : {}", request.getRequestURL().toString());
        log.info("TIME (ms):{}", (end - start));
        return jsonObject;
    }
}
