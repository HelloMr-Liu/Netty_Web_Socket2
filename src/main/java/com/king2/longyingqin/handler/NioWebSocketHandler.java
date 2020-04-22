package com.king2.longyingqin.handler;

import com.king2.longyingqin.Initializer.NioWebSocketChannelInitializer;
import com.king2.longyingqin.configuration.NettyConfiguration;
import com.king2.longyingqin.utils.JsonUtils;
import com.king2.longyingqin.vo.Member;
import com.king2.longyingqin.vo.SendContent;
import com.king2.longyingqin.vo.SystemResult;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ================================================================
 * 说明：自定义的处理器 通过每次网络通信到当前处理器上 处理对应的 业务
 * <p>
 * 作者          时间                    注释
 * 刘梓江	2020/4/21  17:59            创建
 * =================================================================
 **/
public class NioWebSocketHandler  extends SimpleChannelInboundHandler<Object> {

    private static Logger logger = LoggerFactory.getLogger(NioWebSocketHandler.class);
    private static final String WEB_SOCKET_URL = "ws://localhost:9090/myWebSocket";
    private WebSocketServerHandshaker handshaker;


    //客户端与服务器建立连接的时候触发，
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //添加当前客户端连接相关的一些信息
        NettyConfiguration.group.add(ctx.channel());
        int countMember= NettyConfiguration.onlineCount.incrementAndGet();
        //添加对应的用户id及对应的连接时间
        String str = "yyy-MM-dd HH:mm:ss";
        NettyConfiguration.memberMap.put(ctx.channel().id().asShortText(),new SimpleDateFormat(str).format(new Date()));

        logger.info("客户端与服务端连接开启...当前在先人数："+countMember+"====当前连接的ID "+ctx.channel().id());
    }

    //客户端与服务器关闭连接的时候触发，
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //移除掉当前客户端连接相关的一些信息
        NettyConfiguration.group.remove(ctx.channel());
        int countMember= NettyConfiguration.onlineCount.decrementAndGet();
        NettyConfiguration.memberMap.remove(ctx.channel().id().asShortText());

        //广播一次线程人数信息
        refreshOnlineMemberMessage();
        logger.info("客户端与服务端连接关闭...当前在先人数："+countMember);
    }

    //服务端接收客户端发送过来的数据结束之后调用
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
        logger.info("客户端发送数据完毕...");
    }

    //服务器接受客户端的数据信息,websocket请求的核心方法
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.info("服务器收到的数据...："+msg);

        //请求消息由HTTP协议承载，所以它是一个HTTP消息，执行handleHttpRequest方法来处理WebSocket握手请求。
        if (msg instanceof FullHttpRequest){
            //当如果是浏览器访问栏就会进入到该方法中
            handleHttpRequest(ctx, (FullHttpRequest) msg);
            logger.info("请求消息由HTTP协议承载");
        // WebSocket接入
        // 客户端通过文本框提交请求消息给服务端，WebSocketServerHandler接收到的是已经解码后的WebSocketFrame消息。
        }else if (msg instanceof  WebSocketFrame){
            //处理websocket客户端的消息
            handlerWebSocketFrame(ctx, (WebSocketFrame) msg);
        }
    }

    //处理客户端与服务端之前的websocket业务
    private void handlerWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
        //判断是否是关闭websocket的指令
        if (frame instanceof CloseWebSocketFrame) {
            handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
        }
        //判断是否是ping消息
        if (frame instanceof PingWebSocketFrame) {
            ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
            return;
        }
        //判断是否是二进制消息，如果是二进制消息，抛出异常
        if (!(frame instanceof TextWebSocketFrame)) {
            logger.info("目前我们不支持二进制消息");
            throw new RuntimeException("【" + this.getClass().getName() + "】不支持消息");
        }
        //获取客户端向服务端发送的消息
        String message = ((TextWebSocketFrame) frame).text();
        logger.info("服务端收到客户端的消息====>>>" + message);
        //群发，服务端向每个连接上来的客户端群发消息
        sendAllMessage(ctx,message);
    }

    //发送群消息,此时其他客户端也能收到群消息
    private  void sendAllMessage(ChannelHandlerContext ctx,String message){
        if(!message.equals("GET_MEMBER_ONLINE")){
            SendContent sendContent = JsonUtils.jsonToPojo(message, SendContent.class);
            String timeFormat = new SimpleDateFormat("yyy-MM-dd HH:mm:ss").format(new Date());
            sendContent.setTime(timeFormat);
            //获取目标用户id
            String targetMemberId=sendContent.getId();
            //将发送者id存储到sendContent中
            sendContent.setId(ctx.channel().id().asShortText());
            sendMessageByMemberId(JsonUtils.objectToJson(sendContent),targetMemberId);
        }else{
            refreshOnlineMemberMessage(ctx.channel().id().asShortText());
        }
    }
    //给固定的人发消息
    private  void sendMessageByMemberId(String sendContent,String targetMemberId) {
        NettyConfiguration.group.iterator().forEachRemaining(
            e->{
                if(e.id().asShortText().equals(targetMemberId)){
                    e.writeAndFlush(new TextWebSocketFrame(sendContent));
                }
            }
        );
    }
    //广播一次在线人数信息
    public void refreshOnlineMemberMessage(String ... currMemberId){
        //连接后一个群发消息
        SystemResult result=new SystemResult();
        result.setCurrMemberId(currMemberId.length>=1?currMemberId[0]:"");
        result.setNumberOnline(NettyConfiguration.group.size());
        List<Member> onlineMemberList = result.getOnlineMemberList();
        NettyConfiguration.memberMap.entrySet().forEach(
            (e2)->{
                onlineMemberList.add(new Member(e2.getKey(),e2.getValue()));
            }
        );
        //连接成功后广播一次消息
        String s = JsonUtils.objectToJson(result);
        TextWebSocketFrame frame=new TextWebSocketFrame(s);
        NettyConfiguration.group.writeAndFlush(frame);
    }

    //唯一的一次http请求，用于创建websocket
    private void handleHttpRequest(ChannelHandlerContext ctx,
                                   FullHttpRequest req) {
        //要求Upgrade为websocket，过滤掉get/Post
        if (!req.decoderResult().isSuccess()
                || (!"websocket".equals(req.headers().get("Upgrade")))) {
            //若不是websocket方式，则创建BAD_REQUEST的req，返回给客户端
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
                WEB_SOCKET_URL, null, false);
        handshaker = wsFactory.newHandshaker(req);
        if(handshaker == null){
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        }else{
            //进行连接
            handshaker.handshake(ctx.channel(), (FullHttpRequest) req);
            //拉取未发送的数据
            //TODO
        }
    }
    //服务端向客户端响应消息
    private void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req,DefaultFullHttpResponse res) {
        // 返回应答给客户端
        if (res.getStatus().code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer(res.getStatus().toString(), CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
        }
        // 如果是非Keep-Alive，关闭连接
        ChannelFuture f = ctx.channel().writeAndFlush(res);
        if (res.getStatus().code() != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }

    /**
     * 这里是保持服务器与客户端长连接  进行心跳检测 避免连接断开
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){
            IdleStateEvent stateEvent = (IdleStateEvent) evt;
            //PingWebSocketFrame ping = new PingWebSocketFrame();
            switch (stateEvent.state()){
                //读空闲（服务器端）
                case READER_IDLE:
                    logger.info("【"+ctx.channel().remoteAddress()+"】读空闲（服务器端）");
                    //ctx.writeAndFlush(ping);
                    break;
                //写空闲（客户端）
                case WRITER_IDLE:
                    logger.info("【"+ctx.channel().remoteAddress()+"】写空闲（客户端）");
                    //ctx.writeAndFlush(ping);
                    break;
                case ALL_IDLE:
                    logger.info("【"+ctx.channel().remoteAddress()+"】读写空闲");
                    break;
            }
        }
    }



    //工程出现异常的时候调用
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
        logger.info("系统出现异常...");
    }

}
    
    
    