package fr.ocelet.runtime.relation.regularedges;

import fr.ocelet.runtime.raster.Grid;

public class SimpleCellEdgesBoolean {
	
	private boolean[][] edges;
	
	public SimpleCellEdgesBoolean(Grid grid){
		
		edges = new boolean[grid.getWidth()][grid.getHeight()];
	}
	
	public Boolean get(int x, int y){
		return edges[x][y];
	}
	
	public void set(int x, int y, Boolean value){
		edges[x][y] = value;
	}
}
