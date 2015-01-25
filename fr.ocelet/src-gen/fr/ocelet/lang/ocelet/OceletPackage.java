/**
 */
package fr.ocelet.lang.ocelet;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see fr.ocelet.lang.ocelet.OceletFactory
 * @model kind="package"
 * @generated
 */
public interface OceletPackage extends EPackage
{
  /**
   * The package name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNAME = "ocelet";

  /**
   * The package namespace URI.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_URI = "http://www.ocelet.fr/lang/Ocelet";

  /**
   * The package namespace name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_PREFIX = "ocelet";

  /**
   * The singleton instance of the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  OceletPackage eINSTANCE = fr.ocelet.lang.ocelet.impl.OceletPackageImpl.init();

  /**
   * The meta object id for the '{@link fr.ocelet.lang.ocelet.impl.ModelImpl <em>Model</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see fr.ocelet.lang.ocelet.impl.ModelImpl
   * @see fr.ocelet.lang.ocelet.impl.OceletPackageImpl#getModel()
   * @generated
   */
  int MODEL = 0;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MODEL__NAME = 0;

  /**
   * The feature id for the '<em><b>Modelns</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MODEL__MODELNS = 1;

  /**
   * The number of structural features of the '<em>Model</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MODEL_FEATURE_COUNT = 2;

  /**
   * The meta object id for the '{@link fr.ocelet.lang.ocelet.impl.ModElnImpl <em>Mod Eln</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see fr.ocelet.lang.ocelet.impl.ModElnImpl
   * @see fr.ocelet.lang.ocelet.impl.OceletPackageImpl#getModEln()
   * @generated
   */
  int MOD_ELN = 1;

  /**
   * The number of structural features of the '<em>Mod Eln</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MOD_ELN_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link fr.ocelet.lang.ocelet.impl.MetadataImpl <em>Metadata</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see fr.ocelet.lang.ocelet.impl.MetadataImpl
   * @see fr.ocelet.lang.ocelet.impl.OceletPackageImpl#getMetadata()
   * @generated
   */
  int METADATA = 2;

  /**
   * The feature id for the '<em><b>Desc</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int METADATA__DESC = MOD_ELN_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Webp</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int METADATA__WEBP = MOD_ELN_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Paramdefs</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int METADATA__PARAMDEFS = MOD_ELN_FEATURE_COUNT + 2;

  /**
   * The number of structural features of the '<em>Metadata</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int METADATA_FEATURE_COUNT = MOD_ELN_FEATURE_COUNT + 3;

  /**
   * The meta object id for the '{@link fr.ocelet.lang.ocelet.impl.ParameterImpl <em>Parameter</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see fr.ocelet.lang.ocelet.impl.ParameterImpl
   * @see fr.ocelet.lang.ocelet.impl.OceletPackageImpl#getParameter()
   * @generated
   */
  int PARAMETER = 3;

  /**
   * The feature id for the '<em><b>Ptype</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PARAMETER__PTYPE = 0;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PARAMETER__NAME = 1;

  /**
   * The feature id for the '<em><b>Paramparts</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PARAMETER__PARAMPARTS = 2;

  /**
   * The number of structural features of the '<em>Parameter</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PARAMETER_FEATURE_COUNT = 3;

  /**
   * The meta object id for the '{@link fr.ocelet.lang.ocelet.impl.ParampartImpl <em>Parampart</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see fr.ocelet.lang.ocelet.impl.ParampartImpl
   * @see fr.ocelet.lang.ocelet.impl.OceletPackageImpl#getParampart()
   * @generated
   */
  int PARAMPART = 4;

  /**
   * The number of structural features of the '<em>Parampart</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PARAMPART_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link fr.ocelet.lang.ocelet.impl.ParamunitImpl <em>Paramunit</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see fr.ocelet.lang.ocelet.impl.ParamunitImpl
   * @see fr.ocelet.lang.ocelet.impl.OceletPackageImpl#getParamunit()
   * @generated
   */
  int PARAMUNIT = 5;

  /**
   * The feature id for the '<em><b>Parunit</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PARAMUNIT__PARUNIT = PARAMPART_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Paramunit</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PARAMUNIT_FEATURE_COUNT = PARAMPART_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link fr.ocelet.lang.ocelet.impl.ParamdefaImpl <em>Paramdefa</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see fr.ocelet.lang.ocelet.impl.ParamdefaImpl
   * @see fr.ocelet.lang.ocelet.impl.OceletPackageImpl#getParamdefa()
   * @generated
   */
  int PARAMDEFA = 6;

  /**
   * The feature id for the '<em><b>Pardefa</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PARAMDEFA__PARDEFA = PARAMPART_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Paramdefa</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PARAMDEFA_FEATURE_COUNT = PARAMPART_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link fr.ocelet.lang.ocelet.impl.ParaminImpl <em>Paramin</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see fr.ocelet.lang.ocelet.impl.ParaminImpl
   * @see fr.ocelet.lang.ocelet.impl.OceletPackageImpl#getParamin()
   * @generated
   */
  int PARAMIN = 7;

  /**
   * The feature id for the '<em><b>Parmin</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PARAMIN__PARMIN = PARAMPART_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Paramin</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PARAMIN_FEATURE_COUNT = PARAMPART_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link fr.ocelet.lang.ocelet.impl.ParamaxImpl <em>Paramax</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see fr.ocelet.lang.ocelet.impl.ParamaxImpl
   * @see fr.ocelet.lang.ocelet.impl.OceletPackageImpl#getParamax()
   * @generated
   */
  int PARAMAX = 8;

  /**
   * The feature id for the '<em><b>Parmax</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PARAMAX__PARMAX = PARAMPART_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Paramax</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PARAMAX_FEATURE_COUNT = PARAMPART_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link fr.ocelet.lang.ocelet.impl.ParadescImpl <em>Paradesc</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see fr.ocelet.lang.ocelet.impl.ParadescImpl
   * @see fr.ocelet.lang.ocelet.impl.OceletPackageImpl#getParadesc()
   * @generated
   */
  int PARADESC = 9;

  /**
   * The feature id for the '<em><b>Pardesc</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PARADESC__PARDESC = PARAMPART_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Paradesc</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PARADESC_FEATURE_COUNT = PARAMPART_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link fr.ocelet.lang.ocelet.impl.ParaoptImpl <em>Paraopt</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see fr.ocelet.lang.ocelet.impl.ParaoptImpl
   * @see fr.ocelet.lang.ocelet.impl.OceletPackageImpl#getParaopt()
   * @generated
   */
  int PARAOPT = 10;

  /**
   * The feature id for the '<em><b>Paropt</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PARAOPT__PAROPT = PARAMPART_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Paraopt</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PARAOPT_FEATURE_COUNT = PARAMPART_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link fr.ocelet.lang.ocelet.impl.EntityImpl <em>Entity</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see fr.ocelet.lang.ocelet.impl.EntityImpl
   * @see fr.ocelet.lang.ocelet.impl.OceletPackageImpl#getEntity()
   * @generated
   */
  int ENTITY = 11;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ENTITY__NAME = MOD_ELN_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Entelns</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ENTITY__ENTELNS = MOD_ELN_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>Entity</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ENTITY_FEATURE_COUNT = MOD_ELN_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link fr.ocelet.lang.ocelet.impl.EntityElementsImpl <em>Entity Elements</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see fr.ocelet.lang.ocelet.impl.EntityElementsImpl
   * @see fr.ocelet.lang.ocelet.impl.OceletPackageImpl#getEntityElements()
   * @generated
   */
  int ENTITY_ELEMENTS = 12;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ENTITY_ELEMENTS__NAME = 0;

  /**
   * The number of structural features of the '<em>Entity Elements</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ENTITY_ELEMENTS_FEATURE_COUNT = 1;

  /**
   * The meta object id for the '{@link fr.ocelet.lang.ocelet.impl.PropertyDefImpl <em>Property Def</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see fr.ocelet.lang.ocelet.impl.PropertyDefImpl
   * @see fr.ocelet.lang.ocelet.impl.OceletPackageImpl#getPropertyDef()
   * @generated
   */
  int PROPERTY_DEF = 13;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROPERTY_DEF__NAME = ENTITY_ELEMENTS__NAME;

  /**
   * The feature id for the '<em><b>Type</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROPERTY_DEF__TYPE = ENTITY_ELEMENTS_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Property Def</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROPERTY_DEF_FEATURE_COUNT = ENTITY_ELEMENTS_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link fr.ocelet.lang.ocelet.impl.ServiceDefImpl <em>Service Def</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see fr.ocelet.lang.ocelet.impl.ServiceDefImpl
   * @see fr.ocelet.lang.ocelet.impl.OceletPackageImpl#getServiceDef()
   * @generated
   */
  int SERVICE_DEF = 14;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SERVICE_DEF__NAME = ENTITY_ELEMENTS__NAME;

  /**
   * The feature id for the '<em><b>Type</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SERVICE_DEF__TYPE = ENTITY_ELEMENTS_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Params</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SERVICE_DEF__PARAMS = ENTITY_ELEMENTS_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Body</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SERVICE_DEF__BODY = ENTITY_ELEMENTS_FEATURE_COUNT + 2;

  /**
   * The number of structural features of the '<em>Service Def</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SERVICE_DEF_FEATURE_COUNT = ENTITY_ELEMENTS_FEATURE_COUNT + 3;

  /**
   * The meta object id for the '{@link fr.ocelet.lang.ocelet.impl.ConstructorDefImpl <em>Constructor Def</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see fr.ocelet.lang.ocelet.impl.ConstructorDefImpl
   * @see fr.ocelet.lang.ocelet.impl.OceletPackageImpl#getConstructorDef()
   * @generated
   */
  int CONSTRUCTOR_DEF = 15;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CONSTRUCTOR_DEF__NAME = ENTITY_ELEMENTS__NAME;

  /**
   * The feature id for the '<em><b>Params</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CONSTRUCTOR_DEF__PARAMS = ENTITY_ELEMENTS_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Body</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CONSTRUCTOR_DEF__BODY = ENTITY_ELEMENTS_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>Constructor Def</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CONSTRUCTOR_DEF_FEATURE_COUNT = ENTITY_ELEMENTS_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link fr.ocelet.lang.ocelet.impl.RelationImpl <em>Relation</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see fr.ocelet.lang.ocelet.impl.RelationImpl
   * @see fr.ocelet.lang.ocelet.impl.OceletPackageImpl#getRelation()
   * @generated
   */
  int RELATION = 16;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RELATION__NAME = MOD_ELN_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Roles</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RELATION__ROLES = MOD_ELN_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Relelns</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RELATION__RELELNS = MOD_ELN_FEATURE_COUNT + 2;

  /**
   * The number of structural features of the '<em>Relation</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RELATION_FEATURE_COUNT = MOD_ELN_FEATURE_COUNT + 3;

  /**
   * The meta object id for the '{@link fr.ocelet.lang.ocelet.impl.RoleImpl <em>Role</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see fr.ocelet.lang.ocelet.impl.RoleImpl
   * @see fr.ocelet.lang.ocelet.impl.OceletPackageImpl#getRole()
   * @generated
   */
  int ROLE = 17;

  /**
   * The feature id for the '<em><b>Type</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ROLE__TYPE = 0;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ROLE__NAME = 1;

  /**
   * The number of structural features of the '<em>Role</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ROLE_FEATURE_COUNT = 2;

  /**
   * The meta object id for the '{@link fr.ocelet.lang.ocelet.impl.RelElementsImpl <em>Rel Elements</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see fr.ocelet.lang.ocelet.impl.RelElementsImpl
   * @see fr.ocelet.lang.ocelet.impl.OceletPackageImpl#getRelElements()
   * @generated
   */
  int REL_ELEMENTS = 18;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REL_ELEMENTS__NAME = 0;

  /**
   * The number of structural features of the '<em>Rel Elements</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REL_ELEMENTS_FEATURE_COUNT = 1;

  /**
   * The meta object id for the '{@link fr.ocelet.lang.ocelet.impl.RelPropertyDefImpl <em>Rel Property Def</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see fr.ocelet.lang.ocelet.impl.RelPropertyDefImpl
   * @see fr.ocelet.lang.ocelet.impl.OceletPackageImpl#getRelPropertyDef()
   * @generated
   */
  int REL_PROPERTY_DEF = 19;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REL_PROPERTY_DEF__NAME = REL_ELEMENTS__NAME;

  /**
   * The feature id for the '<em><b>Type</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REL_PROPERTY_DEF__TYPE = REL_ELEMENTS_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Rel Property Def</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REL_PROPERTY_DEF_FEATURE_COUNT = REL_ELEMENTS_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link fr.ocelet.lang.ocelet.impl.InteractionDefImpl <em>Interaction Def</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see fr.ocelet.lang.ocelet.impl.InteractionDefImpl
   * @see fr.ocelet.lang.ocelet.impl.OceletPackageImpl#getInteractionDef()
   * @generated
   */
  int INTERACTION_DEF = 20;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INTERACTION_DEF__NAME = REL_ELEMENTS__NAME;

  /**
   * The feature id for the '<em><b>Params</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INTERACTION_DEF__PARAMS = REL_ELEMENTS_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Body</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INTERACTION_DEF__BODY = REL_ELEMENTS_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Comitexpressions</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INTERACTION_DEF__COMITEXPRESSIONS = REL_ELEMENTS_FEATURE_COUNT + 2;

  /**
   * The number of structural features of the '<em>Interaction Def</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INTERACTION_DEF_FEATURE_COUNT = REL_ELEMENTS_FEATURE_COUNT + 3;

  /**
   * The meta object id for the '{@link fr.ocelet.lang.ocelet.impl.ComitexprImpl <em>Comitexpr</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see fr.ocelet.lang.ocelet.impl.ComitexprImpl
   * @see fr.ocelet.lang.ocelet.impl.OceletPackageImpl#getComitexpr()
   * @generated
   */
  int COMITEXPR = 21;

  /**
   * The feature id for the '<em><b>Rol</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int COMITEXPR__ROL = 0;

  /**
   * The feature id for the '<em><b>Prop</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int COMITEXPR__PROP = 1;

  /**
   * The feature id for the '<em><b>Usepreval</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int COMITEXPR__USEPREVAL = 2;

  /**
   * The feature id for the '<em><b>Agrfunc</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int COMITEXPR__AGRFUNC = 3;

  /**
   * The number of structural features of the '<em>Comitexpr</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int COMITEXPR_FEATURE_COUNT = 4;

  /**
   * The meta object id for the '{@link fr.ocelet.lang.ocelet.impl.FilterdefImpl <em>Filterdef</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see fr.ocelet.lang.ocelet.impl.FilterdefImpl
   * @see fr.ocelet.lang.ocelet.impl.OceletPackageImpl#getFilterdef()
   * @generated
   */
  int FILTERDEF = 22;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FILTERDEF__NAME = REL_ELEMENTS__NAME;

  /**
   * The feature id for the '<em><b>Params</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FILTERDEF__PARAMS = REL_ELEMENTS_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Body</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FILTERDEF__BODY = REL_ELEMENTS_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>Filterdef</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FILTERDEF_FEATURE_COUNT = REL_ELEMENTS_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link fr.ocelet.lang.ocelet.impl.StrucdefImpl <em>Strucdef</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see fr.ocelet.lang.ocelet.impl.StrucdefImpl
   * @see fr.ocelet.lang.ocelet.impl.OceletPackageImpl#getStrucdef()
   * @generated
   */
  int STRUCDEF = 23;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STRUCDEF__NAME = MOD_ELN_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Type Argument</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STRUCDEF__TYPE_ARGUMENT = MOD_ELN_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Super Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STRUCDEF__SUPER_TYPE = MOD_ELN_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>Strucelns</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STRUCDEF__STRUCELNS = MOD_ELN_FEATURE_COUNT + 3;

  /**
   * The number of structural features of the '<em>Strucdef</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STRUCDEF_FEATURE_COUNT = MOD_ELN_FEATURE_COUNT + 4;

  /**
   * The meta object id for the '{@link fr.ocelet.lang.ocelet.impl.StrucElnImpl <em>Struc Eln</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see fr.ocelet.lang.ocelet.impl.StrucElnImpl
   * @see fr.ocelet.lang.ocelet.impl.OceletPackageImpl#getStrucEln()
   * @generated
   */
  int STRUC_ELN = 24;

  /**
   * The feature id for the '<em><b>Type</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STRUC_ELN__TYPE = 0;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STRUC_ELN__NAME = 1;

  /**
   * The number of structural features of the '<em>Struc Eln</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STRUC_ELN_FEATURE_COUNT = 2;

  /**
   * The meta object id for the '{@link fr.ocelet.lang.ocelet.impl.StrucVarDefImpl <em>Struc Var Def</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see fr.ocelet.lang.ocelet.impl.StrucVarDefImpl
   * @see fr.ocelet.lang.ocelet.impl.OceletPackageImpl#getStrucVarDef()
   * @generated
   */
  int STRUC_VAR_DEF = 25;

  /**
   * The feature id for the '<em><b>Type</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STRUC_VAR_DEF__TYPE = STRUC_ELN__TYPE;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STRUC_VAR_DEF__NAME = STRUC_ELN__NAME;

  /**
   * The number of structural features of the '<em>Struc Var Def</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STRUC_VAR_DEF_FEATURE_COUNT = STRUC_ELN_FEATURE_COUNT + 0;

  /**
   * The meta object id for the '{@link fr.ocelet.lang.ocelet.impl.StrucFuncDefImpl <em>Struc Func Def</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see fr.ocelet.lang.ocelet.impl.StrucFuncDefImpl
   * @see fr.ocelet.lang.ocelet.impl.OceletPackageImpl#getStrucFuncDef()
   * @generated
   */
  int STRUC_FUNC_DEF = 26;

  /**
   * The feature id for the '<em><b>Type</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STRUC_FUNC_DEF__TYPE = STRUC_ELN__TYPE;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STRUC_FUNC_DEF__NAME = STRUC_ELN__NAME;

  /**
   * The feature id for the '<em><b>Params</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STRUC_FUNC_DEF__PARAMS = STRUC_ELN_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Body</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STRUC_FUNC_DEF__BODY = STRUC_ELN_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>Struc Func Def</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STRUC_FUNC_DEF_FEATURE_COUNT = STRUC_ELN_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link fr.ocelet.lang.ocelet.impl.DatafacerImpl <em>Datafacer</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see fr.ocelet.lang.ocelet.impl.DatafacerImpl
   * @see fr.ocelet.lang.ocelet.impl.OceletPackageImpl#getDatafacer()
   * @generated
   */
  int DATAFACER = 27;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DATAFACER__NAME = MOD_ELN_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Storetype</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DATAFACER__STORETYPE = MOD_ELN_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Arguments</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DATAFACER__ARGUMENTS = MOD_ELN_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>Matchbox</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DATAFACER__MATCHBOX = MOD_ELN_FEATURE_COUNT + 3;

  /**
   * The number of structural features of the '<em>Datafacer</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DATAFACER_FEATURE_COUNT = MOD_ELN_FEATURE_COUNT + 4;

  /**
   * The meta object id for the '{@link fr.ocelet.lang.ocelet.impl.MatchImpl <em>Match</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see fr.ocelet.lang.ocelet.impl.MatchImpl
   * @see fr.ocelet.lang.ocelet.impl.OceletPackageImpl#getMatch()
   * @generated
   */
  int MATCH = 28;

  /**
   * The feature id for the '<em><b>Mtype</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MATCH__MTYPE = 0;

  /**
   * The feature id for the '<em><b>Matchprops</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MATCH__MATCHPROPS = 1;

  /**
   * The number of structural features of the '<em>Match</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MATCH_FEATURE_COUNT = 2;

  /**
   * The meta object id for the '{@link fr.ocelet.lang.ocelet.impl.MatchtypeImpl <em>Matchtype</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see fr.ocelet.lang.ocelet.impl.MatchtypeImpl
   * @see fr.ocelet.lang.ocelet.impl.OceletPackageImpl#getMatchtype()
   * @generated
   */
  int MATCHTYPE = 29;

  /**
   * The number of structural features of the '<em>Matchtype</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MATCHTYPE_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link fr.ocelet.lang.ocelet.impl.MdefImpl <em>Mdef</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see fr.ocelet.lang.ocelet.impl.MdefImpl
   * @see fr.ocelet.lang.ocelet.impl.OceletPackageImpl#getMdef()
   * @generated
   */
  int MDEF = 30;

  /**
   * The feature id for the '<em><b>Prop</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MDEF__PROP = 0;

  /**
   * The feature id for the '<em><b>Colname</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MDEF__COLNAME = 1;

  /**
   * The number of structural features of the '<em>Mdef</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MDEF_FEATURE_COUNT = 2;

  /**
   * The meta object id for the '{@link fr.ocelet.lang.ocelet.impl.AgregdefImpl <em>Agregdef</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see fr.ocelet.lang.ocelet.impl.AgregdefImpl
   * @see fr.ocelet.lang.ocelet.impl.OceletPackageImpl#getAgregdef()
   * @generated
   */
  int AGREGDEF = 31;

  /**
   * The feature id for the '<em><b>Type</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int AGREGDEF__TYPE = MOD_ELN_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int AGREGDEF__NAME = MOD_ELN_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Body</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int AGREGDEF__BODY = MOD_ELN_FEATURE_COUNT + 2;

  /**
   * The number of structural features of the '<em>Agregdef</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int AGREGDEF_FEATURE_COUNT = MOD_ELN_FEATURE_COUNT + 3;

  /**
   * The meta object id for the '{@link fr.ocelet.lang.ocelet.impl.ScenarioImpl <em>Scenario</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see fr.ocelet.lang.ocelet.impl.ScenarioImpl
   * @see fr.ocelet.lang.ocelet.impl.OceletPackageImpl#getScenario()
   * @generated
   */
  int SCENARIO = 32;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SCENARIO__NAME = MOD_ELN_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Sccode</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SCENARIO__SCCODE = MOD_ELN_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>Scenario</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SCENARIO_FEATURE_COUNT = MOD_ELN_FEATURE_COUNT + 2;


  /**
   * Returns the meta object for class '{@link fr.ocelet.lang.ocelet.Model <em>Model</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Model</em>'.
   * @see fr.ocelet.lang.ocelet.Model
   * @generated
   */
  EClass getModel();

  /**
   * Returns the meta object for the attribute '{@link fr.ocelet.lang.ocelet.Model#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see fr.ocelet.lang.ocelet.Model#getName()
   * @see #getModel()
   * @generated
   */
  EAttribute getModel_Name();

  /**
   * Returns the meta object for the containment reference list '{@link fr.ocelet.lang.ocelet.Model#getModelns <em>Modelns</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Modelns</em>'.
   * @see fr.ocelet.lang.ocelet.Model#getModelns()
   * @see #getModel()
   * @generated
   */
  EReference getModel_Modelns();

  /**
   * Returns the meta object for class '{@link fr.ocelet.lang.ocelet.ModEln <em>Mod Eln</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Mod Eln</em>'.
   * @see fr.ocelet.lang.ocelet.ModEln
   * @generated
   */
  EClass getModEln();

  /**
   * Returns the meta object for class '{@link fr.ocelet.lang.ocelet.Metadata <em>Metadata</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Metadata</em>'.
   * @see fr.ocelet.lang.ocelet.Metadata
   * @generated
   */
  EClass getMetadata();

  /**
   * Returns the meta object for the attribute '{@link fr.ocelet.lang.ocelet.Metadata#getDesc <em>Desc</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Desc</em>'.
   * @see fr.ocelet.lang.ocelet.Metadata#getDesc()
   * @see #getMetadata()
   * @generated
   */
  EAttribute getMetadata_Desc();

  /**
   * Returns the meta object for the attribute '{@link fr.ocelet.lang.ocelet.Metadata#getWebp <em>Webp</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Webp</em>'.
   * @see fr.ocelet.lang.ocelet.Metadata#getWebp()
   * @see #getMetadata()
   * @generated
   */
  EAttribute getMetadata_Webp();

  /**
   * Returns the meta object for the containment reference list '{@link fr.ocelet.lang.ocelet.Metadata#getParamdefs <em>Paramdefs</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Paramdefs</em>'.
   * @see fr.ocelet.lang.ocelet.Metadata#getParamdefs()
   * @see #getMetadata()
   * @generated
   */
  EReference getMetadata_Paramdefs();

  /**
   * Returns the meta object for class '{@link fr.ocelet.lang.ocelet.Parameter <em>Parameter</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Parameter</em>'.
   * @see fr.ocelet.lang.ocelet.Parameter
   * @generated
   */
  EClass getParameter();

  /**
   * Returns the meta object for the containment reference '{@link fr.ocelet.lang.ocelet.Parameter#getPtype <em>Ptype</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Ptype</em>'.
   * @see fr.ocelet.lang.ocelet.Parameter#getPtype()
   * @see #getParameter()
   * @generated
   */
  EReference getParameter_Ptype();

  /**
   * Returns the meta object for the attribute '{@link fr.ocelet.lang.ocelet.Parameter#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see fr.ocelet.lang.ocelet.Parameter#getName()
   * @see #getParameter()
   * @generated
   */
  EAttribute getParameter_Name();

  /**
   * Returns the meta object for the containment reference list '{@link fr.ocelet.lang.ocelet.Parameter#getParamparts <em>Paramparts</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Paramparts</em>'.
   * @see fr.ocelet.lang.ocelet.Parameter#getParamparts()
   * @see #getParameter()
   * @generated
   */
  EReference getParameter_Paramparts();

  /**
   * Returns the meta object for class '{@link fr.ocelet.lang.ocelet.Parampart <em>Parampart</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Parampart</em>'.
   * @see fr.ocelet.lang.ocelet.Parampart
   * @generated
   */
  EClass getParampart();

  /**
   * Returns the meta object for class '{@link fr.ocelet.lang.ocelet.Paramunit <em>Paramunit</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Paramunit</em>'.
   * @see fr.ocelet.lang.ocelet.Paramunit
   * @generated
   */
  EClass getParamunit();

  /**
   * Returns the meta object for the attribute '{@link fr.ocelet.lang.ocelet.Paramunit#getParunit <em>Parunit</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Parunit</em>'.
   * @see fr.ocelet.lang.ocelet.Paramunit#getParunit()
   * @see #getParamunit()
   * @generated
   */
  EAttribute getParamunit_Parunit();

  /**
   * Returns the meta object for class '{@link fr.ocelet.lang.ocelet.Paramdefa <em>Paramdefa</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Paramdefa</em>'.
   * @see fr.ocelet.lang.ocelet.Paramdefa
   * @generated
   */
  EClass getParamdefa();

  /**
   * Returns the meta object for the attribute '{@link fr.ocelet.lang.ocelet.Paramdefa#getPardefa <em>Pardefa</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Pardefa</em>'.
   * @see fr.ocelet.lang.ocelet.Paramdefa#getPardefa()
   * @see #getParamdefa()
   * @generated
   */
  EAttribute getParamdefa_Pardefa();

  /**
   * Returns the meta object for class '{@link fr.ocelet.lang.ocelet.Paramin <em>Paramin</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Paramin</em>'.
   * @see fr.ocelet.lang.ocelet.Paramin
   * @generated
   */
  EClass getParamin();

  /**
   * Returns the meta object for the attribute '{@link fr.ocelet.lang.ocelet.Paramin#getParmin <em>Parmin</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Parmin</em>'.
   * @see fr.ocelet.lang.ocelet.Paramin#getParmin()
   * @see #getParamin()
   * @generated
   */
  EAttribute getParamin_Parmin();

  /**
   * Returns the meta object for class '{@link fr.ocelet.lang.ocelet.Paramax <em>Paramax</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Paramax</em>'.
   * @see fr.ocelet.lang.ocelet.Paramax
   * @generated
   */
  EClass getParamax();

  /**
   * Returns the meta object for the attribute '{@link fr.ocelet.lang.ocelet.Paramax#getParmax <em>Parmax</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Parmax</em>'.
   * @see fr.ocelet.lang.ocelet.Paramax#getParmax()
   * @see #getParamax()
   * @generated
   */
  EAttribute getParamax_Parmax();

  /**
   * Returns the meta object for class '{@link fr.ocelet.lang.ocelet.Paradesc <em>Paradesc</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Paradesc</em>'.
   * @see fr.ocelet.lang.ocelet.Paradesc
   * @generated
   */
  EClass getParadesc();

  /**
   * Returns the meta object for the attribute '{@link fr.ocelet.lang.ocelet.Paradesc#getPardesc <em>Pardesc</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Pardesc</em>'.
   * @see fr.ocelet.lang.ocelet.Paradesc#getPardesc()
   * @see #getParadesc()
   * @generated
   */
  EAttribute getParadesc_Pardesc();

  /**
   * Returns the meta object for class '{@link fr.ocelet.lang.ocelet.Paraopt <em>Paraopt</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Paraopt</em>'.
   * @see fr.ocelet.lang.ocelet.Paraopt
   * @generated
   */
  EClass getParaopt();

  /**
   * Returns the meta object for the attribute '{@link fr.ocelet.lang.ocelet.Paraopt#getParopt <em>Paropt</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Paropt</em>'.
   * @see fr.ocelet.lang.ocelet.Paraopt#getParopt()
   * @see #getParaopt()
   * @generated
   */
  EAttribute getParaopt_Paropt();

  /**
   * Returns the meta object for class '{@link fr.ocelet.lang.ocelet.Entity <em>Entity</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Entity</em>'.
   * @see fr.ocelet.lang.ocelet.Entity
   * @generated
   */
  EClass getEntity();

  /**
   * Returns the meta object for the attribute '{@link fr.ocelet.lang.ocelet.Entity#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see fr.ocelet.lang.ocelet.Entity#getName()
   * @see #getEntity()
   * @generated
   */
  EAttribute getEntity_Name();

  /**
   * Returns the meta object for the containment reference list '{@link fr.ocelet.lang.ocelet.Entity#getEntelns <em>Entelns</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Entelns</em>'.
   * @see fr.ocelet.lang.ocelet.Entity#getEntelns()
   * @see #getEntity()
   * @generated
   */
  EReference getEntity_Entelns();

  /**
   * Returns the meta object for class '{@link fr.ocelet.lang.ocelet.EntityElements <em>Entity Elements</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Entity Elements</em>'.
   * @see fr.ocelet.lang.ocelet.EntityElements
   * @generated
   */
  EClass getEntityElements();

  /**
   * Returns the meta object for the attribute '{@link fr.ocelet.lang.ocelet.EntityElements#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see fr.ocelet.lang.ocelet.EntityElements#getName()
   * @see #getEntityElements()
   * @generated
   */
  EAttribute getEntityElements_Name();

  /**
   * Returns the meta object for class '{@link fr.ocelet.lang.ocelet.PropertyDef <em>Property Def</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Property Def</em>'.
   * @see fr.ocelet.lang.ocelet.PropertyDef
   * @generated
   */
  EClass getPropertyDef();

  /**
   * Returns the meta object for the containment reference '{@link fr.ocelet.lang.ocelet.PropertyDef#getType <em>Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Type</em>'.
   * @see fr.ocelet.lang.ocelet.PropertyDef#getType()
   * @see #getPropertyDef()
   * @generated
   */
  EReference getPropertyDef_Type();

  /**
   * Returns the meta object for class '{@link fr.ocelet.lang.ocelet.ServiceDef <em>Service Def</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Service Def</em>'.
   * @see fr.ocelet.lang.ocelet.ServiceDef
   * @generated
   */
  EClass getServiceDef();

  /**
   * Returns the meta object for the containment reference '{@link fr.ocelet.lang.ocelet.ServiceDef#getType <em>Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Type</em>'.
   * @see fr.ocelet.lang.ocelet.ServiceDef#getType()
   * @see #getServiceDef()
   * @generated
   */
  EReference getServiceDef_Type();

  /**
   * Returns the meta object for the containment reference list '{@link fr.ocelet.lang.ocelet.ServiceDef#getParams <em>Params</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Params</em>'.
   * @see fr.ocelet.lang.ocelet.ServiceDef#getParams()
   * @see #getServiceDef()
   * @generated
   */
  EReference getServiceDef_Params();

  /**
   * Returns the meta object for the containment reference '{@link fr.ocelet.lang.ocelet.ServiceDef#getBody <em>Body</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Body</em>'.
   * @see fr.ocelet.lang.ocelet.ServiceDef#getBody()
   * @see #getServiceDef()
   * @generated
   */
  EReference getServiceDef_Body();

  /**
   * Returns the meta object for class '{@link fr.ocelet.lang.ocelet.ConstructorDef <em>Constructor Def</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Constructor Def</em>'.
   * @see fr.ocelet.lang.ocelet.ConstructorDef
   * @generated
   */
  EClass getConstructorDef();

  /**
   * Returns the meta object for the containment reference list '{@link fr.ocelet.lang.ocelet.ConstructorDef#getParams <em>Params</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Params</em>'.
   * @see fr.ocelet.lang.ocelet.ConstructorDef#getParams()
   * @see #getConstructorDef()
   * @generated
   */
  EReference getConstructorDef_Params();

  /**
   * Returns the meta object for the containment reference '{@link fr.ocelet.lang.ocelet.ConstructorDef#getBody <em>Body</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Body</em>'.
   * @see fr.ocelet.lang.ocelet.ConstructorDef#getBody()
   * @see #getConstructorDef()
   * @generated
   */
  EReference getConstructorDef_Body();

  /**
   * Returns the meta object for class '{@link fr.ocelet.lang.ocelet.Relation <em>Relation</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Relation</em>'.
   * @see fr.ocelet.lang.ocelet.Relation
   * @generated
   */
  EClass getRelation();

  /**
   * Returns the meta object for the attribute '{@link fr.ocelet.lang.ocelet.Relation#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see fr.ocelet.lang.ocelet.Relation#getName()
   * @see #getRelation()
   * @generated
   */
  EAttribute getRelation_Name();

  /**
   * Returns the meta object for the containment reference list '{@link fr.ocelet.lang.ocelet.Relation#getRoles <em>Roles</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Roles</em>'.
   * @see fr.ocelet.lang.ocelet.Relation#getRoles()
   * @see #getRelation()
   * @generated
   */
  EReference getRelation_Roles();

  /**
   * Returns the meta object for the containment reference list '{@link fr.ocelet.lang.ocelet.Relation#getRelelns <em>Relelns</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Relelns</em>'.
   * @see fr.ocelet.lang.ocelet.Relation#getRelelns()
   * @see #getRelation()
   * @generated
   */
  EReference getRelation_Relelns();

  /**
   * Returns the meta object for class '{@link fr.ocelet.lang.ocelet.Role <em>Role</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Role</em>'.
   * @see fr.ocelet.lang.ocelet.Role
   * @generated
   */
  EClass getRole();

  /**
   * Returns the meta object for the reference '{@link fr.ocelet.lang.ocelet.Role#getType <em>Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>Type</em>'.
   * @see fr.ocelet.lang.ocelet.Role#getType()
   * @see #getRole()
   * @generated
   */
  EReference getRole_Type();

  /**
   * Returns the meta object for the attribute '{@link fr.ocelet.lang.ocelet.Role#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see fr.ocelet.lang.ocelet.Role#getName()
   * @see #getRole()
   * @generated
   */
  EAttribute getRole_Name();

  /**
   * Returns the meta object for class '{@link fr.ocelet.lang.ocelet.RelElements <em>Rel Elements</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Rel Elements</em>'.
   * @see fr.ocelet.lang.ocelet.RelElements
   * @generated
   */
  EClass getRelElements();

  /**
   * Returns the meta object for the attribute '{@link fr.ocelet.lang.ocelet.RelElements#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see fr.ocelet.lang.ocelet.RelElements#getName()
   * @see #getRelElements()
   * @generated
   */
  EAttribute getRelElements_Name();

  /**
   * Returns the meta object for class '{@link fr.ocelet.lang.ocelet.RelPropertyDef <em>Rel Property Def</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Rel Property Def</em>'.
   * @see fr.ocelet.lang.ocelet.RelPropertyDef
   * @generated
   */
  EClass getRelPropertyDef();

  /**
   * Returns the meta object for the containment reference '{@link fr.ocelet.lang.ocelet.RelPropertyDef#getType <em>Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Type</em>'.
   * @see fr.ocelet.lang.ocelet.RelPropertyDef#getType()
   * @see #getRelPropertyDef()
   * @generated
   */
  EReference getRelPropertyDef_Type();

  /**
   * Returns the meta object for class '{@link fr.ocelet.lang.ocelet.InteractionDef <em>Interaction Def</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Interaction Def</em>'.
   * @see fr.ocelet.lang.ocelet.InteractionDef
   * @generated
   */
  EClass getInteractionDef();

  /**
   * Returns the meta object for the containment reference list '{@link fr.ocelet.lang.ocelet.InteractionDef#getParams <em>Params</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Params</em>'.
   * @see fr.ocelet.lang.ocelet.InteractionDef#getParams()
   * @see #getInteractionDef()
   * @generated
   */
  EReference getInteractionDef_Params();

  /**
   * Returns the meta object for the containment reference '{@link fr.ocelet.lang.ocelet.InteractionDef#getBody <em>Body</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Body</em>'.
   * @see fr.ocelet.lang.ocelet.InteractionDef#getBody()
   * @see #getInteractionDef()
   * @generated
   */
  EReference getInteractionDef_Body();

  /**
   * Returns the meta object for the containment reference list '{@link fr.ocelet.lang.ocelet.InteractionDef#getComitexpressions <em>Comitexpressions</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Comitexpressions</em>'.
   * @see fr.ocelet.lang.ocelet.InteractionDef#getComitexpressions()
   * @see #getInteractionDef()
   * @generated
   */
  EReference getInteractionDef_Comitexpressions();

  /**
   * Returns the meta object for class '{@link fr.ocelet.lang.ocelet.Comitexpr <em>Comitexpr</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Comitexpr</em>'.
   * @see fr.ocelet.lang.ocelet.Comitexpr
   * @generated
   */
  EClass getComitexpr();

  /**
   * Returns the meta object for the reference '{@link fr.ocelet.lang.ocelet.Comitexpr#getRol <em>Rol</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>Rol</em>'.
   * @see fr.ocelet.lang.ocelet.Comitexpr#getRol()
   * @see #getComitexpr()
   * @generated
   */
  EReference getComitexpr_Rol();

  /**
   * Returns the meta object for the attribute '{@link fr.ocelet.lang.ocelet.Comitexpr#getProp <em>Prop</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Prop</em>'.
   * @see fr.ocelet.lang.ocelet.Comitexpr#getProp()
   * @see #getComitexpr()
   * @generated
   */
  EAttribute getComitexpr_Prop();

  /**
   * Returns the meta object for the attribute '{@link fr.ocelet.lang.ocelet.Comitexpr#isUsepreval <em>Usepreval</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Usepreval</em>'.
   * @see fr.ocelet.lang.ocelet.Comitexpr#isUsepreval()
   * @see #getComitexpr()
   * @generated
   */
  EAttribute getComitexpr_Usepreval();

  /**
   * Returns the meta object for the containment reference '{@link fr.ocelet.lang.ocelet.Comitexpr#getAgrfunc <em>Agrfunc</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Agrfunc</em>'.
   * @see fr.ocelet.lang.ocelet.Comitexpr#getAgrfunc()
   * @see #getComitexpr()
   * @generated
   */
  EReference getComitexpr_Agrfunc();

  /**
   * Returns the meta object for class '{@link fr.ocelet.lang.ocelet.Filterdef <em>Filterdef</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Filterdef</em>'.
   * @see fr.ocelet.lang.ocelet.Filterdef
   * @generated
   */
  EClass getFilterdef();

  /**
   * Returns the meta object for the containment reference list '{@link fr.ocelet.lang.ocelet.Filterdef#getParams <em>Params</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Params</em>'.
   * @see fr.ocelet.lang.ocelet.Filterdef#getParams()
   * @see #getFilterdef()
   * @generated
   */
  EReference getFilterdef_Params();

  /**
   * Returns the meta object for the containment reference '{@link fr.ocelet.lang.ocelet.Filterdef#getBody <em>Body</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Body</em>'.
   * @see fr.ocelet.lang.ocelet.Filterdef#getBody()
   * @see #getFilterdef()
   * @generated
   */
  EReference getFilterdef_Body();

  /**
   * Returns the meta object for class '{@link fr.ocelet.lang.ocelet.Strucdef <em>Strucdef</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Strucdef</em>'.
   * @see fr.ocelet.lang.ocelet.Strucdef
   * @generated
   */
  EClass getStrucdef();

  /**
   * Returns the meta object for the attribute '{@link fr.ocelet.lang.ocelet.Strucdef#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see fr.ocelet.lang.ocelet.Strucdef#getName()
   * @see #getStrucdef()
   * @generated
   */
  EAttribute getStrucdef_Name();

  /**
   * Returns the meta object for the attribute '{@link fr.ocelet.lang.ocelet.Strucdef#getTypeArgument <em>Type Argument</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Type Argument</em>'.
   * @see fr.ocelet.lang.ocelet.Strucdef#getTypeArgument()
   * @see #getStrucdef()
   * @generated
   */
  EAttribute getStrucdef_TypeArgument();

  /**
   * Returns the meta object for the attribute '{@link fr.ocelet.lang.ocelet.Strucdef#getSuperType <em>Super Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Super Type</em>'.
   * @see fr.ocelet.lang.ocelet.Strucdef#getSuperType()
   * @see #getStrucdef()
   * @generated
   */
  EAttribute getStrucdef_SuperType();

  /**
   * Returns the meta object for the containment reference list '{@link fr.ocelet.lang.ocelet.Strucdef#getStrucelns <em>Strucelns</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Strucelns</em>'.
   * @see fr.ocelet.lang.ocelet.Strucdef#getStrucelns()
   * @see #getStrucdef()
   * @generated
   */
  EReference getStrucdef_Strucelns();

  /**
   * Returns the meta object for class '{@link fr.ocelet.lang.ocelet.StrucEln <em>Struc Eln</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Struc Eln</em>'.
   * @see fr.ocelet.lang.ocelet.StrucEln
   * @generated
   */
  EClass getStrucEln();

  /**
   * Returns the meta object for the containment reference '{@link fr.ocelet.lang.ocelet.StrucEln#getType <em>Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Type</em>'.
   * @see fr.ocelet.lang.ocelet.StrucEln#getType()
   * @see #getStrucEln()
   * @generated
   */
  EReference getStrucEln_Type();

  /**
   * Returns the meta object for the attribute '{@link fr.ocelet.lang.ocelet.StrucEln#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see fr.ocelet.lang.ocelet.StrucEln#getName()
   * @see #getStrucEln()
   * @generated
   */
  EAttribute getStrucEln_Name();

  /**
   * Returns the meta object for class '{@link fr.ocelet.lang.ocelet.StrucVarDef <em>Struc Var Def</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Struc Var Def</em>'.
   * @see fr.ocelet.lang.ocelet.StrucVarDef
   * @generated
   */
  EClass getStrucVarDef();

  /**
   * Returns the meta object for class '{@link fr.ocelet.lang.ocelet.StrucFuncDef <em>Struc Func Def</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Struc Func Def</em>'.
   * @see fr.ocelet.lang.ocelet.StrucFuncDef
   * @generated
   */
  EClass getStrucFuncDef();

  /**
   * Returns the meta object for the containment reference list '{@link fr.ocelet.lang.ocelet.StrucFuncDef#getParams <em>Params</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Params</em>'.
   * @see fr.ocelet.lang.ocelet.StrucFuncDef#getParams()
   * @see #getStrucFuncDef()
   * @generated
   */
  EReference getStrucFuncDef_Params();

  /**
   * Returns the meta object for the containment reference '{@link fr.ocelet.lang.ocelet.StrucFuncDef#getBody <em>Body</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Body</em>'.
   * @see fr.ocelet.lang.ocelet.StrucFuncDef#getBody()
   * @see #getStrucFuncDef()
   * @generated
   */
  EReference getStrucFuncDef_Body();

  /**
   * Returns the meta object for class '{@link fr.ocelet.lang.ocelet.Datafacer <em>Datafacer</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Datafacer</em>'.
   * @see fr.ocelet.lang.ocelet.Datafacer
   * @generated
   */
  EClass getDatafacer();

  /**
   * Returns the meta object for the attribute '{@link fr.ocelet.lang.ocelet.Datafacer#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see fr.ocelet.lang.ocelet.Datafacer#getName()
   * @see #getDatafacer()
   * @generated
   */
  EAttribute getDatafacer_Name();

  /**
   * Returns the meta object for the attribute '{@link fr.ocelet.lang.ocelet.Datafacer#getStoretype <em>Storetype</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Storetype</em>'.
   * @see fr.ocelet.lang.ocelet.Datafacer#getStoretype()
   * @see #getDatafacer()
   * @generated
   */
  EAttribute getDatafacer_Storetype();

  /**
   * Returns the meta object for the containment reference list '{@link fr.ocelet.lang.ocelet.Datafacer#getArguments <em>Arguments</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Arguments</em>'.
   * @see fr.ocelet.lang.ocelet.Datafacer#getArguments()
   * @see #getDatafacer()
   * @generated
   */
  EReference getDatafacer_Arguments();

  /**
   * Returns the meta object for the containment reference list '{@link fr.ocelet.lang.ocelet.Datafacer#getMatchbox <em>Matchbox</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Matchbox</em>'.
   * @see fr.ocelet.lang.ocelet.Datafacer#getMatchbox()
   * @see #getDatafacer()
   * @generated
   */
  EReference getDatafacer_Matchbox();

  /**
   * Returns the meta object for class '{@link fr.ocelet.lang.ocelet.Match <em>Match</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Match</em>'.
   * @see fr.ocelet.lang.ocelet.Match
   * @generated
   */
  EClass getMatch();

  /**
   * Returns the meta object for the reference '{@link fr.ocelet.lang.ocelet.Match#getMtype <em>Mtype</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>Mtype</em>'.
   * @see fr.ocelet.lang.ocelet.Match#getMtype()
   * @see #getMatch()
   * @generated
   */
  EReference getMatch_Mtype();

  /**
   * Returns the meta object for the containment reference list '{@link fr.ocelet.lang.ocelet.Match#getMatchprops <em>Matchprops</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Matchprops</em>'.
   * @see fr.ocelet.lang.ocelet.Match#getMatchprops()
   * @see #getMatch()
   * @generated
   */
  EReference getMatch_Matchprops();

  /**
   * Returns the meta object for class '{@link fr.ocelet.lang.ocelet.Matchtype <em>Matchtype</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Matchtype</em>'.
   * @see fr.ocelet.lang.ocelet.Matchtype
   * @generated
   */
  EClass getMatchtype();

  /**
   * Returns the meta object for class '{@link fr.ocelet.lang.ocelet.Mdef <em>Mdef</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Mdef</em>'.
   * @see fr.ocelet.lang.ocelet.Mdef
   * @generated
   */
  EClass getMdef();

  /**
   * Returns the meta object for the attribute '{@link fr.ocelet.lang.ocelet.Mdef#getProp <em>Prop</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Prop</em>'.
   * @see fr.ocelet.lang.ocelet.Mdef#getProp()
   * @see #getMdef()
   * @generated
   */
  EAttribute getMdef_Prop();

  /**
   * Returns the meta object for the attribute '{@link fr.ocelet.lang.ocelet.Mdef#getColname <em>Colname</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Colname</em>'.
   * @see fr.ocelet.lang.ocelet.Mdef#getColname()
   * @see #getMdef()
   * @generated
   */
  EAttribute getMdef_Colname();

  /**
   * Returns the meta object for class '{@link fr.ocelet.lang.ocelet.Agregdef <em>Agregdef</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Agregdef</em>'.
   * @see fr.ocelet.lang.ocelet.Agregdef
   * @generated
   */
  EClass getAgregdef();

  /**
   * Returns the meta object for the containment reference '{@link fr.ocelet.lang.ocelet.Agregdef#getType <em>Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Type</em>'.
   * @see fr.ocelet.lang.ocelet.Agregdef#getType()
   * @see #getAgregdef()
   * @generated
   */
  EReference getAgregdef_Type();

  /**
   * Returns the meta object for the attribute '{@link fr.ocelet.lang.ocelet.Agregdef#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see fr.ocelet.lang.ocelet.Agregdef#getName()
   * @see #getAgregdef()
   * @generated
   */
  EAttribute getAgregdef_Name();

  /**
   * Returns the meta object for the containment reference '{@link fr.ocelet.lang.ocelet.Agregdef#getBody <em>Body</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Body</em>'.
   * @see fr.ocelet.lang.ocelet.Agregdef#getBody()
   * @see #getAgregdef()
   * @generated
   */
  EReference getAgregdef_Body();

  /**
   * Returns the meta object for class '{@link fr.ocelet.lang.ocelet.Scenario <em>Scenario</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Scenario</em>'.
   * @see fr.ocelet.lang.ocelet.Scenario
   * @generated
   */
  EClass getScenario();

  /**
   * Returns the meta object for the attribute '{@link fr.ocelet.lang.ocelet.Scenario#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see fr.ocelet.lang.ocelet.Scenario#getName()
   * @see #getScenario()
   * @generated
   */
  EAttribute getScenario_Name();

  /**
   * Returns the meta object for the containment reference '{@link fr.ocelet.lang.ocelet.Scenario#getSccode <em>Sccode</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Sccode</em>'.
   * @see fr.ocelet.lang.ocelet.Scenario#getSccode()
   * @see #getScenario()
   * @generated
   */
  EReference getScenario_Sccode();

  /**
   * Returns the factory that creates the instances of the model.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the factory that creates the instances of the model.
   * @generated
   */
  OceletFactory getOceletFactory();

  /**
   * <!-- begin-user-doc -->
   * Defines literals for the meta objects that represent
   * <ul>
   *   <li>each class,</li>
   *   <li>each feature of each class,</li>
   *   <li>each enum,</li>
   *   <li>and each data type</li>
   * </ul>
   * <!-- end-user-doc -->
   * @generated
   */
  interface Literals
  {
    /**
     * The meta object literal for the '{@link fr.ocelet.lang.ocelet.impl.ModelImpl <em>Model</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see fr.ocelet.lang.ocelet.impl.ModelImpl
     * @see fr.ocelet.lang.ocelet.impl.OceletPackageImpl#getModel()
     * @generated
     */
    EClass MODEL = eINSTANCE.getModel();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MODEL__NAME = eINSTANCE.getModel_Name();

    /**
     * The meta object literal for the '<em><b>Modelns</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference MODEL__MODELNS = eINSTANCE.getModel_Modelns();

    /**
     * The meta object literal for the '{@link fr.ocelet.lang.ocelet.impl.ModElnImpl <em>Mod Eln</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see fr.ocelet.lang.ocelet.impl.ModElnImpl
     * @see fr.ocelet.lang.ocelet.impl.OceletPackageImpl#getModEln()
     * @generated
     */
    EClass MOD_ELN = eINSTANCE.getModEln();

    /**
     * The meta object literal for the '{@link fr.ocelet.lang.ocelet.impl.MetadataImpl <em>Metadata</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see fr.ocelet.lang.ocelet.impl.MetadataImpl
     * @see fr.ocelet.lang.ocelet.impl.OceletPackageImpl#getMetadata()
     * @generated
     */
    EClass METADATA = eINSTANCE.getMetadata();

    /**
     * The meta object literal for the '<em><b>Desc</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute METADATA__DESC = eINSTANCE.getMetadata_Desc();

    /**
     * The meta object literal for the '<em><b>Webp</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute METADATA__WEBP = eINSTANCE.getMetadata_Webp();

    /**
     * The meta object literal for the '<em><b>Paramdefs</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference METADATA__PARAMDEFS = eINSTANCE.getMetadata_Paramdefs();

    /**
     * The meta object literal for the '{@link fr.ocelet.lang.ocelet.impl.ParameterImpl <em>Parameter</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see fr.ocelet.lang.ocelet.impl.ParameterImpl
     * @see fr.ocelet.lang.ocelet.impl.OceletPackageImpl#getParameter()
     * @generated
     */
    EClass PARAMETER = eINSTANCE.getParameter();

    /**
     * The meta object literal for the '<em><b>Ptype</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference PARAMETER__PTYPE = eINSTANCE.getParameter_Ptype();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute PARAMETER__NAME = eINSTANCE.getParameter_Name();

    /**
     * The meta object literal for the '<em><b>Paramparts</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference PARAMETER__PARAMPARTS = eINSTANCE.getParameter_Paramparts();

    /**
     * The meta object literal for the '{@link fr.ocelet.lang.ocelet.impl.ParampartImpl <em>Parampart</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see fr.ocelet.lang.ocelet.impl.ParampartImpl
     * @see fr.ocelet.lang.ocelet.impl.OceletPackageImpl#getParampart()
     * @generated
     */
    EClass PARAMPART = eINSTANCE.getParampart();

    /**
     * The meta object literal for the '{@link fr.ocelet.lang.ocelet.impl.ParamunitImpl <em>Paramunit</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see fr.ocelet.lang.ocelet.impl.ParamunitImpl
     * @see fr.ocelet.lang.ocelet.impl.OceletPackageImpl#getParamunit()
     * @generated
     */
    EClass PARAMUNIT = eINSTANCE.getParamunit();

    /**
     * The meta object literal for the '<em><b>Parunit</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute PARAMUNIT__PARUNIT = eINSTANCE.getParamunit_Parunit();

    /**
     * The meta object literal for the '{@link fr.ocelet.lang.ocelet.impl.ParamdefaImpl <em>Paramdefa</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see fr.ocelet.lang.ocelet.impl.ParamdefaImpl
     * @see fr.ocelet.lang.ocelet.impl.OceletPackageImpl#getParamdefa()
     * @generated
     */
    EClass PARAMDEFA = eINSTANCE.getParamdefa();

    /**
     * The meta object literal for the '<em><b>Pardefa</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute PARAMDEFA__PARDEFA = eINSTANCE.getParamdefa_Pardefa();

    /**
     * The meta object literal for the '{@link fr.ocelet.lang.ocelet.impl.ParaminImpl <em>Paramin</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see fr.ocelet.lang.ocelet.impl.ParaminImpl
     * @see fr.ocelet.lang.ocelet.impl.OceletPackageImpl#getParamin()
     * @generated
     */
    EClass PARAMIN = eINSTANCE.getParamin();

    /**
     * The meta object literal for the '<em><b>Parmin</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute PARAMIN__PARMIN = eINSTANCE.getParamin_Parmin();

    /**
     * The meta object literal for the '{@link fr.ocelet.lang.ocelet.impl.ParamaxImpl <em>Paramax</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see fr.ocelet.lang.ocelet.impl.ParamaxImpl
     * @see fr.ocelet.lang.ocelet.impl.OceletPackageImpl#getParamax()
     * @generated
     */
    EClass PARAMAX = eINSTANCE.getParamax();

    /**
     * The meta object literal for the '<em><b>Parmax</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute PARAMAX__PARMAX = eINSTANCE.getParamax_Parmax();

    /**
     * The meta object literal for the '{@link fr.ocelet.lang.ocelet.impl.ParadescImpl <em>Paradesc</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see fr.ocelet.lang.ocelet.impl.ParadescImpl
     * @see fr.ocelet.lang.ocelet.impl.OceletPackageImpl#getParadesc()
     * @generated
     */
    EClass PARADESC = eINSTANCE.getParadesc();

    /**
     * The meta object literal for the '<em><b>Pardesc</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute PARADESC__PARDESC = eINSTANCE.getParadesc_Pardesc();

    /**
     * The meta object literal for the '{@link fr.ocelet.lang.ocelet.impl.ParaoptImpl <em>Paraopt</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see fr.ocelet.lang.ocelet.impl.ParaoptImpl
     * @see fr.ocelet.lang.ocelet.impl.OceletPackageImpl#getParaopt()
     * @generated
     */
    EClass PARAOPT = eINSTANCE.getParaopt();

    /**
     * The meta object literal for the '<em><b>Paropt</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute PARAOPT__PAROPT = eINSTANCE.getParaopt_Paropt();

    /**
     * The meta object literal for the '{@link fr.ocelet.lang.ocelet.impl.EntityImpl <em>Entity</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see fr.ocelet.lang.ocelet.impl.EntityImpl
     * @see fr.ocelet.lang.ocelet.impl.OceletPackageImpl#getEntity()
     * @generated
     */
    EClass ENTITY = eINSTANCE.getEntity();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute ENTITY__NAME = eINSTANCE.getEntity_Name();

    /**
     * The meta object literal for the '<em><b>Entelns</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference ENTITY__ENTELNS = eINSTANCE.getEntity_Entelns();

    /**
     * The meta object literal for the '{@link fr.ocelet.lang.ocelet.impl.EntityElementsImpl <em>Entity Elements</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see fr.ocelet.lang.ocelet.impl.EntityElementsImpl
     * @see fr.ocelet.lang.ocelet.impl.OceletPackageImpl#getEntityElements()
     * @generated
     */
    EClass ENTITY_ELEMENTS = eINSTANCE.getEntityElements();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute ENTITY_ELEMENTS__NAME = eINSTANCE.getEntityElements_Name();

    /**
     * The meta object literal for the '{@link fr.ocelet.lang.ocelet.impl.PropertyDefImpl <em>Property Def</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see fr.ocelet.lang.ocelet.impl.PropertyDefImpl
     * @see fr.ocelet.lang.ocelet.impl.OceletPackageImpl#getPropertyDef()
     * @generated
     */
    EClass PROPERTY_DEF = eINSTANCE.getPropertyDef();

    /**
     * The meta object literal for the '<em><b>Type</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference PROPERTY_DEF__TYPE = eINSTANCE.getPropertyDef_Type();

    /**
     * The meta object literal for the '{@link fr.ocelet.lang.ocelet.impl.ServiceDefImpl <em>Service Def</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see fr.ocelet.lang.ocelet.impl.ServiceDefImpl
     * @see fr.ocelet.lang.ocelet.impl.OceletPackageImpl#getServiceDef()
     * @generated
     */
    EClass SERVICE_DEF = eINSTANCE.getServiceDef();

    /**
     * The meta object literal for the '<em><b>Type</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference SERVICE_DEF__TYPE = eINSTANCE.getServiceDef_Type();

    /**
     * The meta object literal for the '<em><b>Params</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference SERVICE_DEF__PARAMS = eINSTANCE.getServiceDef_Params();

    /**
     * The meta object literal for the '<em><b>Body</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference SERVICE_DEF__BODY = eINSTANCE.getServiceDef_Body();

    /**
     * The meta object literal for the '{@link fr.ocelet.lang.ocelet.impl.ConstructorDefImpl <em>Constructor Def</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see fr.ocelet.lang.ocelet.impl.ConstructorDefImpl
     * @see fr.ocelet.lang.ocelet.impl.OceletPackageImpl#getConstructorDef()
     * @generated
     */
    EClass CONSTRUCTOR_DEF = eINSTANCE.getConstructorDef();

    /**
     * The meta object literal for the '<em><b>Params</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference CONSTRUCTOR_DEF__PARAMS = eINSTANCE.getConstructorDef_Params();

    /**
     * The meta object literal for the '<em><b>Body</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference CONSTRUCTOR_DEF__BODY = eINSTANCE.getConstructorDef_Body();

    /**
     * The meta object literal for the '{@link fr.ocelet.lang.ocelet.impl.RelationImpl <em>Relation</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see fr.ocelet.lang.ocelet.impl.RelationImpl
     * @see fr.ocelet.lang.ocelet.impl.OceletPackageImpl#getRelation()
     * @generated
     */
    EClass RELATION = eINSTANCE.getRelation();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute RELATION__NAME = eINSTANCE.getRelation_Name();

    /**
     * The meta object literal for the '<em><b>Roles</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference RELATION__ROLES = eINSTANCE.getRelation_Roles();

    /**
     * The meta object literal for the '<em><b>Relelns</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference RELATION__RELELNS = eINSTANCE.getRelation_Relelns();

    /**
     * The meta object literal for the '{@link fr.ocelet.lang.ocelet.impl.RoleImpl <em>Role</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see fr.ocelet.lang.ocelet.impl.RoleImpl
     * @see fr.ocelet.lang.ocelet.impl.OceletPackageImpl#getRole()
     * @generated
     */
    EClass ROLE = eINSTANCE.getRole();

    /**
     * The meta object literal for the '<em><b>Type</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference ROLE__TYPE = eINSTANCE.getRole_Type();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute ROLE__NAME = eINSTANCE.getRole_Name();

    /**
     * The meta object literal for the '{@link fr.ocelet.lang.ocelet.impl.RelElementsImpl <em>Rel Elements</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see fr.ocelet.lang.ocelet.impl.RelElementsImpl
     * @see fr.ocelet.lang.ocelet.impl.OceletPackageImpl#getRelElements()
     * @generated
     */
    EClass REL_ELEMENTS = eINSTANCE.getRelElements();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute REL_ELEMENTS__NAME = eINSTANCE.getRelElements_Name();

    /**
     * The meta object literal for the '{@link fr.ocelet.lang.ocelet.impl.RelPropertyDefImpl <em>Rel Property Def</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see fr.ocelet.lang.ocelet.impl.RelPropertyDefImpl
     * @see fr.ocelet.lang.ocelet.impl.OceletPackageImpl#getRelPropertyDef()
     * @generated
     */
    EClass REL_PROPERTY_DEF = eINSTANCE.getRelPropertyDef();

    /**
     * The meta object literal for the '<em><b>Type</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference REL_PROPERTY_DEF__TYPE = eINSTANCE.getRelPropertyDef_Type();

    /**
     * The meta object literal for the '{@link fr.ocelet.lang.ocelet.impl.InteractionDefImpl <em>Interaction Def</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see fr.ocelet.lang.ocelet.impl.InteractionDefImpl
     * @see fr.ocelet.lang.ocelet.impl.OceletPackageImpl#getInteractionDef()
     * @generated
     */
    EClass INTERACTION_DEF = eINSTANCE.getInteractionDef();

    /**
     * The meta object literal for the '<em><b>Params</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference INTERACTION_DEF__PARAMS = eINSTANCE.getInteractionDef_Params();

    /**
     * The meta object literal for the '<em><b>Body</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference INTERACTION_DEF__BODY = eINSTANCE.getInteractionDef_Body();

    /**
     * The meta object literal for the '<em><b>Comitexpressions</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference INTERACTION_DEF__COMITEXPRESSIONS = eINSTANCE.getInteractionDef_Comitexpressions();

    /**
     * The meta object literal for the '{@link fr.ocelet.lang.ocelet.impl.ComitexprImpl <em>Comitexpr</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see fr.ocelet.lang.ocelet.impl.ComitexprImpl
     * @see fr.ocelet.lang.ocelet.impl.OceletPackageImpl#getComitexpr()
     * @generated
     */
    EClass COMITEXPR = eINSTANCE.getComitexpr();

    /**
     * The meta object literal for the '<em><b>Rol</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference COMITEXPR__ROL = eINSTANCE.getComitexpr_Rol();

    /**
     * The meta object literal for the '<em><b>Prop</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute COMITEXPR__PROP = eINSTANCE.getComitexpr_Prop();

    /**
     * The meta object literal for the '<em><b>Usepreval</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute COMITEXPR__USEPREVAL = eINSTANCE.getComitexpr_Usepreval();

    /**
     * The meta object literal for the '<em><b>Agrfunc</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference COMITEXPR__AGRFUNC = eINSTANCE.getComitexpr_Agrfunc();

    /**
     * The meta object literal for the '{@link fr.ocelet.lang.ocelet.impl.FilterdefImpl <em>Filterdef</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see fr.ocelet.lang.ocelet.impl.FilterdefImpl
     * @see fr.ocelet.lang.ocelet.impl.OceletPackageImpl#getFilterdef()
     * @generated
     */
    EClass FILTERDEF = eINSTANCE.getFilterdef();

    /**
     * The meta object literal for the '<em><b>Params</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference FILTERDEF__PARAMS = eINSTANCE.getFilterdef_Params();

    /**
     * The meta object literal for the '<em><b>Body</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference FILTERDEF__BODY = eINSTANCE.getFilterdef_Body();

    /**
     * The meta object literal for the '{@link fr.ocelet.lang.ocelet.impl.StrucdefImpl <em>Strucdef</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see fr.ocelet.lang.ocelet.impl.StrucdefImpl
     * @see fr.ocelet.lang.ocelet.impl.OceletPackageImpl#getStrucdef()
     * @generated
     */
    EClass STRUCDEF = eINSTANCE.getStrucdef();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute STRUCDEF__NAME = eINSTANCE.getStrucdef_Name();

    /**
     * The meta object literal for the '<em><b>Type Argument</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute STRUCDEF__TYPE_ARGUMENT = eINSTANCE.getStrucdef_TypeArgument();

    /**
     * The meta object literal for the '<em><b>Super Type</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute STRUCDEF__SUPER_TYPE = eINSTANCE.getStrucdef_SuperType();

    /**
     * The meta object literal for the '<em><b>Strucelns</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference STRUCDEF__STRUCELNS = eINSTANCE.getStrucdef_Strucelns();

    /**
     * The meta object literal for the '{@link fr.ocelet.lang.ocelet.impl.StrucElnImpl <em>Struc Eln</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see fr.ocelet.lang.ocelet.impl.StrucElnImpl
     * @see fr.ocelet.lang.ocelet.impl.OceletPackageImpl#getStrucEln()
     * @generated
     */
    EClass STRUC_ELN = eINSTANCE.getStrucEln();

    /**
     * The meta object literal for the '<em><b>Type</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference STRUC_ELN__TYPE = eINSTANCE.getStrucEln_Type();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute STRUC_ELN__NAME = eINSTANCE.getStrucEln_Name();

    /**
     * The meta object literal for the '{@link fr.ocelet.lang.ocelet.impl.StrucVarDefImpl <em>Struc Var Def</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see fr.ocelet.lang.ocelet.impl.StrucVarDefImpl
     * @see fr.ocelet.lang.ocelet.impl.OceletPackageImpl#getStrucVarDef()
     * @generated
     */
    EClass STRUC_VAR_DEF = eINSTANCE.getStrucVarDef();

    /**
     * The meta object literal for the '{@link fr.ocelet.lang.ocelet.impl.StrucFuncDefImpl <em>Struc Func Def</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see fr.ocelet.lang.ocelet.impl.StrucFuncDefImpl
     * @see fr.ocelet.lang.ocelet.impl.OceletPackageImpl#getStrucFuncDef()
     * @generated
     */
    EClass STRUC_FUNC_DEF = eINSTANCE.getStrucFuncDef();

    /**
     * The meta object literal for the '<em><b>Params</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference STRUC_FUNC_DEF__PARAMS = eINSTANCE.getStrucFuncDef_Params();

    /**
     * The meta object literal for the '<em><b>Body</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference STRUC_FUNC_DEF__BODY = eINSTANCE.getStrucFuncDef_Body();

    /**
     * The meta object literal for the '{@link fr.ocelet.lang.ocelet.impl.DatafacerImpl <em>Datafacer</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see fr.ocelet.lang.ocelet.impl.DatafacerImpl
     * @see fr.ocelet.lang.ocelet.impl.OceletPackageImpl#getDatafacer()
     * @generated
     */
    EClass DATAFACER = eINSTANCE.getDatafacer();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute DATAFACER__NAME = eINSTANCE.getDatafacer_Name();

    /**
     * The meta object literal for the '<em><b>Storetype</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute DATAFACER__STORETYPE = eINSTANCE.getDatafacer_Storetype();

    /**
     * The meta object literal for the '<em><b>Arguments</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference DATAFACER__ARGUMENTS = eINSTANCE.getDatafacer_Arguments();

    /**
     * The meta object literal for the '<em><b>Matchbox</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference DATAFACER__MATCHBOX = eINSTANCE.getDatafacer_Matchbox();

    /**
     * The meta object literal for the '{@link fr.ocelet.lang.ocelet.impl.MatchImpl <em>Match</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see fr.ocelet.lang.ocelet.impl.MatchImpl
     * @see fr.ocelet.lang.ocelet.impl.OceletPackageImpl#getMatch()
     * @generated
     */
    EClass MATCH = eINSTANCE.getMatch();

    /**
     * The meta object literal for the '<em><b>Mtype</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference MATCH__MTYPE = eINSTANCE.getMatch_Mtype();

    /**
     * The meta object literal for the '<em><b>Matchprops</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference MATCH__MATCHPROPS = eINSTANCE.getMatch_Matchprops();

    /**
     * The meta object literal for the '{@link fr.ocelet.lang.ocelet.impl.MatchtypeImpl <em>Matchtype</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see fr.ocelet.lang.ocelet.impl.MatchtypeImpl
     * @see fr.ocelet.lang.ocelet.impl.OceletPackageImpl#getMatchtype()
     * @generated
     */
    EClass MATCHTYPE = eINSTANCE.getMatchtype();

    /**
     * The meta object literal for the '{@link fr.ocelet.lang.ocelet.impl.MdefImpl <em>Mdef</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see fr.ocelet.lang.ocelet.impl.MdefImpl
     * @see fr.ocelet.lang.ocelet.impl.OceletPackageImpl#getMdef()
     * @generated
     */
    EClass MDEF = eINSTANCE.getMdef();

    /**
     * The meta object literal for the '<em><b>Prop</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MDEF__PROP = eINSTANCE.getMdef_Prop();

    /**
     * The meta object literal for the '<em><b>Colname</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MDEF__COLNAME = eINSTANCE.getMdef_Colname();

    /**
     * The meta object literal for the '{@link fr.ocelet.lang.ocelet.impl.AgregdefImpl <em>Agregdef</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see fr.ocelet.lang.ocelet.impl.AgregdefImpl
     * @see fr.ocelet.lang.ocelet.impl.OceletPackageImpl#getAgregdef()
     * @generated
     */
    EClass AGREGDEF = eINSTANCE.getAgregdef();

    /**
     * The meta object literal for the '<em><b>Type</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference AGREGDEF__TYPE = eINSTANCE.getAgregdef_Type();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute AGREGDEF__NAME = eINSTANCE.getAgregdef_Name();

    /**
     * The meta object literal for the '<em><b>Body</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference AGREGDEF__BODY = eINSTANCE.getAgregdef_Body();

    /**
     * The meta object literal for the '{@link fr.ocelet.lang.ocelet.impl.ScenarioImpl <em>Scenario</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see fr.ocelet.lang.ocelet.impl.ScenarioImpl
     * @see fr.ocelet.lang.ocelet.impl.OceletPackageImpl#getScenario()
     * @generated
     */
    EClass SCENARIO = eINSTANCE.getScenario();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute SCENARIO__NAME = eINSTANCE.getScenario_Name();

    /**
     * The meta object literal for the '<em><b>Sccode</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference SCENARIO__SCCODE = eINSTANCE.getScenario_Sccode();

  }

} //OceletPackage
