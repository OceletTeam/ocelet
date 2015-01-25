/**
 */
package fr.ocelet.lang.ocelet;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Match</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link fr.ocelet.lang.ocelet.Match#getMtype <em>Mtype</em>}</li>
 *   <li>{@link fr.ocelet.lang.ocelet.Match#getMatchprops <em>Matchprops</em>}</li>
 * </ul>
 * </p>
 *
 * @see fr.ocelet.lang.ocelet.OceletPackage#getMatch()
 * @model
 * @generated
 */
public interface Match extends EObject
{
  /**
   * Returns the value of the '<em><b>Mtype</b></em>' reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Mtype</em>' reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Mtype</em>' reference.
   * @see #setMtype(Matchtype)
   * @see fr.ocelet.lang.ocelet.OceletPackage#getMatch_Mtype()
   * @model
   * @generated
   */
  Matchtype getMtype();

  /**
   * Sets the value of the '{@link fr.ocelet.lang.ocelet.Match#getMtype <em>Mtype</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Mtype</em>' reference.
   * @see #getMtype()
   * @generated
   */
  void setMtype(Matchtype value);

  /**
   * Returns the value of the '<em><b>Matchprops</b></em>' containment reference list.
   * The list contents are of type {@link fr.ocelet.lang.ocelet.Mdef}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Matchprops</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Matchprops</em>' containment reference list.
   * @see fr.ocelet.lang.ocelet.OceletPackage#getMatch_Matchprops()
   * @model containment="true"
   * @generated
   */
  EList<Mdef> getMatchprops();

} // Match
