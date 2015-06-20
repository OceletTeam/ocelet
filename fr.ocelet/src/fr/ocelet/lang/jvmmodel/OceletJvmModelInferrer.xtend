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
import org.eclipse.xtext.common.types.TypesFactory
import org.eclipse.xtext.common.types.util.Primitives
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.eclipse.xtext.xbase.jvmmodel.AbstractModelInferrer
import org.eclipse.xtext.xbase.jvmmodel.IJvmDeclaredTypeAcceptor
import org.eclipse.xtext.xbase.jvmmodel.JvmTypesBuilder

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

                  // InputDatafacer functions
                  if (Class::forName('fr.ocelet.datafacer.InputDatafacer').isAssignableFrom(Class::forName('fr.ocelet.datafacer.ocltypes.'+meln.storetype))) {
                  	val inputRecordType = typeRef('fr.ocelet.datafacer.InputDataRecord')
                  	members += meln.toMethod('readAll'+entname,listype)[
                  	  body='''
                  	    List<«entname»> _elist = new List<«entname»>();
                  	    for («inputRecordType» _record : this) {
                  	      _elist.add(create«entname»FromRecord(_record));
                  	     }
                  	    close();
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
      	      	    set«hprop.name.toFirstUpper»(new «vtyp»«IF (vtyp.qualifiedName.equals("java.lang.Integer") ||
                      vtyp.qualifiedName.equals("java.lang.Double") ||
                      vtyp.qualifiedName.equals("java.lang.Float") ||
                      vtyp.qualifiedName.equals("java.lang.Long") ||
                      vtyp.qualifiedName.equals("java.lang.Byte") ||
                      vtyp.qualifiedName.equals("java.lang.Short"))»("0"));«ELSEIF (vtyp.qualifiedName.equals("java.lang.Boolean"))»(false));«ELSE»());
                    «ENDIF»
      	      	  «ENDFOR»
      	      	'''
      	       ]
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
            if ((meln.roles.size >= 2) &&
            	(meln.roles.get(0)!=null) && (meln.roles.get(1)!=null) &&
            	(meln.roles.get(0).type != null) && (meln.roles.get(1).type != null) &&
               	(meln.roles.get(0).type.fullyQualifiedName != null) &&
               	(meln.roles.get(1).type.fullyQualifiedName != null) &&
            	(meln.roles.get(0).name != null) && (meln.roles.get(1).name != null)
                ) {

            if (meln.roles.size > 2) println("Sorry, only graphs with two roles are supported by this version. The two first roles will be used and the others will be ignored.")

            val isAutoGraph = (meln.roles.get(0).type.equals(meln.roles.get(1).type))
            val graphTypeName = if(isAutoGraph) 'fr.ocelet.runtime.relation.impl.AutoGraph'
                                           else 'fr.ocelet.runtime.relation.impl.DiGraph'

            // Generate the edge class
          	acceptor.accept(modl.toClass(edgecname))[
          	  superTypes += typeRef('fr.ocelet.runtime.relation.OcltEdge')
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
          	  	  	]
          	  	  }
           	  	}
          	 }
          	
          	// -- Generate the interaction graph class --
          	if	(typeRef(edgecname) != null) {
          	acceptor.accept(modl.toClass(graphcname)) [
      		  documentation = meln.documentation
              val firstRole = meln.roles.get(0)
              val secondRole = meln.roles.get(1)
              val firstRoleType = typeRef(firstRole.type.fullyQualifiedName.toString)
              val secondRoleType = typeRef(secondRole.type.fullyQualifiedName.toString)
              val rolset1 = meln.roles.get(0).name+"Set"
              val rolset2 = meln.roles.get(1).name+"Set"
                  		  
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
              
              // TODO : Generaliser ce système en utilisant des noms de methode générable : getSet1 get Set2 etc.
              //        ce qui permet d'integrer cette génération de code dans la boucle des roles
              //        et d'être davantage générique vis à vis du nombre de roles
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
   		  	   }
   		     }
     		  
     		  // Generate Properties, Interactions and Filters code on the graph level
    		  for (reln:meln.relelns){
    		  	switch(reln) {
          	  	  RelPropertyDef : {
                    members+=reln.toMethod("set"+reln.name.toFirstUpper,typeRef(Void::TYPE))[
                      parameters+= reln.toParameter(reln.name,reln.type)	
                      body='''
                      	«val typ_edgecname = typeRef(edgecname)»
                      	beginTransaction();
                      	for(«typ_edgecname» _edg_ : this)
                      	  _edg_.set«reln.name.toFirstUpper»(«reln.name»);
                      	endTransaction();
                      '''
                    ]
          	  	  }
          	  	  InteractionDef : {
                    members+=reln.toMethod(reln.name,typeRef(Void::TYPE))[
          	  	  	  for(p:reln.params){
          	  	  	  	parameters += reln.toParameter(p.name,p.parameterType)
          	  	  	  }
                      body='''
                      	«val typ_edgecname = typeRef(edgecname)»
                      	beginTransaction();
                      	«var ci=0»
                      	for(«typ_edgecname» _edg_ : this) {
                      	  _edg_.«reln.name»(«FOR p:reln.params»«IF (ci > 0)»,«ENDIF»«p.name»«ci=1»«ENDFOR»);
                      	 «IF (reln.comitexpressions.size() > 0)»
                      	 _edg_._agr_«reln.name»();«ENDIF»
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
                      body ='''
                        «meln.name+"_"+reln.name» _filter = new «meln.name+"_"+reln.name»(«IF (reln.params.size() > 0)»«FOR i:0..(reln.params.size()-1)»«reln.params.get(i).name»«IF i < (reln.params.size()-1)»,«ENDIF»«ENDFOR»«ENDIF»);
                        super.addFilter(_filter);
                        return this;
                      '''
          	  	  	]
          	  	  }
 		        }
  		      }
     		]
     		}
     		} // if series testing uncomplete relation definition
          }
          






                      
       // ---- Structure -----------------------------------
          Strucdef : {
          	acceptor.accept(meln.toClass(meln.fullyQualifiedName))[
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
          	  	  super();
                  «FOR vardef:lvdefs»
                    «var vtyp = vardef.type»
                    «IF !vtyp.primitive»
                    «vardef.name» = new «vtyp»«IF vtyp.qualifiedName.equals("java.lang.Integer")||
                      	  vtyp.qualifiedName.equals("java.lang.Double") ||
                      	  vtyp.qualifiedName.equals("java.lang.Float") ||
                      	  vtyp.qualifiedName.equals("java.lang.Long") ||
                      	  vtyp.qualifiedName.equals("java.lang.Byte") ||
                      	  vtyp.qualifiedName.equals("java.lang.Short")»("0");
                      «ELSEIF (vtyp.qualifiedName.equals("java.lang.Boolean"))»(false);«ENDIF»
                      «ELSE»();
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
}

