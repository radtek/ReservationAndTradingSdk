package com.icbc.devp.gui.info;

import java.awt.Point;
import java.util.ArrayList;

/**TODO:��ϵ�߶��󣬱�ʾһ�����ӽڵ�֮��Ĺ�ϵ���ߣ�*/
public class RelLine {
	
	public RelLine(StepInfo father, StepInfo son){
		this.father = father;
		this.son = son;
		this.initLinePool();
	}
	
	/**TODO:�������Ӽ��������һ���㷨����֤���ӽڵ������һֱ���ڣ������������ڵ��λ��ʵʱ����*/
	public ArrayList<BasicLine> getLines(){
		this.lineList.clear();
		Point fsPoint = father.getStartPoint();
		Point ssPoint = son.getStartPoint();
		if(fsPoint == null || ssPoint == null){
			return this.lineList;
		}
		//�ж��Ƿ���Ҫ����
		int cmpX = ssPoint.x - fsPoint.x;
		int cmpY = ssPoint.y - fsPoint.y;
		
		int cmpWidth  = son.getRectWidth();
		int cmpHeight = son.getRectHeight();
		
		if(cmpX < 0){
			//�ø��ڵ���Ϊ�Ƚϻ�׼
			cmpWidth = father.getRectWidth();
		}
		
		if(cmpY < 0){
			cmpHeight = father.getRectWidth();
		}
		
		if(Math.abs(cmpX) <= cmpWidth && Math.abs(cmpY) <= cmpHeight){
			return this.lineList;
		}
		
		//��������˵����Ҫ���ߣ������������
		BasicLine line;
		int disX, disY;	//�����ڵ��ʵ�ʾ���
		if(cmpX > 0){
			//�������
			disX = cmpX - father.getRectWidth();
			
		}
		
		return this.lineList;
	}

	private void initLinePool(){
		this.linePool = new BasicLine[LINE_COUNT];
		for(int i=0; i<this.linePool.length; i++){
			linePool[i] = new BasicLine(0,0,0,0);
		}
	}
	
	/**TODO:�жϸ����߶��Ƿ�ѡ����*/
	public boolean isSelected(){
		return isSelected;
	}
	
	private StepInfo father;
	private StepInfo son;
	private boolean isSelected = false;
	private ArrayList<BasicLine> lineList = new ArrayList<BasicLine>();
	private BasicLine[] linePool;
	private static final int LINE_COUNT = 5;	//���5����
}
