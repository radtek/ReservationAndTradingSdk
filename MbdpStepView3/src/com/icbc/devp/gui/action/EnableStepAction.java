package com.icbc.devp.gui.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;

import com.icbc.devp.gui.info.DbDataUpdate;
import com.icbc.devp.gui.info.StepInfo;
import com.icbc.devp.gui.tabpane.StepInfoPane;
import com.icbc.devp.tool.log.Log;

/**TODO:更新 MBDP_B_STEPDEF的enable_flag*/
public class EnableStepAction implements ActionListener {

	public EnableStepAction(StepInfoPane pane){
		this.pane = pane;
		updater = new DbDataUpdate();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		try{
			StepInfo info = this.pane.getSelectedNode();
			if(info == null && this.pane.getSelectedList().isEmpty() && this.pane.getSelectedMap().isEmpty()){
				return;
			}
			String flag = info.isEnable() ? "0" : "1";
			boolean isEnable = info.isEnable() ? false : true;
			if(this.pane.getSelectedList().isEmpty() && this.pane.getSelectedMap().isEmpty()){
				if(info.isStartNode()){
					return;
				}
				if(updater.updateEnableFlag(info.getInfo().getApp(), info.getInfo().getCycle(), info.getInfo().getDataSource(), info.getInfo().getStepno(), !info.isEnable())){
					info.getInfo().setEnableFlag(flag);
					info.repaintImage();
					this.pane.repaint();
				}else{
					Log.getInstance().error("[EnableStepAction.actionPerformed]更新"+info.getId()+"失败！");
				}
			}else{
				ArrayList<StepInfo> infoList = this.pane.getSelectedList();
				stepList.clear();
				if(this.pane.getSelectedNode() != null && !this.pane.getSelectedNode().isStartNode()){
					stepList.add(this.pane.getSelectedNode().getId());
				}
				for(int i=0; i<infoList.size(); i++){
					if(infoList.get(i).getInfo().isStartNode()){
						continue;
					}
					stepList.add(infoList.get(i).getInfo().getStepno());
				}
				for(Iterator<Entry<String,StepInfo>> it = this.pane.getSelectedMap().entrySet().iterator(); it.hasNext();){
					info = it.next().getValue();
					if(info.isStartNode()){
						continue;
					}
					stepList.add(info.getInfo().getStepno());
				}
				if(updater.updateEnableFlag(info.getInfo().getApp(), info.getInfo().getCycle(), info.getInfo().getDataSource(), stepList, isEnable)){
					for(int i=0; i<infoList.size(); i++){
						if(infoList.get(i).isStartNode()){
							continue;
						}
						infoList.get(i).getInfo().setEnableFlag(flag);
						infoList.get(i).repaintImage();
					}
					for(Iterator<Entry<String,StepInfo>> it = this.pane.getSelectedMap().entrySet().iterator(); it.hasNext();){
						info = it.next().getValue();
						if(info.isStartNode()){
							continue;
						}
						info.getInfo().setEnableFlag(flag);
						info.repaintImage();
					}
					if(this.pane.getSelectedNode() != null && !this.pane.getSelectedNode().isStartNode()){
						this.pane.getSelectedNode().getInfo().setEnableFlag(flag);
						this.pane.getSelectedNode().repaintImage();
					}
					this.pane.repaint();
				}else{
					Log.getInstance().error("[EnableStepAction.actionPerformed]更新"+info.getId()+"失败！");
				}
			}

		}catch(Exception ex){
			Log.getInstance().exception(ex);
		}
	}

	private StepInfoPane pane;
	private DbDataUpdate updater;
	private ArrayList<String> stepList = new ArrayList<String>();
}
