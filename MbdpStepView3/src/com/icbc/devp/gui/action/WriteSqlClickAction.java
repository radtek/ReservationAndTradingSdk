package com.icbc.devp.gui.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.icbc.devp.gui.info.StepInfo;
import com.icbc.devp.gui.info.StepInfoTree;
import com.icbc.devp.gui.tabpane.StepInfoPane;
import com.icbc.devp.tool.date.DateUtil;
import com.icbc.devp.tool.file.FileUtil;
import com.icbc.devp.tool.util.EnvUtil;
import com.icbc.devp.txtdraw.base.Info;

public class WriteSqlClickAction implements ActionListener {

	protected StepInfoPane pane;
	
	public WriteSqlClickAction(StepInfoPane pane){
		this.pane = pane;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		StepInfoTree tree = this.pane.getInfoTree();
		if(tree == null){
			return;
		}
		HashMap<String, StepInfo> infoMap = tree.getStepInfoMap();
		if(infoMap == null || infoMap.isEmpty()){
			return;
		}
		this.writeDefSqlToDisk(infoMap, "MBDP_B_STEPDEF_C");
		this.writeRelSqlToDisk(infoMap,"MBDP_B_STEPREL_C");
	}
	
	/**TODO:生成SQL文件到执行目录中*/
	public void writeDefSqlToDisk(HashMap<String, StepInfo> infoMap, String fileName){
		File dir = new File(EnvUtil.getInstance().getRootPath(), "sql" );
		if(!dir.isDirectory()){
			dir.mkdirs();
		}
		
		ArrayList<String> lineList = new ArrayList<String>();
		StepInfo infoNode;
		Info info;
		int i = 0;
		String line;
		for(Iterator<StepInfo> it = infoMap.values().iterator(); it.hasNext(); ){
			infoNode = it.next();
			info = infoNode.getInfo();
			if(info.isStartNode()){
				continue;
			}
			line = "insert into MBDP_B_STEPDEF(APP, CYCLE, DATA_SOURCE, PROG_TYPE, STEPNO, PROG, PROG_NAME, PARAMETER, DEFDATE, WEIGHT, ENABLE_FLAG)\r\n values('"
				 + info.getApp() +"', '"+info.getCycle()+"', '"+info.getDataSource()+"', '"+info.getProcType()+"', '"+info.getStepno()+"', '"
				 + info.getProc()+"', '"+(info.getProcDesc()==null? "" : info.getProcDesc().replace("'", "''"))+"', '"+(info.getParameter()==null? "" : info.getParameter().replace("'", "''"))+"', '"+DateUtil.getCurrdate("yyyy-MM-dd")+"', '0', '"+info.getEnableFlag()+"');\r\n";
			if(i>0){
				lineList.add(line);
			}else{
				lineList.add("delete from MBDP_B_STEPDEF where APP='"+info.getApp() +"' and CYCLE='"+info.getCycle()+"' and DATA_SOURCE='"+info.getDataSource()+"';\r\n");
				lineList.add(line);
				fileName += "_"+info.getApp()+"_"+info.getCycle()+"_"+info.getDataSource()+".sql";
			}
			i++;
		}
		File file  = new File(dir, fileName);
		FileUtil.writeFile(file.getAbsolutePath(), false, lineList);
	}
	
	public void writeRelSqlToDisk(HashMap<String, StepInfo> infoMap, String fileName){
		File dir = new File(EnvUtil.getInstance().getRootPath(), "sql" );
		if(!dir.isDirectory()){
			dir.mkdirs();
		}
		
		ArrayList<String> lineList = new ArrayList<String>();
		StepInfo infoNode;
		Info info, sInfo;
		int count = 0;
		String line;
		HashMap<String, String> chkMap = new HashMap<String, String>();
		String id;
		ArrayList<StepInfo> sList;
		for(Iterator<StepInfo> it = infoMap.values().iterator(); it.hasNext(); ){
			infoNode = it.next();
			info = infoNode.getInfo();
			sList = infoNode.getSList();
			if(sList == null || sList.isEmpty()){
				continue;
			}
			for(int i=0; i<sList.size(); i++){
				sInfo = sList.get(i).getInfo();
				id = info.getId()+"#"+sInfo.getId();
				if(chkMap.get(id) != null){
					continue;
				}
				line = "insert into MBDP_B_STEPREL(APP, CYCLE, DATA_SOURCE, STEPNO_P, STEPNO) values('"+info.getApp()+"', '"+info.getCycle()+"', '"
				     + info.getDataSource()+"', '" + (info.isStartNode() ? "N/A" : info.getStepno()) + "', '"+sInfo.getStepno()+"');\r\n";
				if(count > 0){
					lineList.add(line);
				}else{
					lineList.add("delete from MBDP_B_STEPREL where APP='"+info.getApp() +"' and CYCLE='"+info.getCycle()+"' and DATA_SOURCE='"+info.getDataSource()+"';\r\n");
					lineList.add(line);
					fileName += "_"+info.getApp()+"_"+info.getCycle()+"_"+info.getDataSource()+".sql";
				}
				count ++;
				chkMap.put(id, "1");
			}
		}
		File file  = new File(dir, fileName);
		FileUtil.writeFile(file.getAbsolutePath(), false, lineList);
	}

	
}
