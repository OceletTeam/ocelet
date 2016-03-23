package fr.ocelet.runtime.relation;

import java.util.ArrayList;



public class EntityContainer {
	
	private ArrayList<OcltRole> list = new ArrayList<OcltRole>();
	
	public EntityContainer(OcltRole role){
		list.add(role);
	}
	
	
	public OcltRole get(int index){
		return list.get(index);
	}
	public void add(OcltRole role){
		list.add(role);
	}
	
	public ArrayList<OcltRole> get(){
		return list;
	}
	public boolean contains(OcltRole r2){
		return list.contains(r2);
	}
	
	public boolean containOnly(OcltRole r2){
		
		if(list.size() == 1){
			if(list.contains(r2))
				return true;
		}
		return false;
		
	}
	
	public boolean containList(EntityContainer ec, OcltRole r2){
		
		boolean contain = true;
		for(OcltRole role : list){
			
			
			
			if(!ec.contains(role)){
				contain = false;
			}
			if(!ec.contains(r2)){
			contain = false;
			}
			
		}
		return contain;
	}
	
	public EntityContainer newEC(EntityContainer ec, OcltRole r2){
		
		EntityContainer e = new EntityContainer(r2);
		for(OcltRole role : ec.get()){
			e.add(role);
		}
		return e;
	}
	
	public int size(){
		return list.size();
	}
}
