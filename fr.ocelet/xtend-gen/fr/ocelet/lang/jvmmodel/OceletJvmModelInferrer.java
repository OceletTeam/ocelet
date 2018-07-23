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
package fr.ocelet.lang.jvmmodel;

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
import java.util.Set;
import org.eclipse.emf.common.util.EList;
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

/**
 * Java code inferrer of the Ocelet language
 * 
 * @author Pascal Degenne - Initial contribution
 * @author Mathieu Castets - Raster related code generator
 */
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
    final String modlName = res.getURI().segment(1);
    String _name = modl.getName();
    boolean _tripleEquals = (_name == null);
    if (_tripleEquals) {
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
        if (meln instanceof Scenario) {
          _matched=true;
          int _compareTo = ((Scenario)meln).getName().compareTo(modlName);
          boolean _equals = (_compareTo == 0);
          if (_equals) {
            mainScen = true;
          }
          scens.add(((Scenario)meln));
        }
        if (!_matched) {
          if (meln instanceof Metadata) {
            _matched=true;
            EList<Parameter> _paramdefs = ((Metadata)meln).getParamdefs();
            boolean _tripleNotEquals = (_paramdefs != null);
            if (_tripleNotEquals) {
              md.setModeldesc(((Metadata)meln).getDesc());
              md.setWebpage(((Metadata)meln).getWebp());
              EList<Parameter> _paramdefs_1 = ((Metadata)meln).getParamdefs();
              for (final Parameter paramdef : _paramdefs_1) {
                {
                  String _name_2 = paramdef.getName();
                  JvmTypeReference _ptype = paramdef.getPtype();
                  final Parameterstuff pst = new Parameterstuff(_name_2, _ptype);
                  EList<Parampart> _paramparts = paramdef.getParamparts();
                  for (final Parampart ppart : _paramparts) {
                    boolean _matched_1 = false;
                    if (ppart instanceof Paramunit) {
                      _matched_1=true;
                      pst.setUnit(((Paramunit)ppart).getParunit());
                    }
                    if (!_matched_1) {
                      if (ppart instanceof Paramdefa) {
                        _matched_1=true;
                        pst.setDefvalue(((Paramdefa)ppart).getPardefa());
                      }
                    }
                    if (!_matched_1) {
                      if (ppart instanceof Rangevals) {
                        _matched_1=true;
                        pst.setMin(((Rangevals)ppart).getParmin());
                        pst.setMax(((Rangevals)ppart).getParmax());
                      }
                    }
                    if (!_matched_1) {
                      if (ppart instanceof Paradesc) {
                        _matched_1=true;
                        pst.setDescription(((Paradesc)ppart).getPardesc());
                      }
                    }
                    if (!_matched_1) {
                      if (ppart instanceof Paraopt) {
                        _matched_1=true;
                        int _compareToIgnoreCase = ((Paraopt)ppart).getParopt().compareToIgnoreCase("true");
                        boolean _equals = (_compareToIgnoreCase == 0);
                        pst.setOptionnal(Boolean.valueOf(_equals));
                      }
                    }
                  }
                  md.getParams().add(pst);
                }
              }
            }
          }
        }
        if (!_matched) {
          if (meln instanceof Datafacer) {
            _matched=true;
            final Procedure1<JvmGenericType> _function = (JvmGenericType it) -> {
              try {
                EList<JvmTypeReference> _superTypes = it.getSuperTypes();
                String _storetype = ((Datafacer)meln).getStoretype();
                String _plus_1 = ("fr.ocelet.datafacer.ocltypes." + _storetype);
                JvmTypeReference _typeRef = this._typeReferenceBuilder.typeRef(_plus_1);
                this._jvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes, _typeRef);
                EList<JvmMember> _members = it.getMembers();
                final Procedure1<JvmConstructor> _function_1 = (JvmConstructor it_1) -> {
                  final Procedure1<ITreeAppendable> _function_2 = (ITreeAppendable it_2) -> {
                    StringConcatenation _builder = new StringConcatenation();
                    _builder.append("super(");
                    it_2.append(_builder);
                    int carg = 0;
                    EList<XExpression> _arguments = ((Datafacer)meln).getArguments();
                    for (final XExpression arg : _arguments) {
                      {
                        if ((carg > 0)) {
                          StringConcatenation _builder_1 = new StringConcatenation();
                          _builder_1.append(",");
                          it_2.append(_builder_1);
                        }
                        this.ocltCompiler.compileDatafacerParamExpression(arg, it_2);
                        carg = (carg + 1);
                      }
                    }
                    StringConcatenation _builder_1 = new StringConcatenation();
                    _builder_1.append(");");
                    it_2.append(_builder_1);
                  };
                  this._jvmTypesBuilder.setBody(it_1, _function_2);
                };
                JvmConstructor _constructor = this._jvmTypesBuilder.toConstructor(meln, _function_1);
                this._jvmTypesBuilder.<JvmConstructor>operator_add(_members, _constructor);
                boolean isFirst = true;
                EList<Match> _matchbox = ((Datafacer)meln).getMatchbox();
                for (final Match matchdef : _matchbox) {
                  {
                    final Matchtype mt = matchdef.getMtype();
                    if ((mt != null)) {
                      boolean _matched_1 = false;
                      if (mt instanceof Entity) {
                        _matched_1=true;
                        final JvmTypeReference entype = this._typeReferenceBuilder.typeRef(this._iQualifiedNameProvider.getFullyQualifiedName(mt).toString());
                        final String entname = StringExtensions.toFirstUpper(((Entity)mt).getName());
                        final JvmTypeReference listype = this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.ocltypes.List", entype);
                        final HashMap<String, String> propmap = new HashMap<String, String>();
                        final HashMap<String, String> propmapf = new HashMap<String, String>();
                        EList<EntityElements> _entelns = ((Entity)mt).getEntelns();
                        for (final EntityElements eprop : _entelns) {
                          boolean _matched_2 = false;
                          if (eprop instanceof PropertyDef) {
                            _matched_2=true;
                            JvmTypeReference _type = ((PropertyDef)eprop).getType();
                            boolean _tripleNotEquals = (_type != null);
                            if (_tripleNotEquals) {
                              propmap.put(((PropertyDef)eprop).getName(), ((PropertyDef)eprop).getType().getSimpleName());
                              propmapf.put(((PropertyDef)eprop).getName(), ((PropertyDef)eprop).getType().getQualifiedName());
                            }
                          }
                        }
                        String _storetype_1 = ((Datafacer)meln).getStoretype();
                        String _plus_2 = ("" + _storetype_1);
                        boolean _equals = "RasterFile".equals(_plus_2);
                        if (_equals) {
                          final JvmTypeReference tabType = this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.ocltypes.List", entype);
                          EList<JvmMember> _members_1 = it.getMembers();
                          final Procedure1<JvmOperation> _function_2 = (JvmOperation it_1) -> {
                            StringConcatenationClient _client = new StringConcatenationClient() {
                              @Override
                              protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                _builder.append(entname);
                                _builder.append(" entity = new ");
                                _builder.append(entname);
                                _builder.append("();");
                                _builder.newLineIfNotEmpty();
                                {
                                  EList<Mdef> _matchprops = matchdef.getMatchprops();
                                  for(final Mdef mp : _matchprops) {
                                    final String eproptype = propmap.get(mp.getProp());
                                    _builder.newLineIfNotEmpty();
                                    {
                                      if ((eproptype != null)) {
                                        {
                                          String _colname = mp.getColname();
                                          boolean _tripleNotEquals = (_colname != null);
                                          if (_tripleNotEquals) {
                                            _builder.append("addProperty(\"");
                                            String _prop = mp.getProp();
                                            _builder.append(_prop);
                                            _builder.append("\",");
                                            String _colname_1 = mp.getColname();
                                            _builder.append(_colname_1);
                                            _builder.append(");");
                                            _builder.newLineIfNotEmpty();
                                          }
                                        }
                                      }
                                    }
                                  }
                                }
                                _builder.append("this.grid = createGrid(entity.getProps(), \"");
                                _builder.append(entname);
                                _builder.append("\");");
                                _builder.newLineIfNotEmpty();
                                _builder.append("((");
                                JvmTypeReference _typeRef = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.geom.ocltypes.Cell");
                                _builder.append(_typeRef);
                                _builder.append(")entity.getSpatialType()).setGrid(grid);");
                                _builder.newLineIfNotEmpty();
                                _builder.append("List<");
                                _builder.append(entname);
                                _builder.append("> entityList = new List<");
                                _builder.append(entname);
                                _builder.append(">();");
                                _builder.newLineIfNotEmpty();
                                _builder.append("entityList.cellCut();");
                                _builder.newLine();
                                _builder.append("entityList.add(entity);");
                                _builder.newLine();
                                _builder.newLine();
                                _builder.append("return entityList;");
                                _builder.newLine();
                              }
                            };
                            this._jvmTypesBuilder.setBody(it_1, _client);
                          };
                          JvmOperation _method = this._jvmTypesBuilder.toMethod(meln, ("readAll" + entname), tabType, _function_2);
                          this._jvmTypesBuilder.<JvmOperation>operator_add(_members_1, _method);
                          EList<JvmMember> _members_2 = it.getMembers();
                          final Procedure1<JvmOperation> _function_3 = (JvmOperation it_1) -> {
                            EList<JvmFormalParameter> _parameters = it_1.getParameters();
                            JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "shp", this._typeReferenceBuilder.typeRef("fr.ocelet.datafacer.ocltypes.Shapefile"));
                            this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                            StringConcatenationClient _client = new StringConcatenationClient() {
                              @Override
                              protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                _builder.append("                  \t    ");
                                _builder.append(entname, "                  \t    ");
                                _builder.append(" entity = new ");
                                _builder.append(entname, "                  \t    ");
                                _builder.append("();");
                                _builder.newLineIfNotEmpty();
                                {
                                  EList<Mdef> _matchprops = matchdef.getMatchprops();
                                  for(final Mdef mp : _matchprops) {
                                    final String eproptype = propmap.get(mp.getProp());
                                    _builder.newLineIfNotEmpty();
                                    {
                                      if ((eproptype != null)) {
                                        {
                                          String _colname = mp.getColname();
                                          boolean _tripleNotEquals = (_colname != null);
                                          if (_tripleNotEquals) {
                                            _builder.append("addProperty(\"");
                                            String _prop = mp.getProp();
                                            _builder.append(_prop);
                                            _builder.append("\",");
                                            String _colname_1 = mp.getColname();
                                            _builder.append(_colname_1);
                                            _builder.append(");");
                                            _builder.newLineIfNotEmpty();
                                          }
                                        }
                                      }
                                    }
                                  }
                                }
                                _builder.append("this.grid = createGrid(entity.getProps(), shp, \t\"");
                                _builder.append(entname);
                                _builder.append("\");");
                                _builder.newLineIfNotEmpty();
                                _builder.append("((");
                                JvmTypeReference _typeRef = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.geom.ocltypes.Cell");
                                _builder.append(_typeRef);
                                _builder.append(")entity.getSpatialType()).setGrid(grid);");
                                _builder.newLineIfNotEmpty();
                                _builder.append("List<");
                                _builder.append(entname);
                                _builder.append("> entityList = new List<");
                                _builder.append(entname);
                                _builder.append(">();");
                                _builder.newLineIfNotEmpty();
                                _builder.append("entityList.cellCut();");
                                _builder.newLine();
                                _builder.append("entityList.add(entity);");
                                _builder.newLine();
                                _builder.append("return entityList;");
                                _builder.newLine();
                              }
                            };
                            this._jvmTypesBuilder.setBody(it_1, _client);
                          };
                          JvmOperation _method_1 = this._jvmTypesBuilder.toMethod(meln, ("readAll" + entname), tabType, _function_3);
                          this._jvmTypesBuilder.<JvmOperation>operator_add(_members_2, _method_1);
                          EList<JvmMember> _members_3 = it.getMembers();
                          final Procedure1<JvmOperation> _function_4 = (JvmOperation it_1) -> {
                            EList<JvmFormalParameter> _parameters = it_1.getParameters();
                            JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "geometry", this._typeReferenceBuilder.typeRef("com.vividsolutions.jts.geom.Geometry"));
                            this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                            StringConcatenationClient _client = new StringConcatenationClient() {
                              @Override
                              protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                _builder.append(entname);
                                _builder.append(" entity = new ");
                                _builder.append(entname);
                                _builder.append("();");
                                _builder.newLineIfNotEmpty();
                                {
                                  EList<Mdef> _matchprops = matchdef.getMatchprops();
                                  for(final Mdef mp : _matchprops) {
                                    final String eproptype = propmap.get(mp.getProp());
                                    _builder.newLineIfNotEmpty();
                                    {
                                      if ((eproptype != null)) {
                                        {
                                          String _colname = mp.getColname();
                                          boolean _tripleNotEquals = (_colname != null);
                                          if (_tripleNotEquals) {
                                            _builder.append("addProperty(\"");
                                            String _prop = mp.getProp();
                                            _builder.append(_prop);
                                            _builder.append("\",");
                                            String _colname_1 = mp.getColname();
                                            _builder.append(_colname_1);
                                            _builder.append(");");
                                            _builder.newLineIfNotEmpty();
                                          }
                                        }
                                      }
                                    }
                                  }
                                }
                                _builder.append("this.grid = createGrid(entity.getProps(), geometry, \t\"");
                                _builder.append(entname);
                                _builder.append("\");");
                                _builder.newLineIfNotEmpty();
                                _builder.append("entity.updateCellInfo(\"QUADRILATERAL\");");
                                _builder.newLine();
                                _builder.append("((");
                                JvmTypeReference _typeRef = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.geom.ocltypes.Cell");
                                _builder.append(_typeRef);
                                _builder.append(")entity.getSpatialType()).setGrid(grid);");
                                _builder.newLineIfNotEmpty();
                                _builder.append("List<");
                                _builder.append(entname);
                                _builder.append("> entityList = new List<");
                                _builder.append(entname);
                                _builder.append(">();");
                                _builder.newLineIfNotEmpty();
                                _builder.append("entityList.cellCut();");
                                _builder.newLine();
                                _builder.append("entityList.add(entity);");
                                _builder.newLine();
                                _builder.append("return entityList;");
                                _builder.newLine();
                              }
                            };
                            this._jvmTypesBuilder.setBody(it_1, _client);
                          };
                          JvmOperation _method_2 = this._jvmTypesBuilder.toMethod(meln, ("readAll" + entname), tabType, _function_4);
                          this._jvmTypesBuilder.<JvmOperation>operator_add(_members_3, _method_2);
                        } else {
                          String _storetype_2 = ((Datafacer)meln).getStoretype();
                          String _plus_3 = ("fr.ocelet.datafacer.ocltypes." + _storetype_2);
                          boolean _isAssignableFrom = Class.forName("fr.ocelet.datafacer.InputDatafacer").isAssignableFrom(Class.forName(_plus_3));
                          if (_isAssignableFrom) {
                            final JvmTypeReference inputRecordType = this._typeReferenceBuilder.typeRef("fr.ocelet.datafacer.InputDataRecord");
                            EList<JvmMember> _members_4 = it.getMembers();
                            final Procedure1<JvmOperation> _function_5 = (JvmOperation it_1) -> {
                              StringConcatenationClient _client = new StringConcatenationClient() {
                                @Override
                                protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                  _builder.append("List<");
                                  _builder.append(entname);
                                  _builder.append("> _elist = new List<");
                                  _builder.append(entname);
                                  _builder.append(">();");
                                  _builder.newLineIfNotEmpty();
                                  _builder.append("for (");
                                  _builder.append(inputRecordType);
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
                                  _builder.append("resetIterator();");
                                  _builder.newLine();
                                  _builder.append("return _elist;");
                                  _builder.newLine();
                                }
                              };
                              this._jvmTypesBuilder.setBody(it_1, _client);
                            };
                            JvmOperation _method_3 = this._jvmTypesBuilder.toMethod(meln, ("readAll" + entname), listype, _function_5);
                            this._jvmTypesBuilder.<JvmOperation>operator_add(_members_4, _method_3);
                            if (isFirst) {
                              EList<JvmMember> _members_5 = it.getMembers();
                              final Procedure1<JvmOperation> _function_6 = (JvmOperation it_1) -> {
                                StringConcatenationClient _client = new StringConcatenationClient() {
                                  @Override
                                  protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                    _builder.append("return readAll");
                                    _builder.append(entname);
                                    _builder.append("();");
                                  }
                                };
                                this._jvmTypesBuilder.setBody(it_1, _client);
                              };
                              JvmOperation _method_4 = this._jvmTypesBuilder.toMethod(meln, "readAll", listype, _function_6);
                              this._jvmTypesBuilder.<JvmOperation>operator_add(_members_5, _method_4);
                            }
                            EList<JvmMember> _members_6 = it.getMembers();
                            final Procedure1<JvmOperation> _function_7 = (JvmOperation it_1) -> {
                              EList<JvmFormalParameter> _parameters = it_1.getParameters();
                              JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "_rec", this._typeReferenceBuilder.typeRef("fr.ocelet.datafacer.InputDataRecord"));
                              this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
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
                                      final String eproptype = propmap.get(mp.getProp());
                                      _builder.newLineIfNotEmpty();
                                      {
                                        if ((eproptype != null)) {
                                          {
                                            String _colname = mp.getColname();
                                            boolean _tripleNotEquals = (_colname != null);
                                            if (_tripleNotEquals) {
                                              _builder.append("_entity.setProperty(\"");
                                              String _prop = mp.getProp();
                                              _builder.append(_prop);
                                              _builder.append("\",read");
                                              _builder.append(eproptype);
                                              _builder.append("(\"");
                                              String _colname_1 = mp.getColname();
                                              _builder.append(_colname_1);
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
                              this._jvmTypesBuilder.setBody(it_1, _client);
                            };
                            JvmOperation _method_5 = this._jvmTypesBuilder.toMethod(meln, (("create" + entname) + "FromRecord"), entype, _function_7);
                            this._jvmTypesBuilder.<JvmOperation>operator_add(_members_6, _method_5);
                            final JvmTypeReference hmtype = this._typeReferenceBuilder.typeRef("java.util.HashMap", this._typeReferenceBuilder.typeRef("java.lang.String"), this._typeReferenceBuilder.typeRef("java.lang.String"));
                            EList<JvmMember> _members_7 = it.getMembers();
                            final Procedure1<JvmOperation> _function_8 = (JvmOperation it_1) -> {
                              StringConcatenationClient _client = new StringConcatenationClient() {
                                @Override
                                protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                  String _simpleName = hmtype.getSimpleName();
                                  _builder.append(_simpleName);
                                  _builder.append(" hm = new ");
                                  _builder.append(hmtype);
                                  _builder.append("();");
                                  _builder.newLineIfNotEmpty();
                                  {
                                    EList<Mdef> _matchprops = matchdef.getMatchprops();
                                    for(final Mdef mp : _matchprops) {
                                      _builder.append("                  \t  \t  ");
                                      final String epropftype = propmapf.get(mp.getProp());
                                      _builder.newLineIfNotEmpty();
                                      {
                                        if ((epropftype != null)) {
                                          {
                                            String _colname = mp.getColname();
                                            boolean _tripleNotEquals = (_colname != null);
                                            if (_tripleNotEquals) {
                                              _builder.append("hm.put(\"");
                                              String _colname_1 = mp.getColname();
                                              _builder.append(_colname_1);
                                              _builder.append("\",\"");
                                              _builder.append(epropftype);
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
                              this._jvmTypesBuilder.setBody(it_1, _client);
                            };
                            JvmOperation _method_6 = this._jvmTypesBuilder.toMethod(meln, "getMatchdef", hmtype, _function_8);
                            this._jvmTypesBuilder.<JvmOperation>operator_add(_members_7, _method_6);
                          }
                          String _storetype_3 = ((Datafacer)meln).getStoretype();
                          String _plus_4 = ("fr.ocelet.datafacer.ocltypes." + _storetype_3);
                          boolean _isAssignableFrom_1 = Class.forName("fr.ocelet.datafacer.FiltrableDatafacer").isAssignableFrom(Class.forName(_plus_4));
                          if (_isAssignableFrom_1) {
                            EList<JvmMember> _members_8 = it.getMembers();
                            final Procedure1<JvmOperation> _function_9 = (JvmOperation it_1) -> {
                              EList<JvmFormalParameter> _parameters = it_1.getParameters();
                              JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "_filt", this._typeReferenceBuilder.typeRef("java.lang.String"));
                              this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                              StringConcatenationClient _client = new StringConcatenationClient() {
                                @Override
                                protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                  _builder.append("setFilter(_filt);");
                                  _builder.newLine();
                                  _builder.append("return readAll");
                                  _builder.append(entname);
                                  _builder.append("();");
                                  _builder.newLineIfNotEmpty();
                                }
                              };
                              this._jvmTypesBuilder.setBody(it_1, _client);
                            };
                            JvmOperation _method_7 = this._jvmTypesBuilder.toMethod(meln, ("readFiltered" + entname), listype, _function_9);
                            this._jvmTypesBuilder.<JvmOperation>operator_add(_members_8, _method_7);
                          }
                          String _storetype_4 = ((Datafacer)meln).getStoretype();
                          String _plus_5 = ("fr.ocelet.datafacer.ocltypes." + _storetype_4);
                          boolean _isAssignableFrom_2 = Class.forName("fr.ocelet.datafacer.OutputDatafacer").isAssignableFrom(Class.forName(_plus_5));
                          if (_isAssignableFrom_2) {
                            EList<JvmMember> _members_9 = it.getMembers();
                            final Procedure1<JvmOperation> _function_10 = (JvmOperation it_1) -> {
                              EList<JvmFormalParameter> _parameters = it_1.getParameters();
                              JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "ety", this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.entity.Entity"));
                              this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                              EList<JvmTypeReference> _exceptions = it_1.getExceptions();
                              JvmTypeReference _typeRef_1 = this._typeReferenceBuilder.typeRef("java.lang.IllegalArgumentException");
                              this._jvmTypesBuilder.<JvmTypeReference>operator_add(_exceptions, _typeRef_1);
                              StringConcatenationClient _client = new StringConcatenationClient() {
                                @Override
                                protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                  final JvmTypeReference odrtype = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("fr.ocelet.datafacer.OutputDataRecord");
                                  _builder.newLineIfNotEmpty();
                                  _builder.append(odrtype);
                                  _builder.append(" odr = createOutputDataRec();");
                                  _builder.newLineIfNotEmpty();
                                  _builder.append("if (odr != null) {");
                                  _builder.newLine();
                                  {
                                    EList<Mdef> _matchprops = matchdef.getMatchprops();
                                    for(final Mdef mp : _matchprops) {
                                      _builder.append("odr.setAttribute(\"");
                                      String _colname = mp.getColname();
                                      _builder.append(_colname);
                                      _builder.append("\",((");
                                      _builder.append(entname);
                                      _builder.append(") ety).get");
                                      String _firstUpper = StringExtensions.toFirstUpper(mp.getProp());
                                      _builder.append(_firstUpper);
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
                              this._jvmTypesBuilder.setBody(it_1, _client);
                            };
                            JvmOperation _method_8 = this._jvmTypesBuilder.toMethod(meln, "createRecord", this._typeReferenceBuilder.typeRef("fr.ocelet.datafacer.OutputDataRecord"), _function_10);
                            this._jvmTypesBuilder.<JvmOperation>operator_add(_members_9, _method_8);
                          }
                          String _storetype_5 = ((Datafacer)meln).getStoretype();
                          String _plus_6 = ("fr.ocelet.datafacer.ocltypes." + _storetype_5);
                          boolean _isAssignableFrom_3 = Class.forName("fr.ocelet.datafacer.ocltypes.Csvfile").isAssignableFrom(Class.forName(_plus_6));
                          if (_isAssignableFrom_3) {
                            EList<JvmMember> _members_10 = it.getMembers();
                            final Procedure1<JvmOperation> _function_11 = (JvmOperation it_1) -> {
                              StringConcatenationClient _client = new StringConcatenationClient() {
                                @Override
                                protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                  _builder.append("StringBuffer sb = new StringBuffer();");
                                  _builder.newLine();
                                  int coma = 0;
                                  _builder.newLineIfNotEmpty();
                                  {
                                    EList<Mdef> _matchprops = matchdef.getMatchprops();
                                    for(final Mdef mp : _matchprops) {
                                      {
                                        int _plusPlus = coma++;
                                        boolean _greaterThan = (_plusPlus > 0);
                                        if (_greaterThan) {
                                          _builder.append("sb.append(separator);");
                                        }
                                      }
                                      _builder.append("                     ");
                                      _builder.newLineIfNotEmpty();
                                      _builder.append("sb.append(\"");
                                      String _colname = mp.getColname();
                                      _builder.append(_colname);
                                      _builder.append("\");");
                                      _builder.newLineIfNotEmpty();
                                    }
                                  }
                                  _builder.append("return sb.toString();");
                                  _builder.newLine();
                                }
                              };
                              this._jvmTypesBuilder.setBody(it_1, _client);
                            };
                            JvmOperation _method_9 = this._jvmTypesBuilder.toMethod(meln, "headerString", this._typeReferenceBuilder.typeRef("java.lang.String"), _function_11);
                            this._jvmTypesBuilder.<JvmOperation>operator_add(_members_10, _method_9);
                            EList<JvmMember> _members_11 = it.getMembers();
                            final Procedure1<JvmOperation> _function_12 = (JvmOperation it_1) -> {
                              EList<JvmFormalParameter> _parameters = it_1.getParameters();
                              JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "_entity", this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.entity.Entity"));
                              this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                              StringConcatenationClient _client = new StringConcatenationClient() {
                                @Override
                                protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                  _builder.append("StringBuffer sb = new StringBuffer();");
                                  _builder.newLine();
                                  int coma = 0;
                                  _builder.newLineIfNotEmpty();
                                  {
                                    EList<Mdef> _matchprops = matchdef.getMatchprops();
                                    for(final Mdef mp : _matchprops) {
                                      {
                                        int _plusPlus = coma++;
                                        boolean _greaterThan = (_plusPlus > 0);
                                        if (_greaterThan) {
                                          _builder.append("sb.append(separator);");
                                        }
                                      }
                                      _builder.append("                     ");
                                      _builder.newLineIfNotEmpty();
                                      _builder.append("sb.append(_entity.getProperty(\"");
                                      String _prop = mp.getProp();
                                      _builder.append(_prop);
                                      _builder.append("\").toString());");
                                      _builder.newLineIfNotEmpty();
                                    }
                                  }
                                  _builder.append("return sb.toString();");
                                  _builder.newLine();
                                }
                              };
                              this._jvmTypesBuilder.setBody(it_1, _client);
                            };
                            JvmOperation _method_10 = this._jvmTypesBuilder.toMethod(meln, "propsString", this._typeReferenceBuilder.typeRef("java.lang.String"), _function_12);
                            this._jvmTypesBuilder.<JvmOperation>operator_add(_members_11, _method_10);
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
            };
            acceptor.<JvmGenericType>accept(this._jvmTypesBuilder.toClass(modl, this._iQualifiedNameProvider.getFullyQualifiedName(meln)), _function);
          }
        }
        if (!_matched) {
          if (meln instanceof Entity) {
            _matched=true;
            final Procedure1<JvmGenericType> _function = (JvmGenericType it) -> {
              this._jvmTypesBuilder.setDocumentation(it, this._jvmTypesBuilder.getDocumentation(meln));
              EList<JvmTypeReference> _superTypes = it.getSuperTypes();
              JvmTypeReference _typeRef = this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.entity.AbstractEntity");
              this._jvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes, _typeRef);
              final List<PropertyDef> lpropdefs = CollectionLiterals.<PropertyDef>newArrayList();
              final List<String> cellProps = CollectionLiterals.<String>newArrayList();
              final HashMap<String, String> typeProps = CollectionLiterals.<String, String>newHashMap();
              boolean isCell = false;
              String cellNameTemp = "";
              int index = 0;
              EList<EntityElements> _entelns = ((Entity)meln).getEntelns();
              for (final EntityElements enteln : _entelns) {
                boolean _matched_1 = false;
                if (enteln instanceof PropertyDef) {
                  _matched_1=true;
                  String _name_2 = ((PropertyDef)enteln).getName();
                  boolean _tripleNotEquals = (_name_2 != null);
                  if (_tripleNotEquals) {
                    boolean _equals = ((PropertyDef)enteln).getType().getSimpleName().equals("Cell");
                    if (_equals) {
                      isCell = true;
                      cellNameTemp = ((PropertyDef)enteln).getName();
                    }
                  }
                }
              }
              final String cellName = cellNameTemp;
              EList<EntityElements> _entelns_1 = ((Entity)meln).getEntelns();
              for (final EntityElements enteln_1 : _entelns_1) {
                boolean _matched_2 = false;
                if (enteln_1 instanceof PropertyDef) {
                  _matched_2=true;
                  String _name_2 = ((PropertyDef)enteln_1).getName();
                  boolean _tripleNotEquals = (_name_2 != null);
                  if (_tripleNotEquals) {
                    lpropdefs.add(((PropertyDef)enteln_1));
                    boolean _equals = ((PropertyDef)enteln_1).getType().getSimpleName().equals("Cell");
                    boolean _not = (!_equals);
                    if (_not) {
                      cellProps.add(((PropertyDef)enteln_1).getName());
                      typeProps.put(((PropertyDef)enteln_1).getName(), ((PropertyDef)enteln_1).getType().getSimpleName());
                    }
                    final int fIndex = index;
                    if (isCell) {
                      boolean _equals_1 = ((PropertyDef)enteln_1).getType().getSimpleName().equals("Cell");
                      boolean _not_1 = (!_equals_1);
                      if (_not_1) {
                        EList<JvmMember> _members = it.getMembers();
                        String _firstUpper = StringExtensions.toFirstUpper(((PropertyDef)enteln_1).getName());
                        String _plus_1 = ("set" + _firstUpper);
                        final Procedure1<JvmOperation> _function_1 = (JvmOperation it_1) -> {
                          this._jvmTypesBuilder.setDocumentation(it_1, this._jvmTypesBuilder.getDocumentation(enteln_1));
                          final String parName = ((PropertyDef)enteln_1).getName();
                          EList<JvmFormalParameter> _parameters = it_1.getParameters();
                          JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(enteln_1, parName, ((PropertyDef)enteln_1).getType());
                          this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                          boolean _equals_2 = ((PropertyDef)enteln_1).getType().getSimpleName().equals("Double");
                          if (_equals_2) {
                            StringConcatenationClient _client = new StringConcatenationClient() {
                              @Override
                              protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                _builder.append(cellName);
                                _builder.append(".getGrid().setValue(");
                                _builder.append(fIndex);
                                _builder.append(",getX(), getY(),");
                                String _name = ((PropertyDef)enteln_1).getName();
                                _builder.append(_name);
                                _builder.append(");");
                                _builder.newLineIfNotEmpty();
                                _builder.append(" ");
                                _builder.newLine();
                              }
                            };
                            this._jvmTypesBuilder.setBody(it_1, _client);
                          } else {
                            boolean _equals_3 = ((PropertyDef)enteln_1).getType().getSimpleName().equals("Integer");
                            if (_equals_3) {
                              StringConcatenationClient _client_1 = new StringConcatenationClient() {
                                @Override
                                protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                  _builder.append(cellName);
                                  _builder.append(".getGrid().setValue(");
                                  _builder.append(fIndex);
                                  _builder.append(",getX(), getY(),");
                                  String _name = ((PropertyDef)enteln_1).getName();
                                  _builder.append(_name);
                                  _builder.append(".doubleValue());");
                                  _builder.newLineIfNotEmpty();
                                }
                              };
                              this._jvmTypesBuilder.setBody(it_1, _client_1);
                            } else {
                              boolean _equals_4 = ((PropertyDef)enteln_1).getType().getSimpleName().equals("Float");
                              if (_equals_4) {
                                StringConcatenationClient _client_2 = new StringConcatenationClient() {
                                  @Override
                                  protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                    _builder.append(cellName);
                                    _builder.append(".getGrid().setValue(");
                                    _builder.append(fIndex);
                                    _builder.append(",getX(), getY(),");
                                    String _name = ((PropertyDef)enteln_1).getName();
                                    _builder.append(_name);
                                    _builder.append(".doubleValue());");
                                    _builder.newLineIfNotEmpty();
                                  }
                                };
                                this._jvmTypesBuilder.setBody(it_1, _client_2);
                              } else {
                                boolean _equals_5 = ((PropertyDef)enteln_1).getType().getSimpleName().equals("Boolean");
                                if (_equals_5) {
                                  StringConcatenationClient _client_3 = new StringConcatenationClient() {
                                    @Override
                                    protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                      _builder.append("if(");
                                      String _name = ((PropertyDef)enteln_1).getName();
                                      _builder.append(_name);
                                      _builder.append(" == true){");
                                      _builder.newLineIfNotEmpty();
                                      _builder.append("\t");
                                      _builder.append(cellName, "\t");
                                      _builder.append(".getGrid().setValue(");
                                      _builder.append(fIndex, "\t");
                                      _builder.append(",getX(), getY(),1.0);");
                                      _builder.newLineIfNotEmpty();
                                      _builder.append("}else{");
                                      _builder.newLine();
                                      _builder.append("\t");
                                      _builder.append(cellName, "\t");
                                      _builder.append(".getGrid().setValue(");
                                      _builder.append(fIndex, "\t");
                                      _builder.append(",getX(), getY(),0.0);");
                                      _builder.newLineIfNotEmpty();
                                      _builder.append("}");
                                      _builder.newLine();
                                    }
                                  };
                                  this._jvmTypesBuilder.setBody(it_1, _client_3);
                                } else {
                                  boolean _equals_6 = ((PropertyDef)enteln_1).getType().getSimpleName().equals("Byte");
                                  if (_equals_6) {
                                    StringConcatenationClient _client_4 = new StringConcatenationClient() {
                                      @Override
                                      protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                        _builder.append(cellName);
                                        _builder.append(".getGrid().setValue(");
                                        _builder.append(fIndex);
                                        _builder.append(",getX(), getY(),");
                                        String _name = ((PropertyDef)enteln_1).getName();
                                        _builder.append(_name);
                                        _builder.append(".doubleValue());");
                                        _builder.newLineIfNotEmpty();
                                      }
                                    };
                                    this._jvmTypesBuilder.setBody(it_1, _client_4);
                                  } else {
                                    StringConcatenationClient _client_5 = new StringConcatenationClient() {
                                      @Override
                                      protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                        _builder.append("System.out.println(\"");
                                        String _simpleName = ((PropertyDef)enteln_1).getType().getSimpleName();
                                        _builder.append(_simpleName);
                                        _builder.append(" type is not allowed for a cell entity\");");
                                        _builder.newLineIfNotEmpty();
                                      }
                                    };
                                    this._jvmTypesBuilder.setBody(it_1, _client_5);
                                  }
                                }
                              }
                            }
                          }
                        };
                        JvmOperation _method = this._jvmTypesBuilder.toMethod(enteln_1, _plus_1, this._typeReferenceBuilder.typeRef(Void.TYPE), _function_1);
                        this._jvmTypesBuilder.<JvmOperation>operator_add(_members, _method);
                        EList<JvmMember> _members_1 = it.getMembers();
                        String _firstUpper_1 = StringExtensions.toFirstUpper(((PropertyDef)enteln_1).getName());
                        String _plus_2 = ("get" + _firstUpper_1);
                        final Procedure1<JvmOperation> _function_2 = (JvmOperation it_1) -> {
                          this._jvmTypesBuilder.setDocumentation(it_1, this._jvmTypesBuilder.getDocumentation(enteln_1));
                          boolean _equals_2 = ((PropertyDef)enteln_1).getType().getSimpleName().equals("Double");
                          if (_equals_2) {
                            StringConcatenationClient _client = new StringConcatenationClient() {
                              @Override
                              protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                _builder.append("return ");
                                _builder.append(cellName);
                                _builder.append(".getGrid().getValue(");
                                _builder.append(fIndex);
                                _builder.append(",getX(), getY());");
                                _builder.newLineIfNotEmpty();
                              }
                            };
                            this._jvmTypesBuilder.setBody(it_1, _client);
                          } else {
                            boolean _equals_3 = ((PropertyDef)enteln_1).getType().getSimpleName().equals("Integer");
                            if (_equals_3) {
                              StringConcatenationClient _client_1 = new StringConcatenationClient() {
                                @Override
                                protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                  _builder.append("return ");
                                  _builder.append(cellName);
                                  _builder.append(".getGrid().getValue(");
                                  _builder.append(fIndex);
                                  _builder.append(",getX(), getY()).intValue();");
                                  _builder.newLineIfNotEmpty();
                                }
                              };
                              this._jvmTypesBuilder.setBody(it_1, _client_1);
                            } else {
                              boolean _equals_4 = ((PropertyDef)enteln_1).getType().getSimpleName().equals("Float");
                              if (_equals_4) {
                                StringConcatenationClient _client_2 = new StringConcatenationClient() {
                                  @Override
                                  protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                    _builder.append("return ");
                                    _builder.append(cellName);
                                    _builder.append(".getGrid().getValue(");
                                    _builder.append(fIndex);
                                    _builder.append(",getX(), getY()).floatValue();");
                                    _builder.newLineIfNotEmpty();
                                  }
                                };
                                this._jvmTypesBuilder.setBody(it_1, _client_2);
                              } else {
                                boolean _equals_5 = ((PropertyDef)enteln_1).getType().getSimpleName().equals("Byte");
                                if (_equals_5) {
                                  StringConcatenationClient _client_3 = new StringConcatenationClient() {
                                    @Override
                                    protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                      _builder.append("return ");
                                      _builder.append(cellName);
                                      _builder.append(".getGrid().getValue(");
                                      _builder.append(fIndex);
                                      _builder.append(",getX(), getY()).byteValue();");
                                      _builder.newLineIfNotEmpty();
                                    }
                                  };
                                  this._jvmTypesBuilder.setBody(it_1, _client_3);
                                } else {
                                  boolean _equals_6 = ((PropertyDef)enteln_1).getType().getSimpleName().equals("Boolean");
                                  if (_equals_6) {
                                    StringConcatenationClient _client_4 = new StringConcatenationClient() {
                                      @Override
                                      protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                        _builder.append("Double val =");
                                        _builder.append(cellName);
                                        _builder.append(".getGrid().getValue(");
                                        _builder.append(fIndex);
                                        _builder.append(",getX(), getY());");
                                        _builder.newLineIfNotEmpty();
                                        _builder.append("if(val == 1.0){");
                                        _builder.newLine();
                                        _builder.append("\t");
                                        _builder.append("return true;");
                                        _builder.newLine();
                                        _builder.append("} ");
                                        _builder.newLine();
                                        _builder.append("return false;");
                                        _builder.newLine();
                                      }
                                    };
                                    this._jvmTypesBuilder.setBody(it_1, _client_4);
                                  } else {
                                    StringConcatenationClient _client_5 = new StringConcatenationClient() {
                                      @Override
                                      protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                        _builder.append("System.out.println(\"");
                                        String _simpleName = ((PropertyDef)enteln_1).getType().getSimpleName();
                                        _builder.append(_simpleName);
                                        _builder.append(" type is not allowed for a cell entity\");");
                                        _builder.newLineIfNotEmpty();
                                        _builder.append("return null;");
                                        _builder.newLine();
                                      }
                                    };
                                    this._jvmTypesBuilder.setBody(it_1, _client_5);
                                  }
                                }
                              }
                            }
                          }
                        };
                        JvmOperation _method_1 = this._jvmTypesBuilder.toMethod(enteln_1, _plus_2, ((PropertyDef)enteln_1).getType(), _function_2);
                        this._jvmTypesBuilder.<JvmOperation>operator_add(_members_1, _method_1);
                        index = (index + 1);
                      }
                    } else {
                      EList<JvmMember> _members_2 = it.getMembers();
                      String _firstUpper_2 = StringExtensions.toFirstUpper(((PropertyDef)enteln_1).getName());
                      String _plus_3 = ("set" + _firstUpper_2);
                      final Procedure1<JvmOperation> _function_3 = (JvmOperation it_1) -> {
                        this._jvmTypesBuilder.setDocumentation(it_1, this._jvmTypesBuilder.getDocumentation(enteln_1));
                        final String parName = ((PropertyDef)enteln_1).getName();
                        EList<JvmFormalParameter> _parameters = it_1.getParameters();
                        JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(enteln_1, parName, ((PropertyDef)enteln_1).getType());
                        this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                        StringConcatenationClient _client = new StringConcatenationClient() {
                          @Override
                          protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                            _builder.append("setProperty(\"");
                            String _name = ((PropertyDef)enteln_1).getName();
                            _builder.append(_name);
                            _builder.append("\",");
                            _builder.append(parName);
                            _builder.append(");");
                          }
                        };
                        this._jvmTypesBuilder.setBody(it_1, _client);
                      };
                      JvmOperation _method_2 = this._jvmTypesBuilder.toMethod(enteln_1, _plus_3, this._typeReferenceBuilder.typeRef(Void.TYPE), _function_3);
                      this._jvmTypesBuilder.<JvmOperation>operator_add(_members_2, _method_2);
                      EList<JvmMember> _members_3 = it.getMembers();
                      String _firstUpper_3 = StringExtensions.toFirstUpper(((PropertyDef)enteln_1).getName());
                      String _plus_4 = ("get" + _firstUpper_3);
                      final Procedure1<JvmOperation> _function_4 = (JvmOperation it_1) -> {
                        this._jvmTypesBuilder.setDocumentation(it_1, this._jvmTypesBuilder.getDocumentation(enteln_1));
                        StringConcatenationClient _client = new StringConcatenationClient() {
                          @Override
                          protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                            _builder.append("return getProperty(\"");
                            String _name = ((PropertyDef)enteln_1).getName();
                            _builder.append(_name);
                            _builder.append("\");");
                          }
                        };
                        this._jvmTypesBuilder.setBody(it_1, _client);
                      };
                      JvmOperation _method_3 = this._jvmTypesBuilder.toMethod(enteln_1, _plus_4, ((PropertyDef)enteln_1).getType(), _function_4);
                      this._jvmTypesBuilder.<JvmOperation>operator_add(_members_3, _method_3);
                    }
                  }
                }
                if (!_matched_2) {
                  if (enteln_1 instanceof ServiceDef) {
                    _matched_2=true;
                    JvmTypeReference rtype = ((ServiceDef)enteln_1).getType();
                    if ((rtype == null)) {
                      rtype = this._typeReferenceBuilder.typeRef(Void.TYPE);
                    }
                    EList<JvmMember> _members = it.getMembers();
                    final Procedure1<JvmOperation> _function_1 = (JvmOperation it_1) -> {
                      this._jvmTypesBuilder.setDocumentation(it_1, this._jvmTypesBuilder.getDocumentation(enteln_1));
                      EList<JvmFormalParameter> _params = ((ServiceDef)enteln_1).getParams();
                      for (final JvmFormalParameter p : _params) {
                        EList<JvmFormalParameter> _parameters = it_1.getParameters();
                        JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(p, p.getName(), p.getParameterType());
                        this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                      }
                      this._jvmTypesBuilder.setBody(it_1, ((ServiceDef)enteln_1).getBody());
                    };
                    JvmOperation _method = this._jvmTypesBuilder.toMethod(enteln_1, ((ServiceDef)enteln_1).getName(), rtype, _function_1);
                    this._jvmTypesBuilder.<JvmOperation>operator_add(_members, _method);
                  }
                }
                if (!_matched_2) {
                  if (enteln_1 instanceof ConstructorDef) {
                    _matched_2=true;
                    EList<JvmMember> _members = it.getMembers();
                    final Procedure1<JvmOperation> _function_1 = (JvmOperation it_1) -> {
                      it_1.setStatic(true);
                      this._jvmTypesBuilder.setDocumentation(it_1, this._jvmTypesBuilder.getDocumentation(enteln_1));
                      EList<JvmFormalParameter> _params = ((ConstructorDef)enteln_1).getParams();
                      for (final JvmFormalParameter p : _params) {
                        EList<JvmFormalParameter> _parameters = it_1.getParameters();
                        JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(p, p.getName(), p.getParameterType());
                        this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                      }
                      this._jvmTypesBuilder.setBody(it_1, ((ConstructorDef)enteln_1).getBody());
                    };
                    JvmOperation _method = this._jvmTypesBuilder.toMethod(enteln_1, ((ConstructorDef)enteln_1).getName(), this._typeReferenceBuilder.typeRef(this._iQualifiedNameProvider.getFullyQualifiedName(meln).toString()), _function_1);
                    this._jvmTypesBuilder.<JvmOperation>operator_add(_members, _method);
                  }
                }
              }
              if ((!isCell)) {
                EList<JvmMember> _members = it.getMembers();
                final Procedure1<JvmConstructor> _function_1 = (JvmConstructor it_1) -> {
                  StringConcatenationClient _client = new StringConcatenationClient() {
                    @Override
                    protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                      _builder.append("super();");
                      _builder.newLine();
                      {
                        for(final PropertyDef hprop : lpropdefs) {
                          final JvmTypeReference hhtype = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.entity.Hproperty", OceletJvmModelInferrer.this._primitives.asWrapperTypeIfPrimitive(hprop.getType()));
                          _builder.newLineIfNotEmpty();
                          _builder.append("defProperty(\"");
                          String _name = hprop.getName();
                          _builder.append(_name);
                          _builder.append("\",new ");
                          _builder.append(hhtype);
                          _builder.append("());");
                          _builder.newLineIfNotEmpty();
                          final JvmTypeReference vtyp = OceletJvmModelInferrer.this._primitives.asWrapperTypeIfPrimitive(hprop.getType());
                          _builder.newLineIfNotEmpty();
                          _builder.append("set");
                          String _firstUpper = StringExtensions.toFirstUpper(hprop.getName());
                          _builder.append(_firstUpper);
                          _builder.append("(new ");
                          _builder.append(vtyp);
                          {
                            boolean _isNumberType = OceletJvmModelInferrer.this.isNumberType(vtyp);
                            if (_isNumberType) {
                              _builder.append("(\"0\"));");
                            } else {
                              boolean _equals = vtyp.getQualifiedName().equals("java.lang.Boolean");
                              if (_equals) {
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
                  this._jvmTypesBuilder.setBody(it_1, _client);
                };
                JvmConstructor _constructor = this._jvmTypesBuilder.toConstructor(meln, _function_1);
                this._jvmTypesBuilder.<JvmConstructor>operator_add(_members, _constructor);
              } else {
                EList<JvmMember> _members_1 = it.getMembers();
                final Procedure1<JvmConstructor> _function_2 = (JvmConstructor it_1) -> {
                  StringConcatenationClient _client = new StringConcatenationClient() {
                    @Override
                    protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                      _builder.append("this.");
                      _builder.append(cellName);
                      _builder.append(" = new fr.ocelet.runtime.geom.ocltypes.Cell();");
                      _builder.newLineIfNotEmpty();
                      _builder.append("this.setSpatialType(");
                      _builder.append(cellName);
                      _builder.append(");");
                      _builder.newLineIfNotEmpty();
                    }
                  };
                  this._jvmTypesBuilder.setBody(it_1, _client);
                };
                JvmConstructor _constructor_1 = this._jvmTypesBuilder.toConstructor(meln, _function_2);
                this._jvmTypesBuilder.<JvmConstructor>operator_add(_members_1, _constructor_1);
                EList<JvmMember> _members_2 = it.getMembers();
                final Procedure1<JvmOperation> _function_3 = (JvmOperation it_1) -> {
                  StringConcatenationClient _client = new StringConcatenationClient() {
                    @Override
                    protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                      _builder.append("List<String> names = new List<String>();");
                      _builder.newLine();
                      {
                        for(final String name : cellProps) {
                          _builder.append("names.add(\"");
                          _builder.append(name);
                          _builder.append("\");");
                          _builder.newLineIfNotEmpty();
                        }
                      }
                      _builder.append("return names;");
                      _builder.newLine();
                    }
                  };
                  this._jvmTypesBuilder.setBody(it_1, _client);
                };
                JvmOperation _method = this._jvmTypesBuilder.toMethod(meln, "getProps", this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.ocltypes.List", this._typeReferenceBuilder.typeRef("java.lang.String")), _function_3);
                this._jvmTypesBuilder.<JvmOperation>operator_add(_members_2, _method);
                EList<JvmMember> _members_3 = it.getMembers();
                final Procedure1<JvmOperation> _function_4 = (JvmOperation it_1) -> {
                  StringConcatenationClient _client = new StringConcatenationClient() {
                    @Override
                    protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                      _builder.append("KeyMap<String, String> names = new KeyMap<String, String>();");
                      _builder.newLine();
                      {
                        for(final String name : cellProps) {
                          _builder.append("names.put(\"");
                          _builder.append(name);
                          _builder.append("\",\"");
                          String _get = typeProps.get(name);
                          _builder.append(_get);
                          _builder.append("\");");
                          _builder.newLineIfNotEmpty();
                        }
                      }
                      _builder.append("return names;");
                      _builder.newLine();
                    }
                  };
                  this._jvmTypesBuilder.setBody(it_1, _client);
                };
                JvmOperation _method_1 = this._jvmTypesBuilder.toMethod(meln, "getTypeProps", this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.ocltypes.KeyMap", this._typeReferenceBuilder.typeRef("java.lang.String"), this._typeReferenceBuilder.typeRef("java.lang.String")), _function_4);
                this._jvmTypesBuilder.<JvmOperation>operator_add(_members_3, _method_1);
                EList<JvmMember> _members_4 = it.getMembers();
                final Procedure1<JvmOperation> _function_5 = (JvmOperation it_1) -> {
                  EList<JvmFormalParameter> _parameters = it_1.getParameters();
                  JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "type", this._typeReferenceBuilder.typeRef("java.lang.String"));
                  this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                  StringConcatenationClient _client = new StringConcatenationClient() {
                    @Override
                    protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                      _builder.append("this.");
                      _builder.append(cellName);
                      _builder.append(".setType(type);");
                      _builder.newLineIfNotEmpty();
                    }
                  };
                  this._jvmTypesBuilder.setBody(it_1, _client);
                };
                JvmOperation _method_2 = this._jvmTypesBuilder.toMethod(meln, "updateCellInfo", this._typeReferenceBuilder.typeRef(Void.TYPE), _function_5);
                this._jvmTypesBuilder.<JvmOperation>operator_add(_members_4, _method_2);
                JvmField jvmFieldCell = this._jvmTypesBuilder.toField(meln, cellName, this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.geom.ocltypes.Cell"));
                if ((jvmFieldCell != null)) {
                  jvmFieldCell.setFinal(false);
                  EList<JvmMember> _members_5 = it.getMembers();
                  this._jvmTypesBuilder.<JvmField>operator_add(_members_5, jvmFieldCell);
                }
                JvmField jvmFieldX = this._jvmTypesBuilder.toField(meln, "x", this._typeReferenceBuilder.typeRef("java.lang.Integer"));
                if ((jvmFieldX != null)) {
                  jvmFieldX.setFinal(false);
                  EList<JvmMember> _members_6 = it.getMembers();
                  this._jvmTypesBuilder.<JvmField>operator_add(_members_6, jvmFieldX);
                }
                JvmField jvmFieldY = this._jvmTypesBuilder.toField(meln, "y", this._typeReferenceBuilder.typeRef("java.lang.Integer"));
                if ((jvmFieldY != null)) {
                  jvmFieldY.setFinal(false);
                  EList<JvmMember> _members_7 = it.getMembers();
                  this._jvmTypesBuilder.<JvmField>operator_add(_members_7, jvmFieldY);
                }
                JvmField jvmFieldNum = this._jvmTypesBuilder.toField(meln, "numGrid", this._typeReferenceBuilder.typeRef("java.lang.Integer"));
                if ((jvmFieldNum != null)) {
                  jvmFieldNum.setFinal(false);
                  jvmFieldNum.setStatic(true);
                  EList<JvmMember> _members_8 = it.getMembers();
                  this._jvmTypesBuilder.<JvmField>operator_add(_members_8, jvmFieldNum);
                }
                EList<JvmMember> _members_9 = it.getMembers();
                final Procedure1<JvmOperation> _function_6 = (JvmOperation it_1) -> {
                  EList<JvmFormalParameter> _parameters = it_1.getParameters();
                  JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "x", this._typeReferenceBuilder.typeRef("java.lang.Integer"));
                  this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                  StringConcatenationClient _client = new StringConcatenationClient() {
                    @Override
                    protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                      _builder.append("this.");
                      _builder.append(cellName);
                      _builder.append(".setX(x); ");
                      _builder.newLineIfNotEmpty();
                    }
                  };
                  this._jvmTypesBuilder.setBody(it_1, _client);
                };
                JvmOperation _method_3 = this._jvmTypesBuilder.toMethod(meln, "setX", this._typeReferenceBuilder.typeRef(Void.TYPE), _function_6);
                this._jvmTypesBuilder.<JvmOperation>operator_add(_members_9, _method_3);
                EList<JvmMember> _members_10 = it.getMembers();
                final Procedure1<JvmOperation> _function_7 = (JvmOperation it_1) -> {
                  EList<JvmFormalParameter> _parameters = it_1.getParameters();
                  JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "y", this._typeReferenceBuilder.typeRef("java.lang.Integer"));
                  this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                  StringConcatenationClient _client = new StringConcatenationClient() {
                    @Override
                    protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                      _builder.append("this.");
                      _builder.append(cellName);
                      _builder.append(".setY(y); ");
                      _builder.newLineIfNotEmpty();
                    }
                  };
                  this._jvmTypesBuilder.setBody(it_1, _client);
                };
                JvmOperation _method_4 = this._jvmTypesBuilder.toMethod(meln, "setY", this._typeReferenceBuilder.typeRef(Void.TYPE), _function_7);
                this._jvmTypesBuilder.<JvmOperation>operator_add(_members_10, _method_4);
                EList<JvmMember> _members_11 = it.getMembers();
                final Procedure1<JvmOperation> _function_8 = (JvmOperation it_1) -> {
                  StringConcatenationClient _client = new StringConcatenationClient() {
                    @Override
                    protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                      _builder.append("return this.");
                      _builder.append(cellName);
                      _builder.append(".getX(); ");
                      _builder.newLineIfNotEmpty();
                    }
                  };
                  this._jvmTypesBuilder.setBody(it_1, _client);
                };
                JvmOperation _method_5 = this._jvmTypesBuilder.toMethod(meln, "getX", this._typeReferenceBuilder.typeRef("java.lang.Integer"), _function_8);
                this._jvmTypesBuilder.<JvmOperation>operator_add(_members_11, _method_5);
                EList<JvmMember> _members_12 = it.getMembers();
                String _firstUpper = StringExtensions.toFirstUpper(cellName);
                String _plus_1 = ("get" + _firstUpper);
                final Procedure1<JvmOperation> _function_9 = (JvmOperation it_1) -> {
                  StringConcatenationClient _client = new StringConcatenationClient() {
                    @Override
                    protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                      _builder.append("return ");
                      _builder.append(cellName);
                      _builder.append(";");
                      _builder.newLineIfNotEmpty();
                    }
                  };
                  this._jvmTypesBuilder.setBody(it_1, _client);
                };
                JvmOperation _method_6 = this._jvmTypesBuilder.toMethod(meln, _plus_1, this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.geom.ocltypes.Cell"), _function_9);
                this._jvmTypesBuilder.<JvmOperation>operator_add(_members_12, _method_6);
                EList<JvmMember> _members_13 = it.getMembers();
                final Procedure1<JvmOperation> _function_10 = (JvmOperation it_1) -> {
                  StringConcatenationClient _client = new StringConcatenationClient() {
                    @Override
                    protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                      _builder.append("return this.");
                      _builder.append(cellName);
                      _builder.append(".getY();");
                      _builder.newLineIfNotEmpty();
                    }
                  };
                  this._jvmTypesBuilder.setBody(it_1, _client);
                };
                JvmOperation _method_7 = this._jvmTypesBuilder.toMethod(meln, "getY", this._typeReferenceBuilder.typeRef("java.lang.Integer"), _function_10);
                this._jvmTypesBuilder.<JvmOperation>operator_add(_members_13, _method_7);
              }
            };
            acceptor.<JvmGenericType>accept(this._jvmTypesBuilder.toClass(modl, this._iQualifiedNameProvider.getFullyQualifiedName(meln)), _function);
          }
        }
        if (!_matched) {
          if (meln instanceof Agregdef) {
            _matched=true;
            JvmTypeReference _type = ((Agregdef)meln).getType();
            boolean _tripleNotEquals = (_type != null);
            if (_tripleNotEquals) {
              final Procedure1<JvmGenericType> _function = (JvmGenericType it) -> {
                EList<JvmTypeReference> _superTypes = it.getSuperTypes();
                JvmTypeReference _typeRef = this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.relation.AggregOperator", ((Agregdef)meln).getType(), this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.ocltypes.List", ((Agregdef)meln).getType()));
                this._jvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes, _typeRef);
                EList<JvmMember> _members = it.getMembers();
                final Procedure1<JvmOperation> _function_1 = (JvmOperation it_1) -> {
                  EList<JvmFormalParameter> _parameters = it_1.getParameters();
                  JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "values", this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.ocltypes.List", ((Agregdef)meln).getType()));
                  this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                  EList<JvmFormalParameter> _parameters_1 = it_1.getParameters();
                  JvmFormalParameter _parameter_1 = this._jvmTypesBuilder.toParameter(meln, "preval", ((Agregdef)meln).getType());
                  this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_1, _parameter_1);
                  this._jvmTypesBuilder.setBody(it_1, ((Agregdef)meln).getBody());
                };
                JvmOperation _method = this._jvmTypesBuilder.toMethod(meln, "compute", ((Agregdef)meln).getType(), _function_1);
                this._jvmTypesBuilder.<JvmOperation>operator_add(_members, _method);
              };
              acceptor.<JvmGenericType>accept(this._jvmTypesBuilder.toClass(modl, this._iQualifiedNameProvider.getFullyQualifiedName(meln)), _function);
            }
          }
        }
        if (!_matched) {
          if (meln instanceof Relation) {
            _matched=true;
            final QualifiedName graphcname = this._iQualifiedNameProvider.getFullyQualifiedName(meln);
            final String edgecname = (graphcname + "_Edge");
            int _size = ((Relation)meln).getRoles().size();
            boolean _greaterThan = (_size > 2);
            if (_greaterThan) {
              InputOutput.<String>println("Sorry, only graphs with two roles are supported by this version. The two first roles will be used and the others will be ignored.");
            }
            final JvmTypeReference aggregType = this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.raster.CellAggregOperator");
            final JvmTypeReference listype = this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.ocltypes.List", aggregType);
            final Procedure1<JvmGenericType> _function = (JvmGenericType it) -> {
              final boolean isAutoGraph = ((Relation)meln).getRoles().get(0).getType().equals(((Relation)meln).getRoles().get(1).getType());
              boolean isCellGraph = false;
              boolean isCellGeomGraph = false;
              boolean testCell1 = false;
              boolean testCell2 = false;
              boolean testGeom1 = false;
              boolean testGeom2 = false;
              final Entity rol1 = ((Relation)meln).getRoles().get(0).getType();
              final Entity rol2 = ((Relation)meln).getRoles().get(1).getType();
              EList<EObject> _eContents = rol1.eContents();
              for (final EObject e : _eContents) {
                boolean _matched_1 = false;
                if (e instanceof PropertyDef) {
                  _matched_1=true;
                  boolean _equals = ((PropertyDef)e).getType().getSimpleName().equals("Cell");
                  if (_equals) {
                    testCell1 = true;
                  }
                  if (((((((((PropertyDef)e).getType().getSimpleName().equals("Line") || ((PropertyDef)e).getType().getSimpleName().equals("MultiLine")) || 
                    ((PropertyDef)e).getType().getSimpleName().equals("Polygon")) || ((PropertyDef)e).getType().getSimpleName().equals("MultiPolygon")) || 
                    ((PropertyDef)e).getType().getSimpleName().equals("Point")) || ((PropertyDef)e).getType().getSimpleName().equals("MultiPoint")) || 
                    ((PropertyDef)e).getType().getSimpleName().equals("Ring"))) {
                    testGeom1 = true;
                  }
                }
              }
              EList<EObject> _eContents_1 = rol2.eContents();
              for (final EObject e_1 : _eContents_1) {
                boolean _matched_2 = false;
                if (e_1 instanceof PropertyDef) {
                  _matched_2=true;
                  boolean _equals = ((PropertyDef)e_1).getType().getSimpleName().equals("Cell");
                  if (_equals) {
                    testCell2 = true;
                  }
                  if (((((((((PropertyDef)e_1).getType().getSimpleName().equals("Line") || ((PropertyDef)e_1).getType().getSimpleName().equals("MultiLine")) || 
                    ((PropertyDef)e_1).getType().getSimpleName().equals("Polygon")) || ((PropertyDef)e_1).getType().getSimpleName().equals("MultiPolygon")) || 
                    ((PropertyDef)e_1).getType().getSimpleName().equals("Point")) || ((PropertyDef)e_1).getType().getSimpleName().equals("MultiPoint")) || 
                    ((PropertyDef)e_1).getType().getSimpleName().equals("Ring"))) {
                    testGeom2 = true;
                  }
                }
              }
              if ((testCell1 && testCell2)) {
                isCellGraph = true;
              }
              if (((testGeom1 && testCell2) || (testGeom2 && testCell1))) {
                isCellGeomGraph = true;
              }
              if (isCellGeomGraph) {
                final Role firstRole = ((Relation)meln).getRoles().get(0);
                final Role secondRole = ((Relation)meln).getRoles().get(1);
                JvmTypeReference tempcellType = this._typeReferenceBuilder.typeRef(this._iQualifiedNameProvider.getFullyQualifiedName(firstRole.getType()).toString());
                JvmTypeReference tempgeomType = this._typeReferenceBuilder.typeRef(this._iQualifiedNameProvider.getFullyQualifiedName(secondRole.getType()).toString());
                String tempCellName = firstRole.getName();
                String tempGeomName = secondRole.getName();
                if (testCell2) {
                  tempcellType = this._typeReferenceBuilder.typeRef(this._iQualifiedNameProvider.getFullyQualifiedName(secondRole.getType()).toString());
                  tempCellName = secondRole.getName();
                  tempgeomType = this._typeReferenceBuilder.typeRef(this._iQualifiedNameProvider.getFullyQualifiedName(firstRole.getType()).toString());
                  tempGeomName = firstRole.getName();
                }
                final JvmTypeReference cellType = tempcellType;
                final JvmTypeReference geomType = tempgeomType;
                final String cellName = tempCellName;
                final String geomName = tempGeomName;
                final JvmTypeReference cellList1 = this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.ocltypes.List", cellType);
                final String cellListName = (cellName + "s");
                final String geomNames = (geomName + "s");
                EList<JvmTypeReference> _superTypes = it.getSuperTypes();
                JvmTypeReference _typeRef = this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.relation.GeomCellEdge", cellType, geomType);
                this._jvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes, _typeRef);
                if ((((((((((((Relation)meln).getRoles().size() >= 2) && (((Relation)meln).getRoles().get(0) != null)) && (((Relation)meln).getRoles().get(1) != null)) && (((Relation)meln).getRoles().get(0).getType() != null)) && (((Relation)meln).getRoles().get(1).getType() != null)) && (this._iQualifiedNameProvider.getFullyQualifiedName(((Relation)meln).getRoles().get(0).getType()) != null)) && (this._iQualifiedNameProvider.getFullyQualifiedName(((Relation)meln).getRoles().get(1).getType()) != null)) && (((Relation)meln).getRoles().get(0).getName() != null)) && (((Relation)meln).getRoles().get(1).getName() != null))) {
                  JvmField jvmField = this._jvmTypesBuilder.toField(meln, cellName, cellType);
                  if ((jvmField != null)) {
                    jvmField.setFinal(false);
                    EList<JvmMember> _members = it.getMembers();
                    this._jvmTypesBuilder.<JvmField>operator_add(_members, jvmField);
                    EList<JvmMember> _members_1 = it.getMembers();
                    JvmOperation _setter = this._jvmTypesBuilder.toSetter(meln, cellName, cellType);
                    this._jvmTypesBuilder.<JvmOperation>operator_add(_members_1, _setter);
                    EList<JvmMember> _members_2 = it.getMembers();
                    JvmOperation _getter = this._jvmTypesBuilder.toGetter(meln, cellName, cellType);
                    this._jvmTypesBuilder.<JvmOperation>operator_add(_members_2, _getter);
                  }
                  jvmField = this._jvmTypesBuilder.toField(meln, geomName, geomType);
                  if ((jvmField != null)) {
                    jvmField.setFinal(false);
                    EList<JvmMember> _members_3 = it.getMembers();
                    this._jvmTypesBuilder.<JvmField>operator_add(_members_3, jvmField);
                    EList<JvmMember> _members_4 = it.getMembers();
                    JvmOperation _setter_1 = this._jvmTypesBuilder.toSetter(meln, geomName, geomType);
                    this._jvmTypesBuilder.<JvmOperation>operator_add(_members_4, _setter_1);
                    EList<JvmMember> _members_5 = it.getMembers();
                    JvmOperation _getter_1 = this._jvmTypesBuilder.toGetter(meln, geomName, geomType);
                    this._jvmTypesBuilder.<JvmOperation>operator_add(_members_5, _getter_1);
                  }
                  EList<JvmMember> _members_6 = it.getMembers();
                  final Procedure1<JvmConstructor> _function_1 = (JvmConstructor it_1) -> {
                    EList<JvmFormalParameter> _parameters = it_1.getParameters();
                    JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, cellListName, cellList1);
                    this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                    EList<JvmFormalParameter> _parameters_1 = it_1.getParameters();
                    JvmFormalParameter _parameter_1 = this._jvmTypesBuilder.toParameter(meln, geomNames, this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.ocltypes.List", geomType));
                    this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_1, _parameter_1);
                    StringConcatenationClient _client = new StringConcatenationClient() {
                      @Override
                      protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                        _builder.append("super(");
                        _builder.append(cellListName);
                        _builder.append(", ");
                        _builder.append(geomNames);
                        _builder.append(");  ");
                        _builder.newLineIfNotEmpty();
                        _builder.append("this.");
                        _builder.append(cellName);
                        _builder.append(" = new ");
                        _builder.append(cellType);
                        _builder.append("();");
                        _builder.newLineIfNotEmpty();
                        _builder.newLine();
                        _builder.append("((");
                        JvmTypeReference _typeRef = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.geom.ocltypes.Cell");
                        _builder.append(_typeRef);
                        _builder.append(")this.");
                        _builder.append(cellName);
                        _builder.append(".getSpatialType()).setGrid(grid);");
                        _builder.newLineIfNotEmpty();
                      }
                    };
                    this._jvmTypesBuilder.setBody(it_1, _client);
                  };
                  JvmConstructor _constructor = this._jvmTypesBuilder.toConstructor(meln, _function_1);
                  this._jvmTypesBuilder.<JvmConstructor>operator_add(_members_6, _constructor);
                  EList<JvmMember> _members_7 = it.getMembers();
                  final Procedure1<JvmConstructor> _function_2 = (JvmConstructor it_1) -> {
                    EList<JvmFormalParameter> _parameters = it_1.getParameters();
                    JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, cellListName, cellList1);
                    this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                    EList<JvmFormalParameter> _parameters_1 = it_1.getParameters();
                    JvmFormalParameter _parameter_1 = this._jvmTypesBuilder.toParameter(meln, geomNames, this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.ocltypes.List", geomType));
                    this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_1, _parameter_1);
                    EList<JvmFormalParameter> _parameters_2 = it_1.getParameters();
                    JvmFormalParameter _parameter_2 = this._jvmTypesBuilder.toParameter(meln, "distance", this._typeReferenceBuilder.typeRef(Double.TYPE));
                    this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_2, _parameter_2);
                    StringConcatenationClient _client = new StringConcatenationClient() {
                      @Override
                      protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                        _builder.append("super(");
                        _builder.append(cellListName);
                        _builder.append(", ");
                        _builder.append(geomNames);
                        _builder.append(", distance);  ");
                        _builder.newLineIfNotEmpty();
                        _builder.append("this.");
                        _builder.append(cellName);
                        _builder.append(" = new ");
                        _builder.append(cellType);
                        _builder.append("();");
                        _builder.newLineIfNotEmpty();
                        _builder.append("          \t\t\t ");
                        _builder.append("((");
                        JvmTypeReference _typeRef = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.geom.ocltypes.Cell");
                        _builder.append(_typeRef, "          \t\t\t ");
                        _builder.append(")this.");
                        _builder.append(cellName, "          \t\t\t ");
                        _builder.append(".getSpatialType()).setGrid(grid);");
                        _builder.newLineIfNotEmpty();
                      }
                    };
                    this._jvmTypesBuilder.setBody(it_1, _client);
                  };
                  JvmConstructor _constructor_1 = this._jvmTypesBuilder.toConstructor(meln, _function_2);
                  this._jvmTypesBuilder.<JvmConstructor>operator_add(_members_7, _constructor_1);
                  EList<JvmMember> _members_8 = it.getMembers();
                  final Procedure1<JvmOperation> _function_3 = (JvmOperation it_1) -> {
                    EList<JvmFormalParameter> _parameters = it_1.getParameters();
                    JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "i", this._typeReferenceBuilder.typeRef("int"));
                    this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                    StringConcatenationClient _client = new StringConcatenationClient() {
                      @Override
                      protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                        _builder.append("if (i==0) return ");
                        _builder.append(cellName);
                        _builder.append(";");
                        _builder.newLineIfNotEmpty();
                        _builder.append("else if (i==1) return ");
                        _builder.append(geomName);
                        _builder.append(";");
                        _builder.newLineIfNotEmpty();
                        _builder.append("else return null;");
                        _builder.newLine();
                      }
                    };
                    this._jvmTypesBuilder.setBody(it_1, _client);
                  };
                  JvmOperation _method = this._jvmTypesBuilder.toMethod(meln, "getRole", this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.relation.OcltRole"), _function_3);
                  this._jvmTypesBuilder.<JvmOperation>operator_add(_members_8, _method);
                  EList<JvmMember> _members_9 = it.getMembers();
                  final Procedure1<JvmOperation> _function_4 = (JvmOperation it_1) -> {
                    StringConcatenationClient _client = new StringConcatenationClient() {
                      @Override
                      protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                        _builder.append("this.");
                        _builder.append(cellName);
                        _builder.append(".setX(getX());");
                        _builder.newLineIfNotEmpty();
                        _builder.append("this.");
                        _builder.append(cellName);
                        _builder.append(".setY(getY());");
                        _builder.newLineIfNotEmpty();
                        _builder.append("this. ");
                        _builder.append(geomName);
                        _builder.append(" = getGeomEntity();");
                        _builder.newLineIfNotEmpty();
                      }
                    };
                    this._jvmTypesBuilder.setBody(it_1, _client);
                  };
                  JvmOperation _method_1 = this._jvmTypesBuilder.toMethod(meln, "update", this._typeReferenceBuilder.typeRef(Void.TYPE), _function_4);
                  this._jvmTypesBuilder.<JvmOperation>operator_add(_members_9, _method_1);
                }
                final HashMap<String, JvmTypeReference> typeProps = CollectionLiterals.<String, JvmTypeReference>newHashMap();
                EList<RelElements> _relelns = ((Relation)meln).getRelelns();
                for (final RelElements reln : _relelns) {
                  boolean _matched_3 = false;
                  if (reln instanceof RelPropertyDef) {
                    _matched_3=true;
                    typeProps.put(((RelPropertyDef)reln).getName(), ((RelPropertyDef)reln).getType());
                  }
                  if (!_matched_3) {
                    if (reln instanceof InteractionDef) {
                      _matched_3=true;
                      EList<JvmMember> _members_10 = it.getMembers();
                      final Procedure1<JvmOperation> _function_5 = (JvmOperation it_1) -> {
                        EList<JvmFormalParameter> _params = ((InteractionDef)reln).getParams();
                        for (final JvmFormalParameter p : _params) {
                          EList<JvmFormalParameter> _parameters = it_1.getParameters();
                          JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(reln, p.getName(), p.getParameterType());
                          this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                        }
                        this._jvmTypesBuilder.setBody(it_1, ((InteractionDef)reln).getBody());
                      };
                      JvmOperation _method_2 = this._jvmTypesBuilder.toMethod(reln, ((InteractionDef)reln).getName(), this._typeReferenceBuilder.typeRef(Void.TYPE), _function_5);
                      this._jvmTypesBuilder.<JvmOperation>operator_add(_members_10, _method_2);
                      int _size_1 = ((InteractionDef)reln).getComitexpressions().size();
                      boolean _greaterThan_1 = (_size_1 > 0);
                      if (_greaterThan_1) {
                        EList<JvmMember> _members_11 = it.getMembers();
                        String _name_2 = ((InteractionDef)reln).getName();
                        String _plus_1 = ("get_agr_" + _name_2);
                        final Procedure1<JvmOperation> _function_6 = (JvmOperation it_1) -> {
                          StringConcatenationClient _client = new StringConcatenationClient() {
                            @Override
                            protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                              int index = 0;
                              _builder.newLineIfNotEmpty();
                              _builder.append(listype);
                              _builder.append(" cvtList = new ");
                              _builder.append(listype);
                              _builder.append("();");
                              _builder.newLineIfNotEmpty();
                              {
                                EList<Comitexpr> _comitexpressions = ((InteractionDef)reln).getComitexpressions();
                                for(final Comitexpr ce : _comitexpressions) {
                                  {
                                    boolean _equals = OceletJvmModelInferrer.this._iQualifiedNameProvider.getFullyQualifiedName(ce.getRol().getType()).toString().equals(cellType.getQualifiedName().toString());
                                    if (_equals) {
                                      _builder.append(aggregType);
                                      _builder.append(" cvt");
                                      _builder.append(index);
                                      _builder.append(" = new ");
                                      _builder.append(aggregType);
                                      _builder.append("();");
                                      _builder.newLineIfNotEmpty();
                                      _builder.append("cvt");
                                      _builder.append(index);
                                      _builder.append(".setName(\"");
                                      String _prop = ce.getProp();
                                      _builder.append(_prop);
                                      _builder.append("\"); ");
                                      _builder.newLineIfNotEmpty();
                                      _builder.append("cvt");
                                      _builder.append(index);
                                      _builder.append(".setCellOperator(new  ");
                                      JvmTypeReference _agrfunc = ce.getAgrfunc();
                                      _builder.append(_agrfunc);
                                      _builder.append("(), ");
                                      _builder.append(cellName);
                                      _builder.append(".getTypeProps(), ");
                                      boolean _isUsepreval = ce.isUsepreval();
                                      _builder.append(_isUsepreval);
                                      _builder.append(");");
                                      _builder.newLineIfNotEmpty();
                                      _builder.append("cvtList.add(cvt");
                                      _builder.append(index);
                                      _builder.append(");");
                                      _builder.newLineIfNotEmpty();
                                      Object _xblockexpression = null;
                                      {
                                        index = (index + 1);
                                        _xblockexpression = null;
                                      }
                                      _builder.append(_xblockexpression);
                                      _builder.newLineIfNotEmpty();
                                    }
                                  }
                                }
                              }
                              _builder.append("return cvtList;");
                              _builder.newLine();
                            }
                          };
                          this._jvmTypesBuilder.setBody(it_1, _client);
                        };
                        JvmOperation _method_3 = this._jvmTypesBuilder.toMethod(reln, _plus_1, listype, _function_6);
                        this._jvmTypesBuilder.<JvmOperation>operator_add(_members_11, _method_3);
                        EList<JvmMember> _members_12 = it.getMembers();
                        String _name_3 = ((InteractionDef)reln).getName();
                        String _plus_2 = ("_agr_" + _name_3);
                        final Procedure1<JvmOperation> _function_7 = (JvmOperation it_1) -> {
                          StringConcatenationClient _client = new StringConcatenationClient() {
                            @Override
                            protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                              {
                                EList<Comitexpr> _comitexpressions = ((InteractionDef)reln).getComitexpressions();
                                for(final Comitexpr ce : _comitexpressions) {
                                  final String t1 = OceletJvmModelInferrer.this._iQualifiedNameProvider.getFullyQualifiedName(ce.getRol().getType()).toString();
                                  _builder.newLineIfNotEmpty();
                                  final String t2 = cellType.getQualifiedName().toString();
                                  _builder.newLineIfNotEmpty();
                                  {
                                    boolean _equals = t1.equals(t2);
                                    boolean _not = (!_equals);
                                    if (_not) {
                                      _builder.append("\t");
                                      _builder.append("this.");
                                      String _name = ce.getRol().getName();
                                      _builder.append(_name, "\t");
                                      _builder.append(".setAgregOp(\"");
                                      String _prop = ce.getProp();
                                      _builder.append(_prop, "\t");
                                      _builder.append("\",new ");
                                      JvmTypeReference _agrfunc = ce.getAgrfunc();
                                      _builder.append(_agrfunc, "\t");
                                      _builder.append("(),");
                                      boolean _isUsepreval = ce.isUsepreval();
                                      _builder.append(_isUsepreval, "\t");
                                      _builder.append(");");
                                      _builder.newLineIfNotEmpty();
                                    }
                                  }
                                }
                              }
                            }
                          };
                          this._jvmTypesBuilder.setBody(it_1, _client);
                        };
                        JvmOperation _method_4 = this._jvmTypesBuilder.toMethod(reln, _plus_2, this._typeReferenceBuilder.typeRef(Void.TYPE), _function_7);
                        this._jvmTypesBuilder.<JvmOperation>operator_add(_members_12, _method_4);
                      } else {
                        EList<JvmMember> _members_13 = it.getMembers();
                        String _name_4 = ((InteractionDef)reln).getName();
                        String _plus_3 = ("get_agr_" + _name_4);
                        final Procedure1<JvmOperation> _function_8 = (JvmOperation it_1) -> {
                          StringConcatenationClient _client = new StringConcatenationClient() {
                            @Override
                            protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                              _builder.append("return null;");
                              _builder.newLine();
                            }
                          };
                          this._jvmTypesBuilder.setBody(it_1, _client);
                        };
                        JvmOperation _method_5 = this._jvmTypesBuilder.toMethod(reln, _plus_3, listype, _function_8);
                        this._jvmTypesBuilder.<JvmOperation>operator_add(_members_13, _method_5);
                      }
                    }
                  }
                }
                int indexDouble = 0;
                int indexInteger = 0;
                int indexBoolean = 0;
                Set<String> _keySet = typeProps.keySet();
                for (final String name : _keySet) {
                  {
                    boolean _equals = typeProps.get(name).getSimpleName().equals("Double");
                    if (_equals) {
                      final int index = indexDouble;
                      EList<JvmMember> _members_10 = it.getMembers();
                      String _firstUpper = StringExtensions.toFirstUpper(name);
                      String _plus_1 = ("set" + _firstUpper);
                      final Procedure1<JvmOperation> _function_5 = (JvmOperation it_1) -> {
                        EList<JvmFormalParameter> _parameters = it_1.getParameters();
                        JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "value", typeProps.get(name));
                        this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                        StringConcatenationClient _client = new StringConcatenationClient() {
                          @Override
                          protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                            _builder.append("setDoubleProperty(");
                            _builder.append(index);
                            _builder.append(", value);");
                            _builder.newLineIfNotEmpty();
                          }
                        };
                        this._jvmTypesBuilder.setBody(it_1, _client);
                      };
                      JvmOperation _method_2 = this._jvmTypesBuilder.toMethod(meln, _plus_1, this._typeReferenceBuilder.typeRef(Void.TYPE), _function_5);
                      this._jvmTypesBuilder.<JvmOperation>operator_add(_members_10, _method_2);
                      EList<JvmMember> _members_11 = it.getMembers();
                      String _firstUpper_1 = StringExtensions.toFirstUpper(name);
                      String _plus_2 = ("get" + _firstUpper_1);
                      final Procedure1<JvmOperation> _function_6 = (JvmOperation it_1) -> {
                        StringConcatenationClient _client = new StringConcatenationClient() {
                          @Override
                          protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                            _builder.append("return getDoubleProperty(");
                            _builder.append(index);
                            _builder.append(");");
                            _builder.newLineIfNotEmpty();
                          }
                        };
                        this._jvmTypesBuilder.setBody(it_1, _client);
                      };
                      JvmOperation _method_3 = this._jvmTypesBuilder.toMethod(meln, _plus_2, this._typeReferenceBuilder.typeRef("java.lang.Double"), _function_6);
                      this._jvmTypesBuilder.<JvmOperation>operator_add(_members_11, _method_3);
                      indexDouble = (indexDouble + 1);
                    }
                    boolean _equals_1 = typeProps.get(name).getSimpleName().equals("Integer");
                    if (_equals_1) {
                      final int index_1 = indexInteger;
                      EList<JvmMember> _members_12 = it.getMembers();
                      String _firstUpper_2 = StringExtensions.toFirstUpper(name);
                      String _plus_3 = ("set" + _firstUpper_2);
                      final Procedure1<JvmOperation> _function_7 = (JvmOperation it_1) -> {
                        EList<JvmFormalParameter> _parameters = it_1.getParameters();
                        JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "value", typeProps.get(name));
                        this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                        StringConcatenationClient _client = new StringConcatenationClient() {
                          @Override
                          protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                            _builder.append("setIntegerProperty(");
                            _builder.append(index_1);
                            _builder.append(", value);");
                            _builder.newLineIfNotEmpty();
                          }
                        };
                        this._jvmTypesBuilder.setBody(it_1, _client);
                      };
                      JvmOperation _method_4 = this._jvmTypesBuilder.toMethod(meln, _plus_3, this._typeReferenceBuilder.typeRef(Void.TYPE), _function_7);
                      this._jvmTypesBuilder.<JvmOperation>operator_add(_members_12, _method_4);
                      EList<JvmMember> _members_13 = it.getMembers();
                      String _firstUpper_3 = StringExtensions.toFirstUpper(name);
                      String _plus_4 = ("get" + _firstUpper_3);
                      final Procedure1<JvmOperation> _function_8 = (JvmOperation it_1) -> {
                        StringConcatenationClient _client = new StringConcatenationClient() {
                          @Override
                          protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                            _builder.append("return getIntegerProperty(");
                            _builder.append(index_1);
                            _builder.append(");");
                            _builder.newLineIfNotEmpty();
                          }
                        };
                        this._jvmTypesBuilder.setBody(it_1, _client);
                      };
                      JvmOperation _method_5 = this._jvmTypesBuilder.toMethod(meln, _plus_4, this._typeReferenceBuilder.typeRef("java.lang.Integer"), _function_8);
                      this._jvmTypesBuilder.<JvmOperation>operator_add(_members_13, _method_5);
                      indexInteger = (indexInteger + 1);
                    }
                    boolean _equals_2 = typeProps.get(name).getSimpleName().equals("Boolean");
                    if (_equals_2) {
                      final int index_2 = indexBoolean;
                      EList<JvmMember> _members_14 = it.getMembers();
                      String _firstUpper_4 = StringExtensions.toFirstUpper(name);
                      String _plus_5 = ("set" + _firstUpper_4);
                      final Procedure1<JvmOperation> _function_9 = (JvmOperation it_1) -> {
                        EList<JvmFormalParameter> _parameters = it_1.getParameters();
                        JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "value", typeProps.get(name));
                        this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                        StringConcatenationClient _client = new StringConcatenationClient() {
                          @Override
                          protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                            _builder.append("setBooleanProperty(");
                            _builder.append(index_2);
                            _builder.append(", value);");
                            _builder.newLineIfNotEmpty();
                          }
                        };
                        this._jvmTypesBuilder.setBody(it_1, _client);
                      };
                      JvmOperation _method_6 = this._jvmTypesBuilder.toMethod(meln, _plus_5, this._typeReferenceBuilder.typeRef(Void.TYPE), _function_9);
                      this._jvmTypesBuilder.<JvmOperation>operator_add(_members_14, _method_6);
                      EList<JvmMember> _members_15 = it.getMembers();
                      String _firstUpper_5 = StringExtensions.toFirstUpper(name);
                      String _plus_6 = ("get" + _firstUpper_5);
                      final Procedure1<JvmOperation> _function_10 = (JvmOperation it_1) -> {
                        StringConcatenationClient _client = new StringConcatenationClient() {
                          @Override
                          protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                            _builder.append("return getBooleanProperty(");
                            _builder.append(index_2);
                            _builder.append(");");
                            _builder.newLineIfNotEmpty();
                          }
                        };
                        this._jvmTypesBuilder.setBody(it_1, _client);
                      };
                      JvmOperation _method_7 = this._jvmTypesBuilder.toMethod(meln, _plus_6, this._typeReferenceBuilder.typeRef("java.lang.Boolean"), _function_10);
                      this._jvmTypesBuilder.<JvmOperation>operator_add(_members_15, _method_7);
                      indexBoolean = (indexBoolean + 1);
                    }
                  }
                }
                EList<JvmMember> _members_10 = it.getMembers();
                final Procedure1<JvmOperation> _function_5 = (JvmOperation it_1) -> {
                  StringConcatenationClient _client = new StringConcatenationClient() {
                    @Override
                    protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                      _builder.append("KeyMap<String, String> properties = new KeyMap<String, String>();\t");
                      _builder.newLine();
                      {
                        Set<String> _keySet = typeProps.keySet();
                        for(final String name : _keySet) {
                          _builder.append("properties.put(\"");
                          _builder.append(name);
                          _builder.append("\",\"");
                          String _simpleName = typeProps.get(name).getSimpleName();
                          _builder.append(_simpleName);
                          _builder.append("\");");
                          _builder.newLineIfNotEmpty();
                        }
                      }
                      _builder.append("return properties;         \t  \t      \t\t");
                      _builder.newLine();
                    }
                  };
                  this._jvmTypesBuilder.setBody(it_1, _client);
                };
                JvmOperation _method_2 = this._jvmTypesBuilder.toMethod(meln, "getEdgeProperties", 
                  this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.ocltypes.KeyMap", 
                    this._typeReferenceBuilder.typeRef("java.lang.String"), this._typeReferenceBuilder.typeRef("java.lang.String")), _function_5);
                this._jvmTypesBuilder.<JvmOperation>operator_add(_members_10, _method_2);
              } else {
                if (isCellGraph) {
                  if ((!isAutoGraph)) {
                    EList<JvmTypeReference> _superTypes_1 = it.getSuperTypes();
                    JvmTypeReference _typeRef_1 = this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.relation.DiCursorEdge");
                    this._jvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes_1, _typeRef_1);
                    final Role firstRole_1 = ((Relation)meln).getRoles().get(0);
                    final Role secondRole_1 = ((Relation)meln).getRoles().get(1);
                    final JvmTypeReference firstRoleType = this._typeReferenceBuilder.typeRef(this._iQualifiedNameProvider.getFullyQualifiedName(firstRole_1.getType()).toString());
                    final JvmTypeReference secondRoleType = this._typeReferenceBuilder.typeRef(this._iQualifiedNameProvider.getFullyQualifiedName(secondRole_1.getType()).toString());
                    final JvmTypeReference cellList1_1 = this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.ocltypes.List", firstRoleType);
                    final JvmTypeReference cellList2 = this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.ocltypes.List", secondRoleType);
                    String _name_2 = firstRole_1.getName();
                    final String firstName = (_name_2 + "s");
                    String _name_3 = secondRole_1.getName();
                    final String secondName = (_name_3 + "s");
                    JvmField jvmField_1 = this._jvmTypesBuilder.toField(meln, firstRole_1.getName(), firstRoleType);
                    if ((jvmField_1 != null)) {
                      jvmField_1.setFinal(false);
                      EList<JvmMember> _members_11 = it.getMembers();
                      this._jvmTypesBuilder.<JvmField>operator_add(_members_11, jvmField_1);
                    }
                    jvmField_1 = this._jvmTypesBuilder.toField(meln, secondRole_1.getName(), secondRoleType);
                    if ((jvmField_1 != null)) {
                      jvmField_1.setFinal(false);
                      EList<JvmMember> _members_12 = it.getMembers();
                      this._jvmTypesBuilder.<JvmField>operator_add(_members_12, jvmField_1);
                    }
                    EList<JvmMember> _members_13 = it.getMembers();
                    final Procedure1<JvmConstructor> _function_6 = (JvmConstructor it_1) -> {
                      EList<JvmFormalParameter> _parameters = it_1.getParameters();
                      JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, firstName, cellList1_1);
                      this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                      EList<JvmFormalParameter> _parameters_1 = it_1.getParameters();
                      JvmFormalParameter _parameter_1 = this._jvmTypesBuilder.toParameter(meln, secondName, cellList2);
                      this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_1, _parameter_1);
                      StringConcatenationClient _client = new StringConcatenationClient() {
                        @Override
                        protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                          _builder.append("super(");
                          _builder.append(firstName);
                          _builder.append(", ");
                          _builder.append(secondName);
                          _builder.append(");");
                          _builder.newLineIfNotEmpty();
                          String _name = firstRole_1.getName();
                          _builder.append(_name);
                          _builder.append(" = new ");
                          _builder.append(firstRoleType);
                          _builder.append("();");
                          _builder.newLineIfNotEmpty();
                          String _name_1 = secondRole_1.getName();
                          _builder.append(_name_1);
                          _builder.append(" = new ");
                          _builder.append(secondRoleType);
                          _builder.append("();");
                          _builder.newLineIfNotEmpty();
                          _builder.append("((");
                          JvmTypeReference _typeRef = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.geom.ocltypes.Cell");
                          _builder.append(_typeRef);
                          _builder.append(")");
                          String _name_2 = firstRole_1.getName();
                          _builder.append(_name_2);
                          _builder.append(".getSpatialType()).setGrid(grid1);");
                          _builder.newLineIfNotEmpty();
                          _builder.append("((");
                          JvmTypeReference _typeRef_1 = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.geom.ocltypes.Cell");
                          _builder.append(_typeRef_1);
                          _builder.append(")");
                          String _name_3 = secondRole_1.getName();
                          _builder.append(_name_3);
                          _builder.append(".getSpatialType()).setGrid(grid2);");
                          _builder.newLineIfNotEmpty();
                          _builder.append("updateRoleInfo();");
                          _builder.newLine();
                        }
                      };
                      this._jvmTypesBuilder.setBody(it_1, _client);
                    };
                    JvmConstructor _constructor_2 = this._jvmTypesBuilder.toConstructor(meln, _function_6);
                    this._jvmTypesBuilder.<JvmConstructor>operator_add(_members_13, _constructor_2);
                    EList<JvmMember> _members_14 = it.getMembers();
                    final Procedure1<JvmOperation> _function_7 = (JvmOperation it_1) -> {
                      EList<JvmFormalParameter> _parameters = it_1.getParameters();
                      JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "i", this._typeReferenceBuilder.typeRef("java.lang.Integer"));
                      this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                      StringConcatenationClient _client = new StringConcatenationClient() {
                        @Override
                        protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                          _builder.append("if (i==0) return ");
                          String _name = firstRole_1.getName();
                          _builder.append(_name);
                          _builder.append(";");
                          _builder.newLineIfNotEmpty();
                          _builder.append("else if (i==1) return ");
                          String _name_1 = secondRole_1.getName();
                          _builder.append(_name_1);
                          _builder.append(";");
                          _builder.newLineIfNotEmpty();
                          _builder.append("else return null;");
                          _builder.newLine();
                        }
                      };
                      this._jvmTypesBuilder.setBody(it_1, _client);
                    };
                    JvmOperation _method_3 = this._jvmTypesBuilder.toMethod(meln, "getRole", this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.relation.OcltRole"), _function_7);
                    this._jvmTypesBuilder.<JvmOperation>operator_add(_members_14, _method_3);
                    EList<JvmMember> _members_15 = it.getMembers();
                    final Procedure1<JvmOperation> _function_8 = (JvmOperation it_1) -> {
                      EList<JvmFormalParameter> _parameters = it_1.getParameters();
                      JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, firstRole_1.getName(), firstRoleType);
                      this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                      StringConcatenationClient _client = new StringConcatenationClient() {
                        @Override
                        protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                          _builder.append("Cell ");
                          String _name = firstRole_1.getName();
                          _builder.append(_name);
                          _builder.append("Cell = (");
                          JvmTypeReference _typeRef = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.geom.ocltypes.Cell");
                          _builder.append(_typeRef);
                          _builder.append(")");
                          String _name_1 = firstRole_1.getName();
                          _builder.append(_name_1);
                          _builder.append(".getSpatialType();");
                          _builder.newLineIfNotEmpty();
                          _builder.append("double[] ");
                          String _name_2 = firstRole_1.getName();
                          _builder.append(_name_2);
                          _builder.append("Coordinate = ");
                          String _name_3 = firstRole_1.getName();
                          _builder.append(_name_3);
                          _builder.append("Cell.getGrid().gridDoubleCoordinate(");
                          String _name_4 = firstRole_1.getName();
                          _builder.append(_name_4);
                          _builder.append("Cell.getX(), ");
                          String _name_5 = firstRole_1.getName();
                          _builder.append(_name_5);
                          _builder.append("Cell.getY());");
                          _builder.newLineIfNotEmpty();
                          _builder.append("Cell ");
                          String _name_6 = secondRole_1.getName();
                          _builder.append(_name_6);
                          _builder.append("Cell = (");
                          JvmTypeReference _typeRef_1 = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.geom.ocltypes.Cell");
                          _builder.append(_typeRef_1);
                          _builder.append(")");
                          String _name_7 = secondRole_1.getName();
                          _builder.append(_name_7);
                          _builder.append(".getSpatialType();");
                          _builder.newLineIfNotEmpty();
                          _builder.append("int[] newCoords = ");
                          String _name_8 = secondRole_1.getName();
                          _builder.append(_name_8);
                          _builder.append("Cell.getGrid().gridCoordinate(");
                          String _name_9 = firstRole_1.getName();
                          _builder.append(_name_9);
                          _builder.append("Coordinate[0], ");
                          String _name_10 = firstRole_1.getName();
                          _builder.append(_name_10);
                          _builder.append("Coordinate[1]);");
                          _builder.newLineIfNotEmpty();
                          String _name_11 = secondRole_1.getName();
                          _builder.append(_name_11);
                          _builder.append("Cell.setX(newCoords[0]);");
                          _builder.newLineIfNotEmpty();
                          String _name_12 = secondRole_1.getName();
                          _builder.append(_name_12);
                          _builder.append("Cell.setY(newCoords[1]);");
                          _builder.newLineIfNotEmpty();
                          _builder.append("return ");
                          String _name_13 = secondRole_1.getName();
                          _builder.append(_name_13);
                          _builder.append(";");
                          _builder.newLineIfNotEmpty();
                        }
                      };
                      this._jvmTypesBuilder.setBody(it_1, _client);
                    };
                    JvmOperation _method_4 = this._jvmTypesBuilder.toMethod(meln, "getNearest", secondRoleType, _function_8);
                    this._jvmTypesBuilder.<JvmOperation>operator_add(_members_15, _method_4);
                    EList<JvmMember> _members_16 = it.getMembers();
                    final Procedure1<JvmOperation> _function_9 = (JvmOperation it_1) -> {
                      EList<JvmFormalParameter> _parameters = it_1.getParameters();
                      JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, secondRole_1.getName(), secondRoleType);
                      this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                      StringConcatenationClient _client = new StringConcatenationClient() {
                        @Override
                        protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                          _builder.append("Cell ");
                          String _name = secondRole_1.getName();
                          _builder.append(_name);
                          _builder.append("Cell = (");
                          JvmTypeReference _typeRef = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.geom.ocltypes.Cell");
                          _builder.append(_typeRef);
                          _builder.append(")");
                          String _name_1 = secondRole_1.getName();
                          _builder.append(_name_1);
                          _builder.append(".getSpatialType();");
                          _builder.newLineIfNotEmpty();
                          _builder.append("double[] ");
                          String _name_2 = secondRole_1.getName();
                          _builder.append(_name_2);
                          _builder.append("Coordinate = ");
                          String _name_3 = secondRole_1.getName();
                          _builder.append(_name_3);
                          _builder.append("Cell.getGrid().gridDoubleCoordinate(");
                          String _name_4 = secondRole_1.getName();
                          _builder.append(_name_4);
                          _builder.append("Cell.getX(), ");
                          String _name_5 = secondRole_1.getName();
                          _builder.append(_name_5);
                          _builder.append("Cell.getY());");
                          _builder.newLineIfNotEmpty();
                          _builder.append("Cell ");
                          String _name_6 = firstRole_1.getName();
                          _builder.append(_name_6);
                          _builder.append("Cell = (");
                          JvmTypeReference _typeRef_1 = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.geom.ocltypes.Cell");
                          _builder.append(_typeRef_1);
                          _builder.append(")");
                          String _name_7 = firstRole_1.getName();
                          _builder.append(_name_7);
                          _builder.append(".getSpatialType();");
                          _builder.newLineIfNotEmpty();
                          _builder.append("int[] newCoords = ");
                          String _name_8 = firstRole_1.getName();
                          _builder.append(_name_8);
                          _builder.append("Cell.getGrid().gridCoordinate(");
                          String _name_9 = secondRole_1.getName();
                          _builder.append(_name_9);
                          _builder.append("Coordinate[0], ");
                          String _name_10 = secondRole_1.getName();
                          _builder.append(_name_10);
                          _builder.append("Coordinate[1]);");
                          _builder.newLineIfNotEmpty();
                          String _name_11 = firstRole_1.getName();
                          _builder.append(_name_11);
                          _builder.append("Cell.setX(newCoords[0]);");
                          _builder.newLineIfNotEmpty();
                          String _name_12 = firstRole_1.getName();
                          _builder.append(_name_12);
                          _builder.append("Cell.setY(newCoords[1]);");
                          _builder.newLineIfNotEmpty();
                          _builder.append("return ");
                          String _name_13 = firstRole_1.getName();
                          _builder.append(_name_13);
                          _builder.append(";");
                          _builder.newLineIfNotEmpty();
                        }
                      };
                      this._jvmTypesBuilder.setBody(it_1, _client);
                    };
                    JvmOperation _method_5 = this._jvmTypesBuilder.toMethod(meln, "getNearest", firstRoleType, _function_9);
                    this._jvmTypesBuilder.<JvmOperation>operator_add(_members_16, _method_5);
                    EList<JvmMember> _members_17 = it.getMembers();
                    final Procedure1<JvmOperation> _function_10 = (JvmOperation it_1) -> {
                      EList<JvmFormalParameter> _parameters = it_1.getParameters();
                      JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, firstRole_1.getName(), firstRoleType);
                      this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                      StringConcatenationClient _client = new StringConcatenationClient() {
                        @Override
                        protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                          _builder.append("Cell ");
                          String _name = firstRole_1.getName();
                          _builder.append(_name);
                          _builder.append("Cell = (");
                          JvmTypeReference _typeRef = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.geom.ocltypes.Cell");
                          _builder.append(_typeRef);
                          _builder.append(")");
                          String _name_1 = firstRole_1.getName();
                          _builder.append(_name_1);
                          _builder.append(".getSpatialType();");
                          _builder.newLineIfNotEmpty();
                          _builder.append("double[] ");
                          String _name_2 = firstRole_1.getName();
                          _builder.append(_name_2);
                          _builder.append("Coordinate = ");
                          String _name_3 = firstRole_1.getName();
                          _builder.append(_name_3);
                          _builder.append("Cell.getGrid().gridDoubleCoordinate(");
                          String _name_4 = firstRole_1.getName();
                          _builder.append(_name_4);
                          _builder.append("Cell.getX(), ");
                          String _name_5 = firstRole_1.getName();
                          _builder.append(_name_5);
                          _builder.append("Cell.getY());");
                          _builder.newLineIfNotEmpty();
                          _builder.append("Cell ");
                          String _name_6 = secondRole_1.getName();
                          _builder.append(_name_6);
                          _builder.append("Cell = (");
                          JvmTypeReference _typeRef_1 = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.geom.ocltypes.Cell");
                          _builder.append(_typeRef_1);
                          _builder.append(")");
                          String _name_7 = secondRole_1.getName();
                          _builder.append(_name_7);
                          _builder.append(".getSpatialType();");
                          _builder.newLineIfNotEmpty();
                          _builder.append("int[] newCoords = ");
                          String _name_8 = secondRole_1.getName();
                          _builder.append(_name_8);
                          _builder.append("Cell.getGrid().gridCoordinate(");
                          String _name_9 = firstRole_1.getName();
                          _builder.append(_name_9);
                          _builder.append("Coordinate[0], ");
                          String _name_10 = firstRole_1.getName();
                          _builder.append(_name_10);
                          _builder.append("Coordinate[1]);");
                          _builder.newLineIfNotEmpty();
                          _builder.append("if(newCoords == null){");
                          _builder.newLine();
                          _builder.append("   \t");
                          _builder.append("return false;");
                          _builder.newLine();
                          _builder.append("}");
                          _builder.newLine();
                          _builder.append("return true;");
                          _builder.newLine();
                        }
                      };
                      this._jvmTypesBuilder.setBody(it_1, _client);
                    };
                    JvmOperation _method_6 = this._jvmTypesBuilder.toMethod(meln, "hasNearest", this._typeReferenceBuilder.typeRef(Boolean.TYPE), _function_10);
                    this._jvmTypesBuilder.<JvmOperation>operator_add(_members_17, _method_6);
                    EList<JvmMember> _members_18 = it.getMembers();
                    final Procedure1<JvmOperation> _function_11 = (JvmOperation it_1) -> {
                      EList<JvmFormalParameter> _parameters = it_1.getParameters();
                      JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, secondRole_1.getName(), secondRoleType);
                      this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                      StringConcatenationClient _client = new StringConcatenationClient() {
                        @Override
                        protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                          _builder.append("Cell ");
                          String _name = secondRole_1.getName();
                          _builder.append(_name);
                          _builder.append("Cell = (");
                          JvmTypeReference _typeRef = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.geom.ocltypes.Cell");
                          _builder.append(_typeRef);
                          _builder.append(")");
                          String _name_1 = secondRole_1.getName();
                          _builder.append(_name_1);
                          _builder.append(".getSpatialType();");
                          _builder.newLineIfNotEmpty();
                          _builder.append("double[] ");
                          String _name_2 = secondRole_1.getName();
                          _builder.append(_name_2);
                          _builder.append("Coordinate = ");
                          String _name_3 = secondRole_1.getName();
                          _builder.append(_name_3);
                          _builder.append("Cell.getGrid().gridDoubleCoordinate(");
                          String _name_4 = secondRole_1.getName();
                          _builder.append(_name_4);
                          _builder.append("Cell.getX(), ");
                          String _name_5 = secondRole_1.getName();
                          _builder.append(_name_5);
                          _builder.append("Cell.getY());");
                          _builder.newLineIfNotEmpty();
                          _builder.append("Cell ");
                          String _name_6 = firstRole_1.getName();
                          _builder.append(_name_6);
                          _builder.append("Cell = (");
                          JvmTypeReference _typeRef_1 = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.geom.ocltypes.Cell");
                          _builder.append(_typeRef_1);
                          _builder.append(")");
                          String _name_7 = firstRole_1.getName();
                          _builder.append(_name_7);
                          _builder.append(".getSpatialType();");
                          _builder.newLineIfNotEmpty();
                          _builder.append("int[] newCoords = ");
                          String _name_8 = firstRole_1.getName();
                          _builder.append(_name_8);
                          _builder.append("Cell.getGrid().gridCoordinate(");
                          String _name_9 = secondRole_1.getName();
                          _builder.append(_name_9);
                          _builder.append("Coordinate[0], ");
                          String _name_10 = secondRole_1.getName();
                          _builder.append(_name_10);
                          _builder.append("Coordinate[1]);");
                          _builder.newLineIfNotEmpty();
                          _builder.append("if(newCoords == null){");
                          _builder.newLine();
                          _builder.append("\t");
                          _builder.append("return false;");
                          _builder.newLine();
                          _builder.append("}          \t\t\t\t\t\t\t");
                          _builder.newLine();
                          _builder.append("return true;");
                          _builder.newLine();
                        }
                      };
                      this._jvmTypesBuilder.setBody(it_1, _client);
                    };
                    JvmOperation _method_7 = this._jvmTypesBuilder.toMethod(meln, "hasNearest", this._typeReferenceBuilder.typeRef(Boolean.TYPE), _function_11);
                    this._jvmTypesBuilder.<JvmOperation>operator_add(_members_18, _method_7);
                    EList<JvmMember> _members_19 = it.getMembers();
                    final Procedure1<JvmOperation> _function_12 = (JvmOperation it_1) -> {
                      StringConcatenationClient _client = new StringConcatenationClient() {
                        @Override
                        protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                          _builder.append("this. ");
                          String _name = firstRole_1.getName();
                          _builder.append(_name);
                          _builder.append(".setX(x);");
                          _builder.newLineIfNotEmpty();
                          _builder.append("this. ");
                          String _name_1 = firstRole_1.getName();
                          _builder.append(_name_1);
                          _builder.append(".setY(y);");
                          _builder.newLineIfNotEmpty();
                          _builder.append("this. ");
                          String _name_2 = secondRole_1.getName();
                          _builder.append(_name_2);
                          _builder.append(".setX(x2);");
                          _builder.newLineIfNotEmpty();
                          _builder.append("this. ");
                          String _name_3 = secondRole_1.getName();
                          _builder.append(_name_3);
                          _builder.append(".setY(y2);");
                          _builder.newLineIfNotEmpty();
                          _builder.append("// this.e1.setX(x);");
                          _builder.newLine();
                          _builder.append("//this.e1.setY(y);");
                          _builder.newLine();
                          _builder.append("//this.e2.setX(x2);");
                          _builder.newLine();
                          _builder.append("//this.e2.setY(y2);");
                          _builder.newLine();
                          _builder.newLine();
                        }
                      };
                      this._jvmTypesBuilder.setBody(it_1, _client);
                    };
                    JvmOperation _method_8 = this._jvmTypesBuilder.toMethod(meln, "update", this._typeReferenceBuilder.typeRef(Void.TYPE), _function_12);
                    this._jvmTypesBuilder.<JvmOperation>operator_add(_members_19, _method_8);
                    final HashMap<String, JvmTypeReference> typeProps_1 = CollectionLiterals.<String, JvmTypeReference>newHashMap();
                    EList<RelElements> _relelns_1 = ((Relation)meln).getRelelns();
                    for (final RelElements reln_1 : _relelns_1) {
                      boolean _matched_4 = false;
                      if (reln_1 instanceof RelPropertyDef) {
                        _matched_4=true;
                        typeProps_1.put(((RelPropertyDef)reln_1).getName(), ((RelPropertyDef)reln_1).getType());
                      }
                      if (!_matched_4) {
                        if (reln_1 instanceof InteractionDef) {
                          _matched_4=true;
                          EList<JvmMember> _members_20 = it.getMembers();
                          final Procedure1<JvmOperation> _function_13 = (JvmOperation it_1) -> {
                            String params = "";
                            int index = 0;
                            EList<JvmFormalParameter> _params = ((InteractionDef)reln_1).getParams();
                            for (final JvmFormalParameter p : _params) {
                              {
                                EList<JvmFormalParameter> _parameters = it_1.getParameters();
                                JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(reln_1, p.getName(), p.getParameterType());
                                this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                                int _size_1 = ((InteractionDef)reln_1).getParams().size();
                                boolean _equals = (index == _size_1);
                                if (_equals) {
                                  String _name_4 = p.getName();
                                  String _plus_1 = (params + _name_4);
                                  params = _plus_1;
                                } else {
                                  String _name_5 = p.getName();
                                  String _plus_2 = (params + _name_5);
                                  String _plus_3 = (_plus_2 + ",");
                                  params = _plus_3;
                                }
                              }
                            }
                            this._jvmTypesBuilder.setBody(it_1, ((InteractionDef)reln_1).getBody());
                          };
                          JvmOperation _method_9 = this._jvmTypesBuilder.toMethod(reln_1, ((InteractionDef)reln_1).getName(), this._typeReferenceBuilder.typeRef(Void.TYPE), _function_13);
                          this._jvmTypesBuilder.<JvmOperation>operator_add(_members_20, _method_9);
                          int _size_1 = ((InteractionDef)reln_1).getComitexpressions().size();
                          boolean _greaterThan_1 = (_size_1 > 0);
                          if (_greaterThan_1) {
                            EList<JvmMember> _members_21 = it.getMembers();
                            String _name_4 = ((InteractionDef)reln_1).getName();
                            String _plus_1 = ("get_agr_" + _name_4);
                            final Procedure1<JvmOperation> _function_14 = (JvmOperation it_1) -> {
                              StringConcatenationClient _client = new StringConcatenationClient() {
                                @Override
                                protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                  _builder.append("          \t  \t      \t\t");
                                  int index = 0;
                                  _builder.newLineIfNotEmpty();
                                  _builder.append("          \t  \t      \t\t");
                                  _builder.append(listype, "          \t  \t      \t\t");
                                  _builder.append(" cvtList = new ");
                                  _builder.append(listype, "          \t  \t      \t\t");
                                  _builder.append("();\t");
                                  _builder.newLineIfNotEmpty();
                                  {
                                    EList<Comitexpr> _comitexpressions = ((InteractionDef)reln_1).getComitexpressions();
                                    for(final Comitexpr ce : _comitexpressions) {
                                      _builder.append(aggregType);
                                      _builder.append(" cvt");
                                      _builder.append(index);
                                      _builder.append(" = new ");
                                      _builder.append(aggregType);
                                      _builder.append("();\t");
                                      _builder.newLineIfNotEmpty();
                                      _builder.append("cvt");
                                      _builder.append(index);
                                      _builder.append(".setName(\"");
                                      String _prop = ce.getProp();
                                      _builder.append(_prop);
                                      _builder.append("\"); ");
                                      _builder.newLineIfNotEmpty();
                                      _builder.append("\t\t\t\t\t\t\t");
                                      _builder.newLine();
                                      JvmTypeReference _typeRef = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.geom.ocltypes.Cell");
                                      _builder.append(_typeRef);
                                      _builder.append(" cellIndex");
                                      _builder.append(index);
                                      _builder.append(" = (");
                                      JvmTypeReference _typeRef_1 = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.geom.ocltypes.Cell");
                                      _builder.append(_typeRef_1);
                                      _builder.append(")");
                                      String _name = ce.getRol().getName();
                                      _builder.append(_name);
                                      _builder.append(".getSpatialType();");
                                      _builder.newLineIfNotEmpty();
                                      _builder.append("cvt");
                                      _builder.append(index);
                                      _builder.append(".setCellOperator(new ");
                                      JvmTypeReference _agrfunc = ce.getAgrfunc();
                                      _builder.append(_agrfunc);
                                      _builder.append("(), ");
                                      String _name_1 = ce.getRol().getName();
                                      _builder.append(_name_1);
                                      _builder.append(".getTypeProps(), ");
                                      boolean _isUsepreval = ce.isUsepreval();
                                      _builder.append(_isUsepreval);
                                      _builder.append(", cellIndex");
                                      _builder.append(index);
                                      _builder.append(".getGrid());");
                                      _builder.newLineIfNotEmpty();
                                      _builder.append("cvtList.add(cvt");
                                      _builder.append(index);
                                      _builder.append(");");
                                      _builder.newLineIfNotEmpty();
                                      _builder.append("          \t  \t      \t  \t \t");
                                      Object _xblockexpression = null;
                                      {
                                        index = (index + 1);
                                        _xblockexpression = null;
                                      }
                                      _builder.append(_xblockexpression, "          \t  \t      \t  \t \t");
                                      _builder.newLineIfNotEmpty();
                                    }
                                  }
                                  _builder.append("return cvtList;");
                                  _builder.newLine();
                                }
                              };
                              this._jvmTypesBuilder.setBody(it_1, _client);
                            };
                            JvmOperation _method_10 = this._jvmTypesBuilder.toMethod(reln_1, _plus_1, listype, _function_14);
                            this._jvmTypesBuilder.<JvmOperation>operator_add(_members_21, _method_10);
                          } else {
                            EList<JvmMember> _members_22 = it.getMembers();
                            String _name_5 = ((InteractionDef)reln_1).getName();
                            String _plus_2 = ("get_agr_" + _name_5);
                            final Procedure1<JvmOperation> _function_15 = (JvmOperation it_1) -> {
                              StringConcatenationClient _client = new StringConcatenationClient() {
                                @Override
                                protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                  _builder.append("return null;");
                                  _builder.newLine();
                                }
                              };
                              this._jvmTypesBuilder.setBody(it_1, _client);
                            };
                            JvmOperation _method_11 = this._jvmTypesBuilder.toMethod(reln_1, _plus_2, listype, _function_15);
                            this._jvmTypesBuilder.<JvmOperation>operator_add(_members_22, _method_11);
                          }
                        }
                      }
                    }
                    int indexDouble_1 = 0;
                    int indexInteger_1 = 0;
                    int indexBoolean_1 = 0;
                    Set<String> _keySet_1 = typeProps_1.keySet();
                    for (final String name_1 : _keySet_1) {
                      {
                        boolean _equals = typeProps_1.get(name_1).getSimpleName().equals("Double");
                        if (_equals) {
                          final int index = indexDouble_1;
                          EList<JvmMember> _members_20 = it.getMembers();
                          String _firstUpper = StringExtensions.toFirstUpper(name_1);
                          String _plus_1 = ("set" + _firstUpper);
                          final Procedure1<JvmOperation> _function_13 = (JvmOperation it_1) -> {
                            EList<JvmFormalParameter> _parameters = it_1.getParameters();
                            JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "value", typeProps_1.get(name_1));
                            this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                            StringConcatenationClient _client = new StringConcatenationClient() {
                              @Override
                              protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                _builder.append("setDoubleProperty(");
                                _builder.append(index);
                                _builder.append(", value);");
                                _builder.newLineIfNotEmpty();
                              }
                            };
                            this._jvmTypesBuilder.setBody(it_1, _client);
                          };
                          JvmOperation _method_9 = this._jvmTypesBuilder.toMethod(meln, _plus_1, this._typeReferenceBuilder.typeRef(Void.TYPE), _function_13);
                          this._jvmTypesBuilder.<JvmOperation>operator_add(_members_20, _method_9);
                          EList<JvmMember> _members_21 = it.getMembers();
                          String _firstUpper_1 = StringExtensions.toFirstUpper(name_1);
                          String _plus_2 = ("get" + _firstUpper_1);
                          final Procedure1<JvmOperation> _function_14 = (JvmOperation it_1) -> {
                            StringConcatenationClient _client = new StringConcatenationClient() {
                              @Override
                              protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                _builder.append("return getDoubleProperty(");
                                _builder.append(index);
                                _builder.append(");");
                                _builder.newLineIfNotEmpty();
                              }
                            };
                            this._jvmTypesBuilder.setBody(it_1, _client);
                          };
                          JvmOperation _method_10 = this._jvmTypesBuilder.toMethod(meln, _plus_2, this._typeReferenceBuilder.typeRef("java.lang.Double"), _function_14);
                          this._jvmTypesBuilder.<JvmOperation>operator_add(_members_21, _method_10);
                          indexDouble_1 = (indexDouble_1 + 1);
                        }
                        boolean _equals_1 = typeProps_1.get(name_1).getSimpleName().equals("Integer");
                        if (_equals_1) {
                          final int index_1 = indexInteger_1;
                          EList<JvmMember> _members_22 = it.getMembers();
                          String _firstUpper_2 = StringExtensions.toFirstUpper(name_1);
                          String _plus_3 = ("set" + _firstUpper_2);
                          final Procedure1<JvmOperation> _function_15 = (JvmOperation it_1) -> {
                            EList<JvmFormalParameter> _parameters = it_1.getParameters();
                            JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "value", typeProps_1.get(name_1));
                            this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                            StringConcatenationClient _client = new StringConcatenationClient() {
                              @Override
                              protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                _builder.append("setIntegerProperty(");
                                _builder.append(index_1);
                                _builder.append(", value);");
                                _builder.newLineIfNotEmpty();
                              }
                            };
                            this._jvmTypesBuilder.setBody(it_1, _client);
                          };
                          JvmOperation _method_11 = this._jvmTypesBuilder.toMethod(meln, _plus_3, this._typeReferenceBuilder.typeRef(Void.TYPE), _function_15);
                          this._jvmTypesBuilder.<JvmOperation>operator_add(_members_22, _method_11);
                          EList<JvmMember> _members_23 = it.getMembers();
                          String _firstUpper_3 = StringExtensions.toFirstUpper(name_1);
                          String _plus_4 = ("get" + _firstUpper_3);
                          final Procedure1<JvmOperation> _function_16 = (JvmOperation it_1) -> {
                            StringConcatenationClient _client = new StringConcatenationClient() {
                              @Override
                              protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                _builder.append("return getIntegerProperty(");
                                _builder.append(index_1);
                                _builder.append(");");
                                _builder.newLineIfNotEmpty();
                              }
                            };
                            this._jvmTypesBuilder.setBody(it_1, _client);
                          };
                          JvmOperation _method_12 = this._jvmTypesBuilder.toMethod(meln, _plus_4, this._typeReferenceBuilder.typeRef("java.lang.Integer"), _function_16);
                          this._jvmTypesBuilder.<JvmOperation>operator_add(_members_23, _method_12);
                          indexInteger_1 = (indexInteger_1 + 1);
                        }
                        boolean _equals_2 = typeProps_1.get(name_1).getSimpleName().equals("Boolean");
                        if (_equals_2) {
                          final int index_2 = indexBoolean_1;
                          EList<JvmMember> _members_24 = it.getMembers();
                          String _firstUpper_4 = StringExtensions.toFirstUpper(name_1);
                          String _plus_5 = ("set" + _firstUpper_4);
                          final Procedure1<JvmOperation> _function_17 = (JvmOperation it_1) -> {
                            EList<JvmFormalParameter> _parameters = it_1.getParameters();
                            JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "value", typeProps_1.get(name_1));
                            this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                            StringConcatenationClient _client = new StringConcatenationClient() {
                              @Override
                              protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                _builder.append("setBooleanProperty(");
                                _builder.append(index_2);
                                _builder.append(", value);");
                                _builder.newLineIfNotEmpty();
                              }
                            };
                            this._jvmTypesBuilder.setBody(it_1, _client);
                          };
                          JvmOperation _method_13 = this._jvmTypesBuilder.toMethod(meln, _plus_5, this._typeReferenceBuilder.typeRef(Void.TYPE), _function_17);
                          this._jvmTypesBuilder.<JvmOperation>operator_add(_members_24, _method_13);
                          EList<JvmMember> _members_25 = it.getMembers();
                          String _firstUpper_5 = StringExtensions.toFirstUpper(name_1);
                          String _plus_6 = ("get" + _firstUpper_5);
                          final Procedure1<JvmOperation> _function_18 = (JvmOperation it_1) -> {
                            StringConcatenationClient _client = new StringConcatenationClient() {
                              @Override
                              protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                _builder.append("return getBooleanProperty(");
                                _builder.append(index_2);
                                _builder.append(");");
                                _builder.newLineIfNotEmpty();
                              }
                            };
                            this._jvmTypesBuilder.setBody(it_1, _client);
                          };
                          JvmOperation _method_14 = this._jvmTypesBuilder.toMethod(meln, _plus_6, this._typeReferenceBuilder.typeRef("java.lang.Boolean"), _function_18);
                          this._jvmTypesBuilder.<JvmOperation>operator_add(_members_25, _method_14);
                          indexBoolean_1 = (indexBoolean_1 + 1);
                        }
                      }
                    }
                    EList<JvmMember> _members_20 = it.getMembers();
                    final Procedure1<JvmOperation> _function_13 = (JvmOperation it_1) -> {
                      StringConcatenationClient _client = new StringConcatenationClient() {
                        @Override
                        protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                          _builder.append("KeyMap<String, String> properties = new KeyMap<String, String>();\t");
                          _builder.newLine();
                          {
                            Set<String> _keySet = typeProps_1.keySet();
                            for(final String name : _keySet) {
                              _builder.append("properties.put(\"");
                              _builder.append(name);
                              _builder.append("\",\"");
                              String _simpleName = typeProps_1.get(name).getSimpleName();
                              _builder.append(_simpleName);
                              _builder.append("\");");
                              _builder.newLineIfNotEmpty();
                            }
                          }
                          _builder.append("return properties;         \t  \t      \t\t");
                          _builder.newLine();
                        }
                      };
                      this._jvmTypesBuilder.setBody(it_1, _client);
                    };
                    JvmOperation _method_9 = this._jvmTypesBuilder.toMethod(meln, "getEdgeProperties", 
                      this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.ocltypes.KeyMap", 
                        this._typeReferenceBuilder.typeRef("java.lang.String"), this._typeReferenceBuilder.typeRef("java.lang.String")), _function_13);
                    this._jvmTypesBuilder.<JvmOperation>operator_add(_members_20, _method_9);
                  } else {
                    EList<JvmTypeReference> _superTypes_2 = it.getSuperTypes();
                    JvmTypeReference _typeRef_2 = this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.relation.CursorEdge");
                    this._jvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes_2, _typeRef_2);
                    final Role firstRole_2 = ((Relation)meln).getRoles().get(0);
                    final Role secondRole_2 = ((Relation)meln).getRoles().get(1);
                    final JvmTypeReference firstRoleType_1 = this._typeReferenceBuilder.typeRef(this._iQualifiedNameProvider.getFullyQualifiedName(firstRole_2.getType()).toString());
                    final JvmTypeReference secondRoleType_1 = this._typeReferenceBuilder.typeRef(this._iQualifiedNameProvider.getFullyQualifiedName(secondRole_2.getType()).toString());
                    final JvmTypeReference cellList = this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.ocltypes.List", firstRoleType_1);
                    String _name_4 = firstRole_2.getName();
                    final String firstName_1 = (_name_4 + "s");
                    JvmField jvmField_2 = this._jvmTypesBuilder.toField(meln, firstRole_2.getName(), firstRoleType_1);
                    if ((jvmField_2 != null)) {
                      jvmField_2.setFinal(false);
                      EList<JvmMember> _members_21 = it.getMembers();
                      this._jvmTypesBuilder.<JvmField>operator_add(_members_21, jvmField_2);
                    }
                    jvmField_2 = this._jvmTypesBuilder.toField(meln, secondRole_2.getName(), secondRoleType_1);
                    if ((jvmField_2 != null)) {
                      jvmField_2.setFinal(false);
                      EList<JvmMember> _members_22 = it.getMembers();
                      this._jvmTypesBuilder.<JvmField>operator_add(_members_22, jvmField_2);
                    }
                    EList<JvmMember> _members_23 = it.getMembers();
                    final Procedure1<JvmConstructor> _function_14 = (JvmConstructor it_1) -> {
                      EList<JvmFormalParameter> _parameters = it_1.getParameters();
                      JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, firstName_1, cellList);
                      this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                      StringConcatenationClient _client = new StringConcatenationClient() {
                        @Override
                        protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                          _builder.append("super(");
                          _builder.append(firstName_1);
                          _builder.append(");");
                          _builder.newLineIfNotEmpty();
                          String _name = firstRole_2.getName();
                          _builder.append(_name);
                          _builder.append(" = new ");
                          _builder.append(firstRoleType_1);
                          _builder.append("();");
                          _builder.newLineIfNotEmpty();
                          String _name_1 = secondRole_2.getName();
                          _builder.append(_name_1);
                          _builder.append(" = new ");
                          _builder.append(secondRoleType_1);
                          _builder.append("();");
                          _builder.newLineIfNotEmpty();
                          _builder.append("((");
                          JvmTypeReference _typeRef = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.geom.ocltypes.Cell");
                          _builder.append(_typeRef);
                          _builder.append(")");
                          String _name_2 = firstRole_2.getName();
                          _builder.append(_name_2);
                          _builder.append(".getSpatialType()).setGrid(grid);");
                          _builder.newLineIfNotEmpty();
                          _builder.append("((");
                          JvmTypeReference _typeRef_1 = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.geom.ocltypes.Cell");
                          _builder.append(_typeRef_1);
                          _builder.append(")");
                          String _name_3 = secondRole_2.getName();
                          _builder.append(_name_3);
                          _builder.append(".getSpatialType()).setGrid(grid);           \t\t\t    ");
                          _builder.newLineIfNotEmpty();
                        }
                      };
                      this._jvmTypesBuilder.setBody(it_1, _client);
                    };
                    JvmConstructor _constructor_3 = this._jvmTypesBuilder.toConstructor(meln, _function_14);
                    this._jvmTypesBuilder.<JvmConstructor>operator_add(_members_23, _constructor_3);
                    EList<JvmMember> _members_24 = it.getMembers();
                    final Procedure1<JvmOperation> _function_15 = (JvmOperation it_1) -> {
                      EList<JvmFormalParameter> _parameters = it_1.getParameters();
                      JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "i", this._typeReferenceBuilder.typeRef("java.lang.Integer"));
                      this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                      StringConcatenationClient _client = new StringConcatenationClient() {
                        @Override
                        protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                          _builder.append("if (i==0) return ");
                          String _name = firstRole_2.getName();
                          _builder.append(_name);
                          _builder.append(";");
                          _builder.newLineIfNotEmpty();
                          _builder.append("else if (i==1) return ");
                          String _name_1 = secondRole_2.getName();
                          _builder.append(_name_1);
                          _builder.append(";");
                          _builder.newLineIfNotEmpty();
                          _builder.append("else return null;");
                          _builder.newLine();
                        }
                      };
                      this._jvmTypesBuilder.setBody(it_1, _client);
                    };
                    JvmOperation _method_10 = this._jvmTypesBuilder.toMethod(meln, "getRole", this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.relation.OcltRole"), _function_15);
                    this._jvmTypesBuilder.<JvmOperation>operator_add(_members_24, _method_10);
                    EList<JvmMember> _members_25 = it.getMembers();
                    final Procedure1<JvmOperation> _function_16 = (JvmOperation it_1) -> {
                      StringConcatenationClient _client = new StringConcatenationClient() {
                        @Override
                        protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                          String _name = firstRole_2.getName();
                          _builder.append(_name);
                          _builder.append(".updateCellInfo(getCellType());");
                          _builder.newLineIfNotEmpty();
                          String _name_1 = secondRole_2.getName();
                          _builder.append(_name_1);
                          _builder.append(".updateCellInfo(getCellType());");
                          _builder.newLineIfNotEmpty();
                        }
                      };
                      this._jvmTypesBuilder.setBody(it_1, _client);
                    };
                    JvmOperation _method_11 = this._jvmTypesBuilder.toMethod(meln, "updateCellType", this._typeReferenceBuilder.typeRef(Void.TYPE), _function_16);
                    this._jvmTypesBuilder.<JvmOperation>operator_add(_members_25, _method_11);
                    EList<JvmMember> _members_26 = it.getMembers();
                    final Procedure1<JvmOperation> _function_17 = (JvmOperation it_1) -> {
                      StringConcatenationClient _client = new StringConcatenationClient() {
                        @Override
                        protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                          _builder.append("this. ");
                          String _name = firstRole_2.getName();
                          _builder.append(_name);
                          _builder.append(".setX(x);");
                          _builder.newLineIfNotEmpty();
                          _builder.append("this. ");
                          String _name_1 = firstRole_2.getName();
                          _builder.append(_name_1);
                          _builder.append(".setY(y);");
                          _builder.newLineIfNotEmpty();
                          _builder.append("this. ");
                          String _name_2 = secondRole_2.getName();
                          _builder.append(_name_2);
                          _builder.append(".setX(x2);");
                          _builder.newLineIfNotEmpty();
                          _builder.append("this. ");
                          String _name_3 = secondRole_2.getName();
                          _builder.append(_name_3);
                          _builder.append(".setY(y2);");
                          _builder.newLineIfNotEmpty();
                        }
                      };
                      this._jvmTypesBuilder.setBody(it_1, _client);
                    };
                    JvmOperation _method_12 = this._jvmTypesBuilder.toMethod(meln, "update", this._typeReferenceBuilder.typeRef(Void.TYPE), _function_17);
                    this._jvmTypesBuilder.<JvmOperation>operator_add(_members_26, _method_12);
                    final HashMap<String, JvmTypeReference> typeProps_2 = CollectionLiterals.<String, JvmTypeReference>newHashMap();
                    EList<RelElements> _relelns_2 = ((Relation)meln).getRelelns();
                    for (final RelElements reln_2 : _relelns_2) {
                      boolean _matched_5 = false;
                      if (reln_2 instanceof RelPropertyDef) {
                        _matched_5=true;
                        typeProps_2.put(((RelPropertyDef)reln_2).getName(), ((RelPropertyDef)reln_2).getType());
                      }
                      if (!_matched_5) {
                        if (reln_2 instanceof InteractionDef) {
                          _matched_5=true;
                          EList<JvmMember> _members_27 = it.getMembers();
                          final Procedure1<JvmOperation> _function_18 = (JvmOperation it_1) -> {
                            String params = "";
                            int index = 0;
                            EList<JvmFormalParameter> _params = ((InteractionDef)reln_2).getParams();
                            for (final JvmFormalParameter p : _params) {
                              {
                                EList<JvmFormalParameter> _parameters = it_1.getParameters();
                                JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(reln_2, p.getName(), p.getParameterType());
                                this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                                int _size_1 = ((InteractionDef)reln_2).getParams().size();
                                boolean _equals = (index == _size_1);
                                if (_equals) {
                                  String _name_5 = p.getName();
                                  String _plus_1 = (params + _name_5);
                                  params = _plus_1;
                                } else {
                                  String _name_6 = p.getName();
                                  String _plus_2 = (params + _name_6);
                                  String _plus_3 = (_plus_2 + ",");
                                  params = _plus_3;
                                }
                              }
                            }
                            this._jvmTypesBuilder.setBody(it_1, ((InteractionDef)reln_2).getBody());
                          };
                          JvmOperation _method_13 = this._jvmTypesBuilder.toMethod(reln_2, ((InteractionDef)reln_2).getName(), this._typeReferenceBuilder.typeRef(Void.TYPE), _function_18);
                          this._jvmTypesBuilder.<JvmOperation>operator_add(_members_27, _method_13);
                          int _size_1 = ((InteractionDef)reln_2).getComitexpressions().size();
                          boolean _greaterThan_1 = (_size_1 > 0);
                          if (_greaterThan_1) {
                            EList<JvmMember> _members_28 = it.getMembers();
                            String _name_5 = ((InteractionDef)reln_2).getName();
                            String _plus_1 = ("get_agr_" + _name_5);
                            final Procedure1<JvmOperation> _function_19 = (JvmOperation it_1) -> {
                              StringConcatenationClient _client = new StringConcatenationClient() {
                                @Override
                                protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                  _builder.append("          \t  \t      \t\t");
                                  int index = 0;
                                  _builder.newLineIfNotEmpty();
                                  _builder.append("          \t  \t      \t");
                                  _builder.append(listype, "          \t  \t      \t");
                                  _builder.append(" cvtList = new ");
                                  _builder.append(listype, "          \t  \t      \t");
                                  _builder.append("();");
                                  _builder.newLineIfNotEmpty();
                                  {
                                    EList<Comitexpr> _comitexpressions = ((InteractionDef)reln_2).getComitexpressions();
                                    for(final Comitexpr ce : _comitexpressions) {
                                      _builder.append(aggregType);
                                      _builder.append(" cvt");
                                      _builder.append(index);
                                      _builder.append(" = new ");
                                      _builder.append(aggregType);
                                      _builder.append("();");
                                      _builder.newLineIfNotEmpty();
                                      _builder.append("cvt");
                                      _builder.append(index);
                                      _builder.append(".setName(\"");
                                      String _prop = ce.getProp();
                                      _builder.append(_prop);
                                      _builder.append("\"); ");
                                      _builder.newLineIfNotEmpty();
                                      _builder.newLine();
                                      _builder.append("cvt");
                                      _builder.append(index);
                                      _builder.append(".setCellOperator(new ");
                                      JvmTypeReference _agrfunc = ce.getAgrfunc();
                                      _builder.append(_agrfunc);
                                      _builder.append("(), ");
                                      String _name = firstRole_2.getName();
                                      _builder.append(_name);
                                      _builder.append(".getTypeProps(), ");
                                      boolean _isUsepreval = ce.isUsepreval();
                                      _builder.append(_isUsepreval);
                                      _builder.append(");");
                                      _builder.newLineIfNotEmpty();
                                      _builder.append("cvtList.add(cvt");
                                      _builder.append(index);
                                      _builder.append(");");
                                      _builder.newLineIfNotEmpty();
                                      _builder.append("          \t  \t      \t  \t");
                                      Object _xblockexpression = null;
                                      {
                                        index = (index + 1);
                                        _xblockexpression = null;
                                      }
                                      _builder.append(_xblockexpression, "          \t  \t      \t  \t");
                                      _builder.newLineIfNotEmpty();
                                    }
                                  }
                                  _builder.append("return cvtList;");
                                  _builder.newLine();
                                }
                              };
                              this._jvmTypesBuilder.setBody(it_1, _client);
                            };
                            JvmOperation _method_14 = this._jvmTypesBuilder.toMethod(reln_2, _plus_1, listype, _function_19);
                            this._jvmTypesBuilder.<JvmOperation>operator_add(_members_28, _method_14);
                          } else {
                            EList<JvmMember> _members_29 = it.getMembers();
                            String _name_6 = ((InteractionDef)reln_2).getName();
                            String _plus_2 = ("get_agr_" + _name_6);
                            final Procedure1<JvmOperation> _function_20 = (JvmOperation it_1) -> {
                              StringConcatenationClient _client = new StringConcatenationClient() {
                                @Override
                                protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                  _builder.append("return null;");
                                  _builder.newLine();
                                }
                              };
                              this._jvmTypesBuilder.setBody(it_1, _client);
                            };
                            JvmOperation _method_15 = this._jvmTypesBuilder.toMethod(reln_2, _plus_2, listype, _function_20);
                            this._jvmTypesBuilder.<JvmOperation>operator_add(_members_29, _method_15);
                          }
                        }
                      }
                    }
                    int indexDouble_2 = 0;
                    int indexInteger_2 = 0;
                    int indexBoolean_2 = 0;
                    Set<String> _keySet_2 = typeProps_2.keySet();
                    for (final String name_2 : _keySet_2) {
                      {
                        boolean _equals = typeProps_2.get(name_2).getSimpleName().equals("Double");
                        if (_equals) {
                          final int index = indexDouble_2;
                          EList<JvmMember> _members_27 = it.getMembers();
                          String _firstUpper = StringExtensions.toFirstUpper(name_2);
                          String _plus_1 = ("set" + _firstUpper);
                          final Procedure1<JvmOperation> _function_18 = (JvmOperation it_1) -> {
                            EList<JvmFormalParameter> _parameters = it_1.getParameters();
                            JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "value", typeProps_2.get(name_2));
                            this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                            StringConcatenationClient _client = new StringConcatenationClient() {
                              @Override
                              protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                _builder.append("setDoubleProperty(");
                                _builder.append(index);
                                _builder.append(", value);");
                                _builder.newLineIfNotEmpty();
                              }
                            };
                            this._jvmTypesBuilder.setBody(it_1, _client);
                          };
                          JvmOperation _method_13 = this._jvmTypesBuilder.toMethod(meln, _plus_1, this._typeReferenceBuilder.typeRef(Void.TYPE), _function_18);
                          this._jvmTypesBuilder.<JvmOperation>operator_add(_members_27, _method_13);
                          EList<JvmMember> _members_28 = it.getMembers();
                          String _firstUpper_1 = StringExtensions.toFirstUpper(name_2);
                          String _plus_2 = ("get" + _firstUpper_1);
                          final Procedure1<JvmOperation> _function_19 = (JvmOperation it_1) -> {
                            StringConcatenationClient _client = new StringConcatenationClient() {
                              @Override
                              protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                _builder.append("return getDoubleProperty(");
                                _builder.append(index);
                                _builder.append(");");
                                _builder.newLineIfNotEmpty();
                              }
                            };
                            this._jvmTypesBuilder.setBody(it_1, _client);
                          };
                          JvmOperation _method_14 = this._jvmTypesBuilder.toMethod(meln, _plus_2, this._typeReferenceBuilder.typeRef("java.lang.Double"), _function_19);
                          this._jvmTypesBuilder.<JvmOperation>operator_add(_members_28, _method_14);
                          indexDouble_2 = (indexDouble_2 + 1);
                        }
                        boolean _equals_1 = typeProps_2.get(name_2).getSimpleName().equals("Integer");
                        if (_equals_1) {
                          final int index_1 = indexInteger_2;
                          EList<JvmMember> _members_29 = it.getMembers();
                          String _firstUpper_2 = StringExtensions.toFirstUpper(name_2);
                          String _plus_3 = ("set" + _firstUpper_2);
                          final Procedure1<JvmOperation> _function_20 = (JvmOperation it_1) -> {
                            EList<JvmFormalParameter> _parameters = it_1.getParameters();
                            JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "value", typeProps_2.get(name_2));
                            this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                            StringConcatenationClient _client = new StringConcatenationClient() {
                              @Override
                              protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                _builder.append("setIntegerProperty(");
                                _builder.append(index_1);
                                _builder.append(", value);");
                                _builder.newLineIfNotEmpty();
                              }
                            };
                            this._jvmTypesBuilder.setBody(it_1, _client);
                          };
                          JvmOperation _method_15 = this._jvmTypesBuilder.toMethod(meln, _plus_3, this._typeReferenceBuilder.typeRef(Void.TYPE), _function_20);
                          this._jvmTypesBuilder.<JvmOperation>operator_add(_members_29, _method_15);
                          EList<JvmMember> _members_30 = it.getMembers();
                          String _firstUpper_3 = StringExtensions.toFirstUpper(name_2);
                          String _plus_4 = ("get" + _firstUpper_3);
                          final Procedure1<JvmOperation> _function_21 = (JvmOperation it_1) -> {
                            StringConcatenationClient _client = new StringConcatenationClient() {
                              @Override
                              protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                _builder.append("return getIntegerProperty(");
                                _builder.append(index_1);
                                _builder.append(");");
                                _builder.newLineIfNotEmpty();
                              }
                            };
                            this._jvmTypesBuilder.setBody(it_1, _client);
                          };
                          JvmOperation _method_16 = this._jvmTypesBuilder.toMethod(meln, _plus_4, this._typeReferenceBuilder.typeRef("java.lang.Integer"), _function_21);
                          this._jvmTypesBuilder.<JvmOperation>operator_add(_members_30, _method_16);
                          indexInteger_2 = (indexInteger_2 + 1);
                        }
                        boolean _equals_2 = typeProps_2.get(name_2).getSimpleName().equals("Boolean");
                        if (_equals_2) {
                          final int index_2 = indexBoolean_2;
                          EList<JvmMember> _members_31 = it.getMembers();
                          String _firstUpper_4 = StringExtensions.toFirstUpper(name_2);
                          String _plus_5 = ("set" + _firstUpper_4);
                          final Procedure1<JvmOperation> _function_22 = (JvmOperation it_1) -> {
                            EList<JvmFormalParameter> _parameters = it_1.getParameters();
                            JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "value", typeProps_2.get(name_2));
                            this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                            StringConcatenationClient _client = new StringConcatenationClient() {
                              @Override
                              protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                _builder.append("setBooleanProperty(");
                                _builder.append(index_2);
                                _builder.append(", value);");
                                _builder.newLineIfNotEmpty();
                              }
                            };
                            this._jvmTypesBuilder.setBody(it_1, _client);
                          };
                          JvmOperation _method_17 = this._jvmTypesBuilder.toMethod(meln, _plus_5, this._typeReferenceBuilder.typeRef(Void.TYPE), _function_22);
                          this._jvmTypesBuilder.<JvmOperation>operator_add(_members_31, _method_17);
                          EList<JvmMember> _members_32 = it.getMembers();
                          String _firstUpper_5 = StringExtensions.toFirstUpper(name_2);
                          String _plus_6 = ("get" + _firstUpper_5);
                          final Procedure1<JvmOperation> _function_23 = (JvmOperation it_1) -> {
                            StringConcatenationClient _client = new StringConcatenationClient() {
                              @Override
                              protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                _builder.append("return getBooleanProperty(");
                                _builder.append(index_2);
                                _builder.append(");");
                                _builder.newLineIfNotEmpty();
                              }
                            };
                            this._jvmTypesBuilder.setBody(it_1, _client);
                          };
                          JvmOperation _method_18 = this._jvmTypesBuilder.toMethod(meln, _plus_6, this._typeReferenceBuilder.typeRef("java.lang.Boolean"), _function_23);
                          this._jvmTypesBuilder.<JvmOperation>operator_add(_members_32, _method_18);
                          indexBoolean_2 = (indexBoolean_2 + 1);
                        }
                      }
                    }
                    EList<JvmMember> _members_27 = it.getMembers();
                    final Procedure1<JvmOperation> _function_18 = (JvmOperation it_1) -> {
                      StringConcatenationClient _client = new StringConcatenationClient() {
                        @Override
                        protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                          _builder.append("KeyMap<String, String> properties = new KeyMap<String, String>();\t");
                          _builder.newLine();
                          {
                            Set<String> _keySet = typeProps_2.keySet();
                            for(final String name : _keySet) {
                              _builder.append("properties.put(\"");
                              _builder.append(name);
                              _builder.append("\",\"");
                              String _simpleName = typeProps_2.get(name).getSimpleName();
                              _builder.append(_simpleName);
                              _builder.append("\");");
                              _builder.newLineIfNotEmpty();
                            }
                          }
                          _builder.append("return properties;         \t  \t      \t\t");
                          _builder.newLine();
                        }
                      };
                      this._jvmTypesBuilder.setBody(it_1, _client);
                    };
                    JvmOperation _method_13 = this._jvmTypesBuilder.toMethod(meln, "getEdgeProperties", 
                      this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.ocltypes.KeyMap", 
                        this._typeReferenceBuilder.typeRef("java.lang.String"), this._typeReferenceBuilder.typeRef("java.lang.String")), _function_18);
                    this._jvmTypesBuilder.<JvmOperation>operator_add(_members_27, _method_13);
                  }
                } else {
                  EList<JvmTypeReference> _superTypes_3 = it.getSuperTypes();
                  JvmTypeReference _typeRef_3 = this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.relation.OcltEdge");
                  this._jvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes_3, _typeRef_3);
                  if ((((((((((((Relation)meln).getRoles().size() >= 2) && (((Relation)meln).getRoles().get(0) != null)) && (((Relation)meln).getRoles().get(1) != null)) && (((Relation)meln).getRoles().get(0).getType() != null)) && (((Relation)meln).getRoles().get(1).getType() != null)) && (this._iQualifiedNameProvider.getFullyQualifiedName(((Relation)meln).getRoles().get(0).getType()) != null)) && (this._iQualifiedNameProvider.getFullyQualifiedName(((Relation)meln).getRoles().get(1).getType()) != null)) && (((Relation)meln).getRoles().get(0).getName() != null)) && (((Relation)meln).getRoles().get(1).getName() != null))) {
                    final Role firstRole_3 = ((Relation)meln).getRoles().get(0);
                    final Role secondRole_3 = ((Relation)meln).getRoles().get(1);
                    final JvmTypeReference firstRoleType_2 = this._typeReferenceBuilder.typeRef(this._iQualifiedNameProvider.getFullyQualifiedName(firstRole_3.getType()).toString());
                    final JvmTypeReference secondRoleType_2 = this._typeReferenceBuilder.typeRef(this._iQualifiedNameProvider.getFullyQualifiedName(secondRole_3.getType()).toString());
                    JvmField jvmField_3 = this._jvmTypesBuilder.toField(meln, firstRole_3.getName(), firstRoleType_2);
                    if ((jvmField_3 != null)) {
                      jvmField_3.setFinal(false);
                      EList<JvmMember> _members_28 = it.getMembers();
                      this._jvmTypesBuilder.<JvmField>operator_add(_members_28, jvmField_3);
                      EList<JvmMember> _members_29 = it.getMembers();
                      JvmOperation _setter_2 = this._jvmTypesBuilder.toSetter(meln, firstRole_3.getName(), firstRoleType_2);
                      this._jvmTypesBuilder.<JvmOperation>operator_add(_members_29, _setter_2);
                      EList<JvmMember> _members_30 = it.getMembers();
                      JvmOperation _getter_2 = this._jvmTypesBuilder.toGetter(meln, firstRole_3.getName(), firstRoleType_2);
                      this._jvmTypesBuilder.<JvmOperation>operator_add(_members_30, _getter_2);
                    }
                    jvmField_3 = this._jvmTypesBuilder.toField(meln, secondRole_3.getName(), secondRoleType_2);
                    if ((jvmField_3 != null)) {
                      jvmField_3.setFinal(false);
                      EList<JvmMember> _members_31 = it.getMembers();
                      this._jvmTypesBuilder.<JvmField>operator_add(_members_31, jvmField_3);
                      EList<JvmMember> _members_32 = it.getMembers();
                      JvmOperation _setter_3 = this._jvmTypesBuilder.toSetter(meln, secondRole_3.getName(), secondRoleType_2);
                      this._jvmTypesBuilder.<JvmOperation>operator_add(_members_32, _setter_3);
                      EList<JvmMember> _members_33 = it.getMembers();
                      JvmOperation _getter_3 = this._jvmTypesBuilder.toGetter(meln, secondRole_3.getName(), secondRoleType_2);
                      this._jvmTypesBuilder.<JvmOperation>operator_add(_members_33, _getter_3);
                    }
                    EList<JvmMember> _members_34 = it.getMembers();
                    final Procedure1<JvmConstructor> _function_19 = (JvmConstructor it_1) -> {
                      EList<JvmFormalParameter> _parameters = it_1.getParameters();
                      JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "igr", this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.relation.InteractionGraph"));
                      this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                      EList<JvmFormalParameter> _parameters_1 = it_1.getParameters();
                      JvmFormalParameter _parameter_1 = this._jvmTypesBuilder.toParameter(meln, "first", firstRoleType_2);
                      this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_1, _parameter_1);
                      EList<JvmFormalParameter> _parameters_2 = it_1.getParameters();
                      JvmFormalParameter _parameter_2 = this._jvmTypesBuilder.toParameter(meln, "second", secondRoleType_2);
                      this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_2, _parameter_2);
                      StringConcatenationClient _client = new StringConcatenationClient() {
                        @Override
                        protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                          _builder.append("super(igr);");
                          _builder.newLine();
                          String _name = firstRole_3.getName();
                          _builder.append(_name);
                          _builder.append("=first;");
                          _builder.newLineIfNotEmpty();
                          String _name_1 = secondRole_3.getName();
                          _builder.append(_name_1);
                          _builder.append("=second;");
                          _builder.newLineIfNotEmpty();
                        }
                      };
                      this._jvmTypesBuilder.setBody(it_1, _client);
                    };
                    JvmConstructor _constructor_4 = this._jvmTypesBuilder.toConstructor(meln, _function_19);
                    this._jvmTypesBuilder.<JvmConstructor>operator_add(_members_34, _constructor_4);
                    EList<JvmMember> _members_35 = it.getMembers();
                    final Procedure1<JvmOperation> _function_20 = (JvmOperation it_1) -> {
                      EList<JvmFormalParameter> _parameters = it_1.getParameters();
                      JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "i", this._typeReferenceBuilder.typeRef("int"));
                      this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                      StringConcatenationClient _client = new StringConcatenationClient() {
                        @Override
                        protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                          _builder.append("if (i==0) return ");
                          String _name = firstRole_3.getName();
                          _builder.append(_name);
                          _builder.append(";");
                          _builder.newLineIfNotEmpty();
                          _builder.append("else if (i==1) return ");
                          String _name_1 = secondRole_3.getName();
                          _builder.append(_name_1);
                          _builder.append(";");
                          _builder.newLineIfNotEmpty();
                          _builder.append("else return null;");
                          _builder.newLine();
                        }
                      };
                      this._jvmTypesBuilder.setBody(it_1, _client);
                    };
                    JvmOperation _method_14 = this._jvmTypesBuilder.toMethod(meln, "getRole", this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.relation.OcltRole"), _function_20);
                    this._jvmTypesBuilder.<JvmOperation>operator_add(_members_35, _method_14);
                  }
                  EList<RelElements> _relelns_3 = ((Relation)meln).getRelelns();
                  for (final RelElements reln_3 : _relelns_3) {
                    boolean _matched_6 = false;
                    if (reln_3 instanceof RelPropertyDef) {
                      _matched_6=true;
                      final JvmField rField = this._jvmTypesBuilder.toField(reln_3, ((RelPropertyDef)reln_3).getName(), ((RelPropertyDef)reln_3).getType());
                      if ((rField != null)) {
                        rField.setFinal(false);
                        EList<JvmMember> _members_36 = it.getMembers();
                        this._jvmTypesBuilder.<JvmField>operator_add(_members_36, rField);
                        EList<JvmMember> _members_37 = it.getMembers();
                        JvmOperation _setter_4 = this._jvmTypesBuilder.toSetter(reln_3, ((RelPropertyDef)reln_3).getName(), ((RelPropertyDef)reln_3).getType());
                        this._jvmTypesBuilder.<JvmOperation>operator_add(_members_37, _setter_4);
                        EList<JvmMember> _members_38 = it.getMembers();
                        JvmOperation _getter_4 = this._jvmTypesBuilder.toGetter(reln_3, ((RelPropertyDef)reln_3).getName(), ((RelPropertyDef)reln_3).getType());
                        this._jvmTypesBuilder.<JvmOperation>operator_add(_members_38, _getter_4);
                      }
                    }
                    if (!_matched_6) {
                      if (reln_3 instanceof InteractionDef) {
                        _matched_6=true;
                        EList<JvmMember> _members_36 = it.getMembers();
                        final Procedure1<JvmOperation> _function_21 = (JvmOperation it_1) -> {
                          EList<JvmFormalParameter> _params = ((InteractionDef)reln_3).getParams();
                          for (final JvmFormalParameter p : _params) {
                            EList<JvmFormalParameter> _parameters = it_1.getParameters();
                            JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(reln_3, p.getName(), p.getParameterType());
                            this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                          }
                          this._jvmTypesBuilder.setBody(it_1, ((InteractionDef)reln_3).getBody());
                        };
                        JvmOperation _method_15 = this._jvmTypesBuilder.toMethod(reln_3, ((InteractionDef)reln_3).getName(), this._typeReferenceBuilder.typeRef(Void.TYPE), _function_21);
                        this._jvmTypesBuilder.<JvmOperation>operator_add(_members_36, _method_15);
                        int _size_1 = ((InteractionDef)reln_3).getComitexpressions().size();
                        boolean _greaterThan_1 = (_size_1 > 0);
                        if (_greaterThan_1) {
                          EList<JvmMember> _members_37 = it.getMembers();
                          String _name_5 = ((InteractionDef)reln_3).getName();
                          String _plus_1 = ("_agr_" + _name_5);
                          final Procedure1<JvmOperation> _function_22 = (JvmOperation it_1) -> {
                            StringConcatenationClient _client = new StringConcatenationClient() {
                              @Override
                              protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                {
                                  EList<Comitexpr> _comitexpressions = ((InteractionDef)reln_3).getComitexpressions();
                                  for(final Comitexpr ce : _comitexpressions) {
                                    _builder.newLineIfNotEmpty();
                                    _builder.append("this.");
                                    String _name = ce.getRol().getName();
                                    _builder.append(_name);
                                    _builder.append(".setAgregOp(\"");
                                    String _prop = ce.getProp();
                                    _builder.append(_prop);
                                    _builder.append("\",new ");
                                    JvmTypeReference _agrfunc = ce.getAgrfunc();
                                    _builder.append(_agrfunc);
                                    _builder.append("(),");
                                    boolean _isUsepreval = ce.isUsepreval();
                                    _builder.append(_isUsepreval);
                                    _builder.append(");");
                                    _builder.newLineIfNotEmpty();
                                  }
                                }
                              }
                            };
                            this._jvmTypesBuilder.setBody(it_1, _client);
                          };
                          JvmOperation _method_16 = this._jvmTypesBuilder.toMethod(reln_3, _plus_1, this._typeReferenceBuilder.typeRef(Void.TYPE), _function_22);
                          this._jvmTypesBuilder.<JvmOperation>operator_add(_members_37, _method_16);
                        }
                      }
                    }
                  }
                }
              }
            };
            acceptor.<JvmGenericType>accept(this._jvmTypesBuilder.toClass(modl, edgecname), _function);
            EList<RelElements> _relelns = ((Relation)meln).getRelelns();
            for (final RelElements reln : _relelns) {
              boolean _matched_1 = false;
              if (reln instanceof Filterdef) {
                _matched_1=true;
                String _plus_1 = (graphcname + "_");
                String _name_2 = ((Filterdef)reln).getName();
                final String filterfqn = (_plus_1 + _name_2);
                final Procedure1<JvmGenericType> _function_1 = (JvmGenericType it) -> {
                  final boolean isAutoGraph = ((Relation)meln).getRoles().get(0).getType().equals(((Relation)meln).getRoles().get(1).getType());
                  boolean isCellGraph = false;
                  boolean isCellGeomGraph = false;
                  boolean testCell1 = false;
                  boolean testCell2 = false;
                  boolean testGeom1 = false;
                  boolean testGeom2 = false;
                  final Entity rol1 = ((Relation)meln).getRoles().get(0).getType();
                  final Entity rol2 = ((Relation)meln).getRoles().get(1).getType();
                  EList<EObject> _eContents = rol1.eContents();
                  for (final EObject e : _eContents) {
                    boolean _matched_2 = false;
                    if (e instanceof PropertyDef) {
                      _matched_2=true;
                      boolean _equals = ((PropertyDef)e).getType().getSimpleName().equals("Cell");
                      if (_equals) {
                        testCell1 = true;
                      }
                      if (((((((((PropertyDef)e).getType().getSimpleName().equals("Line") || ((PropertyDef)e).getType().getSimpleName().equals("MultiLine")) || 
                        ((PropertyDef)e).getType().getSimpleName().equals("Polygon")) || ((PropertyDef)e).getType().getSimpleName().equals("MultiPolygon")) || 
                        ((PropertyDef)e).getType().getSimpleName().equals("Point")) || ((PropertyDef)e).getType().getSimpleName().equals("MultiPoint")) || 
                        ((PropertyDef)e).getType().getSimpleName().equals("Ring"))) {
                        testGeom1 = true;
                      }
                    }
                  }
                  EList<EObject> _eContents_1 = rol2.eContents();
                  for (final EObject e_1 : _eContents_1) {
                    boolean _matched_3 = false;
                    if (e_1 instanceof PropertyDef) {
                      _matched_3=true;
                      boolean _equals = ((PropertyDef)e_1).getType().getSimpleName().equals("Cell");
                      if (_equals) {
                        testCell2 = true;
                      }
                      if (((((((((PropertyDef)e_1).getType().getSimpleName().equals("Line") || ((PropertyDef)e_1).getType().getSimpleName().equals("MultiLine")) || 
                        ((PropertyDef)e_1).getType().getSimpleName().equals("Polygon")) || ((PropertyDef)e_1).getType().getSimpleName().equals("MultiPolygon")) || 
                        ((PropertyDef)e_1).getType().getSimpleName().equals("Point")) || ((PropertyDef)e_1).getType().getSimpleName().equals("MultiPoint")) || 
                        ((PropertyDef)e_1).getType().getSimpleName().equals("Ring"))) {
                        testGeom2 = true;
                      }
                    }
                  }
                  if ((testCell1 && testCell2)) {
                    isCellGraph = true;
                  }
                  if (((testGeom1 && testCell2) || (testGeom2 && testCell1))) {
                    isCellGeomGraph = true;
                  }
                  if (isCellGeomGraph) {
                    final Role firstRole = ((Relation)meln).getRoles().get(0);
                    final Role secondRole = ((Relation)meln).getRoles().get(1);
                    final JvmTypeReference firstRoleType = this._typeReferenceBuilder.typeRef(this._iQualifiedNameProvider.getFullyQualifiedName(firstRole.getType()).toString());
                    final JvmTypeReference secondRoleType = this._typeReferenceBuilder.typeRef(this._iQualifiedNameProvider.getFullyQualifiedName(secondRole.getType()).toString());
                    EList<JvmTypeReference> _superTypes = it.getSuperTypes();
                    JvmTypeReference _typeRef = this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.relation.EdgeFilter", firstRoleType, secondRoleType);
                    this._jvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes, _typeRef);
                    EList<JvmFormalParameter> _params = ((Filterdef)reln).getParams();
                    for (final JvmFormalParameter p : _params) {
                      {
                        final JvmField pfield = this._jvmTypesBuilder.toField(reln, p.getName(), p.getParameterType());
                        if ((pfield != null)) {
                          pfield.setFinal(false);
                          EList<JvmMember> _members = it.getMembers();
                          this._jvmTypesBuilder.<JvmField>operator_add(_members, pfield);
                        }
                      }
                    }
                    EList<JvmMember> _members = it.getMembers();
                    final Procedure1<JvmConstructor> _function_2 = (JvmConstructor it_1) -> {
                      EList<JvmFormalParameter> _params_1 = ((Filterdef)reln).getParams();
                      for (final JvmFormalParameter p_1 : _params_1) {
                        EList<JvmFormalParameter> _parameters = it_1.getParameters();
                        JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(reln, p_1.getName(), p_1.getParameterType());
                        this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                      }
                      StringConcatenationClient _client = new StringConcatenationClient() {
                        @Override
                        protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                          {
                            EList<JvmFormalParameter> _params = ((Filterdef)reln).getParams();
                            for(final JvmFormalParameter p : _params) {
                              _builder.append("this.");
                              String _name = p.getName();
                              _builder.append(_name);
                              _builder.append(" = ");
                              String _name_1 = p.getName();
                              _builder.append(_name_1);
                              _builder.append(";");
                              _builder.newLineIfNotEmpty();
                            }
                          }
                        }
                      };
                      this._jvmTypesBuilder.setBody(it_1, _client);
                    };
                    JvmConstructor _constructor = this._jvmTypesBuilder.toConstructor(reln, _function_2);
                    this._jvmTypesBuilder.<JvmConstructor>operator_add(_members, _constructor);
                    EList<JvmMember> _members_1 = it.getMembers();
                    final Procedure1<JvmOperation> _function_3 = (JvmOperation it_1) -> {
                      EList<JvmFormalParameter> _parameters = it_1.getParameters();
                      JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(reln, firstRole.getName(), firstRoleType);
                      this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                      EList<JvmFormalParameter> _parameters_1 = it_1.getParameters();
                      JvmFormalParameter _parameter_1 = this._jvmTypesBuilder.toParameter(reln, secondRole.getName(), secondRoleType);
                      this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_1, _parameter_1);
                      this._jvmTypesBuilder.setBody(it_1, ((Filterdef)reln).getBody());
                    };
                    JvmOperation _method = this._jvmTypesBuilder.toMethod(reln, "filter", this._typeReferenceBuilder.typeRef("java.lang.Boolean"), _function_3);
                    this._jvmTypesBuilder.<JvmOperation>operator_add(_members_1, _method);
                  } else {
                    if (isCellGraph) {
                      if ((!isAutoGraph)) {
                        final Role firstRole_1 = ((Relation)meln).getRoles().get(0);
                        final Role secondRole_1 = ((Relation)meln).getRoles().get(1);
                        final JvmTypeReference firstRoleType_1 = this._typeReferenceBuilder.typeRef(this._iQualifiedNameProvider.getFullyQualifiedName(firstRole_1.getType()).toString());
                        final JvmTypeReference secondRoleType_1 = this._typeReferenceBuilder.typeRef(this._iQualifiedNameProvider.getFullyQualifiedName(secondRole_1.getType()).toString());
                        EList<JvmTypeReference> _superTypes_1 = it.getSuperTypes();
                        JvmTypeReference _typeRef_1 = this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.relation.EdgeFilter", firstRoleType_1, secondRoleType_1);
                        this._jvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes_1, _typeRef_1);
                        EList<JvmFormalParameter> _params_1 = ((Filterdef)reln).getParams();
                        for (final JvmFormalParameter p_1 : _params_1) {
                          {
                            final JvmField pfield = this._jvmTypesBuilder.toField(reln, p_1.getName(), p_1.getParameterType());
                            if ((pfield != null)) {
                              pfield.setFinal(false);
                              EList<JvmMember> _members_2 = it.getMembers();
                              this._jvmTypesBuilder.<JvmField>operator_add(_members_2, pfield);
                            }
                          }
                        }
                        EList<JvmMember> _members_2 = it.getMembers();
                        final Procedure1<JvmConstructor> _function_4 = (JvmConstructor it_1) -> {
                          EList<JvmFormalParameter> _params_2 = ((Filterdef)reln).getParams();
                          for (final JvmFormalParameter p_2 : _params_2) {
                            EList<JvmFormalParameter> _parameters = it_1.getParameters();
                            JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(reln, p_2.getName(), p_2.getParameterType());
                            this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                          }
                          StringConcatenationClient _client = new StringConcatenationClient() {
                            @Override
                            protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                              {
                                EList<JvmFormalParameter> _params = ((Filterdef)reln).getParams();
                                for(final JvmFormalParameter p : _params) {
                                  _builder.append("this.");
                                  String _name = p.getName();
                                  _builder.append(_name);
                                  _builder.append(" = ");
                                  String _name_1 = p.getName();
                                  _builder.append(_name_1);
                                  _builder.append(";");
                                  _builder.newLineIfNotEmpty();
                                }
                              }
                            }
                          };
                          this._jvmTypesBuilder.setBody(it_1, _client);
                        };
                        JvmConstructor _constructor_1 = this._jvmTypesBuilder.toConstructor(reln, _function_4);
                        this._jvmTypesBuilder.<JvmConstructor>operator_add(_members_2, _constructor_1);
                        EList<JvmMember> _members_3 = it.getMembers();
                        final Procedure1<JvmOperation> _function_5 = (JvmOperation it_1) -> {
                          EList<JvmFormalParameter> _parameters = it_1.getParameters();
                          JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(reln, firstRole_1.getName(), firstRoleType_1);
                          this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                          EList<JvmFormalParameter> _parameters_1 = it_1.getParameters();
                          JvmFormalParameter _parameter_1 = this._jvmTypesBuilder.toParameter(reln, secondRole_1.getName(), secondRoleType_1);
                          this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_1, _parameter_1);
                          this._jvmTypesBuilder.setBody(it_1, ((Filterdef)reln).getBody());
                        };
                        JvmOperation _method_1 = this._jvmTypesBuilder.toMethod(reln, "filter", this._typeReferenceBuilder.typeRef("java.lang.Boolean"), _function_5);
                        this._jvmTypesBuilder.<JvmOperation>operator_add(_members_3, _method_1);
                      } else {
                        final Role firstRole_2 = ((Relation)meln).getRoles().get(0);
                        final Role secondRole_2 = ((Relation)meln).getRoles().get(1);
                        final JvmTypeReference firstRoleType_2 = this._typeReferenceBuilder.typeRef(this._iQualifiedNameProvider.getFullyQualifiedName(firstRole_2.getType()).toString());
                        final JvmTypeReference secondRoleType_2 = this._typeReferenceBuilder.typeRef(this._iQualifiedNameProvider.getFullyQualifiedName(secondRole_2.getType()).toString());
                        EList<JvmTypeReference> _superTypes_2 = it.getSuperTypes();
                        JvmTypeReference _typeRef_2 = this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.relation.EdgeFilter", firstRoleType_2, secondRoleType_2);
                        this._jvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes_2, _typeRef_2);
                        EList<JvmFormalParameter> _params_2 = ((Filterdef)reln).getParams();
                        for (final JvmFormalParameter p_2 : _params_2) {
                          {
                            final JvmField pfield = this._jvmTypesBuilder.toField(reln, p_2.getName(), p_2.getParameterType());
                            if ((pfield != null)) {
                              pfield.setFinal(false);
                              EList<JvmMember> _members_4 = it.getMembers();
                              this._jvmTypesBuilder.<JvmField>operator_add(_members_4, pfield);
                            }
                          }
                        }
                        EList<JvmMember> _members_4 = it.getMembers();
                        final Procedure1<JvmConstructor> _function_6 = (JvmConstructor it_1) -> {
                          EList<JvmFormalParameter> _params_3 = ((Filterdef)reln).getParams();
                          for (final JvmFormalParameter p_3 : _params_3) {
                            EList<JvmFormalParameter> _parameters = it_1.getParameters();
                            JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(reln, p_3.getName(), p_3.getParameterType());
                            this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                          }
                          StringConcatenationClient _client = new StringConcatenationClient() {
                            @Override
                            protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                              {
                                EList<JvmFormalParameter> _params = ((Filterdef)reln).getParams();
                                for(final JvmFormalParameter p : _params) {
                                  _builder.append("this.");
                                  String _name = p.getName();
                                  _builder.append(_name);
                                  _builder.append(" = ");
                                  String _name_1 = p.getName();
                                  _builder.append(_name_1);
                                  _builder.append(";");
                                  _builder.newLineIfNotEmpty();
                                }
                              }
                            }
                          };
                          this._jvmTypesBuilder.setBody(it_1, _client);
                        };
                        JvmConstructor _constructor_2 = this._jvmTypesBuilder.toConstructor(reln, _function_6);
                        this._jvmTypesBuilder.<JvmConstructor>operator_add(_members_4, _constructor_2);
                        EList<JvmMember> _members_5 = it.getMembers();
                        final Procedure1<JvmOperation> _function_7 = (JvmOperation it_1) -> {
                          EList<JvmFormalParameter> _parameters = it_1.getParameters();
                          JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(reln, firstRole_2.getName(), firstRoleType_2);
                          this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                          EList<JvmFormalParameter> _parameters_1 = it_1.getParameters();
                          JvmFormalParameter _parameter_1 = this._jvmTypesBuilder.toParameter(reln, secondRole_2.getName(), secondRoleType_2);
                          this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_1, _parameter_1);
                          this._jvmTypesBuilder.setBody(it_1, ((Filterdef)reln).getBody());
                        };
                        JvmOperation _method_2 = this._jvmTypesBuilder.toMethod(reln, "filter", this._typeReferenceBuilder.typeRef("java.lang.Boolean"), _function_7);
                        this._jvmTypesBuilder.<JvmOperation>operator_add(_members_5, _method_2);
                      }
                    } else {
                      if ((((((((((((Relation)meln).getRoles().size() >= 2) && (((Relation)meln).getRoles().get(0) != null)) && (((Relation)meln).getRoles().get(1) != null)) && (((Relation)meln).getRoles().get(0).getType() != null)) && (((Relation)meln).getRoles().get(1).getType() != null)) && (this._iQualifiedNameProvider.getFullyQualifiedName(((Relation)meln).getRoles().get(0).getType()) != null)) && (this._iQualifiedNameProvider.getFullyQualifiedName(((Relation)meln).getRoles().get(1).getType()) != null)) && (((Relation)meln).getRoles().get(0).getName() != null)) && (((Relation)meln).getRoles().get(1).getName() != null))) {
                        final Role firstRole_3 = ((Relation)meln).getRoles().get(0);
                        final Role secondRole_3 = ((Relation)meln).getRoles().get(1);
                        final JvmTypeReference firstRoleType_3 = this._typeReferenceBuilder.typeRef(this._iQualifiedNameProvider.getFullyQualifiedName(firstRole_3.getType()).toString());
                        final JvmTypeReference secondRoleType_3 = this._typeReferenceBuilder.typeRef(this._iQualifiedNameProvider.getFullyQualifiedName(secondRole_3.getType()).toString());
                        EList<JvmTypeReference> _superTypes_3 = it.getSuperTypes();
                        JvmTypeReference _typeRef_3 = this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.relation.EdgeFilter", firstRoleType_3, secondRoleType_3);
                        this._jvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes_3, _typeRef_3);
                        EList<JvmFormalParameter> _params_3 = ((Filterdef)reln).getParams();
                        for (final JvmFormalParameter p_3 : _params_3) {
                          {
                            final JvmField pfield = this._jvmTypesBuilder.toField(reln, p_3.getName(), p_3.getParameterType());
                            if ((pfield != null)) {
                              pfield.setFinal(false);
                              EList<JvmMember> _members_6 = it.getMembers();
                              this._jvmTypesBuilder.<JvmField>operator_add(_members_6, pfield);
                            }
                          }
                        }
                        EList<JvmMember> _members_6 = it.getMembers();
                        final Procedure1<JvmConstructor> _function_8 = (JvmConstructor it_1) -> {
                          EList<JvmFormalParameter> _params_4 = ((Filterdef)reln).getParams();
                          for (final JvmFormalParameter p_4 : _params_4) {
                            EList<JvmFormalParameter> _parameters = it_1.getParameters();
                            JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(reln, p_4.getName(), p_4.getParameterType());
                            this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                          }
                          StringConcatenationClient _client = new StringConcatenationClient() {
                            @Override
                            protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                              {
                                EList<JvmFormalParameter> _params = ((Filterdef)reln).getParams();
                                for(final JvmFormalParameter p : _params) {
                                  _builder.append("this.");
                                  String _name = p.getName();
                                  _builder.append(_name);
                                  _builder.append(" = ");
                                  String _name_1 = p.getName();
                                  _builder.append(_name_1);
                                  _builder.append(";");
                                  _builder.newLineIfNotEmpty();
                                }
                              }
                            }
                          };
                          this._jvmTypesBuilder.setBody(it_1, _client);
                        };
                        JvmConstructor _constructor_3 = this._jvmTypesBuilder.toConstructor(reln, _function_8);
                        this._jvmTypesBuilder.<JvmConstructor>operator_add(_members_6, _constructor_3);
                        EList<JvmMember> _members_7 = it.getMembers();
                        final Procedure1<JvmOperation> _function_9 = (JvmOperation it_1) -> {
                          EList<JvmFormalParameter> _parameters = it_1.getParameters();
                          JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(reln, firstRole_3.getName(), firstRoleType_3);
                          this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                          EList<JvmFormalParameter> _parameters_1 = it_1.getParameters();
                          JvmFormalParameter _parameter_1 = this._jvmTypesBuilder.toParameter(reln, secondRole_3.getName(), secondRoleType_3);
                          this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_1, _parameter_1);
                          this._jvmTypesBuilder.setBody(it_1, ((Filterdef)reln).getBody());
                        };
                        JvmOperation _method_3 = this._jvmTypesBuilder.toMethod(reln, "filter", this._typeReferenceBuilder.typeRef("java.lang.Boolean"), _function_9);
                        this._jvmTypesBuilder.<JvmOperation>operator_add(_members_7, _method_3);
                      }
                    }
                  }
                };
                acceptor.<JvmGenericType>accept(this._jvmTypesBuilder.toClass(modl, filterfqn), _function_1);
              }
            }
            JvmTypeReference _typeRef = this._typeReferenceBuilder.typeRef(edgecname);
            boolean _tripleNotEquals = (_typeRef != null);
            if (_tripleNotEquals) {
              final Procedure1<JvmGenericType> _function_1 = (JvmGenericType it) -> {
                this._jvmTypesBuilder.setDocumentation(it, this._jvmTypesBuilder.getDocumentation(meln));
                final boolean isAutoGraph = ((Relation)meln).getRoles().get(0).getType().equals(((Relation)meln).getRoles().get(1).getType());
                boolean isCellGraph = false;
                boolean isCellGeomGraph = false;
                boolean testCell1 = false;
                boolean testCell2 = false;
                boolean testGeom1 = false;
                boolean testGeom2 = false;
                final Entity rol1 = ((Relation)meln).getRoles().get(0).getType();
                final Entity rol2 = ((Relation)meln).getRoles().get(1).getType();
                EList<EObject> _eContents = rol1.eContents();
                for (final EObject e : _eContents) {
                  boolean _matched_2 = false;
                  if (e instanceof PropertyDef) {
                    _matched_2=true;
                    boolean _equals = ((PropertyDef)e).getType().getSimpleName().equals("Cell");
                    if (_equals) {
                      testCell1 = true;
                    }
                    if (((((((((PropertyDef)e).getType().getSimpleName().equals("Line") || ((PropertyDef)e).getType().getSimpleName().equals("MultiLine")) || 
                      ((PropertyDef)e).getType().getSimpleName().equals("Polygon")) || ((PropertyDef)e).getType().getSimpleName().equals("MultiPolygon")) || 
                      ((PropertyDef)e).getType().getSimpleName().equals("Point")) || ((PropertyDef)e).getType().getSimpleName().equals("MultiPoint")) || 
                      ((PropertyDef)e).getType().getSimpleName().equals("Ring"))) {
                      testGeom1 = true;
                    }
                  }
                }
                EList<EObject> _eContents_1 = rol2.eContents();
                for (final EObject e_1 : _eContents_1) {
                  boolean _matched_3 = false;
                  if (e_1 instanceof PropertyDef) {
                    _matched_3=true;
                    boolean _equals = ((PropertyDef)e_1).getType().getSimpleName().equals("Cell");
                    if (_equals) {
                      testCell2 = true;
                    }
                    if (((((((((PropertyDef)e_1).getType().getSimpleName().equals("Line") || ((PropertyDef)e_1).getType().getSimpleName().equals("MultiLine")) || 
                      ((PropertyDef)e_1).getType().getSimpleName().equals("Polygon")) || ((PropertyDef)e_1).getType().getSimpleName().equals("MultiPolygon")) || 
                      ((PropertyDef)e_1).getType().getSimpleName().equals("Point")) || ((PropertyDef)e_1).getType().getSimpleName().equals("MultiPoint")) || 
                      ((PropertyDef)e_1).getType().getSimpleName().equals("Ring"))) {
                      testGeom2 = true;
                    }
                  }
                }
                if ((testCell1 && testCell2)) {
                  isCellGraph = true;
                }
                if (((testGeom1 && testCell2) || (testGeom2 && testCell1))) {
                  isCellGeomGraph = true;
                }
                String graphname = "fr.ocelet.runtime.relation.impl.AutoGraph";
                if ((!isAutoGraph)) {
                  graphname = "fr.ocelet.runtime.relation.impl.DiGraph";
                }
                if (isCellGraph) {
                  if (isAutoGraph) {
                    graphname = "fr.ocelet.runtime.relation.impl.CellGraph";
                  } else {
                    graphname = "fr.ocelet.runtime.relation.impl.DiCellGraph";
                  }
                }
                if (isCellGeomGraph) {
                  graphname = "fr.ocelet.runtime.relation.impl.GeometryCellGraph";
                }
                final String graphTypeName = graphname;
                if (isCellGeomGraph) {
                  final Role firstRole = ((Relation)meln).getRoles().get(0);
                  final Role secondRole = ((Relation)meln).getRoles().get(1);
                  JvmTypeReference tempcellType = this._typeReferenceBuilder.typeRef(this._iQualifiedNameProvider.getFullyQualifiedName(firstRole.getType()).toString());
                  JvmTypeReference tempgeomType = this._typeReferenceBuilder.typeRef(this._iQualifiedNameProvider.getFullyQualifiedName(secondRole.getType()).toString());
                  String tempCellName = firstRole.getName();
                  String tempGeomName = secondRole.getName();
                  if (testCell2) {
                    tempcellType = this._typeReferenceBuilder.typeRef(this._iQualifiedNameProvider.getFullyQualifiedName(secondRole.getType()).toString());
                    tempCellName = secondRole.getName();
                    tempgeomType = this._typeReferenceBuilder.typeRef(this._iQualifiedNameProvider.getFullyQualifiedName(firstRole.getType()).toString());
                    tempGeomName = firstRole.getName();
                  }
                  final JvmTypeReference cellType = tempcellType;
                  final JvmTypeReference geomType = tempgeomType;
                  final String cellName = tempCellName;
                  final String geomName = tempGeomName;
                  EList<JvmTypeReference> _superTypes = it.getSuperTypes();
                  JvmTypeReference _typeRef_1 = this._typeReferenceBuilder.typeRef(graphTypeName, this._typeReferenceBuilder.typeRef(edgecname), cellType, geomType);
                  this._jvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes, _typeRef_1);
                  EList<JvmMember> _members = it.getMembers();
                  final Procedure1<JvmConstructor> _function_2 = (JvmConstructor it_1) -> {
                    StringConcatenationClient _client = new StringConcatenationClient() {
                      @Override
                      protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                        _builder.append("super();");
                        _builder.newLine();
                      }
                    };
                    this._jvmTypesBuilder.setBody(it_1, _client);
                  };
                  JvmConstructor _constructor = this._jvmTypesBuilder.toConstructor(meln, _function_2);
                  this._jvmTypesBuilder.<JvmConstructor>operator_add(_members, _constructor);
                  final JvmTypeReference geomList = this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.ocltypes.List", geomType);
                  final JvmTypeReference cellList = this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.ocltypes.List", cellType);
                  final String firstName = (cellName + "s");
                  final String secondName = (geomName + "s");
                  if ((testGeom1 == true)) {
                    EList<JvmMember> _members_1 = it.getMembers();
                    final Procedure1<JvmOperation> _function_3 = (JvmOperation it_1) -> {
                      EList<JvmFormalParameter> _parameters = it_1.getParameters();
                      JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, secondName, geomList);
                      this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                      EList<JvmFormalParameter> _parameters_1 = it_1.getParameters();
                      JvmFormalParameter _parameter_1 = this._jvmTypesBuilder.toParameter(meln, firstName, cellList);
                      this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_1, _parameter_1);
                      StringConcatenationClient _client = new StringConcatenationClient() {
                        @Override
                        protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                          JvmTypeReference _typeRef = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef(edgecname);
                          _builder.append(_typeRef);
                          _builder.append(" _gen_edge = new ");
                          String _name = ((Relation)meln).getName();
                          String _plus = (_name + "_Edge");
                          _builder.append(_plus);
                          _builder.append("(");
                          _builder.append(firstName);
                          _builder.append(", ");
                          _builder.append(secondName);
                          _builder.append(");");
                          _builder.newLineIfNotEmpty();
                          _builder.append("setCompleteIteratorGeomCell(_gen_edge);");
                          _builder.newLine();
                        }
                      };
                      this._jvmTypesBuilder.setBody(it_1, _client);
                    };
                    JvmOperation _method = this._jvmTypesBuilder.toMethod(meln, "connect", this._typeReferenceBuilder.typeRef(Void.TYPE), _function_3);
                    this._jvmTypesBuilder.<JvmOperation>operator_add(_members_1, _method);
                    EList<JvmMember> _members_2 = it.getMembers();
                    final Procedure1<JvmOperation> _function_4 = (JvmOperation it_1) -> {
                      EList<JvmFormalParameter> _parameters = it_1.getParameters();
                      JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, secondName, geomList);
                      this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                      EList<JvmFormalParameter> _parameters_1 = it_1.getParameters();
                      JvmFormalParameter _parameter_1 = this._jvmTypesBuilder.toParameter(meln, firstName, cellList);
                      this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_1, _parameter_1);
                      EList<JvmFormalParameter> _parameters_2 = it_1.getParameters();
                      JvmFormalParameter _parameter_2 = this._jvmTypesBuilder.toParameter(meln, "distance", this._typeReferenceBuilder.typeRef(Double.TYPE));
                      this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_2, _parameter_2);
                      StringConcatenationClient _client = new StringConcatenationClient() {
                        @Override
                        protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                          JvmTypeReference _typeRef = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef(edgecname);
                          _builder.append(_typeRef);
                          _builder.append(" _gen_edge = new ");
                          String _name = ((Relation)meln).getName();
                          String _plus = (_name + "_Edge");
                          _builder.append(_plus);
                          _builder.append("(");
                          _builder.append(firstName);
                          _builder.append(", ");
                          _builder.append(secondName);
                          _builder.append(", distance);");
                          _builder.newLineIfNotEmpty();
                          _builder.append("setCompleteIteratorGeomCell(_gen_edge);");
                          _builder.newLine();
                        }
                      };
                      this._jvmTypesBuilder.setBody(it_1, _client);
                    };
                    JvmOperation _method_1 = this._jvmTypesBuilder.toMethod(meln, "connect", this._typeReferenceBuilder.typeRef(Void.TYPE), _function_4);
                    this._jvmTypesBuilder.<JvmOperation>operator_add(_members_2, _method_1);
                  } else {
                    EList<JvmMember> _members_3 = it.getMembers();
                    final Procedure1<JvmOperation> _function_5 = (JvmOperation it_1) -> {
                      EList<JvmFormalParameter> _parameters = it_1.getParameters();
                      JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, firstName, cellList);
                      this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                      EList<JvmFormalParameter> _parameters_1 = it_1.getParameters();
                      JvmFormalParameter _parameter_1 = this._jvmTypesBuilder.toParameter(meln, secondName, geomList);
                      this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_1, _parameter_1);
                      StringConcatenationClient _client = new StringConcatenationClient() {
                        @Override
                        protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                          JvmTypeReference _typeRef = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef(edgecname);
                          _builder.append(_typeRef);
                          _builder.append(" _gen_edge = new ");
                          String _name = ((Relation)meln).getName();
                          String _plus = (_name + "_Edge");
                          _builder.append(_plus);
                          _builder.append("(");
                          _builder.append(firstName);
                          _builder.append(", ");
                          _builder.append(secondName);
                          _builder.append(");");
                          _builder.newLineIfNotEmpty();
                          _builder.append("setCompleteIteratorGeomCell(_gen_edge);");
                          _builder.newLine();
                        }
                      };
                      this._jvmTypesBuilder.setBody(it_1, _client);
                    };
                    JvmOperation _method_2 = this._jvmTypesBuilder.toMethod(meln, "connect", this._typeReferenceBuilder.typeRef(Void.TYPE), _function_5);
                    this._jvmTypesBuilder.<JvmOperation>operator_add(_members_3, _method_2);
                    EList<JvmMember> _members_4 = it.getMembers();
                    final Procedure1<JvmOperation> _function_6 = (JvmOperation it_1) -> {
                      EList<JvmFormalParameter> _parameters = it_1.getParameters();
                      JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, firstName, cellList);
                      this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                      EList<JvmFormalParameter> _parameters_1 = it_1.getParameters();
                      JvmFormalParameter _parameter_1 = this._jvmTypesBuilder.toParameter(meln, secondName, geomList);
                      this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_1, _parameter_1);
                      EList<JvmFormalParameter> _parameters_2 = it_1.getParameters();
                      JvmFormalParameter _parameter_2 = this._jvmTypesBuilder.toParameter(meln, "distance", this._typeReferenceBuilder.typeRef(Double.TYPE));
                      this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_2, _parameter_2);
                      StringConcatenationClient _client = new StringConcatenationClient() {
                        @Override
                        protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                          JvmTypeReference _typeRef = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef(edgecname);
                          _builder.append(_typeRef);
                          _builder.append(" _gen_edge = new ");
                          String _name = ((Relation)meln).getName();
                          String _plus = (_name + "_Edge");
                          _builder.append(_plus);
                          _builder.append("(");
                          _builder.append(firstName);
                          _builder.append(", ");
                          _builder.append(secondName);
                          _builder.append(", distance);");
                          _builder.newLineIfNotEmpty();
                          _builder.append("setCompleteIteratorGeomCell(_gen_edge);");
                          _builder.newLine();
                        }
                      };
                      this._jvmTypesBuilder.setBody(it_1, _client);
                    };
                    JvmOperation _method_3 = this._jvmTypesBuilder.toMethod(meln, "connect", this._typeReferenceBuilder.typeRef(Void.TYPE), _function_6);
                    this._jvmTypesBuilder.<JvmOperation>operator_add(_members_4, _method_3);
                  }
                  EList<RelElements> _relelns_1 = ((Relation)meln).getRelelns();
                  for (final RelElements reln_1 : _relelns_1) {
                    boolean _matched_4 = false;
                    if (reln_1 instanceof InteractionDef) {
                      _matched_4=true;
                      EList<JvmMember> _members_5 = it.getMembers();
                      final Procedure1<JvmOperation> _function_7 = (JvmOperation it_1) -> {
                        EList<JvmFormalParameter> _params = ((InteractionDef)reln_1).getParams();
                        for (final JvmFormalParameter p : _params) {
                          EList<JvmFormalParameter> _parameters = it_1.getParameters();
                          JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(reln_1, p.getName(), p.getParameterType());
                          this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                        }
                        StringConcatenationClient _client = new StringConcatenationClient() {
                          @Override
                          protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                            _builder.append("setMode(2);");
                            _builder.newLine();
                            _builder.append("cleanOperator();");
                            _builder.newLine();
                            _builder.append(listype);
                            _builder.append(" cvtList = ((");
                            JvmTypeReference _typeRef = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef(edgecname);
                            _builder.append(_typeRef);
                            _builder.append(")getEdge()).get_agr_");
                            String _name = ((InteractionDef)reln_1).getName();
                            _builder.append(_name);
                            _builder.append("();");
                            _builder.newLineIfNotEmpty();
                            _builder.append(" \t\t");
                            _builder.append("if(cvtList != null){");
                            _builder.newLine();
                            _builder.append(" \t\t\t");
                            _builder.append("for(");
                            _builder.append(aggregType, " \t\t\t");
                            _builder.append(" cvt : cvtList) {");
                            _builder.newLineIfNotEmpty();
                            _builder.append(" \t\t\t\t");
                            _builder.append("setCellOperator(cvt);");
                            _builder.newLine();
                            _builder.append(" \t\t\t");
                            _builder.append("}");
                            _builder.newLine();
                            _builder.append(" \t\t");
                            _builder.append("}");
                            _builder.newLine();
                            _builder.append("beginTransaction();");
                            _builder.newLine();
                            _builder.append("initInteraction();");
                            _builder.newLine();
                            _builder.append("for(");
                            JvmTypeReference _typeRef_1 = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef(edgecname);
                            _builder.append(_typeRef_1);
                            _builder.append(" _edg_ : this){");
                            _builder.newLineIfNotEmpty();
                            _builder.append("\t");
                            _builder.append("_edg_.");
                            String _name_1 = ((InteractionDef)reln_1).getName();
                            _builder.append(_name_1, "\t");
                            _builder.append("(");
                            int ci = 0;
                            {
                              EList<JvmFormalParameter> _params = ((InteractionDef)reln_1).getParams();
                              for(final JvmFormalParameter p : _params) {
                                {
                                  if ((ci > 0)) {
                                    _builder.append(",");
                                  }
                                }
                                String _name_2 = p.getName();
                                _builder.append(_name_2, "\t");
                                Object _xblockexpression = null;
                                {
                                  ci = 1;
                                  _xblockexpression = null;
                                }
                                _builder.append(_xblockexpression, "\t");
                              }
                            }
                            _builder.append(");");
                            _builder.newLineIfNotEmpty();
                            {
                              int _size = ((InteractionDef)reln_1).getComitexpressions().size();
                              boolean _greaterThan = (_size > 0);
                              if (_greaterThan) {
                                _builder.append("                      \t \t");
                                boolean test = false;
                                _builder.newLineIfNotEmpty();
                                {
                                  EList<Comitexpr> _comitexpressions = ((InteractionDef)reln_1).getComitexpressions();
                                  for(final Comitexpr ce : _comitexpressions) {
                                    {
                                      boolean _equals = ce.getRol().getType().equals(cellType);
                                      boolean _not = (!_equals);
                                      if (_not) {
                                        Object _xblockexpression_1 = null;
                                        {
                                          test = true;
                                          _xblockexpression_1 = null;
                                        }
                                        _builder.append(_xblockexpression_1);
                                        _builder.newLineIfNotEmpty();
                                      }
                                    }
                                  }
                                }
                                {
                                  if (test = true) {
                                    _builder.append("_edg_._agr_");
                                    String _name_3 = ((InteractionDef)reln_1).getName();
                                    _builder.append(_name_3);
                                    _builder.append("();");
                                    _builder.newLineIfNotEmpty();
                                  }
                                }
                              }
                            }
                            _builder.append("}");
                            _builder.newLine();
                            _builder.append("endTransaction();");
                            _builder.newLine();
                            _builder.append("endInteraction();");
                            _builder.newLine();
                          }
                        };
                        this._jvmTypesBuilder.setBody(it_1, _client);
                      };
                      JvmOperation _method_4 = this._jvmTypesBuilder.toMethod(reln_1, ((InteractionDef)reln_1).getName(), this._typeReferenceBuilder.typeRef(Void.TYPE), _function_7);
                      this._jvmTypesBuilder.<JvmOperation>operator_add(_members_5, _method_4);
                    }
                    if (!_matched_4) {
                      if (reln_1 instanceof Filterdef) {
                        _matched_4=true;
                        EList<JvmMember> _members_5 = it.getMembers();
                        final Procedure1<JvmOperation> _function_7 = (JvmOperation it_1) -> {
                          EList<JvmFormalParameter> _params = ((Filterdef)reln_1).getParams();
                          for (final JvmFormalParameter p : _params) {
                            EList<JvmFormalParameter> _parameters = it_1.getParameters();
                            JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(reln_1, p.getName(), p.getParameterType());
                            this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                          }
                          StringConcatenationClient _client = new StringConcatenationClient() {
                            @Override
                            protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                              String _name = ((Relation)meln).getName();
                              String _plus = (_name + "_");
                              String _name_1 = ((Filterdef)reln_1).getName();
                              String _plus_1 = (_plus + _name_1);
                              _builder.append(_plus_1);
                              _builder.append(" _filter = new ");
                              String _name_2 = ((Relation)meln).getName();
                              String _plus_2 = (_name_2 + "_");
                              String _name_3 = ((Filterdef)reln_1).getName();
                              String _plus_3 = (_plus_2 + _name_3);
                              _builder.append(_plus_3);
                              _builder.append("(");
                              _builder.newLineIfNotEmpty();
                              {
                                int _size = ((Filterdef)reln_1).getParams().size();
                                boolean _greaterThan = (_size > 0);
                                if (_greaterThan) {
                                  {
                                    int _size_1 = ((Filterdef)reln_1).getParams().size();
                                    int _minus = (_size_1 - 1);
                                    IntegerRange _upTo = new IntegerRange(0, _minus);
                                    for(final Integer i : _upTo) {
                                      String _name_4 = ((Filterdef)reln_1).getParams().get((i).intValue()).getName();
                                      _builder.append(_name_4);
                                      _builder.newLineIfNotEmpty();
                                      {
                                        int _size_2 = ((Filterdef)reln_1).getParams().size();
                                        int _minus_1 = (_size_2 - 1);
                                        boolean _lessThan = ((i).intValue() < _minus_1);
                                        if (_lessThan) {
                                          _builder.append(",");
                                          _builder.newLine();
                                        }
                                      }
                                    }
                                  }
                                }
                              }
                              _builder.append(");");
                              _builder.newLine();
                              _builder.append("super.addFilter(_filter);");
                              _builder.newLine();
                              _builder.append("return this;");
                              _builder.newLine();
                            }
                          };
                          this._jvmTypesBuilder.setBody(it_1, _client);
                        };
                        JvmOperation _method_4 = this._jvmTypesBuilder.toMethod(reln_1, ((Filterdef)reln_1).getName(), this._typeReferenceBuilder.typeRef(graphcname.toString()), _function_7);
                        this._jvmTypesBuilder.<JvmOperation>operator_add(_members_5, _method_4);
                      }
                    }
                  }
                } else {
                  if (isCellGraph) {
                    if ((!isAutoGraph)) {
                      final Role firstRole_1 = ((Relation)meln).getRoles().get(0);
                      final Role secondRole_1 = ((Relation)meln).getRoles().get(1);
                      final JvmTypeReference firstRoleType = this._typeReferenceBuilder.typeRef(this._iQualifiedNameProvider.getFullyQualifiedName(firstRole_1.getType()).toString());
                      final JvmTypeReference secondRoleType = this._typeReferenceBuilder.typeRef(this._iQualifiedNameProvider.getFullyQualifiedName(secondRole_1.getType()).toString());
                      final JvmTypeReference firstCellList = this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.ocltypes.List", firstRoleType);
                      final JvmTypeReference secondCellList = this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.ocltypes.List", secondRoleType);
                      String _name_2 = firstRole_1.getName();
                      final String firstName_1 = (_name_2 + "s");
                      String _name_3 = secondRole_1.getName();
                      final String secondName_1 = (_name_3 + "s");
                      EList<JvmTypeReference> _superTypes_1 = it.getSuperTypes();
                      JvmTypeReference _typeRef_2 = this._typeReferenceBuilder.typeRef(graphTypeName, this._typeReferenceBuilder.typeRef(edgecname), firstRoleType, secondRoleType);
                      this._jvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes_1, _typeRef_2);
                      EList<JvmMember> _members_5 = it.getMembers();
                      final Procedure1<JvmConstructor> _function_7 = (JvmConstructor it_1) -> {
                        StringConcatenationClient _client = new StringConcatenationClient() {
                          @Override
                          protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                            _builder.append("super();");
                            _builder.newLine();
                          }
                        };
                        this._jvmTypesBuilder.setBody(it_1, _client);
                      };
                      JvmConstructor _constructor_1 = this._jvmTypesBuilder.toConstructor(meln, _function_7);
                      this._jvmTypesBuilder.<JvmConstructor>operator_add(_members_5, _constructor_1);
                      EList<JvmMember> _members_6 = it.getMembers();
                      final Procedure1<JvmOperation> _function_8 = (JvmOperation it_1) -> {
                        EList<JvmFormalParameter> _parameters = it_1.getParameters();
                        JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, firstRole_1.getName(), firstRoleType);
                        this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                        StringConcatenationClient _client = new StringConcatenationClient() {
                          @Override
                          protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                            _builder.newLine();
                            _builder.append("\t");
                            _builder.append("return ((");
                            JvmTypeReference _typeRef = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef(edgecname);
                            _builder.append(_typeRef, "\t");
                            _builder.append(")getEdge()).getNearest(");
                            String _name = firstRole_1.getName();
                            _builder.append(_name, "\t");
                            _builder.append(");");
                            _builder.newLineIfNotEmpty();
                          }
                        };
                        this._jvmTypesBuilder.setBody(it_1, _client);
                      };
                      JvmOperation _method_4 = this._jvmTypesBuilder.toMethod(meln, "getNearest", secondRoleType, _function_8);
                      this._jvmTypesBuilder.<JvmOperation>operator_add(_members_6, _method_4);
                      EList<JvmMember> _members_7 = it.getMembers();
                      final Procedure1<JvmOperation> _function_9 = (JvmOperation it_1) -> {
                        EList<JvmFormalParameter> _parameters = it_1.getParameters();
                        JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, secondRole_1.getName(), secondRoleType);
                        this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                        StringConcatenationClient _client = new StringConcatenationClient() {
                          @Override
                          protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                            _builder.append("return ((");
                            JvmTypeReference _typeRef = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef(edgecname);
                            _builder.append(_typeRef);
                            _builder.append(")getEdge()).getNearest(");
                            String _name = secondRole_1.getName();
                            _builder.append(_name);
                            _builder.append(");");
                            _builder.newLineIfNotEmpty();
                          }
                        };
                        this._jvmTypesBuilder.setBody(it_1, _client);
                      };
                      JvmOperation _method_5 = this._jvmTypesBuilder.toMethod(meln, "getNearest", firstRoleType, _function_9);
                      this._jvmTypesBuilder.<JvmOperation>operator_add(_members_7, _method_5);
                      EList<JvmMember> _members_8 = it.getMembers();
                      final Procedure1<JvmOperation> _function_10 = (JvmOperation it_1) -> {
                        EList<JvmFormalParameter> _parameters = it_1.getParameters();
                        JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, firstRole_1.getName(), firstRoleType);
                        this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                        StringConcatenationClient _client = new StringConcatenationClient() {
                          @Override
                          protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                            _builder.newLine();
                            _builder.append("\t");
                            _builder.append("return ((");
                            JvmTypeReference _typeRef = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef(edgecname);
                            _builder.append(_typeRef, "\t");
                            _builder.append(")getEdge()).hasNearest(");
                            String _name = firstRole_1.getName();
                            _builder.append(_name, "\t");
                            _builder.append(");");
                            _builder.newLineIfNotEmpty();
                          }
                        };
                        this._jvmTypesBuilder.setBody(it_1, _client);
                      };
                      JvmOperation _method_6 = this._jvmTypesBuilder.toMethod(meln, "hasNearest", this._typeReferenceBuilder.typeRef(Boolean.TYPE), _function_10);
                      this._jvmTypesBuilder.<JvmOperation>operator_add(_members_8, _method_6);
                      EList<JvmMember> _members_9 = it.getMembers();
                      final Procedure1<JvmOperation> _function_11 = (JvmOperation it_1) -> {
                        EList<JvmFormalParameter> _parameters = it_1.getParameters();
                        JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, secondRole_1.getName(), secondRoleType);
                        this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                        StringConcatenationClient _client = new StringConcatenationClient() {
                          @Override
                          protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                            _builder.append("return ((");
                            JvmTypeReference _typeRef = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef(edgecname);
                            _builder.append(_typeRef);
                            _builder.append(")getEdge()).hasNearest(");
                            String _name = secondRole_1.getName();
                            _builder.append(_name);
                            _builder.append(");");
                            _builder.newLineIfNotEmpty();
                          }
                        };
                        this._jvmTypesBuilder.setBody(it_1, _client);
                      };
                      JvmOperation _method_7 = this._jvmTypesBuilder.toMethod(meln, "hasNearest", this._typeReferenceBuilder.typeRef(Boolean.TYPE), _function_11);
                      this._jvmTypesBuilder.<JvmOperation>operator_add(_members_9, _method_7);
                      EList<JvmMember> _members_10 = it.getMembers();
                      final Procedure1<JvmOperation> _function_12 = (JvmOperation it_1) -> {
                        EList<JvmFormalParameter> _parameters = it_1.getParameters();
                        JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, firstName_1, firstCellList);
                        this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                        EList<JvmFormalParameter> _parameters_1 = it_1.getParameters();
                        JvmFormalParameter _parameter_1 = this._jvmTypesBuilder.toParameter(meln, secondName_1, secondCellList);
                        this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_1, _parameter_1);
                        StringConcatenationClient _client = new StringConcatenationClient() {
                          @Override
                          protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                            _builder.append("super.setGrid(");
                            _builder.append(firstName_1);
                            _builder.append(", ");
                            _builder.append(secondName_1);
                            _builder.append(");");
                            _builder.newLineIfNotEmpty();
                            JvmTypeReference _typeRef = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef(edgecname);
                            _builder.append(_typeRef);
                            _builder.append(" _gen_edge_ = new ");
                            String _name = ((Relation)meln).getName();
                            String _plus = (_name + "_Edge");
                            _builder.append(_plus);
                            _builder.append("(");
                            _builder.append(firstName_1);
                            _builder.append(", ");
                            _builder.append(secondName_1);
                            _builder.append(");");
                            _builder.newLineIfNotEmpty();
                            _builder.append("setCompleteIteratorDiCell(_gen_edge_ );");
                            _builder.newLine();
                          }
                        };
                        this._jvmTypesBuilder.setBody(it_1, _client);
                      };
                      JvmOperation _method_8 = this._jvmTypesBuilder.toMethod(meln, "connect", this._typeReferenceBuilder.typeRef(Void.TYPE), _function_12);
                      this._jvmTypesBuilder.<JvmOperation>operator_add(_members_10, _method_8);
                      EList<RelElements> _relelns_2 = ((Relation)meln).getRelelns();
                      for (final RelElements reln_2 : _relelns_2) {
                        boolean _matched_5 = false;
                        if (reln_2 instanceof RelPropertyDef) {
                          _matched_5=true;
                          EList<JvmMember> _members_11 = it.getMembers();
                          String _firstUpper = StringExtensions.toFirstUpper(((RelPropertyDef)reln_2).getName());
                          String _plus_1 = ("set" + _firstUpper);
                          final Procedure1<JvmOperation> _function_13 = (JvmOperation it_1) -> {
                            EList<JvmFormalParameter> _parameters = it_1.getParameters();
                            JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(reln_2, ((RelPropertyDef)reln_2).getName(), ((RelPropertyDef)reln_2).getType());
                            this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                            StringConcatenationClient _client = new StringConcatenationClient() {
                              @Override
                              protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                _builder.append("for( ");
                                JvmTypeReference _typeRef = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef(edgecname);
                                _builder.append(_typeRef);
                                _builder.append(" _edg_ : this )");
                                _builder.newLineIfNotEmpty();
                                _builder.append("_edg_.set");
                                String _firstUpper = StringExtensions.toFirstUpper(((RelPropertyDef)reln_2).getName());
                                _builder.append(_firstUpper);
                                _builder.append("(");
                                String _name = ((RelPropertyDef)reln_2).getName();
                                _builder.append(_name);
                                _builder.append(");");
                                _builder.newLineIfNotEmpty();
                              }
                            };
                            this._jvmTypesBuilder.setBody(it_1, _client);
                          };
                          JvmOperation _method_9 = this._jvmTypesBuilder.toMethod(reln_2, _plus_1, this._typeReferenceBuilder.typeRef(Void.TYPE), _function_13);
                          this._jvmTypesBuilder.<JvmOperation>operator_add(_members_11, _method_9);
                        }
                        if (!_matched_5) {
                          if (reln_2 instanceof InteractionDef) {
                            _matched_5=true;
                            EList<JvmMember> _members_11 = it.getMembers();
                            final Procedure1<JvmOperation> _function_13 = (JvmOperation it_1) -> {
                              EList<JvmFormalParameter> _params = ((InteractionDef)reln_2).getParams();
                              for (final JvmFormalParameter p : _params) {
                                EList<JvmFormalParameter> _parameters = it_1.getParameters();
                                JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(reln_2, p.getName(), p.getParameterType());
                                this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                              }
                              StringConcatenationClient _client = new StringConcatenationClient() {
                                @Override
                                protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                  _builder.append("updateGrid();");
                                  _builder.newLine();
                                  _builder.append("cleanOperator();");
                                  _builder.newLine();
                                  _builder.append(listype);
                                  _builder.append(" cvtList = ((");
                                  JvmTypeReference _typeRef = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef(edgecname);
                                  _builder.append(_typeRef);
                                  _builder.append(")getEdge()).get_agr_");
                                  String _name = ((InteractionDef)reln_2).getName();
                                  _builder.append(_name);
                                  _builder.append("();");
                                  _builder.newLineIfNotEmpty();
                                  _builder.append("if(cvtList != null){");
                                  _builder.newLine();
                                  _builder.append("\t");
                                  _builder.append("for(");
                                  _builder.append(aggregType, "\t");
                                  _builder.append(" cvt : cvtList){");
                                  _builder.newLineIfNotEmpty();
                                  _builder.append("\t\t");
                                  _builder.append("setCellOperator(cvt);");
                                  _builder.newLine();
                                  _builder.append("\t");
                                  _builder.append("}");
                                  _builder.newLine();
                                  _builder.append("}");
                                  _builder.newLine();
                                  _builder.append("initInteraction();");
                                  _builder.newLine();
                                  _builder.append("for(");
                                  JvmTypeReference _typeRef_1 = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef(edgecname);
                                  _builder.append(_typeRef_1);
                                  _builder.append(" _edg_ : this) {");
                                  _builder.newLineIfNotEmpty();
                                  _builder.append("\t");
                                  _builder.append("_edg_.");
                                  String _name_1 = ((InteractionDef)reln_2).getName();
                                  _builder.append(_name_1, "\t");
                                  _builder.append("(");
                                  int ci = 0;
                                  {
                                    EList<JvmFormalParameter> _params = ((InteractionDef)reln_2).getParams();
                                    for(final JvmFormalParameter p : _params) {
                                      {
                                        if ((ci > 0)) {
                                          _builder.append(",");
                                        }
                                      }
                                      String _name_2 = p.getName();
                                      _builder.append(_name_2, "\t");
                                      Object _xblockexpression = null;
                                      {
                                        ci = 1;
                                        _xblockexpression = null;
                                      }
                                      _builder.append(_xblockexpression, "\t");
                                    }
                                  }
                                  _builder.append(");");
                                  _builder.newLineIfNotEmpty();
                                  _builder.append("}");
                                  _builder.newLine();
                                  _builder.append("endInteraction();");
                                  _builder.newLine();
                                }
                              };
                              this._jvmTypesBuilder.setBody(it_1, _client);
                            };
                            JvmOperation _method_9 = this._jvmTypesBuilder.toMethod(reln_2, ((InteractionDef)reln_2).getName(), this._typeReferenceBuilder.typeRef(Void.TYPE), _function_13);
                            this._jvmTypesBuilder.<JvmOperation>operator_add(_members_11, _method_9);
                          }
                        }
                        if (!_matched_5) {
                          if (reln_2 instanceof Filterdef) {
                            _matched_5=true;
                            EList<JvmMember> _members_11 = it.getMembers();
                            final Procedure1<JvmOperation> _function_13 = (JvmOperation it_1) -> {
                              EList<JvmFormalParameter> _params = ((Filterdef)reln_2).getParams();
                              for (final JvmFormalParameter p : _params) {
                                EList<JvmFormalParameter> _parameters = it_1.getParameters();
                                JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(reln_2, p.getName(), p.getParameterType());
                                this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                              }
                              StringConcatenationClient _client = new StringConcatenationClient() {
                                @Override
                                protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                  String _name = ((Relation)meln).getName();
                                  String _plus = (_name + "_");
                                  String _name_1 = ((Filterdef)reln_2).getName();
                                  String _plus_1 = (_plus + _name_1);
                                  _builder.append(_plus_1);
                                  _builder.append(" _filter = new ");
                                  String _name_2 = ((Relation)meln).getName();
                                  String _plus_2 = (_name_2 + "_");
                                  String _name_3 = ((Filterdef)reln_2).getName();
                                  String _plus_3 = (_plus_2 + _name_3);
                                  _builder.append(_plus_3);
                                  _builder.append("(");
                                  _builder.newLineIfNotEmpty();
                                  {
                                    int _size = ((Filterdef)reln_2).getParams().size();
                                    boolean _greaterThan = (_size > 0);
                                    if (_greaterThan) {
                                      {
                                        int _size_1 = ((Filterdef)reln_2).getParams().size();
                                        int _minus = (_size_1 - 1);
                                        IntegerRange _upTo = new IntegerRange(0, _minus);
                                        for(final Integer i : _upTo) {
                                          String _name_4 = ((Filterdef)reln_2).getParams().get((i).intValue()).getName();
                                          _builder.append(_name_4);
                                          _builder.newLineIfNotEmpty();
                                          {
                                            int _size_2 = ((Filterdef)reln_2).getParams().size();
                                            int _minus_1 = (_size_2 - 1);
                                            boolean _lessThan = ((i).intValue() < _minus_1);
                                            if (_lessThan) {
                                              _builder.append(",");
                                              _builder.newLine();
                                            }
                                          }
                                        }
                                      }
                                    }
                                  }
                                  _builder.append(");");
                                  _builder.newLine();
                                  _builder.append("super.addFilter(_filter);");
                                  _builder.newLine();
                                  _builder.append("return this;");
                                  _builder.newLine();
                                }
                              };
                              this._jvmTypesBuilder.setBody(it_1, _client);
                            };
                            JvmOperation _method_9 = this._jvmTypesBuilder.toMethod(reln_2, ((Filterdef)reln_2).getName(), this._typeReferenceBuilder.typeRef(graphcname.toString()), _function_13);
                            this._jvmTypesBuilder.<JvmOperation>operator_add(_members_11, _method_9);
                          }
                        }
                      }
                    } else {
                      final Role firstRole_2 = ((Relation)meln).getRoles().get(0);
                      final JvmTypeReference firstRoleType_1 = this._typeReferenceBuilder.typeRef(this._iQualifiedNameProvider.getFullyQualifiedName(firstRole_2.getType()).toString());
                      final JvmTypeReference firstCellList_1 = this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.ocltypes.List", firstRoleType_1);
                      final String firstName_2 = firstRole_2.getName();
                      String _simpleName = firstRoleType_1.getSimpleName();
                      final String getEntity = ("getAll" + _simpleName);
                      EList<JvmTypeReference> _superTypes_2 = it.getSuperTypes();
                      JvmTypeReference _typeRef_3 = this._typeReferenceBuilder.typeRef(graphTypeName, this._typeReferenceBuilder.typeRef(edgecname), firstRoleType_1);
                      this._jvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes_2, _typeRef_3);
                      EList<JvmMember> _members_11 = it.getMembers();
                      final Procedure1<JvmConstructor> _function_13 = (JvmConstructor it_1) -> {
                        StringConcatenationClient _client = new StringConcatenationClient() {
                          @Override
                          protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                            _builder.append("super();");
                            _builder.newLine();
                          }
                        };
                        this._jvmTypesBuilder.setBody(it_1, _client);
                      };
                      JvmConstructor _constructor_2 = this._jvmTypesBuilder.toConstructor(meln, _function_13);
                      this._jvmTypesBuilder.<JvmConstructor>operator_add(_members_11, _constructor_2);
                      EList<JvmMember> _members_12 = it.getMembers();
                      final Procedure1<JvmOperation> _function_14 = (JvmOperation it_1) -> {
                        StringConcatenationClient _client = new StringConcatenationClient() {
                          @Override
                          protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                            _builder.append(firstRoleType_1);
                            _builder.append(" entity = new ");
                            _builder.append(firstRoleType_1);
                            _builder.append("();");
                            _builder.newLineIfNotEmpty();
                            _builder.newLine();
                            _builder.append("((");
                            JvmTypeReference _typeRef = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.geom.ocltypes.Cell");
                            _builder.append(_typeRef);
                            _builder.append(")entity.getSpatialType()).setGrid(grid);");
                            _builder.newLineIfNotEmpty();
                            _builder.append(firstCellList_1);
                            _builder.append(" array = new ");
                            _builder.append(firstCellList_1);
                            _builder.append("();");
                            _builder.newLineIfNotEmpty();
                            _builder.append("array.cellCut();");
                            _builder.newLine();
                            _builder.append("array.add(entity);");
                            _builder.newLine();
                            _builder.append("return array;");
                            _builder.newLine();
                          }
                        };
                        this._jvmTypesBuilder.setBody(it_1, _client);
                      };
                      JvmOperation _method_9 = this._jvmTypesBuilder.toMethod(meln, getEntity, firstCellList_1, _function_14);
                      this._jvmTypesBuilder.<JvmOperation>operator_add(_members_12, _method_9);
                      EList<JvmMember> _members_13 = it.getMembers();
                      final Procedure1<JvmOperation> _function_15 = (JvmOperation it_1) -> {
                        EList<JvmFormalParameter> _parameters = it_1.getParameters();
                        JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, firstName_2, firstCellList_1);
                        this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                        StringConcatenationClient _client = new StringConcatenationClient() {
                          @Override
                          protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                            _builder.append("super.setGrid(");
                            _builder.append(firstName_2);
                            _builder.append(");");
                            _builder.newLineIfNotEmpty();
                            JvmTypeReference _typeRef = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef(edgecname);
                            _builder.append(_typeRef);
                            _builder.append(" _gen_edge_ = new ");
                            String _name = ((Relation)meln).getName();
                            String _plus = (_name + "_Edge");
                            _builder.append(_plus);
                            _builder.append("(");
                            _builder.append(firstName_2);
                            _builder.append(");");
                            _builder.newLineIfNotEmpty();
                            _builder.append("setCompleteIteratorCell(_gen_edge_ );");
                            _builder.newLine();
                          }
                        };
                        this._jvmTypesBuilder.setBody(it_1, _client);
                      };
                      JvmOperation _method_10 = this._jvmTypesBuilder.toMethod(meln, "connect", this._typeReferenceBuilder.typeRef(Void.TYPE), _function_15);
                      this._jvmTypesBuilder.<JvmOperation>operator_add(_members_13, _method_10);
                      EList<JvmMember> _members_14 = it.getMembers();
                      final Procedure1<JvmOperation> _function_16 = (JvmOperation it_1) -> {
                        EList<JvmFormalParameter> _parameters = it_1.getParameters();
                        JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "shp", this._typeReferenceBuilder.typeRef("fr.ocelet.datafacer.ocltypes.Shapefile"));
                        this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                        EList<JvmFormalParameter> _parameters_1 = it_1.getParameters();
                        JvmFormalParameter _parameter_1 = this._jvmTypesBuilder.toParameter(meln, "size", this._typeReferenceBuilder.typeRef("java.lang.Double"));
                        this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_1, _parameter_1);
                        StringConcatenationClient _client = new StringConcatenationClient() {
                          @Override
                          protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                            _builder.append(firstRoleType_1);
                            _builder.append(" entity = new ");
                            _builder.append(firstRoleType_1);
                            _builder.append("();");
                            _builder.newLineIfNotEmpty();
                            _builder.append("grid = createHexagon(\"");
                            _builder.append(firstRoleType_1);
                            _builder.append("\",entity.getProps(), shp.getBounds(), size);");
                            _builder.newLineIfNotEmpty();
                            _builder.append("((");
                            JvmTypeReference _typeRef = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.geom.ocltypes.Cell");
                            _builder.append(_typeRef);
                            _builder.append(")entity.getSpatialType()).setGrid(grid);");
                            _builder.newLineIfNotEmpty();
                            _builder.append(firstCellList_1);
                            _builder.append(" array = new ");
                            _builder.append(firstCellList_1);
                            _builder.append("();");
                            _builder.newLineIfNotEmpty();
                            _builder.append("array.cellCut();");
                            _builder.newLine();
                            _builder.append("array.add(entity);");
                            _builder.newLine();
                            _builder.append("connect(array);");
                            _builder.newLine();
                          }
                        };
                        this._jvmTypesBuilder.setBody(it_1, _client);
                      };
                      JvmOperation _method_11 = this._jvmTypesBuilder.toMethod(meln, "createHexagons", this._typeReferenceBuilder.typeRef(Void.TYPE), _function_16);
                      this._jvmTypesBuilder.<JvmOperation>operator_add(_members_14, _method_11);
                      EList<JvmMember> _members_15 = it.getMembers();
                      final Procedure1<JvmOperation> _function_17 = (JvmOperation it_1) -> {
                        EList<JvmFormalParameter> _parameters = it_1.getParameters();
                        JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "geometry", this._typeReferenceBuilder.typeRef("com.vividsolutions.jts.geom.Geometry"));
                        this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                        EList<JvmFormalParameter> _parameters_1 = it_1.getParameters();
                        JvmFormalParameter _parameter_1 = this._jvmTypesBuilder.toParameter(meln, "size", this._typeReferenceBuilder.typeRef("java.lang.Double"));
                        this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_1, _parameter_1);
                        StringConcatenationClient _client = new StringConcatenationClient() {
                          @Override
                          protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                            _builder.append(firstRoleType_1);
                            _builder.append(" entity = new ");
                            _builder.append(firstRoleType_1);
                            _builder.append("();");
                            _builder.newLineIfNotEmpty();
                            _builder.append("grid = createHexagon(\"");
                            _builder.append(firstRoleType_1);
                            _builder.append("\",entity.getProps(), geometry, size);");
                            _builder.newLineIfNotEmpty();
                            _builder.append("((");
                            JvmTypeReference _typeRef = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.geom.ocltypes.Cell");
                            _builder.append(_typeRef);
                            _builder.append(")entity.getSpatialType()).setGrid(grid);");
                            _builder.newLineIfNotEmpty();
                            _builder.append(firstCellList_1);
                            _builder.append(" array = new ");
                            _builder.append(firstCellList_1);
                            _builder.append("();");
                            _builder.newLineIfNotEmpty();
                            _builder.append("array.cellCut();");
                            _builder.newLine();
                            _builder.append("array.add(entity);");
                            _builder.newLine();
                            _builder.append("connect(array);");
                            _builder.newLine();
                          }
                        };
                        this._jvmTypesBuilder.setBody(it_1, _client);
                      };
                      JvmOperation _method_12 = this._jvmTypesBuilder.toMethod(meln, "createHexagons", this._typeReferenceBuilder.typeRef(Void.TYPE), _function_17);
                      this._jvmTypesBuilder.<JvmOperation>operator_add(_members_15, _method_12);
                      EList<JvmMember> _members_16 = it.getMembers();
                      final Procedure1<JvmOperation> _function_18 = (JvmOperation it_1) -> {
                        EList<JvmFormalParameter> _parameters = it_1.getParameters();
                        JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "size", this._typeReferenceBuilder.typeRef("java.lang.Double"));
                        this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                        EList<JvmFormalParameter> _parameters_1 = it_1.getParameters();
                        JvmFormalParameter _parameter_1 = this._jvmTypesBuilder.toParameter(meln, "minX", this._typeReferenceBuilder.typeRef("java.lang.Double"));
                        this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_1, _parameter_1);
                        EList<JvmFormalParameter> _parameters_2 = it_1.getParameters();
                        JvmFormalParameter _parameter_2 = this._jvmTypesBuilder.toParameter(meln, "minY", this._typeReferenceBuilder.typeRef("java.lang.Double"));
                        this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_2, _parameter_2);
                        EList<JvmFormalParameter> _parameters_3 = it_1.getParameters();
                        JvmFormalParameter _parameter_3 = this._jvmTypesBuilder.toParameter(meln, "maxX", this._typeReferenceBuilder.typeRef("java.lang.Double"));
                        this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_3, _parameter_3);
                        EList<JvmFormalParameter> _parameters_4 = it_1.getParameters();
                        JvmFormalParameter _parameter_4 = this._jvmTypesBuilder.toParameter(meln, "maxY", this._typeReferenceBuilder.typeRef("java.lang.Double"));
                        this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_4, _parameter_4);
                        StringConcatenationClient _client = new StringConcatenationClient() {
                          @Override
                          protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                            _builder.append(firstRoleType_1);
                            _builder.append(" entity = new ");
                            _builder.append(firstRoleType_1);
                            _builder.append("();");
                            _builder.newLineIfNotEmpty();
                            _builder.append("grid =  createHexagon(\"");
                            _builder.append(firstRoleType_1);
                            _builder.append("\",entity.getProps(), minX, minY, maxX, maxY, size);");
                            _builder.newLineIfNotEmpty();
                            _builder.append("((");
                            JvmTypeReference _typeRef = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.geom.ocltypes.Cell");
                            _builder.append(_typeRef);
                            _builder.append(")entity.getSpatialType()).setGrid(grid);");
                            _builder.newLineIfNotEmpty();
                            _builder.append(firstCellList_1);
                            _builder.append(" array = new ");
                            _builder.append(firstCellList_1);
                            _builder.append("();");
                            _builder.newLineIfNotEmpty();
                            _builder.append("array.cellCut();");
                            _builder.newLine();
                            _builder.append("array.add(entity);");
                            _builder.newLine();
                            _builder.append("connect(array);");
                            _builder.newLine();
                          }
                        };
                        this._jvmTypesBuilder.setBody(it_1, _client);
                      };
                      JvmOperation _method_13 = this._jvmTypesBuilder.toMethod(meln, "createHexagons", this._typeReferenceBuilder.typeRef(Void.TYPE), _function_18);
                      this._jvmTypesBuilder.<JvmOperation>operator_add(_members_16, _method_13);
                      EList<JvmMember> _members_17 = it.getMembers();
                      final Procedure1<JvmOperation> _function_19 = (JvmOperation it_1) -> {
                        EList<JvmFormalParameter> _parameters = it_1.getParameters();
                        JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "shp", this._typeReferenceBuilder.typeRef("fr.ocelet.datafacer.ocltypes.Shapefile"));
                        this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                        EList<JvmFormalParameter> _parameters_1 = it_1.getParameters();
                        JvmFormalParameter _parameter_1 = this._jvmTypesBuilder.toParameter(meln, "xRes", this._typeReferenceBuilder.typeRef("java.lang.Double"));
                        this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_1, _parameter_1);
                        EList<JvmFormalParameter> _parameters_2 = it_1.getParameters();
                        JvmFormalParameter _parameter_2 = this._jvmTypesBuilder.toParameter(meln, "yRes", this._typeReferenceBuilder.typeRef("java.lang.Double"));
                        this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_2, _parameter_2);
                        StringConcatenationClient _client = new StringConcatenationClient() {
                          @Override
                          protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                            _builder.append(firstRoleType_1);
                            _builder.append(" entity = new ");
                            _builder.append(firstRoleType_1);
                            _builder.append("();");
                            _builder.newLineIfNotEmpty();
                            _builder.append("grid = createSquare(\"");
                            _builder.append(firstRoleType_1);
                            _builder.append("\",entity.getProps(), shp.getBounds(), xRes, yRes);");
                            _builder.newLineIfNotEmpty();
                            _builder.append("((");
                            JvmTypeReference _typeRef = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.geom.ocltypes.Cell");
                            _builder.append(_typeRef);
                            _builder.append(")entity.getSpatialType()).setGrid(grid);");
                            _builder.newLineIfNotEmpty();
                            _builder.append(firstCellList_1);
                            _builder.append(" array = new ");
                            _builder.append(firstCellList_1);
                            _builder.append("();");
                            _builder.newLineIfNotEmpty();
                            _builder.append("array.cellCut();");
                            _builder.newLine();
                            _builder.append("array.add(entity);");
                            _builder.newLine();
                            _builder.append("connect(array);");
                            _builder.newLine();
                          }
                        };
                        this._jvmTypesBuilder.setBody(it_1, _client);
                      };
                      JvmOperation _method_14 = this._jvmTypesBuilder.toMethod(meln, "createSquares", this._typeReferenceBuilder.typeRef(Void.TYPE), _function_19);
                      this._jvmTypesBuilder.<JvmOperation>operator_add(_members_17, _method_14);
                      EList<JvmMember> _members_18 = it.getMembers();
                      final Procedure1<JvmOperation> _function_20 = (JvmOperation it_1) -> {
                        EList<JvmFormalParameter> _parameters = it_1.getParameters();
                        JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "geometry", this._typeReferenceBuilder.typeRef("com.vividsolutions.jts.geom.Geometry"));
                        this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                        EList<JvmFormalParameter> _parameters_1 = it_1.getParameters();
                        JvmFormalParameter _parameter_1 = this._jvmTypesBuilder.toParameter(meln, "xRes", this._typeReferenceBuilder.typeRef("java.lang.Double"));
                        this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_1, _parameter_1);
                        EList<JvmFormalParameter> _parameters_2 = it_1.getParameters();
                        JvmFormalParameter _parameter_2 = this._jvmTypesBuilder.toParameter(meln, "yRes", this._typeReferenceBuilder.typeRef("java.lang.Double"));
                        this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_2, _parameter_2);
                        StringConcatenationClient _client = new StringConcatenationClient() {
                          @Override
                          protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                            _builder.append(firstRoleType_1);
                            _builder.append(" entity = new ");
                            _builder.append(firstRoleType_1);
                            _builder.append("();");
                            _builder.newLineIfNotEmpty();
                            _builder.append("grid = createSquare(\"");
                            _builder.append(firstRoleType_1);
                            _builder.append("\",entity.getProps(), geometry, xRes, yRes);");
                            _builder.newLineIfNotEmpty();
                            _builder.append("((");
                            JvmTypeReference _typeRef = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.geom.ocltypes.Cell");
                            _builder.append(_typeRef);
                            _builder.append(")entity.getSpatialType()).setGrid(grid);");
                            _builder.newLineIfNotEmpty();
                            _builder.append(firstCellList_1);
                            _builder.append(" array = new ");
                            _builder.append(firstCellList_1);
                            _builder.append("();");
                            _builder.newLineIfNotEmpty();
                            _builder.append("array.cellCut();");
                            _builder.newLine();
                            _builder.append("array.add(entity);");
                            _builder.newLine();
                            _builder.append("connect(array);");
                            _builder.newLine();
                          }
                        };
                        this._jvmTypesBuilder.setBody(it_1, _client);
                      };
                      JvmOperation _method_15 = this._jvmTypesBuilder.toMethod(meln, "createSquares", this._typeReferenceBuilder.typeRef(Void.TYPE), _function_20);
                      this._jvmTypesBuilder.<JvmOperation>operator_add(_members_18, _method_15);
                      EList<JvmMember> _members_19 = it.getMembers();
                      final Procedure1<JvmOperation> _function_21 = (JvmOperation it_1) -> {
                        EList<JvmFormalParameter> _parameters = it_1.getParameters();
                        JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "xRes", this._typeReferenceBuilder.typeRef("java.lang.Double"));
                        this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                        EList<JvmFormalParameter> _parameters_1 = it_1.getParameters();
                        JvmFormalParameter _parameter_1 = this._jvmTypesBuilder.toParameter(meln, "yRes", this._typeReferenceBuilder.typeRef("java.lang.Double"));
                        this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_1, _parameter_1);
                        EList<JvmFormalParameter> _parameters_2 = it_1.getParameters();
                        JvmFormalParameter _parameter_2 = this._jvmTypesBuilder.toParameter(meln, "minX", this._typeReferenceBuilder.typeRef("java.lang.Double"));
                        this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_2, _parameter_2);
                        EList<JvmFormalParameter> _parameters_3 = it_1.getParameters();
                        JvmFormalParameter _parameter_3 = this._jvmTypesBuilder.toParameter(meln, "minY", this._typeReferenceBuilder.typeRef("java.lang.Double"));
                        this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_3, _parameter_3);
                        EList<JvmFormalParameter> _parameters_4 = it_1.getParameters();
                        JvmFormalParameter _parameter_4 = this._jvmTypesBuilder.toParameter(meln, "maxX", this._typeReferenceBuilder.typeRef("java.lang.Double"));
                        this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_4, _parameter_4);
                        EList<JvmFormalParameter> _parameters_5 = it_1.getParameters();
                        JvmFormalParameter _parameter_5 = this._jvmTypesBuilder.toParameter(meln, "maxY", this._typeReferenceBuilder.typeRef("java.lang.Double"));
                        this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_5, _parameter_5);
                        StringConcatenationClient _client = new StringConcatenationClient() {
                          @Override
                          protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                            _builder.append(firstRoleType_1);
                            _builder.append(" entity = new ");
                            _builder.append(firstRoleType_1);
                            _builder.append("();");
                            _builder.newLineIfNotEmpty();
                            _builder.append("grid = createSquare(\"");
                            _builder.append(firstRoleType_1);
                            _builder.append("\",entity.getProps(), minX, minY, maxX, maxY, xRes, yRes);");
                            _builder.newLineIfNotEmpty();
                            _builder.append("((");
                            JvmTypeReference _typeRef = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.geom.ocltypes.Cell");
                            _builder.append(_typeRef);
                            _builder.append(")entity.getSpatialType()).setGrid(grid);");
                            _builder.newLineIfNotEmpty();
                            _builder.append(firstCellList_1);
                            _builder.append(" array = new ");
                            _builder.append(firstCellList_1);
                            _builder.append("();");
                            _builder.newLineIfNotEmpty();
                            _builder.append("array.cellCut();");
                            _builder.newLine();
                            _builder.append("array.add(entity);");
                            _builder.newLine();
                            _builder.append("connect(array);");
                            _builder.newLine();
                          }
                        };
                        this._jvmTypesBuilder.setBody(it_1, _client);
                      };
                      JvmOperation _method_16 = this._jvmTypesBuilder.toMethod(meln, "createSquares", this._typeReferenceBuilder.typeRef(Void.TYPE), _function_21);
                      this._jvmTypesBuilder.<JvmOperation>operator_add(_members_19, _method_16);
                      EList<JvmMember> _members_20 = it.getMembers();
                      final Procedure1<JvmOperation> _function_22 = (JvmOperation it_1) -> {
                        EList<JvmFormalParameter> _parameters = it_1.getParameters();
                        JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "geometry", this._typeReferenceBuilder.typeRef("com.vividsolutions.jts.geom.Geometry"));
                        this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                        EList<JvmFormalParameter> _parameters_1 = it_1.getParameters();
                        JvmFormalParameter _parameter_1 = this._jvmTypesBuilder.toParameter(meln, "size", this._typeReferenceBuilder.typeRef("java.lang.Double"));
                        this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_1, _parameter_1);
                        StringConcatenationClient _client = new StringConcatenationClient() {
                          @Override
                          protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                            _builder.append(firstRoleType_1);
                            _builder.append(" entity = new ");
                            _builder.append(firstRoleType_1);
                            _builder.append("();");
                            _builder.newLineIfNotEmpty();
                            _builder.append("grid = createTriangle(\"");
                            _builder.append(firstRoleType_1);
                            _builder.append("\",entity.getProps(), geometry, size);");
                            _builder.newLineIfNotEmpty();
                            _builder.append("((");
                            JvmTypeReference _typeRef = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.geom.ocltypes.Cell");
                            _builder.append(_typeRef);
                            _builder.append(")entity.getSpatialType()).setGrid(grid);");
                            _builder.newLineIfNotEmpty();
                            _builder.append(firstCellList_1);
                            _builder.append(" array = new ");
                            _builder.append(firstCellList_1);
                            _builder.append("();");
                            _builder.newLineIfNotEmpty();
                            _builder.append("array.cellCut();");
                            _builder.newLine();
                            _builder.append("array.add(entity);");
                            _builder.newLine();
                            _builder.append("connect(array);");
                            _builder.newLine();
                            _builder.newLine();
                          }
                        };
                        this._jvmTypesBuilder.setBody(it_1, _client);
                      };
                      JvmOperation _method_17 = this._jvmTypesBuilder.toMethod(meln, "createTriangles", this._typeReferenceBuilder.typeRef(Void.TYPE), _function_22);
                      this._jvmTypesBuilder.<JvmOperation>operator_add(_members_20, _method_17);
                      EList<JvmMember> _members_21 = it.getMembers();
                      final Procedure1<JvmOperation> _function_23 = (JvmOperation it_1) -> {
                        EList<JvmFormalParameter> _parameters = it_1.getParameters();
                        JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "shp", this._typeReferenceBuilder.typeRef("fr.ocelet.datafacer.ocltypes.Shapefile"));
                        this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                        EList<JvmFormalParameter> _parameters_1 = it_1.getParameters();
                        JvmFormalParameter _parameter_1 = this._jvmTypesBuilder.toParameter(meln, "size", this._typeReferenceBuilder.typeRef("java.lang.Double"));
                        this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_1, _parameter_1);
                        StringConcatenationClient _client = new StringConcatenationClient() {
                          @Override
                          protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                            _builder.append(firstRoleType_1);
                            _builder.append(" entity = new ");
                            _builder.append(firstRoleType_1);
                            _builder.append("();");
                            _builder.newLineIfNotEmpty();
                            _builder.append("grid = createTriangle(\"");
                            _builder.append(firstRoleType_1);
                            _builder.append("\",entity.getProps(), shp.getBounds(), size);");
                            _builder.newLineIfNotEmpty();
                            _builder.append("((");
                            JvmTypeReference _typeRef = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.geom.ocltypes.Cell");
                            _builder.append(_typeRef);
                            _builder.append(")entity.getSpatialType()).setGrid(grid);");
                            _builder.newLineIfNotEmpty();
                            _builder.append(firstCellList_1);
                            _builder.append(" array = new ");
                            _builder.append(firstCellList_1);
                            _builder.append("();");
                            _builder.newLineIfNotEmpty();
                            _builder.append("array.cellCut();");
                            _builder.newLine();
                            _builder.append("array.add(entity);");
                            _builder.newLine();
                            _builder.append("connect(array);");
                            _builder.newLine();
                            _builder.newLine();
                          }
                        };
                        this._jvmTypesBuilder.setBody(it_1, _client);
                      };
                      JvmOperation _method_18 = this._jvmTypesBuilder.toMethod(meln, "createTriangles", this._typeReferenceBuilder.typeRef(Void.TYPE), _function_23);
                      this._jvmTypesBuilder.<JvmOperation>operator_add(_members_21, _method_18);
                      EList<JvmMember> _members_22 = it.getMembers();
                      final Procedure1<JvmOperation> _function_24 = (JvmOperation it_1) -> {
                        EList<JvmFormalParameter> _parameters = it_1.getParameters();
                        JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "size", this._typeReferenceBuilder.typeRef("java.lang.Double"));
                        this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                        EList<JvmFormalParameter> _parameters_1 = it_1.getParameters();
                        JvmFormalParameter _parameter_1 = this._jvmTypesBuilder.toParameter(meln, "minX", this._typeReferenceBuilder.typeRef("java.lang.Double"));
                        this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_1, _parameter_1);
                        EList<JvmFormalParameter> _parameters_2 = it_1.getParameters();
                        JvmFormalParameter _parameter_2 = this._jvmTypesBuilder.toParameter(meln, "minY", this._typeReferenceBuilder.typeRef("java.lang.Double"));
                        this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_2, _parameter_2);
                        EList<JvmFormalParameter> _parameters_3 = it_1.getParameters();
                        JvmFormalParameter _parameter_3 = this._jvmTypesBuilder.toParameter(meln, "maxX", this._typeReferenceBuilder.typeRef("java.lang.Double"));
                        this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_3, _parameter_3);
                        EList<JvmFormalParameter> _parameters_4 = it_1.getParameters();
                        JvmFormalParameter _parameter_4 = this._jvmTypesBuilder.toParameter(meln, "maxY", this._typeReferenceBuilder.typeRef("java.lang.Double"));
                        this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_4, _parameter_4);
                        StringConcatenationClient _client = new StringConcatenationClient() {
                          @Override
                          protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                            _builder.append(firstRoleType_1);
                            _builder.append(" entity = new ");
                            _builder.append(firstRoleType_1);
                            _builder.append("();");
                            _builder.newLineIfNotEmpty();
                            _builder.append("grid = createTriangle(\"");
                            _builder.append(firstRoleType_1);
                            _builder.append("\",entity.getProps(), minX, minY, maxX, maxY, size);");
                            _builder.newLineIfNotEmpty();
                            _builder.append("((");
                            JvmTypeReference _typeRef = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.geom.ocltypes.Cell");
                            _builder.append(_typeRef);
                            _builder.append(")entity.getSpatialType()).setGrid(grid);\t\t\t\t\t\t");
                            _builder.newLineIfNotEmpty();
                            _builder.append(firstCellList_1);
                            _builder.append(" array = new ");
                            _builder.append(firstCellList_1);
                            _builder.append("();");
                            _builder.newLineIfNotEmpty();
                            _builder.append("array.cellCut();");
                            _builder.newLine();
                            _builder.append("array.add(entity);");
                            _builder.newLine();
                            _builder.append("connect(array);");
                            _builder.newLine();
                            _builder.newLine();
                          }
                        };
                        this._jvmTypesBuilder.setBody(it_1, _client);
                      };
                      JvmOperation _method_19 = this._jvmTypesBuilder.toMethod(meln, "createTriangles", this._typeReferenceBuilder.typeRef(Void.TYPE), _function_24);
                      this._jvmTypesBuilder.<JvmOperation>operator_add(_members_22, _method_19);
                      EList<RelElements> _relelns_3 = ((Relation)meln).getRelelns();
                      for (final RelElements reln_3 : _relelns_3) {
                        boolean _matched_6 = false;
                        if (reln_3 instanceof RelPropertyDef) {
                          _matched_6=true;
                          EList<JvmMember> _members_23 = it.getMembers();
                          String _firstUpper = StringExtensions.toFirstUpper(((RelPropertyDef)reln_3).getName());
                          String _plus_1 = ("set" + _firstUpper);
                          final Procedure1<JvmOperation> _function_25 = (JvmOperation it_1) -> {
                            EList<JvmFormalParameter> _parameters = it_1.getParameters();
                            JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(reln_3, ((RelPropertyDef)reln_3).getName(), ((RelPropertyDef)reln_3).getType());
                            this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                            StringConcatenationClient _client = new StringConcatenationClient() {
                              @Override
                              protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                _builder.newLine();
                                _builder.append("for(");
                                JvmTypeReference _typeRef = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef(edgecname);
                                _builder.append(_typeRef);
                                _builder.append(" _edg_ : this)");
                                _builder.newLineIfNotEmpty();
                                _builder.append("_edg_.set");
                                String _firstUpper = StringExtensions.toFirstUpper(((RelPropertyDef)reln_3).getName());
                                _builder.append(_firstUpper);
                                _builder.append("(");
                                String _name = ((RelPropertyDef)reln_3).getName();
                                _builder.append(_name);
                                _builder.append(");");
                                _builder.newLineIfNotEmpty();
                              }
                            };
                            this._jvmTypesBuilder.setBody(it_1, _client);
                          };
                          JvmOperation _method_20 = this._jvmTypesBuilder.toMethod(reln_3, _plus_1, this._typeReferenceBuilder.typeRef(Void.TYPE), _function_25);
                          this._jvmTypesBuilder.<JvmOperation>operator_add(_members_23, _method_20);
                        }
                        if (!_matched_6) {
                          if (reln_3 instanceof InteractionDef) {
                            _matched_6=true;
                            EList<JvmMember> _members_23 = it.getMembers();
                            final Procedure1<JvmOperation> _function_25 = (JvmOperation it_1) -> {
                              EList<JvmFormalParameter> _params = ((InteractionDef)reln_3).getParams();
                              for (final JvmFormalParameter p : _params) {
                                EList<JvmFormalParameter> _parameters = it_1.getParameters();
                                JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(reln_3, p.getName(), p.getParameterType());
                                this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                              }
                              StringConcatenationClient _client = new StringConcatenationClient() {
                                @Override
                                protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                  _builder.append("setMode(0);");
                                  _builder.newLine();
                                  _builder.append("cleanOperator();");
                                  _builder.newLine();
                                  _builder.append(listype);
                                  _builder.append(" cvtList = ((");
                                  JvmTypeReference _typeRef = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef(edgecname);
                                  _builder.append(_typeRef);
                                  _builder.append(")getEdge()).get_agr_");
                                  String _name = ((InteractionDef)reln_3).getName();
                                  _builder.append(_name);
                                  _builder.append("();");
                                  _builder.newLineIfNotEmpty();
                                  _builder.append("if(cvtList != null){");
                                  _builder.newLine();
                                  _builder.append("\t");
                                  _builder.append("for(");
                                  _builder.append(aggregType, "\t");
                                  _builder.append(" cvt : cvtList){");
                                  _builder.newLineIfNotEmpty();
                                  _builder.append("\t\t");
                                  _builder.append("setCellOperator(cvt);");
                                  _builder.newLine();
                                  _builder.append("\t");
                                  _builder.append("} ");
                                  _builder.newLine();
                                  _builder.append("}");
                                  _builder.newLine();
                                  _builder.append("initInteraction();");
                                  _builder.newLine();
                                  _builder.append("for(");
                                  JvmTypeReference _typeRef_1 = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef(edgecname);
                                  _builder.append(_typeRef_1);
                                  _builder.append(" _edg_ : this) {");
                                  _builder.newLineIfNotEmpty();
                                  _builder.append("\t");
                                  _builder.append("_edg_.");
                                  String _name_1 = ((InteractionDef)reln_3).getName();
                                  _builder.append(_name_1, "\t");
                                  _builder.append("(");
                                  int ci = 0;
                                  {
                                    EList<JvmFormalParameter> _params = ((InteractionDef)reln_3).getParams();
                                    for(final JvmFormalParameter p : _params) {
                                      _builder.append(" ");
                                      {
                                        if ((ci > 0)) {
                                          _builder.append(",");
                                        }
                                      }
                                      String _name_2 = p.getName();
                                      _builder.append(_name_2, "\t");
                                      Object _xblockexpression = null;
                                      {
                                        ci = 1;
                                        _xblockexpression = null;
                                      }
                                      _builder.append(_xblockexpression, "\t");
                                    }
                                  }
                                  _builder.append(");");
                                  _builder.newLineIfNotEmpty();
                                  _builder.append("}");
                                  _builder.newLine();
                                  _builder.append("endInteraction();");
                                  _builder.newLine();
                                }
                              };
                              this._jvmTypesBuilder.setBody(it_1, _client);
                            };
                            JvmOperation _method_20 = this._jvmTypesBuilder.toMethod(reln_3, ((InteractionDef)reln_3).getName(), this._typeReferenceBuilder.typeRef(Void.TYPE), _function_25);
                            this._jvmTypesBuilder.<JvmOperation>operator_add(_members_23, _method_20);
                          }
                        }
                        if (!_matched_6) {
                          if (reln_3 instanceof Filterdef) {
                            _matched_6=true;
                            EList<JvmMember> _members_23 = it.getMembers();
                            final Procedure1<JvmOperation> _function_25 = (JvmOperation it_1) -> {
                              EList<JvmFormalParameter> _params = ((Filterdef)reln_3).getParams();
                              for (final JvmFormalParameter p : _params) {
                                EList<JvmFormalParameter> _parameters = it_1.getParameters();
                                JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(reln_3, p.getName(), p.getParameterType());
                                this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                              }
                              StringConcatenationClient _client = new StringConcatenationClient() {
                                @Override
                                protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                  String _name = ((Relation)meln).getName();
                                  String _plus = (_name + "_");
                                  String _name_1 = ((Filterdef)reln_3).getName();
                                  String _plus_1 = (_plus + _name_1);
                                  _builder.append(_plus_1);
                                  _builder.append(" _filter = new ");
                                  String _name_2 = ((Relation)meln).getName();
                                  String _plus_2 = (_name_2 + "_");
                                  String _name_3 = ((Filterdef)reln_3).getName();
                                  String _plus_3 = (_plus_2 + _name_3);
                                  _builder.append(_plus_3);
                                  _builder.append("(");
                                  _builder.newLineIfNotEmpty();
                                  {
                                    int _size = ((Filterdef)reln_3).getParams().size();
                                    boolean _greaterThan = (_size > 0);
                                    if (_greaterThan) {
                                      {
                                        int _size_1 = ((Filterdef)reln_3).getParams().size();
                                        int _minus = (_size_1 - 1);
                                        IntegerRange _upTo = new IntegerRange(0, _minus);
                                        for(final Integer i : _upTo) {
                                          String _name_4 = ((Filterdef)reln_3).getParams().get((i).intValue()).getName();
                                          _builder.append(_name_4);
                                          _builder.newLineIfNotEmpty();
                                          {
                                            int _size_2 = ((Filterdef)reln_3).getParams().size();
                                            int _minus_1 = (_size_2 - 1);
                                            boolean _lessThan = ((i).intValue() < _minus_1);
                                            if (_lessThan) {
                                              _builder.append(",");
                                              _builder.newLine();
                                            }
                                          }
                                        }
                                      }
                                    }
                                  }
                                  _builder.append(");");
                                  _builder.newLine();
                                  _builder.append("super.addFilter(_filter);");
                                  _builder.newLine();
                                  _builder.append("return this;");
                                  _builder.newLine();
                                }
                              };
                              this._jvmTypesBuilder.setBody(it_1, _client);
                            };
                            JvmOperation _method_20 = this._jvmTypesBuilder.toMethod(reln_3, ((Filterdef)reln_3).getName(), this._typeReferenceBuilder.typeRef(graphcname.toString()), _function_25);
                            this._jvmTypesBuilder.<JvmOperation>operator_add(_members_23, _method_20);
                          }
                        }
                      }
                    }
                  } else {
                    if ((((((((((((Relation)meln).getRoles().size() >= 2) && (((Relation)meln).getRoles().get(0) != null)) && (((Relation)meln).getRoles().get(1) != null)) && (((Relation)meln).getRoles().get(0).getType() != null)) && (((Relation)meln).getRoles().get(1).getType() != null)) && (this._iQualifiedNameProvider.getFullyQualifiedName(((Relation)meln).getRoles().get(0).getType()) != null)) && (this._iQualifiedNameProvider.getFullyQualifiedName(((Relation)meln).getRoles().get(1).getType()) != null)) && (((Relation)meln).getRoles().get(0).getName() != null)) && (((Relation)meln).getRoles().get(1).getName() != null))) {
                      final Role firstRole_3 = ((Relation)meln).getRoles().get(0);
                      final Role secondRole_2 = ((Relation)meln).getRoles().get(1);
                      final JvmTypeReference firstRoleType_2 = this._typeReferenceBuilder.typeRef(this._iQualifiedNameProvider.getFullyQualifiedName(firstRole_3.getType()).toString());
                      final JvmTypeReference secondRoleType_1 = this._typeReferenceBuilder.typeRef(this._iQualifiedNameProvider.getFullyQualifiedName(secondRole_2.getType()).toString());
                      String _name_4 = ((Relation)meln).getRoles().get(0).getName();
                      final String rolset1 = (_name_4 + "Set");
                      String _name_5 = ((Relation)meln).getRoles().get(1).getName();
                      final String rolset2 = (_name_5 + "Set");
                      if (isAutoGraph) {
                        EList<JvmTypeReference> _superTypes_3 = it.getSuperTypes();
                        JvmTypeReference _typeRef_4 = this._typeReferenceBuilder.typeRef(graphTypeName, this._typeReferenceBuilder.typeRef(edgecname), firstRoleType_2);
                        this._jvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes_3, _typeRef_4);
                      } else {
                        EList<JvmTypeReference> _superTypes_4 = it.getSuperTypes();
                        JvmTypeReference _typeRef_5 = this._typeReferenceBuilder.typeRef(graphTypeName, this._typeReferenceBuilder.typeRef(edgecname), firstRoleType_2, secondRoleType_1);
                        this._jvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes_4, _typeRef_5);
                      }
                      EList<JvmMember> _members_23 = it.getMembers();
                      final Procedure1<JvmConstructor> _function_25 = (JvmConstructor it_1) -> {
                        StringConcatenationClient _client = new StringConcatenationClient() {
                          @Override
                          protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                            _builder.append("super();");
                          }
                        };
                        this._jvmTypesBuilder.setBody(it_1, _client);
                      };
                      JvmConstructor _constructor_3 = this._jvmTypesBuilder.toConstructor(meln, _function_25);
                      this._jvmTypesBuilder.<JvmConstructor>operator_add(_members_23, _constructor_3);
                      EList<JvmMember> _members_24 = it.getMembers();
                      final Procedure1<JvmOperation> _function_26 = (JvmOperation it_1) -> {
                        EList<JvmFormalParameter> _parameters = it_1.getParameters();
                        JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, firstRole_3.getName(), firstRoleType_2);
                        this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                        EList<JvmFormalParameter> _parameters_1 = it_1.getParameters();
                        JvmFormalParameter _parameter_1 = this._jvmTypesBuilder.toParameter(meln, secondRole_2.getName(), secondRoleType_1);
                        this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_1, _parameter_1);
                        StringConcatenationClient _client = new StringConcatenationClient() {
                          @Override
                          protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                            _builder.append("if ((this.");
                            _builder.append(rolset1);
                            _builder.append(" == null) || (!this.");
                            _builder.append(rolset1);
                            _builder.append(".contains(");
                            String _name = firstRole_3.getName();
                            _builder.append(_name);
                            _builder.append("))) add(");
                            String _name_1 = firstRole_3.getName();
                            _builder.append(_name_1);
                            _builder.append(");");
                            _builder.newLineIfNotEmpty();
                            {
                              if ((!isAutoGraph)) {
                                _builder.append("if ((this.");
                                _builder.append(rolset2);
                                _builder.append(" == null) || (!this.");
                                _builder.append(rolset2);
                                _builder.append(".contains(");
                                String _name_2 = secondRole_2.getName();
                                _builder.append(_name_2);
                                _builder.append("))) add(");
                                String _name_3 = secondRole_2.getName();
                                _builder.append(_name_3);
                                _builder.append(");");
                                _builder.newLineIfNotEmpty();
                              } else {
                                _builder.append("if ((this.");
                                _builder.append(rolset1);
                                _builder.append(" == null) || (!this.");
                                _builder.append(rolset1);
                                _builder.append(".contains(");
                                String _name_4 = secondRole_2.getName();
                                _builder.append(_name_4);
                                _builder.append("))) add(");
                                String _name_5 = secondRole_2.getName();
                                _builder.append(_name_5);
                                _builder.append(");");
                                _builder.newLineIfNotEmpty();
                              }
                            }
                            final JvmTypeReference typ_edgecname = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef(edgecname);
                            _builder.newLineIfNotEmpty();
                            _builder.append(typ_edgecname);
                            _builder.append(" _gen_edge_ = new ");
                            String _name_6 = ((Relation)meln).getName();
                            String _plus = (_name_6 + "_Edge");
                            _builder.append(_plus);
                            _builder.append("(this,");
                            String _name_7 = firstRole_3.getName();
                            _builder.append(_name_7);
                            _builder.append(",");
                            String _name_8 = secondRole_2.getName();
                            _builder.append(_name_8);
                            _builder.append(");");
                            _builder.newLineIfNotEmpty();
                            _builder.append("addEdge(_gen_edge_);");
                            _builder.newLine();
                            _builder.append("return _gen_edge_;");
                            _builder.newLine();
                          }
                        };
                        this._jvmTypesBuilder.setBody(it_1, _client);
                      };
                      JvmOperation _method_20 = this._jvmTypesBuilder.toMethod(meln, "connect", this._typeReferenceBuilder.typeRef(edgecname), _function_26);
                      this._jvmTypesBuilder.<JvmOperation>operator_add(_members_24, _method_20);
                      EList<JvmMember> _members_25 = it.getMembers();
                      final Procedure1<JvmOperation> _function_27 = (JvmOperation it_1) -> {
                        StringConcatenationClient _client = new StringConcatenationClient() {
                          @Override
                          protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                            _builder.append("return ");
                            _builder.append(rolset1);
                            _builder.append(";");
                          }
                        };
                        this._jvmTypesBuilder.setBody(it_1, _client);
                      };
                      JvmOperation _method_21 = this._jvmTypesBuilder.toMethod(meln, "getLeftSet", this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.relation.RoleSet", firstRoleType_2), _function_27);
                      this._jvmTypesBuilder.<JvmOperation>operator_add(_members_25, _method_21);
                      EList<JvmMember> _members_26 = it.getMembers();
                      final Procedure1<JvmOperation> _function_28 = (JvmOperation it_1) -> {
                        StringConcatenationClient _client = new StringConcatenationClient() {
                          @Override
                          protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                            {
                              if (isAutoGraph) {
                                _builder.append("return ");
                                _builder.append(rolset1);
                                _builder.append(";");
                                _builder.newLineIfNotEmpty();
                              } else {
                                _builder.append("return ");
                                _builder.append(rolset2);
                                _builder.append(";");
                                _builder.newLineIfNotEmpty();
                              }
                            }
                          }
                        };
                        this._jvmTypesBuilder.setBody(it_1, _client);
                      };
                      JvmOperation _method_22 = this._jvmTypesBuilder.toMethod(meln, "getRightSet", this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.relation.RoleSet", secondRoleType_1), _function_28);
                      this._jvmTypesBuilder.<JvmOperation>operator_add(_members_26, _method_22);
                      EList<JvmMember> _members_27 = it.getMembers();
                      final Procedure1<JvmOperation> _function_29 = (JvmOperation it_1) -> {
                        StringConcatenationClient _client = new StringConcatenationClient() {
                          @Override
                          protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                            _builder.append("return (");
                            String _name = ((Relation)meln).getName();
                            _builder.append(_name);
                            _builder.append(")super.getComplete();");
                          }
                        };
                        this._jvmTypesBuilder.setBody(it_1, _client);
                      };
                      JvmOperation _method_23 = this._jvmTypesBuilder.toMethod(meln, "getComplete", this._typeReferenceBuilder.typeRef(graphcname.toString()), _function_29);
                      this._jvmTypesBuilder.<JvmOperation>operator_add(_members_27, _method_23);
                      EList<JvmMember> _members_28 = it.getMembers();
                      final Procedure1<JvmOperation> _function_30 = (JvmOperation it_1) -> {
                        EList<JvmFormalParameter> _parameters = it_1.getParameters();
                        JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(firstRole_3, firstRole_3.getName(), firstRoleType_2);
                        this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                        EList<JvmFormalParameter> _parameters_1 = it_1.getParameters();
                        JvmFormalParameter _parameter_1 = this._jvmTypesBuilder.toParameter(secondRole_2, secondRole_2.getName(), secondRoleType_1);
                        this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_1, _parameter_1);
                        StringConcatenationClient _client = new StringConcatenationClient() {
                          @Override
                          protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                            _builder.append(" ");
                            _builder.append("return new ");
                            String _name = ((Relation)meln).getName();
                            String _plus = (_name + "_Edge");
                            _builder.append(_plus, " ");
                            _builder.append("(this,");
                            String _name_1 = firstRole_3.getName();
                            _builder.append(_name_1, " ");
                            _builder.append(",");
                            String _name_2 = secondRole_2.getName();
                            _builder.append(_name_2, " ");
                            _builder.append(");");
                          }
                        };
                        this._jvmTypesBuilder.setBody(it_1, _client);
                      };
                      JvmOperation _method_24 = this._jvmTypesBuilder.toMethod(meln, "createEdge", this._typeReferenceBuilder.typeRef(edgecname), _function_30);
                      this._jvmTypesBuilder.<JvmOperation>operator_add(_members_28, _method_24);
                      final JvmTypeReference rsetype = this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.relation.RoleSet", firstRoleType_2);
                      final JvmField rsfield = this._jvmTypesBuilder.toField(meln, rolset1, rsetype);
                      if ((rsfield != null)) {
                        EList<JvmMember> _members_29 = it.getMembers();
                        this._jvmTypesBuilder.<JvmField>operator_add(_members_29, rsfield);
                        EList<JvmMember> _members_30 = it.getMembers();
                        String _firstUpper = StringExtensions.toFirstUpper(rolset1);
                        String _plus_1 = ("set" + _firstUpper);
                        final Procedure1<JvmOperation> _function_31 = (JvmOperation it_1) -> {
                          EList<JvmFormalParameter> _parameters = it_1.getParameters();
                          JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(firstRole_3, "croles", this._typeReferenceBuilder.typeRef("java.util.Collection", firstRoleType_2));
                          this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                          StringConcatenationClient _client = new StringConcatenationClient() {
                            @Override
                            protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                              final JvmTypeReference rsimplt = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.relation.impl.RoleSetImpl", firstRoleType_2);
                              _builder.newLineIfNotEmpty();
                              _builder.append("this.");
                              _builder.append(rolset1);
                              _builder.append("=new ");
                              _builder.append(rsimplt);
                              _builder.append("(croles);");
                              _builder.newLineIfNotEmpty();
                            }
                          };
                          this._jvmTypesBuilder.setBody(it_1, _client);
                        };
                        JvmOperation _method_25 = this._jvmTypesBuilder.toMethod(meln, _plus_1, this._typeReferenceBuilder.typeRef(Void.TYPE), _function_31);
                        this._jvmTypesBuilder.<JvmOperation>operator_add(_members_30, _method_25);
                        EList<JvmMember> _members_31 = it.getMembers();
                        String _firstUpper_1 = StringExtensions.toFirstUpper(rolset1);
                        String _plus_2 = ("get" + _firstUpper_1);
                        final Procedure1<JvmOperation> _function_32 = (JvmOperation it_1) -> {
                          StringConcatenationClient _client = new StringConcatenationClient() {
                            @Override
                            protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                              _builder.append("return ");
                              _builder.append(rolset1);
                              _builder.append(";");
                            }
                          };
                          this._jvmTypesBuilder.setBody(it_1, _client);
                        };
                        JvmOperation _method_26 = this._jvmTypesBuilder.toMethod(meln, _plus_2, rsetype, _function_32);
                        this._jvmTypesBuilder.<JvmOperation>operator_add(_members_31, _method_26);
                        if ((!isAutoGraph)) {
                          final JvmTypeReference rsetype2 = this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.relation.RoleSet", secondRoleType_1);
                          final JvmField rsfield2 = this._jvmTypesBuilder.toField(meln, rolset2, rsetype2);
                          if ((rsfield2 != null)) {
                            EList<JvmMember> _members_32 = it.getMembers();
                            this._jvmTypesBuilder.<JvmField>operator_add(_members_32, rsfield2);
                            EList<JvmMember> _members_33 = it.getMembers();
                            String _firstUpper_2 = StringExtensions.toFirstUpper(rolset2);
                            String _plus_3 = ("set" + _firstUpper_2);
                            final Procedure1<JvmOperation> _function_33 = (JvmOperation it_1) -> {
                              EList<JvmFormalParameter> _parameters = it_1.getParameters();
                              JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(secondRole_2, "croles", this._typeReferenceBuilder.typeRef("java.util.Collection", secondRoleType_1));
                              this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                              StringConcatenationClient _client = new StringConcatenationClient() {
                                @Override
                                protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                  final JvmTypeReference rsimplt = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.relation.impl.RoleSetImpl", secondRoleType_1);
                                  _builder.newLineIfNotEmpty();
                                  _builder.append("this.");
                                  _builder.append(rolset2);
                                  _builder.append("=new ");
                                  _builder.append(rsimplt);
                                  _builder.append("(croles);");
                                  _builder.newLineIfNotEmpty();
                                }
                              };
                              this._jvmTypesBuilder.setBody(it_1, _client);
                            };
                            JvmOperation _method_27 = this._jvmTypesBuilder.toMethod(meln, _plus_3, this._typeReferenceBuilder.typeRef(Void.TYPE), _function_33);
                            this._jvmTypesBuilder.<JvmOperation>operator_add(_members_33, _method_27);
                            EList<JvmMember> _members_34 = it.getMembers();
                            String _firstUpper_3 = StringExtensions.toFirstUpper(rolset2);
                            String _plus_4 = ("get" + _firstUpper_3);
                            final Procedure1<JvmOperation> _function_34 = (JvmOperation it_1) -> {
                              StringConcatenationClient _client = new StringConcatenationClient() {
                                @Override
                                protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                  _builder.append("return ");
                                  _builder.append(rolset2);
                                  _builder.append(";");
                                }
                              };
                              this._jvmTypesBuilder.setBody(it_1, _client);
                            };
                            JvmOperation _method_28 = this._jvmTypesBuilder.toMethod(meln, _plus_4, rsetype2, _function_34);
                            this._jvmTypesBuilder.<JvmOperation>operator_add(_members_34, _method_28);
                          }
                        }
                        EList<JvmMember> _members_35 = it.getMembers();
                        final Procedure1<JvmOperation> _function_35 = (JvmOperation it_1) -> {
                          EList<JvmFormalParameter> _parameters = it_1.getParameters();
                          JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "role", firstRoleType_2);
                          this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                          StringConcatenationClient _client = new StringConcatenationClient() {
                            @Override
                            protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                              _builder.append("add");
                              _builder.append(firstRoleType_2);
                              _builder.append("(role);");
                            }
                          };
                          this._jvmTypesBuilder.setBody(it_1, _client);
                        };
                        JvmOperation _method_29 = this._jvmTypesBuilder.toMethod(meln, "add", this._typeReferenceBuilder.typeRef(Void.TYPE), _function_35);
                        this._jvmTypesBuilder.<JvmOperation>operator_add(_members_35, _method_29);
                        EList<JvmMember> _members_36 = it.getMembers();
                        final Procedure1<JvmOperation> _function_36 = (JvmOperation it_1) -> {
                          EList<JvmFormalParameter> _parameters = it_1.getParameters();
                          JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "role", firstRoleType_2);
                          this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                          StringConcatenationClient _client = new StringConcatenationClient() {
                            @Override
                            protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                              _builder.append("remove");
                              _builder.append(firstRoleType_2);
                              _builder.append("(role);");
                            }
                          };
                          this._jvmTypesBuilder.setBody(it_1, _client);
                        };
                        JvmOperation _method_30 = this._jvmTypesBuilder.toMethod(meln, "remove", this._typeReferenceBuilder.typeRef(Void.TYPE), _function_36);
                        this._jvmTypesBuilder.<JvmOperation>operator_add(_members_36, _method_30);
                        EList<JvmMember> _members_37 = it.getMembers();
                        String _simpleName_1 = firstRoleType_2.getSimpleName();
                        String _plus_5 = ("add" + _simpleName_1);
                        final Procedure1<JvmOperation> _function_37 = (JvmOperation it_1) -> {
                          EList<JvmFormalParameter> _parameters = it_1.getParameters();
                          JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "role", firstRoleType_2);
                          this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                          StringConcatenationClient _client = new StringConcatenationClient() {
                            @Override
                            protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                              final JvmTypeReference ltype = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("java.util.HashSet", firstRoleType_2);
                              _builder.newLineIfNotEmpty();
                              _builder.append("if (this.");
                              _builder.append(rolset1);
                              _builder.append(" == null) set");
                              String _firstUpper = StringExtensions.toFirstUpper(rolset1);
                              _builder.append(_firstUpper);
                              _builder.append("( new ");
                              _builder.append(ltype);
                              _builder.append("());");
                              _builder.newLineIfNotEmpty();
                              _builder.append("this.");
                              _builder.append(rolset1);
                              _builder.append(".addRole(role);");
                              _builder.newLineIfNotEmpty();
                            }
                          };
                          this._jvmTypesBuilder.setBody(it_1, _client);
                        };
                        JvmOperation _method_31 = this._jvmTypesBuilder.toMethod(meln, _plus_5, this._typeReferenceBuilder.typeRef(Void.TYPE), _function_37);
                        this._jvmTypesBuilder.<JvmOperation>operator_add(_members_37, _method_31);
                        EList<JvmMember> _members_38 = it.getMembers();
                        String _simpleName_2 = firstRoleType_2.getSimpleName();
                        String _plus_6 = ("remove" + _simpleName_2);
                        final Procedure1<JvmOperation> _function_38 = (JvmOperation it_1) -> {
                          EList<JvmFormalParameter> _parameters = it_1.getParameters();
                          JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "role", firstRoleType_2);
                          this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                          StringConcatenationClient _client = new StringConcatenationClient() {
                            @Override
                            protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                              _builder.append("if (this.");
                              _builder.append(rolset1);
                              _builder.append(" != null) this.");
                              _builder.append(rolset1);
                              _builder.append(".removeRole(role);");
                            }
                          };
                          this._jvmTypesBuilder.setBody(it_1, _client);
                        };
                        JvmOperation _method_32 = this._jvmTypesBuilder.toMethod(meln, _plus_6, this._typeReferenceBuilder.typeRef(Void.TYPE), _function_38);
                        this._jvmTypesBuilder.<JvmOperation>operator_add(_members_38, _method_32);
                        EList<JvmMember> _members_39 = it.getMembers();
                        String _simpleName_3 = firstRoleType_2.getSimpleName();
                        String _plus_7 = ("addAll" + _simpleName_3);
                        final Procedure1<JvmOperation> _function_39 = (JvmOperation it_1) -> {
                          EList<JvmFormalParameter> _parameters = it_1.getParameters();
                          JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "roles", this._typeReferenceBuilder.typeRef("java.lang.Iterable", firstRoleType_2));
                          this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                          StringConcatenationClient _client = new StringConcatenationClient() {
                            @Override
                            protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                              final JvmTypeReference ltype = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("java.util.HashSet", firstRoleType_2);
                              _builder.newLineIfNotEmpty();
                              _builder.append("if (this.");
                              _builder.append(rolset1);
                              _builder.append(" == null) set");
                              String _firstUpper = StringExtensions.toFirstUpper(rolset1);
                              _builder.append(_firstUpper);
                              _builder.append("( new ");
                              _builder.append(ltype);
                              _builder.append("());");
                              _builder.newLineIfNotEmpty();
                              _builder.append("this.");
                              _builder.append(rolset1);
                              _builder.append(".addRoles(roles);");
                              _builder.newLineIfNotEmpty();
                            }
                          };
                          this._jvmTypesBuilder.setBody(it_1, _client);
                        };
                        JvmOperation _method_33 = this._jvmTypesBuilder.toMethod(meln, _plus_7, this._typeReferenceBuilder.typeRef(Void.TYPE), _function_39);
                        this._jvmTypesBuilder.<JvmOperation>operator_add(_members_39, _method_33);
                        EList<JvmMember> _members_40 = it.getMembers();
                        String _simpleName_4 = firstRoleType_2.getSimpleName();
                        String _plus_8 = ("removeAll" + _simpleName_4);
                        final Procedure1<JvmOperation> _function_40 = (JvmOperation it_1) -> {
                          EList<JvmFormalParameter> _parameters = it_1.getParameters();
                          JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "roles", this._typeReferenceBuilder.typeRef("java.lang.Iterable", firstRoleType_2));
                          this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                          StringConcatenationClient _client = new StringConcatenationClient() {
                            @Override
                            protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                              _builder.append("if (this.");
                              _builder.append(rolset1);
                              _builder.append(" != null) this.");
                              _builder.append(rolset1);
                              _builder.append(".removeRoles(roles);");
                            }
                          };
                          this._jvmTypesBuilder.setBody(it_1, _client);
                        };
                        JvmOperation _method_34 = this._jvmTypesBuilder.toMethod(meln, _plus_8, this._typeReferenceBuilder.typeRef(Void.TYPE), _function_40);
                        this._jvmTypesBuilder.<JvmOperation>operator_add(_members_40, _method_34);
                        if ((!isAutoGraph)) {
                          EList<JvmMember> _members_41 = it.getMembers();
                          String _simpleName_5 = secondRoleType_1.getSimpleName();
                          String _plus_9 = ("add" + _simpleName_5);
                          final Procedure1<JvmOperation> _function_41 = (JvmOperation it_1) -> {
                            EList<JvmFormalParameter> _parameters = it_1.getParameters();
                            JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "role", secondRoleType_1);
                            this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                            StringConcatenationClient _client = new StringConcatenationClient() {
                              @Override
                              protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                final JvmTypeReference ltype = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("java.util.HashSet", secondRoleType_1);
                                _builder.newLineIfNotEmpty();
                                _builder.append("if (this.");
                                _builder.append(rolset2);
                                _builder.append(" == null) set");
                                String _firstUpper = StringExtensions.toFirstUpper(rolset2);
                                _builder.append(_firstUpper);
                                _builder.append("( new ");
                                _builder.append(ltype);
                                _builder.append("());");
                                _builder.newLineIfNotEmpty();
                                _builder.append("this.");
                                _builder.append(rolset2);
                                _builder.append(".addRole(role);");
                                _builder.newLineIfNotEmpty();
                              }
                            };
                            this._jvmTypesBuilder.setBody(it_1, _client);
                          };
                          JvmOperation _method_35 = this._jvmTypesBuilder.toMethod(meln, _plus_9, this._typeReferenceBuilder.typeRef(Void.TYPE), _function_41);
                          this._jvmTypesBuilder.<JvmOperation>operator_add(_members_41, _method_35);
                          EList<JvmMember> _members_42 = it.getMembers();
                          final Procedure1<JvmOperation> _function_42 = (JvmOperation it_1) -> {
                            EList<JvmFormalParameter> _parameters = it_1.getParameters();
                            JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "role", secondRoleType_1);
                            this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                            StringConcatenationClient _client = new StringConcatenationClient() {
                              @Override
                              protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                _builder.append("remove");
                                _builder.append(secondRoleType_1);
                                _builder.append("(role);");
                              }
                            };
                            this._jvmTypesBuilder.setBody(it_1, _client);
                          };
                          JvmOperation _method_36 = this._jvmTypesBuilder.toMethod(meln, "remove", this._typeReferenceBuilder.typeRef(Void.TYPE), _function_42);
                          this._jvmTypesBuilder.<JvmOperation>operator_add(_members_42, _method_36);
                          EList<JvmMember> _members_43 = it.getMembers();
                          final Procedure1<JvmOperation> _function_43 = (JvmOperation it_1) -> {
                            EList<JvmFormalParameter> _parameters = it_1.getParameters();
                            JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "role", secondRoleType_1);
                            this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                            StringConcatenationClient _client = new StringConcatenationClient() {
                              @Override
                              protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                _builder.append("add");
                                _builder.append(secondRoleType_1);
                                _builder.append("(role);");
                              }
                            };
                            this._jvmTypesBuilder.setBody(it_1, _client);
                          };
                          JvmOperation _method_37 = this._jvmTypesBuilder.toMethod(meln, "add", this._typeReferenceBuilder.typeRef(Void.TYPE), _function_43);
                          this._jvmTypesBuilder.<JvmOperation>operator_add(_members_43, _method_37);
                          EList<JvmMember> _members_44 = it.getMembers();
                          String _simpleName_6 = secondRoleType_1.getSimpleName();
                          String _plus_10 = ("remove" + _simpleName_6);
                          final Procedure1<JvmOperation> _function_44 = (JvmOperation it_1) -> {
                            EList<JvmFormalParameter> _parameters = it_1.getParameters();
                            JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "role", secondRoleType_1);
                            this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                            StringConcatenationClient _client = new StringConcatenationClient() {
                              @Override
                              protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                _builder.append("if (this.");
                                _builder.append(rolset2);
                                _builder.append(" != null) this.");
                                _builder.append(rolset2);
                                _builder.append(".removeRole(role);");
                              }
                            };
                            this._jvmTypesBuilder.setBody(it_1, _client);
                          };
                          JvmOperation _method_38 = this._jvmTypesBuilder.toMethod(meln, _plus_10, this._typeReferenceBuilder.typeRef(Void.TYPE), _function_44);
                          this._jvmTypesBuilder.<JvmOperation>operator_add(_members_44, _method_38);
                          EList<JvmMember> _members_45 = it.getMembers();
                          String _simpleName_7 = secondRoleType_1.getSimpleName();
                          String _plus_11 = ("addAll" + _simpleName_7);
                          final Procedure1<JvmOperation> _function_45 = (JvmOperation it_1) -> {
                            EList<JvmFormalParameter> _parameters = it_1.getParameters();
                            JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "roles", this._typeReferenceBuilder.typeRef("java.lang.Iterable", secondRoleType_1));
                            this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                            StringConcatenationClient _client = new StringConcatenationClient() {
                              @Override
                              protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                final JvmTypeReference rtype = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("java.util.HashSet", secondRoleType_1);
                                _builder.newLineIfNotEmpty();
                                _builder.append("if (this.");
                                _builder.append(rolset2);
                                _builder.append(" == null) set");
                                String _firstUpper = StringExtensions.toFirstUpper(rolset2);
                                _builder.append(_firstUpper);
                                _builder.append("( new ");
                                _builder.append(rtype);
                                _builder.append("());");
                                _builder.newLineIfNotEmpty();
                                _builder.append("this.");
                                _builder.append(rolset2);
                                _builder.append(".addRoles(roles);");
                                _builder.newLineIfNotEmpty();
                              }
                            };
                            this._jvmTypesBuilder.setBody(it_1, _client);
                          };
                          JvmOperation _method_39 = this._jvmTypesBuilder.toMethod(meln, _plus_11, this._typeReferenceBuilder.typeRef(Void.TYPE), _function_45);
                          this._jvmTypesBuilder.<JvmOperation>operator_add(_members_45, _method_39);
                          EList<JvmMember> _members_46 = it.getMembers();
                          String _simpleName_8 = secondRoleType_1.getSimpleName();
                          String _plus_12 = ("removeAll" + _simpleName_8);
                          final Procedure1<JvmOperation> _function_46 = (JvmOperation it_1) -> {
                            EList<JvmFormalParameter> _parameters = it_1.getParameters();
                            JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "roles", this._typeReferenceBuilder.typeRef("java.lang.Iterable", secondRoleType_1));
                            this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                            StringConcatenationClient _client = new StringConcatenationClient() {
                              @Override
                              protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                _builder.append("if (this.");
                                _builder.append(rolset2);
                                _builder.append(" != null) this.");
                                _builder.append(rolset2);
                                _builder.append(".removeRoles(roles);");
                              }
                            };
                            this._jvmTypesBuilder.setBody(it_1, _client);
                          };
                          JvmOperation _method_40 = this._jvmTypesBuilder.toMethod(meln, _plus_12, this._typeReferenceBuilder.typeRef(Void.TYPE), _function_46);
                          this._jvmTypesBuilder.<JvmOperation>operator_add(_members_46, _method_40);
                        }
                      }
                    }
                    EList<RelElements> _relelns_4 = ((Relation)meln).getRelelns();
                    for (final RelElements reln_4 : _relelns_4) {
                      boolean _matched_7 = false;
                      if (reln_4 instanceof RelPropertyDef) {
                        _matched_7=true;
                        String _name_6 = ((RelPropertyDef)reln_4).getName();
                        boolean _tripleNotEquals_1 = (_name_6 != null);
                        if (_tripleNotEquals_1) {
                          EList<JvmMember> _members_47 = it.getMembers();
                          String _firstUpper_4 = StringExtensions.toFirstUpper(((RelPropertyDef)reln_4).getName());
                          String _plus_13 = ("set" + _firstUpper_4);
                          final Procedure1<JvmOperation> _function_47 = (JvmOperation it_1) -> {
                            EList<JvmFormalParameter> _parameters = it_1.getParameters();
                            JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(reln_4, ((RelPropertyDef)reln_4).getName(), ((RelPropertyDef)reln_4).getType());
                            this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                            StringConcatenationClient _client = new StringConcatenationClient() {
                              @Override
                              protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                final JvmTypeReference typ_edgecname = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef(edgecname);
                                _builder.newLineIfNotEmpty();
                                _builder.append("beginTransaction();");
                                _builder.newLine();
                                _builder.append("for(");
                                _builder.append(typ_edgecname);
                                _builder.append(" _edg_ : this)");
                                _builder.newLineIfNotEmpty();
                                _builder.append("  ");
                                _builder.append("_edg_.set");
                                String _firstUpper = StringExtensions.toFirstUpper(((RelPropertyDef)reln_4).getName());
                                _builder.append(_firstUpper, "  ");
                                _builder.append("(");
                                String _name = ((RelPropertyDef)reln_4).getName();
                                _builder.append(_name, "  ");
                                _builder.append(");");
                                _builder.newLineIfNotEmpty();
                                _builder.append("endTransaction();");
                                _builder.newLine();
                              }
                            };
                            this._jvmTypesBuilder.setBody(it_1, _client);
                          };
                          JvmOperation _method_41 = this._jvmTypesBuilder.toMethod(reln_4, _plus_13, this._typeReferenceBuilder.typeRef(Void.TYPE), _function_47);
                          this._jvmTypesBuilder.<JvmOperation>operator_add(_members_47, _method_41);
                        }
                      }
                      if (!_matched_7) {
                        if (reln_4 instanceof InteractionDef) {
                          _matched_7=true;
                          String _name_6 = ((InteractionDef)reln_4).getName();
                          boolean _tripleNotEquals_1 = (_name_6 != null);
                          if (_tripleNotEquals_1) {
                            EList<JvmMember> _members_47 = it.getMembers();
                            final Procedure1<JvmOperation> _function_47 = (JvmOperation it_1) -> {
                              EList<JvmFormalParameter> _params = ((InteractionDef)reln_4).getParams();
                              for (final JvmFormalParameter p : _params) {
                                EList<JvmFormalParameter> _parameters = it_1.getParameters();
                                JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(reln_4, p.getName(), p.getParameterType());
                                this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
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
                                  _builder.append(typ_edgecname);
                                  _builder.append(" _edg_ : this) {");
                                  _builder.newLineIfNotEmpty();
                                  _builder.append("  ");
                                  _builder.append("_edg_.");
                                  String _name = ((InteractionDef)reln_4).getName();
                                  _builder.append(_name, "  ");
                                  _builder.append("(");
                                  {
                                    EList<JvmFormalParameter> _params = ((InteractionDef)reln_4).getParams();
                                    for(final JvmFormalParameter p : _params) {
                                      {
                                        int _plusPlus = ci++;
                                        boolean _greaterThan = (_plusPlus > 0);
                                        if (_greaterThan) {
                                          _builder.append(",");
                                        }
                                      }
                                      String _name_1 = p.getName();
                                      _builder.append(_name_1, "  ");
                                    }
                                  }
                                  _builder.append(");");
                                  _builder.newLineIfNotEmpty();
                                  {
                                    int _size = ((InteractionDef)reln_4).getComitexpressions().size();
                                    boolean _greaterThan_1 = (_size > 0);
                                    if (_greaterThan_1) {
                                      _builder.append(" ");
                                      _builder.append("_edg_._agr_");
                                      String _name_2 = ((InteractionDef)reln_4).getName();
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
                              this._jvmTypesBuilder.setBody(it_1, _client);
                            };
                            JvmOperation _method_41 = this._jvmTypesBuilder.toMethod(reln_4, ((InteractionDef)reln_4).getName(), this._typeReferenceBuilder.typeRef(Void.TYPE), _function_47);
                            this._jvmTypesBuilder.<JvmOperation>operator_add(_members_47, _method_41);
                          }
                        }
                      }
                      if (!_matched_7) {
                        if (reln_4 instanceof Filterdef) {
                          _matched_7=true;
                          String _name_6 = ((Filterdef)reln_4).getName();
                          boolean _tripleNotEquals_1 = (_name_6 != null);
                          if (_tripleNotEquals_1) {
                            EList<JvmMember> _members_47 = it.getMembers();
                            final Procedure1<JvmOperation> _function_47 = (JvmOperation it_1) -> {
                              EList<JvmFormalParameter> _params = ((Filterdef)reln_4).getParams();
                              for (final JvmFormalParameter p : _params) {
                                EList<JvmFormalParameter> _parameters = it_1.getParameters();
                                JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(reln_4, p.getName(), p.getParameterType());
                                this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                              }
                              StringConcatenationClient _client = new StringConcatenationClient() {
                                @Override
                                protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                  String _name = ((Relation)meln).getName();
                                  String _plus = (_name + "_");
                                  String _name_1 = ((Filterdef)reln_4).getName();
                                  String _plus_1 = (_plus + _name_1);
                                  _builder.append(_plus_1);
                                  _builder.append(" _filter = new ");
                                  String _name_2 = ((Relation)meln).getName();
                                  String _plus_2 = (_name_2 + "_");
                                  String _name_3 = ((Filterdef)reln_4).getName();
                                  String _plus_3 = (_plus_2 + _name_3);
                                  _builder.append(_plus_3);
                                  _builder.append("(");
                                  {
                                    int _size = ((Filterdef)reln_4).getParams().size();
                                    boolean _greaterThan = (_size > 0);
                                    if (_greaterThan) {
                                      {
                                        int _size_1 = ((Filterdef)reln_4).getParams().size();
                                        int _minus = (_size_1 - 1);
                                        IntegerRange _upTo = new IntegerRange(0, _minus);
                                        for(final Integer i : _upTo) {
                                          String _name_4 = ((Filterdef)reln_4).getParams().get((i).intValue()).getName();
                                          _builder.append(_name_4);
                                          {
                                            int _size_2 = ((Filterdef)reln_4).getParams().size();
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
                              this._jvmTypesBuilder.setBody(it_1, _client);
                            };
                            JvmOperation _method_41 = this._jvmTypesBuilder.toMethod(reln_4, ((Filterdef)reln_4).getName(), this._typeReferenceBuilder.typeRef(graphcname.toString()), _function_47);
                            this._jvmTypesBuilder.<JvmOperation>operator_add(_members_47, _method_41);
                          }
                        }
                      }
                    }
                  }
                }
              };
              acceptor.<JvmGenericType>accept(this._jvmTypesBuilder.toClass(modl, graphcname), _function_1);
            }
          }
        }
        if (!_matched) {
          if (meln instanceof Strucdef) {
            _matched=true;
            final Procedure1<JvmGenericType> _function = (JvmGenericType it) -> {
              String _typeArgument = ((Strucdef)meln).getTypeArgument();
              boolean _tripleNotEquals = (_typeArgument != null);
              if (_tripleNotEquals) {
                final JvmTypeParameter param = TypesFactory.eINSTANCE.createJvmTypeParameter();
                param.setName(((Strucdef)meln).getTypeArgument());
                EList<JvmTypeParameter> _typeParameters = it.getTypeParameters();
                this._jvmTypesBuilder.<JvmTypeParameter>operator_add(_typeParameters, param);
                String _superType = ((Strucdef)meln).getSuperType();
                boolean _tripleNotEquals_1 = (_superType != null);
                if (_tripleNotEquals_1) {
                  EList<JvmTypeReference> _superTypes = it.getSuperTypes();
                  JvmTypeReference _typeRef = this._typeReferenceBuilder.typeRef(((Strucdef)meln).getSuperType(), this._typeReferenceBuilder.typeRef(param));
                  this._jvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes, _typeRef);
                }
              } else {
                String _superType_1 = ((Strucdef)meln).getSuperType();
                boolean _tripleNotEquals_2 = (_superType_1 != null);
                if (_tripleNotEquals_2) {
                  EList<JvmTypeReference> _superTypes_1 = it.getSuperTypes();
                  JvmTypeReference _typeRef_1 = this._typeReferenceBuilder.typeRef(((Strucdef)meln).getSuperType());
                  this._jvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes_1, _typeRef_1);
                }
              }
              final List<StrucVarDef> lvdefs = CollectionLiterals.<StrucVarDef>newArrayList();
              EList<StrucEln> _strucelns = ((Strucdef)meln).getStrucelns();
              for (final StrucEln steln : _strucelns) {
                boolean _matched_1 = false;
                if (steln instanceof StrucVarDef) {
                  _matched_1=true;
                  lvdefs.add(((StrucVarDef)steln));
                  JvmField jvmField = this._jvmTypesBuilder.toField(steln, ((StrucVarDef)steln).getName(), ((StrucVarDef)steln).getType());
                  if ((jvmField != null)) {
                    jvmField.setFinal(false);
                    EList<JvmMember> _members = it.getMembers();
                    this._jvmTypesBuilder.<JvmField>operator_add(_members, jvmField);
                    EList<JvmMember> _members_1 = it.getMembers();
                    JvmOperation _setter = this._jvmTypesBuilder.toSetter(steln, ((StrucVarDef)steln).getName(), ((StrucVarDef)steln).getType());
                    this._jvmTypesBuilder.<JvmOperation>operator_add(_members_1, _setter);
                    EList<JvmMember> _members_2 = it.getMembers();
                    JvmOperation _getter = this._jvmTypesBuilder.toGetter(steln, ((StrucVarDef)steln).getName(), ((StrucVarDef)steln).getType());
                    this._jvmTypesBuilder.<JvmOperation>operator_add(_members_2, _getter);
                  }
                }
                if (!_matched_1) {
                  if (steln instanceof StrucFuncDef) {
                    _matched_1=true;
                    JvmTypeReference _type = ((StrucFuncDef)steln).getType();
                    boolean _tripleEquals_1 = (_type == null);
                    if (_tripleEquals_1) {
                      ((StrucFuncDef)steln).setType(this._typeReferenceBuilder.typeRef(Void.TYPE));
                    }
                    EList<JvmMember> _members = it.getMembers();
                    final Procedure1<JvmOperation> _function_1 = (JvmOperation it_1) -> {
                      this._jvmTypesBuilder.setDocumentation(it_1, this._jvmTypesBuilder.getDocumentation(steln));
                      EList<JvmFormalParameter> _params = ((StrucFuncDef)steln).getParams();
                      for (final JvmFormalParameter p : _params) {
                        EList<JvmFormalParameter> _parameters = it_1.getParameters();
                        JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(p, p.getName(), p.getParameterType());
                        this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                      }
                      this._jvmTypesBuilder.setBody(it_1, ((StrucFuncDef)steln).getBody());
                    };
                    JvmOperation _method = this._jvmTypesBuilder.toMethod(steln, ((StrucFuncDef)steln).getName(), ((StrucFuncDef)steln).getType(), _function_1);
                    this._jvmTypesBuilder.<JvmOperation>operator_add(_members, _method);
                  }
                }
              }
              EList<JvmMember> _members = it.getMembers();
              final Procedure1<JvmConstructor> _function_1 = (JvmConstructor it_1) -> {
                StringConcatenationClient _client = new StringConcatenationClient() {
                  @Override
                  protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                    _builder.append("super();");
                    {
                      for(final StrucVarDef vardef : lvdefs) {
                        _builder.newLineIfNotEmpty();
                        JvmTypeReference vtyp = vardef.getType();
                        _builder.newLineIfNotEmpty();
                        {
                          boolean _isPrimitive = OceletJvmModelInferrer.this._primitives.isPrimitive(vtyp);
                          boolean _not = (!_isPrimitive);
                          if (_not) {
                            String _name = vardef.getName();
                            _builder.append(_name);
                            _builder.append(" = new ");
                            _builder.append(vtyp);
                            {
                              boolean _isNumberType = OceletJvmModelInferrer.this.isNumberType(vtyp);
                              if (_isNumberType) {
                                _builder.append("(\"0\");");
                              } else {
                                boolean _equals = vtyp.getQualifiedName().equals("java.lang.Boolean");
                                if (_equals) {
                                  _builder.append("(false);");
                                } else {
                                  _builder.append("();");
                                }
                              }
                            }
                            _builder.newLineIfNotEmpty();
                          }
                        }
                      }
                    }
                  }
                };
                this._jvmTypesBuilder.setBody(it_1, _client);
              };
              JvmConstructor _constructor = this._jvmTypesBuilder.toConstructor(meln, _function_1);
              this._jvmTypesBuilder.<JvmConstructor>operator_add(_members, _constructor);
            };
            acceptor.<JvmGenericType>accept(this._jvmTypesBuilder.toClass(modl, this._iQualifiedNameProvider.getFullyQualifiedName(meln)), _function);
          }
        }
      } catch (final Throwable _t) {
        if (_t instanceof Exception) {
          final Exception e = (Exception)_t;
          StringConcatenation _builder = new StringConcatenation();
          _builder.append("Exception caught : ");
          String _message = e.getMessage();
          _builder.append(_message);
          InputOutput.<String>println(_builder.toString());
        } else {
          throw Exceptions.sneakyThrow(_t);
        }
      }
    }
    if (mainScen) {
      final Procedure1<JvmGenericType> _function = (JvmGenericType it) -> {
        this._jvmTypesBuilder.setDocumentation(it, this._jvmTypesBuilder.getDocumentation(modl));
        EList<JvmTypeReference> _superTypes = it.getSuperTypes();
        JvmTypeReference _typeRef = this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.model.AbstractModel");
        this._jvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes, _typeRef);
        EList<JvmMember> _members = it.getMembers();
        final Procedure1<JvmConstructor> _function_1 = (JvmConstructor it_1) -> {
          StringConcatenationClient _client = new StringConcatenationClient() {
            @Override
            protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
              _builder.append("super(\"");
              _builder.append(modlName);
              _builder.append("\");");
              _builder.newLineIfNotEmpty();
              {
                String _modeldesc = md.getModeldesc();
                boolean _tripleNotEquals = (_modeldesc != null);
                if (_tripleNotEquals) {
                  _builder.append("modDescription = \"");
                  String _modeldesc_1 = md.getModeldesc();
                  _builder.append(_modeldesc_1);
                  _builder.append("\";");
                }
              }
              _builder.newLineIfNotEmpty();
              {
                String _webpage = md.getWebpage();
                boolean _tripleNotEquals_1 = (_webpage != null);
                if (_tripleNotEquals_1) {
                  _builder.append("modelWebPage = \"");
                  String _webpage_1 = md.getWebpage();
                  _builder.append(_webpage_1);
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
                      final JvmTypeReference genptype = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.model.Parameter", pstuff.getType());
                      _builder.newLineIfNotEmpty();
                      {
                        boolean _isNumericType = pstuff.isNumericType();
                        if (_isNumericType) {
                          final JvmTypeReference implptype = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.model.NumericParameterImpl", pstuff.getType());
                          _builder.newLineIfNotEmpty();
                          _builder.append(genptype);
                          _builder.append(" par_");
                          String _name = pstuff.getName();
                          _builder.append(_name);
                          _builder.append(" = new ");
                          _builder.append(implptype);
                          _builder.append("(\"");
                          String _name_1 = pstuff.getName();
                          _builder.append(_name_1);
                          _builder.append("\",\"");
                          String _description = pstuff.getDescription();
                          _builder.append(_description);
                          _builder.append("\",");
                          Boolean _optionnal = pstuff.getOptionnal();
                          _builder.append(_optionnal);
                          _builder.append(",");
                          String _dvalueString = pstuff.getDvalueString();
                          _builder.append(_dvalueString);
                          {
                            Object _minvalue = pstuff.getMinvalue();
                            boolean _tripleEquals = (_minvalue == null);
                            if (_tripleEquals) {
                              _builder.append(",null");
                            } else {
                              _builder.append(",");
                              Object _minvalue_1 = pstuff.getMinvalue();
                              _builder.append(_minvalue_1);
                            }
                          }
                          {
                            Object _maxvalue = pstuff.getMaxvalue();
                            boolean _tripleEquals_1 = (_maxvalue == null);
                            if (_tripleEquals_1) {
                              _builder.append(",null");
                            } else {
                              _builder.append(",");
                              Object _maxvalue_1 = pstuff.getMaxvalue();
                              _builder.append(_maxvalue_1);
                            }
                          }
                          {
                            String _unit = pstuff.getUnit();
                            boolean _tripleEquals_2 = (_unit == null);
                            if (_tripleEquals_2) {
                              _builder.append(",null");
                            } else {
                              _builder.append(",");
                              String _unit_1 = pstuff.getUnit();
                              _builder.append(_unit_1);
                            }
                          }
                          _builder.append(");");
                          _builder.newLineIfNotEmpty();
                        } else {
                          final JvmTypeReference implptype_1 = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.model.ParameterImpl", pstuff.getType());
                          _builder.newLineIfNotEmpty();
                          _builder.append(genptype);
                          _builder.append(" par_");
                          String _name_2 = pstuff.getName();
                          _builder.append(_name_2);
                          _builder.append(" = new ");
                          _builder.append(implptype_1);
                          _builder.append("(\"");
                          String _name_3 = pstuff.getName();
                          _builder.append(_name_3);
                          _builder.append("\",\"");
                          String _description_1 = pstuff.getDescription();
                          _builder.append(_description_1);
                          _builder.append("\",");
                          Boolean _optionnal_1 = pstuff.getOptionnal();
                          _builder.append(_optionnal_1);
                          _builder.append(",");
                          String _dvalueString_1 = pstuff.getDvalueString();
                          _builder.append(_dvalueString_1);
                          {
                            String _unit_2 = pstuff.getUnit();
                            boolean _tripleEquals_3 = (_unit_2 == null);
                            if (_tripleEquals_3) {
                              _builder.append(",null");
                            } else {
                              _builder.append(",\"");
                              String _unit_3 = pstuff.getUnit();
                              _builder.append(_unit_3);
                              _builder.append("\"");
                            }
                          }
                          _builder.append(");");
                          _builder.newLineIfNotEmpty();
                        }
                      }
                      _builder.append("addParameter(par_");
                      String _name_4 = pstuff.getName();
                      _builder.append(_name_4);
                      _builder.append(");");
                      _builder.newLineIfNotEmpty();
                      {
                        Object _dvalue = pstuff.getDvalue();
                        boolean _tripleNotEquals_2 = (_dvalue != null);
                        if (_tripleNotEquals_2) {
                          _builder.append(pstuff.name);
                          _builder.append(" = ");
                          String _dvalueString_2 = pstuff.getDvalueString();
                          _builder.append(_dvalueString_2);
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
          this._jvmTypesBuilder.setBody(it_1, _client);
        };
        JvmConstructor _constructor = this._jvmTypesBuilder.toConstructor(modl, _function_1);
        this._jvmTypesBuilder.<JvmConstructor>operator_add(_members, _constructor);
        for (final Scenario scen : scens) {
          int _compareTo = scen.getName().compareTo(modlName);
          boolean _equals = (_compareTo == 0);
          if (_equals) {
            EList<JvmMember> _members_1 = it.getMembers();
            final Procedure1<JvmOperation> _function_2 = (JvmOperation it_1) -> {
              EList<JvmFormalParameter> _parameters = it_1.getParameters();
              JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(modl, "args", this._jvmTypesBuilder.addArrayTypeDimension(this._typeReferenceBuilder.typeRef("java.lang.String")));
              this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
              it_1.setStatic(true);
              StringConcatenationClient _client = new StringConcatenationClient() {
                @Override
                protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                  _builder.append(modlName);
                  _builder.append(" model_");
                  _builder.append(modlName);
                  _builder.append(" = new ");
                  _builder.append(modlName);
                  _builder.append("();");
                  _builder.newLineIfNotEmpty();
                  _builder.append("model_");
                  _builder.append(modlName);
                  _builder.append(".run_");
                  _builder.append(modlName);
                  _builder.append("();");
                }
              };
              this._jvmTypesBuilder.setBody(it_1, _client);
            };
            JvmOperation _method = this._jvmTypesBuilder.toMethod(modl, "main", this._typeReferenceBuilder.typeRef(Void.TYPE), _function_2);
            this._jvmTypesBuilder.<JvmOperation>operator_add(_members_1, _method);
            EList<JvmMember> _members_2 = it.getMembers();
            final Procedure1<JvmOperation> _function_3 = (JvmOperation it_1) -> {
              this._jvmTypesBuilder.setBody(it_1, scen.getBody());
            };
            JvmOperation _method_1 = this._jvmTypesBuilder.toMethod(modl, ("run_" + modlName), this._typeReferenceBuilder.typeRef(Void.TYPE), _function_3);
            this._jvmTypesBuilder.<JvmOperation>operator_add(_members_2, _method_1);
            EList<JvmMember> _members_3 = it.getMembers();
            final Procedure1<JvmOperation> _function_4 = (JvmOperation it_1) -> {
              EList<JvmFormalParameter> _parameters = it_1.getParameters();
              JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(modl, "in_params", this._typeReferenceBuilder.typeRef("java.util.HashMap", this._typeReferenceBuilder.typeRef("java.lang.String"), this._typeReferenceBuilder.typeRef("java.lang.Object")));
              this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
              StringConcatenationClient _client = new StringConcatenationClient() {
                @Override
                protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                  {
                    boolean _hasParameters = md.hasParameters();
                    if (_hasParameters) {
                      {
                        ArrayList<Parameterstuff> _params = md.getParams();
                        for(final Parameterstuff pstuff : _params) {
                          String _simpleName = pstuff.getType().getSimpleName();
                          _builder.append(_simpleName);
                          _builder.append(" val_");
                          String _name = pstuff.getName();
                          _builder.append(_name);
                          _builder.append(" = (");
                          String _simpleName_1 = pstuff.getType().getSimpleName();
                          _builder.append(_simpleName_1);
                          _builder.append(") in_params.get(\"");
                          String _name_1 = pstuff.getName();
                          _builder.append(_name_1);
                          _builder.append("\");");
                          _builder.newLineIfNotEmpty();
                          _builder.append("if (val_");
                          String _name_2 = pstuff.getName();
                          _builder.append(_name_2);
                          _builder.append(" != null) ");
                          String _name_3 = pstuff.getName();
                          _builder.append(_name_3);
                          _builder.append(" = val_");
                          String _name_4 = pstuff.getName();
                          _builder.append(_name_4);
                          _builder.append(";");
                          _builder.newLineIfNotEmpty();
                        }
                      }
                    }
                  }
                  _builder.append("run_");
                  _builder.append(modlName);
                  _builder.append("();");
                  _builder.newLineIfNotEmpty();
                }
              };
              this._jvmTypesBuilder.setBody(it_1, _client);
            };
            JvmOperation _method_2 = this._jvmTypesBuilder.toMethod(modl, "simulate", this._typeReferenceBuilder.typeRef(Void.TYPE), _function_4);
            this._jvmTypesBuilder.<JvmOperation>operator_add(_members_3, _method_2);
          } else {
            JvmTypeReference rtype = scen.getType();
            if ((rtype == null)) {
              rtype = this._typeReferenceBuilder.typeRef(Void.TYPE);
            }
            EList<JvmMember> _members_4 = it.getMembers();
            final Procedure1<JvmOperation> _function_5 = (JvmOperation it_1) -> {
              EList<JvmFormalParameter> _params = scen.getParams();
              for (final JvmFormalParameter p : _params) {
                EList<JvmFormalParameter> _parameters = it_1.getParameters();
                JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(p, p.getName(), p.getParameterType());
                this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
              }
              this._jvmTypesBuilder.setBody(it_1, scen.getBody());
            };
            JvmOperation _method_3 = this._jvmTypesBuilder.toMethod(scen, scen.getName(), rtype, _function_5);
            this._jvmTypesBuilder.<JvmOperation>operator_add(_members_4, _method_3);
          }
        }
        boolean _hasParameters = md.hasParameters();
        if (_hasParameters) {
          ArrayList<Parameterstuff> _params = md.getParams();
          for (final Parameterstuff pstuff : _params) {
            {
              JvmField jvmField = this._jvmTypesBuilder.toField(modl, pstuff.name, pstuff.type);
              if ((jvmField != null)) {
                jvmField.setFinal(false);
                EList<JvmMember> _members_5 = it.getMembers();
                this._jvmTypesBuilder.<JvmField>operator_add(_members_5, jvmField);
                EList<JvmMember> _members_6 = it.getMembers();
                JvmOperation _setter = this._jvmTypesBuilder.toSetter(modl, pstuff.name, pstuff.type);
                this._jvmTypesBuilder.<JvmOperation>operator_add(_members_6, _setter);
                EList<JvmMember> _members_7 = it.getMembers();
                JvmOperation _getter = this._jvmTypesBuilder.toGetter(modl, pstuff.name, pstuff.type);
                this._jvmTypesBuilder.<JvmOperation>operator_add(_members_7, _getter);
              }
            }
          }
        }
      };
      acceptor.<JvmGenericType>accept(this._jvmTypesBuilder.toClass(modl, (packg + modlName)), _function);
    }
  }
  
  private boolean isNumberType(final JvmTypeReference vtyp) {
    return (((((vtyp.getQualifiedName().equals("java.lang.Integer") || 
      vtyp.getQualifiedName().equals("java.lang.Double")) || 
      vtyp.getQualifiedName().equals("java.lang.Float")) || 
      vtyp.getQualifiedName().equals("java.lang.Long")) || 
      vtyp.getQualifiedName().equals("java.lang.Byte")) || 
      vtyp.getQualifiedName().equals("java.lang.Short"));
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
