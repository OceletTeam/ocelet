package fr.ocelet.runtime.relation.regularedges;

import fr.ocelet.runtime.raster.Grid;

public class DiRegularCellsEdgeManager {
	
	private DiRegularCellsEdgesBoolean[] booleanValues;
	private DiRegularCellsEdgesDouble[] doubleValues;
	private DiRegularCellsEdgesInteger[] integerValues;
	
	public void setBooleanPropertySize(int size, Grid grid1, Grid grid2){
		if(size > 0){
			booleanValues = new DiRegularCellsEdgesBoolean[size];
			for(int i = 0; i< size; i ++){
				DiRegularCellsEdgesBoolean drce = new DiRegularCellsEdgesBoolean(grid1, grid2);
				booleanValues[i] = drce;
			}
		}
	}
	
	public void setIntegerPropertySize(int size, Grid grid1, Grid grid2){
		if(size > 0){
			integerValues = new DiRegularCellsEdgesInteger[size];
			for(int i = 0; i< size; i ++){
				DiRegularCellsEdgesInteger drce = new DiRegularCellsEdgesInteger(grid1, grid2);
				integerValues[i] = drce;
			}
		}
	}
	public void setDoublePropertySize(int size, Grid grid1, Grid grid2){
		if(size > 0){
			doubleValues = new DiRegularCellsEdgesDouble[size];
			for(int i = 0; i< size; i ++){
				DiRegularCellsEdgesDouble drce = new DiRegularCellsEdgesDouble(grid1, grid2);
				doubleValues[i] = drce;
			}
		}
	}
	
	public void setDoubleValue(int x, int y, int property, Double value){
		doubleValues[property].set(x, y, value);
	}
	
	public void setIntegerValue(int x, int y, int property, Integer value){
		integerValues[property].set(x, y, value);
	}
	
	public void setBooleanValue(int x, int y, int property, Boolean value){
		booleanValues[property].set(x, y, value);
	}
	
	public Double getDoubleValue(int x, int y, int property){
		return doubleValues[property].get(x, y);
	}
	
	public Boolean getBooleanValue(int x, int y, int property){
		return booleanValues[property].get(x, y);
	}
	
	public Integer getIntegerValue(int x, int y, int property){
		return integerValues[property].get(x, y);
	}
	
}
