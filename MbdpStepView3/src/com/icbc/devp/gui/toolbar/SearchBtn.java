package com.icbc.devp.gui.toolbar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import com.icbc.devp.gui.tabpane.PicTabPane;
import com.icbc.devp.gui.tabpane.StepInfoPane;

public class SearchBtn extends JButton implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public SearchBtn(String desc, JTextField searchField, JTabbedPane tabPane){
		this.setText(desc);
		this.searchField = searchField;
		this.tabPane = tabPane;
		this.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		PicTabPane picPane = (PicTabPane)tabPane.getSelectedComponent();
		StepInfoPane viewPane = picPane.getPane();
		viewPane.scrollToNode(searchField.getText().trim());
	}
	
	//≤È’“BUTTON
	private JTextField searchField;
	private JTabbedPane tabPane;
}
