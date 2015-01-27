package fr.ocelet.lang.jvmmodel

import com.google.inject.Inject
import org.eclipse.xtext.xbase.jvmmodel.AbstractModelInferrer
import org.eclipse.xtext.xbase.jvmmodel.IJvmDeclaredTypeAcceptor
import org.eclipse.xtext.xbase.jvmmodel.JvmTypesBuilder
import org.eclipse.xtext.xbase.compiler.TypeReferenceSerializer;
import fr.ocelet.lang.ocelet.Model
import java.util.List
import fr.ocelet.lang.ocelet.Metadata
import fr.ocelet.lang.ocelet.Paradesc
import fr.ocelet.lang.ocelet.Paramdefa
import fr.ocelet.lang.ocelet.Paramax
import fr.ocelet.lang.ocelet.Paramin
import fr.ocelet.lang.ocelet.Paramunit
import fr.ocelet.lang.ocelet.Paraopt
import org.eclipse.emf.ecore.resource.Resource
import fr.ocelet.lang.ocelet.Scenario

class OceletJvmModelInferrer extends AbstractModelInferrer {

	@Inject extension JvmTypesBuilder

    // Used in our case to deal with imports in the generated code
    @Inject extension TypeReferenceSerializer
    
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
          			  Paramin : {pst.setMin(ppart.parmin)}
          			  Paramax : {pst.setMax(ppart.parmax)}
          			  Paradesc : {pst.setDescription(ppart.pardesc)}
          			  Paraopt : {pst.setOptionnal((ppart.paropt.compareToIgnoreCase("true") == 0))}
          			}
          		}
          	  md.params.add(pst)
          	}
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
      	   body = [
             append('''super("«modlName»");''')
             if (md.getModeldesc != null) {
             	newLine
             	append('''modDescription = "«md.getModeldesc»";''')
             }
             if (md.getWebpage != null) {
             	newLine
             	append('''modelWebPage = "«md.getWebpage»";''')
             }
             if (md.hasParameters) {
             	for(pstuff:md.params) {
             		// TODO Enum type
            	  val genptype = typeRef('fr.ocelet.runtime.model.Parameter',pstuff.getType)
             	  if (pstuff.numericType) {
             	  	val implptype = typeRef('fr.ocelet.runtime.model.NumericParameterImpl',pstuff.getType)
             	  	newLine
             	  	genptype.serialize(genptype,it)
             	  	append(''' par_«pstuff.getName» = new ''')
             	  	implptype.serialize(implptype,it)
               	  	// NumericParameterImpl(String pname, String pdesc, boolean opt,T defval, T minvalue, T maxvalue)
             	  	append('''("«pstuff.getName»","«pstuff.getDescription»",«pstuff.getOptionnal»''')
             	  	if (pstuff.getDvalue == null) append(''',null''') else append(''',«pstuff.getDvalue»''')
             	  	if (pstuff.getMinvalue == null) append(''',null''') else append(''',«pstuff.getMinvalue»''')
             	  	if (pstuff.getMaxvalue == null) append(''',null''') else append(''',«pstuff.getMaxvalue»''')
             	  	if (pstuff.getUnit == null) append(''',null''') else append(''',"«pstuff.getUnit»"''')
                    append(''');''')
             	  }
             	  else {
             	  	val implptype = typeRef('fr.ocelet.runtime.model.ParameterImpl',pstuff.getType)
             	  	newLine
             	  	genptype.serialize(genptype,it)
             	  	append(''' par_«pstuff.getName» = new ''')
             	  	implptype.serialize(implptype,it)
              	  	// String pname, String pdesc, boolean opt, T defval
             	  	append('''("«pstuff.getName»","«pstuff.getDescription»",«pstuff.getOptionnal»''')
             	  	if (pstuff.getDvalue == null) append(''',null''')
             	  	 else {
             	  	 	if (pstuff.stringType) append(''',"«pstuff.getDvalue»"''')
             	  	 	else append(''',«pstuff.getDvalue»''')
             	  	}
             	  	if (pstuff.getUnit == null) append(''',null''') else append(''',"«pstuff.getUnit»"''')                    append(''');''')
             	  }
                 	newLine
                 	append('''addParameter(par_«pstuff.getName»);''')
                    if (pstuff.getDvalue != null) {
                    	newLine
                    	append('''«pstuff.name» = ''')
                    	if (pstuff.stringType) append('''"«pstuff.getDvalue»";''')
             	  	 	else append('''«pstuff.getDvalue»;''')
                    }
             	}
             }
            ]
          ]
                  
          for(scen:scens) {
   	  	    if (scen.name.compareTo(modlName) == 0) {
              members += modl.toMethod("main",typeRef(Void.TYPE)) [
              	 parameters += modl.toParameter('args', typeRef('java.lang.String').addArrayTypeDimension)
              	 setStatic(true)
                 body = [ append('''
                 «modlName» model_«modlName» = new «modlName»();
                 model_«modlName».run_«modlName»();''')]
              ]
              members += modl.toMethod("run_"+modlName,typeRef(Void.TYPE)) [
              	body = scen.sccode
              ]
              members += modl.toMethod("simulate",typeRef(Void.TYPE)) [
              	parameters += modl.toParameter('in_params',typeRef('java.util.HashMap',typeRef('java.lang.String'),typeRef('java.lang.Object')))
              	body = [
              	 if (md.hasParameters) {
             	   for(pstuff:md.params) {
             	   	 append('''«pstuff.getType.getSimpleName» val_«pstuff.getName» = («pstuff.getType.getSimpleName») in_params.get("«pstuff.getName»");''') newLine
              		 append('''if (val_«pstuff.getName» != null) «pstuff.getName» = val_«pstuff.getName»;''') newLine
              		}
              	  }
           		 append('''run_«modlName»();''')
              	]
              ]
              
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

