package fr.ocelet.runtime.relation.regularedges;

import fr.ocelet.runtime.raster.Grid;

public class SimpleCellEdgesInteger {
	
	private int[][] edges;
	
	public SimpleCellEdgesInteger(Grid grid){
		
		edges = new int[grid.getWidth()][grid.getHeight()];
	}
	
	public Integer get(int x, int y){
		return edges[x][y];
	}
	
	public void set(int x, int y, Integer value){
		edges[x][y] = value;
	}
}
