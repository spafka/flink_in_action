package com.github.spafka.cep;

import lombok.ToString;

import java.io.Serializable;
@ToString(callSuper = true)
public class GPSDTO extends BASEDTO implements Serializable {
    public  int compareId=2;

    @Override
    public int getCompareId() {
        return compareId;
    }
}
