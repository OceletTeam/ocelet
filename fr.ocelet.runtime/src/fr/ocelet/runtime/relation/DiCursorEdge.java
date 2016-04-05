package fr.ocelet.runtime.relation;

import com.vividsolutions.jts.geom.Coordinate;

import fr.ocelet.runtime.geom.ocltypes.Cell;
import fr.ocelet.runtime.ocltypes.KeyMap;
import fr.ocelet.runtime.ocltypes.List;
import fr.ocelet.runtime.raster.CellAggregOperator;
import fr.ocelet.runtime.raster.Grid;
import fr.ocelet.runtime.raster.GridManager;
import fr.ocelet.runtime.raster.MultiResolutionManager;

import java.util.*;

// Referenced classes of package fr.ocelet.runtime.relation:
//            OcltEdge, OcltRole, AggregOperator

public abstract class DiCursorEdge extends OcltEdge{

    private Grid globalGrid;
    private Grid grid;
    protected int x;
    protected int y;
    protected int x2;
    protected int y2;
    private int mode;
    private double r1XRes;
    private double r1YRes;
    private double xRes;
    private double yRes;
    private int rXRes;
    private int rYRes;
    private OcltRole r1;
    private OcltRole r2;
    private HashMap<String, CellAggregOperator> aggregMap = new HashMap<String, CellAggregOperator>();
    private boolean equalGrid = false;
    private GridCursor gridCursor;    
    private MultiResolutionManager mrm;
    
    
    public DiCursorEdge(Grid grid1, Grid grid2){
    
        x = 0;
        y = 0;
        x2 = - 1;
        y2 = - 1;       
        gridCursor = new GridCursor();
        
        if(grid1.getXRes() * grid1.getYRes() == grid2.getXRes() * grid2.getYRes()) {
            
            globalGrid = grid1;
            grid = grid2;
            equalGrid = true;
           
          /*  if(grid.getWidth() != globalGrid.getWidth())
                convertXAxis();
            if(grid.getHeight() != globalGrid.getHeight())
                convertYAxis();
            equalGrid = true;*/
        }else{
        if(grid1.getXRes() * grid1.getYRes() < grid2.getXRes() * grid2.getYRes()){        	
            globalGrid = grid2;
            grid = grid1;
            
                     
             
            
        } else {
        
            globalGrid = grid1;
            grid = grid2;
          
        }
        }
    }

   

  

    protected String getCellType(){
    	return null;
    }
    public void updateRoleInfo(){	
    		    	    	
    	   int numGrid = ((Cell)getRole(new Integer(0)).getSpatialType()).getNumGrid();
    	   
           if ( GridManager.getInstance().get(numGrid).equals(globalGrid)){
        	   r1 = getRole(new Integer(0));
        	   r2 = getRole(new Integer(1));
        	   
           }else{
        	   r1 = getRole(new Integer(1));
        	   r2 = getRole(new Integer(0));
           }
    }

    public void updateGrid(){
    
        if(equalGrid){
            globalGrid.setMode(1);
            grid.setMode(1);
        } else {
        
            globalGrid.setMode(4);
            grid.setMode(1);
        }
    }
    
    
    public abstract OcltRole getRole(Integer i);
    

   
    public OcltRole getRole(int i){
    	return null;
    }
    public boolean hasNext(){
    
        if(equalGrid){
            if(x == globalGrid.getWidth() - 1 && y == globalGrid.getHeight() - 1){
            
                x = 0;
                y = 0;
                x2 = 0;
                y2 = 0;
                
                return false;
            } else {            
                return true;
            }
        }
        if(x2 == grid.getWidth() - 1 && y2 == grid.getHeight() - 1){
        	x2 = - 1;
        	y2 = - 1;
        	globalSynchronisation();
        	
        	clearAggregMap();
        //	globalGrid.cleanOperator();
        	return false;
        }
        
       return true; 
      /*  if(x == globalGrid.getWidth() - 1 && y == globalGrid.getHeight() - 1){
        
            if(gridCursor.hasNext()){
            
                return true;
            } else{
            
                x = 0;
                y = 0;
                x2 = -1;
                y2 = -1;
              //  setCursor();
                return false;
            }
        } else{
        
            return true;
        }*/
    }

    /*public void next(){    
    	
    	
        if(equalGrid){
        
            if(x2 == -1 && y2 == -1){
            
                x2 = 0;
                y2 = 0;
                
            } else  if(x == globalGrid.getWidth() - 1){        
                x = 0;
                y++;
                x2 = 0;
                y2++;
                
            } else {
            
                x++;
                x2++;
            }
            
        } else {
        	
        	
        	if(x2 == grid.getWidth() - 1){
        		
        		x2 = 0;
        		y2 ++;
        		globalSynchronisation();
            	Coordinate c = grid.gridCoordinate(x2, y2);
            	int[] c2 = globalGrid.gridCoordinate(c.x, c.y);
            	x = c2[0];
            	y = c2[1];
        	}else{
        		x2 ++;
        		Coordinate c = grid.gridCoordinate(x2, y2);
            	int[] c2 = globalGrid.gridCoordinate(c.x, c.y);
        	}
        	
        	
        	 if(x2 == -1 && y2 == -1){
                 
                 x2 = 0;
                 y2 = 0;
                 setCursor();
             }else{
            	 
        	if(gridCursor.hasNext()){
        	// setCursor();
            gridCursor.next();
          //  globalGrid.cleanOperator();
            x2 = gridCursor.getX();// + gridCursor.minX;
            y2 = gridCursor.getY();// + gridCursor.minY;
        	} else{
        	
        	if(x == globalGrid.getWidth() - 1){
        
       
            globalSynchronisation();
           
            x = 0;
            y++;
            setCursor();
            //gridCursor.next();
            
            x2 = gridCursor.getX();// + gridCursor.minX;
            y2 = gridCursor.getY();// + gridCursor.minY;
        } else {
        	
            globalSynchronisation();
           
            x++;
            setCursor();
           // gridCursor.next();
            x2 = gridCursor.getX();// + gridCursor.minX;
            y2 = gridCursor.getY();// + gridCursor.minY;
           
        }
        	
        }
        }
    }
       
       
        updateBis();
        
       
    }*/
    
    public void next(){
    	
    	   if(equalGrid){
    	        
               if(x2 == -1 && y2 == -1){
               
                   x2 = 0;
                   y2 = 0;
                   
               } else  if(x == globalGrid.getWidth() - 1){        
                   x = 0;
                   y++;
                   x2 = 0;
                   y2++;
                   
               } else {
               
                   x++;
                   x2++;
               }
               
           }else{
        	   
        	    if(x2 == -1 && y2 == -1){
        	    	 globalGrid.initMrm();
        	    	mrm = globalGrid.getMrm();
        	    	x2 = 0;
        	    	y2 = 0;
               		x = 0;
               		y = 0;
        	    }else{
        	   
        	   if(x2 == grid.getWidth() - 1){
        		   
        		   	x2 = 0;
        		   	y2++;
        		   
        		   	Coordinate c = grid.gridCoordinate(x2, y2);
               		int[] c2 = globalGrid.gridCoordinate(c.x, c.y);
               		if(c2[1] != y){
               			globalSynchronisation();
               			//y = c2[1];
               			y = y + 1;
               		}
               		
               	
               		x = 0;
               		
        	   }else{
        		   x2++;
        		   Coordinate c = grid.gridCoordinate(x2, y2);
        		   int[] c2 = globalGrid.gridCoordinate(c.x, c.y);
        		   x = c2[0];
        	   }
        	    }
        	   
        	   
           }
    	   updateBis();
    }
    
    

   /* public void globalSynchronisation(){
       	
    	
    	for(String name : globalGrid.getTempName()){
    		
    	       List<Double> values = globalGrid.getGeomTempValues(name);
    	       
    	       if(!values.isEmpty()){
    	    	   
    	    	   if(aggregMap.keySet().contains(name)){
               
                   CellAggregOperator cao = (CellAggregOperator)aggregMap.get(name);                   
                   Double d = cao.apply(values, 0.0);                  
                   globalGrid.setCellValue(name, x, y, d);
                //   globalGrid.cleanOperator();
               
    	    	   } else{
             
                   globalGrid.setCellValue(name, x, y, (Double)values.get((int)(Math.random() * (double)values.size())));
                   //globalGrid.clearGeomTempVal();
               	   }
    	       }
    	
    	}
        globalGrid.clearGeomTempVal();

    
     	for(String name : grid.getTempName()){
    		
    	       List<Double> values = grid.getGeomTempValues(name);
    	       if(!values.isEmpty()){
               if(aggregMap.keySet().contains(name)){
               
                   CellAggregOperator cao = (CellAggregOperator)aggregMap.get(name);
                   Double d = cao.apply(values, 0.0);
                   grid.setCellValue(name, x2, y2, d);
                  
                  grid.cleanOperator();
               
               } else{
              
                   grid.setCellValue(name, x2, y2, (Double)values.get((int)(Math.random() * (double)values.size())));
               }
    	       }
    	
    	}
     	 grid.clearGeomTempVal();
    	

    }*/

 
    
       

    private int[] square2(){
    	
    	
    	
    	int minX = x * rXRes;
    	int minY = y * rYRes;
    	int maxX =0;
    	int maxY =0;
    	
    	if(minX + rXRes > grid.getWidth()){
    		maxX = grid.getWidth();
    	}else{
    		maxX = minX + rXRes - 1;
    	}
    	
    	if(minY + rYRes > grid.getHeight()){
    		maxY = grid.getHeight();
    	}else{
    		maxY = minY + rYRes - 1;
    	}
    	return new int[]{minX, minY, maxX, maxY};
    	
    	
    }
    
    
    
   /* private Coordinate worldUpLeftCoordinate(){
    
        int newX = (x + globalGrid.getMinX()) - r1XRes / 2;
        int newY = (y + globalGrid.getMinY()) - r1YRes / 2;
        
       
        if(newX > globalGrid.getWidth() - 1){
        	newX = globalGrid.getWidth() - 1;
        }
        if(newY > globalGrid.getHeight() - 1){
        	newY = globalGrid.getHeight() - 1;
        }
        return globalGrid.gridCoordinate(newX, newY);
    }

    private Coordinate worldDownRightCoordinate(){
    
        int newX = x + globalGrid.getMinX() + r1XRes / 2;
        int newY = y + globalGrid.getMinY() + r1YRes / 2;        
        if(newX > globalGrid.getWidth() - 1){
        	newX = globalGrid.getWidth() - 1;
        }
        if(newY > globalGrid.getHeight() - 1){
        	newY = globalGrid.getHeight() - 1;
        }
        return globalGrid.gridCoordinate(newX, newY);
    }*/

    private void setCursor(){
   
    int[] square = square2();
       
        gridCursor.init(square);
    }

    public abstract void update();
    
    public void updateBis(){
    	
    	
    	((Cell)r1.getSpatialType()).setX(x);
    	((Cell)r1.getSpatialType()).setY(y);
    	((Cell)r2.getSpatialType()).setX(x2);
    	((Cell)r2.getSpatialType()).setY(y2);
    	
    }

    public void clearAggregMap(){    
        aggregMap.clear();
    }

    public void addOperator(String name, CellAggregOperator operator){    
        aggregMap.put(name, operator);
    }

   /* public void setCellOperator(String name, AggregOperator<Double, List<Double>> operator){    
        CellAggregOperator cao = new CellAggregOperator(operator, name);
        aggregMap.put(name, cao);
    }*/
    public void setCellOperator(String name, AggregOperator operator, KeyMap<String, String> typeProps)
    {
        CellAggregOperator cao = new CellAggregOperator();
        if(typeProps.get(name).equals("Double")){
        	setAggregOpDouble(name, operator, false);
        }else if(typeProps.get(name).equals("Integer")){
        	setAggregOpInteger(name, operator, false);

        }else if(typeProps.get(name).equals("Float")){
        	setAggregOpFloat(name, operator, false);

        }else if(typeProps.get(name).equals("Byte")){
        	setAggregOpByte(name, operator, false);

        }else if(typeProps.get(name).equals("Boolean")){
        	setAggregOpBoolean(name, operator, false);

        }
    }
    public void setAggregOpDouble(String name, AggregOperator<Double, List<Double>> agg, boolean val){
        CellAggregOperator cao = new CellAggregOperator();
        cao.setOperatorDouble(agg);
        aggregMap.put(name, cao);

    }
public void setAggregOpInteger(String name, AggregOperator<Integer, List<Integer>> agg, boolean val){
        CellAggregOperator cao = new CellAggregOperator();
        cao.setOperatorInteger(agg);
        aggregMap.put(name, cao);

    }

public void setAggregOpFloat(String name, AggregOperator<Float, List<Float>> agg, boolean val){
        CellAggregOperator cao = new CellAggregOperator();
        cao.setOperatorFloat(agg);
        aggregMap.put(name, cao);
    }

public void setAggregOpByte(String name, AggregOperator<Byte, List<Byte>> agg, boolean val){
        CellAggregOperator cao = new CellAggregOperator();
        cao.setOperatorByte(agg);
        aggregMap.put(name, cao);
    }

public void setAggregOpBoolean(String name, AggregOperator<Boolean, List<Boolean>> agg, boolean val){
        CellAggregOperator cao = new CellAggregOperator();
        cao.setOperatorBoolean(agg);
        aggregMap.put(name, cao);
    }
    public void globalSynchronisation(){
    	for(int i = 0; i < globalGrid.getWidth(); i ++){
    		
    	      CellValues cv = mrm.get(i); 
    	for(String name : mrm.getProperties()){
    		
    	      List<Double> values  =cv.getValues(name);
    	       if(!values.isEmpty()){
    	    	   
    	    	  // .println("Size "+i+" "+y+" "+ values.size() +" "+name);
    	    	   if(aggregMap.keySet().contains(name)){
               
                   CellAggregOperator cao = (CellAggregOperator)aggregMap.get(name);                   
                   Double d = cao.apply(values, 0.0);                  
                   globalGrid.setCellValue(name, i, y, d);
                //   globalGrid.cleanOperator();
               
    	    	   } else{
             
                   globalGrid.setCellValue(name, i, y, (Double)values.get((int)(Math.random() * (double)values.size())));
                   //globalGrid.clearGeomTempVal();
               	   }
    	       }
    	       cv.clear(name);
    	     //  CellValues v = mrm.get(globalGrid.getWidth());
    	      // v.clear(name);
    	}
    	
    	}
    }

    public class GridCursor {
        
        int x3;
        int y3;
        int width;
        int height;
        int minX;
        int minY;
        int maxX;
        int maxY;
     
       public void init(int[] square){
     
           /*this.minX = square[0];
           this.minY = square[3];
           width = square[2] - square[0];
           height = square[1] - square[3];
           x3 = minX;
           y3 = minY;*/
    	   
    	   this.x3 = square[0];
    	   this.y3 = square[1];
    	   
    	   this.minX = square[0];
    	   this.minY = square[1];
    	   
    	   this.maxX = square[2];
    	   this.maxY = square[3];
    	   
           
       }

       public boolean hasNext(){        
    	   if(x3 == maxX && y3 == maxY){
    		   return false;
    	   }
    	   return true;
       }

       public void next(){
       
           if(x3 == maxX){            
               x3 = minX;
               y3++;
               
           } else {            
               x3++;
           }
         
          // update();
       }   
       
       public int getX(){
       	return x3;
       }
       
       public int getY(){
       	return y3;
       }

      
   }
   /* public class GridCursor {
        
    	 int x3;
         int y3;
         int width;
         int height;
         int minX;
         int minY;
      
        public void init(int[] square){
      
            this.minX = square[0];
            this.minY = square[3];
            width = square[2] - square[0];
            height = square[1] - square[3];
            x3 = minX;
            y3 = minY;
            
        }

        public boolean hasNext(){        
            return x3 < width + minX && y3 < height + minY;
        }

        public void next(){
        
            if(x3 == width + minX - 1){            
                x3 = minX;
                y3++;
                
            } else {            
                x3++;
            }
           // update();
        }   
        
        public int getX(){
        	return x3;
        }
        
        public int getY(){
        	return y3;
        }

       
    }*/
}
