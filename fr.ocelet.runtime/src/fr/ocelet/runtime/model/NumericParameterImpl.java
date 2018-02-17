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
package fr.ocelet.runtime.model;

/**
 * In use for the metadata definitions of in a model
 * @author Pascal Degenne - Initial contribution
 *
 * @param <T>
 */
public class NumericParameterImpl<T extends Number> extends ParameterImpl<T>
		implements NumericParameter<T> {

	protected T minv;
	protected T maxv;

	public NumericParameterImpl() {
		super();
	}

	public NumericParameterImpl(String pname, String pdesc, boolean opt,
			T defval, T minvalue, T maxvalue, String unit) {
		super(pname, pdesc, opt, defval, unit);
		this.minv = minvalue;
		this.maxv = maxvalue;
	}

	@Override
	public T getMinValue() {
		return minv;
	}

	@Override
	public T getMaxValue() {
		return maxv;
	}

}
