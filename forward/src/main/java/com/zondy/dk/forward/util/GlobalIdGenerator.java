package com.zondy.dk.forward.util;


/**
 * 使用雪花算法，计算全局唯一id
 * @author: zj
 * @date: 2020/7/27
 */
public class GlobalIdGenerator {

    private final static  SnowflakeIdGenerator SNOWFLAKE_ID_GENERATOR = new SnowflakeIdGenerator();

    /**
     * 生成id字符串
     * @return
     */
    public static String generate(){
        long nextId = SNOWFLAKE_ID_GENERATOR.nextId();
        return String.valueOf(nextId);
    }
}
