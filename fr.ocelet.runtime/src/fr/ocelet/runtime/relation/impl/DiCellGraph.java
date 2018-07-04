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

package fr.ocelet.runtime.relation.impl;

import fr.ocelet.runtime.entity.AbstractEntity;
import fr.ocelet.runtime.geom.ocltypes.Cell;
import fr.ocelet.runtime.ocltypes.List;
import fr.ocelet.runtime.raster.CellAggregOperator;
import fr.ocelet.runtime.raster.Grid;
import fr.ocelet.runtime.relation.*;

import java.util.Iterator;

// Referenced classes of package fr.ocelet.runtime.relation.impl:
//            CompleteIteratorDiCell

public class DiCellGraph<E extends OcltEdge, R1 extends OcltRole, R2 extends OcltRole>
implements DiGraphInterface<E, R1, R2> {

    public DiCellGraph(){
    
    }
	protected Iterator<E> filteredIterator;
    public void setGrid(List<R1> r1List, List<R2> r2List){
    	
    	AbstractEntity r1 = (AbstractEntity)r1List.get(0);
    	AbstractEntity r2 = (AbstractEntity)r2List.get(0);
    	
    	Cell c1 = (Cell)r1.getSpatialType();
    	Cell c2 = (Cell)r2.getSpatialType();
        this.grid1 = c1.getGrid();
        this.grid2 = c2.getGrid();   
        }
	public void addFilter(EdgeFilter<R1, R2> ef) {
		if ((filteredIterator == null)
				|| !(filteredIterator instanceof EdgeFilteringIterator))
			filteredIterator = new EdgeFilteringIteratorImpl(iterator());
		((EdgeFilteringIterator) filteredIterator).addFilter(ef);
	}
	public void initInteraction() {
		completeIteratorDiCell.initInteraction();
	}
	public void endInteraction() {
		
	}
    public void setCompleteIteratorDiCell(E e){
    
        completeIteratorDiCell = new CompleteIteratorDiCell<E, R1, R2>(this, e);
    }

    public void beginTransaction(){
    }

    public void abortTransaction(){
    }
    
    public void endTransaction(){
    }

    public void disconnect(OcltEdge ocltedge){
    }

    public void disconnect(Iterable iterable){
    	
    }

    public int size(){
        return 0;
    }

    public Iterator<E> iterator()
    {
        return completeIteratorDiCell;
    }

    public E connect(OcltRole left, OcltRole right)
    {
        return null;
    }

    public E createEdge(OcltRole gro, OcltRole lro)
    {
        return null;
    }

    public DiGraphInterface<E, R1, R2> getComplete()
    {
        return null;
    }

    public void cleanOperator(){
    
        completeIteratorDiCell.clearAggregMap();
        grid1.clearGeomTempVal2();
        grid2.clearGeomTempVal2();
    }

    public void setCellOperator(CellAggregOperator operator)
    {
        completeIteratorDiCell.addOperator(operator.getName(), operator);
    }

    public OcltEdge getEdge()
    {
        return completeIteratorDiCell.getEdge();
    }

    public void updateGrid()
    {
        completeIteratorDiCell.updateGrid();
    }

    protected Grid grid1;
    protected Grid grid2;
    private CompleteIteratorDiCell<E, R1, R2> completeIteratorDiCell;
}
