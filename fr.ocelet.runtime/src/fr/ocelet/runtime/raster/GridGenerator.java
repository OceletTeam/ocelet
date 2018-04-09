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

import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.processing.Operations;

import com.sun.media.jai.codecimpl.util.RasterFactory;
import com.vividsolutions.jts.geom.Coordinate;

import fr.ocelet.runtime.geom.SpatialManager;
import fr.ocelet.runtime.geom.ocltypes.Polygon;
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
import org.opengis.referencing.crs.CoordinateReferenceSystem;

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

		HashMap<String, Integer> initProps = new HashMap<String, Integer>();

		int index = 0;
		for(String s : props){
			initProps.put(s, index);
			index ++;
		}

		double rectangleWidth = size + size / 2;
		double rectangleHeight = 2 * Math.sqrt( size * size - (size / 2) * (size / 2));
		double nminX = minX - rectangleWidth;
		double nminY = minY - rectangleHeight / 2;
		double nmaxX = maxX + rectangleWidth;
		double nmaxY = maxY+ rectangleHeight / 2; ;
		double width = nmaxX - nminX;
		double height = nmaxY - nminY;
		int col = (int) Math.round(width / rectangleWidth);
		int row = (int) Math.round(height / rectangleHeight);
		if(row % 2 == 0){
			row++;
		}
		
		width = col * rectangleWidth;
		height = row * rectangleHeight;
		nmaxX = nminX + width;
		nmaxY = nminY + height;
		
		double[] center = center(minX, minY, maxX, maxY, nminX, nminY, nmaxX, nmaxY);
		nminX = center[0];
		nminY = center[1];
		nmaxX = center[2];
		nmaxY = center[3];
		
		
		double hexaMinY = nminY - rectangleHeight / 2;
		Envelope2D env = createEnvelope(nminX, nminY, nmaxX, nmaxY);
		WritableRaster raster = createRaster(index, col, row);
		GridCoverage2D coverage =  createCoverage(name, raster, env);
		Grid grid = new Grid(name, col, row, coverage.getGridGeometry());
		grid.setRasterProperties(initProps);
		grid.setRaster(raster);
		grid.setXRes(size);
		grid.setYRes(rectangleHeight);
		grid.setWorldBounds(new Double[]{nminX, hexaMinY, nmaxX + size / 2, nmaxY});
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
		double width = nmaxX - nminX;
		double height = nmaxY - nminY;
		
		int iWidth = (int) Math.round(width);
		int iHeight = (int) Math.round(height);
		int cellWidth = 0;
		if(width - iWidth < 0.5) {
			cellWidth = (int)(Math.round(width / (xRes)) + 1);
		}else {
			cellWidth = (int)(Math.round(width / (xRes)));
		}
		
		int cellHeight = 0;
		
		if(height - iHeight < 0.5) {
			cellHeight = (int)(Math.round(height / (yRes)) + 1);
		}else {
			cellHeight = (int)(Math.round(height / (yRes)));
		}
		double newMinX = nminX;
		double newMinY = nminY;
		double newMaxX = newMinX + (cellWidth * xRes);
		double newMaxY = newMinY + (cellHeight * yRes);
		
		double[] center = center(minX, minY, maxX, maxY, newMinX, newMinY, newMaxX, newMaxY);
		
		newMinX = center[0];
		newMinY = center[1];
		newMaxX = center[2];
		newMaxY = center[3];
	
		while(newMaxX <= maxX){
			
			cellWidth++;
			 newMaxX = newMinX + (cellWidth * xRes);
			 center = center(minX, minY, maxX, maxY, newMinX, newMinY, newMaxX, newMaxY);
				
				newMinX = center[0];
				newMinY = center[1];
				newMaxX = center[2];
				newMaxY = center[3];
		}
		while(newMaxY <= maxY) {
		
			cellHeight++;
			 newMaxY = newMinY + (cellHeight * yRes);
			 center = center(minX, minY, maxX, maxY, newMinX, newMinY, newMaxX, newMaxY);
				
				newMinX = center[0];
				newMinY = center[1];
				newMaxX = center[2];
				newMaxY = center[3];
		}
		
		
	
		 
		Envelope2D env = createEnvelope(newMinX, newMinY,newMaxX, newMaxY);
		//env.setCoordinateReferenceSystem();
		WritableRaster raster = createRaster(index, cellWidth, cellHeight);
		GridCoverage2D coverage =  createCoverage(name, raster, env);
		Grid grid = new Grid(name,cellWidth, cellHeight, coverage.getGridGeometry());
		grid.setRasterProperties(initProps);
		grid.setRaster(raster);
		grid.setXRes(xRes);
		grid.setYRes(yRes);
		grid.setWorldBounds(new Double[]{newMinX, newMinY, newMaxX, newMaxY});
		grid.setEnv(env);
		grid.setCellShapeType("QUADRILATERAL");

		return grid;

	}
	
	public static Grid readSquareGrid(String name, List<String> props,CoordinateReferenceSystem crs, double xRes, double yRes,Double[] bounds){	

		double minX = bounds[0];
		double minY = bounds[1];
		double maxX = bounds[2];
		double maxY = bounds[3];
		return readSquareGrid(name, props, crs,xRes, yRes, minX, minY, maxX, maxY);

	}
	public static Grid readSquareGrid(String name, List<String> props, CoordinateReferenceSystem crs,double xRes, double yRes, double minX, double minY, double maxX, double maxY){


		HashMap<String, Integer> initProps = new HashMap<String, Integer>();

		int index = 0;
		for(String s : props){
			initProps.put(s, index);
			index ++;
		}
		double nminX = minX - xRes;
		double nminY = minY - yRes;
		double nmaxX = maxX;
		double nmaxY = maxY;
		double width = nmaxX - nminX;
		double height = nmaxY - nminY;

		int cellWidth = (int)(Math.round(width / (xRes))) + 2;
		int cellHeight = (int) (Math.round(height / (yRes))) + 2;	
		double newMinX = nminX;
		double newMinY = nminY;
		double newMaxX = newMinX + (cellWidth * xRes);
		double newMaxY = newMinY + (cellHeight * yRes);
		Envelope2D env = createEnvelope(newMinX, newMinY,newMaxX, newMaxY);
		//env.setCoordinateReferenceSystem();
		WritableRaster raster = createRaster(index, cellWidth, cellHeight);
		GridCoverage2D coverage =  createCoverage(name, raster, env);
		Grid grid = new Grid(name,cellWidth, cellHeight, coverage.getGridGeometry());
		grid.setRasterProperties(initProps);
		grid.setRaster(raster);
		grid.setXRes(xRes);
		grid.setYRes(yRes);
		grid.setWorldBounds(new Double[]{newMinX, newMinY, newMaxX, newMaxY});
		grid.setEnv(env);
		grid.setCellShapeType("QUADRILATERAL");

		return grid;

	}
	public static Grid squareGridFromShp(String name, List<String> props,ORaster raster, double xRes, double yRes, double minX, double minY, double maxX, double maxY){

		
		
		HashMap<String, Integer> initProps = new HashMap<String, Integer>();

		int index = 0;
		for(String s : props){
			initProps.put(s, index);
			index ++;
		}
		
	
	
		Double[] lowerCorner = new Double[]{raster.getWorldBounds()[0], raster.getWorldBounds()[1]};
		Double[] upperCorner = new Double[]{raster.getWorldBounds()[2], raster.getWorldBounds()[3]};
		
	
		double nminX = lowerCorner[0];
		double nminY = lowerCorner[1];
		double nmaxX = upperCorner[0];
		double nmaxY = upperCorner[1];
	
		double finalMinX = minX;
		double finalMinY = minY;
		double finalMaxX = maxX;
		double finalMaxY = maxY;

		boolean testMinX = false;
		boolean testMinY = false;
		boolean testMaxX = false;
		boolean testMaxY = false;

		if(lowerCorner[0] > minX){

			double diff = lowerCorner[0] - minX;

			int resDiff = (int)Math.round(diff / xRes);
			if((diff/xRes - resDiff) < 0.5) {
				resDiff = 1;
			}
			double rescale = resDiff * xRes;
			finalMinX = lowerCorner[0] - rescale;
			testMinX = true;
		}




		if(lowerCorner[1] > minY){

			double diff = lowerCorner[1] -minY;

			int resDiff = (int)Math.round(diff / yRes);
			if((diff/yRes - resDiff) < 0.5) {
				resDiff = 1;
			}
			double rescale = resDiff * yRes;
			finalMinY = lowerCorner[1] - rescale;
		
			testMinY = true;
		}

		if(upperCorner[0] < maxX){

			double diff = maxX - upperCorner[0];			
			int resDiff = (int)Math.round(diff / xRes);
			if((diff/xRes - resDiff) < 0.5) {
				resDiff = 1;
			}
			double rescale = resDiff * xRes;
			finalMaxX = upperCorner[0] + rescale;
			testMaxX = true;
		}

		if(upperCorner[1] < maxY){

			double diff = maxY - upperCorner[1];

			int resDiff = (int)Math.round(diff / yRes);
			if((diff/yRes - resDiff) < 0.5) {
				resDiff = 1;
			}
			double rescale = resDiff * yRes;
			finalMaxY = upperCorner[1] + rescale;
			testMaxY = true;
		}


		//Testing if raster coord is out of shapefile description
		double precisionScale = 0.01;
    	double precision1 = 1.0 - precisionScale;
    	double precision2 = 1.0+ precisionScale;
    	
       
		//lower corner :
		if(testMinX == false && testMinY == false){
			int[] minGridCoordFromShp = null;
			if( ((lowerCorner[1] / minY) > precision1 &&    (lowerCorner[1] / minY)< precision2) ) {
				if( ((lowerCorner[0] / minX) > precision1 &&    (lowerCorner[0] / minX)< precision2) ) {
					minGridCoordFromShp = raster.worldToGrid(minX + xRes / 2, minY + yRes / 2);
				}else {
					minGridCoordFromShp = raster.worldToGrid(minX, minY + yRes / 2);
				}
			}else {
				if( ((lowerCorner[0] / minX) > precision1 &&    (lowerCorner[0] / minX)< precision2) ) {
					minGridCoordFromShp = raster.worldToGrid(minX + xRes / 2, minY);
				}else {
					minGridCoordFromShp = raster.worldToGrid(minX, minY);
				}
			}
			//minGridCoordFromShp = raster.worldToGrid(minX, minY);
			double[] minCoordFromShp = raster.gridToWorld(minGridCoordFromShp[0], minGridCoordFromShp[1]);
			finalMinX = minCoordFromShp[0] - xRes / 2;
			finalMinY = minCoordFromShp[1] - yRes / 2;

		}

		if(testMinX == false && testMinY == true){
			
			int[] minGridCoordFromShp = null;
				if( ((lowerCorner[0] / minX) > precision1 &&    (lowerCorner[0] / minX)< precision2) ) {
					minGridCoordFromShp = raster.worldToGrid(minX + xRes / 2, lowerCorner[1] + yRes / 2);
					
				}else {
					minGridCoordFromShp = raster.worldToGrid(minX, lowerCorner[1] + yRes / 2);
				
				}
			
			
			double[] minCoordFromShp = raster.gridToWorld(minGridCoordFromShp[0], minGridCoordFromShp[1]);
			finalMinX = minCoordFromShp[0] - xRes / 2;
			//finalMinY = minCoordFromShp[1] - yRes / 2;


		}

		if(testMinX == true && testMinY == false){
			int[] minGridCoordFromShp = null;
			if( ((lowerCorner[1] / minY) > precision1 &&    (lowerCorner[1] / minY)< precision2) ) {
				minGridCoordFromShp = raster.worldToGrid(lowerCorner[0] + xRes / 2, minY + yRes / 2);
			}else {
				minGridCoordFromShp = raster.worldToGrid(lowerCorner[0], minY);
			}
		
			double[] minCoordFromShp = raster.gridToWorld(minGridCoordFromShp[0], minGridCoordFromShp[1]);
			//finalMinX = minCoordFromShp[0] - xRes / 2;
			finalMinY = minCoordFromShp[1] - yRes / 2;

		}

		//upper corner :

		if(testMaxX == false && testMaxY == false){
			
			
			int[] maxGridCoordFromShp = null;
			if( ((upperCorner[1] / maxY) > precision1 &&    (upperCorner[1] / maxY)< precision2) ) {
				if(((upperCorner[0] / maxX) > precision1 &&    (upperCorner[0] / maxX)< precision2)) {
					maxGridCoordFromShp = raster.worldToGrid(maxX - xRes / 2, maxY - yRes / 2);
				}else {
					maxGridCoordFromShp = raster.worldToGrid(maxX, maxY - yRes / 2);
				}
			}else {
				if(((upperCorner[0] / maxX) > precision1 &&    (upperCorner[0] / maxX)< precision2)) {
					maxGridCoordFromShp = raster.worldToGrid(maxX - xRes / 2, maxY);
				}else {
					maxGridCoordFromShp = raster.worldToGrid(maxX, maxY);
				}
			}
			
			
			
			double[] maxCoordFromShp = raster.gridToWorld(maxGridCoordFromShp[0], maxGridCoordFromShp[1]);
			finalMaxX = maxCoordFromShp[0] + xRes / 2;
			finalMaxY = maxCoordFromShp[1] + yRes / 2;

		}

		if(testMaxX == false && testMaxY == true){
			int[] maxGridCoordFromShp = null;
			
			
			if(((upperCorner[0] / maxX) > precision1 &&    (upperCorner[0] / maxX)< precision2)) {
				maxGridCoordFromShp = raster.worldToGrid(maxX - xRes / 2, upperCorner[1] - yRes / 2);
			}else {
				maxGridCoordFromShp = raster.worldToGrid(maxX, upperCorner[1]- yRes / 2);
			}
			double[] maxCoordFromShp = raster.gridToWorld(maxGridCoordFromShp[0], maxGridCoordFromShp[1]);
			finalMaxX = maxCoordFromShp[0] + xRes / 2;
			//finalMaxY = maxCoordFromShp[1] + yRes / 2;


		}

		if(testMaxX == true && testMaxY == false){
			int[] maxGridCoordFromShp = null;
			if(((upperCorner[1] / maxY) > precision1 &&    (upperCorner[1] / maxY)< precision2)) {
				maxGridCoordFromShp = raster.worldToGrid(upperCorner[0] - xRes / 2, maxY - yRes / 2);
			}else {
				maxGridCoordFromShp = raster.worldToGrid(upperCorner[0] - xRes / 2, maxY);
			}
			
			double[] maxCoordFromShp = raster.gridToWorld(maxGridCoordFromShp[0], maxGridCoordFromShp[1]);
			//finalMaxX = maxCoordFromShp[0] + xRes / 2;
			finalMaxY = maxCoordFromShp[1] + yRes / 2;

		}

		nminX = finalMinX;
		nminY = finalMinY;
		nmaxX = finalMaxX;
		nmaxY = finalMaxY;

		double newMinX = nminX;// - 2*xRes;
		double newMinY = nminY; // - 2*yRes;
		double newMaxX = nmaxX; // + 2* xRes;// newMinX + (cellWidth * xRes);
		double newMaxY = nmaxY; // + 2* yRes;//newMinY + (cellHeight * yRes);

		double width = newMaxX - newMinX;
		double height = newMaxY - newMinY;

		int cellWidth = (int)(Math.round(width / xRes));
		int cellHeight = (int) (Math.round(height / yRes));	
		if(Math.round(width / xRes) - cellWidth < 0.5) {
			cellWidth ++;
		}
		if(Math.round(height / yRes) - cellHeight < 0.5) {
			cellHeight ++;
		}

		newMaxX = newMinX + cellWidth * xRes;
		newMaxY = newMinY + cellHeight * yRes;
		/*width = cellWidth * xRes;
		height = cellHeight * yRes;
		newMaxX = newMinX + width;
		newMaxY = newMinY + height;*/
		
		
		Envelope2D env = createEnvelope(newMinX, newMinY,newMaxX, newMaxY);
		env.setCoordinateReferenceSystem(raster.getCRS());
		WritableRaster newRaster = createRaster(index, cellWidth, cellHeight);
		GridCoverage2D coverage =  createCoverage(name, newRaster, env);		
		
		
		
		
		Grid grid = new Grid(name,cellWidth, cellHeight, coverage.getGridGeometry());
		
		grid.setRasterProperties(initProps);
		grid.setRaster(newRaster);
		grid.setXRes(xRes);
		grid.setYRes(yRes);
		
		grid.setWorldBounds(new Double[]{newMinX, newMinY, newMaxX, newMaxY});
		grid.setEnv(env);
		grid.setCRS(raster.getCRS());
		grid.setCellShapeType("QUADRILATERAL");


		return grid;


	}

	public static Grid squareGridFromShp(String name, List<String> props,ORaster raster, double xRes, double yRes,Double[] bounds){	
		double minX = bounds[0];
		double minY = bounds[1];
		double maxX = bounds[2];
		double maxY = bounds[3];

		return squareGridFromShp(name, props,raster, xRes, yRes, minX, minY, maxX, maxY);

	}
	
	public static Grid squareGridFromShp(String name, List<String> props,ORaster raster, double xRes, double yRes,Polygon polygon){	
		
		Coordinate[] coords = polygon.getCoordinates();
		double minX = coords[0].x;
		double minY =  coords[0].y;
		double maxX =  coords[2].x;
		double maxY =  coords[2].y;

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
		Envelope2D targetEnv = initRaster.getGridGeometry().getEnvelope2D();
		Envelope2D env = createEnvelope(targetEnv.getMinX(), targetEnv.getMinY(),targetEnv.getMaxX(), targetEnv.getMaxY());
		//env.setCoordinateReferenceSystem(initRaster.getCRS());
	
		int[] rasterCoordMin = new int[]{initRaster.getMinPixel(0), initRaster.getMinPixel(1)};
		int[] rasterCoordMax = new int[]{initRaster.getMaxPixel(0), initRaster.getMaxPixel(1)};

		double[] rasterWorldMin = new double[]{initRaster.getMinimum(0), initRaster.getMinimum(1)};
		double[] rasterWorldMax = new double[]{initRaster.getMaximum(0), initRaster.getMaximum(1)};
		double nminX = rasterWorldMin[0];
		double nminY = rasterWorldMin[1];
		double nmaxX = rasterWorldMax[0];
		double nmaxY = rasterWorldMax[1];
		nminX = env.getMinX();
		nminY = env.getMinY();
		nmaxX = env.getMaxX();
		nmaxY = env.getMaxY();


		int cellWidth = rasterCoordMax[0] - rasterCoordMin[0] + 1;
		int cellHeight = rasterCoordMax[1] - rasterCoordMin[1] + 1;		
		cellWidth =initRaster.getGridWidth();
		
		cellHeight = initRaster.getGridHeight();
		WritableRaster raster = createRaster(index, cellWidth, cellHeight);
		GridCoverage2D coverage =  createCoverage(name, raster, env);
		//coverage = initRaster.getGridGeometry().getc
		Grid grid = new Grid(name,cellWidth, cellHeight, coverage.getGridGeometry());
		grid.setRasterProperties(initProps);
		grid.setRaster(raster);
		grid.setXRes(initRaster.getXRes());
		grid.setYRes(initRaster.getYRes());

		grid.setWorldBounds(new Double[]{nminX, nminY, nmaxX, nmaxY});
		grid.setEnv(env);
		grid.setCRS(initRaster.getCRS());
		grid.setCellShapeType("QUADRILATERAL");

		return grid;


	}



	public static Grid triangularGrid(String name, List<String> props, double size, double minX, double minY, double maxX, double maxY){

		HashMap<String, Integer> initProps = new HashMap<String, Integer>();

		int index = 0;
		for(String s : props){
			initProps.put(s, index);
			index ++;
		}

		double l = (Math.sqrt(3) / 2) * size;
		
		l = (Math.sqrt(3) * size / 3) + (Math.sqrt(3) * size / 6); 
		double nminX = minX   - 3 * size;
		double nminY = minY - (3 * l);
		double nmaxX = maxX  + 3 * size;
		double nmaxY = maxY +  3 * l;
		
		
		double width = (nmaxX - nminX);
		double height = (nmaxY - nminY);
		int upNbr = (int)Math.round(width / size);
		
		int cellWidth = 0;
		if(upNbr % 2 == 0){
			cellWidth = upNbr + upNbr - 1;
		}else{
			cellWidth = upNbr + upNbr + 1;
		}
		int cellHeight = (int) (Math.round(height / l));	

		/*if(cellWidth % 2 == 0){
			cellWidth++;
		}*/

		if(cellHeight % 2 == 0){
			cellHeight++;
		}

		double newMinX = nminX;
		double newMinY = nminY;
		double newMaxX = newMinX + ((cellWidth-1) * size / 2) + size;
		double newMaxY = newMinY + (cellHeight * l);// + (Math.sqrt(3) * size) / 3;
;
		double squareMinY = newMinY;// - l /2 + ((Math.sqrt(3) * size) / 6);
		double newSquareMaxY = squareMinY + (cellHeight * l);
		double squareMaxX = nminX + (cellWidth * size);
		//cellWidth = cellWidth + (cellWidth / 4);

		Envelope2D env = createEnvelope(nminX, squareMinY, squareMaxX, newSquareMaxY);
		WritableRaster raster = createRaster(index, cellWidth, cellHeight);
		GridCoverage2D coverage =  createCoverage(name, raster, env);
		Grid grid = new Grid(name,cellWidth, cellHeight, coverage.getGridGeometry());
		
		grid.setRasterProperties(initProps);
		grid.setRaster(raster);
		grid.setXRes(size);
		grid.setYRes(l);
		grid.setWorldBounds(new Double[]{newMinX, newMinY, newMaxX, newMaxY});
		grid.setworldboundsPrime(new Double[]{nminX, squareMinY, squareMaxX, newSquareMaxY});
		grid.setEnv(env);
		grid.setCellShapeType("TRIANGULAR");

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
		return nr.createCompatibleWritableRaster(); 

	}

	public static WritableRaster createRasterInt(int numBand, int cellWidth, int cellHeight){

		java.awt.image.DataBuffer db2 = new DataBufferInt(0, numBand);

		SampleModel sample = RasterFactory.createBandedSampleModel(DataBuffer.TYPE_INT, cellWidth, cellHeight, numBand);
		Raster nr = Raster.createRaster(sample, db2, null);
		return nr.createCompatibleWritableRaster(); 

	}


	public static Envelope2D createEnvelope(double minX, double minY, double maxX, double maxY){


		DirectPosition2D dpMin = new DirectPosition2D(minX, minY);
		DirectPosition2D dpMax = new DirectPosition2D(maxX, maxY);
		Envelope2D envelope = new Envelope2D(dpMin , dpMax);

		return envelope;
	}
	private static double[] center(double iMinX,double iMinY, double iMaxX,double iMaxY, double minX, double minY, double maxX, double maxY) {
		
		
		
		double iCenterX = (iMaxX - iMinX) / 2;
		double iCenterY = (iMaxY - iMinY) / 2;
		
		double centerX = (maxX - minX) / 2;
		double centerY = (maxY - minY) / 2;
		
		double diffX = centerX - iCenterX;
		double diffY = centerY - iCenterY;
		
		return new double[] {minX - diffX, minY - diffY, maxX - diffX, maxY - diffY};
		
	}
	private static double roundValue(double value, Integer precision){
		Integer scale = (int)Math.round(value * precision);
		return scale.doubleValue() / precision.doubleValue();

	}
}
