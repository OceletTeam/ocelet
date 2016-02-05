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

package fr.ocelet.runtime.model;

import java.util.Collection;
import java.util.HashMap;

/**
 * An interface implemented by Ocelet generated models that will
 * allow to control those models from an external environment.
 * 
 * @author Pascal Degenne, Initial contribution
 */
public interface OceletModel {

	//-------- General model metadata --------
	
	/**
	 * @return The name of this model
	 */
	public String getModelName();
	
	/**
	 * @return A textual description of what this model is about.
	 */
	public String getModelDescription();

	/**
	 * @return The web page Url given by the modeller for this model or null if none was given. 
	 */
	public String getModelWebPage();
	
	/**
	 * Every input parameter declared by the model is described in a parameter class.
	 * @return A collection of parameter description.
	 */
	public Collection<Parameter> getParameterMetadata();
	
	// -------- Simulation control --------
	
	public void simulate(HashMap<String, Object> params);
	
	public void addSimulationListener(SimulationListener sl);
	public void removeSimulationListener(SimulationListener sl);
	
	/**
	 * This method is a way to change the path of the base directory where
	 * the directories data and output should be found.
	 * @param basedir The new base directory
	 */
	public void setBaseDir(String basedir);
	
}
