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

package fr.ocelet.lang.ui.outline

import fr.ocelet.lang.ocelet.Agregdef
import fr.ocelet.lang.ocelet.ConstructorDef
import fr.ocelet.lang.ocelet.Datafacer
import fr.ocelet.lang.ocelet.Filterdef
import fr.ocelet.lang.ocelet.InteractionDef
import fr.ocelet.lang.ocelet.Parameter
import fr.ocelet.lang.ocelet.PropertyDef
import fr.ocelet.lang.ocelet.RelPropertyDef
import fr.ocelet.lang.ocelet.Scenario
import fr.ocelet.lang.ocelet.ServiceDef
import fr.ocelet.lang.ocelet.StrucFuncDef
import fr.ocelet.lang.ocelet.StrucVarDef
import org.eclipse.xtext.ui.editor.outline.impl.DefaultOutlineTreeProvider

/**
 * Customization of the default outline structure.
 *
 * @author Pascal Degenne - Initial contribution
 */
class OceletOutlineTreeProvider extends DefaultOutlineTreeProvider {
	def _isLeaf(ServiceDef sd) {
		return true;
	}

	def _isLeaf(ConstructorDef sd) {
		return true;
	}

	def _isLeaf(PropertyDef pdef) {
		return true;
	}

	def _isLeaf(StrucVarDef svdef) {
		return true;
	}

	def _isLeaf(StrucFuncDef sfdef) {
		return true;
	}

	def _isLeaf(Scenario scn) {
		return true;
	}

	def _isLeaf(Datafacer dat) {
		return true;
	}

	def _isLeaf(RelPropertyDef rpd) {
		return true;
	}

	def _isLeaf(InteractionDef idef) {
		return true;
	}

	def _isLeaf(Parameter idef) {
		return true;
	}

	def _isLeaf(Agregdef adef) {
		return true;
	}
	
	def _isLeaf(Filterdef fdef) {
		return true;
	}
}
