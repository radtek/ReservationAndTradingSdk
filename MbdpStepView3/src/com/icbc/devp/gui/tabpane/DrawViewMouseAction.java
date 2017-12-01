package com.icbc.devp.gui.tabpane;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Iterator;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;


import com.icbc.devp.gui.action.DelSelectedSqlClickAction;
import com.icbc.devp.gui.action.EnableStepAction;
import com.icbc.devp.gui.action.NewNodeAction;
import com.icbc.devp.gui.action.SaveFlowImageAction;
import com.icbc.devp.gui.action.SearchRelStepAction;
import com.icbc.devp.gui.action.SearchTbAction;
import com.icbc.devp.gui.action.WriteSelectedSqlClickAction;
import com.icbc.devp.gui.action.WriteSqlClickAction;
import com.icbc.devp.gui.dialog.StepDialog;
import com.icbc.devp.gui.factory.IconFactory;
import com.icbc.devp.gui.info.StepInfo;
import com.icbc.devp.tool.util.EnvUtil;

public class DrawViewMouseAction implements MouseListener, KeyListener, MouseMotionListener {

	public DrawViewMouseAction(StepInfoPane pane){
		super();
		this.pane = pane;
		this.popMenu = new JPopupMenu();
		this.loadRelItem = new JMenuItem("突出关联步骤");
		this.clearRelItem = new JMenuItem("取消突出显示");
		this.newNodeItem  = new JMenuItem("新增节点");
		this.sqlItem      = new JMenuItem("生成SQL文件(存量)");
		JMenuItem zSqlItem      = new JMenuItem("生成选中节点的新增SQL");
		JMenuItem dSqlItem = new JMenuItem("生成选中节点的删除SQL");
		JMenuItem saveImage = new JMenuItem("保存图片");
		
		//lxc
		this.searchTbItem = new JMenuItem("该步骤访问的表");
		this.searchRelStepItem = new JMenuItem("未配置关联关系的关联步骤");

		
		this.sqlAction = new WriteSqlClickAction(this.pane);
		this.loadRelItem.setIcon(IconFactory.getInstance().getIcon("OFFSET"));
		this.clearRelItem.setIcon(IconFactory.getInstance().getIcon("CANCEL_SHOW"));
		this.sqlItem.setIcon(IconFactory.getInstance().getIcon("OPENBOOK"));
		zSqlItem.setIcon(IconFactory.getInstance().getIcon("OPENBOOK"));
		dSqlItem.setIcon(IconFactory.getInstance().getIcon("OPENBOOK"));
		saveImage.setIcon(IconFactory.getInstance().getIcon("APPLY"));
		
		this.enableItem  = new JMenuItem();
		this.popMenu.add(this.loadRelItem);
		this.popMenu.add(this.clearRelItem);
//		this.popMenu.add(saveImage);
//		this.popMenu.add(this.sqlItem);
//		this.popMenu.add(zSqlItem);
//		this.popMenu.add(dSqlItem);
		
		this.popMenu.add(this.enableItem);
		
		//lxc
		this.popMenu.add(searchTbItem);
		this.popMenu.add(searchRelStepItem);
		this.searchTbAction = new SearchTbAction(this.pane);
		this.searchTbItem.addActionListener(searchTbAction);
		this.searchRelStepAction = new SearchRelStepAction(this.pane);
		this.searchRelStepItem.addActionListener(searchRelStepAction);
		
		this.activeAction = new ActiveAction(pane);
		this.loadRelItem.addActionListener(this.activeAction);
		this.clearRelItem.addActionListener(new InActiveAction(this.pane));
		this.enableItem.addActionListener(new EnableStepAction(this.pane));
		this.sqlItem.addActionListener(this.sqlAction);
		saveImage.addActionListener(new SaveFlowImageAction(this.pane));
		zSqlItem.addActionListener(new WriteSelectedSqlClickAction(this.pane));
		dSqlItem.addActionListener(new DelSelectedSqlClickAction(this.pane));
		//事件
		this.newNodeAction = new NewNodeAction(pane);
		this.newNodeItem.addActionListener(newNodeAction);
		
		this.dialog = new StepDialog(this.pane);
		this.dialog.setResizable(false);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		this.mouseDown = false;
		this.enableItem.setEnabled(true);
		this.loadRelItem.setEnabled(true);
		if(e.getClickCount() == 2 && e.getButton() == 1){	//双击步骤事件
			if(this.pane.getSelectedNode() != null){
				this.dialog.setInfo(this.pane.getSelectedNode());
				dlgPoint = new Point(e.getPoint());
				SwingUtilities.convertPointToScreen(this.dlgPoint, this.pane);
				dlgPoint = EnvUtil.getRightPoint(dlgPoint, this.dialog.getSize());
				this.dialog.showInfo(dlgPoint.x, dlgPoint.y);
			}
		}

		if(e.getClickCount() == 1 && e.getButton() == 3){
			this.newNodeAction.setLocation(e.getX(), e.getY());


			
			StepInfo info = this.pane.getSelectedNode();
			if(info == null || !info.constains(e.getPoint())){
				for(Iterator<StepInfo> it = this.pane.getInfoIterator(); it.hasNext();){
					info = it.next();
					if(info.constains(e.getPoint())){
						this.pane.setSelectedNode(info);
						this.pane.repaint();
						break;
					}
				}
				if(!info.constains(e.getPoint())){
					this.pane.setSelectedNode(null);
					this.pane.repaint();
					info = null;
				}
			}
			if(info != null){
				if(info.isEnable()){
					this.enableItem.setText("禁用选中步骤");
					this.enableItem.setIcon(IconFactory.getInstance().getIcon("CROSS"));
				}else{
					this.enableItem.setText("启用选中步骤");
					this.enableItem.setIcon(IconFactory.getInstance().getIcon("ENABLE_REPAIR"));
				}
				//有的
				this.popMenu.show(e.getComponent(), e.getX(), e.getY());
				
				//lxc
				this.searchTbAction.setLocation(e);
				this.searchRelStepAction.setLocation(e);
//				this.searchTbAction.setInfo(info);
			}else{
				this.enableItem.setEnabled(false);
				this.loadRelItem.setEnabled(false);
				this.popMenu.show(e.getComponent(), e.getX(), e.getY());
			}
		}
		
		if(e.getClickCount() == 1 && e.getButton() == 1){
			if(this.isCtrlDown){
				if(this.pane.getSelectedNode() != null){
					this.pane.addCtrlSelectedNode(this.pane.getSelectedNode());
					this.pane.repaint();
				}
			}
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
		mouseDown = true;
		this.enableItem.setEnabled(true);
		this.loadRelItem.setEnabled(true);
		if(e.getClickCount() == 1 && e.getButton() == 1){
			//看看有没有图案被选中
			StepInfo info;
			for(Iterator<StepInfo> it = this.pane.getInfoIterator(); it.hasNext();){
				info = it.next();
				if(info.constains(e.getPoint())){
					this.pane.setSelectedNode(info);
					this.pane.setDragStartPoint(e.getPoint());
//					if(this.isCtrlDown){
//						this.pane.addCtrlSelectedNode(info);
//					}
					this.pane.repaint();
					return;
				}
			}
			this.pane.setSelectedNode(null);
			this.pane.setRecStartPoint(e.getPoint().x, e.getPoint().y);
			this.pane.repaint();
			return;
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		mouseDown = false;
		this.pane.markInsideRecNodes();
		this.pane.setRecStartPoint(-1, -1);
		this.pane.setRecEndPoint(-1, -1);
		this.pane.repaint();
	}
	
	//下面是键盘事件的方法
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyCode() == KeyEvent.VK_CONTROL){
			this.isCtrlDown = true;
			if(this.pane.getSelectedNode() != null){
				this.pane.addCtrlSelectedNode(this.pane.getSelectedNode());
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyCode() == KeyEvent.VK_CONTROL){
			
			this.isCtrlDown = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}
	//拖动的接口

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		StepInfo step = pane.getSelectedNode();
		if(step == null){
			this.pane.setRecEndPoint(e.getPoint().x, e.getPoint().y);
			this.pane.repaint();
		}else{
			if(!this.mouseDown){
				return;
			}
			curPoint.x  = e.getPoint().x - pane.getDragDistance().x;
			curPoint.y  = e.getPoint().y - pane.getDragDistance().y;
			step.setLocation(curPoint.x, curPoint.y);
			pane.repaint();
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}
	
	private StepInfoPane pane;
	private JPopupMenu popMenu;
	private JMenuItem loadRelItem;
	private JMenuItem clearRelItem;
	private StepDialog dialog;
	private ActiveAction activeAction;
	private Point dlgPoint;
	private JMenuItem enableItem;
	private JMenuItem newNodeItem;
	private JMenuItem sqlItem;
	private NewNodeAction newNodeAction;
	private WriteSqlClickAction sqlAction;
	//判断是否多选按钮按下来了
	private boolean isCtrlDown = false;
	private Point curPoint = new Point(0,0);
	//鼠标按下判断
	private boolean mouseDown = false;
	
	//查找步骤访问的表
	private JMenuItem searchTbItem;
	private SearchTbAction searchTbAction;
	//查找可能存在关联关系的步骤
	private JMenuItem searchRelStepItem;
	private SearchRelStepAction searchRelStepAction;
}
