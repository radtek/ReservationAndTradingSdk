package com.icbc.devp.impl.tmp;

import com.icbc.devp.object.file.MscImportDsObject;

public class ShowMbdpFileInfoEntry {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String path = "E:\\workspace\\MBDP\\MbdpAsistTool\\result\\tmp";
		try{
			MscImportDsObject obj = new MscImportDsObject();
			if(obj.loadAllConfigFile(path, ".xml") != 0){
				System.out.println("error-1");
//				return;
			}
//			if(!obj.writeXmlInfo("D:\\imp_config\\FileTableRel.csv")){
			if(!obj.writeFileTableRel("D:\\imp_config\\FileTableRel.csv", false, "result\\tmp")){
				System.out.println("error-2");
				return;
			}
			System.out.println("SUCCESS!!!");
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
