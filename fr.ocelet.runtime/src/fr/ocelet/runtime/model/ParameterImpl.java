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
 * Parameter definition to be used by the metadata declared
 * in an Ocelet model
 * @author Pascal Degenne - Initial contribution
 *
 * @param <T>
 */
public class ParameterImpl<T> implements Parameter<T> {

	protected String pname;
	protected String pdesc;
	protected String punit;
	protected boolean optionnal;
	protected T defval;

	public ParameterImpl() {
		this.pname = "?";
		this.pdesc = "";
		this.optionnal = true;
	}

	public ParameterImpl(String pname, String pdesc, boolean opt, T defval, String unit) {
		this.pname = pname;
		this.pdesc = pdesc;
		this.optionnal = opt;
		this.defval = defval;
		this.punit =unit;
	}

	@Override
	public String getName() {
		return pname;
	}

	@Override
	public String getDescription() {
		return pdesc;
	}

	@Override
	public boolean isOptionnal() {
		return optionnal;
	}

	@Override
	public T getDefaultValue() {
		return defval;
	}

	@Override
	public String getUnit() {
		return punit;
	}

}
