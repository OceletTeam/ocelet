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
import fr.ocelet.runtime.raster.GridManager;

import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.gce.geotiff.GeoTiffWriter;
import org.geotools.geometry.Envelope2D;
import org.geotools.referencing.CRS;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequence;

public class RasterFile{
	
	private KeyMap<String, Integer> propMatched = new KeyMap<String, Integer>();
	
    public RasterFile(String fileName){
    	try{
        raster = new ORaster(FileUtils.applyOutput(fileName));
    	}catch(Exception e){
    		
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
    public void setFileName(String fileName){
    	try{
        raster = new ORaster(FileUtils.applyOutput(fileName));
    	}catch(Exception e){
    		
    	}
    }
    
    public int getWidth(){
        return raster.getMaxPixel(0);
    }

    public int getHeight(){
    
        return raster.getMaxPixel(1);
    }

    public GridGeometry2D getGridGeometry(){
    
        return raster.getGridGeometry();
    }

    public WritableRaster getRaster(){
    
        return raster.getWritableRaster();
    }
   
    
    
    protected Grid createGrid(List<String> properties, Shapefile shp, String gridName){
    	grid = GridGenerator.squareGridFromShp(gridName, properties, raster,raster.getXRes(), raster.getYRes(), shp.getBounds());
        fr.ocelet.runtime.raster.GridManager.getInstance().add(grid);
        grid.copy(raster, propMatched);
        return grid;
    }
    
    protected void createGrid(List<String> properties, String gridName){
    	grid = GridGenerator.squareGridFrom(gridName, properties, raster);
        fr.ocelet.runtime.raster.GridManager.getInstance().add(grid);
        grid.copy(raster, propMatched);
        

    }
    
    protected void createGrid(List<String> properties, String gridName, int minX, int minY, int maxX, int maxY){
    	
    }
    
    
    public void export(List<? extends AbstractEntity> entities, String path, String epsgCode, String... names){
    	
    	
    	
    	
    	AbstractEntity ae = entities.get(0);
    	Cell cell = (Cell)ae.getSpatialType();
    	Grid grid = GridManager.getInstance().get(cell.getNumGrid());
 GeneralParameterValue paramValues[] = null; //getInitialParameters();
    	 
         File file = new File(FileUtils.applyOutput(path));
         GeoTiffWriter writer = null;
         
         Double[] wBounds = grid.getWorldBounds();
         
 		Envelope2D env = GridGenerator.createEnvelope(wBounds[0], wBounds[1],
 		wBounds[0]+ (grid.getWidth() * grid.getXRes()), wBounds[1] + (grid.getHeight() * grid.getYRes()));
 		WritableRaster raster = GridGenerator.createRaster(names.length, grid.getWidth(), grid.getHeight());
 		CoordinateReferenceSystem crs = null;
		try {
			crs = CRS.decode(epsgCode);
		} catch (NoSuchAuthorityCodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FactoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 		env.setCoordinateReferenceSystem(crs);
 		for(int i = 0; i < grid.getWidth(); i ++){
 			
 			for(int j = 0; j < grid.getHeight(); j ++){
 				
 				for(int n = 0 ; n < names.length; n ++){
 					raster.setSample(i, j, n, grid.getValue(names[n], i, j));
 					//System.out.println( grid.getValue(names[n], i, j));
 				}
 			}
 			
 		}
         
       
 		 
 	 		
         try{
         
             writer = new GeoTiffWriter(file);
         }
         catch(IOException ex){
         
        	 ex.printStackTrace();
         }
         	GridCoverage2D cov = (new GridCoverageFactory()).create(path, raster, env);
        
         try{
         
             writer.write(cov, paramValues);
         }
         catch(IllegalArgumentException ex){
         
        	 ex.printStackTrace();
         }
         catch(IOException ex){
         
        	 ex.printStackTrace();
         }
         catch(IndexOutOfBoundsException ex){
         
        	 ex.printStackTrace();
         }
         System.out.println((new StringBuilder("Raster File created in : ")).append(path).toString());
         writer.dispose();
    	
    }
    
    public void export(List<? extends AbstractEntity> entities, String path, String epsgCode){
    	 AbstractEntity ae = entities.get(0);
    	Cell cell = (Cell)ae.getSpatialType();
    	Grid grid = GridManager.getInstance().get(cell.getNumGrid());

 GeneralParameterValue paramValues[] = null; //getInitialParameters();
    	 
         File file = new File(FileUtils.applyOutput(path));
         GeoTiffWriter writer = null;
         
         CoordinateReferenceSystem crs = null;
 		try {
 			crs = CRS.decode(epsgCode);
 		} catch (NoSuchAuthorityCodeException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		} catch (FactoryException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}
  		grid.getEnv().setCoordinateReferenceSystem(crs);
    	try{
         
             writer = new GeoTiffWriter(file);
         }
         catch(IOException ex){
         
        	 ex.printStackTrace();
         }
         	GridCoverage2D cov = (new GridCoverageFactory()).create(path, grid.getRaster(), grid.getEnv());
        
         try{
         
             writer.write(cov, paramValues);
         }
         catch(IllegalArgumentException ex){
         
        	 ex.printStackTrace();
         }
         catch(IOException ex){
         
        	 ex.printStackTrace();
         }
         catch(IndexOutOfBoundsException ex){
         
        	 ex.printStackTrace();
         }
         System.out.println((new StringBuilder("Raster File created in : ")).append(path).toString());
         writer.dispose();
    }

    protected void addProperty(String name, Integer band){
    	if(!propMatched.keySet().contains(name))
    	propMatched.put(name, band);
    }
    protected ORaster raster;
    protected Grid grid;
    protected File sourceFile;
}
