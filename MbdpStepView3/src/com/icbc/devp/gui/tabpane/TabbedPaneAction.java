package com.icbc.devp.gui.tabpane;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JTabbedPane;

import com.icbc.devp.tool.log.Log;

public class TabbedPaneAction implements MouseListener {

	public TabbedPaneAction(){
		this.index = -1;
		this.lastButton = -1;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		//双击关闭选项卡
		JTabbedPane pane = (JTabbedPane)e.getSource();
		Point p = e.getPoint();
		int tmp = pane.indexAtLocation(p.x, p.y);
		if(e.getClickCount() == 1 && e.getButton() == 1 && tmp != -1 ){
			index = tmp;
			PicTabPane pic = null;
			try {
				pic = (PicTabPane)pane.getSelectedComponent();
				if(!pic.getPane().requestFocusInWindow()){
					Log.getInstance().error("[TabbedPaneAction]请求焦点被拒绝。");
				}
			} 
			catch(Exception ee) {
				System.out.println("not PicTabPane");
			}
			this.lastButton = 1;
			return;
		}
		if(e.getClickCount() == 2 && e.getButton() == 1 && tmp != -1){
			if(index == tmp && this.lastButton == e.getButton()){
				pane.remove(index);
				this.lastButton = -1;
				this.index = -1;
				return;
			}
		}
		this.index = -1;
		this.lastButton = -1;
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	private int index;			//第一次点击的卡片选项
	private int lastButton;		//上一次点击的鼠标键
}
