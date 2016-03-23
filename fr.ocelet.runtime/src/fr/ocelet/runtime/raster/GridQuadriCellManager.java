// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   GridCellManager.java

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
           
                
                if(aggregMap.containsKey(name)){
                
                    if(!cv.getValues(name).isEmpty())
                        grid.setCellValue(name, i, y, aggregMap.get(name).apply(cv.getValues(name), 0.0));
                } else if(!cv.getValues(name).isEmpty()){
                
                    grid.setCellValue(name, i, y, (Double)cv.getValues(name).get((int)(Math.random() * (double)cv.getValues(name).size())));
                }
            }

        }

    }
    
    public void validateAll(){
    	
        for(int i = 0; i < firstLine.length; i++){
            
            CellValues cv = firstLine[i];
            
            for(String name : properties){
           
                
                if(aggregMap.containsKey(name)){
                
                    if(!cv.getValues(name).isEmpty())
                        grid.setCellValue(name, i, y, aggregMap.get(name).apply(cv.getValues(name), 0.0));
                } else if(!cv.getValues(name).isEmpty()){
                
                    grid.setCellValue(name, i, y, (Double)cv.getValues(name).get((int)(Math.random() * (double)cv.getValues(name).size())));
                }
            }

        }
    	
    }

  
}
