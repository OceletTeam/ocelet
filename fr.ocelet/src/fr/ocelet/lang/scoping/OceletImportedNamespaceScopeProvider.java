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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.scoping.impl.ImportNormalizer;
import org.eclipse.xtext.xbase.scoping.XImportSectionNamespaceScopeProvider;

/**
 * Adds a series of classes to be directly accessible from the Ocelet language
 * 
 * @author Pascal Degenne - Initial contribution
 *
 */
@SuppressWarnings("restriction")
public class OceletImportedNamespaceScopeProvider extends
XImportSectionNamespaceScopeProvider {

	@Override
	protected List<ImportNormalizer> getImplicitImports(boolean ignoreCase) {
		List<ImportNormalizer> inl = new ArrayList<ImportNormalizer>();
		inl.add(new ImportNormalizer(QualifiedName.create("java", "lang"),
				true, ignoreCase));
		inl.add(new ImportNormalizer(QualifiedName.create("fr","ocelet","runtime","ocltypes"), true, ignoreCase));
		inl.add(new ImportNormalizer(QualifiedName.create("fr","ocelet","runtime","geom","ocltypes"), true, ignoreCase));
		inl.add(new ImportNormalizer(QualifiedName.create("fr","ocelet","datafacer","ocltypes"), true, ignoreCase));
		inl.add(new ImportNormalizer(QualifiedName.create("fr","ocelet","runtime","relation","aggregops"), true, ignoreCase));
		return inl;
	}

}