package com.icbc.devp.dbstep;

import java.sql.Connection;

import com.icbc.devp.tool.db.ConnectionManager;
import com.icbc.devp.tool.db.DBUtils;
import com.icbc.devp.tool.file.FileUtil;

public class Test {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Connection con = null;
		try{
			String filePath = "D:/result.txt";
			String strBuf = FileUtil.readFile(filePath).get(0).replace("'", "''");
			String updSql = "UPDATE CBMS_UMSPMSG_DTL SET CONTENT=CONTENT||'";
			int start=0, end=1000;
			int length = strBuf.length();
			String sql;
			String tmp;
			ConnectionManager m = new ConnectionManager();
			m.initDBConnection("122.16.93.235", "CBMSDB", "CBMS1411", "cbms", "1521");
			con = m.getConnection();
			DBUtils util = new DBUtils();
			while(end <= length){
				tmp = strBuf.substring(start, end);
//				tmp = "";
				sql = updSql + tmp+"' where TIMESTAMP_NO='2014122012304500878700963974'";
				
				if(!util.executeUpdate(con, sql)){
					break;
				}
				if(end >= length){
					System.out.println("当前位置--->:"+end);
					break;
				}
				start = end;
				end += 1000;
				if(end > length){
					end = length;
				}
				System.out.println("当前位置:"+end);
			}
			System.out.println("ok");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(con != null){
					con.close();
				}
			}catch(Exception e1){
				e1.printStackTrace();
			}
		}
		
	}
}
