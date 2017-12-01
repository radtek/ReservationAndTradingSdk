package com.icbc.devp.gui.info;

import java.sql.Connection;
import java.util.ArrayList;

import com.icbc.devp.bean.db.MbdpCtlBean;
import com.icbc.devp.gui.UiContext;
import com.icbc.devp.tool.db.ConnectionManager;
import com.icbc.devp.tool.db.DBUtils;
import com.icbc.devp.tool.log.Log;

/**TODO:更新启用标志*/
public class DbDataUpdate {

	public DbDataUpdate(){
		this.dbManager = UiContext.getInstance().getDbManager();
		this.util = new DBUtils();
	}
	
	/**TODO:更新启用标志.true启用，false禁用*/
	public boolean updateEnableFlag(String app, 
			                        String cycle, 
			                        String dataSource, 
			                        String stepno,
			                        boolean flag)
	  throws Exception{
		Connection con = dbManager.getConnection();
		try{
			
			String enableFlag =  flag ? "1" : "0";
			String sqlText = "UPDATE MBDP_B_STEPDEF SET ENABLE_FLAG='"
				             + enableFlag+"' WHERE APP='"+app+"' AND CYCLE='"+cycle+"' AND DATA_SOURCE='"
				             + dataSource+"' AND STEPNO='"+stepno+"'";
			Log.getInstance().info("[DbDataUpdate.updateEnableFlag]执行语句:"+sqlText);
			try{
				if(util.executeUpdate(con, sqlText)){
					return true;
				}else{
					//看看是不是OBMS的
					sqlText = "UPDATE OBMS_BATCHSTEP_DEF SET ENABLE_FLAG='"
			                + enableFlag+"' WHERE APP='"+app+"' AND CYCLE='"+cycle+"' AND DATA_SOURCE||'#'||TZGCODE='"
			                + dataSource+"' AND STEPNO='"+stepno+"'";
					Log.getInstance().info("[DbDataUpdate.updateEnableFlag]执行语句:"+sqlText);
					return util.executeUpdate(con, sqlText);
				}
			}catch(Exception sqle){
				//看看是不是OBMS的
				sqlText = "UPDATE OBMS_BATCHSTEP_DEF SET ENABLE_FLAG='"
		                + enableFlag+"' WHERE APP='"+app+"' AND CYCLE='"+cycle+"' AND DATA_SOURCE||'#'||TZGCODE='"
		                + dataSource+"' AND STEPNO='"+stepno+"'";
				Log.getInstance().info("[DbDataUpdate.updateEnableFlag]执行语句:"+sqlText);
				return util.executeUpdate(con, sqlText);
			}
		}catch(Exception e){
			Log.getInstance().exception(e);
			return false;
		}finally{
			if( con != null){
				con = null;
			}
		}	
	}
	
	/**TODO:更新启用标志，true启用，false禁用，启用一批的*/
	public boolean updateEnableFlag(String app, 
						            String cycle, 
						            String dataSource, 
						            ArrayList<String> stepList,
						            boolean flag)
	  throws Exception{
		Connection con = dbManager.getConnection();
		try{
			if(stepList == null || stepList.isEmpty()){
				return true;
			}
			String enableFlag =  flag ? "1" : "0";
			String stepno = "'" + stepList.get(0) + "'";
			for(int i=1; i<stepList.size();i++){
				stepno += ", '"+stepList.get(i)+"'";
			}
			String sqlText = "UPDATE MBDP_B_STEPDEF SET ENABLE_FLAG='"
				             + enableFlag+"' WHERE APP='"+app+"' AND CYCLE='"+cycle+"' AND DATA_SOURCE='"
				             + dataSource+"' AND STEPNO IN ("+stepno+")";
			Log.getInstance().info("[DbDataUpdate.updateEnableFlag]执行语句:"+sqlText);
			try{
				if(util.executeUpdate(con, sqlText)){
					return true;
				}else{
					sqlText = "UPDATE OBMS_BATCHSTEP_DEF SET ENABLE_FLAG='"
		                + enableFlag+"' WHERE APP='"+app+"' AND CYCLE='"+cycle+"' AND DATA_SOURCE||'#'||TZGCODE='"
		                + dataSource+"' AND STEPNO IN ("+stepno+")";
					Log.getInstance().info("[DbDataUpdate.updateEnableFlag]执行语句:"+sqlText);
					return util.executeUpdate(con, sqlText);
				}
			}catch(Exception sqle){
				//看看是不是OBMS的
				sqlText = "UPDATE OBMS_BATCHSTEP_DEF SET ENABLE_FLAG='"
		                + enableFlag+"' WHERE APP='"+app+"' AND CYCLE='"+cycle+"' AND DATA_SOURCE||'#'||TZGCODE='"
		                + dataSource+"' AND STEPNO IN ("+stepno+")";
				Log.getInstance().info("[DbDataUpdate.updateEnableFlag]执行语句:"+sqlText);
				return util.executeUpdate(con, sqlText);
			}
		}catch(Exception e){
			Log.getInstance().exception(e);
			return false;
		}finally{
			if( con != null){
				con = null;
			}
		}	
	}
	
	/**TODO:更新批量日期等的相关信息*/
	public boolean updateCtlInfo(MbdpCtlBean bean){
		Connection con = null;
		try{
			con = this.dbManager.getConnection();
			String sqlText = "UPDATE MBDP_B_CTL SET WORKDATE='"+bean.getWorkdate()+"', MON_RUNDAY='"
			                 +bean.getMonRunDay()+"', PRET_TYPE='"+bean.getPretType()+"', STATUS='"+bean.getStatus()+"' WHERE APP='"+bean.getApp()
			                 +"' AND CYCLE='"+bean.getCycle()+"' AND DATA_SOURCE='" + bean.getDatasource()+"'";
			Log.getInstance().info("执行SQL语句："+sqlText);
//			return util.executeUpdate(con, sqlText);
			try{
				if(util.executeUpdate(con, sqlText)){
					return true;
				}else{
					sqlText = "UPDATE OBMS_BATCH_CTL SET WORKDATE='"+bean.getWorkdate()+"', MON_RUNDAY='"
		              +bean.getMonRunDay()+"', PRET_TYPE='"+bean.getPretType()+"', STATUS='"+bean.getStatus()+"' WHERE APP='"+bean.getApp()
		              +"' AND CYCLE='"+bean.getCycle()+"' AND DATA_SOURCE||'#'||TZGCODE='" + bean.getDatasource()+"'";
					Log.getInstance().info("[DbDataUpdate.updateEnableFlag]执行语句:"+sqlText);
					return util.executeUpdate(con, sqlText);
				}
			}catch(Exception sqle){
				//看看是不是OBMS的
				sqlText = "UPDATE OBMS_BATCH_CTL SET WORKDATE='"+bean.getWorkdate()+"', MON_RUNDAY='"
			              +bean.getMonRunDay()+"', PRET_TYPE='"+bean.getPretType()+"', STATUS='"+bean.getStatus()+"' WHERE APP='"+bean.getApp()
			              +"' AND CYCLE='"+bean.getCycle()+"' AND DATA_SOURCE||'#'||TZGCODE='" + bean.getDatasource()+"'";
				Log.getInstance().info("[DbDataUpdate.updateEnableFlag]执行语句:"+sqlText);
				return util.executeUpdate(con, sqlText);
			}
		}catch(Exception e){
			Log.getInstance().exception(e);
			return false;
		}finally{
			if(con != null){
				con = null;
			}
		}
	}
	
	private ConnectionManager dbManager;
	private DBUtils util;
}
