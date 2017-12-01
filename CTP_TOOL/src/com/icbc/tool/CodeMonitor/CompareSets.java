package com.icbc.tool.CodeMonitor;

import java.util.HashSet;

/**
 * �Ƚϸ����ʹ��ߵ�δ�����嵥��CC��Git��¼�ĳ���䶯�����
 * ��ͬ���ĳ������ɸѡ����
 * @author kfzx-yanyj
 *
 */
public class CompareSets {

	public void compareCCandCoverage(HashSet<CCRecordBean> ccRecords,HashSet<CoverageBean>covers){
		
		boolean flag ; 
		int count = 0 ;
		for(CoverageBean cover : covers){
			flag = false;
			for(CCRecordBean cc : ccRecords){
				/**
				 * �Ƚ�������Ӧ�����ͳ������ϱ���һ��
				 */
				if (cc.getBranch().toLowerCase().indexOf(getAppName(cover.getApp_name().toLowerCase()))!=-1 && 
						cc.getElement_name().toLowerCase().equals(cover.getFile_name().toLowerCase())
						|| cc.getElement_name().toLowerCase().indexOf(cover.getFile_name().toLowerCase())!=-1
						|| isSQLCheck(cover.getFile_path().toLowerCase(),cc.getElement_name().toLowerCase())){
					flag = true;
					System.out.println("Found "+cc.getElement_name());
					break;
				}
			}
			if(!flag){
				count++;
				System.out.println("Not Found Program " + cover.getApp_name() + " "+cover.getFile_name() + " "+cover.getFile_path());
			}
		}
		//δ���ǳ�����CC���ޱ����¼�ĳ������
		System.out.println("Total = "+count);
	}
	
	public boolean isSQLCheck(String str1,String str2){
		int idx = str2.toLowerCase().indexOf(".sql");
		if(idx!=-1){
			//System.out.println("++"+str2);
			str2 = str2.substring(0,idx);
			//System.out.println("**"+str2);
		}
		if(str1.indexOf(str2)!=-1)return true;
		return false;
	}
	
	public String getAppName(String str){
	    int idx = str.indexOf("F-");
	    if(idx!=-1){
	    	str = str.substring(idx+2);
	        idx = str.lastIndexOf("-");
	        if(idx!=-1){
	    	  str = str.substring(0,idx-1);
	        }
	    }
		return str;
	}
	public void proc(){
		try {
			HashSet<CCRecordBean> ccRecords = new ReadCCRecord().readTableColumns();
			HashSet<CoverageBean> covers= new ReadCoverageDB().readTableColumns();
			
			compareCCandCoverage(ccRecords,covers);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args){
		new CompareSets().proc();
	}
}
