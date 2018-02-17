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
package fr.ocelet.lang.ui.labeling;

import com.google.common.base.Objects;
import com.google.inject.Inject;
import fr.ocelet.lang.ocelet.Agregdef;
import fr.ocelet.lang.ocelet.ConstructorDef;
import fr.ocelet.lang.ocelet.Datafacer;
import fr.ocelet.lang.ocelet.Entity;
import fr.ocelet.lang.ocelet.Filterdef;
import fr.ocelet.lang.ocelet.InteractionDef;
import fr.ocelet.lang.ocelet.Metadata;
import fr.ocelet.lang.ocelet.Model;
import fr.ocelet.lang.ocelet.Parameter;
import fr.ocelet.lang.ocelet.PropertyDef;
import fr.ocelet.lang.ocelet.RelPropertyDef;
import fr.ocelet.lang.ocelet.Relation;
import fr.ocelet.lang.ocelet.Role;
import fr.ocelet.lang.ocelet.Scenario;
import fr.ocelet.lang.ocelet.ServiceDef;
import fr.ocelet.lang.ocelet.StrucFuncDef;
import fr.ocelet.lang.ocelet.StrucVarDef;
import fr.ocelet.lang.ocelet.Strucdef;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.ui.labeling.XbaseLabelProvider;

/**
 * Customize outline text and images
 * @author Pascal Degenne - Initial contribution
 */
@SuppressWarnings("all")
public class OceletLabelProvider extends XbaseLabelProvider {
  @Inject
  public OceletLabelProvider(final AdapterFactoryLabelProvider delegate) {
    super(delegate);
  }
  
  @Override
  protected Object doGetImage(final Object eln) {
    Object _switchResult = null;
    boolean _matched = false;
    if (eln instanceof Agregdef) {
      _matched=true;
      _switchResult = "outline/gif/agreg.gif";
    }
    if (!_matched) {
      if (eln instanceof ConstructorDef) {
        _matched=true;
        _switchResult = "outline/gif/entity_init.gif";
      }
    }
    if (!_matched) {
      if (eln instanceof Datafacer) {
        _matched=true;
        _switchResult = "outline/gif/datafacer.gif";
      }
    }
    if (!_matched) {
      if (eln instanceof Entity) {
        _matched=true;
        _switchResult = "outline/gif/entity.gif";
      }
    }
    if (!_matched) {
      if (eln instanceof Filterdef) {
        _matched=true;
        _switchResult = "outline/gif/filter.gif";
      }
    }
    if (!_matched) {
      if (eln instanceof InteractionDef) {
        _matched=true;
        _switchResult = "outline/gif/service_rel.gif";
      }
    }
    if (!_matched) {
      if (eln instanceof Metadata) {
        _matched=true;
        _switchResult = "outline/gif/model.gif";
      }
    }
    if (!_matched) {
      if (eln instanceof Model) {
        _matched=true;
        _switchResult = "outline/gif/model.gif";
      }
    }
    if (!_matched) {
      if (eln instanceof Parameter) {
        _matched=true;
        _switchResult = "outline/gif/propStruc.gif";
      }
    }
    if (!_matched) {
      if (eln instanceof PropertyDef) {
        _matched=true;
        _switchResult = "outline/gif/property.gif";
      }
    }
    if (!_matched) {
      if (eln instanceof Relation) {
        _matched=true;
        _switchResult = "outline/gif/relation.gif";
      }
    }
    if (!_matched) {
      if (eln instanceof RelPropertyDef) {
        _matched=true;
        _switchResult = "outline/gif/property.gif";
      }
    }
    if (!_matched) {
      if (eln instanceof Role) {
        _matched=true;
        _switchResult = "outline/gif/default.gif";
      }
    }
    if (!_matched) {
      if (eln instanceof Scenario) {
        _matched=true;
        _switchResult = "outline/gif/scenario.gif";
      }
    }
    if (!_matched) {
      if (eln instanceof ServiceDef) {
        _matched=true;
        _switchResult = "outline/gif/entity_svc.gif";
      }
    }
    if (!_matched) {
      if (eln instanceof Strucdef) {
        _matched=true;
        _switchResult = "outline/gif/structure.gif";
      }
    }
    if (!_matched) {
      if (eln instanceof StrucVarDef) {
        _matched=true;
        _switchResult = "outline/gif/propStruc.gif";
      }
    }
    if (!_matched) {
      if (eln instanceof StrucFuncDef) {
        _matched=true;
        _switchResult = "outline/gif/entity_svc.gif";
      }
    }
    if (!_matched) {
      _switchResult = super.doGetImage(eln);
    }
    return _switchResult;
  }
  
  @Override
  protected Object doGetText(final Object eln) {
    String _switchResult = null;
    boolean _matched = false;
    if (eln instanceof Agregdef) {
      if (((!Objects.equal(((Agregdef)eln).getName(), null)) && (!Objects.equal(((Agregdef)eln).getType(), null)))) {
        _matched=true;
        String _name = ((Agregdef)eln).getName();
        String _plus = (_name + "() returns ");
        String _simpleName = ((Agregdef)eln).getType().getSimpleName();
        _switchResult = (_plus + _simpleName);
      }
    }
    if (!_matched) {
      if (eln instanceof ConstructorDef) {
        String _name = ((ConstructorDef)eln).getName();
        boolean _notEquals = (!Objects.equal(_name, null));
        if (_notEquals) {
          _matched=true;
          String _name_1 = ((ConstructorDef)eln).getName();
          _switchResult = (_name_1 + "()");
        }
      }
    }
    if (!_matched) {
      if (eln instanceof Datafacer) {
        String _name = ((Datafacer)eln).getName();
        boolean _notEquals = (!Objects.equal(_name, null));
        if (_notEquals) {
          _matched=true;
          _switchResult = ((Datafacer)eln).getName();
        }
      }
    }
    if (!_matched) {
      if (eln instanceof Entity) {
        String _name = ((Entity)eln).getName();
        boolean _notEquals = (!Objects.equal(_name, null));
        if (_notEquals) {
          _matched=true;
          _switchResult = ((Entity)eln).getName();
        }
      }
    }
    if (!_matched) {
      if (eln instanceof Filterdef) {
        String _name = ((Filterdef)eln).getName();
        boolean _notEquals = (!Objects.equal(_name, null));
        if (_notEquals) {
          _matched=true;
          String _name_1 = ((Filterdef)eln).getName();
          _switchResult = (_name_1 + "()");
        }
      }
    }
    if (!_matched) {
      if (eln instanceof InteractionDef) {
        String _name = ((InteractionDef)eln).getName();
        boolean _notEquals = (!Objects.equal(_name, null));
        if (_notEquals) {
          _matched=true;
          String _name_1 = ((InteractionDef)eln).getName();
          _switchResult = (_name_1 + "()");
        }
      }
    }
    if (!_matched) {
      if (eln instanceof Metadata) {
        _matched=true;
        _switchResult = "Metadata";
      }
    }
    if (!_matched) {
      if (eln instanceof Model) {
        _matched=true;
        _switchResult = "Model";
      }
    }
    if (!_matched) {
      if (eln instanceof Parameter) {
        if (((!Objects.equal(((Parameter)eln).getName(), null)) && (!Objects.equal(((Parameter)eln).getPtype(), null)))) {
          _matched=true;
          String _name = ((Parameter)eln).getName();
          String _plus = (_name + " is ");
          String _simpleName = ((Parameter)eln).getPtype().getSimpleName();
          _switchResult = (_plus + _simpleName);
        }
      }
    }
    if (!_matched) {
      if (eln instanceof PropertyDef) {
        if (((!Objects.equal(((PropertyDef)eln).getName(), null)) && (!Objects.equal(((PropertyDef)eln).getType(), null)))) {
          _matched=true;
          String _name = ((PropertyDef)eln).getName();
          String _plus = (_name + " is ");
          String _simpleName = ((PropertyDef)eln).getType().getSimpleName();
          _switchResult = (_plus + _simpleName);
        }
      }
    }
    if (!_matched) {
      if (eln instanceof Relation) {
        _matched=true;
        _switchResult = this.relText(((Relation)eln));
      }
    }
    if (!_matched) {
      if (eln instanceof RelPropertyDef) {
        if (((!Objects.equal(((RelPropertyDef)eln).getName(), null)) && (!Objects.equal(((RelPropertyDef)eln).getType(), null)))) {
          _matched=true;
          String _name = ((RelPropertyDef)eln).getName();
          String _plus = (_name + " is ");
          String _simpleName = ((RelPropertyDef)eln).getType().getSimpleName();
          _switchResult = (_plus + _simpleName);
        }
      }
    }
    if (!_matched) {
      if (eln instanceof Role) {
        String _name = ((Role)eln).getName();
        boolean _notEquals = (!Objects.equal(_name, null));
        if (_notEquals) {
          _matched=true;
          _switchResult = ((Role)eln).getName();
        }
      }
    }
    if (!_matched) {
      if (eln instanceof Scenario) {
        String _name = ((Scenario)eln).getName();
        boolean _notEquals = (!Objects.equal(_name, null));
        if (_notEquals) {
          _matched=true;
          _switchResult = ((Scenario)eln).getName();
        }
      }
    }
    if (!_matched) {
      if (eln instanceof ServiceDef) {
        if (((!Objects.equal(((ServiceDef)eln).getName(), null)) && (!Objects.equal(((ServiceDef)eln).getType(), null)))) {
          _matched=true;
          String _name = ((ServiceDef)eln).getName();
          String _plus = (_name + "() returns ");
          String _simpleName = ((ServiceDef)eln).getType().getSimpleName();
          _switchResult = (_plus + _simpleName);
        }
      }
    }
    if (!_matched) {
      if (eln instanceof ServiceDef) {
        if (((!Objects.equal(((ServiceDef)eln).getName(), null)) && Objects.equal(((ServiceDef)eln).getType(), null))) {
          _matched=true;
          String _name = ((ServiceDef)eln).getName();
          _switchResult = (_name + "()");
        }
      }
    }
    if (!_matched) {
      if (eln instanceof Strucdef) {
        String _name = ((Strucdef)eln).getName();
        boolean _notEquals = (!Objects.equal(_name, null));
        if (_notEquals) {
          _matched=true;
          _switchResult = ((Strucdef)eln).getName();
        }
      }
    }
    if (!_matched) {
      if (eln instanceof StrucVarDef) {
        if (((!Objects.equal(((StrucVarDef)eln).getName(), null)) && (!Objects.equal(((StrucVarDef)eln).getType(), null)))) {
          _matched=true;
          String _name = ((StrucVarDef)eln).getName();
          String _plus = (_name + " is ");
          String _simpleName = ((StrucVarDef)eln).getType().getSimpleName();
          _switchResult = (_plus + _simpleName);
        }
      }
    }
    if (!_matched) {
      if (eln instanceof StrucFuncDef) {
        if (((!Objects.equal(((StrucFuncDef)eln).getName(), null)) && (!Objects.equal(((StrucFuncDef)eln).getType(), null)))) {
          _matched=true;
          String _name = ((StrucFuncDef)eln).getName();
          String _plus = (_name + "() returns ");
          String _simpleName = ((StrucFuncDef)eln).getType().getSimpleName();
          _switchResult = (_plus + _simpleName);
        }
      }
    }
    if (!_matched) {
      if (eln instanceof StrucFuncDef) {
        if (((!Objects.equal(((StrucFuncDef)eln).getName(), null)) && Objects.equal(((StrucFuncDef)eln).getType(), null))) {
          _matched=true;
          String _name = ((StrucFuncDef)eln).getName();
          _switchResult = (_name + "()");
        }
      }
    }
    if (!_matched) {
      _switchResult = "...";
    }
    return _switchResult;
  }
  
  private String relText(final Relation rel) {
    final StringBuffer r = new StringBuffer();
    String _name = rel.getName();
    String _plus = (_name + "<");
    r.append(_plus);
    try {
      int i = 0;
      EList<Role> _roles = rel.getRoles();
      for (final Role rol : _roles) {
        {
          if ((i > 0)) {
            r.append(",");
          }
          i = (i + 1);
          String _elvis = null;
          Entity _type = rol.getType();
          String _name_1 = null;
          if (_type!=null) {
            _name_1=_type.getName();
          }
          if (_name_1 != null) {
            _elvis = _name_1;
          } else {
            _elvis = ".?.";
          }
          r.append(_elvis);
        }
      }
    } catch (final Throwable _t) {
      if (_t instanceof NullPointerException) {
        final NullPointerException npe = (NullPointerException)_t;
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    }
    r.append(">");
    return r.toString();
  }
}
