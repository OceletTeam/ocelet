/*
*  Ocelet spatial modelling language.   www.ocelet.org
*  Copyright Cirad 2010-2018
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
package fr.ocelet.datafacer.ocltypes;

import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.gce.geotiff.GeoTiffWriter;
import org.geotools.geometry.Envelope2D;
import org.geotools.referencing.CRS;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import fr.ocelet.datafacer.Datafacer;
import fr.ocelet.runtime.entity.AbstractEntity;
import fr.ocelet.runtime.geom.ocltypes.Cell;
import fr.ocelet.runtime.ocltypes.List;
import fr.ocelet.runtime.raster.Grid;
import fr.ocelet.runtime.raster.GridGenerator;
import fr.ocelet.runtime.util.FileUtils;

/**
 * Datafacer used to save geotiff raster files.
 * 
 * @author Mathieu Castets - Initial contribution
 */
public class RasterExport implements Datafacer{

	@Override
	public String getErrHeader() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void export(List<? extends AbstractEntity> entities, String path, String epsgCode, String... names){
		
		AbstractEntity ae = entities.get(0);
    	Cell cell = (Cell)ae.getSpatialType();
    	Grid grid = cell.getGrid();
		GeneralParameterValue paramValues[] = null; //getInitialParameters();
        File file = new File(FileUtils.applyOutput(path));
        GeoTiffWriter writer = null;
        Double[] wBounds = grid.getWorldBounds();
        
 		//Envelope2D env = GridGenerator.createEnvelope(wBounds[0], wBounds[1],
 		//wBounds[0]+ (grid.getWidth() * grid.getXRes()), wBounds[1] + (grid.getHeight() * grid.getYRes()));
 		
 		Envelope2D env = GridGenerator.createEnvelope(wBounds[0], wBounds[1],
 		wBounds[2], wBounds[3]);
 		env = grid.getEnv();
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
 					raster.setSample(i, j, n, grid.getValue(grid.getBand(names[n]), i, j));
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
       
         writer.dispose();
    	
    }
	
	public void exportLongInt(List<? extends AbstractEntity> entities, String path, String epsgCode, String... names){
		
		AbstractEntity ae = entities.get(0);
    	Cell cell = (Cell)ae.getSpatialType();
    	Grid grid = cell.getGrid();
		GeneralParameterValue paramValues[] = null; //getInitialParameters();
        File file = new File(FileUtils.applyOutput(path));
        GeoTiffWriter writer = null;
        Double[] wBounds = grid.getWorldBounds();
         
 		//Envelope2D env = GridGenerator.createEnvelope(wBounds[0], wBounds[1],
 		//wBounds[0]+ (grid.getWidth() * grid.getXRes()), wBounds[1] + (grid.getHeight() * grid.getYRes()));
 		
 		Envelope2D env = GridGenerator.createEnvelope(wBounds[0], wBounds[1],
 		wBounds[2], wBounds[3]);
 		env = grid.getEnv();
 		WritableRaster raster = GridGenerator.createRasterInt(names.length, grid.getWidth(), grid.getHeight());
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
 					raster.setSample(i, j, n, grid.getValue(grid.getBand(names[n]), i, j).longValue());
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
    	Grid grid = cell.getGrid();

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
        
         try {
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

}
