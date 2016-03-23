// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DiCellGraph.java

package fr.ocelet.runtime.relation.impl;

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

    public void setGrid(Grid grid1, Grid grid2){
    
        this.grid1 = grid1;
        this.grid2 = grid2;
    }

    public void setCompleteIteratorDiCell(E e){
    
        completeIteratorDiCell = new CompleteIteratorDiCell<E, R1, R2>(this, e);
    }

    public void beginTransaction()
    {
    }

    public void abortTransaction()
    {
    }

    public void endTransaction()
    {
    }

    public void disconnect(OcltEdge ocltedge)
    {
    }

    public void disconnect(Iterable iterable)
    {
    }

    public int size()
    {
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
        grid1.clearGeomTempVal();
        grid2.clearGeomTempVal();
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

    private Grid grid1;
    private Grid grid2;
    private CompleteIteratorDiCell<E, R1, R2> completeIteratorDiCell;
}
