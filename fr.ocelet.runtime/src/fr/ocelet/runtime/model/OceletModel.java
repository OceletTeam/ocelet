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
