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
	
	/**TODO:判断树是否为空*/
	public boolean isEmpty(){
		return this.root == null ? true : this.root.getSList().isEmpty();
	}
	
	public HashMap<String, StepInfo> getStepInfoMap() {
		return StepInfoMap;
	}

	public void setStepInfoMap(HashMap<String, StepInfo> StepInfoMap) {
		this.StepInfoMap = StepInfoMap;
	}

	/**TODO:获取根节点*/
	public StepInfo getRoot() {
		return root;
	}

	/**TODO:设置节点*/
	public void setRoot(StepInfo root) {
		this.root = root;
		this.setNode(root);
	}
	/**TODO:设置root节点*/
	public boolean setRoot(String id){
		StepInfo node = this.StepInfoMap.get(id);
		if(node == null){
			return false;
		}
		//if(this.r)
		this.root = node;
		return true;
	}

	/**TODO:设置节点信息*/
	public void setNode(StepInfo node){
		String id = node.getId();
		this.StepInfoMap.put(id, node);
	}
	
	/**TODO:设置依赖关系
	 * @param String id
	 * 自己节点ID
	 * @param String fatherId
	 * 父节点ID
	 * @param String sonId
	 * 子节点ID
	 * @return 
	 * 如果找不到父节点或者子节点，就返回设置失败*/
	public boolean setNodeRelation(String id, String fatherId, String sonId){
		try{
			StepInfo node = this.StepInfoMap.get(id);
			StepInfo father = this.StepInfoMap.get(fatherId);
			StepInfo son = this.StepInfoMap.get(sonId);
			if(node == null){
				System.out.println("Warning:节点"+id+"不存在。");
				//没有节点
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
	
	/**TODO:设置父子关系*/
	public boolean setFatherRelation(String id, String fatherId, String idStepno){
		try{
			StepInfo node   = this.StepInfoMap.get(id);
			StepInfo father = this.StepInfoMap.get(fatherId);
			Info info;
			String array[];
			if(node == null){
				System.out.println("Warning:子节点"+id+"不存在。");
				node = new StepInfo(10,10,200,60);
				info = new Info();
				array = id.split("_");
				info.setApp(array[0]);
				info.setCycle(array[1]);
				info.setDataSource(array[2]);
				info.setProc("");
				info.setProcDesc("不存在的步骤");
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

	/**TODO:设置父子关系*/
	public boolean setSonRelation(String id, String sonId){
		try{
			StepInfo node   = this.StepInfoMap.get(id);
			StepInfo son = this.StepInfoMap.get(sonId);
			if(node == null){
				System.out.println("Warning:节点"+id+"不存在。");
				//没有节点
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
	
	/**TODO:判断节点是否存在*/
	public boolean exists(StepInfo node){
		StepInfo check = this.StepInfoMap.get(node.getId());
		return check != null;
	}
	/**TODO:判断节点是否存在*/
	public boolean exists(String id){
		return this.StepInfoMap.get(id) != null;
	}
	
	/**TODO:获取节点*/
	public StepInfo getNode(String nodeId){
		return this.StepInfoMap.get(nodeId);
	}
	
	/**TODO:获取前继节点*/
	public String getIdPrefix(){
		return root.getInfo().getIdPrefix();
	}

	/**TODO:以一个节点为树叶，补充数的父节点，新的树*/
	public void setFatherTree(StepInfoTree tree,
			                  StepInfo node,
			                  StepInfo sonNode){
		//首先给自己弄进去
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
		//已经是父节点，设置完关系就不用管了
		if(!node.hasFather()){
			if(sonId==null){
				return;
			}
			tree.setNodeRelation(tmpNode.getId(), null, sonId);
			return;
		}
		//有父节点
		ArrayList<StepInfo> fList = node.getFList();
		StepInfo curNode;
		for(int i=0; i<fList.size(); i++){
			curNode = fList.get(i);
			tree.setNodeRelation(id, curNode.getId(), sonId);
			tree.setFatherTree(tree, curNode, tmpNode);
		}
	}

	/**TODO:以一个节点为跟节点，补充数的子节点，新的树*/
	public void setSonTree(StepInfoTree tree,
			               StepInfo node,
			               StepInfo fatherNode){
		//首先给自己弄进去
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
		//已经是树叶，设置完关系就不用管了
		if(!node.hasSon()){
			if(fid==null){
				return;
			}
			tree.setNodeRelation(tmpNode.getId(), fid, null);
			return;
		}
		//有子节点
		ArrayList<StepInfo> sList = node.getSList();
		StepInfo curNode;
		for(int i=0; i<sList.size(); i++){
			curNode = sList.get(i);
			tree.setNodeRelation(id, fid, curNode.getId());
			tree.setSonTree(tree, curNode, tmpNode);
		}
	}
	
	/**TODO:获取tree的size，有多少个节点*/
	public int size(){
		return this.StepInfoMap.size();
	}

	/**TODO: 获取树的深度（这棵树有多深），如果是空树就是0
	 * @param boolean reCal
	 * 重新计算标志，如果是false，且已经计算过，就直接返回。否则重新计算*/
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
	
	/**TODO:获取当前节点的宽度*/
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
		//看看哪一个层级的深度大
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
	
	/**TODO:计算所有节点的深度*/
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
	
	/**TODO:获取当前节点的深度*/
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
	
	/**TODO:重置树的宽度、深度*/
	public void resetTree(){
		this.treeHeight = null;
		this.treeWidth  = null;
	}
	
	/**TODO:清空整棵树*/
	public void clear(){
		this.StepInfoMap.clear();
		this.root = null;
		this.treeHeight = null;
		this.treeWidth = null;
	}
	
	/**TODO:根据传入的步骤号超找步骤并返回*/
	public StepInfo findStepInfo(String stepno){
		if(this.root == null || this.StepInfoMap == null){
			return null;
		}
		if(stepno == null){
			return null;
		}
		String id = root.getInfo().getApp()+"_"+root.getInfo().getCycle()+"_"+root.getInfo().getDataSource()+"_"+stepno;
		//返回
		StepInfo info;
		info =  this.StepInfoMap.get(id);
		if(info != null){
			return info;
		}
		Entry<String, StepInfo> entry;
		Info step;
		String tmp;
		String word = stepno.toUpperCase();
		//如果为null，继续执行后续内容
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
	
	//图哈希表
	private HashMap<String,StepInfo> StepInfoMap;
	//跟节点
	private StepInfo root;
	//树的最大宽度
	private Integer treeWidth = null;
	//树的最大深度
	private Integer treeHeight = null;
}
