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
import fr.ocelet.lang.ocelet.Agregdef;
import fr.ocelet.lang.ocelet.Comitexpr;
import fr.ocelet.lang.ocelet.Datafacer;
import fr.ocelet.lang.ocelet.Entity;
import fr.ocelet.lang.ocelet.EntityElements;
import fr.ocelet.lang.ocelet.Match;
import fr.ocelet.lang.ocelet.Matchtype;
import fr.ocelet.lang.ocelet.Mdef;
import fr.ocelet.lang.ocelet.ModEln;
import fr.ocelet.lang.ocelet.Model;
import fr.ocelet.lang.ocelet.OceletPackage;
import fr.ocelet.lang.ocelet.Paramdefa;
import fr.ocelet.lang.ocelet.Parameter;
import fr.ocelet.lang.ocelet.Parampart;
import fr.ocelet.lang.ocelet.PropertyDef;
import fr.ocelet.lang.ocelet.Rangevals;
import fr.ocelet.lang.ocelet.RelPropertyDef;
import fr.ocelet.lang.ocelet.Relation;
import fr.ocelet.lang.ocelet.Scenario;
import fr.ocelet.lang.ocelet.StrucEln;
import fr.ocelet.lang.ocelet.StrucVarDef;
import fr.ocelet.lang.ocelet.Strucdef;
import fr.ocelet.lang.validation.AbstractOceletValidator;
import java.util.ArrayList;
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
  @Check
  public void checkDuplicateEntity(final Entity ent) {
    EObject _eContainer = ent.eContainer();
    final Model meln = ((Model) _eContainer);
    int ecount = 0;
    EList<ModEln> _modelns = meln.getModelns();
    for (final ModEln eln : _modelns) {
      boolean _matched = false;
      if (eln instanceof Entity) {
        _matched=true;
        int _compareTo = ((Entity)eln).getName().compareTo(ent.getName());
        boolean _equals = (_compareTo == 0);
        if (_equals) {
          ecount++;
        }
      }
    }
    if ((ecount > 1)) {
      String _name = ent.getName();
      String _plus = ("An entity named " + _name);
      String _plus_1 = (_plus + " already exists in this model.");
      this.error(_plus_1, OceletPackage.Literals.ENTITY__NAME);
    }
  }
  
  @Check
  public void checkDuplicateDatafacer(final Datafacer daf) {
    EObject _eContainer = daf.eContainer();
    final Model meln = ((Model) _eContainer);
    int ecount = 0;
    EList<ModEln> _modelns = meln.getModelns();
    for (final ModEln eln : _modelns) {
      boolean _matched = false;
      if (eln instanceof Datafacer) {
        _matched=true;
        int _compareTo = ((Datafacer)eln).getName().compareTo(daf.getName());
        boolean _equals = (_compareTo == 0);
        if (_equals) {
          ecount++;
        }
      }
    }
    if ((ecount > 1)) {
      String _name = daf.getName();
      String _plus = ("A datafacer named " + _name);
      String _plus_1 = (_plus + " already exists in this model.");
      this.error(_plus_1, OceletPackage.Literals.DATAFACER__NAME);
    }
  }
  
  @Check
  public void checkDuplicateRelation(final Relation rel) {
    EObject _eContainer = rel.eContainer();
    final Model meln = ((Model) _eContainer);
    int ecount = 0;
    EList<ModEln> _modelns = meln.getModelns();
    for (final ModEln eln : _modelns) {
      boolean _matched = false;
      if (eln instanceof Relation) {
        _matched=true;
        int _compareTo = ((Relation)eln).getName().compareTo(rel.getName());
        boolean _equals = (_compareTo == 0);
        if (_equals) {
          ecount++;
        }
      }
    }
    if ((ecount > 1)) {
      String _name = rel.getName();
      String _plus = ("A relation named " + _name);
      String _plus_1 = (_plus + " already exists in this model.");
      this.error(_plus_1, OceletPackage.Literals.RELATION__NAME);
    }
  }
  
  @Check
  public void checkDuplicateStructure(final Strucdef stru) {
    EObject _eContainer = stru.eContainer();
    final Model meln = ((Model) _eContainer);
    int ecount = 0;
    EList<ModEln> _modelns = meln.getModelns();
    for (final ModEln eln : _modelns) {
      boolean _matched = false;
      if (eln instanceof Strucdef) {
        _matched=true;
        int _compareTo = ((Strucdef)eln).getName().compareTo(stru.getName());
        boolean _equals = (_compareTo == 0);
        if (_equals) {
          ecount++;
        }
      }
    }
    if ((ecount > 1)) {
      String _name = stru.getName();
      String _plus = ("A structure named " + _name);
      String _plus_1 = (_plus + " already exists in this model.");
      this.error(_plus_1, OceletPackage.Literals.STRUCDEF__NAME);
    }
  }
  
  @Check
  public void checkDuplicateAgreg(final Agregdef agr) {
    EObject _eContainer = agr.eContainer();
    final Model meln = ((Model) _eContainer);
    int ecount = 0;
    EList<ModEln> _modelns = meln.getModelns();
    for (final ModEln eln : _modelns) {
      boolean _matched = false;
      if (eln instanceof Agregdef) {
        _matched=true;
        int _compareTo = ((Agregdef)eln).getName().compareTo(agr.getName());
        boolean _equals = (_compareTo == 0);
        if (_equals) {
          ecount++;
        }
      }
    }
    if ((ecount > 1)) {
      String _name = agr.getName();
      String _plus = ("An aggregation function named " + _name);
      String _plus_1 = (_plus + " already exists in this model.");
      this.error(_plus_1, OceletPackage.Literals.AGREGDEF__NAME);
    }
  }
  
  @Check
  public void checkDuplicateScen(final Scenario scn) {
    EObject _eContainer = scn.eContainer();
    final Model meln = ((Model) _eContainer);
    int ecount = 0;
    EList<ModEln> _modelns = meln.getModelns();
    for (final ModEln eln : _modelns) {
      boolean _matched = false;
      if (eln instanceof Scenario) {
        _matched=true;
        int _compareTo = ((Scenario)eln).getName().compareTo(scn.getName());
        boolean _equals = (_compareTo == 0);
        if (_equals) {
          ecount++;
        }
      }
    }
    if ((ecount > 1)) {
      String _name = scn.getName();
      String _plus = ("A scenario named " + _name);
      String _plus_1 = (_plus + " already exists in this model.");
      this.error(_plus_1, OceletPackage.Literals.SCENARIO__NAME);
    }
  }
  
  /**
   * Agg : does the given property exist ?
   */
  @Check
  public void checkAggProperties(final Comitexpr ce) {
    final String prop = ce.getProp();
    boolean res = false;
    EList<EntityElements> _entelns = ce.getRol().getType().getEntelns();
    for (final EntityElements eln : _entelns) {
      int _compareTo = eln.getName().compareTo(prop);
      boolean _equals = (_compareTo == 0);
      if (_equals) {
        res = true;
      }
    }
    if ((!res)) {
      this.error(("Unkown property " + prop), OceletPackage.Literals.COMITEXPR__PROP);
    }
  }
  
  private final static ArrayList<String> ldn = new ArrayList<String>();
  
  /**
   * Make sure the given datafacer type does exist
   */
  @Check
  public void checkDatafacerType(final Datafacer df) {
    int _size = OceletValidator.ldn.size();
    boolean _equals = (_size == 0);
    if (_equals) {
      OceletValidator.ldn.add("Csvfile");
      OceletValidator.ldn.add("KmlExport");
      OceletValidator.ldn.add("Postgis");
      OceletValidator.ldn.add("RasterFile");
      OceletValidator.ldn.add("Shapefile");
      OceletValidator.ldn.add("TemporalSeriesFile");
    }
    boolean _contains = OceletValidator.ldn.contains(df.getStoretype());
    boolean _not = (!_contains);
    if (_not) {
      this.error("Unknown datafacer type.", OceletPackage.Literals.DATAFACER__STORETYPE);
    }
  }
  
  /**
   * Property names must not begin with an uppercase letter
   */
  @Check
  public void checkPropertyNameLowercase(final PropertyDef pdef) {
    boolean _isUpperCase = Character.isUpperCase(pdef.getName().charAt(0));
    if (_isUpperCase) {
      this.error("The name of a property must not begin with an upper case character. Thank you.", OceletPackage.Literals.ENTITY_ELEMENTS__NAME);
    }
  }
  
  /**
   * Property names must not begin with an uppercase letter
   */
  @Check
  public void checkRelPropertyNameLowercase(final RelPropertyDef rpdef) {
    boolean _isUpperCase = Character.isUpperCase(rpdef.getName().charAt(0));
    if (_isUpperCase) {
      this.error("The name of a property must not begin with an upper case character. Thank you.", OceletPackage.Literals.REL_ELEMENTS__NAME);
    }
  }
  
  /**
   * Structure variable  names must not begin with an uppercase letter
   */
  @Check
  public void checkStrucVarNameLowercase(final StrucVarDef svd) {
    boolean _isUpperCase = Character.isUpperCase(svd.getName().charAt(0));
    if (_isUpperCase) {
      this.error("The variable name of a structure must not begin with an upper case character. Thank you.", OceletPackage.Literals.STRUC_ELN__NAME);
    }
  }
  
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
    if (mt instanceof Entity) {
      _matched=true;
      EList<EntityElements> _entelns = ((Entity)mt).getEntelns();
      for (final EntityElements eln : _entelns) {
        boolean _matched_1 = false;
        if (eln instanceof PropertyDef) {
          _matched_1=true;
          int _compareTo = ((PropertyDef)eln).getName().compareTo(mdef.getProp());
          boolean _equals = (_compareTo == 0);
          if (_equals) {
            propfound = true;
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
    if (!_matched) {
      if (mt instanceof Strucdef) {
        _matched=true;
        EList<StrucEln> _strucelns = ((Strucdef)mt).getStrucelns();
        for (final StrucEln eln : _strucelns) {
          boolean _matched_1 = false;
          if (eln instanceof StrucVarDef) {
            _matched_1=true;
            int _compareTo = ((StrucVarDef)eln).getName().compareTo(mdef.getProp());
            boolean _equals = (_compareTo == 0);
            if (_equals) {
              propfound = true;
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
        if (pp instanceof Paramdefa) {
          _matched=true;
          sdef = ((Paramdefa)pp).getPardefa();
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
