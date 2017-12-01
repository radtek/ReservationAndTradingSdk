package com.icbc.devp.tool.filter;

import java.io.File;
import java.io.FilenameFilter;

public class LastNameFilter implements FilenameFilter{

	public LastNameFilter(String lastName){
		this.lastName = lastName.toLowerCase();
	}
	
	public boolean accept(File dir, String name) {
		File file = new File(dir,name);
		if(!file.isFile()){
			return false;
		}
		String fileName = name.toLowerCase();
		if(!fileName.endsWith(this.lastName)){
			return false;
		}else{
			return true;
		}
	}
	
	private String lastName;
}
