/**
 */
package fr.ocelet.lang.ocelet.impl;

import fr.ocelet.lang.ocelet.Comitexpr;
import fr.ocelet.lang.ocelet.OceletPackage;
import fr.ocelet.lang.ocelet.Role;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.xtext.common.types.JvmTypeReference;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Comitexpr</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link fr.ocelet.lang.ocelet.impl.ComitexprImpl#getRol <em>Rol</em>}</li>
 *   <li>{@link fr.ocelet.lang.ocelet.impl.ComitexprImpl#getProp <em>Prop</em>}</li>
 *   <li>{@link fr.ocelet.lang.ocelet.impl.ComitexprImpl#isUsepreval <em>Usepreval</em>}</li>
 *   <li>{@link fr.ocelet.lang.ocelet.impl.ComitexprImpl#getAgrfunc <em>Agrfunc</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ComitexprImpl extends MinimalEObjectImpl.Container implements Comitexpr
{
  /**
   * The cached value of the '{@link #getRol() <em>Rol</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRol()
   * @generated
   * @ordered
   */
  protected Role rol;

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
   * The default value of the '{@link #isUsepreval() <em>Usepreval</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isUsepreval()
   * @generated
   * @ordered
   */
  protected static final boolean USEPREVAL_EDEFAULT = false;

  /**
   * The cached value of the '{@link #isUsepreval() <em>Usepreval</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isUsepreval()
   * @generated
   * @ordered
   */
  protected boolean usepreval = USEPREVAL_EDEFAULT;

  /**
   * The cached value of the '{@link #getAgrfunc() <em>Agrfunc</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getAgrfunc()
   * @generated
   * @ordered
   */
  protected JvmTypeReference agrfunc;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ComitexprImpl()
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
    return OceletPackage.Literals.COMITEXPR;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Role getRol()
  {
    if (rol != null && rol.eIsProxy())
    {
      InternalEObject oldRol = (InternalEObject)rol;
      rol = (Role)eResolveProxy(oldRol);
      if (rol != oldRol)
      {
        if (eNotificationRequired())
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, OceletPackage.COMITEXPR__ROL, oldRol, rol));
      }
    }
    return rol;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Role basicGetRol()
  {
    return rol;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setRol(Role newRol)
  {
    Role oldRol = rol;
    rol = newRol;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, OceletPackage.COMITEXPR__ROL, oldRol, rol));
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
      eNotify(new ENotificationImpl(this, Notification.SET, OceletPackage.COMITEXPR__PROP, oldProp, prop));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean isUsepreval()
  {
    return usepreval;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setUsepreval(boolean newUsepreval)
  {
    boolean oldUsepreval = usepreval;
    usepreval = newUsepreval;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, OceletPackage.COMITEXPR__USEPREVAL, oldUsepreval, usepreval));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public JvmTypeReference getAgrfunc()
  {
    return agrfunc;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetAgrfunc(JvmTypeReference newAgrfunc, NotificationChain msgs)
  {
    JvmTypeReference oldAgrfunc = agrfunc;
    agrfunc = newAgrfunc;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, OceletPackage.COMITEXPR__AGRFUNC, oldAgrfunc, newAgrfunc);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setAgrfunc(JvmTypeReference newAgrfunc)
  {
    if (newAgrfunc != agrfunc)
    {
      NotificationChain msgs = null;
      if (agrfunc != null)
        msgs = ((InternalEObject)agrfunc).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - OceletPackage.COMITEXPR__AGRFUNC, null, msgs);
      if (newAgrfunc != null)
        msgs = ((InternalEObject)newAgrfunc).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - OceletPackage.COMITEXPR__AGRFUNC, null, msgs);
      msgs = basicSetAgrfunc(newAgrfunc, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, OceletPackage.COMITEXPR__AGRFUNC, newAgrfunc, newAgrfunc));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs)
  {
    switch (featureID)
    {
      case OceletPackage.COMITEXPR__AGRFUNC:
        return basicSetAgrfunc(null, msgs);
    }
    return super.eInverseRemove(otherEnd, featureID, msgs);
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
      case OceletPackage.COMITEXPR__ROL:
        if (resolve) return getRol();
        return basicGetRol();
      case OceletPackage.COMITEXPR__PROP:
        return getProp();
      case OceletPackage.COMITEXPR__USEPREVAL:
        return isUsepreval();
      case OceletPackage.COMITEXPR__AGRFUNC:
        return getAgrfunc();
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
      case OceletPackage.COMITEXPR__ROL:
        setRol((Role)newValue);
        return;
      case OceletPackage.COMITEXPR__PROP:
        setProp((String)newValue);
        return;
      case OceletPackage.COMITEXPR__USEPREVAL:
        setUsepreval((Boolean)newValue);
        return;
      case OceletPackage.COMITEXPR__AGRFUNC:
        setAgrfunc((JvmTypeReference)newValue);
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
      case OceletPackage.COMITEXPR__ROL:
        setRol((Role)null);
        return;
      case OceletPackage.COMITEXPR__PROP:
        setProp(PROP_EDEFAULT);
        return;
      case OceletPackage.COMITEXPR__USEPREVAL:
        setUsepreval(USEPREVAL_EDEFAULT);
        return;
      case OceletPackage.COMITEXPR__AGRFUNC:
        setAgrfunc((JvmTypeReference)null);
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
      case OceletPackage.COMITEXPR__ROL:
        return rol != null;
      case OceletPackage.COMITEXPR__PROP:
        return PROP_EDEFAULT == null ? prop != null : !PROP_EDEFAULT.equals(prop);
      case OceletPackage.COMITEXPR__USEPREVAL:
        return usepreval != USEPREVAL_EDEFAULT;
      case OceletPackage.COMITEXPR__AGRFUNC:
        return agrfunc != null;
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
    result.append(", usepreval: ");
    result.append(usepreval);
    result.append(')');
    return result.toString();
  }

} //ComitexprImpl
