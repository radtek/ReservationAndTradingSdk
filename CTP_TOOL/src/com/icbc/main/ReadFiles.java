package com.icbc.main;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import com.icbc.tool.db.CTP3OPBean;
import com.icbc.tool.db.CTPBean;
import com.icbc.tool.db.OPGBean;
import com.icbc.tool.db.ReadDBTable;
import com.icbc.tool.db.SQLXMLBean;
import com.icbc.tool.util.ReadCQL;
import com.icbc.tool.util.ReadJSP;
import com.icbc.tool.util.ReadOP;
import com.icbc.tool.util.WriteExcel;
import com.icbc.tool.xml.ReadFlowc;
import com.icbc.tool.xml.ReadOPG;

public class ReadFiles {

	List<String> pathFiles = new ArrayList<String>();
	HashSet<CTP3OPBean> ctp3ops = new HashSet<CTP3OPBean>();
	HashMap<String,String> sqlXMLs = new HashMap<String,String>();
	
	public static int IDCount ;
    
	private List<Integer> menuID = new ArrayList<Integer>();
	
    public List<Integer> getMenuID() {
		return menuID;
	}

	public void setMenuID(List<Integer> menuID) {
		this.menuID = menuID;
	}

	/**
	 * 递归遍历文件
	 * @param fileName
	 */
	public void getPathFile(String fileName) {
		File f = new File(fileName);
		if (f.isDirectory()) {
			File[] fileList = f.listFiles();
			if (fileList.length > 0) {
				for (File file : fileList) {
					if (file.isDirectory())
						getPathFile(file.getAbsolutePath());
					else
						pathFiles.add(file.getAbsolutePath());
				}
			}
		} else {
			pathFiles.add(fileName);
		}
	}

	/**
	 * 
	 * @param str
	 * @return 字符串中取出文件名称
	 */
	
	public int findFileName(String str) {
		int idx = str.lastIndexOf(".jsp");
		int cntDir = 0;
		while (cntDir < 2 && idx >= 0) {
			if (str.charAt(idx) == '\\') {
				cntDir++;
			}
			--idx;
		}
		return idx + 2;
	}

	public String DealStr(String str) {
		return str.replace('/', '\\');
	}

	/**
	 * @param str 项目文件存储路径
	 * @return 构造jsp，vjson, opg 文件之间有向连接关系
	 * @throws Exception 
	 * 
	 */
	
	public List<CTPBean> ReadFiles(String str) throws Exception {
		getPathFile(str); // 获取路径下的所有文件
		List<CTPBean> result = new ArrayList<CTPBean>();
		try {
			result = doXML();
		} catch (Exception e) {
			e.printStackTrace();
		}
		ReadJSP r = new ReadJSP();
		for (String file : pathFiles) {
			if (file.lastIndexOf(".jsp") != -1) {
				int idx = findFileName(file);
				String fileName = file.substring(idx,
						file.lastIndexOf(".jsp") + 4);
				if (fileName == null)
					continue;
				fileName = fileName.replace('/', '\\');
				for (CTPBean tmp : result) {
					if (tmp.getFileName() != null)
						tmp.setFileName(tmp.getFileName().replace('/', '\\'));
					else
						continue;
					//tmp表示当前JSP文件
					if (tmp.getFileName().equals(fileName)) {
						if (tmp.getNextID() == null) {
							tmp.setNextID(new ArrayList<Integer>());
						}
						// 获取当前JSP文件链接的后向JSP文件
						List<CTPBean> jspNext = new ArrayList<CTPBean>();
						try {
							jspNext = r.readJSP(file);
						} catch (Exception e) {
							e.printStackTrace();
						}
						//处理JSP的链接关系
						if (jspNext != null) {
							ArrayList<CTPBean> tmpjspNext = new ArrayList<CTPBean>();
							for (CTPBean obj : jspNext) {
								//处理ctp3交易
							/*	if(!obj.getCtp3OpName().equals("")){
									System.out.println(tmp.getFileName() + " contains CTP3 OP : "
											+ obj.getCtp3OpName() + " -> ");
									for(CTP3OPBean ctp3 :ctp3ops){
										if(ctp3.getOpStepID().equals(obj.getCtp3OpName())){
										    for(String dsrName : ctp3.getRelDSR()){
										    	tmp.getDsrName().add(dsrName);
										    	System.out.println(dsrName);
										    }
										    CTPBean jsp = new CTPBean();
										    //添加链接的JSP页面
										    for(String jspfile:ctp3.getRelJSP()){
										    	jsp = new CTPBean();
										    	jsp.setFileName(jspfile);
										    	tmpjspNext.add(jsp);
										    	//System.out.println(jspfile);
										    }
										    //添加链接的OP,处理方法，把op调用的存储过程 指向链向 ctp3 op的jsp页面
										    for(String opName:ctp3.getRelOP()){
										    	OPGBean e = new OPGBean();
										    	e.getOpName().add(sqlXMLs.get(opName));
										    	if(tmp.getOpgBean()!=null)
										    	   tmp.getOpgBean().add(e);
										    	else {
										    	    List<OPGBean> lOPGBean = new ArrayList<OPGBean>();
										    	    lOPGBean.add(e);
										    		tmp.setOpgBean(lOPGBean);
										    	}
										    	//System.out.println(e.toString());
										    }
											break;
										}
									}
								}*/
							}
							
							/*if(tmpjspNext!=null){
								jspNext.addAll(tmpjspNext);
								//System.out.println(tmpjspNext.toArray().toString());
							}*/
							boolean flag = false;
							for(CTPBean obj:jspNext){
								flag = false;
								for (CTPBean res : result) {
									if(res.getFileName()!=null && res.getFileName().equals(tmp.getFileName()))continue;
									if (obj.getFileName() != null
											&& res.getFileName() != null
											&& res.getFileName().equals(
													obj.getFileName())) {
								    if (tmp.getNextID().isEmpty()|| !tmp.getNextID().contains(res.getID()))
										{
								    	   tmp.getNextID().add(res.getID());
								    	   flag = true;
										}
									if (res.getPredID().isEmpty()|| !res.getPredID().contains(tmp.getID())){
											res.getPredID().add(tmp.getID());
										    flag = true;
											break;
									   }
									}
									if (res.getFileID() == null
											|| res.getFlowcName() == null)
										continue;
									if (res.getFileID().equals(obj.getFileID())
											&& res.getFlowcName().equals(obj.getFlowcName())) {
										if (tmp.getNextID().isEmpty()
												|| !tmp.getNextID().contains(
														res.getID())){
											tmp.getNextID().add(res.getID());
										    flag = true;
										}
										if (res.getPredID().isEmpty()
												|| !res.getPredID().contains(
														tmp.getID())){
											res.getPredID().add(tmp.getID());
										    flag = true;
											break;
										}
									}
								}
								if(!flag){
									obj.setID(IDCount++);
									result.add(obj);
									if (tmp.getNextID().isEmpty()|| !tmp.getNextID().contains(obj.getID()))
									{
							    	   tmp.getNextID().add(obj.getID());
									}
								    if (obj.getPredID().isEmpty()|| !obj.getPredID().contains(tmp.getID())){
										obj.getPredID().add(tmp.getID());
								   }
								}
							}
						}
						break;
					}
				}
			} 
		}
		/**
		 * for (int i = 0; i < result.size(); ++i) { System.out.print("文件编号 ：" +
		 * result.get(i).getID() + " 文件ID： "+ result.get(i).getFileID() +
		 * " 文件名称 : " + result.get(i).getFileName() + " 文件所在Flowc:"+
		 * result.get(i).getFlowcName() + " 文件显示名称 : " +
		 * result.get(i).getDisplayName() + " 文件的后续节点个数: "); int cnt =
		 * (result.get(i).getNextID() != null) ? result.get(i)
		 * .getNextID().size() : 0; System.out.println(cnt); if (cnt > 0) { for
		 * (int j = 0; j < cnt; ++j) { int k = result.get(i).getNextID().get(j);
		 * System.out.println(result.get(i).getFileName() + " ---->>>> No. " + k
		 * + " " + result.get(k).getFileName()); } } }
		 */

		for (CTPBean obj : result) {
			if (obj.getFileName()!=null && obj.getFileName().lastIndexOf(".opg") != -1) {
				for (CTPBean ctp : result) {
					if (ctp.getFileName()==null || ctp.getFileName().equals(obj.getFileName()))
						continue;
					if (ctp.getFileName().lastIndexOf(".opg") != -1) {
						boolean found = false;
						if(obj.getOpgBean()==null || ctp.getOpgBean()==null)continue;
						for (OPGBean A : obj.getOpgBean()) {
							if(A.getOpName()==null)continue;
							for (OPGBean B : ctp.getOpgBean()) {
								if(B.getOpName()==null)continue;
								for (String strA : A.getOpName()) {
									if(strA==""|| strA.indexOf("PROC")==-1) continue;
									for (String strB : B.getOpName()) {
										if( strB=="" || strB.indexOf("PROC")==-1) continue;
										if (strA.equals(strB)) {
											found = true;break;
										}
									}
									if (found)
										break;
								}
								if (found)
									break;
							}
							if (found)
								break;
						}
						if (found) {
							if (!obj.getNextID().contains(ctp.getID()))
								obj.getNextID().add(ctp.getID());
							if (!ctp.getPredID().contains(obj.getID()))
								ctp.getPredID().add(obj.getID());
						  // System.out.println("Match: "+obj.getFileName() + " *** "+ctp.getFileName());
						}
					}

				}
			}
		}
		new WriteExcel().WriteExcel(result,"全量的菜单");
		return result;
	}

	
	
    /**
    * 读取所有类型的文件，包括JSP，FLOWC，OPG，OP
    * @return
    * @throws Exception
    */
	
	public List<CTPBean> doXML() throws Exception {
		ReadFlowc t = new ReadFlowc();
		ReadOP readop = new ReadOP();
		ReadCQL readCQL = new ReadCQL();
		List<CTPBean> flowcDBbean = new ArrayList<CTPBean>(); // flowc文件描述的连接情况
		IDCount = 0;
		for (String str : pathFiles) {
			int idx = str.lastIndexOf("\\");
			if (str.lastIndexOf(".flowc") != -1) {
				List<CTPBean> v = t.readXMLFile(str);
				if (v != null) {
					for (CTPBean vv : v) {
						flowcDBbean.add(vv);
					}
					IDCount += v.size(); // 给文件编序号
				}
			}/*else if(str.lastIndexOf(".op") != -1){
				ctp3ops.add(readop.readOP(str));
			}else if(str.substring(idx).toLowerCase().equals("sql.xml")){
				sqlXMLs.putAll(readCQL.readCQL(str));
			}*/
		}
		
		/**
		 *   获取数据库的EBM_ITEM表的全部列，并得到各JSP页面的菜单名称
		 */
		
		List<Integer> tmpMenuID = new ArrayList<Integer>();
	
		ReadDBTable rdb = new ReadDBTable();
		List<CTPBean> r = rdb.readTableColumns();
		HashMap<String,HashSet<String>> pckgRefTable=rdb.getProcedureRefTable();
			//System.out.println(pckgRefTable);
		
		for (CTPBean vv : flowcDBbean) {
			if(r.isEmpty() || r==null)continue;
				//System.out.println(vv.getFileID() + "  "+vv.getFlowcName());
			for (CTPBean rr : r){	
					if (vv.getFileID().equals(rr.getFileID())
							&& vv.getFlowcName().equals(rr.getFlowcName())) {
						if(vv.getItemName()==null){
						  vv.setItemName(rr.getItemName());
						}else{
							String itemNametmp = vv.getItemName()+"^^"+rr.getItemName();
							vv.setItemName(itemNametmp);
						}
						tmpMenuID.add(vv.getID());
					}
				}
			}
			if(tmpMenuID!=null){
				setMenuID(tmpMenuID);
			}
		
		
		/**
		 * 
		 *    1)考虑<jsp:include page="" />的情况
		 *    2)考虑CTP3交易JSP文件的情况
		 *    3)考虑未处理的OPG文件
		 *    4)考虑未处理的OP文件
		 *    
		 */
		boolean found = false;
		for (String str : pathFiles) {
			if (str.lastIndexOf(".jsp") != -1) {
				int idx = findFileName(str);
				String fileName = str.substring(idx,
						str.lastIndexOf(".jsp") + 4);
				found = false;
				fileName = DealStr(fileName);
				for (CTPBean tmp : flowcDBbean) {
					if (tmp.getFileName() != null
							&& tmp.getFileName().equals(fileName)) {
                    System.out.println(tmp.getFileName() + "**" + fileName);
						found = true;
						break;
					}
				}
				if (found == false) {
					CTPBean newJSP = new CTPBean();
					newJSP.setID((IDCount++));
					newJSP.setFileName(fileName);
					flowcDBbean.add(newJSP);
				}
			} else if (str.lastIndexOf(".opg") != -1) {
				int idx = str.lastIndexOf('\\');
				String fileName = str.substring(idx + 1,
						str.lastIndexOf(".opg") + 4);
				// System.out.println(fileName);
				for (CTPBean tmp : flowcDBbean) {
					if (tmp.getFileName() != null
							&& tmp.getFileName().equals(fileName)) {
						tmp.setOpgBean(new ReadOPG().readOPG(str));
						// System.out.println(tmp.getFileName());
						break;
					}
				}
			}else if(str.lastIndexOf(".op")!=-1) { // 处理 CTP3的交易 
				int idx = str.lastIndexOf('\\');
				String fileName = str.substring(idx + 1,str.lastIndexOf(".op") + 3);
				CTPBean newJSP = new CTPBean();
				newJSP.setID((IDCount++));
				newJSP.setFileName(fileName);
				flowcDBbean.add(newJSP);
			}
		}
	
		for(CTPBean flowc: flowcDBbean){
			if(flowc.getFileName()!=null && flowc.getFileName().indexOf(".opg")!=-1){
				if(flowc.getOpgBean()==null)continue;
				for(OPGBean opg : flowc.getOpgBean()){
					if (opg.getOpName()==null)continue;
					int len = opg.getOpName().size();
					for (int i=0;i<len;++i) {
					   if(opg.getOpName().get(i)==null)continue;
					   if(opg.getOpName().get(i).lastIndexOf("PROC")!=-1){
						 System.out.print(opg.getOpName().get(i) + " : ");
						 if(pckgRefTable.containsKey(opg.getOpName().get(i))){
				            System.out.println(pckgRefTable.get(opg.getOpName().get(i)));
							 opg.getProcedureRefTable().put(opg.getOpName().get(i),pckgRefTable.get(opg.getOpName().get(i)));
						 }
					     
					   }
					}
				  /* if(!opg.getProcedureRefTable().isEmpty())
					   System.out.println(opg.getProcedureRefTable());*/
				}
			}
		}
		return flowcDBbean;
	}

}
