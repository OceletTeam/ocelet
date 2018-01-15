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
package fr.ocelet.lang.validation

import fr.ocelet.lang.ocelet.Agregdef
import fr.ocelet.lang.ocelet.Comitexpr
import fr.ocelet.lang.ocelet.Datafacer
import fr.ocelet.lang.ocelet.Entity
import fr.ocelet.lang.ocelet.Match
import fr.ocelet.lang.ocelet.Mdef
import fr.ocelet.lang.ocelet.Model
import fr.ocelet.lang.ocelet.OceletPackage
import fr.ocelet.lang.ocelet.Paramdefa
import fr.ocelet.lang.ocelet.Parameter
import fr.ocelet.lang.ocelet.PropertyDef
import fr.ocelet.lang.ocelet.Rangevals
import fr.ocelet.lang.ocelet.RelPropertyDef
import fr.ocelet.lang.ocelet.Relation
import fr.ocelet.lang.ocelet.Scenario
import fr.ocelet.lang.ocelet.StrucVarDef
import fr.ocelet.lang.ocelet.Strucdef
import java.util.ArrayList
import org.eclipse.xtext.validation.Check

/**
 * Custom validation rules of the editor
 * 
 * @author Pascal Degenne - Initial contribution
 */
class OceletValidator extends AbstractOceletValidator {

@Check
def checkDuplicateEntity (Entity ent) {
  val meln = ent.eContainer as Model
  var ecount = 0
  for (eln:meln.modelns) {
  	switch (eln) {
  	  Entity : {
  	  	if (eln.name.compareTo(ent.name) == 0) ecount++
  	  }
  	}
  }
  if (ecount > 1) error("An entity named "+ent.name+" already exists in this model.",OceletPackage.Literals.ENTITY__NAME)
}

@Check
def checkDuplicateDatafacer (Datafacer daf) {
  val meln = daf.eContainer as Model
  var ecount = 0
  for (eln:meln.modelns) {
  	switch (eln) {
  	  Datafacer : {
  	  	if (eln.name.compareTo(daf.name) == 0) ecount++
  	  }
  	}
  }
  if (ecount > 1) error("A datafacer named "+daf.name+" already exists in this model.",OceletPackage.Literals.DATAFACER__NAME)
}

@Check
def checkDuplicateRelation (Relation rel) {
  val meln = rel.eContainer as Model
  var ecount = 0
  for (eln:meln.modelns) {
  	switch (eln) {
  	  Relation : {
  	  	if (eln.name.compareTo(rel.name) == 0) ecount++
  	  }
  	}
  }
  if (ecount > 1) error("A relation named "+rel.name+" already exists in this model.",OceletPackage.Literals.RELATION__NAME)
}

@Check
def checkDuplicateStructure (Strucdef stru) {
  val meln = stru.eContainer as Model
  var ecount = 0
  for (eln:meln.modelns) {
  	switch (eln) {
  	  Strucdef : {
  	  	if (eln.name.compareTo(stru.name) == 0) ecount++
  	  }
  	}
  }
  if (ecount > 1) error("A structure named "+stru.name+" already exists in this model.",OceletPackage.Literals.STRUCDEF__NAME)
}

@Check
def checkDuplicateAgreg (Agregdef agr) {
  val meln = agr.eContainer as Model
  var ecount = 0
  for (eln:meln.modelns) {
  	switch (eln) {
  	  Agregdef : {
  	  	if (eln.name.compareTo(agr.name) == 0) ecount++
  	  }
  	}
  }
  if (ecount > 1) error("An aggregation function named "+agr.name+" already exists in this model.",OceletPackage.Literals.AGREGDEF__NAME)
}

@Check
def checkDuplicateScen (Scenario scn) {
  val meln = scn.eContainer as Model
  var ecount = 0
  for (eln:meln.modelns) {
  	switch (eln) {
  	  Scenario : {
  	  	if (eln.name.compareTo(scn.name) == 0) ecount++
  	  }
  	}
  }
  if (ecount > 1) error("A scenario named "+scn.name+" already exists in this model.",OceletPackage.Literals.SCENARIO__NAME)
}


/**
 * Agg : does the given property exist ?
 */
@Check
def checkAggProperties(Comitexpr ce) {
  val prop = ce.getProp
  var res = false
  for (eln:ce.getRol.getType.entelns)  if (eln.name.compareTo(prop) == 0) res = true
  if (!res) error('Unkown property '+prop,OceletPackage.Literals.COMITEXPR__PROP)
}

// List of datafacer types. See checkDatafacerType()
val static ldn = new ArrayList<String>

/**
 * Make sure the given datafacer type does exist
 */
@Check
def checkDatafacerType(Datafacer df) {
  if (ldn.size == 0) {
    ldn.add("Csvfile")
    ldn.add("KmlExport")
    ldn.add("Postgis")
    ldn.add("RasterFile")
    ldn.add("Shapefile")
    ldn.add("TemporalSeriesFile")
  }
  if (!ldn.contains(df.storetype)) {
  	error("Unknown datafacer type.",OceletPackage.Literals.DATAFACER__STORETYPE)
  }
}

/**
 * Property names must not begin with an uppercase letter
 */
 @Check
 def checkPropertyNameLowercase(PropertyDef pdef){
 	if (Character.isUpperCase((pdef.name.charAt(0))))
 	  error('The name of a property must not begin with an upper case character. Thank you.',OceletPackage.Literals.ENTITY_ELEMENTS__NAME)
 }

/**
 * Property names must not begin with an uppercase letter
 */
 @Check
 def checkRelPropertyNameLowercase(RelPropertyDef rpdef){
 	if (Character.isUpperCase((rpdef.name.charAt(0))))
 	  error('The name of a property must not begin with an upper case character. Thank you.',OceletPackage.Literals.REL_ELEMENTS__NAME)
 }

/**
 * Structure variable  names must not begin with an uppercase letter
 */
 @Check
 def checkStrucVarNameLowercase(StrucVarDef svd){
 	if (Character.isUpperCase((svd.name.charAt(0))))
 	  error('The variable name of a structure must not begin with an upper case character. Thank you.',OceletPackage.Literals.STRUC_ELN__NAME)
 }


 /**
  * Makes sure that every datafacer match definition is defined in the
  * corresponding entity (or structure)
  */
 @Check
 def checkMatchElements(Mdef mdef){
   val match = mdef.eContainer as Match
   val mt = match.mtype
   var propfound = false
   switch(mt) {
     Entity: {
       for (eln:mt.entelns)
       switch(eln) {
         PropertyDef: {
           if (eln.name.compareTo(mdef.prop) == 0) propfound=true
         }
       }
       if (!propfound) error('The entity '+mt.name+' has no property named '+mdef.prop,OceletPackage.Literals.MDEF__PROP)
     }
     Strucdef: {
       for (eln:mt.strucelns)
       switch(eln){
         StrucVarDef: {
            if (eln.name.compareTo(mdef.prop) == 0) propfound=true
         }
       }
       if (!propfound) error('The structure '+mt.name+' has no field named '+mdef.prop,OceletPackage.Literals.MDEF__PROP)
     }
   }
}


	@Check
	def checkParamDefaultWithinRange(Parameter parameter) {
		if (parameter.paramparts != null) {
			var sdef = ""
			var rgmin = 0.0
			var rgmax = 0.0
			var rangeDef = false
			for (pp : parameter.paramparts) {
				switch (pp) {
					Paramdefa: {
						sdef = pp.pardefa
					}
					Rangevals: {
						try {
						  rgmin = new Double(pp.parmin)
						  rgmax = new Double(pp.parmax)
						  rangeDef = true
						} catch (NumberFormatException nfe) {
							warning('Parameter range values must be valid numeric values',OceletPackage.Literals.PARAMETER__NAME)
						}
					}
				}
			}
			if (rangeDef) {
				try {
					val ddef = new Double(sdef)
					if ((ddef < rgmin) || (ddef > rgmax))
						warning('The default value of parameter ' + parameter.name + ' is not within the defined range',
							OceletPackage.Literals.PARAMETER__NAME)
				} catch (NumberFormatException e) {
					warning(
						'The parameter Range can only be used for numeric values and ' + sdef +
							' is not a valid numeric value.', OceletPackage.Literals.PARAMETER__NAME)
				}

			}
		}
	}
}

