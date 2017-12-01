package com.icbc.devp.tool.util;

/**TODO:�����ַ���BUFFER����ʼ���Ѿ����������Ĵ�С��*/
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
	
	/**TODO:��ȡbuffer�ĵ�ǰ����*/
	public int getHoldSize(){
		return size;
	}
	
	/**TODO:��ȡ����*/
	public int length(){
		return ptr;
	}
	
	/**TODO:���buffer����������*/
	public void clear(){
		ptr = 0;
	}
	
	/**TODO:������������*/
	public void add(char ch){
		if(ptr >= size){
			this.extendSize();
		}
		array[ptr] = ch;
		ptr ++;
	}
	
	/**TODO:��ȡ�ַ�����ת����*/
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
	
	/**TODO:����*/
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
