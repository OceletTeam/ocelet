package fr.ocelet.lang.ui;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IProject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.ui.resource.IResourceSetProvider;
import org.eclipse.xtext.ui.resource.XtextResourceSetProvider;
import org.eclipse.xtext.ui.util.JdtClasspathUriResolver;
import com.google.inject.Inject;
import com.google.inject.Provider;


public class OceletResourceSetProvider implements IResourceSetProvider {

    private final static Logger LOG = Logger.getLogger(OceletResourceSetProvider.class);
    
	@Inject
	private Provider<XtextResourceSet> resourceSetProvider;
	
	@Inject
	private PlatformURIMapCache platformURIMapCache;
	

	@Inject
	private XtextResourceSetProvider slowProvider;
	
	private boolean slow = Boolean.getBoolean( "slowXtextResourceSetProvider" );
	
	public ResourceSet get(IProject project) {

	    long start = System.nanoTime();
	    try {
	        XtextResourceSet result;
    	    if( slow ) {
    	        result = (XtextResourceSet) slowProvider.get( project );
    	    } else {
    	        result = resourceSetProvider.get();
        		IJavaProject javaProject = JavaCore.create(project);
        		if (javaProject != null && javaProject.exists()) {
        		    result.getURIConverter().getURIMap().putAll(platformURIMapCache.computePlatformURIMap(javaProject));
        		    result.setClasspathURIContext(javaProject);
        		    result.setClasspathUriResolver(new JdtClasspathUriResolver());
        		}
    	    }
    	    
    	    LOG.debug( "OceletResourceSetProvider: map size: " + result.getURIConverter().getURIMap().size() );
    	    
    	    return result;
	    } finally {
	        long end = System.nanoTime();
	        LOG.debug( "OceletResourceSetProvider: " + slow + " " + (end-start)/1000 + " us" );
	        LOG.debug(platformURIMapCache.getStats().toString());
	    }
	}
}
