/**
 * 
 */
package com.icbc.devp.relstep;

/**
 * @author kfzx-liangxch
 *
 */
public class RelStepBean {

	private String stepno;
	private String tbname;
//	private int lineno;
	
	public RelStepBean(String stepno, String tbname) {
		this.stepno = stepno;
		this.tbname = tbname;
//		this.lineno = lineno;
	}
	
	public String getStepno() {
		return stepno;
	}
	public void setStepno(String stepno) {
		this.stepno = stepno;
	}
	public String getTbname() {
		return tbname;
	}
	public void setTbname(String tbname) {
		this.tbname = tbname;
	}
//	public int getLineno() {
//		return lineno;
//	}
//	public void setLineno(int lineno) {
//		this.lineno = lineno;
//	}
}
