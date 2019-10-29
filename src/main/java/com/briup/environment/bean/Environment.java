package com.briup.environment.bean;

import java.io.Serializable;
import java.sql.Timestamp;
/**
 * Simple to Introduction
 * @ProjectName:  智能家居之环境监控系统
 * @Package: com.briup.environment.bean
 * @ClassName:  Environment
 * @Description:  环境存储实体类,包括环境种类名称,发送端id,树莓派系统id<br/>
 * 		实验箱模块id,传感器地址,传感器个数,指令标号,状态,环境值,采集时间
 * @CreateDate:   2018-1-25 14:28:30
 * @author briup
 * @Version: 1.0
 */
public class Environment implements Serializable{
	//环境种类名称
	private String name;
	//发送端id
	private String srcId;
	//树莓派系统id
	private String dstId;
	//实验箱区域模块id(1-8)
	private String devId;
	//模块上传感器地址
	private String sersorAddress;
	//传感器个数
	private int count;
	//发送指令标号 3:接受数据;16:发送数据
	private String cmd;
	//状态 默认为1表示成功
	private int status;
	//环境值
	private float data;
	//采集时间
	private Timestamp gather_date;
	public Environment() {}
	public Environment(String name, String srcId, String dstId, String devId,
			String sersorAddress, int count, String cmd, int status,
			float data, Timestamp gather_date) {
		super();
		this.name = name;
		this.srcId = srcId;
		this.dstId = dstId;
		this.devId = devId;
		this.sersorAddress = sersorAddress;
		this.count = count;
		this.cmd = cmd;
		this.status = status;
		this.data = data;
		this.gather_date = gather_date;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSrcId() {
		return srcId;
	}
	public void setSrcId(String srcId) {
		this.srcId = srcId;
	}
	public String getDstId() {
		return dstId;
	}
	public void setDstId(String dstId) {
		this.dstId = dstId;
	}
	public String getDevId() {
		return devId;
	}
	public void setDevId(String devId) {
		this.devId = devId;
	}
	public String getSersorAddress() {
		return sersorAddress;
	}
	public void setSersorAddress(String sersorAddress) {
		this.sersorAddress = sersorAddress;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getCmd() {
		return cmd;
	}
	public void setCmd(String cmd) {
		this.cmd = cmd;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public float getData() {
		return data;
	}
	public void setData(float data) {
		this.data = data;
	}
	public Timestamp getGather_date() {
		return gather_date;
	}
	public void setGather_date(Timestamp gather_date) {
		this.gather_date = gather_date;
	}
	@Override
	public String toString() {
		return "Environment [name=" + name + ", srcId=" + srcId + ", dstId="
				+ dstId + ", devId=" + devId + ", sersorAddress="
				+ sersorAddress + ", count=" + count + ", cmd=" + cmd
				+ ", status=" + status + ", data=" + data + ", gather_date="
				+ gather_date + "]";
	}
}
