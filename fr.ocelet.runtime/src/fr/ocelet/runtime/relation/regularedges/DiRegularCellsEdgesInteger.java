package fr.ocelet.runtime.relation.regularedges;

import fr.ocelet.runtime.raster.Grid;

public class DiRegularCellsEdgesInteger {
	private int[][] edges;
	
	public DiRegularCellsEdgesInteger(Grid grid1, Grid grid2){
		
		if(grid1.getWidth() * grid1.getHeight() > grid2.getWidth() * grid2.getHeight()){
			
			edges = new int[grid1.getWidth()][grid1.getHeight()];
		}else{
			edges = new int[grid2.getWidth()][grid2.getHeight()];

		}
		
	}
	
	public Integer get(int x, int y){
		return edges[x][y];
	}
	
	public void set(int x, int y, Integer value){
		edges[x][y] = value;
	}
}
