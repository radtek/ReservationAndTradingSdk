/**
 * 
 */
package com.icbc.devp.gui.action;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.plaf.basic.BasicBorders;

import com.icbc.devp.gui.pane.MbdpSplitPane;
import com.icbc.devp.gui.tabpane.TabbedPaneAction;

/**
 * @author kfzx-liangxch
 *
 */
public class FindStepAction implements ActionListener {

	private JTabbedPane tabPane;
	
	private JPanel panel;
	private GridBagLayout layout;
	private GridBagConstraints cons;
	private JLabel tbnameLabel;
	private JTextField tbnameText; 
	private JLabel readLabel;
	private JLabel updateLabel;
	private JTextArea readArea; 
	private JTextArea updateArea; 
	private JScrollPane updateJsp;
	private JScrollPane readJsp;
	private JButton searchBtn;
	private static String tabName = "查找访问表的步骤";

	/**
	 * 
	 */
	public FindStepAction(MbdpSplitPane sp) {
		super();
		this.tabPane = sp.getViewTabPane();
	}


	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		// 判断这个tab是否存在
		int index = this.tabPane.indexOfTab(tabName);
		if (index != -1) {
			tabPane.setSelectedIndex(index);
			return;
		}
		
		cons = new GridBagConstraints();
		layout = new GridBagLayout();
		this.panel = new JPanel();
		this.panel.setLayout(layout);
		init();
		tabPane.add(tabName,panel);
		tabPane.setSelectedComponent(panel);
	}
	private void init() {
		
		this.tbnameLabel = new JLabel("表    名:", JLabel.CENTER);
		this.readLabel = new JLabel("读取表的步骤:", JLabel.CENTER);
		this.updateLabel    = new JLabel("更新表的步骤:", JLabel.CENTER);
		
		this.tbnameText = new JTextField();
		this.readArea = new JTextArea();
		this.updateArea = new JTextArea();
		this.searchBtn = new FindStepBtn("查询", this);
		this.readArea.setEditable(false);
		this.updateArea.setEditable(false);
		
		this.readArea.setLineWrap(true);
		this.readArea.setWrapStyleWord(true);
		this.updateArea.setLineWrap(true);
		this.updateArea.setWrapStyleWord(true);
		this.updateArea.setBorder(BasicBorders.getTextFieldBorder());
		this.readArea.setBorder(BasicBorders.getTextFieldBorder());
		this.readArea.setRows(8);
		this.updateArea.setRows(8);
		this.updateArea.setAutoscrolls(true);
		this.readArea.setAutoscrolls(true);
		this.updateJsp = new JScrollPane(this.updateArea);
		this.updateJsp.setMinimumSize(new Dimension(200,155));
		this.readJsp = new JScrollPane(this.readArea);	
		this.readJsp.setMinimumSize(new Dimension(200,155));

		cons.insets = new Insets(2,2,2,2);
		this.cons.weightx = 0;
		this.layout.setConstraints(this.tbnameLabel, this.cons);
		this.panel.add(this.tbnameLabel);

		this.cons.fill = GridBagConstraints.BOTH;
		this.cons.weightx = 1;
		this.layout.setConstraints(this.tbnameText, this.cons);	
		this.panel.add(this.tbnameText);

		this.cons.weightx = 0;
		this.cons.gridwidth = GridBagConstraints.REMAINDER;
		this.layout.setConstraints(this.searchBtn, this.cons);		
		this.panel.add(this.searchBtn);

		this.cons.gridwidth = 1;
		this.layout.setConstraints(this.readLabel, this.cons);
		this.panel.add(this.readLabel);

		this.cons.fill = GridBagConstraints.HORIZONTAL;
		this.cons.gridwidth = 2;
		this.cons.gridwidth = GridBagConstraints.REMAINDER;
		this.layout.setConstraints(this.readJsp, this.cons);	
		this.panel.add(this.readJsp);

		this.cons.gridwidth = 1;
		this.layout.setConstraints(this.updateLabel, this.cons);
		this.panel.add(this.updateLabel);

		this.cons.fill = GridBagConstraints.HORIZONTAL;
		this.cons.gridwidth = 2;
		this.cons.gridwidth = GridBagConstraints.REMAINDER;
		this.layout.setConstraints(this.updateJsp, this.cons);	
		this.panel.add(this.updateJsp);

	}
	
	public String getTbname() {
		return this.tbnameText.getText();
	}
	
	public void setReadStep(String text) {
		this.readArea.setText(text);
	}
	
	public void setUpdateStep(String text) {
		this.updateArea.setText(text);
	}

}
