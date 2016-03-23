// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   CompleteIteratorDiCell.java

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
