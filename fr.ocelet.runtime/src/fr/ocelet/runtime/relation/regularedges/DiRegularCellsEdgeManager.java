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
*/package fr.ocelet.runtime.relation.regularedges;

import fr.ocelet.runtime.raster.Grid;

/**
 * @author Mathieu Castets - Initial contribution
 */
public class DiRegularCellsEdgeManager {

	private DiRegularCellsEdgesBoolean[] booleanValues;
	private DiRegularCellsEdgesDouble[] doubleValues;
	private DiRegularCellsEdgesInteger[] integerValues;

	public void setBooleanPropertySize(int size, Grid grid1, Grid grid2) {
		if (size > 0) {
			booleanValues = new DiRegularCellsEdgesBoolean[size];
			for (int i = 0; i < size; i++) {
				DiRegularCellsEdgesBoolean drce = new DiRegularCellsEdgesBoolean(grid1, grid2);
				booleanValues[i] = drce;
			}
		}
	}

	public void setIntegerPropertySize(int size, Grid grid1, Grid grid2) {
		if (size > 0) {
			integerValues = new DiRegularCellsEdgesInteger[size];
			for (int i = 0; i < size; i++) {
				DiRegularCellsEdgesInteger drce = new DiRegularCellsEdgesInteger(grid1, grid2);
				integerValues[i] = drce;
			}
		}
	}

	public void setDoublePropertySize(int size, Grid grid1, Grid grid2) {
		if (size > 0) {
			doubleValues = new DiRegularCellsEdgesDouble[size];
			for (int i = 0; i < size; i++) {
				DiRegularCellsEdgesDouble drce = new DiRegularCellsEdgesDouble(grid1, grid2);
				doubleValues[i] = drce;
			}
		}
	}

	public void setDoubleValue(int x, int y, int property, Double value) {
		doubleValues[property].set(x, y, value);
	}

	public void setIntegerValue(int x, int y, int property, Integer value) {
		integerValues[property].set(x, y, value);
	}

	public void setBooleanValue(int x, int y, int property, Boolean value) {
		booleanValues[property].set(x, y, value);
	}

	public Double getDoubleValue(int x, int y, int property) {
		return doubleValues[property].get(x, y);
	}

	public Boolean getBooleanValue(int x, int y, int property) {
		return booleanValues[property].get(x, y);
	}

	public Integer getIntegerValue(int x, int y, int property) {
		return integerValues[property].get(x, y);
	}

}
