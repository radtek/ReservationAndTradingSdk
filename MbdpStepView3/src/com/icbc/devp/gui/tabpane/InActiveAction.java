package com.icbc.devp.gui.tabpane;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InActiveAction implements ActionListener {
	
	public InActiveAction(StepInfoPane pane){
		super();
		this.pane = pane;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(this.pane == null){
			return;
		}
		this.pane.clearSelectedNodes();
		this.pane.repaint();
	}


	private StepInfoPane pane;
}
