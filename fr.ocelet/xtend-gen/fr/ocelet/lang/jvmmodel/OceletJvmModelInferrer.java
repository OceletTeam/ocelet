package fr.ocelet.lang.jvmmodel;

import com.google.common.base.Objects;
import com.google.inject.Inject;
import fr.ocelet.lang.jvmmodel.Metadatastuff;
import fr.ocelet.lang.jvmmodel.Parameterstuff;
import fr.ocelet.lang.ocelet.Metadata;
import fr.ocelet.lang.ocelet.ModEln;
import fr.ocelet.lang.ocelet.Model;
import fr.ocelet.lang.ocelet.Paradesc;
import fr.ocelet.lang.ocelet.Paramdefa;
import fr.ocelet.lang.ocelet.Parameter;
import fr.ocelet.lang.ocelet.Parampart;
import fr.ocelet.lang.ocelet.Paramunit;
import fr.ocelet.lang.ocelet.Paraopt;
import fr.ocelet.lang.ocelet.Rangevals;
import fr.ocelet.lang.ocelet.Scenario;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.common.types.JvmConstructor;
import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmFormalParameter;
import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.common.types.JvmMember;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.xbase.XExpression;
import org.eclipse.xtext.xbase.compiler.TypeReferenceSerializer;
import org.eclipse.xtext.xbase.compiler.output.ITreeAppendable;
import org.eclipse.xtext.xbase.jvmmodel.AbstractModelInferrer;
import org.eclipse.xtext.xbase.jvmmodel.IJvmDeclaredTypeAcceptor;
import org.eclipse.xtext.xbase.jvmmodel.JvmTypesBuilder;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class OceletJvmModelInferrer extends AbstractModelInferrer {
  @Inject
  @Extension
  private JvmTypesBuilder _jvmTypesBuilder;
  
  @Inject
  @Extension
  private TypeReferenceSerializer _typeReferenceSerializer;
  
  protected void _infer(final Model modl, final IJvmDeclaredTypeAcceptor acceptor, final boolean isPreIndexingPhase) {
    final List<Scenario> scens = CollectionLiterals.<Scenario>newArrayList();
    final Metadatastuff md = new Metadatastuff();
    boolean mainScen = false;
    final Resource res = modl.eResource();
    URI _uRI = res.getURI();
    final String modlName = _uRI.segment(1);
    String _name = modl.getName();
    boolean _equals = Objects.equal(_name, null);
    if (_equals) {
      String _lowerCase = modlName.toLowerCase();
      String _plus = ("fr.ocelet.model." + _lowerCase);
      modl.setName(_plus);
    }
    String _name_1 = modl.getName();
    final String packg = (_name_1 + ".");
    EList<ModEln> _modelns = modl.getModelns();
    for (final ModEln meln : _modelns) {
      try {
        boolean _matched = false;
        if (!_matched) {
          if (meln instanceof Scenario) {
            _matched=true;
            String _name_2 = ((Scenario)meln).getName();
            int _compareTo = _name_2.compareTo(modlName);
            boolean _equals_1 = (_compareTo == 0);
            if (_equals_1) {
              mainScen = true;
            }
            scens.add(((Scenario)meln));
          }
        }
        if (!_matched) {
          if (meln instanceof Metadata) {
            _matched=true;
            String _desc = ((Metadata)meln).getDesc();
            md.setModeldesc(_desc);
            String _webp = ((Metadata)meln).getWebp();
            md.setWebpage(_webp);
            EList<Parameter> _paramdefs = ((Metadata)meln).getParamdefs();
            for (final Parameter paramdef : _paramdefs) {
              {
                String _name_2 = paramdef.getName();
                JvmTypeReference _ptype = paramdef.getPtype();
                final Parameterstuff pst = new Parameterstuff(_name_2, _ptype);
                EList<Parampart> _paramparts = paramdef.getParamparts();
                for (final Parampart ppart : _paramparts) {
                  boolean _matched_1 = false;
                  if (!_matched_1) {
                    if (ppart instanceof Paramunit) {
                      _matched_1=true;
                      String _parunit = ((Paramunit)ppart).getParunit();
                      pst.setUnit(_parunit);
                    }
                  }
                  if (!_matched_1) {
                    if (ppart instanceof Paramdefa) {
                      _matched_1=true;
                      String _pardefa = ((Paramdefa)ppart).getPardefa();
                      pst.setDefvalue(_pardefa);
                    }
                  }
                  if (!_matched_1) {
                    if (ppart instanceof Rangevals) {
                      _matched_1=true;
                      String _parmin = ((Rangevals)ppart).getParmin();
                      pst.setMin(_parmin);
                      String _parmax = ((Rangevals)ppart).getParmax();
                      pst.setMax(_parmax);
                    }
                  }
                  if (!_matched_1) {
                    if (ppart instanceof Paradesc) {
                      _matched_1=true;
                      String _pardesc = ((Paradesc)ppart).getPardesc();
                      pst.setDescription(_pardesc);
                    }
                  }
                  if (!_matched_1) {
                    if (ppart instanceof Paraopt) {
                      _matched_1=true;
                      String _paropt = ((Paraopt)ppart).getParopt();
                      int _compareToIgnoreCase = _paropt.compareToIgnoreCase("true");
                      boolean _equals_1 = (_compareToIgnoreCase == 0);
                      pst.setOptionnal(Boolean.valueOf(_equals_1));
                    }
                  }
                }
                ArrayList<Parameterstuff> _params = md.getParams();
                _params.add(pst);
              }
            }
          }
        }
      } catch (final Throwable _t) {
        if (_t instanceof Exception) {
          final Exception e = (Exception)_t;
          StringConcatenation _builder = new StringConcatenation();
          _builder.append("Exception caught : ");
          String _message = e.getMessage();
          _builder.append(_message, "");
          InputOutput.<String>println(_builder.toString());
        } else {
          throw Exceptions.sneakyThrow(_t);
        }
      }
    }
    if (mainScen) {
      JvmGenericType _class = this._jvmTypesBuilder.toClass(modl, (packg + modlName));
      final Procedure1<JvmGenericType> _function = new Procedure1<JvmGenericType>() {
        public void apply(final JvmGenericType it) {
          String _documentation = OceletJvmModelInferrer.this._jvmTypesBuilder.getDocumentation(modl);
          OceletJvmModelInferrer.this._jvmTypesBuilder.setDocumentation(it, _documentation);
          EList<JvmTypeReference> _superTypes = it.getSuperTypes();
          JvmTypeReference _typeRef = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.model.AbstractModel");
          OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes, _typeRef);
          EList<JvmMember> _members = it.getMembers();
          final Procedure1<JvmConstructor> _function = new Procedure1<JvmConstructor>() {
            public void apply(final JvmConstructor it) {
              final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
                public void apply(final ITreeAppendable it) {
                  StringConcatenation _builder = new StringConcatenation();
                  _builder.append("super(\"");
                  _builder.append(modlName, "");
                  _builder.append("\");");
                  it.append(_builder);
                  String _modeldesc = md.getModeldesc();
                  boolean _notEquals = (!Objects.equal(_modeldesc, null));
                  if (_notEquals) {
                    it.newLine();
                    StringConcatenation _builder_1 = new StringConcatenation();
                    _builder_1.append("modDescription = \"");
                    String _modeldesc_1 = md.getModeldesc();
                    _builder_1.append(_modeldesc_1, "");
                    _builder_1.append("\";");
                    it.append(_builder_1);
                  }
                  String _webpage = md.getWebpage();
                  boolean _notEquals_1 = (!Objects.equal(_webpage, null));
                  if (_notEquals_1) {
                    it.newLine();
                    StringConcatenation _builder_2 = new StringConcatenation();
                    _builder_2.append("modelWebPage = \"");
                    String _webpage_1 = md.getWebpage();
                    _builder_2.append(_webpage_1, "");
                    _builder_2.append("\";");
                    it.append(_builder_2);
                  }
                  boolean _hasParameters = md.hasParameters();
                  if (_hasParameters) {
                    ArrayList<Parameterstuff> _params = md.getParams();
                    for (final Parameterstuff pstuff : _params) {
                      {
                        JvmTypeReference _type = pstuff.getType();
                        final JvmTypeReference genptype = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.model.Parameter", _type);
                        boolean _isNumericType = pstuff.isNumericType();
                        if (_isNumericType) {
                          JvmTypeReference _type_1 = pstuff.getType();
                          final JvmTypeReference implptype = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.model.NumericParameterImpl", _type_1);
                          it.newLine();
                          OceletJvmModelInferrer.this._typeReferenceSerializer.serialize(genptype, genptype, it);
                          StringConcatenation _builder_3 = new StringConcatenation();
                          _builder_3.append(" ");
                          _builder_3.append("par_");
                          String _name = pstuff.getName();
                          _builder_3.append(_name, " ");
                          _builder_3.append(" = new ");
                          it.append(_builder_3);
                          OceletJvmModelInferrer.this._typeReferenceSerializer.serialize(implptype, implptype, it);
                          StringConcatenation _builder_4 = new StringConcatenation();
                          _builder_4.append("(\"");
                          String _name_1 = pstuff.getName();
                          _builder_4.append(_name_1, "");
                          _builder_4.append("\",\"");
                          String _description = pstuff.getDescription();
                          _builder_4.append(_description, "");
                          _builder_4.append("\",");
                          Boolean _optionnal = pstuff.getOptionnal();
                          _builder_4.append(_optionnal, "");
                          it.append(_builder_4);
                          Object _dvalue = pstuff.getDvalue();
                          boolean _equals = Objects.equal(_dvalue, null);
                          if (_equals) {
                            StringConcatenation _builder_5 = new StringConcatenation();
                            _builder_5.append(",null");
                            it.append(_builder_5);
                          } else {
                            StringConcatenation _builder_6 = new StringConcatenation();
                            _builder_6.append(",");
                            Object _dvalue_1 = pstuff.getDvalue();
                            _builder_6.append(_dvalue_1, "");
                            it.append(_builder_6);
                          }
                          Object _minvalue = pstuff.getMinvalue();
                          boolean _equals_1 = Objects.equal(_minvalue, null);
                          if (_equals_1) {
                            StringConcatenation _builder_7 = new StringConcatenation();
                            _builder_7.append(",null");
                            it.append(_builder_7);
                          } else {
                            StringConcatenation _builder_8 = new StringConcatenation();
                            _builder_8.append(",");
                            Object _minvalue_1 = pstuff.getMinvalue();
                            _builder_8.append(_minvalue_1, "");
                            it.append(_builder_8);
                          }
                          Object _maxvalue = pstuff.getMaxvalue();
                          boolean _equals_2 = Objects.equal(_maxvalue, null);
                          if (_equals_2) {
                            StringConcatenation _builder_9 = new StringConcatenation();
                            _builder_9.append(",null");
                            it.append(_builder_9);
                          } else {
                            StringConcatenation _builder_10 = new StringConcatenation();
                            _builder_10.append(",");
                            Object _maxvalue_1 = pstuff.getMaxvalue();
                            _builder_10.append(_maxvalue_1, "");
                            it.append(_builder_10);
                          }
                          String _unit = pstuff.getUnit();
                          boolean _equals_3 = Objects.equal(_unit, null);
                          if (_equals_3) {
                            StringConcatenation _builder_11 = new StringConcatenation();
                            _builder_11.append(",null");
                            it.append(_builder_11);
                          } else {
                            StringConcatenation _builder_12 = new StringConcatenation();
                            _builder_12.append(",\"");
                            String _unit_1 = pstuff.getUnit();
                            _builder_12.append(_unit_1, "");
                            _builder_12.append("\"");
                            it.append(_builder_12);
                          }
                          StringConcatenation _builder_13 = new StringConcatenation();
                          _builder_13.append(");");
                          it.append(_builder_13);
                        } else {
                          JvmTypeReference _type_2 = pstuff.getType();
                          final JvmTypeReference implptype_1 = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.model.ParameterImpl", _type_2);
                          it.newLine();
                          OceletJvmModelInferrer.this._typeReferenceSerializer.serialize(genptype, genptype, it);
                          StringConcatenation _builder_14 = new StringConcatenation();
                          _builder_14.append(" ");
                          _builder_14.append("par_");
                          String _name_2 = pstuff.getName();
                          _builder_14.append(_name_2, " ");
                          _builder_14.append(" = new ");
                          it.append(_builder_14);
                          OceletJvmModelInferrer.this._typeReferenceSerializer.serialize(implptype_1, implptype_1, it);
                          StringConcatenation _builder_15 = new StringConcatenation();
                          _builder_15.append("(\"");
                          String _name_3 = pstuff.getName();
                          _builder_15.append(_name_3, "");
                          _builder_15.append("\",\"");
                          String _description_1 = pstuff.getDescription();
                          _builder_15.append(_description_1, "");
                          _builder_15.append("\",");
                          Boolean _optionnal_1 = pstuff.getOptionnal();
                          _builder_15.append(_optionnal_1, "");
                          it.append(_builder_15);
                          Object _dvalue_2 = pstuff.getDvalue();
                          boolean _equals_4 = Objects.equal(_dvalue_2, null);
                          if (_equals_4) {
                            StringConcatenation _builder_16 = new StringConcatenation();
                            _builder_16.append(",null");
                            it.append(_builder_16);
                          } else {
                            boolean _isStringType = pstuff.isStringType();
                            if (_isStringType) {
                              StringConcatenation _builder_17 = new StringConcatenation();
                              _builder_17.append(",\"");
                              Object _dvalue_3 = pstuff.getDvalue();
                              _builder_17.append(_dvalue_3, "");
                              _builder_17.append("\"");
                              it.append(_builder_17);
                            } else {
                              StringConcatenation _builder_18 = new StringConcatenation();
                              _builder_18.append(",");
                              Object _dvalue_4 = pstuff.getDvalue();
                              _builder_18.append(_dvalue_4, "");
                              it.append(_builder_18);
                            }
                          }
                          String _unit_2 = pstuff.getUnit();
                          boolean _equals_5 = Objects.equal(_unit_2, null);
                          if (_equals_5) {
                            StringConcatenation _builder_19 = new StringConcatenation();
                            _builder_19.append(",null");
                            it.append(_builder_19);
                          } else {
                            StringConcatenation _builder_20 = new StringConcatenation();
                            _builder_20.append(",\"");
                            String _unit_3 = pstuff.getUnit();
                            _builder_20.append(_unit_3, "");
                            _builder_20.append("\"");
                            it.append(_builder_20);
                          }
                          StringConcatenation _builder_21 = new StringConcatenation();
                          _builder_21.append(");");
                          it.append(_builder_21);
                        }
                        it.newLine();
                        StringConcatenation _builder_22 = new StringConcatenation();
                        _builder_22.append("addParameter(par_");
                        String _name_4 = pstuff.getName();
                        _builder_22.append(_name_4, "");
                        _builder_22.append(");");
                        it.append(_builder_22);
                        Object _dvalue_5 = pstuff.getDvalue();
                        boolean _notEquals_2 = (!Objects.equal(_dvalue_5, null));
                        if (_notEquals_2) {
                          it.newLine();
                          StringConcatenation _builder_23 = new StringConcatenation();
                          _builder_23.append(pstuff.name, "");
                          _builder_23.append(" = ");
                          it.append(_builder_23);
                          boolean _isStringType_1 = pstuff.isStringType();
                          if (_isStringType_1) {
                            StringConcatenation _builder_24 = new StringConcatenation();
                            _builder_24.append("\"");
                            Object _dvalue_6 = pstuff.getDvalue();
                            _builder_24.append(_dvalue_6, "");
                            _builder_24.append("\";");
                            it.append(_builder_24);
                          } else {
                            StringConcatenation _builder_25 = new StringConcatenation();
                            Object _dvalue_7 = pstuff.getDvalue();
                            _builder_25.append(_dvalue_7, "");
                            _builder_25.append(";");
                            it.append(_builder_25);
                          }
                        }
                      }
                    }
                  }
                }
              };
              OceletJvmModelInferrer.this._jvmTypesBuilder.setBody(it, _function);
            }
          };
          JvmConstructor _constructor = OceletJvmModelInferrer.this._jvmTypesBuilder.toConstructor(modl, _function);
          OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmConstructor>operator_add(_members, _constructor);
          for (final Scenario scen : scens) {
            String _name = scen.getName();
            int _compareTo = _name.compareTo(modlName);
            boolean _equals = (_compareTo == 0);
            if (_equals) {
              EList<JvmMember> _members_1 = it.getMembers();
              JvmTypeReference _typeRef_1 = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef(Void.TYPE);
              final Procedure1<JvmOperation> _function_1 = new Procedure1<JvmOperation>() {
                public void apply(final JvmOperation it) {
                  EList<JvmFormalParameter> _parameters = it.getParameters();
                  JvmTypeReference _typeRef = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("java.lang.String");
                  JvmTypeReference _addArrayTypeDimension = OceletJvmModelInferrer.this._jvmTypesBuilder.addArrayTypeDimension(_typeRef);
                  JvmFormalParameter _parameter = OceletJvmModelInferrer.this._jvmTypesBuilder.toParameter(modl, "args", _addArrayTypeDimension);
                  OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                  it.setStatic(true);
                  final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
                    public void apply(final ITreeAppendable it) {
                      StringConcatenation _builder = new StringConcatenation();
                      _builder.append(modlName, "");
                      _builder.append(" model_");
                      _builder.append(modlName, "");
                      _builder.append(" = new ");
                      _builder.append(modlName, "");
                      _builder.append("();");
                      _builder.newLineIfNotEmpty();
                      _builder.append("model_");
                      _builder.append(modlName, "");
                      _builder.append(".run_");
                      _builder.append(modlName, "");
                      _builder.append("();");
                      it.append(_builder);
                    }
                  };
                  OceletJvmModelInferrer.this._jvmTypesBuilder.setBody(it, _function);
                }
              };
              JvmOperation _method = OceletJvmModelInferrer.this._jvmTypesBuilder.toMethod(modl, "main", _typeRef_1, _function_1);
              OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmOperation>operator_add(_members_1, _method);
              EList<JvmMember> _members_2 = it.getMembers();
              JvmTypeReference _typeRef_2 = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef(Void.TYPE);
              final Procedure1<JvmOperation> _function_2 = new Procedure1<JvmOperation>() {
                public void apply(final JvmOperation it) {
                  XExpression _sccode = scen.getSccode();
                  OceletJvmModelInferrer.this._jvmTypesBuilder.setBody(it, _sccode);
                }
              };
              JvmOperation _method_1 = OceletJvmModelInferrer.this._jvmTypesBuilder.toMethod(modl, ("run_" + modlName), _typeRef_2, _function_2);
              OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmOperation>operator_add(_members_2, _method_1);
              EList<JvmMember> _members_3 = it.getMembers();
              JvmTypeReference _typeRef_3 = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef(Void.TYPE);
              final Procedure1<JvmOperation> _function_3 = new Procedure1<JvmOperation>() {
                public void apply(final JvmOperation it) {
                  EList<JvmFormalParameter> _parameters = it.getParameters();
                  JvmTypeReference _typeRef = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("java.lang.String");
                  JvmTypeReference _typeRef_1 = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("java.lang.Object");
                  JvmTypeReference _typeRef_2 = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("java.util.HashMap", _typeRef, _typeRef_1);
                  JvmFormalParameter _parameter = OceletJvmModelInferrer.this._jvmTypesBuilder.toParameter(modl, "in_params", _typeRef_2);
                  OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                  final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
                    public void apply(final ITreeAppendable it) {
                      boolean _hasParameters = md.hasParameters();
                      if (_hasParameters) {
                        ArrayList<Parameterstuff> _params = md.getParams();
                        for (final Parameterstuff pstuff : _params) {
                          {
                            StringConcatenation _builder = new StringConcatenation();
                            JvmTypeReference _type = pstuff.getType();
                            String _simpleName = _type.getSimpleName();
                            _builder.append(_simpleName, "");
                            _builder.append(" val_");
                            String _name = pstuff.getName();
                            _builder.append(_name, "");
                            _builder.append(" = (");
                            JvmTypeReference _type_1 = pstuff.getType();
                            String _simpleName_1 = _type_1.getSimpleName();
                            _builder.append(_simpleName_1, "");
                            _builder.append(") in_params.get(\"");
                            String _name_1 = pstuff.getName();
                            _builder.append(_name_1, "");
                            _builder.append("\");");
                            it.append(_builder);
                            it.newLine();
                            StringConcatenation _builder_1 = new StringConcatenation();
                            _builder_1.append("if (val_");
                            String _name_2 = pstuff.getName();
                            _builder_1.append(_name_2, "");
                            _builder_1.append(" != null) ");
                            String _name_3 = pstuff.getName();
                            _builder_1.append(_name_3, "");
                            _builder_1.append(" = val_");
                            String _name_4 = pstuff.getName();
                            _builder_1.append(_name_4, "");
                            _builder_1.append(";");
                            it.append(_builder_1);
                            it.newLine();
                          }
                        }
                      }
                      StringConcatenation _builder = new StringConcatenation();
                      _builder.append("run_");
                      _builder.append(modlName, "");
                      _builder.append("();");
                      it.append(_builder);
                    }
                  };
                  OceletJvmModelInferrer.this._jvmTypesBuilder.setBody(it, _function);
                }
              };
              JvmOperation _method_2 = OceletJvmModelInferrer.this._jvmTypesBuilder.toMethod(modl, "simulate", _typeRef_3, _function_3);
              OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmOperation>operator_add(_members_3, _method_2);
            } else {
              EList<JvmMember> _members_4 = it.getMembers();
              String _name_1 = scen.getName();
              JvmTypeReference _typeRef_4 = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef(Void.TYPE);
              final Procedure1<JvmOperation> _function_4 = new Procedure1<JvmOperation>() {
                public void apply(final JvmOperation it) {
                  XExpression _sccode = scen.getSccode();
                  OceletJvmModelInferrer.this._jvmTypesBuilder.setBody(it, _sccode);
                }
              };
              JvmOperation _method_3 = OceletJvmModelInferrer.this._jvmTypesBuilder.toMethod(scen, _name_1, _typeRef_4, _function_4);
              OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmOperation>operator_add(_members_4, _method_3);
            }
          }
          boolean _hasParameters = md.hasParameters();
          if (_hasParameters) {
            ArrayList<Parameterstuff> _params = md.getParams();
            for (final Parameterstuff pstuff : _params) {
              {
                JvmField jvmField = OceletJvmModelInferrer.this._jvmTypesBuilder.toField(modl, pstuff.name, pstuff.type);
                boolean _notEquals = (!Objects.equal(jvmField, null));
                if (_notEquals) {
                  jvmField.setFinal(false);
                  EList<JvmMember> _members_5 = it.getMembers();
                  OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmField>operator_add(_members_5, jvmField);
                  EList<JvmMember> _members_6 = it.getMembers();
                  JvmOperation _setter = OceletJvmModelInferrer.this._jvmTypesBuilder.toSetter(modl, pstuff.name, pstuff.type);
                  OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmOperation>operator_add(_members_6, _setter);
                  EList<JvmMember> _members_7 = it.getMembers();
                  JvmOperation _getter = OceletJvmModelInferrer.this._jvmTypesBuilder.toGetter(modl, pstuff.name, pstuff.type);
                  OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmOperation>operator_add(_members_7, _getter);
                }
              }
            }
          }
        }
      };
      acceptor.<JvmGenericType>accept(_class, _function);
    }
  }
  
  public void infer(final EObject modl, final IJvmDeclaredTypeAcceptor acceptor, final boolean isPreIndexingPhase) {
    if (modl instanceof Model) {
      _infer((Model)modl, acceptor, isPreIndexingPhase);
      return;
    } else if (modl != null) {
      _infer(modl, acceptor, isPreIndexingPhase);
      return;
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(modl, acceptor, isPreIndexingPhase).toString());
    }
  }
}
