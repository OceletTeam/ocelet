package fr.ocelet.platform.dialogs;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import fr.ocelet.platform.PlatformSettings;

/**
 * An information dialog about the Ocelet Modelling Platform
 * 
 * @author Pascal Degenne - Initial contribution
 */
public class AboutOmpDialog extends TitleAreaDialog {

	public AboutOmpDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	public void create() {
		super.create();
		setTitle("Ocelet Modelling Platform");
		StringBuffer aboutmsg=new StringBuffer("");
		aboutmsg.append("Version : "+PlatformSettings.version+", licence : CeCILL 2.1");
		aboutmsg.append("\nMore information on http://www.ocelet.org");
		setMessage(aboutmsg.toString(),IMessageProvider.INFORMATION);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		ScrolledComposite scontainer = new ScrolledComposite(area, SWT.V_SCROLL
				| SWT.H_SCROLL | SWT.BORDER);
		scontainer.setLayout(new FillLayout());
		scontainer.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true,
				1, 1));
		Composite container = new Composite(scontainer, SWT.NONE);
		container.setLayout(new GridLayout(3, false));
		container
				.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		scontainer.setContent(container);
		scontainer.setExpandHorizontal(true);
		scontainer.setExpandVertical(true);
		scontainer.setMinSize(container.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		scontainer.setShowFocusedControl(true);
		scontainer.layout();

		return area;
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	// Overriden to remove the Cancel button.
	@Override
	protected Button createButton(Composite parent, int id, String label,
			boolean defaultButton) {
		if (id == IDialogConstants.CANCEL_ID)
			return null;
		return super.createButton(parent, id, label, defaultButton);
	}

}
