package fr.ocelet.runtime.raster;

import java.util.ArrayList;
import java.util.Iterator;

import fr.ocelet.runtime.entity.AbstractEntity;
import fr.ocelet.runtime.relation.OcltRole;

/*public class IY<R2 extends OcltRole> {
	
	Integer y;
	private ArrayList<R2> entities;
	private int pos = 0;
	
	public IY(Integer y){
		this.y = y;
		entities = new ArrayList<R2>();
		
	}
	public void addEntity(R2 r){
		if(!entities.contains(r)){
			entities.add(r);
		}
	}
	
	public void removeEntity(R2 r){
		
		entities.add(r);
	}
	
	public boolean contains(R2 r){
		
		return entities.contains(r);
	}
	
	public boolean hasNext(){
		if(pos == entities.size() - 1){
		
			return false;
		}
		return true;
	}
	
	public void next(){
		pos++;
	}
	
	public R2 get(){
		return entities.get(pos);
	}

	public void reset(){
		pos = 0;
	}
	
	public Integer getY(){
		return y;
	}
	
}*/

public class IY {
	
	Integer y;
	//private ArrayList<R2> entities;
	private AbstractEntity[] entities = null;
	private int pos = 0;
	
	public IY(Integer y){
		this.y = y;
		//entities = new ArrayList<R2>();
		//entities = new AbstractEntity[1];
	}
	public void addEntity(AbstractEntity r){
		
		boolean test = false;
		
		if(entities == null){
			entities = new AbstractEntity[1];
			entities[0] =r;
		}else{
			for(AbstractEntity a : entities){
				if(a.equals(r)){
					test = true;
				}
			}
		if(!test){
			AbstractEntity[] temp = new AbstractEntity[entities.length + 1];
			for(int i = 0; i< entities.length; i ++){
				temp[i] = entities[i];
			}
			temp[entities.length] = r;
			entities = temp;
		}
		}
	}
	
	
	
	public boolean contains(AbstractEntity a){
		
		boolean test = false;
		for(AbstractEntity e : entities){
			if(e.equals(a)){
				test = true;
			}
		}
		return test;
	}
	
	public boolean hasNext(){
		if(pos == entities.length - 1){
		
			return false;
		}
		return true;
	}
	
	public void next(){
		pos++;
	}
	
	public AbstractEntity get(){
		return entities[pos];
	}

	public void reset(){
		pos = 0;
	}
	
	public Integer getY(){
		return y;
	}
	
}
