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

import org.eclipse.jface.action.IContributionItem;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

	public ApplicationWorkbenchWindowAdvisor(
			IWorkbenchWindowConfigurer configurer) {
		super(configurer);
	}

	public ActionBarAdvisor createActionBarAdvisor(
			IActionBarConfigurer configurer) {
		return new ApplicationActionBarAdvisor(configurer);
	}

	public void preWindowOpen() {
		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		configurer.setInitialSize(new Point(1024, 768));
		configurer.setShowCoolBar(true);
		configurer.setShowStatusLine(false);
		configurer.setShowProgressIndicator(true);
		configurer.setTitle("Ocelet Modelling Platform");
	}

	// Remove unwanted menus and toolbar default icons,
	// and reduce de extra white space on the left of
	// the toolbar (visible only on Windows version)
	public void postWindowOpen() {

		IWorkbenchPage iwp = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();

		iwp.hideActionSet("org.eclipse.search.searchActionSet");
		iwp.hideActionSet("org.eclipse.ui.externaltools.ExternalToolsSet");
		iwp.hideActionSet("org.eclipse.ui.edit.text.actionSet.annotationNavigation");
		iwp.hideActionSet("org.eclipse.ui.edit.text.actionSet.navigation");
		iwp.hideActionSet("org.eclipse.jdt.ui.edit.text.java.toggleMarkOccurrences");
		iwp.hideActionSet("org.eclipse.debug.ui.launchActionSet");

		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		IContributionItem[] icit = configurer.getActionBarConfigurer()
				.getCoolBarManager().getItems();
		for (IContributionItem ici : icit) {
			if (ici.getId().compareTo("org.eclipse.debug.ui.launch.toolbar") == 0) {
				ici.setVisible(false);
			}
			if (ici.getId().compareTo("org.eclipse.debug.ui.main.toolbar") == 0) {
				ici.setVisible(false);
			}
			if ((ici.getId().compareTo("additions") == 0)
					|| (ici.getId().compareTo(
							"org.eclipse.debug.ui.launchActionSet") == 0)
					|| (ici.getId().compareTo(
							"org.eclipse.search.searchActionSet") == 0)
					|| (ici.getId()
							.compareTo(
									"org.eclipse.ui.edit.text.actionSet.annotationNavigation") == 0)
					|| (ici.getId().compareTo(
							"org.eclipse.ui.edit.text.actionSet.navigation") == 0))
				configurer.getActionBarConfigurer().getCoolBarManager()
						.remove(ici);
		}
	}
}
