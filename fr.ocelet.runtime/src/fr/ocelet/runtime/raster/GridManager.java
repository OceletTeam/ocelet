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

import java.util.HashMap;

// Referenced classes of package fr.ocelet.runtime.raster:
//            Grid

public class GridManager
{

    public GridManager(){
           
       
    }

    public static GridManager getInstance()
    {
        if(gridManager == null)
            gridManager = new GridManager();
        return gridManager;
    }

    public void add(Grid grid)
    {
    	int curIndex = - 1;
    	for(Integer i : grids.keySet()) {
    		//if(grid.getName())
    		if(grid.getName().equals(grids.get(i).getName())) {
    			curIndex = i;
    		}
    	}
    	if(curIndex != -1) {
    		Grid precGrid = grids.get(curIndex);
    		precGrid.flush();
    		precGrid = null;
    		grids.put(curIndex, grid);
    		index = curIndex;
    	}else {
    		index = grids.keySet().size();
    		grids.put(grids.keySet().size(), grid);
    		
        	
    	}
    }

    public Grid get(int index)
    {
        return (Grid)grids.get(Integer.valueOf(index));
    }

    public int getCurrentIndex()
    {
    
        return index;
    }

    public int getIndex(Grid grid){
    	
    	int i = - 1;
    	for(Integer num : grids.keySet()){
    		if(grids.get(num).equals(grid)){
    			i = num;
    		}
    	}
    	return i;
    }
    private HashMap<Integer, Grid> grids = new HashMap<Integer, Grid>();
   private int index = 0;
    private static GridManager gridManager = null;

}
