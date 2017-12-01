package com.icbc.devp.tool.util;

/**TODO:容纳字符的BUFFER，初始化已经决定了它的大小。*/
public class CharBuffer {

	public CharBuffer(){
		array = new char[INIT_LENGTH];
		size = INIT_LENGTH;
		ptr = 0;
	}
	
	public CharBuffer(int length){
		if(length<=0){
			array = new char[INIT_LENGTH];
			size = INIT_LENGTH;
			ptr = 0;
		}else{
			array = new char[length];
			size = length;
			ptr = 0;
		}

	}
	
	/**TODO:获取buffer的当前容量*/
	public int getHoldSize(){
		return size;
	}
	
	/**TODO:获取长度*/
	public int length(){
		return ptr;
	}
	
	/**TODO:清空buffer的所有内容*/
	public void clear(){
		ptr = 0;
	}
	
	/**TODO:往最后添加内容*/
	public void add(char ch){
		if(ptr >= size){
			this.extendSize();
		}
		array[ptr] = ch;
		ptr ++;
	}
	
	/**TODO:获取字符串（转换）*/
	public String toString(){
		if(ptr == 0){
			return "";
		}
		char[] tmp = new char[ptr];
		for(int i=0;i<ptr;i++){
			tmp[i] = array[i];
		}
		return String.valueOf(tmp);
	}
	
	/**TODO:扩容*/
	private void extendSize(){
		int tmpSize = size * 2;
		char[] tmpArray = new char[tmpSize];
		for(int i=0;i<ptr;i++){
			tmpArray[i] = array[i];
		}
		size = tmpSize;
		array = tmpArray;
	}
	
	
	private char[] array;
	private static final int INIT_LENGTH = 512;
	private int ptr;
	private int size;
}
