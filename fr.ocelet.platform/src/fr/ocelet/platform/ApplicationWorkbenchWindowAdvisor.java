package fr.ocelet.platform;

import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.IPerspectiveDescriptor;
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
		configurer.setInitialSize(new Point(640, 480));
		configurer.setShowCoolBar(false);
		configurer.setShowStatusLine(true);
		configurer.setTitle("Ocelet Modelling Platform");
	}

	@Override
	public void postWindowOpen() {
		super.postWindowOpen();

		IWorkbenchWindowConfigurer workbenchWindowConfigurer = getWindowConfigurer();
		IActionBarConfigurer actionBarConfigurer = workbenchWindowConfigurer
				.getActionBarConfigurer();
		IMenuManager menuManager = actionBarConfigurer.getMenuManager();
		IContributionItem[] menuItems = menuManager.getItems();
		for (int i = 0; i < menuItems.length; i++) {
			IContributionItem menuItem = menuItems[i];

			// Hack to remove the Run menu - it seems you cannot do this using
			// the
			// "org.eclipse.ui.activities" extension
//			System.out.println("PWO | Menu item id : " + menuItem.getId());
			if ("org.eclipse.ui.run".equals(menuItem.getId())) {
				menuManager.remove(menuItem);
			}
		}
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().hideActionSet( "org.eclipse.ui.run");
		menuManager.update(true);
	}

	public void postWindowCreate() {
		IWorkbenchWindowConfigurer workbenchWindowConfigurer = getWindowConfigurer();
		IActionBarConfigurer actionBarConfigurer = workbenchWindowConfigurer
				.getActionBarConfigurer();
		IMenuManager menuManager = actionBarConfigurer.getMenuManager();
		IContributionItem[] menuItems = menuManager.getItems();
		for (int i = 0; i < menuItems.length; i++) {
			IContributionItem menuItem = menuItems[i];

			// Hack to remove the Run menu - it seems you cannot do this using
			// the
			// "org.eclipse.ui.activities" extension
//			System.out.println("PWC | Menu item id : " + menuItem.getId());
			if ("org.eclipse.ui.run".equals(menuItem.getId())) {
				menuManager.remove(menuItem);
			}
		}
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().hideActionSet( "org.eclipse.ui.run");
		menuManager.update(true);
	}

}
