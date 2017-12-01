package com.icbc.devp.gui.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import com.icbc.devp.gui.info.StepInfo;
import com.icbc.devp.gui.info.StepInfoTree;
import com.icbc.devp.gui.tabpane.StepInfoPane;
import com.icbc.devp.tool.date.DateUtil;
import com.icbc.devp.tool.file.FileUtil;
import com.icbc.devp.tool.util.EnvUtil;
import com.icbc.devp.txtdraw.base.Info;

/**TODO:将选中的节点生成SQL文件，包括关系和步骤定义*/
public class WriteSelectedSqlClickAction implements ActionListener {
	
	protected StepInfoPane pane;
	
	public WriteSelectedSqlClickAction(StepInfoPane pane){
		this.pane = pane;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		StepInfoTree tree = this.pane.getInfoTree();
		if(tree == null){
			return;
		}
		
		ArrayList<StepInfo> infoList = this.pane.getSelectedList();
		
		if(infoList == null || infoList.isEmpty()){
			return;
		}
		this.writeDefSqlToDisk(infoList,"MBDP_B_STEPDEF_Z");
		this.writeRelSqlToDisk(infoList,"MBDP_B_STEPREL_Z");
	}
	
	/**TODO:生成SQL文件到执行目录中*/
	public void writeDefSqlToDisk(ArrayList<StepInfo> infoList, String fileName){
		File dir = new File(EnvUtil.getInstance().getRootPath(), "sql" );
		if(!dir.isDirectory()){
			dir.mkdirs();
		}
		
		ArrayList<String> lineList = new ArrayList<String>();
//		StepInfo infoNode;
		Info info = null;
		String line;
		for(int i=0; i<infoList.size(); i++){
			info = infoList.get(i).getInfo();
			if(info.isStartNode()){
				continue;
			}
			lineList.add("delete from MBDP_B_STEPDEF where APP='"+info.getApp() +"' and CYCLE='"+info.getCycle()+"' and DATA_SOURCE='"+info.getDataSource()
			  		    +"' and stepno='"+info.getStepno()+"';\r\n");
			line = "insert into MBDP_B_STEPDEF(APP, CYCLE, DATA_SOURCE, PROG_TYPE, STEPNO, PROG, PROG_NAME, PARAMETER, DEFDATE, WEIGHT, ENABLE_FLAG)\r\n values('"
				 + info.getApp() +"', '"+info.getCycle()+"', '"+info.getDataSource()+"', '"+info.getProcType()+"', '"+info.getStepno()+"', '"
				 + info.getProc()+"', '"+(info.getProcDesc()==null? "" : info.getProcDesc().replace("'", "''"))+"', '"+(info.getParameter()==null? "" : info.getParameter().replace("'", "''"))+"', '"+DateUtil.getCurrdate("yyyy-MM-dd")+"', '0', '"+info.getEnableFlag()+"');\r\n";
			
			lineList.add(line);
		}
		if(info == null || lineList.isEmpty()){
			return;
		}
		fileName += "_"+info.getApp()+"_"+info.getCycle()+"_"+info.getDataSource()+".sql";
		File file  = new File(dir, fileName);
		FileUtil.writeFile(file.getAbsolutePath(), false, lineList);
	}

	/**TODO:生成SQL文件到执行目录中*/
	public void writeRelSqlToDisk(ArrayList<StepInfo> infoList, String fileName){
		File dir = new File(EnvUtil.getInstance().getRootPath(), "sql" );
		if(!dir.isDirectory()){
			dir.mkdirs();
		}
		ArrayList<String> lineList = new ArrayList<String>();
		StepInfo infoNode;
		Info info = null;
		Info tmpInfo;
		String line;
		HashMap<String, String> chkMap = new HashMap<String, String>();
		ArrayList<StepInfo> sList;
		ArrayList<StepInfo> fList;
		for(int i=0; i<infoList.size(); i++){
			infoNode = infoList.get(i);
			info = infoNode.getInfo();
			if(info.isStartNode()){
				continue;
			}
			
			fList = infoNode.getFList();
			sList = infoNode.getSList();
			if(fList != null){
				for(int k=0; k<fList.size(); k++){
					tmpInfo = fList.get(k).getInfo();
					if(chkMap.get(tmpInfo.getStepno()+"#"+info.getStepno()) != null){
						continue;
					}
					lineList.add("delete from MBDP_B_STEPREL where APP='"+info.getApp() +"' and CYCLE='"+info.getCycle()+"' and DATA_SOURCE='"+info.getDataSource()
							+"' and stepno_p='"+(tmpInfo.isStartNode() ? "N/A" : tmpInfo.getStepno())+"' and stepno='"+info.getStepno()+"';");
					line = "insert into MBDP_B_STEPREL(APP, CYCLE, DATA_SOURCE, STEPNO_P, STEPNO) values('"+info.getApp()+"', '"+info.getCycle()+"', '"
				     + info.getDataSource()+"', '" + (tmpInfo.isStartNode() ? "N/A" : tmpInfo.getStepno()) + "', '"+info.getStepno()+"');\r\n";
					lineList.add(line);
					chkMap.put(tmpInfo.getStepno()+"#"+info.getStepno(), "1");
				}
			}
			if(sList != null){
				for(int k=0; k<sList.size(); k++){
					tmpInfo = sList.get(k).getInfo();
					if(chkMap.get(info.getStepno() +"#"+ tmpInfo.getStepno()) != null){
						continue;
					}
					lineList.add("delete from MBDP_B_STEPREL where APP='"+info.getApp() +"' and CYCLE='"+info.getCycle()+"' and DATA_SOURCE='"+info.getDataSource()
							+"' and stepno_p='"+(info.isStartNode() ? "N/A" : info.getStepno())+"' and stepno='"+tmpInfo.getStepno()+"';");
					line = "insert into MBDP_B_STEPREL(APP, CYCLE, DATA_SOURCE, STEPNO_P, STEPNO) values('"+info.getApp()+"', '"+info.getCycle()+"', '"
				     + info.getDataSource()+"', '" + (info.isStartNode() ? "N/A" : info.getStepno()) + "', '"+tmpInfo.getStepno()+"');\r\n";
					lineList.add(line);
					chkMap.put(info.getStepno() +"#"+ tmpInfo.getStepno(), "1");
				}
			}
		}
		if(info == null || lineList.isEmpty()){
			return;
		}
		
		fileName += "_"+info.getApp()+"_"+info.getCycle()+"_"+info.getDataSource()+".sql";
		File file  = new File(dir, fileName);
		FileUtil.writeFile(file.getAbsolutePath(), false, lineList);
	}
}
