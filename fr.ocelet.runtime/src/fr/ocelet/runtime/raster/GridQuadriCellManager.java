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
           
                CellAggregOperator cao = aggregMap.get(name);
                if(aggregMap.containsKey(name)){
                
                    if(!cv.getValues(name).isEmpty()){
                    	if(cao.preval() == false){
	                		 grid.setCellValue(name, i, y, cao.apply(cv.getValues(name), null));	                    
  
	                	  }else{
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
