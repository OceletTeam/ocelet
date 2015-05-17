package fr.ocelet.lang.jvmmodel;

import org.eclipse.xtext.util.Strings;
import org.eclipse.xtext.xbase.XExpression;
import org.eclipse.xtext.xbase.XStringLiteral;
import org.eclipse.xtext.xbase.compiler.XbaseCompiler;
import org.eclipse.xtext.xbase.compiler.output.ITreeAppendable;
import org.eclipse.xtext.xbase.typesystem.references.LightweightTypeReference;

@SuppressWarnings("restriction")
public class OceletCompiler extends XbaseCompiler {

	public void compileDatafacerParamExpression(XExpression obj,
			ITreeAppendable appendable) {
		super.internalToJavaExpression(obj, appendable);
	}

	/**
	 * Cound not understand how come the type found is null for an XStringLiteral
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
