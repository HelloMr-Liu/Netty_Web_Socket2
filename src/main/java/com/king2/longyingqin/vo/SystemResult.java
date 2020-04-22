package com.king2.longyingqin.vo;

import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;
import java.util.List;

/**
 * ================================================================
 * 说明：当前类说说明
 * <p>
 * 作者          时间                    注释
 * 刘梓江	2020/4/22  13:24            创建
 * =================================================================
 **/

public class SystemResult {
    private Integer numberOnline;
    private String currMemberId;
    private List<Member> onlineMemberList=new ArrayList<>();

    public Integer getNumberOnline() {
        return numberOnline;
    }
    public void setNumberOnline(Integer numberOnline) {
        this.numberOnline = numberOnline;
    }
    public List<Member> getOnlineMemberList() {
        return onlineMemberList;
    }
    public void setOnlineMemberList(List<Member> onlineMemberList) {
        this.onlineMemberList = onlineMemberList;
    }
    public String getCurrMemberId() {
        return currMemberId;
    }
    public void setCurrMemberId(String currMemberId) {
        this.currMemberId = currMemberId;
    }
}
    

