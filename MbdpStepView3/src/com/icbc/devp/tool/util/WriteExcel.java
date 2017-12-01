package com.icbc.devp.tool.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;




import com.icbc.devp.expFileddlChk.DBTableBean;
import com.icbc.devp.expFileddlChk.USER_SEQUENCES_Bean;

import jxl.*;
import jxl.write.*;
import jxl.write.biff.RowsExceededException;

public class WriteExcel {

	final int sheetBase = 50000;
   
	public void WriteDBProcRefTable(HashMap<String, HashSet<String>> pckgRefTable,String fileName) throws Exception{
		/**
		 * 改成当前目录下生成文件
		 */
		WritableWorkbook wr = Workbook.createWorkbook(new File(
				System.getProperty("user.dir") +File.separator+ fileName + ".xls"));
		//System.out.println(System.getProperty("user.dir") +File.separator+ fileName + ".xls");
		WritableSheet sheet = wr.createSheet(fileName + "存储过程与数据库表对应关系", 0);
		Label label1 = new Label(0, 0, "存储过程名");
		Label label2 = new Label(1, 0, "操作的数据库表1");
		Label label3 = new Label(2, 0, "操作的数据库表2");
		Label label4 = new Label(3, 0, "操作的数据库表3");
		Label label5 = new Label(4, 0, "操作的数据库表4");
		sheet.addCell(label1);		sheet.addCell(label2);
		sheet.addCell(label3);		sheet.addCell(label4);
		sheet.addCell(label5);		
		int i = 1, j;
		for (Object obj : pckgRefTable.keySet()) {
			label1 = new Label(0, i, obj.toString());
			sheet.addCell(label1);
			j = 1;
			for (String dbt : pckgRefTable.get(obj)) {
				// 每4个换行
				if(j % 5 ==0){
					++i;
					j = 1;
				}
				label2 = new Label(j, i, dbt);
				sheet.addCell(label2);
				++j;
			}
			++i;
		}
		wr.write();
		wr.close();
	}
    	
	public void WriteDBTableRefProc(
			HashMap<String, HashSet<DBTableBean>> dbTableRefProc,HashSet<USER_SEQUENCES_Bean>user_Seqs,
			String fileName) throws IOException, Exception {
		WritableWorkbook wr = Workbook.createWorkbook(new File(
				System.getProperty("user.dir") +File.separator + fileName + ".xls"));
		//System.out.println(System.getProperty("user.dir") +File.separator+ fileName + ".xls");
		WritableSheet sheet = wr.createSheet(fileName + "数据库表与存储过程对应关系", 0);
		Label label1 = new Label(0, 0, "数据库表名");
		Label label2 = new Label(1, 0, "涉及的存储过程1");
		Label label3 = new Label(2, 0, "涉及的存储过程2");
		Label label4 = new Label(3, 0, "涉及的存储过程3");
		Label label5 = new Label(4, 0, "涉及的存储过程4");
		sheet.addCell(label1);		sheet.addCell(label2);
		sheet.addCell(label3);		sheet.addCell(label4);
		sheet.addCell(label5);		
		int i = 1, j;
		for (Object obj : dbTableRefProc.keySet()) {
			label1 = new Label(0, i, obj.toString());
			sheet.addCell(label1);
			j = 1;
			for (DBTableBean dbt : dbTableRefProc.get(obj)) {
				// 每4个换行
				if(j % 5 ==0){
					++i;
					j = 1;
				}
				label2 = new Label(j, i, dbt.getPckgName() + "[ "
						+ dbt.getProcedureName() + " : " + dbt.getOperType()
						+ " ]");
				sheet.addCell(label2);
				++j;
				
			}
			++i;
		}
		
		WritableSheet sheet2 = wr.createSheet(fileName + "seq与存储过程的关系", 1);
		 label1 = new Label(0, 0, "sequence英文名");
		 label2 = new Label(1, 0, "最小序号");
		 label3 = new Label(2, 0, "最大序号");
		 label4 = new Label(3, 0, "是否循环");
		 label5 = new Label(4, 0, "Cache_Size");
		 Label label6 = new Label(5, 0, "涉及的存储过程");
		 sheet2.addCell(label1);		 sheet2.addCell(label2);
		 sheet2.addCell(label3);		 sheet2.addCell(label4);
		 sheet2.addCell(label5);		 sheet2.addCell(label6);
		 i = 1 ;
		 for(USER_SEQUENCES_Bean seq : user_Seqs)
		 {  
			 label1 = new Label(0,i,seq.getSequence_name());
			 label2 = new Label(1,i,seq.getMin_value());
			 label3 = new Label(2,i,seq.getMax_value());
			 label4 = new Label(3,i,seq.getCycle_flag());
			 label5 = new Label(4,i,seq.getCache_size());
			 sheet2.addCell(label1);		 sheet2.addCell(label2);
			 sheet2.addCell(label3);		 sheet2.addCell(label4);
			 sheet2.addCell(label5);		 
			 for(String proc: seq.getProcs()){
				 label6 = new Label(5,i,proc);
				 sheet2.addCell(label6);
				 ++i;
			 }
			 if(seq.getProcs().size()==0)
				 ++i;
		 }
		wr.write();
		wr.close();
	}
	
	public void WriteDDLdbTableRefFile(String fileName,HashMap<String,HashSet<String>>tableRefFiles,
			HashMap<String,HashSet<String>>tableRefFilesAll)throws IOException, Exception {
		WritableWorkbook wr = Workbook.createWorkbook(new File(
				System.getProperty("user.dir") +File.separator + fileName + "_DDLRefFiles.xls"));
		WritableSheet sheet = wr.createSheet(fileName + "_DDL表与导出文件的对应关系", 0);
		Label label1 = new Label(0, 0, "DDL相关的数据库表名");
		Label label2 = new Label(1, 0, "涉及的批量导出文件");
		sheet.addCell(label1);
		sheet.addCell(label2);
		int i=1,j;
	    for(String  obj : tableRefFiles.keySet()){
	    	 j=0;
	    	 label1 = new Label(j,i,obj);
	    	 sheet.addCell(label1);
	    	 for(String ele : tableRefFiles.get(obj)){
	    		 ++j;
	    		label2 = new Label(j,i,ele);
	    		sheet.addCell(label2);
	    		
	    	 }
	    	 ++i;
	    }
		
	    WritableSheet sheet2 = wr.createSheet(fileName + "数据库表与导出文件的对应关系", 1);
		label1 = new Label(0, 0, "数据库表名");
		label2 = new Label(1, 0, "涉及的批量导出文件");
		sheet2.addCell(label1);
		sheet2.addCell(label2);
		i=1;
		for(String  obj : tableRefFilesAll.keySet()){
	    	 j=0;
	    	 label1 = new Label(j,i,obj);
	    	 sheet2.addCell(label1);
	    	 for(String ele : tableRefFilesAll.get(obj)){
	    		 ++j;
	    		label2 = new Label(j,i,ele);
	    		sheet2.addCell(label2);
	    		
	    	 }
	    	 ++i;
	    }
	    
		wr.write();
		wr.close();
	}
	
}
