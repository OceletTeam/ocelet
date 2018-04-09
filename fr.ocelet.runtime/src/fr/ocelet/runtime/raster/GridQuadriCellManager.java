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

public class GridQuadriCellManager extends GridCellManager{


	  private CellValues firstLine[];
	    private CellValues nextLine[];
	  
	  
	
    public GridQuadriCellManager(Grid grid){
    
    	super(grid);
     
       
    }

    @Override
    public void reset(){
       validate();
        y = 0;
        for(int i = 0; i < grid.getWidth(); i++){
        	
        	for(String name : properties){
        		nextLine[i].clear(name);
        		firstLine[i].clear(name);
        	}
        }
     

    }

    public void init(){
    
        firstLine = new CellValues[grid.getWidth()];
        nextLine = new CellValues[grid.getWidth()];
        for(int i = 0; i < grid.getWidth(); i++){
        
            firstLine[i] = new CellValues();
            nextLine[i] = new CellValues();
            
            for(String name : properties){
            	nextLine[i].set(name);
            	firstLine[i].set(name);
            }
           

        }

    }

    public void increment(){
        validate();
        CellValues temp[] = firstLine;
        firstLine = nextLine;
        nextLine = temp;
        for(int i = 0; i < grid.getWidth(); i++){
        
        	for(String name : properties){
        		nextLine[i].clear(name);
        	}
           

        }

        y++;
    }

    public CellValues get(int x, int y){
        if(y == this.y){
            return firstLine[x];
        }
        if(y - 1 == this.y){
            return nextLine[x];
        }
        
            return null;
    }

   

  

    public void add(int x, int y, String name, Double value){

        get(x, y).add(name, value);
       
    }

    public CellValues[] getValues(){
    
        return nextLine;
    }

     

    public void validate(){
        for(int i = 0; i < firstLine.length; i++){
        
            CellValues cv = firstLine[i];
            
            for(String name : properties){
           
                List<Double> values = cv.getValues(name);
                
                if(aggregMap.containsKey(name)){
                
                    if(!values.isEmpty()){
                    	
                    	Double d;
                    	CellAggregOperator cao = aggregMap.get(name);
                    	if(cao.preval() == false){
                    		d = cao.apply(values, null);
                    	}else{
                    		values.add(grid.getDoubleValue(name, i, y));
                    		d = cao.apply(values, grid.getDoubleValue(name, i, y));
                    	}
                    	
                        grid.setCellValue(name, i, y, d);
                    	
                    }              
                } else if(!cv.getValues(name).isEmpty()){
                
                    grid.setCellValue(name, i, y, values.get((int)(Math.random() * values.size())));
                }
            }

        }

    }
    
    public void validateAll(){
    	
        for(int i = 0; i < firstLine.length; i++){
            
            CellValues cv = firstLine[i];
            
            for(String name : properties){
            	 List<Double> values = cv.getValues(name);
                CellAggregOperator cao = aggregMap.get(name);
                if(aggregMap.containsKey(name)){
                	
                    if(!cv.getValues(name).isEmpty()){
                    	if(cao.preval() == false){
	                		 grid.setCellValue(name, i, y, cao.apply(values, null));	                    
  
	                	  }else{
	                		  values.add(grid.getDoubleValue(name, i, y));
	                		  grid.setCellValue(name, i, y , cao.apply(cv.getValues(name), grid.getDoubleValue(name, i, y)));	                    

	                	  }
                    }
                } else if(!cv.getValues(name).isEmpty()){
                
                    grid.setCellValue(name, i, y, cv.getValues(name).get((int)(Math.random() * cv.getValues(name).size())));
                }
            }

        }
    	
    }

  
}
