package com.icbc.devp.bean.file;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Document;

import com.icbc.devp.tool.log.Log;
import com.icbc.devp.tool.xml.XMLSaveContext;
import com.icbc.devp.tool.xml.XMLLoader;
import com.icbc.devp.tool.xml.XMLConfigParser;
import com.icbc.devp.tool.xml.XMLSaveNode;

/**TODO:���˿�ܸ�ʽ�ĵ����ļ�����BEAN*/
public class MscImpXmlBean {

	public MscImpXmlBean(String filePath){
		this.fieldList     = new ArrayList<MscFileFieldBean>();
		this.fieldIndexMap = new HashMap<String,Integer>();
		this.summaryBean   = new MscFileSummaryBean();
		this.sqlldrBean    = new MscFileSqlldrBean();
		this.filePath      = filePath;
	}
	
	/**TODO:����xml�����ļ���������سɹ�����true�����򷵻�false��
	 * @param String filePath
	 * �����ļ����ڵľ���·��*/
	public boolean loadConfig(){
		String tag = "[MscImpXmlBean.loadConfig]";
		String errinfo;
		int tmpNum;
		String tmpStr;
		try{
			//���ж��ļ��Ƿ����
			File file = new File(filePath);
			if(!file.isFile()){
				errinfo = tag + "FILE_NOT_FOUND���ļ�"+filePath+"�����ڣ�";
				Log.getInstance().errorAndLog(errinfo);
				return false;
			}
			Document doc = (new XMLLoader()).load(filePath); 
			if(doc==null){
				Log.getInstance().errorAndLog(tag + "ERROR:��"+filePath+"ʧ��!");
				return false;
			}
			//�������󱣴�
			XMLSaveContext xmlCtn = (new XMLConfigParser()).parse(doc.getDocumentElement());
			//Ȼ��������Կ�ʼ����
			/*################�ȼ��ظ�Ҫ��Ϣ########################*/
			XMLSaveNode[] nodes = xmlCtn.getSaveNodesByName("FILE_SUMMARY");
			tmpStr = nodes[0].getAttributeValueByName("FILENAME");
			summaryBean.setFileName(tmpStr.toLowerCase());	//�ļ�idȫ����Сд
			tmpStr = nodes[0].getAttributeValueByName("TABLENAME");
			summaryBean.setTableName(tmpStr.toLowerCase());	//����Ҳȫ����Сд
			tmpStr = nodes[0].getAttributeValueByName("LOADMETHOD");
			summaryBean.setLoadMethod(tmpStr);
			tmpStr = nodes[0].getAttributeValueByName("FILETYPE");
			summaryBean.setFileType(tmpStr);
			tmpStr = nodes[0].getAttributeValueByName("LENGTH");
			try{
				tmpNum = Integer.parseInt(tmpStr);
			}catch(Exception e){
				tmpNum = 0;
			}
			summaryBean.setLength(tmpNum);
			tmpStr = nodes[0].getAttributeValueByName("SEPNUM");
			summaryBean.setSepNum(tmpStr);
			tmpStr = nodes[0].getAttributeValueByName("NECESSARY");
			summaryBean.setNecessary("y".equalsIgnoreCase(tmpStr));
			tmpStr = nodes[0].getAttributeValueByName("APPFLAG");
			summaryBean.setAppFlag(tmpStr);
			tmpStr = nodes[0].getAttributeValueByName("SRC_INTERFACE");
			summaryBean.setSrcInterface(tmpStr);
			tmpStr = nodes[0].getAttributeValueByName("TAR_INTERFACE");
			summaryBean.setTargetInterface(tmpStr);
			tmpStr = nodes[0].getAttributeValueByName("FLOOR");
			summaryBean.setFloor(tmpStr);
			tmpStr = nodes[0].getAttributeValueByName("INTERFACETYPE");
			summaryBean.setInterfaceType(tmpStr);
			/*################�ټ���sqlldr������Ϣ########################*/
			nodes = xmlCtn.getSaveNodesByName("SQLLDR_PAR");
			tmpStr = nodes[0].getAttributeValueByName("DIRECT");
			this.sqlldrBean.setDirect("true".equalsIgnoreCase(tmpStr));
			tmpStr = nodes[0].getAttributeValueByName("PARALLEL");
			this.sqlldrBean.setParallel("true".equalsIgnoreCase(tmpStr));
			tmpStr = nodes[0].getAttributeValueByName("SILENT");
			this.sqlldrBean.setSilentMode(tmpStr);
			tmpStr = nodes[0].getAttributeValueByName("SKIP_INDEX_MAINTENANCE");
			this.sqlldrBean.setSkipIndex("true".equalsIgnoreCase(tmpStr));
			tmpStr = nodes[0].getAttributeValueByName("ERRORS");
			tmpNum = Integer.parseInt(tmpStr);
			this.sqlldrBean.setErrors(tmpNum);
			tmpStr = nodes[0].getAttributeValueByName("CHARACTERSET");
			this.sqlldrBean.setCharset(tmpStr);
			/*################�ټ���sqlldr������Ϣ########################*/
			nodes = xmlCtn.getSaveNodesByName("STRUCTURE")[0].getChildNodesByName("FIELD");
//			nodes = xmlCtn.getSaveNodesByName("FIELD");
			XMLSaveNode node;
			MscFileFieldBean bean;
			String fileId;
			int width;
			String desc;
			boolean isFilter;
			String special, consValue;
			boolean isTerminate;
			for(int i=0; i<nodes.length; i++){
				node = nodes[i];
				fileId = node.getAttributeValueByName("COLUMN").toLowerCase();
				width  = Integer.parseInt(node.getAttributeValueByName("WIDTH").trim());
				desc   = node.getAttributeValueByName("DESC");
				isFilter = "Y".equalsIgnoreCase(node.getAttributeValueByName("FILTER"));
				isTerminate = "true".equalsIgnoreCase(node.getAttributeValueByName("ISTERMINATED"));
				special  = node.getAttributeValueByName("SPECIAL");
				consValue = node.getAttributeValueByName("DEFAULT");
				//����
				bean = new MscFileFieldBean();
				bean.setColumn(fileId);
				bean.setWidth(width);
				bean.setDesc(desc);
				bean.setFilter(isFilter);
				bean.setTerminate(isTerminate);
				bean.setSpecial(special);
				bean.setConsValue(consValue);
				//�ӵ�����
				this.fieldList.add(bean);
				//����ӳ���ϵ��
				this.fieldIndexMap.put(fileId, i);
			}
			//ȫ��������ϣ�����true
			return true;
		}catch(Exception e){
			errinfo = tag + "Exception,�����ļ�\""+filePath+"\"���̷����쳣��"+e.getMessage();
			Log.getInstance().errorAndLog(errinfo);
			return false;
		}
	}
	
	/**TODO:��ȡ�����ļ��ĸ�Ҫ��ϢBEAN*/
	public MscFileSummaryBean getSummaryBean() {
		return summaryBean;
	}

	/**TODO:��ȡ�����ļ��ĵ�����ϢBEAN*/
	public MscFileSqlldrBean getSqlldrBean() {
		return sqlldrBean;
	}

	/**TODO:��ȡ�ļ����ֶ������б�BEAN*/
	public ArrayList<MscFileFieldBean> getFieldList() {
		return fieldList;
	}

	/**TODO:��ȡ�ļ����ֶ�˳����չ�ϵHASHMAP*/
	public HashMap<String, Integer> getFieldIndexMap() {
		return fieldIndexMap;
	}

	/**TODO:��ȡ�����õ�Ψһ��ʶid��Сд����Ҳ�����ļ�id*/
	public String getId(){
		return this.summaryBean.getFileName();
	}
	
	/**TODO:��ȡ�ļ��������ʵ��·��*/
	public String getFilePath(){
		return this.filePath;
	}
	
	/**TODO:��ȡUDS�ļ�*/
	public String getUdsTargetDir(){
		StringBuffer buf = new StringBuffer();
		buf.append("/snfs1/"+this.summaryBean.getAppFlag()+"/target/"+this.summaryBean.getTargetInterface()+"/");
		if("PT".equals(this.summaryBean.getInterfaceType())){
			buf.append(this.summaryBean.getSrcInterface()+"/");
		}
		buf.append(this.summaryBean.getFloor()+"/");
		return buf.toString();
	}
	
	/**TODO:��������ļ���Ӧ��һЩ��Ϣ*/
	public String getSummaryInfo(){
		StringBuffer buf = new StringBuffer();
		buf.append(this.summaryBean.getFileName().toUpperCase())
		   .append(",")
		   .append((this.summaryBean.getTableName()).toUpperCase())
		   .append(",")
		   .append(this.summaryBean.getLoadMethod())
		   .append(",")
		   .append(this.summaryBean.getFileType())
		   .append(",")
		   .append(this.summaryBean.getSepNum())
		   .append(",")
		   .append(this.summaryBean.isNecessary()?"Y":"N")
		   .append(",")
		   .append(this.summaryBean.getSrcInterface())
		   .append(",")
		   .append(this.summaryBean.getTargetInterface())
		   .append(",")
		   .append(this.summaryBean.getFloor())
		   .append(",")
		   .append(this.sqlldrBean.getParallel())
		   .append(",")
		   .append(this.sqlldrBean.getErrors())
		   .append(",")
		   .append(this.sqlldrBean.isSkipIndex()?"SKIP":"NOT_SKIP")
		   .append(",")
		   .append(this.filePath)
		   .append(",")
		   .append(this.getUdsTargetDir());
		return buf.toString();
	}
	/**TODO:��getSummaryInfo��ʽһ�£����������ȡ��ͷ��*/
	public static String getSummaryHead(){
		StringBuffer buf = new StringBuffer();
		buf.append("�ļ���")
		   .append(",")
		   .append("����")
		   .append(",")
		   .append("���뷽ʽ")
		   .append(",")
		   .append("�ļ�����")
		   .append(",")
		   .append("�ָ���")
		   .append(",")
		   .append("�Ƿ����")
		   .append(",")
		   .append("Դ�ӿ���")
		   .append(",")
		   .append("Ŀ��ӿ���")
		   .append(",")
		   .append("����")
		   .append(",")
		   .append("�Ƿ�֧�ֲ�������")
		   .append(",")
		   .append("�ݴ��¼��")
		   .append(",")
		   .append("����ά������")
		   .append(",")
		   .append("�����ļ�·��")
		   .append(",")
		   .append("UDS����Ŀ¼");
		return buf.toString();
	}
	
	/*��Ա����*/
	private String filePath;
	//��Ҫ���Ա���
	private MscFileSummaryBean summaryBean;
	private MscFileSqlldrBean  sqlldrBean;
	private ArrayList<MscFileFieldBean> fieldList;	//�����ֶ��б�
	//key���ֶ�����Сд����value��fieldList��Ԫ������λ��
	private HashMap<String,Integer> fieldIndexMap;	//�����ֶ������ֶ��±�֮��Ķ�Ӧ��ϵ
}
