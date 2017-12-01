/**
 * 
 */
package com.icbc.devp.gui.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;

import com.icbc.devp.relstep.RelStep;

/**
 * @author kfzx-liangxch
 *
 */
public class FindStepBtn extends JButton implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private FindStepAction pane;
	private String tbname;
	private RelStep relStep;
	private ArrayList<ArrayList<String>> result;
	private ArrayList<String> resultr;
	private ArrayList<String> resultu;
	private StringBuffer text;
	/**
	 * 
	 */
	public FindStepBtn(String desc, FindStepAction findStepPane) {
		this.setText(desc);
		this.pane = findStepPane;
		this.addActionListener(this);
		this.result = new ArrayList<ArrayList<String>>();
		this.resultr = new ArrayList<String>();
		this.resultu = new ArrayList<String>();
		this.text = new StringBuffer();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		this.tbname = this.pane.getTbname();
		this.pane.setReadStep("");
		this.pane.setUpdateStep("");
		
		try {
			this.relStep = new RelStep();
		} catch (Exception e) {
			this.pane.setReadStep("连接数据库失败");
			e.printStackTrace();
			return;
		}
		
		if (this.tbname.equals("")) {
			this.pane.setReadStep("请输入表名");
			return;
		}
		
		try {
			if (!this.relStep.isTable(this.tbname)) {
				this.pane.setReadStep(this.tbname + "不是表名");
				return;
			}
			
			this.result = this.relStep.getStepRUByTbname(this.tbname);
			int i = 0;
			resultr = result.get(0);
			resultu = result.get(1);
			String lineSep = System.getProperty("line.separator", "\r\n");
			text.delete(0, text.length());
			for (i = 0; i < resultr.size() - 1; ++i) {
				text.append(resultr.get(i) + lineSep);
			}
			if (resultr.size() > 0) {
				text.append(resultr.get(i));
			} else {
				text.append("无");
			}
			this.pane.setReadStep(text.toString());
			
			text.delete(0, text.length());
			for (i = 0; i < resultu.size() - 1; ++i) {
				text.append(resultu.get(i) + lineSep);
			}
			if (resultu.size() > 0) {
				text.append(resultu.get(i));
			} else {
				text.append("无");
			}
			this.pane.setUpdateStep(text.toString());

			
		} catch (Exception e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		
	}

}
