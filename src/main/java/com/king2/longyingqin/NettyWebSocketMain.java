package com.king2.longyingqin;

import com.king2.longyingqin.server.NioWebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * ================================================================
 * 说明：在Springboot启动类里，增加Netty服务端的启动；
 * <p>
 * 作者          时间                    注释
 * 刘梓江	2020/4/22  10:29            创建
 * =================================================================
 **/
@SpringBootApplication
public class NettyWebSocketMain implements CommandLineRunner {

    @Autowired
    private NioWebSocketServer nioWebSocketServer;

    public static void main(String[] args) {
        System.out.println("启动了");
        SpringApplication.run(NettyWebSocketMain.class);
    }

    @Override
    public void run(String... args) throws Exception {
        //netty服务启动的端口号不可和SpringBoot启动类的端口号重复
        //服务停止时关闭nettyServer
        Runtime.getRuntime().addShutdownHook(new Thread(() -> nioWebSocketServer.destory()));
    }
}
    
    
    