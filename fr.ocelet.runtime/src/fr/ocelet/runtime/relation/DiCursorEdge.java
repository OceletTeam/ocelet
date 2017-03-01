package fr.ocelet.runtime.relation;

import com.vividsolutions.jts.geom.Coordinate;

import fr.ocelet.runtime.entity.AbstractEntity;
import fr.ocelet.runtime.geom.ocltypes.Cell;
import fr.ocelet.runtime.ocltypes.KeyMap;
import fr.ocelet.runtime.ocltypes.List;
import fr.ocelet.runtime.raster.CellAggregOperator;
import fr.ocelet.runtime.raster.Grid;
import fr.ocelet.runtime.raster.GridManager;
import fr.ocelet.runtime.raster.MultiResolutionManager;
import fr.ocelet.runtime.relation.regularedges.DiRegularCellsEdgeManager;

import java.util.*;

// Referenced classes of package fr.ocelet.runtime.relation:
//            OcltEdge, OcltRole, AggregOperator

/*public abstract class DiCursorEdge extends OcltEdge{

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
	private MultiResolutionManager mrm;


	public DiCursorEdge(Grid grid1, Grid grid2){

		x = 0;
		y = 0;
		x2 = - 1;
		y2 = - 1;       


		Double[] grid1Bounds = grid1.getWorldBounds();
		Double[] grid2Bounds =  grid2.getWorldBounds();

		double[] scaled = scalingdouble(grid1Bounds, grid2Bounds);


		int[] grid1Min = grid1.gridCoordinate(scaled[0],scaled[3]);
		int[] grid1Max = grid1.gridCoordinate(scaled[2],scaled[1]);

		int[] grid2Min = grid2.gridCoordinate(scaled[0],scaled[3]);
		int[] grid2Max = grid2.gridCoordinate(scaled[2],scaled[1]);


		if(grid1.getXRes() * grid1.getYRes() == grid2.getXRes() * grid2.getYRes()) {

			globalGrid = grid1;
			grid = grid2;

			equalGrid = true;

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

				x = startX;
				y = startY;
				x2 = startX2;
				y2 = startY2;

				return false;
			} else {            
				return true;
			}
		}
		if(x2 == grid.getWidth() - 1 && y2 == grid.getHeight() - 1){

			globalSynchronisation();

			clearAggregMap();


			x2 = -1;
			y2 = -1;
			//	globalGrid.cleanOperator();
			return false;
		}

		return true; 
	}



	public void next(){
		//System.out.println(grid.getWidth()+" "+grid.getHeight()+"  "+globalGrid.getWidth()+"   "+globalGrid.getHeight());
		//System.out.println(x+"  "+y+"  "+x2+" "+y2);
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
				globalGrid.initMrm(globalGrid.getWidth());
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




	private double[] scalingdouble(Double[] bounds1, Double[] bounds2){

		double[] scaled = new double[4];

		if(bounds1[0] < bounds2[0]){
			scaled[0] = bounds2[0];
		}else{
			scaled[0] = bounds1[0];

		}
		if(bounds1[1] < bounds2[1]){
			scaled[1] = bounds2[1];
		}else{
			scaled[1] = bounds1[1];

		}

		if(bounds1[2] > bounds2[2]){
			scaled[2] = bounds2[2];
		}else{
			scaled[2] = bounds1[2];

		}
		if(bounds1[3] > bounds2[3]){
			scaled[3] = bounds2[3];
		}else{
			scaled[3] = bounds1[3];

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

	public void clearAggregMap(){    
		aggregMap.clear();
	}

	public void addOperator(String name, CellAggregOperator operator){    
		aggregMap.put(name, operator);
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
		try{
			for(int i = 0; i < globalGrid.getWidth(); i ++){

				CellValues cv = mrm.get(i); 
				for(String name : mrm.getProperties()){

					List<Double> values  =cv.getValues(name);
					if(!values.isEmpty()){

						// .println("Size "+i+" "+y+" "+ values.size() +" "+name);
						if(aggregMap.keySet().contains(name)){

							CellAggregOperator cao = (CellAggregOperator)aggregMap.get(name);                   
							Double d = cao.apply(values, null);                  
							globalGrid.setCellValue(name, i, y, d);
							//   globalGrid.cleanOperator();

						} else{

							globalGrid.setCellValue(name, i, y, values.get((int)(Math.random() * values.size())));
							//globalGrid.clearGeomTempVal();
						}
					}
					cv.clear(name);
					//  CellValues v = mrm.get(globalGrid.getWidth());
					// v.clear(name);
				}

			}
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Global :"+globalGrid.getWidth()+"  "+globalGrid.getHeight()+" "+x+" "+y);
			System.out.println("normal :"+grid.getWidth()+"  "+grid.getHeight()+" "+x2+" "+y2);
		}
	}


}*/


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

		Grid grid1 = GridManager.getInstance().get(cell1.getNumGrid());
		Grid grid2 = GridManager.getInstance().get(cell2.getNumGrid());


		Double[] grid1Bounds = grid1.getWorldBounds();
		Double[] grid2Bounds =  grid2.getWorldBounds();

		double[] scaled = scalingdouble(grid1Bounds, grid2Bounds);

		/*System.out.println("BOUNDS");
		System.out.println(grid1Bounds[0]+" "+grid1Bounds[2]+" "+grid1Bounds[1]+" "+grid1Bounds[3]);
		System.out.println(grid2Bounds[0]+" "+grid2Bounds[2]+" "+grid2Bounds[1]+" "+grid2Bounds[3]);

		System.out.println("SCALED");
		System.out.println(scaled[0]+" "+scaled[2]+" "+scaled[1]+" "+scaled[3]);*/

		int[] grid1Min = grid1.gridCoordinate(scaled[0],scaled[3]);
		int[] grid1Max = grid1.gridCoordinate(scaled[2],scaled[1]);

		int[] grid2Min = grid2.gridCoordinate(scaled[0],scaled[3]);
		int[] grid2Max = grid2.gridCoordinate(scaled[2],scaled[1]);
		
		
		if(grid1.getWidth() == grid2.getWidth() && grid1.getHeight() == grid2.getHeight()) {
			
			globalGrid = grid1;
			startX = 0;
			startY = 0;
			endX = grid1.getWidth();
			endY = grid1.getHeight();
			grid = grid2;
			startX2 = 0;
			startY2 = 0;
			endX2 = grid2.getWidth();
			endY2 = grid2.getHeight();

			equalGrid = true;
			x = startX;
			y = startY;
			x2 = startX2;
			y2 = startY2;       

			//System.out.println(endX+" "+endY+" "+endX2+" "+endY2);

		}else{
				/*if(grid1.getXRes() * grid1.getYRes() < grid2.getXRes() * grid2.getYRes()){        	
					globalGrid = grid2;
					startX = grid2Min[0];
					startY = grid2Min[1];
					endX = grid2Max[0];
					endY = grid2Max[1];

					grid = grid1;
					startX2 = grid1Min[0];
					startY2 = grid1Min[1];
					endX2 = grid1Max[0];
					endY2 = grid1Max[1];

				} else {

					globalGrid = grid1;
					startX = grid1Min[0];
					startY = grid1Min[1];
					endX = grid1Max[0];
					endY = grid1Max[1];
					grid = grid2;
					startX2 = grid2Min[0];
					startY2 = grid2Min[1];
					endX2 = grid2Max[0];
					endY2 = grid2Max[1];

				}*/
			
			if(grid1.getWidth() < grid2.getWidth()){        	
					globalGrid = grid1;
					startX = grid1Min[0];
					startY = grid1Min[1];
					endX = grid1Max[0];
					endY = grid1Max[1];
					grid = grid2;
					startX2 = grid2Min[0];
					startY2 = grid2Min[1];
					endX2 = grid2Max[0];
					endY2 = grid2Max[1];

				} else {

					
					globalGrid = grid2;
					startX = grid2Min[0];
					startY = grid2Min[1];
					endX = grid2Max[0];
					endY = grid2Max[1];

					grid = grid1;
					startX2 = grid1Min[0];
					startY2 = grid1Min[1];
					endX2 = grid1Max[0];
					endY2 = grid1Max[1];

					
					
				}
			//System.out.println("ELSE "+endX+" "+endY+" "+endX2+" "+endY2);


		}
				rescale();
		x = startX;
		y = startY;
		x2 = -1;
		y2 = -1;       
		initEdgeProperty();


	/*	System.out.println("GLOBAL : "+startX+" "+startY+" "+endX+" "+endY);
		System.out.println("normal : "+startX2+" "+startY2+" "+endX2+" "+endY2);
		*/ 
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
		int numGrid = ((Cell)getRole(new Integer(0)).getSpatialType()).getNumGrid();
		cell1.setType(GridManager.getInstance().get(numGrid).getCellShapeType());
		
		Cell cell2 = (Cell)getRole(new Integer(1)).getSpatialType();
		int numGrid2 = ((Cell)getRole(new Integer(1)).getSpatialType()).getNumGrid();
		cell2.setType(GridManager.getInstance().get(numGrid2).getCellShapeType());


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
			if(x == endX - 1 && y == endY - 1){

				x = startX;
				y = startY;
				x2 = -1;
				y2 = -1;

				return false;
			} else {            
				return true;
			}
		}
		if(x2 == endX2 - 1 && y2 == endY2 - 1){

			globalSynchronisation();

			clearAggregMap();


			x2 = -1;
			y2 = -1;
			//	globalGrid.cleanOperator();
			return false;
		}

		return true; 
	}



	public void next(){
		/*System.out.println("TYPE "+equalGrid);
		System.out.println(globalGrid.getWidth()+" "+globalGrid.getHeight());
		System.out.println("GLOBAL "+startX+" "+startY+" "+endX+" "+endY);
		System.out.println(grid.getWidth()+" "+grid.getHeight());
		System.out.println("LESSER "+startX2+" "+startY2+" "+endX2+" "+endY2);*/

		//System.out.println(grid.getWidth()+" "+grid.getHeight()+"  "+globalGrid.getWidth()+"   "+globalGrid.getHeight());
		//System.out.println(x+"  "+y+"  "+x2+" "+y2);
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
				globalGrid.initMrm(startX, endX);
				mrm = globalGrid.getMrm();
				x2 = startX2;
				y2 = startY2;
				x = startX;
				y = startY;
			}else{

				if(x2 == endX2 - 1){

					x2 = startX2;
					y2++;

					Coordinate c = grid.gridCoordinate(x2, y2);
					int[] c2 = globalGrid.gridCoordinate(c.x, c.y);
					if(c2[1] != y){
						globalSynchronisation();
						//y = c2[1];
						y = y + 1;
					}


					x = startX;

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




	private double[] scalingdouble(Double[] bounds1, Double[] bounds2){

		double[] scaled = new double[4];

		if(bounds1[0] < bounds2[0]){
			scaled[0] = bounds2[0];
		}else{
			scaled[0] = bounds1[0];

		}
		if(bounds1[1] < bounds2[1]){
			scaled[1] = bounds2[1];
		}else{
			scaled[1] = bounds1[1];

		}

		if(bounds1[2] > bounds2[2]){
			scaled[2] = bounds2[2];
		}else{
			scaled[2] = bounds1[2];

		}
		if(bounds1[3] > bounds2[3]){
			scaled[3] = bounds2[3];
		}else{
			scaled[3] = bounds1[3];

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

	public void clearAggregMap(){    
		aggregMap.clear();
	}

	public void addOperator(String name, CellAggregOperator operator){    
		aggregMap.put(name, operator);
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
		try{
			for(int i = 0; i < endX; i ++){

				CellValues cv = mrm.get(i); 
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
								d = cao.apply(values, globalGrid.getDoubleValue(name, i, y));
							}
							globalGrid.setCellValue(name, i, y, d);
							//   globalGrid.cleanOperator();

						} else{

							globalGrid.setCellValue(name, i, y, values.get((int)(Math.random() * values.size())));
							//globalGrid.clearGeomTempVal();
						}
					}
					cv.clear(name);
					//  CellValues v = mrm.get(globalGrid.getWidth());
					// v.clear(name);
				}

			}
		}catch(Exception e){
		}
	}


}

