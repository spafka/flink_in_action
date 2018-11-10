package com.github.spafka.cep;

import lombok.ToString;

import java.io.Serializable;

@ToString(callSuper = true)
public class CallDTO extends BASEDTO implements Serializable {
    public  int compareId=1;

    @Override
    public int getCompareId() {
        return compareId;
    }
}