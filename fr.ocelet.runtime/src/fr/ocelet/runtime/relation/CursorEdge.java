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
package fr.ocelet.runtime.relation;

import fr.ocelet.runtime.entity.AbstractEntity;
import fr.ocelet.runtime.geom.ocltypes.Cell;
import fr.ocelet.runtime.ocltypes.KeyMap;
import fr.ocelet.runtime.ocltypes.List;
import fr.ocelet.runtime.raster.CellAggregOperator;
import fr.ocelet.runtime.raster.Grid;
import fr.ocelet.runtime.raster.GridCellManager;
import fr.ocelet.runtime.raster.RasterCursor;
import fr.ocelet.runtime.raster.RasterHexagonalCursor;
import fr.ocelet.runtime.raster.RasterMultiQuadrilateralCursor;
import fr.ocelet.runtime.raster.RasterQuadrilateralCursor;
import fr.ocelet.runtime.raster.RasterTriangularCursor;
import fr.ocelet.runtime.relation.regularedges.SimpleCellEdgesManager;

// Referenced classes of package fr.ocelet.runtime.relation:
//            OcltEdge, GridCellManager, OcltRole, AggregOperator
/**
 * @author Mathieu Castets - Initial contribution
 */
public abstract class CursorEdge extends OcltEdge {


	protected int x;
	protected int y;
	protected int x2;
	protected int y2;
	
	protected Grid grid;
	private int direction;
	private boolean state;
	private GridCellManager gridManager;
	private int index;
	private RasterCursor cursor;
	public static String TYPE_QUADRILATERAL = "QUADRILATERAL";
	public static String TYPE_HEXAGONAL = "HEXAGONAL";
	public static String TYPE_TRIANGULAR = "TRIANGULAR";
	
	
	
	private SimpleCellEdgesManager edges = new SimpleCellEdgesManager();
	
	public abstract KeyMap<String, String> getEdgeProperties();
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
	public void setIntegerPropertySize(int size){
		edges.addIntegerEdge(size, grid, 0);
	}
	public void setBooleanPropertySize(int size){
		edges.addBooleanEdge(size, grid, 0);
	}
	public void setDoublePropertySize(int size){
		edges.addDoubleEdge(size, grid, 0);
	}
	
	public void setDoubleProperty(int property, Double value){
		edges.setDoubleValue(x, y, x2, y2, property, value);
	}
	public void setIntegerProperty(int property, Integer value){
		edges.setIntegerValue(x, y, x2, y2, property, value);
	}
	public void setBooleanProperty(int property, Boolean value){
		edges.setBooleanValue(x, y, x2, y2, property, value);
	}
	
	public Double getDoubleProperty(int property){
		return edges.getDoubleValue(x , y, x2, y2, property);
	}
	
	public Integer getIntegerProperty(int property){
		return edges.getIntegerValue(x, y, x2, y2, property);
	}
	public Boolean getBooleanProperty(int property){
		return edges.getBooleanValue(x, y, x2, y2, property);
	}

	public String getCellType(){

		if(cursor instanceof RasterHexagonalCursor){
			return TYPE_HEXAGONAL;
		}

		if(cursor instanceof RasterQuadrilateralCursor){
			return TYPE_QUADRILATERAL;
		}

		if(cursor instanceof RasterTriangularCursor){
			return TYPE_TRIANGULAR;
		}
		return null;
	}

	public void extendedMoore(int n){
		grid.extendedMoore(n);
		RasterMultiQuadrilateralCursor rmqc= new RasterMultiQuadrilateralCursor(grid);
		rmqc.setSize(n, 0);
		cursor = rmqc;
	}
	public void extendedCircularMoore(int n){
		grid.extendedMoore(n);
		RasterMultiQuadrilateralCursor rmqc= new RasterMultiQuadrilateralCursor(grid);
		rmqc.setSize(n, 1);
		cursor = rmqc;
	}
	public void setCursorType(String type){
		gridManager = grid.getGridCellManager();

		if(type.equals(TYPE_HEXAGONAL)){

			cursor = new RasterHexagonalCursor(grid);
		}

		if(type.equals(TYPE_QUADRILATERAL)){

			cursor = new RasterQuadrilateralCursor(grid);
		}

		if(type.equals(TYPE_TRIANGULAR)){

			cursor = new RasterTriangularCursor(grid);
		}

	}
	/*public CursorEdge(int numGrid){


		this.numGrid = numGrid;
		cursor = new RasterQuadrilateralCursor(numGrid);
		x = 0;
		y = 0;
		x2 = 0;
		y2 = 0;
		direction = 0;
		state = true;
		index = 0;
		grid = GridManager.getInstance().get(numGrid);
		gridManager = grid.getGridCellManager();
		initEdgeProperty();
		update();
	}*/

	public CursorEdge(List<? extends AbstractEntity> list){

		AbstractEntity ae = (AbstractEntity)list.get(0);
		Cell cell = (Cell)ae.getSpatialType();

		this.grid = cell.getGrid();
		cursor = new RasterQuadrilateralCursor(grid);
		x = 0;
		y = 0;
		x2 = 0;
		y2 = 0;
		direction = 0;
		state = true;
		index = 0;
		
		gridManager = grid.getGridCellManager();
		initEdgeProperty();

	}

	public abstract void update();

	public boolean hasNext(){


		if(!cursor.hasNext()){

			update();
			cursor.reset();    		
			grid.setMode(1);
			return false;
			
		}
		return true;


		/*  if(x == grid.getWidth() - 2 && y == grid.getHeight() - 1 && x2 == grid.getWidth() - 1 && y2 == grid.getHeight() - 1)
        {
            x = 0;
            y = 0;
            x2 = 0;
            y2 = 0;
            direction = 0;        
            update();
            gridManager.reset();
            return false;
        } else
        {
            return true;
        }*/
	}

	public void next(){
		cursor.next();
		x = cursor.getX();
		y = cursor.getY();
		x2 = cursor.getX2();
		y2 = cursor.getY2();

		update();

		/*
        while(!setEnd2()) ;
        update();*/
	}

	public void move()
	{
		if(x == grid.getWidth() - 1){

			index++;
			x = 0;
			y++;
			gridManager.increment();
		} else {

			x++;
		}
	}

	public boolean isRight(){

		return direction == 0;
	}

	public boolean isRightDown()
	{
		return direction == 1;
	}

	public boolean isDown()
	{
		return direction == 2;
	}

	public boolean isDownLeft()
	{
		return direction == 3;
	}

	public boolean isLeft()
	{
		return direction == 4;
	}

	public boolean isUpLeft()
	{
		return direction == 5;
	}

	public boolean isUp()
	{
		return direction == 6;
	}

	public boolean isUpRight()
	{
		return direction == 7;
	}

	public void increment()
	{
		if(direction == 7)
		{
			direction = 0;
			move();
		} else
		{
			direction++;
		}
	}

	public boolean inbounds(int x, int y)
	{
		return x >= 0 && y >= 0 && x < grid.getWidth() && y < grid.getHeight();
	}

	public boolean state()
	{
		return state;
	}

	public boolean setEnd()
	{
		if(isRight())
		{
			increment();
			if(inbounds(x + 1, y))
			{
				x2 = x + 1;
				y2 = y;
				return true;
			} else
			{
				return false;
			}
		}
		if(isRightDown())
		{
			increment();
			if(inbounds(x + 1, y + 1))
			{
				x2 = x + 1;
				y2 = y + 1;
				return true;
			} else
			{
				return false;
			}
		}
		if(isDown())
		{
			increment();
			if(inbounds(x, y + 1))
			{
				x2 = x;
				y2 = y + 1;
				return true;
			} else
			{
				return false;
			}
		}
		if(isDownLeft())
		{
			increment();
			if(inbounds(x - 1, y + 1))
			{
				x2 = x - 1;
				y2 = y + 1;
				return true;
			} else
			{
				return false;
			}
		}
		if(isLeft())
		{
			increment();
			if(inbounds(x - 1, y))
			{
				x2 = x - 1;
				y2 = y;
				return true;
			} else
			{
				return false;
			}
		}
		if(isUpLeft())
		{
			increment();
			if(inbounds(x - 1, y - 1))
			{
				x2 = x - 1;
				y2 = y - 1;
				return true;
			} else
			{
				return false;
			}
		}
		if(isUp())
		{
			increment();
			if(inbounds(x, y - 1))
			{
				x2 = x;
				y2 = y - 1;
				return true;
			} else
			{
				return false;
			}
		}
		if(isUpRight())
		{
			increment();
			if(inbounds(x + 1, y - 1))
			{
				x2 = x + 1;
				y2 = y - 1;
				return true;
			} else
			{
				return false;
			}
		} else
		{
			return false;
		}
	}

	public int[] getEndPos()
	{
		return (new int[] {
				x2, y2
		});
	}

	public int[] getFirstPos()
	{
		return (new int[] {
				x, y
		});
	}

	public OcltRole getEnd()
	{
		return (OcltRole)gridManager.get(x2, y2);
	}

	public OcltRole getFirst()
	{
		return (OcltRole)gridManager.get(x, y);
	}

	public void clearProperties()
	{
		gridManager.clearProperties();
	}

	public int[] getPos1()
	{
		return (new int[] {
				x, y
		});
	}

	public int[] getPos2()
	{
		return (new int[] {
				x2, y2
		});
	}

	public void setCellOperator(CellAggregOperator operator)
	{
		gridManager.addOperator(operator, operator.getName());
	}

	public void setCellOperator(String name, AggregOperator operator, KeyMap<String, String> typeProps, Boolean preval)
	{
		CellAggregOperator cao = new CellAggregOperator();
		if(typeProps.get(name).equals("Double")){
			setAggregOpDouble(name, operator, preval);
		}else if(typeProps.get(name).equals("Integer")){
			setAggregOpInteger(name, operator, preval);

		}else if(typeProps.get(name).equals("Float")){
			setAggregOpFloat(name, operator, preval);

		}else if(typeProps.get(name).equals("Byte")){
			setAggregOpByte(name, operator, preval);

		}else if(typeProps.get(name).equals("Boolean")){
			setAggregOpBoolean(name, operator, preval);

		}
	}
	public void setAggregOpDouble(String name, AggregOperator<Double, List<Double>> agg, boolean val){
		CellAggregOperator cao = new CellAggregOperator();
		cao.setOperatorDouble(agg);
		gridManager.addOperator(cao, name);
	}
	public void setAggregOpInteger(String name, AggregOperator<Integer, List<Integer>> agg, boolean val){
		CellAggregOperator cao = new CellAggregOperator();
		cao.setOperatorInteger(agg);
		gridManager.addOperator(cao, name);
	}

	public void setAggregOpFloat(String name, AggregOperator<Float, List<Float>> agg, boolean val){
		CellAggregOperator cao = new CellAggregOperator();
		cao.setOperatorFloat(agg);
		gridManager.addOperator(cao, name);
	}

	public void setAggregOpByte(String name, AggregOperator<Byte, List<Byte>> agg, boolean val){
		CellAggregOperator cao = new CellAggregOperator();
		cao.setOperatorByte(agg);
		gridManager.addOperator(cao, name);
	}

	public void setAggregOpBoolean(String name, AggregOperator<Boolean, List<Boolean>> agg, boolean val){
		CellAggregOperator cao = new CellAggregOperator();
		cao.setOperatorBoolean(agg);
		gridManager.addOperator(cao, name);
	}

	public void cleanOperator()
	{
		gridManager.clearAggregMap();
	}

	public void increment2()
	{
		if(direction == 3)
		{
			direction = 0;
			move();
		} else
		{
			direction++;
		}
	}

	public boolean setEnd2()
	{
		if(isRight())
		{
			increment2();
			if(inbounds(x + 1, y))
			{
				x2 = x + 1;
				y2 = y;
				return true;
			} else
			{
				return false;
			}
		}
		if(isRightDown())
		{
			increment2();
			if(inbounds(x + 1, y + 1))
			{
				x2 = x + 1;
				y2 = y + 1;
				return true;
			} else
			{
				return false;
			}
		}
		if(isDown())
		{
			increment2();
			if(inbounds(x, y + 1))
			{
				x2 = x;
				y2 = y + 1;
				return true;
			} else
			{
				return false;
			}
		}
		if(isDownLeft())
		{
			increment2();
			if(inbounds(x - 1, y + 1))
			{
				x2 = x - 1;
				y2 = y + 1;
				return true;
			} else
			{
				return false;
			}
		} else
		{
			return false;
		}
	}

	
	

	public abstract OcltRole getRole(final Integer arg);
	public OcltRole getRole(int arg) {
		Integer i = arg;
		return getRole(i);
	}
	
	public abstract void updateCellType();


	public int getX(){
		return x;
	}

	public int getY(){
		return y;
	}

	public int getY2(){
		return y2;
	}

	public int getX2(){
		return x2;
	}


}
