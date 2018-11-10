package com.github.spafka.cep;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

@Data
public class BASEDTO implements IBASE, Serializable {

    private static final long serialVersionUID = 1L;

    protected static Logger logger = LoggerFactory.getLogger(BASEDTO.class);
    public String ecu;
    public String model;
    public String itemType;
    public int itemLength;
    public String seconds1970;
    public String microSecond;
    public String vin;
    public String hexMetaData;
    public String deviceId;

    public String getEcu() {
        return ecu;
    }

    public void setEcu(String ecu) {
        this.ecu = ecu;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public int getItemLength() {
        return itemLength;
    }

    public void setItemLength(int itemLength) {
        this.itemLength = itemLength;
    }

    public String getSeconds1970() {
        return seconds1970;
    }

    public void setSeconds1970(String seconds1970) {
        this.seconds1970 = seconds1970;
    }

    public String getMicroSecond() {
        return microSecond;
    }

    public void setMicroSecond(String microSecond) {
        this.microSecond = microSecond;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getHexMetaData() {
        return hexMetaData;
    }

    public void setHexMetaData(String hexMetaData) {
        this.hexMetaData = hexMetaData;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

}
