/*
*  Ocelet spatial modelling language.   www.ocelet.org
*  Copyright Cirad 2010-2016
*
*  This software is a domain specific programming language dedicated to writing
*  spatially explicit models and performing spatial dynamics simulations.
*
*  This software is governed by the CeCILL license under French law and
*  abiding by the rules of distribution of free software.  You can  use,
*  modify and/ or redistribute the software under the terms of the CeCILL
*  license as circulated by CEA, CNRS and INRIA at the following URL
*  "http://www.cecill.info".
*  As a counterpart to the access to the source code and  rights to copy,
*  modify and redistribute granted by the license, users are provided only
*  with a limited warranty  and the software's author,  the holder of the
*  economic rights,  and the successive licensors  have only limited
*  liability.
*  The fact that you are presently reading this means that you have had
*  knowledge of the CeCILL license and that you accept its terms.
*/
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

       
    
    public void add(int x, int y, int band, Double value){
        get(x, y).add(band, value);       
    }   

    public void clearAggregMap(){    
        aggregMap.clear();
    }

    public void addOperator(CellAggregOperator operator, String name){    
        aggregMap.put(name, operator);
    }

   

  
}
