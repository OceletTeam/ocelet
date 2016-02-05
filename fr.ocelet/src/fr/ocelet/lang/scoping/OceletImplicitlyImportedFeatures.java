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

package fr.ocelet.lang.scoping;

import java.util.List;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.xbase.scoping.batch.ImplicitlyImportedFeatures;

/**
 * Adds direct access to a series of java static function from the Ocelet language.
 * 
 * @author Pascal Degenne - Initial contribution
 *
 */
@SuppressWarnings("restriction")
public class OceletImplicitlyImportedFeatures extends
		ImplicitlyImportedFeatures {

	/**
	 * @return all JvmType containing static methods which are implicitly
	 *         imported
	 */
	public List<JvmType> getStaticImportClasses(Resource context) {
		List<Class<?>> classes = getStaticImportClasses();

		// The index 2 is to make sure the list remains sorted
		// based on its content since 2.8 version of XBase:
        //		ArrayLiterals.class,
        //		CollectionLiterals.class,
        //		InputOutput.class
		classes.add(2,fr.ocelet.runtime.geom.GeomBuilders.class);

		classes.add(java.lang.Math.class);
        classes.add(fr.ocelet.runtime.Miscutils.class);
        classes.add(fr.ocelet.runtime.TextFileWriter.class);
        classes.add(fr.ocelet.datafacer.KmlUtils.class);
		return getTypes(classes, context);
	}
	
}
