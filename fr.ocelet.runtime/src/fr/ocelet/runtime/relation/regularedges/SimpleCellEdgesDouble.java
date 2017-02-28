package fr.ocelet.runtime.relation.regularedges;

import fr.ocelet.runtime.raster.Grid;

public class SimpleCellEdgesDouble {
	
	
	private double[][] edges;
	
	public SimpleCellEdgesDouble(Grid grid){
		
		edges = new double[grid.getWidth()][grid.getHeight()];
	}
	
	
	public Double get(int x, int y){
		return edges[x][y];
	}
	
	
	public void set(int x, int y,Double value){
		edges[x][y] = value;
	}
	
	
	
	
}
