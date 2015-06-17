package fr.ocelet.lang.jvmmodel;

import com.google.common.base.Objects;
import com.google.inject.Inject;
import fr.ocelet.lang.jvmmodel.Metadatastuff;
import fr.ocelet.lang.jvmmodel.OceletCompiler;
import fr.ocelet.lang.jvmmodel.Parameterstuff;
import fr.ocelet.lang.ocelet.ConstructorDef;
import fr.ocelet.lang.ocelet.Datafacer;
import fr.ocelet.lang.ocelet.Entity;
import fr.ocelet.lang.ocelet.EntityElements;
import fr.ocelet.lang.ocelet.Match;
import fr.ocelet.lang.ocelet.Matchtype;
import fr.ocelet.lang.ocelet.Mdef;
import fr.ocelet.lang.ocelet.Metadata;
import fr.ocelet.lang.ocelet.ModEln;
import fr.ocelet.lang.ocelet.Model;
import fr.ocelet.lang.ocelet.Paradesc;
import fr.ocelet.lang.ocelet.Paramdefa;
import fr.ocelet.lang.ocelet.Parameter;
import fr.ocelet.lang.ocelet.Parampart;
import fr.ocelet.lang.ocelet.Paramunit;
import fr.ocelet.lang.ocelet.Paraopt;
import fr.ocelet.lang.ocelet.PropertyDef;
import fr.ocelet.lang.ocelet.Rangevals;
import fr.ocelet.lang.ocelet.Scenario;
import fr.ocelet.lang.ocelet.ServiceDef;
import fr.ocelet.lang.ocelet.StrucEln;
import fr.ocelet.lang.ocelet.StrucFuncDef;
import fr.ocelet.lang.ocelet.StrucVarDef;
import fr.ocelet.lang.ocelet.Strucdef;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtend2.lib.StringConcatenationClient;
import org.eclipse.xtext.common.types.JvmConstructor;
import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmFormalParameter;
import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.common.types.JvmMember;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmTypeParameter;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.common.types.TypesFactory;
import org.eclipse.xtext.common.types.util.Primitives;
import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.xbase.XExpression;
import org.eclipse.xtext.xbase.compiler.output.ITreeAppendable;
import org.eclipse.xtext.xbase.jvmmodel.AbstractModelInferrer;
import org.eclipse.xtext.xbase.jvmmodel.IJvmDeclaredTypeAcceptor;
import org.eclipse.xtext.xbase.jvmmodel.JvmTypesBuilder;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.StringExtensions;

@SuppressWarnings("all")
public class OceletJvmModelInferrer extends AbstractModelInferrer {
  @Inject
  @Extension
  private JvmTypesBuilder _jvmTypesBuilder;
  
  @Inject
  @Extension
  private IQualifiedNameProvider _iQualifiedNameProvider;
  
  @Inject
  private OceletCompiler ocltCompiler;
  
  @Inject
  @Extension
  private Primitives _primitives;
  
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
            EList<Parameter> _paramdefs = ((Metadata)meln).getParamdefs();
            boolean _notEquals = (!Objects.equal(_paramdefs, null));
            if (_notEquals) {
              String _desc = ((Metadata)meln).getDesc();
              md.setModeldesc(_desc);
              String _webp = ((Metadata)meln).getWebp();
              md.setWebpage(_webp);
              EList<Parameter> _paramdefs_1 = ((Metadata)meln).getParamdefs();
              for (final Parameter paramdef : _paramdefs_1) {
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
        }
        if (!_matched) {
          if (meln instanceof Datafacer) {
            _matched=true;
            QualifiedName _fullyQualifiedName = this._iQualifiedNameProvider.getFullyQualifiedName(meln);
            JvmGenericType _class = this._jvmTypesBuilder.toClass(modl, _fullyQualifiedName);
            final Procedure1<JvmGenericType> _function = new Procedure1<JvmGenericType>() {
              @Override
              public void apply(final JvmGenericType it) {
                try {
                  EList<JvmTypeReference> _superTypes = it.getSuperTypes();
                  String _storetype = ((Datafacer)meln).getStoretype();
                  String _plus = ("fr.ocelet.datafacer.ocltypes." + _storetype);
                  JvmTypeReference _typeRef = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef(_plus);
                  OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes, _typeRef);
                  EList<JvmMember> _members = it.getMembers();
                  final Procedure1<JvmConstructor> _function = new Procedure1<JvmConstructor>() {
                    @Override
                    public void apply(final JvmConstructor it) {
                      final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
                        @Override
                        public void apply(final ITreeAppendable it) {
                          StringConcatenation _builder = new StringConcatenation();
                          _builder.append("super(");
                          it.append(_builder);
                          int carg = 0;
                          EList<XExpression> _arguments = ((Datafacer)meln).getArguments();
                          for (final XExpression arg : _arguments) {
                            {
                              if ((carg > 0)) {
                                StringConcatenation _builder_1 = new StringConcatenation();
                                _builder_1.append(",");
                                it.append(_builder_1);
                              }
                              OceletJvmModelInferrer.this.ocltCompiler.compileDatafacerParamExpression(arg, it);
                              carg = (carg + 1);
                            }
                          }
                          StringConcatenation _builder_1 = new StringConcatenation();
                          _builder_1.append(");");
                          it.append(_builder_1);
                        }
                      };
                      OceletJvmModelInferrer.this._jvmTypesBuilder.setBody(it, _function);
                    }
                  };
                  JvmConstructor _constructor = OceletJvmModelInferrer.this._jvmTypesBuilder.toConstructor(meln, _function);
                  OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmConstructor>operator_add(_members, _constructor);
                  boolean isFirst = true;
                  EList<Match> _matchbox = ((Datafacer)meln).getMatchbox();
                  for (final Match matchdef : _matchbox) {
                    {
                      final Matchtype mt = matchdef.getMtype();
                      boolean _notEquals = (!Objects.equal(mt, null));
                      if (_notEquals) {
                        boolean _matched = false;
                        if (!_matched) {
                          if (mt instanceof Entity) {
                            _matched=true;
                            QualifiedName _fullyQualifiedName = OceletJvmModelInferrer.this._iQualifiedNameProvider.getFullyQualifiedName(mt);
                            String _string = _fullyQualifiedName.toString();
                            final JvmTypeReference entype = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef(_string);
                            String _name = ((Entity)mt).getName();
                            final String entname = StringExtensions.toFirstUpper(_name);
                            final JvmTypeReference listype = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.ocltypes.List", entype);
                            final HashMap<String, String> propmap = new HashMap<String, String>();
                            final HashMap<String, String> propmapf = new HashMap<String, String>();
                            EList<EntityElements> _entelns = ((Entity)mt).getEntelns();
                            for (final EntityElements eprop : _entelns) {
                              boolean _matched_1 = false;
                              if (!_matched_1) {
                                if (eprop instanceof PropertyDef) {
                                  _matched_1=true;
                                  JvmTypeReference _type = ((PropertyDef)eprop).getType();
                                  boolean _notEquals_1 = (!Objects.equal(_type, null));
                                  if (_notEquals_1) {
                                    String _name_1 = ((PropertyDef)eprop).getName();
                                    JvmTypeReference _type_1 = ((PropertyDef)eprop).getType();
                                    String _simpleName = _type_1.getSimpleName();
                                    propmap.put(_name_1, _simpleName);
                                    String _name_2 = ((PropertyDef)eprop).getName();
                                    JvmTypeReference _type_2 = ((PropertyDef)eprop).getType();
                                    String _qualifiedName = _type_2.getQualifiedName();
                                    propmapf.put(_name_2, _qualifiedName);
                                  }
                                }
                              }
                            }
                            Class<?> _forName = Class.forName("fr.ocelet.datafacer.InputDatafacer");
                            String _storetype_1 = ((Datafacer)meln).getStoretype();
                            String _plus_1 = ("fr.ocelet.datafacer.ocltypes." + _storetype_1);
                            Class<?> _forName_1 = Class.forName(_plus_1);
                            boolean _isAssignableFrom = _forName.isAssignableFrom(_forName_1);
                            if (_isAssignableFrom) {
                              final JvmTypeReference inputRecordType = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("fr.ocelet.datafacer.InputDataRecord");
                              EList<JvmMember> _members_1 = it.getMembers();
                              final Procedure1<JvmOperation> _function_1 = new Procedure1<JvmOperation>() {
                                @Override
                                public void apply(final JvmOperation it) {
                                  final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
                                    @Override
                                    public void apply(final ITreeAppendable it) {
                                      StringConcatenation _builder = new StringConcatenation();
                                      _builder.append("List<");
                                      _builder.append(entname, "");
                                      _builder.append("> _elist = new List<");
                                      _builder.append(entname, "");
                                      _builder.append(">();");
                                      it.append(_builder);
                                      it.newLine();
                                      StringConcatenation _builder_1 = new StringConcatenation();
                                      _builder_1.append("for (");
                                      String _simpleName = inputRecordType.getSimpleName();
                                      _builder_1.append(_simpleName, "");
                                      _builder_1.append(" _record : this) {");
                                      it.append(_builder_1);
                                      it.newLine();
                                      StringConcatenation _builder_2 = new StringConcatenation();
                                      _builder_2.append("  ");
                                      _builder_2.append("_elist.add(create");
                                      _builder_2.append(entname, "  ");
                                      _builder_2.append("FromRecord(_record));");
                                      it.append(_builder_2);
                                      StringConcatenation _builder_3 = new StringConcatenation();
                                      _builder_3.append("}");
                                      it.append(_builder_3);
                                      it.newLine();
                                      StringConcatenation _builder_4 = new StringConcatenation();
                                      _builder_4.append("close();");
                                      it.append(_builder_4);
                                      it.newLine();
                                      StringConcatenation _builder_5 = new StringConcatenation();
                                      _builder_5.append("return _elist;");
                                      it.append(_builder_5);
                                    }
                                  };
                                  OceletJvmModelInferrer.this._jvmTypesBuilder.setBody(it, _function);
                                }
                              };
                              JvmOperation _method = OceletJvmModelInferrer.this._jvmTypesBuilder.toMethod(meln, ("readAll" + entname), listype, _function_1);
                              OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmOperation>operator_add(_members_1, _method);
                              if (isFirst) {
                                EList<JvmMember> _members_2 = it.getMembers();
                                final Procedure1<JvmOperation> _function_2 = new Procedure1<JvmOperation>() {
                                  @Override
                                  public void apply(final JvmOperation it) {
                                    final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
                                      @Override
                                      public void apply(final ITreeAppendable it) {
                                        StringConcatenation _builder = new StringConcatenation();
                                        _builder.append("return readAll");
                                        _builder.append(entname, "");
                                        _builder.append("();");
                                        it.append(_builder);
                                      }
                                    };
                                    OceletJvmModelInferrer.this._jvmTypesBuilder.setBody(it, _function);
                                  }
                                };
                                JvmOperation _method_1 = OceletJvmModelInferrer.this._jvmTypesBuilder.toMethod(meln, "readAll", listype, _function_2);
                                OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmOperation>operator_add(_members_2, _method_1);
                              }
                              EList<JvmMember> _members_3 = it.getMembers();
                              final Procedure1<JvmOperation> _function_3 = new Procedure1<JvmOperation>() {
                                @Override
                                public void apply(final JvmOperation it) {
                                  EList<JvmFormalParameter> _parameters = it.getParameters();
                                  JvmTypeReference _typeRef = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("fr.ocelet.datafacer.InputDataRecord");
                                  JvmFormalParameter _parameter = OceletJvmModelInferrer.this._jvmTypesBuilder.toParameter(meln, "_rec", _typeRef);
                                  OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                                  final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
                                    @Override
                                    public void apply(final ITreeAppendable it) {
                                      StringConcatenation _builder = new StringConcatenation();
                                      _builder.append(entname, "");
                                      _builder.append(" _entity = new ");
                                      _builder.append(entname, "");
                                      _builder.append("();");
                                      it.append(_builder);
                                      it.newLine();
                                      EList<Mdef> _matchprops = matchdef.getMatchprops();
                                      for (final Mdef mp : _matchprops) {
                                        {
                                          String _prop = mp.getProp();
                                          final String eproptype = propmap.get(_prop);
                                          boolean _notEquals = (!Objects.equal(eproptype, null));
                                          if (_notEquals) {
                                            String _colname = mp.getColname();
                                            boolean _notEquals_1 = (!Objects.equal(_colname, null));
                                            if (_notEquals_1) {
                                              StringConcatenation _builder_1 = new StringConcatenation();
                                              _builder_1.append("_entity.setProperty(\"");
                                              String _prop_1 = mp.getProp();
                                              _builder_1.append(_prop_1, "");
                                              _builder_1.append("\",read");
                                              _builder_1.append(eproptype, "");
                                              _builder_1.append("(\"");
                                              String _colname_1 = mp.getColname();
                                              _builder_1.append(_colname_1, "");
                                              _builder_1.append("\"));");
                                              it.append(_builder_1);
                                            }
                                            it.newLine();
                                          }
                                        }
                                      }
                                      StringConcatenation _builder_1 = new StringConcatenation();
                                      _builder_1.append("return _entity;");
                                      it.append(_builder_1);
                                    }
                                  };
                                  OceletJvmModelInferrer.this._jvmTypesBuilder.setBody(it, _function);
                                }
                              };
                              JvmOperation _method_2 = OceletJvmModelInferrer.this._jvmTypesBuilder.toMethod(meln, (("create" + entname) + "FromRecord"), entype, _function_3);
                              OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmOperation>operator_add(_members_3, _method_2);
                              JvmTypeReference _typeRef_1 = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("java.lang.String");
                              JvmTypeReference _typeRef_2 = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("java.lang.String");
                              final JvmTypeReference hmtype = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("java.util.HashMap", _typeRef_1, _typeRef_2);
                              EList<JvmMember> _members_4 = it.getMembers();
                              final Procedure1<JvmOperation> _function_4 = new Procedure1<JvmOperation>() {
                                @Override
                                public void apply(final JvmOperation it) {
                                  final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
                                    @Override
                                    public void apply(final ITreeAppendable it) {
                                      StringConcatenation _builder = new StringConcatenation();
                                      String _simpleName = hmtype.getSimpleName();
                                      _builder.append(_simpleName, "");
                                      _builder.append(" hm = new ");
                                      String _simpleName_1 = hmtype.getSimpleName();
                                      _builder.append(_simpleName_1, "");
                                      _builder.append("();");
                                      it.append(_builder);
                                      it.newLine();
                                      EList<Mdef> _matchprops = matchdef.getMatchprops();
                                      for (final Mdef mp : _matchprops) {
                                        {
                                          String _prop = mp.getProp();
                                          final String epropftype = propmapf.get(_prop);
                                          boolean _notEquals = (!Objects.equal(epropftype, null));
                                          if (_notEquals) {
                                            String _colname = mp.getColname();
                                            boolean _notEquals_1 = (!Objects.equal(_colname, null));
                                            if (_notEquals_1) {
                                              StringConcatenation _builder_1 = new StringConcatenation();
                                              _builder_1.append("hm.put(\"");
                                              String _colname_1 = mp.getColname();
                                              _builder_1.append(_colname_1, "");
                                              _builder_1.append("\",\"");
                                              _builder_1.append(epropftype, "");
                                              _builder_1.append("\");");
                                              it.append(_builder_1);
                                            }
                                            it.newLine();
                                          }
                                        }
                                      }
                                      StringConcatenation _builder_1 = new StringConcatenation();
                                      _builder_1.append("return hm;");
                                      it.append(_builder_1);
                                    }
                                  };
                                  OceletJvmModelInferrer.this._jvmTypesBuilder.setBody(it, _function);
                                }
                              };
                              JvmOperation _method_3 = OceletJvmModelInferrer.this._jvmTypesBuilder.toMethod(meln, "getMatchdef", hmtype, _function_4);
                              OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmOperation>operator_add(_members_4, _method_3);
                            }
                            Class<?> _forName_2 = Class.forName("fr.ocelet.datafacer.FiltrableDatafacer");
                            String _storetype_2 = ((Datafacer)meln).getStoretype();
                            String _plus_2 = ("fr.ocelet.datafacer.ocltypes." + _storetype_2);
                            Class<?> _forName_3 = Class.forName(_plus_2);
                            boolean _isAssignableFrom_1 = _forName_2.isAssignableFrom(_forName_3);
                            if (_isAssignableFrom_1) {
                              EList<JvmMember> _members_5 = it.getMembers();
                              final Procedure1<JvmOperation> _function_5 = new Procedure1<JvmOperation>() {
                                @Override
                                public void apply(final JvmOperation it) {
                                  EList<JvmFormalParameter> _parameters = it.getParameters();
                                  JvmTypeReference _typeRef = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("java.lang.String");
                                  JvmFormalParameter _parameter = OceletJvmModelInferrer.this._jvmTypesBuilder.toParameter(meln, "_filt", _typeRef);
                                  OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                                  final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
                                    @Override
                                    public void apply(final ITreeAppendable it) {
                                      StringConcatenation _builder = new StringConcatenation();
                                      _builder.append("setFilter(_filt);");
                                      it.append(_builder);
                                      it.newLine();
                                      StringConcatenation _builder_1 = new StringConcatenation();
                                      _builder_1.append("return readAll");
                                      _builder_1.append(entname, "");
                                      _builder_1.append("();");
                                      it.append(_builder_1);
                                    }
                                  };
                                  OceletJvmModelInferrer.this._jvmTypesBuilder.setBody(it, _function);
                                }
                              };
                              JvmOperation _method_4 = OceletJvmModelInferrer.this._jvmTypesBuilder.toMethod(meln, ("readFiltered" + entname), listype, _function_5);
                              OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmOperation>operator_add(_members_5, _method_4);
                            }
                            Class<?> _forName_4 = Class.forName("fr.ocelet.datafacer.OutputDatafacer");
                            String _storetype_3 = ((Datafacer)meln).getStoretype();
                            String _plus_3 = ("fr.ocelet.datafacer.ocltypes." + _storetype_3);
                            Class<?> _forName_5 = Class.forName(_plus_3);
                            boolean _isAssignableFrom_2 = _forName_4.isAssignableFrom(_forName_5);
                            if (_isAssignableFrom_2) {
                              EList<JvmMember> _members_6 = it.getMembers();
                              JvmTypeReference _typeRef_3 = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("fr.ocelet.datafacer.OutputDataRecord");
                              final Procedure1<JvmOperation> _function_6 = new Procedure1<JvmOperation>() {
                                @Override
                                public void apply(final JvmOperation it) {
                                  EList<JvmFormalParameter> _parameters = it.getParameters();
                                  JvmTypeReference _typeRef = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.entity.Entity");
                                  JvmFormalParameter _parameter = OceletJvmModelInferrer.this._jvmTypesBuilder.toParameter(meln, "ety", _typeRef);
                                  OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                                  final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
                                    @Override
                                    public void apply(final ITreeAppendable it) {
                                      final JvmTypeReference odrtype = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("fr.ocelet.datafacer.OutputDataRecord");
                                      StringConcatenation _builder = new StringConcatenation();
                                      String _simpleName = odrtype.getSimpleName();
                                      _builder.append(_simpleName, "");
                                      _builder.append(" odr = createOutputDataRec();");
                                      it.append(_builder);
                                      it.newLine();
                                      StringConcatenation _builder_1 = new StringConcatenation();
                                      _builder_1.append("if (odr != null) {");
                                      it.append(_builder_1);
                                      it.newLine();
                                      EList<Mdef> _matchprops = matchdef.getMatchprops();
                                      for (final Mdef mp : _matchprops) {
                                        {
                                          StringConcatenation _builder_2 = new StringConcatenation();
                                          _builder_2.append("odr.setAttribute(\"");
                                          String _colname = mp.getColname();
                                          _builder_2.append(_colname, "");
                                          _builder_2.append("\",((");
                                          _builder_2.append(entname, "");
                                          _builder_2.append(") ety).get");
                                          String _prop = mp.getProp();
                                          String _firstUpper = StringExtensions.toFirstUpper(_prop);
                                          _builder_2.append(_firstUpper, "");
                                          _builder_2.append("());");
                                          it.append(_builder_2);
                                          it.newLine();
                                        }
                                      }
                                      StringConcatenation _builder_2 = new StringConcatenation();
                                      _builder_2.append("}");
                                      it.append(_builder_2);
                                      it.newLine();
                                      StringConcatenation _builder_3 = new StringConcatenation();
                                      _builder_3.append("return odr;");
                                      it.append(_builder_3);
                                    }
                                  };
                                  OceletJvmModelInferrer.this._jvmTypesBuilder.setBody(it, _function);
                                }
                              };
                              JvmOperation _method_5 = OceletJvmModelInferrer.this._jvmTypesBuilder.toMethod(meln, "createRecord", _typeRef_3, _function_6);
                              OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmOperation>operator_add(_members_6, _method_5);
                            }
                          }
                        }
                      }
                      isFirst = false;
                    }
                  }
                } catch (Throwable _e) {
                  throw Exceptions.sneakyThrow(_e);
                }
              }
            };
            acceptor.<JvmGenericType>accept(_class, _function);
          }
        }
        if (!_matched) {
          if (meln instanceof Entity) {
            _matched=true;
            QualifiedName _fullyQualifiedName = this._iQualifiedNameProvider.getFullyQualifiedName(meln);
            JvmGenericType _class = this._jvmTypesBuilder.toClass(modl, _fullyQualifiedName);
            final Procedure1<JvmGenericType> _function = new Procedure1<JvmGenericType>() {
              @Override
              public void apply(final JvmGenericType it) {
                String _documentation = OceletJvmModelInferrer.this._jvmTypesBuilder.getDocumentation(meln);
                OceletJvmModelInferrer.this._jvmTypesBuilder.setDocumentation(it, _documentation);
                EList<JvmTypeReference> _superTypes = it.getSuperTypes();
                JvmTypeReference _typeRef = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.entity.AbstractEntity");
                OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes, _typeRef);
                final List<PropertyDef> lpropdefs = CollectionLiterals.<PropertyDef>newArrayList();
                EList<EntityElements> _entelns = ((Entity)meln).getEntelns();
                for (final EntityElements enteln : _entelns) {
                  boolean _matched = false;
                  if (!_matched) {
                    if (enteln instanceof PropertyDef) {
                      _matched=true;
                      String _name = ((PropertyDef)enteln).getName();
                      boolean _notEquals = (!Objects.equal(_name, null));
                      if (_notEquals) {
                        lpropdefs.add(((PropertyDef)enteln));
                        EList<JvmMember> _members = it.getMembers();
                        String _name_1 = ((PropertyDef)enteln).getName();
                        String _firstUpper = StringExtensions.toFirstUpper(_name_1);
                        String _plus = ("set" + _firstUpper);
                        JvmTypeReference _typeRef_1 = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef(Void.TYPE);
                        final Procedure1<JvmOperation> _function = new Procedure1<JvmOperation>() {
                          @Override
                          public void apply(final JvmOperation it) {
                            String _documentation = OceletJvmModelInferrer.this._jvmTypesBuilder.getDocumentation(enteln);
                            OceletJvmModelInferrer.this._jvmTypesBuilder.setDocumentation(it, _documentation);
                            final String parName = ((PropertyDef)enteln).getName();
                            EList<JvmFormalParameter> _parameters = it.getParameters();
                            JvmTypeReference _type = ((PropertyDef)enteln).getType();
                            JvmFormalParameter _parameter = OceletJvmModelInferrer.this._jvmTypesBuilder.toParameter(enteln, parName, _type);
                            OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                            StringConcatenationClient _client = new StringConcatenationClient() {
                              @Override
                              protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                _builder.append("setProperty(\"");
                                String _name = ((PropertyDef)enteln).getName();
                                _builder.append(_name, "");
                                _builder.append("\",");
                                _builder.append(parName, "");
                                _builder.append(");");
                              }
                            };
                            OceletJvmModelInferrer.this._jvmTypesBuilder.setBody(it, _client);
                          }
                        };
                        JvmOperation _method = OceletJvmModelInferrer.this._jvmTypesBuilder.toMethod(enteln, _plus, _typeRef_1, _function);
                        OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmOperation>operator_add(_members, _method);
                        EList<JvmMember> _members_1 = it.getMembers();
                        String _name_2 = ((PropertyDef)enteln).getName();
                        String _firstUpper_1 = StringExtensions.toFirstUpper(_name_2);
                        String _plus_1 = ("get" + _firstUpper_1);
                        JvmTypeReference _type = ((PropertyDef)enteln).getType();
                        final Procedure1<JvmOperation> _function_1 = new Procedure1<JvmOperation>() {
                          @Override
                          public void apply(final JvmOperation it) {
                            String _documentation = OceletJvmModelInferrer.this._jvmTypesBuilder.getDocumentation(enteln);
                            OceletJvmModelInferrer.this._jvmTypesBuilder.setDocumentation(it, _documentation);
                            StringConcatenationClient _client = new StringConcatenationClient() {
                              @Override
                              protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                _builder.append("return getProperty(\"");
                                String _name = ((PropertyDef)enteln).getName();
                                _builder.append(_name, "");
                                _builder.append("\");");
                              }
                            };
                            OceletJvmModelInferrer.this._jvmTypesBuilder.setBody(it, _client);
                          }
                        };
                        JvmOperation _method_1 = OceletJvmModelInferrer.this._jvmTypesBuilder.toMethod(enteln, _plus_1, _type, _function_1);
                        OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmOperation>operator_add(_members_1, _method_1);
                      }
                    }
                  }
                  if (!_matched) {
                    if (enteln instanceof ServiceDef) {
                      _matched=true;
                      JvmTypeReference rtype = ((ServiceDef)enteln).getType();
                      boolean _equals = Objects.equal(rtype, null);
                      if (_equals) {
                        JvmTypeReference _typeRef_1 = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef(Void.TYPE);
                        rtype = _typeRef_1;
                      }
                      EList<JvmMember> _members = it.getMembers();
                      String _name = ((ServiceDef)enteln).getName();
                      final Procedure1<JvmOperation> _function = new Procedure1<JvmOperation>() {
                        @Override
                        public void apply(final JvmOperation it) {
                          String _documentation = OceletJvmModelInferrer.this._jvmTypesBuilder.getDocumentation(enteln);
                          OceletJvmModelInferrer.this._jvmTypesBuilder.setDocumentation(it, _documentation);
                          EList<JvmFormalParameter> _params = ((ServiceDef)enteln).getParams();
                          for (final JvmFormalParameter p : _params) {
                            EList<JvmFormalParameter> _parameters = it.getParameters();
                            String _name = p.getName();
                            JvmTypeReference _parameterType = p.getParameterType();
                            JvmFormalParameter _parameter = OceletJvmModelInferrer.this._jvmTypesBuilder.toParameter(p, _name, _parameterType);
                            OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                          }
                          XExpression _body = ((ServiceDef)enteln).getBody();
                          OceletJvmModelInferrer.this._jvmTypesBuilder.setBody(it, _body);
                        }
                      };
                      JvmOperation _method = OceletJvmModelInferrer.this._jvmTypesBuilder.toMethod(enteln, _name, rtype, _function);
                      OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmOperation>operator_add(_members, _method);
                    }
                  }
                  if (!_matched) {
                    if (enteln instanceof ConstructorDef) {
                      _matched=true;
                      EList<JvmMember> _members = it.getMembers();
                      String _name = ((ConstructorDef)enteln).getName();
                      QualifiedName _fullyQualifiedName = OceletJvmModelInferrer.this._iQualifiedNameProvider.getFullyQualifiedName(meln);
                      String _string = _fullyQualifiedName.toString();
                      JvmTypeReference _typeRef_1 = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef(_string);
                      final Procedure1<JvmOperation> _function = new Procedure1<JvmOperation>() {
                        @Override
                        public void apply(final JvmOperation it) {
                          it.setStatic(true);
                          String _documentation = OceletJvmModelInferrer.this._jvmTypesBuilder.getDocumentation(enteln);
                          OceletJvmModelInferrer.this._jvmTypesBuilder.setDocumentation(it, _documentation);
                          EList<JvmFormalParameter> _params = ((ConstructorDef)enteln).getParams();
                          for (final JvmFormalParameter p : _params) {
                            EList<JvmFormalParameter> _parameters = it.getParameters();
                            String _name = p.getName();
                            JvmTypeReference _parameterType = p.getParameterType();
                            JvmFormalParameter _parameter = OceletJvmModelInferrer.this._jvmTypesBuilder.toParameter(p, _name, _parameterType);
                            OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                          }
                          XExpression _body = ((ConstructorDef)enteln).getBody();
                          OceletJvmModelInferrer.this._jvmTypesBuilder.setBody(it, _body);
                        }
                      };
                      JvmOperation _method = OceletJvmModelInferrer.this._jvmTypesBuilder.toMethod(enteln, _name, _typeRef_1, _function);
                      OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmOperation>operator_add(_members, _method);
                    }
                  }
                }
                EList<JvmMember> _members = it.getMembers();
                final Procedure1<JvmConstructor> _function = new Procedure1<JvmConstructor>() {
                  @Override
                  public void apply(final JvmConstructor it) {
                    StringConcatenationClient _client = new StringConcatenationClient() {
                      @Override
                      protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                        _builder.append("super();");
                        _builder.newLine();
                        {
                          for(final PropertyDef hprop : lpropdefs) {
                            JvmTypeReference _type = hprop.getType();
                            JvmTypeReference _asWrapperTypeIfPrimitive = OceletJvmModelInferrer.this._primitives.asWrapperTypeIfPrimitive(_type);
                            final JvmTypeReference hhtype = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.entity.Hproperty", _asWrapperTypeIfPrimitive);
                            _builder.newLineIfNotEmpty();
                            _builder.append("defProperty(\"");
                            String _name = hprop.getName();
                            _builder.append(_name, "");
                            _builder.append("\",new ");
                            _builder.append(hhtype, "");
                            _builder.append("());");
                            _builder.newLineIfNotEmpty();
                            JvmTypeReference _type_1 = hprop.getType();
                            final JvmTypeReference vtyp = OceletJvmModelInferrer.this._primitives.asWrapperTypeIfPrimitive(_type_1);
                            _builder.newLineIfNotEmpty();
                            _builder.append("set");
                            String _name_1 = hprop.getName();
                            String _firstUpper = StringExtensions.toFirstUpper(_name_1);
                            _builder.append(_firstUpper, "");
                            _builder.append("(new ");
                            _builder.append(vtyp, "");
                            _builder.newLineIfNotEmpty();
                            {
                              boolean _or = false;
                              boolean _or_1 = false;
                              boolean _or_2 = false;
                              boolean _or_3 = false;
                              boolean _or_4 = false;
                              String _qualifiedName = vtyp.getQualifiedName();
                              boolean _equals = _qualifiedName.equals("java.lang.Integer");
                              if (_equals) {
                                _or_4 = true;
                              } else {
                                String _qualifiedName_1 = vtyp.getQualifiedName();
                                boolean _equals_1 = _qualifiedName_1.equals("java.lang.Double");
                                _or_4 = _equals_1;
                              }
                              if (_or_4) {
                                _or_3 = true;
                              } else {
                                String _qualifiedName_2 = vtyp.getQualifiedName();
                                boolean _equals_2 = _qualifiedName_2.equals("java.lang.Float");
                                _or_3 = _equals_2;
                              }
                              if (_or_3) {
                                _or_2 = true;
                              } else {
                                String _qualifiedName_3 = vtyp.getQualifiedName();
                                boolean _equals_3 = _qualifiedName_3.equals("java.lang.Long");
                                _or_2 = _equals_3;
                              }
                              if (_or_2) {
                                _or_1 = true;
                              } else {
                                String _qualifiedName_4 = vtyp.getQualifiedName();
                                boolean _equals_4 = _qualifiedName_4.equals("java.lang.Byte");
                                _or_1 = _equals_4;
                              }
                              if (_or_1) {
                                _or = true;
                              } else {
                                String _qualifiedName_5 = vtyp.getQualifiedName();
                                boolean _equals_5 = _qualifiedName_5.equals("java.lang.Short");
                                _or = _equals_5;
                              }
                              if (_or) {
                                _builder.append("(\"0\"));");
                                _builder.newLineIfNotEmpty();
                                _builder.append("                    ");
                              } else {
                                String _qualifiedName_6 = vtyp.getQualifiedName();
                                boolean _equals_6 = _qualifiedName_6.equals("java.lang.Boolean");
                                if (_equals_6) {
                                  _builder.append("(false));");
                                  _builder.newLineIfNotEmpty();
                                  _builder.append("                    ");
                                } else {
                                  _builder.append("());");
                                  _builder.newLineIfNotEmpty();
                                }
                              }
                            }
                          }
                        }
                      }
                    };
                    OceletJvmModelInferrer.this._jvmTypesBuilder.setBody(it, _client);
                  }
                };
                JvmConstructor _constructor = OceletJvmModelInferrer.this._jvmTypesBuilder.toConstructor(meln, _function);
                OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmConstructor>operator_add(_members, _constructor);
              }
            };
            acceptor.<JvmGenericType>accept(_class, _function);
          }
        }
        if (!_matched) {
          if (meln instanceof Strucdef) {
            _matched=true;
            QualifiedName _fullyQualifiedName = this._iQualifiedNameProvider.getFullyQualifiedName(meln);
            JvmGenericType _class = this._jvmTypesBuilder.toClass(meln, _fullyQualifiedName);
            final Procedure1<JvmGenericType> _function = new Procedure1<JvmGenericType>() {
              @Override
              public void apply(final JvmGenericType it) {
                String _typeArgument = ((Strucdef)meln).getTypeArgument();
                boolean _notEquals = (!Objects.equal(_typeArgument, null));
                if (_notEquals) {
                  final JvmTypeParameter param = TypesFactory.eINSTANCE.createJvmTypeParameter();
                  String _typeArgument_1 = ((Strucdef)meln).getTypeArgument();
                  param.setName(_typeArgument_1);
                  EList<JvmTypeParameter> _typeParameters = it.getTypeParameters();
                  OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmTypeParameter>operator_add(_typeParameters, param);
                  String _superType = ((Strucdef)meln).getSuperType();
                  boolean _notEquals_1 = (!Objects.equal(_superType, null));
                  if (_notEquals_1) {
                    EList<JvmTypeReference> _superTypes = it.getSuperTypes();
                    String _superType_1 = ((Strucdef)meln).getSuperType();
                    JvmTypeReference _typeRef = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef(param);
                    JvmTypeReference _typeRef_1 = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef(_superType_1, _typeRef);
                    OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes, _typeRef_1);
                  }
                } else {
                  String _superType_2 = ((Strucdef)meln).getSuperType();
                  boolean _notEquals_2 = (!Objects.equal(_superType_2, null));
                  if (_notEquals_2) {
                    EList<JvmTypeReference> _superTypes_1 = it.getSuperTypes();
                    String _superType_3 = ((Strucdef)meln).getSuperType();
                    JvmTypeReference _typeRef_2 = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef(_superType_3);
                    OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes_1, _typeRef_2);
                  }
                }
                final List<StrucVarDef> lvdefs = CollectionLiterals.<StrucVarDef>newArrayList();
                EList<StrucEln> _strucelns = ((Strucdef)meln).getStrucelns();
                for (final StrucEln steln : _strucelns) {
                  boolean _matched = false;
                  if (!_matched) {
                    if (steln instanceof StrucVarDef) {
                      _matched=true;
                      lvdefs.add(((StrucVarDef)steln));
                      String _name = ((StrucVarDef)steln).getName();
                      JvmTypeReference _type = ((StrucVarDef)steln).getType();
                      JvmField jvmField = OceletJvmModelInferrer.this._jvmTypesBuilder.toField(steln, _name, _type);
                      boolean _notEquals_3 = (!Objects.equal(jvmField, null));
                      if (_notEquals_3) {
                        jvmField.setFinal(false);
                        EList<JvmMember> _members = it.getMembers();
                        OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmField>operator_add(_members, jvmField);
                        EList<JvmMember> _members_1 = it.getMembers();
                        String _name_1 = ((StrucVarDef)steln).getName();
                        JvmTypeReference _type_1 = ((StrucVarDef)steln).getType();
                        JvmOperation _setter = OceletJvmModelInferrer.this._jvmTypesBuilder.toSetter(steln, _name_1, _type_1);
                        OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmOperation>operator_add(_members_1, _setter);
                        EList<JvmMember> _members_2 = it.getMembers();
                        String _name_2 = ((StrucVarDef)steln).getName();
                        JvmTypeReference _type_2 = ((StrucVarDef)steln).getType();
                        JvmOperation _getter = OceletJvmModelInferrer.this._jvmTypesBuilder.toGetter(steln, _name_2, _type_2);
                        OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmOperation>operator_add(_members_2, _getter);
                      }
                    }
                  }
                  if (!_matched) {
                    if (steln instanceof StrucFuncDef) {
                      _matched=true;
                      JvmTypeReference _type = ((StrucFuncDef)steln).getType();
                      boolean _equals = Objects.equal(_type, null);
                      if (_equals) {
                        JvmTypeReference _typeRef_3 = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef(Void.TYPE);
                        ((StrucFuncDef)steln).setType(_typeRef_3);
                      }
                      EList<JvmMember> _members = it.getMembers();
                      String _name = ((StrucFuncDef)steln).getName();
                      JvmTypeReference _type_1 = ((StrucFuncDef)steln).getType();
                      final Procedure1<JvmOperation> _function = new Procedure1<JvmOperation>() {
                        @Override
                        public void apply(final JvmOperation it) {
                          String _documentation = OceletJvmModelInferrer.this._jvmTypesBuilder.getDocumentation(steln);
                          OceletJvmModelInferrer.this._jvmTypesBuilder.setDocumentation(it, _documentation);
                          EList<JvmFormalParameter> _params = ((StrucFuncDef)steln).getParams();
                          for (final JvmFormalParameter p : _params) {
                            EList<JvmFormalParameter> _parameters = it.getParameters();
                            String _name = p.getName();
                            JvmTypeReference _parameterType = p.getParameterType();
                            JvmFormalParameter _parameter = OceletJvmModelInferrer.this._jvmTypesBuilder.toParameter(p, _name, _parameterType);
                            OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                          }
                          XExpression _body = ((StrucFuncDef)steln).getBody();
                          OceletJvmModelInferrer.this._jvmTypesBuilder.setBody(it, _body);
                        }
                      };
                      JvmOperation _method = OceletJvmModelInferrer.this._jvmTypesBuilder.toMethod(steln, _name, _type_1, _function);
                      OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmOperation>operator_add(_members, _method);
                    }
                  }
                }
                EList<JvmMember> _members = it.getMembers();
                final Procedure1<JvmConstructor> _function = new Procedure1<JvmConstructor>() {
                  @Override
                  public void apply(final JvmConstructor it) {
                    final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
                      @Override
                      public void apply(final ITreeAppendable it) {
                        StringConcatenation _builder = new StringConcatenation();
                        _builder.append("super();");
                        it.append(_builder);
                        for (final StrucVarDef vardef : lvdefs) {
                          {
                            JvmTypeReference vtyp = vardef.getType();
                            boolean _isPrimitive = OceletJvmModelInferrer.this._primitives.isPrimitive(vtyp);
                            boolean _not = (!_isPrimitive);
                            if (_not) {
                              it.newLine();
                              StringConcatenation _builder_1 = new StringConcatenation();
                              String _name = vardef.getName();
                              _builder_1.append(_name, "");
                              _builder_1.append(" = new ");
                              String _simpleName = vtyp.getSimpleName();
                              _builder_1.append(_simpleName, "");
                              it.append(_builder_1);
                              boolean _or = false;
                              boolean _or_1 = false;
                              boolean _or_2 = false;
                              boolean _or_3 = false;
                              boolean _or_4 = false;
                              String _qualifiedName = vtyp.getQualifiedName();
                              boolean _equals = _qualifiedName.equals("java.lang.Integer");
                              if (_equals) {
                                _or_4 = true;
                              } else {
                                String _qualifiedName_1 = vtyp.getQualifiedName();
                                boolean _equals_1 = _qualifiedName_1.equals("java.lang.Double");
                                _or_4 = _equals_1;
                              }
                              if (_or_4) {
                                _or_3 = true;
                              } else {
                                String _qualifiedName_2 = vtyp.getQualifiedName();
                                boolean _equals_2 = _qualifiedName_2.equals("java.lang.Float");
                                _or_3 = _equals_2;
                              }
                              if (_or_3) {
                                _or_2 = true;
                              } else {
                                String _qualifiedName_3 = vtyp.getQualifiedName();
                                boolean _equals_3 = _qualifiedName_3.equals("java.lang.Long");
                                _or_2 = _equals_3;
                              }
                              if (_or_2) {
                                _or_1 = true;
                              } else {
                                String _qualifiedName_4 = vtyp.getQualifiedName();
                                boolean _equals_4 = _qualifiedName_4.equals("java.lang.Byte");
                                _or_1 = _equals_4;
                              }
                              if (_or_1) {
                                _or = true;
                              } else {
                                String _qualifiedName_5 = vtyp.getQualifiedName();
                                boolean _equals_5 = _qualifiedName_5.equals("java.lang.Short");
                                _or = _equals_5;
                              }
                              if (_or) {
                                StringConcatenation _builder_2 = new StringConcatenation();
                                _builder_2.append("(\"0\");");
                                it.append(_builder_2);
                              } else {
                                String _qualifiedName_6 = vtyp.getQualifiedName();
                                boolean _equals_6 = _qualifiedName_6.equals("java.lang.Boolean");
                                if (_equals_6) {
                                  StringConcatenation _builder_3 = new StringConcatenation();
                                  _builder_3.append("(false);");
                                  it.append(_builder_3);
                                } else {
                                  StringConcatenation _builder_4 = new StringConcatenation();
                                  _builder_4.append("();");
                                  it.append(_builder_4);
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
                JvmConstructor _constructor = OceletJvmModelInferrer.this._jvmTypesBuilder.toConstructor(meln, _function);
                OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmConstructor>operator_add(_members, _constructor);
              }
            };
            acceptor.<JvmGenericType>accept(_class, _function);
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
        @Override
        public void apply(final JvmGenericType it) {
          String _documentation = OceletJvmModelInferrer.this._jvmTypesBuilder.getDocumentation(modl);
          OceletJvmModelInferrer.this._jvmTypesBuilder.setDocumentation(it, _documentation);
          EList<JvmTypeReference> _superTypes = it.getSuperTypes();
          JvmTypeReference _typeRef = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.model.AbstractModel");
          OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes, _typeRef);
          EList<JvmMember> _members = it.getMembers();
          final Procedure1<JvmConstructor> _function = new Procedure1<JvmConstructor>() {
            @Override
            public void apply(final JvmConstructor it) {
              StringConcatenationClient _client = new StringConcatenationClient() {
                @Override
                protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                  _builder.append("super(\"");
                  _builder.append(modlName, "");
                  _builder.append("\");");
                  _builder.newLineIfNotEmpty();
                  {
                    String _modeldesc = md.getModeldesc();
                    boolean _notEquals = (!Objects.equal(_modeldesc, null));
                    if (_notEquals) {
                      _builder.append("modDescription = \"");
                      String _modeldesc_1 = md.getModeldesc();
                      _builder.append(_modeldesc_1, "");
                      _builder.append("\";");
                    }
                  }
                  _builder.newLineIfNotEmpty();
                  {
                    String _webpage = md.getWebpage();
                    boolean _notEquals_1 = (!Objects.equal(_webpage, null));
                    if (_notEquals_1) {
                      _builder.append("modelWebPage = \"");
                      String _webpage_1 = md.getWebpage();
                      _builder.append(_webpage_1, "");
                      _builder.append("\";");
                    }
                  }
                  _builder.newLineIfNotEmpty();
                  {
                    boolean _hasParameters = md.hasParameters();
                    if (_hasParameters) {
                      {
                        ArrayList<Parameterstuff> _params = md.getParams();
                        for(final Parameterstuff pstuff : _params) {
                          JvmTypeReference _type = pstuff.getType();
                          final JvmTypeReference genptype = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.model.Parameter", _type);
                          _builder.newLineIfNotEmpty();
                          {
                            boolean _isNumericType = pstuff.isNumericType();
                            if (_isNumericType) {
                              JvmTypeReference _type_1 = pstuff.getType();
                              final JvmTypeReference implptype = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.model.NumericParameterImpl", _type_1);
                              _builder.newLineIfNotEmpty();
                              _builder.append(genptype, "");
                              _builder.append(" par_");
                              String _name = pstuff.getName();
                              _builder.append(_name, "");
                              _builder.append(" = new ");
                              _builder.append(implptype, "");
                              _builder.append("(\"");
                              String _name_1 = pstuff.getName();
                              _builder.append(_name_1, "");
                              _builder.append("\",\"");
                              String _description = pstuff.getDescription();
                              _builder.append(_description, "");
                              _builder.append("\",");
                              Boolean _optionnal = pstuff.getOptionnal();
                              _builder.append(_optionnal, "");
                              _builder.append(",");
                              String _dvalueString = pstuff.getDvalueString();
                              _builder.append(_dvalueString, "");
                              {
                                Object _minvalue = pstuff.getMinvalue();
                                boolean _equals = Objects.equal(_minvalue, null);
                                if (_equals) {
                                  _builder.append(",null");
                                } else {
                                  _builder.append(",");
                                  Object _minvalue_1 = pstuff.getMinvalue();
                                  _builder.append(_minvalue_1, "");
                                }
                              }
                              {
                                Object _maxvalue = pstuff.getMaxvalue();
                                boolean _equals_1 = Objects.equal(_maxvalue, null);
                                if (_equals_1) {
                                  _builder.append(",null");
                                } else {
                                  _builder.append(",");
                                  Object _maxvalue_1 = pstuff.getMaxvalue();
                                  _builder.append(_maxvalue_1, "");
                                }
                              }
                              {
                                String _unit = pstuff.getUnit();
                                boolean _equals_2 = Objects.equal(_unit, null);
                                if (_equals_2) {
                                  _builder.append(",null");
                                } else {
                                  _builder.append(",");
                                  String _unit_1 = pstuff.getUnit();
                                  _builder.append(_unit_1, "");
                                }
                              }
                              _builder.append(");");
                              _builder.newLineIfNotEmpty();
                            } else {
                              JvmTypeReference _type_2 = pstuff.getType();
                              final JvmTypeReference implptype_1 = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.model.ParameterImpl", _type_2);
                              _builder.newLineIfNotEmpty();
                              _builder.append(genptype, "");
                              _builder.append(" par_");
                              String _name_2 = pstuff.getName();
                              _builder.append(_name_2, "");
                              _builder.append(" = new ");
                              _builder.append(implptype_1, "");
                              _builder.append("(\"");
                              String _name_3 = pstuff.getName();
                              _builder.append(_name_3, "");
                              _builder.append("\",\"");
                              String _description_1 = pstuff.getDescription();
                              _builder.append(_description_1, "");
                              _builder.append("\",");
                              Boolean _optionnal_1 = pstuff.getOptionnal();
                              _builder.append(_optionnal_1, "");
                              _builder.append(",");
                              String _dvalueString_1 = pstuff.getDvalueString();
                              _builder.append(_dvalueString_1, "");
                              {
                                String _unit_2 = pstuff.getUnit();
                                boolean _equals_3 = Objects.equal(_unit_2, null);
                                if (_equals_3) {
                                  _builder.append(",null");
                                } else {
                                  _builder.append(",\"");
                                  String _unit_3 = pstuff.getUnit();
                                  _builder.append(_unit_3, "");
                                  _builder.append("\"");
                                }
                              }
                              _builder.append(");");
                              _builder.newLineIfNotEmpty();
                            }
                          }
                          _builder.append("addParameter(par_");
                          String _name_4 = pstuff.getName();
                          _builder.append(_name_4, "");
                          _builder.append(");");
                          _builder.newLineIfNotEmpty();
                          {
                            Object _dvalue = pstuff.getDvalue();
                            boolean _notEquals_2 = (!Objects.equal(_dvalue, null));
                            if (_notEquals_2) {
                              _builder.append(pstuff.name, "");
                              _builder.append(" = ");
                              String _dvalueString_2 = pstuff.getDvalueString();
                              _builder.append(_dvalueString_2, "");
                              _builder.append(";");
                              _builder.newLineIfNotEmpty();
                            }
                          }
                        }
                      }
                    }
                  }
                }
              };
              OceletJvmModelInferrer.this._jvmTypesBuilder.setBody(it, _client);
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
                @Override
                public void apply(final JvmOperation it) {
                  EList<JvmFormalParameter> _parameters = it.getParameters();
                  JvmTypeReference _typeRef = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("java.lang.String");
                  JvmTypeReference _addArrayTypeDimension = OceletJvmModelInferrer.this._jvmTypesBuilder.addArrayTypeDimension(_typeRef);
                  JvmFormalParameter _parameter = OceletJvmModelInferrer.this._jvmTypesBuilder.toParameter(modl, "args", _addArrayTypeDimension);
                  OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                  it.setStatic(true);
                  StringConcatenationClient _client = new StringConcatenationClient() {
                    @Override
                    protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
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
                    }
                  };
                  OceletJvmModelInferrer.this._jvmTypesBuilder.setBody(it, _client);
                }
              };
              JvmOperation _method = OceletJvmModelInferrer.this._jvmTypesBuilder.toMethod(modl, "main", _typeRef_1, _function_1);
              OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmOperation>operator_add(_members_1, _method);
              EList<JvmMember> _members_2 = it.getMembers();
              JvmTypeReference _typeRef_2 = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef(Void.TYPE);
              final Procedure1<JvmOperation> _function_2 = new Procedure1<JvmOperation>() {
                @Override
                public void apply(final JvmOperation it) {
                  XExpression _body = scen.getBody();
                  OceletJvmModelInferrer.this._jvmTypesBuilder.setBody(it, _body);
                }
              };
              JvmOperation _method_1 = OceletJvmModelInferrer.this._jvmTypesBuilder.toMethod(modl, ("run_" + modlName), _typeRef_2, _function_2);
              OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmOperation>operator_add(_members_2, _method_1);
              EList<JvmMember> _members_3 = it.getMembers();
              JvmTypeReference _typeRef_3 = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef(Void.TYPE);
              final Procedure1<JvmOperation> _function_3 = new Procedure1<JvmOperation>() {
                @Override
                public void apply(final JvmOperation it) {
                  EList<JvmFormalParameter> _parameters = it.getParameters();
                  JvmTypeReference _typeRef = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("java.lang.String");
                  JvmTypeReference _typeRef_1 = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("java.lang.Object");
                  JvmTypeReference _typeRef_2 = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("java.util.HashMap", _typeRef, _typeRef_1);
                  JvmFormalParameter _parameter = OceletJvmModelInferrer.this._jvmTypesBuilder.toParameter(modl, "in_params", _typeRef_2);
                  OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                  StringConcatenationClient _client = new StringConcatenationClient() {
                    @Override
                    protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                      {
                        boolean _hasParameters = md.hasParameters();
                        if (_hasParameters) {
                          {
                            ArrayList<Parameterstuff> _params = md.getParams();
                            for(final Parameterstuff pstuff : _params) {
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
                              _builder.newLineIfNotEmpty();
                              _builder.append("if (val_");
                              String _name_2 = pstuff.getName();
                              _builder.append(_name_2, "");
                              _builder.append(" != null) ");
                              String _name_3 = pstuff.getName();
                              _builder.append(_name_3, "");
                              _builder.append(" = val_");
                              String _name_4 = pstuff.getName();
                              _builder.append(_name_4, "");
                              _builder.append(";");
                              _builder.newLineIfNotEmpty();
                            }
                          }
                        }
                      }
                      _builder.append("run_");
                      _builder.append(modlName, "");
                      _builder.append("();");
                      _builder.newLineIfNotEmpty();
                    }
                  };
                  OceletJvmModelInferrer.this._jvmTypesBuilder.setBody(it, _client);
                }
              };
              JvmOperation _method_2 = OceletJvmModelInferrer.this._jvmTypesBuilder.toMethod(modl, "simulate", _typeRef_3, _function_3);
              OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmOperation>operator_add(_members_3, _method_2);
            } else {
              JvmTypeReference rtype = scen.getType();
              boolean _equals_1 = Objects.equal(rtype, null);
              if (_equals_1) {
                JvmTypeReference _typeRef_4 = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef(Void.TYPE);
                rtype = _typeRef_4;
              }
              EList<JvmMember> _members_4 = it.getMembers();
              String _name_1 = scen.getName();
              final Procedure1<JvmOperation> _function_4 = new Procedure1<JvmOperation>() {
                @Override
                public void apply(final JvmOperation it) {
                  EList<JvmFormalParameter> _params = scen.getParams();
                  for (final JvmFormalParameter p : _params) {
                    EList<JvmFormalParameter> _parameters = it.getParameters();
                    String _name = p.getName();
                    JvmTypeReference _parameterType = p.getParameterType();
                    JvmFormalParameter _parameter = OceletJvmModelInferrer.this._jvmTypesBuilder.toParameter(p, _name, _parameterType);
                    OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                  }
                  XExpression _body = scen.getBody();
                  OceletJvmModelInferrer.this._jvmTypesBuilder.setBody(it, _body);
                }
              };
              JvmOperation _method_3 = OceletJvmModelInferrer.this._jvmTypesBuilder.toMethod(scen, _name_1, rtype, _function_4);
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
