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

import java.util.ArrayList;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

import fr.ocelet.runtime.geom.ocltypes.Point;
import fr.ocelet.runtime.raster.RasterQuadrilateralCursor.NeighbourSide;
import fr.ocelet.runtime.relation.CellValues;

public class RasterMultiQuadrilateralCursor extends RasterCursor {
	
	private NeighbourSide ns;
	private int size;
	private int type = 0;

public RasterMultiQuadrilateralCursor(int numGrid) {
		super(numGrid);
		// TODO Auto-generated constructor stub
		direction = -1;
		x = -1;
		
			}

public void setSize(int size, int type){
	
	this.size = size;
	if(type == 0){
		init();
	}else{
		initCircular();
	}
}

	@Override
	public boolean hasNext() { 
		if(x == width - 2 && y == height - 1 && x2 == width - 1 && y2 == height - 1){
        
            x = -1;
            y = 0;
            x2 = 0;
            y2 = 0;
          
            gridManager.reset();
            return false;
        } else
        {
            return true;
        }
	}

	@Override
	public void next() {
		 ns.next();
		
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
	public void initCircular(){
		
		double radius = size * xRes;
		
		Point point = Point.xy(0.0, 0.0);
		Geometry buffer = point.buffer(radius);
		ns = new FirstNeighbourSide(1, 0);
		NeighbourSide f = ns;
		for(int i = 2; i <= size; i ++){
			Point p = Point.xy(i * xRes, 0.0);
			if(buffer.contains(p) || buffer.intersects(p) || buffer.covers(p)){
			NeighbourSide next = new NextNeighbourSide(i, 0);
			ns.next = next;
			ns = next;
			}
		}
		
		for(int j = 1; j <= size + 1; j ++){
			for(int i = -size; i <= size; i ++){
				Point p = Point.xy(i * xRes, j * xRes);
				if(buffer.contains(p) || buffer.intersects(p) || buffer.covers(p)){
				NeighbourSide next = new NextNeighbourSide(i, j);
				ns.next = next;
				ns = next;
				}
			}
			}
		ns.next = f;
		ns = f;
	}
	
	public void init(){
		
		ns = new FirstNeighbourSide(1, 0);
		NeighbourSide f = ns;
		for(int i = 2; i <= size; i ++){
			NeighbourSide next = new NextNeighbourSide(i, 0);
			ns.next = next;
			ns = next;
		}
		for(int j = 1; j <= size; j ++){
		for(int i = -size; i <= size; i ++){
			NeighbourSide next = new NextNeighbourSide(i, j);
			ns.next = next;
			ns = next;
		}
		}
		ns.next = f;
		ns = f;
	}
public class NextNeighbourSide extends NeighbourSide{
		
	
	
	public NextNeighbourSide(int x, int y){
		
		super(x, y);
		
	}
	public void next(){
		 if(inbounds(x + i, y + j)){
			 x2 = x + i;
             y2 = y + j;
             ns = next;
		 }else{
			ns = next;
			ns.next();
		 }
	}

}

public abstract class NeighbourSide{
	
	int i;
	int j;
	NeighbourSide next;
	public NeighbourSide(int i, int j){
		this.i = i;
		this.j = j;
	}
	
	public abstract void next();
		
	
}

public class FirstNeighbourSide extends NeighbourSide{
	
	
	public FirstNeighbourSide(int x, int y){
		super(x, y);
	}
	
	public void next(){
		 move();
		 if(inbounds(x + i, y + j)){
			 x2 = x + i;
             y2 = y + j;
             ns = next;
		 }else{
			ns = next;
			ns.next();
		 }
	}
		
	
}

	



	
	
}
