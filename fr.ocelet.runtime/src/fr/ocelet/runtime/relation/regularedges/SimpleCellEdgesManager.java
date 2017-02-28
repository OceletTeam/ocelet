package fr.ocelet.runtime.relation.regularedges;

import fr.ocelet.runtime.raster.Grid;

public class SimpleCellEdgesManager {
	
	
	private SimpleCellEdgesDouble[] edgeDouble;
	private SimpleCellEdgesInteger[] edgeInteger;
	private SimpleCellEdgesBoolean[] edgeBoolean;

	public void addDoubleEdge(int size, Grid grid, int distance){
		
		edgeDouble = new SimpleCellEdgesDouble[size];
		for(int i= 0; i < size ; i++){
			SimpleCellEdgesDouble mced = new SimpleCellEdgesDouble(grid);
			edgeDouble[i] = mced;
		}
	}
	
	public void addIntegerEdge(int size, Grid grid, int distance){
		
		edgeInteger = new SimpleCellEdgesInteger[size];
		for(int i= 0; i < size ; i++){
			SimpleCellEdgesInteger mcei = new SimpleCellEdgesInteger(grid);
			edgeInteger[i] = mcei;
		}
	}
	public void addBooleanEdge(int size, Grid grid, int distance){
		edgeBoolean = new SimpleCellEdgesBoolean[size];
		for(int i= 0; i < size ; i++){
			
			SimpleCellEdgesBoolean mceb = new SimpleCellEdgesBoolean(grid);
			edgeBoolean[i] = mceb;
		}
	}
	
	public Double getDoubleValue(int x1, int y1, int x2, int y2, int property){
		return edgeDouble[property].get(x2, y2);
	}
	
	public Integer getIntegerValue(int x1, int y1, int x2, int y2, int property){
		return edgeInteger[property].get(x2, y2);
	}
	public Boolean getBooleanValue(int x1, int y1, int x2, int y2, int property){
		return edgeBoolean[property].get(x2, y2);
	}
	
	public void setDoubleValue(int x1, int y1, int x2, int y2, int property, Double value){
		edgeDouble[property].set(x2, y2, value);
	}
	public void setIntegerValue(int x1, int y1, int x2, int y2, int property, Integer value){
		edgeInteger[property].set(x2, y2, value);
	}
	public void setBooleanValue(int x1, int y1, int x2, int y2, int property, Boolean value){
		edgeBoolean[property].set(x1, y1, value);
	}
	
	
}
