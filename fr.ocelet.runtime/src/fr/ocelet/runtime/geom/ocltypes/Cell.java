package fr.ocelet.runtime.geom.ocltypes;

import org.geotools.coverage.grid.GridCoordinates2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.opengis.geometry.DirectPosition;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequence;
import com.vividsolutions.jts.operation.valid.IsValidOp;

import fr.ocelet.runtime.geom.OceletGeomFactory;
import fr.ocelet.runtime.geom.SpatialManager;
import fr.ocelet.runtime.raster.Grid;
import fr.ocelet.runtime.raster.GridManager;


// Referenced classes of package fr.ocelet.runtime.geom.ocltypes:
//            SpatialType

public class Cell implements SpatialType{
   
	
	  private int x;
	    private int y;
	    private String type = "QUADRILATERAL";
	    private int numGrid;
	    private double xRes; 
	    private double yRes;
	    
	    

    public Cell()
    {
    }

    public Cell(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }
    
    public void set(int x, int y){
    	this.x = x;
    	this.y = y;
    }

    public void setType(String type){
    	this.type = type;
    }
    
    public void setNumGrid(int numGrid){
    	this.numGrid = numGrid;
    }
  
    
    public GridGeometry2D getGridGeometry(){
    	return GridManager.getInstance().get(numGrid).getGridGeometry();
    }
    
    public int getNumGrid(){
    	return numGrid;
    }
   
    private Coordinate[] createCoordinates(){
    	
    	
    	if(type.equals("HEXAGONAL")){
    		return hexagonalCoordinate();
    	}
    	if(type.equals("QUADRILATERAL")){
    		return quadriCoordinate();
    	}
    	return null;
    }
    
    private Coordinate getCoord(int x, int y){
    	return GridManager.getInstance().get(numGrid).gridCoordinate(x, y);
    }
    
    
    private Coordinate initCoord(){
    	
    	return GridManager.getInstance().get(numGrid).getInitCoordinate();
    }
    
   private Coordinate[] quadriCoordinate(){
	   
	
	   Coordinate c = getCoord(x, y);
   	double dx = c.x;
   	double dy = c.y;
   	//System.out.println("INIT COORD "+initCoord());
   	Coordinate[] coords= new Coordinate[5];
	coords[0] = new Coordinate(dx + xRes / 2, dy + yRes / 2);
	coords[1] = new Coordinate(dx + xRes / 2, dy - yRes / 2);
	coords[2] = new Coordinate(dx - xRes / 2, dy - yRes / 2);
	coords[3] = new Coordinate(dx - xRes / 2, dy + yRes / 2);
	coords[4] = new Coordinate(dx + xRes / 2, dy + yRes / 2);
	
	return coords;
   	
   }
    private Coordinate[] hexagonalCoordinate(){
    	
    	
    	Coordinate c = getCoord(x, y);
    	double dx = c.x;
    	double dy = c.y;
    	
    	
    	Coordinate[] coords= new Coordinate[7];
    	

    	
  	double l = Math.sin(Math.PI/3) * (xRes / 2);
    	
    	coords[0] = new Coordinate(dx + xRes / 2, dy);
    	coords[1] = new Coordinate(dx + xRes/4, dy - l);
    	coords[2] = new Coordinate(dx - xRes/4, dy- l);
    	coords[3] = new Coordinate(dx - xRes/2, dy);
    	coords[4] = new Coordinate(dx - xRes/4, dy + l);
    	coords[5] = new Coordinate(dx + xRes/4, dy + l);
    	coords[6] = new Coordinate(dx + xRes/2, dy);
    	
    	
    	return coords;
    	
    	
    }
    
    public Polygon toPolygon(){
    	
    	
    	CoordinateSequence cs = new CoordinateArraySequence(createCoordinates());
    	
    	LinearRing lr = new LinearRing(cs,SpatialManager.geometryFactory());
    	
    	Polygon poly = new Polygon(lr, null,SpatialManager.geometryFactory());

    	return poly;
    	
    }
    
    public void updateResInfo(){
    	
    	xRes = GridManager.getInstance().get(numGrid).getXRes();
    	yRes = GridManager.getInstance().get(numGrid).getYRes();
    }
    
    public Double distance(Geometry geom){
    	Coordinate c = getCoord(x, y);
    	return geom.distance(Point.xy(c.x, c.y));
    }
    
    public Point getCentroid(){
    	Coordinate c = getCoord(x, y);
    	return Point.xy(c.x, c.y);

    }
    public Double distance(Cell cell){
    	return getCentroid().distance(cell.getCentroid());
    }
}