package com.icbc.devp.gui.tabpane;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import com.icbc.devp.gui.UiContext;
import com.icbc.devp.gui.factory.ColorFactory;
import com.icbc.devp.gui.info.StepInfo;
import com.icbc.devp.gui.info.StepInfoTree;
import com.icbc.devp.tool.util.EnvUtil;

/**TODO: ������Ϣ��ʾ
 * */
public class StepInfoPane extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public StepInfoPane(){
		this.setPreferredSize(new Dimension(this.viewWidth,this.viewHeight));	//������һ����ʼ��С
		this.setSize(new Dimension(this.viewWidth,this.viewHeight));
		this.stepLevelMap = new HashMap<String, ArrayList<StepInfo>>();
		this.initArrows();
		this.selectedMap = new HashMap<String, StepInfo>();
		this.selectedList = new ArrayList<StepInfo>();
		this.mouseAction = new DrawViewMouseAction(this);
//		DragMouseMotionAction tmpAction = new DragMouseMotionAction(this);
		this.addMouseMotionListener(mouseAction);
		this.addMouseListener(this.mouseAction);
		this.addKeyListener(this.mouseAction);
		this.initNodeList = new ArrayList<StepInfo>();
		this.liststepMap = new HashMap<String, StepInfo>();
		
	}
	

	public void paint(Graphics g){
		g.clearRect(0,0,this.getWidth(), this.getHeight());
		this.drawImage(g);

	}
	
	public void drawImage(Graphics g){
		Graphics2D g2d = (Graphics2D)g;
		if(this.infoTree == null){
			return;
		}
		StepInfo step;
		g2d.setColor(this.basicColor);
		g2d.setStroke(this.basicStroke);
		//����ͼ��
		for(Iterator<StepInfo> it = this.infoTree.getStepInfoMap().values().iterator(); it.hasNext();){
			step = it.next();
			if(this.selectedMap.get(step.getId()) == null){
				this.drawStep(g2d, step);
			}
		}
		for(Iterator<StepInfo> it = this.infoTree.getStepInfoMap().values().iterator(); it.hasNext();){
//			this.drawSonLine(g2d, it.next());
			step = it.next();
//			if(this.selectedMap.get(step.getId()) == null){
				this.drawSonLine(g2d, step, false);
//			}
		}
		//��ѡ��ͼ��
		g2d.setColor(this.selectedColor);
		g2d.setStroke(this.selectedStroke);
		for(Iterator<StepInfo> it = this.selectedMap.values().iterator(); it.hasNext();){
			this.drawSonLine(g2d, it.next(), true);
		}
		for(Iterator<StepInfo> it = this.selectedMap.values().iterator(); it.hasNext();){
			step = it.next();
			this.drawStep(g2d, step);
			g2d.drawRect(step.getStartPoint().x-1, step.getStartPoint().y-1, step.getRectWidth()+2, step.getRectHeight()+2);
		}
		for(int i=0; i<this.selectedList.size(); i++){
			step = this.selectedList.get(i);
			this.drawStep(g2d, step);
			g2d.drawRect(step.getStartPoint().x-1, step.getStartPoint().y-1, step.getRectWidth()+2, step.getRectHeight()+2);
		}
		
		//���ѡ�е�ͼ��
		if(this.selectedNode != null){
			g2d.drawRect(selectedNode.getStartPoint().x-1, selectedNode.getStartPoint().y-1, selectedNode.getRectWidth()+2, selectedNode.getRectHeight()+2);
		}
		//����Ҫ��Ҫ���ߣ��ָ��ʼ����
		if(!this.initNodeList.isEmpty()){
			int initY = this.nodeHeight + this.heightSplit;
			g2d.setColor(BASIC_SPLIT_COLOR);
			g2d.drawLine(0, initY, this.getSize().width, initY);
		}
		//������
		if(this.recStartPoint.x != -1 && this.recEndPoint.x != -1){
			g2d.setColor(ColorFactory.getInstance().getColor("BLUE"));
			g2d.setStroke(this.basicStroke);
//			g2d.drawRect(this.recStartPoint.x, this.recStartPoint.y, Math.abs(this.recEndPoint.x - this.recStartPoint.x), Math.abs(this.recEndPoint.y - this.recStartPoint.y));
			//�������ڵ��
			g2d.drawLine(this.recEndPoint.x, this.recStartPoint.y, this.recEndPoint.x, this.recEndPoint.y);
			g2d.drawLine(this.recStartPoint.x, this.recEndPoint.y, this.recEndPoint.x, this.recEndPoint.y);
			//��ʼ�ڵ������
			g2d.drawLine(this.recStartPoint.x, this.recStartPoint.y, this.recEndPoint.x, this.recStartPoint.y);
			g2d.drawLine(this.recStartPoint.x, this.recStartPoint.y, this.recStartPoint.x, this.recEndPoint.y);
		}
	}
	
	/**TODO:��ȡ������*/
	public StepInfoTree getInfoTree() {
		return infoTree;
	}

	/**TODO:���ò�����*/
	public void setInfoTree(StepInfoTree infoTree) {
		if(this.infoTree != null){
			this.infoTree.clear();
		}
		this.infoTree = infoTree;
		if(this.infoTree == null){
			return;
		}
		//��ʼ��ÿ���ڵ�Ĳ㼶
		HashMap<String, StepInfo> stepInfoMap = this.infoTree.getStepInfoMap();
		if(stepInfoMap.isEmpty()){
			return;
		}
		StepInfo step;
		Collection<StepInfo> steps = stepInfoMap.values();
		Iterator<StepInfo> it;
		String key;
		int level;
		ArrayList<StepInfo> infoList;
		this.stepLevelMap.clear();	//�����ԭ�����е�
		for(it = steps.iterator(); it.hasNext();){
			step = it.next();
			level = step.calHeight(false);	//����Ѿ���������Ͳ����¼���
			key = String.valueOf(level);
			infoList = this.stepLevelMap.get(key);
			if(infoList == null){
				infoList = new ArrayList<StepInfo>();
				infoList.add(step);
				this.stepLevelMap.put(key, infoList);
			}else{
				infoList.add(step);
			}
			if(step.isInitType()){
				this.initNodeList.add(step);
			}
		}
	}


	/**TODO:���÷ֲ�ͼ�����������ڵ�λ��*/
	public boolean initStepLocation(){
		if(this.infoTree == null){
			return true;
		}
//		//�ǿգ�һ�������˿�Ҫ
//		int depth = this.infoTree.getTreeDepth(false);		//�㼶���
//		int levCount = this.infoTree.getTreeWidth(false);	//�����
		//��ס���ù�������
		HashMap<String, String> setMap = new HashMap<String, String>();
		this.levelCurPos = new int[this.stepLevelMap.keySet().size()];
		//����������ߵ���
		StepInfo tmp;
		
		int initY = this.heightSplit/2;
		int initX = this.widthSpilt/2;
		
		if(!this.initNodeList.isEmpty()){
			for(int i=0; i<this.initNodeList.size(); i++){
				tmp = this.initNodeList.get(i);
				tmp.setStartPoint(new Point(initX, initY));
				tmp.setSize(this.nodeWidth, this.nodeHeight);
				setMap.put(tmp.getId(), "1");
				initX += this.nodeWidth + this.widthSpilt;
			}
			initY += this.nodeHeight + this.heightSplit;
		}
		//��ʼ��λ��
		for(int i = 0 ;i<this.levelCurPos.length; i++){
			this.levelCurPos[i] = initY;
		}
		
		StepInfo root = this.infoTree.getRoot();
		int firstY = this.initNodePosition(root, setMap, this.levelCurPos[0]);
		int curX   = this.widthSpilt/2;
		if(this.levelCurPos[0] < firstY){
			this.levelCurPos[0] = firstY;
		}
		firstY = this.levelCurPos[0];
		if(setMap.get(root.getId()) == null){
			root.setStartPoint(new Point(curX, firstY));
			root.setSize(this.nodeWidth, this.nodeHeight);
			this.levelCurPos[0] += this.nodeHeight + this.heightSplit;
			setMap.put(root.getId(), "1");
		}
		
		//�������㼶����1���ٿ�����ʼ��һ��
		ArrayList<StepInfo> zeroList = this.stepLevelMap.get("1");
		Collections.sort(zeroList);
		if(zeroList != null && !zeroList.isEmpty()){
			for(int i=0; i<zeroList.size();i++){
				tmp = zeroList.get(i);
				if(setMap.get(tmp.getId()) == null){
					tmp.setStartPoint(new Point(curX, this.levelCurPos[0]));
					tmp.setSize(this.nodeWidth, this.nodeHeight);
					setMap.put(tmp.getId(), "1");
					this.levelCurPos[0] += this.nodeHeight + this.heightSplit;
				}
			}
		}
		//������ͼ��Χ
		curX = (this.nodeWidth + this.widthSpilt) * this.levelCurPos.length + this.nodeWidth;
		int maxY = this.levelCurPos[0];
		for(int i=1; i<this.levelCurPos.length; i++){
			if(this.levelCurPos[i] > maxY){
				maxY = this.levelCurPos[i];
			}
		}
		this.viewWidth = curX;
		if(initX > curX){
			this.viewWidth = initX;
		}
		this.viewHeight = maxY;
		this.setPreferredSize(new Dimension(this.viewWidth,this.viewHeight));
		this.setSize(this.viewWidth, this.viewHeight);
		
		return true;
	}
	
	/**TODO:��ʼ���ڵ��λ�ã����ص�һ���ӽڵ��λ��*/
	private int initNodePosition(StepInfo node, HashMap<String, String> setMap, int fatherY){
		int level = node.calHeight(false);
		int curX = this.widthSpilt/2 + (level - 1)*(this.nodeWidth + this.widthSpilt);;
		int firstY;
		//�Ѿ����ù���ֱ�������ӽڵ�
		if(!node.hasSon()){
			if(setMap.get(node.getId()) == null){
				if(fatherY > this.levelCurPos[level - 1]){
					this.levelCurPos[level - 1] = fatherY;
				}
				node.setStartPoint(new Point(curX, this.levelCurPos[level-1]));
				node.setSize(this.nodeWidth, this.nodeHeight);
				firstY = this.levelCurPos[level - 1];
				this.levelCurPos[level - 1] += this.nodeHeight + this.heightSplit;
				setMap.put(node.getId(), "1");
				return firstY;
			}
		}
		ArrayList<StepInfo> sList = node.getSList();
		Collections.sort(sList);
		//��ʼ����
		firstY = 0;
		if(this.levelCurPos[level - 1] < fatherY){
			this.levelCurPos[level - 1] = fatherY;
		}
		int count = 0;
		StepInfo tmp;
		firstY = levelCurPos[level - 1];
		for(int i=0; i<sList.size(); i++){
			tmp = sList.get(i);
			if(setMap.get(tmp.getId()) != null){
				continue;
			}
			if(count == 0){
				firstY = this.initNodePosition(tmp, setMap, this.levelCurPos[level - 1]);
				count ++;
			}else{
				this.initNodePosition(tmp, setMap, this.levelCurPos[level - 1]);
			}
			setMap.put(tmp.getId(), "1");
		}
		if(this.levelCurPos[level - 1] < firstY){
			this.levelCurPos[level - 1] = firstY;
		}
		firstY = this.levelCurPos[level - 1];
		node.setStartPoint(new Point(curX, firstY));
		node.setSize(this.nodeWidth, this.nodeHeight);
		this.levelCurPos[level - 1] += this.nodeHeight + this.heightSplit;
		setMap.put(node.getId(), "1");
		return firstY;
	}
	
	//��һ��ͼ
	private void drawStep(Graphics2D g2d, StepInfo info){
		g2d.drawImage(info.getImage(), info.getStartPoint().x, info.getStartPoint().y, null);
	}
	
	//�����ӽڵ����
	private void drawSonLine(Graphics2D g2d, StepInfo info, boolean isSelected){
		if(info == null){
			return;
		}
		if(!info.hasSon()){
			return;
		}
		ArrayList<StepInfo> sList = info.getSList();
		for(int i=0; i<sList.size(); i++){
			this.drawLine(g2d, info, sList.get(i), isSelected);
		}
	}
	
	//��һ����
	private void drawLine(Graphics2D g2d, StepInfo start, StepInfo end, boolean isSelected){
		if(isSelected && (this.selectedMap.get(start.getId()) == null || this.selectedMap.get(end.getId()) == null)){
			return;
		}
		Point startPoint = start.getLineOutPoint();
		Point endPoint   = end.getLineInPoint();
	
		//Ȼ�����ߣ��ܹ���3�Σ�����һ�Σ���ȥһ�Σ�Ȼ�������м�һ��
		if(startPoint.y == endPoint.y || startPoint.x == endPoint.x){
			g2d.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
			//����������Ƕ�������ͷ
			if(!isSelected){
				g2d.drawImage(this.arrowImg, endPoint.x - arrow.getIconWidth(), endPoint.y - arrow.getIconHeight()/2, null);
			}else{
				g2d.drawImage(this.selectedArrowImg, endPoint.x - this.selectedArrow.getIconWidth(), endPoint.y - selectedArrow.getIconHeight()/2, null);
			}
			return;
		}
		int startLen;
		int endLen;
		int disX;
		int disY;
		int startHeight = start.getHeight();
		int endHeight = end.getHeight();
		int dis = Math.abs(endHeight - startHeight);
		//y���ȣ�����x�Ȳ���
		if(startPoint.x < endPoint.x){
			if(dis == 1){
				disX = endPoint.x - startPoint.x;
				startLen = disX/2 - 2;
				endLen = disX - startLen;
				//����������
				g2d.drawLine(startPoint.x, startPoint.y, startPoint.x + startLen, startPoint.y);
				//����ȥ����
				g2d.drawLine(endPoint.x, endPoint.y, endPoint.x - endLen, endPoint.y);
				//��һ������
				g2d.drawLine(startPoint.x + startLen, startPoint.y, startPoint.x + startLen, endPoint.y);
			}else{
				disX = endPoint.x - startPoint.x;
				startLen = this.widthSpilt / 2 + 4;
				endLen   = this.widthSpilt / 2 - 4;
				g2d.drawLine(startPoint.x, startPoint.y, startPoint.x + startLen, startPoint.y);
				//����ȥ����
				g2d.drawLine(endPoint.x, endPoint.y, endPoint.x - endLen, endPoint.y);
				//����������
				if(endPoint.y > startPoint.y){
					disY =  startPoint.y + this.nodeHeight/2 + this.heightSplit/2;
					g2d.drawLine(startPoint.x + startLen, startPoint.y, startPoint.x + startLen, disY);
					g2d.drawLine(endPoint.x - endLen, endPoint.y, endPoint.x - endLen, disY);
					g2d.drawLine(startPoint.x + startLen, disY, endPoint.x - endLen, disY);
				}else{
					disY =  startPoint.y - this.nodeHeight/2 - this.heightSplit/2;
					g2d.drawLine(startPoint.x + startLen, startPoint.y, startPoint.x + startLen, disY);
					g2d.drawLine(startPoint.x + startLen, disY, endPoint.x - endLen, disY);
					g2d.drawLine(endPoint.x - endLen, disY, endPoint.x - endLen, endPoint.y);
				}
			}
		}
		//����������Ƕ�������ͷ
		if(!isSelected){
			g2d.drawImage(this.arrowImg, endPoint.x - arrow.getIconWidth(), endPoint.y - arrow.getIconHeight()/2, null);
		}else{
			g2d.drawImage(this.selectedArrowImg, endPoint.x - this.selectedArrow.getIconWidth(), endPoint.y - selectedArrow.getIconHeight()/2, null);
		}
	}

	/**TODO:��ʼ����ͷ��ͼƬ*/
	private void initArrows(){
		try{
			String rootPath = EnvUtil.getInstance().getRootPath();
			File file = new File(rootPath, "icon/stepinfo/arrow.png");
			this.arrow = new ImageIcon(file.getAbsolutePath());
			file = new File(rootPath, "icon/stepinfo/selectedArrow.png");
			this.selectedArrow = new ImageIcon(file.getAbsolutePath());
			this.arrowImg = this.arrow.getImage();
			this.selectedArrowImg = this.selectedArrow.getImage();
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	
	/**TODO: ���ñ�ѡ�еĽڵ�*/
	public void setSelectedNode(StepInfo info){
		this.selectedNode = info;
	}
	
	/**TODO: ��ȡ��ѡ�еĽڵ�*/
	public StepInfo getSelectedNode(){
		return this.selectedNode;
	}
	
	/**TODO:��ȡ���нڵ�*/
	public Iterator<StepInfo> getInfoIterator(){
		if(infoTree == null){
			return null;
		}
		return this.infoTree.getStepInfoMap().values().iterator();
	}
	
	
	/**TODO:������м��صĹ�����Ϣ*/
	public void clearSelectedNodes(){
		this.selectedMap.clear();
		this.selectedNode = null;
		this.selectedList.clear();
		this.liststepMap.clear();
	}
	
	/**TODO:��ȡѡ�е�MAP*/
	public HashMap<String,StepInfo> getSelectedMap(){
		return this.selectedMap;
	}
	
	/**TODO:��λ��ָ����λ�ã�û�ҵ�����false���ҵ�����true��
	 *����֧�ֳ��������������*/
	public boolean scrollToNode(String text){
		StepInfo info = infoTree.findStepInfo(text);
		if(info == null){
			return false;
		}
		//�ҵ��ˣ���ȡ���λ��
		Rectangle rec = info.getViewRec();
		this.selectedNode = info;
		this.scrollRectToVisible(rec);
		this.repaint();
		return true;
	}
	
	public void setDragStartPoint(Point dragStartPoint){
		this.dragStartPoint = dragStartPoint;
		if(this.getSelectedNode() == null){
			return;
		}
		this.dragDistance.x = this.dragStartPoint.x - this.getSelectedNode().getStartPoint().x;
		this.dragDistance.y = this.dragStartPoint.y - this.getSelectedNode().getStartPoint().y;
	}
	
	public Point getDragDistance(){
		return this.dragDistance;
	}
	
	public void addCtrlSelectedNode(StepInfo info){
		if(this.selectedMap.get(info.getId()) != null){
			return;
		}
		
		this.liststepMap.put(info.getId(), info);
		this.selectedList.add(info);
	}
	
	public ArrayList<StepInfo> getSelectedList(){
		return this.selectedList;
	}
	
	//�������ڿ������Ľڵ㶼ѡ��
	public void markInsideRecNodes(){
		StepInfo step;
		Entry<String, StepInfo> entry;
		if(this.recStartPoint.x == -1 || this.recEndPoint.x == -1){
			return;
		}
		int startX, endX, startY, endY;
		if(this.recStartPoint.x < this.recEndPoint.x){
			startX = this.recStartPoint.x;
			endX   = this.recEndPoint.x;
		}else{
			endX = this.recStartPoint.x;
			startX   = this.recEndPoint.x;
		}
		if(this.recStartPoint.y < this.recEndPoint.y){
			startY = this.recStartPoint.y;
			endY   = this.recEndPoint.y;
		}else{
			endY = this.recStartPoint.y;
			startY   = this.recEndPoint.y;
		}
		//��ʼ�ж�
		for(Iterator<Entry<String, StepInfo>> it = infoTree.getStepInfoMap().entrySet().iterator(); it.hasNext();){
			entry = it.next();
			step = entry.getValue();
			if(this.selectedMap.get(step.getId())!=null || this.liststepMap.get(step.getId()) != null || step == this.selectedNode){
				continue;
			}
			if(step.getStartPoint().x >= startX && step.getStartPoint().y >= startY && 
			   (step.getStartPoint().x+step.getRectWidth()) <= endX && (step.getStartPoint().y+step.getRectHeight()) <= endY ){
				this.addCtrlSelectedNode(step);
			}
		}
	}
	
	public void setRecStartPoint(int x, int y){
		this.recStartPoint.x = x;
		this.recStartPoint.y = y;
	}
	
	public void setRecEndPoint(int x, int y){
		this.recEndPoint.x = x;
		this.recEndPoint.y = y;
	}
	
	
	private StepInfoTree infoTree;
	
	//��¼ÿ���㼶���漰�Ĳ�����Ϣ
	private HashMap<String, ArrayList<StepInfo>> stepLevelMap;
	//��ѡ�еĽڵ㣬���Ҫ��������ʾ
	private HashMap<String, StepInfo> selectedMap;	//��ѡ�е�
	//��ѡ�еĽڵ㣬�������������ʱ������
	private StepInfo selectedNode;
	//��ctrl��ѡ�е�
	private ArrayList<StepInfo> selectedList;
	private HashMap<String, StepInfo> liststepMap;
	
	//CHK��ǰ�ò��裬��������
	private ArrayList<StepInfo> initNodeList;
	
	private Point dragStartPoint;	//�϶���ʼλ��
	private Point dragDistance = new Point(0,0);		//������뿪ʼλ�õĲ��
	
//	private HashMap<String, StepInfo> selectStepMap;
	
	//������Щ�ǻ�ͼ��Ƶ�һЩ��ɫ���������Ϣ������Ĭ�ϵģ��Ժ��ٿ�����ô���û�
//	private Font font = new Font("����", Font.PLAIN, 14);
	private BasicStroke basicStroke = new BasicStroke(1);		//ƽʱ�������ߴ�
	private BasicStroke selectedStroke = new BasicStroke(2);	//��ѡ��֮����ߴ�
	private Color basicColor  = ColorFactory.getInstance().getColor("BLACK");		//��ɫ
	private Color selectedColor = ColorFactory.getInstance().getColor("RED");		//��ɫ
	private final Color BASIC_SPLIT_COLOR = ColorFactory.getInstance().getColor("BSPLIT");
	
	//ÿ�����Ļ�����С
	private int nodeWidth  = UiContext.getInstance().getNodeWidth();
	private int nodeHeight = UiContext.getInstance().getNodeHeight();
	private int widthSpilt = UiContext.getInstance().getWidthSplit();
	private int heightSplit = UiContext.getInstance().getHeightSplit();
	
	private int viewHeight = 1000;
	private int viewWidth  = 1000;
	
	private int[] levelCurPos;
	
	//��һ��icon����
	private ImageIcon arrow;
	private ImageIcon selectedArrow;
	private Image arrowImg;
	private Image selectedArrowImg;
	private DrawViewMouseAction mouseAction;
	//���߿�
	private Point recStartPoint = new Point(-1,-1);
	private Point recEndPoint = new Point(-1,-1);
}