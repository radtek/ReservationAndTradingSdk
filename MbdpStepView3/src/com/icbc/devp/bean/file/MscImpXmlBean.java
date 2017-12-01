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

/**TODO:法人框架格式的导入文件配置BEAN*/
public class MscImpXmlBean {

	public MscImpXmlBean(String filePath){
		this.fieldList     = new ArrayList<MscFileFieldBean>();
		this.fieldIndexMap = new HashMap<String,Integer>();
		this.summaryBean   = new MscFileSummaryBean();
		this.sqlldrBean    = new MscFileSqlldrBean();
		this.filePath      = filePath;
	}
	
	/**TODO:加载xml配置文件，如果加载成功返回true，否则返回false。
	 * @param String filePath
	 * 配置文件所在的绝对路径*/
	public boolean loadConfig(){
		String tag = "[MscImpXmlBean.loadConfig]";
		String errinfo;
		int tmpNum;
		String tmpStr;
		try{
			//先判断文件是否存在
			File file = new File(filePath);
			if(!file.isFile()){
				errinfo = tag + "FILE_NOT_FOUND，文件"+filePath+"不存在！";
				Log.getInstance().errorAndLog(errinfo);
				return false;
			}
			Document doc = (new XMLLoader()).load(filePath); 
			if(doc==null){
				Log.getInstance().errorAndLog(tag + "ERROR:打开"+filePath+"失败!");
				return false;
			}
			//读进来后保存
			XMLSaveContext xmlCtn = (new XMLConfigParser()).parse(doc.getDocumentElement());
			//然后逐个属性开始加载
			/*################先加载概要信息########################*/
			XMLSaveNode[] nodes = xmlCtn.getSaveNodesByName("FILE_SUMMARY");
			tmpStr = nodes[0].getAttributeValueByName("FILENAME");
			summaryBean.setFileName(tmpStr.toLowerCase());	//文件id全部用小写
			tmpStr = nodes[0].getAttributeValueByName("TABLENAME");
			summaryBean.setTableName(tmpStr.toLowerCase());	//表名也全部用小写
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
			/*################再加载sqlldr导入信息########################*/
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
			/*################再加载sqlldr导入信息########################*/
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
				//设置
				bean = new MscFileFieldBean();
				bean.setColumn(fileId);
				bean.setWidth(width);
				bean.setDesc(desc);
				bean.setFilter(isFilter);
				bean.setTerminate(isTerminate);
				bean.setSpecial(special);
				bean.setConsValue(consValue);
				//加到队列
				this.fieldList.add(bean);
				//加入映射关系中
				this.fieldIndexMap.put(fileId, i);
			}
			//全部加载完毕，返回true
			return true;
		}catch(Exception e){
			errinfo = tag + "Exception,加载文件\""+filePath+"\"过程发生异常："+e.getMessage();
			Log.getInstance().errorAndLog(errinfo);
			return false;
		}
	}
	
	/**TODO:获取配置文件的概要信息BEAN*/
	public MscFileSummaryBean getSummaryBean() {
		return summaryBean;
	}

	/**TODO:获取配置文件的导入信息BEAN*/
	public MscFileSqlldrBean getSqlldrBean() {
		return sqlldrBean;
	}

	/**TODO:获取文件的字段配置列表BEAN*/
	public ArrayList<MscFileFieldBean> getFieldList() {
		return fieldList;
	}

	/**TODO:获取文件的字段顺序对照关系HASHMAP*/
	public HashMap<String, Integer> getFieldIndexMap() {
		return fieldIndexMap;
	}

	/**TODO:获取本配置的唯一标识id（小写），也就是文件id*/
	public String getId(){
		return this.summaryBean.getFileName();
	}
	
	/**TODO:获取文件所代表的实际路径*/
	public String getFilePath(){
		return this.filePath;
	}
	
	/**TODO:获取UDS文件*/
	public String getUdsTargetDir(){
		StringBuffer buf = new StringBuffer();
		buf.append("/snfs1/"+this.summaryBean.getAppFlag()+"/target/"+this.summaryBean.getTargetInterface()+"/");
		if("PT".equals(this.summaryBean.getInterfaceType())){
			buf.append(this.summaryBean.getSrcInterface()+"/");
		}
		buf.append(this.summaryBean.getFloor()+"/");
		return buf.toString();
	}
	
	/**TODO:输出配置文件对应的一些信息*/
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
	/**TODO:跟getSummaryInfo格式一致，这个用来获取表头的*/
	public static String getSummaryHead(){
		StringBuffer buf = new StringBuffer();
		buf.append("文件名")
		   .append(",")
		   .append("表名")
		   .append(",")
		   .append("导入方式")
		   .append(",")
		   .append("文件类型")
		   .append(",")
		   .append("分隔符")
		   .append(",")
		   .append("是否必须")
		   .append(",")
		   .append("源接口名")
		   .append(",")
		   .append("目标接口名")
		   .append(",")
		   .append("场次")
		   .append(",")
		   .append("是否支持并发导入")
		   .append(",")
		   .append("容错记录数")
		   .append(",")
		   .append("跳过维护索引")
		   .append(",")
		   .append("配置文件路径")
		   .append(",")
		   .append("UDS接收目录");
		return buf.toString();
	}
	
	/*成员变量*/
	private String filePath;
	//概要属性变量
	private MscFileSummaryBean summaryBean;
	private MscFileSqlldrBean  sqlldrBean;
	private ArrayList<MscFileFieldBean> fieldList;	//加载字段列表
	//key是字段名（小写），value是fieldList中元素所在位置
	private HashMap<String,Integer> fieldIndexMap;	//保存字段名跟字段下标之间的对应关系
}
