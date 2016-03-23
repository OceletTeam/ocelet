// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   CellGraph.java

package fr.ocelet.runtime.relation.impl;

import fr.ocelet.runtime.geom.SpatialManager;
import fr.ocelet.runtime.geom.ocltypes.Line;
import fr.ocelet.runtime.geom.ocltypes.MultiLine;
import fr.ocelet.runtime.geom.ocltypes.Polygon;
import fr.ocelet.runtime.ocltypes.KeyMap;
import fr.ocelet.runtime.ocltypes.List;
import fr.ocelet.runtime.raster.CellAggregOperator;
import fr.ocelet.runtime.raster.Grid;
import fr.ocelet.runtime.raster.GridCellFactory;
import fr.ocelet.runtime.raster.GridCellManager;
import fr.ocelet.runtime.raster.GridGenerator;
import fr.ocelet.runtime.relation.*;

import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequence;
import com.vividsolutions.jts.operation.polygonize.Polygonizer;

// Referenced classes of package fr.ocelet.runtime.relation.impl:
//            CompleteIteratorCell

public abstract class CellGraph<E extends OcltEdge, Ro extends OcltRole> implements AutoGraphInterface<E, Ro>{
  
	
	 protected Grid grid;
	 protected CompleteIteratorCell<E, Ro> cellIterator;
	    
	    
	    
    public CellGraph(Grid grid){    
        this.grid = grid;
        grid.setGridCellManager(GridCellFactory.create(grid));
    }

    public CellGraph(){
    
    }

    public void setGrid(Grid grid)
    {
        this.grid = grid;
        grid.setGridCellManager(GridCellFactory.create(grid));
    }

    public void setCompleteIteratorCell(E e)
    {
        cellIterator = new CompleteIteratorCell<E, Ro>(e, this);
    }

   

    public Iterator<E> iterator()
    {
        if(cellIterator != null)
            return cellIterator;
        else
            return null;
    }

    public int size()
    {
        return grid.getWidth() * grid.getHeight();
    }

    public String toString()
    {
        return (new StringBuilder("Interaction graph (")).append(getClass().getSimpleName()).append(") contains ").append(size()).append(" edges.").toString();
    }

    public int getWidth()
    {
        return grid.getWidth();
    }

    public int getHeight()
    {
        return grid.getHeight();
    }

   

    protected void setAggregOp(String name, AggregOperator agg, boolean val)
    {
        cellIterator.setAggregOp(name, agg, val);
    }

    protected void setCellOperator(CellAggregOperator operator)
    {
        grid.setAggregOp(operator);
    }

    protected void cleanOperator()
    {
        grid.cleanOperator();
        grid.clearGeomTempVal();
    }

    protected void setMode(int mode)
    {
        grid.setMode(mode);
    }
    
  
    public E connect(Ro arg0, Ro arg1) {
    	// TODO Auto-generated method stub
    	return null;
    }

  
    public E createEdge(Ro arg0, Ro arg1) {
    	// TODO Auto-generated method stub
    	return null;
    }

  

	@Override
	public void beginTransaction() {
		
	}

	@Override
	public void endTransaction() {
		
	}

	@Override
	public void abortTransaction() {
		
	}

	public void connect() {
		
	}

	public void disconnect() {
		
	}
	
	@Override
	public void disconnect(E edge) {
		
	}

	@Override
	public void disconnect(Iterable<E> edges) {
		
	}

	protected void cleanGraph() {
		
	}

	public CellGraph<E, Ro> getComplete() {
		return this;
	}	

	protected RoleSet<Ro> getLeftSet() {
		// TODO Auto-generated method stub
		return null;
	}
	
	protected RoleSet<Ro> getRightSet() {
		// TODO Auto-generated method stub
		return null;
	}

	protected E getEdge(){
		return cellIterator.getEdge();
	}

	protected void setCellShapeType(String type){
		grid.setCellShapeType(type);
		cellIterator.setCellShapeType(type);
		
		
	}

	protected Grid createHexagon(String name, List<String> props, Double[] bounds, double size){
		return GridGenerator.hexagonalGrid(name, props, size, bounds);
		
	}

	protected Grid createHexagon(String name, List<String> props, double minX, double minY, double maxX, double maxY, double size){
		return GridGenerator.hexagonalGrid(name, props, size, minX, minY, maxX, maxY);
	}
   
	protected Grid createSquare(String name, List<String> props,  Double[] bounds, double xRes, double yRes){
		return GridGenerator.squareGrid(name, props, xRes, yRes, bounds);
	}

	protected Grid createSquare(String name, List<String> props, double minX, double minY, double maxX, double maxY, double xRes, double yRes){	
		return GridGenerator.squareGrid(name, props, xRes, yRes, minX, minY, maxX, maxY);
	}

	protected Grid createTriangle(String name, List<String> props,  Double[] bounds, double size){
		return null;
	}

	protected Grid createTriangle(String name, List<String> props, double minX, double minY, double maxX, double maxY, double size){
     	return null;
	}
	
	public Grid getGrid(){
		return grid;
	}
	
	public class RoIterator<Ro> implements Iterator<Ro>{

		
		@Override
		public boolean hasNext() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public Ro next() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void remove() {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	protected void update(Grid upGrid){		
		
		HashMap<String , Integer> bands = grid.getBands();
				
		for(int i = 0; i < grid.getWidth() - 1 ; i++){
			
			for(int j = 0; j < grid.getHeight() - 1; j ++){
				
				for(String name : bands.keySet()){
					grid.setCellValue(name, i + grid.getMinX(), j + grid.getMinY(), upGrid.getValue(name, i, j));
				}
			}
		}
	}
	
	
	
	 public KeyMap<Double[], List<Geometry>> isoLines(List<Double> range, String propertyName){
	    	
	    	KeyMap<Double[], List<Geometry>> map = new KeyMap<Double[], List<Geometry>>();	    	
	    	HashMap<Double[], Integer> indexed = new HashMap<Double[], Integer>();	    	
	    	int[][] structure = new int[grid.getWidth()][grid.getHeight()];
	    	int index = 0;
	    	for(int i = 0; i < range.size() - 2; i = i + 2){
	    		
	    		indexed.put(new Double[]{range.get(i), range.get(i + 1)}, index);
	    		
	    		index ++;
	    	}	       	
	    	
	    	for(Double[] d : indexed.keySet()){
	    		
	    		ArrayList<Line> geoms = new ArrayList<Line>();
	    	    structure =	fillStructure(structure, d, propertyName);
	    	    int[][] cells = toCells(structure);
	    	    
	    	    for(int x = 0; x < grid.getWidth() - 1 ; x ++){
	    	    	
	    	    	for(int y = 0 ; y < grid.getHeight() - 1; y ++){
	    	    		Geometry iso = isoLine(cells[x][y], x, y);
	    	    		
	    	    		if(iso != null){
	    	    			if(iso instanceof MultiLine){
	    	    				for(int i = 0; i < iso.getNumGeometries(); i ++){
	    	    					geoms.add((Line)iso.getGeometryN(i));
	    	    				}
	    	    			}else{
	    	    		geoms.add((Line)iso);
	    	    			}
	    	    		//System.out.println(iso);
	    	    		}
	    	    	}
	    	    }	    	    	    	  
	    	    
	    	  MultiLine ml = new MultiLine(geoms.toArray(new Line[geoms.size()]), SpatialManager.geometryFactory()); 
	    	//  System.out.println(ml.union());
	    	  Polygonizer polygonizer=new Polygonizer();
	    	  polygonizer.add(ml);
	    	 
	    	    
	    	   GeometryCollection gc = new GeometryCollection(geoms.toArray(new Geometry[geoms.size()]), SpatialManager.geometryFactory());
	    	   Geometry g = gc.buffer(0.0);
	    	    
	    	    //System.out.println(g);
	    	    List<Geometry> polys = new List<Geometry>();
	    	    
	    	    HashMap<Polygon, Polygon> pMap = new HashMap<Polygon, Polygon>();
	    	    
	    	    for(Object o : polygonizer.getPolygons()){
	    	    	System.out.println(o);
	    	    	if(o instanceof Geometry){
	    	    		Polygon p = (Polygon)o;
	    	    		LinearRing lr = new LinearRing(new CoordinateArraySequence(p.getExteriorRing().getCoordinates()), SpatialManager.geometryFactory());
	    	    		Polygon np = new Polygon(lr, null, SpatialManager.geometryFactory());
	    	    		pMap.put(p, np);
	    	    		polys.add((Geometry)o);
		    		}
		    	  }
	    	    
	    	    List<Geometry> temp = new List<Geometry>();
	    	    for(Geometry g1 : polys){
	    	    	temp.add(g1);
	    	    }
	    	    
	    	    for(Geometry p1 : polys){
	    	    	
	    	    	for(Geometry p2 : polys){
	    	    			    	    		
	    	    		if(!p1.equals(p2)){
	    	    			
	    	    			if(pMap.get(p1).contains(p2)){
	    	    				temp.remove(p2);
	    	    			}
	    	    			if(pMap.get(p2).contains(p1)){
	    	    				temp.remove(p1);
	    	    			}	    	    	
	    	    		}
	    	    	}
	    	    }
	    	    map.put(d, temp);	   	    	    	    
	    	}	    		    	    		  	    	
	    	return map;	    	
	    }
	 
	 private int[][] fillStructure(int[][] structure,Double[] d, String propertyName){
		 
		 for(int i = 0; i < grid.getWidth(); i ++){
			 
			 for(int j = 0; j < grid.getHeight(); j ++){
				 
				 Double val = grid.getValue(propertyName, i, j);
					
	    	    		if(val >= d[0] && val < d[1]){
	    	    		
	    	    			structure[i][j] = 1;
	    	    		}else{
	    	    			structure[i][j] = 0;
	    	     }		    	    					 
			 }		 
		}
		 return structure;
	 }
	 
	 private int[][]  toCells(int[][] structure){
	    	
	    	int[][] cells = new int[grid.getWidth() - 1][grid.getHeight() - 1];    	
	    	
	    	for(int y = 0; y < grid.getHeight() - 1; y ++){
	    		
	    			for(int x = 0; x < grid.getWidth() - 1; x ++){	    		 
    		 
	    				int finalVal = 0;	   		 
	    				int val1 = structure[x][y];
	    				int val2 = structure[x + 1][y];
	    				int val3 = structure[x + 1][y + 1];
	    				int val4 = structure[x][y  + 1];
    		                        
	    				if(val1 ==1){
	    					finalVal += 8;
	    				}
	    				if(val2 ==1){
	    					finalVal += 4;
	    				}
	    				if(val3 ==1){
	    					finalVal += 2;
	    				}
	    				if(val4 ==1){
	    					finalVal += 1;
	    				}
	    				cells[x][y] = finalVal;
        			 
    		 
	    			}
	    	}
	    		
	    	return cells;
	 }
	 
	 
	 public Geometry isoLine(int type, int x, int y){
		 
		 		
		 if(type == 1){ 			
			 Coordinate c = grid.gridCoordinate(x, y);
			 Coordinate[] coordinates = new Coordinate[]{leftCoordinate(c), downCoordinate(c)};
			 CoordinateSequence cs = new CoordinateArraySequence(coordinates);		
			 Line line = new Line(cs, SpatialManager.geometryFactory());
			 return line;			 
		 }
		 if(type == 2){				
			 Coordinate c = grid.gridCoordinate(x, y);
			 Coordinate[] coordinates = new Coordinate[]{downCoordinate(c), rightCoordinate(c)};
			 CoordinateSequence cs = new CoordinateArraySequence(coordinates);		
			 Line line = new Line(cs, SpatialManager.geometryFactory());
			 return line;
		 }
		 if(type == 3){
			 Coordinate c = grid.gridCoordinate(x, y);
			 Coordinate[] coordinates = new Coordinate[]{leftCoordinate(c), rightCoordinate(c)};
			 CoordinateSequence cs = new CoordinateArraySequence(coordinates);		
			 Line line = new Line(cs, SpatialManager.geometryFactory());
			 return line;
		 }
		 if(type == 4){
			 Coordinate c = grid.gridCoordinate(x, y);
			 Coordinate[] coordinates = new Coordinate[]{upCoordinate(c), rightCoordinate(c)};
			 CoordinateSequence cs = new CoordinateArraySequence(coordinates);		
			 Line line = new Line(cs, SpatialManager.geometryFactory());
			 return line;
		 }
		 if(type == 5){
			 Coordinate c = grid.gridCoordinate(x, y);
			 Coordinate[] coordinates = new Coordinate[]{leftCoordinate(c), upCoordinate(c)};
			 CoordinateSequence cs = new CoordinateArraySequence(coordinates);		
			 Line line = new Line(cs, SpatialManager.geometryFactory());			 
			 Coordinate[] coordinates2 = new Coordinate[]{downCoordinate(c), rightCoordinate(c)};
			 CoordinateSequence cs2 = new CoordinateArraySequence(coordinates2);		
			 Line line2 = new Line(cs2, SpatialManager.geometryFactory());			 
			 MultiLine ml = new MultiLine(new Line[]{line,  line2},SpatialManager.geometryFactory());
			 return ml;
		 }
		 if(type == 6){
			 Coordinate c = grid.gridCoordinate(x, y);
			 Coordinate[] coordinates = new Coordinate[]{upCoordinate(c), downCoordinate(c)};
			 CoordinateSequence cs = new CoordinateArraySequence(coordinates);		
			 Line line = new Line(cs, SpatialManager.geometryFactory());
			 return line;
		 }
		 if(type == 7){
			 Coordinate c = grid.gridCoordinate(x, y);
			 Coordinate[] coordinates = new Coordinate[]{leftCoordinate(c), upCoordinate(c)};
			 CoordinateSequence cs = new CoordinateArraySequence(coordinates);		
			 Line line = new Line(cs, SpatialManager.geometryFactory());
			 return line;
		 }
		 if(type == 8){
			 Coordinate c = grid.gridCoordinate(x, y);
			 Coordinate[] coordinates = new Coordinate[]{leftCoordinate(c), upCoordinate(c)};
			 CoordinateSequence cs = new CoordinateArraySequence(coordinates);		
			 Line line = new Line(cs, SpatialManager.geometryFactory());
			 return line;
		 }
		 if(type == 9){
			 Coordinate c = grid.gridCoordinate(x, y);
			 Coordinate[] coordinates = new Coordinate[]{upCoordinate(c), downCoordinate(c)};
			 CoordinateSequence cs = new CoordinateArraySequence(coordinates);		
			 Line line = new Line(cs, SpatialManager.geometryFactory());
			 return line;
		 }
		 if(type == 10){
			 Coordinate c = grid.gridCoordinate(x, y);
			 Coordinate[] coordinates = new Coordinate[]{leftCoordinate(c), downCoordinate(c)};
			 CoordinateSequence cs = new CoordinateArraySequence(coordinates);		
			 Line line = new Line(cs, SpatialManager.geometryFactory());			 
			 Coordinate[] coordinates2 = new Coordinate[]{upCoordinate(c), rightCoordinate(c)};
			 CoordinateSequence cs2 = new CoordinateArraySequence(coordinates2);		
			 Line line2 = new Line(cs2, SpatialManager.geometryFactory());			 
			 MultiLine ml = new MultiLine(new Line[]{line,  line2},SpatialManager.geometryFactory());
			 return ml;
		 }
		 if(type == 11){
			 Coordinate c = grid.gridCoordinate(x, y);
			 Coordinate[] coordinates = new Coordinate[]{upCoordinate(c), rightCoordinate(c)};
			 CoordinateSequence cs = new CoordinateArraySequence(coordinates);		
			 Line line = new Line(cs, SpatialManager.geometryFactory());
			 return line;
		 }
		 if(type == 12){
			 Coordinate c = grid.gridCoordinate(x, y);
			 Coordinate[] coordinates = new Coordinate[]{leftCoordinate(c), rightCoordinate(c)};
			 CoordinateSequence cs = new CoordinateArraySequence(coordinates);		
			 Line line = new Line(cs, SpatialManager.geometryFactory());
			 return line;
		 }
		 if(type == 13){
			 Coordinate c = grid.gridCoordinate(x, y);
			 Coordinate[] coordinates = new Coordinate[]{downCoordinate(c), rightCoordinate(c)};
			 CoordinateSequence cs = new CoordinateArraySequence(coordinates);		
			 Line line = new Line(cs, SpatialManager.geometryFactory());
			 return line;
		 }
		 if(type == 14){

			 Coordinate c = grid.gridCoordinate(x, y);
			 Coordinate[] coordinates = new Coordinate[]{leftCoordinate(c), downCoordinate(c)};
			 CoordinateSequence cs = new CoordinateArraySequence(coordinates);		
			 Line line = new Line(cs, SpatialManager.geometryFactory());
			 return line;
		 }
		return null;
 
	 }
	 
	 private Coordinate upCoordinate(Coordinate c){
		 return new Coordinate(c.x + (grid.getXRes() / 2) - (2 * grid.getXRes()), c.y + (2 * grid.getYRes()));
	 }
	 
	 private Coordinate downCoordinate(Coordinate c){
		 return new Coordinate(c.x + (grid.getXRes() / 2)- (2 * grid.getXRes()), c.y - grid.getYRes() + (2 * grid.getYRes()));
	 }
 
	 private Coordinate rightCoordinate(Coordinate c){
		 return new Coordinate(c.x + (grid.getXRes())- (2 * grid.getXRes()), c.y - (grid.getYRes() / 2)+ (2 * grid.getYRes()));
	 }
 
	 private Coordinate leftCoordinate(Coordinate c){
		 return new Coordinate(c.x- (2 * grid.getXRes()), c.y - (grid.getYRes() / 2)+ (2 * grid.getYRes()));

	 }
	 
	 public void clearData(){
		 grid = null;
	 }
	 
	 public void extendedMoore(int n){
		 cellIterator.extendedMoore(n);
	 }
	 public void extendedCircularMoore(int n){
		 cellIterator.extendedCircularMoore(n);
	 }
	 
	 public int getSize(){
		 return grid.getWidth() * grid.getHeight();
	 }
	
}
