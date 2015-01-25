package fr.ocelet.datafacer;

/**
 * Some datafacers can implement this interface if they accept
 * any kind of WHERE clause filter String.
 * Typically any SQL related datafacer should apply.
 * 
 * The datafacer implementing this interface will have an extra
 * generated method : readFilteredXxxx(String filter) {..}
 * 
 * @author Pascal Degenne, Initial contribution
 */

public interface FiltrableDatafacer {
  public void setFilter(String cqlFilter);
}
