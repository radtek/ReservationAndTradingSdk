package com.icbc.DBUtils;

public class Etl_job_dependencyBean {

	private String ETL_SYSTEM;
	public String getETL_SYSTEM() {
		return ETL_SYSTEM;
	}
	public void setETL_SYSTEM(String eTL_SYSTEM) {
		ETL_SYSTEM = eTL_SYSTEM;
	}
	public String getETL_JOB() {
		return ETL_JOB;
	}
	public void setETL_JOB(String eTL_JOB) {
		ETL_JOB = eTL_JOB;
	}
	public String getDEPENDENCY_SYSTEM() {
		return DEPENDENCY_SYSTEM;
	}
	public void setDEPENDENCY_SYSTEM(String dEPENDENCY_SYSTEM) {
		DEPENDENCY_SYSTEM = dEPENDENCY_SYSTEM;
	}
	public String getDEPENDENCY_JOB() {
		return DEPENDENCY_JOB;
	}
	public void setDEPENDENCY_JOB(String dEPENDENCY_JOB) {
		DEPENDENCY_JOB = dEPENDENCY_JOB;
	}
	public String getDESC() {
		return DESC;
	}
	public void setDESC(String dESC) {
		DESC = dESC;
	}
	public String getENABLE() {
		return ENABLE;
	}
	public void setENABLE(String eNABLE) {
		ENABLE = eNABLE;
	}
	private String ETL_JOB;
	private String DEPENDENCY_SYSTEM;
	private String DEPENDENCY_JOB;
	private String DESC;
	private String ENABLE;
}
