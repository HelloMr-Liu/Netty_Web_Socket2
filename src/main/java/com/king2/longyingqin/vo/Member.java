package com.king2.longyingqin.vo;

public class Member {
    public  Member(String id,String time){
        this.id=id;
        this.time=time;
    }
    private String id;      //用户id
    private String time;    //用户连接的时间

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
    