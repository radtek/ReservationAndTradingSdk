package com.icbc.devp.bean.db;

public class MbdpCtlBean {

	public MbdpCtlBean(){
		
	}
	
	public MbdpCtlBean(	 String app,
						 String cycle,
						 String datasource,
						 String desc,
						 String workdate,
						 String monRunDay,
						 String status,
						 String pretType){
		this.app = app;
		this.cycle = cycle;
		this.datasource = datasource;
		this.desc = desc;
		this.workdate = workdate;
		this.monRunDay = monRunDay;
		this.status = status;
		this.pretType = pretType;
	}
	
	public String getApp() {
		return app;
	}
	public void setApp(String app) {
		this.app = app;
	}
	public String getCycle() {
		return cycle;
	}
	public void setCycle(String cycle) {
		this.cycle = cycle;
	}
	public String getDatasource() {
		return datasource;
	}
	public void setDatasource(String datasource) {
		this.datasource = datasource;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getName(){
		return app+"_"+cycle+"_"+datasource+"("+desc+")";
	}
	
	

	public String getWorkdate() {
		return workdate;
	}


	public void setWorkdate(String workdate) {
		this.workdate = workdate;
	}


	public String getMonRunDay() {
		return monRunDay;
	}


	public void setMonRunDay(String monRunDay) {
		this.monRunDay = monRunDay;
	}


	public String getStatus() {
		return status;
	}

	public String getStatusDesc(){
		if("1".equals(this.status)){
			return "1-初始";
		}
		if("2".equals(this.status)){
			return "2-运行中";
		}
		if("4".equals(this.status)){
			return "4-中断";
		}
		return "";
	}

	public void setStatus(String status) {
		this.status = status;
	}


	public String getPretType() {
		return pretType;
	}


	public void setPretType(String pretType) {
		this.pretType = pretType;
	}

	public MbdpCtlBean clone(){
		MbdpCtlBean tmp = new MbdpCtlBean(this.app,this.cycle,this.datasource,this.desc,this.workdate,this.monRunDay,this.status,this.pretType);
		return tmp;
	}


	private String app;
	private String cycle;
	private String datasource;
	private String desc;
	private String workdate;
	private String monRunDay;
	private String status;
	private String pretType;
	
}
