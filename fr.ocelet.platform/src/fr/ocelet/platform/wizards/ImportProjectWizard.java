package fr.ocelet.platform.wizards;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;
import org.eclipse.ui.internal.wizards.datatransfer.DataTransferMessages;

/**
 * Modified version of the
 * org.eclipse.ui.wizards.datatransfer.ExternalProjectImportWizard that adds our
 * own ImportProjectWizardPage.
 * <p>
 * The original class was not intended to be subclassed so we had to make a copy
 * of it.
 * </p>
 */

@SuppressWarnings("restriction")
public class ImportProjectWizard extends Wizard implements IImportWizard {
	private static final String EXTERNAL_PROJECT_SECTION = "ExternalProjectImportWizard";//$NON-NLS-1$
	private ImportProjectWizardPage mainPage;
	private IStructuredSelection currentSelection = null;
	private String initialPath = null;

	/**
	 * Constructor for ExternalProjectImportWizard.
	 */
	public ImportProjectWizard() {
		this(null);
	}

	/**
	 * Constructor for ExternalProjectImportWizard.
	 * 
	 * @param initialPath
	 *            Default path for wizard to import
	 * @since 3.5
	 */
	public ImportProjectWizard(String initialPath) {
		super();
		this.initialPath = initialPath;
		setNeedsProgressMonitor(true);
		IDialogSettings workbenchSettings = IDEWorkbenchPlugin.getDefault()
				.getDialogSettings();

		IDialogSettings wizardSettings = workbenchSettings
				.getSection(EXTERNAL_PROJECT_SECTION);
		if (wizardSettings == null) {
			wizardSettings = workbenchSettings
					.addNewSection(EXTERNAL_PROJECT_SECTION);
		}
		setDialogSettings(wizardSettings);
	}

	public void addPages() {
		super.addPages();
		mainPage = new ImportProjectWizardPage("wizardExternalProjectsPage",
				initialPath, currentSelection);
		addPage(mainPage);
	}

	/*
	 * (non-Javadoc) Method declared on IWorkbenchWizard.
	 */
	public void init(IWorkbench workbench, IStructuredSelection currentSelection) {
		setWindowTitle(DataTransferMessages.DataTransfer_importTitle);
		setDefaultPageImageDescriptor(IDEWorkbenchPlugin
				.getIDEImageDescriptor("wizban/importproj_wiz.png")); //$NON-NLS-1$
		this.currentSelection = currentSelection;
	}

	/*
	 * (non-Javadoc) Method declared on IWizard.
	 */
	public boolean performCancel() {
		mainPage.performCancel();
		return true;
	}

	/*
	 * (non-Javadoc) Method declared on IWizard.
	 */
	public boolean performFinish() {
		return mainPage.createProjects();
	}

}
