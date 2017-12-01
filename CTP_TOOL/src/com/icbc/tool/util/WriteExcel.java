package com.icbc.tool.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.icbc.tool.ctp3.FileObj;
import com.icbc.tool.db.CTPBean;
import com.icbc.tool.db.DBTableBean;
import com.icbc.tool.db.OPGBean;
import com.icbc.tool.db.USER_SEQUENCES_Bean;

import jxl.*;
import jxl.write.*;
import jxl.write.biff.RowsExceededException;

public class WriteExcel {

	final int sheetBase = 50000;

	public void WriteExcel(List<CTPBean> res, String fileName) throws Exception {
		if (fileName.length() > 200) {
			String[] nameArr = fileName.split("_");
			if (nameArr.length > 0) {			
			  fileName = nameArr[0];
			}
		}
		WritableWorkbook workbook = Workbook.createWorkbook(new File(
				"D:\\MenuLinkedPaths\\" + fileName + "关联文件清单.xls"));
		WritableSheet sheet = workbook.createSheet(fileName + "关联文件", 0);
		Label label1 = new Label(0, 0, "菜单名");
		Label label2 = new Label(1, 0, "所在的Flowc");
		Label label3 = new Label(2, 0, "Flowc显示的名字");
		Label label4 = new Label(3, 0, "文件名称");
		Label label5 = new Label(4, 0, "OPG所包含的OPStep内容");
		Label label6 = new Label(5, 0, "OPG对应的存储过程涉及的数据库表");
		Label label7 = new Label(6, 0, "涉及的接口清单");
		sheet.addCell(label1);
		sheet.addCell(label2);
		sheet.addCell(label3);
		sheet.addCell(label4);
		sheet.addCell(label5);
		sheet.addCell(label6);
		sheet.addCell(label7);
		int Rows = res.size();
		for (int i = 0; i < Rows; ++i) {
			label1 = new Label(0, i + 1, res.get(i).getItemName());
			label2 = new Label(1, i + 1, res.get(i).getFlowcName());
			label3 = new Label(2, i + 1, res.get(i).getDisplayName());
			label4 = new Label(3, i + 1, res.get(i).getFileName());
			label5 = new Label(4, i + 1, showOPG(res.get(i).getOpgBean(), res
					.get(i).getFileName()));
			label6 = new Label(5, i + 1, showOPGRefTable(res.get(i)
					.getOpgBean(), res.get(i).getFileName()));
			//label7 = new Label(6, i + 1,res.get(i).getDsrName().toString());
			
			sheet.addCell(label1);
			sheet.addCell(label2);
			sheet.addCell(label3);
			sheet.addCell(label4);
			sheet.addCell(label5);
			sheet.addCell(label6);
			//sheet.addCell(label7);
			System.out.println(res.get(i).getItemName() + " "+
					           res.get(i).getFlowcName() + " "+
					           res.get(i).getDisplayName() + " "+
					           res.get(i).getFileName() + " "+
					           showOPG(res.get(i).getOpgBean(), res
										.get(i).getFileName()) + " "+
							   showOPGRefTable(res.get(i)
												.getOpgBean(), res.get(i).getFileName()) + " "
							  /* res.get(i).getDsrName().toString()*/ );
		}
		workbook.write();
		workbook.close();
	}

	public void WriteLinkedExcel(List<ArrayList<String>> res, String fileName)
			throws Exception {
		int len, lengthOfPaths = res.size();
		if (lengthOfPaths == 0)
			return;
		if (fileName.length() > 200) {
			String[] nameArr = fileName.split("_");
			//for (String name : nameArr)System.out.print(name + " ");
			if (nameArr.length > 0) {
			
			   fileName = nameArr[0];
			}
		}
		WritableWorkbook wr = Workbook.createWorkbook(new File(
				"D:\\MenuLinkedPaths\\" + fileName + "关联路径.xls"));
		System.out.println(fileName + " 共 " + lengthOfPaths + " 条路径！");
		int i = 0, sheetCnt = lengthOfPaths / sheetBase;
		for (int sheetIdx = 0; sheetIdx <= sheetCnt && i < lengthOfPaths; ++sheetIdx) {
			WritableSheet sheet = wr.createSheet(fileName + "关联路径" + sheetIdx,
					sheetIdx);
			Label label;
			for (int k = 0; k < sheetBase && i < lengthOfPaths; ++k, ++i) {
				label = new Label(0, k, "第  " + (i + 1) + "条路径");
				sheet.addCell(label);
				len = res.get(i).size();
				for (int j = 0; j < len; ++j) {
					label = new Label(j + 1, k, res.get(i).get(j));
					sheet.addCell(label);
				}
			}
		}
		wr.write();
		wr.close();
	}
	
	public void WriteDBProcRefTable(HashMap<String, HashSet<String>> pckgRefTable,String fileName) throws Exception{
		WritableWorkbook wr = Workbook.createWorkbook(new File(
				"D:\\MenuLinkedPaths\\" + fileName + ".xls"));
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
				"D:\\MenuLinkedPaths\\" + fileName + ".xls"));
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
	
    public void WriteCTP3Menu(HashMap<String, FileObj> passFileObjMap,String fileName) throws Exception{
    	WritableWorkbook workbook = Workbook.createWorkbook(new File(
				"D:\\MenuLinkedPaths\\" + fileName + "关联文件清单.xls"));
		WritableSheet sheet = workbook.createSheet(fileName + "CTP3关联菜单", 0);
		
	}

	public String showOPG(List<OPGBean> OPG, String fileType) {	
		StringBuilder result = new StringBuilder();
		if (OPG != null) {
			for (OPGBean opg : OPG) {
				if (opg.getDisplayName() != null)
					result.append(opg.getOPGtype() + "^" + opg.getDisplayName()
							+ "[");
				if (opg.getOpName() != null) {
					int len = opg.getOpName().size();
					for (int i = 0; i < len - 1; ++i) {
						result.append(opg.getOpName().get(i) + "^"
								+ opg.getOpDesc().get(i) + " , ");
					}
					if (len > 0)
						result.append(opg.getOpName().get(len - 1) + "^"
								+ opg.getOpDesc().get(len - 1));
				}
				result.append("] ");
			}
		}
		if (result.toString() != "" && result.toString() != null)
			return "(" + result.toString() + ")";
		else
			return result.toString();
	}

	public String showOPGRefTable(List<OPGBean> OPG, String fileType) {
		if (fileType == null)
			return "";
		if (fileType.toLowerCase().indexOf(".opg") == -1)
			return "";
		StringBuilder result = new StringBuilder();
		if (OPG != null) {
			for (OPGBean opg : OPG) {
				if (opg.getProcedureRefTable() != null) {
					for (Object obj : opg.getProcedureRefTable().keySet()) {
						result.append(obj + "{ "
								+ opg.getProcedureRefTable().get(obj) + " }");
					}
				}
			}
		}
		return result.toString();
	}
	
}
