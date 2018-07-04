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
package fr.ocelet.runtime.relation;

import com.vividsolutions.jts.geom.Coordinate;

import fr.ocelet.runtime.entity.AbstractEntity;
import fr.ocelet.runtime.geom.ocltypes.Cell;
import fr.ocelet.runtime.ocltypes.KeyMap;
import fr.ocelet.runtime.ocltypes.List;
import fr.ocelet.runtime.raster.CellAggregOperator;
import fr.ocelet.runtime.raster.Grid;
import fr.ocelet.runtime.raster.MultiResolutionManager;
import fr.ocelet.runtime.relation.regularedges.DiRegularCellsEdgeManager;

import java.util.*;
import java.util.concurrent.TimeUnit;

// Referenced classes of package fr.ocelet.runtime.relation:
//            OcltEdge, OcltRole, AggregOperator



public abstract class DiCursorEdge extends OcltEdge{

	private Grid globalGrid; // grid of same or lower resolution
	private Grid grid; // grid of same or higher resolution

	protected int x; // x cell value for globalGrid
	protected int y; // y cell value for globalGrid
	protected int x2; // x cell value for grid
	protected int y2; // y cell value for grid

	protected int startX; 
	protected int startY;

	protected int startX2;
	protected int startY2;

	protected int endX;
	protected int endY;

	protected int endX2;
	protected int endY2;

	protected int globalWidth;

	private int colCount = 0;
	private int currentY;
	protected Grid grid1;
	protected Grid grid2;
	
	private OcltRole r1;
	private OcltRole r2;
	private HashMap<String, CellAggregOperator> aggregMap = new HashMap<String, CellAggregOperator>();
	private HashMap<String, CellAggregOperator> gridAggregMap = new HashMap<String, CellAggregOperator>();
	private boolean equalGrid = false;
	private MultiResolutionManager mrm;
	private DiRegularCellsEdgeManager edges = new DiRegularCellsEdgeManager();

	public abstract KeyMap<String, String> getEdgeProperties();

	public void setIntegerPropertySize(int size){
		edges.setIntegerPropertySize(size, grid, globalGrid);
	}
	public void setBooleanPropertySize(int size){
		edges.setBooleanPropertySize(size, grid, globalGrid);
	}
	public void setDoublePropertySize(int size){
		edges.setDoublePropertySize(size, grid, globalGrid);
	}

	public void setDoubleProperty(int property, Double value){
		edges.setDoubleValue(x2, y2, property, value);
	}
	public void setIntegerProperty(int property, Integer value){
		edges.setIntegerValue(x2, y2, property, value);
	}
	public void setBooleanProperty(int property, Boolean value){
		edges.setBooleanValue(x2, y2, property, value);
	}

	public Double getDoubleProperty(int property){
		return edges.getDoubleValue(x2, y2, property);
	}

	public Integer getIntegerProperty(int property){
		return edges.getIntegerValue(x2, y2, property);
	}
	public Boolean getBooleanProperty(int property){
		return edges.getBooleanValue(x2, y2, property);
	}

	private void initEdgeProperty(){

		KeyMap<String, String> properties = getEdgeProperties();
		int doubleIndex = 0;
		int integerIndex = 0;
		int booleanIndex = 0;		
		for(String name : properties.keySet()){

			if(properties.get(name).equals("Double")){
				doubleIndex++;
			}
			if(properties.get(name).equals("Integer")){
				integerIndex++;
			}
			if(properties.get(name).equals("Boolean")){
				booleanIndex++;
			}
		}

		setIntegerPropertySize(integerIndex);
		setBooleanPropertySize(booleanIndex);
		setDoublePropertySize(doubleIndex);
	}
	
	
	
	public DiCursorEdge(List<? extends AbstractEntity> r1List, List<? extends AbstractEntity> r2List){   //Grid grid1, Grid grid2){

		AbstractEntity ae1 = (AbstractEntity)r1List.get(0);
		Cell cell1 = (Cell)ae1.getSpatialType();

		AbstractEntity ae2 = (AbstractEntity)r2List.get(0);
		Cell cell2 = (Cell)ae2.getSpatialType();

		 grid1 = cell1.getGrid();
		 grid2 = cell2.getGrid();
		
		
			rescale2();
	
	

	}

	private void rescale(){
		if(startX < 0){
			startX = 0;
		}
		if(startY < 0){
			startY = 0;
		}
		if(startY2 < 0){
			startY2 = 0;
		}
		if(startX2 < 0){
			startX2 = 0;
		}
		if(endX > globalGrid.getWidth()){
			endX = globalGrid.getWidth();
		}
		if(endY > globalGrid.getHeight()){
			endY = globalGrid.getHeight();
		}

		if(endX2 > grid.getWidth()){
			endX2 = grid.getWidth();
		}

		if(endY2 > grid.getHeight()){
			endY2 = grid.getHeight();
		}
	}
	protected String getCellType(){
		return null;
	}
	public void updateRoleInfo(){	

		Cell cell1 = (Cell)getRole(new Integer(0)).getSpatialType();
		Grid grid = cell1.getGrid();
		
		


		if (grid.equals(globalGrid)){
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
			grid.setMode(2);
		}


	}


	public void initInteraction() {
	
	}


	public abstract OcltRole getRole(Integer i);



	public OcltRole getRole(int i){
		return null;
	}

	public boolean hasNext(){

		if(equalGrid){
			if(x == endX - 1 && y == endY){
				cellSynchronisation();
				x = startX;
				y = startY;
				x2 = -1;
				y2 = -1;

				return false;
			} else {            
				return true;
			}
		}
		if((x2 == endX2 && y2 == endY2)){// || (x == endX && y == endY)){

			
			finalSynchronisation();
			clearAggregMap();
			cellSynchronisation();
			
			
			colCount = startX2;
			x2 = -1;
			y2 = -1;
			x = startX;
			y = startY;
			grid.setMode(1);
			globalGrid.setMode(1);
			
			//	globalGrid.cleanOperator();
			return false;
		}
		//if(x2 != -1 && y2 != -1)
		cellSynchronisation();
		return true; 
	}



	public void next(){

		
		if(equalGrid){

			if(x2 == -1 && y2 == -1){

				x2 = startX2;
				y2 = startY2;

			} else  if(x == endX - 1){        
				x = startX;
				y++;
				x2 = startX2;
				y2++;

			} else {

				x++;
				x2++;
			}

		}else{

			if(x2 == -1 && y2 == -1){
				
				x2 = startX2;
				y2 = startY2;
				x = startX;
				y = startY;
				
				Coordinate c = null;
				int[] c2 = null;
		
					c = grid.gridCoordinate(x2, y2);
					if(c != null)
					c2 = globalGrid.gridCoordinate(c.x, c.y);
			
				//colCount++;

				////System.out.println(x+" "+y+" "+x2+" "+y2+" " +colCount);

				if(c2 != null){
					
					x = c2[0];
					y = c2[1];
				
					if(y == currentY + 1 || y == currentY + 2){
						colCount++;

						////System.out.print("  "+colCount);
					}
				}
				currentY = y;
				globalGrid.initMrm(startX, endX, currentY);
				mrm = globalGrid.getMrm();
				
			}else{
				if(colCount == endX2){

					globalSynchronisation();

					currentY ++;
					mrm.switchLine();
					colCount = startX2;
				}
			
				if(x2 == endX2){
					colCount = startX2;
					
					x2 = startX2;
					y2++;
				
					if(colCount == endX2){
					
						

					//	globalSynchronisation();

						y = currentY + 1;
						//mrm.switchLine();
						currentY = y;
						x = startX;
					//	colCount = startX2;
						Coordinate c = null;
						int[] c2 = null;
							c = grid.gridCoordinate(x2, y2);
							if(c != null)
							c2 = globalGrid.gridCoordinate(c.x, c.y);

						if(c2 != null){
							x = c2[0];
							y = c2[1];
						
							if(y == currentY + 1 || y == currentY + 2){
								colCount++;

							}
						}else{
							
							if(y == currentY + 1 || y == currentY + 2){
								colCount++;
							}
							if(hasNext()){
								next();
							}else{
								System.out.println("end1"+" "+x+" "+y);
								x2 = endX2;
								y2 = endY2;
							}
						}

					}else{
						
						Coordinate c = null;
						int[] c2 = null;
					
							c = grid.gridCoordinate(x2, y2);
							if(c != null) {
								
							c2 = globalGrid.gridCoordinate(c.x, c.y);
							//colCount = x2;	
							}

						if(c2 != null){
							x = c2[0];
							y = c2[1];
						
							if(y == currentY + 1 || y == currentY + 2){
								colCount++;
							}


						
						}else{
							
							if(y == currentY + 1 || y == currentY + 2){

								colCount++;

							
							}
							if(hasNext()){
								next();
							}else{
								System.out.println("end2"+" "+x+" "+y);
								x2 = endX2;
								y2 = endY2;
							}
						}
					}			

						
				}else{
					
					x2++;			
					Coordinate c = null;
					int[] c2 = null;				
					try {
						c = grid.gridCoordinate(x2, y2);
					}catch(Exception e) {
						
					}
					if(c != null )
						c2 = globalGrid.gridCoordinate(c.x, c.y);			
					

					if(c2 != null){
						
						x = c2[0];
						y = c2[1];
						
						if(y == currentY + 1 || y ==currentY + 2){
							colCount++;
						}
						
					}else{
						
						if(y == currentY + 1 || y == currentY + 2){

							colCount++;
						}
						if(hasNext()){
							
							next();
						}else{
						
							x2 = endX2;
							y2 = endY2;
							x = endX;
							y = endY;
									
						}
					}
				}
			}
		}
		updateBis();
	}




	private double[] scalingdouble(Double[] bounds1, Double[] bounds2){

		double[] scaled = new double[4];

		if(bounds1[0]  < bounds2[0] ){
			scaled[0]  = bounds2[0];
		}else{
			scaled[0] = bounds1[0];

		}
		if(bounds1[1] < bounds2[1]){
			scaled[1] = bounds2[1];
		}else{
			scaled[1] = bounds1[1];

		}

		if(bounds1[2]  > bounds2[2] ){
			scaled[2] = bounds2[2];
		}else{
			scaled[2] = bounds1[2];

		}
		if(bounds1[3]  > bounds2[3]){
			scaled[3] = bounds2[3] ;
		}else{
			scaled[3] = bounds1[3] ;

		}
		return scaled;
	}


	public abstract void update();

	public void updateBis(){


		((Cell)r1.getSpatialType()).setX(x);
		((Cell)r1.getSpatialType()).setY(y);
		((Cell)r2.getSpatialType()).setX(x2);
		((Cell)r2.getSpatialType()).setY(y2);

	}

	public void clearGridAggregMap(){    
		gridAggregMap.clear();
	}

	public void addGridOperator(String name, CellAggregOperator operator){    
		gridAggregMap.put(name, operator);
	}

	public void setGridCellOperator(String name, AggregOperator operator, KeyMap<String, String> typeProps)
	{
		CellAggregOperator cao = new CellAggregOperator();
		if(typeProps.get(name).equals("Double")){
			setGridAggregOpDouble(name, operator, false);
		}else if(typeProps.get(name).equals("Integer")){
			setGridAggregOpInteger(name, operator, false);

		}else if(typeProps.get(name).equals("Float")){
			setGridAggregOpFloat(name, operator, false);

		}else if(typeProps.get(name).equals("Byte")){
			setGridAggregOpByte(name, operator, false);

		}else if(typeProps.get(name).equals("Boolean")){
			setGridAggregOpBoolean(name, operator, false);

		}
	}
	public void setGridAggregOpDouble(String name, AggregOperator<Double, List<Double>> agg, boolean val){
		CellAggregOperator cao = new CellAggregOperator();
		cao.setOperatorDouble(agg);
		gridAggregMap.put(name, cao);

	}
	public void setGridAggregOpInteger(String name, AggregOperator<Integer, List<Integer>> agg, boolean val){
		CellAggregOperator cao = new CellAggregOperator();
		cao.setOperatorInteger(agg);
		gridAggregMap.put(name, cao);

	}

	public void setGridAggregOpFloat(String name, AggregOperator<Float, List<Float>> agg, boolean val){
		CellAggregOperator cao = new CellAggregOperator();
		cao.setOperatorFloat(agg);
		gridAggregMap.put(name, cao);
	}

	public void setGridAggregOpByte(String name, AggregOperator<Byte, List<Byte>> agg, boolean val){
		CellAggregOperator cao = new CellAggregOperator();
		cao.setOperatorByte(agg);
		gridAggregMap.put(name, cao);
	}

	public void setGridAggregOpBoolean(String name, AggregOperator<Boolean, List<Boolean>> agg, boolean val){
		CellAggregOperator cao = new CellAggregOperator();
		cao.setOperatorBoolean(agg);
		gridAggregMap.put(name, cao);
	}
	
	
	public void clearAggregMap(){    
		aggregMap.clear();
		gridAggregMap.clear();
	}

	public void addOperator(String name, CellAggregOperator operator){    
		if(operator.getGrid().equals(globalGrid)) {
			aggregMap.put(name, operator);
		}else {
			gridAggregMap.put(name, operator);
		}
	}

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
		
		
		for(int i = startX; i < endX + 1; i ++){

			CellValues cv = mrm.getFirstLineValue(i); 
			for(int b = 0; b < mrm.getProperties().size(); b++){
				String name = mrm.getProperties().get(b);
				List<Double> values  =cv.getValues(b);
				if(!values.isEmpty()){

					if(aggregMap.keySet().contains(name)){

						CellAggregOperator cao = aggregMap.get(name);   
						Double d;
						if(cao.preval() == false){
							d = cao.apply(values, globalGrid.getDoubleValue(b, i, currentY));
						}else{
							values.add(globalGrid.getDoubleValue(b, i, currentY));
							d = cao.apply(values, globalGrid.getDoubleValue(b, i, currentY));
						}
						globalGrid.setCellValue(b, i, currentY, d);

					} else{

						globalGrid.setCellValue(b, i, currentY, values.get((int)(Math.random() * values.size())));
					}
				}
				cv.clear(b);
			}

		}
			/*for(int i = startX; i < endX + 1; i ++){

				CellValues cv = mrm.getFirstLineValue(i); 
				for(String name : mrm.getProperties()){

					List<Double> values  =cv.getValues(name);
					if(!values.isEmpty()){

						if(aggregMap.keySet().contains(name)){

							CellAggregOperator cao = aggregMap.get(name);   
							Double d;
							if(cao.preval() == false){
								d = cao.apply(values, globalGrid.getDoubleValue(name, i, currentY));
							}else{
								values.add(globalGrid.getDoubleValue(name, i, currentY));
								d = cao.apply(values, globalGrid.getDoubleValue(name, i, currentY));
							}
							globalGrid.setCellValue(name, i, currentY, d);

						} else{

							globalGrid.setCellValue(name, i, currentY, values.get((int)(Math.random() * values.size())));
						}
					}
					cv.clear(name);
				}

			}*/
			//mrm.clearFirstLineProperties();
		
	}

	public void finalSynchronisation(){
		
		
		for(int i = startX; i < endX +  1; i ++){

			CellValues cv = mrm.getFirstLineValue(i); 
			
			for(int b = 0; b < mrm.getProperties().size(); b++){
				String name = mrm.getProperties().get(b);
				List<Double> values  =cv.getValues(b);
				if(!values.isEmpty()){

					
					if(aggregMap.keySet().contains(name)){

						CellAggregOperator cao = aggregMap.get(name);   
						Double d;
						if(cao.preval() == false){
							d = cao.apply(values, globalGrid.getDoubleValue(b, i, currentY));
						}else{
							values.add(globalGrid.getDoubleValue(b, i, currentY));
							d = cao.apply(values, globalGrid.getDoubleValue(b, i, currentY));
						}
						globalGrid.setCellValue(b, i, currentY, d);
						

					} else{

						globalGrid.setCellValue(b, i, currentY, values.get((int)(Math.random() * values.size())));
						
					}
				}
				cv.clear(b);
			
			}
		}
		/*
			for(int i = startX; i < endX +  1; i ++){

				CellValues cv = mrm.getFirstLineValue(i); 
				
				for(String name : mrm.getProperties()){

					List<Double> values  =cv.getValues(name);
					if(!values.isEmpty()){

						
						if(aggregMap.keySet().contains(name)){

							CellAggregOperator cao = aggregMap.get(name);   
							Double d;
							if(cao.preval() == false){
								d = cao.apply(values, globalGrid.getDoubleValue(name, i, currentY));
							}else{
								values.add(globalGrid.getDoubleValue(name, i, currentY));
								d = cao.apply(values, globalGrid.getDoubleValue(name, i, currentY));
							}
							globalGrid.setCellValue(name, i, currentY, d);
							

						} else{

							globalGrid.setCellValue(name, i, currentY, values.get((int)(Math.random() * values.size())));
							
						}
					}
					cv.clear(name);
				
				}
			}*/
					/*
				cv = mrm.getSecondLineValue(i); 
				for(String name : mrm.getProperties()){

					List<Double> values  =cv.getValues(name);
					if(!values.isEmpty()){

						// .println("Size "+i+" "+y+" "+ values.size() +" "+name);
						if(aggregMap.keySet().contains(name)){

							CellAggregOperator cao = aggregMap.get(name);   
							Double d;
							if(cao.preval() == false){
								d = cao.apply(values, null);
							}else{
								d = cao.apply(values, globalGrid.getDoubleValue(name, i, currentY + 1));
							}
							globalGrid.setCellValue(name, i, currentY + 1, d);
							//   globalGrid.cleanOperator();

						} else{

							globalGrid.setCellValue(name, i, currentY + 1, values.get((int)(Math.random() * values.size())));
							//globalGrid.clearGeomTempVal();
						}
					}
					cv.clear(name);
					//  CellValues v = mrm.get(globalGrid.getWidth());
					// v.clear(name);
				}

				cv = mrm.getThirdLineValue(i); 
				for(String name : mrm.getProperties()){

					List<Double> values  =cv.getValues(name);
					if(!values.isEmpty()){

						// .println("Size "+i+" "+y+" "+ values.size() +" "+name);
						if(aggregMap.keySet().contains(name)){

							CellAggregOperator cao = aggregMap.get(name);   
							Double d;
							if(cao.preval() == false){
								d = cao.apply(values, null);
							}else{
								d = cao.apply(values, globalGrid.getDoubleValue(name, i, currentY + 2));
							}
							globalGrid.setCellValue(name, i, currentY + 2, d);
							//   globalGrid.cleanOperator();

						} else{

							globalGrid.setCellValue(name, i, currentY + 2, values.get((int)(Math.random() * values.size())));
							//globalGrid.clearGeomTempVal();
						}
					}
					cv.clear(name);
					//  CellValues v = mrm.get(globalGrid.getWidth());
					// v.clear(name);
				}*/

			
		//mrm.clearFirstLineProperties();
		
	}
	
	public class GraphBounds{
		
		
		public int startX;
		public int startX2;
		public int startY;
		public int startY2;
		public int endX;
		public int endX2;
		public int endY;
		public int endY2;
		
		
		public GraphBounds(){
			
		}
		public void setBounds(int startX, int startY, int endX, int endY, int startX2, int startY2, int endX2, int endY2){
			
			this.startX = startX;
			this.startY = startY;
			this.endX = endX;
			this.endY = endY;
			
			this.startX2 = startX2;
			this.startY2 = startY2;
			this.endX2 = endX2;
			this.endY2 = endY2;
		}

		
		public GraphBounds(int startX, int startY, int endX, int endY, int startX2, int startY2, int endX2, int endY2){
			
			this.startX = startX;
			this.startY = startY;
			this.endX = endX;
			this.endY = endY;			
			this.startX2 = startX2;
			this.startY2 = startY2;
			this.endX2 = endX2;
			this.endY2 = endY2;
		}
		
		@Override
		public String toString(){
			return startX+" "+endX+" "+startY+" "+endY+" "+startX2+" "+endX2+" "+startY+" "+startY2;
		
	}
}

	/*
	 * Return cells grids first and last grids coordinates that can spatially match
	 * Set the grid with the finest resolution as grid and the lowest as globalGrid
	 */
	private void scalingGrids(Grid grid1, Grid grid2) {
	
		
		if(grid1.getWidth() * grid1.getHeight() < grid2.getWidth() * grid2.getHeight()){ 
			globalGrid = grid1;
			grid = grid2;

	
		}else {
			globalGrid = grid2;
			grid = grid1;
		}
		
		int minGridX = 0;
		int minGridY = 0;
		int minGlobalX = 0;
		int minGlobalY = 0;
		int maxGridX = grid.getWidth() - 1;
		int maxGridY = grid.getHeight() - 1;
		int maxGlobalX = globalGrid.getWidth() - 1;
		int maxGlobalY = globalGrid.getHeight() - 1;
		
 		Coordinate upperLeftGrid = grid.gridCoordinate(minGridX, minGridY);
		Coordinate downRightGrid = grid.gridCoordinate(maxGridX, maxGridY);
		
		Coordinate upperLeftGlobal = globalGrid.gridCoordinate(minGlobalX, minGlobalY);
		Coordinate downRightGlobal = globalGrid.gridCoordinate(maxGlobalX, maxGlobalY);
		
		int[] minUpper = grid.gridCoordinate(upperLeftGrid.x, upperLeftGrid.y);
	
		
		/*int[] globalToGridUpperLeft = globalGrid.gridCoordinate(upperLeftGrid.x, upperLeftGrid.y);
		int[] globalToGridDownRight = globalGrid.gridCoordinate(downRightGrid.x, downRightGrid.y);
		
		int[] gridToGlobalUpperLeft = grid.gridCoordinate(upperLeftGlobal.x, upperLeftGlobal.y);
		int[] gridToGlobalDownRight = grid.gridCoordinate(downRightGlobal.x, downRightGlobal.y);*/
		
		Double[] gridBounds = new Double[]{upperLeftGrid.x, downRightGrid.y, downRightGrid.x, upperLeftGrid.y};
		Double[] globalBounds = new Double[]{upperLeftGlobal.x, downRightGlobal.y, downRightGlobal.x, upperLeftGlobal.y};
		 
		double[] scaled = scalingdouble(gridBounds, globalBounds);
		// //System.out.println("cScale "+scaled[0]+" "+scaled[1]+" "+scaled[2]+" "+scaled[3]);
		double xResFactor = 0.0;
		double yResFactor = 0.0;
		double precision = 1.0;
		if(grid.getXRes() == globalGrid.getXRes()) {
			xResFactor = (grid.getXRes() * precision) / 2;
			
		}else {
			if(grid.getXRes() < globalGrid.getXRes()) {
				xResFactor = (grid.getXRes() * precision) / 2; 
			}else {
				xResFactor = (globalGrid.getXRes() * precision) / 2;
 			}
		}	
		if(grid.getYRes() == globalGrid.getYRes()) {
			yResFactor = (grid.getYRes() * precision) / 2;
			
		}else {
			if(grid.getYRes() < globalGrid.getYRes()) {
				yResFactor = (grid.getYRes() * precision) / 2; 
			}else {
				yResFactor = (globalGrid.getYRes() * precision) / 2;
 			}
		}
		
		double factorX = xResFactor;
		double factorY = yResFactor;
		
		int[] gridMin = grid.gridCoordinate(scaled[0] + factorX, scaled[3] - factorY);
		while(gridMin == null) {
			factorX = factorX + xResFactor;
			factorY = factorY + yResFactor;
			gridMin = grid.gridCoordinate(scaled[0] + factorX, scaled[3] - factorY);
		}
		 factorX = xResFactor;
		 factorY = yResFactor;
		int[] gridMax = grid.gridCoordinate(scaled[2] - factorX, scaled[1] + factorY);
		while(gridMax == null) {
			factorX = factorX + xResFactor;
			factorY = factorY + yResFactor;
			gridMax = grid.gridCoordinate(scaled[2] - factorX, scaled[1] + factorY);
		}
		factorX = xResFactor;
		 factorY = yResFactor;
		int[] globalMin = globalGrid.gridCoordinate(scaled[0] +factorX, scaled[3] - factorY);
		while(globalMin == null) {
			factorX = factorX + xResFactor;
			factorY = factorY + yResFactor;
			globalMin = globalGrid.gridCoordinate(scaled[0] + factorX, scaled[3] - factorY);
		}
		factorX = xResFactor;
		 factorY = yResFactor;
		int[] globalMax = globalGrid.gridCoordinate(scaled[2] - factorX, scaled[1] + factorY);
		while(globalMax == null) {
			factorX = factorX + xResFactor;
			factorY = factorY + yResFactor;
			globalMax = globalGrid.gridCoordinate(scaled[2] - factorX, scaled[1] + factorY);
		}
		
		Coordinate gridCMin = grid.gridCoordinate(gridMin[0], gridMin[1]);
		Coordinate gridCMax = grid.gridCoordinate(gridMax[0], gridMax[1]);
		
		Coordinate globalCMin = globalGrid.gridCoordinate(globalMin[0], globalMin[1]);
		Coordinate globalCMax = globalGrid.gridCoordinate(globalMax[0], globalMax[1]);
		
		////System.out.println(globalCMin);
		int[] t1 = globalGrid.gridCoordinate(globalCMin.x, globalCMin.y);
		////System.out.println(t1[0]+" "+t1[1]);
	
		
		////System.out.println(globalCMax);
		int[] t2 = globalGrid.gridCoordinate(globalCMax.x, globalCMax.y);
		////System.out.println(t2[0]+" "+t2[1]);
		
		Double[] chainRescaledGrid = new Double[] {gridCMin.x, gridCMax.y, gridCMax.x, gridCMin.y};
		Double[] chainRescaledGlobal = new Double[] {globalCMin.x, globalCMax.y, globalCMax.x, globalCMin.y};
	
		double[] chainedScale = scalingdouble(chainRescaledGrid, chainRescaledGlobal);
		
		 /*gridMin = grid.gridCoordinate(chainedScale[0], chainedScale[3]);
		 gridMax = grid.gridCoordinate(chainedScale[2], chainedScale[1]);
		 globalMin = globalGrid.gridCoordinate(chainedScale[0], chainedScale[3]);
		 globalMax = globalGrid.gridCoordinate(chainedScale[2], chainedScale[1]);*/
		// //System.out.println("chainedScale "+chainedScale[0]+" "+chainedScale[1]+" "+chainedScale[2]+" "+chainedScale[3]);
		/*if(globalMin == null){
			startX = 0;
			startY = 0;
		}else{
			startX = globalMin[0];
			startY = globalMin[1];
		}
		if(globalMax == null){
			endX = globalGrid.getWidth()-1;
			endY = globalGrid.getHeight()-1;
		}else{
			endX = globalMax[0];
			endY = globalMax[1];
		}
		if(gridMin == null){
			startX2 = 0;
			startY2 = 0;
		}else{
			startX2 = gridMin[0];
			startY2 = gridMin[1];
		}
		if(gridMax == null){
			endX2 = grid.getWidth()-1;
			endY2 = grid.getHeight()-1;
		}else{
			endX2 = gridMax[0];
			endY2 = gridMax[1];
		}*/
		 	
			startX = globalMin[0];
			startY = globalMin[1];		
	
			endX = globalMax[0];
			endY = globalMax[1];
			if(startX > globalGrid.getMinX()) {
				startX = startX - 1;
			}
			if(startY > globalGrid.getMinY()) {
				startY = startY - 1;
			}
			if(endX < globalGrid.getMaxX()) {
				endX = endX + 1;
			}
			if(endY < globalGrid.getMaxY()) {
				endY = endY + 1;
			}
			
			startX2 = gridMin[0];
			startY2 = gridMin[1];
		
		
			endX2 = gridMax[0];
			endY2 = gridMax[1];
		Coordinate mG = globalGrid.gridCoordinate(endX, endY);
		
		/*double xResDiff = globalGrid.getXRes() / 2 - grid.getXRes() / 2;
		int[] img = grid.gridCoordinate(mG.x + xResDiff, mG.y );
		while(img == null) {
			xResDiff = xResDiff - grid.getXRes() / 2;
			img = grid.gridCoordinate(mG.x + xResDiff, mG.y );
			
		}
		endX2 = img[0];
		double yResDiff = - globalGrid.getYRes() / 2 + grid.getYRes() / 2;
		img = grid.gridCoordinate(mG.x , mG.y + yResDiff);
		while(img == null) {
			yResDiff =yResDiff + grid.getYRes() / 2;
			img = grid.gridCoordinate(mG.x , mG.y + yResDiff);
			
		}*/
		/*double xResDiff = globalGrid.getXRes() / 2 - grid.getXRes() / 2;
		double yResDiff = - globalGrid.getYRes() / 2 + grid.getYRes() / 2;
		int[] img = grid.gridCoordinate(mG.x + xResDiff, mG.y + yResDiff );
	
		
		while(img == null) {
			yResDiff =yResDiff + grid.getYRes() / 2;
			xResDiff = xResDiff - grid.getXRes() / 2;
			img = grid.gridCoordinate(mG.x + xResDiff , mG.y + yResDiff);
			
		}
		endY2 = img[1];*/
		if(endX2 > grid.getMaxX()) {
			endX2 = grid.getMaxX();
		}
		
		if(endY2 > grid.getMaxY()) {
			endY2 = grid.getMaxY();
		}
		
		
		
		////System.out.println(startX+" "+startY+" "+endX+" "+endY);
		////System.out.println(startX2+" "+startY2+" "+endX2+" "+endY2);
		//printMatchCoord();
		////System.out.println(globalGrid.getMaxX()+" "+globalGrid.getMaxY());
		equalGrid = false;
		x = startX;
		y = startY;
		x2 = -1;
		y2 = -1;
		currentY = startY;
		colCount = startX2;
		initEdgeProperty();
	}
	

	public void cellSynchronisation(){
		
		for(int b = 0; b < grid.getPropertiesName().size(); b ++) {			
	
			String name = grid.getPropertiesName().get(b);
			
			List<Double> values = grid.getGeomTemp2Values(b);
			
			if(values != null && !values.isEmpty()){

				if(gridAggregMap.keySet().contains(name))
				{
					Double d;
					CellAggregOperator cao = gridAggregMap.get(name);
					Double value = grid.getDoubleValue(b, x2, y2);
					if(cao.preval() == false){
						
						d = cao.apply(values, value);
					}else{
						values.add(value);
						d = cao.apply(values, value);
					}

					grid.setCellValue(b, x2, y2, d);

				} else{
				

					if(values.size() > 1){
						grid.setCellValue(b, x2, y2, values.get((int)(Math.random() * values.size())));
					}else{
						grid.setCellValue(b, x2, y2, values.get(0));
					}
				}
			}
		}	
		grid.clearGeomTempVal2();
		
		/*for(Iterator iterator = grid.getTempName().iterator(); iterator.hasNext();)
		{
			String name = (String)iterator.next();
			
			List<Double> values = grid.getGeomTempValues(name);
			
			if(!values.isEmpty()){

				if(gridAggregMap.keySet().contains(name))
				{
					Double d;
					CellAggregOperator cao = gridAggregMap.get(name);
					Double value = grid.getDoubleValue(name, x2, y2);
					if(cao.preval() == false){
						d = cao.apply(values, value);
					}else{
						values.add(value);
						d = cao.apply(values, value);
					}

					grid.setCellValue(name, x2, y2, d);

				} else{
				

					if(values.size() > 1){
						grid.setCellValue(name, x2, y2, values.get((int)(Math.random() * values.size())));
					}else{
						grid.setCellValue(name, x2, y2, values.get(0));
					}
				}
			}
		}	
		grid.clearGeomTempVal();*/
		
	}
	
	private void rescale2() {
		
		double[] iScale = scalingdouble(grid1.getWorldBounds(), grid2.getWorldBounds());
		
		int g1ScaledWidth = (int)Math.round((iScale[2] - iScale[0]) / grid1.getXRes());
		if((g1ScaledWidth -(iScale[2] - iScale[0])) < 0.5) {
			g1ScaledWidth++;
		}
		
		int g1ScaledHeight = (int)Math.round((iScale[3] - iScale[1]) / grid1.getYRes());
		if((g1ScaledHeight -(iScale[3] - iScale[1])) < 0.5) {
			g1ScaledHeight++;
		}
		
		int g2ScaledWidth = (int)Math.round((iScale[2] - iScale[0]) / grid2.getXRes());
		if((g2ScaledWidth -(iScale[2] - iScale[0])) < 0.5) {
			g2ScaledWidth++;
		}
		
		int g2ScaledHeight = (int)Math.round((iScale[3] - iScale[1]) / grid2.getYRes());
		if((g2ScaledHeight -(iScale[3] - iScale[1])) < 0.5) {
			g2ScaledHeight++;
		}
		
		
		if(g1ScaledWidth * g1ScaledHeight < g2ScaledWidth * g2ScaledHeight){ 
			globalGrid = grid1;
			grid = grid2;

	
		}else {
			globalGrid = grid2;
			grid = grid1;
		}
		
		
		
	
	
		
	
		
		
		Double[] gridBounds = grid.getWorldBounds();
		Double[] globalGridBounds = globalGrid.getWorldBounds();
		Double[] scaled = new Double[4];
		
		double gridMinX = gridBounds[0] + grid.getXRes() / 2;
		double gridMinY = gridBounds[1] + grid.getYRes() / 2;
		double gridMaxX = gridBounds[2] - grid.getXRes() / 2;
		double gridMaxY = gridBounds[3] - grid.getYRes() / 2;
		double globalGridMinX = globalGridBounds[0] + globalGrid.getXRes() / 2;
		double globalGridMinY = globalGridBounds[1] + globalGrid.getYRes() / 2;
		double globalGridMaxX = globalGridBounds[2] - globalGrid.getXRes() / 2;
		double globalGridMaxY = globalGridBounds[3] - globalGrid.getYRes() / 2;
		if(gridBounds[0]  < globalGridBounds[0] ){
			//scaled[0]  = globalGridBounds[0];
			gridMinX = globalGridBounds[0]- grid.getXRes();
		}else{
			if(gridBounds[0]  > globalGridBounds[0]) {
			//scaled[0] = gridBounds[0];
			globalGridMinX = gridBounds[0] - globalGrid.getXRes();
			}
		}
		if(gridBounds[1] < globalGridBounds[1]){
			//scaled[1] = globalGridBounds[1];
			gridMinY = globalGridBounds[1] - grid.getYRes();
		}else{
			if(gridBounds[1] > globalGridBounds[1]) {
			//scaled[1] = gridBounds[1];
			globalGridMinY = gridBounds[1] - globalGrid.getYRes();
			}
		}

		if(gridBounds[2]  > globalGridBounds[2] ){
			//scaled[2] = globalGridBounds[2];
			gridMaxX = globalGridBounds[2] + grid.getXRes();
		}else{
			if(gridBounds[2]  < globalGridBounds[2] ) {
			//scaled[2] = gridBounds[2];
			globalGridMaxX = gridBounds[2] + globalGrid.getXRes();
			}

		}
		if(gridBounds[3]  > globalGridBounds[3]){
			//scaled[3] = globalGridBounds[3] ;
			gridMaxY = globalGridBounds[3] + grid.getYRes();
		}else{
			if(gridBounds[3]  < globalGridBounds[3]){
			//scaled[3] = gridBounds[3] ;
			globalGridMaxY = gridBounds[3] + globalGrid.getYRes();
			}

		}
		
		double ngridMinX = gridMinX;
		double ngridMinY = gridMinY;
		double ngridMaxX = gridMaxX;
		double ngridMaxY = gridMaxY;
		
		double factorX = grid.getXRes() / 2;
		double factorY = grid.getYRes() / 2;
		int[] gridMin = grid.gridCoordinate(ngridMinX, ngridMaxY);
		
		while(gridMin == null) {
			factorX = factorX + grid.getXRes() / 2;
			factorY = factorY + grid.getYRes() / 2;
			gridMin = grid.gridCoordinate(ngridMinX + factorX, ngridMaxY - factorY);
		
			
		}
		 factorX =  grid.getXRes() / 2;
		 factorY =  grid.getYRes() / 2;
		int[] gridMax = grid.gridCoordinate(ngridMaxX, ngridMinY);
		
		while(gridMax == null) {
			factorX = factorX + grid.getXRes() / 2;
			factorY = factorY + grid.getYRes() / 2;
			gridMax = grid.gridCoordinate(ngridMaxX - factorX, ngridMinY + factorY);
	
			
		}
		
		

		 factorX =  globalGrid.getXRes() / 2;
		 factorY =  globalGrid.getYRes() / 2;
		int[] globalGridMin = globalGrid.gridCoordinate(globalGridMinX, globalGridMaxY);
	
		while( globalGridMin == null) {
			factorX = factorX + globalGrid.getXRes() / 2;
			factorY = factorY + globalGrid.getYRes() / 2;
			
			globalGridMin = globalGrid.gridCoordinate(globalGridMinX + factorX, globalGridMaxY - factorY);
			
			
		}
		 factorX =  globalGrid.getXRes() / 2;
		 factorY =  globalGrid.getYRes() / 2;
		int[] globalGridMax = globalGrid.gridCoordinate(globalGridMaxX, globalGridMinY);
		
		while( globalGridMax == null) {
			factorX = factorX + globalGrid.getXRes() / 2;
			factorY = factorY + globalGrid.getYRes() / 2;
			globalGridMax = globalGrid.gridCoordinate(globalGridMaxX - factorX, globalGridMinY + factorY);
			
			
		}
		
	
		startX = globalGridMin[0];
		startY = globalGridMin[1];
		endX = globalGridMax[0];
		endY = globalGridMax[1];
		
		startX2 = gridMin[0];
		startY2 = gridMin[1];
		endX2 = gridMax[0];
		endY2 = gridMax[1];
		
		if(startX2 < grid.getMinX()) {
			startX2 = grid.getMinX();
		}
		if(startY2 < grid.getMinY()) {
			startY2 = grid.getMinY();
		}
		if(endX2 > grid.getMaxX()) {
			endX2 = grid.getMaxX();
		}
		if(endY2 > grid.getMaxY()) {
			endY2 = grid.getMaxY();
		}
		
		if(startX < globalGrid.getMinX()) {
			startX = globalGrid.getMinX();
		}
		if(startY < globalGrid.getMinY()) {
			startY = globalGrid.getMinY();
		}
		if(endX > globalGrid.getMaxX()) {
			endX = globalGrid.getMaxX();
		}
		if(endY > globalGrid.getMaxY()) {
			endY = globalGrid.getMaxY();
		}
		
		
		
		equalGrid = false;
		x = startX;
		y = startY;
		x2 = -1;
		y2 = -1;
		currentY = startY;
		
		colCount = startX2;
		initEdgeProperty();
	}
	
	

}
	