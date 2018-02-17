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
package fr.ocelet.lang;

import org.eclipse.xtext.xbase.compiler.XbaseCompiler;
import org.eclipse.xtext.xbase.scoping.batch.ImplicitlyImportedFeatures;

import com.google.inject.name.Names;

import fr.ocelet.lang.jvmmodel.OceletCompiler;
import fr.ocelet.lang.scoping.OceletImplicitlyImportedFeatures;

@SuppressWarnings("restriction")
public class OceletRuntimeModule extends fr.ocelet.lang.AbstractOceletRuntimeModule {

	public Class<? extends XbaseCompiler> bindXbaseCompiler() {
        return OceletCompiler.class;
    }


	public Class<? extends ImplicitlyImportedFeatures> bindImplicitlyImportedfeatures() {
		return OceletImplicitlyImportedFeatures.class;
	}
	
	@Override
	public void configureIScopeProviderDelegate(com.google.inject.Binder binder) {
		binder.bind(org.eclipse.xtext.scoping.IScopeProvider.class).annotatedWith(Names.named(org.eclipse.xtext.scoping.impl.AbstractDeclarativeScopeProvider.NAMED_DELEGATE)).to(fr.ocelet.lang.scoping.OceletImportedNamespaceScopeProvider.class);
	}
}
