package fr.ocelet.runtime.model;

/**
 * Parameter definition to be used by the metadata declared
 * in an Ocelet model
 * @author Pascal Degenne - Initial contribution
 *
 * @param <T>
 */
public interface Parameter<T> {

	public String getName();
	public String getDescription();
	public boolean isOptionnal();
	public T getDefaultValue();
	public String getUnit();

}
