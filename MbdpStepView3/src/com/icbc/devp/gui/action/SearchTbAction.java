/**
 * 
 */
package com.icbc.devp.gui.action;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicBorders;

import com.icbc.devp.gui.info.StepInfo;
import com.icbc.devp.gui.tabpane.StepInfoPane;
import com.icbc.devp.relstep.RelStep;
import com.icbc.devp.tool.util.EnvUtil;
import com.icbc.devp.txtdraw.base.Info;

/**
 * @author kfzx-liangxch
 *
 */
public class SearchTbAction extends JDialog  implements ActionListener {
	
	private StepInfoPane pane;
	private StepInfo stepInfo;
	private GridBagLayout layout;
	private GridBagConstraints cons;
	private JLabel stepnoLabel;
	private JLabel typeLabel;
	private JLabel progLabel;
	private JLabel parameterLabel;
	private JLabel descLabel;
	private JLabel readLabel;
	private JLabel updateLabel;
	private JTextField stepnoText; 
	private JTextField typeText; 
	private JTextField progText; 
	private JTextArea parameterArea; 
	private JTextField descText; 
	private JTextArea readArea; 
	private JTextArea updateArea; 
	private JScrollPane updateJsp;
	private JScrollPane readJsp;
	private JScrollPane paramJsp;
	
	public SearchTbAction(StepInfoPane pane) {
		super();
		cons = new GridBagConstraints();
		layout = new GridBagLayout();
		this.setLayout(layout);
		this.setSize(500, 400);
		this.setModal(true);
		this.pane = pane;
		this.init();
	}
	/* 
	 * 
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		stepInfo = pane.getSelectedNode();
		
		this.setTitle(stepInfo.getInfo().getStepno() + "步骤访问的表");
		this.setInfo(stepInfo);
		this.setVisible(true);
		
	}
	
	private void init() {
		this.stepnoLabel = new JLabel("步骤 ID:");
		this.typeLabel   = new JLabel("构件类型:");
		this.progLabel   = new JLabel("程    序:");
		this.parameterLabel = new JLabel("入    参:");
		this.descLabel = new JLabel("程序描述:");
		this.readLabel = new JLabel("读取的表:",JLabel.CENTER);
		this.updateLabel    = new JLabel("更新的表:",JLabel.CENTER);
		
		this.stepnoLabel.setHorizontalAlignment(JLabel.CENTER);
		this.typeLabel.setHorizontalAlignment(JLabel.CENTER);
		this.progLabel.setHorizontalAlignment(JLabel.CENTER);
		this.parameterLabel.setHorizontalAlignment(JLabel.CENTER);
		this.descLabel.setHorizontalAlignment(JLabel.CENTER);
		//还有显示
		this.stepnoText = new JTextField();
		this.typeText = new JTextField();
		this.progText = new JTextField();
		this.parameterArea = new JTextArea();
		this.descText = new JTextField();
		this.readArea = new JTextArea();
		this.updateArea = new JTextArea();
		this.stepnoText.setEditable(false);
		this.typeText.setEditable(false);
		this.progText.setEditable(false);
		this.parameterArea.setEditable(false);
		this.descText.setEditable(false);
		this.readArea.setEditable(false);
		this.updateArea.setEditable(false);
		
		this.parameterArea.setAutoscrolls(true);
		//开始
		this.parameterArea.setLineWrap(true);	//自动换行
		this.parameterArea.setWrapStyleWord(true);	//不断字
		this.readArea.setLineWrap(true);
		this.readArea.setWrapStyleWord(true);
		this.updateArea.setLineWrap(true);
		this.updateArea.setWrapStyleWord(true);
		this.updateArea.setBorder(BasicBorders.getTextFieldBorder());
		this.parameterArea.setBorder(BasicBorders.getTextFieldBorder());
		this.readArea.setBorder(BasicBorders.getTextFieldBorder());
		this.readArea.setRows(4);
		this.updateArea.setRows(4);
		this.parameterArea.setRows(3);
		this.updateArea.setAutoscrolls(true);
		this.readArea.setAutoscrolls(true);
		this.parameterArea.setAutoscrolls(true);
		this.updateJsp = new JScrollPane(this.updateArea);
		this.readJsp = new JScrollPane(this.readArea);
		this.paramJsp = new JScrollPane(this.parameterArea);
		//开始加进去
		
		cons.weightx = 100;
//		cons.weighty = 100;
		cons.insets = new Insets(2,2,2,2);
//		cons.gridwidth = 1;
		cons.gridx = 0;
		cons.gridy = 0;
		cons.fill = GridBagConstraints.BOTH;
		cons.anchor = GridBagConstraints.CENTER;
		this.layout.setConstraints(this.stepnoLabel, cons);
		cons.gridy = 1;
		this.layout.setConstraints(this.descLabel, cons);
		cons.gridy = 2;
		this.layout.setConstraints(this.typeLabel, cons);
		cons.gridy = 3;
		this.layout.setConstraints(this.progLabel, cons);
		cons.gridy = 4;
		this.layout.setConstraints(this.parameterLabel, cons);
		cons.gridy = 5;
		this.layout.setConstraints(this.readLabel, cons);
		cons.gridy = 6;
		this.layout.setConstraints(this.updateLabel, cons);
		//
		this.add(this.stepnoLabel);
		this.add(this.typeLabel);
		this.add(this.descLabel);
		this.add(this.progLabel);
		this.add(this.parameterLabel);
		this.add(this.readLabel);
		this.add(this.updateLabel);
		
		cons.weightx = 400;
		cons.gridx = 1;
		cons.gridy = 0;
//		cons.gridwidth = 5;
		this.layout.setConstraints(this.stepnoText, cons);
		cons.gridy = 1;
		this.layout.setConstraints(this.descText, cons);
		cons.gridy = 2;
		this.layout.setConstraints(this.typeText, cons);
		cons.gridy = 3;
		this.layout.setConstraints(this.progText, cons);
		cons.gridy = 4;
		this.layout.setConstraints(this.paramJsp, cons);
		cons.gridy = 5;
		this.layout.setConstraints(this.readJsp, cons);
		cons.gridy = 6;
		this.layout.setConstraints(this.updateJsp, cons);
		
		this.add(this.stepnoText);
		this.add(this.typeText);
		this.add(this.descText);
		this.add(this.progText);
		this.add(this.paramJsp);
		this.add(this.readJsp);
		this.add(this.updateJsp);
	}

	public void setInfo(StepInfo stepInfo){
		this.stepInfo = stepInfo;
		Info info = this.stepInfo.getInfo();
		this.stepnoText.setText(info.getStepno());
		this.typeText.setText(info.getProcType());
		this.descText.setText(info.getProcDesc());
		this.progText.setText(info.getProc());
		this.parameterArea.setText(info.getParameter());
		
		RelStep relStep;
		ArrayList<ArrayList<String>> rlist = null;
		try {
			relStep = new RelStep();
			rlist = relStep.getTablesByStepno(info.getStepno());
		} catch (Exception e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}

		if (rlist != null) {
			StringBuffer str = new StringBuffer();
			String lineSep = System.getProperty("line.separator", "\r\n");
			for (int i = 0; rlist.size() > 0 && i < rlist.get(0).size() - 1; ++i) {
				str.append(rlist.get(0).get(i) + lineSep);
			}
			if (rlist.size() > 0 && rlist.get(0).size() > 0) {
				str.append(rlist.get(0).get(rlist.get(0).size() - 1));
			}else {
				str.append("无");
			}
			this.readArea.setText(str.toString());

//			System.out.println("更新的表：");
			str.delete(0, str.length());
			for (int i = 0; rlist.size() > 1 && i < rlist.get(1).size() - 1; ++i) {
//				System.out.println("\t" + rlist.get(1).get(i));
				str.append(rlist.get(1).get(i) + lineSep);
			}
			if (rlist.size() > 1 && rlist.get(1).size() > 0) {
				str.append(rlist.get(1).get(rlist.get(1).size() - 1));
			}else {
				str.append("无");
			}
			this.updateArea.setText(str.toString());
			 
		} else {
			this.readArea.setText("");
			this.updateArea.setText("");
		}
	}
	
	public void setLocation(MouseEvent e) {
		Point dlgPoint = new Point(e.getPoint());
		SwingUtilities.convertPointToScreen(dlgPoint, this.pane);
		dlgPoint = EnvUtil.getRightPoint(dlgPoint, this.getSize());
		this.setLocation(dlgPoint.x, dlgPoint.y);
	}
}
