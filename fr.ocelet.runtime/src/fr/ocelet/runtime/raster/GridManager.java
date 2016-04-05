package fr.ocelet.runtime.raster;

import java.util.HashMap;

// Referenced classes of package fr.ocelet.runtime.raster:
//            Grid

public class GridManager
{

    public GridManager(){
           
        index = 0;
    }

    public static GridManager getInstance()
    {
        if(gridManager == null)
            gridManager = new GridManager();
        return gridManager;
    }

    public void add(Grid grid)
    {
        grids.put(Integer.valueOf(index), grid);
        index++;
    }

    public Grid get(int index)
    {
        return (Grid)grids.get(Integer.valueOf(index));
    }

    public int getCurrentIndex()
    {
        return index - 1;
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
    private int index;
    private static GridManager gridManager = null;

}
