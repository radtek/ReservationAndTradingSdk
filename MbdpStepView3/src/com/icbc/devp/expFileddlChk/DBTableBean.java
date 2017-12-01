package com.icbc.devp.expFileddlChk;

import java.util.HashMap;
import java.util.HashSet;

public class DBTableBean {

	// 数据库PCKG名称
	private String pckgName = null;

	// 数据库表对应的存储过程Procedure名称 �?
	private String procedureName = null;
	// 操作类型�?SELECT 查询；INSERT 插入；UPDATE 更新；DELETE 删除
	private String operType;

	public String getProcedureName() {
		return procedureName;
	}

	public void setProcedureName(String procedureName) {
		this.procedureName = procedureName;
	}

	public String getOperType() {
		return operType;
	}

	public void setOperType(String operType) {
		this.operType = operType;
	}

	public String getPckgName() {
		return pckgName;
	}

	public void setPckgName(String pckgName) {
		this.pckgName = pckgName;
	}
	
    //自定义对象必须重�?equals() & hasCode()方法
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof DBTableBean)) {
			return false;
		}
		final DBTableBean other = (DBTableBean) o;
		if (this.operType.equals(other.getOperType())
				&& this.pckgName.equals(other.getPckgName())
				&& this.procedureName.equals(other.getProcedureName())) {
			return true;
		} else {
			return false;
		}
	}

	public int hashCode() {
		int result;
		result = (procedureName == null ? 0 : procedureName.hashCode());
		result = 37 * result + (pckgName == null ? 0 : pckgName.hashCode());
		return result;
	}

}
