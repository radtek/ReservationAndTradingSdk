package com.icbc.devp.gui.tabpane;

import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class PicTabPane extends JScrollPane {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	public PicTabPane(StepInfoPane p){
		this.pane = p;
		this.setViewportView(p);
		this.setPreferredSize(new Dimension(800,800));
	}
	
	public PicTabPane(JPanel panel) {
		this.setPreferredSize(new Dimension(800,800));
	}

	public StepInfoPane getPane(){
		return this.pane;
	}
	
	private StepInfoPane pane;
}
