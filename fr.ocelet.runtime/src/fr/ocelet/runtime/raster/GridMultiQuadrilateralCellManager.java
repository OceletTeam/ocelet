package fr.ocelet.runtime.raster;

import java.util.ArrayList;

import fr.ocelet.runtime.relation.CellValues;

public class GridMultiQuadrilateralCellManager extends GridCellManager {

	private int size;
	private ArrayList<CellValues[]> cvArray = new ArrayList<CellValues[]>();
	
	public GridMultiQuadrilateralCellManager(Grid grid){
			super(grid);
	}
	public void setSize(int size){
		this.size = size;
		init();
	}
	@Override
	public void reset() {
		 validate();
        y = 0;
        for(int i = 0; i < grid.getWidth(); i++){
        for(CellValues[] cv : cvArray){	
        	for(String name : properties){
        		cv[i].clear(name);
        	}
        	}
        }		
	}

	
	@Override
	public void validate() {
		CellValues[] firstLine = cvArray.get(0);
		for(int i = 0; i < firstLine.length; i++){
        
            CellValues cv = firstLine[i];
            
            for(String name : properties){
           
                
                if(aggregMap.containsKey(name)){
                
                    if(!cv.getValues(name).isEmpty())
                        grid.setCellValue(name, i, y, aggregMap.get(name).apply(cv.getValues(name), 0.0));
                } else if(!cv.getValues(name).isEmpty()){
                
                    grid.setCellValue(name, i, y, (Double)cv.getValues(name).get((int)(Math.random() * (double)cv.getValues(name).size())));
                }
            }

        }		
	}

	@Override
	public void init() {
	if(size > 1){
		for(int i = 0; i < size + 1; i ++){
			CellValues[] cv = new CellValues[grid.getWidth()];
			cvArray.add(cv);
		}
		for(int i = 0; i < grid.getWidth(); i ++){
			for(CellValues[] cv : cvArray){
				cv[i] = new CellValues();
				for(String name : properties){
					cv[i].set(name);

				}

			}
		}
	}
	}

	@Override
	public void increment() {
		validate();
        CellValues temp[] = cvArray.get(0);
        cvArray.remove(0);
        for(int i = 0; i < grid.getWidth(); i++){
        
        	for(String name : properties){
        		temp[i].clear(name);
        	}
           

        }
        cvArray.add(temp);
        y++;		
	}

	@Override
	public CellValues get(int x, int y) {
            return cvArray.get(y - this.y)[x];
	}
	@Override
public void add(int x, int y, String name, Double value){

        get(x, y).add(name, value);
       
    }
	@Override
	public CellValues[] getValues() {
		return cvArray.get(0);
	}
}