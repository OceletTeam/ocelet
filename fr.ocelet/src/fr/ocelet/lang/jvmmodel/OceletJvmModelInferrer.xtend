package fr.ocelet.lang.jvmmodel

import com.google.inject.Inject
import fr.ocelet.lang.ocelet.ConstructorDef
import fr.ocelet.lang.ocelet.Datafacer
import fr.ocelet.lang.ocelet.Entity
import fr.ocelet.lang.ocelet.Metadata
import fr.ocelet.lang.ocelet.Model
import fr.ocelet.lang.ocelet.Paradesc
import fr.ocelet.lang.ocelet.Paramdefa
import fr.ocelet.lang.ocelet.Paramunit
import fr.ocelet.lang.ocelet.Paraopt
import fr.ocelet.lang.ocelet.PropertyDef
import fr.ocelet.lang.ocelet.Rangevals
import fr.ocelet.lang.ocelet.Scenario
import fr.ocelet.lang.ocelet.ServiceDef
import java.util.List
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.common.types.util.Primitives
import org.eclipse.xtext.common.types.util.TypeReferences
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.eclipse.xtext.xbase.jvmmodel.AbstractModelInferrer
import org.eclipse.xtext.xbase.jvmmodel.IJvmDeclaredTypeAcceptor
import org.eclipse.xtext.xbase.jvmmodel.JvmTypesBuilder
import java.util.HashMap

class OceletJvmModelInferrer extends AbstractModelInferrer {

	@Inject extension JvmTypesBuilder

    // Used in our case to deal with imports in the generated code
//    @Inject extension TypeReferenceSerializer
    @Inject extension IQualifiedNameProvider
//    @Inject TypeReferences typeReferences
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

                  // InputDatafacer functions
                  if (Class::forName('fr.ocelet.datafacer.InputDatafacer').isAssignableFrom(Class::forName('fr.ocelet.datafacer.ocltypes.'+meln.storetype))) {
                  	val inputRecordType = typeRef('fr.ocelet.datafacer.InputDataRecord')
                  	members += meln.toMethod('readAll'+entname,listype)[
                  	  body=[
                  	  	append('''List<«entname»> _elist = new List<«entname»>();''') newLine
                  	  	append('''for («inputRecordType» _record : this) {''') newLine
                  	  	append('''  _elist.add(create«entname»FromRecord(_record));''')
                  	  	append('''}''') newLine
                  	  	append('''close();''') newLine
                  	  	append('''return _elist;''')
                  	  ]
                  	]
                  	
                  	if (isFirst) {
                  		members += meln.toMethod('readAll',listype)[
                  		  body = [append('''return readAll«entname»();''')]
                  		]
                  	}
                  	
                  	members += meln.toMethod('create'+entname+'FromRecord',entype) [
                  	  parameters += meln.toParameter('_rec', typeRef('fr.ocelet.datafacer.InputDataRecord'))
                  	  body = [
                  	  	append('''«entname» _entity = new «entname»();''') newLine
                  	  	for(mp:matchdef.matchprops) {
                  	  	  val eproptype = propmap.get(mp.prop)
                  	  	  if (eproptype != null) {
                  	  	    if (mp.colname != null) append('''_entity.setProperty("«mp.prop»",read«eproptype»("«mp.colname»"));''')
                  	  	     newLine
                  	  	    }
                  	  	}
                  	  	append('''return _entity;''')
                  	  ]
                  	]
                  	
                  	val hmtype = typeRef('java.util.HashMap',typeRef('java.lang.String'),typeRef('java.lang.String'))
                  	members += meln.toMethod('getMatchdef',hmtype) [
                  		body = [
                  			append('''«hmtype» hm = new «hmtype»();''') newLine
                 	  	    for(mp:matchdef.matchprops) {
                  	  	      val epropftype = propmapf.get(mp.prop)
                  	  	      if (epropftype != null) {
                  	  	        if (mp.colname != null) append('''hm.put("«mp.colname»","«epropftype»");''')
                  	  	        newLine
                  	  	    }
                  	  	  }
                  	  	  append('''return hm;''')
                  		]
                  	  ]
                    }

                 if (Class::forName('fr.ocelet.datafacer.FiltrableDatafacer').isAssignableFrom(Class::forName('fr.ocelet.datafacer.ocltypes.'+meln.storetype))) {
                   members += meln.toMethod('readFiltered'+entname,listype)[
                   parameters += meln.toParameter('_filt', typeRef('java.lang.String'))
                   body = [
                 	append('''setFilter(_filt);''') newLine
                 	append('''return readAll«entname»();''')
                 	]
                   ]
                 }


                 // OutputDatafacer functions
                 if (Class::forName('fr.ocelet.datafacer.OutputDatafacer').isAssignableFrom(Class::forName('fr.ocelet.datafacer.ocltypes.'+meln.storetype))) {
                   members += meln.toMethod('createRecord',typeRef('fr.ocelet.datafacer.OutputDataRecord'))[
                   parameters += meln.toParameter('ety',typeRef('fr.ocelet.runtime.entity.Entity'))
                   body = [
                 	 val odrtype = typeRef('fr.ocelet.datafacer.OutputDataRecord')
                  	 append('''«odrtype» odr = createOutputDataRec();''') newLine
                  	 append('''if (odr != null) {''') newLine
                  	 // Add all the setAttributes
                  	 for(mp:matchdef.matchprops) {
                  	   append('''odr.setAttribute("«mp.colname»",((«entname») ety).get«mp.prop.toFirstUpper»());''') newLine
                  	 }
                  	 append('''}''') newLine
                  	 append('''return odr;''')
                  	]
                  ]
                 }
                  }
                }
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
      		  for(enteln:meln.entelns) {
      		  	switch(enteln) {
      		  		PropertyDef: {
                      if (enteln.name != null) {
      		  			lpropdefs.add(enteln)
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
      	      // Ajout d'un constructeur avec déclaration de toutes les properties
      	      members+= meln.toConstructor[
      	      	body = '''
      	      	  super();
      	      	  «FOR hprop : lpropdefs»
      	      	    «val hhtype = typeRef('fr.ocelet.runtime.entity.Hproperty',asWrapperTypeIfPrimitive(hprop.type))»
      	      	    defProperty("«hprop.name»",new «hhtype»());
      	      	    «val vtyp = asWrapperTypeIfPrimitive(hprop.type)»
      	      	    set«hprop.name.toFirstUpper»(new «vtyp»
      	      	    «IF (vtyp.qualifiedName.equals("java.lang.Integer") ||
                      vtyp.qualifiedName.equals("java.lang.Double") ||
                      vtyp.qualifiedName.equals("java.lang.Float") ||
                      vtyp.qualifiedName.equals("java.lang.Long") ||
                      vtyp.qualifiedName.equals("java.lang.Byte") ||
                      vtyp.qualifiedName.equals("java.lang.Short"))»("0"));
                    «ELSEIF (vtyp.qualifiedName.equals("java.lang.Boolean"))»(false));
                    «ELSE»());
                    «ENDIF»
      	      	  «ENDFOR»
      	      	''']
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
              «genptype» par_«pstuff.getName» = new «implptype»("«pstuff.getName»","«pstuff.getDescription»",«pstuff.getOptionnal»«IF (pstuff.getDvalue == null)»,null«ELSE»,«pstuff.getDvalue»«ENDIF»«IF (pstuff.getMinvalue == null)»,null«ELSE»,«pstuff.getMinvalue»«ENDIF»«IF (pstuff.getMaxvalue == null)»,null«ELSE»,«pstuff.getMaxvalue»«ENDIF»«IF (pstuff.getUnit == null)»,null«ELSE»,«pstuff.getUnit»«ENDIF»);
            «ELSE»
              «val implptype = typeRef('fr.ocelet.runtime.model.ParameterImpl',pstuff.getType)»
              «genptype»par_«pstuff.getName» = new «implptype»("«pstuff.getName»","«pstuff.getDescription»",«pstuff.getOptionnal»«IF (pstuff.getDvalue == null)»,null«ELSE»«IF (pstuff.stringType)»,"«pstuff.getDvalue»"«ELSE»,«pstuff.getDvalue»«ENDIF»
            «ENDIF»«IF (pstuff.getUnit == null)»,null«ELSE»,"«pstuff.getUnit»"«ENDIF»);
            «ENDIF»
            addParameter(par_«pstuff.getName»);
            «IF (pstuff.getDvalue != null)»
            «pstuff.name» = «IF (pstuff.stringType)»«pstuff.getDvalue»";«ELSE»«pstuff.getDvalue»;«ENDIF»
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
              	body = scen.sccode
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
   	  	    } else
              members += scen.toMethod(scen.name,typeRef(Void.TYPE)) [
      		      body = scen.sccode
              ]
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
}

