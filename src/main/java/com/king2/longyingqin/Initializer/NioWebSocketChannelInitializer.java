package com.king2.longyingqin.Initializer;

import com.king2.longyingqin.handler.NioWebSocketHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * ================================================================
 * 说明：创建一个非阻塞网络通信通道的初始化类
 * <p>
 * 作者          时间                    注释
 * 刘梓江	2020/4/21  17:51            创建
 * =================================================================
 **/
public class NioWebSocketChannelInitializer extends ChannelInitializer<SocketChannel> {
    private static Logger logger = LoggerFactory.getLogger(NioWebSocketChannelInitializer.class);
    @Override //注意SocketChannel是netty的  不是nio的SocketChannel
    protected void initChannel(SocketChannel ch) throws Exception {
        logger.info("收到新连接：");
        ch.pipeline().addLast("logging",new LoggingHandler("DEBUG"));//设置log监听器，并且日志级别为debug，方便观察运行流程
        ch.pipeline().addLast("http-codec",new HttpServerCodec());          //设置解码器
        ch.pipeline().addLast("aggregator",new HttpObjectAggregator(65536));//聚合器，使用websocket会用到 将HTTP消息的多个部分合成一条完整的HTTP消息
        ch.pipeline().addLast("http-chunked",new ChunkedWriteHandler());    //用于大数据的分区传输 向客户端发送HTML5文件
        // 进行设置心跳检测
        ch.pipeline().addLast(new IdleStateHandler(60,30,60*30, TimeUnit.SECONDS));
        //设置Socket 请求路径
        ch.pipeline().addLast(new WebSocketServerProtocolHandler("/liuzijiangSocket", "WebSocket", true, 65536 * 10));
        ch.pipeline().addLast("handler",new NioWebSocketHandler());         //自定义的业务handler


    }
}


