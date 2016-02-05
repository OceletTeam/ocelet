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

package fr.ocelet.platform.wizards;

import org.eclipse.core.internal.resources.OS;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import fr.ocelet.platform.wizards.models.DefaultProject;
import fr.ocelet.platform.wizards.models.ProjectModel;

/**
 * New Ocelet project wizard page.
 * @author Pascal Degenne - Initial contribution
 *
 */
@SuppressWarnings("restriction")
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
				String rawname;
				rawname = name.getText().trim();
				rawname = rawname.replace(' ', '_');
				if (OS.isNameValid(rawname)) {
					String pname = Character.toString(rawname.charAt(0))
							.toUpperCase() + rawname.substring(1);
					pm.setModelName(pname);
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
