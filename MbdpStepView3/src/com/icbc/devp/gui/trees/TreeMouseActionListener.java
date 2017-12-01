package com.icbc.devp.gui.trees;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.tree.TreePath;

import com.icbc.devp.gui.factory.IconFactory;
import com.icbc.devp.gui.info.DataLoader;

public class TreeMouseActionListener implements MouseListener {

	public TreeMouseActionListener(JTabbedPane tabPane, DataLoader loader, CtlTreeNode node){
		popupMenu = new JPopupMenu();
		JMenuItem item = new JMenuItem("加载步骤信息");
		JMenuItem ctlItem = new JMenuItem("查看批量信息");
		item.setIcon(IconFactory.getInstance().getIcon("LOADDB"));
		ctlItem.setIcon(IconFactory.getInstance().getIcon("OPENBOOK"));
		
		this.popupMenu.add(item);
		this.popupMenu.add(ctlItem);
		stepLoader = new LoadStepdefAction(node);
		this.ctlAction = new CtlInfoAction(node);
		this.stepLoader.setLoader(loader);
		this.stepLoader.setTabPane(tabPane);
		item.addActionListener(stepLoader);
		ctlItem.addActionListener(ctlAction);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		JTree tree = (JTree)e.getSource();
		if(e.getButton() == 3 && tree.getSelectionPath().getPathCount() >= 2){
			String nodeName = tree.getSelectionPath().getPath()[1].toString();
			stepLoader.setNodeName(nodeName);
			mousePos.x = e.getXOnScreen();
			mousePos.y = e.getYOnScreen();
			
			ctlAction.setNodeName(nodeName, mousePos.x, mousePos.y);
//			tree.getSelectionRows()[0];
			popupMenu.show(e.getComponent(), e.getX(), e.getY());
//			popupMenu.setVisible(true);
			return;
		}
		if(e.getButton() == 1 && e.getClickCount() == 1 && tree.getSelectionPath().getPathCount() >= 2){
			String nodeName = tree.getSelectionPath().getPath()[1].toString();
			lastNodeName = nodeName;
			if(lastNodeName == null){
				lastNodeName = "";
			}
			stepLoader.setNodeName(nodeName);
			mousePos.x = e.getXOnScreen();
			mousePos.y = e.getYOnScreen();
			
			ctlAction.setNodeName(nodeName, mousePos.x, mousePos.y);
			return;
		}
		if(e.getButton() == 1 && e.getClickCount() == 2 && tree.getSelectionPath().getPathCount() >= 2){
			String nodeName = tree.getSelectionPath().getPath()[1].toString();
			if(nodeName == null){
				return;
			}
			if(!nodeName.equals(this.lastNodeName)){
				return;
			}
			this.stepLoader.loadStepInfo();
			return;
		}
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
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		JTree tree = (JTree)e.getSource();
		TreePath path = tree.getPathForLocation(e.getX(), e.getY());
		tree.setSelectionPath(path);
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	private JPopupMenu popupMenu;
	private LoadStepdefAction stepLoader;
	private CtlInfoAction ctlAction;
	private Point mousePos = new Point(0,0);
	private String lastNodeName;
//	private 
}
