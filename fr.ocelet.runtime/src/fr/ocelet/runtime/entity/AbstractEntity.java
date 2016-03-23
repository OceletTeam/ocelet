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

package fr.ocelet.runtime.entity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import fr.ocelet.runtime.geom.ocltypes.SpatialType;
import fr.ocelet.runtime.ocltypes.List;
import fr.ocelet.runtime.relation.AggregOperator;
import fr.ocelet.runtime.relation.OcltRole;

/**
 * Contains the common behavior for all kinds of entities. Maintains the list of
 * properties and their transaction mechanism.
 * 
 * @author Pascal Degenne, Initial contribution
 */
public abstract class AbstractEntity implements Entity {

	protected static Long nextgei = 0L;

	protected boolean transaction;
	protected HashMap<String, Hproperty<?>> propmap;
	protected Long graph_Element_Identifier;
	protected SpatialType spatialType;
	protected String spatialTypeName;
	protected boolean spatialSet = false;
	
	protected AbstractEntity() {
		propmap = new HashMap<String, Hproperty<?>>();
		graph_Element_Identifier = nextgei++;
	}

	/**
	 * Equal operator overridden to provide efficient equality tests
	 * @param e Other entity
	 * @return true if they have both the same identifier
	 */
	public boolean operator_equals(AbstractEntity e){
		return (e.graph_Element_Identifier == this.graph_Element_Identifier);
	}

	/**
	 * Equal operator overridden to provide efficient equality tests
	 * @param e Other entity
	 * @return true if they have both the same identifier
	 */
	public boolean operator_notEquals(AbstractEntity e){
		return (e.graph_Element_Identifier != this.graph_Element_Identifier);
	}
	
	/**
	 * @see fr.ocelet.runtime.relation.OcltGraphElement#getGraph_Element_Identifier()
	 */
	@Override
	public Object getGraph_Element_Identifier() {
		return graph_Element_Identifier;
	}

	/**
	 * Defines and adds a new property to this entity.
	 * 
	 * @param pname
	 *            Name of the property to add to this entity
	 * @param hp
	 *            The property itself
	 */
	protected void defProperty(String pname, Hproperty<?> hp) {
		propmap.put(pname, hp);
	}

	/**
	 * The behavior of this property setter is different if the transaction is
	 * true or false. If transaction is false, the property is directly set to
	 * val. If transaction is true, val is added to the property's list of new
	 * value candidates and that list will be later processed, at the end of the
	 * transaction. Note that when the transaction end, an AggregOperator will
	 * be used to compute the new values candidate list. If no AggregOperator
	 * has been given to the property, the Any is the default affectation
	 * operator that will be used.
	 * 
	 * @param pname
	 *            Name of the property
	 * @param val
	 *            Candidate new value for the property.
	 */
	public <T> void setProperty(String pname, T val) {
		Hproperty<T> hp = (Hproperty<T>) propmap.get(pname);
		if (transaction){
			hp.setFuture(val);
		}else{
			hp.set(val);
			
			if(!spatialSet){
				if(hp.get() instanceof SpatialType){
					spatialType = (SpatialType)hp.get();
					spatialSet = true;
					spatialTypeName = pname;
					
				}
			}else{
				if(spatialTypeName == pname)
				spatialType = (SpatialType)hp.get();
			}
		}
	}

	/**
	 * Changes the aggregation operator associated to a given property
	 * 
	 * @param pname
	 *            Name of the property
	 * @param ao
	 *            The affectation operator Class to be applied when the
	 *            transaction will end.
	 */
	@Override
	public <T> void setAgregOp(String pname, AggregOperator<T, List<T>> ao, boolean usepreval) {
		Hproperty<T> hp = (Hproperty<T>) propmap.get(pname);
		hp.setAffectOperator(ao,usepreval);
	}

	/**
	 * Property getter. The returned value is the last valid state of the
	 * property. It means that if new values have been assigned to that property
	 * but the transaction has not been yet committed, the value returned is the
	 * state before assigning those new values.
	 * 
	 * @param pname
	 *            Name of the property to get.
	 * @return The value corresponding to the last valid state of that property.
	 */
	public <T> T getProperty(String pname) {
		Hproperty<T> hp = (Hproperty<T>) propmap.get(pname);
		return hp.get();
	}

	/**
	 * Property getter. The returned value is the historic state of the property
	 * pointed by the null or negative index given in argument. A call to
	 * getProperty(name,0) is equivalent to getProperty(name).
	 * 
	 * @param pname
	 *            Name of the property to get.
	 * @param hindex
	 *            Null or negative index to indicate at what point of the
	 *            history the value must be read
	 * @return The value corresponding to the historic state pointed by hindex.
	 */
	public <T> T getProperty(String pname, int hindex) {
		Hproperty<T> hp = (Hproperty<T>) propmap.get(pname);
		return hp.get(hindex);
	}

	/**
	 * Begins a transaction for this entity. Any call to setProperty will just
	 * add the new values given to a new value candidate list and the new state
	 * of the property will not be update until we get a call to tcommit().
	 */
	public void tbegin() {
		transaction = true;
	}

	/**
	 * Ends a transaction for this entity. All it's properties are assigned
	 * their new values of any has been assigned during the transaction. It
	 * means that the entity officially switches from state n to state n+1.
	 */
	public void tcommit() {
		if (transaction)
			for (Iterator<Entry<String, Hproperty<?>>> pit = propmap.entrySet()
					.iterator(); pit.hasNext();) {
				((Hproperty<?>) pit.next().getValue()).commitFuture();
			}
		transaction = false;
	}

	/**
	 * Ends a transaction aborting any property change. All values assigned to
	 * any property since the last tbegin() are lost.
	 */
	public void tabort() {
		if (transaction)
			for (Iterator<Entry<String, Hproperty<?>>> pit = propmap.entrySet()
					.iterator(); pit.hasNext();) {
				((Hproperty<?>) pit.next().getValue()).flushFuture();
			}
		transaction = false;
	}

	/**
	 * Overrides the Object.toString()
	 * 
	 * @return A string in the form prop1_name=prop1_value;
	 *         prop2_name=prop2_value; etc.
	 */
	public String toString() {
		String ts = " ";
		for (Iterator<Entry<String, Hproperty<?>>> pit = propmap.entrySet()
				.iterator(); pit.hasNext();) {
			Entry<String, Hproperty<?>> e = pit.next();
			ts += e.getKey() + "=" + ((Hproperty<?>) e.getValue()) + "; ";
		}
		return ts;
	}
	

	public SpatialType getSpatialType(){
		return spatialType;
	}
	
	public void setSpatialType(SpatialType st){
		this.spatialType = st;
	}
	
	public <T> String getPropName(T val){
		
		for(String s : propmap.keySet()){
			if(propmap.get(s).get().equals(val)){
				return s;
			}
		}
		return null;
		
	}

}
