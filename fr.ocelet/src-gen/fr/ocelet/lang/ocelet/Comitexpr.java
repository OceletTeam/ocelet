/**
 */
package fr.ocelet.lang.ocelet;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.xtext.common.types.JvmTypeReference;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Comitexpr</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link fr.ocelet.lang.ocelet.Comitexpr#getRol <em>Rol</em>}</li>
 *   <li>{@link fr.ocelet.lang.ocelet.Comitexpr#getProp <em>Prop</em>}</li>
 *   <li>{@link fr.ocelet.lang.ocelet.Comitexpr#isUsepreval <em>Usepreval</em>}</li>
 *   <li>{@link fr.ocelet.lang.ocelet.Comitexpr#getAgrfunc <em>Agrfunc</em>}</li>
 * </ul>
 * </p>
 *
 * @see fr.ocelet.lang.ocelet.OceletPackage#getComitexpr()
 * @model
 * @generated
 */
public interface Comitexpr extends EObject
{
  /**
   * Returns the value of the '<em><b>Rol</b></em>' reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Rol</em>' reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Rol</em>' reference.
   * @see #setRol(Role)
   * @see fr.ocelet.lang.ocelet.OceletPackage#getComitexpr_Rol()
   * @model
   * @generated
   */
  Role getRol();

  /**
   * Sets the value of the '{@link fr.ocelet.lang.ocelet.Comitexpr#getRol <em>Rol</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Rol</em>' reference.
   * @see #getRol()
   * @generated
   */
  void setRol(Role value);

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
   * @see fr.ocelet.lang.ocelet.OceletPackage#getComitexpr_Prop()
   * @model
   * @generated
   */
  String getProp();

  /**
   * Sets the value of the '{@link fr.ocelet.lang.ocelet.Comitexpr#getProp <em>Prop</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Prop</em>' attribute.
   * @see #getProp()
   * @generated
   */
  void setProp(String value);

  /**
   * Returns the value of the '<em><b>Usepreval</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Usepreval</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Usepreval</em>' attribute.
   * @see #setUsepreval(boolean)
   * @see fr.ocelet.lang.ocelet.OceletPackage#getComitexpr_Usepreval()
   * @model
   * @generated
   */
  boolean isUsepreval();

  /**
   * Sets the value of the '{@link fr.ocelet.lang.ocelet.Comitexpr#isUsepreval <em>Usepreval</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Usepreval</em>' attribute.
   * @see #isUsepreval()
   * @generated
   */
  void setUsepreval(boolean value);

  /**
   * Returns the value of the '<em><b>Agrfunc</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Agrfunc</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Agrfunc</em>' containment reference.
   * @see #setAgrfunc(JvmTypeReference)
   * @see fr.ocelet.lang.ocelet.OceletPackage#getComitexpr_Agrfunc()
   * @model containment="true"
   * @generated
   */
  JvmTypeReference getAgrfunc();

  /**
   * Sets the value of the '{@link fr.ocelet.lang.ocelet.Comitexpr#getAgrfunc <em>Agrfunc</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Agrfunc</em>' containment reference.
   * @see #getAgrfunc()
   * @generated
   */
  void setAgrfunc(JvmTypeReference value);

} // Comitexpr
