package com.icbc.devp.tool.util;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.File;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

public class EnvUtil {
	
	private static EnvUtil _instance = new EnvUtil();

	public static EnvUtil getInstance(){
		return _instance;
	}
	
	/**获取根目录*/
	public String getRootPath(){
		return System.getProperty("user.dir") + File.separator+"src";
//		return System.getProperty("user.dir");
	}

	//为了不让窗口弹出去窗口之外
	public static Point getRightPoint(Point p, Dimension rec){
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		Point tmp = new Point(p.x + rec.width, p.y + rec.height);
		Point newP = p;
		if(tmp.x > d.width){
			newP.x = p.x - rec.width;
		}
		if(tmp.y > d.height){
			newP.y = p.y - rec.height;
		}
		return newP;
	}
	
	//转换到窗口位置
	public static void convertToScreenPoint(Point p,JComponent com){
		SwingUtilities.convertPointToScreen(p, com);
	}
	
}
