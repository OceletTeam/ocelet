package fr.ocelet.runtime.raster;

import java.util.Iterator;

import fr.ocelet.runtime.relation.OcltRole;

public class CellIterator<Ro extends OcltRole> implements Iterator<Ro>{

	private Grid grid;
	private int x = 0;
	private int y = 0;
	
	public CellIterator(Grid grid){
		this.grid = grid;
	}
	@Override
	public boolean hasNext() {
		
		if(x == grid.getWidth() && y == grid.getHeight()){
			return false;
		}
		return true;
		// TODO Auto-generated method stub
		
	}

	@Override
	public Ro next() {
		
		if(x == grid.getWidth()){
			x = 0;
			y++;
		}else{
			x++;
		}
		return null;
	
	}

	@Override
	public void remove() {
		// TODO Auto-generated method stub
		
	}

	
	
}
