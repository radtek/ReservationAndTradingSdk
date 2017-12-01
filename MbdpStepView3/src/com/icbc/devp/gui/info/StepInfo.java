package com.icbc.devp.gui.info;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.UIManager;

import com.icbc.devp.tool.log.Log;
import com.icbc.devp.txtdraw.base.Info;

public class StepInfo implements Comparable<StepInfo>{

	public StepInfo(){
		this.image = null;
		this.fList = new ArrayList<StepInfo>();
		this.sList = new ArrayList<StepInfo>();
	}
	
	/**TODO:比较两个步骤的大小，如果没有子节点，那么就是最大的，然后按从小到大排序*/
	public int compareTo(StepInfo other){
		if(other == null || other.getInfo() == null){
			return 1;
		}
		if(this.info == null){
			return -1;
		}
		if("NOT_FOUND".equals(this.info.getProcType())){
			return 1;
		}
		if("NOT_FOUND".equals(other.getInfo().getProcType())){
			return -1;
		}
		//
		if(!this.hasSon()){
			return -1;
		}
		if(!other.hasSon()){
			return 1;
		}
		int oth = other.getSList().size();
		if(this.sList.size() > oth){
			return 1;
		}
		if(this.sList.size() == oth){
			int fat = other.getFList().size();
			if(this.getFList().size() > fat){
				return 1;
			}
			if(this.getFList().size() < fat){
				return -1;
			}
			return 0;
		}
		return -1;
	}
	
	/**TODO:依次传入左上角的点Point，以及这个矩形的宽度，长度*/
	public StepInfo(Point startPoint, int width, int height){
		this.startPoint = startPoint;
		this.endPoint = new Point(this.startPoint.x + width, this.startPoint.y + height);
		this.image = null;
		this.fList = new ArrayList<StepInfo>();
		this.sList = new ArrayList<StepInfo>();
	}
	
	/**TODO:依次传入左上角的x，y，以及这个矩形的宽度，长度*/
	public StepInfo(int startX, int startY, int width, int height){
		this.startPoint = new Point(startX, startY);
		this.endPoint   = new Point(this.startPoint.x + width, this.startPoint.y + height);
		this.image = null;
		this.fList = new ArrayList<StepInfo>();
		this.sList = new ArrayList<StepInfo>();
	}
	
	/**TODO:判断点是否在本区域内*/
	public boolean constains(Point point){
		if(point == null){
			return false;
		}
		//看看是否在区域内
		if(point.x >= this.startPoint.x && point.x <= this.endPoint.x && point.y >= startPoint.y && point.y <= this.endPoint.y){
			return true;
		}
		//不在区域内
		return false;
	}
	
	/**TODO:获取矩形宽度*/
	public int getRectWidth(){
		return this.endPoint.x - this.startPoint.x;
	}
	
	/**TODO:获取矩形高度*/
	public int getRectHeight(){
		return this.endPoint.y - this.startPoint.y;
	}
	
	/**TODO: 获取可是区域*/
	public Rectangle getViewRec(){
		return new Rectangle(this.startPoint.x, this.startPoint.y, this.getRectWidth() + 10, this.getRectHeight() + 10);
	}
	
	/**TODO:获取起始位置*/
	public Point getStartPoint() {
		return startPoint;
	}


	/**TODO:设置左上角点的位置（初始化时候用）*/
	public void setStartPoint(Point startPoint) {
		this.startPoint = startPoint;
	}

	/**TODO:设置矩形的宽度，要先设置矩形的起始点*/
	public void setSize(int width, int height){
		if(this.endPoint == null){
			this.endPoint = new Point(this.startPoint.x + width, this.startPoint.y + height);
			return;
		}
		this.endPoint.x = this.startPoint.x + width;
		this.endPoint.y = this.startPoint.y + height;
	}

	/**TODO:设置位置*/
	public void setLocation(Point point){
		int width = this.endPoint.x - this.startPoint.x;
		int height = this.endPoint.y - this.startPoint.y;
		this.startPoint = point;
		this.endPoint.x = this.startPoint.x + width;
		this.endPoint.y = this.startPoint.y + height;
	}
	
	/**TODO:设置位置*/
	public void setLocation(int x, int y){
		int width = this.endPoint.x - this.startPoint.x;
		int height = this.endPoint.y - this.startPoint.y;
		this.startPoint.x = x;
		this.startPoint.y = y;
		this.endPoint.x = x + width;
		this.endPoint.y = y + height;
	}
	
	/**TODO:获取图片*/
	public BufferedImage getImage(){
		if(this.image == null){
			this.image = new BufferedImage(this.endPoint.x - this.startPoint.x, this.endPoint.y - this.startPoint.y, BufferedImage.TYPE_INT_RGB);
			this.initImage();
		}
		return this.image;
	}

	public void repaintImage(){
		if(this.image == null){
			this.getImage();
			return;
		}
		this.initImage();
	}
	
	/**TODO:初始化图片区域大小*/
	private void initImage(){
		Graphics2D g2d = (Graphics2D)image.getGraphics();
		g2d.setColor(new Color(223,248,219));
		Color color;
		Stroke stroke;
		//背景色调
		if("0".equals(this.getInfo().getEnableFlag())){
			//禁用
			color = g2d.getColor();
			g2d.setColor(DISABLE_COLOR);
			stroke = g2d.getStroke();
			g2d.setStroke(NOT_FOUND_STROKE);
			g2d.fillRect(0, 0, image.getWidth(),image.getHeight());
			g2d.setColor(color);
			g2d.setStroke(stroke);
		}else{
			//启用
			g2d.fillRect(0, 0, image.getWidth(),image.getHeight());
		}
		
		//要画上去的东西
		if(font != null){
			g2d.setFont(font);	//字体还是要设置好
		}else
		{
			font = UIManager.getFont("Panel.font");
			g2d.setFont(font);
		}
		
		
		//画图开始结束位置
		int curY = 2;
		int curX = 2;
//		Image img = null;
//		try{
//			img = this.info.getIcon().getImage();
//			g2d.drawImage(img,curX,curY,null);
//		}catch(Exception e){
//			
//		}
//		
//		//步骤号
//		if(img != null){
//			curX += img.getWidth(null)+2;
//		}else{
//			curX += 34;
//		}
		curX+=2;
		
		g2d.setColor(new Color(0,0,0));
		curY += this.font.getSize();
		g2d.drawString(info.getStepno(), curX, curY);
		//步骤描述
		curY += this.font.getSize() + 2;
		g2d.drawString(info.getProcDesc(), curX, curY);
		//存储过程
		curY += this.font.getSize() + 2;
		g2d.drawString(info.getProc(), curX, curY);
		//最后画个框
		if(getNotFoundStepId().equals(this.info.getProcType())){
			color = g2d.getColor();
			g2d.setColor(NOT_FOUND_COLOR);
			stroke = g2d.getStroke();
			g2d.setStroke(NOT_FOUND_STROKE);
			g2d.drawRect(0, 0, image.getWidth()-1, image.getHeight()-1);
			g2d.setColor(color);
			g2d.setStroke(stroke);
		}else{
			g2d.drawRect(0, 0, image.getWidth()-1, image.getHeight()-1);
		}
	}
	
	public static String getNotFoundStepId(){
		return "NOT_FOUND";
	}
	
	/**TODO:获取连接点（入节点）位置*/
	public Point getLineInPoint(){
		return new Point(this.startPoint.x, (this.startPoint.y + this.endPoint.y)/2);
	}
	
	/**TODO:获取连接点（出节点）位置*/
	public Point getLineOutPoint(){
		return new Point(this.endPoint.x, (this.startPoint.y + this.endPoint.y)/2);
	}
	
	/**TODO:设置字体*/
	public void setFont(Font font){
		this.font = font;
	}
	
	/**TODO:设置步骤信息*/
	public void setStepInfo(Info info){
		this.info = info;
	}
	
	//****************************************************************************************************
	/**TODO:获取id*/
	public String getId(){
		try{
			return this.info.getId();
		}catch(Exception e){
			Log.getInstance().exception(e);
			return null;
		}
	}
	
	/**TODO:判断是否有父节点，用于判断是否为根节点*/
	public boolean hasFather(){
		return !this.fList.isEmpty();
	}
	
	/**TODO:判断是否有子节点，用于判断是否为树叶*/
	public boolean hasSon(){
		return !this.sList.isEmpty();
	}
	
	/**TODO:生成节点的高度，深度优先*/
	private int calHeight(ArrayList<StepInfo> tmpList){
		//如果父节点是空，则高度是1，否则逐个父节点遍历获取最大的返回
		if(tmpList == null || tmpList.isEmpty()){
			return 1;
		}
		int maxHeight = 0;
		int tmpHeight;
		StepInfo node;
		for(int i=0;i<tmpList.size();i++){
			node = tmpList.get(i);
			tmpHeight = this.calHeight(node.getFList());
			if(tmpHeight > maxHeight){
				maxHeight = tmpHeight;
			}
		}
		return maxHeight + 1;
	}

	public void setSList(ArrayList<StepInfo> list) {
		sList = list;
	}
	
	public Info getInfo() {
		return info;
	}

	/**TODO:或的节点深度*/
	public int getHeight() {
		return height;
	}


	public void setHeight(int height) {
		this.height = height;
	}

    /**TODO:添加父节点*/
	public void addFahterNode(StepInfo node){
		this.fList.add(node);
	}
	
	/**TODO:添加子节点*/
	public void addSonNode(StepInfo node){
		this.sList.add(node);
	}
	
	public int calHeight(boolean flag){
		if(!flag && this.height != -1){
			return this.height;
		}
		this.height = this.calHeight(this.fList);
		return this.height;
	}

	public ArrayList<StepInfo> getFList() {
		return fList;
	}


	public void setFList(ArrayList<StepInfo> list) {
		fList = list;
	}


	public ArrayList<StepInfo> getSList() {
		return sList;
	}
	
	/**TODO:判断是否初始化类型的节点*/
	public boolean isInitType(){
		if(this.fList.isEmpty() && this.sList.isEmpty()){
			if("INIT".equals(this.info.getProcType()) || "CHK".equals(this.info.getProcType()) || "JUMP".equals(this.info.getProcType())){
				return true;
			}
		}
		return false;
	}
	
	/**TODO: 比较传入的和实际的父节点是否一致，如果不一致，将差异的内容返回给外层
	 * 第一位是STEPNO，第二位是标志，0表示多出来的，1表示少的（要delete的）*/
	public ArrayList<String[]> compareList(ArrayList<StepInfo> infoList, boolean isFather){
		ArrayList<StepInfo> tmpList;
		if(isFather){
			tmpList = this.fList;
		}else{
			tmpList = this.sList;
		}
		String[] lines;
		StepInfo inf;
		boolean isFound;
		ArrayList<String[]> lineList = new ArrayList<String[]>();
		for(int i = 0; i<infoList.size(); i++){
			inf = infoList.get(i);
			isFound = false;
			for(int k=0; k<tmpList.size(); k++){
				if(inf == tmpList.get(k)){
					isFound = true;
					break;
				}
			}
			//没有，加进去
			if(!isFound){
				lines = new String[3];
				if(isFather){
					lines[0] = inf.getInfo().getStepno();
					lines[1] = this.info.getStepno();
					lines[2] = "0";
				}else{
					lines[0] = this.info.getStepno();
					lines[1] = inf.getInfo().getStepno();
					lines[2] = "0";
				}
				lineList.add(lines);
			}
		}
		//看看少了的
		for(int i=0; i<tmpList.size(); i++){
			inf = tmpList.get(i);
			isFound = false;
			for(int k=0; k<infoList.size(); k++){
				if(inf == infoList.get(k)){
					isFound = true;
					break;
				}
			}
			if(!isFound){
				lines = new String[3];
				if(isFather){
					lines[0] = inf.getInfo().getStepno();
					lines[1] = this.info.getStepno();
					lines[2] = "1";
				}else{
					lines[0] = this.info.getStepno();
					lines[1] = inf.getInfo().getStepno();
					lines[2] = "1";
				}
				lineList.add(lines);
			}
		}
		return lineList;
	}
	
	/**TODO:判断是否启用*/
	public boolean isEnable(){
		if(info == null){
			return false;
		}
		return "1".equals(this.info.getEnableFlag());
	}
	
	public boolean isStartNode(){
		return info.isStartNode();
	}
	
	public void setNewNodeFlag(boolean flag){
		this.isAddedNode = flag;
	}
	
	public boolean isNewNode(){
		return this.isAddedNode;
	}

	private static final Color NOT_FOUND_COLOR = new Color(82,60,250);
	private static final Stroke NOT_FOUND_STROKE = new BasicStroke(3);
	private static final Color DISABLE_COLOR = new Color(205,204,219);
	//4个点
	private Point startPoint;	//左上方的点
	private Point endPoint;		//右下方的点
	//步骤信息
	private Info info;
	private boolean isAddedNode = false;		//判断是否新加的
	private ArrayList<StepInfo> fList;	//父节点列表
	private ArrayList<StepInfo> sList;	//子节点点列表
	
	//节点的深度，根节点是0
	private int height = -1;
	//图片区域
	private BufferedImage image;
	private Font font = null;
}
