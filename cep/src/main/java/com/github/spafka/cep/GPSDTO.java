package com.github.spafka.cep;

import java.io.Serializable;

public class GPSDTO extends BASEDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * gps是否有效
	 */
	private int gpsok;

	/**
	 * 东西半球 1-east;0-west
	 */
	private int eastern;

	/**
	 * 南北半球 1-sourth;0-north
	 */
	private int sourthern;

	/**
	 * 经度
	 */
	private int lat;

	/**
	 * 纬度
	 */
	private int lng;

	/**
	 * 海拔高度
	 */
	private int altitude;

	public GPSDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public GPSDTO(int gpsok, int eastern, int sourthern, int lat, int lng, int altitude) {
		super();
		this.gpsok = gpsok;
		this.eastern = eastern;
		this.sourthern = sourthern;
		this.lat = lat;
		this.lng = lng;
		this.altitude = altitude;
	}

	public int getGpsok() {
		return gpsok;
	}

	public void setGpsok(int gpsok) {
		this.gpsok = gpsok;
	}

	public int getEastern() {
		return eastern;
	}

	public void setEastern(int eastern) {
		this.eastern = eastern;
	}

	public int getSourthern() {
		return sourthern;
	}

	public void setSourthern(int sourthern) {
		this.sourthern = sourthern;
	}

	public int getLat() {
		return lat;
	}

	public void setLat(int lat) {
		this.lat = lat;
	}

	public int getLng() {
		return lng;
	}

	public void setLng(int lng) {
		this.lng = lng;
	}

	public int getAltitude() {
		return altitude;
	}

	public void setAltitude(int altitude) {
		this.altitude = altitude;
	}

	@Override
	public String toString() {
		return "GPSDTO{" +
				"gpsok=" + gpsok +
				", eastern=" + eastern +
				", sourthern=" + sourthern +
				", lat=" + lat +
				", lng=" + lng +
				", altitude=" + altitude +
				", seconds1970='" + seconds1970 + '\'' +
				", microSecond='" + microSecond + '\'' +
				", vin='" + vin + '\'' +
				", deviceId='" + deviceId + '\'' +
				'}';
	}
}
