package fr.ocelet.runtime.raster;

import fr.ocelet.runtime.ocltypes.List;
import fr.ocelet.runtime.relation.CellValues;

public class GridHexaCellManager extends GridCellManager {

	

	  private CellValues firstLine[];
	  private CellValues midLine[];
	    private CellValues nextLine[];
	  
	  
	
  public GridHexaCellManager(Grid grid){
  
  	super(grid);
   
     
  }

  public void reset(){
  
	  validateAll();
     
      y = 0;
      for(int i = 0; i < grid.getWidth(); i++){
      	
      	for(String name : properties){
      		midLine[i].clear(name);
      		nextLine[i].clear(name);
      		firstLine[i].clear(name);
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
          for(String name : properties){
          	nextLine[i].set(name);
          	midLine[i].set(name);
          	firstLine[i].set(name);
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
      for(int i = 0; i < grid.getWidth(); i++){
      
      	for(String name : properties){
      		nextLine[i].clear(name);
      	}       
      }     
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



  public void add(int x, int y, String name, Double value){
	  
	    if(y + 1== this.y){
	    	//  System.out.println("FIRST ");
	    	// System.out.println("ADD FIRST "+x+" "+y+" "+value+" "+this.y);
	      }
	      if(y == this.y){
	    	//  System.out.println("MID ");
	    	 // System.out.println("ADD MID "+x+" "+y+" "+value+" "+this.y);
	      }
	      if(y - 1 == this.y){
	    	 // System.out.println("ADD NEXT "+x+" "+y+" "+value+" "+this.y);
	    	//  System.out.println("NEXT ");
	         
	      }
	// System.out.println("ADD "+x+" "+y+" "+value+" "+this.y);
	  //System.out.println(get(x, y).getValues(name));
      get(x, y).add(name, value);
   //   System.out.println(get(x, y).getValues(name));
     
  }

  @Override
  public CellValues[] getValues(){
  
      return nextLine;
  }


 public void validateAll(){
	 
//	 System.out.println("VALIDATE ALL "+this.y);
	   for(int i = 0; i < firstLine.length; i++){
		      
	        //  CellValues cv = firstLine[i];
	          CellValues cv2 = firstLine[i];
	          CellValues cv3 = midLine[i];
	          
	          for(String name : properties){
	              
	              if(aggregMap.containsKey(name)){
	              
	            	   CellAggregOperator cao = aggregMap.get(name);
	               /*   if(!cv.getValues(name).isEmpty()){	                	 
	              
	                      grid.setCellValue(name, i, y - 1, aggregMap.get(name).apply(cv.getValues(name), 0.0));	                    
	                  }*/

	                  if(!cv2.getValues(name).isEmpty()){	  
	                /*	  System.out.println(i+" "+(y - 1));
	              System.out.println(" CV2 "+cv2);*/
	                	  
	                	  
	                	  
	                	  if(cao.preval() == false){
	                		 grid.setCellValue(name, i, y - 1, cao.apply(cv2.getValues(name), null));	                    
  
	                	  }else{
	                		  grid.setCellValue(name, i, y - 1, cao.apply(cv2.getValues(name), grid.getDoubleValue(name, i, y - 1)));	                    

	                	  }
	                	  
	                	  
	                    //  grid.setCellValue(name, i, y - 1, aggregMap.get(name).apply(cv2.getValues(name), 0.0));	                    
	                  }

	                  if(!cv3.getValues(name).isEmpty()){
	                	/*  System.out.println(i+" "+(y ));
	                	  System.out.println(" CV2 "+cv3);*/
	                	  if(cao.preval() == false){
	                		 grid.setCellValue(name, i, y, cao.apply(cv3.getValues(name), null));	                    
  
	                	  }else{
	                		  grid.setCellValue(name, i, y, cao.apply(cv3.getValues(name), grid.getDoubleValue(name, i, y)));	                    

	                	  }
	                     // grid.setCellValue(name, i, y, aggregMap.get(name).apply(cv3.getValues(name), 0.0));	                    
	                  }
	                  
	              } else {
	            	 /* if(!cv.getValues(name).isEmpty()){
	                  grid.setCellValue(name, i, y- 1, (Double)cv.getValues(name).get((int)(Math.random() * (double)cv.getValues(name).size())));
	                  
	              }*/
	            	  if(!cv2.getValues(name).isEmpty()){
		                  grid.setCellValue(name, i, y - 1, cv2.getValues(name).get((int)(Math.random() * cv2.getValues(name).size())));
		                  
		              }
	            	  if(!cv3.getValues(name).isEmpty()){
		                  grid.setCellValue(name, i, y , cv3.getValues(name).get((int)(Math.random() * cv3.getValues(name).size())));
		                  
		              }
	              
	          }
	          }

	      }
 }
  
@Override
  public void validate(){
  
	//System.out.println("VALIDATE "+(y-1));
      for(int i = 0; i < firstLine.length; i++){
      //System.out.println(i+" "+(y - 1));
          CellValues cv = firstLine[i];
          
          for(String name : properties){
              
              if(aggregMap.containsKey(name)){
            	 
            	  CellAggregOperator cao = aggregMap.get(name);
                  if(!cv.getValues(name).isEmpty()){
                	  
                 Double d; 
               	 if(cao.preval() == false){
               		 
               		d = cao.apply(cv.getValues(name), null); 
               	 }else{
               		 d = cao.apply(cv.getValues(name), grid.getDoubleValue(name, i, y - 1)); 

               	 }
                	 //System.out.println(" CV1 "+cv);
                      grid.setCellValue(name, i, y - 1, d);
                     
                  }
              } else if(!cv.getValues(name).isEmpty()){
              
                  grid.setCellValue(name, i, y - 1, cv.getValues(name).get((int)(Math.random() * cv.getValues(name).size())));
              }
          }

      }

  }

}
