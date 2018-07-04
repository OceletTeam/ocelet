/*
*  Ocelet spatial modelling language.   www.ocelet.org
*  Copyright Cirad 2010-2018
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

/**
 * @author Mathieu Castets - Initial contribution
 */
public class GridHexaCellManager extends GridCellManager {

	

	  private CellValues firstLine[];
	  private CellValues midLine[];
	    private CellValues nextLine[];
	  
	  
	
  public GridHexaCellManager(Grid grid){
  
  	super(grid);
   
     
  }
  @Override
  public void reset(){
  
	  validateAll();
     
      y = 0;
      for(int i = 0; i < grid.getWidth(); i++){
      	
    	  for(int b = 0; b < properties.size(); b++){
      		midLine[i].clear(b);
      		nextLine[i].clear(b);
      		firstLine[i].clear(b);
      	}
      }
   

  }

  @Override
  public void init(){
  
      firstLine = new CellValues[grid.getWidth()];
      midLine = new CellValues[grid.getWidth()];
      nextLine = new CellValues[grid.getWidth()];
      for(int i = 0; i < grid.getWidth(); i++){
      
    	  midLine[i] = new CellValues();
          firstLine[i] = new CellValues();
          nextLine[i] = new CellValues();
          for(int b = 0; b < properties.size(); b++){
          	nextLine[i].set(b);
          	midLine[i].set(b);
          	firstLine[i].set(b);
          }
         

      }

  }

  @Override
  public void increment(){
	  
	  //System.out.println(" increment "+y);
	  if(y > 0)
      validate();
	  
      CellValues temp[] = firstLine;
      firstLine = midLine;
      midLine = nextLine;
      nextLine = temp;
     /* for(int i = 0; i < grid.getWidth(); i++){
      
      	for(String name : properties){
      		nextLine[i].clear(name);
      	}       
      }   */
      y++;
  }
  
  

  @Override
  public CellValues get(int x, int y){
	  
      if(y + 1== this.y){
    	//  System.out.println("FIRST ");
          return firstLine[x];
      }
      if(y == this.y){
    	//  System.out.println("MID ");
          return midLine[x];
      }
      if(y - 1 == this.y){
    	//  System.out.println("NEXT ");
          return nextLine[x];
      }
      
          return null;
  }


  @Override
  public void add(int x, int y, int band, Double value){
	  
	   /* if(y + 1== this.y){
	      }
	      if(y == this.y){
	      }
	      if(y - 1 == this.y){
	         
	      }*/
      get(x, y).add(band, value);
     
  }

  @Override
  public CellValues[] getValues(){
  
      return nextLine;
  }


 public void validateAll(){
	 
	 for(int i = 0; i < firstLine.length; i++){
	      
	        //  CellValues cv = firstLine[i];
	          CellValues cv2 = firstLine[i];
	          CellValues cv3 = midLine[i];
	          
	          for(int b = 0; b < properties.size(); b ++){
	              String name = properties.get(b);
	              
	              if(aggregMap.containsKey(name)){
	              
	            	   CellAggregOperator cao = aggregMap.get(name);
	              

	                  if(!cv2.getValues(b).isEmpty()){	  
	                 if(cao.preval() == false){
	                		 grid.setCellValue(b, i, y - 1, cao.apply(cv2.getValues(b), grid.getDoubleValue(b, i, y - 1)));	                    

	                	  }else{
	                		  
	                		  cv2.getValues(b).add(grid.getDoubleValue(b, i, y - 1));
	                		  grid.setCellValue(b, i, y - 1, cao.apply(cv2.getValues(b), grid.getDoubleValue(b, i, y - 1)));	                    

	                	  }
	                	  	                	  
	                                   
	                  }

	                  if(!cv3.getValues(b).isEmpty()){
	                
	                	  if(cao.preval() == false){
	                		 grid.setCellValue(b, i, y, cao.apply(cv3.getValues(b), grid.getDoubleValue(b, i, y)));	                    

	                	  }else{
	                		  cv2.getValues(b).add(grid.getDoubleValue(b, i, y));
	                		  grid.setCellValue(b, i, y, cao.apply(cv3.getValues(b), grid.getDoubleValue(b, i, y)));	                    

	                	  }
	                            
	                  }
	                  
	              } else {
	            	
	            	  if(!cv2.getValues(b).isEmpty()){
		                  grid.setCellValue(b, i, y - 1, cv2.getValues(b).get((int)(Math.random() * cv2.getValues(b).size())));
		                  
		              }
	            	  if(!cv3.getValues(b).isEmpty()){
		                  grid.setCellValue(b, i, y , cv3.getValues(b).get((int)(Math.random() * cv3.getValues(b).size())));
		                  
	              }
	          }
	              cv2.clear(b);
	              cv3.clear(b);
       }
	 }
	 
//	 System.out.println("VALIDATE ALL "+this.y);
	  /* for(int i = 0; i < firstLine.length; i++){
		      
	        //  CellValues cv = firstLine[i];
	          CellValues cv2 = firstLine[i];
	          CellValues cv3 = midLine[i];
	          
	          for(String name : properties){
	              
	              if(aggregMap.containsKey(name)){
	              
	            	   CellAggregOperator cao = aggregMap.get(name);
	              

	                  if(!cv2.getValues(name).isEmpty()){	  
	                 if(cao.preval() == false){
	                		 grid.setCellValue(name, i, y - 1, cao.apply(cv2.getValues(name), grid.getDoubleValue(name, i, y - 1)));	                    
  
	                	  }else{
	                		  
	                		  cv2.getValues(name).add(grid.getDoubleValue(name, i, y - 1));
	                		  grid.setCellValue(name, i, y - 1, cao.apply(cv2.getValues(name), grid.getDoubleValue(name, i, y - 1)));	                    

	                	  }
	                	  	                	  
	                                   
	                  }

	                  if(!cv3.getValues(name).isEmpty()){
	                
	                	  if(cao.preval() == false){
	                		 grid.setCellValue(name, i, y, cao.apply(cv3.getValues(name), grid.getDoubleValue(name, i, y)));	                    
  
	                	  }else{
	                		  cv2.getValues(name).add(grid.getDoubleValue(name, i, y));
	                		  grid.setCellValue(name, i, y, cao.apply(cv3.getValues(name), grid.getDoubleValue(name, i, y)));	                    

	                	  }
	                            
	                  }
	                  
	              } else {
	            	
	            	  if(!cv2.getValues(name).isEmpty()){
		                  grid.setCellValue(name, i, y - 1, cv2.getValues(name).get((int)(Math.random() * cv2.getValues(name).size())));
		                  
		              }
	            	  if(!cv3.getValues(name).isEmpty()){
		                  grid.setCellValue(name, i, y , cv3.getValues(name).get((int)(Math.random() * cv3.getValues(name).size())));
		                  
	              }
	          }
	              cv2.clear(name);
	              cv3.clear(name);
          }
	 }*/
 }
  
@Override
  public void validate(){
  
	
	for(int i = 0; i < firstLine.length; i++){
	      
        CellValues cv = firstLine[i];
        
        for(int b = 0; b < properties.size(); b++){
            
        	String name = properties.get(b);
            if(aggregMap.containsKey(name)){
          	 
          	  CellAggregOperator cao = aggregMap.get(name);
                if(!cv.getValues(b).isEmpty()){
              	  
               Double d; 
               List<Double> values = cv.getValues(b);
               Double value = grid.getDoubleValue(b, i, y - 1);
             	 if(cao.preval() == false){
             		 
             		d = cao.apply(values, value); 
             	 }else{
             		 values.add(value);
             		 d = cao.apply(values, value); 

             	 }
              	 //System.out.println(" CV1 "+cv);
                    grid.setCellValue(b, i, y - 1, d);
                   
                }
            } else if(!cv.getValues(b).isEmpty()){
            
                grid.setCellValue(b, i, y - 1, cv.getValues(b).get((int)(Math.random() * cv.getValues(b).size())));
            }
            cv.clear(b);
        }
        

    }
	/*
      for(int i = 0; i < firstLine.length; i++){
      
          CellValues cv = firstLine[i];
          
          for(String name : properties){
              
              if(aggregMap.containsKey(name)){
            	 
            	  CellAggregOperator cao = aggregMap.get(name);
                  if(!cv.getValues(name).isEmpty()){
                	  
                 Double d; 
                 List<Double> values = cv.getValues(name);
                 Double value = grid.getDoubleValue(name, i, y - 1);
               	 if(cao.preval() == false){
               		 
               		d = cao.apply(values, value); 
               	 }else{
               		 values.add(value);
               		 d = cao.apply(values, value); 

               	 }
                	 //System.out.println(" CV1 "+cv);
                      grid.setCellValue(name, i, y - 1, d);
                     
                  }
              } else if(!cv.getValues(name).isEmpty()){
              
                  grid.setCellValue(name, i, y - 1, cv.getValues(name).get((int)(Math.random() * cv.getValues(name).size())));
              }
              cv.clear(name);
          }
          

      }

      */
  }

}
