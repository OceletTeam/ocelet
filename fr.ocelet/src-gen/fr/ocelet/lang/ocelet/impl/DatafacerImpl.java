/**
 */
package fr.ocelet.lang.ocelet.impl;

import fr.ocelet.lang.ocelet.Datafacer;
import fr.ocelet.lang.ocelet.Match;
import fr.ocelet.lang.ocelet.OceletPackage;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.xtext.xbase.XExpression;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Datafacer</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link fr.ocelet.lang.ocelet.impl.DatafacerImpl#getName <em>Name</em>}</li>
 *   <li>{@link fr.ocelet.lang.ocelet.impl.DatafacerImpl#getStoretype <em>Storetype</em>}</li>
 *   <li>{@link fr.ocelet.lang.ocelet.impl.DatafacerImpl#getArguments <em>Arguments</em>}</li>
 *   <li>{@link fr.ocelet.lang.ocelet.impl.DatafacerImpl#getMatchbox <em>Matchbox</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DatafacerImpl extends ModElnImpl implements Datafacer
{
  /**
   * The default value of the '{@link #getName() <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getName()
   * @generated
   * @ordered
   */
  protected static final String NAME_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getName()
   * @generated
   * @ordered
   */
  protected String name = NAME_EDEFAULT;

  /**
   * The default value of the '{@link #getStoretype() <em>Storetype</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getStoretype()
   * @generated
   * @ordered
   */
  protected static final String STORETYPE_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getStoretype() <em>Storetype</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getStoretype()
   * @generated
   * @ordered
   */
  protected String storetype = STORETYPE_EDEFAULT;

  /**
   * The cached value of the '{@link #getArguments() <em>Arguments</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getArguments()
   * @generated
   * @ordered
   */
  protected EList<XExpression> arguments;

  /**
   * The cached value of the '{@link #getMatchbox() <em>Matchbox</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMatchbox()
   * @generated
   * @ordered
   */
  protected EList<Match> matchbox;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected DatafacerImpl()
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
    return OceletPackage.Literals.DATAFACER;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getName()
  {
    return name;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setName(String newName)
  {
    String oldName = name;
    name = newName;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, OceletPackage.DATAFACER__NAME, oldName, name));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getStoretype()
  {
    return storetype;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setStoretype(String newStoretype)
  {
    String oldStoretype = storetype;
    storetype = newStoretype;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, OceletPackage.DATAFACER__STORETYPE, oldStoretype, storetype));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<XExpression> getArguments()
  {
    if (arguments == null)
    {
      arguments = new EObjectContainmentEList<XExpression>(XExpression.class, this, OceletPackage.DATAFACER__ARGUMENTS);
    }
    return arguments;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<Match> getMatchbox()
  {
    if (matchbox == null)
    {
      matchbox = new EObjectContainmentEList<Match>(Match.class, this, OceletPackage.DATAFACER__MATCHBOX);
    }
    return matchbox;
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
      case OceletPackage.DATAFACER__ARGUMENTS:
        return ((InternalEList<?>)getArguments()).basicRemove(otherEnd, msgs);
      case OceletPackage.DATAFACER__MATCHBOX:
        return ((InternalEList<?>)getMatchbox()).basicRemove(otherEnd, msgs);
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
      case OceletPackage.DATAFACER__NAME:
        return getName();
      case OceletPackage.DATAFACER__STORETYPE:
        return getStoretype();
      case OceletPackage.DATAFACER__ARGUMENTS:
        return getArguments();
      case OceletPackage.DATAFACER__MATCHBOX:
        return getMatchbox();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @SuppressWarnings("unchecked")
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case OceletPackage.DATAFACER__NAME:
        setName((String)newValue);
        return;
      case OceletPackage.DATAFACER__STORETYPE:
        setStoretype((String)newValue);
        return;
      case OceletPackage.DATAFACER__ARGUMENTS:
        getArguments().clear();
        getArguments().addAll((Collection<? extends XExpression>)newValue);
        return;
      case OceletPackage.DATAFACER__MATCHBOX:
        getMatchbox().clear();
        getMatchbox().addAll((Collection<? extends Match>)newValue);
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
      case OceletPackage.DATAFACER__NAME:
        setName(NAME_EDEFAULT);
        return;
      case OceletPackage.DATAFACER__STORETYPE:
        setStoretype(STORETYPE_EDEFAULT);
        return;
      case OceletPackage.DATAFACER__ARGUMENTS:
        getArguments().clear();
        return;
      case OceletPackage.DATAFACER__MATCHBOX:
        getMatchbox().clear();
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
      case OceletPackage.DATAFACER__NAME:
        return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
      case OceletPackage.DATAFACER__STORETYPE:
        return STORETYPE_EDEFAULT == null ? storetype != null : !STORETYPE_EDEFAULT.equals(storetype);
      case OceletPackage.DATAFACER__ARGUMENTS:
        return arguments != null && !arguments.isEmpty();
      case OceletPackage.DATAFACER__MATCHBOX:
        return matchbox != null && !matchbox.isEmpty();
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
    result.append(" (name: ");
    result.append(name);
    result.append(", storetype: ");
    result.append(storetype);
    result.append(')');
    return result.toString();
  }

} //DatafacerImpl
