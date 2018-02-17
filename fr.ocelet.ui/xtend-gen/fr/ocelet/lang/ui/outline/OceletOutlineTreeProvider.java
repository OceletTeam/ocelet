/**
 * Ocelet spatial modelling language.   www.ocelet.org
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
package fr.ocelet.lang.ui.outline;

import fr.ocelet.lang.ocelet.Agregdef;
import fr.ocelet.lang.ocelet.ConstructorDef;
import fr.ocelet.lang.ocelet.Datafacer;
import fr.ocelet.lang.ocelet.Filterdef;
import fr.ocelet.lang.ocelet.InteractionDef;
import fr.ocelet.lang.ocelet.Parameter;
import fr.ocelet.lang.ocelet.PropertyDef;
import fr.ocelet.lang.ocelet.RelPropertyDef;
import fr.ocelet.lang.ocelet.Scenario;
import fr.ocelet.lang.ocelet.ServiceDef;
import fr.ocelet.lang.ocelet.StrucFuncDef;
import fr.ocelet.lang.ocelet.StrucVarDef;
import org.eclipse.xtext.ui.editor.outline.impl.DefaultOutlineTreeProvider;

/**
 * Customization of the default outline structure.
 * 
 * @author Pascal Degenne - Initial contribution
 */
@SuppressWarnings("all")
public class OceletOutlineTreeProvider extends DefaultOutlineTreeProvider {
  public boolean _isLeaf(final ServiceDef sd) {
    return true;
  }
  
  public boolean _isLeaf(final ConstructorDef sd) {
    return true;
  }
  
  public boolean _isLeaf(final PropertyDef pdef) {
    return true;
  }
  
  public boolean _isLeaf(final StrucVarDef svdef) {
    return true;
  }
  
  public boolean _isLeaf(final StrucFuncDef sfdef) {
    return true;
  }
  
  public boolean _isLeaf(final Scenario scn) {
    return true;
  }
  
  public boolean _isLeaf(final Datafacer dat) {
    return true;
  }
  
  public boolean _isLeaf(final RelPropertyDef rpd) {
    return true;
  }
  
  public boolean _isLeaf(final InteractionDef idef) {
    return true;
  }
  
  public boolean _isLeaf(final Parameter idef) {
    return true;
  }
  
  public boolean _isLeaf(final Agregdef adef) {
    return true;
  }
  
  public boolean _isLeaf(final Filterdef fdef) {
    return true;
  }
}
