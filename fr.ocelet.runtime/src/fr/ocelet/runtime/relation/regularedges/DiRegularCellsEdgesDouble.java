package fr.ocelet.runtime.relation.regularedges;

import fr.ocelet.runtime.raster.Grid;

public class DiRegularCellsEdgesDouble {
	
	
	private double[][] edges;
	
	public DiRegularCellsEdgesDouble(Grid grid1, Grid grid2){
		
		if(grid1.getWidth() * grid1.getHeight() > grid2.getWidth() * grid2.getHeight()){
			
			edges = new double[grid1.getWidth()][grid1.getHeight()];
		}else{
			edges = new double[grid2.getWidth()][grid2.getHeight()];

		}
		
	}
	
	public Double get(int x, int y){
		return edges[x][y];
	}
	
	public void set(int x, int y, Double value){
		edges[x][y] = value;
	}
	
}
