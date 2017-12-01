package com.icbc.devp.gui.pane;

import java.awt.Dimension;

import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;

import com.icbc.devp.gui.tabpane.TabbedPaneAction;


public class MbdpSplitPane extends JSplitPane {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public MbdpSplitPane(){
	}
	
	/**TODO:������Ǵ��ڴ�С����*/
	public void initParam(Dimension d){
		//��ֱ�ָ�
		this.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		this.setDividerLocation(d.width/4);	//��������1:5�ָ�
		this.setOneTouchExpandable(true);
		
		this.leftPane = new JScrollPane();
		this.rightPane = new JTabbedPane();
		
		this.leftPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		this.leftPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		this.setLeftComponent(this.leftPane);
		this.setRightComponent(this.rightPane);
		this.rightPane.addMouseListener(new TabbedPaneAction());
		
	}
	

	public void setCtlTree(JTree tree){
		this.leftPane.getViewport().add(tree);
		this.leftPane.setVisible(true);
	}
	
	public JTabbedPane getViewTabPane(){
		return this.rightPane;
	}
	
	private JScrollPane leftPane;
	private JTabbedPane rightPane;

}
