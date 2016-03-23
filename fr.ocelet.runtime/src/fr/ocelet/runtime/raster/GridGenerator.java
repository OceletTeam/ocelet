package fr.ocelet.runtime.raster;

import org.geotools.coverage.grid.GridCoverageFactory;
import com.sun.media.jai.codecimpl.util.RasterFactory;


import fr.ocelet.runtime.ocltypes.List;
import fr.ocelet.runtime.relation.OcltRole;

import java.awt.image.BandedSampleModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferDouble;
import java.awt.image.DataBufferFloat;
import java.awt.image.DataBufferInt;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.util.HashMap;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.Envelope2D;

public class GridGenerator {

	public void initGrid(OcltRole role, List<String> properties){
		
		Grid grid = new Grid();
		HashMap<String, Integer> initProps = new HashMap<String, Integer>();
		
		int index = 0;
		for(String s : properties){
			initProps.put(s, index);
			index ++;
		}
		
		grid.setRasterProperties(initProps);
		
	}
	
	public static Grid hexagonalGrid(String name, List<String> props, double size, double minX, double minY, double maxX, double maxY){
		
		
		
			
	/*HashMap<String, Integer> initProps = new HashMap<String, Integer>();
		
		int index = 0;
		for(String s : props){
			initProps.put(s, index);
			index ++;
		}
		
				
		double width = maxX - minX;
		double height = maxY - minY;
		int cellWidth = (int)(width / (size * 2));
		int cellHeight = (int) (height / (size * 2));
		Envelope2D env = createEnvelope(minX, minY, maxX, maxY);
		WritableRaster raster = createRaster(index, cellWidth, cellHeight);
		GridCoverage2D coverage =  createCoverage(name, raster, env);
		Grid grid = new Grid(cellWidth, cellHeight, coverage.getGridGeometry());
		grid.setInitRasterProperties(initProps);
		grid.setInitRaster(raster);
		grid.setXRes((int)(size * 2));
		grid.setYRes((int)(size * 2));*/
		
		//Grid grid = new Grid();
	HashMap<String, Integer> initProps = new HashMap<String, Integer>();
		
		int index = 0;
		for(String s : props){
			initProps.put(s, index);
			index ++;
		}
	
		double nminX = minX - size * 2;
		double nminY = minY - size * 2;
		double nmaxX = maxX + size * 2;
		double nmaxY = maxY +size * 2;
	double width = nmaxX - nminX;
	double height = nmaxY - nminY;
		double l = Math.sin(Math.PI/3) * (size * 2);
		int cellWidth = (int)(Math.round(width / (size * 2))) + 4;
		int cellHeight = (int) (Math.round(height / (l))) + 4;	
		cellWidth = cellWidth + (cellWidth / 4) + 4;
	
		double newMinX = minX - (4 * size);
		double newMinY = minY - (4 * size);
		
		Envelope2D env = createEnvelope(newMinX, newMinY, newMinX + (cellWidth * size * 2), newMinY + (cellHeight * l));
		WritableRaster raster = createRaster(index, cellWidth, cellHeight);
		GridCoverage2D coverage =  createCoverage(name, raster, env);
		Grid grid = new Grid(cellWidth - 4, cellHeight - 4, coverage.getGridGeometry());
		grid.setRasterProperties(initProps);
		grid.setRaster(raster);
		grid.setXRes((int)(size * 2));
		grid.setYRes((int)(l));
		grid.setWorldBounds(new Double[]{minX, minY, maxX, maxY});
		grid.setEnv(env);
		return grid;
		
		
	}
	
    public static Grid squareGrid(String name, List<String> props, double xRes, double yRes,Double[] bounds){	
	
    	double minX = bounds[0];
    	double minY = bounds[1];
    	double maxX = bounds[2];
    	double maxY = bounds[3];
    
	    return squareGrid(name, props, xRes, yRes, minX, minY, maxX, maxY);
	
	}
    
    public static Grid squareGrid(String name, List<String> props, double xRes, double yRes, double minX, double minY, double maxX, double maxY){
		
		
		//Grid grid = new Grid();
	HashMap<String, Integer> initProps = new HashMap<String, Integer>();
		
		int index = 0;
		for(String s : props){
			initProps.put(s, index);
			index ++;
		}
			double nminX = minX - xRes;
			double nminY = minY - yRes;
			double nmaxX = maxX + xRes;
			double nmaxY = maxY +yRes;
			
			/*double nminX = minX;
			double nminY = minY;
			double nmaxX = maxX;
			double nmaxY = maxY;*/
		double width = nmaxX - nminX;
		double height = nmaxY - nminY;
		
		int cellWidth = (int)(Math.round(width / (xRes))) + 4;
		int cellHeight = (int) (Math.round(height / (yRes))) + 4;	
		
		//System.out.println(cellWidth+" "+cellHeight+" total : "+cellWidth * cellHeight);
		double newMinX = nminX - (2 * xRes);
		double newMinY = nminY - (2 * yRes);
		double newMaxX = newMinX + (cellWidth * xRes);
		double newMaxY = newMinY + (cellHeight * yRes);
		
		Envelope2D env = createEnvelope(newMinX, newMinY,newMaxX, newMaxY);
		WritableRaster raster = createRaster(index, cellWidth, cellHeight);
		GridCoverage2D coverage =  createCoverage(name, raster, env);
		Grid grid = new Grid(cellWidth - 4, cellHeight - 4, coverage.getGridGeometry());
		grid.setRasterProperties(initProps);
		grid.setRaster(raster);
		grid.setXRes(xRes);
		grid.setYRes(yRes);
		grid.setWorldBounds(new Double[]{nminX, nminY, nmaxX, nmaxY});
		grid.setEnv(env);
		return grid;
		
		
	}
	
    public static Grid hexagonalGrid(String name, List<String> props, double size,Double[] bounds){
			
    	double minX = bounds[0];
    	double minY = bounds[1];
    	double maxX = bounds[2];
    	double maxY = bounds[3];
		
	return hexagonalGrid(name, props, size, minX, minY, maxX, maxY);
	
	}
	
	public static GridCoverage2D createSquares(int numBand, String name,double size, double minX, double minY, double maxX, double maxY){
		
		
		double width = maxX - minX;
		double height = maxY - minY;
		int cellWidth = (int)(width / size);
		int cellHeight = (int) (height / size);		
		
		Envelope2D env = createEnvelope(minX, minY, maxX, maxY);
		WritableRaster raster = createRaster(numBand, cellWidth, cellHeight);
		return createCoverage(name, raster, env);
		
	}

	public static GridCoverage2D createRegularHexagons(int numBand, String name,double size, double minX, double minY, double maxX, double maxY){
	
		double width = maxX - minX;
		double height = maxY - minY;
		int cellWidth = (int)(width / (size * 2));
		int cellHeight = (int) (height / (size * 2));
		Envelope2D env = createEnvelope(minX, minY, maxX, maxY);
		WritableRaster raster = createRaster(numBand, cellWidth, cellHeight);
		return createCoverage(name, raster, env);
		
	}


	public static GridCoverage2D createCoverage(String name, WritableRaster raster, Envelope2D env){

		
		CharSequence cs = name;
		GridCoverageFactory gcf = new GridCoverageFactory();
		return gcf.create(cs, raster, env);

	}


	public static WritableRaster createRaster(int numBand, int cellWidth, int cellHeight){
		
		java.awt.image.DataBuffer db2 = new DataBufferDouble(1, numBand);
		
		SampleModel sample = RasterFactory.createBandedSampleModel(DataBuffer.TYPE_DOUBLE, cellWidth, cellHeight, numBand);
		Raster nr = Raster.createRaster(sample, db2, null);
		//Raster nr = Raster.createBandedRaster(, cellWidth, cellHeight, numBand, null);
		//BandedSampleModel bsm = new BandedSampleModel
		//RasterFactory.createBandedRaster(dataBuffer, width, height, scanlineStride, bankIndices, bandOffsets, location)
		return nr.createCompatibleWritableRaster(); 

	}


	public static Envelope2D createEnvelope(double minX, double minY, double width, double height){
	

		DirectPosition2D dpMin = new DirectPosition2D(width, height);
		DirectPosition2D dpMax = new DirectPosition2D(minX, minY);
		Envelope2D envelope = new Envelope2D(dpMin , dpMax);
		
		return envelope;
	}
}
