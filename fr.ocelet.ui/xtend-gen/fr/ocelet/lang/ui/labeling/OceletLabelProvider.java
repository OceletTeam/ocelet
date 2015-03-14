/**
 * Customize outline text and images
 * @author Pascal Degenne - Initial contribution
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
import fr.ocelet.lang.ocelet.StrucVarDef;
import fr.ocelet.lang.ocelet.Strucdef;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.xbase.ui.labeling.XbaseLabelProvider;

/**
 * Provides labels for a EObjects.
 * 
 * see http://www.eclipse.org/Xtext/documentation.html#labelProvider
 */
@SuppressWarnings("all")
public class OceletLabelProvider extends XbaseLabelProvider {
  @Inject
  public OceletLabelProvider(final AdapterFactoryLabelProvider delegate) {
    super(delegate);
  }
  
  protected Object doGetImage(final Object eln) {
    Object _switchResult = null;
    boolean _matched = false;
    if (!_matched) {
      if (eln instanceof Agregdef) {
        _matched=true;
        _switchResult = "outline/gif/agreg.gif";
      }
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
      _switchResult = super.doGetImage(eln);
    }
    return _switchResult;
  }
  
  protected Object doGetText(final Object eln) {
    Object _switchResult = null;
    boolean _matched = false;
    if (!_matched) {
      if (eln instanceof Agregdef) {
        _matched=true;
        String _name = ((Agregdef)eln).getName();
        String _plus = (_name + "() returns ");
        JvmTypeReference _type = ((Agregdef)eln).getType();
        String _simpleName = _type.getSimpleName();
        _switchResult = (_plus + _simpleName);
      }
    }
    if (!_matched) {
      if (eln instanceof ConstructorDef) {
        _matched=true;
        String _name = ((ConstructorDef)eln).getName();
        _switchResult = (_name + "()");
      }
    }
    if (!_matched) {
      if (eln instanceof Entity) {
        _matched=true;
        _switchResult = ((Entity)eln).getName();
      }
    }
    if (!_matched) {
      if (eln instanceof Filterdef) {
        _matched=true;
        String _name = ((Filterdef)eln).getName();
        _switchResult = (_name + "()");
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
        _matched=true;
        String _name = ((Parameter)eln).getName();
        String _plus = (_name + " is ");
        JvmTypeReference _ptype = ((Parameter)eln).getPtype();
        String _simpleName = _ptype.getSimpleName();
        _switchResult = (_plus + _simpleName);
      }
    }
    if (!_matched) {
      if (eln instanceof PropertyDef) {
        _matched=true;
        String _name = ((PropertyDef)eln).getName();
        String _plus = (_name + " is ");
        JvmTypeReference _type = ((PropertyDef)eln).getType();
        String _simpleName = _type.getSimpleName();
        _switchResult = (_plus + _simpleName);
      }
    }
    if (!_matched) {
      if (eln instanceof Relation) {
        _matched=true;
        _switchResult = this.relText(((Relation)eln));
      }
    }
    if (!_matched) {
      if (eln instanceof Scenario) {
        _matched=true;
        _switchResult = ((Scenario)eln).getName();
      }
    }
    if (!_matched) {
      if (eln instanceof ServiceDef) {
        JvmTypeReference _type = ((ServiceDef)eln).getType();
        boolean _notEquals = (!Objects.equal(_type, null));
        if (_notEquals) {
          _matched=true;
          String _name = ((ServiceDef)eln).getName();
          String _plus = (_name + "() returns ");
          JvmTypeReference _type_1 = ((ServiceDef)eln).getType();
          String _simpleName = _type_1.getSimpleName();
          _switchResult = (_plus + _simpleName);
        }
      }
    }
    if (!_matched) {
      if (eln instanceof ServiceDef) {
        JvmTypeReference _type = ((ServiceDef)eln).getType();
        boolean _equals = Objects.equal(_type, null);
        if (_equals) {
          _matched=true;
          String _name = ((ServiceDef)eln).getName();
          _switchResult = (_name + "()");
        }
      }
    }
    if (!_matched) {
      if (eln instanceof Strucdef) {
        _matched=true;
        _switchResult = ((Strucdef)eln).getName();
      }
    }
    if (!_matched) {
      if (eln instanceof StrucVarDef) {
        _matched=true;
        String _name = ((StrucVarDef)eln).getName();
        String _plus = (_name + " is ");
        JvmTypeReference _type = ((StrucVarDef)eln).getType();
        String _simpleName = _type.getSimpleName();
        _switchResult = (_plus + _simpleName);
      }
    }
    if (!_matched) {
      _switchResult = super.doGetText(eln);
    }
    return _switchResult;
  }
  
  private String relText(final Relation rel) {
    final StringBuffer r = new StringBuffer();
    String _name = rel.getName();
    String _plus = (_name + "<");
    r.append(_plus);
    int i = 0;
    EList<Role> _roles = rel.getRoles();
    for (final Role rol : _roles) {
      {
        if ((i > 0)) {
          r.append(",");
        }
        i = (i + 1);
        Entity _type = rol.getType();
        String _name_1 = _type.getName();
        r.append(_name_1);
      }
    }
    r.append(">");
    return r.toString();
  }
}
