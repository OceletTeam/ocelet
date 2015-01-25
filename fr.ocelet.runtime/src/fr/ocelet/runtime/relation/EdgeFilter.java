package fr.ocelet.runtime.relation;

/**
 * Generic edge filter definition
 * @author Pascal Degenne - Initial contribution
 *
 */
public interface EdgeFilter<Eg extends OcltRole, Ed extends OcltRole> {

	public Boolean filter(Eg left, Ed right);
	
}
