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

/**
 * Java code inferrer of the Ocelet language
 * 
 * @author Pascal Degenne - Initial contribution
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
                    boolean _notEquals = (!Objects.equal(mt, null));
                    if (_notEquals) {
                      boolean _matched_1 = false;
                      if (!_matched_1) {
                        if (mt instanceof Entity) {
                          _matched_1=true;
                          QualifiedName _fullyQualifiedName_1 = this._iQualifiedNameProvider.getFullyQualifiedName(mt);
                          String _string = _fullyQualifiedName_1.toString();
                          final JvmTypeReference entype = this._typeReferenceBuilder.typeRef(_string);
                          String _name_2 = ((Entity)mt).getName();
                          final String entname = StringExtensions.toFirstUpper(_name_2);
                          final JvmTypeReference listype = this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.ocltypes.List", entype);
                          final HashMap<String, String> propmap = new HashMap<String, String>();
                          final HashMap<String, String> propmapf = new HashMap<String, String>();
                          EList<EntityElements> _entelns = ((Entity)mt).getEntelns();
                          for (final EntityElements eprop : _entelns) {
                            boolean _matched_2 = false;
                            if (!_matched_2) {
                              if (eprop instanceof PropertyDef) {
                                _matched_2=true;
                                JvmTypeReference _type = ((PropertyDef)eprop).getType();
                                boolean _notEquals_1 = (!Objects.equal(_type, null));
                                if (_notEquals_1) {
                                  String _name_3 = ((PropertyDef)eprop).getName();
                                  JvmTypeReference _type_1 = ((PropertyDef)eprop).getType();
                                  String _simpleName = _type_1.getSimpleName();
                                  propmap.put(_name_3, _simpleName);
                                  String _name_4 = ((PropertyDef)eprop).getName();
                                  JvmTypeReference _type_2 = ((PropertyDef)eprop).getType();
                                  String _qualifiedName = _type_2.getQualifiedName();
                                  propmapf.put(_name_4, _qualifiedName);
                                }
                              }
                            }
                          }
                          String _storetype_1 = ((Datafacer)meln).getStoretype();
                          String _plus_2 = ("" + _storetype_1);
                          boolean _equals_1 = "RasterFile".equals(_plus_2);
                          if (_equals_1) {
                            final JvmTypeReference tabType = this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.raster.Grid");
                            EList<JvmMember> _members_1 = it.getMembers();
                            final Procedure1<JvmOperation> _function_2 = (JvmOperation it_1) -> {
                              StringConcatenationClient _client = new StringConcatenationClient() {
                                @Override
                                protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                  _builder.append("if(grid == null){");
                                  _builder.newLine();
                                  _builder.append("                  \t\t\t\t");
                                  _builder.append("grid = new Grid(getWidth(), getHeight(), getGridGeometry());");
                                  _builder.newLine();
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
                                              _builder.append("grid.addProp(\"");
                                              String _prop_1 = mp.getProp();
                                              _builder.append(_prop_1, "");
                                              _builder.append("\",\"");
                                              String _colname_1 = mp.getColname();
                                              _builder.append(_colname_1, "");
                                              _builder.append("\");");
                                              _builder.newLineIfNotEmpty();
                                            }
                                          }
                                        }
                                      }
                                    }
                                  }
                                  _builder.append("                  \t\t\t\t");
                                  _builder.append(entname, "                  \t\t\t\t");
                                  _builder.append(" entity = new ");
                                  _builder.append(entname, "                  \t\t\t\t");
                                  _builder.append("();");
                                  _builder.newLineIfNotEmpty();
                                  _builder.append("                  \t   \t    \t");
                                  _builder.append("grid.setInitRaster(raster.getRaster(0, 0 , getWidth(), getHeight()));");
                                  _builder.newLine();
                                  _builder.append("                  \t   \t    \t");
                                  _builder.append("grid.setFinalProperties(entity.getProps());");
                                  _builder.newLine();
                                  _builder.append("                  \t   \t    \t");
                                  _builder.append("grid.setRes(raster);");
                                  _builder.newLine();
                                  _builder.append("                  \t   \t    \t");
                                  _builder.append("fr.ocelet.runtime.raster.GridManager.getInstance().add(grid); ");
                                  _builder.newLine();
                                  _builder.append("                  \t   \t    \t");
                                  _builder.append("entity.setNumGrid(fr.ocelet.runtime.raster.GridManager.getInstance().getCurrentIndex());");
                                  _builder.newLine();
                                  _builder.append("                  \t \t\t\t");
                                  _builder.append("}");
                                  _builder.newLine();
                                  _builder.append("                  \t  \t\t\t");
                                  _builder.append("return grid;");
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
                              JvmTypeReference _typeRef_1 = this._typeReferenceBuilder.typeRef("java.lang.Integer");
                              JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "minX", _typeRef_1);
                              this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                              EList<JvmFormalParameter> _parameters_1 = it_1.getParameters();
                              JvmTypeReference _typeRef_2 = this._typeReferenceBuilder.typeRef("java.lang.Integer");
                              JvmFormalParameter _parameter_1 = this._jvmTypesBuilder.toParameter(meln, "minY", _typeRef_2);
                              this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_1, _parameter_1);
                              EList<JvmFormalParameter> _parameters_2 = it_1.getParameters();
                              JvmTypeReference _typeRef_3 = this._typeReferenceBuilder.typeRef("java.lang.Integer");
                              JvmFormalParameter _parameter_2 = this._jvmTypesBuilder.toParameter(meln, "maxX", _typeRef_3);
                              this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_2, _parameter_2);
                              EList<JvmFormalParameter> _parameters_3 = it_1.getParameters();
                              JvmTypeReference _typeRef_4 = this._typeReferenceBuilder.typeRef("java.lang.Integer");
                              JvmFormalParameter _parameter_3 = this._jvmTypesBuilder.toParameter(meln, "maxY", _typeRef_4);
                              this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_3, _parameter_3);
                              StringConcatenationClient _client = new StringConcatenationClient() {
                                @Override
                                protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                  _builder.append("if(grid == null){");
                                  _builder.newLine();
                                  _builder.append("                  \t  \t\t");
                                  _builder.append("grid = new Grid(minX, minY, maxX, maxY, getGridGeometry());");
                                  _builder.newLine();
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
                                              _builder.append("grid.addProp(\"");
                                              String _prop_1 = mp.getProp();
                                              _builder.append(_prop_1, "");
                                              _builder.append("\",\"");
                                              String _colname_1 = mp.getColname();
                                              _builder.append(_colname_1, "");
                                              _builder.append("\");");
                                              _builder.newLineIfNotEmpty();
                                            }
                                          }
                                        }
                                      }
                                    }
                                  }
                                  _builder.append("                  \t  \t  \t");
                                  _builder.append(entname, "                  \t  \t  \t");
                                  _builder.append(" entity = new ");
                                  _builder.append(entname, "                  \t  \t  \t");
                                  _builder.append("(); ");
                                  _builder.newLineIfNotEmpty();
                                  _builder.append("                  \t   \t    ");
                                  _builder.append("grid.setInitRaster(raster.getRaster(minX, minY, maxX, maxY));");
                                  _builder.newLine();
                                  _builder.append("                  \t   \t    ");
                                  _builder.append("grid.setFinalProperties(entity.getProps());");
                                  _builder.newLine();
                                  _builder.append("                  \t   \t    ");
                                  _builder.append("grid.setRes(raster);");
                                  _builder.newLine();
                                  _builder.append("                  \t   \t    ");
                                  _builder.append("fr.ocelet.runtime.raster.GridManager.getInstance().add(grid);");
                                  _builder.newLine();
                                  _builder.append("                  \t   \t    ");
                                  _builder.append("entity.setNumGrid(fr.ocelet.runtime.raster.GridManager.getInstance().getCurrentIndex());");
                                  _builder.newLine();
                                  _builder.append("                  \t  \t\t");
                                  _builder.append("}");
                                  _builder.newLine();
                                  _builder.append("                  \t  \t\t");
                                  _builder.append("return grid;");
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
                              JvmTypeReference _typeRef_1 = this._typeReferenceBuilder.typeRef("fr.ocelet.datafacer.ocltypes.Shapefile");
                              JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "shp", _typeRef_1);
                              this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                              StringConcatenationClient _client = new StringConcatenationClient() {
                                @Override
                                protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                  _builder.append("if(grid == null){");
                                  _builder.newLine();
                                  _builder.append("grid = new Grid();");
                                  _builder.newLine();
                                  {
                                    EList<Mdef> _matchprops = matchdef.getMatchprops();
                                    for(final Mdef mp : _matchprops) {
                                      _builder.append("                  \t");
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
                                              _builder.append("grid.addProp(\"");
                                              String _prop_1 = mp.getProp();
                                              _builder.append(_prop_1, "");
                                              _builder.append("\",\"");
                                              String _colname_1 = mp.getColname();
                                              _builder.append(_colname_1, "");
                                              _builder.append("\");");
                                              _builder.newLineIfNotEmpty();
                                            }
                                          }
                                        }
                                      }
                                    }
                                  }
                                  _builder.append(entname, "");
                                  _builder.append(" entity = new ");
                                  _builder.append(entname, "");
                                  _builder.append("();");
                                  _builder.newLineIfNotEmpty();
                                  _builder.append("                   \t");
                                  _builder.append("grid.setData(shp.getBounds(), raster, entity.getProps().size());");
                                  _builder.newLine();
                                  _builder.append("                    ");
                                  _builder.append("grid.setFinalProperties(entity.getProps());");
                                  _builder.newLine();
                                  _builder.append("grid.setRes(raster);");
                                  _builder.newLine();
                                  _builder.append("fr.ocelet.runtime.raster.GridManager.getInstance().add(grid); ");
                                  _builder.newLine();
                                  _builder.append("                    ");
                                  _builder.append("entity.setNumGrid(fr.ocelet.runtime.raster.GridManager.getInstance().getCurrentIndex());");
                                  _builder.newLine();
                                  _builder.append("}");
                                  _builder.newLine();
                                  _builder.append("                    ");
                                  _builder.append("return grid;");
                                  _builder.newLine();
                                }
                              };
                              this._jvmTypesBuilder.setBody(it_1, _client);
                            };
                            JvmOperation _method_2 = this._jvmTypesBuilder.toMethod(meln, ("readAll" + entname), tabType, _function_4);
                            this._jvmTypesBuilder.<JvmOperation>operator_add(_members_3, _method_2);
                          } else {
                            Class<?> _forName = Class.forName("fr.ocelet.datafacer.InputDatafacer");
                            String _storetype_2 = ((Datafacer)meln).getStoretype();
                            String _plus_3 = ("fr.ocelet.datafacer.ocltypes." + _storetype_2);
                            Class<?> _forName_1 = Class.forName(_plus_3);
                            boolean _isAssignableFrom = _forName.isAssignableFrom(_forName_1);
                            if (_isAssignableFrom) {
                              final JvmTypeReference inputRecordType = this._typeReferenceBuilder.typeRef("fr.ocelet.datafacer.InputDataRecord");
                              EList<JvmMember> _members_4 = it.getMembers();
                              final Procedure1<JvmOperation> _function_5 = (JvmOperation it_1) -> {
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
                                      _builder.append(entname, "");
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
                                JvmTypeReference _typeRef_1 = this._typeReferenceBuilder.typeRef("fr.ocelet.datafacer.InputDataRecord");
                                JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "_rec", _typeRef_1);
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
                                this._jvmTypesBuilder.setBody(it_1, _client);
                              };
                              JvmOperation _method_5 = this._jvmTypesBuilder.toMethod(meln, (("create" + entname) + "FromRecord"), entype, _function_7);
                              this._jvmTypesBuilder.<JvmOperation>operator_add(_members_6, _method_5);
                              JvmTypeReference _typeRef_1 = this._typeReferenceBuilder.typeRef("java.lang.String");
                              JvmTypeReference _typeRef_2 = this._typeReferenceBuilder.typeRef("java.lang.String");
                              final JvmTypeReference hmtype = this._typeReferenceBuilder.typeRef("java.util.HashMap", _typeRef_1, _typeRef_2);
                              EList<JvmMember> _members_7 = it.getMembers();
                              final Procedure1<JvmOperation> _function_8 = (JvmOperation it_1) -> {
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
                                this._jvmTypesBuilder.setBody(it_1, _client);
                              };
                              JvmOperation _method_6 = this._jvmTypesBuilder.toMethod(meln, "getMatchdef", hmtype, _function_8);
                              this._jvmTypesBuilder.<JvmOperation>operator_add(_members_7, _method_6);
                            }
                            Class<?> _forName_2 = Class.forName("fr.ocelet.datafacer.FiltrableDatafacer");
                            String _storetype_3 = ((Datafacer)meln).getStoretype();
                            String _plus_4 = ("fr.ocelet.datafacer.ocltypes." + _storetype_3);
                            Class<?> _forName_3 = Class.forName(_plus_4);
                            boolean _isAssignableFrom_1 = _forName_2.isAssignableFrom(_forName_3);
                            if (_isAssignableFrom_1) {
                              EList<JvmMember> _members_8 = it.getMembers();
                              final Procedure1<JvmOperation> _function_9 = (JvmOperation it_1) -> {
                                EList<JvmFormalParameter> _parameters = it_1.getParameters();
                                JvmTypeReference _typeRef_3 = this._typeReferenceBuilder.typeRef("java.lang.String");
                                JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "_filt", _typeRef_3);
                                this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
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
                                this._jvmTypesBuilder.setBody(it_1, _client);
                              };
                              JvmOperation _method_7 = this._jvmTypesBuilder.toMethod(meln, ("readFiltered" + entname), listype, _function_9);
                              this._jvmTypesBuilder.<JvmOperation>operator_add(_members_8, _method_7);
                            }
                            Class<?> _forName_4 = Class.forName("fr.ocelet.datafacer.OutputDatafacer");
                            String _storetype_4 = ((Datafacer)meln).getStoretype();
                            String _plus_5 = ("fr.ocelet.datafacer.ocltypes." + _storetype_4);
                            Class<?> _forName_5 = Class.forName(_plus_5);
                            boolean _isAssignableFrom_2 = _forName_4.isAssignableFrom(_forName_5);
                            if (_isAssignableFrom_2) {
                              EList<JvmMember> _members_9 = it.getMembers();
                              JvmTypeReference _typeRef_3 = this._typeReferenceBuilder.typeRef("fr.ocelet.datafacer.OutputDataRecord");
                              final Procedure1<JvmOperation> _function_10 = (JvmOperation it_1) -> {
                                EList<JvmFormalParameter> _parameters = it_1.getParameters();
                                JvmTypeReference _typeRef_4 = this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.entity.Entity");
                                JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "ety", _typeRef_4);
                                this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                                EList<JvmTypeReference> _exceptions = it_1.getExceptions();
                                JvmTypeReference _typeRef_5 = this._typeReferenceBuilder.typeRef("java.lang.IllegalArgumentException");
                                this._jvmTypesBuilder.<JvmTypeReference>operator_add(_exceptions, _typeRef_5);
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
                                        _builder.append("                   ");
                                        _builder.append("odr.setAttribute(\"");
                                        String _colname = mp.getColname();
                                        _builder.append(_colname, "                   ");
                                        _builder.append("\",((");
                                        _builder.append(entname, "                   ");
                                        _builder.append(") ety).get");
                                        String _prop = mp.getProp();
                                        String _firstUpper = StringExtensions.toFirstUpper(_prop);
                                        _builder.append(_firstUpper, "                   ");
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
                              JvmOperation _method_8 = this._jvmTypesBuilder.toMethod(meln, "createRecord", _typeRef_3, _function_10);
                              this._jvmTypesBuilder.<JvmOperation>operator_add(_members_9, _method_8);
                            }
                            Class<?> _forName_6 = Class.forName("fr.ocelet.datafacer.ocltypes.Csvfile");
                            String _storetype_5 = ((Datafacer)meln).getStoretype();
                            String _plus_6 = ("fr.ocelet.datafacer.ocltypes." + _storetype_5);
                            Class<?> _forName_7 = Class.forName(_plus_6);
                            boolean _isAssignableFrom_3 = _forName_6.isAssignableFrom(_forName_7);
                            if (_isAssignableFrom_3) {
                              EList<JvmMember> _members_10 = it.getMembers();
                              JvmTypeReference _typeRef_4 = this._typeReferenceBuilder.typeRef("java.lang.String");
                              final Procedure1<JvmOperation> _function_11 = (JvmOperation it_1) -> {
                                StringConcatenationClient _client = new StringConcatenationClient() {
                                  @Override
                                  protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                    _builder.append("  ");
                                    _builder.append("StringBuffer sb = new StringBuffer();");
                                    _builder.newLine();
                                    _builder.append("  ");
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
                                        _builder.append(_colname, "");
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
                              JvmOperation _method_9 = this._jvmTypesBuilder.toMethod(meln, "headerString", _typeRef_4, _function_11);
                              this._jvmTypesBuilder.<JvmOperation>operator_add(_members_10, _method_9);
                              EList<JvmMember> _members_11 = it.getMembers();
                              JvmTypeReference _typeRef_5 = this._typeReferenceBuilder.typeRef("java.lang.String");
                              final Procedure1<JvmOperation> _function_12 = (JvmOperation it_1) -> {
                                EList<JvmFormalParameter> _parameters = it_1.getParameters();
                                JvmTypeReference _typeRef_6 = this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.entity.Entity");
                                JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "_entity", _typeRef_6);
                                this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                                StringConcatenationClient _client = new StringConcatenationClient() {
                                  @Override
                                  protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                    _builder.append("  ");
                                    _builder.append("StringBuffer sb = new StringBuffer();");
                                    _builder.newLine();
                                    _builder.append("  ");
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
                                        _builder.append(_prop, "");
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
                              JvmOperation _method_10 = this._jvmTypesBuilder.toMethod(meln, "propsString", _typeRef_5, _function_12);
                              this._jvmTypesBuilder.<JvmOperation>operator_add(_members_11, _method_10);
                            }
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
            acceptor.<JvmGenericType>accept(_class, _function);
          }
        }
        if (!_matched) {
          if (meln instanceof Entity) {
            _matched=true;
            QualifiedName _fullyQualifiedName = this._iQualifiedNameProvider.getFullyQualifiedName(meln);
            JvmGenericType _class = this._jvmTypesBuilder.toClass(modl, _fullyQualifiedName);
            final Procedure1<JvmGenericType> _function = (JvmGenericType it) -> {
              String _documentation = this._jvmTypesBuilder.getDocumentation(meln);
              this._jvmTypesBuilder.setDocumentation(it, _documentation);
              EList<JvmTypeReference> _superTypes = it.getSuperTypes();
              JvmTypeReference _typeRef = this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.entity.AbstractEntity");
              this._jvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes, _typeRef);
              final List<PropertyDef> lpropdefs = CollectionLiterals.<PropertyDef>newArrayList();
              final List<String> cellProps = CollectionLiterals.<String>newArrayList();
              boolean isCell = false;
              EList<EntityElements> _entelns = ((Entity)meln).getEntelns();
              for (final EntityElements enteln : _entelns) {
                boolean _matched_1 = false;
                if (!_matched_1) {
                  if (enteln instanceof PropertyDef) {
                    _matched_1=true;
                    String _name_2 = ((PropertyDef)enteln).getName();
                    boolean _notEquals = (!Objects.equal(_name_2, null));
                    if (_notEquals) {
                      JvmTypeReference _type = ((PropertyDef)enteln).getType();
                      String _simpleName = _type.getSimpleName();
                      boolean _equals_1 = _simpleName.equals("Cell");
                      if (_equals_1) {
                        isCell = true;
                      }
                    }
                  }
                }
              }
              EList<EntityElements> _entelns_1 = ((Entity)meln).getEntelns();
              for (final EntityElements enteln_1 : _entelns_1) {
                boolean _matched_2 = false;
                if (!_matched_2) {
                  if (enteln_1 instanceof PropertyDef) {
                    _matched_2=true;
                    String _name_2 = ((PropertyDef)enteln_1).getName();
                    boolean _notEquals = (!Objects.equal(_name_2, null));
                    if (_notEquals) {
                      lpropdefs.add(((PropertyDef)enteln_1));
                      JvmTypeReference _type = ((PropertyDef)enteln_1).getType();
                      String _simpleName = _type.getSimpleName();
                      boolean _equals_1 = _simpleName.equals("Cell");
                      boolean _not = (!_equals_1);
                      if (_not) {
                        String _name_3 = ((PropertyDef)enteln_1).getName();
                        cellProps.add(_name_3);
                      }
                      if (isCell) {
                        String _name_4 = ((PropertyDef)enteln_1).getName();
                        boolean _equals_2 = _name_4.equals("cell");
                        boolean _not_1 = (!_equals_2);
                        if (_not_1) {
                          EList<JvmMember> _members = it.getMembers();
                          String _name_5 = ((PropertyDef)enteln_1).getName();
                          String _firstUpper = StringExtensions.toFirstUpper(_name_5);
                          String _plus_1 = ("set" + _firstUpper);
                          JvmTypeReference _typeRef_1 = this._typeReferenceBuilder.typeRef(Void.TYPE);
                          final Procedure1<JvmOperation> _function_1 = (JvmOperation it_1) -> {
                            String _documentation_1 = this._jvmTypesBuilder.getDocumentation(enteln_1);
                            this._jvmTypesBuilder.setDocumentation(it_1, _documentation_1);
                            final String parName = ((PropertyDef)enteln_1).getName();
                            EList<JvmFormalParameter> _parameters = it_1.getParameters();
                            JvmTypeReference _type_1 = ((PropertyDef)enteln_1).getType();
                            JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(enteln_1, parName, _type_1);
                            this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                            StringConcatenationClient _client = new StringConcatenationClient() {
                              @Override
                              protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                _builder.append("fr.ocelet.runtime.raster.GridManager.getInstance().get(numGrid).setValue(\"");
                                String _name = ((PropertyDef)enteln_1).getName();
                                _builder.append(_name, "");
                                _builder.append("\",getX(), getY(),");
                                String _name_1 = ((PropertyDef)enteln_1).getName();
                                _builder.append(_name_1, "");
                                _builder.append(");");
                                _builder.newLineIfNotEmpty();
                              }
                            };
                            this._jvmTypesBuilder.setBody(it_1, _client);
                          };
                          JvmOperation _method = this._jvmTypesBuilder.toMethod(enteln_1, _plus_1, _typeRef_1, _function_1);
                          this._jvmTypesBuilder.<JvmOperation>operator_add(_members, _method);
                          EList<JvmMember> _members_1 = it.getMembers();
                          String _name_6 = ((PropertyDef)enteln_1).getName();
                          String _firstUpper_1 = StringExtensions.toFirstUpper(_name_6);
                          String _plus_2 = ("get" + _firstUpper_1);
                          JvmTypeReference _type_1 = ((PropertyDef)enteln_1).getType();
                          final Procedure1<JvmOperation> _function_2 = (JvmOperation it_1) -> {
                            String _documentation_1 = this._jvmTypesBuilder.getDocumentation(enteln_1);
                            this._jvmTypesBuilder.setDocumentation(it_1, _documentation_1);
                            StringConcatenationClient _client = new StringConcatenationClient() {
                              @Override
                              protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                _builder.append("return fr.ocelet.runtime.raster.GridManager.getInstance().get(numGrid).getValue(\"");
                                String _name = ((PropertyDef)enteln_1).getName();
                                _builder.append(_name, "");
                                _builder.append("\",getX(), getY());");
                                _builder.newLineIfNotEmpty();
                              }
                            };
                            this._jvmTypesBuilder.setBody(it_1, _client);
                          };
                          JvmOperation _method_1 = this._jvmTypesBuilder.toMethod(enteln_1, _plus_2, _type_1, _function_2);
                          this._jvmTypesBuilder.<JvmOperation>operator_add(_members_1, _method_1);
                        }
                      } else {
                        EList<JvmMember> _members_2 = it.getMembers();
                        String _name_7 = ((PropertyDef)enteln_1).getName();
                        String _firstUpper_2 = StringExtensions.toFirstUpper(_name_7);
                        String _plus_3 = ("set" + _firstUpper_2);
                        JvmTypeReference _typeRef_2 = this._typeReferenceBuilder.typeRef(Void.TYPE);
                        final Procedure1<JvmOperation> _function_3 = (JvmOperation it_1) -> {
                          String _documentation_1 = this._jvmTypesBuilder.getDocumentation(enteln_1);
                          this._jvmTypesBuilder.setDocumentation(it_1, _documentation_1);
                          final String parName = ((PropertyDef)enteln_1).getName();
                          EList<JvmFormalParameter> _parameters = it_1.getParameters();
                          JvmTypeReference _type_2 = ((PropertyDef)enteln_1).getType();
                          JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(enteln_1, parName, _type_2);
                          this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                          StringConcatenationClient _client = new StringConcatenationClient() {
                            @Override
                            protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                              _builder.append("setProperty(\"");
                              String _name = ((PropertyDef)enteln_1).getName();
                              _builder.append(_name, "");
                              _builder.append("\",");
                              _builder.append(parName, "");
                              _builder.append(");");
                            }
                          };
                          this._jvmTypesBuilder.setBody(it_1, _client);
                        };
                        JvmOperation _method_2 = this._jvmTypesBuilder.toMethod(enteln_1, _plus_3, _typeRef_2, _function_3);
                        this._jvmTypesBuilder.<JvmOperation>operator_add(_members_2, _method_2);
                        EList<JvmMember> _members_3 = it.getMembers();
                        String _name_8 = ((PropertyDef)enteln_1).getName();
                        String _firstUpper_3 = StringExtensions.toFirstUpper(_name_8);
                        String _plus_4 = ("get" + _firstUpper_3);
                        JvmTypeReference _type_2 = ((PropertyDef)enteln_1).getType();
                        final Procedure1<JvmOperation> _function_4 = (JvmOperation it_1) -> {
                          String _documentation_1 = this._jvmTypesBuilder.getDocumentation(enteln_1);
                          this._jvmTypesBuilder.setDocumentation(it_1, _documentation_1);
                          StringConcatenationClient _client = new StringConcatenationClient() {
                            @Override
                            protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                              _builder.append("return getProperty(\"");
                              String _name = ((PropertyDef)enteln_1).getName();
                              _builder.append(_name, "");
                              _builder.append("\");");
                            }
                          };
                          this._jvmTypesBuilder.setBody(it_1, _client);
                        };
                        JvmOperation _method_3 = this._jvmTypesBuilder.toMethod(enteln_1, _plus_4, _type_2, _function_4);
                        this._jvmTypesBuilder.<JvmOperation>operator_add(_members_3, _method_3);
                      }
                    }
                  }
                }
                if (!_matched_2) {
                  if (enteln_1 instanceof ServiceDef) {
                    _matched_2=true;
                    JvmTypeReference rtype = ((ServiceDef)enteln_1).getType();
                    boolean _equals_1 = Objects.equal(rtype, null);
                    if (_equals_1) {
                      JvmTypeReference _typeRef_1 = this._typeReferenceBuilder.typeRef(Void.TYPE);
                      rtype = _typeRef_1;
                    }
                    EList<JvmMember> _members = it.getMembers();
                    String _name_2 = ((ServiceDef)enteln_1).getName();
                    final Procedure1<JvmOperation> _function_1 = (JvmOperation it_1) -> {
                      String _documentation_1 = this._jvmTypesBuilder.getDocumentation(enteln_1);
                      this._jvmTypesBuilder.setDocumentation(it_1, _documentation_1);
                      EList<JvmFormalParameter> _params = ((ServiceDef)enteln_1).getParams();
                      for (final JvmFormalParameter p : _params) {
                        EList<JvmFormalParameter> _parameters = it_1.getParameters();
                        String _name_3 = p.getName();
                        JvmTypeReference _parameterType = p.getParameterType();
                        JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(p, _name_3, _parameterType);
                        this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                      }
                      XExpression _body = ((ServiceDef)enteln_1).getBody();
                      this._jvmTypesBuilder.setBody(it_1, _body);
                    };
                    JvmOperation _method = this._jvmTypesBuilder.toMethod(enteln_1, _name_2, rtype, _function_1);
                    this._jvmTypesBuilder.<JvmOperation>operator_add(_members, _method);
                  }
                }
                if (!_matched_2) {
                  if (enteln_1 instanceof ConstructorDef) {
                    _matched_2=true;
                    EList<JvmMember> _members = it.getMembers();
                    String _name_2 = ((ConstructorDef)enteln_1).getName();
                    QualifiedName _fullyQualifiedName_1 = this._iQualifiedNameProvider.getFullyQualifiedName(meln);
                    String _string = _fullyQualifiedName_1.toString();
                    JvmTypeReference _typeRef_1 = this._typeReferenceBuilder.typeRef(_string);
                    final Procedure1<JvmOperation> _function_1 = (JvmOperation it_1) -> {
                      it_1.setStatic(true);
                      String _documentation_1 = this._jvmTypesBuilder.getDocumentation(enteln_1);
                      this._jvmTypesBuilder.setDocumentation(it_1, _documentation_1);
                      EList<JvmFormalParameter> _params = ((ConstructorDef)enteln_1).getParams();
                      for (final JvmFormalParameter p : _params) {
                        EList<JvmFormalParameter> _parameters = it_1.getParameters();
                        String _name_3 = p.getName();
                        JvmTypeReference _parameterType = p.getParameterType();
                        JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(p, _name_3, _parameterType);
                        this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                      }
                      XExpression _body = ((ConstructorDef)enteln_1).getBody();
                      this._jvmTypesBuilder.setBody(it_1, _body);
                    };
                    JvmOperation _method = this._jvmTypesBuilder.toMethod(enteln_1, _name_2, _typeRef_1, _function_1);
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
                            boolean _isNumberType = OceletJvmModelInferrer.this.isNumberType(vtyp);
                            if (_isNumberType) {
                              _builder.append("(\"0\"));");
                            } else {
                              String _qualifiedName = vtyp.getQualifiedName();
                              boolean _equals = _qualifiedName.equals("java.lang.Boolean");
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
                      _builder.append("this.cell = new fr.ocelet.runtime.geom.ocltypes.Cell();");
                      _builder.newLine();
                      _builder.append("this.setSpatialType(cell);");
                      _builder.newLine();
                    }
                  };
                  this._jvmTypesBuilder.setBody(it_1, _client);
                };
                JvmConstructor _constructor_1 = this._jvmTypesBuilder.toConstructor(meln, _function_2);
                this._jvmTypesBuilder.<JvmConstructor>operator_add(_members_1, _constructor_1);
                EList<JvmMember> _members_2 = it.getMembers();
                JvmTypeReference _typeRef_1 = this._typeReferenceBuilder.typeRef("java.lang.String");
                JvmTypeReference _typeRef_2 = this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.ocltypes.List", _typeRef_1);
                final Procedure1<JvmOperation> _function_3 = (JvmOperation it_1) -> {
                  StringConcatenationClient _client = new StringConcatenationClient() {
                    @Override
                    protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                      _builder.append("List<String> names = new List<String>();");
                      _builder.newLine();
                      {
                        for(final String name : cellProps) {
                          _builder.append("names.add(\"");
                          _builder.append(name, "");
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
                JvmOperation _method = this._jvmTypesBuilder.toMethod(meln, "getProps", _typeRef_2, _function_3);
                this._jvmTypesBuilder.<JvmOperation>operator_add(_members_2, _method);
                EList<JvmMember> _members_3 = it.getMembers();
                JvmTypeReference _typeRef_3 = this._typeReferenceBuilder.typeRef(Void.TYPE);
                final Procedure1<JvmOperation> _function_4 = (JvmOperation it_1) -> {
                  EList<JvmFormalParameter> _parameters = it_1.getParameters();
                  JvmTypeReference _typeRef_4 = this._typeReferenceBuilder.typeRef("java.lang.String");
                  JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "type", _typeRef_4);
                  this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                  StringConcatenationClient _client = new StringConcatenationClient() {
                    @Override
                    protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                      _builder.append("this.cell.setType(type);");
                      _builder.newLine();
                      _builder.append("this.cell.setNumGrid(this.numGrid);");
                      _builder.newLine();
                      _builder.append("this.cell.updateResInfo();");
                      _builder.newLine();
                    }
                  };
                  this._jvmTypesBuilder.setBody(it_1, _client);
                };
                JvmOperation _method_1 = this._jvmTypesBuilder.toMethod(meln, "updateCellInfo", _typeRef_3, _function_4);
                this._jvmTypesBuilder.<JvmOperation>operator_add(_members_3, _method_1);
                JvmTypeReference _typeRef_4 = this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.geom.ocltypes.Cell");
                JvmField jvmFieldCell = this._jvmTypesBuilder.toField(meln, "cell", _typeRef_4);
                boolean _notEquals = (!Objects.equal(jvmFieldCell, null));
                if (_notEquals) {
                  jvmFieldCell.setFinal(false);
                  EList<JvmMember> _members_4 = it.getMembers();
                  this._jvmTypesBuilder.<JvmField>operator_add(_members_4, jvmFieldCell);
                }
                JvmTypeReference _typeRef_5 = this._typeReferenceBuilder.typeRef("java.lang.Integer");
                JvmField jvmFieldX = this._jvmTypesBuilder.toField(meln, "x", _typeRef_5);
                boolean _notEquals_1 = (!Objects.equal(jvmFieldX, null));
                if (_notEquals_1) {
                  jvmFieldX.setFinal(false);
                  EList<JvmMember> _members_5 = it.getMembers();
                  this._jvmTypesBuilder.<JvmField>operator_add(_members_5, jvmFieldX);
                }
                JvmTypeReference _typeRef_6 = this._typeReferenceBuilder.typeRef("java.lang.Integer");
                JvmField jvmFieldY = this._jvmTypesBuilder.toField(meln, "y", _typeRef_6);
                boolean _notEquals_2 = (!Objects.equal(jvmFieldY, null));
                if (_notEquals_2) {
                  jvmFieldY.setFinal(false);
                  EList<JvmMember> _members_6 = it.getMembers();
                  this._jvmTypesBuilder.<JvmField>operator_add(_members_6, jvmFieldY);
                }
                JvmTypeReference _typeRef_7 = this._typeReferenceBuilder.typeRef("java.lang.Integer");
                JvmField jvmFieldNum = this._jvmTypesBuilder.toField(meln, "numGrid", _typeRef_7);
                boolean _notEquals_3 = (!Objects.equal(jvmFieldNum, null));
                if (_notEquals_3) {
                  jvmFieldNum.setFinal(false);
                  jvmFieldNum.setStatic(true);
                  EList<JvmMember> _members_7 = it.getMembers();
                  this._jvmTypesBuilder.<JvmField>operator_add(_members_7, jvmFieldNum);
                }
                EList<JvmMember> _members_8 = it.getMembers();
                JvmTypeReference _typeRef_8 = this._typeReferenceBuilder.typeRef(Void.TYPE);
                final Procedure1<JvmOperation> _function_5 = (JvmOperation it_1) -> {
                  EList<JvmFormalParameter> _parameters = it_1.getParameters();
                  JvmTypeReference _typeRef_9 = this._typeReferenceBuilder.typeRef("java.lang.Integer");
                  JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "numGrid", _typeRef_9);
                  this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                  StringConcatenationClient _client = new StringConcatenationClient() {
                    @Override
                    protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                      _builder.append("this.numGrid = numGrid;");
                      _builder.newLine();
                      _builder.append("this.cell.setNumGrid(this.numGrid);");
                      _builder.newLine();
                    }
                  };
                  this._jvmTypesBuilder.setBody(it_1, _client);
                };
                JvmOperation _method_2 = this._jvmTypesBuilder.toMethod(meln, "setNumGrid", _typeRef_8, _function_5);
                this._jvmTypesBuilder.<JvmOperation>operator_add(_members_8, _method_2);
                EList<JvmMember> _members_9 = it.getMembers();
                JvmTypeReference _typeRef_9 = this._typeReferenceBuilder.typeRef(Void.TYPE);
                final Procedure1<JvmOperation> _function_6 = (JvmOperation it_1) -> {
                  EList<JvmFormalParameter> _parameters = it_1.getParameters();
                  JvmTypeReference _typeRef_10 = this._typeReferenceBuilder.typeRef("java.lang.Integer");
                  JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "x", _typeRef_10);
                  this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                  StringConcatenationClient _client = new StringConcatenationClient() {
                    @Override
                    protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                      _builder.append("this.cell.setX(x); ");
                      _builder.newLine();
                    }
                  };
                  this._jvmTypesBuilder.setBody(it_1, _client);
                };
                JvmOperation _method_3 = this._jvmTypesBuilder.toMethod(meln, "setX", _typeRef_9, _function_6);
                this._jvmTypesBuilder.<JvmOperation>operator_add(_members_9, _method_3);
                EList<JvmMember> _members_10 = it.getMembers();
                JvmTypeReference _typeRef_10 = this._typeReferenceBuilder.typeRef(Void.TYPE);
                final Procedure1<JvmOperation> _function_7 = (JvmOperation it_1) -> {
                  EList<JvmFormalParameter> _parameters = it_1.getParameters();
                  JvmTypeReference _typeRef_11 = this._typeReferenceBuilder.typeRef("java.lang.Integer");
                  JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "y", _typeRef_11);
                  this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                  StringConcatenationClient _client = new StringConcatenationClient() {
                    @Override
                    protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                      _builder.append("this.cell.setY(y); ");
                      _builder.newLine();
                    }
                  };
                  this._jvmTypesBuilder.setBody(it_1, _client);
                };
                JvmOperation _method_4 = this._jvmTypesBuilder.toMethod(meln, "setY", _typeRef_10, _function_7);
                this._jvmTypesBuilder.<JvmOperation>operator_add(_members_10, _method_4);
                EList<JvmMember> _members_11 = it.getMembers();
                JvmTypeReference _typeRef_11 = this._typeReferenceBuilder.typeRef("java.lang.Integer");
                final Procedure1<JvmOperation> _function_8 = (JvmOperation it_1) -> {
                  StringConcatenationClient _client = new StringConcatenationClient() {
                    @Override
                    protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                      _builder.append("return this.cell.getX(); ");
                      _builder.newLine();
                    }
                  };
                  this._jvmTypesBuilder.setBody(it_1, _client);
                };
                JvmOperation _method_5 = this._jvmTypesBuilder.toMethod(meln, "getX", _typeRef_11, _function_8);
                this._jvmTypesBuilder.<JvmOperation>operator_add(_members_11, _method_5);
                EList<JvmMember> _members_12 = it.getMembers();
                JvmTypeReference _typeRef_12 = this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.geom.ocltypes.Cell");
                final Procedure1<JvmOperation> _function_9 = (JvmOperation it_1) -> {
                  StringConcatenationClient _client = new StringConcatenationClient() {
                    @Override
                    protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                      _builder.append("return cell;");
                      _builder.newLine();
                    }
                  };
                  this._jvmTypesBuilder.setBody(it_1, _client);
                };
                JvmOperation _method_6 = this._jvmTypesBuilder.toMethod(meln, "getCell", _typeRef_12, _function_9);
                this._jvmTypesBuilder.<JvmOperation>operator_add(_members_12, _method_6);
                EList<JvmMember> _members_13 = it.getMembers();
                JvmTypeReference _typeRef_13 = this._typeReferenceBuilder.typeRef("java.lang.Integer");
                final Procedure1<JvmOperation> _function_10 = (JvmOperation it_1) -> {
                  StringConcatenationClient _client = new StringConcatenationClient() {
                    @Override
                    protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                      _builder.append("return this.cell.getY();");
                      _builder.newLine();
                    }
                  };
                  this._jvmTypesBuilder.setBody(it_1, _client);
                };
                JvmOperation _method_7 = this._jvmTypesBuilder.toMethod(meln, "getY", _typeRef_13, _function_10);
                this._jvmTypesBuilder.<JvmOperation>operator_add(_members_13, _method_7);
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
              final Procedure1<JvmGenericType> _function = (JvmGenericType it) -> {
                EList<JvmTypeReference> _superTypes = it.getSuperTypes();
                JvmTypeReference _type_1 = ((Agregdef)meln).getType();
                JvmTypeReference _type_2 = ((Agregdef)meln).getType();
                JvmTypeReference _typeRef = this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.ocltypes.List", _type_2);
                JvmTypeReference _typeRef_1 = this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.relation.AggregOperator", _type_1, _typeRef);
                this._jvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes, _typeRef_1);
                EList<JvmMember> _members = it.getMembers();
                JvmTypeReference _type_3 = ((Agregdef)meln).getType();
                final Procedure1<JvmOperation> _function_1 = (JvmOperation it_1) -> {
                  EList<JvmFormalParameter> _parameters = it_1.getParameters();
                  JvmTypeReference _type_4 = ((Agregdef)meln).getType();
                  JvmTypeReference _typeRef_2 = this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.ocltypes.List", _type_4);
                  JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "values", _typeRef_2);
                  this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                  EList<JvmFormalParameter> _parameters_1 = it_1.getParameters();
                  JvmTypeReference _type_5 = ((Agregdef)meln).getType();
                  JvmFormalParameter _parameter_1 = this._jvmTypesBuilder.toParameter(meln, "preval", _type_5);
                  this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_1, _parameter_1);
                  XExpression _body = ((Agregdef)meln).getBody();
                  this._jvmTypesBuilder.setBody(it_1, _body);
                };
                JvmOperation _method = this._jvmTypesBuilder.toMethod(meln, "compute", _type_3, _function_1);
                this._jvmTypesBuilder.<JvmOperation>operator_add(_members, _method);
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
            EList<Role> _roles = ((Relation)meln).getRoles();
            int _size = _roles.size();
            boolean _greaterThan = (_size > 2);
            if (_greaterThan) {
              InputOutput.<String>println("Sorry, only graphs with two roles are supported by this version. The two first roles will be used and the others will be ignored.");
            }
            boolean _and = false;
            boolean _and_1 = false;
            boolean _and_2 = false;
            boolean _and_3 = false;
            boolean _and_4 = false;
            boolean _and_5 = false;
            boolean _and_6 = false;
            boolean _and_7 = false;
            EList<Role> _roles_1 = ((Relation)meln).getRoles();
            int _size_1 = _roles_1.size();
            boolean _greaterEqualsThan = (_size_1 >= 2);
            if (!_greaterEqualsThan) {
              _and_7 = false;
            } else {
              EList<Role> _roles_2 = ((Relation)meln).getRoles();
              Role _get = _roles_2.get(0);
              boolean _notEquals = (!Objects.equal(_get, null));
              _and_7 = _notEquals;
            }
            if (!_and_7) {
              _and_6 = false;
            } else {
              EList<Role> _roles_3 = ((Relation)meln).getRoles();
              Role _get_1 = _roles_3.get(1);
              boolean _notEquals_1 = (!Objects.equal(_get_1, null));
              _and_6 = _notEquals_1;
            }
            if (!_and_6) {
              _and_5 = false;
            } else {
              EList<Role> _roles_4 = ((Relation)meln).getRoles();
              Role _get_2 = _roles_4.get(0);
              Entity _type = _get_2.getType();
              boolean _notEquals_2 = (!Objects.equal(_type, null));
              _and_5 = _notEquals_2;
            }
            if (!_and_5) {
              _and_4 = false;
            } else {
              EList<Role> _roles_5 = ((Relation)meln).getRoles();
              Role _get_3 = _roles_5.get(1);
              Entity _type_1 = _get_3.getType();
              boolean _notEquals_3 = (!Objects.equal(_type_1, null));
              _and_4 = _notEquals_3;
            }
            if (!_and_4) {
              _and_3 = false;
            } else {
              EList<Role> _roles_6 = ((Relation)meln).getRoles();
              Role _get_4 = _roles_6.get(0);
              Entity _type_2 = _get_4.getType();
              QualifiedName _fullyQualifiedName = this._iQualifiedNameProvider.getFullyQualifiedName(_type_2);
              boolean _notEquals_4 = (!Objects.equal(_fullyQualifiedName, null));
              _and_3 = _notEquals_4;
            }
            if (!_and_3) {
              _and_2 = false;
            } else {
              EList<Role> _roles_7 = ((Relation)meln).getRoles();
              Role _get_5 = _roles_7.get(1);
              Entity _type_3 = _get_5.getType();
              QualifiedName _fullyQualifiedName_1 = this._iQualifiedNameProvider.getFullyQualifiedName(_type_3);
              boolean _notEquals_5 = (!Objects.equal(_fullyQualifiedName_1, null));
              _and_2 = _notEquals_5;
            }
            if (!_and_2) {
              _and_1 = false;
            } else {
              EList<Role> _roles_8 = ((Relation)meln).getRoles();
              Role _get_6 = _roles_8.get(0);
              String _name_2 = _get_6.getName();
              boolean _notEquals_6 = (!Objects.equal(_name_2, null));
              _and_1 = _notEquals_6;
            }
            if (!_and_1) {
              _and = false;
            } else {
              EList<Role> _roles_9 = ((Relation)meln).getRoles();
              Role _get_7 = _roles_9.get(1);
              String _name_3 = _get_7.getName();
              boolean _notEquals_7 = (!Objects.equal(_name_3, null));
              _and = _notEquals_7;
            }
            if (_and) {
              EList<Role> _roles_10 = ((Relation)meln).getRoles();
              Role _get_8 = _roles_10.get(0);
              Entity _type_4 = _get_8.getType();
              EList<Role> _roles_11 = ((Relation)meln).getRoles();
              Role _get_9 = _roles_11.get(1);
              Entity _type_5 = _get_9.getType();
              final boolean isAutoGraph = _type_4.equals(_type_5);
              boolean isCellGraph = false;
              boolean isCellGeomGraph = false;
              boolean testCell1 = false;
              boolean testCell2 = false;
              boolean testGeom1 = false;
              boolean testGeom2 = false;
              EList<Role> _roles_12 = ((Relation)meln).getRoles();
              Role _get_10 = _roles_12.get(0);
              final Entity rol1 = _get_10.getType();
              EList<Role> _roles_13 = ((Relation)meln).getRoles();
              Role _get_11 = _roles_13.get(1);
              final Entity rol2 = _get_11.getType();
              InputOutput.<String>print("test ok");
              EList<EObject> _eContents = rol1.eContents();
              for (final EObject e : _eContents) {
                boolean _matched_1 = false;
                if (!_matched_1) {
                  if (e instanceof PropertyDef) {
                    _matched_1=true;
                    JvmTypeReference _type_6 = ((PropertyDef)e).getType();
                    String _simpleName = _type_6.getSimpleName();
                    boolean _equals_1 = _simpleName.equals("Cell");
                    if (_equals_1) {
                      testCell1 = true;
                    }
                    boolean _or = false;
                    boolean _or_1 = false;
                    boolean _or_2 = false;
                    boolean _or_3 = false;
                    boolean _or_4 = false;
                    boolean _or_5 = false;
                    JvmTypeReference _type_7 = ((PropertyDef)e).getType();
                    String _simpleName_1 = _type_7.getSimpleName();
                    boolean _equals_2 = _simpleName_1.equals("Line");
                    if (_equals_2) {
                      _or_5 = true;
                    } else {
                      JvmTypeReference _type_8 = ((PropertyDef)e).getType();
                      String _simpleName_2 = _type_8.getSimpleName();
                      boolean _equals_3 = _simpleName_2.equals("MultiLine");
                      _or_5 = _equals_3;
                    }
                    if (_or_5) {
                      _or_4 = true;
                    } else {
                      JvmTypeReference _type_9 = ((PropertyDef)e).getType();
                      String _simpleName_3 = _type_9.getSimpleName();
                      boolean _equals_4 = _simpleName_3.equals("Polygon");
                      _or_4 = _equals_4;
                    }
                    if (_or_4) {
                      _or_3 = true;
                    } else {
                      JvmTypeReference _type_10 = ((PropertyDef)e).getType();
                      String _simpleName_4 = _type_10.getSimpleName();
                      boolean _equals_5 = _simpleName_4.equals("MultiPolygon");
                      _or_3 = _equals_5;
                    }
                    if (_or_3) {
                      _or_2 = true;
                    } else {
                      JvmTypeReference _type_11 = ((PropertyDef)e).getType();
                      String _simpleName_5 = _type_11.getSimpleName();
                      boolean _equals_6 = _simpleName_5.equals("Point");
                      _or_2 = _equals_6;
                    }
                    if (_or_2) {
                      _or_1 = true;
                    } else {
                      JvmTypeReference _type_12 = ((PropertyDef)e).getType();
                      String _simpleName_6 = _type_12.getSimpleName();
                      boolean _equals_7 = _simpleName_6.equals("MultiPoint");
                      _or_1 = _equals_7;
                    }
                    if (_or_1) {
                      _or = true;
                    } else {
                      JvmTypeReference _type_13 = ((PropertyDef)e).getType();
                      String _simpleName_7 = _type_13.getSimpleName();
                      boolean _equals_8 = _simpleName_7.equals("Ring");
                      _or = _equals_8;
                    }
                    if (_or) {
                      testGeom1 = true;
                    }
                  }
                }
              }
              EList<EObject> _eContents_1 = rol2.eContents();
              for (final EObject e_1 : _eContents_1) {
                boolean _matched_2 = false;
                if (!_matched_2) {
                  if (e_1 instanceof PropertyDef) {
                    _matched_2=true;
                    JvmTypeReference _type_6 = ((PropertyDef)e_1).getType();
                    String _simpleName = _type_6.getSimpleName();
                    boolean _equals_1 = _simpleName.equals("Cell");
                    if (_equals_1) {
                      testCell2 = true;
                    }
                    boolean _or = false;
                    boolean _or_1 = false;
                    boolean _or_2 = false;
                    boolean _or_3 = false;
                    boolean _or_4 = false;
                    boolean _or_5 = false;
                    JvmTypeReference _type_7 = ((PropertyDef)e_1).getType();
                    String _simpleName_1 = _type_7.getSimpleName();
                    boolean _equals_2 = _simpleName_1.equals("Line");
                    if (_equals_2) {
                      _or_5 = true;
                    } else {
                      JvmTypeReference _type_8 = ((PropertyDef)e_1).getType();
                      String _simpleName_2 = _type_8.getSimpleName();
                      boolean _equals_3 = _simpleName_2.equals("MultiLine");
                      _or_5 = _equals_3;
                    }
                    if (_or_5) {
                      _or_4 = true;
                    } else {
                      JvmTypeReference _type_9 = ((PropertyDef)e_1).getType();
                      String _simpleName_3 = _type_9.getSimpleName();
                      boolean _equals_4 = _simpleName_3.equals("Polygon");
                      _or_4 = _equals_4;
                    }
                    if (_or_4) {
                      _or_3 = true;
                    } else {
                      JvmTypeReference _type_10 = ((PropertyDef)e_1).getType();
                      String _simpleName_4 = _type_10.getSimpleName();
                      boolean _equals_5 = _simpleName_4.equals("MultiPolygon");
                      _or_3 = _equals_5;
                    }
                    if (_or_3) {
                      _or_2 = true;
                    } else {
                      JvmTypeReference _type_11 = ((PropertyDef)e_1).getType();
                      String _simpleName_5 = _type_11.getSimpleName();
                      boolean _equals_6 = _simpleName_5.equals("Point");
                      _or_2 = _equals_6;
                    }
                    if (_or_2) {
                      _or_1 = true;
                    } else {
                      JvmTypeReference _type_12 = ((PropertyDef)e_1).getType();
                      String _simpleName_6 = _type_12.getSimpleName();
                      boolean _equals_7 = _simpleName_6.equals("MultiPoint");
                      _or_1 = _equals_7;
                    }
                    if (_or_1) {
                      _or = true;
                    } else {
                      JvmTypeReference _type_13 = ((PropertyDef)e_1).getType();
                      String _simpleName_7 = _type_13.getSimpleName();
                      boolean _equals_8 = _simpleName_7.equals("Ring");
                      _or = _equals_8;
                    }
                    if (_or) {
                      testGeom2 = true;
                    }
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
              final JvmTypeReference aggregType = this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.raster.CellAggregOperator");
              final JvmTypeReference listype = this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.ocltypes.List", aggregType);
              if (isCellGeomGraph) {
                final JvmTypeReference gridType = this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.raster.Grid");
                EList<Role> _roles_14 = ((Relation)meln).getRoles();
                final Role firstRole = _roles_14.get(0);
                EList<Role> _roles_15 = ((Relation)meln).getRoles();
                final Role secondRole = _roles_15.get(1);
                Entity _type_6 = firstRole.getType();
                QualifiedName _fullyQualifiedName_2 = this._iQualifiedNameProvider.getFullyQualifiedName(_type_6);
                String _string = _fullyQualifiedName_2.toString();
                JvmTypeReference tempcellType = this._typeReferenceBuilder.typeRef(_string);
                Entity _type_7 = secondRole.getType();
                QualifiedName _fullyQualifiedName_3 = this._iQualifiedNameProvider.getFullyQualifiedName(_type_7);
                String _string_1 = _fullyQualifiedName_3.toString();
                JvmTypeReference tempgeomType = this._typeReferenceBuilder.typeRef(_string_1);
                String tempCellName = firstRole.getName();
                String tempGeomName = secondRole.getName();
                if (testCell2) {
                  Entity _type_8 = secondRole.getType();
                  QualifiedName _fullyQualifiedName_4 = this._iQualifiedNameProvider.getFullyQualifiedName(_type_8);
                  String _string_2 = _fullyQualifiedName_4.toString();
                  JvmTypeReference _typeRef = this._typeReferenceBuilder.typeRef(_string_2);
                  tempcellType = _typeRef;
                  String _name_4 = secondRole.getName();
                  tempCellName = _name_4;
                  Entity _type_9 = firstRole.getType();
                  QualifiedName _fullyQualifiedName_5 = this._iQualifiedNameProvider.getFullyQualifiedName(_type_9);
                  String _string_3 = _fullyQualifiedName_5.toString();
                  JvmTypeReference _typeRef_1 = this._typeReferenceBuilder.typeRef(_string_3);
                  tempgeomType = _typeRef_1;
                  String _name_5 = firstRole.getName();
                  tempGeomName = _name_5;
                }
                final JvmTypeReference cellType = tempcellType;
                final JvmTypeReference geomType = tempgeomType;
                final String cellName = tempCellName;
                final String geomName = tempGeomName;
                JvmGenericType _class = this._jvmTypesBuilder.toClass(modl, edgecname);
                final Procedure1<JvmGenericType> _function = (JvmGenericType it) -> {
                  EList<JvmTypeReference> _superTypes = it.getSuperTypes();
                  JvmTypeReference _typeRef_2 = this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.relation.GeomCellEdge", cellType, geomType);
                  this._jvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes, _typeRef_2);
                  JvmField jvmField = this._jvmTypesBuilder.toField(meln, cellName, cellType);
                  boolean _notEquals_8 = (!Objects.equal(jvmField, null));
                  if (_notEquals_8) {
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
                  JvmField _field = this._jvmTypesBuilder.toField(meln, geomName, geomType);
                  jvmField = _field;
                  boolean _notEquals_9 = (!Objects.equal(jvmField, null));
                  if (_notEquals_9) {
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
                    JvmTypeReference _typeRef_3 = this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.raster.Grid");
                    JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "grid", _typeRef_3);
                    this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                    EList<JvmFormalParameter> _parameters_1 = it_1.getParameters();
                    JvmTypeReference _typeRef_4 = this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.ocltypes.List", geomType);
                    JvmFormalParameter _parameter_1 = this._jvmTypesBuilder.toParameter(meln, "geom", _typeRef_4);
                    this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_1, _parameter_1);
                    StringConcatenationClient _client = new StringConcatenationClient() {
                      @Override
                      protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                        _builder.append("super(grid, geom);  ");
                        _builder.newLine();
                        _builder.append("this.");
                        _builder.append(cellName, "");
                        _builder.append(" = new ");
                        _builder.append(cellType, "");
                        _builder.append("();");
                        _builder.newLineIfNotEmpty();
                        _builder.append(cellName, "");
                        _builder.append(".updateCellInfo(getCellType());");
                        _builder.newLineIfNotEmpty();
                      }
                    };
                    this._jvmTypesBuilder.setBody(it_1, _client);
                  };
                  JvmConstructor _constructor = this._jvmTypesBuilder.toConstructor(meln, _function_1);
                  this._jvmTypesBuilder.<JvmConstructor>operator_add(_members_6, _constructor);
                  EList<JvmMember> _members_7 = it.getMembers();
                  JvmTypeReference _typeRef_3 = this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.relation.OcltRole");
                  final Procedure1<JvmOperation> _function_2 = (JvmOperation it_1) -> {
                    EList<JvmFormalParameter> _parameters = it_1.getParameters();
                    JvmTypeReference _typeRef_4 = this._typeReferenceBuilder.typeRef("int");
                    JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "i", _typeRef_4);
                    this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                    StringConcatenationClient _client = new StringConcatenationClient() {
                      @Override
                      protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                        _builder.append("if (i==0) return ");
                        _builder.append(cellName, "");
                        _builder.append(";");
                        _builder.newLineIfNotEmpty();
                        _builder.append("else if (i==1) return ");
                        _builder.append(geomName, "");
                        _builder.append(";");
                        _builder.newLineIfNotEmpty();
                        _builder.append("else return null;");
                        _builder.newLine();
                      }
                    };
                    this._jvmTypesBuilder.setBody(it_1, _client);
                  };
                  JvmOperation _method = this._jvmTypesBuilder.toMethod(meln, "getRole", _typeRef_3, _function_2);
                  this._jvmTypesBuilder.<JvmOperation>operator_add(_members_7, _method);
                  EList<JvmMember> _members_8 = it.getMembers();
                  JvmTypeReference _typeRef_4 = this._typeReferenceBuilder.typeRef(Void.TYPE);
                  final Procedure1<JvmOperation> _function_3 = (JvmOperation it_1) -> {
                    StringConcatenationClient _client = new StringConcatenationClient() {
                      @Override
                      protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                        _builder.append("this.");
                        _builder.append(cellName, "");
                        _builder.append(".setX(getX());");
                        _builder.newLineIfNotEmpty();
                        _builder.append("          \t\t        ");
                        _builder.append("this.");
                        _builder.append(cellName, "          \t\t        ");
                        _builder.append(".setY(getY());");
                        _builder.newLineIfNotEmpty();
                        _builder.append("          \t\t\t    ");
                        _builder.append("this. ");
                        _builder.append(geomName, "          \t\t\t    ");
                        _builder.append(" = getGeomEntity();");
                        _builder.newLineIfNotEmpty();
                      }
                    };
                    this._jvmTypesBuilder.setBody(it_1, _client);
                  };
                  JvmOperation _method_1 = this._jvmTypesBuilder.toMethod(meln, "update", _typeRef_4, _function_3);
                  this._jvmTypesBuilder.<JvmOperation>operator_add(_members_8, _method_1);
                  EList<RelElements> _relelns = ((Relation)meln).getRelelns();
                  for (final RelElements reln : _relelns) {
                    boolean _matched_3 = false;
                    if (!_matched_3) {
                      if (reln instanceof InteractionDef) {
                        _matched_3=true;
                        EList<JvmMember> _members_9 = it.getMembers();
                        String _name_6 = ((InteractionDef)reln).getName();
                        JvmTypeReference _typeRef_5 = this._typeReferenceBuilder.typeRef(Void.TYPE);
                        final Procedure1<JvmOperation> _function_4 = (JvmOperation it_1) -> {
                          EList<JvmFormalParameter> _params = ((InteractionDef)reln).getParams();
                          for (final JvmFormalParameter p : _params) {
                            EList<JvmFormalParameter> _parameters = it_1.getParameters();
                            String _name_7 = p.getName();
                            JvmTypeReference _parameterType = p.getParameterType();
                            JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(reln, _name_7, _parameterType);
                            this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                          }
                          XExpression _body = ((InteractionDef)reln).getBody();
                          this._jvmTypesBuilder.setBody(it_1, _body);
                        };
                        JvmOperation _method_2 = this._jvmTypesBuilder.toMethod(reln, _name_6, _typeRef_5, _function_4);
                        this._jvmTypesBuilder.<JvmOperation>operator_add(_members_9, _method_2);
                        EList<Comitexpr> _comitexpressions = ((InteractionDef)reln).getComitexpressions();
                        int _size_2 = _comitexpressions.size();
                        boolean _greaterThan_1 = (_size_2 > 0);
                        if (_greaterThan_1) {
                          EList<JvmMember> _members_10 = it.getMembers();
                          String _name_7 = ((InteractionDef)reln).getName();
                          String _plus_1 = ("get_agr_" + _name_7);
                          final Procedure1<JvmOperation> _function_5 = (JvmOperation it_1) -> {
                            StringConcatenationClient _client = new StringConcatenationClient() {
                              @Override
                              protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                int index = 0;
                                _builder.newLineIfNotEmpty();
                                _builder.append(listype, "");
                                _builder.append(" cvtList = new ");
                                _builder.append(listype, "");
                                _builder.append("();");
                                _builder.newLineIfNotEmpty();
                                {
                                  EList<Comitexpr> _comitexpressions = ((InteractionDef)reln).getComitexpressions();
                                  for(final Comitexpr ce : _comitexpressions) {
                                    {
                                      Role _rol = ce.getRol();
                                      Entity _type = _rol.getType();
                                      QualifiedName _fullyQualifiedName = OceletJvmModelInferrer.this._iQualifiedNameProvider.getFullyQualifiedName(_type);
                                      String _string = _fullyQualifiedName.toString();
                                      String _qualifiedName = cellType.getQualifiedName();
                                      String _string_1 = _qualifiedName.toString();
                                      boolean _equals = _string.equals(_string_1);
                                      if (_equals) {
                                        _builder.append("          \t  \t      \t  ");
                                        _builder.append(aggregType, "          \t  \t      \t  ");
                                        _builder.append(" cvt");
                                        _builder.append(index, "          \t  \t      \t  ");
                                        _builder.append(" = new ");
                                        _builder.append(aggregType, "          \t  \t      \t  ");
                                        _builder.append("();");
                                        _builder.newLineIfNotEmpty();
                                        _builder.append("          \t  \t      \t  \t \t");
                                        _builder.append("cvt");
                                        _builder.append(index, "          \t  \t      \t  \t \t");
                                        _builder.append(".setName(\"");
                                        String _prop = ce.getProp();
                                        _builder.append(_prop, "          \t  \t      \t  \t \t");
                                        _builder.append("\"); ");
                                        _builder.newLineIfNotEmpty();
                                        _builder.append("          \t  \t      \t  \t \t");
                                        _builder.append("cvt");
                                        _builder.append(index, "          \t  \t      \t  \t \t");
                                        _builder.append(".setOperator(new  ");
                                        JvmTypeReference _agrfunc = ce.getAgrfunc();
                                        _builder.append(_agrfunc, "          \t  \t      \t  \t \t");
                                        _builder.append("());");
                                        _builder.newLineIfNotEmpty();
                                        _builder.append("          \t  \t      \t  \t \t");
                                        _builder.append("cvtList.add(cvt");
                                        _builder.append(index, "          \t  \t      \t  \t \t");
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
                                  }
                                }
                                _builder.append("           \t  \t      \t  ");
                                _builder.append("return cvtList;");
                                _builder.newLine();
                              }
                            };
                            this._jvmTypesBuilder.setBody(it_1, _client);
                          };
                          JvmOperation _method_3 = this._jvmTypesBuilder.toMethod(reln, _plus_1, listype, _function_5);
                          this._jvmTypesBuilder.<JvmOperation>operator_add(_members_10, _method_3);
                          EList<JvmMember> _members_11 = it.getMembers();
                          String _name_8 = ((InteractionDef)reln).getName();
                          String _plus_2 = ("_agr_" + _name_8);
                          JvmTypeReference _typeRef_6 = this._typeReferenceBuilder.typeRef(Void.TYPE);
                          final Procedure1<JvmOperation> _function_6 = (JvmOperation it_1) -> {
                            StringConcatenationClient _client = new StringConcatenationClient() {
                              @Override
                              protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                _builder.newLine();
                                {
                                  EList<Comitexpr> _comitexpressions = ((InteractionDef)reln).getComitexpressions();
                                  for(final Comitexpr ce : _comitexpressions) {
                                    Role _rol = ce.getRol();
                                    Entity _type = _rol.getType();
                                    QualifiedName _fullyQualifiedName = OceletJvmModelInferrer.this._iQualifiedNameProvider.getFullyQualifiedName(_type);
                                    final String t1 = _fullyQualifiedName.toString();
                                    _builder.newLineIfNotEmpty();
                                    String _qualifiedName = cellType.getQualifiedName();
                                    final String t2 = _qualifiedName.toString();
                                    _builder.newLineIfNotEmpty();
                                    {
                                      boolean _equals = t1.equals(t2);
                                      boolean _not = (!_equals);
                                      if (_not) {
                                        _builder.append("\t");
                                        _builder.append("this.");
                                        Role _rol_1 = ce.getRol();
                                        String _name = _rol_1.getName();
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
                          JvmOperation _method_4 = this._jvmTypesBuilder.toMethod(reln, _plus_2, _typeRef_6, _function_6);
                          this._jvmTypesBuilder.<JvmOperation>operator_add(_members_11, _method_4);
                        } else {
                          EList<JvmMember> _members_12 = it.getMembers();
                          String _name_9 = ((InteractionDef)reln).getName();
                          String _plus_3 = ("get_agr_" + _name_9);
                          final Procedure1<JvmOperation> _function_7 = (JvmOperation it_1) -> {
                            StringConcatenationClient _client = new StringConcatenationClient() {
                              @Override
                              protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                _builder.append("return null;");
                                _builder.newLine();
                              }
                            };
                            this._jvmTypesBuilder.setBody(it_1, _client);
                          };
                          JvmOperation _method_5 = this._jvmTypesBuilder.toMethod(reln, _plus_3, listype, _function_7);
                          this._jvmTypesBuilder.<JvmOperation>operator_add(_members_12, _method_5);
                        }
                      }
                    }
                  }
                };
                acceptor.<JvmGenericType>accept(_class, _function);
                EList<RelElements> _relelns = ((Relation)meln).getRelelns();
                for (final RelElements reln : _relelns) {
                  boolean _matched_3 = false;
                  if (!_matched_3) {
                    if (reln instanceof Filterdef) {
                      _matched_3=true;
                      String _plus_1 = (graphcname + "_");
                      String _name_6 = ((Filterdef)reln).getName();
                      final String filterfqn = (_plus_1 + _name_6);
                      JvmGenericType _class_1 = this._jvmTypesBuilder.toClass(modl, filterfqn);
                      final Procedure1<JvmGenericType> _function_1 = (JvmGenericType it) -> {
                        Entity _type_10 = firstRole.getType();
                        QualifiedName _fullyQualifiedName_6 = this._iQualifiedNameProvider.getFullyQualifiedName(_type_10);
                        String _string_4 = _fullyQualifiedName_6.toString();
                        final JvmTypeReference firstRoleType = this._typeReferenceBuilder.typeRef(_string_4);
                        Entity _type_11 = secondRole.getType();
                        QualifiedName _fullyQualifiedName_7 = this._iQualifiedNameProvider.getFullyQualifiedName(_type_11);
                        String _string_5 = _fullyQualifiedName_7.toString();
                        final JvmTypeReference secondRoleType = this._typeReferenceBuilder.typeRef(_string_5);
                        EList<JvmTypeReference> _superTypes = it.getSuperTypes();
                        JvmTypeReference _typeRef_2 = this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.relation.EdgeFilter", firstRoleType, secondRoleType);
                        this._jvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes, _typeRef_2);
                        EList<JvmFormalParameter> _params = ((Filterdef)reln).getParams();
                        for (final JvmFormalParameter p : _params) {
                          {
                            String _name_7 = p.getName();
                            JvmTypeReference _parameterType = p.getParameterType();
                            final JvmField pfield = this._jvmTypesBuilder.toField(reln, _name_7, _parameterType);
                            boolean _notEquals_8 = (!Objects.equal(pfield, null));
                            if (_notEquals_8) {
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
                            String _name_7 = p_1.getName();
                            JvmTypeReference _parameterType = p_1.getParameterType();
                            JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(reln, _name_7, _parameterType);
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
                                  _builder.append(_name, "");
                                  _builder.append(" = ");
                                  String _name_1 = p.getName();
                                  _builder.append(_name_1, "");
                                  _builder.append(";");
                                  _builder.newLineIfNotEmpty();
                                }
                              }
                              _builder.newLine();
                            }
                          };
                          this._jvmTypesBuilder.setBody(it_1, _client);
                        };
                        JvmConstructor _constructor = this._jvmTypesBuilder.toConstructor(reln, _function_2);
                        this._jvmTypesBuilder.<JvmConstructor>operator_add(_members, _constructor);
                        EList<JvmMember> _members_1 = it.getMembers();
                        JvmTypeReference _typeRef_3 = this._typeReferenceBuilder.typeRef("java.lang.Boolean");
                        final Procedure1<JvmOperation> _function_3 = (JvmOperation it_1) -> {
                          EList<JvmFormalParameter> _parameters = it_1.getParameters();
                          String _name_7 = firstRole.getName();
                          JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(reln, _name_7, firstRoleType);
                          this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                          EList<JvmFormalParameter> _parameters_1 = it_1.getParameters();
                          String _name_8 = secondRole.getName();
                          JvmFormalParameter _parameter_1 = this._jvmTypesBuilder.toParameter(reln, _name_8, secondRoleType);
                          this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_1, _parameter_1);
                          XExpression _body = ((Filterdef)reln).getBody();
                          this._jvmTypesBuilder.setBody(it_1, _body);
                        };
                        JvmOperation _method = this._jvmTypesBuilder.toMethod(reln, "filter", _typeRef_3, _function_3);
                        this._jvmTypesBuilder.<JvmOperation>operator_add(_members_1, _method);
                      };
                      acceptor.<JvmGenericType>accept(_class_1, _function_1);
                    }
                  }
                }
                JvmTypeReference _typeRef_2 = this._typeReferenceBuilder.typeRef(edgecname);
                boolean _notEquals_8 = (!Objects.equal(_typeRef_2, null));
                if (_notEquals_8) {
                  JvmGenericType _class_1 = this._jvmTypesBuilder.toClass(modl, graphcname);
                  final Procedure1<JvmGenericType> _function_1 = (JvmGenericType it) -> {
                    String _documentation = this._jvmTypesBuilder.getDocumentation(meln);
                    this._jvmTypesBuilder.setDocumentation(it, _documentation);
                    EList<JvmTypeReference> _superTypes = it.getSuperTypes();
                    JvmTypeReference _typeRef_3 = this._typeReferenceBuilder.typeRef(edgecname);
                    JvmTypeReference _typeRef_4 = this._typeReferenceBuilder.typeRef(graphTypeName, _typeRef_3, cellType, geomType);
                    this._jvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes, _typeRef_4);
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
                    EList<JvmMember> _members_1 = it.getMembers();
                    JvmTypeReference _typeRef_5 = this._typeReferenceBuilder.typeRef(Void.TYPE);
                    final Procedure1<JvmOperation> _function_3 = (JvmOperation it_1) -> {
                      EList<JvmFormalParameter> _parameters = it_1.getParameters();
                      JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "grid", gridType);
                      this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                      EList<JvmFormalParameter> _parameters_1 = it_1.getParameters();
                      JvmFormalParameter _parameter_1 = this._jvmTypesBuilder.toParameter(meln, "geom", geomList);
                      this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_1, _parameter_1);
                      StringConcatenationClient _client = new StringConcatenationClient() {
                        @Override
                        protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                          JvmTypeReference _typeRef = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef(edgecname);
                          _builder.append(_typeRef, "");
                          _builder.append(" _gen_edge = new ");
                          String _name = ((Relation)meln).getName();
                          String _plus = (_name + "_Edge");
                          _builder.append(_plus, "");
                          _builder.append("(grid, geom);");
                          _builder.newLineIfNotEmpty();
                          _builder.append("     \t\t  \t  ");
                          _builder.append("setCompleteIteratorGeomCell(_gen_edge);");
                          _builder.newLine();
                        }
                      };
                      this._jvmTypesBuilder.setBody(it_1, _client);
                    };
                    JvmOperation _method = this._jvmTypesBuilder.toMethod(meln, "connect", _typeRef_5, _function_3);
                    this._jvmTypesBuilder.<JvmOperation>operator_add(_members_1, _method);
                    EList<RelElements> _relelns_1 = ((Relation)meln).getRelelns();
                    for (final RelElements reln_1 : _relelns_1) {
                      boolean _matched_4 = false;
                      if (!_matched_4) {
                        if (reln_1 instanceof InteractionDef) {
                          _matched_4=true;
                          EList<JvmMember> _members_2 = it.getMembers();
                          String _name_6 = ((InteractionDef)reln_1).getName();
                          JvmTypeReference _typeRef_6 = this._typeReferenceBuilder.typeRef(Void.TYPE);
                          final Procedure1<JvmOperation> _function_4 = (JvmOperation it_1) -> {
                            EList<JvmFormalParameter> _params = ((InteractionDef)reln_1).getParams();
                            for (final JvmFormalParameter p : _params) {
                              EList<JvmFormalParameter> _parameters = it_1.getParameters();
                              String _name_7 = p.getName();
                              JvmTypeReference _parameterType = p.getParameterType();
                              JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(reln_1, _name_7, _parameterType);
                              this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                            }
                            StringConcatenationClient _client = new StringConcatenationClient() {
                              @Override
                              protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                _builder.append("setMode(2);");
                                _builder.newLine();
                                _builder.append("cleanOperator();");
                                _builder.newLine();
                                _builder.append(listype, "");
                                _builder.append(" cvtList = ((");
                                JvmTypeReference _typeRef = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef(edgecname);
                                _builder.append(_typeRef, "");
                                _builder.append(")getEdge()).get_agr_");
                                String _name = ((InteractionDef)reln_1).getName();
                                _builder.append(_name, "");
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
                                _builder.append("for(");
                                JvmTypeReference _typeRef_1 = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef(edgecname);
                                _builder.append(_typeRef_1, "");
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
                                  EList<Comitexpr> _comitexpressions = ((InteractionDef)reln_1).getComitexpressions();
                                  int _size = _comitexpressions.size();
                                  boolean _greaterThan = (_size > 0);
                                  if (_greaterThan) {
                                    boolean test = false;
                                    _builder.newLineIfNotEmpty();
                                    {
                                      EList<Comitexpr> _comitexpressions_1 = ((InteractionDef)reln_1).getComitexpressions();
                                      for(final Comitexpr ce : _comitexpressions_1) {
                                        {
                                          Role _rol = ce.getRol();
                                          Entity _type = _rol.getType();
                                          boolean _equals = _type.equals(cellType);
                                          boolean _not = (!_equals);
                                          if (_not) {
                                            Object _xblockexpression_1 = null;
                                            {
                                              test = true;
                                              _xblockexpression_1 = null;
                                            }
                                            _builder.append(_xblockexpression_1, "");
                                            _builder.newLineIfNotEmpty();
                                          }
                                        }
                                      }
                                    }
                                    {
                                      if (test = true) {
                                        _builder.append("          \t  \t      \t   \t\t");
                                        _builder.append("_edg_._agr_");
                                        String _name_3 = ((InteractionDef)reln_1).getName();
                                        _builder.append(_name_3, "          \t  \t      \t   \t\t");
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
                              }
                            };
                            this._jvmTypesBuilder.setBody(it_1, _client);
                          };
                          JvmOperation _method_1 = this._jvmTypesBuilder.toMethod(reln_1, _name_6, _typeRef_6, _function_4);
                          this._jvmTypesBuilder.<JvmOperation>operator_add(_members_2, _method_1);
                        }
                      }
                      if (!_matched_4) {
                        if (reln_1 instanceof Filterdef) {
                          _matched_4=true;
                          EList<JvmMember> _members_2 = it.getMembers();
                          String _name_6 = ((Filterdef)reln_1).getName();
                          String _string_4 = graphcname.toString();
                          JvmTypeReference _typeRef_6 = this._typeReferenceBuilder.typeRef(_string_4);
                          final Procedure1<JvmOperation> _function_4 = (JvmOperation it_1) -> {
                            EList<JvmFormalParameter> _params = ((Filterdef)reln_1).getParams();
                            for (final JvmFormalParameter p : _params) {
                              EList<JvmFormalParameter> _parameters = it_1.getParameters();
                              String _name_7 = p.getName();
                              JvmTypeReference _parameterType = p.getParameterType();
                              JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(reln_1, _name_7, _parameterType);
                              this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                            }
                            StringConcatenationClient _client = new StringConcatenationClient() {
                              @Override
                              protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                String _name = ((Relation)meln).getName();
                                String _plus = (_name + "_");
                                String _name_1 = ((Filterdef)reln_1).getName();
                                String _plus_1 = (_plus + _name_1);
                                _builder.append(_plus_1, "");
                                _builder.append(" _filter = new ");
                                String _name_2 = ((Relation)meln).getName();
                                String _plus_2 = (_name_2 + "_");
                                String _name_3 = ((Filterdef)reln_1).getName();
                                String _plus_3 = (_plus_2 + _name_3);
                                _builder.append(_plus_3, "");
                                _builder.append("(");
                                _builder.newLineIfNotEmpty();
                                {
                                  EList<JvmFormalParameter> _params = ((Filterdef)reln_1).getParams();
                                  int _size = _params.size();
                                  boolean _greaterThan = (_size > 0);
                                  if (_greaterThan) {
                                    {
                                      EList<JvmFormalParameter> _params_1 = ((Filterdef)reln_1).getParams();
                                      int _size_1 = _params_1.size();
                                      int _minus = (_size_1 - 1);
                                      IntegerRange _upTo = new IntegerRange(0, _minus);
                                      for(final Integer i : _upTo) {
                                        EList<JvmFormalParameter> _params_2 = ((Filterdef)reln_1).getParams();
                                        JvmFormalParameter _get = _params_2.get((i).intValue());
                                        String _name_4 = _get.getName();
                                        _builder.append(_name_4, "");
                                        _builder.newLineIfNotEmpty();
                                        {
                                          EList<JvmFormalParameter> _params_3 = ((Filterdef)reln_1).getParams();
                                          int _size_2 = _params_3.size();
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
                          JvmOperation _method_1 = this._jvmTypesBuilder.toMethod(reln_1, _name_6, _typeRef_6, _function_4);
                          this._jvmTypesBuilder.<JvmOperation>operator_add(_members_2, _method_1);
                        }
                      }
                    }
                  };
                  acceptor.<JvmGenericType>accept(_class_1, _function_1);
                }
              } else {
                if (isCellGraph) {
                  final JvmTypeReference gridType_1 = this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.raster.Grid");
                  EList<Role> _roles_16 = ((Relation)meln).getRoles();
                  final Role firstRole_1 = _roles_16.get(0);
                  EList<Role> _roles_17 = ((Relation)meln).getRoles();
                  final Role secondRole_1 = _roles_17.get(1);
                  Entity _type_10 = firstRole_1.getType();
                  QualifiedName _fullyQualifiedName_6 = this._iQualifiedNameProvider.getFullyQualifiedName(_type_10);
                  String _string_4 = _fullyQualifiedName_6.toString();
                  final JvmTypeReference roleType = this._typeReferenceBuilder.typeRef(_string_4);
                  if ((!isAutoGraph)) {
                    JvmGenericType _class_2 = this._jvmTypesBuilder.toClass(modl, edgecname);
                    final Procedure1<JvmGenericType> _function_2 = (JvmGenericType it) -> {
                      EList<JvmTypeReference> _superTypes = it.getSuperTypes();
                      JvmTypeReference _typeRef_3 = this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.relation.DiCursorEdge");
                      this._jvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes, _typeRef_3);
                      Entity _type_11 = firstRole_1.getType();
                      QualifiedName _fullyQualifiedName_7 = this._iQualifiedNameProvider.getFullyQualifiedName(_type_11);
                      String _string_5 = _fullyQualifiedName_7.toString();
                      final JvmTypeReference firstRoleType = this._typeReferenceBuilder.typeRef(_string_5);
                      Entity _type_12 = secondRole_1.getType();
                      QualifiedName _fullyQualifiedName_8 = this._iQualifiedNameProvider.getFullyQualifiedName(_type_12);
                      String _string_6 = _fullyQualifiedName_8.toString();
                      final JvmTypeReference secondRoleType = this._typeReferenceBuilder.typeRef(_string_6);
                      String _name_6 = firstRole_1.getName();
                      JvmField jvmField = this._jvmTypesBuilder.toField(meln, _name_6, firstRoleType);
                      boolean _notEquals_9 = (!Objects.equal(jvmField, null));
                      if (_notEquals_9) {
                        jvmField.setFinal(false);
                        EList<JvmMember> _members = it.getMembers();
                        this._jvmTypesBuilder.<JvmField>operator_add(_members, jvmField);
                      }
                      String _name_7 = secondRole_1.getName();
                      JvmField _field = this._jvmTypesBuilder.toField(meln, _name_7, secondRoleType);
                      jvmField = _field;
                      boolean _notEquals_10 = (!Objects.equal(jvmField, null));
                      if (_notEquals_10) {
                        jvmField.setFinal(false);
                        EList<JvmMember> _members_1 = it.getMembers();
                        this._jvmTypesBuilder.<JvmField>operator_add(_members_1, jvmField);
                      }
                      EList<JvmMember> _members_2 = it.getMembers();
                      final Procedure1<JvmConstructor> _function_3 = (JvmConstructor it_1) -> {
                        EList<JvmFormalParameter> _parameters = it_1.getParameters();
                        JvmTypeReference _typeRef_4 = this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.raster.Grid");
                        JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "grid1", _typeRef_4);
                        this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                        EList<JvmFormalParameter> _parameters_1 = it_1.getParameters();
                        JvmTypeReference _typeRef_5 = this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.raster.Grid");
                        JvmFormalParameter _parameter_1 = this._jvmTypesBuilder.toParameter(meln, "grid2", _typeRef_5);
                        this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_1, _parameter_1);
                        StringConcatenationClient _client = new StringConcatenationClient() {
                          @Override
                          protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                            _builder.append("super(grid1, grid2);");
                            _builder.newLine();
                            String _name = firstRole_1.getName();
                            _builder.append(_name, "");
                            _builder.append(" = new ");
                            _builder.append(firstRoleType, "");
                            _builder.append("();");
                            _builder.newLineIfNotEmpty();
                            String _name_1 = secondRole_1.getName();
                            _builder.append(_name_1, "");
                            _builder.append(" = new ");
                            _builder.append(secondRoleType, "");
                            _builder.append("();");
                            _builder.newLineIfNotEmpty();
                            String _name_2 = firstRole_1.getName();
                            _builder.append(_name_2, "");
                            _builder.append(".updateCellInfo(getCellType());");
                            _builder.newLineIfNotEmpty();
                            String _name_3 = secondRole_1.getName();
                            _builder.append(_name_3, "");
                            _builder.append(".updateCellInfo(getCellType());");
                            _builder.newLineIfNotEmpty();
                            _builder.append("updateRoleInfo();");
                            _builder.newLine();
                          }
                        };
                        this._jvmTypesBuilder.setBody(it_1, _client);
                      };
                      JvmConstructor _constructor = this._jvmTypesBuilder.toConstructor(meln, _function_3);
                      this._jvmTypesBuilder.<JvmConstructor>operator_add(_members_2, _constructor);
                      EList<JvmMember> _members_3 = it.getMembers();
                      JvmTypeReference _typeRef_4 = this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.relation.OcltRole");
                      final Procedure1<JvmOperation> _function_4 = (JvmOperation it_1) -> {
                        EList<JvmFormalParameter> _parameters = it_1.getParameters();
                        JvmTypeReference _typeRef_5 = this._typeReferenceBuilder.typeRef("java.lang.Integer");
                        JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "i", _typeRef_5);
                        this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                        StringConcatenationClient _client = new StringConcatenationClient() {
                          @Override
                          protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                            _builder.append("if (i==0) return ");
                            String _name = firstRole_1.getName();
                            _builder.append(_name, "");
                            _builder.append(";");
                            _builder.newLineIfNotEmpty();
                            _builder.append("else if (i==1) return ");
                            String _name_1 = secondRole_1.getName();
                            _builder.append(_name_1, "");
                            _builder.append(";");
                            _builder.newLineIfNotEmpty();
                            _builder.append("else return null;");
                            _builder.newLine();
                          }
                        };
                        this._jvmTypesBuilder.setBody(it_1, _client);
                      };
                      JvmOperation _method = this._jvmTypesBuilder.toMethod(meln, "getRole", _typeRef_4, _function_4);
                      this._jvmTypesBuilder.<JvmOperation>operator_add(_members_3, _method);
                      EList<JvmMember> _members_4 = it.getMembers();
                      JvmTypeReference _typeRef_5 = this._typeReferenceBuilder.typeRef(Void.TYPE);
                      final Procedure1<JvmOperation> _function_5 = (JvmOperation it_1) -> {
                        StringConcatenationClient _client = new StringConcatenationClient() {
                          @Override
                          protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                            _builder.append("this. ");
                            String _name = firstRole_1.getName();
                            _builder.append(_name, "");
                            _builder.append(".setX(x);");
                            _builder.newLineIfNotEmpty();
                            _builder.append("this. ");
                            String _name_1 = firstRole_1.getName();
                            _builder.append(_name_1, "");
                            _builder.append(".setY(y);");
                            _builder.newLineIfNotEmpty();
                            _builder.append("this. ");
                            String _name_2 = secondRole_1.getName();
                            _builder.append(_name_2, "");
                            _builder.append(".setX(x2);");
                            _builder.newLineIfNotEmpty();
                            _builder.append("this. ");
                            String _name_3 = secondRole_1.getName();
                            _builder.append(_name_3, "");
                            _builder.append(".setY(y2);");
                            _builder.newLineIfNotEmpty();
                          }
                        };
                        this._jvmTypesBuilder.setBody(it_1, _client);
                      };
                      JvmOperation _method_1 = this._jvmTypesBuilder.toMethod(meln, "update", _typeRef_5, _function_5);
                      this._jvmTypesBuilder.<JvmOperation>operator_add(_members_4, _method_1);
                      EList<RelElements> _relelns_1 = ((Relation)meln).getRelelns();
                      for (final RelElements reln_1 : _relelns_1) {
                        boolean _matched_4 = false;
                        if (!_matched_4) {
                          if (reln_1 instanceof InteractionDef) {
                            _matched_4=true;
                            EList<JvmMember> _members_5 = it.getMembers();
                            String _name_8 = ((InteractionDef)reln_1).getName();
                            JvmTypeReference _typeRef_6 = this._typeReferenceBuilder.typeRef(Void.TYPE);
                            final Procedure1<JvmOperation> _function_6 = (JvmOperation it_1) -> {
                              String params = "";
                              int index = 0;
                              EList<JvmFormalParameter> _params = ((InteractionDef)reln_1).getParams();
                              for (final JvmFormalParameter p : _params) {
                                {
                                  EList<JvmFormalParameter> _parameters = it_1.getParameters();
                                  String _name_9 = p.getName();
                                  JvmTypeReference _parameterType = p.getParameterType();
                                  JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(reln_1, _name_9, _parameterType);
                                  this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                                  EList<JvmFormalParameter> _params_1 = ((InteractionDef)reln_1).getParams();
                                  int _size_2 = _params_1.size();
                                  boolean _equals_1 = (index == _size_2);
                                  if (_equals_1) {
                                    String _name_10 = p.getName();
                                    String _plus_1 = (params + _name_10);
                                    params = _plus_1;
                                  } else {
                                    String _name_11 = p.getName();
                                    String _plus_2 = (params + _name_11);
                                    String _plus_3 = (_plus_2 + ",");
                                    params = _plus_3;
                                  }
                                }
                              }
                              final String finalParams = params;
                              XExpression _body = ((InteractionDef)reln_1).getBody();
                              this._jvmTypesBuilder.setBody(it_1, _body);
                            };
                            JvmOperation _method_2 = this._jvmTypesBuilder.toMethod(reln_1, _name_8, _typeRef_6, _function_6);
                            this._jvmTypesBuilder.<JvmOperation>operator_add(_members_5, _method_2);
                            EList<Comitexpr> _comitexpressions = ((InteractionDef)reln_1).getComitexpressions();
                            int _size_2 = _comitexpressions.size();
                            boolean _greaterThan_1 = (_size_2 > 0);
                            if (_greaterThan_1) {
                              EList<JvmMember> _members_6 = it.getMembers();
                              String _name_9 = ((InteractionDef)reln_1).getName();
                              String _plus_1 = ("get_agr_" + _name_9);
                              final Procedure1<JvmOperation> _function_7 = (JvmOperation it_1) -> {
                                StringConcatenationClient _client = new StringConcatenationClient() {
                                  @Override
                                  protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                    int index = 0;
                                    _builder.newLineIfNotEmpty();
                                    _builder.append(listype, "");
                                    _builder.append(" cvtList = new ");
                                    _builder.append(listype, "");
                                    _builder.append("();\t");
                                    _builder.newLineIfNotEmpty();
                                    {
                                      EList<Comitexpr> _comitexpressions = ((InteractionDef)reln_1).getComitexpressions();
                                      for(final Comitexpr ce : _comitexpressions) {
                                        _builder.append(aggregType, "");
                                        _builder.append(" cvt");
                                        _builder.append(index, "");
                                        _builder.append(" = new ");
                                        _builder.append(aggregType, "");
                                        _builder.append("();\t");
                                        _builder.newLineIfNotEmpty();
                                        _builder.append("          \t  \t      \t  \t \t");
                                        _builder.append("cvt");
                                        _builder.append(index, "          \t  \t      \t  \t \t");
                                        _builder.append(".setName(\"");
                                        String _prop = ce.getProp();
                                        _builder.append(_prop, "          \t  \t      \t  \t \t");
                                        _builder.append("\"); ");
                                        _builder.newLineIfNotEmpty();
                                        _builder.append("          \t  \t      \t  \t \t");
                                        _builder.append("cvt");
                                        _builder.append(index, "          \t  \t      \t  \t \t");
                                        _builder.append(".setOperator(new ");
                                        JvmTypeReference _agrfunc = ce.getAgrfunc();
                                        _builder.append(_agrfunc, "          \t  \t      \t  \t \t");
                                        _builder.append("());");
                                        _builder.newLineIfNotEmpty();
                                        _builder.append("          \t  \t      \t  \t \t");
                                        _builder.append("cvtList.add(cvt");
                                        _builder.append(index, "          \t  \t      \t  \t \t");
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
                                    _builder.append("           \t  \t      \t  ");
                                    _builder.append("return cvtList;");
                                    _builder.newLine();
                                  }
                                };
                                this._jvmTypesBuilder.setBody(it_1, _client);
                              };
                              JvmOperation _method_3 = this._jvmTypesBuilder.toMethod(reln_1, _plus_1, listype, _function_7);
                              this._jvmTypesBuilder.<JvmOperation>operator_add(_members_6, _method_3);
                            } else {
                              EList<JvmMember> _members_7 = it.getMembers();
                              String _name_10 = ((InteractionDef)reln_1).getName();
                              String _plus_2 = ("get_agr_" + _name_10);
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
                              JvmOperation _method_4 = this._jvmTypesBuilder.toMethod(reln_1, _plus_2, listype, _function_8);
                              this._jvmTypesBuilder.<JvmOperation>operator_add(_members_7, _method_4);
                            }
                          }
                        }
                      }
                    };
                    acceptor.<JvmGenericType>accept(_class_2, _function_2);
                    EList<RelElements> _relelns_1 = ((Relation)meln).getRelelns();
                    for (final RelElements reln_1 : _relelns_1) {
                      boolean _matched_4 = false;
                      if (!_matched_4) {
                        if (reln_1 instanceof Filterdef) {
                          _matched_4=true;
                          String _plus_1 = (graphcname + "_");
                          String _name_6 = ((Filterdef)reln_1).getName();
                          final String filterfqn = (_plus_1 + _name_6);
                          JvmGenericType _class_3 = this._jvmTypesBuilder.toClass(modl, filterfqn);
                          final Procedure1<JvmGenericType> _function_3 = (JvmGenericType it) -> {
                            Entity _type_11 = firstRole_1.getType();
                            QualifiedName _fullyQualifiedName_7 = this._iQualifiedNameProvider.getFullyQualifiedName(_type_11);
                            String _string_5 = _fullyQualifiedName_7.toString();
                            final JvmTypeReference firstRoleType = this._typeReferenceBuilder.typeRef(_string_5);
                            Entity _type_12 = secondRole_1.getType();
                            QualifiedName _fullyQualifiedName_8 = this._iQualifiedNameProvider.getFullyQualifiedName(_type_12);
                            String _string_6 = _fullyQualifiedName_8.toString();
                            final JvmTypeReference secondRoleType = this._typeReferenceBuilder.typeRef(_string_6);
                            EList<JvmTypeReference> _superTypes = it.getSuperTypes();
                            JvmTypeReference _typeRef_3 = this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.relation.EdgeFilter", firstRoleType, secondRoleType);
                            this._jvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes, _typeRef_3);
                            EList<JvmFormalParameter> _params = ((Filterdef)reln_1).getParams();
                            for (final JvmFormalParameter p : _params) {
                              {
                                String _name_7 = p.getName();
                                JvmTypeReference _parameterType = p.getParameterType();
                                final JvmField pfield = this._jvmTypesBuilder.toField(reln_1, _name_7, _parameterType);
                                boolean _notEquals_9 = (!Objects.equal(pfield, null));
                                if (_notEquals_9) {
                                  pfield.setFinal(false);
                                  EList<JvmMember> _members = it.getMembers();
                                  this._jvmTypesBuilder.<JvmField>operator_add(_members, pfield);
                                }
                              }
                            }
                            EList<JvmMember> _members = it.getMembers();
                            final Procedure1<JvmConstructor> _function_4 = (JvmConstructor it_1) -> {
                              EList<JvmFormalParameter> _params_1 = ((Filterdef)reln_1).getParams();
                              for (final JvmFormalParameter p_1 : _params_1) {
                                EList<JvmFormalParameter> _parameters = it_1.getParameters();
                                String _name_7 = p_1.getName();
                                JvmTypeReference _parameterType = p_1.getParameterType();
                                JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(reln_1, _name_7, _parameterType);
                                this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                              }
                              StringConcatenationClient _client = new StringConcatenationClient() {
                                @Override
                                protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                  {
                                    EList<JvmFormalParameter> _params = ((Filterdef)reln_1).getParams();
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
                              this._jvmTypesBuilder.setBody(it_1, _client);
                            };
                            JvmConstructor _constructor = this._jvmTypesBuilder.toConstructor(reln_1, _function_4);
                            this._jvmTypesBuilder.<JvmConstructor>operator_add(_members, _constructor);
                            EList<JvmMember> _members_1 = it.getMembers();
                            JvmTypeReference _typeRef_4 = this._typeReferenceBuilder.typeRef("java.lang.Boolean");
                            final Procedure1<JvmOperation> _function_5 = (JvmOperation it_1) -> {
                              EList<JvmFormalParameter> _parameters = it_1.getParameters();
                              String _name_7 = firstRole_1.getName();
                              JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(reln_1, _name_7, firstRoleType);
                              this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                              EList<JvmFormalParameter> _parameters_1 = it_1.getParameters();
                              String _name_8 = secondRole_1.getName();
                              JvmFormalParameter _parameter_1 = this._jvmTypesBuilder.toParameter(reln_1, _name_8, secondRoleType);
                              this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_1, _parameter_1);
                              XExpression _body = ((Filterdef)reln_1).getBody();
                              this._jvmTypesBuilder.setBody(it_1, _body);
                            };
                            JvmOperation _method = this._jvmTypesBuilder.toMethod(reln_1, "filter", _typeRef_4, _function_5);
                            this._jvmTypesBuilder.<JvmOperation>operator_add(_members_1, _method);
                          };
                          acceptor.<JvmGenericType>accept(_class_3, _function_3);
                        }
                      }
                    }
                    JvmTypeReference _typeRef_3 = this._typeReferenceBuilder.typeRef(edgecname);
                    boolean _notEquals_9 = (!Objects.equal(_typeRef_3, null));
                    if (_notEquals_9) {
                      JvmGenericType _class_3 = this._jvmTypesBuilder.toClass(modl, graphcname);
                      final Procedure1<JvmGenericType> _function_3 = (JvmGenericType it) -> {
                        String _documentation = this._jvmTypesBuilder.getDocumentation(meln);
                        this._jvmTypesBuilder.setDocumentation(it, _documentation);
                        Entity _type_11 = firstRole_1.getType();
                        QualifiedName _fullyQualifiedName_7 = this._iQualifiedNameProvider.getFullyQualifiedName(_type_11);
                        String _string_5 = _fullyQualifiedName_7.toString();
                        final JvmTypeReference firstRoleType = this._typeReferenceBuilder.typeRef(_string_5);
                        Entity _type_12 = secondRole_1.getType();
                        QualifiedName _fullyQualifiedName_8 = this._iQualifiedNameProvider.getFullyQualifiedName(_type_12);
                        String _string_6 = _fullyQualifiedName_8.toString();
                        final JvmTypeReference secondRoleType = this._typeReferenceBuilder.typeRef(_string_6);
                        EList<JvmTypeReference> _superTypes = it.getSuperTypes();
                        JvmTypeReference _typeRef_4 = this._typeReferenceBuilder.typeRef(edgecname);
                        JvmTypeReference _typeRef_5 = this._typeReferenceBuilder.typeRef(graphTypeName, _typeRef_4, firstRoleType, secondRoleType);
                        this._jvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes, _typeRef_5);
                        EList<JvmMember> _members = it.getMembers();
                        final Procedure1<JvmConstructor> _function_4 = (JvmConstructor it_1) -> {
                          StringConcatenationClient _client = new StringConcatenationClient() {
                            @Override
                            protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                              _builder.append("super();");
                              _builder.newLine();
                            }
                          };
                          this._jvmTypesBuilder.setBody(it_1, _client);
                        };
                        JvmConstructor _constructor = this._jvmTypesBuilder.toConstructor(meln, _function_4);
                        this._jvmTypesBuilder.<JvmConstructor>operator_add(_members, _constructor);
                        EList<JvmMember> _members_1 = it.getMembers();
                        JvmTypeReference _typeRef_6 = this._typeReferenceBuilder.typeRef(Void.TYPE);
                        final Procedure1<JvmOperation> _function_5 = (JvmOperation it_1) -> {
                          EList<JvmFormalParameter> _parameters = it_1.getParameters();
                          JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "grid1", gridType_1);
                          this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                          EList<JvmFormalParameter> _parameters_1 = it_1.getParameters();
                          JvmFormalParameter _parameter_1 = this._jvmTypesBuilder.toParameter(meln, "grid2", gridType_1);
                          this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_1, _parameter_1);
                          StringConcatenationClient _client = new StringConcatenationClient() {
                            @Override
                            protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                              _builder.newLine();
                              _builder.append("                  ");
                              _builder.append("super.setGrid(grid1, grid2);");
                              _builder.newLine();
                              _builder.append("                   ");
                              JvmTypeReference _typeRef = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef(edgecname);
                              _builder.append(_typeRef, "                   ");
                              _builder.append(" _gen_edge_ = new ");
                              String _name = ((Relation)meln).getName();
                              String _plus = (_name + "_Edge");
                              _builder.append(_plus, "                   ");
                              _builder.append("(grid1, grid2);");
                              _builder.newLineIfNotEmpty();
                              _builder.append("     \t\t  \t  ");
                              _builder.append("setCompleteIteratorDiCell(_gen_edge_ );");
                              _builder.newLine();
                            }
                          };
                          this._jvmTypesBuilder.setBody(it_1, _client);
                        };
                        JvmOperation _method = this._jvmTypesBuilder.toMethod(meln, "setGraph", _typeRef_6, _function_5);
                        this._jvmTypesBuilder.<JvmOperation>operator_add(_members_1, _method);
                        EList<RelElements> _relelns_2 = ((Relation)meln).getRelelns();
                        for (final RelElements reln_2 : _relelns_2) {
                          boolean _matched_5 = false;
                          if (!_matched_5) {
                            if (reln_2 instanceof RelPropertyDef) {
                              _matched_5=true;
                              EList<JvmMember> _members_2 = it.getMembers();
                              String _name_6 = ((RelPropertyDef)reln_2).getName();
                              String _firstUpper = StringExtensions.toFirstUpper(_name_6);
                              String _plus_1 = ("set" + _firstUpper);
                              JvmTypeReference _typeRef_7 = this._typeReferenceBuilder.typeRef(Void.TYPE);
                              final Procedure1<JvmOperation> _function_6 = (JvmOperation it_1) -> {
                                EList<JvmFormalParameter> _parameters = it_1.getParameters();
                                String _name_7 = ((RelPropertyDef)reln_2).getName();
                                JvmTypeReference _type_13 = ((RelPropertyDef)reln_2).getType();
                                JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(reln_2, _name_7, _type_13);
                                this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                                StringConcatenationClient _client = new StringConcatenationClient() {
                                  @Override
                                  protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                    _builder.append("for( ");
                                    JvmTypeReference _typeRef = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef(edgecname);
                                    _builder.append(_typeRef, "");
                                    _builder.append(" _edg_ : this )");
                                    _builder.newLineIfNotEmpty();
                                    _builder.append("_edg_.set");
                                    String _name = ((RelPropertyDef)reln_2).getName();
                                    String _firstUpper = StringExtensions.toFirstUpper(_name);
                                    _builder.append(_firstUpper, "");
                                    _builder.append("(");
                                    String _name_1 = ((RelPropertyDef)reln_2).getName();
                                    _builder.append(_name_1, "");
                                    _builder.append(");");
                                    _builder.newLineIfNotEmpty();
                                  }
                                };
                                this._jvmTypesBuilder.setBody(it_1, _client);
                              };
                              JvmOperation _method_1 = this._jvmTypesBuilder.toMethod(reln_2, _plus_1, _typeRef_7, _function_6);
                              this._jvmTypesBuilder.<JvmOperation>operator_add(_members_2, _method_1);
                            }
                          }
                          if (!_matched_5) {
                            if (reln_2 instanceof InteractionDef) {
                              _matched_5=true;
                              EList<JvmMember> _members_2 = it.getMembers();
                              String _name_6 = ((InteractionDef)reln_2).getName();
                              JvmTypeReference _typeRef_7 = this._typeReferenceBuilder.typeRef(Void.TYPE);
                              final Procedure1<JvmOperation> _function_6 = (JvmOperation it_1) -> {
                                EList<JvmFormalParameter> _params = ((InteractionDef)reln_2).getParams();
                                for (final JvmFormalParameter p : _params) {
                                  EList<JvmFormalParameter> _parameters = it_1.getParameters();
                                  String _name_7 = p.getName();
                                  JvmTypeReference _parameterType = p.getParameterType();
                                  JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(reln_2, _name_7, _parameterType);
                                  this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                                }
                                StringConcatenationClient _client = new StringConcatenationClient() {
                                  @Override
                                  protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                    _builder.append("updateGrid();");
                                    _builder.newLine();
                                    _builder.append("cleanOperator();");
                                    _builder.newLine();
                                    _builder.append(listype, "");
                                    _builder.append(" cvtList = ((");
                                    JvmTypeReference _typeRef = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef(edgecname);
                                    _builder.append(_typeRef, "");
                                    _builder.append(")getEdge()).get_agr_");
                                    String _name = ((InteractionDef)reln_2).getName();
                                    _builder.append(_name, "");
                                    _builder.append("();");
                                    _builder.newLineIfNotEmpty();
                                    _builder.append("if(cvtList != null){");
                                    _builder.newLine();
                                    _builder.append("\t");
                                    _builder.append("for(");
                                    _builder.append(aggregType, "\t");
                                    _builder.append(" cvt : cvtList){");
                                    _builder.newLineIfNotEmpty();
                                    _builder.append(" \t\t");
                                    _builder.append("setCellOperator(cvt);");
                                    _builder.newLine();
                                    _builder.append("\t");
                                    _builder.append("}");
                                    _builder.newLine();
                                    _builder.append("}");
                                    _builder.newLine();
                                    _builder.append("                       \t");
                                    _builder.append("for(");
                                    JvmTypeReference _typeRef_1 = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef(edgecname);
                                    _builder.append(_typeRef_1, "                       \t");
                                    _builder.append(" _edg_ : this) {");
                                    _builder.newLineIfNotEmpty();
                                    _builder.append("_edg_.");
                                    String _name_1 = ((InteractionDef)reln_2).getName();
                                    _builder.append(_name_1, "");
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
                                        _builder.append(_name_2, "");
                                        Object _xblockexpression = null;
                                        {
                                          ci = 1;
                                          _xblockexpression = null;
                                        }
                                        _builder.append(_xblockexpression, "");
                                      }
                                    }
                                    _builder.append(");");
                                    _builder.newLineIfNotEmpty();
                                    _builder.append("}");
                                    _builder.newLine();
                                  }
                                };
                                this._jvmTypesBuilder.setBody(it_1, _client);
                              };
                              JvmOperation _method_1 = this._jvmTypesBuilder.toMethod(reln_2, _name_6, _typeRef_7, _function_6);
                              this._jvmTypesBuilder.<JvmOperation>operator_add(_members_2, _method_1);
                            }
                          }
                          if (!_matched_5) {
                            if (reln_2 instanceof Filterdef) {
                              _matched_5=true;
                              EList<JvmMember> _members_2 = it.getMembers();
                              String _name_6 = ((Filterdef)reln_2).getName();
                              String _string_7 = graphcname.toString();
                              JvmTypeReference _typeRef_7 = this._typeReferenceBuilder.typeRef(_string_7);
                              final Procedure1<JvmOperation> _function_6 = (JvmOperation it_1) -> {
                                EList<JvmFormalParameter> _params = ((Filterdef)reln_2).getParams();
                                for (final JvmFormalParameter p : _params) {
                                  EList<JvmFormalParameter> _parameters = it_1.getParameters();
                                  String _name_7 = p.getName();
                                  JvmTypeReference _parameterType = p.getParameterType();
                                  JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(reln_2, _name_7, _parameterType);
                                  this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                                }
                                StringConcatenationClient _client = new StringConcatenationClient() {
                                  @Override
                                  protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                    String _name = ((Relation)meln).getName();
                                    String _plus = (_name + "_");
                                    String _name_1 = ((Filterdef)reln_2).getName();
                                    String _plus_1 = (_plus + _name_1);
                                    _builder.append(_plus_1, "");
                                    _builder.append(" _filter = new ");
                                    String _name_2 = ((Relation)meln).getName();
                                    String _plus_2 = (_name_2 + "_");
                                    String _name_3 = ((Filterdef)reln_2).getName();
                                    String _plus_3 = (_plus_2 + _name_3);
                                    _builder.append(_plus_3, "");
                                    _builder.newLineIfNotEmpty();
                                    {
                                      EList<JvmFormalParameter> _params = ((Filterdef)reln_2).getParams();
                                      int _size = _params.size();
                                      boolean _greaterThan = (_size > 0);
                                      if (_greaterThan) {
                                        {
                                          EList<JvmFormalParameter> _params_1 = ((Filterdef)reln_2).getParams();
                                          int _size_1 = _params_1.size();
                                          int _minus = (_size_1 - 1);
                                          IntegerRange _upTo = new IntegerRange(0, _minus);
                                          for(final Integer i : _upTo) {
                                            _builder.append("\treln.params.get(i).name»");
                                            {
                                              EList<JvmFormalParameter> _params_2 = ((Filterdef)reln_2).getParams();
                                              int _size_2 = _params_2.size();
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
                                    _builder.append(";");
                                    _builder.newLineIfNotEmpty();
                                    _builder.append("                        ");
                                    _builder.append("super.addFilter(_filter);");
                                    _builder.newLine();
                                    _builder.append("                        ");
                                    _builder.append("return this;");
                                    _builder.newLine();
                                    _builder.newLine();
                                  }
                                };
                                this._jvmTypesBuilder.setBody(it_1, _client);
                              };
                              JvmOperation _method_1 = this._jvmTypesBuilder.toMethod(reln_2, _name_6, _typeRef_7, _function_6);
                              this._jvmTypesBuilder.<JvmOperation>operator_add(_members_2, _method_1);
                            }
                          }
                        }
                      };
                      acceptor.<JvmGenericType>accept(_class_3, _function_3);
                    }
                  } else {
                    JvmGenericType _class_4 = this._jvmTypesBuilder.toClass(modl, edgecname);
                    final Procedure1<JvmGenericType> _function_4 = (JvmGenericType it) -> {
                      EList<JvmTypeReference> _superTypes = it.getSuperTypes();
                      JvmTypeReference _typeRef_4 = this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.relation.CursorEdge");
                      this._jvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes, _typeRef_4);
                      Entity _type_11 = firstRole_1.getType();
                      QualifiedName _fullyQualifiedName_7 = this._iQualifiedNameProvider.getFullyQualifiedName(_type_11);
                      String _string_5 = _fullyQualifiedName_7.toString();
                      final JvmTypeReference firstRoleType = this._typeReferenceBuilder.typeRef(_string_5);
                      Entity _type_12 = secondRole_1.getType();
                      QualifiedName _fullyQualifiedName_8 = this._iQualifiedNameProvider.getFullyQualifiedName(_type_12);
                      String _string_6 = _fullyQualifiedName_8.toString();
                      final JvmTypeReference secondRoleType = this._typeReferenceBuilder.typeRef(_string_6);
                      String _name_6 = firstRole_1.getName();
                      JvmField jvmField = this._jvmTypesBuilder.toField(meln, _name_6, firstRoleType);
                      boolean _notEquals_10 = (!Objects.equal(jvmField, null));
                      if (_notEquals_10) {
                        jvmField.setFinal(false);
                        EList<JvmMember> _members = it.getMembers();
                        this._jvmTypesBuilder.<JvmField>operator_add(_members, jvmField);
                      }
                      String _name_7 = secondRole_1.getName();
                      JvmField _field = this._jvmTypesBuilder.toField(meln, _name_7, secondRoleType);
                      jvmField = _field;
                      boolean _notEquals_11 = (!Objects.equal(jvmField, null));
                      if (_notEquals_11) {
                        jvmField.setFinal(false);
                        EList<JvmMember> _members_1 = it.getMembers();
                        this._jvmTypesBuilder.<JvmField>operator_add(_members_1, jvmField);
                      }
                      EList<JvmMember> _members_2 = it.getMembers();
                      final Procedure1<JvmConstructor> _function_5 = (JvmConstructor it_1) -> {
                        EList<JvmFormalParameter> _parameters = it_1.getParameters();
                        JvmTypeReference _typeRef_5 = this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.raster.Grid");
                        JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "igr", _typeRef_5);
                        this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                        StringConcatenationClient _client = new StringConcatenationClient() {
                          @Override
                          protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                            _builder.append("super(igr);");
                            _builder.newLine();
                            _builder.append("          \t\t\t    ");
                            String _name = firstRole_1.getName();
                            _builder.append(_name, "          \t\t\t    ");
                            _builder.append(" = new ");
                            _builder.append(firstRoleType, "          \t\t\t    ");
                            _builder.append("();");
                            _builder.newLineIfNotEmpty();
                            _builder.append("          \t\t\t    ");
                            String _name_1 = secondRole_1.getName();
                            _builder.append(_name_1, "          \t\t\t    ");
                            _builder.append(" = new ");
                            _builder.append(secondRoleType, "          \t\t\t    ");
                            _builder.append("();");
                            _builder.newLineIfNotEmpty();
                            _builder.append("          \t\t\t    ");
                            String _name_2 = firstRole_1.getName();
                            _builder.append(_name_2, "          \t\t\t    ");
                            _builder.append(".updateCellInfo(getCellType());");
                            _builder.newLineIfNotEmpty();
                            _builder.append("          \t\t\t    ");
                            String _name_3 = secondRole_1.getName();
                            _builder.append(_name_3, "          \t\t\t    ");
                            _builder.append(".updateCellInfo(getCellType());");
                            _builder.newLineIfNotEmpty();
                          }
                        };
                        this._jvmTypesBuilder.setBody(it_1, _client);
                      };
                      JvmConstructor _constructor = this._jvmTypesBuilder.toConstructor(meln, _function_5);
                      this._jvmTypesBuilder.<JvmConstructor>operator_add(_members_2, _constructor);
                      EList<JvmMember> _members_3 = it.getMembers();
                      JvmTypeReference _typeRef_5 = this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.relation.OcltRole");
                      final Procedure1<JvmOperation> _function_6 = (JvmOperation it_1) -> {
                        EList<JvmFormalParameter> _parameters = it_1.getParameters();
                        JvmTypeReference _typeRef_6 = this._typeReferenceBuilder.typeRef("java.lang.Integer");
                        JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "i", _typeRef_6);
                        this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                        StringConcatenationClient _client = new StringConcatenationClient() {
                          @Override
                          protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                            _builder.append("if (i==0) return ");
                            String _name = firstRole_1.getName();
                            _builder.append(_name, "");
                            _builder.append(";");
                            _builder.newLineIfNotEmpty();
                            _builder.append("else if (i==1) return ");
                            String _name_1 = secondRole_1.getName();
                            _builder.append(_name_1, "");
                            _builder.append(";");
                            _builder.newLineIfNotEmpty();
                            _builder.append("else return null;");
                            _builder.newLine();
                          }
                        };
                        this._jvmTypesBuilder.setBody(it_1, _client);
                      };
                      JvmOperation _method = this._jvmTypesBuilder.toMethod(meln, "getRole", _typeRef_5, _function_6);
                      this._jvmTypesBuilder.<JvmOperation>operator_add(_members_3, _method);
                      EList<JvmMember> _members_4 = it.getMembers();
                      JvmTypeReference _typeRef_6 = this._typeReferenceBuilder.typeRef(Void.TYPE);
                      final Procedure1<JvmOperation> _function_7 = (JvmOperation it_1) -> {
                        StringConcatenationClient _client = new StringConcatenationClient() {
                          @Override
                          protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                            String _name = firstRole_1.getName();
                            _builder.append(_name, "");
                            _builder.append(".updateCellInfo(getCellType());");
                            _builder.newLineIfNotEmpty();
                            String _name_1 = secondRole_1.getName();
                            _builder.append(_name_1, "");
                            _builder.append(".updateCellInfo(getCellType());");
                            _builder.newLineIfNotEmpty();
                          }
                        };
                        this._jvmTypesBuilder.setBody(it_1, _client);
                      };
                      JvmOperation _method_1 = this._jvmTypesBuilder.toMethod(meln, "updateCellType", _typeRef_6, _function_7);
                      this._jvmTypesBuilder.<JvmOperation>operator_add(_members_4, _method_1);
                      EList<JvmMember> _members_5 = it.getMembers();
                      JvmTypeReference _typeRef_7 = this._typeReferenceBuilder.typeRef(Void.TYPE);
                      final Procedure1<JvmOperation> _function_8 = (JvmOperation it_1) -> {
                        StringConcatenationClient _client = new StringConcatenationClient() {
                          @Override
                          protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                            _builder.newLine();
                            _builder.append("this. ");
                            String _name = firstRole_1.getName();
                            _builder.append(_name, "");
                            _builder.append(".setX(x);");
                            _builder.newLineIfNotEmpty();
                            _builder.append("this. ");
                            String _name_1 = firstRole_1.getName();
                            _builder.append(_name_1, "");
                            _builder.append(".setY(y);");
                            _builder.newLineIfNotEmpty();
                            _builder.append("          \t\t\t");
                            _builder.append("this. ");
                            String _name_2 = secondRole_1.getName();
                            _builder.append(_name_2, "          \t\t\t");
                            _builder.append(".setX(x2);");
                            _builder.newLineIfNotEmpty();
                            _builder.append("          \t\t\t");
                            _builder.append("this. ");
                            String _name_3 = secondRole_1.getName();
                            _builder.append(_name_3, "          \t\t\t");
                            _builder.append(".setY(y2);");
                            _builder.newLineIfNotEmpty();
                          }
                        };
                        this._jvmTypesBuilder.setBody(it_1, _client);
                      };
                      JvmOperation _method_2 = this._jvmTypesBuilder.toMethod(meln, "update", _typeRef_7, _function_8);
                      this._jvmTypesBuilder.<JvmOperation>operator_add(_members_5, _method_2);
                      EList<RelElements> _relelns_2 = ((Relation)meln).getRelelns();
                      for (final RelElements reln_2 : _relelns_2) {
                        boolean _matched_5 = false;
                        if (!_matched_5) {
                          if (reln_2 instanceof InteractionDef) {
                            _matched_5=true;
                            EList<JvmMember> _members_6 = it.getMembers();
                            String _name_8 = ((InteractionDef)reln_2).getName();
                            JvmTypeReference _typeRef_8 = this._typeReferenceBuilder.typeRef(Void.TYPE);
                            final Procedure1<JvmOperation> _function_9 = (JvmOperation it_1) -> {
                              String params = "";
                              int index = 0;
                              EList<JvmFormalParameter> _params = ((InteractionDef)reln_2).getParams();
                              for (final JvmFormalParameter p : _params) {
                                {
                                  EList<JvmFormalParameter> _parameters = it_1.getParameters();
                                  String _name_9 = p.getName();
                                  JvmTypeReference _parameterType = p.getParameterType();
                                  JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(reln_2, _name_9, _parameterType);
                                  this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                                  EList<JvmFormalParameter> _params_1 = ((InteractionDef)reln_2).getParams();
                                  int _size_2 = _params_1.size();
                                  boolean _equals_1 = (index == _size_2);
                                  if (_equals_1) {
                                    String _name_10 = p.getName();
                                    String _plus_1 = (params + _name_10);
                                    params = _plus_1;
                                  } else {
                                    String _name_11 = p.getName();
                                    String _plus_2 = (params + _name_11);
                                    String _plus_3 = (_plus_2 + ",");
                                    params = _plus_3;
                                  }
                                }
                              }
                              final String finalParams = params;
                              XExpression _body = ((InteractionDef)reln_2).getBody();
                              this._jvmTypesBuilder.setBody(it_1, _body);
                            };
                            JvmOperation _method_3 = this._jvmTypesBuilder.toMethod(reln_2, _name_8, _typeRef_8, _function_9);
                            this._jvmTypesBuilder.<JvmOperation>operator_add(_members_6, _method_3);
                            EList<Comitexpr> _comitexpressions = ((InteractionDef)reln_2).getComitexpressions();
                            int _size_2 = _comitexpressions.size();
                            boolean _greaterThan_1 = (_size_2 > 0);
                            if (_greaterThan_1) {
                              EList<JvmMember> _members_7 = it.getMembers();
                              String _name_9 = ((InteractionDef)reln_2).getName();
                              String _plus_1 = ("get_agr_" + _name_9);
                              final Procedure1<JvmOperation> _function_10 = (JvmOperation it_1) -> {
                                StringConcatenationClient _client = new StringConcatenationClient() {
                                  @Override
                                  protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                    _builder.append("\t");
                                    int index = 0;
                                    _builder.newLineIfNotEmpty();
                                    _builder.append(listype, "");
                                    _builder.append(" cvtList = new ");
                                    _builder.append(listype, "");
                                    _builder.append("();");
                                    _builder.newLineIfNotEmpty();
                                    {
                                      EList<Comitexpr> _comitexpressions = ((InteractionDef)reln_2).getComitexpressions();
                                      for(final Comitexpr ce : _comitexpressions) {
                                        _builder.append(aggregType, "");
                                        _builder.append(" cvt");
                                        _builder.append(index, "");
                                        _builder.append(" = new ");
                                        _builder.append(aggregType, "");
                                        _builder.append("();");
                                        _builder.newLineIfNotEmpty();
                                        _builder.append("          \t  \t      \t  \t");
                                        _builder.append("cvt");
                                        _builder.append(index, "          \t  \t      \t  \t");
                                        _builder.append(".setName(\"");
                                        String _prop = ce.getProp();
                                        _builder.append(_prop, "          \t  \t      \t  \t");
                                        _builder.append("\"); ");
                                        _builder.newLineIfNotEmpty();
                                        _builder.append("          \t  \t      \t  \t");
                                        _builder.append("cvt");
                                        _builder.append(index, "          \t  \t      \t  \t");
                                        _builder.append(".setOperator(new  ");
                                        JvmTypeReference _agrfunc = ce.getAgrfunc();
                                        _builder.append(_agrfunc, "          \t  \t      \t  \t");
                                        _builder.append("());");
                                        _builder.newLineIfNotEmpty();
                                        _builder.append("          \t  \t      \t  \t");
                                        _builder.append("cvtList.add(cvt");
                                        _builder.append(index, "          \t  \t      \t  \t");
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
                                    _builder.append("           \t  \t      \t   ");
                                    _builder.append("return cvtList;");
                                    _builder.newLine();
                                  }
                                };
                                this._jvmTypesBuilder.setBody(it_1, _client);
                              };
                              JvmOperation _method_4 = this._jvmTypesBuilder.toMethod(reln_2, _plus_1, listype, _function_10);
                              this._jvmTypesBuilder.<JvmOperation>operator_add(_members_7, _method_4);
                            } else {
                              EList<JvmMember> _members_8 = it.getMembers();
                              String _name_10 = ((InteractionDef)reln_2).getName();
                              String _plus_2 = ("get_agr_" + _name_10);
                              final Procedure1<JvmOperation> _function_11 = (JvmOperation it_1) -> {
                                StringConcatenationClient _client = new StringConcatenationClient() {
                                  @Override
                                  protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                    _builder.append("return null;");
                                    _builder.newLine();
                                  }
                                };
                                this._jvmTypesBuilder.setBody(it_1, _client);
                              };
                              JvmOperation _method_5 = this._jvmTypesBuilder.toMethod(reln_2, _plus_2, listype, _function_11);
                              this._jvmTypesBuilder.<JvmOperation>operator_add(_members_8, _method_5);
                            }
                          }
                        }
                      }
                    };
                    acceptor.<JvmGenericType>accept(_class_4, _function_4);
                    EList<RelElements> _relelns_2 = ((Relation)meln).getRelelns();
                    for (final RelElements reln_2 : _relelns_2) {
                      boolean _matched_5 = false;
                      if (!_matched_5) {
                        if (reln_2 instanceof Filterdef) {
                          _matched_5=true;
                          String _plus_1 = (graphcname + "_");
                          String _name_6 = ((Filterdef)reln_2).getName();
                          final String filterfqn = (_plus_1 + _name_6);
                          JvmGenericType _class_5 = this._jvmTypesBuilder.toClass(modl, filterfqn);
                          final Procedure1<JvmGenericType> _function_5 = (JvmGenericType it) -> {
                            Entity _type_11 = firstRole_1.getType();
                            QualifiedName _fullyQualifiedName_7 = this._iQualifiedNameProvider.getFullyQualifiedName(_type_11);
                            String _string_5 = _fullyQualifiedName_7.toString();
                            final JvmTypeReference firstRoleType = this._typeReferenceBuilder.typeRef(_string_5);
                            Entity _type_12 = secondRole_1.getType();
                            QualifiedName _fullyQualifiedName_8 = this._iQualifiedNameProvider.getFullyQualifiedName(_type_12);
                            String _string_6 = _fullyQualifiedName_8.toString();
                            final JvmTypeReference secondRoleType = this._typeReferenceBuilder.typeRef(_string_6);
                            EList<JvmTypeReference> _superTypes = it.getSuperTypes();
                            JvmTypeReference _typeRef_4 = this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.relation.EdgeFilter", firstRoleType, secondRoleType);
                            this._jvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes, _typeRef_4);
                            EList<JvmFormalParameter> _params = ((Filterdef)reln_2).getParams();
                            for (final JvmFormalParameter p : _params) {
                              {
                                String _name_7 = p.getName();
                                JvmTypeReference _parameterType = p.getParameterType();
                                final JvmField pfield = this._jvmTypesBuilder.toField(reln_2, _name_7, _parameterType);
                                boolean _notEquals_10 = (!Objects.equal(pfield, null));
                                if (_notEquals_10) {
                                  pfield.setFinal(false);
                                  EList<JvmMember> _members = it.getMembers();
                                  this._jvmTypesBuilder.<JvmField>operator_add(_members, pfield);
                                }
                              }
                            }
                            EList<JvmMember> _members = it.getMembers();
                            final Procedure1<JvmConstructor> _function_6 = (JvmConstructor it_1) -> {
                              EList<JvmFormalParameter> _params_1 = ((Filterdef)reln_2).getParams();
                              for (final JvmFormalParameter p_1 : _params_1) {
                                EList<JvmFormalParameter> _parameters = it_1.getParameters();
                                String _name_7 = p_1.getName();
                                JvmTypeReference _parameterType = p_1.getParameterType();
                                JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(reln_2, _name_7, _parameterType);
                                this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                              }
                              StringConcatenationClient _client = new StringConcatenationClient() {
                                @Override
                                protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                  {
                                    EList<JvmFormalParameter> _params = ((Filterdef)reln_2).getParams();
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
                              this._jvmTypesBuilder.setBody(it_1, _client);
                            };
                            JvmConstructor _constructor = this._jvmTypesBuilder.toConstructor(reln_2, _function_6);
                            this._jvmTypesBuilder.<JvmConstructor>operator_add(_members, _constructor);
                            EList<JvmMember> _members_1 = it.getMembers();
                            JvmTypeReference _typeRef_5 = this._typeReferenceBuilder.typeRef("java.lang.Boolean");
                            final Procedure1<JvmOperation> _function_7 = (JvmOperation it_1) -> {
                              EList<JvmFormalParameter> _parameters = it_1.getParameters();
                              String _name_7 = firstRole_1.getName();
                              JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(reln_2, _name_7, firstRoleType);
                              this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                              EList<JvmFormalParameter> _parameters_1 = it_1.getParameters();
                              String _name_8 = secondRole_1.getName();
                              JvmFormalParameter _parameter_1 = this._jvmTypesBuilder.toParameter(reln_2, _name_8, secondRoleType);
                              this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_1, _parameter_1);
                              XExpression _body = ((Filterdef)reln_2).getBody();
                              this._jvmTypesBuilder.setBody(it_1, _body);
                            };
                            JvmOperation _method = this._jvmTypesBuilder.toMethod(reln_2, "filter", _typeRef_5, _function_7);
                            this._jvmTypesBuilder.<JvmOperation>operator_add(_members_1, _method);
                          };
                          acceptor.<JvmGenericType>accept(_class_5, _function_5);
                        }
                      }
                    }
                    JvmTypeReference _typeRef_4 = this._typeReferenceBuilder.typeRef(edgecname);
                    boolean _notEquals_10 = (!Objects.equal(_typeRef_4, null));
                    if (_notEquals_10) {
                      JvmGenericType _class_5 = this._jvmTypesBuilder.toClass(modl, graphcname);
                      final Procedure1<JvmGenericType> _function_5 = (JvmGenericType it) -> {
                        String _documentation = this._jvmTypesBuilder.getDocumentation(meln);
                        this._jvmTypesBuilder.setDocumentation(it, _documentation);
                        Entity _type_11 = firstRole_1.getType();
                        QualifiedName _fullyQualifiedName_7 = this._iQualifiedNameProvider.getFullyQualifiedName(_type_11);
                        String _string_5 = _fullyQualifiedName_7.toString();
                        final JvmTypeReference firstRoleType = this._typeReferenceBuilder.typeRef(_string_5);
                        EList<JvmTypeReference> _superTypes = it.getSuperTypes();
                        JvmTypeReference _typeRef_5 = this._typeReferenceBuilder.typeRef(edgecname);
                        JvmTypeReference _typeRef_6 = this._typeReferenceBuilder.typeRef(graphTypeName, _typeRef_5, firstRoleType);
                        this._jvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes, _typeRef_6);
                        EList<JvmMember> _members = it.getMembers();
                        final Procedure1<JvmConstructor> _function_6 = (JvmConstructor it_1) -> {
                          StringConcatenationClient _client = new StringConcatenationClient() {
                            @Override
                            protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                              _builder.append("super();");
                              _builder.newLine();
                            }
                          };
                          this._jvmTypesBuilder.setBody(it_1, _client);
                        };
                        JvmConstructor _constructor = this._jvmTypesBuilder.toConstructor(meln, _function_6);
                        this._jvmTypesBuilder.<JvmConstructor>operator_add(_members, _constructor);
                        EList<JvmMember> _members_1 = it.getMembers();
                        JvmTypeReference _typeRef_7 = this._typeReferenceBuilder.typeRef(Void.TYPE);
                        final Procedure1<JvmOperation> _function_7 = (JvmOperation it_1) -> {
                          EList<JvmFormalParameter> _parameters = it_1.getParameters();
                          JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "grid", gridType_1);
                          this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                          StringConcatenationClient _client = new StringConcatenationClient() {
                            @Override
                            protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                              _builder.newLine();
                              _builder.newLine();
                              _builder.append("                  ");
                              _builder.append("super.setGrid(grid);");
                              _builder.newLine();
                              _builder.append("                   ");
                              JvmTypeReference _typeRef = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef(edgecname);
                              _builder.append(_typeRef, "                   ");
                              _builder.append(" _gen_edge_ = new ");
                              String _name = ((Relation)meln).getName();
                              String _plus = (_name + "_Edge");
                              _builder.append(_plus, "                   ");
                              _builder.append("(grid);");
                              _builder.newLineIfNotEmpty();
                              _builder.append("     \t\t  \t  ");
                              _builder.append("setCompleteIteratorCell(_gen_edge_ );");
                              _builder.newLine();
                            }
                          };
                          this._jvmTypesBuilder.setBody(it_1, _client);
                        };
                        JvmOperation _method = this._jvmTypesBuilder.toMethod(meln, "setGrid", _typeRef_7, _function_7);
                        this._jvmTypesBuilder.<JvmOperation>operator_add(_members_1, _method);
                        EList<JvmMember> _members_2 = it.getMembers();
                        JvmTypeReference _typeRef_8 = this._typeReferenceBuilder.typeRef(Void.TYPE);
                        final Procedure1<JvmOperation> _function_8 = (JvmOperation it_1) -> {
                          EList<JvmFormalParameter> _parameters = it_1.getParameters();
                          JvmTypeReference _typeRef_9 = this._typeReferenceBuilder.typeRef("fr.ocelet.datafacer.ocltypes.Shapefile");
                          JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "shp", _typeRef_9);
                          this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                          EList<JvmFormalParameter> _parameters_1 = it_1.getParameters();
                          JvmTypeReference _typeRef_10 = this._typeReferenceBuilder.typeRef("java.lang.Double");
                          JvmFormalParameter _parameter_1 = this._jvmTypesBuilder.toParameter(meln, "size", _typeRef_10);
                          this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_1, _parameter_1);
                          StringConcatenationClient _client = new StringConcatenationClient() {
                            @Override
                            protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                              _builder.append(firstRoleType, "");
                              _builder.append(" entity = new ");
                              _builder.append(firstRoleType, "");
                              _builder.append("();");
                              _builder.newLineIfNotEmpty();
                              _builder.append(gridType_1, "");
                              _builder.append(" grid = createHexagon(\"");
                              _builder.append(graphcname, "");
                              _builder.append("\",entity.getProps(), shp.getBounds(), size);");
                              _builder.newLineIfNotEmpty();
                              _builder.newLine();
                              _builder.append("          \t\t\t    ");
                              _builder.append("fr.ocelet.runtime.raster.GridManager.getInstance().add(grid); ");
                              _builder.newLine();
                              _builder.append("                  \t \t");
                              _builder.append("entity.setNumGrid(fr.ocelet.runtime.raster.GridManager.getInstance().getCurrentIndex());");
                              _builder.newLine();
                              _builder.append("                  \t   \t");
                              _builder.append("setGrid(grid);");
                              _builder.newLine();
                              _builder.append("                  \t   \t");
                              _builder.append("setCellShapeType(\"HEXAGONAL\");");
                              _builder.newLine();
                            }
                          };
                          this._jvmTypesBuilder.setBody(it_1, _client);
                        };
                        JvmOperation _method_1 = this._jvmTypesBuilder.toMethod(meln, "createHexagons", _typeRef_8, _function_8);
                        this._jvmTypesBuilder.<JvmOperation>operator_add(_members_2, _method_1);
                        EList<JvmMember> _members_3 = it.getMembers();
                        JvmTypeReference _typeRef_9 = this._typeReferenceBuilder.typeRef(Void.TYPE);
                        final Procedure1<JvmOperation> _function_9 = (JvmOperation it_1) -> {
                          EList<JvmFormalParameter> _parameters = it_1.getParameters();
                          JvmTypeReference _typeRef_10 = this._typeReferenceBuilder.typeRef("java.lang.Double");
                          JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "size", _typeRef_10);
                          this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                          EList<JvmFormalParameter> _parameters_1 = it_1.getParameters();
                          JvmTypeReference _typeRef_11 = this._typeReferenceBuilder.typeRef("java.lang.Double");
                          JvmFormalParameter _parameter_1 = this._jvmTypesBuilder.toParameter(meln, "minX", _typeRef_11);
                          this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_1, _parameter_1);
                          EList<JvmFormalParameter> _parameters_2 = it_1.getParameters();
                          JvmTypeReference _typeRef_12 = this._typeReferenceBuilder.typeRef("java.lang.Double");
                          JvmFormalParameter _parameter_2 = this._jvmTypesBuilder.toParameter(meln, "minY", _typeRef_12);
                          this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_2, _parameter_2);
                          EList<JvmFormalParameter> _parameters_3 = it_1.getParameters();
                          JvmTypeReference _typeRef_13 = this._typeReferenceBuilder.typeRef("java.lang.Double");
                          JvmFormalParameter _parameter_3 = this._jvmTypesBuilder.toParameter(meln, "maxX", _typeRef_13);
                          this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_3, _parameter_3);
                          EList<JvmFormalParameter> _parameters_4 = it_1.getParameters();
                          JvmTypeReference _typeRef_14 = this._typeReferenceBuilder.typeRef("java.lang.Double");
                          JvmFormalParameter _parameter_4 = this._jvmTypesBuilder.toParameter(meln, "maxY", _typeRef_14);
                          this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_4, _parameter_4);
                          StringConcatenationClient _client = new StringConcatenationClient() {
                            @Override
                            protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                              _builder.append(firstRoleType, "");
                              _builder.append(" entity = new ");
                              _builder.append(firstRoleType, "");
                              _builder.append("();");
                              _builder.newLineIfNotEmpty();
                              _builder.append(gridType_1, "");
                              _builder.append(" grid =  createHexagon(\"");
                              _builder.append(graphcname, "");
                              _builder.append("\",entity.getProps(), minX, minY, maxX, maxY, size);");
                              _builder.newLineIfNotEmpty();
                              _builder.append("fr.ocelet.runtime.raster.GridManager.getInstance().add(grid);");
                              _builder.newLine();
                              _builder.append("                  \t   \t");
                              _builder.append("entity.setNumGrid(fr.ocelet.runtime.raster.GridManager.getInstance().getCurrentIndex());");
                              _builder.newLine();
                              _builder.append("                  \t   \t");
                              _builder.append("setGrid(grid);");
                              _builder.newLine();
                              _builder.append("                  \t   \t");
                              _builder.append("setCellShapeType(\"HEXAGONAL\");");
                              _builder.newLine();
                            }
                          };
                          this._jvmTypesBuilder.setBody(it_1, _client);
                        };
                        JvmOperation _method_2 = this._jvmTypesBuilder.toMethod(meln, "createHexagons", _typeRef_9, _function_9);
                        this._jvmTypesBuilder.<JvmOperation>operator_add(_members_3, _method_2);
                        EList<JvmMember> _members_4 = it.getMembers();
                        JvmTypeReference _typeRef_10 = this._typeReferenceBuilder.typeRef(Void.TYPE);
                        final Procedure1<JvmOperation> _function_10 = (JvmOperation it_1) -> {
                          EList<JvmFormalParameter> _parameters = it_1.getParameters();
                          JvmTypeReference _typeRef_11 = this._typeReferenceBuilder.typeRef("fr.ocelet.datafacer.ocltypes.Shapefile");
                          JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "shp", _typeRef_11);
                          this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                          EList<JvmFormalParameter> _parameters_1 = it_1.getParameters();
                          JvmTypeReference _typeRef_12 = this._typeReferenceBuilder.typeRef("java.lang.Double");
                          JvmFormalParameter _parameter_1 = this._jvmTypesBuilder.toParameter(meln, "xRes", _typeRef_12);
                          this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_1, _parameter_1);
                          EList<JvmFormalParameter> _parameters_2 = it_1.getParameters();
                          JvmTypeReference _typeRef_13 = this._typeReferenceBuilder.typeRef("java.lang.Double");
                          JvmFormalParameter _parameter_2 = this._jvmTypesBuilder.toParameter(meln, "yRes", _typeRef_13);
                          this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_2, _parameter_2);
                          StringConcatenationClient _client = new StringConcatenationClient() {
                            @Override
                            protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                              _builder.append(firstRoleType, "");
                              _builder.append(" entity = new ");
                              _builder.append(firstRoleType, "");
                              _builder.append("();");
                              _builder.newLineIfNotEmpty();
                              _builder.append(gridType_1, "");
                              _builder.append(" grid = createSquare(\"");
                              _builder.append(graphcname, "");
                              _builder.append("\",entity.getProps(), shp.getBounds(), xRes, yRes);");
                              _builder.newLineIfNotEmpty();
                              _builder.append("fr.ocelet.runtime.raster.GridManager.getInstance().add(grid); ");
                              _builder.newLine();
                              _builder.append("                  \t   \t");
                              _builder.append("entity.setNumGrid(fr.ocelet.runtime.raster.GridManager.getInstance().getCurrentIndex());");
                              _builder.newLine();
                              _builder.append("                  \t   \t");
                              _builder.append("setGrid(grid);");
                              _builder.newLine();
                              _builder.append("                  \t   \t");
                              _builder.append("setCellShapeType(\"QUADRILATERAL\");");
                              _builder.newLine();
                            }
                          };
                          this._jvmTypesBuilder.setBody(it_1, _client);
                        };
                        JvmOperation _method_3 = this._jvmTypesBuilder.toMethod(meln, "createSquares", _typeRef_10, _function_10);
                        this._jvmTypesBuilder.<JvmOperation>operator_add(_members_4, _method_3);
                        EList<JvmMember> _members_5 = it.getMembers();
                        JvmTypeReference _typeRef_11 = this._typeReferenceBuilder.typeRef(Void.TYPE);
                        final Procedure1<JvmOperation> _function_11 = (JvmOperation it_1) -> {
                          EList<JvmFormalParameter> _parameters = it_1.getParameters();
                          JvmTypeReference _typeRef_12 = this._typeReferenceBuilder.typeRef("java.lang.Double");
                          JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "xRes", _typeRef_12);
                          this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                          EList<JvmFormalParameter> _parameters_1 = it_1.getParameters();
                          JvmTypeReference _typeRef_13 = this._typeReferenceBuilder.typeRef("java.lang.Double");
                          JvmFormalParameter _parameter_1 = this._jvmTypesBuilder.toParameter(meln, "yRes", _typeRef_13);
                          this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_1, _parameter_1);
                          EList<JvmFormalParameter> _parameters_2 = it_1.getParameters();
                          JvmTypeReference _typeRef_14 = this._typeReferenceBuilder.typeRef("java.lang.Double");
                          JvmFormalParameter _parameter_2 = this._jvmTypesBuilder.toParameter(meln, "minX", _typeRef_14);
                          this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_2, _parameter_2);
                          EList<JvmFormalParameter> _parameters_3 = it_1.getParameters();
                          JvmTypeReference _typeRef_15 = this._typeReferenceBuilder.typeRef("java.lang.Double");
                          JvmFormalParameter _parameter_3 = this._jvmTypesBuilder.toParameter(meln, "minY", _typeRef_15);
                          this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_3, _parameter_3);
                          EList<JvmFormalParameter> _parameters_4 = it_1.getParameters();
                          JvmTypeReference _typeRef_16 = this._typeReferenceBuilder.typeRef("java.lang.Double");
                          JvmFormalParameter _parameter_4 = this._jvmTypesBuilder.toParameter(meln, "maxX", _typeRef_16);
                          this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_4, _parameter_4);
                          EList<JvmFormalParameter> _parameters_5 = it_1.getParameters();
                          JvmTypeReference _typeRef_17 = this._typeReferenceBuilder.typeRef("java.lang.Double");
                          JvmFormalParameter _parameter_5 = this._jvmTypesBuilder.toParameter(meln, "maxY", _typeRef_17);
                          this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_5, _parameter_5);
                          StringConcatenationClient _client = new StringConcatenationClient() {
                            @Override
                            protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                              _builder.newLine();
                              _builder.append(firstRoleType, "");
                              _builder.append(" entity = new ");
                              _builder.append(firstRoleType, "");
                              _builder.append("();");
                              _builder.newLineIfNotEmpty();
                              _builder.append(gridType_1, "");
                              _builder.append(" grid = createSquare(\"");
                              _builder.append(graphcname, "");
                              _builder.append("\",entity.getProps(), minX, minY, maxX, maxY, xRes, yRes);");
                              _builder.newLineIfNotEmpty();
                              _builder.append("          \t\t\t    ");
                              _builder.append("fr.ocelet.runtime.raster.GridManager.getInstance().add(grid);");
                              _builder.newLine();
                              _builder.append("                  \t    ");
                              _builder.append("entity.setNumGrid(fr.ocelet.runtime.raster.GridManager.getInstance().getCurrentIndex());");
                              _builder.newLine();
                              _builder.append("                  \t    ");
                              _builder.append("setGrid(grid);");
                              _builder.newLine();
                              _builder.append("                  \t    ");
                              _builder.append("setCellShapeType(\"QUADRILATERAL\");");
                              _builder.newLine();
                            }
                          };
                          this._jvmTypesBuilder.setBody(it_1, _client);
                        };
                        JvmOperation _method_4 = this._jvmTypesBuilder.toMethod(meln, "createSquares", _typeRef_11, _function_11);
                        this._jvmTypesBuilder.<JvmOperation>operator_add(_members_5, _method_4);
                        EList<JvmMember> _members_6 = it.getMembers();
                        JvmTypeReference _typeRef_12 = this._typeReferenceBuilder.typeRef(Void.TYPE);
                        final Procedure1<JvmOperation> _function_12 = (JvmOperation it_1) -> {
                          EList<JvmFormalParameter> _parameters = it_1.getParameters();
                          JvmTypeReference _typeRef_13 = this._typeReferenceBuilder.typeRef("fr.ocelet.datafacer.ocltypes.Shapefile");
                          JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "shp", _typeRef_13);
                          this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                          EList<JvmFormalParameter> _parameters_1 = it_1.getParameters();
                          JvmTypeReference _typeRef_14 = this._typeReferenceBuilder.typeRef("java.lang.Double");
                          JvmFormalParameter _parameter_1 = this._jvmTypesBuilder.toParameter(meln, "size", _typeRef_14);
                          this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_1, _parameter_1);
                          StringConcatenationClient _client = new StringConcatenationClient() {
                            @Override
                            protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                              _builder.append(firstRoleType, "");
                              _builder.append(" entity = new ");
                              _builder.append(firstRoleType, "");
                              _builder.append("();");
                              _builder.newLineIfNotEmpty();
                              _builder.append(gridType_1, "");
                              _builder.append(" grid = createTriangle(\"");
                              _builder.append(graphcname, "");
                              _builder.append("\",entity.getProps(), shp.getBounds(), size);");
                              _builder.newLineIfNotEmpty();
                              _builder.append("fr.ocelet.runtime.raster.GridManager.getInstance().add(grid); ");
                              _builder.newLine();
                              _builder.append("                  \t   \t");
                              _builder.append("entity.setNumGrid(fr.ocelet.runtime.raster.GridManager.getInstance().getCurrentIndex());");
                              _builder.newLine();
                              _builder.append("                  \t   \t");
                              _builder.append("setGrid(grid);");
                              _builder.newLine();
                              _builder.append("                  \t   \t");
                              _builder.append("setCellShapeType(\"TRIANGULAR\");");
                              _builder.newLine();
                            }
                          };
                          this._jvmTypesBuilder.setBody(it_1, _client);
                        };
                        JvmOperation _method_5 = this._jvmTypesBuilder.toMethod(meln, "createTriangles", _typeRef_12, _function_12);
                        this._jvmTypesBuilder.<JvmOperation>operator_add(_members_6, _method_5);
                        EList<JvmMember> _members_7 = it.getMembers();
                        JvmTypeReference _typeRef_13 = this._typeReferenceBuilder.typeRef(Void.TYPE);
                        final Procedure1<JvmOperation> _function_13 = (JvmOperation it_1) -> {
                          EList<JvmFormalParameter> _parameters = it_1.getParameters();
                          JvmTypeReference _typeRef_14 = this._typeReferenceBuilder.typeRef("java.lang.Double");
                          JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "size", _typeRef_14);
                          this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                          EList<JvmFormalParameter> _parameters_1 = it_1.getParameters();
                          JvmTypeReference _typeRef_15 = this._typeReferenceBuilder.typeRef("java.lang.Double");
                          JvmFormalParameter _parameter_1 = this._jvmTypesBuilder.toParameter(meln, "minX", _typeRef_15);
                          this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_1, _parameter_1);
                          EList<JvmFormalParameter> _parameters_2 = it_1.getParameters();
                          JvmTypeReference _typeRef_16 = this._typeReferenceBuilder.typeRef("java.lang.Double");
                          JvmFormalParameter _parameter_2 = this._jvmTypesBuilder.toParameter(meln, "minY", _typeRef_16);
                          this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_2, _parameter_2);
                          EList<JvmFormalParameter> _parameters_3 = it_1.getParameters();
                          JvmTypeReference _typeRef_17 = this._typeReferenceBuilder.typeRef("java.lang.Double");
                          JvmFormalParameter _parameter_3 = this._jvmTypesBuilder.toParameter(meln, "maxX", _typeRef_17);
                          this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_3, _parameter_3);
                          EList<JvmFormalParameter> _parameters_4 = it_1.getParameters();
                          JvmTypeReference _typeRef_18 = this._typeReferenceBuilder.typeRef("java.lang.Double");
                          JvmFormalParameter _parameter_4 = this._jvmTypesBuilder.toParameter(meln, "maxY", _typeRef_18);
                          this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_4, _parameter_4);
                          StringConcatenationClient _client = new StringConcatenationClient() {
                            @Override
                            protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                              _builder.append(firstRoleType, "");
                              _builder.append(" entity = new ");
                              _builder.append(firstRoleType, "");
                              _builder.append("();");
                              _builder.newLineIfNotEmpty();
                              _builder.append(gridType_1, "");
                              _builder.append(" grid = createTriangle(\"");
                              _builder.append(graphcname, "");
                              _builder.append("\",entity.getProps(), minX, minY, maxX, maxY, size);");
                              _builder.newLineIfNotEmpty();
                              _builder.append("          \t\t\t \t");
                              _builder.append("fr.ocelet.runtime.raster.GridManager.getInstance().add(grid);");
                              _builder.newLine();
                              _builder.append("                  \t   \t");
                              _builder.append("entity.setNumGrid(fr.ocelet.runtime.raster.GridManager.getInstance().getCurrentIndex());");
                              _builder.newLine();
                              _builder.append("                  \t   \t");
                              _builder.append("setGrid(grid);");
                              _builder.newLine();
                              _builder.append("                  \t   \t");
                              _builder.append("setCellShapeType(\"TRIANGULAR\");");
                              _builder.newLine();
                            }
                          };
                          this._jvmTypesBuilder.setBody(it_1, _client);
                        };
                        JvmOperation _method_6 = this._jvmTypesBuilder.toMethod(meln, "createTriangles", _typeRef_13, _function_13);
                        this._jvmTypesBuilder.<JvmOperation>operator_add(_members_7, _method_6);
                        EList<RelElements> _relelns_3 = ((Relation)meln).getRelelns();
                        for (final RelElements reln_3 : _relelns_3) {
                          boolean _matched_6 = false;
                          if (!_matched_6) {
                            if (reln_3 instanceof RelPropertyDef) {
                              _matched_6=true;
                              EList<JvmMember> _members_8 = it.getMembers();
                              String _name_6 = ((RelPropertyDef)reln_3).getName();
                              String _firstUpper = StringExtensions.toFirstUpper(_name_6);
                              String _plus_1 = ("set" + _firstUpper);
                              JvmTypeReference _typeRef_14 = this._typeReferenceBuilder.typeRef(Void.TYPE);
                              final Procedure1<JvmOperation> _function_14 = (JvmOperation it_1) -> {
                                EList<JvmFormalParameter> _parameters = it_1.getParameters();
                                String _name_7 = ((RelPropertyDef)reln_3).getName();
                                JvmTypeReference _type_12 = ((RelPropertyDef)reln_3).getType();
                                JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(reln_3, _name_7, _type_12);
                                this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                                StringConcatenationClient _client = new StringConcatenationClient() {
                                  @Override
                                  protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                    _builder.newLine();
                                    _builder.append("for(");
                                    JvmTypeReference _typeRef = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef(edgecname);
                                    _builder.append(_typeRef, "");
                                    _builder.append(" _edg_ : this)");
                                    _builder.newLineIfNotEmpty();
                                    _builder.append("_edg_.set");
                                    String _name = ((RelPropertyDef)reln_3).getName();
                                    String _firstUpper = StringExtensions.toFirstUpper(_name);
                                    _builder.append(_firstUpper, "");
                                    _builder.append("(");
                                    String _name_1 = ((RelPropertyDef)reln_3).getName();
                                    _builder.append(_name_1, "");
                                    _builder.append(");");
                                    _builder.newLineIfNotEmpty();
                                  }
                                };
                                this._jvmTypesBuilder.setBody(it_1, _client);
                              };
                              JvmOperation _method_7 = this._jvmTypesBuilder.toMethod(reln_3, _plus_1, _typeRef_14, _function_14);
                              this._jvmTypesBuilder.<JvmOperation>operator_add(_members_8, _method_7);
                            }
                          }
                          if (!_matched_6) {
                            if (reln_3 instanceof InteractionDef) {
                              _matched_6=true;
                              EList<JvmMember> _members_8 = it.getMembers();
                              String _name_6 = ((InteractionDef)reln_3).getName();
                              JvmTypeReference _typeRef_14 = this._typeReferenceBuilder.typeRef(Void.TYPE);
                              final Procedure1<JvmOperation> _function_14 = (JvmOperation it_1) -> {
                                EList<JvmFormalParameter> _params = ((InteractionDef)reln_3).getParams();
                                for (final JvmFormalParameter p : _params) {
                                  EList<JvmFormalParameter> _parameters = it_1.getParameters();
                                  String _name_7 = p.getName();
                                  JvmTypeReference _parameterType = p.getParameterType();
                                  JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(reln_3, _name_7, _parameterType);
                                  this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                                }
                                StringConcatenationClient _client = new StringConcatenationClient() {
                                  @Override
                                  protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                    _builder.append("setMode(0);");
                                    _builder.newLine();
                                    _builder.append("                      \t \t");
                                    _builder.append("cleanOperator();");
                                    _builder.newLine();
                                    _builder.append("                      \t \t");
                                    _builder.append(listype, "                      \t \t");
                                    _builder.append(" cvtList = ((typeRef(edgecname))getEdge()).get_agr_");
                                    String _name = ((InteractionDef)reln_3).getName();
                                    _builder.append(_name, "                      \t \t");
                                    _builder.append("();");
                                    _builder.newLineIfNotEmpty();
                                    _builder.append("                      \t \t");
                                    _builder.append("if(cvtList != null){");
                                    _builder.newLine();
                                    _builder.append("                      \t \t\t");
                                    _builder.append("for(");
                                    _builder.append(aggregType, "                      \t \t\t");
                                    _builder.append(" cvt : cvtList){");
                                    _builder.newLineIfNotEmpty();
                                    _builder.append("                      \t \t\t\t");
                                    _builder.append("setCellOperator(cvt);");
                                    _builder.newLine();
                                    _builder.append("                      \t \t\t");
                                    _builder.append("} ");
                                    _builder.newLine();
                                    _builder.append("                      \t \t");
                                    _builder.append("}");
                                    _builder.newLine();
                                    _builder.append("                       \t\t");
                                    _builder.append("for(");
                                    JvmTypeReference _typeRef = OceletJvmModelInferrer.this._typeReferenceBuilder.typeRef(edgecname);
                                    _builder.append(_typeRef, "                       \t\t");
                                    _builder.append(" _edg_ : this) {");
                                    _builder.newLineIfNotEmpty();
                                    _builder.append("  ");
                                    _builder.append("_edg_.");
                                    String _name_1 = ((InteractionDef)reln_3).getName();
                                    _builder.append(_name_1, "  ");
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
                                        _builder.append(_name_2, "  ");
                                        Object _xblockexpression = null;
                                        {
                                          ci = 1;
                                          _xblockexpression = null;
                                        }
                                        _builder.append(_xblockexpression, "  ");
                                      }
                                    }
                                    _builder.append(");");
                                    _builder.newLineIfNotEmpty();
                                    _builder.newLine();
                                    _builder.append("}");
                                    _builder.newLine();
                                  }
                                };
                                this._jvmTypesBuilder.setBody(it_1, _client);
                              };
                              JvmOperation _method_7 = this._jvmTypesBuilder.toMethod(reln_3, _name_6, _typeRef_14, _function_14);
                              this._jvmTypesBuilder.<JvmOperation>operator_add(_members_8, _method_7);
                            }
                          }
                          if (!_matched_6) {
                            if (reln_3 instanceof Filterdef) {
                              _matched_6=true;
                              EList<JvmMember> _members_8 = it.getMembers();
                              String _name_6 = ((Filterdef)reln_3).getName();
                              String _string_6 = graphcname.toString();
                              JvmTypeReference _typeRef_14 = this._typeReferenceBuilder.typeRef(_string_6);
                              final Procedure1<JvmOperation> _function_14 = (JvmOperation it_1) -> {
                                EList<JvmFormalParameter> _params = ((Filterdef)reln_3).getParams();
                                for (final JvmFormalParameter p : _params) {
                                  EList<JvmFormalParameter> _parameters = it_1.getParameters();
                                  String _name_7 = p.getName();
                                  JvmTypeReference _parameterType = p.getParameterType();
                                  JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(reln_3, _name_7, _parameterType);
                                  this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                                }
                                StringConcatenationClient _client = new StringConcatenationClient() {
                                  @Override
                                  protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                    String _name = ((Relation)meln).getName();
                                    String _plus = (_name + "_");
                                    String _name_1 = ((Filterdef)reln_3).getName();
                                    String _plus_1 = (_plus + _name_1);
                                    _builder.append(_plus_1, "");
                                    _builder.append(" _filter = new ");
                                    String _name_2 = ((Relation)meln).getName();
                                    String _plus_2 = (_name_2 + "_");
                                    String _name_3 = ((Filterdef)reln_3).getName();
                                    String _plus_3 = (_plus_2 + _name_3);
                                    _builder.append(_plus_3, "");
                                    _builder.append("(");
                                    _builder.newLineIfNotEmpty();
                                    {
                                      EList<JvmFormalParameter> _params = ((Filterdef)reln_3).getParams();
                                      int _size = _params.size();
                                      boolean _greaterThan = (_size > 0);
                                      if (_greaterThan) {
                                        {
                                          EList<JvmFormalParameter> _params_1 = ((Filterdef)reln_3).getParams();
                                          int _size_1 = _params_1.size();
                                          int _minus = (_size_1 - 1);
                                          IntegerRange _upTo = new IntegerRange(0, _minus);
                                          for(final Integer i : _upTo) {
                                            EList<JvmFormalParameter> _params_2 = ((Filterdef)reln_3).getParams();
                                            JvmFormalParameter _get = _params_2.get((i).intValue());
                                            String _name_4 = _get.getName();
                                            _builder.append(_name_4, "");
                                            _builder.newLineIfNotEmpty();
                                            {
                                              EList<JvmFormalParameter> _params_3 = ((Filterdef)reln_3).getParams();
                                              int _size_2 = _params_3.size();
                                              int _minus_1 = (_size_2 - 1);
                                              boolean _lessThan = ((i).intValue() < _minus_1);
                                              if (_lessThan) {
                                                _builder.append("\t");
                                                _builder.append(",");
                                                _builder.newLine();
                                              }
                                            }
                                          }
                                        }
                                      }
                                    }
                                    _builder.append("\t\t");
                                    _builder.append(");");
                                    _builder.newLine();
                                    _builder.append("\t\t\t");
                                    _builder.newLine();
                                    _builder.append("                        ");
                                    _builder.append("super.addFilter(_filter);");
                                    _builder.newLine();
                                    _builder.append("                        ");
                                    _builder.append("return this;");
                                    _builder.newLine();
                                  }
                                };
                                this._jvmTypesBuilder.setBody(it_1, _client);
                              };
                              JvmOperation _method_7 = this._jvmTypesBuilder.toMethod(reln_3, _name_6, _typeRef_14, _function_14);
                              this._jvmTypesBuilder.<JvmOperation>operator_add(_members_8, _method_7);
                            }
                          }
                        }
                      };
                      acceptor.<JvmGenericType>accept(_class_5, _function_5);
                    }
                  }
                } else {
                  JvmGenericType _class_6 = this._jvmTypesBuilder.toClass(modl, edgecname);
                  final Procedure1<JvmGenericType> _function_6 = (JvmGenericType it) -> {
                    EList<JvmTypeReference> _superTypes = it.getSuperTypes();
                    JvmTypeReference _typeRef_5 = this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.relation.OcltEdge");
                    this._jvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes, _typeRef_5);
                    EList<Role> _roles_18 = ((Relation)meln).getRoles();
                    final Role firstRole_2 = _roles_18.get(0);
                    EList<Role> _roles_19 = ((Relation)meln).getRoles();
                    final Role secondRole_2 = _roles_19.get(1);
                    Entity _type_11 = firstRole_2.getType();
                    QualifiedName _fullyQualifiedName_7 = this._iQualifiedNameProvider.getFullyQualifiedName(_type_11);
                    String _string_5 = _fullyQualifiedName_7.toString();
                    final JvmTypeReference firstRoleType = this._typeReferenceBuilder.typeRef(_string_5);
                    Entity _type_12 = secondRole_2.getType();
                    QualifiedName _fullyQualifiedName_8 = this._iQualifiedNameProvider.getFullyQualifiedName(_type_12);
                    String _string_6 = _fullyQualifiedName_8.toString();
                    final JvmTypeReference secondRoleType = this._typeReferenceBuilder.typeRef(_string_6);
                    String _name_6 = firstRole_2.getName();
                    JvmField jvmField = this._jvmTypesBuilder.toField(meln, _name_6, firstRoleType);
                    boolean _notEquals_11 = (!Objects.equal(jvmField, null));
                    if (_notEquals_11) {
                      jvmField.setFinal(false);
                      EList<JvmMember> _members = it.getMembers();
                      this._jvmTypesBuilder.<JvmField>operator_add(_members, jvmField);
                      EList<JvmMember> _members_1 = it.getMembers();
                      String _name_7 = firstRole_2.getName();
                      JvmOperation _setter = this._jvmTypesBuilder.toSetter(meln, _name_7, firstRoleType);
                      this._jvmTypesBuilder.<JvmOperation>operator_add(_members_1, _setter);
                      EList<JvmMember> _members_2 = it.getMembers();
                      String _name_8 = firstRole_2.getName();
                      JvmOperation _getter = this._jvmTypesBuilder.toGetter(meln, _name_8, firstRoleType);
                      this._jvmTypesBuilder.<JvmOperation>operator_add(_members_2, _getter);
                    }
                    String _name_9 = secondRole_2.getName();
                    JvmField _field = this._jvmTypesBuilder.toField(meln, _name_9, secondRoleType);
                    jvmField = _field;
                    boolean _notEquals_12 = (!Objects.equal(jvmField, null));
                    if (_notEquals_12) {
                      jvmField.setFinal(false);
                      EList<JvmMember> _members_3 = it.getMembers();
                      this._jvmTypesBuilder.<JvmField>operator_add(_members_3, jvmField);
                      EList<JvmMember> _members_4 = it.getMembers();
                      String _name_10 = secondRole_2.getName();
                      JvmOperation _setter_1 = this._jvmTypesBuilder.toSetter(meln, _name_10, secondRoleType);
                      this._jvmTypesBuilder.<JvmOperation>operator_add(_members_4, _setter_1);
                      EList<JvmMember> _members_5 = it.getMembers();
                      String _name_11 = secondRole_2.getName();
                      JvmOperation _getter_1 = this._jvmTypesBuilder.toGetter(meln, _name_11, secondRoleType);
                      this._jvmTypesBuilder.<JvmOperation>operator_add(_members_5, _getter_1);
                    }
                    EList<JvmMember> _members_6 = it.getMembers();
                    final Procedure1<JvmConstructor> _function_7 = (JvmConstructor it_1) -> {
                      EList<JvmFormalParameter> _parameters = it_1.getParameters();
                      JvmTypeReference _typeRef_6 = this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.relation.InteractionGraph");
                      JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "igr", _typeRef_6);
                      this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                      EList<JvmFormalParameter> _parameters_1 = it_1.getParameters();
                      JvmFormalParameter _parameter_1 = this._jvmTypesBuilder.toParameter(meln, "first", firstRoleType);
                      this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_1, _parameter_1);
                      EList<JvmFormalParameter> _parameters_2 = it_1.getParameters();
                      JvmFormalParameter _parameter_2 = this._jvmTypesBuilder.toParameter(meln, "second", secondRoleType);
                      this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_2, _parameter_2);
                      StringConcatenationClient _client = new StringConcatenationClient() {
                        @Override
                        protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                          _builder.append("super(igr);");
                          _builder.newLine();
                          String _name = firstRole_2.getName();
                          _builder.append(_name, "");
                          _builder.append("=first;");
                          _builder.newLineIfNotEmpty();
                          String _name_1 = secondRole_2.getName();
                          _builder.append(_name_1, "");
                          _builder.append("=second;");
                          _builder.newLineIfNotEmpty();
                        }
                      };
                      this._jvmTypesBuilder.setBody(it_1, _client);
                    };
                    JvmConstructor _constructor = this._jvmTypesBuilder.toConstructor(meln, _function_7);
                    this._jvmTypesBuilder.<JvmConstructor>operator_add(_members_6, _constructor);
                    EList<JvmMember> _members_7 = it.getMembers();
                    JvmTypeReference _typeRef_6 = this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.relation.OcltRole");
                    final Procedure1<JvmOperation> _function_8 = (JvmOperation it_1) -> {
                      EList<JvmFormalParameter> _parameters = it_1.getParameters();
                      JvmTypeReference _typeRef_7 = this._typeReferenceBuilder.typeRef("int");
                      JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "i", _typeRef_7);
                      this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                      StringConcatenationClient _client = new StringConcatenationClient() {
                        @Override
                        protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                          _builder.append("if (i==0) return ");
                          String _name = firstRole_2.getName();
                          _builder.append(_name, "");
                          _builder.append(";");
                          _builder.newLineIfNotEmpty();
                          _builder.append("else if (i==1) return ");
                          String _name_1 = secondRole_2.getName();
                          _builder.append(_name_1, "");
                          _builder.append(";");
                          _builder.newLineIfNotEmpty();
                          _builder.append("else return null;");
                          _builder.newLine();
                        }
                      };
                      this._jvmTypesBuilder.setBody(it_1, _client);
                    };
                    JvmOperation _method = this._jvmTypesBuilder.toMethod(meln, "getRole", _typeRef_6, _function_8);
                    this._jvmTypesBuilder.<JvmOperation>operator_add(_members_7, _method);
                    EList<RelElements> _relelns_3 = ((Relation)meln).getRelelns();
                    for (final RelElements reln_3 : _relelns_3) {
                      boolean _matched_6 = false;
                      if (!_matched_6) {
                        if (reln_3 instanceof RelPropertyDef) {
                          _matched_6=true;
                          String _name_12 = ((RelPropertyDef)reln_3).getName();
                          JvmTypeReference _type_13 = ((RelPropertyDef)reln_3).getType();
                          final JvmField rField = this._jvmTypesBuilder.toField(reln_3, _name_12, _type_13);
                          boolean _notEquals_13 = (!Objects.equal(rField, null));
                          if (_notEquals_13) {
                            rField.setFinal(false);
                            EList<JvmMember> _members_8 = it.getMembers();
                            this._jvmTypesBuilder.<JvmField>operator_add(_members_8, rField);
                            EList<JvmMember> _members_9 = it.getMembers();
                            String _name_13 = ((RelPropertyDef)reln_3).getName();
                            JvmTypeReference _type_14 = ((RelPropertyDef)reln_3).getType();
                            JvmOperation _setter_2 = this._jvmTypesBuilder.toSetter(reln_3, _name_13, _type_14);
                            this._jvmTypesBuilder.<JvmOperation>operator_add(_members_9, _setter_2);
                            EList<JvmMember> _members_10 = it.getMembers();
                            String _name_14 = ((RelPropertyDef)reln_3).getName();
                            JvmTypeReference _type_15 = ((RelPropertyDef)reln_3).getType();
                            JvmOperation _getter_2 = this._jvmTypesBuilder.toGetter(reln_3, _name_14, _type_15);
                            this._jvmTypesBuilder.<JvmOperation>operator_add(_members_10, _getter_2);
                          }
                        }
                      }
                      if (!_matched_6) {
                        if (reln_3 instanceof InteractionDef) {
                          _matched_6=true;
                          EList<JvmMember> _members_8 = it.getMembers();
                          String _name_12 = ((InteractionDef)reln_3).getName();
                          JvmTypeReference _typeRef_7 = this._typeReferenceBuilder.typeRef(Void.TYPE);
                          final Procedure1<JvmOperation> _function_9 = (JvmOperation it_1) -> {
                            EList<JvmFormalParameter> _params = ((InteractionDef)reln_3).getParams();
                            for (final JvmFormalParameter p : _params) {
                              EList<JvmFormalParameter> _parameters = it_1.getParameters();
                              String _name_13 = p.getName();
                              JvmTypeReference _parameterType = p.getParameterType();
                              JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(reln_3, _name_13, _parameterType);
                              this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                            }
                            XExpression _body = ((InteractionDef)reln_3).getBody();
                            this._jvmTypesBuilder.setBody(it_1, _body);
                          };
                          JvmOperation _method_1 = this._jvmTypesBuilder.toMethod(reln_3, _name_12, _typeRef_7, _function_9);
                          this._jvmTypesBuilder.<JvmOperation>operator_add(_members_8, _method_1);
                          EList<Comitexpr> _comitexpressions = ((InteractionDef)reln_3).getComitexpressions();
                          int _size_2 = _comitexpressions.size();
                          boolean _greaterThan_1 = (_size_2 > 0);
                          if (_greaterThan_1) {
                            EList<JvmMember> _members_9 = it.getMembers();
                            String _name_13 = ((InteractionDef)reln_3).getName();
                            String _plus_1 = ("_agr_" + _name_13);
                            JvmTypeReference _typeRef_8 = this._typeReferenceBuilder.typeRef(Void.TYPE);
                            final Procedure1<JvmOperation> _function_10 = (JvmOperation it_1) -> {
                              StringConcatenationClient _client = new StringConcatenationClient() {
                                @Override
                                protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                  {
                                    EList<Comitexpr> _comitexpressions = ((InteractionDef)reln_3).getComitexpressions();
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
                              this._jvmTypesBuilder.setBody(it_1, _client);
                            };
                            JvmOperation _method_2 = this._jvmTypesBuilder.toMethod(reln_3, _plus_1, _typeRef_8, _function_10);
                            this._jvmTypesBuilder.<JvmOperation>operator_add(_members_9, _method_2);
                          }
                        }
                      }
                    }
                  };
                  acceptor.<JvmGenericType>accept(_class_6, _function_6);
                  EList<RelElements> _relelns_3 = ((Relation)meln).getRelelns();
                  for (final RelElements reln_3 : _relelns_3) {
                    boolean _matched_6 = false;
                    if (!_matched_6) {
                      if (reln_3 instanceof Filterdef) {
                        _matched_6=true;
                        String _plus_1 = (graphcname + "_");
                        String _name_6 = ((Filterdef)reln_3).getName();
                        final String filterfqn = (_plus_1 + _name_6);
                        JvmGenericType _class_7 = this._jvmTypesBuilder.toClass(modl, filterfqn);
                        final Procedure1<JvmGenericType> _function_7 = (JvmGenericType it) -> {
                          boolean _and_8 = false;
                          boolean _and_9 = false;
                          boolean _and_10 = false;
                          boolean _and_11 = false;
                          boolean _and_12 = false;
                          boolean _and_13 = false;
                          boolean _and_14 = false;
                          boolean _and_15 = false;
                          EList<Role> _roles_18 = ((Relation)meln).getRoles();
                          int _size_2 = _roles_18.size();
                          boolean _greaterEqualsThan_1 = (_size_2 >= 2);
                          if (!_greaterEqualsThan_1) {
                            _and_15 = false;
                          } else {
                            EList<Role> _roles_19 = ((Relation)meln).getRoles();
                            Role _get_12 = _roles_19.get(0);
                            boolean _notEquals_11 = (!Objects.equal(_get_12, null));
                            _and_15 = _notEquals_11;
                          }
                          if (!_and_15) {
                            _and_14 = false;
                          } else {
                            EList<Role> _roles_20 = ((Relation)meln).getRoles();
                            Role _get_13 = _roles_20.get(1);
                            boolean _notEquals_12 = (!Objects.equal(_get_13, null));
                            _and_14 = _notEquals_12;
                          }
                          if (!_and_14) {
                            _and_13 = false;
                          } else {
                            EList<Role> _roles_21 = ((Relation)meln).getRoles();
                            Role _get_14 = _roles_21.get(0);
                            Entity _type_11 = _get_14.getType();
                            boolean _notEquals_13 = (!Objects.equal(_type_11, null));
                            _and_13 = _notEquals_13;
                          }
                          if (!_and_13) {
                            _and_12 = false;
                          } else {
                            EList<Role> _roles_22 = ((Relation)meln).getRoles();
                            Role _get_15 = _roles_22.get(1);
                            Entity _type_12 = _get_15.getType();
                            boolean _notEquals_14 = (!Objects.equal(_type_12, null));
                            _and_12 = _notEquals_14;
                          }
                          if (!_and_12) {
                            _and_11 = false;
                          } else {
                            EList<Role> _roles_23 = ((Relation)meln).getRoles();
                            Role _get_16 = _roles_23.get(0);
                            Entity _type_13 = _get_16.getType();
                            QualifiedName _fullyQualifiedName_7 = this._iQualifiedNameProvider.getFullyQualifiedName(_type_13);
                            boolean _notEquals_15 = (!Objects.equal(_fullyQualifiedName_7, null));
                            _and_11 = _notEquals_15;
                          }
                          if (!_and_11) {
                            _and_10 = false;
                          } else {
                            EList<Role> _roles_24 = ((Relation)meln).getRoles();
                            Role _get_17 = _roles_24.get(1);
                            Entity _type_14 = _get_17.getType();
                            QualifiedName _fullyQualifiedName_8 = this._iQualifiedNameProvider.getFullyQualifiedName(_type_14);
                            boolean _notEquals_16 = (!Objects.equal(_fullyQualifiedName_8, null));
                            _and_10 = _notEquals_16;
                          }
                          if (!_and_10) {
                            _and_9 = false;
                          } else {
                            EList<Role> _roles_25 = ((Relation)meln).getRoles();
                            Role _get_18 = _roles_25.get(0);
                            String _name_7 = _get_18.getName();
                            boolean _notEquals_17 = (!Objects.equal(_name_7, null));
                            _and_9 = _notEquals_17;
                          }
                          if (!_and_9) {
                            _and_8 = false;
                          } else {
                            EList<Role> _roles_26 = ((Relation)meln).getRoles();
                            Role _get_19 = _roles_26.get(1);
                            String _name_8 = _get_19.getName();
                            boolean _notEquals_18 = (!Objects.equal(_name_8, null));
                            _and_8 = _notEquals_18;
                          }
                          if (_and_8) {
                            EList<Role> _roles_27 = ((Relation)meln).getRoles();
                            final Role firstRole_2 = _roles_27.get(0);
                            EList<Role> _roles_28 = ((Relation)meln).getRoles();
                            final Role secondRole_2 = _roles_28.get(1);
                            Entity _type_15 = firstRole_2.getType();
                            QualifiedName _fullyQualifiedName_9 = this._iQualifiedNameProvider.getFullyQualifiedName(_type_15);
                            String _string_5 = _fullyQualifiedName_9.toString();
                            final JvmTypeReference firstRoleType = this._typeReferenceBuilder.typeRef(_string_5);
                            Entity _type_16 = secondRole_2.getType();
                            QualifiedName _fullyQualifiedName_10 = this._iQualifiedNameProvider.getFullyQualifiedName(_type_16);
                            String _string_6 = _fullyQualifiedName_10.toString();
                            final JvmTypeReference secondRoleType = this._typeReferenceBuilder.typeRef(_string_6);
                            EList<JvmTypeReference> _superTypes = it.getSuperTypes();
                            JvmTypeReference _typeRef_5 = this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.relation.EdgeFilter", firstRoleType, secondRoleType);
                            this._jvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes, _typeRef_5);
                            EList<JvmFormalParameter> _params = ((Filterdef)reln_3).getParams();
                            for (final JvmFormalParameter p : _params) {
                              {
                                String _name_9 = p.getName();
                                JvmTypeReference _parameterType = p.getParameterType();
                                final JvmField pfield = this._jvmTypesBuilder.toField(reln_3, _name_9, _parameterType);
                                boolean _notEquals_19 = (!Objects.equal(pfield, null));
                                if (_notEquals_19) {
                                  pfield.setFinal(false);
                                  EList<JvmMember> _members = it.getMembers();
                                  this._jvmTypesBuilder.<JvmField>operator_add(_members, pfield);
                                }
                              }
                            }
                            EList<JvmMember> _members = it.getMembers();
                            final Procedure1<JvmConstructor> _function_8 = (JvmConstructor it_1) -> {
                              EList<JvmFormalParameter> _params_1 = ((Filterdef)reln_3).getParams();
                              for (final JvmFormalParameter p_1 : _params_1) {
                                EList<JvmFormalParameter> _parameters = it_1.getParameters();
                                String _name_9 = p_1.getName();
                                JvmTypeReference _parameterType = p_1.getParameterType();
                                JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(reln_3, _name_9, _parameterType);
                                this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                              }
                              StringConcatenationClient _client = new StringConcatenationClient() {
                                @Override
                                protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                  {
                                    EList<JvmFormalParameter> _params = ((Filterdef)reln_3).getParams();
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
                              this._jvmTypesBuilder.setBody(it_1, _client);
                            };
                            JvmConstructor _constructor = this._jvmTypesBuilder.toConstructor(reln_3, _function_8);
                            this._jvmTypesBuilder.<JvmConstructor>operator_add(_members, _constructor);
                            EList<JvmMember> _members_1 = it.getMembers();
                            JvmTypeReference _typeRef_6 = this._typeReferenceBuilder.typeRef("java.lang.Boolean");
                            final Procedure1<JvmOperation> _function_9 = (JvmOperation it_1) -> {
                              EList<JvmFormalParameter> _parameters = it_1.getParameters();
                              String _name_9 = firstRole_2.getName();
                              JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(reln_3, _name_9, firstRoleType);
                              this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                              EList<JvmFormalParameter> _parameters_1 = it_1.getParameters();
                              String _name_10 = secondRole_2.getName();
                              JvmFormalParameter _parameter_1 = this._jvmTypesBuilder.toParameter(reln_3, _name_10, secondRoleType);
                              this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_1, _parameter_1);
                              XExpression _body = ((Filterdef)reln_3).getBody();
                              this._jvmTypesBuilder.setBody(it_1, _body);
                            };
                            JvmOperation _method = this._jvmTypesBuilder.toMethod(reln_3, "filter", _typeRef_6, _function_9);
                            this._jvmTypesBuilder.<JvmOperation>operator_add(_members_1, _method);
                          }
                        };
                        acceptor.<JvmGenericType>accept(_class_7, _function_7);
                      }
                    }
                  }
                  JvmTypeReference _typeRef_5 = this._typeReferenceBuilder.typeRef(edgecname);
                  boolean _notEquals_11 = (!Objects.equal(_typeRef_5, null));
                  if (_notEquals_11) {
                    JvmGenericType _class_7 = this._jvmTypesBuilder.toClass(modl, graphcname);
                    final Procedure1<JvmGenericType> _function_7 = (JvmGenericType it) -> {
                      String _documentation = this._jvmTypesBuilder.getDocumentation(meln);
                      this._jvmTypesBuilder.setDocumentation(it, _documentation);
                      boolean _and_8 = false;
                      boolean _and_9 = false;
                      boolean _and_10 = false;
                      boolean _and_11 = false;
                      boolean _and_12 = false;
                      boolean _and_13 = false;
                      boolean _and_14 = false;
                      boolean _and_15 = false;
                      EList<Role> _roles_18 = ((Relation)meln).getRoles();
                      int _size_2 = _roles_18.size();
                      boolean _greaterEqualsThan_1 = (_size_2 >= 2);
                      if (!_greaterEqualsThan_1) {
                        _and_15 = false;
                      } else {
                        EList<Role> _roles_19 = ((Relation)meln).getRoles();
                        Role _get_12 = _roles_19.get(0);
                        boolean _notEquals_12 = (!Objects.equal(_get_12, null));
                        _and_15 = _notEquals_12;
                      }
                      if (!_and_15) {
                        _and_14 = false;
                      } else {
                        EList<Role> _roles_20 = ((Relation)meln).getRoles();
                        Role _get_13 = _roles_20.get(1);
                        boolean _notEquals_13 = (!Objects.equal(_get_13, null));
                        _and_14 = _notEquals_13;
                      }
                      if (!_and_14) {
                        _and_13 = false;
                      } else {
                        EList<Role> _roles_21 = ((Relation)meln).getRoles();
                        Role _get_14 = _roles_21.get(0);
                        Entity _type_11 = _get_14.getType();
                        boolean _notEquals_14 = (!Objects.equal(_type_11, null));
                        _and_13 = _notEquals_14;
                      }
                      if (!_and_13) {
                        _and_12 = false;
                      } else {
                        EList<Role> _roles_22 = ((Relation)meln).getRoles();
                        Role _get_15 = _roles_22.get(1);
                        Entity _type_12 = _get_15.getType();
                        boolean _notEquals_15 = (!Objects.equal(_type_12, null));
                        _and_12 = _notEquals_15;
                      }
                      if (!_and_12) {
                        _and_11 = false;
                      } else {
                        EList<Role> _roles_23 = ((Relation)meln).getRoles();
                        Role _get_16 = _roles_23.get(0);
                        Entity _type_13 = _get_16.getType();
                        QualifiedName _fullyQualifiedName_7 = this._iQualifiedNameProvider.getFullyQualifiedName(_type_13);
                        boolean _notEquals_16 = (!Objects.equal(_fullyQualifiedName_7, null));
                        _and_11 = _notEquals_16;
                      }
                      if (!_and_11) {
                        _and_10 = false;
                      } else {
                        EList<Role> _roles_24 = ((Relation)meln).getRoles();
                        Role _get_17 = _roles_24.get(1);
                        Entity _type_14 = _get_17.getType();
                        QualifiedName _fullyQualifiedName_8 = this._iQualifiedNameProvider.getFullyQualifiedName(_type_14);
                        boolean _notEquals_17 = (!Objects.equal(_fullyQualifiedName_8, null));
                        _and_10 = _notEquals_17;
                      }
                      if (!_and_10) {
                        _and_9 = false;
                      } else {
                        EList<Role> _roles_25 = ((Relation)meln).getRoles();
                        Role _get_18 = _roles_25.get(0);
                        String _name_6 = _get_18.getName();
                        boolean _notEquals_18 = (!Objects.equal(_name_6, null));
                        _and_9 = _notEquals_18;
                      }
                      if (!_and_9) {
                        _and_8 = false;
                      } else {
                        EList<Role> _roles_26 = ((Relation)meln).getRoles();
                        Role _get_19 = _roles_26.get(1);
                        String _name_7 = _get_19.getName();
                        boolean _notEquals_19 = (!Objects.equal(_name_7, null));
                        _and_8 = _notEquals_19;
                      }
                      if (_and_8) {
                        EList<Role> _roles_27 = ((Relation)meln).getRoles();
                        final Role firstRole_2 = _roles_27.get(0);
                        EList<Role> _roles_28 = ((Relation)meln).getRoles();
                        final Role secondRole_2 = _roles_28.get(1);
                        Entity _type_15 = firstRole_2.getType();
                        QualifiedName _fullyQualifiedName_9 = this._iQualifiedNameProvider.getFullyQualifiedName(_type_15);
                        String _string_5 = _fullyQualifiedName_9.toString();
                        final JvmTypeReference firstRoleType = this._typeReferenceBuilder.typeRef(_string_5);
                        Entity _type_16 = secondRole_2.getType();
                        QualifiedName _fullyQualifiedName_10 = this._iQualifiedNameProvider.getFullyQualifiedName(_type_16);
                        String _string_6 = _fullyQualifiedName_10.toString();
                        final JvmTypeReference secondRoleType = this._typeReferenceBuilder.typeRef(_string_6);
                        EList<Role> _roles_29 = ((Relation)meln).getRoles();
                        Role _get_20 = _roles_29.get(0);
                        String _name_8 = _get_20.getName();
                        final String rolset1 = (_name_8 + "Set");
                        EList<Role> _roles_30 = ((Relation)meln).getRoles();
                        Role _get_21 = _roles_30.get(1);
                        String _name_9 = _get_21.getName();
                        final String rolset2 = (_name_9 + "Set");
                        if (isAutoGraph) {
                          EList<JvmTypeReference> _superTypes = it.getSuperTypes();
                          JvmTypeReference _typeRef_6 = this._typeReferenceBuilder.typeRef(edgecname);
                          JvmTypeReference _typeRef_7 = this._typeReferenceBuilder.typeRef(graphTypeName, _typeRef_6, firstRoleType);
                          this._jvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes, _typeRef_7);
                        } else {
                          EList<JvmTypeReference> _superTypes_1 = it.getSuperTypes();
                          JvmTypeReference _typeRef_8 = this._typeReferenceBuilder.typeRef(edgecname);
                          JvmTypeReference _typeRef_9 = this._typeReferenceBuilder.typeRef(graphTypeName, _typeRef_8, firstRoleType, secondRoleType);
                          this._jvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes_1, _typeRef_9);
                        }
                        EList<JvmMember> _members = it.getMembers();
                        final Procedure1<JvmConstructor> _function_8 = (JvmConstructor it_1) -> {
                          StringConcatenationClient _client = new StringConcatenationClient() {
                            @Override
                            protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                              _builder.append("super();");
                            }
                          };
                          this._jvmTypesBuilder.setBody(it_1, _client);
                        };
                        JvmConstructor _constructor = this._jvmTypesBuilder.toConstructor(meln, _function_8);
                        this._jvmTypesBuilder.<JvmConstructor>operator_add(_members, _constructor);
                        EList<JvmMember> _members_1 = it.getMembers();
                        JvmTypeReference _typeRef_10 = this._typeReferenceBuilder.typeRef(edgecname);
                        final Procedure1<JvmOperation> _function_9 = (JvmOperation it_1) -> {
                          EList<JvmFormalParameter> _parameters = it_1.getParameters();
                          String _name_10 = firstRole_2.getName();
                          JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, _name_10, firstRoleType);
                          this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                          EList<JvmFormalParameter> _parameters_1 = it_1.getParameters();
                          String _name_11 = secondRole_2.getName();
                          JvmFormalParameter _parameter_1 = this._jvmTypesBuilder.toParameter(meln, _name_11, secondRoleType);
                          this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters_1, _parameter_1);
                          StringConcatenationClient _client = new StringConcatenationClient() {
                            @Override
                            protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                              _builder.append("if ((this.");
                              _builder.append(rolset1, "");
                              _builder.append(" == null) || (!this.");
                              _builder.append(rolset1, "");
                              _builder.append(".contains(");
                              String _name = firstRole_2.getName();
                              _builder.append(_name, "");
                              _builder.append("))) add(");
                              String _name_1 = firstRole_2.getName();
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
                                  String _name_2 = secondRole_2.getName();
                                  _builder.append(_name_2, "");
                                  _builder.append("))) add(");
                                  String _name_3 = secondRole_2.getName();
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
                              String _name_5 = firstRole_2.getName();
                              _builder.append(_name_5, "");
                              _builder.append(",");
                              String _name_6 = secondRole_2.getName();
                              _builder.append(_name_6, "");
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
                        JvmOperation _method = this._jvmTypesBuilder.toMethod(meln, "connect", _typeRef_10, _function_9);
                        this._jvmTypesBuilder.<JvmOperation>operator_add(_members_1, _method);
                        EList<JvmMember> _members_2 = it.getMembers();
                        JvmTypeReference _typeRef_11 = this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.relation.RoleSet", firstRoleType);
                        final Procedure1<JvmOperation> _function_10 = (JvmOperation it_1) -> {
                          StringConcatenationClient _client = new StringConcatenationClient() {
                            @Override
                            protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                              _builder.append("return ");
                              _builder.append(rolset1, "");
                              _builder.append(";");
                            }
                          };
                          this._jvmTypesBuilder.setBody(it_1, _client);
                        };
                        JvmOperation _method_1 = this._jvmTypesBuilder.toMethod(meln, "getLeftSet", _typeRef_11, _function_10);
                        this._jvmTypesBuilder.<JvmOperation>operator_add(_members_2, _method_1);
                        EList<JvmMember> _members_3 = it.getMembers();
                        JvmTypeReference _typeRef_12 = this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.relation.RoleSet", secondRoleType);
                        final Procedure1<JvmOperation> _function_11 = (JvmOperation it_1) -> {
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
                          this._jvmTypesBuilder.setBody(it_1, _client);
                        };
                        JvmOperation _method_2 = this._jvmTypesBuilder.toMethod(meln, "getRightSet", _typeRef_12, _function_11);
                        this._jvmTypesBuilder.<JvmOperation>operator_add(_members_3, _method_2);
                        EList<JvmMember> _members_4 = it.getMembers();
                        String _string_7 = graphcname.toString();
                        JvmTypeReference _typeRef_13 = this._typeReferenceBuilder.typeRef(_string_7);
                        final Procedure1<JvmOperation> _function_12 = (JvmOperation it_1) -> {
                          StringConcatenationClient _client = new StringConcatenationClient() {
                            @Override
                            protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                              _builder.append("return (");
                              String _name = ((Relation)meln).getName();
                              _builder.append(_name, "");
                              _builder.append(")super.getComplete();");
                            }
                          };
                          this._jvmTypesBuilder.setBody(it_1, _client);
                        };
                        JvmOperation _method_3 = this._jvmTypesBuilder.toMethod(meln, "getComplete", _typeRef_13, _function_12);
                        this._jvmTypesBuilder.<JvmOperation>operator_add(_members_4, _method_3);
                        EList<JvmMember> _members_5 = it.getMembers();
                        JvmTypeReference _typeRef_14 = this._typeReferenceBuilder.typeRef(edgecname);
                        final Procedure1<JvmOperation> _function_13 = (JvmOperation it_1) -> {
                          EList<JvmFormalParameter> _parameters = it_1.getParameters();
                          String _name_10 = firstRole_2.getName();
                          JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(firstRole_2, _name_10, firstRoleType);
                          this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                          EList<JvmFormalParameter> _parameters_1 = it_1.getParameters();
                          String _name_11 = secondRole_2.getName();
                          JvmFormalParameter _parameter_1 = this._jvmTypesBuilder.toParameter(secondRole_2, _name_11, secondRoleType);
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
                              String _name_1 = firstRole_2.getName();
                              _builder.append(_name_1, " ");
                              _builder.append(",");
                              String _name_2 = secondRole_2.getName();
                              _builder.append(_name_2, " ");
                              _builder.append(");");
                            }
                          };
                          this._jvmTypesBuilder.setBody(it_1, _client);
                        };
                        JvmOperation _method_4 = this._jvmTypesBuilder.toMethod(meln, "createEdge", _typeRef_14, _function_13);
                        this._jvmTypesBuilder.<JvmOperation>operator_add(_members_5, _method_4);
                        final JvmTypeReference rsetype = this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.relation.RoleSet", firstRoleType);
                        final JvmField rsfield = this._jvmTypesBuilder.toField(meln, rolset1, rsetype);
                        boolean _notEquals_20 = (!Objects.equal(rsfield, null));
                        if (_notEquals_20) {
                          EList<JvmMember> _members_6 = it.getMembers();
                          this._jvmTypesBuilder.<JvmField>operator_add(_members_6, rsfield);
                          EList<JvmMember> _members_7 = it.getMembers();
                          String _firstUpper = StringExtensions.toFirstUpper(rolset1);
                          String _plus_1 = ("set" + _firstUpper);
                          JvmTypeReference _typeRef_15 = this._typeReferenceBuilder.typeRef(Void.TYPE);
                          final Procedure1<JvmOperation> _function_14 = (JvmOperation it_1) -> {
                            EList<JvmFormalParameter> _parameters = it_1.getParameters();
                            JvmTypeReference _typeRef_16 = this._typeReferenceBuilder.typeRef("java.util.Collection", firstRoleType);
                            JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(firstRole_2, "croles", _typeRef_16);
                            this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
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
                            this._jvmTypesBuilder.setBody(it_1, _client);
                          };
                          JvmOperation _method_5 = this._jvmTypesBuilder.toMethod(meln, _plus_1, _typeRef_15, _function_14);
                          this._jvmTypesBuilder.<JvmOperation>operator_add(_members_7, _method_5);
                          EList<JvmMember> _members_8 = it.getMembers();
                          String _firstUpper_1 = StringExtensions.toFirstUpper(rolset1);
                          String _plus_2 = ("get" + _firstUpper_1);
                          final Procedure1<JvmOperation> _function_15 = (JvmOperation it_1) -> {
                            StringConcatenationClient _client = new StringConcatenationClient() {
                              @Override
                              protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                _builder.append("return ");
                                _builder.append(rolset1, "");
                                _builder.append(";");
                              }
                            };
                            this._jvmTypesBuilder.setBody(it_1, _client);
                          };
                          JvmOperation _method_6 = this._jvmTypesBuilder.toMethod(meln, _plus_2, rsetype, _function_15);
                          this._jvmTypesBuilder.<JvmOperation>operator_add(_members_8, _method_6);
                          if ((!isAutoGraph)) {
                            final JvmTypeReference rsetype2 = this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.relation.RoleSet", secondRoleType);
                            final JvmField rsfield2 = this._jvmTypesBuilder.toField(meln, rolset2, rsetype2);
                            boolean _notEquals_21 = (!Objects.equal(rsfield2, null));
                            if (_notEquals_21) {
                              EList<JvmMember> _members_9 = it.getMembers();
                              this._jvmTypesBuilder.<JvmField>operator_add(_members_9, rsfield2);
                              EList<JvmMember> _members_10 = it.getMembers();
                              String _firstUpper_2 = StringExtensions.toFirstUpper(rolset2);
                              String _plus_3 = ("set" + _firstUpper_2);
                              JvmTypeReference _typeRef_16 = this._typeReferenceBuilder.typeRef(Void.TYPE);
                              final Procedure1<JvmOperation> _function_16 = (JvmOperation it_1) -> {
                                EList<JvmFormalParameter> _parameters = it_1.getParameters();
                                JvmTypeReference _typeRef_17 = this._typeReferenceBuilder.typeRef("java.util.Collection", secondRoleType);
                                JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(secondRole_2, "croles", _typeRef_17);
                                this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
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
                                this._jvmTypesBuilder.setBody(it_1, _client);
                              };
                              JvmOperation _method_7 = this._jvmTypesBuilder.toMethod(meln, _plus_3, _typeRef_16, _function_16);
                              this._jvmTypesBuilder.<JvmOperation>operator_add(_members_10, _method_7);
                              EList<JvmMember> _members_11 = it.getMembers();
                              String _firstUpper_3 = StringExtensions.toFirstUpper(rolset2);
                              String _plus_4 = ("get" + _firstUpper_3);
                              final Procedure1<JvmOperation> _function_17 = (JvmOperation it_1) -> {
                                StringConcatenationClient _client = new StringConcatenationClient() {
                                  @Override
                                  protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                    _builder.append("return ");
                                    _builder.append(rolset2, "");
                                    _builder.append(";");
                                  }
                                };
                                this._jvmTypesBuilder.setBody(it_1, _client);
                              };
                              JvmOperation _method_8 = this._jvmTypesBuilder.toMethod(meln, _plus_4, rsetype2, _function_17);
                              this._jvmTypesBuilder.<JvmOperation>operator_add(_members_11, _method_8);
                            }
                          }
                          EList<JvmMember> _members_12 = it.getMembers();
                          JvmTypeReference _typeRef_17 = this._typeReferenceBuilder.typeRef(Void.TYPE);
                          final Procedure1<JvmOperation> _function_18 = (JvmOperation it_1) -> {
                            EList<JvmFormalParameter> _parameters = it_1.getParameters();
                            JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "role", firstRoleType);
                            this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                            StringConcatenationClient _client = new StringConcatenationClient() {
                              @Override
                              protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                _builder.append("add");
                                _builder.append(firstRoleType, "");
                                _builder.append("(role);");
                              }
                            };
                            this._jvmTypesBuilder.setBody(it_1, _client);
                          };
                          JvmOperation _method_9 = this._jvmTypesBuilder.toMethod(meln, "add", _typeRef_17, _function_18);
                          this._jvmTypesBuilder.<JvmOperation>operator_add(_members_12, _method_9);
                          EList<JvmMember> _members_13 = it.getMembers();
                          JvmTypeReference _typeRef_18 = this._typeReferenceBuilder.typeRef(Void.TYPE);
                          final Procedure1<JvmOperation> _function_19 = (JvmOperation it_1) -> {
                            EList<JvmFormalParameter> _parameters = it_1.getParameters();
                            JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "role", firstRoleType);
                            this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                            StringConcatenationClient _client = new StringConcatenationClient() {
                              @Override
                              protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                _builder.append("remove");
                                _builder.append(firstRoleType, "");
                                _builder.append("(role);");
                              }
                            };
                            this._jvmTypesBuilder.setBody(it_1, _client);
                          };
                          JvmOperation _method_10 = this._jvmTypesBuilder.toMethod(meln, "remove", _typeRef_18, _function_19);
                          this._jvmTypesBuilder.<JvmOperation>operator_add(_members_13, _method_10);
                          EList<JvmMember> _members_14 = it.getMembers();
                          String _simpleName = firstRoleType.getSimpleName();
                          String _plus_5 = ("add" + _simpleName);
                          JvmTypeReference _typeRef_19 = this._typeReferenceBuilder.typeRef(Void.TYPE);
                          final Procedure1<JvmOperation> _function_20 = (JvmOperation it_1) -> {
                            EList<JvmFormalParameter> _parameters = it_1.getParameters();
                            JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "role", firstRoleType);
                            this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
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
                            this._jvmTypesBuilder.setBody(it_1, _client);
                          };
                          JvmOperation _method_11 = this._jvmTypesBuilder.toMethod(meln, _plus_5, _typeRef_19, _function_20);
                          this._jvmTypesBuilder.<JvmOperation>operator_add(_members_14, _method_11);
                          EList<JvmMember> _members_15 = it.getMembers();
                          String _simpleName_1 = firstRoleType.getSimpleName();
                          String _plus_6 = ("remove" + _simpleName_1);
                          JvmTypeReference _typeRef_20 = this._typeReferenceBuilder.typeRef(Void.TYPE);
                          final Procedure1<JvmOperation> _function_21 = (JvmOperation it_1) -> {
                            EList<JvmFormalParameter> _parameters = it_1.getParameters();
                            JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "role", firstRoleType);
                            this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
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
                            this._jvmTypesBuilder.setBody(it_1, _client);
                          };
                          JvmOperation _method_12 = this._jvmTypesBuilder.toMethod(meln, _plus_6, _typeRef_20, _function_21);
                          this._jvmTypesBuilder.<JvmOperation>operator_add(_members_15, _method_12);
                          EList<JvmMember> _members_16 = it.getMembers();
                          String _simpleName_2 = firstRoleType.getSimpleName();
                          String _plus_7 = ("addAll" + _simpleName_2);
                          JvmTypeReference _typeRef_21 = this._typeReferenceBuilder.typeRef(Void.TYPE);
                          final Procedure1<JvmOperation> _function_22 = (JvmOperation it_1) -> {
                            EList<JvmFormalParameter> _parameters = it_1.getParameters();
                            JvmTypeReference _typeRef_22 = this._typeReferenceBuilder.typeRef("java.lang.Iterable", firstRoleType);
                            JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "roles", _typeRef_22);
                            this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
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
                            this._jvmTypesBuilder.setBody(it_1, _client);
                          };
                          JvmOperation _method_13 = this._jvmTypesBuilder.toMethod(meln, _plus_7, _typeRef_21, _function_22);
                          this._jvmTypesBuilder.<JvmOperation>operator_add(_members_16, _method_13);
                          EList<JvmMember> _members_17 = it.getMembers();
                          String _simpleName_3 = firstRoleType.getSimpleName();
                          String _plus_8 = ("removeAll" + _simpleName_3);
                          JvmTypeReference _typeRef_22 = this._typeReferenceBuilder.typeRef(Void.TYPE);
                          final Procedure1<JvmOperation> _function_23 = (JvmOperation it_1) -> {
                            EList<JvmFormalParameter> _parameters = it_1.getParameters();
                            JvmTypeReference _typeRef_23 = this._typeReferenceBuilder.typeRef("java.lang.Iterable", firstRoleType);
                            JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "roles", _typeRef_23);
                            this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
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
                            this._jvmTypesBuilder.setBody(it_1, _client);
                          };
                          JvmOperation _method_14 = this._jvmTypesBuilder.toMethod(meln, _plus_8, _typeRef_22, _function_23);
                          this._jvmTypesBuilder.<JvmOperation>operator_add(_members_17, _method_14);
                          if ((!isAutoGraph)) {
                            EList<JvmMember> _members_18 = it.getMembers();
                            String _simpleName_4 = secondRoleType.getSimpleName();
                            String _plus_9 = ("add" + _simpleName_4);
                            JvmTypeReference _typeRef_23 = this._typeReferenceBuilder.typeRef(Void.TYPE);
                            final Procedure1<JvmOperation> _function_24 = (JvmOperation it_1) -> {
                              EList<JvmFormalParameter> _parameters = it_1.getParameters();
                              JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "role", secondRoleType);
                              this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
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
                              this._jvmTypesBuilder.setBody(it_1, _client);
                            };
                            JvmOperation _method_15 = this._jvmTypesBuilder.toMethod(meln, _plus_9, _typeRef_23, _function_24);
                            this._jvmTypesBuilder.<JvmOperation>operator_add(_members_18, _method_15);
                            EList<JvmMember> _members_19 = it.getMembers();
                            JvmTypeReference _typeRef_24 = this._typeReferenceBuilder.typeRef(Void.TYPE);
                            final Procedure1<JvmOperation> _function_25 = (JvmOperation it_1) -> {
                              EList<JvmFormalParameter> _parameters = it_1.getParameters();
                              JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "role", secondRoleType);
                              this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                              StringConcatenationClient _client = new StringConcatenationClient() {
                                @Override
                                protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                  _builder.append("remove");
                                  _builder.append(secondRoleType, "");
                                  _builder.append("(role);");
                                }
                              };
                              this._jvmTypesBuilder.setBody(it_1, _client);
                            };
                            JvmOperation _method_16 = this._jvmTypesBuilder.toMethod(meln, "remove", _typeRef_24, _function_25);
                            this._jvmTypesBuilder.<JvmOperation>operator_add(_members_19, _method_16);
                            EList<JvmMember> _members_20 = it.getMembers();
                            JvmTypeReference _typeRef_25 = this._typeReferenceBuilder.typeRef(Void.TYPE);
                            final Procedure1<JvmOperation> _function_26 = (JvmOperation it_1) -> {
                              EList<JvmFormalParameter> _parameters = it_1.getParameters();
                              JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "role", secondRoleType);
                              this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                              StringConcatenationClient _client = new StringConcatenationClient() {
                                @Override
                                protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                  _builder.append("add");
                                  _builder.append(secondRoleType, "");
                                  _builder.append("(role);");
                                }
                              };
                              this._jvmTypesBuilder.setBody(it_1, _client);
                            };
                            JvmOperation _method_17 = this._jvmTypesBuilder.toMethod(meln, "add", _typeRef_25, _function_26);
                            this._jvmTypesBuilder.<JvmOperation>operator_add(_members_20, _method_17);
                            EList<JvmMember> _members_21 = it.getMembers();
                            String _simpleName_5 = secondRoleType.getSimpleName();
                            String _plus_10 = ("remove" + _simpleName_5);
                            JvmTypeReference _typeRef_26 = this._typeReferenceBuilder.typeRef(Void.TYPE);
                            final Procedure1<JvmOperation> _function_27 = (JvmOperation it_1) -> {
                              EList<JvmFormalParameter> _parameters = it_1.getParameters();
                              JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "role", secondRoleType);
                              this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
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
                              this._jvmTypesBuilder.setBody(it_1, _client);
                            };
                            JvmOperation _method_18 = this._jvmTypesBuilder.toMethod(meln, _plus_10, _typeRef_26, _function_27);
                            this._jvmTypesBuilder.<JvmOperation>operator_add(_members_21, _method_18);
                            EList<JvmMember> _members_22 = it.getMembers();
                            String _simpleName_6 = secondRoleType.getSimpleName();
                            String _plus_11 = ("addAll" + _simpleName_6);
                            JvmTypeReference _typeRef_27 = this._typeReferenceBuilder.typeRef(Void.TYPE);
                            final Procedure1<JvmOperation> _function_28 = (JvmOperation it_1) -> {
                              EList<JvmFormalParameter> _parameters = it_1.getParameters();
                              JvmTypeReference _typeRef_28 = this._typeReferenceBuilder.typeRef("java.lang.Iterable", secondRoleType);
                              JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "roles", _typeRef_28);
                              this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
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
                              this._jvmTypesBuilder.setBody(it_1, _client);
                            };
                            JvmOperation _method_19 = this._jvmTypesBuilder.toMethod(meln, _plus_11, _typeRef_27, _function_28);
                            this._jvmTypesBuilder.<JvmOperation>operator_add(_members_22, _method_19);
                            EList<JvmMember> _members_23 = it.getMembers();
                            String _simpleName_7 = secondRoleType.getSimpleName();
                            String _plus_12 = ("removeAll" + _simpleName_7);
                            JvmTypeReference _typeRef_28 = this._typeReferenceBuilder.typeRef(Void.TYPE);
                            final Procedure1<JvmOperation> _function_29 = (JvmOperation it_1) -> {
                              EList<JvmFormalParameter> _parameters = it_1.getParameters();
                              JvmTypeReference _typeRef_29 = this._typeReferenceBuilder.typeRef("java.lang.Iterable", secondRoleType);
                              JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(meln, "roles", _typeRef_29);
                              this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
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
                              this._jvmTypesBuilder.setBody(it_1, _client);
                            };
                            JvmOperation _method_20 = this._jvmTypesBuilder.toMethod(meln, _plus_12, _typeRef_28, _function_29);
                            this._jvmTypesBuilder.<JvmOperation>operator_add(_members_23, _method_20);
                          }
                        }
                      }
                      EList<RelElements> _relelns_4 = ((Relation)meln).getRelelns();
                      for (final RelElements reln_4 : _relelns_4) {
                        boolean _matched_7 = false;
                        if (!_matched_7) {
                          if (reln_4 instanceof RelPropertyDef) {
                            _matched_7=true;
                            String _name_10 = ((RelPropertyDef)reln_4).getName();
                            boolean _notEquals_22 = (!Objects.equal(_name_10, null));
                            if (_notEquals_22) {
                              EList<JvmMember> _members_24 = it.getMembers();
                              String _name_11 = ((RelPropertyDef)reln_4).getName();
                              String _firstUpper_4 = StringExtensions.toFirstUpper(_name_11);
                              String _plus_13 = ("set" + _firstUpper_4);
                              JvmTypeReference _typeRef_29 = this._typeReferenceBuilder.typeRef(Void.TYPE);
                              final Procedure1<JvmOperation> _function_30 = (JvmOperation it_1) -> {
                                EList<JvmFormalParameter> _parameters = it_1.getParameters();
                                String _name_12 = ((RelPropertyDef)reln_4).getName();
                                JvmTypeReference _type_17 = ((RelPropertyDef)reln_4).getType();
                                JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(reln_4, _name_12, _type_17);
                                this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
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
                                    String _name = ((RelPropertyDef)reln_4).getName();
                                    String _firstUpper = StringExtensions.toFirstUpper(_name);
                                    _builder.append(_firstUpper, "  ");
                                    _builder.append("(");
                                    String _name_1 = ((RelPropertyDef)reln_4).getName();
                                    _builder.append(_name_1, "  ");
                                    _builder.append(");");
                                    _builder.newLineIfNotEmpty();
                                    _builder.append("endTransaction();");
                                    _builder.newLine();
                                  }
                                };
                                this._jvmTypesBuilder.setBody(it_1, _client);
                              };
                              JvmOperation _method_21 = this._jvmTypesBuilder.toMethod(reln_4, _plus_13, _typeRef_29, _function_30);
                              this._jvmTypesBuilder.<JvmOperation>operator_add(_members_24, _method_21);
                            }
                          }
                        }
                        if (!_matched_7) {
                          if (reln_4 instanceof InteractionDef) {
                            _matched_7=true;
                            String _name_10 = ((InteractionDef)reln_4).getName();
                            boolean _notEquals_22 = (!Objects.equal(_name_10, null));
                            if (_notEquals_22) {
                              EList<JvmMember> _members_24 = it.getMembers();
                              String _name_11 = ((InteractionDef)reln_4).getName();
                              JvmTypeReference _typeRef_29 = this._typeReferenceBuilder.typeRef(Void.TYPE);
                              final Procedure1<JvmOperation> _function_30 = (JvmOperation it_1) -> {
                                EList<JvmFormalParameter> _params = ((InteractionDef)reln_4).getParams();
                                for (final JvmFormalParameter p : _params) {
                                  EList<JvmFormalParameter> _parameters = it_1.getParameters();
                                  String _name_12 = p.getName();
                                  JvmTypeReference _parameterType = p.getParameterType();
                                  JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(reln_4, _name_12, _parameterType);
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
                                    _builder.append(typ_edgecname, "");
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
                                      EList<Comitexpr> _comitexpressions = ((InteractionDef)reln_4).getComitexpressions();
                                      int _size = _comitexpressions.size();
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
                              JvmOperation _method_21 = this._jvmTypesBuilder.toMethod(reln_4, _name_11, _typeRef_29, _function_30);
                              this._jvmTypesBuilder.<JvmOperation>operator_add(_members_24, _method_21);
                            }
                          }
                        }
                        if (!_matched_7) {
                          if (reln_4 instanceof Filterdef) {
                            _matched_7=true;
                            String _name_10 = ((Filterdef)reln_4).getName();
                            boolean _notEquals_22 = (!Objects.equal(_name_10, null));
                            if (_notEquals_22) {
                              EList<JvmMember> _members_24 = it.getMembers();
                              String _name_11 = ((Filterdef)reln_4).getName();
                              String _string_8 = graphcname.toString();
                              JvmTypeReference _typeRef_29 = this._typeReferenceBuilder.typeRef(_string_8);
                              final Procedure1<JvmOperation> _function_30 = (JvmOperation it_1) -> {
                                EList<JvmFormalParameter> _params = ((Filterdef)reln_4).getParams();
                                for (final JvmFormalParameter p : _params) {
                                  EList<JvmFormalParameter> _parameters = it_1.getParameters();
                                  String _name_12 = p.getName();
                                  JvmTypeReference _parameterType = p.getParameterType();
                                  JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(reln_4, _name_12, _parameterType);
                                  this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                                }
                                StringConcatenationClient _client = new StringConcatenationClient() {
                                  @Override
                                  protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                                    String _name = ((Relation)meln).getName();
                                    String _plus = (_name + "_");
                                    String _name_1 = ((Filterdef)reln_4).getName();
                                    String _plus_1 = (_plus + _name_1);
                                    _builder.append(_plus_1, "");
                                    _builder.append(" _filter = new ");
                                    String _name_2 = ((Relation)meln).getName();
                                    String _plus_2 = (_name_2 + "_");
                                    String _name_3 = ((Filterdef)reln_4).getName();
                                    String _plus_3 = (_plus_2 + _name_3);
                                    _builder.append(_plus_3, "");
                                    _builder.append("(");
                                    {
                                      EList<JvmFormalParameter> _params = ((Filterdef)reln_4).getParams();
                                      int _size = _params.size();
                                      boolean _greaterThan = (_size > 0);
                                      if (_greaterThan) {
                                        {
                                          EList<JvmFormalParameter> _params_1 = ((Filterdef)reln_4).getParams();
                                          int _size_1 = _params_1.size();
                                          int _minus = (_size_1 - 1);
                                          IntegerRange _upTo = new IntegerRange(0, _minus);
                                          for(final Integer i : _upTo) {
                                            EList<JvmFormalParameter> _params_2 = ((Filterdef)reln_4).getParams();
                                            JvmFormalParameter _get = _params_2.get((i).intValue());
                                            String _name_4 = _get.getName();
                                            _builder.append(_name_4, "");
                                            {
                                              EList<JvmFormalParameter> _params_3 = ((Filterdef)reln_4).getParams();
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
                                this._jvmTypesBuilder.setBody(it_1, _client);
                              };
                              JvmOperation _method_21 = this._jvmTypesBuilder.toMethod(reln_4, _name_11, _typeRef_29, _function_30);
                              this._jvmTypesBuilder.<JvmOperation>operator_add(_members_24, _method_21);
                            }
                          }
                        }
                      }
                    };
                    acceptor.<JvmGenericType>accept(_class_7, _function_7);
                  }
                }
              }
            }
          }
        }
        if (!_matched) {
          if (meln instanceof Strucdef) {
            _matched=true;
            QualifiedName _fullyQualifiedName = this._iQualifiedNameProvider.getFullyQualifiedName(meln);
            JvmGenericType _class = this._jvmTypesBuilder.toClass(modl, _fullyQualifiedName);
            final Procedure1<JvmGenericType> _function = (JvmGenericType it) -> {
              String _typeArgument = ((Strucdef)meln).getTypeArgument();
              boolean _notEquals = (!Objects.equal(_typeArgument, null));
              if (_notEquals) {
                final JvmTypeParameter param = TypesFactory.eINSTANCE.createJvmTypeParameter();
                String _typeArgument_1 = ((Strucdef)meln).getTypeArgument();
                param.setName(_typeArgument_1);
                EList<JvmTypeParameter> _typeParameters = it.getTypeParameters();
                this._jvmTypesBuilder.<JvmTypeParameter>operator_add(_typeParameters, param);
                String _superType = ((Strucdef)meln).getSuperType();
                boolean _notEquals_1 = (!Objects.equal(_superType, null));
                if (_notEquals_1) {
                  EList<JvmTypeReference> _superTypes = it.getSuperTypes();
                  String _superType_1 = ((Strucdef)meln).getSuperType();
                  JvmTypeReference _typeRef = this._typeReferenceBuilder.typeRef(param);
                  JvmTypeReference _typeRef_1 = this._typeReferenceBuilder.typeRef(_superType_1, _typeRef);
                  this._jvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes, _typeRef_1);
                }
              } else {
                String _superType_2 = ((Strucdef)meln).getSuperType();
                boolean _notEquals_2 = (!Objects.equal(_superType_2, null));
                if (_notEquals_2) {
                  EList<JvmTypeReference> _superTypes_1 = it.getSuperTypes();
                  String _superType_3 = ((Strucdef)meln).getSuperType();
                  JvmTypeReference _typeRef_2 = this._typeReferenceBuilder.typeRef(_superType_3);
                  this._jvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes_1, _typeRef_2);
                }
              }
              final List<StrucVarDef> lvdefs = CollectionLiterals.<StrucVarDef>newArrayList();
              EList<StrucEln> _strucelns = ((Strucdef)meln).getStrucelns();
              for (final StrucEln steln : _strucelns) {
                boolean _matched_1 = false;
                if (!_matched_1) {
                  if (steln instanceof StrucVarDef) {
                    _matched_1=true;
                    lvdefs.add(((StrucVarDef)steln));
                    String _name_2 = ((StrucVarDef)steln).getName();
                    JvmTypeReference _type = ((StrucVarDef)steln).getType();
                    JvmField jvmField = this._jvmTypesBuilder.toField(steln, _name_2, _type);
                    boolean _notEquals_3 = (!Objects.equal(jvmField, null));
                    if (_notEquals_3) {
                      jvmField.setFinal(false);
                      EList<JvmMember> _members = it.getMembers();
                      this._jvmTypesBuilder.<JvmField>operator_add(_members, jvmField);
                      EList<JvmMember> _members_1 = it.getMembers();
                      String _name_3 = ((StrucVarDef)steln).getName();
                      JvmTypeReference _type_1 = ((StrucVarDef)steln).getType();
                      JvmOperation _setter = this._jvmTypesBuilder.toSetter(steln, _name_3, _type_1);
                      this._jvmTypesBuilder.<JvmOperation>operator_add(_members_1, _setter);
                      EList<JvmMember> _members_2 = it.getMembers();
                      String _name_4 = ((StrucVarDef)steln).getName();
                      JvmTypeReference _type_2 = ((StrucVarDef)steln).getType();
                      JvmOperation _getter = this._jvmTypesBuilder.toGetter(steln, _name_4, _type_2);
                      this._jvmTypesBuilder.<JvmOperation>operator_add(_members_2, _getter);
                    }
                  }
                }
                if (!_matched_1) {
                  if (steln instanceof StrucFuncDef) {
                    _matched_1=true;
                    JvmTypeReference _type = ((StrucFuncDef)steln).getType();
                    boolean _equals_1 = Objects.equal(_type, null);
                    if (_equals_1) {
                      JvmTypeReference _typeRef_3 = this._typeReferenceBuilder.typeRef(Void.TYPE);
                      ((StrucFuncDef)steln).setType(_typeRef_3);
                    }
                    EList<JvmMember> _members = it.getMembers();
                    String _name_2 = ((StrucFuncDef)steln).getName();
                    JvmTypeReference _type_1 = ((StrucFuncDef)steln).getType();
                    final Procedure1<JvmOperation> _function_1 = (JvmOperation it_1) -> {
                      String _documentation = this._jvmTypesBuilder.getDocumentation(steln);
                      this._jvmTypesBuilder.setDocumentation(it_1, _documentation);
                      EList<JvmFormalParameter> _params = ((StrucFuncDef)steln).getParams();
                      for (final JvmFormalParameter p : _params) {
                        EList<JvmFormalParameter> _parameters = it_1.getParameters();
                        String _name_3 = p.getName();
                        JvmTypeReference _parameterType = p.getParameterType();
                        JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(p, _name_3, _parameterType);
                        this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                      }
                      XExpression _body = ((StrucFuncDef)steln).getBody();
                      this._jvmTypesBuilder.setBody(it_1, _body);
                    };
                    JvmOperation _method = this._jvmTypesBuilder.toMethod(steln, _name_2, _type_1, _function_1);
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
                            _builder.append(_name, "");
                            _builder.append(" = new ");
                            _builder.append(vtyp, "");
                            {
                              boolean _isNumberType = OceletJvmModelInferrer.this.isNumberType(vtyp);
                              if (_isNumberType) {
                                _builder.append("(\"0\");");
                              } else {
                                String _qualifiedName = vtyp.getQualifiedName();
                                boolean _equals = _qualifiedName.equals("java.lang.Boolean");
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
      final Procedure1<JvmGenericType> _function = (JvmGenericType it) -> {
        String _documentation = this._jvmTypesBuilder.getDocumentation(modl);
        this._jvmTypesBuilder.setDocumentation(it, _documentation);
        EList<JvmTypeReference> _superTypes = it.getSuperTypes();
        JvmTypeReference _typeRef = this._typeReferenceBuilder.typeRef("fr.ocelet.runtime.model.AbstractModel");
        this._jvmTypesBuilder.<JvmTypeReference>operator_add(_superTypes, _typeRef);
        EList<JvmMember> _members = it.getMembers();
        final Procedure1<JvmConstructor> _function_1 = (JvmConstructor it_1) -> {
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
          this._jvmTypesBuilder.setBody(it_1, _client);
        };
        JvmConstructor _constructor = this._jvmTypesBuilder.toConstructor(modl, _function_1);
        this._jvmTypesBuilder.<JvmConstructor>operator_add(_members, _constructor);
        for (final Scenario scen : scens) {
          String _name_2 = scen.getName();
          int _compareTo = _name_2.compareTo(modlName);
          boolean _equals_1 = (_compareTo == 0);
          if (_equals_1) {
            EList<JvmMember> _members_1 = it.getMembers();
            JvmTypeReference _typeRef_1 = this._typeReferenceBuilder.typeRef(Void.TYPE);
            final Procedure1<JvmOperation> _function_2 = (JvmOperation it_1) -> {
              EList<JvmFormalParameter> _parameters = it_1.getParameters();
              JvmTypeReference _typeRef_2 = this._typeReferenceBuilder.typeRef("java.lang.String");
              JvmTypeReference _addArrayTypeDimension = this._jvmTypesBuilder.addArrayTypeDimension(_typeRef_2);
              JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(modl, "args", _addArrayTypeDimension);
              this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
              it_1.setStatic(true);
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
              this._jvmTypesBuilder.setBody(it_1, _client);
            };
            JvmOperation _method = this._jvmTypesBuilder.toMethod(modl, "main", _typeRef_1, _function_2);
            this._jvmTypesBuilder.<JvmOperation>operator_add(_members_1, _method);
            EList<JvmMember> _members_2 = it.getMembers();
            JvmTypeReference _typeRef_2 = this._typeReferenceBuilder.typeRef(Void.TYPE);
            final Procedure1<JvmOperation> _function_3 = (JvmOperation it_1) -> {
              XExpression _body = scen.getBody();
              this._jvmTypesBuilder.setBody(it_1, _body);
            };
            JvmOperation _method_1 = this._jvmTypesBuilder.toMethod(modl, ("run_" + modlName), _typeRef_2, _function_3);
            this._jvmTypesBuilder.<JvmOperation>operator_add(_members_2, _method_1);
            EList<JvmMember> _members_3 = it.getMembers();
            JvmTypeReference _typeRef_3 = this._typeReferenceBuilder.typeRef(Void.TYPE);
            final Procedure1<JvmOperation> _function_4 = (JvmOperation it_1) -> {
              EList<JvmFormalParameter> _parameters = it_1.getParameters();
              JvmTypeReference _typeRef_4 = this._typeReferenceBuilder.typeRef("java.lang.String");
              JvmTypeReference _typeRef_5 = this._typeReferenceBuilder.typeRef("java.lang.Object");
              JvmTypeReference _typeRef_6 = this._typeReferenceBuilder.typeRef("java.util.HashMap", _typeRef_4, _typeRef_5);
              JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(modl, "in_params", _typeRef_6);
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
              this._jvmTypesBuilder.setBody(it_1, _client);
            };
            JvmOperation _method_2 = this._jvmTypesBuilder.toMethod(modl, "simulate", _typeRef_3, _function_4);
            this._jvmTypesBuilder.<JvmOperation>operator_add(_members_3, _method_2);
          } else {
            JvmTypeReference rtype = scen.getType();
            boolean _equals_2 = Objects.equal(rtype, null);
            if (_equals_2) {
              JvmTypeReference _typeRef_4 = this._typeReferenceBuilder.typeRef(Void.TYPE);
              rtype = _typeRef_4;
            }
            EList<JvmMember> _members_4 = it.getMembers();
            String _name_3 = scen.getName();
            final Procedure1<JvmOperation> _function_5 = (JvmOperation it_1) -> {
              EList<JvmFormalParameter> _params = scen.getParams();
              for (final JvmFormalParameter p : _params) {
                EList<JvmFormalParameter> _parameters = it_1.getParameters();
                String _name_4 = p.getName();
                JvmTypeReference _parameterType = p.getParameterType();
                JvmFormalParameter _parameter = this._jvmTypesBuilder.toParameter(p, _name_4, _parameterType);
                this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
              }
              XExpression _body = scen.getBody();
              this._jvmTypesBuilder.setBody(it_1, _body);
            };
            JvmOperation _method_3 = this._jvmTypesBuilder.toMethod(scen, _name_3, rtype, _function_5);
            this._jvmTypesBuilder.<JvmOperation>operator_add(_members_4, _method_3);
          }
        }
        boolean _hasParameters = md.hasParameters();
        if (_hasParameters) {
          ArrayList<Parameterstuff> _params = md.getParams();
          for (final Parameterstuff pstuff : _params) {
            {
              JvmField jvmField = this._jvmTypesBuilder.toField(modl, pstuff.name, pstuff.type);
              boolean _notEquals = (!Objects.equal(jvmField, null));
              if (_notEquals) {
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
      acceptor.<JvmGenericType>accept(_class, _function);
    }
  }
  
  private boolean isNumberType(final JvmTypeReference vtyp) {
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
    return _or;
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
