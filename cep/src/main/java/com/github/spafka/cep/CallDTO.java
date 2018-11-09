package com.github.spafka.cep;

import java.io.Serializable;

public class CallDTO extends BASEDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 12V battery
	 */
	private int lowBattery;
	/**
	 * 内部电池低电压
	 */
	private int innerBattery;
	/**
	 * 安全气囊
	 */
	private String srs;
	/**
	 * 防盗报警
	 */
	private String burglarAlarm;
	/**
	 * call 回调
	 */
	private String callbackRequest;
	/**
	 * ICall
	 */
	private String iCall;
	/**
	 * BCall
	 */
	private String bCall;
	/**
	 * ECall
	 */
	private String eCall;
	/**
	 * 超速: 0-not overspeed; 1-overspeed;
	 */
	private String overspeed;

	/**
	 * 疲劳驾驶: 0-not fatigue; 1-fatigue;
	 */
	private String driverFatigue;

	/**
	 * 发动机状态: 0-not running; 1-running;
	 */
	private String engineRunning;

	/**
	 * acc: 0-off; 1-on;车内上电状态
	 */
	private String accOn;
	/**
	 * 发动机数据异常
	 */
	private int engineDataException;
	/**
	 * 机油压力异常
	 */
	private int engineOilPressure;
	/**
	 * 制动系统故障
	 */
	private int brakeSystemFault;
	/**
	 * Ecall 制式
	 */
	private int ecallFormat;
	/**
	 * can唤醒
	 */
	private int canWake;
	/**
	 * ecall 唤醒
	 */
	private int ecallWake;
	/**
	 * 3G唤醒
	 */
	private int thirdGenerationWake;
	/**
	 * 传感器唤醒
	 */
	private int sensorWake;
	/**
	 * 12V低电压唤醒
	 */
	private int lowBatteryWake;
	/**
	 * acc唤醒
	 */
	private int accWake;
	/**
	 * 语音拨打状态
	 */
	private int voiceCallStatus;

	public CallDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CallDTO(int lowBattery, int innerBattery, String srs, String burglarAlarm, String callbackRequest,
			String iCall, String bCall, String eCall, String overspeed, String driverFatigue, String engineRunning,
			String accOn, int engineDataException, int engineOilPressure, int brakeSystemFault, int ecallFormat,
			int canWake, int ecallWake, int thirdGenerationWake, int sensorWake, int lowBatteryWake, int accWake,
			int voiceCallStatus) {
		super();
		this.lowBattery = lowBattery;
		this.innerBattery = innerBattery;
		this.srs = srs;
		this.burglarAlarm = burglarAlarm;
		this.callbackRequest = callbackRequest;
		this.iCall = iCall;
		this.bCall = bCall;
		this.eCall = eCall;
		this.overspeed = overspeed;
		this.driverFatigue = driverFatigue;
		this.engineRunning = engineRunning;
		this.accOn = accOn;
		this.engineDataException = engineDataException;
		this.engineOilPressure = engineOilPressure;
		this.brakeSystemFault = brakeSystemFault;
		this.ecallFormat = ecallFormat;
		this.canWake = canWake;
		this.ecallWake = ecallWake;
		this.thirdGenerationWake = thirdGenerationWake;
		this.sensorWake = sensorWake;
		this.lowBatteryWake = lowBatteryWake;
		this.accWake = accWake;
		this.voiceCallStatus = voiceCallStatus;
	}

	public int getLowBattery() {
		return lowBattery;
	}

	public void setLowBattery(int lowBattery) {
		this.lowBattery = lowBattery;
	}

	public int getInnerBattery() {
		return innerBattery;
	}

	public void setInnerBattery(int innerBattery) {
		this.innerBattery = innerBattery;
	}

	public String getSrs() {
		return srs;
	}

	public void setSrs(String srs) {
		this.srs = srs;
	}

	public String getBurglarAlarm() {
		return burglarAlarm;
	}

	public void setBurglarAlarm(String burglarAlarm) {
		this.burglarAlarm = burglarAlarm;
	}

	public String getCallbackRequest() {
		return callbackRequest;
	}

	public void setCallbackRequest(String callbackRequest) {
		this.callbackRequest = callbackRequest;
	}

	public String getiCall() {
		return iCall;
	}

	public void setiCall(String iCall) {
		this.iCall = iCall;
	}

	public String getbCall() {
		return bCall;
	}

	public void setbCall(String bCall) {
		this.bCall = bCall;
	}

	public String geteCall() {
		return eCall;
	}

	public void seteCall(String eCall) {
		this.eCall = eCall;
	}

	public String getOverspeed() {
		return overspeed;
	}

	public void setOverspeed(String overspeed) {
		this.overspeed = overspeed;
	}

	public String getDriverFatigue() {
		return driverFatigue;
	}

	public void setDriverFatigue(String driverFatigue) {
		this.driverFatigue = driverFatigue;
	}

	public String getEngineRunning() {
		return engineRunning;
	}

	public void setEngineRunning(String engineRunning) {
		this.engineRunning = engineRunning;
	}

	public String getAccOn() {
		return accOn;
	}

	public void setAccOn(String accOn) {
		this.accOn = accOn;
	}

	public int getEngineDataException() {
		return engineDataException;
	}

	public void setEngineDataException(int engineDataException) {
		this.engineDataException = engineDataException;
	}

	public int getEngineOilPressure() {
		return engineOilPressure;
	}

	public void setEngineOilPressure(int engineOilPressure) {
		this.engineOilPressure = engineOilPressure;
	}

	public int getBrakeSystemFault() {
		return brakeSystemFault;
	}

	public void setBrakeSystemFault(int brakeSystemFault) {
		this.brakeSystemFault = brakeSystemFault;
	}

	public int getEcallFormat() {
		return ecallFormat;
	}

	public void setEcallFormat(int ecallFormat) {
		this.ecallFormat = ecallFormat;
	}

	public int getCanWake() {
		return canWake;
	}

	public void setCanWake(int canWake) {
		this.canWake = canWake;
	}

	public int getEcallWake() {
		return ecallWake;
	}

	public void setEcallWake(int ecallWake) {
		this.ecallWake = ecallWake;
	}

	public int getThirdGenerationWake() {
		return thirdGenerationWake;
	}

	public void setThirdGenerationWake(int thirdGenerationWake) {
		this.thirdGenerationWake = thirdGenerationWake;
	}

	public int getSensorWake() {
		return sensorWake;
	}

	public void setSensorWake(int sensorWake) {
		this.sensorWake = sensorWake;
	}

	public int getLowBatteryWake() {
		return lowBatteryWake;
	}

	public void setLowBatteryWake(int lowBatteryWake) {
		this.lowBatteryWake = lowBatteryWake;
	}

	public int getAccWake() {
		return accWake;
	}

	public void setAccWake(int accWake) {
		this.accWake = accWake;
	}

	public int getVoiceCallStatus() {
		return voiceCallStatus;
	}

	public void setVoiceCallStatus(int voiceCallStatus) {
		this.voiceCallStatus = voiceCallStatus;
	}

	@Override
	public String toString() {
		return "CallDTO [lowBattery=" + lowBattery + ", innerBattery=" + innerBattery + ", srs=" + srs
				+ ", burglarAlarm=" + burglarAlarm + ", callbackRequest=" + callbackRequest + ", iCall=" + iCall
				+ ", bCall=" + bCall + ", eCall=" + eCall + ", overspeed=" + overspeed + ", driverFatigue="
				+ driverFatigue + ", engineRunning=" + engineRunning + ", accOn=" + accOn + ", engineDataException="
				+ engineDataException + ", engineOilPressure=" + engineOilPressure + ", brakeSystemFault="
				+ brakeSystemFault + ", ecallFormat=" + ecallFormat + ", canWake=" + canWake + ", ecallWake="
				+ ecallWake + ", thirdGenerationWake=" + thirdGenerationWake + ", sensorWake=" + sensorWake
				+ ", lowBatteryWake=" + lowBatteryWake + ", accWake=" + accWake + ", voiceCallStatus=" + voiceCallStatus
				+ "]";
	}

}