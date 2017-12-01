package com.icbc.tool.db;

import java.util.HashMap;
import java.util.HashSet;

public class MBDPMatrix {
	public String getDATA_SOURCE() {
		return DATA_SOURCE;
	}

	public void setDATA_SOURCE(String dATA_SOURCE) {
		this.DATA_SOURCE = dATA_SOURCE;
	}

	public String getCYCLE() {
		return CYCLE;
	}

	public void setCYCLE(String cYCLE) {
		this.CYCLE = cYCLE;
	}

	public HashSet<String> getSteps() {
		return steps;
	}

	public void setSteps(HashSet<String> steps) {
		this.steps = steps;
	}

	public HashMap<HashMap<String, String>, Boolean> getMbdpMatrix() {
		return mbdpMatrix;
	}

	private String mbdp_ID;

	public String getMbdp_ID() {
		return mbdp_ID;
	}

	public void setMbdp_ID(String mbdp_ID) {
		this.mbdp_ID = mbdp_ID;
	}

	public void setMbdpMatrix(
			HashMap<HashMap<String, String>, Boolean> mbdpMatrix) {
		this.mbdpMatrix = mbdpMatrix;
	}

	private String DATA_SOURCE;// 数据源
	private String CYCLE; // 日批or月批
	private HashSet<String> steps = new HashSet<String>();// 步骤

	private HashMap<HashMap<String, String>, Boolean> mbdpMatrix = new HashMap<HashMap<String, String>, Boolean>(); // 全部步骤的前后关联关系

	public void initMatrix() {
		for (String step1 : this.steps) {
			for (String step2 : this.steps) {
				if (step1.equals(step2))
					continue;
				HashMap<String, String> stepPair = new HashMap<String, String>();
				stepPair.put(step1, step2);
				this.mbdpMatrix.put(stepPair, false);
			}
		}
	}

	public void addMatrix(String stepno_p, String stepno) {
		HashMap<String, String> stepPair = new HashMap<String, String>();
		stepPair.put(stepno_p, stepno);
		this.mbdpMatrix.remove(stepPair);
		this.mbdpMatrix.put(stepPair, true);
	}

	public void genMatrix() {
		for (String step1 : this.steps) {
			for (String step2 : this.steps) {
				if (step1.equals(step2))
					continue;
				for (String step3 : this.steps) {
					if (step2.equals(step3) || step3.equals(step1))
						continue;
					HashMap<String, String> stepPair1 = new HashMap<String, String>();
					HashMap<String, String> stepPair2 = new HashMap<String, String>();
					HashMap<String, String> stepPair3 = new HashMap<String, String>();
					stepPair1.put(step1, step3);
					stepPair2.put(step3, step2);
					stepPair3.put(step1, step2);
					if (this.mbdpMatrix.get(stepPair1)
							&& this.mbdpMatrix.get(stepPair2)) {
						this.mbdpMatrix.remove(stepPair3);
						this.mbdpMatrix.put(stepPair3, true);
					}
				}
			}
		}
	}

	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o.getClass() == MBDPMatrix.class) {
			MBDPMatrix n = (MBDPMatrix) o;
			return n.mbdp_ID.equals(mbdp_ID);
		}
		return false;
	}

	// 根据 mbdp_ID 计算 MBDPMatrix对象的 hashCode() 返回值
	public int hashCode() {
		return mbdp_ID.hashCode();
	}

}
