package com.icbc.devp.gui.info;

import java.awt.Graphics;
import java.awt.Point;

/**TODO:������*/
public class BasicLine {

	/**TODO:���x1,y1���յ�x2,y2���г�ʼ��*/
	public BasicLine(int x1, int y1, int x2, int y2){
		this.startPoint = new Point(x1,y1);
		this.endPoint   = new Point(x2,y2);
	}
	
	public BasicLine(Point startPoint, Point endPoint){
		this.startPoint = startPoint;
		this.endPoint   = endPoint;
	}
	
	public Point getStartPoint() {
		return startPoint;
	}
	public void setStartPoint(Point startPoint) {
		this.startPoint = startPoint;
	}
	public Point getEndPoint() {
		return endPoint;
	}
	public void setEndPoint(Point endPoint) {
		this.endPoint = endPoint;
	}
	
	/**TODO:���߻�����*/
	public void show(Graphics g){
		g.drawLine(this.startPoint.x, this.startPoint.y, this.endPoint.x, this.endPoint.y);
	}

	private Point startPoint;
	private Point endPoint;
}
