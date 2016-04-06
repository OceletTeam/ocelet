package fr.ocelet.runtime.raster;

import com.sun.media.jai.codecimpl.util.RasterFactory;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.index.quadtree.Key;

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
    public void setXRes(double xRes)
    {
        this.xRes = xRes;
    }

    public void setYRes(double yRes)
    {
       
        this.yRes = yRes;
    }

    public Double getXRes()
    {
       
        return xRes;
    }

    public Double getYRes()
    {
        return yRes;
    }

    public int getMinX()
    {
        return minX;
    }

    public int getMinY()
    {
        return minY;
    }

    public int getMaxX()
    {
        return maxX;
    }

    public int getMaxY()
    {
        return maxY;
    }

    public void setGridCellManager(GridCellManager gridCellManager)
    {
        this.gridCellManager = gridCellManager;
    }

    public void setRaster(WritableRaster raster)
    {
        this.raster = raster;
    }

  /*  public void setInitRaster(WritableRaster raster)
    {
        initRaster = raster;
    }*/

    public void setMode(int mode)
    {
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

    public void addProp(String name, String band)
    {
        int numBand = Integer.valueOf(band).intValue();
        rasterProps.put(name, Integer.valueOf(numBand));
    }

    public void setFinalProperties(List<String> props)
    {
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

    public int getPropBand(String name)
    {
        //if(initRasterProps.keySet().contains(name))
          //  return ((Integer)initRasterProps.get(name)).intValue();
        //else
            return ((Integer)rasterProps.get(name)).intValue();
    }

    public Grid(int width, int height, GridGeometry2D gridGeometry){
    
        //initRasterProps = new HashMap<String, Integer>();
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
    	//initRasterProps = new HashMap<String, Integer>();
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
    	//initRasterProps = new HashMap<String, Integer>();
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

   /* public Grid(Double bounds[], GridGeometry2D gridGeometry, int xRes, int yRes){
        worldBounds = bounds;
    	initRasterProps = new HashMap<String, Integer>();
        rasterProps = new HashMap<String, Integer>();
        this.gridGeometry = gridGeometry;
        this.xRes = xRes;
        this.yRes = yRes;
        
        int rXRes = (int)Math.round(xRes);
        int rYRes = (int)Math.round(yRes);
        
        width = (int) Math.round((bounds[2] - bounds[0]) / rXRes);
        height = (int) Math.round((bounds[3] - bounds[1]) / rYRes);
       
        double maxX = bounds[0] + (width * xRes);
        double maxY = bounds[1] + (height * yRes);
        
       int[] gridCoord = gridCoordinate(bounds[0], bounds[3]);
                
       initRaster = GridGenerator.createRaster(initRasterProps.keySet().size(), width, height);

    	
    }*/
   
    
    public void  copy(ORaster raster, KeyMap<String, Integer> matchedBand){
    	
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
    		System.out.println(width+" "+height);
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
    			for(String name : matchedBand.keySet()){
    			try{
    				 this.raster.setSample(i + gridMin[0], j + gridMin[1], rasterProps.get(name), raster.getDoubleValue(i + rasterMin[0] , j + rasterMin[1], matchedBand.get(name)));
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
    		System.out.println(width+" "+height);
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
    	
    	
    	
    	
    	
    	
    	/*int[] rasterC = raster.worldToGrid(boundsX, boundsY);
    		int[] r2 = gridCoordinate(boundsX, boundsY);
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
    		
    	//System.out.println(width +" "+height+" total : "+width * height);	
    		int exI = 0;
    		int exJ = 0;
    	    	for(int i = 0; i < width; i ++){    		
    		
    		for(int j = 0; j < height; j ++){
    			for(String name : rasterProps.keySet()){
    			try{
    				 this.raster.setSample(i + 2 + r2[0], j + 2 + r2[1], rasterProps.get(name), raster.getDoubleValue(i + diffX , j + diffY, rasterProps.get(name)));
    			}catch (Exception e){
    				
    				
    			}
    			}    			
    		}
    	}    	*/
    }
    
    /*
 public void copy(ORaster raster){
    	
    	int[] rasterC = raster.worldToGrid(boundsX, boundsY);
    		int[] r2 = gridCoordinate(boundsX, boundsY);
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
    		
    	//System.out.println(width +" "+height+" total : "+width * height);	
    		int exI = 0;
    		int exJ = 0;
    	    	for(int i = 0; i < width; i ++){    		
    		
    		for(int j = 0; j < height; j ++){
    			for(String name : rasterProps.keySet()){
    			try{
    				 this.raster.setSample(i + 2 + r2[0], j + 2 + r2[1], rasterProps.get(name), raster.getDoubleValue(i + diffX , j + diffY, rasterProps.get(name)));
    			}catch (Exception e){
    				
    				
    			}
    			}    			
    		}
    	}    	
    }*/
    
    
    public void setData(Double[] bounds,  ORaster raster, int numBands){
    	
    	xRes = raster.getXRes();
        yRes = raster.getYRes();
        Double[] newBounds = new Double[4];
        newBounds[0] = bounds[0] - xRes;
        newBounds[1] = bounds[1] - yRes;
        newBounds[2] = bounds[2] + xRes;
        newBounds[3] = bounds[3] + yRes;
       /* newBounds[0] = bounds[0];
        newBounds[1] = bounds[1];
        newBounds[2] = bounds[2];
        newBounds[3] = bounds[3];*/
        worldBounds = newBounds;
        int cellWidth = (int)(Math.round( (newBounds[2] - newBounds[0]) / xRes )) + 4;
        int cellHeight = (int)(Math.round( (newBounds[3] - newBounds[1]) / yRes )) + 4;
        minX = 0;
        minY = 0;
        maxX = cellWidth - 4;
        maxY = cellHeight - 4;
        double newMinX = newBounds[0] - (2 * xRes);
        double newMinY = newBounds[1] - (2 * yRes);
        width = cellWidth - 4;
        height = cellHeight - 4; 
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
        int cellWidth = (int)(Math.round( (newBounds[2] - newBounds[0]) / xRes )) + 4;
        int cellHeight = (int)(Math.round( (newBounds[3] - newBounds[1]) / yRes )) + 4;
        minX = 0;
        minY = 0;
        maxX = cellWidth - 4;
        maxY = cellHeight - 4;
        double newMinX = newBounds[0] - (2 * xRes);
        double newMinY = newBounds[1] - (2 * yRes);
        width = cellWidth - 4;
        height = cellHeight - 4; 
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
    public Grid(Double bounds[], GridGeometry2D gridGeometry, ORaster raster)
    {
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
			dp = gridGeometry.gridToWorld(new GridCoordinates2D(minX + 2, minY + 2));
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

 

    public int[] gridCoordinate(double x, double y)
    {
        int gCoord[] = new int[2];
        
        
        
        if(cellShapeType.equals("HEXAGONAL")){
        	
        	double l = Math.sin(Math.PI/3) * (xRes / 2);
        	
        	if(x%(xRes/2) == 0){
        		
        		gCoord[0] = (int) ((4 * (x - initCoordinates.x)) / (3 * xRes));
        		
        		gCoord[1] = (int)-((y - initCoordinates.y) / (2 * l));
        		
        	}else{
        		gCoord[0] = (int) ((4 * (x - initCoordinates.x)) / (3 * xRes));
        		gCoord[1] = (int)- ( (y - initCoordinates.y + l) / ( 2 * l) ) ;
        	}
        	gCoord[0] = gCoord[0];
        	gCoord[1] = gCoord[1];
        	return gCoord;        	
        }
        
        try
        {
           // GridCoordinates2D gc = gridGeometry.worldToGrid(new DirectPosition2D(x - (2 * xRes), y + (2 * yRes)));
             GridCoordinates2D gc = gridGeometry.worldToGrid(new DirectPosition2D(x, y ));

            gCoord[0] = gc.x - 2;
            gCoord[1] = gc.y - 2;
        }
        catch(TransformException ex)
        {
           // Logger.getLogger(fr/ocelet/runtime/raster/Grid.getName()).log(Level.SEVERE, null, ex);
        }
        return gCoord;
    }

    public Coordinate gridCoordinate(int x, int y){
 
    	if(cellShapeType.equals("HEXAGONAL")){
    	
    		double dx = initCoordinates.x;
    		double dy = initCoordinates.y;
    	 	double l = Math.sin(Math.PI/3) * (xRes / 2);
    	 	
    		if((x%2) == 0){
        		
    	    		dx = dx + ((x  * xRes )/ 2) + ((x  * xRes )/ 4);
    	    		dy = dy - (2 * y  * l);
    	    		
    	    	}else{
    	    		
    	    		
    	    		dx = dx + ((3 *x * xRes) / 4);
    	    		dy = dy - (2 * l*y ) - l ;
    	    	}
    	
    		return new Coordinate(dx, dy);
    	}
    
    
        try{
        
            DirectPosition dp = gridGeometry.gridToWorld(new GridCoordinates2D(x + 2, y + 2));
            return new Coordinate(dp.getCoordinate()[0], dp.getCoordinate()[1]);
        }
        catch(TransformException ex)
        {
           // Logger.getLogger(fr/ocelet/runtime/raster/Grid.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Coordinate[] cboundary(Polygon polygon)
    {
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

    public Coordinate[] cboundary(Line line)
    {
        Coordinate bounds[] = new Coordinate[2];
        double minX = Double.POSITIVE_INFINITY;
        double minY = Double.POSITIVE_INFINITY;
        double maxX = 0;
        double maxY = 0;
        Coordinate acoordinate[];
        int j = (acoordinate = line.getCoordinates()).length;
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

    public int[] intBounds(Polygon polygon)
    {
        int bounds[] = new int[4];
        Coordinate cBounds[] = cboundary(polygon);
        
      
        int gridCoord1[] = gridCoordinate(cBounds[0].x, cBounds[0].y);
        int gridCoord2[] = gridCoordinate(cBounds[1].x, cBounds[1].y);
        
        bounds[0] = gridCoord1[0] - 1;
        bounds[1] = gridCoord2[1] - 1;
        bounds[2] = gridCoord2[0] + 1;
        bounds[3] = gridCoord1[1] + 1;
   
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

    public int[] intBounds(Line line)
    {
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

    public int reverse(int y)
    {
        return height - y;
    }

    public void setMaxX(int maxX)
    {
        this.maxX = maxX;
    }

    public void setMaxY(int maxY)
    {
        this.maxY = maxY;
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }
    
    
    public Double getDoubleValue(String name, int x, int y)
    {
       
       // if(rasterProps.keySet().contains(name)){
        
          return raster.getSampleDouble(x, y, rasterProps.get(name));
           
      //  } else{
        
         //    return initRaster.getSampleDouble(x + minX, y + minY, initRasterProps.get(name));
           
       // }
    }
    
    public Integer getIntegerValue(String name, int x, int y){
    
       
      //  if(rasterProps.keySet().contains(name)){
        
            return raster.getSample(x, y, rasterProps.get(name));
          
        //} else {
        //    return  initRaster.getSample(x + minX, y + minY, initRasterProps.get(name));
          
        //}
    }

    public Double getValue(String name, int x, int y)
    {
       // Double value = null;
        //if(rasterProps.keySet().contains(name))
        //{
            return raster.getSampleDouble(x + 2, y + 2, rasterProps.get(name));
            //return value;
        //} else
        //{
          //  value = initRaster.getSampleDouble(x + 2, y + 2, initRasterProps.get(name));
            //return value;
        //}
    }

    public void setCellValue(String name, int x, int y, Double value)
    {
    	
        //if(rasterProps.keySet().contains(name))
            raster.setSample(x + 2, y + 2, rasterProps.get(name), value);
        //else
          //  initRaster.setSample(x + 2, y + 2, initRasterProps.get(name), value);
    }

    public WritableRaster getRaster()
    {
        return raster;
    }

 

    public int[] getPos1()
    {
        return pos1;
    }

    public void setPos1(int pos1[])
    {
        this.pos1 = pos1;
    }

    public int[] getPos2()
    {
        return pos2;
    }

    public void setPos2(int pos2[])
    {
        this.pos2 = pos2;
    }

    public void setPos1(int x, int y)
    {
        pos1[0] = x;
        pos1[1] = y;
    }

    public void setPos2(int x, int y)
    {
        pos2[0] = x;
        pos2[1] = y;
    }

    public ArrayList<String> getPropertiesName()
    {
    	
        ArrayList<String> names = new ArrayList<String>();
        
        //for(String name : initRasterProps.keySet()){
        	//names.add(name);
        
        //}
        for(String name : rasterProps.keySet()){
        	names.add(name);
        	
        }              
        return names;
    }

    public GridCellManager getGridCellManager()
    {
        return gridCellManager;
    }
    
    public void initMrm(){
    	mrm = new MultiResolutionManager(width, getPropertiesName());

    }

    public MultiResolutionManager getMrm(){
    		return mrm;	
    }
    private void addMrmValue(String name, int x, Double value){
    	mrm.add(x, name, value);
    }
    public void addTempValue(String name, int x, int y, Double value)
    {
        gridCellManager.add(x, y, name, value);
    }

    /*public CellValues[] getCellValuesToSynchro()
    {
        return gridCellManager.getValues();
    }*/

    public void setValue(String name, int x, int y, Double value)
    {
    	
    	ts.setValue(name, x, y, value);
       /* if(mode == modeTemp){
        	
            addTempValue(name, x, y, value);
        }
        if(mode == modeNorm){
        	
            setCellValue(name, x, y, value);
        }
        if(mode == modeGeom){
        
            if(geomTempVal.keySet().contains(name)){
            
                geomTempVal.get(name).add(value);
             
            } else
            {
                List<Double> values = new List<Double>();
                values.add(value);
        
                geomTempVal.put(name, values);
            }
        }
        if(mode == 4){
        	addMrmValue(name, x, value);
        }*/
    }

    public List<Double> getGeomTempValues(String name)
    {
        return geomTempVal.get(name);
    }
    
    public void clearGeomTemVal(String name){
    	geomTempVal.get(name).clear();
    }

    public Set<String> getTempName()
    {
        return geomTempVal.keySet();
    }

    public void clearGeomTempVal(){
        	
    	for(String name : geomTempVal.keySet()){
    		geomTempVal.get(name).clear();
    	}    

    }

    public void setAggregOp(CellAggregOperator cao)
    {
        gridCellManager.addOperator(cao, cao.getName());
    }

    public void cleanOperator()
    {
        gridCellManager.clearAggregMap();
    }

    public int[] intBounds(Double bounds[])
    {
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
    	this.gridCellManager = GridCellFactory.create(this);
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
    
    
    
    /*
      if(mode == modeTemp){
        	
            addTempValue(name, x, y, value);
        }
        if(mode == modeNorm){
        	
            setCellValue(name, x, y, value);
        }
        if(mode == modeGeom){
        
            if(geomTempVal.keySet().contains(name)){
            
                geomTempVal.get(name).add(value);
             
            } else
            {
                List<Double> values = new List<Double>();
                values.add(value);
        
                geomTempVal.put(name, values);
            }
        }
        if(mode == 4){
        	addMrmValue(name, x, value);
        }
     */
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
           
          } else
          {
              List<Double> values = new List<Double>();
              values.add(value);
      
              geomTempVal.put(name, values);
          }
 		
 	}
 }
 
 public class MrmSetter extends TypeSetter{
 	
 	public void setValue(String name, int x, int y, Double value){
 		addMrmValue(name, x, value);
 		
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
