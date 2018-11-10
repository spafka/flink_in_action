package com.github.spafka.cep;

import lombok.Data;

import java.io.Serializable;

@Data
public class BASEDTO implements Serializable {

    public int compareId=0;

    public int getCompareId() {
        return compareId;
    }

    public String seconds1970;
    public String deviceId;


    public String getSeconds1970() {
        return seconds1970;
    }

    public void setSeconds1970(String seconds1970) {
        this.seconds1970 = seconds1970;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}



