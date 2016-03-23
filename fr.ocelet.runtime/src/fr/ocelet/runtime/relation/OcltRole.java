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

package fr.ocelet.runtime.relation;

import fr.ocelet.runtime.geom.ocltypes.SpatialType;
import fr.ocelet.runtime.ocltypes.List;

/**
 * Generic representation of a Role in Ocelet's relations.
 * 
 * @author Pascal Degenne, Initial contribution
 */
public interface OcltRole extends OcltGraphElement {

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
	public <T> T getProperty(String pname);

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
	public <T> void setProperty(String pname, T val);

	/**
	 * Changes the aggregation operator associated to a given property
	 * 
	 * @param pname
	 *            Name of the property
	 * @param ao
	 *            The affectation operator Class to be applied when the
	 *            transaction will end.
	 */
	public <T> void setAgregOp(String pname, AggregOperator<T,List<T>> ao, boolean usePreval);

	/**
	 * Begins a transaction for this entity. Any call to setProperty will just
	 * add the new values given to a new value candidate list and the new state
	 * of the property will not be update until we get a call to tcommit().
	 */
	public void tbegin();

	/**
	 * Ends a transaction for this entity. All it's properties are assigned
	 * their new values of any has been assigned during the transaction. It
	 * means that the entity officially switches from state n to state n+1.
	 */
	public void tcommit();

	/**
	 * Ends a transaction aborting any property change. All values assigned to
	 * any property since the last tbegin() are lost.
	 */
	public void tabort();
	
	public SpatialType getSpatialType();
	
	public void setSpatialType(SpatialType st);
	
	public <T> String getPropName(T val);
}
