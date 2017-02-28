package fr.ocelet.runtime.relation.regularedges;

import fr.ocelet.runtime.raster.Grid;

public class MultiCellEdgesBoolean {
	
	private Neighbor[][] edges;
	private int distance;
	
	public MultiCellEdgesBoolean(Grid grid, int distance){
		this.distance = distance;
		edges = new Neighbor[grid.getWidth() * distance][grid.getHeight() * distance];
		
		for(int i = 0; i < grid.getWidth(); i ++){
			for(int j = 0; j < grid.getHeight(); j ++){
				Neighbor n = new Neighbor(distance);
				edges[i][j] = n;
			}
		}
	}	
	
	
	public Boolean get(int x1, int y1, int x2, int y2){
			
		return edges[x1][y1].get(x2 - x1 + distance, y2 - y1 + distance);
	}
	
	
	public void set(int x1, int y1, int x2, int y2, Boolean value){
		
		edges[x1][y2].set(x2 - x1 +distance , y2 - y1 + distance, value); 
	}
	
	public class Neighbor{
		
		private boolean[][] values;
		
		public Neighbor(int distance){
			values = new boolean[distance * 2 + 1][distance * 2 + 1];
		}
		
		public boolean get(int x, int y){
			return values[x][y];
		}
		
		public void set(int x, int y, boolean value){
			values[x][y] = value;
		}
		
		
	}
}
