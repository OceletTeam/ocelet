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

import fr.ocelet.runtime.ocltypes.List;
import fr.ocelet.runtime.raster.CellAggregOperator;
import fr.ocelet.runtime.raster.Grid;
import fr.ocelet.runtime.relation.GeomCellEdge;
import fr.ocelet.runtime.relation.OcltEdge;
import fr.ocelet.runtime.relation.OcltRole;

import java.util.Iterator;

import com.vividsolutions.jts.geom.Geometry;

// Referenced classes of package fr.ocelet.runtime.relation.impl:
//            GeometryCellGraph


public class CompleteIteratorGeomCell<E extends OcltEdge, R1 extends OcltRole, R2 extends OcltRole> implements Iterator<E>{
   
	
	  protected GeometryCellGraph<E, R1, R2> bigr;
	    protected E geomCellEdge;

    public CompleteIteratorGeomCell(E geomCellEdge, GeometryCellGraph<E, R1, R2> bigr)
    {
        this.bigr = bigr;
        this.geomCellEdge = geomCellEdge;
    }

    public boolean hasNext()
    {
        return ((GeomCellEdge)geomCellEdge).hasNext();
    }

    public E next(){
    
        ((GeomCellEdge)geomCellEdge).next();
        return geomCellEdge;
    }

    public List<R2> getGeomEntities()
    {
        return ((GeomCellEdge)geomCellEdge).getGeomEntities();
    }

    public void remove()
    {
    }

    public void setMode(int mode)
    {
        ((GeomCellEdge)geomCellEdge).setMode(mode);
    }

    public void clearAggregMap()
    {
        ((GeomCellEdge)geomCellEdge).clearAggregMap();
    }

    public void addOperator(CellAggregOperator operator)
    {
        ((GeomCellEdge)geomCellEdge).addOperator(operator.getName(), operator);
    }

   public Grid getGrid(){
	  return ((GeomCellEdge)geomCellEdge).getGrid();
  }

   public Iterator<R1> getCellIterator(){
		  return ((GeomCellEdge)geomCellEdge).getCells();
	   }
  
   public E getEdge(){
	   return geomCellEdge;
   }
   
   public void connect(R1 r1, R2 r2){
	  ((GeomCellEdge)geomCellEdge).connect(r1, r2);
   }
   public void disconnect(R1 r1, R2 r2){
	   ((GeomCellEdge)geomCellEdge).disconnect(r1, r2);
   }
   
   public void connect(R2 r2, Geometry zone){
	  // ((GeomCellEdge)geomCellEdge).connect(r2, zone);
   }
   
   public void disconnect(R2 r2, Geometry zone){
	 //  ((GeomCellEdge)geomCellEdge).disconnect(r2, zone);
   }
   public void morph(Double buffer){
	   ((GeomCellEdge)geomCellEdge).morph(buffer);
   }
   
   public void disconnectAll(){
	  ((GeomCellEdge)geomCellEdge).disconnectAll();
   }
}
