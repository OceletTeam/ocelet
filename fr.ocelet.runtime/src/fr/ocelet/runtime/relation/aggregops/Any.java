package fr.ocelet.runtime.relation.aggregops;

import fr.ocelet.runtime.ocltypes.List;
import fr.ocelet.runtime.relation.AggregOperator;

/**
 * Returns one randomly chosen element from the given list
 * @see AggregOperator
 * @author Pascal Degenne, initial contribution
 */
public class Any<T> implements AggregOperator<T,List<T>>{

	/**
	 * Computes all the candidate values and produces one unique
	 * value of the same type to be used for property affectation.
	 * @param future Vector of candidate values.
	 * @return One randomly chosen value from the argument future Vector.
	 */



	public T compute(List<T> future, T preval) {

		T choice=null;
		if (future.size() > 1) {
			int x= Math.round((float)(Math.floor(Math.random()*future.size())));
			choice = future.get(x);
		}
		else choice = future.get(0);
		return choice;
	}
}
