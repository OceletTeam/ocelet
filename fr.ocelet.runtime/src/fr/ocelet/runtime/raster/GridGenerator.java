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
import org.opengis.geometry.DirectPosition;

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
		//int cellWidth = (int)(Math.round(width / (size * 2))) + 4;
		int cellWidth = (int)(Math.round(((width / 2)  / l) + ((width / 2) / size)));

		int cellHeight = (int) (Math.round(height / (l))) + 4;	
		//cellWidth = cellWidth + (cellWidth / 4) + 4;
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
		grid.setCellShapeType("HEXAGONAL");
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
	public static Grid squareGridFromShp(String name, List<String> props,ORaster raster, double xRes, double yRes, double minX, double minY, double maxX, double maxY){


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


		int[] rasterCoordMin = new int[]{raster.getMinPixel(0), raster.getMinPixel(1)};
		int[] rasterCoordMax = new int[]{raster.getMaxPixel(0), raster.getMaxPixel(1)};

		double[] rasterWorldMin = new double[]{raster.getMinimum(0), raster.getMinimum(1)};
		double[] rasterWorldMax = new double[]{raster.getMaximum(0), raster.getMaximum(1)};

		DirectPosition dpLow = raster.getGridGeometry().getEnvelope().getLowerCorner();
		DirectPosition dpUp = raster.getGridGeometry().getEnvelope().getUpperCorner();
		//raster.worldToGrid(minX, minY);
		//raster.worldToGrid(maxX, maxY);
		double[] lowerCorner = dpLow.getCoordinate();
		double[] upperCorner = dpUp.getCoordinate();

	/*	System.out.println("lower "+lowerCorner[0]+" "+lowerCorner[1]);
		System.out.println("upper "+upperCorner[0]+" "+upperCorner[1]);
		System.out.println(" "+minX+" "+minY+" "+maxX+" "+maxY );*/
		double nminX = lowerCorner[0];
		double nminY = lowerCorner[1];
		double nmaxX = upperCorner[0];
		double nmaxY = upperCorner[1];
		
		
		
		
		
		if(lowerCorner[0] < minX && lowerCorner[1] < minY && upperCorner[0] > maxX && upperCorner[1] > maxY){
			
			int[] coordMin = raster.worldToGrid(minX, maxY);
			int[] coordMax = raster.worldToGrid(maxX, minY);
			
			double[] minDouble = raster.gridToWorld(coordMin[0], coordMax[1]);
			double[] maxDouble = raster.gridToWorld(coordMax[0], coordMin[1]);

			nminX = minDouble[0];
			nminY = minDouble[1];
			nmaxX = maxDouble[0];
			nmaxY = maxDouble[1];
			
		}else{
		
		
		
		
		if(lowerCorner[0] < minX){
			
			double init = Math.abs(lowerCorner[0]);
			double shp = Math.abs(minX);
			int gap = 0;
			if(lowerCorner[0] < 0 && minX < 0){
				
				gap = (int)Math.round((init - shp) / xRes);
			}else{
				gap = (int)Math.round((minX - lowerCorner[0]) / xRes);
			}

			nminX = nminX + gap * xRes;
		}else{
			nminX = minX;
		}
		if(lowerCorner[1] < minY){
			double init = Math.abs(lowerCorner[1]);
			double shp = Math.abs(minY);
			int gap = 0;
			if(lowerCorner[1] < 0 && minY < 0){
				
				gap = (int)Math.round((init - shp) / yRes);
			}else{
				gap = (int)Math.round((minY - lowerCorner[1]) / yRes);
			}

			nminY = nminY + gap * yRes;
		}else{
			nminY = minY;
		}

		if(upperCorner[0] > maxX){
			double init = Math.abs(upperCorner[0]);
			double shp = Math.abs(maxX);
			int gap = 0;
			if(upperCorner[0] < 0 && maxX < 0){
				
				gap = (int)Math.round((shp - init) / xRes);
			}else{
				gap = (int)Math.round((upperCorner[0] - maxX) / xRes);
			}

			nmaxX = nmaxX - gap * xRes;
		}else{
			nmaxX = maxX;
		}

		if(upperCorner[1] > maxY){
			
			double init = Math.abs(upperCorner[1]);
			double shp = Math.abs(maxY);
			double gap = 0;
			if(upperCorner[1] < 0 && maxY < 0){
				
				gap = (int)Math.round((shp - init) / yRes);
			}else{
				gap = (int)Math.round((upperCorner[1] - maxY) / yRes);
			}
			nmaxY = nmaxY - gap * yRes;

		}else{
			nmaxY = maxY;
		}
		}
		
		/*if(lowerCorner[1] < minY){
			int gap =(int) Math.round((minY - lowerCorner[1]) / yRes);
			nminY = nminY + gap * yRes;
		}else{
			nminY = minY;
		}

		if(upperCorner[0] > maxX){
			int gap =(int) Math.round((upperCorner[0] - maxX) / xRes);
			nmaxX = nmaxX - gap * xRes;
		}else{
			nmaxX = maxX;
		}

		if(upperCorner[1] > maxY){
			int gap =(int) Math.round((upperCorner[1] - maxY) / yRes);

			nmaxY = nmaxY - gap * yRes;

		}else{
			nmaxY = maxY;
		}*/
		//System.out.println("NEW "+nminX+" "+nminY+" "+nmaxX+" "+nmaxY );
		double newMinX = nminX - 2*xRes;
		double newMinY = nminY - 2*yRes;
		double newMaxX = nmaxX + 2* xRes;// newMinX + (cellWidth * xRes);
		double newMaxY = nmaxY + 2* yRes;//newMinY + (cellHeight * yRes);

		double width = newMaxX - newMinX;
		double height = newMaxY - newMinY;

		int cellWidth = (int)(Math.round((width) / xRes));
		int cellHeight = (int) (Math.round((height) / yRes));	
		//newMinX = newMinX - xRes / 2;
		//newMinY = newMinY - xRes / 2;
		//newMaxX = newMinX + cellWidth * xRes;
		//newMaxY = newMinY + cellHeight * yRes;

		Envelope2D env = createEnvelope(newMinX, newMinY,newMaxX, newMaxY);
		WritableRaster newRaster = createRaster(index, cellWidth, cellHeight);
		GridCoverage2D coverage =  createCoverage(name, newRaster, env);
		Grid grid = new Grid(cellWidth, cellHeight, coverage.getGridGeometry());
		grid.setRasterProperties(initProps);
		grid.setRaster(newRaster);
		grid.setXRes(xRes);
		grid.setYRes(yRes);

		grid.setWorldBounds(new Double[]{newMinX, newMinY, newMaxX, newMaxY});
		grid.setEnv(env);


		return grid;


	}

	public static Grid squareGridFromShp(String name, List<String> props,ORaster raster, double xRes, double yRes,Double[] bounds){	

		double minX = bounds[0];
		double minY = bounds[1];
		double maxX = bounds[2];
		double maxY = bounds[3];

		return squareGridFromShp(name, props,raster, xRes, yRes, minX, minY, maxX, maxY);

	}

	//public static Grid squareGridFrom(String name, List<String> props,ORaster raster){//, double xRes, double yRes,Double[] bounds){	


	//  return squareGridFrom(name, props, raster);

	//}

	public static Grid squareGridFrom(String name, List<String> props,ORaster initRaster ){


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
		int[] rasterCoordMin = new int[]{initRaster.getMinPixel(0), initRaster.getMinPixel(1)};
		int[] rasterCoordMax = new int[]{initRaster.getMaxPixel(0), initRaster.getMaxPixel(1)};

		double[] rasterWorldMin = new double[]{initRaster.getMinimum(0), initRaster.getMinimum(1)};
		double[] rasterWorldMax = new double[]{initRaster.getMaximum(0), initRaster.getMaximum(1)};

		double nminX = rasterWorldMin[0];
		double nminY = rasterWorldMin[1];
		double nmaxX = rasterWorldMax[0];
		double nmaxY = rasterWorldMax[1];

		double width = nmaxX - nminX;
		double height = nmaxY - nminY;

		int cellWidth = rasterCoordMax[0] - rasterCoordMin[0] + 1;
		int cellHeight = rasterCoordMax[1] - rasterCoordMin[1] + 1;		
		//System.out.println(cellWidth+" "+cellHeight+" total : "+cellWidth * cellHeight);
		double newMinX = nminX;
		double newMinY = nminY;
		double newMaxX = nmaxX;
		double newMaxY = nmaxY;
		System.out.println("raster : "+initRaster.getWritableRaster().getWidth()+" "+initRaster.getWritableRaster().getHeight());
		System.out.println(cellWidth+" "+cellHeight);
		Envelope2D env = createEnvelope(newMinX, newMinY,newMaxX, newMaxY);
		env = initRaster.getGridGeometry().getEnvelope2D();
		WritableRaster raster = createRaster(index, cellWidth, cellHeight);
		GridCoverage2D coverage =  createCoverage(name, raster, env);
		//coverage = initRaster.getGridGeometry().getc
		Grid grid = new Grid(cellWidth, cellHeight, coverage.getGridGeometry());
		grid.setRasterProperties(initProps);
		grid.setRaster(raster);
		grid.setXRes(initRaster.getXRes());
		grid.setYRes(initRaster.getYRes());

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

		double nminX = minX;
		double nminY = minY;
		double nmaxX = maxX;
		double nmaxY = maxY;

		double width = (nmaxX - nminX);
		double height = (nmaxY - nminY);

		double l = Math.sqrt(3) / 2 * size;
		int cellWidth = (int)(Math.round((width * 2 / size)) - 1);
		int cellHeight = (int) (Math.round(height / l));	
		System.out.println(cellWidth+" "+cellHeight);

		if(cellWidth % 2 == 0){
			cellWidth++;
		}

		if(cellHeight % 2 == 0){
			cellHeight++;
		}
		System.out.println(cellWidth+" "+cellHeight);

		double newMinX = nminX;
		double newMinY = nminY;
		double newMaxX = newMinX + ((cellWidth /2 + 1) * size);
		double newMaxY = newMinY + (cellHeight * l);
		//cellWidth = cellWidth + (cellWidth / 4);

		Envelope2D env = createEnvelope(newMinX, newMinY, newMaxX, newMaxY);
		WritableRaster raster = createRaster(index, cellWidth, cellHeight);
		GridCoverage2D coverage =  createCoverage(name, raster, env);
		Grid grid = new Grid(cellWidth, cellHeight, coverage.getGridGeometry());
		grid.setRasterProperties(initProps);
		grid.setRaster(raster);
		grid.setXRes(size);
		grid.setYRes(size);
		grid.setWorldBounds(new Double[]{newMinX, newMinY, newMaxX, newMaxY});
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
