package fr.ocelet.runtime.raster;

import java.util.ArrayList;

import fr.ocelet.runtime.relation.CellValues;

public class MultiResolutionManager {
	
    private CellValues[] globalLine;
    private ArrayList<String> properties;
    private int width;
    private int start;
    private int end;
    
    public MultiResolutionManager(int width, ArrayList<String> properties){
    	this.width = width + 1;
    	this.properties = properties;
    	init();
    }
    
    public MultiResolutionManager(int start, int end, ArrayList<String> properties){
    	this.start = start;
    	this.end = end + 1;
    	this.width = end - start;
    	this.properties = properties;
    	init();
    }
    
    public void add(int x, String name, Double value){
        get(x).add(name, value);       
    }
    
    public CellValues get(int x){
    	return globalLine[x];
    }
    public void init(){
    	globalLine = new CellValues[width];
    	for(int i = 0; i < width; i ++){
    		globalLine[i] = new CellValues();
    		for(String name : properties){
    			globalLine[i].set(name);
    		}
    	}
    }
    public void clear(String name){
    	for(int i = 0; i < width; i ++){
    		globalLine[i].clear(name);
    	}
    }
    public void clear(){
    	for(int i = 0; i < width; i ++){
    		for(String name : properties){
    			globalLine[i].clear(name);
    		}
    	}
    }
   public ArrayList<String> getProperties(){
	   return properties;
   }
}
