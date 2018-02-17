/*
*  Ocelet spatial modelling language.   www.ocelet.org
*  Copyright Cirad 2010-2018
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
package fr.ocelet.runtime.relation.regularedges;

import fr.ocelet.runtime.raster.Grid;

/**
 * @author Mathieu Castets - Initial contribution
 */
public class SimpleCellEdgesManager {
	
	
	private SimpleCellEdgesDouble[] edgeDouble;
	private SimpleCellEdgesInteger[] edgeInteger;
	private SimpleCellEdgesBoolean[] edgeBoolean;

	public void addDoubleEdge(int size, Grid grid, int distance){
		
		edgeDouble = new SimpleCellEdgesDouble[size];
		for(int i= 0; i < size ; i++){
			SimpleCellEdgesDouble mced = new SimpleCellEdgesDouble(grid);
			edgeDouble[i] = mced;
		}
	}
	
	public void addIntegerEdge(int size, Grid grid, int distance){
		
		edgeInteger = new SimpleCellEdgesInteger[size];
		for(int i= 0; i < size ; i++){
			SimpleCellEdgesInteger mcei = new SimpleCellEdgesInteger(grid);
			edgeInteger[i] = mcei;
		}
	}
	public void addBooleanEdge(int size, Grid grid, int distance){
		edgeBoolean = new SimpleCellEdgesBoolean[size];
		for(int i= 0; i < size ; i++){
			
			SimpleCellEdgesBoolean mceb = new SimpleCellEdgesBoolean(grid);
			edgeBoolean[i] = mceb;
		}
	}
	
	public Double getDoubleValue(int x1, int y1, int x2, int y2, int property){
		return edgeDouble[property].get(x2, y2);
	}
	
	public Integer getIntegerValue(int x1, int y1, int x2, int y2, int property){
		return edgeInteger[property].get(x2, y2);
	}
	public Boolean getBooleanValue(int x1, int y1, int x2, int y2, int property){
		return edgeBoolean[property].get(x2, y2);
	}
	
	public void setDoubleValue(int x1, int y1, int x2, int y2, int property, Double value){
		edgeDouble[property].set(x2, y2, value);
	}
	public void setIntegerValue(int x1, int y1, int x2, int y2, int property, Integer value){
		edgeInteger[property].set(x2, y2, value);
	}
	public void setBooleanValue(int x1, int y1, int x2, int y2, int property, Boolean value){
		edgeBoolean[property].set(x1, y1, value);
	}
	
	
}
