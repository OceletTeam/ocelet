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
package fr.ocelet.runtime.relation;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequence;
import com.vividsolutions.jts.operation.union.CascadedPolygonUnion;
import com.vividsolutions.jts.simplify.DouglasPeuckerSimplifier;

import fr.ocelet.runtime.entity.AbstractEntity;
import fr.ocelet.runtime.geom.OceletGeomFactory;
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
import fr.ocelet.runtime.ocltypes.array.CellArray;
import fr.ocelet.runtime.raster.CellAggregOperator;
import fr.ocelet.runtime.raster.Grid;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.LinkedHashMap;

// Referenced classes of package fr.ocelet.runtime.relation:
//            OcltEdge, OcltRole, AggregOperator



public abstract class GeomCellEdge<R1 extends OcltRole, R2 extends OcltRole> extends OcltEdge {


	private HashMap<String, CellAggregOperator> aggregMap = new HashMap<String, CellAggregOperator>();

	private LinkedHashMap<Integer, LinkedHashMap<Integer, ArrayList<GeomContainer>>> matrice = new LinkedHashMap<Integer, LinkedHashMap<Integer,ArrayList<GeomContainer>>>();

	private List<R2> geomEntities;	//List of non cells entities to synchronize at the end    
	protected Grid grid; // the grid	    
	private int currentxKey; // current x cells coordinate	    
	private int currentyKey; // current y cells coordinate
	
	private GeomContainer currentRoleKey; // current non cell entity 	    
	private Iterator<Integer> xIterator; // iterator on x cells coordinate	    
	private Iterator<Integer> yIterator; // iterator on y cells coodinate bounded on the x ones	    
	private Iterator<GeomContainer> roleIterator; // iterator on non cell entities bounded on the y ones	    
	private String cellShapeType = "QUADRILATERAL";	    
	private HashMap<R2, ArrayList<Integer[]>> added = new HashMap<R2, ArrayList<Integer[]>>();
	private HashMap<Integer, ArrayList<Integer>> cellMap; // cellmap contained specific cells
	
	private Nexter nexter;
	private CellIterator<R1> cellIterator = new CellIterator<R1>();
	
	private Double distance = 0.0;
	
	private int[] aggregIndex = null;
	private int[] notAggregIndex = null;
	
	private ArrayList<String> gridProperties;

	public String getCellType(){
		return grid.getCellShapeType();
	}
	public void clearAggregMap()
	{
		aggregMap.clear();

	}

	public void clear(){
		matrice.clear();
		geomEntities.clear();
	}
	public void addOperator(String name, CellAggregOperator operator)
	{
		aggregMap.put(name, operator);
	}

	public void setAggregIndex() {
			
		ArrayList<Integer> aggregBand = new ArrayList<Integer>();
		
		for(String name : aggregMap.keySet()) {
			
			aggregBand.add(grid.getBand(name));
		}
		notAggregIndex = new int[gridProperties.size() - aggregBand.size()];
		aggregIndex = new int[aggregBand.size()];
		
		int indexNot = 0;
		int index = 0;
		for(int i = 0; i < gridProperties.size(); i ++) {
			
			if(!aggregBand.contains(i)) {
				notAggregIndex[indexNot] = i;
				indexNot++;
			}else {
				aggregIndex[index] = i;
				index++;
			}
		}
	}
	
	public void initInteraction() {
		setAggregIndex();
	}
	/*public void setCellOperator(String name, AggregOperator operator)
{
  CellAggregOperator cao = new CellAggregOperator(operator, name);
  aggregMap.put(name, cao);
}*/
	public void setCellOperator(String name, AggregOperator operator, KeyMap<String, String> typeProps)
	{
		CellAggregOperator cao = new CellAggregOperator();
		if(typeProps.get(name).equals("Double")){
			setAggregOpDouble(name, operator, false);
		}else if(typeProps.get(name).equals("Integer")){
			setAggregOpInteger(name, operator, false);

		}else if(typeProps.get(name).equals("Float")){
			setAggregOpFloat(name, operator, false);

		}else if(typeProps.get(name).equals("Byte")){
			setAggregOpByte(name, operator, false);

		}else if(typeProps.get(name).equals("Boolean")){
			setAggregOpBoolean(name, operator, false);

		}
		
		
		
	}
	public void setAggregOpDouble(String name, AggregOperator<Double, List<Double>> agg, boolean val){
		CellAggregOperator cao = new CellAggregOperator();
		cao.setOperatorDouble(agg);
		aggregMap.put(name, cao);

	}
	public void setAggregOpInteger(String name, AggregOperator<Integer, List<Integer>> agg, boolean val){
		CellAggregOperator cao = new CellAggregOperator();
		cao.setOperatorInteger(agg);
		aggregMap.put(name, cao);

	}

	public void setAggregOpFloat(String name, AggregOperator<Float, List<Float>> agg, boolean val){
		CellAggregOperator cao = new CellAggregOperator();
		cao.setOperatorFloat(agg);
		aggregMap.put(name, cao);
	}

	public void setAggregOpByte(String name, AggregOperator<Byte, List<Byte>> agg, boolean val){
		CellAggregOperator cao = new CellAggregOperator();
		cao.setOperatorByte(agg);
		aggregMap.put(name, cao);
	}

	public void setAggregOpBoolean(String name, AggregOperator<Boolean, List<Boolean>> agg, boolean val){
		CellAggregOperator cao = new CellAggregOperator();
		cao.setOperatorBoolean(agg);
		aggregMap.put(name, cao);
	}
	public GeomCellEdge(List<? extends AbstractEntity> cellList, List<R2> geomEntities){ //Grid grid, List<R2> geomEntities){


		this.geomEntities = geomEntities;
		AbstractEntity ae = cellList.get(0);
		Cell cell = (Cell)ae.getSpatialType();
		cellList.visit(this);
		
		this.grid = cell.getGrid();
		gridProperties = grid.getPropertiesName();
		
		
		this.distance = grid.getXRes() / 2;
		initEdgeProperty();
		fill(this.geomEntities);

	}
	public GeomCellEdge(List<? extends AbstractEntity> cellList, List<R2> geomEntities, Double distance){ //Grid grid, List<R2> geomEntities){
		this.distance = distance;
		this.geomEntities = geomEntities;
		AbstractEntity ae = cellList.get(0);
		Cell cell = (Cell)ae.getSpatialType();
		cellList.visit(this);
		
		this.grid = cell.getGrid();
		gridProperties = grid.getPropertiesName();
		
		initEdgeProperty();
		fill(this.geomEntities);

	}
	
	public void setCellMap(HashMap<Integer, ArrayList<Integer>> cellMap) {
		this.cellMap = cellMap;
	}


	private boolean contains(int x, int y, R2 e){
		ArrayList<GeomContainer> gcs = matrice.get(x).get(y);
		boolean contains = false;
		for(GeomContainer gc : gcs){
			if(gc.getGeom().equals(e)){
				contains = true;
			}
		}
		return contains;
	}

	public void add(R2 e, int x, int y){    

		GeomContainer gc = new GeomContainer(e);

		if(doublePropertySize > 0){
			gc.initDouble(doublePropertySize);
		}
		if(intPropertySize > 0){
			gc.initInteger(intPropertySize);
		}
		if(booleanPropertySize > 0){
			gc.initBoolean(booleanPropertySize);
		}


		if(x < 0 || y < 0){

		}else{
			if(!matrice.keySet().contains(x)){

				ArrayList<GeomContainer> newList = new ArrayList<GeomContainer>();
				newList.add(gc);
				LinkedHashMap<Integer, ArrayList<GeomContainer>> yHash = new LinkedHashMap<Integer, ArrayList<GeomContainer>>();
				yHash.put(y, newList);
				matrice.put(x, yHash);

			} else{

				if(!matrice.get(x).keySet().contains(y)){

					ArrayList<GeomContainer> newList = new ArrayList<GeomContainer>();
					newList.add(gc);
					matrice.get(x).put(y, newList);

				} else{


					if(!contains(x, y, e)){
						matrice.get(x).get(y).add(gc);
					}
				}
			}
		}
	}

	/*  public ArrayList<OcltRole> get(int x, int y){
	        return matrice[x][y].get();
}*/
	/*public ArrayList<R2> get(int x, int y){
	return matrice.get(x).get(y);
//   return matrice[x][y].get();
}*/

	public void fill(List<R2> list){
		if(cellMap == null) {
		if(grid.getCellShapeType().equals("QUADRILATERAL")){
			quadrilateralExtract(list);
		}
		if(grid.getCellShapeType().equals("HEXAGONAL")){
			hexagonalExtract(list);
		}

		if(grid.getCellShapeType().equals("TRIANGULAR")){
			triangularExtract(list);
		}
		}else {
			if(grid.getCellShapeType().equals("QUADRILATERAL")){
				quadrilateralExtractCut(list);
			}
			if(grid.getCellShapeType().equals("HEXAGONAL")){
				hexagonalExtractCut(list);
			}

			if(grid.getCellShapeType().equals("TRIANGULAR")){
				triangularExtractCut(list);
			}
		}
		
		
		if(!matrice.isEmpty()) {
			xIterator = matrice.keySet().iterator();
			currentxKey = xIterator.next();
			yIterator = matrice.get(currentxKey).keySet().iterator();
			currentyKey = yIterator.next();
			roleIterator = matrice.get(currentxKey).get(currentyKey).iterator();
			nexter = new ValideNexter();
			
		}else {
			nexter = new UnValideNexter();
		}
	}


	public void resetIterator(){

		xIterator = matrice.keySet().iterator();
		currentxKey = xIterator.next();
		yIterator = matrice.get(currentxKey).keySet().iterator();
		currentyKey = yIterator.next();
		roleIterator = matrice.get(currentxKey).get(currentyKey).iterator();
		update();
		
	}
	
	
	private void setTriangularCells(R2 r2, Polygon polygon, HashMap<Integer, ArrayList<Line>> lines)
	{
		int bounds[] = null;
		if(distance == 0) {
		bounds = grid.intBounds(polygon);
		}else {
			bounds = grid.intBounds(polygon, distance);
		}
		int index = 0;

		if(bounds != null) {
		if(bounds[1] > 0){
			bounds[1] = bounds[1] - 1;
		}

		if(bounds[3] < grid.getHeight() - 1){
			bounds[3] = bounds[3] + 1;
		}
		if(bounds[0] > 0) {
			bounds[0] = bounds[0] - 1;
		}
		
		if(bounds[2] < grid.getWidth() - 1) {
			bounds[2] = bounds[2] + 1;
		}

		for(int j = bounds[1]; j < bounds[3]; j++){

			for(int i = bounds[0]; i < bounds[2]; i ++){
				Coordinate c = grid.gridCoordinate(i, j);
				CoordinateSequence seq = new CoordinateArraySequence(new Coordinate[]{c});
				Point p = new Point(seq, SpatialManager.geometryFactory());
				if(polygon.distance(p) < grid.getXRes() / 2){
					add(r2, i, j );
					index++;
				}

			}
		}

		if(index == 0)
		{
			int scaledCentroid[] = grid.gridCoordinate(polygon.getCentroid().getX(), polygon.getCentroid().getY());
			if(scaledCentroid != null)
				add(r2, scaledCentroid[0], scaledCentroid[1]);
		}
		}
	}
	
	private void setTriangularCellsCut(R2 r2, Polygon polygon, HashMap<Integer, ArrayList<Line>> lines)
	{
		int bounds[] = null;
		if(distance == 0) {
		bounds = grid.intBounds(polygon);
		}else {
			bounds = grid.intBounds(polygon, distance);
		}
		int index = 0;
		
		if(bounds != null) {
		if(bounds[1] > 0){
			bounds[1] = bounds[1] - 1;
		}

		if(bounds[3] < grid.getHeight() - 1){
			bounds[3] = bounds[3] + 1;
		}
		if(bounds[0] > 0) {
			bounds[0] = bounds[0] - 1;
		}
		
		if(bounds[2] < grid.getWidth() - 1) {
			bounds[2] = bounds[2] + 1;
		}

		for(int j = bounds[1]; j < bounds[3]; j++){

			for(int i = bounds[0]; i < bounds[2]; i ++){
				Coordinate c = grid.gridCoordinate(i, j);
				CoordinateSequence seq = new CoordinateArraySequence(new Coordinate[]{c});
				Point p = new Point(seq, SpatialManager.geometryFactory());
				if(polygon.distance(p) < grid.getXRes() / 2){
					if(cellMap.keySet().contains(i)) {
						if(cellMap.get(i).contains(j)) {
							add(r2, i, j);
							index++;
						}
					}
				}

			}
		}

		if(index == 0)
		{
			int scaledCentroid[] = grid.gridCoordinate(polygon.getCentroid().getX(), polygon.getCentroid().getY());
			if(scaledCentroid != null) {
				if(cellMap.keySet().contains(scaledCentroid[0])) {
					if(cellMap.get(scaledCentroid[0]).contains( scaledCentroid[1])) {
						add(r2, scaledCentroid[0], scaledCentroid[1]);
						
					}
				}
			}
		}
		}
	}

	private void setTriangularCells(R2 r2, Line line)
	{
		Coordinate coordinates[] = line.getCoordinates();
		for(int i = 0; i < coordinates.length - 1; i++)
		{
			int c1[] = grid.gridCoordinate(coordinates[i].x, coordinates[i].y);
			int c2[] = grid.gridCoordinate(coordinates[i + 1].x, coordinates[i + 1].y);
			if(c1!= null && c2 != null) {
				int x = c1[0];
				int y = c1[1];
				int x2 = c2[0];
				int y2 = c2[1];
				bresenham(x, y, x2, y2, r2);
			}
		}

	}
	
	private void setTriangularCellsCut(R2 r2, Line line)
	{
		Coordinate coordinates[] = line.getCoordinates();
		for(int i = 0; i < coordinates.length - 1; i++)
		{
			int c1[] = grid.gridCoordinate(coordinates[i].x, coordinates[i].y);
			int c2[] = grid.gridCoordinate(coordinates[i + 1].x, coordinates[i + 1].y);
			if(c1!= null && c2 != null) {
				int x = c1[0];
				int y = c1[1];
				int x2 = c2[0];
				int y2 = c2[1];
			bresenhamCut(x, y, x2, y2, r2);
			}
		}

	}

	private void setTriangularCells(R2 r2, Point point)
	{
		Coordinate coordinates[] = point.getCoordinates();

		int c1[] = grid.gridCoordinate(coordinates[0].x, coordinates[0].y);
		if(c1 != null) {
			add(r2, c1[0], c1[1]);
		}

	}
	
	private void setTriangularCellsCut(R2 r2, Point point)
	{
		Coordinate coordinates[] = point.getCoordinates();

		int c1[] = grid.gridCoordinate(coordinates[0].x, coordinates[0].y);
		if(c1 != null) {
			if(cellMap.keySet().contains(c1[0])) {
				
				if(cellMap.get(c1[0]).contains(c1[1])) {
					add(r2, c1[0], c1[1]);
				}
				
			}
		}


	}

	private void setHexagonalCells(R2 r2, Polygon polygon){

		int bounds[] = null;
		if(distance == 0) {
		bounds = grid.intBounds(polygon);
		}else {
			bounds = grid.intBounds(polygon, distance);
		}
		int index = 0;
		if(bounds != null) {
		if(bounds[1] > 0){
			bounds[1] = bounds[1] - 1;
		}

		if(bounds[3] < grid.getHeight() - 1){
			bounds[3] = bounds[3] + 1;
		}
		if(bounds[0] > 0) {
			bounds[0] = bounds[0] - 1;
		}
		
		if(bounds[2] < grid.getWidth() - 1) {
			bounds[2] = bounds[2] + 1;
		}
		for(int i = bounds[0]; i <= bounds[2]; i++){

			for(int j = bounds[1]; j <= bounds[3]; j++){


				Coordinate c = grid.gridCoordinate(i, j);

				Point 	point = Point.xy(Double.valueOf(c.x), Double.valueOf(c.y));
				if(polygon.distance(point) <= grid.getXRes()){

					add(r2, i, j);
					index++;
				}
			}

		}

		if(index == 0){


			int[] scaledCentroid = grid.gridCoordinate(polygon.getCentroid().getX(), polygon.getCentroid().getY());

			if(scaledCentroid != null) 
				add(r2, scaledCentroid[0], scaledCentroid[1]);
		}
		}
	}
	
	private void setHexagonalCellsCut(R2 r2, Polygon polygon){

		int bounds[] = null;
		if(distance == 0) {
		bounds = grid.intBounds(polygon);
		}else {
			bounds = grid.intBounds(polygon, distance);
		}
		int index = 0;
		if(bounds != null) {
		if(bounds[1] > 0){
			bounds[1] = bounds[1] - 1;
		}

		if(bounds[3] < grid.getHeight() - 1){
			bounds[3] = bounds[3] + 1;
		}
		if(bounds[0] > 0) {
			bounds[0] = bounds[0] - 1;
		}
		
		if(bounds[2] < grid.getWidth() - 1) {
			bounds[2] = bounds[2] + 1;
		}
		for(int i = bounds[0]; i <= bounds[2]; i++){

			for(int j = bounds[1]; j <= bounds[3]; j++){


				Coordinate c = grid.gridCoordinate(i, j);

				Point 	point = Point.xy(Double.valueOf(c.x), Double.valueOf(c.y));
				if(polygon.distance(point) <= grid.getXRes()){
					if(cellMap.keySet().contains(i)) {
						if(cellMap.get(i).contains(j)) {
							add(r2, i, j);
							index++;
						}
					}
					
				}
			}

		}

		if(index == 0){


			int[] scaledCentroid = grid.gridCoordinate(polygon.getCentroid().getX(), polygon.getCentroid().getY());

			if(scaledCentroid != null) {
				if(cellMap.keySet().contains(scaledCentroid[0])) {
					if(cellMap.get(scaledCentroid[0]).contains( scaledCentroid[1])) {
						add(r2, scaledCentroid[0], scaledCentroid[1]);
						
					}
				}
			}
				
		}
		}
	}

	private void setHexagonalCells(R2 r2, Line line){
	
		Coordinate coordinates[] = line.getCoordinates();
		for(int i = 0; i < coordinates.length - 1; i++)
		{
			int c1[] = grid.gridCoordinate(coordinates[i].x, coordinates[i].y);
			int c2[] = grid.gridCoordinate(coordinates[i + 1].x, coordinates[i + 1].y);
			if( c1 != null && c2 != null) {
				int x = c1[0];
				int y = c1[1];
				int x2 = c2[0];
				int y2 = c2[1];
				bresenham(x, y, x2, y2, r2);
			}
		}

	}
	
	private void setHexagonalCellsCut(R2 r2, Line line){
		
		Coordinate coordinates[] = line.getCoordinates();
		for(int i = 0; i < coordinates.length - 1; i++)
		{
			int c1[] = grid.gridCoordinate(coordinates[i].x, coordinates[i].y);
			int c2[] = grid.gridCoordinate(coordinates[i + 1].x, coordinates[i + 1].y);
			if(c1 != null && c2 != null) {
				int x = c1[0];
				int y = c1[1];
				int x2 = c2[0];
				int y2 = c2[1];
				bresenhamCut(x, y, x2, y2, r2);
			}
		}

	}


	private void setHexagonalCells(R2 r2, Point point){

		Coordinate coordinates[] = point.getCoordinates();

		int c1[] = grid.gridCoordinate(coordinates[0].x, coordinates[0].y);
		if(c1 != null) {
			add(r2, c1[0], c1[1]);
		}


	}
	
	private void setHexagonalCellsCut(R2 r2, Point point){

		Coordinate coordinates[] = point.getCoordinates();

		int c1[] = grid.gridCoordinate(coordinates[0].x, coordinates[0].y);
		if(c1 != null) {
			if(cellMap.keySet().contains(c1[0])) {
			
				if(cellMap.get(c1[0]).contains(c1[1])) {
					add(r2, c1[0], c1[1]);
				}
			}
		}
	}



	private HashMap<Integer, ArrayList<Line>> makeLines(){
		HashMap<Integer, ArrayList<Line>> lines = new HashMap<Integer, ArrayList<Line>>();

		int width = grid.getWidth();
		int height = grid.getHeight();
		double xRes = grid.getXRes(); 
		double yRes = grid.getYRes();
		Double[] bounds = grid.getWorldBounds();

		double minX = bounds[0]- ( 3 * xRes);

		double maxX = bounds[2]+ ( 3 * xRes);

		double minY = bounds[1] - ( 3 * yRes);

		double maxY = bounds[3] + ( 3 * yRes); 

		double precision = 0.9;
		double resPrecision = (yRes / 2) * precision;
		for(int i = - 3; i < height + 3; i ++){



			Point p1 = Point.xy(minX, maxY - resPrecision);
			Point p2 = Point.xy(maxX, maxY - resPrecision);

			Point p3 = Point.xy(minX, maxY + resPrecision);
			Point p4 = Point.xy(maxX, maxY + resPrecision);


			maxY = maxY - yRes;
			Line l = Line.points(p1, p2);
			Line l2 = Line.points(p3, p4);
			ArrayList<Line> newLines = new ArrayList<Line>();
			newLines.add(l);
			newLines.add(l2);
			lines.put(i, newLines);
		}


		return lines;
	}


	private void setQuadrilateralCells(R2 r2, Polygon polygon,HashMap<Integer, ArrayList<Line>> keyLines)
	{
		
		
		int bounds[] = null;
		if(distance == grid.getXRes() / 2) {
			bounds = grid.intBounds(polygon);
		}else {
			bounds = grid.intBounds(polygon, distance);
		}
		int index = 0;
		if(bounds != null) {

		if(bounds[1] > 0){
			bounds[1] = bounds[1] - 1;
		}

		if(bounds[3] < grid.getHeight() - 1){
			bounds[3] = bounds[3] + 1;
		}
		if(bounds[0] > 0) {
			bounds[0] = bounds[0] - 1;
		}
		
		if(bounds[2] < grid.getWidth() - 1) {
			bounds[2] = bounds[2] + 1;
		}
	
		for(int i = bounds[0]; i < bounds[2]; i ++){
			
		for(int j = bounds[1]; j < bounds[3]; j++){

			

				Coordinate c = null;
				try {
					c = grid.gridCoordinate(i, j);
				}catch(Exception e) {
					
				}
				if(c != null) {		
				CoordinateSequence seq = new CoordinateArraySequence(new Coordinate[]{c});
				Point p = new Point(seq, SpatialManager.geometryFactory());
				if(polygon.distance(p) <= distance){
					
					add(r2, i, j );
					index++;
				}
				/*if(polygon.distance(p) <= distance){
					
					if(coordList.keySet().contains(i)) {
						coordList.get(i).add(j);
					}else {
						ArrayList<Integer> ys = new ArrayList<Integer>();
						ys.add(j);
						if(!xs.contains(i)) {
							xs.add(i);
						}
					
						coordList.put(i, ys);
					}
					index++;
				}*/
				
				
				}
			}
		}
		if(index == 0){
		
			int scaledCentroid[] = null;
			try{
				scaledCentroid = grid.gridCoordinate(polygon.getCentroid().getX(), polygon.getCentroid().getY());
			}catch(Exception e) {
				
			}
			if(scaledCentroid != null) {
				if(scaledCentroid[0] < grid.getWidth() && scaledCentroid[0] >= 0 && scaledCentroid[1] < grid.getHeight() && scaledCentroid[1] >=0) {
				
					
					add(r2, scaledCentroid[0], scaledCentroid[1]);
				}
			}
		}
	  }
	
	} 
	
	private void setQuadrilateralCellsCut(R2 r2, Polygon polygon,HashMap<Integer, ArrayList<Line>> keyLines)
	{
		
		int bounds[] = grid.intBounds(polygon);
		int index = 0;

		if(bounds != null) {
		if(bounds[1] > 0){
			bounds[1] = bounds[1] - 1;
		}

		if(bounds[3] < grid.getHeight() - 1){
			bounds[3] = bounds[3] + 1;
		}
		if(bounds[0] > 0) {
			bounds[0] = bounds[0] - 1;
		}
		
		if(bounds[2] < grid.getWidth() - 1) {
			bounds[2] = bounds[2] + 1;
		}
	
		for(int j = bounds[1]; j < bounds[3]; j++){

			for(int i = bounds[0]; i < bounds[2]; i ++){

				Coordinate c = null;
				try {
					c = grid.gridCoordinate(i, j);
				}catch(Exception e) {
					
				}
				if(c != null) {		
				CoordinateSequence seq = new CoordinateArraySequence(new Coordinate[]{c});
				Point p = new Point(seq, SpatialManager.geometryFactory());
				if(polygon.distance(p) <= grid.getXRes() / 2){
					if(cellMap.keySet().contains(i)) {
						if(cellMap.get(i).contains(j)) {
							add(r2, i, j );
							index++;
						}
					}
					
				}
				}
			}
		}
		if(index == 0)
		{
			int scaledCentroid[] = null;
			try{
				scaledCentroid = grid.gridCoordinate(polygon.getCentroid().getX(), polygon.getCentroid().getY());
			}catch(Exception e) {
				
			}
			if(scaledCentroid != null) {
				if(scaledCentroid[0] < grid.getWidth() && scaledCentroid[0] >= 0 && scaledCentroid[1] < grid.getHeight() && scaledCentroid[1] >=0) {
					if(cellMap.keySet().contains(scaledCentroid[0])) {
						if(cellMap.get(scaledCentroid[0]).contains(scaledCentroid[1])) {
							add(r2, scaledCentroid[0], scaledCentroid[1]);
							
						}
					}
					
				}
				
			}
		}
		}
	} 

	private void setQuadrilateralCells(R2 r2, Line line)
	{
		Coordinate coordinates[] = line.getCoordinates();
		for(int i = 0; i < coordinates.length - 1; i++)
		{
			int c1[] = grid.gridCoordinate(coordinates[i].x, coordinates[i].y);
			int c2[] = grid.gridCoordinate(coordinates[i + 1].x, coordinates[i + 1].y);
			if(c1 != null && c2 != null) {
				int x = c1[0];
				int y = c1[1];
				int x2 = c2[0];
				int y2 = c2[1];
				bresenham(x, y, x2, y2, r2);
			}
		}

	}
	private void setQuadrilateralCellsCut(R2 r2, Line line)
	{
		Coordinate coordinates[] = line.getCoordinates();
		for(int i = 0; i < coordinates.length - 1; i++)
		{
			int c1[] = grid.gridCoordinate(coordinates[i].x, coordinates[i].y);
			int c2[] = grid.gridCoordinate(coordinates[i + 1].x, coordinates[i + 1].y);
			if(c1 != null && c2 != null) {
				int x = c1[0];
				int y = c1[1];
				int x2 = c2[0];
				int y2 = c2[1];
				bresenhamCut(x, y, x2, y2, r2);
			}
		}

	}

	private void setQuadrilateralCells(R2 r2, Point point)
	{
		Coordinate coordinates[] = point.getCoordinates();
		int c1[] = grid.gridCoordinate(coordinates[0].x, coordinates[0].y);
		if(c1 != null) {
		add(r2, c1[0], c1[1]);
		}else {
			System.out.println("point not found");
		}


	}
	
	private void setQuadrilateralCellsCut(R2 r2, Point point)
	{
		Coordinate coordinates[] = point.getCoordinates();
		int c1[] = grid.gridCoordinate(coordinates[0].x, coordinates[0].y);
		if(c1 != null) {
			if(cellMap.keySet().contains(c1[0])) {
				if(cellMap.get(c1[0]).contains(c1[1])) {
					add(r2, c1[0], c1[1]);
				}
			}
		
		}


	}

	public void bresenham(int x1, int y1, int x2, int y2, R2 role)
	{
		if(x1 == x2 && y1 == y2)
		{
			add(role, x1 - grid.getMinX(), y1 - grid.getMinY());
		} else
		{
			int dx = Math.abs(x2 - x1);
			int dy = Math.abs(y2 - y1);
			int rozdil = dx - dy;
			int posun_x;
			if(x1 < x2)
				posun_x = 1;
			else
				posun_x = -1;
			int posun_y;
			if(y1 < y2)
				posun_y = 1;
			else
				posun_y = -1;
			while(x1 != x2 || y1 != y2){ 
			
				int p = 2 * rozdil;
				if(p > -dy)
				{
					rozdil -= dy;
					x1 += posun_x;
				}
				if(p < dx)
				{
					rozdil += dx;
					y1 += posun_y;
				}
				add(role, x1, y1);
			}
		}
	}
	
	public void bresenhamCut(int x1, int y1, int x2, int y2, R2 role)
	{
		if(x1 == x2 && y1 == y2)
		{
			if(cellMap.keySet().contains(x1 - grid.getMinX())) {
				if(cellMap.get(x1 - grid.getMinX()).contains(y1 - grid.getMinY())) {
					add(role, x1 - grid.getMinX(), y1 - grid.getMinY());
				}
			}
			
		} else
		{
			int dx = Math.abs(x2 - x1);
			int dy = Math.abs(y2 - y1);
			int rozdil = dx - dy;
			int posun_x;
			if(x1 < x2)
				posun_x = 1;
			else
				posun_x = -1;
			int posun_y;
			if(y1 < y2)
				posun_y = 1;
			else
				posun_y = -1;
			while(x1 != x2 || y1 != y2) 
			{
				int p = 2 * rozdil;
				if(p > -dy)
				{
					rozdil -= dy;
					x1 += posun_x;
				}
				if(p < dx)
				{
					rozdil += dx;
					y1 += posun_y;
				}
				if(cellMap.keySet().contains(x1)) {
					if(cellMap.get(x1).contains(y1)) {
						add(role, x1, y1);
					}
				}
			
			}
		}
	}

	public int[] getBounds(Coordinate coordinates[])
	{
		int bounds[] = new int[4];
		int minX = 0x7fffffff;
		int minY = 0x7fffffff;
		int maxX = 0;
		int maxY = 0;
		Coordinate acoordinate[];
		int j = (acoordinate = coordinates).length;
		for(int i = 0; i < j; i++)
		{
			Coordinate c = acoordinate[i];
			if(c.x > (double)maxX)
				maxX = (int)c.x;
			if(c.y > (double)maxY)
				maxY = (int)c.y;
			if(c.x < (double)minX)
				minX = (int)c.x;
			if(c.y < (double)minY)
				minY = (int)c.y;
		}

		bounds[0] = minX - 1;
		bounds[1] = minY - 1;
		bounds[2] = maxX + 1;
		bounds[3] = maxY + 1;
		return bounds;
	}

	

	public boolean xHasNext()
	{
		return xIterator.hasNext();
	}

	public boolean yHasNext()
	{
		return yIterator.hasNext();
	}

	public boolean roleHasNext()
	{
		return roleIterator.hasNext();
	}

	public void cellSynchronisation(){

		for(int b = 0; b < grid.getPropertiesName().size(); b ++){
		
			String name = grid.getPropertiesName().get(b);

			List<Double> values = grid.getGeomTemp2Values(b);
			if(values != null && !values.isEmpty()){

				if(aggregMap.keySet().contains(name)){
					
					Double d;
					CellAggregOperator cao = aggregMap.get(name);
					Double value = grid.getDoubleValue(b, currentxKey, currentyKey);
					if(cao.preval() == false){
						d = cao.apply(values, value);
					}else{
						values.add(value);
						d = cao.apply(values, value);
					}

					grid.setCellValue(b, currentxKey, currentyKey, d);

				} else{				

					if(values.size() > 1){
						grid.setCellValue(b, currentxKey, currentyKey, values.get((int)(Math.random() * values.size())));
					}else{
						grid.setCellValue(b, currentxKey, currentyKey, values.get(0));
					}

				}
			}

		}
		grid.clearGeomTempVal2();

	}
	
	/*public void cellSynchronisation(){

		for(Iterator iterator = grid.getTempName().iterator(); iterator.hasNext();)
		{
			String name = (String)iterator.next();

			List<Double> values = grid.getGeomTempValues(name);
			if(!values.isEmpty()){

				if(aggregMap.keySet().contains(name))
				{
					Double d;
					CellAggregOperator cao = aggregMap.get(name);
					Double value = grid.getDoubleValue(name, currentxKey, currentyKey);
					if(cao.preval() == false){
						d = cao.apply(values, value);
					}else{
						values.add(value);
						d = cao.apply(values, value);
					}

					grid.setCellValue(name, currentxKey, currentyKey, d);

				} else{				

					if(values.size() > 1){
						grid.setCellValue(name, currentxKey, currentyKey, values.get((int)(Math.random() * values.size())));
					}else{
						grid.setCellValue(name, currentxKey, currentyKey, values.get(0));
					}

				}
			}

		}
		grid.clearGeomTempVal();

	}*/

	public void xNext(){

		currentxKey = xIterator.next().intValue();
		yIterator = matrice.get(currentxKey).keySet().iterator();
		yNext();
	}

	public void yNext(){

		currentyKey = ((Integer)yIterator.next()).intValue();
		roleIterator = matrice.get(currentxKey).get(currentyKey).iterator();
		roleNext();
	}

	public void roleNext(){

		currentRoleKey = roleIterator.next();
	}

	public boolean hasNext(){
		return nexter.hasNext();
		/*if(!matrice.isEmpty() && !xHasNext() && !yHasNext() && !roleHasNext()){

			cellSynchronisation();
			resetIterator();
			grid.clearGeomTempVal();
			grid.setMode(1);
			return false;
		}
		return true;*/

	}

		public abstract class Nexter {
			
			public abstract boolean hasNext();
			public abstract void next();
		}
		
		public class ValideNexter extends Nexter{
			public boolean hasNext() {
				if(!xHasNext() && !yHasNext() && !roleHasNext()){
					
					cellSynchronisation();
					resetIterator();
					grid.clearGeomTempVal2();
					grid.setMode(1);
					return false;
				}
				return true;
			}
			
			public void next() {
				if(roleHasNext()){

					roleNext();
				}else{
					cellSynchronisation();

					if(yHasNext()){

						yNext();
					}else{

						xNext();
					}
				}
				update();
			}
		}
		
		public class UnValideNexter extends Nexter{
			public boolean hasNext() {
				return false;
			}
		
		public void next(){
		}


		
	}

		public void next() {
			nexter.next();
		}
	public List<R2> getGeomEntities()
	{
		return geomEntities;
	}

	public abstract void update();

	public Integer getX()
	{
		return currentxKey;
	}

	public Integer getY()
	{
		return currentyKey;
	}


	public R2 getGeomEntity(){
		return currentRoleKey.getGeom();
	}

	public void setMode(int mode)
	{
		grid.setMode(mode);
	}


	public void quadrilateralExtract(List<R2> list){

		HashMap<Integer, ArrayList<Line>> lines = makeLines();

		for(R2 r2 : list){

			OcltRole e = (OcltRole)r2;
			if(e.getSpatialType() instanceof Polygon){
				setQuadrilateralCells(r2, (Polygon)e.getSpatialType(), lines);
			}
			if(e.getSpatialType() instanceof MultiPolygon)
			{

				MultiPolygon mp = (MultiPolygon)e.getSpatialType();
				for(int i = 0; i < mp.getNumGeometries(); i++){
					setQuadrilateralCells(r2, (Polygon)mp.getGeometryN(i), lines);
				}
			}
			if(e.getSpatialType() instanceof Line)
				setQuadrilateralCells(r2, (Line)e.getSpatialType());
			if(e.getSpatialType() instanceof MultiLine)
			{
				MultiLine ml = (MultiLine)e.getSpatialType();
				for(int i = 0; i < ml.getNumGeometries(); i++)
					setQuadrilateralCells(r2, (Line)ml.getGeometryN(i));

			}

			if(e.getSpatialType() instanceof Point)
				setQuadrilateralCells(r2, (Point)e.getSpatialType());
			if(e.getSpatialType() instanceof MultiPoint)
			{
				MultiPoint mp = (MultiPoint)e.getSpatialType();
				for(int i = 0; i < mp.getNumGeometries(); i++)
					setQuadrilateralCells(r2, (Point)mp.getGeometryN(i));

			}

		}

	}
	public void quadrilateralExtractCut(List<R2> list){

		HashMap<Integer, ArrayList<Line>> lines = makeLines();

		for(R2 r2 : list){

			OcltRole e = (OcltRole)r2;
			if(e.getSpatialType() instanceof Polygon){
				setQuadrilateralCellsCut(r2, (Polygon)e.getSpatialType(), lines);
			}
			if(e.getSpatialType() instanceof MultiPolygon)
			{

				MultiPolygon mp = (MultiPolygon)e.getSpatialType();
				for(int i = 0; i < mp.getNumGeometries(); i++){
					setQuadrilateralCellsCut(r2, (Polygon)mp.getGeometryN(i), lines);
				}
			}
			if(e.getSpatialType() instanceof Line)
				setQuadrilateralCellsCut(r2, (Line)e.getSpatialType());
			if(e.getSpatialType() instanceof MultiLine)
			{
				MultiLine ml = (MultiLine)e.getSpatialType();
				for(int i = 0; i < ml.getNumGeometries(); i++)
					setQuadrilateralCellsCut(r2, (Line)ml.getGeometryN(i));

			}

			if(e.getSpatialType() instanceof Point)
				setQuadrilateralCellsCut(r2, (Point)e.getSpatialType());
			if(e.getSpatialType() instanceof MultiPoint)
			{
				MultiPoint mp = (MultiPoint)e.getSpatialType();
				for(int i = 0; i < mp.getNumGeometries(); i++)
					setQuadrilateralCellsCut(r2, (Point)mp.getGeometryN(i));

			}

		}

	}
	public void hexagonalExtract(List<R2> list){
		for(R2 r2 : list){

			OcltRole e = (OcltRole)r2;
			if(e.getSpatialType() instanceof Polygon)
				setHexagonalCells(r2, (Polygon)e.getSpatialType());
			if(e.getSpatialType() instanceof MultiPolygon)
			{

				MultiPolygon mp = (MultiPolygon)e.getSpatialType();
				for(int i = 0; i < mp.getNumGeometries(); i++)
					setHexagonalCells(r2, (Polygon)mp.getGeometryN(i));

			}
			if(e.getSpatialType() instanceof Line)
				setQuadrilateralCells(r2, (Line)e.getSpatialType());
			if(e.getSpatialType() instanceof MultiLine)
			{
				MultiLine ml = (MultiLine)e.getSpatialType();
				for(int i = 0; i < ml.getNumGeometries(); i++)
					setHexagonalCells(r2, (Line)ml.getGeometryN(i));

			}
			if(e.getSpatialType() instanceof Point)
				setHexagonalCells(r2, (Point)e.getSpatialType());
			if(e.getSpatialType() instanceof MultiPoint)
			{
				MultiPoint mp = (MultiPoint)e.getSpatialType();
				for(int i = 0; i < mp.getNumGeometries(); i++)
					setHexagonalCells(r2, (Point)mp.getGeometryN(i));

			}
		}

	}
	public void hexagonalExtractCut(List<R2> list){
		for(R2 r2 : list){

			OcltRole e = (OcltRole)r2;
			if(e.getSpatialType() instanceof Polygon)
				setHexagonalCellsCut(r2, (Polygon)e.getSpatialType());
			if(e.getSpatialType() instanceof MultiPolygon)
			{

				MultiPolygon mp = (MultiPolygon)e.getSpatialType();
				for(int i = 0; i < mp.getNumGeometries(); i++)
					setHexagonalCellsCut(r2, (Polygon)mp.getGeometryN(i));

			}
			if(e.getSpatialType() instanceof Line)
				setQuadrilateralCellsCut(r2, (Line)e.getSpatialType());
			if(e.getSpatialType() instanceof MultiLine)
			{
				MultiLine ml = (MultiLine)e.getSpatialType();
				for(int i = 0; i < ml.getNumGeometries(); i++)
					setHexagonalCellsCut(r2, (Line)ml.getGeometryN(i));

			}
			if(e.getSpatialType() instanceof Point)
				setHexagonalCellsCut(r2, (Point)e.getSpatialType());
			if(e.getSpatialType() instanceof MultiPoint)
			{
				MultiPoint mp = (MultiPoint)e.getSpatialType();
				for(int i = 0; i < mp.getNumGeometries(); i++)
					setHexagonalCellsCut(r2, (Point)mp.getGeometryN(i));

			}
		}

	}
	public void triangularExtract(List<R2> list){

		HashMap<Integer, ArrayList<Line>> lines = makeLines();

		for(R2 r2 : list){

			OcltRole e = (OcltRole)r2;
			if(e.getSpatialType() instanceof Polygon)
				setTriangularCells(r2, (Polygon)e.getSpatialType(), lines);
			if(e.getSpatialType() instanceof MultiPolygon)
			{

				MultiPolygon mp = (MultiPolygon)e.getSpatialType();
				for(int i = 0; i < mp.getNumGeometries(); i++)
					setTriangularCells(r2, (Polygon)mp.getGeometryN(i), lines);

			}
			if(e.getSpatialType() instanceof Line)
				setTriangularCells(r2, (Line)e.getSpatialType());
			if(e.getSpatialType() instanceof MultiLine)
			{
				MultiLine ml = (MultiLine)e.getSpatialType();
				for(int i = 0; i < ml.getNumGeometries(); i++)
					setTriangularCells(r2, (Line)ml.getGeometryN(i));

			}
			if(e.getSpatialType() instanceof Point)
				setTriangularCells(r2, (Point)e.getSpatialType());
			if(e.getSpatialType() instanceof MultiPoint)
			{
				MultiPoint mp = (MultiPoint)e.getSpatialType();
				for(int i = 0; i < mp.getNumGeometries(); i++)
					setTriangularCells(r2, (Point)mp.getGeometryN(i));

			}
		}

	}
	
	public void triangularExtractCut(List<R2> list){

		HashMap<Integer, ArrayList<Line>> lines = makeLines();

		for(R2 r2 : list){

			OcltRole e = (OcltRole)r2;
			if(e.getSpatialType() instanceof Polygon)
				setTriangularCellsCut(r2, (Polygon)e.getSpatialType(), lines);
			if(e.getSpatialType() instanceof MultiPolygon)
			{

				MultiPolygon mp = (MultiPolygon)e.getSpatialType();
				for(int i = 0; i < mp.getNumGeometries(); i++)
					setTriangularCellsCut(r2, (Polygon)mp.getGeometryN(i), lines);

			}
			if(e.getSpatialType() instanceof Line)
				setTriangularCellsCut(r2, (Line)e.getSpatialType());
			if(e.getSpatialType() instanceof MultiLine)
			{
				MultiLine ml = (MultiLine)e.getSpatialType();
				for(int i = 0; i < ml.getNumGeometries(); i++)
					setTriangularCellsCut(r2, (Line)ml.getGeometryN(i));

			}
			if(e.getSpatialType() instanceof Point)
				setTriangularCellsCut(r2, (Point)e.getSpatialType());
			if(e.getSpatialType() instanceof MultiPoint)
			{
				MultiPoint mp = (MultiPoint)e.getSpatialType();
				for(int i = 0; i < mp.getNumGeometries(); i++)
					setTriangularCellsCut(r2, (Point)mp.getGeometryN(i));

			}
		}

	}

	public void connect(R1 cell, R2 role){

		Cell c = (Cell)cell.getSpatialType();
		add(role, c.getX(), c.getY());
		if(added.keySet().contains(role)){
			added.get(role).add(new Integer[]{c.getX(), c.getY()});
		}else{
			ArrayList<Integer[]> coords = new ArrayList<Integer[]>();
			coords.add(new Integer[]{c.getX(), c.getY()});
			added.put(role, coords);
		}
		xIterator = matrice.keySet().iterator();
		currentxKey = xIterator.next().intValue();
		yIterator = matrice.get(currentxKey).keySet().iterator();
		currentyKey = yIterator.next().intValue();
		roleIterator = matrice.get(currentxKey).get(currentyKey).iterator();

	}
	public void disconnectAll(){
		matrice = new LinkedHashMap<Integer, LinkedHashMap<Integer,ArrayList<GeomContainer>>>();
	}
	public void disconnect(R1 cell, R2 role){

		Cell c = (Cell)cell.getSpatialType();

		if(matrice.containsKey(c.getX())){

			if(matrice.get(c.getX()).containsKey(c.getY())){

				if(matrice.get(c.getX()).get(c.getY()).contains(role)){

					matrice.get(c.getX()).get(c.getY()).remove(role);

					if(matrice.get(c.getX()).get(c.getY()).isEmpty()){

						matrice.get(c.getX()).remove(c.getY());

						if(matrice.get(c.getX()).isEmpty()){

							matrice.remove(c.getX());
						}
					}
					xIterator = matrice.keySet().iterator();
					currentxKey = xIterator.next().intValue();
					yIterator = matrice.get(currentxKey).keySet().iterator();
					currentyKey = yIterator.next().intValue();
					roleIterator = matrice.get(currentxKey).get(currentyKey).iterator();
				}


			}
		}



	}

	/*   public void connect(R2 r2, Geometry zone){
	HashMap<Integer, Line> lines = makeLines();
	if(grid.getCellShapeType().equals("QUADRILATERAL")){
	    OcltRole e = (OcltRole)r2;
       if(e.getSpatialType() instanceof Polygon)
           //setQuadrilateralCells(r2, (Polygon)e.getSpatialType(), lines);
       setQuadrilateralCells(r2, (Polygon)e.getSpatialType());
       if(e.getSpatialType() instanceof MultiPolygon)
       {
           MultiPolygon mp = (MultiPolygon)e.getSpatialType();
           for(int i = 0; i < mp.getNumGeometries(); i++)
           	// setQuadrilateralCells(r2, (Polygon)e.getSpatialType(), lines);
               setQuadrilateralCells(r2, (Polygon)mp.getGeometryN(i));
       }
       if(e.getSpatialType() instanceof Line)
           setQuadrilateralCells(r2, (Line)e.getSpatialType());
       if(e.getSpatialType() instanceof MultiLine)
       {
           MultiLine ml = (MultiLine)e.getSpatialType();
           for(int i = 0; i < ml.getNumGeometries(); i++)
               setQuadrilateralCells(r2, (Line)ml.getGeometryN(i));
       }
       if(e.getSpatialType() instanceof Point)
           setQuadrilateralCells(r2, (Point)e.getSpatialType());
       if(e.getSpatialType() instanceof MultiPoint)
       {
           MultiPoint mp = (MultiPoint)e.getSpatialType();
           for(int i = 0; i < mp.getNumGeometries(); i++)
               setQuadrilateralCells(r2, (Point)mp.getGeometryN(i));
       }
	}
	if(grid.getCellShapeType().equals("HEXAGONAL")){
	     OcltRole e = (OcltRole)r2;
        if(e.getSpatialType() instanceof Polygon)
            setHexagonalCells(r2, (Polygon)e.getSpatialType());
        if(e.getSpatialType() instanceof MultiPolygon)
        {
            MultiPolygon mp = (MultiPolygon)e.getSpatialType();
            for(int i = 0; i < mp.getNumGeometries(); i++)
                setHexagonalCells(r2, (Polygon)mp.getGeometryN(i));
        }
        if(e.getSpatialType() instanceof Line)
            setQuadrilateralCells(r2, (Line)e.getSpatialType());
        if(e.getSpatialType() instanceof MultiLine)
        {
            MultiLine ml = (MultiLine)e.getSpatialType();
            for(int i = 0; i < ml.getNumGeometries(); i++)
                setHexagonalCells(r2, (Line)ml.getGeometryN(i));
        }
        if(e.getSpatialType() instanceof Point)
            setHexagonalCells(r2, (Point)e.getSpatialType());
        if(e.getSpatialType() instanceof MultiPoint)
        {
            MultiPoint mp = (MultiPoint)e.getSpatialType();
            for(int i = 0; i < mp.getNumGeometries(); i++)
                setHexagonalCells(r2, (Point)mp.getGeometryN(i));
        }
	}
	if(grid.getCellShapeType().equals("TRIANGULAR")){
	    OcltRole e = (OcltRole)r2;
       if(e.getSpatialType() instanceof Polygon)
           setTriangularCells(r2, (Polygon)e.getSpatialType());
       if(e.getSpatialType() instanceof MultiPolygon)
       {
           MultiPolygon mp = (MultiPolygon)e.getSpatialType();
           for(int i = 0; i < mp.getNumGeometries(); i++)
               setTriangularCells(r2, (Polygon)mp.getGeometryN(i));
       }
       if(e.getSpatialType() instanceof Line)
           setTriangularCells(r2, (Line)e.getSpatialType());
       if(e.getSpatialType() instanceof MultiLine)
       {
           MultiLine ml = (MultiLine)e.getSpatialType();
           for(int i = 0; i < ml.getNumGeometries(); i++)
               setTriangularCells(r2, (Line)ml.getGeometryN(i));
       }
       if(e.getSpatialType() instanceof Point)
           setTriangularCells(r2, (Point)e.getSpatialType());
       if(e.getSpatialType() instanceof MultiPoint)
       {
           MultiPoint mp = (MultiPoint)e.getSpatialType();
           for(int i = 0; i < mp.getNumGeometries(); i++)
               setTriangularCells(r2, (Point)mp.getGeometryN(i));
       }
	}
	xIterator = matrice.keySet().iterator();
      currentxKey = xIterator.next().intValue();
      yIterator = matrice.get(currentxKey).keySet().iterator();
      currentyKey = yIterator.next().intValue();
      roleIterator = matrice.get(currentxKey).get(currentyKey).iterator();
}*/

	public void disconnect(R2 r2, Geometry zone){


		if(zone instanceof Polygon){

			disconnectPolygon((Polygon)zone, r2);
		}
		if(zone instanceof MultiPolygon)
		{

			MultiPolygon mp = (MultiPolygon)zone;
			for(int k = 0; k < mp.getNumGeometries(); k++){

				disconnectPolygon((Polygon)mp.getGeometryN(k), r2);
			}


		}
		if(zone instanceof Line){

			disconnectLine((Line)zone, r2);
		}

		if(zone instanceof MultiLine)
		{
			MultiLine ml = (MultiLine)zone;
			for(int i = 0; i < ml.getNumGeometries(); i++){
				disconnectLine((Line)ml.getGeometryN(i), r2);

			}


		}
		if(zone instanceof Point){


			int[] ic = grid.gridCoordinate(zone.getCentroid().getX(), zone.getCentroid().getY());
			disconnect(ic[0], ic[1], r2);

		}

		if(zone instanceof MultiPoint)
		{
			MultiPoint mp = (MultiPoint)zone;
			for(int i = 0; i < mp.getNumGeometries(); i++){

				int[] ic = grid.gridCoordinate(mp.getGeometryN(i).getCentroid().getX(), mp.getGeometryN(i).getCentroid().getY());
				disconnect(ic[0], ic[1], r2);
			}


		}

	}

	public void disconnectPolygon(Polygon polygon, R2 role){

		int bounds[] = grid.intBounds(polygon);
		int index = 0;
		for(int i = bounds[0]; i < bounds[2]; i++)
		{
			for(int j = bounds[1]; j < bounds[3]; j++)
			{
				Coordinate c = grid.gridCoordinate(i, j);
				Point point = Point.xy(Double.valueOf(c.x), Double.valueOf(c.y));
				if(polygon.touches(point) || point.within(polygon))
				{
					disconnect( i , j, role);
					index ++;

				}
			}

		}

		if(index == 0)
		{
			int scaledCentroid[] = grid.gridCoordinate(polygon.getCentroid().getX(), polygon.getCentroid().getY());
			disconnect(scaledCentroid[0], scaledCentroid[1], role);
		}


	}

	public void disconnectLine(Line line, R2 role){

		Coordinate coordinates[] = line.getCoordinates();
		for(int i = 0; i < coordinates.length - 1; i++)
		{
			int c1[] = grid.gridCoordinate(coordinates[i].x, coordinates[i].y);
			int c2[] = grid.gridCoordinate(coordinates[i + 1].x, coordinates[i + 1].y);
			int x = c1[0];
			int y = c1[1];
			int x2 = c2[0];
			int y2 = c2[1];
			disconnectBresenham(x, y, x2, y2, role);
		}


	}

	public void disconnectBresenham(int x1, int y1, int x2, int y2, R2 role)
	{
		if(x1 == x2 && y1 == y2)
		{
			add(role, x1 - grid.getMinX(), y1 - grid.getMinY());
		} else
		{
			int dx = Math.abs(x2 - x1);
			int dy = Math.abs(y2 - y1);
			int rozdil = dx - dy;
			int posun_x;
			if(x1 < x2)
				posun_x = 1;
			else
				posun_x = -1;
			int posun_y;
			if(y1 < y2)
				posun_y = 1;
			else
				posun_y = -1;
			while(x1 != x2 || y1 != y2) 
			{
				int p = 2 * rozdil;
				if(p > -dy)
				{
					rozdil -= dy;
					x1 += posun_x;
				}
				if(p < dx)
				{
					rozdil += dx;
					y1 += posun_y;
				}
				disconnect( x1, y1, role);
			}
		}
	}

	public void disconnect(int x, int y, R2 role){
		if(matrice.containsKey(x)){
			if(matrice.get(x).containsKey(y)){
				if(matrice.get(x).get(y).contains(role)){
					matrice.get(x).get(y).remove(role);
				}
			}
		}
	}
	public Grid getGrid(){
		return grid;
	}



	private Geometry getCellShape(Point p){
		if(grid.getCellShapeType().equals("HEXAGONAL")){

		}
		if(grid.getCellShapeType().equals("QUADRILATERAL")){

			double dx = p.getX();
			double dy = p.getY();

			Coordinate[] coords= new Coordinate[5];
			coords[0] = new Coordinate(dx + grid.getXRes() / 2, dy + grid.getYRes() / 2);
			coords[1] = new Coordinate(dx + grid.getXRes() / 2, dy - grid.getYRes() / 2);
			coords[2] = new Coordinate(dx - grid.getXRes() / 2, dy - grid.getYRes() / 2);
			coords[3] = new Coordinate(dx - grid.getXRes() / 2, dy + grid.getYRes() / 2);
			coords[4] = new Coordinate(dx + grid.getXRes() / 2, dy +grid.getYRes() / 2);
			return toPolygon(coords);
		}

		return null;
	}

	private Polygon toPolygon(Coordinate[] c){


		CoordinateSequence cs = new CoordinateArraySequence(c);

		LinearRing lr = new LinearRing(cs,new OceletGeomFactory());

		Polygon poly = new Polygon(lr, null,new OceletGeomFactory());

		return poly;

	}

	public void morph(Double buffer){

		HashMap<R2, ArrayList<Geometry>> geoms = new HashMap<R2, ArrayList<Geometry>>();

		for(R2 r : added.keySet()){

			for(Integer[] coords : added.get(r)){

				Coordinate wCoord = grid.gridCoordinate(coords[0], coords[1]);

				Point p = Point.xy(wCoord.x, wCoord.y);
				if(geoms.containsKey(r)){
					geoms.get(r).add(getCellShape(p));
				}else{

					ArrayList<Geometry> gList = new ArrayList<Geometry>();
					gList.add(getCellShape(p));
					geoms.put(r, gList);
				}
			}
		}





		if(!geoms.isEmpty())
			convert2(geoms, buffer);

		added.clear();
	}
	public void convert2(HashMap<R2, ArrayList<Geometry>> geoms, Double distance){    	    	

		for(R2 role : geoms.keySet()){

			Geometry initG = ((Geometry)role.getSpatialType()).buffer(0.0);    	
			Geometry[] gs = new Geometry[geoms.get(role).size()];    		    		

			for(int i = 0; i < geoms.get(role).size(); i ++){

				gs[i] = geoms.get(role).get(i);

			}



			GeometryCollection gc = new GeometryCollection(gs,SpatialManager.geometryFactory());    		

			Geometry g3 = gc.buffer(0.0);

			Geometry[] gs2 = new Geometry[g3.getNumGeometries() + 1];
			gs2[0] = initG;




			for(int i = 0; i < g3.getNumGeometries(); i ++){

				gs2[i + 1] = g3.getGeometryN(i);




			}


			GeometryCollection gc2 = new GeometryCollection(gs2,SpatialManager.geometryFactory());    	    	
			Geometry g = gc2.buffer(0.0);    		

			String name = role.getPropName(role.getSpatialType());    	

			for(R2 role2 : geoms.keySet()){

				if(!role.equals(role2)){

					Geometry g2 = (Geometry)role2.getSpatialType();

					if(g.intersects(g2)){


						g2 = g2.difference(g);

						g= g.difference(g2);


						if(g2 instanceof MultiPolygon && role2.getSpatialType() instanceof MultiPolygon){    	    			
							role2.setProperty(name, g2);
						}

						if(g2 instanceof Polygon && role2.getSpatialType() instanceof MultiPolygon){

							Polygon[] ps = new Polygon[1];
							ps[0] = (Polygon)g2;
							MultiPolygon mp = new MultiPolygon(ps, SpatialManager.geometryFactory());


							role2.setProperty(name, mp);
						}

					}
				}
			}

			if(g instanceof MultiPolygon && role.getSpatialType() instanceof MultiPolygon){  			

				role.setProperty(name, g);
			}

			if(g instanceof Polygon && role.getSpatialType() instanceof MultiPolygon){
				Polygon[] ps = new Polygon[1];    		
				ps[0] = (Polygon)g;
				MultiPolygon mp = new MultiPolygon(ps, SpatialManager.geometryFactory());


				role.setProperty(name, mp);
			}

		}
	}
	public Iterator<R1> getCells(){
		return cellIterator;
	}
	public class CellIterator<R1 extends OcltRole> implements Iterator<R1>{


		@Override
		public boolean hasNext() {

			boolean test = xHasNext() || yHasNext();
			if(!test){
				xIterator = matrice.keySet().iterator();
				currentxKey = xIterator.next();
				yIterator = matrice.get(currentxKey).keySet().iterator();
				currentyKey = yIterator.next();

			}
			return test;

		}

		@Override
		public R1 next() {
			if(yIterator.hasNext()){
				currentyKey = yIterator.next();
			}else{

				currentxKey = xIterator.next();
				yIterator = matrice.get(currentxKey).keySet().iterator();
				currentyKey = yIterator.next();


			}
			R1 r1 = (R1) getRole(0);
			Cell cell = (Cell)r1.getSpatialType();
			cell.setX(currentxKey);
			cell.setY(currentyKey);
			// TODO Auto-generated method stub
			return r1;
		}

		@Override
		public void remove() {
			// TODO Auto-generated method stub

		}

	}

	public abstract KeyMap<String, String> getEdgeProperties();


	private int doublePropertySize = 0;
	private int intPropertySize = 0;
	private int booleanPropertySize = 0;

	public void setDoublePropertySize(int size){
		this.doublePropertySize = size;
	}

	public void setIntegerPropertySize(int size){
		this.intPropertySize = size;
	}

	public void setBooleanPropertySize(int size){
		this.booleanPropertySize = size;
	}

	public Double getDoubleProperty(int property){
		return currentRoleKey.getDoubleValue(property);
	}
	public Integer getIntegerProperty(int property){
		return currentRoleKey.getIntegerValue(property);
	}
	public Boolean getBooleanProperty(int property){
		return currentRoleKey.getBooleanValue(property);
	}

	public void setIntegerProperty(int property, Integer value){
		currentRoleKey.setIntegerValue(property, value);
	}
	public void setDoubleProperty(int property, Double value){
		currentRoleKey.setDoubleValue(property, value);
	}
	public void setBooleanProperty(int property, Boolean value){
		currentRoleKey.setBooleanValue(property, value);
	}
	public void shuffle() {
		
		//Collections.shuffle(matrice.keySet());
	}
	private void initEdgeProperty(){

		KeyMap<String, String> properties = getEdgeProperties();
		int doubleIndex = 0;
		int integerIndex = 0;
		int booleanIndex = 0;

		for(String name : properties.keySet()){

			if(properties.get(name).equals("Double")){
				doubleIndex++;
			}
			if(properties.get(name).equals("Integer")){
				integerIndex++;
			}
			if(properties.get(name).equals("Boolean")){
				booleanIndex++;
			}
		}
		setIntegerPropertySize(integerIndex);
		setBooleanPropertySize(booleanIndex);
		setDoublePropertySize(doubleIndex);
	}

	public class GeomContainer{

		public R2 geom;
		public double[] doubleValues;
		public int[] intValues;
		public boolean[] booleanValues;

		public GeomContainer(R2 r2){
			geom = r2;
		}

		public R2 getGeom(){
			return geom;
		}
		public void initDouble(int size){
			doubleValues = new double[size];
		}

		public void initInteger(int size){

			intValues = new int[size];
		}

		public void initBoolean(int size){
			booleanValues = new boolean[size];
		}

		public void setBooleanValue(int property, Boolean value){
			booleanValues[property] = value;
		}

		public Boolean getBooleanValue(int property){
			return booleanValues[property];
		}

		public void setIntegerValue(int property, Integer value){
			intValues[property] = value;
		}

		public Integer getIntegerValue(int property){
			return intValues[property];
		}

		public void setDoubleValue(int property, Double value){
			doubleValues[property] = value;
		}

		public Double getDoubleValue(int property){
			return doubleValues[property];
		}


	}


}