package fr.ocelet.datafacer.ocltypes;

import fr.ocelet.runtime.entity.AbstractEntity;
import fr.ocelet.runtime.geom.SpatialManager;
import fr.ocelet.runtime.geom.ocltypes.Cell;
import fr.ocelet.runtime.geom.ocltypes.Polygon;
import fr.ocelet.runtime.ocltypes.KeyMap;
import fr.ocelet.runtime.ocltypes.List;
import fr.ocelet.runtime.raster.Grid;
import fr.ocelet.runtime.raster.ORaster;
import fr.ocelet.runtime.util.FileUtils;
import fr.ocelet.runtime.raster.GridGenerator;


import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.factory.Hints;
import org.geotools.gce.geotiff.GeoTiffWriter;
import org.geotools.geometry.Envelope2D;
import org.geotools.referencing.CRS;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequence;

public class RasterFile{
	
	private KeyMap<String, Integer> propMatched = new KeyMap<String, Integer>();
	private CoordinateReferenceSystem crs;
	
	private String path;
	private String directory;
	private boolean isDirectory;
	protected String[] names;
	private int index = 0;
	private Double[] bounds;
	private boolean bounded = false;
	private String fileFormat = null;
	
	
	
    public RasterFile(String fileName){
    	/*try{
        raster = new ORaster(FileUtils.applyOutput(fileName));
    	}catch(Exception e){
    		
    	}*/
    	
	      	File file = new File(FileUtils.applyOutput(fileName));
	      	if(file.isDirectory()) {
	      		directory = fileName;
	        names = file.list();
	        isDirectory = true;
	        File[] files = file.listFiles();
	        ArrayList<String> tempNames = new ArrayList<String>();
	        for(File f : files){
	        	if(!f.isDirectory()){
	        		tempNames.add(f.getName());

	        	}
	        }
	        Collections.sort(tempNames);
	        names = tempNames.toArray(new String[tempNames.size()]);
	      
	      	}else {
	      		this.path = fileName;
	      	}
	      	
	      	
    }
    
    public RasterFile(String fileName, String epsg) {
    	/*try{
            raster = new ORaster(FileUtils.applyOutput(fileName));
        	}catch(Exception e){
        		e.printStackTrace();
        	}*/
    	File file = new File(FileUtils.applyOutput(fileName));
      	if(file.isDirectory()) {
      		isDirectory = true;
      		directory = fileName;
        names = file.list();
        File[] files = file.listFiles();
        ArrayList<String> tempNames = new ArrayList<String>();
        for(File f : files){
        	if(!f.isDirectory()){
        		tempNames.add(f.getName());

        	}
        }
        Collections.sort(tempNames);
        names = tempNames.toArray(new String[tempNames.size()]);
        
      	}else {
      		this.path = fileName;
      	}
      	
    	setCrs(epsg);
    	//raster.setCRS(crs);
    }
    
    public void setFileFormat(String fileFormat) {
    	this.fileFormat = fileFormat;
    	 ArrayList<String> tempNames = new ArrayList<String>();
    	 for(String name : names) {
    		
    		 if(name.substring(name.length() - 4, name.length()).equals(fileFormat)) {
    			
    			 tempNames.add(name);
    			 
    		 }
    	 }
    	 Collections.sort(tempNames);
         names = tempNames.toArray(new String[tempNames.size()]);
         this.path = directory+"/"+names[index];
    	
    }
    public void setDirectory(String fileName){
   	 directory = fileName;
   
     	File file = new File(FileUtils.applyOutput(directory));
       names = file.list();
       isDirectory = true;
       File[] files = file.listFiles();
       ArrayList<String> tempNames = new ArrayList<String>();
       for(File f : files){
       	if(!f.isDirectory()){
       		tempNames.add(f.getName());

       	}
       }
       Collections.sort(tempNames);
       names = tempNames.toArray(new String[tempNames.size()]);
       index = 0;
       this.path = directory+"/"+names[index];
       
       if(fileFormat != null) {
    	   setFileFormat(fileFormat);
       }
   }
    
    
    public boolean hasNext(){
    	if(index < names.length - 1){
    		return true;
    	}
    	return false;
    }

    public void next(){
    	index ++;
    }
    
    public void update(){
    	
    	
    	if(bounded) {
    	try{
            raster = new ORaster(FileUtils.applyOutput(directory+"/"+names[index]), bounds);
        	}catch(Exception e){
        		e.printStackTrace();
        	}
    	if(crs != null) {
    		raster.setCRS(crs);
    	}
    	}else {
    		try{
                raster = new ORaster(FileUtils.applyOutput(directory+"/"+names[index]));
            	}catch(Exception e){
            		e.printStackTrace();
            	}
        	if(crs != null) {
        		raster.setCRS(crs);
        	}
    	}
       
         this.grid.copy(raster, propMatched);
    }
    
    public Integer size() {
    	if(isDirectory) {
    		return names.length;
    	}else {
    		return 1;
    	}
    }
    public Polygon getBoundaries(){
    	Coordinate[] coordinates = new Coordinate[5];
    	Double[] bounds = grid.getWorldBounds();
		coordinates[0] = new Coordinate(bounds[0], bounds[1]);
		coordinates[1] = new Coordinate(bounds[0], bounds[3]);
		coordinates[2] = new Coordinate(bounds[2], bounds[3]);
		coordinates[3] = new Coordinate(bounds[2], bounds[1]);
		coordinates[4] = coordinates[0];

		CoordinateSequence cs = new CoordinateArraySequence(coordinates);
		LinearRing lr = new LinearRing(cs, SpatialManager.geometryFactory());
		return new Polygon(lr, null, SpatialManager.geometryFactory());

    }
    
    /**
	 * Decodes the EPSG String to obtain the corresponding CRS and obtains a
	 * MathTransform if the model's CRS is different.
	 * 
	 * @param epsg
	 *            The coordinate system in text format. Ex: "EPSG:4326"
	 */
	protected void setCrs(String epsgCode) {
		try {
			crs = CRS.decode(epsgCode);
			Hints.putSystemDefault(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, 
					Boolean.TRUE);
			

		
			
		} catch (NoSuchAuthorityCodeException e) {
			System.out.println("Unknown EPSG code : "
					+ epsgCode);
		} catch (FactoryException e) {
			System.out.println(
					"Failed to build the coordinate system :" + epsgCode);
			e.printStackTrace();
		}
	}
    public void setFileName(String fileName){
    	
    	
    	if(isDirectory) {
    		int i = 0;
    		for(String name : names) {
    			if(name.equals(fileName)) {
    				index = i;
    				
    			}
    			i++;
    		}
    	}else {
    	this.path = fileName;
    	}
    	
    }
    public String getFileName() {
    	if(isDirectory) {
    		return names[index];
    	}else {
    		return path;
    	}
    }
    public int getWidth(){
        return raster.getMaxPixel(0);
    }

    public int getHeight(){
    
        return raster.getMaxPixel(1);
    }

  
     
    
    
    protected Grid createGrid(List<String> properties, Shapefile shp, String gridName){
    	
    	 bounds = shp.getBounds();
    	bounded = true;
    	if(isDirectory) {
    		 raster = new ORaster(FileUtils.applyOutput(directory+"/"+names[index]), bounds);
    	}else {
    	try{
            raster = new ORaster(FileUtils.applyOutput(path), bounds);
        	}catch(Exception e){
        		e.printStackTrace();
        	}
    	}
    	if(crs != null) {
    		raster.setCRS(crs);
    	}
    	grid = GridGenerator.squareGridFromShp(gridName, properties, raster,raster.getXRes(), raster.getYRes(), shp.getBounds());
      
        grid.copy(raster, propMatched);
        return grid;
    }
    
    protected Grid createGrid(List<String> properties, List<Geometry> geometries, String gridName){
    	
    	
    	 bounds = getDoubleBounds(geometries);
    	bounded = true;
    	
    	if(isDirectory) {
   		 raster = new ORaster(FileUtils.applyOutput(directory+"/"+names[index]), bounds);
   	}else {
   	try{
           raster = new ORaster(FileUtils.applyOutput(path), bounds);
       	}catch(Exception e){
       		e.printStackTrace();
       	}
   	}
    	if(crs != null) {
    		raster.setCRS(crs);
    	}
    	grid = GridGenerator.squareGridFromShp(gridName, properties, raster,raster.getXRes(), raster.getYRes(), getBounds(geometries));
       
        grid.copy(raster, propMatched);
        return grid;
    }
    
    protected Grid createGrid(List<String> properties, Geometry geometry, String gridName){
    	
	bounds = getDoubleBounds(geometry);
    	bounded = true;
    	
    	if(isDirectory) {
   		 raster = new ORaster(FileUtils.applyOutput(directory+"/"+names[index]), bounds);
   	}else {
   	try{
           raster = new ORaster(FileUtils.applyOutput(path), bounds);
       	}catch(Exception e){
       		e.printStackTrace();
       	}
   	}
    	if(crs != null) {
    		raster.setCRS(crs);
    	}
    	grid = GridGenerator.squareGridFromShp(gridName, properties, raster,raster.getXRes(), raster.getYRes(), getBounds(geometry));
       
        grid.copy(raster, propMatched);
        return grid;
    }
    
    protected Grid createGrid(List<String> properties, String gridName){
    	
    	
    	if(isDirectory) {
   		 raster = new ORaster(FileUtils.applyOutput(directory+"/"+names[index]));
   	}else {
   	try{
           raster = new ORaster(FileUtils.applyOutput(path));
       	}catch(Exception e){
       		e.printStackTrace();
       	}
   	}
    	if(crs != null) {
    		raster.setCRS(crs);
    	}
    	grid = GridGenerator.squareGridFrom(gridName, properties, raster);
       
        grid.copy(raster, propMatched);
        return grid;

    }
    
   
    
   
    
    private Polygon getBounds(List<Geometry> geometries) {
    	
    	double minX = Double.POSITIVE_INFINITY;
    	double minY = Double.POSITIVE_INFINITY;
    	double maxX = Double.NEGATIVE_INFINITY;
    	double maxY = Double.NEGATIVE_INFINITY;
    	
    	for(Geometry g : geometries) {
    		
    		for(Coordinate c : g.getCoordinates()) {
    			if(c.x > maxX) {
    				maxX = c.x;
    			}
    			if(c.y > maxY) {
    				maxY = c.y;
    			}
    			
    			if(c.x < minX) {
    				minX = c.x;
    			}
    			if(c.x < minY) {
    				minY = c.y;
    			}
    			
    		}
    	}
    	
    	Coordinate[] coords = new Coordinate[5];
    	CoordinateSequence cs = new CoordinateArraySequence(coords);
    	LinearRing lr = new LinearRing(cs, SpatialManager.geometryFactory());
    	Polygon env = new Polygon(lr, null, SpatialManager.geometryFactory());
    	return env;
    	
    }
    
  private Double[] getDoubleBounds(List<Geometry> geometries) {
    	
	  
	  Double[] bounds = new Double[4];
    	double minX = Double.POSITIVE_INFINITY;
    	double minY = Double.POSITIVE_INFINITY;
    	double maxX = Double.NEGATIVE_INFINITY;
    	double maxY = Double.NEGATIVE_INFINITY;
    	
    	for(Geometry g : geometries) {
    		
    		for(Coordinate c : g.getCoordinates()) {
    			if(c.x > maxX) {
    				maxX = c.x;
    			}
    			if(c.y > maxY) {
    				maxY = c.y;
    			}
    			
    			if(c.x < minX) {
    				minX = c.x;
    			}
    			if(c.x < minY) {
    				minY = c.y;
    			}
    			
    		}
    	}
    	bounds[0] = minX;
    	bounds[1] = minY;
    	bounds[2] = maxX;
    	bounds[3] = maxY;

    	return bounds;
    	
    }
  
private Double[] getDoubleBounds(Geometry geometry) {
    	
	  
	  Double[] bounds = new Double[4];
    	double minX = Double.POSITIVE_INFINITY;
    	double minY = Double.POSITIVE_INFINITY;
    	double maxX = Double.NEGATIVE_INFINITY;
    	double maxY = Double.NEGATIVE_INFINITY;
    	
    	
    		
    		for(Coordinate c : geometry.getCoordinates()) {
    			if(c.x > maxX) {
    				maxX = c.x;
    			}
    			if(c.y > maxY) {
    				maxY = c.y;
    			}
    			
    			if(c.x < minX) {
    				minX = c.x;
    			}
    			if(c.y < minY) {
    				minY = c.y;
    			}
    			
    		}
    	
    	bounds[0] = minX;
    	bounds[1] = minY;
    	bounds[2] = maxX;
    	bounds[3] = maxY;

    	return bounds;
    	
    }
    
    private Polygon getBounds(Geometry geometry) {
    	
    	double minX = Double.POSITIVE_INFINITY;
    	double minY = Double.POSITIVE_INFINITY;
    	double maxX = Double.NEGATIVE_INFINITY;
    	double maxY = Double.NEGATIVE_INFINITY;
    	
    	
    		
    		for(Coordinate c : geometry.getCoordinates()) {
    			if(c.x > maxX) {
    				maxX = c.x;
    			}
    			if(c.y > maxY) {
    				maxY = c.y;
    			}
    			
    			if(c.x < minX) {
    				minX = c.x;
    			}
    			if(c.y < minY) {
    				minY = c.y;
    			}
    			
    		}
    	
    	
    	Coordinate[] coords = new Coordinate[5];
    	coords[0] = new Coordinate(minX, minY);
    	coords[1] = new Coordinate(maxX, minY);
    	coords[2] = new Coordinate(maxX, maxY);
    	coords[3] = new Coordinate(minX, maxY);
    	coords[4] = new Coordinate(minX, minY);
    	
    	CoordinateSequence cs = new CoordinateArraySequence(coords);
    	LinearRing lr = new LinearRing(cs, SpatialManager.geometryFactory());
    	Polygon env = new Polygon(lr, null, SpatialManager.geometryFactory());
    	return env;
    	
    }
  

    protected void addProperty(String name, Integer band){
    	if(!propMatched.keySet().contains(name))
    	propMatched.put(name, band);
    }
    protected ORaster raster;
    protected Grid grid;
    protected File sourceFile;
}
