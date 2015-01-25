package fr.ocelet.runtime.model;

public interface SimulationListener {

	/**
	 * @param progress A value between 0 and 1 indicating the percentage of simulation progress.
	 */
	public void progressEvent(float progress);

	/**
	 * 
	 * @param consoleText A String printed to the console by the model.
	 */
	public void consoleEvent(String consoleText);
		
}
