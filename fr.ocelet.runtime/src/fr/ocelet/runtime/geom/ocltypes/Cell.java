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



// Referenced classes of package fr.ocelet.runtime.geom.ocltypes:
//            SpatialType

public class Cell implements SpatialType{
   
	private Grid grid;
	  private int x;
	    private int y;
	    private String type = "QUADRILATERAL";
	   
	    private double xRes; 
	    private double yRes;
	    
	    public Grid getGrid() {
	    	return grid;
	    }
	    
	    public void setGrid(Grid grid) {
	    	this.grid = grid;
	    	this.xRes = grid.getXRes();
	    	this.yRes = grid.getYRes();
	    	this.type = grid.getCellShapeType();
	    }

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
    
    
  
    
    public GridGeometry2D getGridGeometry(){
    	return grid.getGridGeometry();
    }
    
   
   
    private Coordinate[] createCoordinates(){
    	
    	if(type == null){
    		type = grid.getCellShapeType();
    		//return quadriCoordinate();
    	}
    	if(type.equals("QUADRILATERAL")){
    		return quadriCoordinate();

    	}
    	if(type.equals("HEXAGONAL")){
    		return hexagonalCoordinate();
    	}
    	
    	if(type.equals("TRIANGULAR")){
    		return triangularCoordinate();
    	}
    	return null;
    }
    
    private Coordinate getCoord(int x, int y){
    	return grid.gridCoordinate(x, y);
    }
    
    
    private Coordinate initCoord(){
    	
    	return grid.getInitCoordinate();
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
    	

    	
  	//double l = Math.sin(Math.PI/3) * (xRes / 2);
    	
    	/*coords[0] = new Coordinate(dx + xRes / 2, dy);
    	coords[1] = new Coordinate(dx + xRes/4, dy - yRes);
    	coords[2] = new Coordinate(dx - xRes/4, dy- yRes);
    	coords[3] = new Coordinate(dx - xRes/2, dy);
    	coords[4] = new Coordinate(dx - xRes/4, dy + yRes);
    	coords[5] = new Coordinate(dx + xRes/4, dy + yRes);
    	coords[6] = new Coordinate(dx + xRes/2, dy);*/
    	
    	coords[0] = new Coordinate(dx + xRes, dy);
    	coords[1] = new Coordinate(dx + xRes/2, dy - yRes / 2);
    	coords[2] = new Coordinate(dx - xRes/2, dy- yRes / 2);
    	coords[3] = new Coordinate(dx - xRes, dy);
    	coords[4] = new Coordinate(dx - xRes/2, dy + yRes / 2);
    	coords[5] = new Coordinate(dx + xRes/2, dy + yRes / 2);
    	coords[6] = new Coordinate(dx + xRes, dy);

    	return coords;
    	
    	
    }
    
    
    private Coordinate[] triangularCoordinate(){
    		
    	if(x % 2 == 0){
    		if(y % 2 == 0){
    			return triangularDownCoordinate();
    		}else{
    			return triangularTopCoordinate();
    		}
    	}else{
    		if(y % 2 == 0){
    			return triangularTopCoordinate();
    		}else{
    			return triangularDownCoordinate();
    		}
    	}
    }
    
    private Coordinate[] triangularTopCoordinate(){
    	
    	Coordinate[] coords = new Coordinate[4];
    	
    	
    	Coordinate c = getCoord(x, y);
    	double dx = c.x;
    	double dy = c.y;
    	double sqr = Math.sqrt(3);
    	double h = xRes * sqr / 6;
		double h2 = xRes * sqr / 3;

		coords[0] = new Coordinate(dx, dy + h2);
		coords[1] = new Coordinate(dx + xRes / 2, dy - h);
		coords[2] = new Coordinate(dx - xRes / 2, dy- h);
    	coords[3] =  coords[0];

    	return coords;
    }
    
    private Coordinate[] triangularDownCoordinate(){
    	
    	Coordinate[] coords = new Coordinate[4];
    	
    	
    	Coordinate c = getCoord(x, y);
    double dx = c.x;
    	double dy = c.y;
    	double sqr = Math.sqrt(3);
    	double h = xRes * sqr / 6;
		double h2 = xRes * sqr / 3;
		coords[0] = new Coordinate(dx, dy - h2);
		coords[1] = new Coordinate(dx + xRes / 2, dy + h);
		coords[2] = new Coordinate(dx - xRes / 2, dy +  h);
    	coords[3] =  coords[0];

    	return coords;
    }
    
    public Polygon toPolygon(){
    	
    	
    	CoordinateSequence cs = new CoordinateArraySequence(createCoordinates());
    	
    	LinearRing lr = new LinearRing(cs,SpatialManager.geometryFactory());
    	
    	Polygon poly = new Polygon(lr, null,SpatialManager.geometryFactory());

    	return poly;
    	
    }
    
    public void updateResInfo(){
    	
    	xRes = grid.getXRes();
    	yRes = grid.getYRes();
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