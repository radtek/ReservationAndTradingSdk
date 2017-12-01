package com.icbc.tool.db;

import java.util.HashSet;

import com.icbc.tool.CodeMonitor.CoverageBean;

public class USER_SEQUENCES_Bean {

	
	public String getSequence_name() {
		return sequence_name;
	}
	public void setSequence_name(String sequence_name) {
		this.sequence_name = sequence_name;
	}
	public String getMin_value() {
		return min_value;
	}
	public void setMin_value(String min_value) {
		this.min_value = min_value;
	}
	public String getMax_value() {
		return max_value;
	}
	public void setMax_value(String max_value) {
		this.max_value = max_value;
	}
	public String getCycle_flag() {
		return cycle_flag;
	}
	public void setCycle_flag(String cycle_flag) {
		this.cycle_flag = cycle_flag;
	}
	public String getOrder_flag() {
		return order_flag;
	}
	public void setOrder_flag(String order_flag) {
		this.order_flag = order_flag;
	}
	public String getCache_size() {
		return cache_size;
	}
	public void setCache_size(String cache_size) {
		this.cache_size = cache_size;
	}
	public String getLast_name() {
		return last_number;
	}
	public void setLast_Number(String last_number) {
		this.last_number = last_number;
	}
	private String sequence_name;
	private String min_value;
	private String max_value;
	private String cycle_flag;
	private String order_flag;
	private String cache_size;
	private String last_number;
	private HashSet<String> procs ;
	public String getLast_number() {
		return last_number;
	}
	public void setLast_number(String last_number) {
		this.last_number = last_number;
	}
	public HashSet<String> getProcs() {
		return procs;
	}
	public void setProcs(HashSet<String> procs) {
		this.procs = procs;
	}
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof USER_SEQUENCES_Bean)) {
			return false;
		}
		final USER_SEQUENCES_Bean other = (USER_SEQUENCES_Bean) o;
		if (this.sequence_name.equals(other.getSequence_name())){
			return true;
		} else {
			return false;
		}
	}

	public int hashCode() {
		int result;
		result = (sequence_name == null ? 0 : sequence_name.hashCode());
		return result;
	}
	
}
