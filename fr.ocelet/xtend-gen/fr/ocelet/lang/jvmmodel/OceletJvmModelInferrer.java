package fr.ocelet.lang.jvmmodel;

import com.google.common.base.Objects;
import com.google.inject.Inject;
import fr.ocelet.lang.jvmmodel.Metadatastuff;
import fr.ocelet.lang.jvmmodel.OceletCompiler;
import fr.ocelet.lang.jvmmodel.Parameterstuff;
import fr.ocelet.lang.ocelet.Agregdef;
import fr.ocelet.lang.ocelet.Comitexpr;
import fr.ocelet.lang.ocelet.ConstructorDef;
import fr.ocelet.lang.ocelet.Datafacer;
import fr.ocelet.lang.ocelet.Entity;
import fr.ocelet.lang.ocelet.EntityElements;
import fr.ocelet.lang.ocelet.Filterdef;
import fr.ocelet.lang.ocelet.InteractionDef;
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
import fr.ocelet.lang.ocelet.RelElements;
import fr.ocelet.lang.ocelet.RelPropertyDef;
import fr.ocelet.lang.ocelet.Relation;
import fr.ocelet.lang.ocelet.Role;
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
import org.eclipse.xtext.xbase.lib.IntegerRange;
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
                                  StringConcatenationClient _client = new StringConcatenationClient() {
                                    @Override
                                    protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                      _builder.append("List<");
                                      _builder.append(entname, "");
                                      _builder.append("> _elist = new List<");
                                      _builder.append(entname, "");
                                      _builder.append(">();");
                                      _builder.newLineIfNotEmpty();
                                      _builder.append("for (");
                                      _builder.append(inputRecordType, "");
                                      _builder.append(" _record : this) {");
                                      _builder.newLineIfNotEmpty();
                                      _builder.append("  ");
                                      _builder.append("_elist.add(create");
                                      _builder.append(entname, "  ");
                                      _builder.append("FromRecord(_record));");
                                      _builder.newLineIfNotEmpty();
                                      _builder.append(" ");
                                      _builder.append("}");
                                      _builder.newLine();
                                      _builder.append("close();");
                                      _builder.newLine();
                                      _builder.append("return _elist;");
                                      _builder.newLine();
                                    }
                                  };
                                  OceletJvmModelInferrer.this._jvmTypesBuilder.setBody(it, _client);
                                }
                              };
                              JvmOperation _method = OceletJvmModelInferrer.this._jvmTypesBuilder.toMethod(meln, ("readAll" + entname), listype, _function_1);
                              OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmOperation>operator_add(_members_1, _method);
                              if (isFirst) {
                                EList<JvmMember> _members_2 = it.getMembers();
                                final Procedure1<JvmOperation> _function_2 = new Procedure1<JvmOperation>() {
                                  @Override
                                  public void apply(final JvmOperation it) {
                                    StringConcatenationClient _client = new StringConcatenationClient() {
                                      @Override
                                      protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                        _builder.append("return readAll");
                                        _builder.append(entname, "");
                                        _builder.append("();");
                                      }
                                    };
                                    OceletJvmModelInferrer.this._jvmTypesBuilder.setBody(it, _client);
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
                                  StringConcatenationClient _client = new StringConcatenationClient() {
                                    @Override
                                    protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                      _builder.append("                  \t    ");
                                      _builder.append(entname, "                  \t    ");
                                      _builder.append(" _entity = new ");
                                      _builder.append(entname, "                  \t    ");
                                      _builder.append("();");
                                      _builder.newLineIfNotEmpty();
                                      {
                                        EList<Mdef> _matchprops = matchdef.getMatchprops();
                                        for(final Mdef mp : _matchprops) {
                                          String _prop = mp.getProp();
                                          final String eproptype = propmap.get(_prop);
                                          _builder.newLineIfNotEmpty();
                                          {
                                            boolean _notEquals = (!Objects.equal(eproptype, null));
                                            if (_notEquals) {
                                              {
                                                String _colname = mp.getColname();
                                                boolean _notEquals_1 = (!Objects.equal(_colname, null));
                                                if (_notEquals_1) {
                                                  _builder.append("_entity.setProperty(\"");
                                                  String _prop_1 = mp.getProp();
                                                  _builder.append(_prop_1, "");
                                                  _builder.append("\",read");
                                                  _builder.append(eproptype, "");
                                                  _builder.append("(\"");
                                                  String _colname_1 = mp.getColname();
                                                  _builder.append(_colname_1, "");
                                                  _builder.append("\"));");
                                                }
                                              }
                                              _builder.newLineIfNotEmpty();
                                            }
                                          }
                                        }
                                      }
                                      _builder.append("return _entity;");
                                      _builder.newLine();
                                    }
                                  };
                                  OceletJvmModelInferrer.this._jvmTypesBuilder.setBody(it, _client);
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
                                  StringConcatenationClient _client = new StringConcatenationClient() {
                                    @Override
                                    protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                      String _simpleName = hmtype.getSimpleName();
                                      _builder.append(_simpleName, "");
                                      _builder.append(" hm = new ");
                                      _builder.append(hmtype, "");
                                      _builder.append("();");
                                      _builder.newLineIfNotEmpty();
                                      {
                                        EList<Mdef> _matchprops = matchdef.getMatchprops();
                                        for(final Mdef mp : _matchprops) {
                                          _builder.append("                  \t  \t  ");
                                          String _prop = mp.getProp();
                                          final String epropftype = propmapf.get(_prop);
                                          _builder.newLineIfNotEmpty();
                                          {
                                            boolean _notEquals = (!Objects.equal(epropftype, null));
                                            if (_notEquals) {
                                              {
                                                String _colname = mp.getColname();
                                                boolean _notEquals_1 = (!Objects.equal(_colname, null));
                                                if (_notEquals_1) {
                                                  _builder.append("hm.put(\"");
                                                  String _colname_1 = mp.getColname();
                                                  _builder.append(_colname_1, "");
                                                  _builder.append("\",\"");
                                                  _builder.append(epropftype, "");
                                                  _builder.append("\");");
                                                }
                                              }
                                              _builder.newLineIfNotEmpty();
                                            }
                                          }
                                        }
                                      }
                                      _builder.append("return hm;");
                                      _builder.newLine();
                                    }
                                  };
                                  OceletJvmModelInferrer.this._jvmTypesBuilder.setBody(it, _client);
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
                                  StringConcatenationClient _client = new StringConcatenationClient() {
                                    @Override
                                    protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                      _builder.append("setFilter(_filt);");
                                      _builder.newLine();
                                      _builder.append("return readAll");
                                      _builder.append(entname, "");
                                      _builder.append("();");
                                      _builder.newLineIfNotEmpty();
                                    }
                                  };
                                  OceletJvmModelInferrer.this._jvmTypesBuilder.setBody(it, _client);
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
                                  StringConcatenationClient _client = new StringConcatenationClient() {
                                    @Override
                                    protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                      final JvmTypeReference odrtype = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("fr.ocelet.datafacer.OutputDataRecord");
                                      _builder.newLineIfNotEmpty();
                                      _builder.append(odrtype, "");
                                      _builder.append(" odr = createOutputDataRec();");
                                      _builder.newLineIfNotEmpty();
                                      _builder.append("if (odr != null) {");
                                      _builder.newLine();
                                      {
                                        EList<Mdef> _matchprops = matchdef.getMatchprops();
                                        for(final Mdef mp : _matchprops) {
                                          _builder.append("odr.setAttribute(\"");
                                          String _colname = mp.getColname();
                                          _builder.append(_colname, "");
                                          _builder.append("\",((");
                                          _builder.append(entname, "");
                                          _builder.append(") ety).get");
                                          String _prop = mp.getProp();
                                          String _firstUpper = StringExtensions.toFirstUpper(_prop);
                                          _builder.append(_firstUpper, "");
                                          _builder.append("());");
                                          _builder.newLineIfNotEmpty();
                                        }
                                      }
                                      _builder.append("}");
                                      _builder.newLine();
                                      _builder.append("return odr;");
                                      _builder.newLine();
                                    }
                                  };
                                  OceletJvmModelInferrer.this._jvmTypesBuilder.setBody(it, _client);
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
                              } else {
                                String _qualifiedName_6 = vtyp.getQualifiedName();
                                boolean _equals_6 = _qualifiedName_6.equals("java.lang.Boolean");
                                if (_equals_6) {
                                  _builder.append("(false));");
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
          if (meln instanceof Agregdef) {
            _matched=true;
            JvmTypeReference _type = ((Agregdef)meln).getType();
            boolean _notEquals = (!Objects.equal(_type, null));
            if (_notEquals) {
              QualifiedName _fullyQualifiedName = this._iQualifiedNameProvider.getFullyQualifiedName(meln);
              JvmGenericType _class = this._jvmTypesBuilder.toClass(modl, _fullyQualifiedName);
              final Procedure1<JvmGenericType> _function = new Procedure1<JvmGenericType>() {
                @Override
                public void apply(final JvmGenericType it) {
                  EList<JvmTypeReference> _superTypes = it.getSuperTypes();
                  JvmTypeReference _type = ((Agregdef)meln).getType();
                  JvmTypeReference _type_1 = ((Agregdef)meln).getType();
                  JvmTypeReference _typeRef = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.ocltypes.List", _type_1);
                  JvmTypeReference _typeRef_1 = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.relation.AggregOperator", _type, _typeRef);
                  OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes, _typeRef_1);
                  EList<JvmMember> _members = it.getMembers();
                  JvmTypeReference _type_2 = ((Agregdef)meln).getType();
                  final Procedure1<JvmOperation> _function = new Procedure1<JvmOperation>() {
                    @Override
                    public void apply(final JvmOperation it) {
                      EList<JvmFormalParameter> _parameters = it.getParameters();
                      JvmTypeReference _type = ((Agregdef)meln).getType();
                      JvmTypeReference _typeRef = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.ocltypes.List", _type);
                      JvmFormalParameter _parameter = OceletJvmModelInferrer.this._jvmTypesBuilder.toParameter(meln, "values", _typeRef);
                      OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                      EList<JvmFormalParameter> _parameters_1 = it.getParameters();
                      JvmTypeReference _type_1 = ((Agregdef)meln).getType();
                      JvmFormalParameter _parameter_1 = OceletJvmModelInferrer.this._jvmTypesBuilder.toParameter(meln, "preval", _type_1);
                      OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_1, _parameter_1);
                      XExpression _body = ((Agregdef)meln).getBody();
                      OceletJvmModelInferrer.this._jvmTypesBuilder.setBody(it, _body);
                    }
                  };
                  JvmOperation _method = OceletJvmModelInferrer.this._jvmTypesBuilder.toMethod(meln, "compute", _type_2, _function);
                  OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmOperation>operator_add(_members, _method);
                }
              };
              acceptor.<JvmGenericType>accept(_class, _function);
            }
          }
        }
        if (!_matched) {
          if (meln instanceof Relation) {
            _matched=true;
            final QualifiedName graphcname = this._iQualifiedNameProvider.getFullyQualifiedName(meln);
            final String edgecname = (graphcname + "_Edge");
            boolean _and = false;
            boolean _and_1 = false;
            boolean _and_2 = false;
            boolean _and_3 = false;
            boolean _and_4 = false;
            boolean _and_5 = false;
            boolean _and_6 = false;
            boolean _and_7 = false;
            EList<Role> _roles = ((Relation)meln).getRoles();
            int _size = _roles.size();
            boolean _greaterEqualsThan = (_size >= 2);
            if (!_greaterEqualsThan) {
              _and_7 = false;
            } else {
              EList<Role> _roles_1 = ((Relation)meln).getRoles();
              Role _get = _roles_1.get(0);
              boolean _notEquals = (!Objects.equal(_get, null));
              _and_7 = _notEquals;
            }
            if (!_and_7) {
              _and_6 = false;
            } else {
              EList<Role> _roles_2 = ((Relation)meln).getRoles();
              Role _get_1 = _roles_2.get(1);
              boolean _notEquals_1 = (!Objects.equal(_get_1, null));
              _and_6 = _notEquals_1;
            }
            if (!_and_6) {
              _and_5 = false;
            } else {
              EList<Role> _roles_3 = ((Relation)meln).getRoles();
              Role _get_2 = _roles_3.get(0);
              Entity _type = _get_2.getType();
              boolean _notEquals_2 = (!Objects.equal(_type, null));
              _and_5 = _notEquals_2;
            }
            if (!_and_5) {
              _and_4 = false;
            } else {
              EList<Role> _roles_4 = ((Relation)meln).getRoles();
              Role _get_3 = _roles_4.get(1);
              Entity _type_1 = _get_3.getType();
              boolean _notEquals_3 = (!Objects.equal(_type_1, null));
              _and_4 = _notEquals_3;
            }
            if (!_and_4) {
              _and_3 = false;
            } else {
              EList<Role> _roles_5 = ((Relation)meln).getRoles();
              Role _get_4 = _roles_5.get(0);
              Entity _type_2 = _get_4.getType();
              QualifiedName _fullyQualifiedName = this._iQualifiedNameProvider.getFullyQualifiedName(_type_2);
              boolean _notEquals_4 = (!Objects.equal(_fullyQualifiedName, null));
              _and_3 = _notEquals_4;
            }
            if (!_and_3) {
              _and_2 = false;
            } else {
              EList<Role> _roles_6 = ((Relation)meln).getRoles();
              Role _get_5 = _roles_6.get(1);
              Entity _type_3 = _get_5.getType();
              QualifiedName _fullyQualifiedName_1 = this._iQualifiedNameProvider.getFullyQualifiedName(_type_3);
              boolean _notEquals_5 = (!Objects.equal(_fullyQualifiedName_1, null));
              _and_2 = _notEquals_5;
            }
            if (!_and_2) {
              _and_1 = false;
            } else {
              EList<Role> _roles_7 = ((Relation)meln).getRoles();
              Role _get_6 = _roles_7.get(0);
              String _name_2 = _get_6.getName();
              boolean _notEquals_6 = (!Objects.equal(_name_2, null));
              _and_1 = _notEquals_6;
            }
            if (!_and_1) {
              _and = false;
            } else {
              EList<Role> _roles_8 = ((Relation)meln).getRoles();
              Role _get_7 = _roles_8.get(1);
              String _name_3 = _get_7.getName();
              boolean _notEquals_7 = (!Objects.equal(_name_3, null));
              _and = _notEquals_7;
            }
            if (_and) {
              EList<Role> _roles_9 = ((Relation)meln).getRoles();
              int _size_1 = _roles_9.size();
              boolean _greaterThan = (_size_1 > 2);
              if (_greaterThan) {
                InputOutput.<String>println("Sorry, only graphs with two roles are supported by this version. The two first roles will be used and the others will be ignored.");
              }
              EList<Role> _roles_10 = ((Relation)meln).getRoles();
              Role _get_8 = _roles_10.get(0);
              Entity _type_4 = _get_8.getType();
              EList<Role> _roles_11 = ((Relation)meln).getRoles();
              Role _get_9 = _roles_11.get(1);
              Entity _type_5 = _get_9.getType();
              final boolean isAutoGraph = _type_4.equals(_type_5);
              String _xifexpression = null;
              if (isAutoGraph) {
                _xifexpression = "fr.ocelet.runtime.relation.impl.AutoGraph";
              } else {
                _xifexpression = "fr.ocelet.runtime.relation.impl.DiGraph";
              }
              final String graphTypeName = _xifexpression;
              JvmGenericType _class = this._jvmTypesBuilder.toClass(modl, edgecname);
              final Procedure1<JvmGenericType> _function = new Procedure1<JvmGenericType>() {
                @Override
                public void apply(final JvmGenericType it) {
                  EList<JvmTypeReference> _superTypes = it.getSuperTypes();
                  JvmTypeReference _typeRef = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.relation.OcltEdge");
                  OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes, _typeRef);
                  EList<Role> _roles = ((Relation)meln).getRoles();
                  final Role firstRole = _roles.get(0);
                  EList<Role> _roles_1 = ((Relation)meln).getRoles();
                  final Role secondRole = _roles_1.get(1);
                  Entity _type = firstRole.getType();
                  QualifiedName _fullyQualifiedName = OceletJvmModelInferrer.this._iQualifiedNameProvider.getFullyQualifiedName(_type);
                  String _string = _fullyQualifiedName.toString();
                  final JvmTypeReference firstRoleType = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef(_string);
                  Entity _type_1 = secondRole.getType();
                  QualifiedName _fullyQualifiedName_1 = OceletJvmModelInferrer.this._iQualifiedNameProvider.getFullyQualifiedName(_type_1);
                  String _string_1 = _fullyQualifiedName_1.toString();
                  final JvmTypeReference secondRoleType = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef(_string_1);
                  String _name = firstRole.getName();
                  JvmField jvmField = OceletJvmModelInferrer.this._jvmTypesBuilder.toField(meln, _name, firstRoleType);
                  boolean _notEquals = (!Objects.equal(jvmField, null));
                  if (_notEquals) {
                    jvmField.setFinal(false);
                    EList<JvmMember> _members = it.getMembers();
                    OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmField>operator_add(_members, jvmField);
                    EList<JvmMember> _members_1 = it.getMembers();
                    String _name_1 = firstRole.getName();
                    JvmOperation _setter = OceletJvmModelInferrer.this._jvmTypesBuilder.toSetter(meln, _name_1, firstRoleType);
                    OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmOperation>operator_add(_members_1, _setter);
                    EList<JvmMember> _members_2 = it.getMembers();
                    String _name_2 = firstRole.getName();
                    JvmOperation _getter = OceletJvmModelInferrer.this._jvmTypesBuilder.toGetter(meln, _name_2, firstRoleType);
                    OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmOperation>operator_add(_members_2, _getter);
                  }
                  String _name_3 = secondRole.getName();
                  JvmField _field = OceletJvmModelInferrer.this._jvmTypesBuilder.toField(meln, _name_3, secondRoleType);
                  jvmField = _field;
                  boolean _notEquals_1 = (!Objects.equal(jvmField, null));
                  if (_notEquals_1) {
                    jvmField.setFinal(false);
                    EList<JvmMember> _members_3 = it.getMembers();
                    OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmField>operator_add(_members_3, jvmField);
                    EList<JvmMember> _members_4 = it.getMembers();
                    String _name_4 = secondRole.getName();
                    JvmOperation _setter_1 = OceletJvmModelInferrer.this._jvmTypesBuilder.toSetter(meln, _name_4, secondRoleType);
                    OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmOperation>operator_add(_members_4, _setter_1);
                    EList<JvmMember> _members_5 = it.getMembers();
                    String _name_5 = secondRole.getName();
                    JvmOperation _getter_1 = OceletJvmModelInferrer.this._jvmTypesBuilder.toGetter(meln, _name_5, secondRoleType);
                    OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmOperation>operator_add(_members_5, _getter_1);
                  }
                  EList<JvmMember> _members_6 = it.getMembers();
                  final Procedure1<JvmConstructor> _function = new Procedure1<JvmConstructor>() {
                    @Override
                    public void apply(final JvmConstructor it) {
                      EList<JvmFormalParameter> _parameters = it.getParameters();
                      JvmTypeReference _typeRef = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.relation.InteractionGraph");
                      JvmFormalParameter _parameter = OceletJvmModelInferrer.this._jvmTypesBuilder.toParameter(meln, "igr", _typeRef);
                      OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                      EList<JvmFormalParameter> _parameters_1 = it.getParameters();
                      JvmFormalParameter _parameter_1 = OceletJvmModelInferrer.this._jvmTypesBuilder.toParameter(meln, "first", firstRoleType);
                      OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_1, _parameter_1);
                      EList<JvmFormalParameter> _parameters_2 = it.getParameters();
                      JvmFormalParameter _parameter_2 = OceletJvmModelInferrer.this._jvmTypesBuilder.toParameter(meln, "second", secondRoleType);
                      OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_2, _parameter_2);
                      StringConcatenationClient _client = new StringConcatenationClient() {
                        @Override
                        protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                          _builder.append("super(igr);");
                          _builder.newLine();
                          String _name = firstRole.getName();
                          _builder.append(_name, "");
                          _builder.append("=first;");
                          _builder.newLineIfNotEmpty();
                          String _name_1 = secondRole.getName();
                          _builder.append(_name_1, "");
                          _builder.append("=second;");
                          _builder.newLineIfNotEmpty();
                        }
                      };
                      OceletJvmModelInferrer.this._jvmTypesBuilder.setBody(it, _client);
                    }
                  };
                  JvmConstructor _constructor = OceletJvmModelInferrer.this._jvmTypesBuilder.toConstructor(meln, _function);
                  OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmConstructor>operator_add(_members_6, _constructor);
                  EList<JvmMember> _members_7 = it.getMembers();
                  JvmTypeReference _typeRef_1 = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.relation.OcltRole");
                  final Procedure1<JvmOperation> _function_1 = new Procedure1<JvmOperation>() {
                    @Override
                    public void apply(final JvmOperation it) {
                      EList<JvmFormalParameter> _parameters = it.getParameters();
                      JvmTypeReference _typeRef = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("int");
                      JvmFormalParameter _parameter = OceletJvmModelInferrer.this._jvmTypesBuilder.toParameter(meln, "i", _typeRef);
                      OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                      StringConcatenationClient _client = new StringConcatenationClient() {
                        @Override
                        protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                          _builder.append("if (i==0) return ");
                          String _name = firstRole.getName();
                          _builder.append(_name, "");
                          _builder.append(";");
                          _builder.newLineIfNotEmpty();
                          _builder.append("else if (i==1) return ");
                          String _name_1 = secondRole.getName();
                          _builder.append(_name_1, "");
                          _builder.append(";");
                          _builder.newLineIfNotEmpty();
                          _builder.append("else return null;");
                          _builder.newLine();
                        }
                      };
                      OceletJvmModelInferrer.this._jvmTypesBuilder.setBody(it, _client);
                    }
                  };
                  JvmOperation _method = OceletJvmModelInferrer.this._jvmTypesBuilder.toMethod(meln, "getRole", _typeRef_1, _function_1);
                  OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmOperation>operator_add(_members_7, _method);
                  EList<RelElements> _relelns = ((Relation)meln).getRelelns();
                  for (final RelElements reln : _relelns) {
                    boolean _matched = false;
                    if (!_matched) {
                      if (reln instanceof RelPropertyDef) {
                        _matched=true;
                        String _name_6 = ((RelPropertyDef)reln).getName();
                        JvmTypeReference _type_2 = ((RelPropertyDef)reln).getType();
                        final JvmField rField = OceletJvmModelInferrer.this._jvmTypesBuilder.toField(reln, _name_6, _type_2);
                        boolean _notEquals_2 = (!Objects.equal(rField, null));
                        if (_notEquals_2) {
                          rField.setFinal(false);
                          EList<JvmMember> _members_8 = it.getMembers();
                          OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmField>operator_add(_members_8, rField);
                          EList<JvmMember> _members_9 = it.getMembers();
                          String _name_7 = ((RelPropertyDef)reln).getName();
                          JvmTypeReference _type_3 = ((RelPropertyDef)reln).getType();
                          JvmOperation _setter_2 = OceletJvmModelInferrer.this._jvmTypesBuilder.toSetter(reln, _name_7, _type_3);
                          OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmOperation>operator_add(_members_9, _setter_2);
                          EList<JvmMember> _members_10 = it.getMembers();
                          String _name_8 = ((RelPropertyDef)reln).getName();
                          JvmTypeReference _type_4 = ((RelPropertyDef)reln).getType();
                          JvmOperation _getter_2 = OceletJvmModelInferrer.this._jvmTypesBuilder.toGetter(reln, _name_8, _type_4);
                          OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmOperation>operator_add(_members_10, _getter_2);
                        }
                      }
                    }
                    if (!_matched) {
                      if (reln instanceof InteractionDef) {
                        _matched=true;
                        EList<JvmMember> _members_8 = it.getMembers();
                        String _name_6 = ((InteractionDef)reln).getName();
                        JvmTypeReference _typeRef_2 = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef(Void.TYPE);
                        final Procedure1<JvmOperation> _function_2 = new Procedure1<JvmOperation>() {
                          @Override
                          public void apply(final JvmOperation it) {
                            EList<JvmFormalParameter> _params = ((InteractionDef)reln).getParams();
                            for (final JvmFormalParameter p : _params) {
                              EList<JvmFormalParameter> _parameters = it.getParameters();
                              String _name = p.getName();
                              JvmTypeReference _parameterType = p.getParameterType();
                              JvmFormalParameter _parameter = OceletJvmModelInferrer.this._jvmTypesBuilder.toParameter(reln, _name, _parameterType);
                              OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                            }
                            XExpression _body = ((InteractionDef)reln).getBody();
                            OceletJvmModelInferrer.this._jvmTypesBuilder.setBody(it, _body);
                          }
                        };
                        JvmOperation _method_1 = OceletJvmModelInferrer.this._jvmTypesBuilder.toMethod(reln, _name_6, _typeRef_2, _function_2);
                        OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmOperation>operator_add(_members_8, _method_1);
                        EList<Comitexpr> _comitexpressions = ((InteractionDef)reln).getComitexpressions();
                        int _size = _comitexpressions.size();
                        boolean _greaterThan = (_size > 0);
                        if (_greaterThan) {
                          EList<JvmMember> _members_9 = it.getMembers();
                          String _name_7 = ((InteractionDef)reln).getName();
                          String _plus = ("_agr_" + _name_7);
                          JvmTypeReference _typeRef_3 = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef(Void.TYPE);
                          final Procedure1<JvmOperation> _function_3 = new Procedure1<JvmOperation>() {
                            @Override
                            public void apply(final JvmOperation it) {
                              StringConcatenationClient _client = new StringConcatenationClient() {
                                @Override
                                protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                  {
                                    EList<Comitexpr> _comitexpressions = ((InteractionDef)reln).getComitexpressions();
                                    for(final Comitexpr ce : _comitexpressions) {
                                      _builder.newLineIfNotEmpty();
                                      _builder.append("this.");
                                      Role _rol = ce.getRol();
                                      String _name = _rol.getName();
                                      _builder.append(_name, "");
                                      _builder.append(".setAgregOp(\"");
                                      String _prop = ce.getProp();
                                      _builder.append(_prop, "");
                                      _builder.append("\",new ");
                                      JvmTypeReference _agrfunc = ce.getAgrfunc();
                                      _builder.append(_agrfunc, "");
                                      _builder.append("(),");
                                      boolean _isUsepreval = ce.isUsepreval();
                                      _builder.append(_isUsepreval, "");
                                      _builder.append(");");
                                      _builder.newLineIfNotEmpty();
                                    }
                                  }
                                }
                              };
                              OceletJvmModelInferrer.this._jvmTypesBuilder.setBody(it, _client);
                            }
                          };
                          JvmOperation _method_2 = OceletJvmModelInferrer.this._jvmTypesBuilder.toMethod(reln, _plus, _typeRef_3, _function_3);
                          OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmOperation>operator_add(_members_9, _method_2);
                        }
                      }
                    }
                  }
                }
              };
              acceptor.<JvmGenericType>accept(_class, _function);
              EList<RelElements> _relelns = ((Relation)meln).getRelelns();
              for (final RelElements reln : _relelns) {
                boolean _matched_1 = false;
                if (!_matched_1) {
                  if (reln instanceof Filterdef) {
                    _matched_1=true;
                    String _plus_1 = (graphcname + "_");
                    String _name_4 = ((Filterdef)reln).getName();
                    final String filterfqn = (_plus_1 + _name_4);
                    JvmGenericType _class_1 = this._jvmTypesBuilder.toClass(modl, filterfqn);
                    final Procedure1<JvmGenericType> _function_1 = new Procedure1<JvmGenericType>() {
                      @Override
                      public void apply(final JvmGenericType it) {
                        EList<Role> _roles = ((Relation)meln).getRoles();
                        final Role firstRole = _roles.get(0);
                        EList<Role> _roles_1 = ((Relation)meln).getRoles();
                        final Role secondRole = _roles_1.get(1);
                        Entity _type = firstRole.getType();
                        QualifiedName _fullyQualifiedName = OceletJvmModelInferrer.this._iQualifiedNameProvider.getFullyQualifiedName(_type);
                        String _string = _fullyQualifiedName.toString();
                        final JvmTypeReference firstRoleType = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef(_string);
                        Entity _type_1 = secondRole.getType();
                        QualifiedName _fullyQualifiedName_1 = OceletJvmModelInferrer.this._iQualifiedNameProvider.getFullyQualifiedName(_type_1);
                        String _string_1 = _fullyQualifiedName_1.toString();
                        final JvmTypeReference secondRoleType = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef(_string_1);
                        EList<JvmTypeReference> _superTypes = it.getSuperTypes();
                        JvmTypeReference _typeRef = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.relation.EdgeFilter", firstRoleType, secondRoleType);
                        OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes, _typeRef);
                        EList<JvmFormalParameter> _params = ((Filterdef)reln).getParams();
                        for (final JvmFormalParameter p : _params) {
                          {
                            String _name = p.getName();
                            JvmTypeReference _parameterType = p.getParameterType();
                            final JvmField pfield = OceletJvmModelInferrer.this._jvmTypesBuilder.toField(reln, _name, _parameterType);
                            boolean _notEquals = (!Objects.equal(pfield, null));
                            if (_notEquals) {
                              pfield.setFinal(false);
                              EList<JvmMember> _members = it.getMembers();
                              OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmField>operator_add(_members, pfield);
                            }
                          }
                        }
                        EList<JvmMember> _members = it.getMembers();
                        final Procedure1<JvmConstructor> _function = new Procedure1<JvmConstructor>() {
                          @Override
                          public void apply(final JvmConstructor it) {
                            EList<JvmFormalParameter> _params = ((Filterdef)reln).getParams();
                            for (final JvmFormalParameter p : _params) {
                              EList<JvmFormalParameter> _parameters = it.getParameters();
                              String _name = p.getName();
                              JvmTypeReference _parameterType = p.getParameterType();
                              JvmFormalParameter _parameter = OceletJvmModelInferrer.this._jvmTypesBuilder.toParameter(reln, _name, _parameterType);
                              OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                            }
                            StringConcatenationClient _client = new StringConcatenationClient() {
                              @Override
                              protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                {
                                  EList<JvmFormalParameter> _params = ((Filterdef)reln).getParams();
                                  for(final JvmFormalParameter p : _params) {
                                    _builder.append("this.");
                                    String _name = p.getName();
                                    _builder.append(_name, "");
                                    _builder.append(" = ");
                                    String _name_1 = p.getName();
                                    _builder.append(_name_1, "");
                                    _builder.append(";");
                                    _builder.newLineIfNotEmpty();
                                  }
                                }
                              }
                            };
                            OceletJvmModelInferrer.this._jvmTypesBuilder.setBody(it, _client);
                          }
                        };
                        JvmConstructor _constructor = OceletJvmModelInferrer.this._jvmTypesBuilder.toConstructor(reln, _function);
                        OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmConstructor>operator_add(_members, _constructor);
                        EList<JvmMember> _members_1 = it.getMembers();
                        JvmTypeReference _typeRef_1 = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("java.lang.Boolean");
                        final Procedure1<JvmOperation> _function_1 = new Procedure1<JvmOperation>() {
                          @Override
                          public void apply(final JvmOperation it) {
                            EList<JvmFormalParameter> _parameters = it.getParameters();
                            String _name = firstRole.getName();
                            JvmFormalParameter _parameter = OceletJvmModelInferrer.this._jvmTypesBuilder.toParameter(reln, _name, firstRoleType);
                            OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                            EList<JvmFormalParameter> _parameters_1 = it.getParameters();
                            String _name_1 = secondRole.getName();
                            JvmFormalParameter _parameter_1 = OceletJvmModelInferrer.this._jvmTypesBuilder.toParameter(reln, _name_1, secondRoleType);
                            OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_1, _parameter_1);
                            XExpression _body = ((Filterdef)reln).getBody();
                            OceletJvmModelInferrer.this._jvmTypesBuilder.setBody(it, _body);
                          }
                        };
                        JvmOperation _method = OceletJvmModelInferrer.this._jvmTypesBuilder.toMethod(reln, "filter", _typeRef_1, _function_1);
                        OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmOperation>operator_add(_members_1, _method);
                      }
                    };
                    acceptor.<JvmGenericType>accept(_class_1, _function_1);
                  }
                }
              }
              JvmTypeReference _typeRef = this._typeReferenceBuilder.typeRef(edgecname);
              boolean _notEquals_8 = (!Objects.equal(_typeRef, null));
              if (_notEquals_8) {
                JvmGenericType _class_1 = this._jvmTypesBuilder.toClass(modl, graphcname);
                final Procedure1<JvmGenericType> _function_1 = new Procedure1<JvmGenericType>() {
                  @Override
                  public void apply(final JvmGenericType it) {
                    String _documentation = OceletJvmModelInferrer.this._jvmTypesBuilder.getDocumentation(meln);
                    OceletJvmModelInferrer.this._jvmTypesBuilder.setDocumentation(it, _documentation);
                    EList<Role> _roles = ((Relation)meln).getRoles();
                    final Role firstRole = _roles.get(0);
                    EList<Role> _roles_1 = ((Relation)meln).getRoles();
                    final Role secondRole = _roles_1.get(1);
                    Entity _type = firstRole.getType();
                    QualifiedName _fullyQualifiedName = OceletJvmModelInferrer.this._iQualifiedNameProvider.getFullyQualifiedName(_type);
                    String _string = _fullyQualifiedName.toString();
                    final JvmTypeReference firstRoleType = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef(_string);
                    Entity _type_1 = secondRole.getType();
                    QualifiedName _fullyQualifiedName_1 = OceletJvmModelInferrer.this._iQualifiedNameProvider.getFullyQualifiedName(_type_1);
                    String _string_1 = _fullyQualifiedName_1.toString();
                    final JvmTypeReference secondRoleType = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef(_string_1);
                    EList<Role> _roles_2 = ((Relation)meln).getRoles();
                    Role _get = _roles_2.get(0);
                    String _name = _get.getName();
                    final String rolset1 = (_name + "Set");
                    EList<Role> _roles_3 = ((Relation)meln).getRoles();
                    Role _get_1 = _roles_3.get(1);
                    String _name_1 = _get_1.getName();
                    final String rolset2 = (_name_1 + "Set");
                    if (isAutoGraph) {
                      EList<JvmTypeReference> _superTypes = it.getSuperTypes();
                      JvmTypeReference _typeRef = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef(edgecname);
                      JvmTypeReference _typeRef_1 = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef(graphTypeName, _typeRef, firstRoleType);
                      OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes, _typeRef_1);
                    } else {
                      EList<JvmTypeReference> _superTypes_1 = it.getSuperTypes();
                      JvmTypeReference _typeRef_2 = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef(edgecname);
                      JvmTypeReference _typeRef_3 = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef(graphTypeName, _typeRef_2, firstRoleType, secondRoleType);
                      OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes_1, _typeRef_3);
                    }
                    EList<JvmMember> _members = it.getMembers();
                    final Procedure1<JvmConstructor> _function = new Procedure1<JvmConstructor>() {
                      @Override
                      public void apply(final JvmConstructor it) {
                        StringConcatenationClient _client = new StringConcatenationClient() {
                          @Override
                          protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                            _builder.append("super();");
                          }
                        };
                        OceletJvmModelInferrer.this._jvmTypesBuilder.setBody(it, _client);
                      }
                    };
                    JvmConstructor _constructor = OceletJvmModelInferrer.this._jvmTypesBuilder.toConstructor(meln, _function);
                    OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmConstructor>operator_add(_members, _constructor);
                    EList<JvmMember> _members_1 = it.getMembers();
                    JvmTypeReference _typeRef_4 = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef(edgecname);
                    final Procedure1<JvmOperation> _function_1 = new Procedure1<JvmOperation>() {
                      @Override
                      public void apply(final JvmOperation it) {
                        EList<JvmFormalParameter> _parameters = it.getParameters();
                        String _name = firstRole.getName();
                        JvmFormalParameter _parameter = OceletJvmModelInferrer.this._jvmTypesBuilder.toParameter(meln, _name, firstRoleType);
                        OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                        EList<JvmFormalParameter> _parameters_1 = it.getParameters();
                        String _name_1 = secondRole.getName();
                        JvmFormalParameter _parameter_1 = OceletJvmModelInferrer.this._jvmTypesBuilder.toParameter(meln, _name_1, secondRoleType);
                        OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_1, _parameter_1);
                        StringConcatenationClient _client = new StringConcatenationClient() {
                          @Override
                          protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                            _builder.append("if ((this.");
                            _builder.append(rolset1, "");
                            _builder.append(" == null) || (!this.");
                            _builder.append(rolset1, "");
                            _builder.append(".contains(");
                            String _name = firstRole.getName();
                            _builder.append(_name, "");
                            _builder.append("))) add(");
                            String _name_1 = firstRole.getName();
                            _builder.append(_name_1, "");
                            _builder.append(");");
                            _builder.newLineIfNotEmpty();
                            {
                              if ((!isAutoGraph)) {
                                _builder.append("if ((this.");
                                _builder.append(rolset2, "");
                                _builder.append(" == null) || (!this.");
                                _builder.append(rolset2, "");
                                _builder.append(".contains(");
                                String _name_2 = secondRole.getName();
                                _builder.append(_name_2, "");
                                _builder.append("))) add(");
                                String _name_3 = secondRole.getName();
                                _builder.append(_name_3, "");
                                _builder.append(");");
                                _builder.newLineIfNotEmpty();
                              }
                            }
                            final JvmTypeReference typ_edgecname = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef(edgecname);
                            _builder.newLineIfNotEmpty();
                            _builder.append(typ_edgecname, "");
                            _builder.append(" _gen_edge_ = new ");
                            String _name_4 = ((Relation)meln).getName();
                            String _plus = (_name_4 + "_Edge");
                            _builder.append(_plus, "");
                            _builder.append("(this,");
                            String _name_5 = firstRole.getName();
                            _builder.append(_name_5, "");
                            _builder.append(",");
                            String _name_6 = secondRole.getName();
                            _builder.append(_name_6, "");
                            _builder.append(");");
                            _builder.newLineIfNotEmpty();
                            _builder.append("addEdge(_gen_edge_);");
                            _builder.newLine();
                            _builder.append("return _gen_edge_;");
                            _builder.newLine();
                          }
                        };
                        OceletJvmModelInferrer.this._jvmTypesBuilder.setBody(it, _client);
                      }
                    };
                    JvmOperation _method = OceletJvmModelInferrer.this._jvmTypesBuilder.toMethod(meln, "connect", _typeRef_4, _function_1);
                    OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmOperation>operator_add(_members_1, _method);
                    EList<JvmMember> _members_2 = it.getMembers();
                    JvmTypeReference _typeRef_5 = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.relation.RoleSet", firstRoleType);
                    final Procedure1<JvmOperation> _function_2 = new Procedure1<JvmOperation>() {
                      @Override
                      public void apply(final JvmOperation it) {
                        StringConcatenationClient _client = new StringConcatenationClient() {
                          @Override
                          protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                            _builder.append("return ");
                            _builder.append(rolset1, "");
                            _builder.append(";");
                          }
                        };
                        OceletJvmModelInferrer.this._jvmTypesBuilder.setBody(it, _client);
                      }
                    };
                    JvmOperation _method_1 = OceletJvmModelInferrer.this._jvmTypesBuilder.toMethod(meln, "getLeftSet", _typeRef_5, _function_2);
                    OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmOperation>operator_add(_members_2, _method_1);
                    EList<JvmMember> _members_3 = it.getMembers();
                    JvmTypeReference _typeRef_6 = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.relation.RoleSet", secondRoleType);
                    final Procedure1<JvmOperation> _function_3 = new Procedure1<JvmOperation>() {
                      @Override
                      public void apply(final JvmOperation it) {
                        StringConcatenationClient _client = new StringConcatenationClient() {
                          @Override
                          protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                            {
                              if (isAutoGraph) {
                                _builder.append("return ");
                                _builder.append(rolset1, "");
                                _builder.append(";");
                                _builder.newLineIfNotEmpty();
                              } else {
                                _builder.append("return ");
                                _builder.append(rolset2, "");
                                _builder.append(";");
                                _builder.newLineIfNotEmpty();
                              }
                            }
                          }
                        };
                        OceletJvmModelInferrer.this._jvmTypesBuilder.setBody(it, _client);
                      }
                    };
                    JvmOperation _method_2 = OceletJvmModelInferrer.this._jvmTypesBuilder.toMethod(meln, "getRightSet", _typeRef_6, _function_3);
                    OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmOperation>operator_add(_members_3, _method_2);
                    EList<JvmMember> _members_4 = it.getMembers();
                    String _string_2 = graphcname.toString();
                    JvmTypeReference _typeRef_7 = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef(_string_2);
                    final Procedure1<JvmOperation> _function_4 = new Procedure1<JvmOperation>() {
                      @Override
                      public void apply(final JvmOperation it) {
                        StringConcatenationClient _client = new StringConcatenationClient() {
                          @Override
                          protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                            _builder.append("return (");
                            String _name = ((Relation)meln).getName();
                            _builder.append(_name, "");
                            _builder.append(")super.getComplete();");
                          }
                        };
                        OceletJvmModelInferrer.this._jvmTypesBuilder.setBody(it, _client);
                      }
                    };
                    JvmOperation _method_3 = OceletJvmModelInferrer.this._jvmTypesBuilder.toMethod(meln, "getComplete", _typeRef_7, _function_4);
                    OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmOperation>operator_add(_members_4, _method_3);
                    EList<JvmMember> _members_5 = it.getMembers();
                    JvmTypeReference _typeRef_8 = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef(edgecname);
                    final Procedure1<JvmOperation> _function_5 = new Procedure1<JvmOperation>() {
                      @Override
                      public void apply(final JvmOperation it) {
                        EList<JvmFormalParameter> _parameters = it.getParameters();
                        String _name = firstRole.getName();
                        JvmFormalParameter _parameter = OceletJvmModelInferrer.this._jvmTypesBuilder.toParameter(firstRole, _name, firstRoleType);
                        OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                        EList<JvmFormalParameter> _parameters_1 = it.getParameters();
                        String _name_1 = secondRole.getName();
                        JvmFormalParameter _parameter_1 = OceletJvmModelInferrer.this._jvmTypesBuilder.toParameter(secondRole, _name_1, secondRoleType);
                        OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_1, _parameter_1);
                        StringConcatenationClient _client = new StringConcatenationClient() {
                          @Override
                          protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                            _builder.append(" ");
                            _builder.append("return new ");
                            String _name = ((Relation)meln).getName();
                            String _plus = (_name + "_Edge");
                            _builder.append(_plus, " ");
                            _builder.append("(this,");
                            String _name_1 = firstRole.getName();
                            _builder.append(_name_1, " ");
                            _builder.append(",");
                            String _name_2 = secondRole.getName();
                            _builder.append(_name_2, " ");
                            _builder.append(");");
                          }
                        };
                        OceletJvmModelInferrer.this._jvmTypesBuilder.setBody(it, _client);
                      }
                    };
                    JvmOperation _method_4 = OceletJvmModelInferrer.this._jvmTypesBuilder.toMethod(meln, "createEdge", _typeRef_8, _function_5);
                    OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmOperation>operator_add(_members_5, _method_4);
                    final JvmTypeReference rsetype = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.relation.RoleSet", firstRoleType);
                    final JvmField rsfield = OceletJvmModelInferrer.this._jvmTypesBuilder.toField(meln, rolset1, rsetype);
                    boolean _notEquals = (!Objects.equal(rsfield, null));
                    if (_notEquals) {
                      EList<JvmMember> _members_6 = it.getMembers();
                      OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmField>operator_add(_members_6, rsfield);
                      EList<JvmMember> _members_7 = it.getMembers();
                      String _firstUpper = StringExtensions.toFirstUpper(rolset1);
                      String _plus = ("set" + _firstUpper);
                      JvmTypeReference _typeRef_9 = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef(Void.TYPE);
                      final Procedure1<JvmOperation> _function_6 = new Procedure1<JvmOperation>() {
                        @Override
                        public void apply(final JvmOperation it) {
                          EList<JvmFormalParameter> _parameters = it.getParameters();
                          JvmTypeReference _typeRef = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("java.util.Collection", firstRoleType);
                          JvmFormalParameter _parameter = OceletJvmModelInferrer.this._jvmTypesBuilder.toParameter(firstRole, "croles", _typeRef);
                          OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                          StringConcatenationClient _client = new StringConcatenationClient() {
                            @Override
                            protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                              final JvmTypeReference rsimplt = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.relation.impl.RoleSetImpl", firstRoleType);
                              _builder.newLineIfNotEmpty();
                              _builder.append("this.");
                              _builder.append(rolset1, "");
                              _builder.append("=new ");
                              _builder.append(rsimplt, "");
                              _builder.append("(croles);");
                              _builder.newLineIfNotEmpty();
                            }
                          };
                          OceletJvmModelInferrer.this._jvmTypesBuilder.setBody(it, _client);
                        }
                      };
                      JvmOperation _method_5 = OceletJvmModelInferrer.this._jvmTypesBuilder.toMethod(meln, _plus, _typeRef_9, _function_6);
                      OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmOperation>operator_add(_members_7, _method_5);
                      EList<JvmMember> _members_8 = it.getMembers();
                      String _firstUpper_1 = StringExtensions.toFirstUpper(rolset1);
                      String _plus_1 = ("get" + _firstUpper_1);
                      final Procedure1<JvmOperation> _function_7 = new Procedure1<JvmOperation>() {
                        @Override
                        public void apply(final JvmOperation it) {
                          StringConcatenationClient _client = new StringConcatenationClient() {
                            @Override
                            protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                              _builder.append("return ");
                              _builder.append(rolset1, "");
                              _builder.append(";");
                            }
                          };
                          OceletJvmModelInferrer.this._jvmTypesBuilder.setBody(it, _client);
                        }
                      };
                      JvmOperation _method_6 = OceletJvmModelInferrer.this._jvmTypesBuilder.toMethod(meln, _plus_1, rsetype, _function_7);
                      OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmOperation>operator_add(_members_8, _method_6);
                      if ((!isAutoGraph)) {
                        final JvmTypeReference rsetype2 = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.relation.RoleSet", secondRoleType);
                        final JvmField rsfield2 = OceletJvmModelInferrer.this._jvmTypesBuilder.toField(meln, rolset2, rsetype2);
                        boolean _notEquals_1 = (!Objects.equal(rsfield2, null));
                        if (_notEquals_1) {
                          EList<JvmMember> _members_9 = it.getMembers();
                          OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmField>operator_add(_members_9, rsfield2);
                          EList<JvmMember> _members_10 = it.getMembers();
                          String _firstUpper_2 = StringExtensions.toFirstUpper(rolset2);
                          String _plus_2 = ("set" + _firstUpper_2);
                          JvmTypeReference _typeRef_10 = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef(Void.TYPE);
                          final Procedure1<JvmOperation> _function_8 = new Procedure1<JvmOperation>() {
                            @Override
                            public void apply(final JvmOperation it) {
                              EList<JvmFormalParameter> _parameters = it.getParameters();
                              JvmTypeReference _typeRef = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("java.util.Collection", secondRoleType);
                              JvmFormalParameter _parameter = OceletJvmModelInferrer.this._jvmTypesBuilder.toParameter(secondRole, "croles", _typeRef);
                              OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                              StringConcatenationClient _client = new StringConcatenationClient() {
                                @Override
                                protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                  final JvmTypeReference rsimplt = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.relation.impl.RoleSetImpl", secondRoleType);
                                  _builder.newLineIfNotEmpty();
                                  _builder.append("this.");
                                  _builder.append(rolset2, "");
                                  _builder.append("=new ");
                                  _builder.append(rsimplt, "");
                                  _builder.append("(croles);");
                                  _builder.newLineIfNotEmpty();
                                }
                              };
                              OceletJvmModelInferrer.this._jvmTypesBuilder.setBody(it, _client);
                            }
                          };
                          JvmOperation _method_7 = OceletJvmModelInferrer.this._jvmTypesBuilder.toMethod(meln, _plus_2, _typeRef_10, _function_8);
                          OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmOperation>operator_add(_members_10, _method_7);
                          EList<JvmMember> _members_11 = it.getMembers();
                          String _firstUpper_3 = StringExtensions.toFirstUpper(rolset2);
                          String _plus_3 = ("get" + _firstUpper_3);
                          final Procedure1<JvmOperation> _function_9 = new Procedure1<JvmOperation>() {
                            @Override
                            public void apply(final JvmOperation it) {
                              StringConcatenationClient _client = new StringConcatenationClient() {
                                @Override
                                protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                  _builder.append("return ");
                                  _builder.append(rolset2, "");
                                  _builder.append(";");
                                }
                              };
                              OceletJvmModelInferrer.this._jvmTypesBuilder.setBody(it, _client);
                            }
                          };
                          JvmOperation _method_8 = OceletJvmModelInferrer.this._jvmTypesBuilder.toMethod(meln, _plus_3, rsetype2, _function_9);
                          OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmOperation>operator_add(_members_11, _method_8);
                        }
                      }
                      EList<JvmMember> _members_12 = it.getMembers();
                      JvmTypeReference _typeRef_11 = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef(Void.TYPE);
                      final Procedure1<JvmOperation> _function_10 = new Procedure1<JvmOperation>() {
                        @Override
                        public void apply(final JvmOperation it) {
                          EList<JvmFormalParameter> _parameters = it.getParameters();
                          JvmFormalParameter _parameter = OceletJvmModelInferrer.this._jvmTypesBuilder.toParameter(meln, "role", firstRoleType);
                          OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                          StringConcatenationClient _client = new StringConcatenationClient() {
                            @Override
                            protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                              _builder.append("add");
                              _builder.append(firstRoleType, "");
                              _builder.append("(role);");
                            }
                          };
                          OceletJvmModelInferrer.this._jvmTypesBuilder.setBody(it, _client);
                        }
                      };
                      JvmOperation _method_9 = OceletJvmModelInferrer.this._jvmTypesBuilder.toMethod(meln, "add", _typeRef_11, _function_10);
                      OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmOperation>operator_add(_members_12, _method_9);
                      EList<JvmMember> _members_13 = it.getMembers();
                      JvmTypeReference _typeRef_12 = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef(Void.TYPE);
                      final Procedure1<JvmOperation> _function_11 = new Procedure1<JvmOperation>() {
                        @Override
                        public void apply(final JvmOperation it) {
                          EList<JvmFormalParameter> _parameters = it.getParameters();
                          JvmFormalParameter _parameter = OceletJvmModelInferrer.this._jvmTypesBuilder.toParameter(meln, "role", firstRoleType);
                          OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                          StringConcatenationClient _client = new StringConcatenationClient() {
                            @Override
                            protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                              _builder.append("remove");
                              _builder.append(firstRoleType, "");
                              _builder.append("(role);");
                            }
                          };
                          OceletJvmModelInferrer.this._jvmTypesBuilder.setBody(it, _client);
                        }
                      };
                      JvmOperation _method_10 = OceletJvmModelInferrer.this._jvmTypesBuilder.toMethod(meln, "remove", _typeRef_12, _function_11);
                      OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmOperation>operator_add(_members_13, _method_10);
                      EList<JvmMember> _members_14 = it.getMembers();
                      String _simpleName = firstRoleType.getSimpleName();
                      String _plus_4 = ("add" + _simpleName);
                      JvmTypeReference _typeRef_13 = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef(Void.TYPE);
                      final Procedure1<JvmOperation> _function_12 = new Procedure1<JvmOperation>() {
                        @Override
                        public void apply(final JvmOperation it) {
                          EList<JvmFormalParameter> _parameters = it.getParameters();
                          JvmFormalParameter _parameter = OceletJvmModelInferrer.this._jvmTypesBuilder.toParameter(meln, "role", firstRoleType);
                          OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                          StringConcatenationClient _client = new StringConcatenationClient() {
                            @Override
                            protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                              final JvmTypeReference ltype = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("java.util.HashSet", firstRoleType);
                              _builder.newLineIfNotEmpty();
                              _builder.append("if (this.");
                              _builder.append(rolset1, "");
                              _builder.append(" == null) set");
                              String _firstUpper = StringExtensions.toFirstUpper(rolset1);
                              _builder.append(_firstUpper, "");
                              _builder.append("( new ");
                              _builder.append(ltype, "");
                              _builder.append("());");
                              _builder.newLineIfNotEmpty();
                              _builder.append("this.");
                              _builder.append(rolset1, "");
                              _builder.append(".addRole(role);");
                              _builder.newLineIfNotEmpty();
                            }
                          };
                          OceletJvmModelInferrer.this._jvmTypesBuilder.setBody(it, _client);
                        }
                      };
                      JvmOperation _method_11 = OceletJvmModelInferrer.this._jvmTypesBuilder.toMethod(meln, _plus_4, _typeRef_13, _function_12);
                      OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmOperation>operator_add(_members_14, _method_11);
                      EList<JvmMember> _members_15 = it.getMembers();
                      String _simpleName_1 = firstRoleType.getSimpleName();
                      String _plus_5 = ("remove" + _simpleName_1);
                      JvmTypeReference _typeRef_14 = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef(Void.TYPE);
                      final Procedure1<JvmOperation> _function_13 = new Procedure1<JvmOperation>() {
                        @Override
                        public void apply(final JvmOperation it) {
                          EList<JvmFormalParameter> _parameters = it.getParameters();
                          JvmFormalParameter _parameter = OceletJvmModelInferrer.this._jvmTypesBuilder.toParameter(meln, "role", firstRoleType);
                          OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                          StringConcatenationClient _client = new StringConcatenationClient() {
                            @Override
                            protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                              _builder.append("if (this.");
                              _builder.append(rolset1, "");
                              _builder.append(" != null) this.");
                              _builder.append(rolset1, "");
                              _builder.append(".removeRole(role);");
                            }
                          };
                          OceletJvmModelInferrer.this._jvmTypesBuilder.setBody(it, _client);
                        }
                      };
                      JvmOperation _method_12 = OceletJvmModelInferrer.this._jvmTypesBuilder.toMethod(meln, _plus_5, _typeRef_14, _function_13);
                      OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmOperation>operator_add(_members_15, _method_12);
                      EList<JvmMember> _members_16 = it.getMembers();
                      String _simpleName_2 = firstRoleType.getSimpleName();
                      String _plus_6 = ("addAll" + _simpleName_2);
                      JvmTypeReference _typeRef_15 = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef(Void.TYPE);
                      final Procedure1<JvmOperation> _function_14 = new Procedure1<JvmOperation>() {
                        @Override
                        public void apply(final JvmOperation it) {
                          EList<JvmFormalParameter> _parameters = it.getParameters();
                          JvmTypeReference _typeRef = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("java.lang.Iterable", firstRoleType);
                          JvmFormalParameter _parameter = OceletJvmModelInferrer.this._jvmTypesBuilder.toParameter(meln, "roles", _typeRef);
                          OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                          StringConcatenationClient _client = new StringConcatenationClient() {
                            @Override
                            protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                              final JvmTypeReference ltype = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("java.util.HashSet", firstRoleType);
                              _builder.newLineIfNotEmpty();
                              _builder.append("if (this.");
                              _builder.append(rolset1, "");
                              _builder.append(" == null) set");
                              String _firstUpper = StringExtensions.toFirstUpper(rolset1);
                              _builder.append(_firstUpper, "");
                              _builder.append("( new ");
                              _builder.append(ltype, "");
                              _builder.append("());");
                              _builder.newLineIfNotEmpty();
                              _builder.append("this.");
                              _builder.append(rolset1, "");
                              _builder.append(".addRoles(roles);");
                              _builder.newLineIfNotEmpty();
                            }
                          };
                          OceletJvmModelInferrer.this._jvmTypesBuilder.setBody(it, _client);
                        }
                      };
                      JvmOperation _method_13 = OceletJvmModelInferrer.this._jvmTypesBuilder.toMethod(meln, _plus_6, _typeRef_15, _function_14);
                      OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmOperation>operator_add(_members_16, _method_13);
                      EList<JvmMember> _members_17 = it.getMembers();
                      String _simpleName_3 = firstRoleType.getSimpleName();
                      String _plus_7 = ("removeAll" + _simpleName_3);
                      JvmTypeReference _typeRef_16 = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef(Void.TYPE);
                      final Procedure1<JvmOperation> _function_15 = new Procedure1<JvmOperation>() {
                        @Override
                        public void apply(final JvmOperation it) {
                          EList<JvmFormalParameter> _parameters = it.getParameters();
                          JvmTypeReference _typeRef = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("java.lang.Iterable", firstRoleType);
                          JvmFormalParameter _parameter = OceletJvmModelInferrer.this._jvmTypesBuilder.toParameter(meln, "roles", _typeRef);
                          OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                          StringConcatenationClient _client = new StringConcatenationClient() {
                            @Override
                            protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                              _builder.append("if (this.");
                              _builder.append(rolset1, "");
                              _builder.append(" != null) this.");
                              _builder.append(rolset1, "");
                              _builder.append(".removeRoles(roles);");
                            }
                          };
                          OceletJvmModelInferrer.this._jvmTypesBuilder.setBody(it, _client);
                        }
                      };
                      JvmOperation _method_14 = OceletJvmModelInferrer.this._jvmTypesBuilder.toMethod(meln, _plus_7, _typeRef_16, _function_15);
                      OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmOperation>operator_add(_members_17, _method_14);
                      if ((!isAutoGraph)) {
                        EList<JvmMember> _members_18 = it.getMembers();
                        String _simpleName_4 = secondRoleType.getSimpleName();
                        String _plus_8 = ("add" + _simpleName_4);
                        JvmTypeReference _typeRef_17 = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef(Void.TYPE);
                        final Procedure1<JvmOperation> _function_16 = new Procedure1<JvmOperation>() {
                          @Override
                          public void apply(final JvmOperation it) {
                            EList<JvmFormalParameter> _parameters = it.getParameters();
                            JvmFormalParameter _parameter = OceletJvmModelInferrer.this._jvmTypesBuilder.toParameter(meln, "role", secondRoleType);
                            OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                            StringConcatenationClient _client = new StringConcatenationClient() {
                              @Override
                              protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                final JvmTypeReference ltype = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("java.util.HashSet", secondRoleType);
                                _builder.newLineIfNotEmpty();
                                _builder.append("if (this.");
                                _builder.append(rolset2, "");
                                _builder.append(" == null) set");
                                String _firstUpper = StringExtensions.toFirstUpper(rolset2);
                                _builder.append(_firstUpper, "");
                                _builder.append("( new ");
                                _builder.append(ltype, "");
                                _builder.append("());");
                                _builder.newLineIfNotEmpty();
                                _builder.append("this.");
                                _builder.append(rolset2, "");
                                _builder.append(".addRole(role);");
                                _builder.newLineIfNotEmpty();
                              }
                            };
                            OceletJvmModelInferrer.this._jvmTypesBuilder.setBody(it, _client);
                          }
                        };
                        JvmOperation _method_15 = OceletJvmModelInferrer.this._jvmTypesBuilder.toMethod(meln, _plus_8, _typeRef_17, _function_16);
                        OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmOperation>operator_add(_members_18, _method_15);
                        EList<JvmMember> _members_19 = it.getMembers();
                        JvmTypeReference _typeRef_18 = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef(Void.TYPE);
                        final Procedure1<JvmOperation> _function_17 = new Procedure1<JvmOperation>() {
                          @Override
                          public void apply(final JvmOperation it) {
                            EList<JvmFormalParameter> _parameters = it.getParameters();
                            JvmFormalParameter _parameter = OceletJvmModelInferrer.this._jvmTypesBuilder.toParameter(meln, "role", secondRoleType);
                            OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                            StringConcatenationClient _client = new StringConcatenationClient() {
                              @Override
                              protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                _builder.append("remove");
                                _builder.append(secondRoleType, "");
                                _builder.append("(role);");
                              }
                            };
                            OceletJvmModelInferrer.this._jvmTypesBuilder.setBody(it, _client);
                          }
                        };
                        JvmOperation _method_16 = OceletJvmModelInferrer.this._jvmTypesBuilder.toMethod(meln, "remove", _typeRef_18, _function_17);
                        OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmOperation>operator_add(_members_19, _method_16);
                        EList<JvmMember> _members_20 = it.getMembers();
                        JvmTypeReference _typeRef_19 = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef(Void.TYPE);
                        final Procedure1<JvmOperation> _function_18 = new Procedure1<JvmOperation>() {
                          @Override
                          public void apply(final JvmOperation it) {
                            EList<JvmFormalParameter> _parameters = it.getParameters();
                            JvmFormalParameter _parameter = OceletJvmModelInferrer.this._jvmTypesBuilder.toParameter(meln, "role", secondRoleType);
                            OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                            StringConcatenationClient _client = new StringConcatenationClient() {
                              @Override
                              protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                _builder.append("add");
                                _builder.append(secondRoleType, "");
                                _builder.append("(role);");
                              }
                            };
                            OceletJvmModelInferrer.this._jvmTypesBuilder.setBody(it, _client);
                          }
                        };
                        JvmOperation _method_17 = OceletJvmModelInferrer.this._jvmTypesBuilder.toMethod(meln, "add", _typeRef_19, _function_18);
                        OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmOperation>operator_add(_members_20, _method_17);
                        EList<JvmMember> _members_21 = it.getMembers();
                        String _simpleName_5 = secondRoleType.getSimpleName();
                        String _plus_9 = ("remove" + _simpleName_5);
                        JvmTypeReference _typeRef_20 = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef(Void.TYPE);
                        final Procedure1<JvmOperation> _function_19 = new Procedure1<JvmOperation>() {
                          @Override
                          public void apply(final JvmOperation it) {
                            EList<JvmFormalParameter> _parameters = it.getParameters();
                            JvmFormalParameter _parameter = OceletJvmModelInferrer.this._jvmTypesBuilder.toParameter(meln, "role", secondRoleType);
                            OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                            StringConcatenationClient _client = new StringConcatenationClient() {
                              @Override
                              protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                _builder.append("if (this.");
                                _builder.append(rolset2, "");
                                _builder.append(" != null) this.");
                                _builder.append(rolset2, "");
                                _builder.append(".removeRole(role);");
                              }
                            };
                            OceletJvmModelInferrer.this._jvmTypesBuilder.setBody(it, _client);
                          }
                        };
                        JvmOperation _method_18 = OceletJvmModelInferrer.this._jvmTypesBuilder.toMethod(meln, _plus_9, _typeRef_20, _function_19);
                        OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmOperation>operator_add(_members_21, _method_18);
                        EList<JvmMember> _members_22 = it.getMembers();
                        String _simpleName_6 = secondRoleType.getSimpleName();
                        String _plus_10 = ("addAll" + _simpleName_6);
                        JvmTypeReference _typeRef_21 = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef(Void.TYPE);
                        final Procedure1<JvmOperation> _function_20 = new Procedure1<JvmOperation>() {
                          @Override
                          public void apply(final JvmOperation it) {
                            EList<JvmFormalParameter> _parameters = it.getParameters();
                            JvmTypeReference _typeRef = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("java.lang.Iterable", secondRoleType);
                            JvmFormalParameter _parameter = OceletJvmModelInferrer.this._jvmTypesBuilder.toParameter(meln, "roles", _typeRef);
                            OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                            StringConcatenationClient _client = new StringConcatenationClient() {
                              @Override
                              protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                final JvmTypeReference rtype = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("java.util.HashSet", secondRoleType);
                                _builder.newLineIfNotEmpty();
                                _builder.append("if (this.");
                                _builder.append(rolset2, "");
                                _builder.append(" == null) set");
                                String _firstUpper = StringExtensions.toFirstUpper(rolset2);
                                _builder.append(_firstUpper, "");
                                _builder.append("( new ");
                                _builder.append(rtype, "");
                                _builder.append("());");
                                _builder.newLineIfNotEmpty();
                                _builder.append("this.");
                                _builder.append(rolset2, "");
                                _builder.append(".addRoles(roles);");
                                _builder.newLineIfNotEmpty();
                              }
                            };
                            OceletJvmModelInferrer.this._jvmTypesBuilder.setBody(it, _client);
                          }
                        };
                        JvmOperation _method_19 = OceletJvmModelInferrer.this._jvmTypesBuilder.toMethod(meln, _plus_10, _typeRef_21, _function_20);
                        OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmOperation>operator_add(_members_22, _method_19);
                        EList<JvmMember> _members_23 = it.getMembers();
                        String _simpleName_7 = secondRoleType.getSimpleName();
                        String _plus_11 = ("removeAll" + _simpleName_7);
                        JvmTypeReference _typeRef_22 = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef(Void.TYPE);
                        final Procedure1<JvmOperation> _function_21 = new Procedure1<JvmOperation>() {
                          @Override
                          public void apply(final JvmOperation it) {
                            EList<JvmFormalParameter> _parameters = it.getParameters();
                            JvmTypeReference _typeRef = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("java.lang.Iterable", secondRoleType);
                            JvmFormalParameter _parameter = OceletJvmModelInferrer.this._jvmTypesBuilder.toParameter(meln, "roles", _typeRef);
                            OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                            StringConcatenationClient _client = new StringConcatenationClient() {
                              @Override
                              protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                _builder.append("if (this.");
                                _builder.append(rolset2, "");
                                _builder.append(" != null) this.");
                                _builder.append(rolset2, "");
                                _builder.append(".removeRoles(roles);");
                              }
                            };
                            OceletJvmModelInferrer.this._jvmTypesBuilder.setBody(it, _client);
                          }
                        };
                        JvmOperation _method_20 = OceletJvmModelInferrer.this._jvmTypesBuilder.toMethod(meln, _plus_11, _typeRef_22, _function_21);
                        OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmOperation>operator_add(_members_23, _method_20);
                      }
                    }
                    EList<RelElements> _relelns = ((Relation)meln).getRelelns();
                    for (final RelElements reln : _relelns) {
                      boolean _matched = false;
                      if (!_matched) {
                        if (reln instanceof RelPropertyDef) {
                          _matched=true;
                          EList<JvmMember> _members_24 = it.getMembers();
                          String _name_2 = ((RelPropertyDef)reln).getName();
                          String _firstUpper_4 = StringExtensions.toFirstUpper(_name_2);
                          String _plus_12 = ("set" + _firstUpper_4);
                          JvmTypeReference _typeRef_23 = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef(Void.TYPE);
                          final Procedure1<JvmOperation> _function_22 = new Procedure1<JvmOperation>() {
                            @Override
                            public void apply(final JvmOperation it) {
                              EList<JvmFormalParameter> _parameters = it.getParameters();
                              String _name = ((RelPropertyDef)reln).getName();
                              JvmTypeReference _type = ((RelPropertyDef)reln).getType();
                              JvmFormalParameter _parameter = OceletJvmModelInferrer.this._jvmTypesBuilder.toParameter(reln, _name, _type);
                              OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                              StringConcatenationClient _client = new StringConcatenationClient() {
                                @Override
                                protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                  final JvmTypeReference typ_edgecname = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef(edgecname);
                                  _builder.newLineIfNotEmpty();
                                  _builder.append("beginTransaction();");
                                  _builder.newLine();
                                  _builder.append("for(");
                                  _builder.append(typ_edgecname, "");
                                  _builder.append(" _edg_ : this)");
                                  _builder.newLineIfNotEmpty();
                                  _builder.append("  ");
                                  _builder.append("_edg_.set");
                                  String _name = ((RelPropertyDef)reln).getName();
                                  String _firstUpper = StringExtensions.toFirstUpper(_name);
                                  _builder.append(_firstUpper, "  ");
                                  _builder.append("(");
                                  String _name_1 = ((RelPropertyDef)reln).getName();
                                  _builder.append(_name_1, "  ");
                                  _builder.append(");");
                                  _builder.newLineIfNotEmpty();
                                  _builder.append("endTransaction();");
                                  _builder.newLine();
                                }
                              };
                              OceletJvmModelInferrer.this._jvmTypesBuilder.setBody(it, _client);
                            }
                          };
                          JvmOperation _method_21 = OceletJvmModelInferrer.this._jvmTypesBuilder.toMethod(reln, _plus_12, _typeRef_23, _function_22);
                          OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmOperation>operator_add(_members_24, _method_21);
                        }
                      }
                      if (!_matched) {
                        if (reln instanceof InteractionDef) {
                          _matched=true;
                          EList<JvmMember> _members_24 = it.getMembers();
                          String _name_2 = ((InteractionDef)reln).getName();
                          JvmTypeReference _typeRef_23 = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef(Void.TYPE);
                          final Procedure1<JvmOperation> _function_22 = new Procedure1<JvmOperation>() {
                            @Override
                            public void apply(final JvmOperation it) {
                              EList<JvmFormalParameter> _params = ((InteractionDef)reln).getParams();
                              for (final JvmFormalParameter p : _params) {
                                EList<JvmFormalParameter> _parameters = it.getParameters();
                                String _name = p.getName();
                                JvmTypeReference _parameterType = p.getParameterType();
                                JvmFormalParameter _parameter = OceletJvmModelInferrer.this._jvmTypesBuilder.toParameter(reln, _name, _parameterType);
                                OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                              }
                              StringConcatenationClient _client = new StringConcatenationClient() {
                                @Override
                                protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                  final JvmTypeReference typ_edgecname = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef(edgecname);
                                  _builder.newLineIfNotEmpty();
                                  _builder.append("beginTransaction();");
                                  _builder.newLine();
                                  int ci = 0;
                                  _builder.newLineIfNotEmpty();
                                  _builder.append("for(");
                                  _builder.append(typ_edgecname, "");
                                  _builder.append(" _edg_ : this) {");
                                  _builder.newLineIfNotEmpty();
                                  _builder.append("  ");
                                  _builder.append("_edg_.");
                                  String _name = ((InteractionDef)reln).getName();
                                  _builder.append(_name, "  ");
                                  _builder.append("(");
                                  {
                                    EList<JvmFormalParameter> _params = ((InteractionDef)reln).getParams();
                                    for(final JvmFormalParameter p : _params) {
                                      {
                                        if ((ci > 0)) {
                                          _builder.append(",");
                                        }
                                      }
                                      String _name_1 = p.getName();
                                      _builder.append(_name_1, "  ");
                                      _builder.append(ci = 1, "  ");
                                    }
                                  }
                                  _builder.append(");");
                                  _builder.newLineIfNotEmpty();
                                  {
                                    EList<Comitexpr> _comitexpressions = ((InteractionDef)reln).getComitexpressions();
                                    int _size = _comitexpressions.size();
                                    boolean _greaterThan = (_size > 0);
                                    if (_greaterThan) {
                                      _builder.append(" ");
                                      _builder.append("_edg_._agr_");
                                      String _name_2 = ((InteractionDef)reln).getName();
                                      _builder.append(_name_2, " ");
                                      _builder.append("();");
                                    }
                                  }
                                  _builder.newLineIfNotEmpty();
                                  _builder.append("}");
                                  _builder.newLine();
                                  _builder.append("endTransaction();");
                                  _builder.newLine();
                                }
                              };
                              OceletJvmModelInferrer.this._jvmTypesBuilder.setBody(it, _client);
                            }
                          };
                          JvmOperation _method_21 = OceletJvmModelInferrer.this._jvmTypesBuilder.toMethod(reln, _name_2, _typeRef_23, _function_22);
                          OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmOperation>operator_add(_members_24, _method_21);
                        }
                      }
                      if (!_matched) {
                        if (reln instanceof Filterdef) {
                          _matched=true;
                          EList<JvmMember> _members_24 = it.getMembers();
                          String _name_2 = ((Filterdef)reln).getName();
                          String _string_3 = graphcname.toString();
                          JvmTypeReference _typeRef_23 = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef(_string_3);
                          final Procedure1<JvmOperation> _function_22 = new Procedure1<JvmOperation>() {
                            @Override
                            public void apply(final JvmOperation it) {
                              EList<JvmFormalParameter> _params = ((Filterdef)reln).getParams();
                              for (final JvmFormalParameter p : _params) {
                                EList<JvmFormalParameter> _parameters = it.getParameters();
                                String _name = p.getName();
                                JvmTypeReference _parameterType = p.getParameterType();
                                JvmFormalParameter _parameter = OceletJvmModelInferrer.this._jvmTypesBuilder.toParameter(reln, _name, _parameterType);
                                OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                              }
                              StringConcatenationClient _client = new StringConcatenationClient() {
                                @Override
                                protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                  String _name = ((Relation)meln).getName();
                                  String _plus = (_name + "_");
                                  String _name_1 = ((Filterdef)reln).getName();
                                  String _plus_1 = (_plus + _name_1);
                                  _builder.append(_plus_1, "");
                                  _builder.append(" _filter = new ");
                                  String _name_2 = ((Relation)meln).getName();
                                  String _plus_2 = (_name_2 + "_");
                                  String _name_3 = ((Filterdef)reln).getName();
                                  String _plus_3 = (_plus_2 + _name_3);
                                  _builder.append(_plus_3, "");
                                  _builder.append("(");
                                  {
                                    EList<JvmFormalParameter> _params = ((Filterdef)reln).getParams();
                                    int _size = _params.size();
                                    boolean _greaterThan = (_size > 0);
                                    if (_greaterThan) {
                                      {
                                        EList<JvmFormalParameter> _params_1 = ((Filterdef)reln).getParams();
                                        int _size_1 = _params_1.size();
                                        int _minus = (_size_1 - 1);
                                        IntegerRange _upTo = new IntegerRange(0, _minus);
                                        for(final Integer i : _upTo) {
                                          EList<JvmFormalParameter> _params_2 = ((Filterdef)reln).getParams();
                                          JvmFormalParameter _get = _params_2.get((i).intValue());
                                          String _name_4 = _get.getName();
                                          _builder.append(_name_4, "");
                                          {
                                            EList<JvmFormalParameter> _params_3 = ((Filterdef)reln).getParams();
                                            int _size_2 = _params_3.size();
                                            int _minus_1 = (_size_2 - 1);
                                            boolean _lessThan = ((i).intValue() < _minus_1);
                                            if (_lessThan) {
                                              _builder.append(",");
                                            }
                                          }
                                        }
                                      }
                                    }
                                  }
                                  _builder.append(");");
                                  _builder.newLineIfNotEmpty();
                                  _builder.append("super.addFilter(_filter);");
                                  _builder.newLine();
                                  _builder.append("return this;");
                                  _builder.newLine();
                                }
                              };
                              OceletJvmModelInferrer.this._jvmTypesBuilder.setBody(it, _client);
                            }
                          };
                          JvmOperation _method_21 = OceletJvmModelInferrer.this._jvmTypesBuilder.toMethod(reln, _name_2, _typeRef_23, _function_22);
                          OceletJvmModelInferrer.this._jvmTypesBuilder.<JvmOperation>operator_add(_members_24, _method_21);
                        }
                      }
                    }
                  }
                };
                acceptor.<JvmGenericType>accept(_class_1, _function_1);
              }
            }
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
                    StringConcatenationClient _client = new StringConcatenationClient() {
                      @Override
                      protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                        _builder.append("super();");
                        _builder.newLine();
                        {
                          for(final StrucVarDef vardef : lvdefs) {
                            _builder.append("                  ");
                            JvmTypeReference vtyp = vardef.getType();
                            _builder.newLineIfNotEmpty();
                            {
                              boolean _isPrimitive = OceletJvmModelInferrer.this._primitives.isPrimitive(vtyp);
                              boolean _not = (!_isPrimitive);
                              if (_not) {
                                _builder.append("                  ");
                                String _name = vardef.getName();
                                _builder.append(_name, "                  ");
                                _builder.append(" = new ");
                                _builder.append(vtyp, "                  ");
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
                                    _builder.append("(\"0\");");
                                    _builder.newLineIfNotEmpty();
                                    _builder.append("                  ");
                                    _builder.append("  ");
                                  } else {
                                    String _qualifiedName_6 = vtyp.getQualifiedName();
                                    boolean _equals_6 = _qualifiedName_6.equals("java.lang.Boolean");
                                    if (_equals_6) {
                                      _builder.append("(false);");
                                    }
                                  }
                                }
                                _builder.newLineIfNotEmpty();
                                _builder.append("                  ");
                                _builder.append("  ");
                              } else {
                                _builder.append("();");
                                _builder.newLineIfNotEmpty();
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
