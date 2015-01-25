/**
 */
package fr.ocelet.lang.ocelet;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Mdef</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link fr.ocelet.lang.ocelet.Mdef#getProp <em>Prop</em>}</li>
 *   <li>{@link fr.ocelet.lang.ocelet.Mdef#getColname <em>Colname</em>}</li>
 * </ul>
 * </p>
 *
 * @see fr.ocelet.lang.ocelet.OceletPackage#getMdef()
 * @model
 * @generated
 */
public interface Mdef extends EObject
{
  /**
   * Returns the value of the '<em><b>Prop</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Prop</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Prop</em>' attribute.
   * @see #setProp(String)
   * @see fr.ocelet.lang.ocelet.OceletPackage#getMdef_Prop()
   * @model
   * @generated
   */
  String getProp();

  /**
   * Sets the value of the '{@link fr.ocelet.lang.ocelet.Mdef#getProp <em>Prop</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Prop</em>' attribute.
   * @see #getProp()
   * @generated
   */
  void setProp(String value);

  /**
   * Returns the value of the '<em><b>Colname</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Colname</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Colname</em>' attribute.
   * @see #setColname(String)
   * @see fr.ocelet.lang.ocelet.OceletPackage#getMdef_Colname()
   * @model
   * @generated
   */
  String getColname();

  /**
   * Sets the value of the '{@link fr.ocelet.lang.ocelet.Mdef#getColname <em>Colname</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Colname</em>' attribute.
   * @see #getColname()
   * @generated
   */
  void setColname(String value);

} // Mdef
