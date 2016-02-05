/*
*  Ocelet spatial modelling language.   www.ocelet.org
*  Copyright Cirad 2010-2016
*
*  This software is a domain specific programming language dedicated to writing
*  spatially explicit models and performing spatial dynamics simulations.
*
*  This software is governed by the CeCILL license under French law and
*  abiding by the rules of distribution of free software.  You can  use,
*  modify and/ or redistribute the software under the terms of the CeCILL
*  license as circulated by CEA, CNRS and INRIA at the following URL
*  "http://www.cecill.info".
*  As a counterpart to the access to the source code and  rights to copy,
*  modify and redistribute granted by the license, users are provided only
*  with a limited warranty  and the software's author,  the holder of the
*  economic rights,  and the successive licensors  have only limited
*  liability.
*  The fact that you are presently reading this means that you have had
*  knowledge of the CeCILL license and that you accept its terms.
*/

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
