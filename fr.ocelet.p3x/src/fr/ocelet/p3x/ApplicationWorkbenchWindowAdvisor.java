package fr.ocelet.p3x;

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

	// Remove unwanted menus and toolbar default icons
	public void postWindowOpen() {

		IWorkbenchPage iwp = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();

		iwp.hideActionSet("org.eclipse.search.searchActionSet");
		iwp.hideActionSet("org.eclipse.ui.externaltools.ExternalToolsSet");
		iwp.hideActionSet("org.eclipse.ui.edit.text.actionSet.annotationNavigation");
		iwp.hideActionSet("org.eclipse.ui.edit.text.actionSet.navigation");
//		iwp.hideActionSet("org.eclipse.jdt.ui.text.java.actionSet.presentation");
		iwp.hideActionSet("org.eclipse.jdt.ui.edit.text.java.toggleMarkOccurrences");
	}
}
