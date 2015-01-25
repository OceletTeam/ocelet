package fr.ocelet.runtime.model;

public interface Parameter<T> {

	public String getName();
	public String getDescription();
	public boolean isOptionnal();
	public T getDefaultValue();
	public String getUnit();

}
