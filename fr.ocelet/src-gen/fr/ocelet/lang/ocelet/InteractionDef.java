/**
 */
package fr.ocelet.lang.ocelet;

import org.eclipse.emf.common.util.EList;

import org.eclipse.xtext.common.types.JvmFormalParameter;

import org.eclipse.xtext.xbase.XExpression;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Interaction Def</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link fr.ocelet.lang.ocelet.InteractionDef#getParams <em>Params</em>}</li>
 *   <li>{@link fr.ocelet.lang.ocelet.InteractionDef#getBody <em>Body</em>}</li>
 *   <li>{@link fr.ocelet.lang.ocelet.InteractionDef#getComitexpressions <em>Comitexpressions</em>}</li>
 * </ul>
 * </p>
 *
 * @see fr.ocelet.lang.ocelet.OceletPackage#getInteractionDef()
 * @model
 * @generated
 */
public interface InteractionDef extends RelElements
{
  /**
   * Returns the value of the '<em><b>Params</b></em>' containment reference list.
   * The list contents are of type {@link org.eclipse.xtext.common.types.JvmFormalParameter}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Params</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Params</em>' containment reference list.
   * @see fr.ocelet.lang.ocelet.OceletPackage#getInteractionDef_Params()
   * @model containment="true"
   * @generated
   */
  EList<JvmFormalParameter> getParams();

  /**
   * Returns the value of the '<em><b>Body</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Body</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Body</em>' containment reference.
   * @see #setBody(XExpression)
   * @see fr.ocelet.lang.ocelet.OceletPackage#getInteractionDef_Body()
   * @model containment="true"
   * @generated
   */
  XExpression getBody();

  /**
   * Sets the value of the '{@link fr.ocelet.lang.ocelet.InteractionDef#getBody <em>Body</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Body</em>' containment reference.
   * @see #getBody()
   * @generated
   */
  void setBody(XExpression value);

  /**
   * Returns the value of the '<em><b>Comitexpressions</b></em>' containment reference list.
   * The list contents are of type {@link fr.ocelet.lang.ocelet.Comitexpr}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Comitexpressions</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Comitexpressions</em>' containment reference list.
   * @see fr.ocelet.lang.ocelet.OceletPackage#getInteractionDef_Comitexpressions()
   * @model containment="true"
   * @generated
   */
  EList<Comitexpr> getComitexpressions();

} // InteractionDef
