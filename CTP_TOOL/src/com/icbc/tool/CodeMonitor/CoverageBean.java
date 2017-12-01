package com.icbc.tool.CodeMonitor;

public class CoverageBean {

	
	public String getApp_name() {
		return app_name;
	}
	public void setApp_name(String app_name) {
		this.app_name = app_name;
	}
	public String getBirthland() {
		return birthland;
	}
	public void setBirthland(String birthland) {
		this.birthland = birthland;
	}
	public String getApp_version() {
		return app_version;
	}
	public void setApp_version(String app_version) {
		this.app_version = app_version;
	}
	public String getFile_name() {
		return file_name;
	}
	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}
	public String getFile_path() {
		return file_path;
	}
	public void setFile_path(String full_path) {
		this.file_path = full_path;
	}
	public String getFile_type() {
		return file_type;
	}
	public void setFile_type(String file_type) {
		this.file_type = file_type;
	}
	public String getLast_modified() {
		return last_modified;
	}
	public void setLast_modified(String last_modified) {
		this.last_modified = last_modified;
	}
	private String app_name;
	private String birthland;
	private String app_version;
	private String file_name;
	private String file_path;
	private String file_type;
	private String last_modified;
	
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof CoverageBean)) {
			return false;
		}
		final CoverageBean other = (CoverageBean) o;
		if (this.app_name.equals(other.getApp_name())
				&& this.file_name.equals(other.getFile_name())
				&& this.file_path.equals(other.getFile_path())
				&& this.app_version.equals(other.getApp_version())){
			return true;
		} else {
			return false;
		}
	}

	public int hashCode() {
		int result;
		result = (app_name == null ? 0 : app_name.hashCode());
		result = 43 * result + (file_name == null ? 0 : file_name.hashCode());
		result = 47 * result + (file_path == null ? 0 : file_path.hashCode());
		result = 53 * result + (app_version == null ? 0 : app_version.hashCode());
		return result;
	}
	
}
