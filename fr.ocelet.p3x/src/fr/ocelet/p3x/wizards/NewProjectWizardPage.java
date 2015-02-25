package fr.ocelet.p3x.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import fr.ocelet.p3x.wizards.models.DefaultProject;
import fr.ocelet.p3x.wizards.models.ProjectModel;

public class NewProjectWizardPage extends WizardPage {

	private Composite container;
	private ProjectModel pm;
	private Text name;

	public NewProjectWizardPage() {
		super("New Ocelet Project");
		setTitle("Project name setting");
		pm = new DefaultProject(); // Will be dynamically affected in a later
									// version.
	}

	@Override
	public void createControl(Composite parent) {

		container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 2;
		Label label1 = new Label(container, SWT.NULL);
		label1.setText("Project name: ");

		name = new Text(container, SWT.BORDER | SWT.SINGLE);
		name.setText("");
		name.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (!name.getText().isEmpty()) {
					pm.setModelName(name.getText());
					setPageComplete(true);
				}
			}
		});

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		name.setLayoutData(gd);
		// Required to avoid an error in the system
		setControl(container);
		setPageComplete(false);
	}

	public ProjectModel getProjectModel() {
		return this.pm;
	}

}
