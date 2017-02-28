/**
 * Ocelet spatial modelling language.   www.ocelet.org
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
package fr.ocelet.lang.validation;

import com.google.common.base.Objects;
import fr.ocelet.lang.ocelet.Entity;
import fr.ocelet.lang.ocelet.EntityElements;
import fr.ocelet.lang.ocelet.Match;
import fr.ocelet.lang.ocelet.Matchtype;
import fr.ocelet.lang.ocelet.Mdef;
import fr.ocelet.lang.ocelet.OceletPackage;
import fr.ocelet.lang.ocelet.Paramdefa;
import fr.ocelet.lang.ocelet.Parameter;
import fr.ocelet.lang.ocelet.Parampart;
import fr.ocelet.lang.ocelet.PropertyDef;
import fr.ocelet.lang.ocelet.Rangevals;
import fr.ocelet.lang.ocelet.StrucEln;
import fr.ocelet.lang.ocelet.StrucVarDef;
import fr.ocelet.lang.ocelet.Strucdef;
import fr.ocelet.lang.validation.AbstractOceletValidator;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.validation.Check;
import org.eclipse.xtext.xbase.lib.Exceptions;

/**
 * Custom validation rules of the editor
 * 
 * @author Pascal Degenne - Initial contribution
 */
@SuppressWarnings("all")
public class OceletValidator extends AbstractOceletValidator {
  /**
   * Makes sure that every datafacer match definition is defined in the
   * corresponding entity (or structure)
   */
  @Check
  public void checkMatchElements(final Mdef mdef) {
    EObject _eContainer = mdef.eContainer();
    final Match match = ((Match) _eContainer);
    final Matchtype mt = match.getMtype();
    boolean propfound = false;
    boolean _matched = false;
    if (!_matched) {
      if (mt instanceof Entity) {
        _matched=true;
        EList<EntityElements> _entelns = ((Entity)mt).getEntelns();
        for (final EntityElements eln : _entelns) {
          boolean _matched_1 = false;
          if (!_matched_1) {
            if (eln instanceof PropertyDef) {
              _matched_1=true;
              String _name = ((PropertyDef)eln).getName();
              String _prop = mdef.getProp();
              int _compareTo = _name.compareTo(_prop);
              boolean _equals = (_compareTo == 0);
              if (_equals) {
                propfound = true;
              }
            }
          }
        }
        if ((!propfound)) {
          String _name = ((Entity)mt).getName();
          String _plus = ("The entity " + _name);
          String _plus_1 = (_plus + " has no property named ");
          String _prop = mdef.getProp();
          String _plus_2 = (_plus_1 + _prop);
          this.error(_plus_2, OceletPackage.Literals.MDEF__PROP);
        }
      }
    }
    if (!_matched) {
      if (mt instanceof Strucdef) {
        _matched=true;
        EList<StrucEln> _strucelns = ((Strucdef)mt).getStrucelns();
        for (final StrucEln eln : _strucelns) {
          boolean _matched_1 = false;
          if (!_matched_1) {
            if (eln instanceof StrucVarDef) {
              _matched_1=true;
              String _name = ((StrucVarDef)eln).getName();
              String _prop = mdef.getProp();
              int _compareTo = _name.compareTo(_prop);
              boolean _equals = (_compareTo == 0);
              if (_equals) {
                propfound = true;
              }
            }
          }
        }
        if ((!propfound)) {
          String _name = ((Strucdef)mt).getName();
          String _plus = ("The structure " + _name);
          String _plus_1 = (_plus + " has no field named ");
          String _prop = mdef.getProp();
          String _plus_2 = (_plus_1 + _prop);
          this.error(_plus_2, OceletPackage.Literals.MDEF__PROP);
        }
      }
    }
  }
  
  @Check
  public void checkParamDefaultWithinRange(final Parameter parameter) {
    EList<Parampart> _paramparts = parameter.getParamparts();
    boolean _notEquals = (!Objects.equal(_paramparts, null));
    if (_notEquals) {
      String sdef = "";
      double rgmin = 0.0;
      double rgmax = 0.0;
      boolean rangeDef = false;
      EList<Parampart> _paramparts_1 = parameter.getParamparts();
      for (final Parampart pp : _paramparts_1) {
        boolean _matched = false;
        if (!_matched) {
          if (pp instanceof Paramdefa) {
            _matched=true;
            String _pardefa = ((Paramdefa)pp).getPardefa();
            sdef = _pardefa;
          }
        }
        if (!_matched) {
          if (pp instanceof Rangevals) {
            _matched=true;
            try {
              String _parmin = ((Rangevals)pp).getParmin();
              Double _double = new Double(_parmin);
              rgmin = (_double).doubleValue();
              String _parmax = ((Rangevals)pp).getParmax();
              Double _double_1 = new Double(_parmax);
              rgmax = (_double_1).doubleValue();
              rangeDef = true;
            } catch (final Throwable _t) {
              if (_t instanceof NumberFormatException) {
                final NumberFormatException nfe = (NumberFormatException)_t;
                this.warning("Parameter range values must be valid numeric values", OceletPackage.Literals.PARAMETER__NAME);
              } else {
                throw Exceptions.sneakyThrow(_t);
              }
            }
          }
        }
      }
      if (rangeDef) {
        try {
          final Double ddef = new Double(sdef);
          if ((((ddef).doubleValue() < rgmin) || ((ddef).doubleValue() > rgmax))) {
            String _name = parameter.getName();
            String _plus = ("The default value of parameter " + _name);
            String _plus_1 = (_plus + " is not within the defined range");
            this.warning(_plus_1, 
              OceletPackage.Literals.PARAMETER__NAME);
          }
        } catch (final Throwable _t) {
          if (_t instanceof NumberFormatException) {
            final NumberFormatException e = (NumberFormatException)_t;
            this.warning(
              (("The parameter Range can only be used for numeric values and " + sdef) + 
                " is not a valid numeric value."), OceletPackage.Literals.PARAMETER__NAME);
          } else {
            throw Exceptions.sneakyThrow(_t);
          }
        }
      }
    }
  }
}
