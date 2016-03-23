/*
*  Ocelet spatial modelling language.   www.ocelet.org
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
package fr.ocelet.lang.jvmmodel

import com.google.inject.Inject
import fr.ocelet.lang.ocelet.Agregdef
import fr.ocelet.lang.ocelet.ConstructorDef
import fr.ocelet.lang.ocelet.Datafacer
import fr.ocelet.lang.ocelet.Entity
import fr.ocelet.lang.ocelet.Filterdef
import fr.ocelet.lang.ocelet.InteractionDef
import fr.ocelet.lang.ocelet.Metadata
import fr.ocelet.lang.ocelet.Model
import fr.ocelet.lang.ocelet.Paradesc
import fr.ocelet.lang.ocelet.Paramdefa
import fr.ocelet.lang.ocelet.Paramunit
import fr.ocelet.lang.ocelet.Paraopt
import fr.ocelet.lang.ocelet.PropertyDef
import fr.ocelet.lang.ocelet.Rangevals
import fr.ocelet.lang.ocelet.RelPropertyDef
import fr.ocelet.lang.ocelet.Relation
import fr.ocelet.lang.ocelet.Scenario
import fr.ocelet.lang.ocelet.ServiceDef
import fr.ocelet.lang.ocelet.StrucFuncDef
import fr.ocelet.lang.ocelet.StrucVarDef
import fr.ocelet.lang.ocelet.Strucdef
import java.util.HashMap
import java.util.List
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.common.types.JvmTypeParameter
import org.eclipse.xtext.common.types.JvmTypeReference
import org.eclipse.xtext.common.types.TypesFactory
import org.eclipse.xtext.common.types.util.Primitives
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.eclipse.xtext.xbase.jvmmodel.AbstractModelInferrer
import org.eclipse.xtext.xbase.jvmmodel.IJvmDeclaredTypeAcceptor
import org.eclipse.xtext.xbase.jvmmodel.JvmTypesBuilder
/**
 * Java code inferrer of the Ocelet language
 * 
 * @author Pascal Degenne - Initial contribution
 */
class OceletJvmModelInferrer extends AbstractModelInferrer {

	@Inject extension JvmTypesBuilder

    @Inject extension IQualifiedNameProvider
    @Inject OceletCompiler ocltCompiler
    // Used to wrap primtive types to their corresponding java classes when needed.
    @Inject extension Primitives
        
   	def dispatch void infer(Model modl, IJvmDeclaredTypeAcceptor acceptor, boolean isPreIndexingPhase) {
   	  val List<Scenario> scens = newArrayList()
   	  val Metadatastuff md = new Metadatastuff();
      var boolean mainScen = false

      // ---- Récupération du nom du modèle ----
      val Resource res = modl.eResource();
      val modlName = res.getURI.segment(1)
      if(modl.getName() == null) modl.setName("fr.ocelet.model."+ modlName.toLowerCase());
      val packg = modl.getName()+"."
  
      // ---- Remplissage de la liste des scenarios et repérage du main ----
      for(meln:modl.modelns) {
      	try {
          switch(meln) {
            Scenario : { 
              if (meln.name.compareTo(modlName) == 0) mainScen = true
     	      scens.add(meln)
            }
            
      // ---- Metadata ------------------------------------
          Metadata : {
            if (meln.paramdefs != null) {          	
          	md.setModeldesc(meln.desc)
          	md.setWebpage(meln.webp)
          	for (paramdef:meln.paramdefs) {
          		val pst = new Parameterstuff(paramdef.name,paramdef.ptype)
          		for(ppart:paramdef.paramparts) {
          			switch(ppart) {
          			  Paramunit : {pst.setUnit(ppart.parunit)}
          			  Paramdefa : {pst.setDefvalue(ppart.pardefa)}
          			  Rangevals : {pst.setMin(ppart.parmin); pst.setMax(ppart.parmax)}
          			  Paradesc : {pst.setDescription(ppart.pardesc)}
          			  Paraopt : {pst.setOptionnal((ppart.paropt.compareToIgnoreCase("true") == 0))}
          			}
          		}
          	  md.params.add(pst)
          	}
          	}
          }
          
// ---- Datafacer ----------------------------------
          Datafacer : {
          	
                 	
          	
          	acceptor.accept(modl.toClass(meln.fullyQualifiedName)) [
          	  superTypes += typeRef('fr.ocelet.datafacer.ocltypes.'+meln.storetype)
          	  members+= meln.toConstructor[
                body = [
                	append('''super(''')
                	var int carg=0
                	for (arg:meln.arguments) {
                	  if (carg > 0) append(''',''')
                	  ocltCompiler.compileDatafacerParamExpression(arg,it)
//                	  append('''«arg»''')
                	  carg = carg+1
                	}
                	append(''');''')
                ]
     		  ]
     		  // Generates a set of functions for every match definition
     		  // We have to add .simpleName to every type we need to generate
     		  // don't know why so far ! ... but it works.
          	var isFirst = true
          	for(matchdef:meln.matchbox){
     		  val mt = matchdef.mtype
     		  if (mt != null) switch (mt) {
     		    Entity : {
     		  	  val entype = typeRef(mt.fullyQualifiedName.toString)
                  val entname = mt.name.toFirstUpper
                  val listype = typeRef('fr.ocelet.runtime.ocltypes.List',entype)
                  val propmap = new HashMap<String,String>()
                  val propmapf = new HashMap<String,String>()
                  for(eprop:mt.entelns) {
                  	switch(eprop) {
                  		PropertyDef : {
                  		  if (eprop.type != null) {
                  		   propmap.put(eprop.name,eprop.type.simpleName) 
                  		   propmapf.put(eprop.name,eprop.type.qualifiedName)
                  		  }	
                  		}
                  	  }
                    }
  					if ('RasterFile'.equals(''+meln.storetype)) { 
                 // if (Class::forName('fr.ocelet.datafacer.RasterFile').isAssignableFrom(Class::forName('fr.ocelet.datafacer.ocltypes.'+meln.storetype))) {

                 		val tabType = typeRef('fr.ocelet.runtime.raster.Grid')
                  		members += meln.toMethod('readAll'+entname,tabType)[
                  		body='''
                   			if(grid == null){
                  				grid = new Grid(getWidth(), getHeight(), getGridGeometry());
                  	  			«FOR mp:matchdef.matchprops»
                  	  	  		«val eproptype = propmap.get(mp.prop)»
                 	  	  			«IF eproptype != null»
                  	  	    			«IF mp.colname != null»
                  	  	    				grid.addProp("«mp.prop»","«mp.colname»");
                  	  	    			«ENDIF»
                  	  	  			«ENDIF»
                  	  			«ENDFOR»
                  				«entname» entity = new «entname»();
                  	   	    	grid.setInitRaster(raster.getRaster(0, 0 , getWidth(), getHeight()));
                  	   	    	grid.setFinalProperties(entity.getProps());
                  	   	    	grid.setRes(raster);
                  	   	    	fr.ocelet.runtime.raster.GridManager.getInstance().add(grid); 
                  	   	    	entity.setNumGrid(fr.ocelet.runtime.raster.GridManager.getInstance().getCurrentIndex());
                  	 			}
                  	  			return grid;
                  		  	'''
                  		]                  	
                  	  	members += meln.toMethod('readAll'+entname,tabType)[
                  	  	parameters += meln.toParameter('minX', typeRef('java.lang.Integer'))
                  	  	parameters += meln.toParameter('minY', typeRef('java.lang.Integer'))
                  	  	parameters += meln.toParameter('maxX', typeRef('java.lang.Integer'))
                  	  	parameters += meln.toParameter('maxY', typeRef('java.lang.Integer'))
                  	  	body='''                 
                  			if(grid == null){
                  	  		grid = new Grid(minX, minY, maxX, maxY, getGridGeometry());
                  	 		«FOR mp:matchdef.matchprops»
                  	  	  	«val eproptype = propmap.get(mp.prop)»
                 	  	  		«IF eproptype != null»
                  	  	    		«IF mp.colname != null»
                  	  	    			grid.addProp("«mp.prop»","«mp.colname»");
                  	  	    		«ENDIF»
                  	  	  		«ENDIF»
                  	  		«ENDFOR»
                  	  	  	«entname» entity = new «entname»(); 
                  	   	    grid.setInitRaster(raster.getRaster(minX, minY, maxX, maxY));
                  	   	    grid.setFinalProperties(entity.getProps());
                  	   	    grid.setRes(raster);
                  	   	    fr.ocelet.runtime.raster.GridManager.getInstance().add(grid);
                  	   	    entity.setNumGrid(fr.ocelet.runtime.raster.GridManager.getInstance().getCurrentIndex());
                  	  		}
                  	  		return grid;
                  	  	'''
                  	]
                   	members += meln.toMethod('readAll'+entname,tabType)[
                  	parameters += meln.toParameter('shp', typeRef('fr.ocelet.datafacer.ocltypes.Shapefile'))
                  	body='''
                  	if(grid == null){
                  	grid = new Grid();
                  	«FOR mp:matchdef.matchprops»
                  	«val eproptype = propmap.get(mp.prop)»
                 	«IF eproptype != null»
                  	«IF mp.colname != null»
                  	grid.addProp("«mp.prop»","«mp.colname»");
                  	«ENDIF»
                  	«ENDIF»
                  	«ENDFOR»
                  	«entname» entity = new «entname»();
                   	grid.setData(shp.getBounds(), raster, entity.getProps().size());
                    grid.setFinalProperties(entity.getProps());
                  	grid.setRes(raster);
                  	fr.ocelet.runtime.raster.GridManager.getInstance().add(grid); 
                    entity.setNumGrid(fr.ocelet.runtime.raster.GridManager.getInstance().getCurrentIndex());
                  	}
                    return grid;
                    '''
                  ]
                  	
       		}else{

                  // InputDatafacer functions
                  if (Class::forName('fr.ocelet.datafacer.InputDatafacer').isAssignableFrom(Class::forName('fr.ocelet.datafacer.ocltypes.'+meln.storetype))) {
                  	val inputRecordType = typeRef('fr.ocelet.datafacer.InputDataRecord')
                  	members += meln.toMethod('readAll'+entname,listype)[
                  	  body='''
                  	    List<«entname»> _elist = new List<«entname»>();
                  	    for («inputRecordType» _record : this) {
                  	      _elist.add(create«entname»FromRecord(_record));
                  	     }
                  	    resetIterator();
                  	    return _elist;
                  	  '''
                  	]
                  	
                  	if (isFirst) {
                  		members += meln.toMethod('readAll',listype)[
                  		  body = '''return readAll«entname»();'''
                  		]
                  	}
                  	
                  	members += meln.toMethod('create'+entname+'FromRecord',entype) [
                  	  parameters += meln.toParameter('_rec', typeRef('fr.ocelet.datafacer.InputDataRecord'))
                  	  body = '''
                  	    «entname» _entity = new «entname»();
                  	  	«FOR mp:matchdef.matchprops»
                  	  	  «val eproptype = propmap.get(mp.prop)»
                  	  	  «IF eproptype != null»
                  	  	    «IF mp.colname != null»_entity.setProperty("«mp.prop»",read«eproptype»("«mp.colname»"));«ENDIF»
                  	  	    «ENDIF»
                  	  	«ENDFOR»
                  	  	return _entity;
                  	  '''
                  	]
                  	
                  	val hmtype = typeRef('java.util.HashMap',typeRef('java.lang.String'),typeRef('java.lang.String'))
                  	members += meln.toMethod('getMatchdef',hmtype) [
                  	  body = '''
                  	    «hmtype.simpleName» hm = new «hmtype»();
                 	  	«FOR mp:matchdef.matchprops»
                  	  	  «val epropftype = propmapf.get(mp.prop)»
                 	  	  «IF epropftype != null»
                  	  	    «IF mp.colname != null»hm.put("«mp.colname»","«epropftype»");«ENDIF»
                  	  	  «ENDIF»
                  	  	«ENDFOR»
                  	    return hm;
                  		'''
                  	  ]
                    }

                 if (Class::forName('fr.ocelet.datafacer.FiltrableDatafacer').isAssignableFrom(Class::forName('fr.ocelet.datafacer.ocltypes.'+meln.storetype))) {
                   members += meln.toMethod('readFiltered'+entname,listype)[
                   parameters += meln.toParameter('_filt', typeRef('java.lang.String'))
                   body = '''
                 	 setFilter(_filt);
                 	 return readAll«entname»();
                 	'''
                   ]
                 }


                 // OutputDatafacer functions
                 if (Class::forName('fr.ocelet.datafacer.OutputDatafacer').isAssignableFrom(Class::forName('fr.ocelet.datafacer.ocltypes.'+meln.storetype))) {
                   members += meln.toMethod('createRecord',typeRef('fr.ocelet.datafacer.OutputDataRecord'))[
                   parameters += meln.toParameter('ety',typeRef('fr.ocelet.runtime.entity.Entity'))
                   exceptions += typeRef('java.lang.IllegalArgumentException')
                   body = '''
                 	 «val odrtype = typeRef('fr.ocelet.datafacer.OutputDataRecord')»
                 	 «odrtype» odr = createOutputDataRec();
                 	 if (odr != null) {
                   «FOR mp:matchdef.matchprops»
                     odr.setAttribute("«mp.colname»",((«entname») ety).get«mp.prop.toFirstUpper»());
                 	 «ENDFOR»
                 	 }
                 	 return odr;
                    '''
                  ]
                 }
                 
                 if (Class::forName('fr.ocelet.datafacer.ocltypes.Csvfile').isAssignableFrom(Class::forName('fr.ocelet.datafacer.ocltypes.'+meln.storetype))) {
                   members += meln.toMethod('headerString',typeRef('java.lang.String'))[
                     body = '''
                     StringBuffer sb = new StringBuffer();
                     «var coma = 0»
                   «FOR mp:matchdef.matchprops»
                     «IF coma++ > 0»sb.append(separator);«ENDIF»                     
                     sb.append("«mp.colname»");
                   «ENDFOR»
                   return sb.toString();
                   '''  
                   ]
                   
                   members += meln.toMethod('propsString',typeRef('java.lang.String'))[
                     parameters += meln.toParameter('_entity', typeRef('fr.ocelet.runtime.entity.Entity'))
                     body='''
                     StringBuffer sb = new StringBuffer();
                     «var coma = 0»
                   «FOR mp:matchdef.matchprops»
                     «IF coma++ > 0»sb.append(separator);«ENDIF»                     
                     sb.append(_entity.getProperty("«mp.prop»").toString());
                   «ENDFOR»
                   return sb.toString();
                     '''
                   ]
                 }
                 
                  }
                }
                
                }
                isFirst = false
              }
     		]
     	  }     
          
      // ---- Entity --------------------------------------
          Entity : {
            // Generation d'une classe par entity
            acceptor.accept(modl.toClass(meln.fullyQualifiedName)) [
      		  documentation = meln.documentation
     		  superTypes += typeRef('fr.ocelet.runtime.entity.AbstractEntity')
     		  val List<PropertyDef> lpropdefs = <PropertyDef>newArrayList()
     		  val List<String> cellProps = <String>newArrayList() 
     		  var boolean isCell = false
     		  
     		  for(enteln:meln.entelns) {
      		  	switch(enteln) {
      		  		PropertyDef: {
      		  			if (enteln.name != null) {
      		  				if(enteln.type.simpleName.equals("Cell")){
      		  					isCell = true;
      		  				}
      		  			}
      		  		}
      		  	}
      		  }
     		  
      		  for(enteln:meln.entelns) {
      		  	switch(enteln) {
      		  		
      		  		PropertyDef: {
                      if (enteln.name != null) {
      		  			lpropdefs.add(enteln)
      		  			
      		  			if(!enteln.type.simpleName.equals("Cell"))
      		  			cellProps.add(enteln.name)
      		  			
      		  			
      		  			if(isCell){
      		  				if(!enteln.name.equals('cell')){
      		  					members += enteln.toMethod('set'+enteln.name.toFirstUpper,typeRef(Void::TYPE))[
      		  				documentation = enteln.documentation
      		  				val parName = enteln.name
      		  				parameters += enteln.toParameter(parName, enteln.type)
      		  				body= '''
      		  					 fr.ocelet.runtime.raster.GridManager.getInstance().get(numGrid).setValue("«enteln.name»",getX(), getY(),«enteln.name»);
      		  				'''
      		  			]
      		  			members += enteln.toMethod('get'+enteln.name.toFirstUpper,enteln.type)[
      		  				documentation = enteln.documentation
      		  				body= '''
      		  					return fr.ocelet.runtime.raster.GridManager.getInstance().get(numGrid).getValue("«enteln.name»",getX(), getY());
      		  				'''
      		  			]
      		  				}
      		  			
      		  			}else{
      		  				// Add special setter and getter
      		  				// We can't use the toSetter and toGetter methods because we do not create a real field.
      		  				members += enteln.toMethod('set'+enteln.name.toFirstUpper,typeRef(Void::TYPE))[
      		  					documentation = enteln.documentation
      		  					val parName = enteln.name
      		  					parameters += enteln.toParameter(parName, enteln.type)
      		  					body='''setProperty("«enteln.name»",«parName»);'''
      		  				]
      		  				members += enteln.toMethod('get'+enteln.name.toFirstUpper,enteln.type)[
      		  					documentation = enteln.documentation
      		  					body='''return getProperty("«enteln.name»");'''
      		  					]
      		  		  		}
      		  			}
      		  		}
      		  		ServiceDef: {
      		  			var rtype = enteln.type
      		  			if (rtype == null) rtype = typeRef(Void::TYPE)
      		  			members+= enteln.toMethod(enteln.name, rtype)[
      		  				documentation = enteln.documentation
      		  				for (p: enteln.params) {
      		  					parameters += p.toParameter(p.name, p.parameterType)
      		  				}
      		  				body = enteln.body
      		  			]
      		  		}
      		  		ConstructorDef: {
      		  			members+= enteln.toMethod(enteln.name, typeRef(meln.fullyQualifiedName.toString))[
      		  				setStatic(true)
      		  				documentation = enteln.documentation
      		  				for (p: enteln.params) {
      		  					parameters += p.toParameter(p.name, p.parameterType)
      		  				}
      		  				body =	enteln.body
      		  			]
      		  		}
      		  	}
      		  }
      		  if(!isCell){
      	      // Ajout d'un constructeur avec déclaration de toutes les properties
      	      members+= meln.toConstructor[
      	      	body = '''
      	      	  super();
      	      	  «FOR hprop : lpropdefs»
      	      	    «val hhtype = typeRef('fr.ocelet.runtime.entity.Hproperty',asWrapperTypeIfPrimitive(hprop.type))»
      	      	    defProperty("«hprop.name»",new «hhtype»());
      	      	    «val vtyp = asWrapperTypeIfPrimitive(hprop.type)»
      	      	    set«hprop.name.toFirstUpper»(new «vtyp»«IF (vtyp.isNumberType)»("0"));«ELSEIF (vtyp.qualifiedName.equals("java.lang.Boolean"))»(false));«ELSE»());
                    «ENDIF»
      	      	  «ENDFOR»
      	      	'''
      	       ]
      	       
      	 	} else{
      	    	{
      	      		members+= meln.toConstructor[
      	        	body = '''
               			this.cell = new fr.ocelet.runtime.geom.ocltypes.Cell();
               			this.setSpatialType(cell);
                		'''
      	      		]  
      	      	members += meln.toMethod('getProps',typeRef('fr.ocelet.runtime.ocltypes.List',typeRef('java.lang.String')))[    	      	
      	            		  				
      		  			body='''
      		  				List<String> names = new List<String>();
      		  				«FOR name : cellProps»
      		  					names.add("«name»");
      		  				«ENDFOR»
      		  				return names;
      		  			'''
      		  		]
      		  			
      		  			 members += meln.toMethod('updateCellInfo',typeRef(Void::TYPE))[    	      	
      	            		  	parameters += meln.toParameter('type', typeRef('java.lang.String'))
      		  				body='''
      		  			this.cell.setType(type);
      		  			this.cell.setNumGrid(this.numGrid);
      		  			this.cell.updateResInfo();
  		  				'''
      		  			]
      		  
      
        	  var jvmFieldCell = meln.toField("cell", typeRef("fr.ocelet.runtime.geom.ocltypes.Cell"))
          	    if (jvmFieldCell != null) {
          	      jvmFieldCell.setFinal(false) 
          		  members+= jvmFieldCell
          		
          		 }
      		  	
          	  var jvmFieldX = meln.toField("x", typeRef("java.lang.Integer"))
          	    if (jvmFieldX != null) {
          	      jvmFieldX.setFinal(false) 
          		  members+= jvmFieldX
          		
          		 }
          		  var jvmFieldY = meln.toField("y", typeRef("java.lang.Integer"))
          	    if (jvmFieldY != null) {
          	      jvmFieldY.setFinal(false) 
          		  members+= jvmFieldY
          		
          		 }
          		 
          		var jvmFieldNum = meln.toField("numGrid", typeRef("java.lang.Integer"))
          	    if (jvmFieldNum != null) {
          	      jvmFieldNum.setFinal(false) 
          	      jvmFieldNum.setStatic(true)
          		  members+= jvmFieldNum
          		 }
      		  
      		  members += meln.toMethod('setNumGrid', typeRef(Void::TYPE))[    	      	
      	         parameters += meln.toParameter('numGrid', typeRef('java.lang.Integer'))
      		  				body='''
      		  					this.numGrid = numGrid;
      		  					this.cell.setNumGrid(this.numGrid);
      		  				'''
      		  			]
      		   members += meln.toMethod('setX', typeRef(Void::TYPE))[    	      	
      	         parameters += meln.toParameter('x', typeRef('java.lang.Integer'))
      		  				body='''
      		  					this.cell.setX(x); 
      		  				'''
      		  			]
      		  	 members += meln.toMethod('setY', typeRef(Void::TYPE))[    	      	
      	         parameters += meln.toParameter('y', typeRef('java.lang.Integer'))
      		  				body= '''
      		  					this.cell.setY(y); 
      		  				'''
      		  			]
      		  			   members += meln.toMethod('getX',typeRef('java.lang.Integer'))[    	      	
      		  				body= '''
      		  					return this.cell.getX(); 
      		  					'''
      		  			]
      		  			  members += meln.toMethod('getCell', typeRef("fr.ocelet.runtime.geom.ocltypes.Cell"))[    	      	
      		  				body= '''
      		  					return cell;
      		  				'''
      		  			]
      		   members += meln.toMethod('getY', typeRef('java.lang.Integer'))[    	      	
      		  				body= '''
      		  					return this.cell.getY();
      		  				'''
      		  			]		
      		  }
      	       	
      	       }
      	    ]
          }

      // ---- Agregdef --------------------------------------
          Agregdef : {
            // Generates one class for every agregation function
            if (meln.type != null) {
            acceptor.accept(modl.toClass(meln.fullyQualifiedName)) [
              superTypes += typeRef('fr.ocelet.runtime.relation.AggregOperator',meln.type,typeRef('fr.ocelet.runtime.ocltypes.List',meln.type))
              members += meln.toMethod('compute',meln.type)[
                parameters += meln.toParameter('values', typeRef('fr.ocelet.runtime.ocltypes.List',meln.type))
                parameters += meln.toParameter('preval',meln.type)
                body  = meln.body
              ]
            ]
          }
        }


      // ---- Relation ------------------------------------
          Relation : {
          	val graphcname = meln.fullyQualifiedName
          	val edgecname = graphcname+"_Edge"
            if (meln.roles.size > 2) println("Sorry, only graphs with two roles are supported by this version. The two first roles will be used and the others will be ignored.")



            if ((meln.roles.size >= 2) &&
            	(meln.roles.get(0)!=null) && (meln.roles.get(1)!=null) &&
            	(meln.roles.get(0).type != null) && (meln.roles.get(1).type != null) &&
               	(meln.roles.get(0).type.fullyQualifiedName != null) &&
               	(meln.roles.get(1).type.fullyQualifiedName != null) &&
            	(meln.roles.get(0).name != null) && (meln.roles.get(1).name != null)
                ) {


            val isAutoGraph = (meln.roles.get(0).type.equals(meln.roles.get(1).type))
            var isCellGraph = false
            var isCellGeomGraph = false
            var testCell1 = false
            var testCell2 = false
            var testGeom1 = false
            var testGeom2 = false
               val rol1 = meln.roles.get(0).type
              val rol2 = meln.roles.get(1).type
            
              print("test ok") 
           for(e : rol1.eContents){
           	switch (e){
           		PropertyDef : {
           			if(e.type.simpleName.equals('Cell')){
           				testCell1 = true
           			}
           			if(e.type.simpleName.equals('Line') || e.type.simpleName.equals('MultiLine') ||
           				e.type.simpleName.equals('Polygon') || e.type.simpleName.equals('MultiPolygon') ||
           				e.type.simpleName.equals('Point') || e.type.simpleName.equals('MultiPoint') ||
           				e.type.simpleName.equals('Ring')
           			){
           				testGeom1 = true
           			}
           		}
           	}
           	
           }
            for(e : rol2.eContents){
           	switch (e){
           		PropertyDef : {
           			if(e.type.simpleName.equals('Cell')){
           				testCell2 = true
           			}
           				if(e.type.simpleName.equals('Line') || e.type.simpleName.equals('MultiLine') ||
           				e.type.simpleName.equals('Polygon') || e.type.simpleName.equals('MultiPolygon') ||
           				e.type.simpleName.equals('Point') || e.type.simpleName.equals('MultiPoint') ||
           				e.type.simpleName.equals('Ring')
           			){
           				testGeom2 = true
           			}
           		}
           	}
           	
           }
               
     
            if(testCell1 && testCell2){
            	isCellGraph = true
            }
            if(testGeom1 && testCell2 || testGeom2 && testCell1){
            	isCellGeomGraph = true
            }
                        var graphname = 'fr.ocelet.runtime.relation.impl.AutoGraph'
            
            if(!isAutoGraph){
            	graphname = 'fr.ocelet.runtime.relation.impl.DiGraph'
            }
            if(isCellGraph){
            	if(isAutoGraph){
            	graphname = 'fr.ocelet.runtime.relation.impl.CellGraph'
            	}else{
            		graphname = 'fr.ocelet.runtime.relation.impl.DiCellGraph'
            	}
            }




			 if(isCellGeomGraph){
            	graphname = 'fr.ocelet.runtime.relation.impl.GeometryCellGraph'
            }
            val graphTypeName = graphname 
            val aggregType = typeRef('fr.ocelet.runtime.raster.CellAggregOperator')
            val listype = typeRef('fr.ocelet.runtime.ocltypes.List',aggregType)
    		if(isCellGeomGraph){
	 			val gridType = typeRef('fr.ocelet.runtime.raster.Grid')
              	val firstRole = meln.roles.get(0)                                 
              	val secondRole = meln.roles.get(1)     
               
              	var tempcellType = typeRef(firstRole.type.fullyQualifiedName.toString)
              	var tempgeomType = typeRef(secondRole.type.fullyQualifiedName.toString)
              
             	var tempCellName = firstRole.name
              	var tempGeomName = secondRole.name
              
             	if(testCell2){
              	
              		tempcellType = typeRef(secondRole.type.fullyQualifiedName.toString)
              		tempCellName = secondRole.name
            		tempgeomType = typeRef(firstRole.type.fullyQualifiedName.toString)
              		tempGeomName = firstRole.name
              	}
              	val cellType = tempcellType
              	val geomType = tempgeomType
              	val cellName = tempCellName
              	val geomName = tempGeomName
              
     
	 			// Generate the edge class
          		acceptor.accept(modl.toClass(edgecname))[        		
          		superTypes += typeRef('fr.ocelet.runtime.relation.GeomCellEdge', cellType, geomType)            
          		
          	 		var jvmField = meln.toField(cellName, cellType)
          	    	if (jvmField != null) {
          	      		jvmField.setFinal(false) 
          		  		members+= jvmField
          		  		members+= meln.toSetter(cellName, cellType)
          		  		members+= meln.toGetter(cellName, cellType)
          		  	}
          			jvmField = meln.toField(geomName, geomType)
          	  		if (jvmField != null) {
          	  	  		jvmField.setFinal(false) 
          		  		members+= jvmField
          		  		members+= meln.toSetter(geomName, geomType)
          		  		members+= meln.toGetter(geomName, geomType)
          			}          		
          		
          			members+= meln.toConstructor[
          			parameters += meln.toParameter("grid", typeRef("fr.ocelet.runtime.raster.Grid"))
          			parameters += meln.toParameter("geom", typeRef("fr.ocelet.runtime.ocltypes.List", geomType))
          			body = '''
          			  super(grid, geom);  
          			  this.«cellName» = new «cellType»();
          			  «cellName».updateCellInfo(getCellType());
          		  '''
          		]
          		members += meln.toMethod("getRole", typeRef("fr.ocelet.runtime.relation.OcltRole"))[
          			parameters += meln.toParameter("i",typeRef("int"))
          						body = '''
          				if (i==0) return «cellName»;
          				else if (i==1) return «geomName»;
          				else return null;
          			'''
          		]
          		
          		members += meln.toMethod("update", typeRef(Void::TYPE))[
          			
          			body = '''
          		    	this.«cellName».setX(getX());
          		        this.«cellName».setY(getY());
          			    this. «geomName» = getGeomEntity();
          			'''
          		]
          		
     		  // Generate Properties and Interactions on the edge level
    		  for (reln:meln.relelns){
    		  	switch(reln) {
          	  
          	  	  InteractionDef : {
          	  	  	members+= reln.toMethod(reln.name,typeRef(Void::TYPE))[
          	  	  	  for(p:reln.params){
          	  	  	  	parameters += reln.toParameter(p.name,p.parameterType)
          	  	  	  }
          	  	  	  body = reln.body
          	  	  	]
          	  	    if (reln.comitexpressions.size() > 0) {
          	  	    	
          	  	    	
          	  	        members += reln.toMethod("get_agr_"+reln.name,listype)[
          	  	      	body = '''
          	  	      		«var index = 0»
          	  	      		«listype» cvtList = new «listype»();
          	  	      	  «FOR ce : reln.comitexpressions»
          	  	      	  	«IF ce.rol.type.fullyQualifiedName.toString.equals(cellType.qualifiedName.toString)»
          	  	      	  		«aggregType» cvt«index» = new «aggregType»();
          	  	      	  	 	cvt«index».setName("«ce.prop»"); 
          	  	      	  	 	cvt«index».setOperator(new  «ce.agrfunc»());
          	  	      	  	 	cvtList.add(cvt«index»);
          	  	      	  	 	«{index = index + 1; null}»
          	  	      	  	«ENDIF»
           	  	      	  «ENDFOR »
           	  	      	  return cvtList;
          	  	      	'''
          	  	      ]
          	  	    	
          	  	    	
          	  	      members += reln.toMethod("_agr_"+reln.name,typeRef(Void::TYPE))[
          	  	      	body = '''
          	  	      		
          	  	      		«FOR ce:reln.comitexpressions»
          	  	      		«val t1 = ce.rol.type.fullyQualifiedName.toString»
          	  	      		«val t2 = cellType.qualifiedName.toString»
          	  	      			«IF !t1.equals(t2)»
          	  	      				this.«ce.rol.getName()».setAgregOp("«ce.prop»",new «ce.agrfunc»(),«ce.usepreval»);
								«ENDIF»
							«ENDFOR»          	  	      		
          	  	      	'''
          	  	      ]
          	  	    }else{
          	  	    	   members += reln.toMethod("get_agr_"+reln.name,listype)[
          	  	      	body = ''' 
          	  	      		return null;
          	  	      	'''
          	  	      	]
          	  	    }
          	  	 }
          	  }
  		    }
         ]
          	
          	// -- Generate filters classes --
          	for (reln:meln.relelns){
    		  	switch(reln) {
          	  	  Filterdef : {
          	  	  	val filterfqn = graphcname+"_"+reln.name
          	  	  	acceptor.accept(modl.toClass(filterfqn))[
                     
                      val firstRoleType = typeRef(firstRole.type.fullyQualifiedName.toString)
                      val secondRoleType = typeRef(secondRole.type.fullyQualifiedName.toString)
                      superTypes += typeRef("fr.ocelet.runtime.relation.EdgeFilter",firstRoleType,secondRoleType)
                      for(p:reln.params){
                      	val pfield = reln.toField(p.name,p.parameterType)
                      	if (pfield != null) {
          	  	          pfield.setFinal(false)
          	  	          members += pfield
          	  	        }
                      }
                      members += reln.toConstructor()[
                      	for(p:reln.params) {
                      	  parameters += reln.toParameter(p.name,p.parameterType)
                        }
                        body = '''
                        	« FOR p : reln.params»
                         	  	this.«p.name» = «p.name»;
                        	« ENDFOR»

                        '''
                      ]                      
          	  	  	  members += reln.toMethod("filter",typeRef("java.lang.Boolean"))[
          	  	  		parameters += reln.toParameter(firstRole.name,firstRoleType)
          	  	  		parameters += reln.toParameter(secondRole.name,secondRoleType)
          	  	  		body = reln.body
          	  	  	  ]
          	  	  	]
          	  	  }
           	  	}
          	 }
          	
          	// -- Generate the interaction graph class --
          	if	(typeRef(edgecname) != null) {
          	acceptor.accept(modl.toClass(graphcname))[
      		  documentation = meln.documentation        
                              		 
              superTypes += typeRef(graphTypeName, typeRef(edgecname), cellType, geomType)
           //  else superTypes += typeRef(graphTypeName, typeRef(edgecname), firstRoleType, secondRoleType)

              // Generate an empty constructor
     		  members+= meln.toConstructor[	
     		  	body = '''
     		  	 super();
     		    ''' 
     		  ]

             
              val geomList = typeRef('fr.ocelet.runtime.ocltypes.List', geomType)
              // Generate DiGraph overridden methods : connect, getLeftSet, getRightSet
              members+= meln.toMethod("connect",typeRef(Void::TYPE))[
              	parameters += meln.toParameter("grid",gridType)
                parameters += meln.toParameter("geom",geomList)
              	body = ''' 
                   «typeRef(edgecname)» _gen_edge = new «meln.name+"_Edge"»(grid, geom);
     		  	  setCompleteIteratorGeomCell(_gen_edge);
              	'''
              ]
              
           
     		  
     		  // Generate Properties, Interactions and Filters code on the graph level
    		  for (reln:meln.relelns){
    		  	switch(reln) {
          	       	  	  InteractionDef : {
                    members+=reln.toMethod(reln.name,typeRef(Void::TYPE))[
          	  	  	  for(p:reln.params){
          	  	  	  	parameters += reln.toParameter(p.name,p.parameterType)
          	  	  	  }
                      body=''' 
                      	setMode(2);
                      	cleanOperator();
                      	«listype» cvtList = ((«typeRef(edgecname)»)getEdge()).get_agr_«reln.name»();
                      	 		if(cvtList != null){
                      	 			for(«aggregType» cvt : cvtList) {
                      	 				setCellOperator(cvt);
                      	 			}
                      	 		}
                      	beginTransaction();
                      	for(«typeRef(edgecname)» _edg_ : this){
                      		_edg_.«reln.name»(«var ci = 0»«FOR p : reln.params»«IF ci > 0»,«ENDIF»«p.name»«{ci = 1; null}»«ENDFOR»);
                      	   		«IF (reln.comitexpressions.size() > 0)»
                      	 		«var test = false»
                      	 		«FOR ce:reln.comitexpressions»
                      	 			«IF !ce.rol.type.equals(cellType)»
                      	 				«{test = true; null}»
                      	 			«ENDIF»
                      	 		«ENDFOR»
          	  	      	   		«IF test = true»
          	  	      	   		_edg_._agr_«reln.name»();
                      	   		«ENDIF»
                      	   «ENDIF»
                      	}
                      	endTransaction();
                      '''
                    ]
          	  	  }
          	  	  Filterdef : {
          	  	  	members += reln.toMethod(reln.name,typeRef(graphcname.toString))[
          	  	  	  for(p:reln.params) {
                        parameters += reln.toParameter(p.name,p.parameterType)
                      }
                      
                      body = '''
                        «meln.name+"_"+reln.name» _filter = new «meln.name+"_"+reln.name»(
                        «IF reln.params.size() > 0»
                        	«FOR i:0..reln.params.size()-1»
                        		«reln.params.get(i).name»
                        		«IF i < (reln.params.size()-1)»
                        			,
                        		«ENDIF»
                        	«ENDFOR»
                        «ENDIF»
                        );
                        super.addFilter(_filter);
                        return this;
                      '''
          	  	  	]
          	  	  }
 		        }
  		      }
     		]
     	}
	
	}else if(isCellGraph){
		val gridType = typeRef('fr.ocelet.runtime.raster.Grid')
 		val firstRole = meln.roles.get(0)
        val secondRole = meln.roles.get(1)      
        val roleType = typeRef(firstRole.type.fullyQualifiedName.toString)      

		if(!isAutoGraph){
	
	 // Generate the edge class
          	acceptor.accept(modl.toClass(edgecname))[          		
          	  superTypes += typeRef('fr.ocelet.runtime.relation.DiCursorEdge')
             
              val firstRoleType = typeRef(firstRole.type.fullyQualifiedName.toString)
              val secondRoleType = typeRef(secondRole.type.fullyQualifiedName.toString)
          		
          	  var jvmField = meln.toField(firstRole.name, firstRoleType)
          	    if (jvmField != null) {
          	      jvmField.setFinal(false) 
          		  members+= jvmField
          		
          		 }
          		jvmField = meln.toField(secondRole.name, secondRoleType)
          	  	if (jvmField != null) {
          	  	  jvmField.setFinal(false) 
          		  members+= jvmField
          	
          		}          		
          		
          		members+= meln.toConstructor[
          			parameters += meln.toParameter("grid1",typeRef("fr.ocelet.runtime.raster.Grid"))
          			parameters += meln.toParameter("grid2",typeRef("fr.ocelet.runtime.raster.Grid"))
          			
          			body = ''' 
          			  super(grid1, grid2);
          			  «firstRole.name» = new «firstRoleType»();
          			  «secondRole.name» = new «secondRoleType»();
          			  «firstRole.name».updateCellInfo(getCellType());
          			  «secondRole.name».updateCellInfo(getCellType());
          			  updateRoleInfo();
          		  '''
          		]
          		
          	
          		members += meln.toMethod("getRole",typeRef("fr.ocelet.runtime.relation.OcltRole"))[
          			parameters += meln.toParameter("i",typeRef('java.lang.Integer'))
          						body = '''
          				if (i==0) return «firstRole.name»;
          				else if (i==1) return «secondRole.name»;
          				else return null;
          			'''
          		]
          	
          		          		
          		members += meln.toMethod("update",typeRef(Void::TYPE))[
          			
          			body = '''
          				this. «firstRole.name».setX(x);
          				this. «firstRole.name».setY(y);
          				this. «secondRole.name».setX(x2);
          				this. «secondRole.name».setY(y2);
          			'''
          		]
          		
     		  // Generate Properties and Interactions on the edge level
    		  for (reln:meln.relelns){
    		  	switch(reln) {
          	  	  InteractionDef : {
          	  	  	
          	  	  	
          	  	  	
          	  	  	members+= reln.toMethod(reln.name, typeRef(Void::TYPE))[
          	  	  		var params =""
          	  	  		
          	  	  
          	  	  	var index = 0;
          	  	  	  for(p:reln.params){
          	  	  	  	parameters += reln.toParameter(p.name,p.parameterType)
          	  	  	  	
          	  	  	  	if(index == reln.params.size){
          	  	  	  		params = params + p.name
          	  	  	  	}else{
          	  	  	  		params = params + p.name+","
          	  	  	  	}
          	  	  	  }
          	  	  	  val finalParams = params
          	  	  	  body = reln.body
          	  	  	]
          	  	    if (reln.comitexpressions.size() > 0) {
          	  	      members += reln.toMethod("get_agr_"+reln.name,listype)[
          	  	      	body = '''
          	  	      		«var index = 0»
          	  	      		«listype» cvtList = new «listype»();	
          	  	      		«FOR ce : reln.comitexpressions»
          	  	      			«aggregType» cvt«index» = new «aggregType»();	
          	  	      	  	 	cvt«index».setName("«ce.prop»"); 
          	  	      	  	 	cvt«index».setOperator(new «ce.agrfunc»());
          	  	      	  	 	cvtList.add(cvt«index»);
          	  	      	  	 	«{index = index + 1; null}»
							«ENDFOR»
           	  	      	  return cvtList;
          	  	      	'''
          	  	      ]
          	  	    }else{
          	  	    	   members += reln.toMethod("get_agr_"+reln.name,listype)[
          	  	      	body = '''
          	  	      		return null;
          	  	      	'''
          	  	      	]
          	  	    }
          	  	  }
          	  	}
  		      }
          		
          	]
          	
          	// -- Generate filters classes --
          	for (reln:meln.relelns){
    		  	switch(reln) {
          	  	  Filterdef : {
          	  	  	val filterfqn = graphcname+"_"+reln.name
          	  	  	acceptor.accept(modl.toClass(filterfqn))[
                     
                      val firstRoleType = typeRef(firstRole.type.fullyQualifiedName.toString)
                      val secondRoleType = typeRef(secondRole.type.fullyQualifiedName.toString)
                      superTypes += typeRef("fr.ocelet.runtime.relation.EdgeFilter",firstRoleType,secondRoleType)
                      for(p:reln.params){
                      	val pfield = reln.toField(p.name,p.parameterType)
                      	if (pfield != null) {
          	  	          pfield.setFinal(false)
          	  	          members += pfield
          	  	        }
                      }
                      members += reln.toConstructor()[
                      	for(p:reln.params) {
                      	  parameters += reln.toParameter(p.name,p.parameterType)
                        }
                        body = '''
                        	«FOR p : reln.params»
                         	this.«p.name» = «p.name»;
                         	«ENDFOR»
                        '''
                      ]                      
          	  	  	  members += reln.toMethod("filter",typeRef("java.lang.Boolean"))[
          	  	  		parameters += reln.toParameter(firstRole.name,firstRoleType)
          	  	  		parameters += reln.toParameter(secondRole.name,secondRoleType)
          	  	  		body = reln.body
          	  	  	  ]
          	  	  	]
          	  	  }
           	  	}
          	 }
          	
          	// -- Generate the interaction graph class --
          	if	(typeRef(edgecname) != null) {
          	acceptor.accept(modl.toClass(graphcname))[
      		  documentation = meln.documentation
           
              val firstRoleType = typeRef(firstRole.type.fullyQualifiedName.toString)
              val secondRoleType = typeRef(secondRole.type.fullyQualifiedName.toString)
              
              superTypes += typeRef(graphTypeName, typeRef(edgecname), firstRoleType, secondRoleType)

              // Generate an empty constructor
     		  members+= meln.toConstructor[	
     		  	body =  '''
     		  		super();
     		     '''
     		  ]
              // Generate DiGraph overridden methods : connect, getLeftSet, getRightSet
              members+= meln.toMethod("setGraph",typeRef(Void::TYPE))[
              	parameters += meln.toParameter("grid1",gridType)
              parameters += meln.toParameter("grid2",gridType)
              	body = '''
              		
                  super.setGrid(grid1, grid2);
                   «typeRef(edgecname)» _gen_edge_ = new «meln.name+"_Edge"»(grid1, grid2);
     		  	  setCompleteIteratorDiCell(_gen_edge_ );
              	'''
              ]
     		  
     		  // Generate Properties, Interactions and Filters code on the graph level
    		  for (reln:meln.relelns){
    		  	switch(reln) {
          	  	  RelPropertyDef : {
                    members+=reln.toMethod("set"+reln.name.toFirstUpper,typeRef(Void::TYPE))[
                      parameters+= reln.toParameter(reln.name,reln.type)	
                      body= '''
                      	for( «typeRef(edgecname)» _edg_ : this )
                      	_edg_.set«reln.name.toFirstUpper»(«reln.name»);
                      '''
                    ]
          	  	  }
          	  	  InteractionDef : {
                    members+=reln.toMethod(reln.name,typeRef(Void::TYPE))[
          	  	  	  for(p:reln.params){
          	  	  	  	parameters += reln.toParameter(p.name,p.parameterType)
          	  	  	  }
                      body= '''
                      	updateGrid();
                      	cleanOperator();
                      	«listype» cvtList = ((«typeRef(edgecname)»)getEdge()).get_agr_«reln.name»();
                      	if(cvtList != null){
                      		for(«aggregType» cvt : cvtList){
                      	 		setCellOperator(cvt);
                      		}
                      	}
                       	for(«typeRef(edgecname)» _edg_ : this) {
                      	_edg_.«reln.name»(«var ci = 0»«FOR p : reln.params»«IF ci > 0»,«ENDIF»«p.name»«{ci = 1; null}»«ENDFOR»);
                      	}
                      '''
                    ]
          	  	  }
          	  	  Filterdef : {
          	  	  	members += reln.toMethod(reln.name,typeRef(graphcname.toString))[
          	  	  	  for(p:reln.params) {
                        parameters += reln.toParameter(p.name,p.parameterType)
                      }
                      body = '''
                      	«meln.name+"_"+reln.name» _filter = new «meln.name+"_"+reln.name»
                      	«IF reln.params.size() > 0»
                      	«FOR i : 0..reln.params.size() - 1»	reln.params.get(i).name»«IF i < (reln.params.size()-1) »,«ENDIF»«ENDFOR»«ENDIF»;
                        super.addFilter(_filter);
                        return this;
                      	
                       '''
          	  	  	]
          	  	  }
 		        }
  		      }
     		]
     	}
	
      }else{
	
             
	 // Generate the edge class
          	acceptor.accept(modl.toClass(edgecname))[          		
          	  superTypes += typeRef('fr.ocelet.runtime.relation.CursorEdge')
             
              val firstRoleType = typeRef(firstRole.type.fullyQualifiedName.toString)
              val secondRoleType = typeRef(secondRole.type.fullyQualifiedName.toString)
          		
          	  var jvmField = meln.toField(firstRole.name, firstRoleType)
          	    if (jvmField != null) {
          	      jvmField.setFinal(false) 
          		  members+= jvmField
          		
          		 }
          		jvmField = meln.toField(secondRole.name, secondRoleType)
          	  	if (jvmField != null) {
          	  	  jvmField.setFinal(false) 
          		  members+= jvmField
          	
          		}          		
          		
          		members+= meln.toConstructor[
          			parameters += meln.toParameter("igr",typeRef("fr.ocelet.runtime.raster.Grid"))
          			body = '''
          				super(igr);
          			    «firstRole.name» = new «firstRoleType»();
          			    «secondRole.name» = new «secondRoleType»();
          			    «firstRole.name».updateCellInfo(getCellType());
          			    «secondRole.name».updateCellInfo(getCellType());
          		  '''
          		]
          		members += meln.toMethod("getRole",typeRef("fr.ocelet.runtime.relation.OcltRole"))[
          			parameters += meln.toParameter("i",typeRef('java.lang.Integer'))
          						body = '''
          				if (i==0) return «firstRole.name»;
          				else if (i==1) return «secondRole.name»;
          				else return null;
          			'''
          		]
          		members += meln.toMethod("updateCellType",typeRef(Void::TYPE))[
          			
          						body = '''
          							 «firstRole.name».updateCellInfo(getCellType());
          							 «secondRole.name».updateCellInfo(getCellType());
          			'''
          		]
          		          		
          		members += meln.toMethod("update",typeRef(Void::TYPE))[
          			
          			body = '''
          				
          				this. «firstRole.name».setX(x);
          				this. «firstRole.name».setY(y);
          			this. «secondRole.name».setX(x2);
          			this. «secondRole.name».setY(y2);
          			'''
          		]
          		
     		  // Generate Properties and Interactions on the edge level
    		  for (reln:meln.relelns){
    		  	switch(reln) {
          	  	  InteractionDef : {          	  	  	          	  	  	
          	  	  	
          	  	  	members+= reln.toMethod(reln.name,typeRef(Void::TYPE))[
          	  	  		var params =""          	  	  		
          	  	  
          	  	  	var index = 0;
          	  	  	  for(p:reln.params){
          	  	  	  	parameters += reln.toParameter(p.name,p.parameterType)
          	  	  	  	
          	  	  	  	if(index == reln.params.size){
          	  	  	  		params = params + p.name
          	  	  	  	}else{
          	  	  	  		params = params + p.name+","
          	  	  	  	}
          	  	  	  }
          	  	  	  val finalParams = params
          	  	  	  body = reln.body
          	  	  	]
          	  	  	 
          	  	    if (reln.comitexpressions.size() > 0) {
          	  	      members += reln.toMethod("get_agr_"+reln.name,listype)[
          	  	      	body = '''
          	  	      		«var index = 0»
          	  	      	«listype» cvtList = new «listype»();
          	  	      	«FOR ce : reln.comitexpressions»
          	  	      		«aggregType» cvt«index» = new «aggregType»();
          	  	      	  	cvt«index».setName("«ce.prop»"); 
          	  	      	  	cvt«index».setOperator(new  «ce.agrfunc»());
          	  	      	  	cvtList.add(cvt«index»);
          	  	      	  	«{index = index + 1; null}»
           	  	      	  «ENDFOR»
           	  	      	   return cvtList;
          	  	      	'''
          	  	      ]
          	  	    }else{
          	  	    	   members += reln.toMethod("get_agr_"+reln.name,listype)[
          	  	      	body = '''
          	  	      		return null;
          	  	      	 '''         	  	      	
          	  	      	]
          	  	    }
          	  	  }
          	  	}
  		      }
          		
          	]
          	
          	// -- Generate filters classes --
          	for (reln:meln.relelns){
    		  	switch(reln) {
          	  	  Filterdef : {
          	  	  	val filterfqn = graphcname+"_"+reln.name
          	  	  	acceptor.accept(modl.toClass(filterfqn))[
                     
                      val firstRoleType = typeRef(firstRole.type.fullyQualifiedName.toString)
                      val secondRoleType = typeRef(secondRole.type.fullyQualifiedName.toString)
                      superTypes += typeRef("fr.ocelet.runtime.relation.EdgeFilter",firstRoleType,secondRoleType)
                      
                      for(p:reln.params){
                      	
                      	val pfield = reln.toField(p.name,p.parameterType)
                      	
                      	if (pfield != null) {
          	  	          pfield.setFinal(false)
          	  	          members += pfield
          	  	        }
                      }
                      members += reln.toConstructor()[
                      	for(p:reln.params) {
                      	  parameters += reln.toParameter(p.name,p.parameterType)
                        }
                        body = '''
                        	«FOR p : reln.params»
                         	  this.«p.name» = «p.name»;
                        	«ENDFOR»
                        '''
                      ]                      
          	  	  	  members += reln.toMethod("filter", typeRef("java.lang.Boolean"))[
          	  	  		parameters += reln.toParameter(firstRole.name,firstRoleType)
          	  	  		parameters += reln.toParameter(secondRole.name,secondRoleType)
          	  	  		body = reln.body
          	  	  	  ]
          	  	  	]
          	  	  }
           	  	}
          	 }
          	
          	// -- Generate the interaction graph class --
          	if	(typeRef(edgecname) != null) {
          	acceptor.accept(modl.toClass(graphcname))[
      		  documentation = meln.documentation
           
              val firstRoleType = typeRef(firstRole.type.fullyQualifiedName.toString)
              
              
              superTypes += typeRef(graphTypeName, typeRef(edgecname), firstRoleType)

              // Generate an empty constructor
     		  members+= meln.toConstructor[	
     		  	body = '''
     		  		 super();
     		    '''
     		  ]
	
              // Generate DiGraph overridden methods : connect, getLeftSet, getRightSet
              members+= meln.toMethod("setGrid",typeRef(Void::TYPE))[
              	parameters += meln.toParameter("grid",gridType)
              
              	body = '''
              		
              		
                  super.setGrid(grid);
                   «typeRef(edgecname)» _gen_edge_ = new «meln.name+"_Edge"»(grid);
     		  	  setCompleteIteratorCell(_gen_edge_ );
              	'''
              ]
              
              	/* Generate method for generation graphs */
              	 
          		members += meln.toMethod("createHexagons",typeRef(Void::TYPE))[
          			parameters += meln.toParameter("shp",typeRef("fr.ocelet.datafacer.ocltypes.Shapefile"))
          			parameters += meln.toParameter("size",typeRef('java.lang.Double'))
          			body = '''
          				«firstRoleType» entity = new «firstRoleType»();
          				«gridType» grid = createHexagon("«graphcname»",entity.getProps(), shp.getBounds(), size);
          				
          			    fr.ocelet.runtime.raster.GridManager.getInstance().add(grid); 
                  	 	entity.setNumGrid(fr.ocelet.runtime.raster.GridManager.getInstance().getCurrentIndex());
                  	   	setGrid(grid);
                  	   	setCellShapeType("HEXAGONAL");
          			'''
          		]
          		
          		members += meln.toMethod("createHexagons",typeRef(Void::TYPE))[
          		parameters += meln.toParameter("size",typeRef('java.lang.Double'))
          		parameters += meln.toParameter("minX",typeRef('java.lang.Double'))
          		parameters += meln.toParameter("minY",typeRef('java.lang.Double'))
          		parameters += meln.toParameter("maxX",typeRef('java.lang.Double'))
          		parameters += meln.toParameter("maxY",typeRef('java.lang.Double'))
          			
          		body = '''
          				«firstRoleType» entity = new «firstRoleType»();
          				«gridType» grid =  createHexagon("«graphcname»",entity.getProps(), minX, minY, maxX, maxY, size);
          				fr.ocelet.runtime.raster.GridManager.getInstance().add(grid);
                  	   	entity.setNumGrid(fr.ocelet.runtime.raster.GridManager.getInstance().getCurrentIndex());
                  	   	setGrid(grid);
                  	   	setCellShapeType("HEXAGONAL");
          			'''
          		]
          		
          		members += meln.toMethod("createSquares",typeRef(Void::TYPE))[
          			parameters += meln.toParameter("shp",typeRef("fr.ocelet.datafacer.ocltypes.Shapefile"))
          			parameters += meln.toParameter("xRes",typeRef('java.lang.Double'))
          			parameters += meln.toParameter("yRes",typeRef('java.lang.Double'))
          						body = '''
          				«firstRoleType» entity = new «firstRoleType»();
          				«gridType» grid = createSquare("«graphcname»",entity.getProps(), shp.getBounds(), xRes, yRes);
          				fr.ocelet.runtime.raster.GridManager.getInstance().add(grid); 
                  	   	entity.setNumGrid(fr.ocelet.runtime.raster.GridManager.getInstance().getCurrentIndex());
                  	   	setGrid(grid);
                  	   	setCellShapeType("QUADRILATERAL");
          			'''
          		]
          		
          		members += meln.toMethod("createSquares",typeRef(Void::TYPE))[
          		parameters += meln.toParameter("xRes",typeRef('java.lang.Double'))
          		parameters += meln.toParameter("yRes",typeRef('java.lang.Double'))
          		parameters += meln.toParameter("minX",typeRef('java.lang.Double'))
          		parameters += meln.toParameter("minY",typeRef('java.lang.Double'))
          		parameters += meln.toParameter("maxX",typeRef('java.lang.Double'))
          		parameters += meln.toParameter("maxY",typeRef('java.lang.Double'))
          			
          			body = '''
          				
          				«firstRoleType» entity = new «firstRoleType»();
          				«gridType» grid = createSquare("«graphcname»",entity.getProps(), minX, minY, maxX, maxY, xRes, yRes);
          			    fr.ocelet.runtime.raster.GridManager.getInstance().add(grid);
                  	    entity.setNumGrid(fr.ocelet.runtime.raster.GridManager.getInstance().getCurrentIndex());
                  	    setGrid(grid);
                  	    setCellShapeType("QUADRILATERAL");
          			'''
          		]
          		
          		members += meln.toMethod("createTriangles",typeRef(Void::TYPE))[
          		parameters += meln.toParameter("shp",typeRef("fr.ocelet.datafacer.ocltypes.Shapefile"))
          		parameters += meln.toParameter("size",typeRef('java.lang.Double'))
          			body = '''
          				«firstRoleType» entity = new «firstRoleType»();
          				«gridType» grid = createTriangle("«graphcname»",entity.getProps(), shp.getBounds(), size);
          				fr.ocelet.runtime.raster.GridManager.getInstance().add(grid); 
                  	   	entity.setNumGrid(fr.ocelet.runtime.raster.GridManager.getInstance().getCurrentIndex());
                  	   	setGrid(grid);
                  	   	setCellShapeType("TRIANGULAR");
          			'''
          		]
          		
          		members += meln.toMethod("createTriangles",typeRef(Void::TYPE))[
          		parameters += meln.toParameter("size",typeRef('java.lang.Double'))
          		parameters += meln.toParameter("minX",typeRef('java.lang.Double'))
          		parameters += meln.toParameter("minY",typeRef('java.lang.Double'))
          		parameters += meln.toParameter("maxX",typeRef('java.lang.Double'))
          		parameters += meln.toParameter("maxY",typeRef('java.lang.Double'))
          			
          			body = '''
          				«firstRoleType» entity = new «firstRoleType»();
          				«gridType» grid = createTriangle("«graphcname»",entity.getProps(), minX, minY, maxX, maxY, size);
          			 	fr.ocelet.runtime.raster.GridManager.getInstance().add(grid);
                  	   	entity.setNumGrid(fr.ocelet.runtime.raster.GridManager.getInstance().getCurrentIndex());
                  	   	setGrid(grid);
                  	   	setCellShapeType("TRIANGULAR");
          			'''
          		]
              
              
     		  
     		  // Generate Properties, Interactions and Filters code on the graph level
    		  for (reln:meln.relelns){
    		  	switch(reln) {
          	  	  RelPropertyDef : {
                    members+=reln.toMethod("set"+reln.name.toFirstUpper,typeRef(Void::TYPE))[
                      parameters+= reln.toParameter(reln.name,reln.type)	
                      body='''
                      	
                      	for(«typeRef(edgecname)» _edg_ : this)
                      	_edg_.set«reln.name.toFirstUpper»(«reln.name»);
                      '''
                    ]
          	  	  }
          	  	  InteractionDef : {
                    members+=reln.toMethod(reln.name,typeRef(Void::TYPE))[
          	  	  	  for(p:reln.params){
          	  	  	  	parameters += reln.toParameter(p.name,p.parameterType)
          	  	  	  }
                      body= '''
                      		setMode(0);
                      	 	cleanOperator();
                      	 	«listype» cvtList = ((typeRef(edgecname))getEdge()).get_agr_«reln.name»();
                      	 	if(cvtList != null){
                      	 		for(«aggregType» cvt : cvtList){
                      	 			setCellOperator(cvt);
                      	 		} 
                      	 	}
                       		for(«typeRef(edgecname)» _edg_ : this) {
                      		  _edg_.«reln.name»(«var ci = 0»«FOR p:reln.params» «IF ci > 0»,«ENDIF»«p.name»«{ci = 1; null}»«ENDFOR»);
                      		
                      		}
                      '''
                    ]
          	  	  }
          	  	  Filterdef : {
          	  	  	members += reln.toMethod(reln.name,typeRef(graphcname.toString))[
          	  	  	  for(p:reln.params) {
                        parameters += reln.toParameter(p.name,p.parameterType)
                      }
                      body = '''
                      	«meln.name+"_"+reln.name» _filter = new «meln.name+"_"+reln.name»(
                      	«IF reln.params.size() > 0»
                      		«FOR i : 0..reln.params.size() - 1»	
                      			«reln.params.get(i).name»
                      				«IF i < (reln.params.size()-1)»	
                      					,
                      				«ENDIF»
                      			«ENDFOR»
                      	«ENDIF»
                      			);
                      				
                        super.addFilter(_filter);
                        return this;
                      '''
          	  	  	]
          	  	  }
 		        }
  		      }
     		]
     	   }
     	}
	}else{



            // Generate the edge class
          	acceptor.accept(modl.toClass(edgecname))[
          	  superTypes += typeRef('fr.ocelet.runtime.relation.OcltEdge')

           /*   if ((meln.roles.size >= 2) &&
            	(meln.roles.get(0)!=null) && (meln.roles.get(1)!=null) &&
            	(meln.roles.get(0).type != null) && (meln.roles.get(1).type != null) &&
       	        (meln.roles.get(0).type.fullyQualifiedName != null) &&
            	(meln.roles.get(1).type.fullyQualifiedName != null) &&
            	(meln.roles.get(0).name != null) && (meln.roles.get(1).name != null)
                ) {*/

              val firstRole = meln.roles.get(0)
              val secondRole = meln.roles.get(1)
              val firstRoleType = typeRef(firstRole.type.fullyQualifiedName.toString)
              val secondRoleType = typeRef(secondRole.type.fullyQualifiedName.toString)
          		
          	  var jvmField = meln.toField(firstRole.name, firstRoleType)
          	    if (jvmField != null) {
          	      jvmField.setFinal(false) 
          		  members+= jvmField
          		  members+= meln.toSetter(firstRole.name, firstRoleType)
          		  members+= meln.toGetter(firstRole.name, firstRoleType)
          		 }
          		jvmField = meln.toField(secondRole.name, secondRoleType)
          	  	if (jvmField != null) {
          	  	  jvmField.setFinal(false) 
          		  members+= jvmField
          		  members+= meln.toSetter(secondRole.name, secondRoleType)
          		  members+= meln.toGetter(secondRole.name, secondRoleType)
          		}          		
          		
          		members+= meln.toConstructor[
          			parameters += meln.toParameter("igr",typeRef("fr.ocelet.runtime.relation.InteractionGraph"))
          			parameters += meln.toParameter("first",firstRoleType)
          			parameters += meln.toParameter("second",secondRoleType)
          			body = '''
          			  super(igr);
          			  «firstRole.name»=first;
          			  «secondRole.name»=second;
          			'''
          		]
          		
          		members += meln.toMethod("getRole",typeRef("fr.ocelet.runtime.relation.OcltRole"))[
          			parameters += meln.toParameter("i",typeRef("int"))
          			body = '''
          			  if (i==0) return «firstRole.name»;
          			  else if (i==1) return «secondRole.name»;
          			  else return null;
          	       '''
          		]
            // } // if roles are well defined
                	
                		    		
     		  // Generate Properties and Interactions on the edge level
    		  for (reln:meln.relelns){
    		  	switch(reln) {
          	  	  RelPropertyDef : {
            	    val rField = reln.toField(reln.name, reln.type)
          	  	    if (rField != null) {
          	  	      rField.setFinal(false) 
          		      members+= rField
          		      members+= reln.toSetter(reln.name, reln.type)
          		      members+= reln.toGetter(reln.name, reln.type)
          		     }
        	  	  	
          	  	  }
          	  	  InteractionDef : {
          	  	  	members+= reln.toMethod(reln.name,typeRef(Void::TYPE))[
          	  	  	  for(p:reln.params){
          	  	  	  	parameters += reln.toParameter(p.name,p.parameterType)
          	  	  	  }
          	  	  	  body = reln.body
          	  	  	]
          	  	    if (reln.comitexpressions.size() > 0) {
          	  	      members += reln.toMethod("_agr_"+reln.name,typeRef(Void::TYPE))[
          	  	      	body = '''«FOR ce:reln.comitexpressions»
          	  	      	    this.«ce.rol.getName()».setAgregOp("«ce.prop»",new «ce.agrfunc»(),«ce.usepreval»);
           	  	      	  «ENDFOR»
          	  	      	'''
          	  	      ]
          	  	    }
          	  	  }
          	  	}
  		      }

          	]
          	
          	// -- Generate filters classes --
          	for (reln:meln.relelns){
    		  switch(reln) {
          	  	Filterdef : {
          	  	  val filterfqn = graphcname+"_"+reln.name
          	  	  acceptor.accept(modl.toClass(filterfqn))[
                    if ((meln.roles.size >= 2) &&
            	    (meln.roles.get(0)!=null) && (meln.roles.get(1)!=null) &&
            	    (meln.roles.get(0).type != null) && (meln.roles.get(1).type != null) &&
       	            (meln.roles.get(0).type.fullyQualifiedName != null) &&
            	    (meln.roles.get(1).type.fullyQualifiedName != null) &&
            	    (meln.roles.get(0).name != null) && (meln.roles.get(1).name != null)
                    ) {
                      val firstRole = meln.roles.get(0)
                      val secondRole = meln.roles.get(1)
                      val firstRoleType = typeRef(firstRole.type.fullyQualifiedName.toString)
                      val secondRoleType = typeRef(secondRole.type.fullyQualifiedName.toString)
                      superTypes += typeRef("fr.ocelet.runtime.relation.EdgeFilter",firstRoleType,secondRoleType)
                      for(p:reln.params){
                      	val pfield = reln.toField(p.name,p.parameterType)
                      	if (pfield != null) {
          	  	          pfield.setFinal(false)
          	  	          members += pfield
          	  	        }
                      }
                      members += reln.toConstructor()[
                      	for(p:reln.params) {
                      	  parameters += reln.toParameter(p.name,p.parameterType)
                        }
                        body = '''
                        	«FOR p:reln.params»
                        	 this.«p.name» = «p.name»;
                        	 «ENDFOR»
                        '''
                      ]                      
          	  	  	  members += reln.toMethod("filter",typeRef("java.lang.Boolean"))[
          	  	  		parameters += reln.toParameter(firstRole.name,firstRoleType)
          	  	  		parameters += reln.toParameter(secondRole.name,secondRoleType)
          	  	  		body = reln.body
          	  	  	  ]
          	  	  	} // if roles are well defined
          	      ]
          	    }
           	  }
          	}
          	
          	// -- Generate the interaction graph class --
          if(typeRef(edgecname) != null) {
          acceptor.accept(modl.toClass(graphcname)) [
      		documentation = meln.documentation
            if ((meln.roles.size >= 2) &&
             (meln.roles.get(0)!=null) && (meln.roles.get(1)!=null) &&
             (meln.roles.get(0).type != null) && (meln.roles.get(1).type != null) &&
       	     (meln.roles.get(0).type.fullyQualifiedName != null) &&
             (meln.roles.get(1).type.fullyQualifiedName != null) &&
             (meln.roles.get(0).name != null) && (meln.roles.get(1).name != null)
             ) {
              val firstRole = meln.roles.get(0)
              val secondRole = meln.roles.get(1)
              val firstRoleType = typeRef(firstRole.type.fullyQualifiedName.toString)
              val secondRoleType = typeRef(secondRole.type.fullyQualifiedName.toString)
              val rolset1 = meln.roles.get(0).name+"Set"
              val rolset2 = meln.roles.get(1).name+"Set"
              //val isAutoGraph = (meln.roles.get(0).type.equals(meln.roles.get(1).type))
              //val graphTypeName = if(isAutoGraph) 'fr.ocelet.runtime.relation.impl.AutoGraph'
                //                           else 'fr.ocelet.runtime.relation.impl.DiGraph'               		  
             if (isAutoGraph) superTypes += typeRef(graphTypeName, typeRef(edgecname), firstRoleType)
             else superTypes += typeRef(graphTypeName, typeRef(edgecname), firstRoleType, secondRoleType)

              // Generate an empty constructor
     		  members+= meln.toConstructor[	body = '''super();''' ]

              // Generate DiGraph overridden methods : connect, getLeftSet, getRightSet
              members+= meln.toMethod("connect",typeRef(edgecname))[
              	parameters += meln.toParameter(firstRole.name,firstRoleType)
              	parameters += meln.toParameter(secondRole.name,secondRoleType)
                body = '''
                if ((this.«rolset1» == null) || (!this.«rolset1».contains(«firstRole.name»))) add(«firstRole.name»);
                «IF (!isAutoGraph)»
                if ((this.«rolset2» == null) || (!this.«rolset2».contains(«secondRole.name»))) add(«secondRole.name»);
                «ENDIF»
                «val typ_edgecname = typeRef(edgecname)»
                «typ_edgecname» _gen_edge_ = new «meln.name+"_Edge"»(this,«firstRole.name»,«secondRole.name»);
                addEdge(_gen_edge_);
                return _gen_edge_;
                '''
              ]
              
              members+= meln.toMethod("getLeftSet", typeRef("fr.ocelet.runtime.relation.RoleSet",firstRoleType))[
              	body ='''return «rolset1»;'''
              ]
              
              members+= meln.toMethod("getRightSet", typeRef("fr.ocelet.runtime.relation.RoleSet",secondRoleType))[
              	body =''' 
              	 «IF (isAutoGraph)»return «rolset1»;
              	 «ELSE»return «rolset2»;
              	 «ENDIF»
              	'''
               ]

          	   members +=meln.toMethod("getComplete",typeRef(graphcname.toString)) [
          			body='''return («meln.name»)super.getComplete();'''
          	   ]

               members += meln.toMethod("createEdge",typeRef(edgecname))[
               	  parameters += firstRole.toParameter(firstRole.name,firstRoleType)
               	  parameters += secondRole.toParameter(secondRole.name,secondRoleType)
               	  body = ''' return new «meln.name+"_Edge"»(this,«firstRole.name»,«secondRole.name»);'''
               ]


              // -- Generate RoleSet fields, setters, getters and add+remove role functions --

     		    val rsetype =  typeRef("fr.ocelet.runtime.relation.RoleSet",firstRoleType)
     		  	val rsfield = meln.toField(rolset1,rsetype)
     		  	if (rsfield != null) {
     		  	  members += rsfield
  		          members+= meln.toMethod('set'+rolset1.toFirstUpper, typeRef(Void::TYPE))[
   		            parameters += firstRole.toParameter("croles",typeRef('java.util.Collection',firstRoleType))
                    body='''
     		          «val rsimplt = typeRef("fr.ocelet.runtime.relation.impl.RoleSetImpl",firstRoleType)»
     		          this.«rolset1»=new «rsimplt»(croles);
     		  	    '''
   		  	      ]
   		  	      
   		  	      members+= meln.toMethod('get'+rolset1.toFirstUpper, rsetype)[
   		  	      	body ='''return «rolset1»;'''
   		  	      ]

                  if(!isAutoGraph) {
     		        val rsetype2 =  typeRef("fr.ocelet.runtime.relation.RoleSet",secondRoleType)
     		  	    val rsfield2 = meln.toField(rolset2,rsetype2)
     		  	    if (rsfield2 != null) {
     		  	      members += rsfield2

   		              members+= meln.toMethod('set'+rolset2.toFirstUpper, typeRef(Void::TYPE))[
     		            parameters += secondRole.toParameter("croles",typeRef('java.util.Collection',secondRoleType))
                        body='''
                          «val rsimplt = typeRef("fr.ocelet.runtime.relation.impl.RoleSetImpl",secondRoleType)»
                          this.«rolset2»=new «rsimplt»(croles);
                        '''
   		  	          ]
   		  	      
   		  	          members+= meln.toMethod('get'+rolset2.toFirstUpper, rsetype2)[
   		  	      	    body ='''return «rolset2»;'''
   		  	          ]
                    }
                  }
   		  	      
   		  	      members += meln.toMethod('add',typeRef(Void::TYPE))[
   		  	      	parameters += meln.toParameter('role', firstRoleType)
   		  	      	body = '''add«firstRoleType»(role);'''
   		  	      ]

   		  	      members += meln.toMethod('remove',typeRef(Void::TYPE))[
   		  	      	parameters += meln.toParameter('role', firstRoleType)
   		  	      	body = '''remove«firstRoleType»(role);'''
   		  	      ]

                  members += meln.toMethod('add'+firstRoleType.simpleName,typeRef(Void::TYPE))[
   		  	      	parameters += meln.toParameter('role',firstRoleType)
   		  	      	body = '''
   		  	      	  «val ltype = typeRef('java.util.HashSet',firstRoleType)»
   		  	      	  if (this.«rolset1» == null) set«rolset1.toFirstUpper»( new «ltype»());
   		  	      	  this.«rolset1».addRole(role);
   		  	      	'''
   		  	      ]

   		  	      members += meln.toMethod('remove'+firstRoleType.simpleName,typeRef(Void::TYPE))[
   		  	      	parameters += meln.toParameter('role', firstRoleType)
   		  	      	body = '''if (this.«rolset1» != null) this.«rolset1».removeRole(role);'''
   		  	      ]
 		  	      
   		  	      members += meln.toMethod('addAll'+firstRoleType.simpleName,typeRef(Void::TYPE))[
   		  	      	parameters += meln.toParameter('roles',typeRef('java.lang.Iterable',firstRoleType))
   		  	      	body = '''
   		  	      	  «val ltype = typeRef('java.util.HashSet',firstRoleType)»
   		  	      	  if (this.«rolset1» == null) set«rolset1.toFirstUpper»( new «ltype»());
   		  	      	  this.«rolset1».addRoles(roles);
   		  	      	'''
   		  	      ]

                  members += meln.toMethod('removeAll'+firstRoleType.simpleName,typeRef(Void::TYPE))[
   		  	      	parameters += meln.toParameter('roles',typeRef('java.lang.Iterable',firstRoleType))
   		  	      	body = '''if (this.«rolset1» != null) this.«rolset1».removeRoles(roles);'''
   		  	      ]
   		  	      
   		  	      if (!isAutoGraph) {
                    members += meln.toMethod('add'+secondRoleType.simpleName,typeRef(Void::TYPE))[
   		  	      	  parameters += meln.toParameter('role',secondRoleType)
   		  	      	  body = '''
   		  	      		«val ltype = typeRef('java.util.HashSet',secondRoleType)»
   		  	      		if (this.«rolset2» == null) set«rolset2.toFirstUpper»( new «ltype»());
   		  	      		this.«rolset2».addRole(role);
   		  	      	  '''
   		  	        ]

   		  	        members += meln.toMethod('remove',typeRef(Void::TYPE))[
   		  	      	  parameters += meln.toParameter('role', secondRoleType)
   		  	      	  body = '''remove«secondRoleType»(role);'''
   		  	        ]

   		  	        members += meln.toMethod('add',typeRef(Void::TYPE))[
   		  	      	  parameters += meln.toParameter('role', secondRoleType)
   		  	      	  body = '''add«secondRoleType»(role);'''
   		  	        ]

   		  	        members += meln.toMethod('remove'+secondRoleType.simpleName,typeRef(Void::TYPE))[
   		  	      	  parameters += meln.toParameter('role', secondRoleType)
   		  	      	  body = '''if (this.«rolset2» != null) this.«rolset2».removeRole(role);'''
   		  	        ]

   		  	        members += meln.toMethod('addAll'+secondRoleType.simpleName,typeRef(Void::TYPE))[
   		  	      	  parameters += meln.toParameter('roles',typeRef('java.lang.Iterable',secondRoleType))
   		  	      	  body = '''
   		  	      		«val rtype = typeRef('java.util.HashSet',secondRoleType)»
   		  	      		if (this.«rolset2» == null) set«rolset2.toFirstUpper»( new «rtype»());
   		  	      		this.«rolset2».addRoles(roles);
   		  	      	  '''
   		  	        ]

                    members += meln.toMethod('removeAll'+secondRoleType.simpleName,typeRef(Void::TYPE))[
   		  	      	  parameters += meln.toParameter('roles',typeRef('java.lang.Iterable',secondRoleType))
   		  	      	  body = '''if (this.«rolset2» != null) this.«rolset2».removeRoles(roles);'''
   		  	        ]
   		  	      } // if roles are well defined
   		  	   }
   		     }
     		  
     		  // Generate Properties, Interactions and Filters code on the graph level
    		  for (reln:meln.relelns){
    		  	switch(reln) {
          	  	  RelPropertyDef : {
          	  	  	if (reln.name != null) {
                    members+=reln.toMethod("set"+reln.name.toFirstUpper,typeRef(Void::TYPE))[
                      parameters+= reln.toParameter(reln.name,reln.type)	
                      body='''
                      	«val typ_edgecname = typeRef(edgecname)»
                      	beginTransaction();
                      	for(«typ_edgecname» _edg_ : this)
                      	  _edg_.set«reln.name.toFirstUpper»(«reln.name»);
                      	endTransaction();
                      '''
                    ]}
          	  	  }
          	  	  InteractionDef : {
          	  	  	if (reln.name != null) {
                    members+=reln.toMethod(reln.name,typeRef(Void::TYPE))[
          	  	  	  for(p:reln.params){
          	  	  	  	parameters += reln.toParameter(p.name,p.parameterType)
          	  	  	  }
                      body='''
                      	«val typ_edgecname = typeRef(edgecname)»
                      	beginTransaction();
                      	«var ci=0»
                      	for(«typ_edgecname» _edg_ : this) {
                      	  _edg_.«reln.name»(«FOR p:reln.params»«IF (ci++ > 0)»,«ENDIF»«p.name»«ENDFOR»);
                      	 «IF (reln.comitexpressions.size() > 0)»
                      	 _edg_._agr_«reln.name»();«ENDIF»
                      	}
                      	endTransaction();
                      '''
                    ]}
          	  	  }
          	  	  Filterdef : {
          	  	  	if (reln.name != null) {
          	  	  	members += reln.toMethod(reln.name,typeRef(graphcname.toString))[
          	  	  	  for(p:reln.params) {
                        parameters += reln.toParameter(p.name,p.parameterType)
                      }
                      body ='''
                        «meln.name+"_"+reln.name» _filter = new «meln.name+"_"+reln.name»(«IF (reln.params.size() > 0)»«FOR i:0..(reln.params.size()-1)»«reln.params.get(i).name»«IF i < (reln.params.size()-1)»,«ENDIF»«ENDFOR»«ENDIF»);
                        super.addFilter(_filter);
                        return this;
                      '''
          	  	  	]}
          	  	  }
 		        }
  		      }
     		]
     		} 
     		}// if (typeref(edgecname != null)
  //   		} // if series testing uncomplete relation definition
          }
		}
                      
       // ---- Structure -----------------------------------
          Strucdef : {
          	acceptor.accept(modl.toClass(meln.fullyQualifiedName))[
              if (meln.typeArgument != null) {
                val JvmTypeParameter param = TypesFactory::eINSTANCE.createJvmTypeParameter
                param.setName(meln.typeArgument)
                typeParameters += param
       		    if (meln.superType != null)
       		      {
       		        superTypes += typeRef(meln.superType, typeRef(param))
                   }
       		    }
       		    else if (meln.superType != null) superTypes += typeRef(meln.superType)
              val List<StrucVarDef> lvdefs = <StrucVarDef>newArrayList()
          	  for(steln:meln.strucelns) {
          	  	switch(steln) {
          	  	  StrucVarDef: {
          	  	   lvdefs.add(steln)
          	  	   var jvmField = steln.toField(steln.name, steln.type)
          	  	     if (jvmField != null) {
          	  	       jvmField.setFinal(false) 
          		       members+= jvmField
          		       members+= steln.toSetter(steln.name, steln.type)
          		       members+= steln.toGetter(steln.name, steln.type)
          		     }
      		      }
      		      StrucFuncDef: {
          	  	    if (steln.type == null) steln.type = typeRef(Void::TYPE)
          	  	    members += steln.toMethod(steln.name,steln.type)[
          	  	      documentation = steln.documentation
      		  	      for (p: steln.params) {
      		  	        parameters += p.toParameter(p.name, p.parameterType)
      		  	      }
  		  		      body = steln.body
          	   	    ]
       		      }
      		    }  
          	  }
          	  members += meln.toConstructor[
          	  	body = '''
          	  	  super();«FOR vardef:lvdefs»
          	  	  «var vtyp = vardef.type»
          	  	  «IF !vtyp.primitive»«vardef.name» = new «vtyp»«IF vtyp.isNumberType»("0");«ELSEIF vtyp.qualifiedName.equals("java.lang.Boolean")»(false);«ELSE»();«ENDIF»
                   «ENDIF»
                  «ENDFOR»
                '''
          	  ]
          	]
          }
    
   	    }          // switch(meln)

      } catch (Exception e) {
	      println('''Exception caught : «e.getMessage()»''')
        }
    }

     
    // ---- Scenario -----------------------------------
    // Génération de la classe qui contient le main() et d'une methode par scenario
    if (mainScen) {
      acceptor.accept(modl.toClass(packg+modlName)) [
          documentation = modl.documentation
          superTypes += typeRef('fr.ocelet.runtime.model.AbstractModel')
          members+= modl.toConstructor[
          // Metadata related code generation
          body = '''
            super("«modlName»");
            «IF (md.getModeldesc != null)»modDescription = "«md.getModeldesc»";«ENDIF»
            «IF (md.getWebpage != null)»modelWebPage = "«md.getWebpage»";«ENDIF»
            «IF (md.hasParameters)»
            «FOR pstuff:md.params»
            «val genptype = typeRef('fr.ocelet.runtime.model.Parameter',pstuff.getType)»
            «IF(pstuff.numericType)»
              «val implptype = typeRef('fr.ocelet.runtime.model.NumericParameterImpl',pstuff.getType)»
              «genptype» par_«pstuff.getName» = new «implptype»("«pstuff.getName»","«pstuff.getDescription»",«pstuff.getOptionnal»,«pstuff.getDvalueString»«IF (pstuff.getMinvalue == null)»,null«ELSE»,«pstuff.getMinvalue»«ENDIF»«IF (pstuff.getMaxvalue == null)»,null«ELSE»,«pstuff.getMaxvalue»«ENDIF»«IF (pstuff.getUnit == null)»,null«ELSE»,«pstuff.getUnit»«ENDIF»);
            «ELSE»
              «val implptype = typeRef('fr.ocelet.runtime.model.ParameterImpl',pstuff.getType)»
              «genptype» par_«pstuff.getName» = new «implptype»("«pstuff.getName»","«pstuff.getDescription»",«pstuff.getOptionnal»,«pstuff.getDvalueString»«IF (pstuff.getUnit == null)»,null«ELSE»,"«pstuff.getUnit»"«ENDIF»);
            «ENDIF»
            addParameter(par_«pstuff.getName»);
            «IF (pstuff.getDvalue != null)»
            «pstuff.name» = «pstuff.getDvalueString»;
            «ENDIF»
            «ENDFOR»
            «ENDIF»
          '''
          ]
                  
          for(scen:scens) {
   	  	    if (scen.name.compareTo(modlName) == 0) {
              members += modl.toMethod("main",typeRef(Void.TYPE)) [
              	 parameters += modl.toParameter('args', typeRef('java.lang.String').addArrayTypeDimension)
              	 setStatic(true)
                 body = '''
                 «modlName» model_«modlName» = new «modlName»();
                 model_«modlName».run_«modlName»();'''
              ]
              members += modl.toMethod("run_"+modlName,typeRef(Void.TYPE)) [
              	body = scen.body
              ]
              members += modl.toMethod("simulate",typeRef(Void.TYPE)) [
              parameters += modl.toParameter('in_params',typeRef('java.util.HashMap',typeRef('java.lang.String'),typeRef('java.lang.Object')))
              body = '''
              «IF (md.hasParameters)»
                «FOR pstuff:md.params»
                  «pstuff.getType.getSimpleName» val_«pstuff.getName» = («pstuff.getType.getSimpleName») in_params.get("«pstuff.getName»");
                  if (val_«pstuff.getName» != null) «pstuff.getName» = val_«pstuff.getName»;
                «ENDFOR»
              «ENDIF»
              run_«modlName»();
              ''']
   	  	    } else {
   	  	      var rtype = scen.type
      		  if (rtype == null) rtype = typeRef(Void::TYPE)
              members += scen.toMethod(scen.name,rtype) [
                for (p: scen.params) {
      		  	  parameters += p.toParameter(p.name, p.parameterType)
      		    }
      		  	body = scen.body
              ]
             }
           }
           // Produces a field for every declared parameter
           if (md.hasParameters) {
             for(pstuff:md.params) {
               var jvmField = modl.toField(pstuff.name, pstuff.type)
          	  	 if (jvmField != null) {
          	  	  jvmField.setFinal(false)
          		  members+= jvmField
          		  members+= modl.toSetter(pstuff.name, pstuff.type)
          		  members+= modl.toGetter(pstuff.name, pstuff.type)
          		 }
          	   }
            }            
         ]
      }
  }
  
  private def boolean isNumberType(JvmTypeReference vtyp){
  	return vtyp.qualifiedName.equals("java.lang.Integer") ||
  	       vtyp.qualifiedName.equals("java.lang.Double") ||
           vtyp.qualifiedName.equals("java.lang.Float") ||
           vtyp.qualifiedName.equals("java.lang.Long") ||
           vtyp.qualifiedName.equals("java.lang.Byte") ||
           vtyp.qualifiedName.equals("java.lang.Short")
   }
}

