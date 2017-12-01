package com.icbc.devp.bean.file;

import com.icbc.devp.tool.util.StringUtil;

/**TODO:���˿�ܵ��ļ�����BIN*/
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
	
	/**TODO:��õ���������*/
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


	private String column;	//�ֶ�����,ȫ��Сд
	private int    width;	//�ֶγ���
	private String desc;	//�ֶ�����
	private boolean isFilter;	//�Ƿ񱻹��˵���Ĭ��false
	private String special;		//���ⷽ��
	private String consValue;	//�̶�ֵ	//��Ӧdefault
	private boolean isTerminate;	//�Ƿ����һ���ַ����ָ������õ�
}
