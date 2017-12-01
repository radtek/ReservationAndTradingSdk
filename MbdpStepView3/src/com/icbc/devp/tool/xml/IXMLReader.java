/*
 * 创建日期 2008-2-4
 *
 * 更改所生成文件模板为
 * 窗口 > 首选项 > Java > 代码生成 > 代码和注释
 */
package com.icbc.devp.tool.xml;


/**
 * @author shilei
 *
 * 更改所生成类型注释的模板为
 * 窗口 > 首选项 > Java > 代码生成 > 代码和注释
 */
public interface IXMLReader {
	public static String DEBUG_VER = "<#NOVA+ V1.1.0#PRS V2.8.0#F-PBMS V2.14.01.0#>";
	public static String DEBUG_PRG_VER = "<#LQH#070319#N#0000#>";

	 
	public XMLSaveContext read(String fileName);	

}
