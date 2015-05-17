package fr.ocelet.lang.scoping;

import java.util.List;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.xbase.scoping.batch.ImplicitlyImportedFeatures;

@SuppressWarnings("restriction")
public class OceletImplicitlyImportedFeatures extends
		ImplicitlyImportedFeatures {

	/**
	 * @return all JvmType containing static methods which are implicitly
	 *         imported
	 */
	public List<JvmType> getStaticImportClasses(Resource context) {
		List<Class<?>> classes = getStaticImportClasses();

		// The index 2 is to make sure the list remains sorted
		// based on its content since 2.8 version of XBase:
        //		ArrayLiterals.class,
        //		CollectionLiterals.class,
        //		InputOutput.class
		classes.add(2,fr.ocelet.runtime.geom.GeomBuilders.class);

		classes.add(java.lang.Math.class);
        classes.add(fr.ocelet.runtime.Miscutils.class);
        classes.add(fr.ocelet.runtime.TextFileWriter.class);
        classes.add(fr.ocelet.datafacer.KmlUtils.class);
		return getTypes(classes, context);
	}
	
}
