// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   GeometryCellGraph.java

package fr.ocelet.runtime.relation.impl;

import fr.ocelet.runtime.geom.ocltypes.Cell;
import fr.ocelet.runtime.ocltypes.List;
import fr.ocelet.runtime.raster.CellAggregOperator;
import fr.ocelet.runtime.raster.Grid;
import fr.ocelet.runtime.relation.*;

import java.util.Iterator;

import com.vividsolutions.jts.geom.Geometry;

// Referenced classes of package fr.ocelet.runtime.relation.impl:
//            CompleteIteratorGeomCell

public abstract class GeometryCellGraph<E extends OcltEdge, R1 extends OcltRole, R2 extends OcltRole>
implements DiGraphInterface<E, R1, R2>{

	 protected CompleteIteratorGeomCell<E, R1, R2> completeIteratorCellGeom;
	
    public GeometryCellGraph(){
    
    }

    public void setCompleteIteratorGeomCell(E e)
    {
        completeIteratorCellGeom = new CompleteIteratorGeomCell<E, R1, R2>(e, this);
       
    }

    public E createEdge(OcltRole r1, OcltRole r2)
    {
        return null;
    }

    public void beginTransaction(){
    
    	for(R2 r2 : completeIteratorCellGeom.getGeomEntities()){
    		
    		r2.tbegin();
    	}
       

    }

    public void endTransaction(){
    
    	for(R2 r2 : completeIteratorCellGeom.getGeomEntities()){
    		r2.tcommit();
    	}       


    }

    public void abortTransaction()
    {
    	for(R2 r2 : completeIteratorCellGeom.getGeomEntities()){
    		r2.tabort();
    	}   

    }

    public Iterator<R1> getCells(){
    	return completeIteratorCellGeom.getCellIterator();
    }
    public void disconnect(OcltEdge ocltedge)
    {
    }

    public void disconnect(Iterable<E> iterable)
    {
    }

    public Iterator<E> iterator()
    {
        return completeIteratorCellGeom;
    }

    public int size()
    {
        return 0;
    }

    public GeometryCellGraph<E, R1, R2> getComplete()
    {
        return this;
    }

    public void addFilter(EdgeFilter edgefilter)
    {
    }

    public String toString()
    {
        return (new StringBuilder("Interaction graph (")).append(getClass().getSimpleName()).append(") contains ").append(size()).append(" edges.").toString();
    }

  

    public E connect(R1 r1, R2 r2)
    {
    	completeIteratorCellGeom.connect(r1, r2);
    	return null;
    }

    public void setCells(){
    
    }

    public void setMode(int mode){
    
        completeIteratorCellGeom.setMode(mode);
    }

    public void cleanOperator()
    {
        completeIteratorCellGeom.clearAggregMap();
    }

    public void setCellOperator(CellAggregOperator operator)
    {
        completeIteratorCellGeom.addOperator(operator);
    }

 
    public E getEdge(){
    	return completeIteratorCellGeom.getEdge();
    }
    public void disconnect(R1 r1, R2 r2){
    
    	completeIteratorCellGeom.disconnect(r1, r2);
    	
    }
    
    public void connect(R2 r2, Geometry zone){
    	completeIteratorCellGeom.connect(r2, zone);
    }
    
    public void disconnect(R2 r2, Geometry zone){
    	completeIteratorCellGeom.disconnect(r2, zone);
    }
    
    public void morph(Double buffer){
    	completeIteratorCellGeom.morph(buffer);
    }
   
    public void disconnectAll(){
    	completeIteratorCellGeom.disconnectAll();
    }
}
