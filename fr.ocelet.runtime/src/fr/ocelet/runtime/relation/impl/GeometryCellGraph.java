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

package fr.ocelet.runtime.relation.impl;

import java.util.Iterator;

import com.vividsolutions.jts.geom.Geometry;

import fr.ocelet.runtime.raster.CellAggregOperator;
import fr.ocelet.runtime.relation.DiGraphInterface;
import fr.ocelet.runtime.relation.EdgeFilter;
import fr.ocelet.runtime.relation.EdgeFilteringIterator;
import fr.ocelet.runtime.relation.GeomCellEdge;
import fr.ocelet.runtime.relation.OcltEdge;
import fr.ocelet.runtime.relation.OcltRole;

// Referenced classes of package fr.ocelet.runtime.relation.impl:

// CompleteIteratorGeomCell

public abstract class GeometryCellGraph<E extends OcltEdge, R1 extends OcltRole, R2 extends OcltRole>
		implements DiGraphInterface<E, R1, R2> {

	protected CompleteIteratorGeomCell<E, R1, R2> completeIteratorCellGeom;
	protected Iterator<E> filteredIterator;
	protected GeomCellEdge geomCellEdge;

	public GeometryCellGraph() {

	}

	public void setCompleteIteratorGeomCell(E e) {
		this.geomCellEdge = (GeomCellEdge) e;
		completeIteratorCellGeom = new CompleteIteratorGeomCell<E, R1, R2>(e, this);

	}

	public E createEdge(OcltRole r1, OcltRole r2) {
		return null;
	}

	public void beginTransaction() {

		for (R2 r2 : completeIteratorCellGeom.getGeomEntities()) {

			r2.tbegin();
		}

	}

	public void endTransaction() {

		for (R2 r2 : completeIteratorCellGeom.getGeomEntities()) {
			r2.tcommit();
		}

	}

	public void abortTransaction() {
		for (R2 r2 : completeIteratorCellGeom.getGeomEntities()) {
			r2.tabort();
		}

	}

	public Iterator<R1> getCells() {
		return completeIteratorCellGeom.getCellIterator();
	}

	public void disconnect(OcltEdge ocltedge) {
	}

	public void disconnect(Iterable<E> iterable) {
	}

	public Iterator<E> iterator() {
		if (filteredIterator != null)
			return filteredIterator;
		// cleanGraph();
		return completeIteratorCellGeom;
	}

	public int size() {
		return 0;
	}

	public GeometryCellGraph<E, R1, R2> getComplete() {
		CompleteIteratorGeomCell<E, R1, R2> cit = new CompleteIteratorGeomCell<E, R1, R2>((E) geomCellEdge, this);
		if ((filteredIterator != null) && (filteredIterator instanceof EdgeFilteringIterator))
			((EdgeFilteringIterator) filteredIterator).setSourceIterator(cit);
		else
			filteredIterator = cit;
		return this;
	}

	public void addFilter(EdgeFilter<R2, R1> ef) {
		if ((filteredIterator == null) || !(filteredIterator instanceof EdgeFilteringIterator))
			filteredIterator = new EdgeFilteringIteratorImpl(iterator());
		((EdgeFilteringIterator) filteredIterator).addFilter(ef);
	}

	public String toString() {
		return (new StringBuilder("Interaction graph (")).append(getClass().getSimpleName()).append(") contains ")
				.append(size()).append(" edges.").toString();
	}

	public E connect(R1 r1, R2 r2) {
		completeIteratorCellGeom.connect(r1, r2);
		return null;
	}

	public void setCells() {

	}

	public void setMode(int mode) {

		completeIteratorCellGeom.setMode(mode);
	}

	public void cleanOperator() {
		completeIteratorCellGeom.clearAggregMap();
	}

	public void setCellOperator(CellAggregOperator operator) {
		completeIteratorCellGeom.addOperator(operator);
	}

	public E getEdge() {
		return completeIteratorCellGeom.getEdge();
	}

	public void disconnect(R1 r1, R2 r2) {

		completeIteratorCellGeom.disconnect(r1, r2);

	}

	public void connect(R2 r2, Geometry zone) {
		completeIteratorCellGeom.connect(r2, zone);
	}

	public void disconnect(R2 r2, Geometry zone) {
		completeIteratorCellGeom.disconnect(r2, zone);
	}

	public void morph(Double buffer) {
		completeIteratorCellGeom.morph(buffer);
	}

	public void disconnectAll() {
		completeIteratorCellGeom.disconnectAll();
	}
}
