package com.github.spafka.cep;

/**
 * 数据类接口
 * 
 * @author Administrator
 *
 */
public interface IBASE {

	/**
	 * 发送方ecu名称
	 * 
	 * @param ecu
	 */
	public void setEcu(String ecu);

	public String getEcu();

	/**
	 * 设置can 数据id 报文id/32即协议id
	 * 
	 * @param id
	 */
	public void setItemType(String id);

	/**
	 * 获取数据id
	 * 
	 * @return
	 */
	public String getItemType();

	/**
	 * @deprecated can数据数据体默认8字节 can数据项 数据题长度 默认 8 字节
	 * @param length
	 */
	public void setItemLength(int length);

	/**
	 * @deprecated can数据数据体默认8字节 获取数据题长度
	 * @return
	 */
	public int getItemLength();

}
