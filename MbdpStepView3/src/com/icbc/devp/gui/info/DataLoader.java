package com.icbc.devp.gui.info;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.icbc.devp.bean.db.MbdpCtlBean;
import com.icbc.devp.gui.UiContext;
import com.icbc.devp.gui.factory.IconFactory;
import com.icbc.devp.tool.db.ConnectionManager;
import com.icbc.devp.tool.db.DBUtils;
import com.icbc.devp.tool.log.Log;
import com.icbc.devp.txtdraw.base.Info;

/**TODO:数据加载器，将数据存到列表里面*/
public class DataLoader {

	public DataLoader(){
		this.dbManager = UiContext.getInstance().getDbManager();
		this.util = new DBUtils();
	}

	
	/**TODO:获取步骤定义数据*/
	@SuppressWarnings("unchecked")
	public boolean loadStepInfoBySql(StepInfoTree tree,
			                         String app,
			                         String cycle,
			                         String dataSource){
		String msg;
		String tag = "[DataLoader.loadStepInfo]";
		Connection con = null;
		try{
			con = dbManager.getConnection();
			StepInfo node;
			Info info;
			ArrayList<HashMap<String, String>> list = null;
			String tmpStr = UiContext.getInstance().getValue("MBDP_FLAG");
			String sqlText;
			boolean isMbdp = true;
			if(tmpStr == null || "0".equals(tmpStr)){
				isMbdp = false;
				sqlText = OBMSSTEP_SQL.replace("$APP$", app).replace("$CYCLE$", cycle).replace("$DATASOURCE$", dataSource);
			}else{
				sqlText = MBDPSTEP_SQL.replace("$APP$", app).replace("$CYCLE$", cycle).replace("$DATASOURCE$", dataSource);
			}
			
			try{
				list = (ArrayList<HashMap<String,String>>)util.executeQry(con, sqlText);
			}catch(Exception e){
				if(isMbdp){
					sqlText = OBMSSTEP_SQL.replace("$APP$", app).replace("$CYCLE$", cycle).replace("$DATASOURCE$", dataSource);
				}else{
					sqlText = MBDPSTEP_SQL.replace("$APP$", app).replace("$CYCLE$", cycle).replace("$DATASOURCE$", dataSource);
				}
				list = (ArrayList<HashMap<String,String>>)util.executeQry(con, sqlText);
			}
			
			HashMap<String, String> lineMap;
			
			//获取数据
			for(int i=0; i<list.size(); i++){
				lineMap = list.get(i);
				info = new Info();
				info.setApp(lineMap.get("APP"));
				info.setCycle(lineMap.get("CYCLE"));
				info.setDataSource(lineMap.get("DATA_SOURCE"));
				info.setProcType(lineMap.get("PROG_TYPE"));
				info.setStepno(lineMap.get("STEPNO"));
				info.setProc(lineMap.get("PROG"));
				info.setProcDesc(lineMap.get("PROG_NAME"));
				info.setEnableFlag(lineMap.get("ENABLE_FLAG"));
				info.setParameter(lineMap.get("PARAMETER"));
				info.setIcon(IconFactory.getInstance().getIcon(info.getProcType()));
				node = new StepInfo(10,10,200,60);
				node.setStepInfo(info);
				tree.setNode(node);
			}
			//设置个空节点
			info = new Info();
			info.setApp(app);
			info.setCycle(cycle);
			info.setDataSource(dataSource);
			info.setStepno(ROOT_ID);
			info.setProcType(Info.getStartNodeId());
			info.setProc("");
			info.setProcDesc("开始节点");
			info.setEnableFlag("1");
			info.setIcon(IconFactory.getInstance().getIcon("N/A"));
			node = new StepInfo(10,10,200,60);
			node.setStepInfo(info);
			tree.setRoot(node);
			//搞定
			return true;
		}catch(Exception e){
			msg = tag + "Exception:"+e.getMessage();
			System.out.println(msg);
			Log.getInstance().exception(e);
			return false;
		}finally{
			try{
				dbManager.freeConnection();
			}catch(Exception qe){
				qe.printStackTrace();
			}
		}
	}
	
	
	/**TODO:获取关系步骤*/
	@SuppressWarnings("unchecked")
	public boolean loadRelationInfoBySql(StepInfoTree tree,
						                 String app,
						                 String cycle,
						                 String dataSource){
		String msg;
		String tag = "[DataLoader.loadRelationInfo]";
		Connection con = null;
		try{
			con = dbManager.getConnection();
			//执行存储过程获取数据
//			String sqlText = MBDPREL_SQL.replace("$APP$", app).replace("$CYCLE$", cycle).replace("$DATASOURCE$", dataSource);
			ArrayList<HashMap<String, String>> list = null;
			HashMap<String, String> lineMap;
			String tmpStr = UiContext.getInstance().getValue("MBDP_FLAG");
			String sqlText;
			boolean isMbdp = true;
		
			if(tmpStr == null || "0".equals(tmpStr)){
				isMbdp = false;
				sqlText = OBMSREL_SQL.replace("$APP$", app).replace("$CYCLE$", cycle).replace("$DATASOURCE$", dataSource);
			}else{
				sqlText = MBDPREL_SQL.replace("$APP$", app).replace("$CYCLE$", cycle).replace("$DATASOURCE$", dataSource);
			}
			try{
				list = (ArrayList<HashMap<String,String>>)util.executeQry(con, sqlText);
			}catch(Exception e){
				if(isMbdp){
					sqlText = OBMSREL_SQL.replace("$APP$", app).replace("$CYCLE$", cycle).replace("$DATASOURCE$", dataSource);
				}else{
					sqlText = MBDPREL_SQL.replace("$APP$", app).replace("$CYCLE$", cycle).replace("$DATASOURCE$", dataSource);
				}
				list = (ArrayList<HashMap<String,String>>)util.executeQry(con, sqlText);
			}
			
			//逐笔处理
			String id;
			String fid;
			for(int i=0; i<list.size(); i++){
				lineMap = list.get(i);
				fid = lineMap.get("APP")+"_"+lineMap.get("CYCLE")+"_"+lineMap.get("DATA_SOURCE")+"_"+lineMap.get("STEPNO_P");
				id  = lineMap.get("APP")+"_"+lineMap.get("CYCLE")+"_"+lineMap.get("DATA_SOURCE")+"_"+lineMap.get("STEPNO");
				//判断是否根节点
				if(ROOT_ID.equals(lineMap.get("STEPNO"))){
					if(!tree.setRoot(fid)){
						System.out.println("添加ROOT节点"+fid+"失败！");
						return false;
					}
				}
				tree.setFatherRelation(id, fid, lineMap.get("STEPNO"));
				tree.setSonRelation(fid, id);
			}
			return true;
		}catch(Exception e){
			msg = tag + "Exception:"+e.getMessage();
			Log.getInstance().exception(e);
			System.out.println(msg);
			return false;
		}finally{
			try{
				dbManager.freeConnection();
			}catch(Exception qe){
				qe.printStackTrace();
			}
		}
	}
	
	/**TODO:获取对应的CTL表的信息*/
	@SuppressWarnings("unchecked")
	public static MbdpCtlBean refreshCtl(String app, String cycle, String dataSource){
		String msg;
		String tag = "[DataLoader.refreshCtl]";
		Connection con = null;
		try{
			String sqlText = CTL_SQL.replace("$APP$", app)
			                        .replace("$CYCLE$", cycle)
			                        .replace("$DATASOURCE$", dataSource);
			con = UiContext.getInstance().getDbManager().getConnection();
			Log.getInstance().info(tag +"执行语句："+sqlText);
			DBUtils utils = new DBUtils();
			ArrayList<HashMap<String, String>> list = null;
			try{
				list = (ArrayList<HashMap<String, String>>)utils.executeQry(con, sqlText);
			}catch(Exception sqle){
				sqlText = OBMS_CTL_SQL.replace("$APP$", app)
						                .replace("$CYCLE$", cycle)
						                .replace("$DATASOURCE$", dataSource);
				list = (ArrayList<HashMap<String, String>>)utils.executeQry(con, sqlText);
			}
			if(list.isEmpty()){
				return null;
			}
			HashMap<String, String> lineMap = list.get(0);
			MbdpCtlBean bean = new MbdpCtlBean();
			bean.setApp(app);
			bean.setCycle(cycle);
			bean.setDatasource(dataSource);
			bean.setMonRunDay(lineMap.get("MON_RUNDAY"));
			bean.setPretType(lineMap.get("PRET_TYPE"));
			bean.setStatus(lineMap.get("STATUS"));
			bean.setWorkdate(lineMap.get("WORKDATE"));
			return bean;
		}catch(Exception e){
			msg = tag + "Exception:"+e.getMessage();
			Log.getInstance().exception(e);
			System.out.println(msg);
			return null;
		}
	}
	
//	//静态方法
//	private static final String PROC_GET_STEP_DEFINE = "PCKG_DEVP_ASIST_TXTDRAW.PROC_GET_STEP_DEFINE";
//	private static final String PROC_GET_STEP_RELATION = "PCKG_DEVP_ASIST_TXTDRAW.PROC_GET_STEP_RELATION";
	private static final String ROOT_ID = "N/A";
	private static final String MBDPSTEP_SQL =  "SELECT T.APP,                                  " +
											    "       T.CYCLE,                                " +
											    "       T.DATA_SOURCE,                          " +
											    "       T.PROG_TYPE,                            " +
											    "       T.STEPNO,                               " +
											    "       T.PROG,                                 " +
											    "       T.PROG_NAME,                            " +
											    "       T.ENABLE_FLAG,                          " +
											    "       T.PARAMETER                             " +
											    "  FROM MBDP_B_STEPDEF T                        " +
											    " WHERE T.APP = UPPER('$APP$')                   " +
											    "   AND T.CYCLE = UPPER('$CYCLE$')               " +
											    "   AND T.DATA_SOURCE = UPPER('$DATASOURCE$')    ORDER BY STEPNO ";// +
											    //"   AND T.PROG_TYPE NOT IN ('INIT','CHK','JUMP') ORDER BY STEPNO";
	private static final String MBDPREL_SQL =   " SELECT T.APP,                              " +
												"        T.CYCLE,                            " +
												"        T.DATA_SOURCE,                      " +
												"        T.STEPNO_P,                         " +
												"        T.STEPNO                            " +
												"   FROM MBDP_B_STEPREL T                    " +
												"  WHERE T.APP = UPPER('$APP$')               " +
												"    AND T.CYCLE = UPPER('$CYCLE$')           " +
												"    AND T.DATA_SOURCE = UPPER('$DATASOURCE$')" +
												"  ORDER BY T.STEPNO ";
	//为了支持OBMS而定的
	private static final String OBMSSTEP_SQL =  "SELECT T.APP,                                  " +
											    "       T.CYCLE,                                " +
											    "       T.DATA_SOURCE|| '#' || T.TZGCODE DATA_SOURCE, " +
											    "       T.PROG_TYPE,                            " +
											    "       T.STEPNO,                               " +
											    "       T.PROG,                                 " +
											    "       T.PROG_NAME,                            " +
											    "       T.ENABLE_FLAG,                          " +
											    "       T.PARAMETER                             " +
											    "  FROM OBMS_BATCHSTEP_DEF T                    " +
											    " WHERE T.APP = UPPER('$APP$')                  " +
											    "   AND T.CYCLE = UPPER('$CYCLE$')              " +
											    "   AND T.DATA_SOURCE || '#' || T.TZGCODE = UPPER('$DATASOURCE$')    ORDER BY STEPNO ";
	private static final String OBMSREL_SQL =  " SELECT T.APP,                              " +
												"        T.CYCLE,                            " +
												"        T.DATA_SOURCE|| '#' || T.TZGCODE DATA_SOURCE," +
												"        T.STEPNO_P,                         " +
												"        T.STEPNO                            " +
												"   FROM OBMS_BATCHSTEP_REL T                " +
												"  WHERE T.APP = UPPER('$APP$')               " +
												"    AND T.CYCLE = UPPER('$CYCLE$')           " +
												"    AND T.DATA_SOURCE || '#' || T.TZGCODE = UPPER('$DATASOURCE$')" +
												"  ORDER BY T.STEPNO ";
	//获取单个CTL
	private static final String CTL_SQL =   "SELECT T.WORKDATE,                    " +
											"       T.MON_RUNDAY,                  " +
											"       T.STATUS,                      " +
											"       T.PRET_TYPE                    " +
											"  FROM MBDP_B_CTL T                   " +
											" WHERE T.APP='$APP$'                  " +
											"   AND T.CYCLE = '$CYCLE$'            " +
											"   AND T.DATA_SOURCE = '$DATASOURCE$' ";
	private static final String OBMS_CTL_SQL =   "SELECT T.WORKDATE,                    " +
												"       T.MON_RUNDAY,                  " +
												"       T.STATUS,                      " +
												"       T.PRET_TYPE                    " +
												"  FROM OBMS_BATCH_CTL T                   " +
												" WHERE T.APP='$APP$'                  " +
												"   AND T.CYCLE = '$CYCLE$'            " +
												"   AND T.DATA_SOURCE||'#'||TZGCODE = '$DATASOURCE$' ";
	private ConnectionManager dbManager;
	private DBUtils util;
	
}
