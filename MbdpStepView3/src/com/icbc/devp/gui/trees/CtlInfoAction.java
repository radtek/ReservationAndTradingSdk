package com.icbc.devp.gui.trees;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.icbc.devp.bean.db.MbdpCtlBean;
import com.icbc.devp.gui.dialog.CtlDialog;
import com.icbc.devp.tool.util.EnvUtil;

public class CtlInfoAction implements ActionListener {

	public CtlInfoAction(CtlTreeNode treeNode){
		this.treeNode = treeNode;
		dlg = new CtlDialog();
		showPos = new Point(0,0);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
//		JMenuItem item = (JMenuItem)e.getSource();
//		Point p = new Point(0,0);
		
		
		MbdpCtlBean bean = treeNode.getCtl(nodeName);
		if(bean == null){
			return;
		}
//		EnvUtil.convertToScreenPoint(this.showPos, (JComponent)e.getSource());
		this.showPos = EnvUtil.getRightPoint(this.showPos, this.dlg.getSize());
		dlg.showCtlInfo(bean, this.showPos.x, this.showPos.y);
	}
	
	public void setNodeName(String nodeName, int x, int y){
		this.nodeName = nodeName;
		this.showPos.x = x;
		this.showPos.y = y;
	}

	private CtlTreeNode treeNode;
	private String nodeName;
	private Point showPos;
	private CtlDialog dlg;
}
