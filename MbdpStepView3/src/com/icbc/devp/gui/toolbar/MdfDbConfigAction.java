package com.icbc.devp.gui.toolbar;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import com.icbc.devp.tool.util.EnvUtil;

public class MdfDbConfigAction implements ActionListener {

	public MdfDbConfigAction(){
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		JButton btn = (JButton)e.getSource();
		Point p = new Point(btn.getSize().width,btn.getSize().height);
		EnvUtil.convertToScreenPoint(p, btn);
		EnvUtil.getRightPoint(p, dlg.getSize());
		this.dlg.setLocation(p);
		this.dlg.showInfo();
		
	}
	
	/**TODO:���ö�Ӧ��dlg*/
	public void setDialog(DbcfgDialog dlg){
		this.dlg = dlg;
	}
	
	private DbcfgDialog dlg;
}
