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

import fr.ocelet.runtime.ocltypes.List;
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
           
                List<Double> values = cv.getValues(name);
                
                if(aggregMap.containsKey(name)){
                
                    if(!values.isEmpty()){
                    	
                    	Double d;
                    	CellAggregOperator cao = aggregMap.get(name);
                    	if(cao.preval() == false){
                    		d = cao.apply(values, null);
                    	}else{
                    		values.add(grid.getDoubleValue(name, i, y));
                    		d = cao.apply(values, grid.getDoubleValue(name, i, y));
                    	}
                    	
                        grid.setCellValue(name, i, y, d);
                    	
                    }
                } else if(!cv.getValues(name).isEmpty()){
                
                    grid.setCellValue(name, i, y, cv.getValues(name).get((int)(Math.random() * (double)cv.getValues(name).size())));
                }
            }

        }		
	}

	@Override
	public void init() {
	if(size > 0){
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