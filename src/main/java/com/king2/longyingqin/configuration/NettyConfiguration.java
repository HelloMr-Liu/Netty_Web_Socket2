package com.king2.longyingqin.configuration;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import javax.websocket.Session;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ================================================================
 * 说明：存储整个工程的全局配置
 * <p>
 * 作者          时间                    注释
 * 刘梓江	2020/4/21  18:04            创建
 * =================================================================
 **/
public class NettyConfiguration {
    /**
     * 存储每一个客户端接入进来时的channel对象
     */
    public static ChannelGroup group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    //存储对应连接成功的客户端ID 及对应的连接时间
    public static ConcurrentHashMap<String,String> memberMap=new ConcurrentHashMap<>();

    //高并发的情况下 多个线程操作同一个i数据，i++无法保证原子性 也无法保证数据一致性，往往会出现问题，所以引入AtomicInteger类。
    //自增：onlineCount.incrementAndGet();
    //获取：onlineCount.get();
    //自减：onlineCount.decrementAndGet();
    public static final AtomicInteger onlineCount= new AtomicInteger(0);
}
    
    
    