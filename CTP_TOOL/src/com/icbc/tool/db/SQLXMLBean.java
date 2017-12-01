package com.icbc.tool.db;

public class SQLXMLBean {

	public String getProcedureID() {
		return procID;
	}
	public void setProcedureID(String procedureID) {
		this.procID = procedureID;
	}
	public String getProcedureName() {
		return procName;
	}
	public void setProcedureName(String procedureName) {
		this.procName = procedureName;
	}
	
	private String procName = "";
	private String procID = "";
	
	public boolean equals(Object obj){
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof SQLXMLBean)) {
			return false;
		}
		final SQLXMLBean other = (SQLXMLBean) obj;
		if(this.procID.equals(other.getProcedureID())&&
		   this.procName.equals(other.getProcedureName()))
		   return true;
		return false;
	}
    
   public int hashCode(){
	    int result ;
	    result = (procID == null ? 0 : procID.hashCode());
		result = 37 * result + (procName == null ? 0 : procName.hashCode());
		return result;
   }
}
