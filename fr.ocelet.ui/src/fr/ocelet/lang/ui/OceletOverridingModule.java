package fr.ocelet.lang.ui;
import org.eclipse.xtext.resource.clustering.DynamicResourceClusteringPolicy;
import org.eclipse.xtext.resource.clustering.IResourceClusteringPolicy;

import com.google.inject.Binder;
import com.google.inject.Module;
public class OceletOverridingModule implements Module {

	@Override
	public void configure(Binder binder) {
		binder.bind(IResourceClusteringPolicy.class).to(DynamicResourceClusteringPolicy.class);

	}

}