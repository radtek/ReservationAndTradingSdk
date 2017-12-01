package com.icbc.tool.CodeMonitor;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;

import jxl.*;
import jxl.read.biff.BiffException;
/**
 * @version 1.0
 * ��ȡ�����ʼ�鹤�����ɵ�δ���ǳ����嵥
 * @author kfzx-yanyj
 *
 */

public class ReadCodeMonitorResults {

	/**
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException 
	 * @throws BiffException 
	 */
	public HashSet<String> readCodeMonitorResults(String fileName) {
		HashSet<String> result = new HashSet<String>();
		try {
			Workbook  workbook =  Workbook.getWorkbook(new File(fileName));
		    Sheet sheet = workbook.getSheet(0);
		    
		} catch (BiffException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
		
	}
}
