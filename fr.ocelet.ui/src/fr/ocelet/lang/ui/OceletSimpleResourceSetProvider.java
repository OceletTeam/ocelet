package fr.ocelet.lang.ui;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.ui.resource.IResourceSetInitializer;
import org.eclipse.xtext.ui.resource.IResourceSetProvider;
import org.eclipse.xtext.ui.shared.contribution.ISharedStateContributionRegistry;
import org.eclipse.xtext.ui.workspace.EclipseProjectConfigProvider;
import org.eclipse.xtext.workspace.ProjectConfigAdapter;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

@Singleton
public class OceletSimpleResourceSetProvider implements IResourceSetProvider{
	
	
	@Inject
	private Provider<XtextResourceSet> resourceSetProvider;
	
	@Inject 
	private EclipseProjectConfigProvider projectConfigProvider;

	private ImmutableList<? extends IResourceSetInitializer> initializers = ImmutableList.of();

	@Inject
	private void setContributions(ISharedStateContributionRegistry contributionRegistry) {
		initializers = contributionRegistry.getContributedInstances(IResourceSetInitializer.class);
	}

	@Override
	public ResourceSet get(IProject project) {
		XtextResourceSet set = resourceSetProvider.get();
		ProjectConfigAdapter.install(set, projectConfigProvider.createProjectConfig(project));
		for(int i = 0; i < initializers.size(); i++) {
			System.out.println(initializers.get(i));
			initializers.get(i).initialize(set, project);
		}
		return set;
	}
}
