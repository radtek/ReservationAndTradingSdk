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
	
	/**TODO:�Ƚ���������Ĵ�С�����û���ӽڵ㣬��ô�������ģ�Ȼ�󰴴�С��������*/
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
	
	/**TODO:���δ������Ͻǵĵ�Point���Լ�������εĿ�ȣ�����*/
	public StepInfo(Point startPoint, int width, int height){
		this.startPoint = startPoint;
		this.endPoint = new Point(this.startPoint.x + width, this.startPoint.y + height);
		this.image = null;
		this.fList = new ArrayList<StepInfo>();
		this.sList = new ArrayList<StepInfo>();
	}
	
	/**TODO:���δ������Ͻǵ�x��y���Լ�������εĿ�ȣ�����*/
	public StepInfo(int startX, int startY, int width, int height){
		this.startPoint = new Point(startX, startY);
		this.endPoint   = new Point(this.startPoint.x + width, this.startPoint.y + height);
		this.image = null;
		this.fList = new ArrayList<StepInfo>();
		this.sList = new ArrayList<StepInfo>();
	}
	
	/**TODO:�жϵ��Ƿ��ڱ�������*/
	public boolean constains(Point point){
		if(point == null){
			return false;
		}
		//�����Ƿ���������
		if(point.x >= this.startPoint.x && point.x <= this.endPoint.x && point.y >= startPoint.y && point.y <= this.endPoint.y){
			return true;
		}
		//����������
		return false;
	}
	
	/**TODO:��ȡ���ο��*/
	public int getRectWidth(){
		return this.endPoint.x - this.startPoint.x;
	}
	
	/**TODO:��ȡ���θ߶�*/
	public int getRectHeight(){
		return this.endPoint.y - this.startPoint.y;
	}
	
	/**TODO: ��ȡ��������*/
	public Rectangle getViewRec(){
		return new Rectangle(this.startPoint.x, this.startPoint.y, this.getRectWidth() + 10, this.getRectHeight() + 10);
	}
	
	/**TODO:��ȡ��ʼλ��*/
	public Point getStartPoint() {
		return startPoint;
	}


	/**TODO:�������Ͻǵ��λ�ã���ʼ��ʱ���ã�*/
	public void setStartPoint(Point startPoint) {
		this.startPoint = startPoint;
	}

	/**TODO:���þ��εĿ�ȣ�Ҫ�����þ��ε���ʼ��*/
	public void setSize(int width, int height){
		if(this.endPoint == null){
			this.endPoint = new Point(this.startPoint.x + width, this.startPoint.y + height);
			return;
		}
		this.endPoint.x = this.startPoint.x + width;
		this.endPoint.y = this.startPoint.y + height;
	}

	/**TODO:����λ��*/
	public void setLocation(Point point){
		int width = this.endPoint.x - this.startPoint.x;
		int height = this.endPoint.y - this.startPoint.y;
		this.startPoint = point;
		this.endPoint.x = this.startPoint.x + width;
		this.endPoint.y = this.startPoint.y + height;
	}
	
	/**TODO:����λ��*/
	public void setLocation(int x, int y){
		int width = this.endPoint.x - this.startPoint.x;
		int height = this.endPoint.y - this.startPoint.y;
		this.startPoint.x = x;
		this.startPoint.y = y;
		this.endPoint.x = x + width;
		this.endPoint.y = y + height;
	}
	
	/**TODO:��ȡͼƬ*/
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
	
	/**TODO:��ʼ��ͼƬ�����С*/
	private void initImage(){
		Graphics2D g2d = (Graphics2D)image.getGraphics();
		g2d.setColor(new Color(223,248,219));
		Color color;
		Stroke stroke;
		//����ɫ��
		if("0".equals(this.getInfo().getEnableFlag())){
			//����
			color = g2d.getColor();
			g2d.setColor(DISABLE_COLOR);
			stroke = g2d.getStroke();
			g2d.setStroke(NOT_FOUND_STROKE);
			g2d.fillRect(0, 0, image.getWidth(),image.getHeight());
			g2d.setColor(color);
			g2d.setStroke(stroke);
		}else{
			//����
			g2d.fillRect(0, 0, image.getWidth(),image.getHeight());
		}
		
		//Ҫ����ȥ�Ķ���
		if(font != null){
			g2d.setFont(font);	//���廹��Ҫ���ú�
		}else
		{
			font = UIManager.getFont("Panel.font");
			g2d.setFont(font);
		}
		
		
		//��ͼ��ʼ����λ��
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
//		//�����
//		if(img != null){
//			curX += img.getWidth(null)+2;
//		}else{
//			curX += 34;
//		}
		curX+=2;
		
		g2d.setColor(new Color(0,0,0));
		curY += this.font.getSize();
		g2d.drawString(info.getStepno(), curX, curY);
		//��������
		curY += this.font.getSize() + 2;
		g2d.drawString(info.getProcDesc(), curX, curY);
		//�洢����
		curY += this.font.getSize() + 2;
		g2d.drawString(info.getProc(), curX, curY);
		//��󻭸���
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
	
	/**TODO:��ȡ���ӵ㣨��ڵ㣩λ��*/
	public Point getLineInPoint(){
		return new Point(this.startPoint.x, (this.startPoint.y + this.endPoint.y)/2);
	}
	
	/**TODO:��ȡ���ӵ㣨���ڵ㣩λ��*/
	public Point getLineOutPoint(){
		return new Point(this.endPoint.x, (this.startPoint.y + this.endPoint.y)/2);
	}
	
	/**TODO:��������*/
	public void setFont(Font font){
		this.font = font;
	}
	
	/**TODO:���ò�����Ϣ*/
	public void setStepInfo(Info info){
		this.info = info;
	}
	
	//****************************************************************************************************
	/**TODO:��ȡid*/
	public String getId(){
		try{
			return this.info.getId();
		}catch(Exception e){
			Log.getInstance().exception(e);
			return null;
		}
	}
	
	/**TODO:�ж��Ƿ��и��ڵ㣬�����ж��Ƿ�Ϊ���ڵ�*/
	public boolean hasFather(){
		return !this.fList.isEmpty();
	}
	
	/**TODO:�ж��Ƿ����ӽڵ㣬�����ж��Ƿ�Ϊ��Ҷ*/
	public boolean hasSon(){
		return !this.sList.isEmpty();
	}
	
	/**TODO:���ɽڵ�ĸ߶ȣ��������*/
	private int calHeight(ArrayList<StepInfo> tmpList){
		//������ڵ��ǿգ���߶���1������������ڵ������ȡ���ķ���
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

	/**TODO:��Ľڵ����*/
	public int getHeight() {
		return height;
	}


	public void setHeight(int height) {
		this.height = height;
	}

    /**TODO:��Ӹ��ڵ�*/
	public void addFahterNode(StepInfo node){
		this.fList.add(node);
	}
	
	/**TODO:����ӽڵ�*/
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
	
	/**TODO:�ж��Ƿ��ʼ�����͵Ľڵ�*/
	public boolean isInitType(){
		if(this.fList.isEmpty() && this.sList.isEmpty()){
			if("INIT".equals(this.info.getProcType()) || "CHK".equals(this.info.getProcType()) || "JUMP".equals(this.info.getProcType())){
				return true;
			}
		}
		return false;
	}
	
	/**TODO: �Ƚϴ���ĺ�ʵ�ʵĸ��ڵ��Ƿ�һ�£������һ�£�����������ݷ��ظ����
	 * ��һλ��STEPNO���ڶ�λ�Ǳ�־��0��ʾ������ģ�1��ʾ�ٵģ�Ҫdelete�ģ�*/
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
			//û�У��ӽ�ȥ
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
		//�������˵�
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
	
	/**TODO:�ж��Ƿ�����*/
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
	//4����
	private Point startPoint;	//���Ϸ��ĵ�
	private Point endPoint;		//���·��ĵ�
	//������Ϣ
	private Info info;
	private boolean isAddedNode = false;		//�ж��Ƿ��¼ӵ�
	private ArrayList<StepInfo> fList;	//���ڵ��б�
	private ArrayList<StepInfo> sList;	//�ӽڵ���б�
	
	//�ڵ����ȣ����ڵ���0
	private int height = -1;
	//ͼƬ����
	private BufferedImage image;
	private Font font = null;
}
