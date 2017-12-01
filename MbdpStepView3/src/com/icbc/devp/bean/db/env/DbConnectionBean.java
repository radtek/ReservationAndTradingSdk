package com.icbc.devp.bean.db.env;

public class DbConnectionBean implements Comparable<DbConnectionBean>{

	public DbConnectionBean(){
		
	}
	
	@Override
	public int compareTo(DbConnectionBean bean){
		if(id == null){
			return -1;
		}
		return id.compareTo(bean.id);
	}
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public String getDbUser() {
		return dbUser;
	}
	public void setDbUser(String dbUser) {
		this.dbUser = dbUser;
	}
	public String getDbPwd() {
		return dbPwd;
	}
	public void setDbPwd(String dbPwd) {
		this.dbPwd = dbPwd;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}



	/**IP��ַ*/
	private String ip;
	/**ʵ����*/
	private String sid;
	/**�û���*/
	private String dbUser;
	/**����*/
	private String dbPwd;
	/**�˿�*/
	private String port;
	/**Ψһ��ʶ��*/
	private String id;
}
