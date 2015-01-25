package fr.ocelet.runtime.relation;

import fr.ocelet.runtime.ocltypes.List;

/**
 * Generic Aggregation Operator definition . Those aggregation operators
 * are used when several candidate values are simultaneously 
 * affected to an Entity's property. That situation happens when
 * an entity is connected to several others in an interaction graph and
 * the interaction function assigns a new value to one property of that
 * entity. New values will come from every arc of the graph that is connected
 * to the entity. An aggregation operator is used to avoid any affectation
 * conflict and perform the appropriate operation in case of multiple values.
 * @author Pascal Degenne, initial contribution
 *
 */
public interface AggregOperator<T,R extends List<T>>{
	
	/**
	 * Computes all the candidate values and produces one unique
	 * value of the same type to be used for property affectation.
	 * @param future Vector of candidate values.
	 * @param actualValue The actual value that is provided in case of needed, either in the computation
	 *  or just to be able to use it as a result to leave the actual value unchanged.
	 * @return One unique value
	 */
	public T compute(R future, T preval);
}
