/**
 */
package fr.ocelet.lang.ocelet.impl;

import fr.ocelet.lang.ocelet.Mdef;
import fr.ocelet.lang.ocelet.OceletPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Mdef</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link fr.ocelet.lang.ocelet.impl.MdefImpl#getProp <em>Prop</em>}</li>
 *   <li>{@link fr.ocelet.lang.ocelet.impl.MdefImpl#getColname <em>Colname</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MdefImpl extends MinimalEObjectImpl.Container implements Mdef
{
  /**
   * The default value of the '{@link #getProp() <em>Prop</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getProp()
   * @generated
   * @ordered
   */
  protected static final String PROP_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getProp() <em>Prop</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getProp()
   * @generated
   * @ordered
   */
  protected String prop = PROP_EDEFAULT;

  /**
   * The default value of the '{@link #getColname() <em>Colname</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getColname()
   * @generated
   * @ordered
   */
  protected static final String COLNAME_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getColname() <em>Colname</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getColname()
   * @generated
   * @ordered
   */
  protected String colname = COLNAME_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected MdefImpl()
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
    return OceletPackage.Literals.MDEF;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getProp()
  {
    return prop;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setProp(String newProp)
  {
    String oldProp = prop;
    prop = newProp;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, OceletPackage.MDEF__PROP, oldProp, prop));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getColname()
  {
    return colname;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setColname(String newColname)
  {
    String oldColname = colname;
    colname = newColname;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, OceletPackage.MDEF__COLNAME, oldColname, colname));
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
      case OceletPackage.MDEF__PROP:
        return getProp();
      case OceletPackage.MDEF__COLNAME:
        return getColname();
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
      case OceletPackage.MDEF__PROP:
        setProp((String)newValue);
        return;
      case OceletPackage.MDEF__COLNAME:
        setColname((String)newValue);
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
      case OceletPackage.MDEF__PROP:
        setProp(PROP_EDEFAULT);
        return;
      case OceletPackage.MDEF__COLNAME:
        setColname(COLNAME_EDEFAULT);
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
      case OceletPackage.MDEF__PROP:
        return PROP_EDEFAULT == null ? prop != null : !PROP_EDEFAULT.equals(prop);
      case OceletPackage.MDEF__COLNAME:
        return COLNAME_EDEFAULT == null ? colname != null : !COLNAME_EDEFAULT.equals(colname);
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
    result.append(" (prop: ");
    result.append(prop);
    result.append(", colname: ");
    result.append(colname);
    result.append(')');
    return result.toString();
  }

} //MdefImpl
