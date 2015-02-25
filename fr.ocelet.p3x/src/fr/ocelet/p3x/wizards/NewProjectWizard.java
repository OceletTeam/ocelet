package fr.ocelet.p3x.wizards;

import org.eclipse.jface.wizard.Wizard;

import fr.ocelet.p3x.wizards.models.ProjectModel;

public class NewProjectWizard extends Wizard {

	protected NewProjectWizardPage one;
	protected ProjectModel pm;

	public NewProjectWizard() {
		super();
		setNeedsProgressMonitor(true);
	}

	@Override
	public void addPages() {
		one = new NewProjectWizardPage();
		addPage(one);
	}

	@Override
	public boolean performFinish() {
		this.pm = one.getProjectModel();
		return true;
	}

	public ProjectModel getProjectModel() {
		return one.getProjectModel();
	}
}