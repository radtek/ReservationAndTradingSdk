package com.icbc.devp.dbstep;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.icbc.devp.bean.db.MbdpCtlBean;
import com.icbc.devp.gui.UiContext;
import com.icbc.devp.tool.db.ConnectionManager;
import com.icbc.devp.tool.db.DBUtils;
import com.icbc.devp.tool.log.Log;

public class DbDataLoader {

	public DbDataLoader(){
		util = new DBUtils();
		this.conManager = UiContext.getInstance().getDbManager();
	}
	
	/**TODO:加载MBDP_B_CTL的信息，如果加载失败则返回NULL*/
	@SuppressWarnings("unchecked")
	public ArrayList<MbdpCtlBean> loadCtlInfoBySql(){
		String sqlText = "SELECT APP,CYCLE,DATA_SOURCE,DATA_SOURCE_NAME,WORKDATE,MON_RUNDAY,STATUS,PRET_TYPE FROM MBDP_B_CTL T WHERE CYCLE IN ('D','M') ORDER BY APP,CYCLE,DATA_SOURCE";
		final String tag = "[DbDataLoader.loadCtlInfo]";
		String msg = "";
		Connection con;
		try{
			con = this.conManager.getConnection();
			ArrayList<HashMap<String, String>> list = null;
			try{
				list = (ArrayList<HashMap<String,String>>)util.executeQry(con, sqlText);
				UiContext.getInstance().putValue("MBDP_FLAG", "1");
			}catch(Exception e){
				UiContext.getInstance().putValue("MBDP_FLAG", "0");
				sqlText = "SELECT APP,CYCLE,DATA_SOURCE||'#'||TZGCODE DATA_SOURCE,DATA_SOURCE_NAME||'#'||TZGNAME DATA_SOURCE_NAME,WORKDATE,MON_RUNDAY,STATUS,PRET_TYPE FROM OBMS_BATCH_CTL T WHERE CYCLE IN ('1','2') ORDER BY APP,CYCLE,DATA_SOURCE";
				list = (ArrayList<HashMap<String,String>>)util.executeQry(con, sqlText);
			}
			//执行存储过程
			ArrayList<MbdpCtlBean> beanList = new ArrayList<MbdpCtlBean>();
			MbdpCtlBean bean;
			HashMap<String, String> lineMap;
			for(int i=0; i<list.size(); i++){
				lineMap = list.get(i);
				bean = new MbdpCtlBean();
				bean.setApp(lineMap.get("APP"));
				bean.setCycle(lineMap.get("CYCLE"));
				bean.setDatasource(lineMap.get("DATA_SOURCE"));
				bean.setDesc(lineMap.get("DATA_SOURCE_NAME"));
				bean.setMonRunDay(lineMap.get("MON_RUNDAY"));
				bean.setPretType(lineMap.get("PRET_TYPE"));
				bean.setWorkdate(lineMap.get("WORKDATE"));
				bean.setStatus(lineMap.get("STATUS"));
				beanList.add(bean);
			}
			return beanList;
		}catch(Exception e){
			msg = tag + "加载MBDP_B_CTL/OBMS_BATCH_CTL信息时发生异常:"+e.getMessage();
			Log.getInstance().error(msg);
			e.printStackTrace();
			return null;
		}
	}
	
	public void release(){
		try{
			if(this.conManager != null){
				this.conManager.closeDBConnection();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
//	private boolean isMbdpDb(){
//		try{
//			util.executeQry(this.conManager.getConnection(), qryCheckText);
//			return true;
//		}catch(Exception e){
//			return false;
//		}
//	}
	
	//用来判断环境表的标志
//	private static final String qryCheckText = "SELECT COUNT(*) FROM MBDP_B_CTL WHERE ROWNUM=1";
	private ConnectionManager conManager;
	private DBUtils util;
}
