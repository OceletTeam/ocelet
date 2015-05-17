package fr.ocelet.lang.scoping;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.scoping.impl.ImportNormalizer;
import org.eclipse.xtext.xbase.scoping.XImportSectionNamespaceScopeProvider;

@SuppressWarnings("restriction")
public class OceletImportedNamespaceScopeProvider extends
XImportSectionNamespaceScopeProvider {

	@Override
	protected List<ImportNormalizer> getImplicitImports(boolean ignoreCase) {
		List<ImportNormalizer> inl = new ArrayList<ImportNormalizer>();
		inl.add(new ImportNormalizer(QualifiedName.create("java", "lang"),
				true, ignoreCase));
		inl.add(new ImportNormalizer(QualifiedName.create("fr","ocelet","runtime","ocltypes"), true, ignoreCase));
		inl.add(new ImportNormalizer(QualifiedName.create("fr","ocelet","runtime","geom","ocltypes"), true, ignoreCase));
		inl.add(new ImportNormalizer(QualifiedName.create("fr","ocelet","datafacer","ocltypes"), true, ignoreCase));
		inl.add(new ImportNormalizer(QualifiedName.create("fr","ocelet","runtime","relation","aggregops"), true, ignoreCase));
		return inl;
	}

}