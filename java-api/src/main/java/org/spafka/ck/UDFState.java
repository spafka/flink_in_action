package org.spafka.ck;


import java.io.Serializable;

// 该类作为快照的一部分，保存用户自定义状态
public class UDFState implements Serializable {
    private long count;

    // 初始化用户自定义状态
    public UDFState() {
        count = 0L;
    }

    // 设置用户自定义状态
    public void setState(long count) {
        this.count = count;
    }

    // 获取用户自定义状态
    public long getState() {
        return this.count;
    }
}