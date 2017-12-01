package com.icbc.devp.object.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;

import com.icbc.devp.bean.file.MscImpXmlBean;
import com.icbc.devp.tool.file.FileUtil;
import com.icbc.devp.tool.filter.LastNameFilter;
import com.icbc.devp.tool.log.Log;

/**TODO:���˿�ܵĵ�������Դ������������ص����е��������ļ���
 * ��������Щ����������һЩͳ�ơ�У��ȹ�����*/
public class MscImportDsObject {

	/**TODO:��ʼ����������Դ����ʵ������Ӧ
	 * ��������xml�ļ�����Ŀ¼��
	 * @param String fileDir
	 * xml���ڵ�Ŀ¼*/
	public MscImportDsObject(String fileDir){
		this.fileDir = fileDir;
		this.beanList = new ArrayList<MscImpXmlBean>();
	}
	
	public MscImportDsObject(){
		this.beanList = new ArrayList<MscImpXmlBean>();
	}
	
	/**TODO:��ʼ���������е��������ļ���xml��*/
	public boolean loadAllConfigFile(){
		String tag = "[MscImportDsObject.loadAllConfigFile]";
		String errinfo;
		try{
			File dir = new File(fileDir);
			LastNameFilter filter = new LastNameFilter(".xml");
			File[] files = dir.listFiles(filter);
			MscImpXmlBean bean;
			String fileName;
			for(int i=0;i<files.length; i++){
				fileName = files[i].getName();
				if(MscImportDsObject.LOAD_CFG_FILE.equals(fileName)){
					//���������
					continue;
				}
				bean = new MscImpXmlBean(files[i].getAbsolutePath());
				if(!bean.loadConfig()){
					return false;
				}
				//����ӵ��б���
				this.beanList.add(bean);
			}
			//�������
			return true;
		}catch(Exception e){
			errinfo = tag + "Exception,�����ļ�\""+fileDir+"\"Ŀ¼���ļ������쳣��"+e.getMessage();
			Log.getInstance().errorAndLog(errinfo);
			return false;
		}
	}

	/**
	 * ��ʼ���������е��������ļ���xml��
	 * @param rootDir �ļ�·��
	 * @param lastName �ļ���׺
	 * @return 0-�����ļ����سɹ���-1-ȫ������ʧ�ܣ�-2-�����ļ�����ʧ��
	 */
	public int loadAllConfigFile(String rootDir,String lastName){
		String tag = "[MscImportDsObject.loadAllConfigFile(String)]";
		String errinfo;
		int retValue = 0;
//		try{
			File dir = new File(rootDir);
			ArrayList<String> fileList = null;
			try {
				fileList = FileUtil.scanFiles(rootDir, lastName);
			} catch (Exception e) {
				errinfo = tag + "Exception,�����ļ�\""+fileDir+"\"Ŀ¼���ļ������쳣��"+e.getMessage();
				Log.getInstance().errorAndLog(errinfo);
				return -1;
			}
			MscImpXmlBean bean;
			String fileName;
			for(int i=0;i<fileList.size(); i++){
				fileName = fileList.get(i);
				if(MscImportDsObject.LOAD_CFG_FILE.equals(fileName)){
					//���������
					continue;
				}
				bean = new MscImpXmlBean(fileName);
				if(!bean.loadConfig()){
//					return false;
					// ���������ļ����ش�������������һ��
					retValue = -2;
					continue;
				}
				//����ӵ��б���
				this.beanList.add(bean);
			}
			//�������
			return retValue;
		/*}catch(Exception e){
			errinfo = tag + "Exception,�����ļ�\""+fileDir+"\"Ŀ¼���ļ������쳣��"+e.getMessage();
			Log.getInstance().errorAndLog(errinfo);
			return false;
		}*/
	}
	
	/**TODO:�ҳ�����ͬ�ؼ��ֵ��ļ��嵥��������һ��HASHMAP��key�ǹؼ��֣�value��
	 * �ļ�����·����*/
	public HashMap<String,ArrayList<String>> findDulKeyFiles()
	  throws Exception{
		String tag = "[MscImportDsObject.findDulKeyFiles]";
		String errinfo;
		try{
			HashMap<String,ArrayList<String>> dulMap = new  HashMap<String,ArrayList<String>>();
			String fileId, cmpFileId;
			MscImpXmlBean bean,cmpBean;
			ArrayList<String> fileList;
			//���������û���ظ���id
			for(int i=0; i<this.beanList.size()-1; i++){
				bean   = beanList.get(i);
				fileId = bean.getId();
				for(int j = i+1; j<this.beanList.size();j++){
					cmpBean = beanList.get(j);
					cmpFileId = cmpBean.getId();
					if( (fileId.indexOf(cmpFileId) != -1) ||(cmpFileId.indexOf(fileId) != -1)){
						//���ظ����ӵ�map���棬�ȿ���map������û�ظ�
						fileList = dulMap.get(fileId);
						if(fileList == null){
							fileList = new ArrayList<String>();
							fileList.add(bean.getFilePath());
							fileList.add(cmpBean.getFilePath());
							dulMap.put(fileId, fileList);
						}else{
							fileList.add(cmpBean.getFilePath());
						}
					}
				}
			}
			return dulMap;
		}catch(Exception e){
			errinfo = tag + "�����ظ���ֵ�ļ����̷����쳣��" + e.getMessage();
			throw new Exception(errinfo);
		}
	}
	
	/**TODO:����������������Ϣ�����ָ���ļ���*/
	public boolean writeXmlInfo(String filePath){
		String msg = "";
		String tag = "[MscImportDsObject.writeXmlInfo]";
		try{
			ArrayList<String> lineList = new ArrayList<String>();
			String line;
			MscImpXmlBean bean;
			line = MscImpXmlBean.getSummaryHead();
			lineList.add(line);
			for(int i=0;i<beanList.size();i++){
				bean = beanList.get(i);
				line = bean.getSummaryInfo();
				lineList.add(line);
			}
			
			return FileUtil.writeFile(filePath, false, lineList);
		}catch(Exception e){
			tag = tag + "[Exception]" + e.getMessage();
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * ����FileTableRel.csv�� 
	 * @param filePath
	 * @param append
	 * @return
	 */
	public boolean writeFileTableRel(String filePath, boolean append, String localPath){
		String tag = "[MscImportDsObject.writeXmlInfo]";
		try{
			ArrayList<String> lineList = new ArrayList<String>();
			String line, line1, line2, line3;
			MscImpXmlBean bean;
			ArrayList<String> oldList;
			if (append == false) {
				line = MscImpXmlBean.getSummaryHead();
				lineList.add(line);
				
				for(int i=0;i<beanList.size();i++){
					bean = beanList.get(i);
					line = bean.getSummaryInfo();
					line1 = line.substring(0, indexOfStr(line, ",", 12) + 1);
					line3 = line.substring(indexOfStr(line, ",", 13));
					line2 = line.substring(line.indexOf(localPath, line1.length()) + localPath.length(), line.indexOf(".xml,") + 4);
					line = line1 + line2 + line3;
					lineList.add(line);
				}
			} else {
				BufferedReader reader = new BufferedReader(new FileReader(filePath));
				oldList = new ArrayList<String>();
				String tmpLine;
				tmpLine = reader.readLine();
				while((tmpLine = reader.readLine()) != null) {
					oldList.add(tmpLine);
				}
				
				for(int i=0;i<beanList.size();i++){
					bean = beanList.get(i);
					line = bean.getSummaryInfo();
					line1 = line.substring(0, indexOfStr(line, ",", 12) + 1);
					line3 = line.substring(indexOfStr(line, ",", 13));
					line2 = line.substring(line.indexOf(localPath, line1.length()) + localPath.length(), line.indexOf(".xml,") + 4);
					line = line1 + line2 + line3;
					if (!oldList.contains(line)) {
						lineList.add(line);
					}
				}
			}

			return FileUtil.writeFile(filePath, append, lineList);
		}catch(Exception e){
			tag = tag + "[Exception]" + e.getMessage();
			e.printStackTrace();
			Log.getInstance().errorAndLog(tag);
			return false;
		}
	}
	private int indexOfStr(String ostr, String subStr, int count) {
		int index = -1;
		while (count > 0) {
			index = ostr.indexOf(subStr, index + 1);
			if (index == -1) {
				return -1;
			} else {
				--count;
			}
		}
		
		return index;
	}
	
	private String fileDir; //�����ļ����ڸ�Ŀ¼
	private ArrayList<MscImpXmlBean> beanList;
	/*��̬���������xml���账��*/
	private static final String LOAD_CFG_FILE="loadcfg.xml";
	
//	public static void main(String[] args) {
////		System.out.println(indexOfStr("E:\\workspace\\MBDP\\MbdpAsistTool\\result\\tmp\\cbms\\day\\corebank18\\NFCPLSG.xml","\\", 5));
//		String tmp = "\\workspace\\MBDP\\MbdpAsistTool";
//		String[] tmpa = tmp.split(Matcher.quoteReplacement("\\"));
//		System.out.println(tmpa.length);
//	}
}
