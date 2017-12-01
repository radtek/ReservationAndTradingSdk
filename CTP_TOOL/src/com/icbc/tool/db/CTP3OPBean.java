package com.icbc.tool.db;

import java.util.HashSet;
import java.util.List;

public class CTP3OPBean {

	public String getOpStepID() {
		return opStepID;
	}

	public void setOpStepID(String opStepID) {
		this.opStepID = opStepID;
	}

	public HashSet<String> getRelJSP() {
		return relJSP;
	}

	public void setRelJSP(HashSet<String> relJSP) {
		this.relJSP = relJSP;
	}

	public HashSet<String> getRelOP() {
		return relOP;
	}

	public void setRelOP(HashSet<String> relOP) {
		this.relOP = relOP;
	}

	public HashSet<String> getRelDSR() {
		return relDSR;
	}

	public void setRelDSR(HashSet<String> relDSR) {
		this.relDSR = relDSR;
	}

	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof DBTableBean)) {
			return false;
		}
		final CTP3OPBean other = (CTP3OPBean) o;
		if (this.opStepID.equals(other.opStepID)) {
			return true;
		} else {
			return false;
		}
	}

	private String opStepID; // ctp3 op的名字标识
	private HashSet<String> relJSP = new HashSet<String>(); // ctp3 op 涉及的JSP页面
	private HashSet<String> relOP = new HashSet<String>(); // ctp3 op涉及的存储过程 OP
	private HashSet<String> relDSR = new HashSet<String>(); // ctp3 op涉及的DSR接口

}
