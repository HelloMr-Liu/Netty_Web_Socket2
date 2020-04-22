package com.king2.longyingqin.server;

import com.king2.longyingqin.Initializer.NioWebSocketChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * ================================================================
 * 说明：创建非阻塞式网络通信服务
 * <p>
 * 作者          时间                    注释
 * 刘梓江	2020/4/21  17:39            创建
 * =================================================================
 **/
@Component
public class NioWebSocketServer {
    private static Logger logger = LoggerFactory.getLogger(NioWebSocketServer.class);
    private int port=9090;
    NioEventLoopGroup boss=new NioEventLoopGroup(); //创建主线程组
    NioEventLoopGroup work=new NioEventLoopGroup(); //创建工作线程组

    @PostConstruct //回调方法
    //基于Netty Web Socket的一个网络通信服务初始化方法
    private void init(){
        logger.info("正在启动websocket服务器");
        try {
            //创建一个独立服务类
            ServerBootstrap bootstrap=new ServerBootstrap();
            //并绑定主线程组、工作线程组
            bootstrap.group(boss,work);
            //绑定当前服务对应的是一个 NIO(非阻塞)通信通道
            bootstrap.channel(NioServerSocketChannel.class);
            //设置Socket服务请求的端口
            bootstrap.localAddress(this.port);
            //添加初始化通信信息
            bootstrap.childHandler(new NioWebSocketChannelInitializer());
            //服务器异步创建绑
            Channel channel = bootstrap.bind(port).sync().channel();
            logger.info("webSocket服务器启动成功："+channel);
            //关闭通道
            channel.closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
            logger.info("运行出错："+e);
        }finally {

        }
    }
    public void destory(){
        boss.shutdownGracefully();
        work.shutdownGracefully();
        logger.info("websocket服务器已关闭");
    }
}
    
    
    