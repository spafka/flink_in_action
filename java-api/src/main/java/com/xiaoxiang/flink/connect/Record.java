package com.xiaoxiang.flink.connect;

/**
 * Created by yuhailin on 2018/3/1.
 */
public class Record {

    private String bizName;     // 业务名字

    private int bizId;        // 业务方id

    private long timestamp;    // 日志产生时间

    private int attr;        // 属性1

    private String data;        // 原始属性描述

    public String getBizName() {
        return bizName;
    }

    public void setBizName(String bizName) {
        this.bizName = bizName;
    }

    public int getBizId() {
        return bizId;
    }

    public void setBizId(int bizId) {
        this.bizId = bizId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getAttr() {
        return attr;
    }

    public void setAttr(int attr) {
        this.attr = attr;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
