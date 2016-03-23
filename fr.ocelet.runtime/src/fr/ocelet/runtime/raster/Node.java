package fr.ocelet.runtime.raster;

import java.util.ArrayList;

import fr.ocelet.runtime.entity.AbstractEntity;
import fr.ocelet.runtime.relation.OcltRole;

/*public class Node<R2 extends OcltRole> {

	private ArrayList<IX<R2>> nodes;
	private int pos = 0;
	public Node(){
		nodes = new ArrayList<IX<R2>>();
	}
	
	public void add(int x, int y, R2 r){
		boolean test = false;
		for(IX<R2> ix : nodes){
			
			if(ix.x == x){
				ix.add(y, r);
				test = true;
			}
		}
		if(!test){
			IX<R2> ix = new IX<R2>(x);
			ix.add(y,r);
			nodes.add(ix);
		}
	}
	
	public boolean hasNext(){
		if(pos == nodes.size() - 1){
			return false;
		}
		return true;
	}
	
	public  void next(){
		
		
			
		pos++;
		
	
		
	}
	
	public IX<R2> get(){
		return nodes.get(pos);
	}
	
	public void reset(){
		
		pos = 0;
	}
	
	public int size(){
		return nodes.size();
	}
}*/



public class Node {

	//private ArrayList<IX<R2>> nodes;
	
	private IX[] nodes = null;
	private int pos = 0;
	public Node(){
		//nodes = new ArrayList<IX<R2>>();
	}
	
	public void add(Integer x, Integer y, AbstractEntity r){
		boolean test = false;
		
		if(nodes == null){
			nodes = new IX[1];
			IX ix = new IX(x);
			ix.add(y,r);
			nodes[0] = ix;
		}else{
		
		
			for(IX ix : nodes){
			
				if(ix.x == x){
					ix.add(y, r);
					test = true;
				}
			}
			if(!test){
				IX ix = new IX(x);
				ix.add(y,r);
				IX[] temp = new IX[nodes.length + 1];
				for(int i = 0; i < nodes.length; i ++){
					temp[i] = nodes[i];
				}
					temp[nodes.length] = ix;
					nodes = temp;
			}
		}
	}
	
	public boolean hasNext(){
		if(pos == nodes.length - 1){
			return false;
		}
		return true;
	}
	
	public  void next(){
		
		
			
		pos++;
		
	
		
	}
	
	public IX get(){
		return nodes[pos];
	}
	
	public void reset(){
		
		pos = 0;
	}
	
	public int size(){
		return nodes.length;
	}
}
