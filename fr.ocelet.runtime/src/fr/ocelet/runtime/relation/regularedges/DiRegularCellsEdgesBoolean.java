package fr.ocelet.runtime.relation.regularedges;

import fr.ocelet.runtime.raster.Grid;

public class DiRegularCellsEdgesBoolean {

	private boolean[][] edges;
	
	public DiRegularCellsEdgesBoolean(Grid grid1, Grid grid2){
		
		if(grid1.getWidth() * grid1.getHeight() > grid2.getWidth() * grid2.getHeight()){
			
			edges = new boolean[grid1.getWidth()][grid1.getHeight()];
		}else{
			edges = new boolean[grid2.getWidth()][grid2.getHeight()];

		}
		
	}
	
	public Boolean get(int x, int y){
		return edges[x][y];
	}
	
	public void set(int x, int y, Boolean value){
		edges[x][y] = value;
	}
}
