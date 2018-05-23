package com.sword.dataprocess.pojo;

import javax.xml.crypto.Data;

public class DataProcess {
	private String p_id; //料件编号
	private String p_name;//品名
	private String p_guige;// 规格
	private String p_xdata;// 行动日期
	private String p_jdate;// 交货日期
	private String p_priority;// 运算优先级
	private Integer p_sourceCount;// 需求数量
	private Integer p_descCount;// 排产数量
	private String p_version; //版本号
	public String getP_id() {
		return p_id;
	}
	public void setP_id(String p_id) {
		this.p_id = p_id;
	}
	public String getP_name() {
		return p_name;
	}
	public void setP_name(String p_name) {
		this.p_name = p_name;
	}
	public String getP_guige() {
		return p_guige;
	}
	public void setP_guige(String p_guige) {
		this.p_guige = p_guige;
	}
	public String getP_xdata() {
		return p_xdata;
	}
	public void setP_xdata(String p_xdata) {
		this.p_xdata = p_xdata;
	}
	public String getP_jdate() {
		return p_jdate;
	}
	public void setP_jdate(String p_jdate) {
		this.p_jdate = p_jdate;
	}
	public Integer getP_sourceCount() {
		return p_sourceCount;
	}
	public void setP_sourceCount(Integer p_sourceCount) {
		this.p_sourceCount = p_sourceCount;
	}
	public Integer getP_descCount() {
		return p_descCount;
	}
	public void setP_descCount(Integer p_descCount) {
		this.p_descCount = p_descCount;
	}
	public String getP_version() {
		return p_version;
	}
	public void setP_version(String p_version) {
		this.p_version = p_version;
	}
	public String getP_priority() {
		return p_priority;
	}
	public void setP_priority(String p_priority) {
		this.p_priority = p_priority;
	}
	
}
