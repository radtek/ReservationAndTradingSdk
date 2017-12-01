package com.icbc.devp.bean.file;

import com.icbc.devp.tool.util.StringUtil;

/**TODO:法人框架的文件加载BIN*/
public class MscFileFieldBean {

	public MscFileFieldBean(){
		isFilter = false;
	}
	
	public String getColumn() {
		return column;
	}
	public void setColumn(String column) {
		this.column = column;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public boolean isFilter() {
		return isFilter;
	}
	public void setFilter(boolean isFilter) {
		this.isFilter = isFilter;
	}
	public String getSpecial() {
		return special;
	}
	public void setSpecial(String special) {
		this.special = special;
	}
	public String getConsValue() {
		return consValue;
	}
	public void setConsValue(String consValue) {
		this.consValue = consValue;
	}

	
	
	public boolean isTerminate() {
		return isTerminate;
	}

	public void setTerminate(boolean isTerminate) {
		this.isTerminate = isTerminate;
	}
	
	/**TODO:获得导入配置行*/
	public String getImpLine(){
		String line = "        <FIELD COLUMN=\"" + this.column.toUpperCase()+"\"";
		line = StringUtil.rightFill(line, 55, ' ') + "WIDTH=\"" + width+"\"";
		line = StringUtil.rightFill(line, 75, ' ') + "DESC=\"" + desc + "\"";
		if(special != null && !"".equals(special)){
			line += " " + special;
		}
		line += " />";
		return line;
	}


	private String column;	//字段名字,全部小写
	private int    width;	//字段长度
	private String desc;	//字段描述
	private boolean isFilter;	//是否被过滤掉，默认false
	private String special;		//特殊方法
	private String consValue;	//固定值	//对应default
	private boolean isTerminate;	//是否最后一个字符，分隔符才用到
}
