/*
*  Ocelet spatial modelling language.   www.ocelet.org
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

package fr.ocelet.runtime.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.logging.Handler;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.geotools.factory.Hints;
import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

import fr.ocelet.runtime.geom.SpatialManager;
import fr.ocelet.runtime.ocltypes.Color;
import fr.ocelet.runtime.ocltypes.KeyMap;
import fr.ocelet.runtime.ocltypes.List;
import fr.ocelet.runtime.styling.Gradient;

/**
 * Defines everything that a good Ocelet model should have !
 * 
 * @author Pascal Degenne - Initial contribution
 */
public abstract class AbstractModel implements OceletModel {

	protected String modName;
	protected String modDescription;
	protected java.util.List<Parameter> modParams;
	protected ArrayList<SimulationListener> simlisteners;
	protected static String modBasedir;
	protected String modelWebPage;
	protected Double minProgress;
	protected Double progressRange;
	protected KeyMap<String, Gradient> gradients;
	protected String[] model_args; // args from a 'java -jar model.jar ...' call from a cmd line

	public static String getBasedir() {
		return modBasedir;
	}

	public AbstractModel(String modelName) {
		this.modName = modelName;
		modParams = new ArrayList<Parameter>();
		this.simlisteners = new ArrayList<SimulationListener>();
		modBasedir = ".";
		Logger globalLogger = Logger.getLogger("global");
		Handler[] handlers = globalLogger.getHandlers();
		for (Handler handler : handlers) {
			globalLogger.removeHandler(handler);
		}
		LogManager.getLogManager().reset();
		System.setProperty("com.sun.media.jai.disableMediaLib", "true");
	}

	@Override
	public String getModelName() {
		return modName;
	}

	@Override
	public String getModelDescription() {
		return modDescription;
	}

	@Override
	public Collection<Parameter> getParameterMetadata() {
		return modParams;
	}

	@Override
	public abstract void simulate(HashMap<String, Object> params);

	@Override
	public void addSimulationListener(SimulationListener sl) {
		simlisteners.add(sl);
	}

	public HashMap<String, Object> parseParams(String[] args) {
		HashMap<String, Object> pmap = new HashMap<String, Object>();
		for (int i = 0; i < args.length; i++) {
			Parameter p = modParams.get(i);
			Object dv = p.getDefaultValue();
			String ptype = "an Object";
			try {
				if (dv instanceof Integer) {
					ptype = "an Integer";
					Integer argval = new Integer(args[i]);
					pmap.put(p.getName(), argval);
				} else if (dv instanceof Double) {
					ptype = "a Double";
					Double argval = new Double(args[i]);
					pmap.put(p.getName(), argval);
				} else if (dv instanceof String) {
					pmap.put(p.getName(), args[i]);
				} else if (dv instanceof Boolean) {
					ptype = "a Boolean";
					Boolean argval = new Boolean(args[i]);
					pmap.put(p.getName(), argval);
				} else if (dv instanceof Float) {
					ptype = "a Float";
					Float argval = new Float(args[i]);
					pmap.put(p.getName(), argval);
				} else if (dv instanceof Long) {
					ptype = "a Long";
					Long argval = new Long(args[i]);
					pmap.put(p.getName(), argval);
				} else if (dv instanceof Short) {
					ptype = "a Short";
					Short argval = new Short(args[i]);
					pmap.put(p.getName(), argval);
				} else if (dv instanceof Byte) {
					ptype = "a Byte";
					Byte argval = new Byte(args[i]);
					pmap.put(p.getName(), argval);
				}
			} catch (NumberFormatException fne) {
				System.err.println("Warning: could not convert the argument \"" + args[i] + "\" into " + ptype
						+ " value for the parameter " + p.getName() + ". The default value will be used instead.");
			}
		}
		return pmap;
	}

	public void removeSimulationListener(SimulationListener sl) {
		simlisteners.remove(sl);
	}

	@Override
	public void setBaseDir(String basedir) {
		this.modBasedir = basedir;
	}

	/**
	 * @since 2.1.0
     * When one launch a simulation using a jar version of an Ocelet
     * model, it is possible to provide a series of arguments on the
     * command line such as :   java -jar mymodel.jar arg1 arg2 arg3
     * This function returns the list of arguments read from the
     * command line in the form of a list of String. From this example,
     * the result would be a List ("arg1","arg2","arg3").
	 * @return The command line arguments in a List of String
	 */
	public List<String> getCmdLineArgs() {
		List<String> la = new List<String>();
        for(String arg : model_args) {
          la.add(arg);
        }
		return la;
	}

	protected void setCmdLineArgs(String[] args) {
		model_args = args;
	}

	protected void addParameter(Parameter param) {
		modParams.add(param);
	}

	/**
	 * Initiate a series of progress event to be caught by SimulationListeners
	 * 
	 * @param min_value The lowest value of the progress bar
	 * @param max_value The highest value of the progress bar
	 */
	public void startProgress(Double min_value, Double max_value) {
		minProgress = min_value;
		progressRange = max_value - min_value;
		updateProgress(min_value);
	}

	/**
	 * Initiate a series of progress event to be caught by SimulationListeners
	 * 
	 * @param min_value The lowest value of the progress bar
	 * @param max_value The highest value of the progress bar
	 */
	public void startProgress(Integer min_value, Integer max_value) {
		minProgress = min_value.doubleValue();
		progressRange = max_value.doubleValue() - min_value.doubleValue();
		updateProgress(minProgress);
	}

	/**
	 * Sends a progress event to simulationListeners
	 * 
	 * @param new_value The actual position of the progress.
	 */
	public void updateProgress(Integer new_value) {
		updateProgress(new_value.doubleValue());
	}

	/**
	 * Sends a progress event to simulationListeners
	 * 
	 * @param new_value The actual position of the progress.
	 */
	public void updateProgress(Double new_value) {
		Double progress = (new_value - minProgress) / progressRange;
		for (SimulationListener sl : simlisteners)
			sl.progressEvent(progress.floatValue());
	}

	@Override
	public String getModelWebPage() {
		return modelWebPage;
	}

	public void setModelWebPage(String mwp) {
		this.modelWebPage = mwp;
	}

	
	/**
	 * Produces a list of Colors from a Gradient
	 * 
	 * @param nbClasses    Number of colors
	 * @param gradientName Name of the source color gradient
	 * @return A List of initialized colors or null if no gradient matches the given
	 *         name
	 */
	public List<Color> colorRange(int nbClasses, String gradientName) {
		return colorRange(nbClasses, gradientName, false);
	}

	/**
	 * Produces a list of Colors from a Gradient taken backward
	 * 
	 * @param nbClasses    Number of colors
	 * @param gradientName Name of the source color gradient
	 * @return A List of initialized colors or null if no gradient matches the given
	 *         name
	 */
	public List<Color> colorRange(int nbClasses, String gradientName, boolean backward) {
		if (gradients == null) {
			gradients = new KeyMap<String, Gradient>();
			try {
				Gradient.readGradients(gradients, "config/gradients.ocg");
			} catch (IOException ioe) {
				System.out.println("Warning : failed to read the color gradient definition file (config/gradient.ocg");
				System.out.println("System message: " + ioe.getMessage());
			}
		}
		List<Color> lc = null;
		Gradient gd = gradients.get(gradientName);
		if (gd != null)
			lc = backward ? gd.toReversedColorList(nbClasses) : gd.toColorList(nbClasses);
		else
			System.out.println("Warning : could not find the color gradient named " + gradientName + ".");
		return lc;
	}

}
