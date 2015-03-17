package fr.ocelet.platform;

import java.net.URL;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipse.ui.ide.IDE;
import org.osgi.framework.Bundle;

public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

	private static final String PERSPECTIVE_ID = "fr.ocelet.platform.perspective";

	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(
			IWorkbenchWindowConfigurer configurer) {
		return new ApplicationWorkbenchWindowAdvisor(configurer);
	}

	public String getInitialWindowPerspectiveId() {
		return PERSPECTIVE_ID;
	}

	public IAdaptable getDefaultPageInput() 
	{
		return ResourcesPlugin.getWorkspace().getRoot();
	}
	
	public void initialize(IWorkbenchConfigurer configurer) 
	{
		super.initialize(configurer);

		// inserted: register workbench adapters
		IDE.registerAdapters();
		
		final String ICONS_PATH = "icons/square16/gif/";
		final String ICONS_WIZARD_PATH = "icons/wizard/";

		Bundle ideBundle = Platform.getBundle("fr.ocelet.platform");

		// Images for the common navigator:
		declareWorkbenchImage(configurer, ideBundle, "IMG_OBJ_PROJECT",
				ICONS_PATH + "file_proj.gif", true);
		declareWorkbenchImage(configurer, ideBundle, "IMG_ICON16_OCELET",
				ICONS_PATH + "file_oclt.gif", true);
		declareWorkbenchImage(configurer, ideBundle, "IMG_ICON16_CONFIG",
				ICONS_PATH + "file_config.gif", true);

		// // Images for the wizards:
		declareWorkbenchImage(configurer, ideBundle, "IMG_WIZARD_NEWPROJ",
				ICONS_WIZARD_PATH + "new_proj.gif", true);
		declareWorkbenchImage(configurer, ideBundle, "IMG_WIZARD_NEWOCLT",
				ICONS_WIZARD_PATH + "new_oclt.gif", true);

	}

	private void declareWorkbenchImage(IWorkbenchConfigurer configurer_p,
			Bundle ideBundle, String symbolicName, String path, boolean shared) {
		URL url = ideBundle.getEntry(path);
		ImageDescriptor desc = ImageDescriptor.createFromURL(url);
		configurer_p.declareImage(symbolicName, desc, shared);
	}
}
