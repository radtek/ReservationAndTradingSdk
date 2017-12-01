package com.icbc.devp.gui.tabpane;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import com.icbc.devp.gui.info.StepInfo;

/**TODO:加载关联的步骤信息*/
public class ActiveAction implements ActionListener {

	public ActiveAction(StepInfoPane pane){
		super();
		this.pane = pane;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		StepInfo info = this.pane.getSelectedNode();
		if(info == null){
			return;
		}
		//开始选择咯
		HashMap<String, StepInfo> infoMap = this.pane.getSelectedMap();
		infoMap.clear();
		infoMap.put(info.getId(), info);
		this.findFatherNodes(info, infoMap);
		this.findSonNodes(info, infoMap);
		this.pane.repaint();
	}
	
	private void findFatherNodes(StepInfo info, HashMap<String, StepInfo> infoMap){
		if(!info.hasFather()){
			return;
		}
		ArrayList<StepInfo> fList = info.getFList();
		StepInfo tmp;
		for(int i=0; i<fList.size(); i++){
			tmp = fList.get(i);
			infoMap.put(tmp.getId(), tmp);
			this.findFatherNodes(tmp, infoMap);
		}
		return;
	}
	
	private void findSonNodes(StepInfo info, HashMap<String, StepInfo> infoMap){
		if(!info.hasSon()){
			return;
		}
		ArrayList<StepInfo> sList = info.getSList();
		StepInfo tmp;
		for(int i=0; i<sList.size(); i++){
			tmp = sList.get(i);
			infoMap.put(tmp.getId(), tmp);
			this.findSonNodes(tmp, infoMap);
		}
		return;
	}

	private StepInfoPane pane;
}
