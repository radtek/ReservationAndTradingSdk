/*
 * 创建日期 2008-9-24
 *
 * 更改所生成文件模板为
 * 窗口 > 首选项 > Java > 代码生成 > 代码和注释
 */
package com.icbc.devp.tool.xml;




import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import com.icbc.devp.tool.log.Log;

/**
 *
 * 
 *
 */
public class XMLLoader {
 
	public Document load(String fileName){
		try{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new File(fileName));           
			return doc;
		}catch(Exception ex){
			ex.printStackTrace();
			Log.getInstance().errorAndLog("[XMLLoader.load]ERROR:文件"+fileName+"加载失败!" + ex.getMessage());
			return null;
		}
	}
}
