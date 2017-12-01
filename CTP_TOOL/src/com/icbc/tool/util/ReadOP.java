package com.icbc.tool.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.icbc.main.ReadFiles;
import com.icbc.tool.ctp3.FileObj;
import com.icbc.tool.db.CTP3OPBean;
import com.icbc.tool.db.CTPBean;

public class ReadOP {

	/**
	 * ctp3OPSteps 存储ctp3的处理过程
	 * */
	HashSet<CTP3OPStep> ctp3OPSteps = new HashSet<CTP3OPStep>();

	public String getOPName(String str) {
		String flowcName = "";
		int idx = str.lastIndexOf(".op");
		while (idx >= 0 && str.charAt(idx) != '\\')
			idx--;
		if (idx >= 0) {
			flowcName = str.substring(idx + 1, str.length());
		}
		return flowcName;
	}

   public String handleJSP(String str){
	   String[] arr = str.split("/");
	   if(arr.length>1){
		 return arr[arr.length-2]+"\\"+arr[arr.length-1];   
	   }else{
		   return str;
	   }
   }
    
	public CTP3OPBean readOP(String file) {
		CTP3OPBean result = new CTP3OPBean();
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(file);

			// 下面开始读取
			Element root = doc.getDocumentElement(); // 获取根元素
            
			result.setOpStepID(root.getAttribute("type"));//获取CTP3 OP的名字
            
			NodeList items = root.getElementsByTagName("opstep");
			for (int i = 0; i < items.getLength(); i++) {
				Element ss = (Element) items.item(i);
				CTP3OPStep tmp = new CTP3OPStep();
				tmp.setOpStepID(ss.getAttribute("id"));
				tmp.setOpStepType(ss.getAttribute("type"));
				//处理链向JSP页面
				boolean isJSP = ss.getAttribute("type").toLowerCase().indexOf("page")==-1 ? false : true;
				//处理链向存储过程文件
				boolean isPROC = ss.getAttribute("type").toLowerCase().indexOf("procedure")==-1? false : true;
				//处理DSR文件
				boolean isDSR = ss.getAttribute("type").toLowerCase().indexOf("dsr")==-1?false:true;
				
				NodeList inputsItem = ss.getElementsByTagName("inputs");
				for (int j = 0; j < inputsItem.getLength(); ++j) {
					Element sta = (Element) inputsItem.item(j);
					NodeList states = sta.getElementsByTagName("input");
					if(isPROC){//处理链向存储过程文件
						Element state = (Element) states.item(0);
						tmp.getOpStepInputs().add(state.getAttribute("src"));
						result.getRelJSP().add(state.getAttribute("src").substring(1, state.getAttribute("src").length()-1));
					}
					else if(isJSP){//处理链向JSP页面
					  for (int k = 0; k < states.getLength(); ++k) {
						Element state = (Element) states.item(k);
						tmp.getOpStepInputs().add(state.getAttribute("src"));
						if(state.getAttribute("src").toLowerCase().indexOf(".jsp")!=-1)
						  result.getRelJSP().add(handleJSP(state.getAttribute("src")));
						}
					}else if(isDSR){//处理DSR文件
						for (int k = 0; k < states.getLength(); ++k) {
							Element state = (Element) states.item(k);
							tmp.getOpStepInputs().add(state.getAttribute("src"));
							if(state.getAttribute("name").toLowerCase().indexOf("tradeid")!=-1)
									result.getRelJSP().add(state.getAttribute("src").substring(1, state.getAttribute("src").length()-1));
							  }	
						}
					}
				
				NodeList statesItem = ss.getElementsByTagName("states");
				for (int j = 0; j < statesItem.getLength(); ++j) {
					Element sta = (Element) statesItem.item(j);
					NodeList states = sta.getElementsByTagName("state");
					for (int k = 0; k < states.getLength(); ++k) {
						Element state = (Element) states.item(k);
						tmp.getOpStepStates().add(state.getAttribute("next"));
					}
				}
				ctp3OPSteps.add(tmp);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	
}

class CTP3OPStep {
	private String opStepID;
	private String opStepType;
	private List<String> opStepStates = new ArrayList<String>(); // ctp3 op跳转链接
	private List<String> opStepInputs = new ArrayList<String>(); // ctp3 op传入条件

	public List<String> getOpStepInputs() {
		return opStepInputs;
	}

	public void setOpStepInputs(List<String> opStepInputs) {
		this.opStepInputs = opStepInputs;
	}

	public String getOpStepID() {
		return opStepID;
	}

	public void setOpStepID(String opStepID) {
		this.opStepID = opStepID;
	}

	public String getOpStepType() {
		return opStepType;
	}

	public void setOpStepType(String opStepType) {
		this.opStepType = opStepType;
	}

	public List<String> getOpStepStates() {
		return opStepStates;
	}

	public void setOpStepStates(List<String> opStepStates) {
		this.opStepStates = opStepStates;
	}
}
