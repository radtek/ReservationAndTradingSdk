package com.icbc.devp.gui.info;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

import com.icbc.devp.tool.log.Log;
import com.icbc.devp.txtdraw.base.Info;

public class StepInfoTree {

	public StepInfoTree(){
		this.StepInfoMap = new HashMap<String, StepInfo>();
		this.root = new StepInfo();
	}
	
	/**TODO:�ж����Ƿ�Ϊ��*/
	public boolean isEmpty(){
		return this.root == null ? true : this.root.getSList().isEmpty();
	}
	
	public HashMap<String, StepInfo> getStepInfoMap() {
		return StepInfoMap;
	}

	public void setStepInfoMap(HashMap<String, StepInfo> StepInfoMap) {
		this.StepInfoMap = StepInfoMap;
	}

	/**TODO:��ȡ���ڵ�*/
	public StepInfo getRoot() {
		return root;
	}

	/**TODO:���ýڵ�*/
	public void setRoot(StepInfo root) {
		this.root = root;
		this.setNode(root);
	}
	/**TODO:����root�ڵ�*/
	public boolean setRoot(String id){
		StepInfo node = this.StepInfoMap.get(id);
		if(node == null){
			return false;
		}
		//if(this.r)
		this.root = node;
		return true;
	}

	/**TODO:���ýڵ���Ϣ*/
	public void setNode(StepInfo node){
		String id = node.getId();
		this.StepInfoMap.put(id, node);
	}
	
	/**TODO:����������ϵ
	 * @param String id
	 * �Լ��ڵ�ID
	 * @param String fatherId
	 * ���ڵ�ID
	 * @param String sonId
	 * �ӽڵ�ID
	 * @return 
	 * ����Ҳ������ڵ�����ӽڵ㣬�ͷ�������ʧ��*/
	public boolean setNodeRelation(String id, String fatherId, String sonId){
		try{
			StepInfo node = this.StepInfoMap.get(id);
			StepInfo father = this.StepInfoMap.get(fatherId);
			StepInfo son = this.StepInfoMap.get(sonId);
			if(node == null){
				System.out.println("Warning:�ڵ�"+id+"�����ڡ�");
				//û�нڵ�
				return true;
			}
			if(father != null){
				node.addFahterNode(father);
			}
			if(son != null){
				node.addSonNode(son);
			}
			return true;
		}catch(Exception e){
			Log.getInstance().exception(e);
			return false;
		}
	}
	
	/**TODO:���ø��ӹ�ϵ*/
	public boolean setFatherRelation(String id, String fatherId, String idStepno){
		try{
			StepInfo node   = this.StepInfoMap.get(id);
			StepInfo father = this.StepInfoMap.get(fatherId);
			Info info;
			String array[];
			if(node == null){
				System.out.println("Warning:�ӽڵ�"+id+"�����ڡ�");
				node = new StepInfo(10,10,200,60);
				info = new Info();
				array = id.split("_");
				info.setApp(array[0]);
				info.setCycle(array[1]);
				info.setDataSource(array[2]);
				info.setProc("");
				info.setProcDesc("�����ڵĲ���");
				info.setProcType("NOT_FOUND");
				info.setStepno(idStepno);
				node.setStepInfo(info);
				node.setHeight(1);
				this.StepInfoMap.put(id, node);
				return true;
			}
			if("NOT_FOUND".equalsIgnoreCase(node.getInfo().getProcType())){
				return true;
			}
			
			if(father != null && !"NOT_FOUND".equalsIgnoreCase(father.getInfo().getProcType())){
				node.addFahterNode(father);
			}
			return true;
		}catch(Exception e){
			Log.getInstance().exception(e);
			return false;
		}
	}

	/**TODO:���ø��ӹ�ϵ*/
	public boolean setSonRelation(String id, String sonId){
		try{
			StepInfo node   = this.StepInfoMap.get(id);
			StepInfo son = this.StepInfoMap.get(sonId);
			if(node == null){
				System.out.println("Warning:�ڵ�"+id+"�����ڡ�");
				//û�нڵ�
				return true;
			}
			if(node != null && !"NOT_FOUND".equalsIgnoreCase(son.getInfo().getProcType()) && !"NOT_FOUND".equalsIgnoreCase(node.getInfo().getProcType())){
				node.addSonNode(son);
			}
			return true;
		}catch(Exception e){
			Log.getInstance().exception(e);
			return false;
		}
	}
	
	/**TODO:�жϽڵ��Ƿ����*/
	public boolean exists(StepInfo node){
		StepInfo check = this.StepInfoMap.get(node.getId());
		return check != null;
	}
	/**TODO:�жϽڵ��Ƿ����*/
	public boolean exists(String id){
		return this.StepInfoMap.get(id) != null;
	}
	
	/**TODO:��ȡ�ڵ�*/
	public StepInfo getNode(String nodeId){
		return this.StepInfoMap.get(nodeId);
	}
	
	/**TODO:��ȡǰ�̽ڵ�*/
	public String getIdPrefix(){
		return root.getInfo().getIdPrefix();
	}

	/**TODO:��һ���ڵ�Ϊ��Ҷ���������ĸ��ڵ㣬�µ���*/
	public void setFatherTree(StepInfoTree tree,
			                  StepInfo node,
			                  StepInfo sonNode){
		//���ȸ��Լ�Ū��ȥ
		StepInfo tmpNode = tree.getNode(node.getId());
		if(tmpNode == null){
			tmpNode = new StepInfo();
			tmpNode.setStepInfo(node.getInfo());
			if(!node.hasFather()){
				tree.setRoot(tmpNode);
			}else{
				tree.setNode(tmpNode);
			}
		}
		String id = tmpNode.getId();
		String sonId = sonNode == null ? null : sonNode.getId();
		//�Ѿ��Ǹ��ڵ㣬�������ϵ�Ͳ��ù���
		if(!node.hasFather()){
			if(sonId==null){
				return;
			}
			tree.setNodeRelation(tmpNode.getId(), null, sonId);
			return;
		}
		//�и��ڵ�
		ArrayList<StepInfo> fList = node.getFList();
		StepInfo curNode;
		for(int i=0; i<fList.size(); i++){
			curNode = fList.get(i);
			tree.setNodeRelation(id, curNode.getId(), sonId);
			tree.setFatherTree(tree, curNode, tmpNode);
		}
	}

	/**TODO:��һ���ڵ�Ϊ���ڵ㣬���������ӽڵ㣬�µ���*/
	public void setSonTree(StepInfoTree tree,
			               StepInfo node,
			               StepInfo fatherNode){
		//���ȸ��Լ�Ū��ȥ
		StepInfo tmpNode = tree.getNode(node.getId());
		if(tmpNode == null){
			tmpNode = new StepInfo();
			tmpNode.setStepInfo(node.getInfo());
			if(!node.hasFather()){
				tree.setRoot(tmpNode);
			}else{
				tree.setNode(tmpNode);
			}
		}
		String id  = tmpNode.getId();
		String fid = fatherNode == null ? null : fatherNode.getId();
		//�Ѿ�����Ҷ���������ϵ�Ͳ��ù���
		if(!node.hasSon()){
			if(fid==null){
				return;
			}
			tree.setNodeRelation(tmpNode.getId(), fid, null);
			return;
		}
		//���ӽڵ�
		ArrayList<StepInfo> sList = node.getSList();
		StepInfo curNode;
		for(int i=0; i<sList.size(); i++){
			curNode = sList.get(i);
			tree.setNodeRelation(id, fid, curNode.getId());
			tree.setSonTree(tree, curNode, tmpNode);
		}
	}
	
	/**TODO:��ȡtree��size���ж��ٸ��ڵ�*/
	public int size(){
		return this.StepInfoMap.size();
	}

	/**TODO: ��ȡ������ȣ�������ж��������ǿ�������0
	 * @param boolean reCal
	 * ���¼����־�������false�����Ѿ����������ֱ�ӷ��ء��������¼���*/
	public int getTreeDepth(boolean reCal){
		if(this.root == null){
			return 0;
		}
		if(!reCal && this.treeHeight != null){
			return this.treeHeight;
		}
		this.treeHeight =  this.getNodeDepth(this.root);
		return treeHeight;
	}
	
	/**TODO:��ȡ��ǰ�ڵ�Ŀ��*/
	public int getTreeWidth(boolean reCal){
		if(this.root == null){
			return 0;
		}
		if(!this.root.hasSon()){
			return 1;
		}
		if(!reCal && this.treeWidth != null){
			return this.treeWidth;
		}
		//������һ���㼶����ȴ�
		int maxWidth = 1;
		Integer width;
		int depth;
		String tmpStr;
		HashMap<String, Integer> depthMap = new HashMap<String, Integer>();
		
		Iterator<Entry<String, StepInfo>> it;
		Entry<String, StepInfo> entry;
		Set<Entry<String, StepInfo>> set = this.StepInfoMap.entrySet();
		StepInfo tmp;
		for(it = set.iterator(); it.hasNext(); ){
			entry = it.next();
			tmp = entry.getValue();
			depth = tmp.getHeight();
			tmpStr = String.valueOf(depth);
			width = depthMap.get(tmpStr);
			if(width == null){
				depthMap.put(tmpStr, new Integer(1));
			}else{
				width ++;
				depthMap.put(tmpStr, width);
				if(width > maxWidth){
					maxWidth = width;
				}
			}
		}
		this.treeWidth = maxWidth;
		return this.treeWidth;
	}
	
	/**TODO:�������нڵ�����*/
	public void calNodesHeight(){
		Iterator<Entry<String, StepInfo>> it;
		Entry<String, StepInfo> entry;
		Set<Entry<String, StepInfo>> set = this.StepInfoMap.entrySet();
		StepInfo tmp;
		for(it = set.iterator(); it.hasNext(); ){
			entry = it.next();
			tmp = entry.getValue();
			if("NOT_FOUND".equals(tmp.getInfo().getProcType())){
				tmp.setHeight(1);
				continue;
			}
			tmp.setHeight(tmp.calHeight(true));
		}
	}
	
	/**TODO:��ȡ��ǰ�ڵ�����*/
	private int getNodeDepth(StepInfo node){
		if(node == null){
			return 0;
		}
		if(!node.hasSon()){
			return 1;
		}else{
			int maxDepth = 0;
			int dep;
			StepInfo tmp;
			ArrayList<StepInfo> sList = node.getSList();
			for(int i=0; i<sList.size(); i++){
				tmp = sList.get(i);
				dep = this.getNodeDepth(tmp);
				if(dep > maxDepth){
					maxDepth = dep;
				}
			}
			return maxDepth + 1;
		}
	}
	
	/**TODO:�������Ŀ�ȡ����*/
	public void resetTree(){
		this.treeHeight = null;
		this.treeWidth  = null;
	}
	
	/**TODO:���������*/
	public void clear(){
		this.StepInfoMap.clear();
		this.root = null;
		this.treeHeight = null;
		this.treeWidth = null;
	}
	
	/**TODO:���ݴ���Ĳ���ų��Ҳ��貢����*/
	public StepInfo findStepInfo(String stepno){
		if(this.root == null || this.StepInfoMap == null){
			return null;
		}
		if(stepno == null){
			return null;
		}
		String id = root.getInfo().getApp()+"_"+root.getInfo().getCycle()+"_"+root.getInfo().getDataSource()+"_"+stepno;
		//����
		StepInfo info;
		info =  this.StepInfoMap.get(id);
		if(info != null){
			return info;
		}
		Entry<String, StepInfo> entry;
		Info step;
		String tmp;
		String word = stepno.toUpperCase();
		//���Ϊnull������ִ�к�������
    	for(Iterator<Entry<String,StepInfo>> it = this.StepInfoMap.entrySet().iterator(); it.hasNext(); ){
			entry = it.next();
			step  = entry.getValue().getInfo();
			tmp = step.getProc();
			if(tmp!=null && tmp.toUpperCase().indexOf(word) != -1){
				return entry.getValue();
			}
			tmp = step.getProcDesc();
			if( tmp!=null && tmp.toUpperCase().indexOf(word) != -1){
				return entry.getValue();
			}
			tmp = step.getParameter();
			if(tmp!=null && tmp.toUpperCase().indexOf(word) != -1){
				return entry.getValue();
			}
		}
		return null;
	}
	
	public void remove(String id){
		this.StepInfoMap.remove(id);
	}
	
	//ͼ��ϣ��
	private HashMap<String,StepInfo> StepInfoMap;
	//���ڵ�
	private StepInfo root;
	//���������
	private Integer treeWidth = null;
	//����������
	private Integer treeHeight = null;
}
