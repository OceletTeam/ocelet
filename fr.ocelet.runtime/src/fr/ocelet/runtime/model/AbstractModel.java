package fr.ocelet.runtime.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import fr.ocelet.runtime.ocltypes.Color;
import fr.ocelet.runtime.ocltypes.KeyMap;
import fr.ocelet.runtime.ocltypes.List;
import fr.ocelet.runtime.styling.Gradient;

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

	public static String getBasedir() {
		return modBasedir;
	}

	public AbstractModel(String modelName) {
		this.modName = modelName;
		modParams = new ArrayList<Parameter>();
		this.simlisteners = new ArrayList<SimulationListener>();
		modBasedir = ".";
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

	public void removeSimulationListener(SimulationListener sl) {
		simlisteners.remove(sl);
	}

	@Override
	public void setBaseDir(String basedir) {
		this.modBasedir = basedir;
	}

	protected void addParameter(Parameter param) {
		modParams.add(param);
	}

	/**
	 * Initiate a series of progress event to be caught by SimulationListeners
	 * 
	 * @param min_value
	 *            The lowest value of the progress bar
	 * @param max_value
	 *            The highest value of the progress bar
	 */
	public void startProgress(Double min_value, Double max_value) {
		minProgress = min_value;
		progressRange = max_value - min_value;
		updateProgress(min_value);
	}

	/**
	 * Initiate a series of progress event to be caught by SimulationListeners
	 * 
	 * @param min_value
	 *            The lowest value of the progress bar
	 * @param max_value
	 *            The highest value of the progress bar
	 */
	public void startProgress(Integer min_value, Integer max_value) {
		minProgress = min_value.doubleValue();
		progressRange = max_value.doubleValue() - min_value.doubleValue();
		updateProgress(minProgress);
	}

	/**
	 * Sends a progress event to simulationListeners
	 * 
	 * @param new_value
	 *            The actual position of the progress.
	 */
	public void updateProgress(Integer new_value) {
		updateProgress(new_value.doubleValue());
	}

	/**
	 * Sends a progress event to simulationListeners
	 * 
	 * @param new_value
	 *            The actual position of the progress.
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
	 * @param nbClasses
	 *            Number of colors
	 * @param gradientName
	 *            Name of the source color gradient
	 * @return A List of initialized colors or null if no gradient matches the
	 *         given name
	 */
	public List<Color> colorRange(int nbClasses, String gradientName) {
		return colorRange(nbClasses, gradientName, false);
	}

	/**
	 * Produces a list of Colors from a Gradient taken backward
	 * 
	 * @param nbClasses
	 *            Number of colors
	 * @param gradientName
	 *            Name of the source color gradient
	 * @return A List of initialized colors or null if no gradient matches the
	 *         given name
	 */
	public List<Color> colorRange(int nbClasses, String gradientName,
			boolean backward) {
		if (gradients == null) {
			gradients = new KeyMap<String, Gradient>();
			try {
				Gradient.readGradients(gradients, "config/gradients.ocg");
			} catch (IOException ioe) {
				System.out
						.println("Warning : failed to read the color gradient definition file (config/gradient.ocg");
				System.out.println("System message: " + ioe.getMessage());
			}
		}
		List<Color> lc = null;
		Gradient gd = gradients.get(gradientName);
		if (gd != null)
			lc = backward ? gd.toReversedColorList(nbClasses) : gd
					.toColorList(nbClasses);
		else
			System.out
					.println("Warning : could not find the color gradient named "
							+ gradientName + ".");
		return lc;
	}

}
