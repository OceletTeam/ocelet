package fr.ocelet.runtime.raster;

import org.geotools.coverage.grid.GridCoverageFactory;
import com.sun.media.jai.codecimpl.util.RasterFactory;
import com.vividsolutions.jts.geom.Coordinate;

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
		grid.setWorldBounds(new Double[]{nminX, nminY, nmaxX, nmaxY});
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
			/*double nminX = minX - xRes;
			double nminY = minY - yRes;
			double nmaxX = maxX + xRes;
			double nmaxY = maxY +yRes;
			*/
			double nminX = minX;
			double nminY = minY;
			double nmaxX = maxX;
			double nmaxY = maxY;
		double width = nmaxX - nminX;
		double height = nmaxY - nminY;
		
		int cellWidth = (int)(Math.round(width / (xRes)));
		int cellHeight = (int) (Math.round(height / (yRes)));	
		
		//System.out.println(cellWidth+" "+cellHeight+" total : "+cellWidth * cellHeight);
		double newMinX = nminX - xRes / 2;
		double newMinY = nminY - xRes / 2;
		double newMaxX = newMinX + (cellWidth * xRes) + (xRes / 2);
		double newMaxY = newMinY + (cellHeight * yRes) + (xRes / 2);
		
		Envelope2D env = createEnvelope(newMinX, newMinY,newMaxX, newMaxY);
		WritableRaster raster = createRaster(index, cellWidth, cellHeight);
		GridCoverage2D coverage =  createCoverage(name, raster, env);
		Grid grid = new Grid(cellWidth, cellHeight, coverage.getGridGeometry());
		grid.setRasterProperties(initProps);
		grid.setRaster(raster);
		grid.setXRes(xRes);
		grid.setYRes(yRes);
		
		grid.setWorldBounds(new Double[]{newMinX, newMinY, newMaxX, newMaxY});
		grid.setEnv(env);
		/*System.out.println("CREATE GRID");
		Coordinate min = grid.gridCoordinate(0, 0);
		System.out.println(newMinX+"   "+newMinY);
		System.out.println(min+"    "+grid.gridCoordinate(min.x, min.y)[0]+"  "+grid.gridCoordinate(min.x, min.y)[1]);
		System.out.println();
		Coordinate max = grid.gridCoordinate(cellWidth - 1, cellHeight - 1);
		System.out.println((cellWidth - 1)+"  "+(cellHeight - 1));
		System.out.println(min+"    "+grid.gridCoordinate(max.x, max.y)[0]+"  "+grid.gridCoordinate(max.x, max.y)[1]);
		System.out.println();*/

		
		return grid;
		
		
	}
    
    public static Grid squareGridFrom(String name, List<String> props,ORaster raster, double xRes, double yRes,Double[] bounds){	
	
    	double minX = bounds[0];
    	double minY = bounds[1];
    	double maxX = bounds[2];
    	double maxY = bounds[3];
    
	    return squareGrid(name, props, xRes, yRes, minX, minY, maxX, maxY);
	
	}
    
    public static Grid squareGridFrom(String name, List<String> props,ORaster initRaster, double xRes, double yRes, double minX, double minY, double maxX, double maxY){
		
		
		//Grid grid = new Grid();
	HashMap<String, Integer> initProps = new HashMap<String, Integer>();
		
		int index = 0;
		for(String s : props){
			initProps.put(s, index);
			index ++;
		}
			/*double nminX = minX - xRes;
			double nminY = minY - yRes;
			double nmaxX = maxX + xRes;
			double nmaxY = maxY +yRes;
			*/
		/*	double nminX = minX;
			double nminY = minY;
			double nmaxX = maxX;
			double nmaxY = maxY;
			*/
			int[] rasterCoordMin = initRaster.worldToGrid(minX, minY);
			int[] rasterCoordMax = initRaster.worldToGrid(maxX, maxY);
			
			double[] rasterWorldMin = initRaster.gridToWorld(rasterCoordMin[0], rasterCoordMin[1]);
			double[] rasterWorldMax = initRaster.gridToWorld(rasterCoordMax[0], rasterCoordMax[1]);

			double nminX = rasterWorldMin[0];
			double nminY = rasterWorldMin[1];
			double nmaxX = rasterWorldMax[0];
			double nmaxY = rasterWorldMax[1];

		double width = nmaxX - nminX;
		double height = nmaxY - nminY;
		
		int cellWidth = (int)(Math.round(width / (xRes)));
		int cellHeight = (int) (Math.round(height / (yRes)));	
		
		//System.out.println(cellWidth+" "+cellHeight+" total : "+cellWidth * cellHeight);
		double newMinX = nminX;
		double newMinY = nminY;
		double newMaxX = newMinX + (cellWidth * xRes);
		double newMaxY = newMinY + (cellHeight * yRes);
		
		Envelope2D env = createEnvelope(newMinX, newMinY,newMaxX, newMaxY);
		WritableRaster raster = createRaster(index, cellWidth, cellHeight);
		GridCoverage2D coverage =  createCoverage(name, raster, env);
		Grid grid = new Grid(cellWidth, cellHeight, coverage.getGridGeometry());
		grid.setRasterProperties(initProps);
		grid.setRaster(raster);
		grid.setXRes(xRes);
		grid.setYRes(yRes);
		
		grid.setWorldBounds(new Double[]{nminX, nminY, nmaxX, nmaxY});
		grid.setEnv(env);
		return grid;
		
		
	}
    
    
    
	public static Grid triangularGrid(String name, List<String> props, double size, double minX, double minY, double maxX, double maxY){
		
		
		
			
		
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
		
	double width = (nmaxX - nminX) * 2;
	double height = (nmaxY - nminY) * 2;
	
		double l = Math.sqrt(size * size - ((size * size) / 4 ));
		int cellWidth = (int)(Math.round(width / (size * 2))) + 4;
		int cellHeight = (int) (Math.round(height / l)) + 4;	
		cellWidth = cellWidth + (cellWidth / 4) + 4;
	
		double newMinX = minX - (4 * size);
		double newMinY = minY - (4 * size);
		
		Envelope2D env = createEnvelope(newMinX, newMinY, newMinX + (cellWidth * size * 2), newMinY + (cellHeight * size * 2));
		WritableRaster raster = createRaster(index, cellWidth, cellHeight);
		GridCoverage2D coverage =  createCoverage(name, raster, env);
		Grid grid = new Grid(cellWidth - 4, cellHeight - 4, coverage.getGridGeometry());
		grid.setRasterProperties(initProps);
		grid.setRaster(raster);
		grid.setXRes((int)(size * 2));
		grid.setYRes((int)(size * 2));
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
public static Grid triangularGrid(String name, List<String> props, double size,Double[] bounds){
			
    	double minX = bounds[0];
    	double minY = bounds[1];
    	double maxX = bounds[2];
    	double maxY = bounds[3];
		
	return triangularGrid(name, props, size, minX, minY, maxX, maxY);
	
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
