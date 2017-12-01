package com.icbc.tool.CodeMonitor;

import com.icbc.tool.db.DBTableBean;

public class CCRecordBean {

	
	public String getBranch() {
		return branch;
	}
	public void setBranch(String branch) {
		this.branch = branch;
	}
	public String getElement_name() {
		return element_name;
	}
	public void setElement_name(String element_name) {
		this.element_name = element_name;
	}
	public String getFullPath() {
		return fullPath;
	}
	public void setFullPath(String fullPath) {
		this.fullPath = fullPath;
	}
	public String getCreate_date() {
		return create_date;
	}
	public void setCreate_date(String create_time) {
		this.create_date = create_time;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getDeveloper() {
		return developer;
	}
	public void setDeveloper(String developer) {
		this.developer = developer;
	}
	public int getAdd_valid() {
		return add_valid;
	}
	public void setAdd_valid(int add_valid) {
		this.add_valid = add_valid;
	}
	public int getMod_valid() {
		return mod_valid;
	}
	public void setMod_valid(int mod_valid) {
		this.mod_valid = mod_valid;
	}
	public int getDel_valid() {
		return del_valid;
	}
	public void setDel_valid(int del_valid) {
		this.del_valid = del_valid;
	}
	public int getMov_valid() {
		return move_valid;
	}
	public void setMov_valid(int mov_valid) {
		this.move_valid = mov_valid;
	}
	private String branch; //�汾��֧
	private String element_name; //������
	private String fullPath; //�ļ�ȫ·��
	private String create_date; //�ļ�����ʱ��
	private String creator ;  //������
	private String developer; //������
	private int add_valid; //��Ч��������
	private int mod_valid; //��Ч�޸�����
	private int move_valid; //��Ч�ƶ�����
	private int del_valid; //��Чɾ������
	
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof CCRecordBean)) {
			return false;
		}
		final CCRecordBean other = (CCRecordBean) o;
		if (this.branch.equals(other.getBranch())
				&& this.element_name.equals(other.getElement_name())
				&& this.fullPath.equals(other.getFullPath())) {
			return true;
		} else {
			return false;
		}
	}

	public int hashCode() {
		int result;
		result = (branch == null ? 0 : branch.hashCode());
		result = 43 * result + (element_name == null ? 0 : element_name.hashCode());
		result = 47 * result + (fullPath == null ? 0 : fullPath.hashCode());
		return result;
	}
	
}
