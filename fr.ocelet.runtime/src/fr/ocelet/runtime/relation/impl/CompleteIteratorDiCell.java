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

import fr.ocelet.runtime.raster.CellAggregOperator;
import fr.ocelet.runtime.relation.DiCursorEdge;
import fr.ocelet.runtime.relation.OcltEdge;
import fr.ocelet.runtime.relation.OcltRole;

import java.util.Iterator;

// Referenced classes of package fr.ocelet.runtime.relation.impl:
//            DiCellGraph

public class CompleteIteratorDiCell<E extends OcltEdge, R1 extends OcltRole, R2 extends OcltRole> implements Iterator<E>{

    

    private DiCellGraph<E, R1, R2> diCellGraph;
    private E diCursorEdge;

    public CompleteIteratorDiCell(DiCellGraph<E, R1, R2> diCellGraph, E diCursorEdge)
    {
        this.diCellGraph = diCellGraph;
        this.diCursorEdge = diCursorEdge;
    }

    public boolean hasNext(){
    
        return ((DiCursorEdge)diCursorEdge).hasNext();
    }

    public E next(){
    
        ((DiCursorEdge)diCursorEdge).next();
        return diCursorEdge;
    }

    public void remove()
    {
    }

    public void clearAggregMap()
    {
        ((DiCursorEdge)diCursorEdge).clearAggregMap();
    }

    public void addOperator(String name, CellAggregOperator operator)
    {
        ((DiCursorEdge)diCursorEdge).addOperator(name, operator);
    }

    public OcltEdge getEdge()
    {
        return diCursorEdge;
    }

    public void updateGrid()    {
        ((DiCursorEdge)diCursorEdge).updateGrid();
    }

   
   

}
