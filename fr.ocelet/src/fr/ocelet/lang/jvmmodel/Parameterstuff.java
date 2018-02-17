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
package fr.ocelet.lang.jvmmodel;

import org.eclipse.xtext.common.types.JvmTypeReference;

/**
 * Metadata's Parameters content 
 * 
 * @author Pascal Degenne - Initial contribution
 */
class Parameterstuff {

	String name;
	String description;
	JvmTypeReference type;
	Object dvalue;
	Object minvalue;
	Object maxvalue;
	String unit;
	Boolean optionnal;

	public Parameterstuff(String name, JvmTypeReference type) {
		this.name = name;
		this.type = type;
		this.description = "";
		this.optionnal = true;
	}

	public void setDefvalue(Object dv) {
		this.dvalue = dv;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public void setMin(Object minv) {
		this.minvalue = minv;
	}

	public void setMax(Object maxv) {
		this.maxvalue = maxv;
	}

	public String getName() {
		return name;
	}

	public JvmTypeReference getType() {
		return type;
	}

	public Object getDvalue() {
		if (dvalue == null) {
			if (isIntType())
				dvalue = 0;
			else if (isFloatType()) {
				if (type.getSimpleName().compareTo("Float") == 0)
					dvalue = 0.0f;
				else
					dvalue = 0.0;
			} else if (isStringType())
				dvalue = "";
			else if (type.getSimpleName().compareTo("Boolean") == 0)
				dvalue = false;
		}
		return dvalue;
	}

	public String getDvalueString() {
		if (type.getSimpleName().compareTo("Float") == 0) return getDvalue()+"f";
		else if (isStringType()) return "\""+getDvalue()+"\"";
		else return getDvalue()+"";
	}
	
	public Object getMinvalue() {
		return minvalue;
	}

	public Object getMaxvalue() {
		return maxvalue;
	}

	public String getUnit() {
		return unit;
	}

	public boolean isNumericType() {
		return (isIntType() || isFloatType());
	}

	public boolean isIntType() {
		return ((type.getSimpleName().compareTo("Integer") == 0)
				|| (type.getSimpleName().compareTo("Long") == 0)
				|| (type.getSimpleName().compareTo("Short") == 0) || (type
				.getSimpleName().compareTo("Byte") == 0));
	}

	public boolean isFloatType() {
		return ((type.getSimpleName().compareTo("Float") == 0) || (type
				.getSimpleName().compareTo("Double") == 0));
	}

	public boolean isStringType() {
		return ((type.getSimpleName().compareTo("String") == 0) || (type
				.getSimpleName().compareTo("Char") == 0));
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getOptionnal() {
		return optionnal;
	}

	public void setOptionnal(Boolean optionnal) {
		this.optionnal = optionnal;
	}
}