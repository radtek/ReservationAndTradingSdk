package com.icbc.devp.txtdraw.base;

import javax.swing.ImageIcon;

/**TODO:保存的信息*/
public class Info {

	public Info(){
		
	}
	
	public String getStepno() {
		return stepno;
	}
	public void setStepno(String stepno) {
		this.stepno = stepno;
	}
	public String getProc() {
		return proc;
	}
	public void setProc(String proc) {
		this.proc = proc;
	}
	public String getProcDesc() {
		return procDesc;
	}
	public void setProcDesc(String procDesc) {
		this.procDesc = procDesc;
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

	public String getDataSource() {
		return dataSource;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	public String getEnableFlag() {
		return enableFlag;
	}

	public void setEnableFlag(String enableFlag) {
		this.enableFlag = enableFlag;
	}

	public String getProcType() {
		return procType;
	}

	public void setProcType(String procType) {
		this.procType = procType;
	}
	
	public String getId(){
		return this.app+"_"+this.cycle+"_"+this.dataSource+"_"+this.stepno;
	}

	public String getIdPrefix(){
		return this.app+"_"+this.cycle+"_"+this.dataSource+"_";
	}
	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	
	public ImageIcon getIcon() {
		return icon;
	}

	public void setIcon(ImageIcon icon) {
		this.icon = icon;
	}

	public boolean isStartNode(){
		return "N/A".equalsIgnoreCase(this.procType);
	}
	
	public static String getStartNodeId(){
		return "N/A";
	}

	/**TODO:返回insert语句*/
	public String getInsertSql(){
		String line = "DELETE FROM MBDP_B_STEPDEF T WHERE T.APP='"+this.app+"' AND T.CYCLE='"+this.cycle+"' AND T.DATA_SOURCE='"+this.dataSource+"' AND T.STEPNO='"+this.stepno+"';";
		line += "\r\ninsert into mbdp_b_stepdef (APP, CYCLE, DATA_SOURCE, PROG_TYPE, STEPNO, PROG, PROG_NAME, PARAMETER, DEFDATE, WEIGHT, ENABLE_FLAG)\r\n" +
		        "values('"+this.app+"', '"+this.cycle+"', '"+this.dataSource+"', '"+this.procType+"', '"+this.stepno+"', '"+this.proc+"', '"+this.procDesc+"', '"+this.parameter+"', TO_CHAR(SYSDATE,'YYYY-MM-DD'), 0,'"+this.enableFlag+"');\r\n";
		return line;
	}
	/**TODO:返回置状态的语句*/
	public String getEnableSql(){
		String line = "UPDATE MBDP_B_STEPDEF T SET T.ENABLE_FLAG='"+this.enableFlag+"' WHERE T.APP='"+this.app+"' AND T.CYCLE='"+this.cycle+"' AND T.DATA_SOURCE='"+this.dataSource+"' AND T.STEPNO='"+this.stepno+"';";
		return line;
	}
	
	private String stepno = null;		//步骤号
	private String proc = null;			//程序
	private String procDesc = null;		//程序中文名
	private String app = null;
	private String cycle = null;
	private String dataSource = null;
	private String enableFlag = null;
	private String procType = null;
	private String parameter = null;	//参数
	//对应的图标
	private ImageIcon icon;
}
