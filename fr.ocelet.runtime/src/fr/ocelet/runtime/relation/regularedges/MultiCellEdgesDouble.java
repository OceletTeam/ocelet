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
public class MultiCellEdgesDouble {

	private Neighbor[][] edges;
	private int distance;

	public MultiCellEdgesDouble(Grid grid, int distance) {
		this.distance = distance;
		edges = new Neighbor[grid.getWidth() * distance][grid.getHeight() * distance];

		for (int i = 0; i < grid.getWidth(); i++) {
			for (int j = 0; j < grid.getHeight(); j++) {
				Neighbor n = new Neighbor(distance);
				edges[i][j] = n;
			}
		}
	}

	public Double get(int x1, int y1, int x2, int y2) {

		return edges[x1][y1].get(x2 - x1 + distance, y2 - y1 + distance);
	}

	public void set(int x1, int y1, int x2, int y2, Double value) {

		edges[x1][y2].set(x2 - x1 + distance, y2 - y1 + distance, value);
	}

	public class Neighbor {

		private double[][] values;

		public Neighbor(int distance) {
			values = new double[distance * 2 + 1][distance * 2 + 1];
		}

		public double get(int x, int y) {
			return values[x][y];
		}

		public void set(int x, int y, Double value) {
			values[x][y] = value;
		}

	}
}
