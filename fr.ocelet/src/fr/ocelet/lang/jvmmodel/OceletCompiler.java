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

import org.eclipse.xtext.util.Strings;
import org.eclipse.xtext.xbase.XExpression;
import org.eclipse.xtext.xbase.XStringLiteral;
import org.eclipse.xtext.xbase.compiler.XbaseCompiler;
import org.eclipse.xtext.xbase.compiler.output.ITreeAppendable;
import org.eclipse.xtext.xbase.typesystem.references.LightweightTypeReference;

/**
 * Customized XbaseCompiler
 * 
 * @author Pascal Degenne - Initial contribution
 */
@SuppressWarnings("restriction")
public class OceletCompiler extends XbaseCompiler {

	public void compileDatafacerParamExpression(XExpression obj,
			ITreeAppendable appendable) {
		super.internalToJavaExpression(obj, appendable);
	}

	/**
	 * Could not understand how come the type found is null for an XStringLiteral
	 * but I had to write this turnaround version of the method to avoid falling
	 * in a NullPointerException when declaring datafacers.
	 */
	@Override
	protected void toJavaExpression(XStringLiteral literal,
			ITreeAppendable appendable, boolean useUnicodeEscapes) {
		LightweightTypeReference type = getLightweightType(literal);
		if (type == null) {
			String javaString = Strings.convertToJavaString(literal.getValue(),
					useUnicodeEscapes);
			appendable.append("\"").append(javaString).append("\"");
		} else
			super.toJavaExpression(literal, appendable, useUnicodeEscapes);
	}
}
