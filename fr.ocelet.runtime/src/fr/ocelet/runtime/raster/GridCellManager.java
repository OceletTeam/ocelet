package fr.ocelet.runtime.raster;

import fr.ocelet.runtime.ocltypes.List;
import fr.ocelet.runtime.relation.CellValues;

import java.util.*;

// Referenced classes of package fr.ocelet.runtime.relation:
//            CellValues

public abstract class GridCellManager{
	
	    private int index;
	    protected int y;
	    protected Grid grid;
	    protected HashMap<String, Integer> props = new HashMap<String, Integer>();
	    protected ArrayList<String> properties = new ArrayList<String>();
	    protected HashMap<String, CellAggregOperator> aggregMap = new HashMap<String, CellAggregOperator>();
	    
    public GridCellManager(Grid grid){      
    	y = 0;
        this.grid = grid;
        properties = grid.getPropertiesName();  
        init();
    }

    public abstract void reset();

    public abstract void validate();

    public abstract void init();

    public abstract void increment();
    
    public void yIncrement(){
    	y++;
    }

    public abstract CellValues get(int x, int y);
    
    public abstract CellValues[] getValues();
    
    public void setProps(HashMap<String, Integer> initProp, HashMap<String, Integer> properties){
    
    	for(String name : initProp.keySet()){
    		props.put(name, initProp.get(name));
    	}
    	
    	for(String name : properties.keySet()){
    		props.put(name, properties.get(name));
    	}      
    }

    public void clearProperties(){    
        properties.clear();
    }

    public void add(int x, int y, String name, Double value){
        get(x, y).add(name, value);       
    }   

    public void clearAggregMap(){    
        aggregMap.clear();
    }

    public void addOperator(CellAggregOperator operator, String name){    
        aggregMap.put(name, operator);
    }

   

  
}
