package com.icbc.devp.gui.action;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.icbc.devp.gui.UiContext;
import com.icbc.devp.gui.info.StepInfo;
import com.icbc.devp.gui.tabpane.StepInfoPane;
import com.icbc.devp.txtdraw.base.Info;

public class NewNodeAction implements ActionListener {

	public NewNodeAction(StepInfoPane pane){
		this.pane = pane;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		StepInfo info = new StepInfo();
		Info step = new Info();
		Info tmp = pane.getInfoTree().getRoot().getInfo();
		step.setApp(tmp.getApp());
		step.setCycle(tmp.getCycle());
		step.setDataSource(tmp.getDataSource());
		step.setEnableFlag("1");
		step.setProc("");
		step.setProcDesc("");
		step.setProcType("");
		step.setParameter("");
		
		//info
		info.setStepInfo(step);
		info.setStartPoint(new Point(x,y));
		info.setSize(UiContext.getInstance().getNodeWidth(), UiContext.getInstance().getNodeHeight());
		info.setHeight(2);
		String stepno;
		for(int i=4000;i<9999;i++){
			stepno = "TEMP-"+i;
			if(this.pane.getInfoTree().getNode(stepno) == null){
				step.setStepno(stepno);
				break;
			}
		}
		info.setNewNodeFlag(true);
		//·Å½øÈ¥
		this.pane.getInfoTree().setNode(info);
	}
	
	public void setLocation(int x, int y){
		this.x = x;
		this.y = y;
	}

	private int x;
	private int y;
	private StepInfoPane pane;
}
