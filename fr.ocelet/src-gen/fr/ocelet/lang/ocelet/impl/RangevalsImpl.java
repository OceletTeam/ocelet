/**
 */
package fr.ocelet.lang.ocelet.impl;

import fr.ocelet.lang.ocelet.OceletPackage;
import fr.ocelet.lang.ocelet.Rangevals;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Rangevals</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link fr.ocelet.lang.ocelet.impl.RangevalsImpl#getParmin <em>Parmin</em>}</li>
 *   <li>{@link fr.ocelet.lang.ocelet.impl.RangevalsImpl#getParmax <em>Parmax</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class RangevalsImpl extends ParampartImpl implements Rangevals
{
  /**
   * The default value of the '{@link #getParmin() <em>Parmin</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getParmin()
   * @generated
   * @ordered
   */
  protected static final String PARMIN_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getParmin() <em>Parmin</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getParmin()
   * @generated
   * @ordered
   */
  protected String parmin = PARMIN_EDEFAULT;

  /**
   * The default value of the '{@link #getParmax() <em>Parmax</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getParmax()
   * @generated
   * @ordered
   */
  protected static final String PARMAX_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getParmax() <em>Parmax</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getParmax()
   * @generated
   * @ordered
   */
  protected String parmax = PARMAX_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected RangevalsImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EClass eStaticClass()
  {
    return OceletPackage.Literals.RANGEVALS;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getParmin()
  {
    return parmin;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setParmin(String newParmin)
  {
    String oldParmin = parmin;
    parmin = newParmin;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, OceletPackage.RANGEVALS__PARMIN, oldParmin, parmin));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getParmax()
  {
    return parmax;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setParmax(String newParmax)
  {
    String oldParmax = parmax;
    parmax = newParmax;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, OceletPackage.RANGEVALS__PARMAX, oldParmax, parmax));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object eGet(int featureID, boolean resolve, boolean coreType)
  {
    switch (featureID)
    {
      case OceletPackage.RANGEVALS__PARMIN:
        return getParmin();
      case OceletPackage.RANGEVALS__PARMAX:
        return getParmax();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case OceletPackage.RANGEVALS__PARMIN:
        setParmin((String)newValue);
        return;
      case OceletPackage.RANGEVALS__PARMAX:
        setParmax((String)newValue);
        return;
    }
    super.eSet(featureID, newValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eUnset(int featureID)
  {
    switch (featureID)
    {
      case OceletPackage.RANGEVALS__PARMIN:
        setParmin(PARMIN_EDEFAULT);
        return;
      case OceletPackage.RANGEVALS__PARMAX:
        setParmax(PARMAX_EDEFAULT);
        return;
    }
    super.eUnset(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean eIsSet(int featureID)
  {
    switch (featureID)
    {
      case OceletPackage.RANGEVALS__PARMIN:
        return PARMIN_EDEFAULT == null ? parmin != null : !PARMIN_EDEFAULT.equals(parmin);
      case OceletPackage.RANGEVALS__PARMAX:
        return PARMAX_EDEFAULT == null ? parmax != null : !PARMAX_EDEFAULT.equals(parmax);
    }
    return super.eIsSet(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String toString()
  {
    if (eIsProxy()) return super.toString();

    StringBuffer result = new StringBuffer(super.toString());
    result.append(" (parmin: ");
    result.append(parmin);
    result.append(", parmax: ");
    result.append(parmax);
    result.append(')');
    return result.toString();
  }

} //RangevalsImpl