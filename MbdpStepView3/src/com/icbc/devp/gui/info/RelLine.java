package com.icbc.devp.gui.info;

import java.awt.Point;
import java.util.ArrayList;

/**TODO:关系线对象，表示一个父子节点之间的关系（线）*/
public class RelLine {
	
	public RelLine(StepInfo father, StepInfo son){
		this.father = father;
		this.son = son;
		this.initLinePool();
	}
	
	/**TODO:画出父子间的线条（一个算法，保证父子节点的线条一直存在），根据两个节点的位置实时生成*/
	public ArrayList<BasicLine> getLines(){
		this.lineList.clear();
		Point fsPoint = father.getStartPoint();
		Point ssPoint = son.getStartPoint();
		if(fsPoint == null || ssPoint == null){
			return this.lineList;
		}
		//判断是否需要画线
		int cmpX = ssPoint.x - fsPoint.x;
		int cmpY = ssPoint.y - fsPoint.y;
		
		int cmpWidth  = son.getRectWidth();
		int cmpHeight = son.getRectHeight();
		
		if(cmpX < 0){
			//用父节点作为比较基准
			cmpWidth = father.getRectWidth();
		}
		
		if(cmpY < 0){
			cmpHeight = father.getRectWidth();
		}
		
		if(Math.abs(cmpX) <= cmpWidth && Math.abs(cmpY) <= cmpHeight){
			return this.lineList;
		}
		
		//到了这里说明需要画线，分类进行讨论
		BasicLine line;
		int disX, disY;	//两个节点的实际距离
		if(cmpX > 0){
			//正常情况
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
	
	/**TODO:判断该条线段是否被选中了*/
	public boolean isSelected(){
		return isSelected;
	}
	
	private StepInfo father;
	private StepInfo son;
	private boolean isSelected = false;
	private ArrayList<BasicLine> lineList = new ArrayList<BasicLine>();
	private BasicLine[] linePool;
	private static final int LINE_COUNT = 5;	//最多5段线
}
