/*
*  Ocelet spatial modelling language.   www.ocelet.org
*  Copyright Cirad 2010-2016
*
*  This software is a domain specific programming language dedicated to writing
*  spatially explicit models and performing spatial dynamics simulations.
*
*  This software is governed by the CeCILL license under French law and
*  abiding by the rules of distribution of free software.  You can  use,
*  modify and/ or redistribute the software under the terms of the CeCILL
*  license as circulated by CEA, CNRS and INRIA at the following URL
*  "http://www.cecill.info".
*  As a counterpart to the access to the source code and  rights to copy,
*  modify and redistribute granted by the license, users are provided only
*  with a limited warranty  and the software's author,  the holder of the
*  economic rights,  and the successive licensors  have only limited
*  liability.
*  The fact that you are presently reading this means that you have had
*  knowledge of the CeCILL license and that you accept its terms.
*/
package fr.ocelet.runtime.raster;

public class RasterTriangularCursor extends RasterCursor{

	private NeighbourSide ns;
	private NeighbourSideRightToBot nsrtb;
	private NeighbourSideRightToRight nsrtr; 
	private NeighbourSideBot nsb;

	public RasterTriangularCursor(Grid grid) {
		super(grid);
		// TODO Auto-generated constructor stub
		direction = -1;
		x = -1;
		nsrtr = new NeighbourSideRightToRight();
		nsrtb = new NeighbourSideRightToBot();
		nsb = new NeighbourSideBot();
		ns = nsrtr;

		// TODO Auto-generated constructor stub
	}



	public void setMode(String mode){

		if(mode.equals("Four")){
			
		}else{
			
		}
	}
	@Override
	public void reset(){
		x = -1;
		y = 0;
		x2 = 0;
		y2 = 0;
		gridManager.reset();
		ns = nsrtr;
	}
	@Override
	public boolean hasNext() {

		if(x == width - 2 && y == height - 1 && x2 == width - 1 && y2 == height - 1)
		{
			x = -1;
			y = 0;
			x2 = 0;
			y2 = 0;
			direction = -1;        

			gridManager.reset();
			ns = nsrtr;

			return false;
		} else {

			return true;
		}
	}

	@Override
	public void next() {		
		// while(!setEnd2()) ;
		if(x == 0){
			if(y % 2 == 0){
				ns = nsrtb;
			}else{
				ns = nsrtr;
			}
		}

		ns.next();
		//cursor();

	}


	@Override
	public void move() {
		if(x == width - 1){
			x = 0;
			y++;

			gridManager.increment();
		} else {
			x++;
		}

	}

	public boolean inbounds(int x, int y){

		return x >= 0 && y >= 0 && x < width && y < height;
	}



	public abstract class NeighbourSide{
		public abstract void next();


	}


	public class NeighbourSideRightToBot extends NeighbourSide{
		public void next(){
			move();
			if(inbounds(x + 1, y)){
				x2 = x + 1;
				y2 = y;
				ns = nsb;

			}else{
				ns = nsb;
				ns.next();
			}
		}
	}


	public class NeighbourSideRightToRight extends NeighbourSide{
		public void next(){
			move();
			if(inbounds(x + 1, y)){
				x2 = x + 1;
				y2 = y;
				ns = nsrtb;

			}else{
				ns = nsrtb;
				ns.next();
			}
		}
	}


	public class NeighbourSideBot extends NeighbourSide{
		public void next(){

			if(inbounds(x, y + 1)){
				x2 = x;
				y2 = y + 1;
				ns = nsrtr;

			}else{
				ns = nsrtr;
				ns.next();
			}
		}
	}

}
