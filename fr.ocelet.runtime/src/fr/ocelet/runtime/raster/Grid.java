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
	protected double boundsX;
	protected double boundsY;
	public Envelope2D env;
	private int pos1[];
	private int pos2[];
	private TypeSetter ts;
	private TempSetter tempSetter = new TempSetter();
	private NormSetter normSetter = new NormSetter();
	private GeomSetter geomSetter = new GeomSetter();
	private MrmSetter mrmSetter = new MrmSetter();


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

	/*  public void setInitRaster(WritableRaster raster)
    {
        initRaster = raster;
    }*/

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

		//if(!rasterProps.keySet().isEmpty())
		// raster = createRaster(rasterProps.keySet().size(), width + 4, height + 4);
	}

	public int getPropBand(String name){
		//if(initRasterProps.keySet().contains(name))
		//  return ((Integer)initRasterProps.get(name)).intValue();
		//else
		return ((Integer)rasterProps.get(name)).intValue();
	}

	public Grid(int width, int height, GridGeometry2D gridGeometry){

		rasterProps = new HashMap<String, Integer>();
		pos1 = new int[2];
		pos2 = new int[2];
		this.gridGeometry = gridGeometry;
		this.width = width;
		this.height = height;
		this.minX = 0;
		this.minY = 0;
		this.maxX = width;
		this.maxY = height;
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


	public void  copy(ORaster raster, KeyMap<String, Integer> matchedBand){

		/*int[] rasterC = raster.worldToGrid(boundsX, boundsY);
    	double[] rbounds = raster.worldBounds();*/

		Double[] scaledD = scalingDouble(worldBounds, raster.worldBounds());
		double[] scaledd = scalingdouble(worldBounds, raster.worldBounds());


		int[] rasterMin = raster.worldToGrid(scaledD[0],scaledD[3]);
		int[] gridMin = gridCoordinate(scaledd[0], scaledd[3]);
		int[] rasterMax = raster.worldToGrid(scaledD[2],scaledD[1]);
		int[] gridMax = gridCoordinate(scaledd[2], scaledd[1]);

		/*int[] r2 = gridCoordinate(boundsX, boundsY);
    		int[] b = raster.getBounds();
    		int diffX = rasterC[0] - r2[0];
    		int diffY = rasterC[1] - r2[1];

    		if(r2[0] < 0){
    			diffX = rasterC[0];
    		}

    		if(r2[1] < 0){
    			diffY = rasterC[1];
    		}
    		if(diffX < 0){
    			diffX = 0;
    		}

    		if(diffY < 0){
    			diffY = 0;
    		}

    		int exI = 0;
    		int exJ = 0;
    	    	for(int i = 0; i < width; i ++){    		

    		for(int j = 0; j < height; j ++){
    			for(String name : matchedBand.keySet()){
    			try{
    				 this.raster.setSample(i + 2 + r2[0], j + 2 + r2[1], rasterProps.get(name), raster.getDoubleValue(i + diffX , j + diffY, matchedBand.get(name)));
    			}catch (Exception e){


    			}
    			}    			
    		}
    	}*/
		//int width = gridMax[0] - gridMin[0];// + 1;
		//int height = gridMax[1] - gridMin[1];// + 1;

		for(int i = 0; i < width; i ++){
			for(int j = 0; j < height; j ++){
				for(String name : matchedBand.keySet()){
					try{

						//this.raster.setSample(i + gridMin[0], j + gridMin[1], rasterProps.get(name), raster.getDoubleValue(i + rasterMin[0], j + rasterMin[1], matchedBand.get(name)));
						Coordinate worlds = this.gridCoordinate(i, j);
						int[] convert = raster.worldToGrid(worlds.x, worlds.y);
						//double[] rW = raster.gridToWorld(i, j);	
						/*if(i != convert[0] || j != convert[1]){
    				}*/
						this.raster.setSample(i, j, rasterProps.get(name), raster.getDoubleValue(convert[0], convert[1], matchedBand.get(name)));
					}catch (Exception e){

						//e.printStackTrace();

					}
				}    			
			}
		}
	}
	public void copy(ORaster raster){


		int[] rasterC = raster.worldToGrid(boundsX, boundsY);
		double[] rbounds = raster.worldBounds();

		Double[] scaledD = scalingDouble(worldBounds, raster.worldBounds());
		double[] scaledd = scalingdouble(worldBounds, raster.worldBounds());


		int[] rasterMin = raster.worldToGrid(scaledD[0],scaledD[3]);
		int[] gridMin = gridCoordinate(scaledd[0], scaledd[3]);
		int[] rasterMax = raster.worldToGrid(scaledD[2],scaledD[1]);
		int[] gridMax = gridCoordinate(scaledd[2], scaledd[1]);

		/*int[] r2 = gridCoordinate(boundsX, boundsY);
    		int[] b = raster.getBounds();
    		int diffX = rasterC[0] - r2[0];
    		int diffY = rasterC[1] - r2[1];

    		if(r2[0] < 0){
    			diffX = rasterC[0];
    		}

    		if(r2[1] < 0){
    			diffY = rasterC[1];
    		}
    		if(diffX < 0){
    			diffX = 0;
    		}

    		if(diffY < 0){
    			diffY = 0;
    		}

    		int exI = 0;
    		int exJ = 0;
    	    	for(int i = 0; i < width; i ++){    		

    		for(int j = 0; j < height; j ++){
    			for(String name : matchedBand.keySet()){
    			try{
    				 this.raster.setSample(i + 2 + r2[0], j + 2 + r2[1], rasterProps.get(name), raster.getDoubleValue(i + diffX , j + diffY, matchedBand.get(name)));
    			}catch (Exception e){


    			}
    			}    			
    		}
    	}*/

		int width = gridMax[0] - gridMin[0];
		int height = gridMax[1] - gridMin[1];
		for(int i = 0; i < width; i ++){
			for(int j = 0; j < height; j ++){
				for(String name : rasterProps.keySet()){
					try{
						this.raster.setSample(i + gridMin[0], j + gridMin[1], rasterProps.get(name), raster.getDoubleValue(i + rasterMin[0] , j + rasterMin[1], rasterProps.get(name)));
					}catch (Exception e){
						//e.printStackTrace();


					}
				}    			
			}
		}
	}


	public void setData(Double[] bounds,  ORaster raster, int numBands){

		xRes = raster.getXRes();
		yRes = raster.getYRes();
		Double[] newBounds = new Double[4];
		newBounds[0] = bounds[0];
		newBounds[1] = bounds[1];
		newBounds[2] = bounds[2];
		newBounds[3] = bounds[3];
		/* newBounds[0] = bounds[0];
        newBounds[1] = bounds[1];
        newBounds[2] = bounds[2];
        newBounds[3] = bounds[3];*/
		worldBounds = newBounds;
		int cellWidth = (int)(Math.round( (newBounds[2] - newBounds[0]) / xRes ));
		int cellHeight = (int)(Math.round( (newBounds[3] - newBounds[1]) / yRes ));
		minX = 0;
		minY = 0;
		maxX = cellWidth;
		maxY = cellHeight;
		double newMinX = newBounds[0];
		double newMinY = newBounds[1];
		width = cellWidth;
		height = cellHeight; 
		boundsX = newBounds[0];
		boundsY = newBounds[3];
		this.raster = GridGenerator.createRaster(numBands, cellWidth, cellHeight);
		Envelope2D env = GridGenerator.createEnvelope(newMinX, newMinY, newMinX+  (xRes * cellWidth), newMinY + (yRes * cellHeight));
		GridCoverage2D coverage = GridGenerator.createCoverage("", this.raster, env);
		setEnv(env);
		gridGeometry = coverage.getGridGeometry();

		copy(raster);
		raster = null;

		setInitCoordinate();
	}

	public void setData( ORaster raster, int numBands){

		xRes = raster.getXRes();
		yRes = raster.getYRes();
		Double[] newBounds = new Double[4];
		double[] bounds = raster.worldBounds();

		/*   newBounds[0] = bounds[0] - xRes;
        newBounds[1] = bounds[1] - yRes;
        newBounds[2] = bounds[2] + xRes;
        newBounds[3] = bounds[3] + yRes;*/
		newBounds[0] = bounds[0];
		newBounds[1] = bounds[1];
		newBounds[2] = bounds[2];
		newBounds[3] = bounds[3];

		worldBounds = newBounds;
		int cellWidth = (int)(Math.round( (newBounds[2] - newBounds[0]) / xRes ));
		int cellHeight = (int)(Math.round( (newBounds[3] - newBounds[1]) / yRes ));
		minX = 0;
		minY = 0;
		maxX = cellWidth;
		maxY = cellHeight;
		double newMinX = newBounds[0];
		double newMinY = newBounds[1];
		width = cellWidth;
		height = cellHeight; 
		boundsX = newBounds[0];
		boundsY = newBounds[3];
		this.raster = GridGenerator.createRaster(numBands, cellWidth, cellHeight);
		Envelope2D env = GridGenerator.createEnvelope(newMinX, newMinY, newMinX+  (xRes * cellWidth), newMinY + (yRes * cellHeight));
		GridCoverage2D coverage = GridGenerator.createCoverage("", this.raster, env);
		setEnv(env);
		gridGeometry = coverage.getGridGeometry();

		copy(raster);
		raster = null;

		setInitCoordinate();
	}
	public Grid(Double bounds[], GridGeometry2D gridGeometry, ORaster raster){
		this.ts = normSetter;
		//initRasterProps = new HashMap<String, Integer>();
		rasterProps = new HashMap<String, Integer>();
		pos1 = new int[2];
		pos2 = new int[2];
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
		// maxX = minX + width;
		// maxY = minY + height;

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
	private Coordinate[] hexagonalCoordinate(int x, int y){


		Coordinate c = gridCoordinate(x, y);
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
		coords[1] = new Coordinate(dx + xRes/2, dy - yRes);
		coords[2] = new Coordinate(dx - xRes/2, dy- yRes);
		coords[3] = new Coordinate(dx - xRes, dy);
		coords[4] = new Coordinate(dx - xRes/2, dy + yRes);
		coords[5] = new Coordinate(dx + xRes/2, dy + yRes);
		coords[6] = new Coordinate(dx + xRes, dy);

		return coords;


	}
	private int[] hexagonalWorldToGrid(double x, double y){

		int[] gCoord = new int[2];

		GridCoordinates2D gc = null;
		try{

			gc = gridGeometry.worldToGrid(new DirectPosition2D(x, y ));
		}catch(TransformException ex){

			ex.printStackTrace();
			// Logger.getLogger(fr/ocelet/runtime/raster/Grid.getName()).log(Level.SEVERE, null, ex);
		}

		boolean gridType = false;
		gCoord[0] = gc.x;
		gCoord[1] = gc.y;

		Integer gridX = null;
		Integer gridY = null;

		double hWidth = 2 * xRes;
		double rWidth = 1.5 * xRes;
		double height  = 2 * yRes;
		double eWidth = 0.5 * xRes;

		int gridModX = (int)(x - initCoordinates.x) % (int)rWidth;
		int gridModY = (int)(y - initCoordinates.y) % (int)height;

		double m = eWidth / yRes;


		Coordinate[] coord = new Coordinate[]{new Coordinate(x, y)};
		CoordinateSequence pointSequence = new CoordinateArraySequence(coord);
		Point point = new Point(pointSequence, SpatialManager.geometryFactory());

		if(gCoord[0] % 2 == 0){

			Coordinate[] coords = hexagonalCoordinate(gCoord[0], gCoord[1]);

			CoordinateSequence cs = new CoordinateArraySequence(coords);

			LinearRing lr = new LinearRing(cs,SpatialManager.geometryFactory());

			Polygon poly = new Polygon(lr, null,SpatialManager.geometryFactory());
			if(poly.contains(point)){
				return gCoord;
			}else{
				if(gCoord[0] - 1 >= 0){
					coords = hexagonalCoordinate(gCoord[0] - 1, gCoord[1]);

					cs = new CoordinateArraySequence(coords);

					lr = new LinearRing(cs,SpatialManager.geometryFactory());

					poly = new Polygon(lr, null,SpatialManager.geometryFactory());
					if(poly.contains(point)){
						return new int[]{gCoord[0] - 1, gCoord[1]};
					}
				}
				if(gCoord[0] - 1 >= 0 && gCoord[1] - 1 >= 0){
					coords = hexagonalCoordinate(gCoord[0] - 1, gCoord[1] - 1);

					cs = new CoordinateArraySequence(coords);

					lr = new LinearRing(cs,SpatialManager.geometryFactory());

					poly = new Polygon(lr, null,SpatialManager.geometryFactory());
					if(poly.contains(point)){
						return new int[]{gCoord[0] - 1, gCoord[1] - 1};
					}
					
					
				}
					
				
			}
			
			return null;

		}else{
			
			Coordinate[] coords = hexagonalCoordinate(gCoord[0], gCoord[1]);

				CoordinateSequence cs = new CoordinateArraySequence(coords);

				LinearRing lr = new LinearRing(cs,SpatialManager.geometryFactory());

				Polygon poly = new Polygon(lr, null,SpatialManager.geometryFactory());
				if(poly.contains(point)){
					return new int[]{gCoord[0], gCoord[1]};
				}
			if(gCoord[0] -1 >=0 && gCoord[1] < height - 1){
				 coords = hexagonalCoordinate(gCoord[0] - 1, gCoord[1]);

				 cs = new CoordinateArraySequence(coords);

				 lr = new LinearRing(cs,SpatialManager.geometryFactory());

				 poly = new Polygon(lr, null,SpatialManager.geometryFactory());
				if(poly.contains(point)){
					return new int[]{gCoord[0] - 1, gCoord[1]};
				}
				
			}
			if(gCoord[1] - 1 >= 0 ){
				 coords = hexagonalCoordinate(gCoord[0], gCoord[1] - 1);

				cs = new CoordinateArraySequence(coords);

				lr = new LinearRing(cs,SpatialManager.geometryFactory());

				poly = new Polygon(lr, null,SpatialManager.geometryFactory());
				if(poly.contains(point)){
					return new int[]{gCoord[0], gCoord[1] - 1};
				}
			}
			
		return null;	
			
			

		}


		/*if(gCoord[0] % 2 == 0){

        	gridX = gCoord[0];
        	gridY = gCoord[1];

        	if(gridModX < eWidth - gridModY * m){
        		gridX = gCoord[0] - 1;
        		gridY = gCoord[1] - 1;
        	}

        	if(gridModX < (-eWidth + gridModY * m)){
        		gridX = gCoord[0];
        		gridY = gCoord[1] - 1;
        	}
        }else{

        	if(gridModY >= yRes){
        		if(gridModX < 2 * eWidth - gridModY * m){
        			gridX = gCoord[0];
        			gridY = gCoord[1] - 1;

        		}


        	}

        	if(gridModY < yRes){
        		if(gridModX < gridModY * m){
        			gridX = gCoord[0];
        			gridY = gCoord[1] - 1;
        		}else{
        			gridX = gCoord[0] - 1;
        			gridY = gCoord[1];
        		}
        	}
        }*/

		/*Coordinate c = gridCoordinate(gCoord[0], gCoord[1]);
        if(c.distance(new Coordinate(x, y)) > xRes){
        	return null;
        }*/
		/*if(gridX == null || gridY == null || gridX < 0 || gridX > width - 1 || gridY < 0 || gridY > height - 1){
			return null;
		}

		gCoord[0] = gridX;
		gCoord[1] = gridY;
		return gCoord;*/
	}


	public int[] gridCoordinate(double x, double y){

		int gCoord[] = new int[2];
		if(cellShapeType.equals("HEXAGONAL")){
			return hexagonalWorldToGrid(x, y);
			/*
        	GridCoordinates2D gc = null;
        	try{

             gc = gridGeometry.worldToGrid(new DirectPosition2D(x, y ));
        	}catch(TransformException ex){


        		ex.printStackTrace();
        		// Logger.getLogger(fr/ocelet/runtime/raster/Grid.getName()).log(Level.SEVERE, null, ex);
        	}

        	if(gCoord[0] % 2 != 0){
        		if(y <= worldBounds[3])
        		try{

             gc = gridGeometry.worldToGrid(new DirectPosition2D(x, y + yRes));
        	}catch(TransformException ex){


        		ex.printStackTrace();
        		// Logger.getLogger(fr/ocelet/runtime/raster/Grid.getName()).log(Level.SEVERE, null, ex);
        	}
        	}
            gCoord[0] = gc.x;
            gCoord[1] = gc.y;
            double wy;
            double wx;

            //Coordinate c = gridCoordinate(gCoord[0], gCoord[1]);
           // Logger.getLogger(fr/ocelet/runtime/raster/Grid.getName()).log(Level.SEVERE, null, ex);
           // Coordinate c = new Coordinate(dp.getCoordinate()[0], dp.getCoordinate()[1]);

            Coordinate c = gridCoordinate(gCoord[0], gCoord[1]);
            if(gCoord[0] % 2 == 0){

            Coordinate scaled = new Coordinate(x , y);


            		if(scaled.distance(c) > xRes){

            		return null;
            	}



            }else{

            	Coordinate scaled = new Coordinate(x , y);
            	if(scaled.distance(c) > xRes){

            		return null;
            	}
            }



        	return gCoord;  */      	
		}
		if(cellShapeType.equals("TRIANGULAR")){

			double dx = x - initCoordinates.x;
			double dy =  y - initCoordinates.y;
			double sqr = Math.sqrt(3);
			int fX = 0;
			int fY = 0;

			fX = (int)Math.round((dx - (xRes/2)) * (2/xRes));	
			//dx = dx + x * (xRes / 2);
			if(x % (xRes/2) == 0){ 
				if( y % (yRes/2) == 0){

					fY = (int)Math.round(dy - ((1/3) * (yRes * sqr / 2)) / ( sqr * yRes / 2));

				}else{
					fY = (int)Math.round(dy - ((2/3) * (yRes * sqr / 2)) / ( sqr * yRes / 2));

				}
			}else{
				if( y % (yRes/2) == 0){
					fY = (int)Math.round(dy - ((2/3) * (yRes * sqr / 2)) / ( sqr * yRes / 2));

				}else{
					fY = (int)Math.round(dy - ((1/3) * (yRes * sqr / 2)) / ( sqr * yRes / 2));


				}
			}
			gCoord[0] = fX;
			gCoord[1] = fY;


		}
		try{

			// GridCoordinates2D gc = gridGeometry.worldToGrid(new DirectPosition2D(x - (2 * xRes), y + (2 * yRes)));
			GridCoordinates2D gc = gridGeometry.worldToGrid(new DirectPosition2D(x, y ));

			gCoord[0] = gc.x;
			gCoord[1] = gc.y;
		} catch(TransformException ex) {

			ex.printStackTrace();
			// Logger.getLogger(fr/ocelet/runtime/raster/Grid.getName()).log(Level.SEVERE, null, ex);
		}
		return gCoord;
	}

	public Coordinate gridCoordinate(int x, int y){

		if(cellShapeType.equals("HEXAGONAL")){

			double dx = initCoordinates.x;
			double dy = initCoordinates.y;
			/*double l = Math.sin(Math.PI/3) * (xRes / 2);

    		if((x%2) == 0){

    	    		dx = dx + ((x  * xRes )/ 2) + ((x  * xRes )/ 4);
    	    		dy = dy - (2 * y  * l);

    	    	}else{


    	    		dx = dx + ((3 *x * xRes) / 4);
    	    		dy = dy - (2 * l*y ) - l ;
    	    	}*/
			DirectPosition dc = null;
			try {
				dc = gridGeometry.gridToWorld(new GridCoordinates2D(x, y));
			} catch (InvalidGridGeometryException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TransformException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			double dist = xRes - (xRes / 2 + xRes / 4);

			if((x%2) == 0){

				dx = dc.getCoordinate()[0] + dist;
				dy = dc.getCoordinate()[1];
				/*dx = dx + ((x  * xRes )) + ((x  * xRes )/ 2);
    	    		dy = dy + (2 * y  * yRes);*/

			}else{
				dx = dc.getCoordinate()[0] + dist;
				dy = dc.getCoordinate()[1] - yRes;


				/*dx = dx + ((3 *x * xRes) / 2);
    	    		dy = dy + (2 * yRes*y ) + yRes ;*/
			}
			return new Coordinate(dx, dy);
		}
		if(cellShapeType.equals("TRIANGULAR")){

			double xInit = initCoordinates.x;
			double yInit = initCoordinates.y;

			double dx = (x * xRes/2) + xRes/2;
			double dy = 0.0;
			//dx = dx + x * (xRes / 2);
			if(x % 2 == 0){ 
				if( y % 2 == 0){
					dy = y * ((Math.sqrt(3)/ 2) * yRes ) + ((Math.sqrt(3) / 2) * yRes ) / 3;
				}else{
					dy = y * ((Math.sqrt(3)/ 2) * yRes ) + ((Math.sqrt(3)) * yRes ) / 3;

				}
			}else{
				if( y % 2 == 0){

					dy = y * ((Math.sqrt(3)/ 2) * yRes ) + ((Math.sqrt(3)) * yRes ) / 3;
				}else{
					dy = y * ((Math.sqrt(3)/ 2) * yRes ) + ((Math.sqrt(3) / 2) * yRes ) / 3;


				}
			}
			dx = dx+ xInit;
			dy = yInit - dy;

			return new Coordinate(dx, dy);
		}

		try{
			DirectPosition dp = gridGeometry.gridToWorld(new GridCoordinates2D(x, y));
			
			return new Coordinate(dp.getCoordinate()[0], dp.getCoordinate()[1]);
		}
		catch(TransformException ex)
		{
			
			//ex.printStackTrace();
			// Logger.getLogger(fr/ocelet/runtime/raster/Grid.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}

	public Coordinate[] cboundary(Polygon polygon){
		Coordinate bounds[] = new Coordinate[2];
		double minX = Double.POSITIVE_INFINITY;
		double minY = Double.POSITIVE_INFINITY;
		double maxX = 0;
		double maxY = 0;
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
		double maxX = 0;
		double maxY = 0;
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
		int gridCoord1[] = gridCoordinate(cBounds[0].x, cBounds[1].y);
		int gridCoord2[] = gridCoordinate(cBounds[1].x, cBounds[0].y);



		if(gridCoord1 == null){
			bounds[0] = 0;
			bounds[1] = 0; 
		}else{

			bounds[0] = gridCoord1[0];

			bounds[1] = gridCoord1[1];
		}

		if(gridCoord2 == null){
			bounds[2] = width;
			bounds[3] = height; 
		}else{


			bounds[2] = gridCoord2[0];
			bounds[3] = gridCoord2[1];
		}
		/* if(cBounds[0].x < initCoordinates.x){
        	bounds[0] = 0;
        }else{

        }
        if(cBounds[0].x < initCoordinates.x){
        	bounds[0] = 0;
        }
        if(cBounds[0].x < initCoordinates.x){
        	bounds[0] = 0;
        }
        if(cBounds[0].x < initCoordinates.x){
        	bounds[0] = 0;
        }*/

		if(bounds[0] < 0){
			bounds[0] = 0;
		}
		if(bounds[1] < 0){
			bounds[1] = 0;
		}

		if(bounds[2] > width - 1){
			bounds[2] = width - 1;
		}

		if(bounds[3] > height - 1){
			bounds[3] = height - 1;
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



	public int[] getPos1(){
		return pos1;
	}

	public void setPos1(int pos1[]){
		this.pos1 = pos1;
	}

	public int[] getPos2(){
		return pos2;
	}

	public void setPos2(int pos2[]){
		this.pos2 = pos2;
	}

	public void setPos1(int x, int y){
		pos1[0] = x;
		pos1[1] = y;
	}

	public void setPos2(int x, int y){
		pos2[0] = x;
		pos2[1] = y;
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


	public void setGridGeometry(GridGeometry2D gridGeometry){
		this.gridGeometry = gridGeometry;
		setInitCoordinate();
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
		this.boundsX = worldBounds[0];
		this.boundsY = worldBounds[3];
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
}
