package com.github.spafka.cep;

import java.io.Serializable;

public class SensorDTO extends BASEDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 加速度 x
	 */
	private double acc_x;
	/**
	 * 加速度 y
	 */
	private double acc_y;
	/**
	 * 加速度 z
	 */
	private double acc_z;

	/**
	 * 行驶方向
	 */
	private int angle_N;

	/**
	 * 陀螺仪 x
	 */
	private int gyro_x;
	/**
	 * 陀螺仪 ｙ
	 */
	private int gyro_y;

	private int gyro_z;

	public SensorDTO() {
		super();
	}

	public SensorDTO(double acc_x, double acc_y, double acc_z, int angle_N, int gyro_x, int gyro_y, int gyro_z) {
		super();
		this.acc_x = acc_x;
		this.acc_y = acc_y;
		this.acc_z = acc_z;
		this.angle_N = angle_N;
		this.gyro_x = gyro_x;
		this.gyro_y = gyro_y;
		this.gyro_z = gyro_z;
	}

	public double getAcc_x() {
		return acc_x;
	}

	public void setAcc_x(double acc_x) {
		this.acc_x = acc_x;
	}

	public double getAcc_y() {
		return acc_y;
	}

	public void setAcc_y(double acc_y) {
		this.acc_y = acc_y;
	}

	public double getAcc_z() {
		return acc_z;
	}

	public void setAcc_z(double acc_z) {
		this.acc_z = acc_z;
	}

	public int getAngle_N() {
		return angle_N;
	}

	public void setAngle_N(int angle_N) {
		this.angle_N = angle_N;
	}

	public int getGyro_x() {
		return gyro_x;
	}

	public void setGyro_x(int gyro_x) {
		this.gyro_x = gyro_x;
	}

	public int getGyro_y() {
		return gyro_y;
	}

	public void setGyro_y(int gyro_y) {
		this.gyro_y = gyro_y;
	}

	public int getGyro_z() {
		return gyro_z;
	}

	public void setGyro_z(int gyro_z) {
		this.gyro_z = gyro_z;
	}

	@Override
	public String toString() {
		return "SensorDTO [acc_x=" + acc_x + ", acc_y=" + acc_y + ", acc_z=" + acc_z + ", angle_N=" + angle_N
				+ ", gyro_x=" + gyro_x + ", gyro_y=" + gyro_y + ", gyro_z=" + gyro_z + "]";
	}

}
