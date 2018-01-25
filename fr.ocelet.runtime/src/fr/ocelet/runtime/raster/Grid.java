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

package fr.ocelet.runtime.raster;

import com.sun.media.jai.codecimpl.util.RasterFactory;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequence;
import com.vividsolutions.jts.index.quadtree.Key;

import fr.ocelet.runtime.geom.SpatialManager;
import fr.ocelet.runtime.geom.ocltypes.Cell;
import fr.ocelet.runtime.geom.ocltypes.Line;
import fr.ocelet.runtime.geom.ocltypes.MultiLine;
import fr.ocelet.runtime.geom.ocltypes.MultiPoint;
import fr.ocelet.runtime.geom.ocltypes.MultiPolygon;
import fr.ocelet.runtime.geom.ocltypes.Point;
import fr.ocelet.runtime.geom.ocltypes.Polygon;
import fr.ocelet.runtime.ocltypes.KeyMap;
import fr.ocelet.runtime.ocltypes.List;
import fr.ocelet.runtime.relation.CellValues;

import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.coverage.grid.GridCoordinates2D;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.InvalidGridGeometryException;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.Envelope2D;
import org.opengis.geometry.DirectPosition;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.NoninvertibleTransformException;
import org.opengis.referencing.operation.TransformException;

/* Grid class used to iterate on raster values */

public class Grid {

	protected GridGeometry2D gridGeometry;
	protected int minX;
	protected int minY;
	protected int maxX;
	protected int maxY;
	protected int width;
	protected int height;
	protected double xRes;
	protected double yRes;
	protected int totalY;
	protected int mode;
	protected static int modeTemp = 0;
	protected static int modeNorm = 1;
	protected static int modeGeom = 2;
	protected WritableRaster raster;
	//  protected WritableRaster initRaster;
	//protected HashMap<String, Integer> initRasterProps = new HashMap<String, Integer>();
	protected HashMap<String, Integer> rasterProps = new HashMap<String, Integer>();
	protected GridCellManager gridCellManager;
	protected MultiResolutionManager mrm;
	protected HashMap<String, List<Double>> geomTempVal = new HashMap<String, List<Double>>();
	protected String cellShapeType = "QUADRILATERAL";
	protected Coordinate initCoordinates;
	protected Double[] worldBounds;
	private String name;
	public Envelope2D env;

	private TypeSetter ts;
	private TempSetter tempSetter = new TempSetter();
	private NormSetter normSetter = new NormSetter();
	private GeomSetter geomSetter = new GeomSetter();
	private MrmSetter mrmSetter = new MrmSetter();
	private Double[] worldBoundsPrime;
	private CoordinateReferenceSystem crs;
	
	public void flush() {
		raster = null;
		gridCellManager = null;
		geomTempVal = null;
	}
	public String getName() {
		return name;
	}

	public void setFinalProperties(List<String> props){
		ArrayList<Integer> indexes = new ArrayList<Integer>();
		for(String name : rasterProps.keySet()){
			indexes.add(rasterProps.get(name));
		}
		int index = 0;
		for(String name : props){
			if(!rasterProps.keySet().contains(name))
			{
				rasterProps.put(name, index);
				index++;
			}else{
				index++;
			}
		}      
	}

	public void setCRS(CoordinateReferenceSystem crs) {
		this.crs = crs;
	}
	
	public CoordinateReferenceSystem getCRS() {
		return crs;
	}
	public int getPropBand(String name){
		return ((Integer)rasterProps.get(name)).intValue();
	}

	public Grid(String name,int width, int height, GridGeometry2D gridGeometry){

		rasterProps = new HashMap<String, Integer>();
		this.name = name;
		this.gridGeometry = gridGeometry;
		this.width = width;
		this.height = height;
		this.minX = 0;
		this.minY = 0;
		this.maxX = width - 1;
		this.maxY = height - 1;
		this.ts = normSetter;
		setInitCoordinate();        
	}

	public Grid(int minX, int minY, int maxX, int maxY, GridGeometry2D gridGeometry2D)
	{
		this.ts = normSetter;
		rasterProps = new HashMap<String, Integer>();      
		gridGeometry = gridGeometry2D;

		this.minX = minX;
		this.minY = minY;
		this.maxX = maxX;
		this.maxY = maxY;

		width = maxX - minX;
		height = maxY - minY;

		setInitCoordinate();
	}    

	public Grid(){
		this.ts = normSetter;

	}

	public Grid(double minX, double minY, double maxX, double maxY, int totalY, GridGeometry2D gridGeometry2D){
		this.ts = normSetter;
		rasterProps = new HashMap<String, Integer>();     
		gridGeometry = gridGeometry2D;
		worldBounds = new Double[]{minX, minY, maxX, maxY};

		int minbounds[] = gridCoordinate(minX, minY);
		int maxbounds[] = gridCoordinate(maxX, maxY);

		this.minX = minbounds[0];
		this.minY = minbounds[1];
		this.maxX = maxbounds[0];
		this.maxY = maxbounds[1];

		width = this.maxX - this.minX;
		height = this.maxY - this.minY;
		setInitCoordinate();
	}

	private Double[] scale(ORaster raster) {
		
		Double[] rBounds = raster.getWorldBounds();
		Double[] scaled = new Double[4];
		/*if(worldBounds[0] < rBounds[0]) {
			scaled[0] = rBounds[0];
		}else {
			scaled[0] = worldBounds[0];
		}
		
		if(worldBounds[1] < rBounds[1]) {
			scaled[1] = rBounds[1];
		}else {
			scaled[1] = worldBounds[1];
		}
		
		if(worldBounds[2] > rBounds[2]) {
			scaled[2] = rBounds[2];
		}else {
			scaled[2] = worldBounds[2];
		}
		
		if(worldBounds[3] > rBounds[3]) {
			scaled[3] = rBounds[3];
		}else {
			scaled[3] = worldBounds[3];
		}*/
		if(worldBounds[0] < rBounds[0]) {
			scaled[0] = rBounds[0];
		}else {
			scaled[0] = worldBounds[0];
		}
		
		if(worldBounds[1] < rBounds[1]) {
			scaled[1] = rBounds[1];
		}else {
			scaled[1] = worldBounds[1];
		}
		
		if(worldBounds[2] > rBounds[2]) {
			scaled[2] = rBounds[2];
		}else {
			scaled[2] = worldBounds[2];
		}
		
		if(worldBounds[3] > rBounds[3]) {
			scaled[3] = rBounds[3];
		}else {
			scaled[3] = worldBounds[3];
		}
		return scaled;
	}
	public void  copy(ORaster raster, KeyMap<String, Integer> matchedBand){
		
		
		Double[] scaled = scale(raster);
	
		
	
		
		int[] gridMin = gridCoordinate(scaled[0] + xRes / 2, scaled[3] - yRes / 2);
		int[] gridMax = gridCoordinate(scaled[2] - xRes / 2, scaled[1] + yRes / 2);
	
		int[] rasterMin = raster.worldToGrid(scaled[0] + xRes / 2, scaled[3] - yRes / 2);
		int[] rasterMax = raster.worldToGrid(scaled[2], scaled[1]);
		
		
	
		int diffX = rasterMin[0] - gridMin[0];
		int diffY = rasterMin[1] - gridMin[1];

		
		
	
		int w = gridMax[0] - gridMin[0];
		int h = gridMax[1] - gridMin[1];
		
		int maxX = gridMax[0] + 1;
		int maxY = gridMax[1] + 1;
	
		/*System.out.println(width + " "+height+" "+raster.getGridWidth()+" "+raster.getGridHeight());
		printWorldBounds();
		raster.printWorldBounds();*/
		/*if(w < width  && w < raster.getGridWidth()  ) {
			maxX ++;
		}
		if(h < height  && h < raster.getGridHeight() ) {
			maxY ++;
		}*/

		
		for(int i = gridMin[0]; i < maxX; i ++){
			for(int j = gridMin[1]; j < maxY; j ++){
				for(String name : matchedBand.keySet()){
					//try{
						//System.out.println(i +" "+j+" "+(i + diffX)+" "+(j + diffY));
						/*Coordinate worlds = this.gridCoordinate(i, j);
						int[] convert = raster.worldToGrid(worlds.x, worlds.y);
						this.raster.setSample(i, j, rasterProps.get(name), raster.getDoubleValue(convert[0], convert[1], matchedBand.get(name)));*/
						this.raster.setSample(i, j, rasterProps.get(name), raster.getDoubleValue(i + diffX, j + diffY , matchedBand.get(name)));
						
					//}catch (Exception e){
					//	System.out.println(i +" "+j+" "+(i+diffX)+" "+(j+diffY));
						
						//e.printStackTrace();
					//}
				}    			
			}
		}
	}
	
	public void printWorldBounds() {
		System.out.println(worldBounds[0]+ " "+worldBounds[1]+ " "+worldBounds[2]+ " "+worldBounds[3]);
	}
	
	public Grid(Double bounds[], GridGeometry2D gridGeometry, ORaster raster){
		this.ts = normSetter;
		//initRasterProps = new HashMap<String, Integer>();
		rasterProps = new HashMap<String, Integer>();
		
		this.gridGeometry = gridGeometry;
		int iBounds[] = intBounds(bounds);
		minX = iBounds[0];
		minY = iBounds[1];
		maxX = iBounds[2];
		maxY = iBounds[3];   
		worldBounds =bounds;
		xRes = raster.getXRes();
		yRes = raster.getYRes();
		width = (int)(Math.round( (bounds[2] - bounds[0]) / xRes ));
		height = (int)(Math.round( (bounds[2] - bounds[0]) / xRes ));
		width = maxX - minX;
		height = maxY - minY;
		setInitCoordinate();

	}

	public Coordinate getInitCoordinate(){
		return initCoordinates;
	}

	public void setInitCoordinate(){
		DirectPosition dp = null;
		try {
			dp = gridGeometry.gridToWorld(new GridCoordinates2D(minX, maxY - 1));
		} catch (TransformException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		initCoordinates =  new Coordinate(dp.getCoordinate()[0], dp.getCoordinate()[1]);
	}
	public void setRes(ORaster raster){

		xRes = raster.getXRes();
		yRes = raster.getYRes();
	}

	private Coordinate[] triangularCoordinate(int x, int y){
		if(x % 2 == 0){
			if(y % 2 == 0){
				return triangularDownCoordinate(x, y);
			}else{
				return triangularTopCoordinate(x, y);
			}
		}else{
			if(y % 2 == 0){
				return triangularTopCoordinate(x, y);
			}else{
				return triangularDownCoordinate(x, y);
			}
		}
	}

	private Coordinate[] triangularTopCoordinate(int x, int y){

		Coordinate[] coords = new Coordinate[4];
		DirectPosition dp = null;
		try {
		 dp = gridGeometry.gridToWorld(new GridCoordinates2D(x, y));
		} catch (TransformException e) {
			// TODO Auto-generated catch block
			return null;
			
			
		}
		/*Coordinate c = gridCoordinate(x, y);
		double dx = c.x;
		double dy = c.y;*/
		
		Coordinate c = new Coordinate(dp.getCoordinate()[0], dp.getCoordinate()[1]);
		c = gridCoordinate(x, y);
		
		//double dx = ((c.x - initCoordinates.x) / 2) + initCoordinates.x;// -xRes;
		double dx = (c.x + initCoordinates.x + (xRes / 2)) / 2;
		dx =  worldBounds[0]+ (((c.x - worldBounds[0]) * (worldBounds[2] - worldBounds[0])) / (worldBoundsPrime[2] - worldBounds[0]));
		dx = c.x;
		//double dx = c.x;
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

	private Coordinate[] triangularDownCoordinate(int x, int y){

		Coordinate[] coords = new Coordinate[4];

		//Coordinate c = gridCoordinate(x, y);
				
		
		DirectPosition dp = null;
		try {
		 dp = gridGeometry.gridToWorld(new GridCoordinates2D(x, y));
		} catch (TransformException e) {
			// TODO Auto-generated catch block
			return null;
		}
		/*Coordinate c = gridCoordinate(x, y);
		double dx = c.x;
		double dy = c.y;*/
		
		Coordinate c = new Coordinate(dp.getCoordinate()[0], dp.getCoordinate()[1]);
		c = gridCoordinate(x, y);
		if(c == null) {
			return null;
		}
		//double dx = ((c.x - initCoordinates.x) / 2) + initCoordinates.x;// - xRes;
		double dx = (c.x + initCoordinates.x + (xRes / 2)) / 2;
		dx = worldBounds[0] + (((c.x - worldBounds[0]) * (worldBounds[2] - worldBounds[0])) / (worldBoundsPrime[2] - worldBounds[0]));
		dx = c.x;
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

	private Coordinate[] hexagonalCoordinate(int x, int y){
		Coordinate c = gridCoordinate(x, y);
		if(c == null) {
			return null;
		}
		double dx = c.x;
		double dy = c.y;
		Coordinate[] coords= new Coordinate[7];
		coords[0] = new Coordinate(dx + xRes, dy);
		coords[1] = new Coordinate(dx + xRes/2, dy - yRes / 2);
		coords[2] = new Coordinate(dx - xRes/2, dy- yRes / 2);
		coords[3] = new Coordinate(dx - xRes, dy);
		coords[4] = new Coordinate(dx - xRes/2, dy + yRes / 2);
		coords[5] = new Coordinate(dx + xRes/2, dy + yRes / 2);
		coords[6] = new Coordinate(dx + xRes, dy);
		return coords;
	}

	private int[] triangularWorldToGrid(double x, double y){
		
		int[] gCoord = new int[2];
		GridCoordinates2D gc = null;
		
		double xP = worldBounds[0] + (((x - worldBounds[0]) * (worldBoundsPrime[2] - worldBounds[0])) / (worldBounds[2] - worldBounds[0]))
				-  xRes / 2;
		
		//xP =((worldBoundsPrime[2] - worldBounds[0]) * x) / (worldBounds[2] - worldBounds[0]);
		try{

			gc = gridGeometry.worldToGrid(new DirectPosition2D(xP, y ));
		}catch(TransformException ex){

			return null;
			// Logger.getLogger(fr/ocelet/runtime/raster/Grid.getName()).log(Level.SEVERE, null, ex);
		}
		
		
		gCoord[0] = gc.x;
		/*if(gc.x > 2){
			gCoord[0] = gc.x - 2;
		}*/
		gCoord[1] = gc.y;
		Coordinate[] coord = new Coordinate[]{new Coordinate(x, y)};
		CoordinateSequence pointSequence = new CoordinateArraySequence(coord);
		Point point = new Point(pointSequence, SpatialManager.geometryFactory());


		Coordinate[] coords = triangularCoordinate(gCoord[0], gCoord[1]);
		CoordinateSequence cs = new CoordinateArraySequence(coords);
		LinearRing lr = new LinearRing(cs,SpatialManager.geometryFactory());
		Polygon poly = new Polygon(lr, null,SpatialManager.geometryFactory());
		//return gCoord;
		if(poly.contains(point) || poly.touches(point)){
			return gCoord;
		}else{
			if(gCoord[0] - 1 >= 0 && gCoord[1] < height){
				if(x < poly.getCentroid().getX())
					return new int[]{gCoord[0] - 1, gCoord[1]};

							}
			if(gCoord[0] + 1 < width && gCoord[1] < height){
				if(x > poly.getCentroid().getX())
					return new int[]{gCoord[0] + 1, gCoord[1]};

			}
		}
		return gCoord;
		//return null;
	}
		/*coords = triangularCoordinate(gCoord[0] - 1, gCoord[1]);
				cs = new CoordinateArraySequence(coords);
				lr = new LinearRing(cs,SpatialManager.geometryFactory());
				poly = new Polygon(lr, null,SpatialManager.geometryFactory());
				
				if(poly.contains(point)){
					return new int[]{gCoord[0] - 1, gCoord[1]};
				}*/

	private int[] hexagonalWorldToGrid(double x, double y){

		int[] gCoord = new int[2];
		GridCoordinates2D gc = null;
		try{
			gc = gridGeometry.worldToGrid(new DirectPosition2D(x, y ));
		}catch(TransformException ex){
			//ex.printStackTrace();
			// Logger.getLogger(fr/ocelet/runtime/raster/Grid.getName()).log(Level.SEVERE, null, ex);
		}
		if(gc == null)
			return null;
		gCoord[0] = gc.x;
		gCoord[1] = gc.y;
		Coordinate[] coord = new Coordinate[]{new Coordinate(x, y)};
		CoordinateSequence pointSequence = new CoordinateArraySequence(coord);
		Point point = new Point(pointSequence, SpatialManager.geometryFactory());

		if(gCoord[0] % 2 == 0){

			Coordinate[] coords = hexagonalCoordinate(gCoord[0], gCoord[1]);
	
			CoordinateSequence cs = null;
			LinearRing lr = null;
			Polygon poly = null;
			
			if(coords != null) {
				 cs = new CoordinateArraySequence(coords);
				 lr = new LinearRing(cs,SpatialManager.geometryFactory());
				 poly = new Polygon(lr, null,SpatialManager.geometryFactory());
				if(poly.contains(point) || poly.touches(point)){
					return gCoord;
				}
			}
			if(gCoord[0] - 1 >= 0){
					coords = hexagonalCoordinate(gCoord[0] - 1, gCoord[1]);
					
					if(coords != null) {
						cs = new CoordinateArraySequence(coords);
	
						lr = new LinearRing(cs,SpatialManager.geometryFactory());
						
						poly = new Polygon(lr, null,SpatialManager.geometryFactory());
						if(poly.contains(point) || poly.touches(point)){
							return new int[]{gCoord[0] - 1, gCoord[1]};
						}
					}
				}
				if(gCoord[0] - 1 >= 0 && gCoord[1] - 1 >= 0){
					
				
					coords = hexagonalCoordinate(gCoord[0] - 1, gCoord[1] - 1);
					if(coords != null) {
						cs = new CoordinateArraySequence(coords);
	
						lr = new LinearRing(cs,SpatialManager.geometryFactory());
	
						poly = new Polygon(lr, null,SpatialManager.geometryFactory());
						if(poly.contains(point) || poly.touches(point)){
							return new int[]{gCoord[0] - 1, gCoord[1] - 1};
						}
					}
				}
			
			return null;

		}else{

			Coordinate[] coords = hexagonalCoordinate(gCoord[0], gCoord[1]);
		
			CoordinateSequence cs = null;
			LinearRing lr = null;
			Polygon poly = null;
			
			if(coords != null) {
			 cs = new CoordinateArraySequence(coords);

			 lr = new LinearRing(cs,SpatialManager.geometryFactory());

			 poly = new Polygon(lr, null,SpatialManager.geometryFactory());
				if(poly.contains(point) || poly.touches(point)){
					return new int[]{gCoord[0], gCoord[1]};
				}
			}
			if(gCoord[0] -1 >=0 && gCoord[1] < height - 1){
				coords = hexagonalCoordinate(gCoord[0] - 1, gCoord[1]);
				
				if(coords != null) {
				cs = new CoordinateArraySequence(coords);

				lr = new LinearRing(cs,SpatialManager.geometryFactory());

				poly = new Polygon(lr, null,SpatialManager.geometryFactory());
				if(poly.contains(point)|| poly.touches(point)){
					return new int[]{gCoord[0] - 1, gCoord[1]};
				}
				}

			}
			if(gCoord[1] - 1 >= 0 ){
				coords = hexagonalCoordinate(gCoord[0], gCoord[1] - 1);
				if(coords != null) {
				
				cs = new CoordinateArraySequence(coords);

				lr = new LinearRing(cs,SpatialManager.geometryFactory());

				poly = new Polygon(lr, null,SpatialManager.geometryFactory());
				if(poly.contains(point) || poly.touches(point)){
					return new int[]{gCoord[0], gCoord[1] - 1};
				}
				}
			}
			return null;	
		}
	}


	public int[] gridCoordinate(double x, double y){
		
		if(x > worldBounds[0] && y > worldBounds[1] && x < worldBounds[2] && y < worldBounds[3]) {
		int gCoord[] = new int[2];
		if(cellShapeType.equals("HEXAGONAL")){
			return hexagonalWorldToGrid(x, y);
		}
		if(cellShapeType.equals("TRIANGULAR")){
			return triangularWorldToGrid(x, y);

		}
		try{

			// GridCoordinates2D gc = gridGeometry.worldToGrid(new DirectPosition2D(x - (2 * xRes), y + (2 * yRes)));
			GridCoordinates2D gc = gridGeometry.worldToGrid(new DirectPosition2D(x, y ));
			
			gCoord[0] = gc.x;
			gCoord[1] = gc.y;
		} catch(TransformException ex) {
			return null;
			//ex.printStackTrace();
			// Logger.getLogger(fr/ocelet/runtime/raster/Grid.getName()).log(Level.SEVERE, null, ex);
		}
		return gCoord;
		}
		return null;
	}
	private Coordinate hexagonalGridToWorld(int x, int y){
		double dx = initCoordinates.x;
		double dy = initCoordinates.y;

		DirectPosition dc = null;
		
		if(x < minX || x > maxX || y < minY || y > maxY) {
			return null;
		}
		try {
			dc = gridGeometry.gridToWorld(new GridCoordinates2D(x, y));
		} catch (InvalidGridGeometryException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (TransformException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		if(dc == null) {
			return null;
		}
		double dist = xRes - (xRes / 2 + xRes / 4);

		if((x%2) == 0){

			dx = dc.getCoordinate()[0] + dist;
			dy = dc.getCoordinate()[1];
			/*dx = dx + ((x  * xRes )) + ((x  * xRes )/ 2);
    	    		dy = dy + (2 * y  * yRes);*/

		}else{
			dx = dc.getCoordinate()[0] + dist;
			dy = dc.getCoordinate()[1] - yRes / 2;


			/*dx = dx + ((3 *x * xRes) / 2);
    	    		dy = dy + (2 * yRes*y ) + yRes ;*/
		}
		return new Coordinate(dx, dy);

	}
	private Coordinate triangularGridToWorld(int x, int y){
		double xInit = initCoordinates.x;
		double yInit = worldBounds[3];//- ((Math.sqrt(3) * xRes ) / 6);;

		double dx = (x * xRes/2) + xRes/2;
		double dy = yInit;//0.0;
		if(x % 2 == 0){ 
			if( y % 2 == 0){
				dy = y * (yRes) + ((Math.sqrt(3) * xRes ) / 6);
			}else{
				dy = y * (yRes) + ((Math.sqrt(3) * xRes ) / 3);
			}
		}else{
			if( y % 2 == 0){
				dy = y * (yRes) + ((Math.sqrt(3) * xRes ) / 3);
			}else{
				dy = y * (yRes) + ((Math.sqrt(3) * xRes ) / 6);
			}


		}
		dx = dx+ xInit;
		dy = yInit - dy;
		return new Coordinate(dx, dy);

	}
	
	public void printGridBounds() {
		System.out.println(minX+" "+minY+" "+maxX+" "+maxY);
	}

	public Coordinate gridCoordinate(int x, int y){

		if(cellShapeType.equals("HEXAGONAL")){
			return hexagonalGridToWorld(x, y);
		}
		if(cellShapeType.equals("TRIANGULAR")){
			return triangularGridToWorld(x, y);
		}
		DirectPosition dp = null;
		try{
		 dp = gridGeometry.gridToWorld(new GridCoordinates2D(x, y));
			
		}
		catch(TransformException ex){
			//return null;
			//ex.printStackTrace();
			// Logger.getLogger(fr/ocelet/runtime/raster/Grid.getName()).log(Level.SEVERE, null, ex);
		}
		if(dp != null)
			return new Coordinate(dp.getCoordinate()[0], dp.getCoordinate()[1]);
		return null;
	}

	public Coordinate[] cboundary(Polygon polygon){
		Coordinate bounds[] = new Coordinate[2];
		double minX = Double.POSITIVE_INFINITY;
		double minY = Double.POSITIVE_INFINITY;
		double maxX = Double.NEGATIVE_INFINITY;
		double maxY = Double.NEGATIVE_INFINITY;
		Coordinate acoordinate[];
		int j = (acoordinate = polygon.getExteriorRing().getCoordinates()).length;
		for(int i = 0; i < j; i++)
		{
			Coordinate c = acoordinate[i];
			if(c.x < minX)
				minX = c.x;
			if(c.x > maxX)
				maxX = c.x;
			if(c.y < minY)
				minY = c.y;
			if(c.y > maxY)
				maxY = c.y;
		}

		bounds[0] = new Coordinate(minX, minY);
		bounds[1] = new Coordinate(maxX, maxY);
		return bounds;
	}

	public Coordinate[] cboundary(Line line){
		Coordinate bounds[] = new Coordinate[2];
		double minX = Double.POSITIVE_INFINITY;
		double minY = Double.POSITIVE_INFINITY;
		double maxX = Double.NEGATIVE_INFINITY;
		double maxY = Double.NEGATIVE_INFINITY;
		Coordinate acoordinate[];
		int j = (acoordinate = line.getCoordinates()).length;
		for(int i = 0; i < j; i++){
			Coordinate c = acoordinate[i];
			if(c.x < minX)
				minX = c.x;
			if(c.x > maxX)
				maxX = c.x;
			if(c.y < minY)
				minY = c.y;
			if(c.y > maxY)
				maxY = c.y;
		}

		bounds[0] = new Coordinate(minX, minY);
		bounds[1] = new Coordinate(maxX, maxY);
		return bounds;
	}

	public int[] intBounds(Polygon polygon){
		int bounds[] = new int[4];
		Coordinate cBounds[] = cboundary(polygon);
		
		
		int[] gridCoord1 = null;
		int[] gridCoord2 = null;
		
		try {
		gridCoord1 = gridCoordinate(cBounds[0].x, cBounds[1].y);
		}catch(Exception e) {
			
		}
		
		try {
		gridCoord2 = gridCoordinate(cBounds[1].x, cBounds[0].y);
		}catch(Exception e) {
			
		}
		if(gridCoord1 == null){
			bounds[0] = 0;
			bounds[1] = 0; 
		}else{

			bounds[0] = gridCoord1[0];

			bounds[1] = gridCoord1[1];
		}

		if(gridCoord2 == null){
			bounds[2] = width - 1;
			bounds[3] = height - 1; 
		}else{


			bounds[2] = gridCoord2[0];
			bounds[3] = gridCoord2[1];
		}

		
		return bounds;
	}

	public int[] intBounds(Line line){
		int bounds[] = new int[4];
		Coordinate cBounds[] = cboundary(line);
		int gridCoord1[] = gridCoordinate(cBounds[0].x, cBounds[0].y);
		int gridCoord2[] = gridCoordinate(cBounds[1].x, cBounds[1].y);
		bounds[0] = gridCoord1[0];
		bounds[1] = gridCoord2[1];
		bounds[2] = gridCoord2[0];
		bounds[3] = gridCoord1[1];
		return bounds;
	}

	public int reverse(int y){
		return height - y;
	}

	public void setMaxX(int maxX){
		this.maxX = maxX;
	}

	public void setMaxY(int maxY){
		this.maxY = maxY;
	}

	public int getWidth(){
		return width;
	}

	public int getHeight(){
		return height;
	}

	public void setWidth(int width){
		this.width = width;
	}

	public void setHeight(int height){
		this.height = height;
	}


	public Double getDoubleValue(String name, int x, int y){
		return raster.getSampleDouble(x, y, rasterProps.get(name));
	}

	public Integer getIntegerValue(String name, int x, int y){
		return raster.getSample(x, y, rasterProps.get(name));
	}

	public Double getValue(String name, int x, int y){
		// Double value = null;
		//if(rasterProps.keySet().contains(name))
		//{
		return raster.getSampleDouble(x, y, rasterProps.get(name));
		//return value;
		//} else
		//{
		//  value = initRaster.getSampleDouble(x + 2, y + 2, initRasterProps.get(name));
		//return value;
		//}
	}

	public void setCellValue(String name, int x, int y, Double value){

		//if(rasterProps.keySet().contains(name))
		raster.setSample(x, y, rasterProps.get(name), value);
		//else
		//  initRaster.setSample(x + 2, y + 2, initRasterProps.get(name), value);
	}

	public WritableRaster getRaster(){
		return raster;
	}



	

	public ArrayList<String> getPropertiesName(){

		ArrayList<String> names = new ArrayList<String>();

		for(String name : rasterProps.keySet()){
			names.add(name);

		}              
		return names;
	}

	public GridCellManager getGridCellManager(){
		return gridCellManager;
	}

	public void initMrm(int width){
		mrm = new MultiResolutionManager(width, getPropertiesName());
	}

	public void initMrm(int startX, int endX, int currentY){
		mrm = new MultiResolutionManager(startX, endX, currentY, getPropertiesName());
	}

	public MultiResolutionManager getMrm(){
		return mrm;	
	}
	private void addMrmValue(String name, int x,int y, Double value){
		try{
			mrm.add(x, y,name, value);
		}catch(Exception e){
		}
	}
	public void addTempValue(String name, int x, int y, Double value){
		gridCellManager.add(x, y, name, value);
	}

	public void setValue(String name, int x, int y, Double value){
		ts.setValue(name, x, y, value);
	}

	public List<Double> getGeomTempValues(String name){
		return geomTempVal.get(name);
	}

	public void clearGeomTemVal(String name){
		geomTempVal.get(name).clear();
	}

	public Set<String> getTempName(){
		return geomTempVal.keySet();
	}

	public void clearGeomTempVal(){

		for(String name : geomTempVal.keySet()){
			geomTempVal.get(name).clear();
		}    

	}

	public void setAggregOp(CellAggregOperator cao){
		gridCellManager.addOperator(cao, cao.getName());
	}

	public void cleanOperator(){
		gridCellManager.clearAggregMap();
	}

	public int[] intBounds(Double bounds[]){
		int gridCoord1[] = gridCoordinate(bounds[0].doubleValue(), bounds[1].doubleValue());
		int gridCoord2[] = gridCoordinate(bounds[2].doubleValue(), bounds[3].doubleValue());


		return (new int[] {
				gridCoord1[0], gridCoord2[1], gridCoord1[0] + width, gridCoord2[1] + height
		});
	}

	public void setRasterProperties(HashMap<String, Integer> initProperties){
		this.rasterProps = initProperties;
	}


	
	public void setCellShapeType(String type){
		this.cellShapeType = type;
		this.gridCellManager = GridCellFactory.create(type, this);
	}
	public void extendedMoore(int n){
		GridMultiQuadrilateralCellManager gmqcm = new GridMultiQuadrilateralCellManager(this);
		gmqcm.setSize(n);
		gridCellManager = gmqcm;
	}
	public String getCellShapeType(){
		return cellShapeType;
	}

	public HashMap<String, Integer> getBands(){
		return rasterProps;
	}

	public Double[] getWorldBounds(){
		return worldBounds;
	}

	public Double getValueByBand(int x, int y, int band){
		return raster.getSampleDouble(x, y, band);
	}

	public void setWorldBounds(Double[] worldBounds){

		this.worldBounds = worldBounds;
		
	}

	public void clearData(){
		//initRaster = null;
		raster = null;
	}

	public int getBand(String name){
		return rasterProps.get(name);
	}

	public void setInitRaster(WritableRaster raster){
		this.raster = raster;
	}

	public abstract class TypeSetter{

		public abstract void setValue(String name, int x, int y, Double value);

	}

	public class TempSetter extends TypeSetter{

		public void setValue(String name, int x, int y, Double value){
			addTempValue(name, x, y, value);

		}
	}

	public class NormSetter extends TypeSetter{

		public void setValue(String name, int x, int y, Double value){
			setCellValue(name, x, y, value);
		}
	}

	public class GeomSetter extends TypeSetter{

		public void setValue(String name, int x, int y, Double value){
			if(geomTempVal.keySet().contains(name)){
				geomTempVal.get(name).add(value);
			} else {
				List<Double> values = new List<Double>();
				values.add(value);
				geomTempVal.put(name, values);
			}
		}
	}

	public class MrmSetter extends TypeSetter{

		public void setValue(String name, int x, int y, Double value){
			addMrmValue(name, x, y, value);
		}
	}
	private double[] scalingdouble(Double[] bounds1, double[] bounds2){

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
	private Double[] scalingDouble(Double[] bounds1, double[] bounds2){

		Double[] scaled = new Double[4];

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
	public void setEnv(Envelope2D env){
		this.env = env;
	}
	public Envelope2D getEnv(){
		return env;
	}

	public GridGeometry2D getGridGeometry(){
		return gridGeometry;
	}
	public void setXRes(double xRes){
		this.xRes = xRes;
	}

	public void setYRes(double yRes){

		this.yRes = yRes;
	}

	public Double getXRes(){
		return xRes;
	}

	public Double getYRes(){
		return yRes;
	}

	public int getMinX(){
		return minX;
	}

	public int getMinY(){
		return minY;
	}

	public int getMaxX(){
		return maxX;
	}

	public int getMaxY()
	{
		return maxY;
	}

	public void setGridCellManager(GridCellManager gridCellManager){
		this.gridCellManager = gridCellManager;
	}

	public void setRaster(WritableRaster raster){
		this.raster = raster;
	}

	public void setMode(int mode){
		this.mode = mode;
		if(mode == modeTemp){
			ts = tempSetter;
		}
		if(mode == modeNorm){

			ts = normSetter; 
		}
		if(mode == modeGeom){

			ts = geomSetter;
		}
		if(mode == 4){
			ts = mrmSetter;
		}
	}

	public void addProp(String name, String band){
		int numBand = Integer.valueOf(band).intValue();
		rasterProps.put(name, Integer.valueOf(numBand));
	}

	public void setworldboundsPrime(Double[] worldBoundsPrime){
		this.worldBoundsPrime = worldBoundsPrime;
	}

}
