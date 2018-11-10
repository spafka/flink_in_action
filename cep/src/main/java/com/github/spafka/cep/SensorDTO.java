package com.github.spafka.cep;

import lombok.ToString;

import java.io.Serializable;
@ToString(callSuper = true)
public class SensorDTO extends BASEDTO implements Serializable {
    public int compareId=3;

    @Override
    public int getCompareId() {
        return compareId;
    }
}
