package fr.ocelet.platform.dialogs;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import fr.ocelet.runtime.ocltypes.KeyMap;
import fr.ocelet.runtime.ocltypes.List;
import fr.ocelet.runtime.styling.Gradient;

/**
 * An information dialog that displays all the available
 * color gradients defined in config/gradients.ocg
 * 
 * @author Pascal Degenne - Initial contribution
 */
public class ShowGradientsDialog extends TitleAreaDialog {

	private static KeyMap<String, Gradient> gkm;
	private String projName;

	public ShowGradientsDialog(Shell parentShell,
			KeyMap<String, Gradient> gmap, String projname) {
		super(parentShell);
		gkm = gmap;
		this.projName = projname;
	}

	@Override
	public void create() {
		super.create();
		setTitle("Available color gradients for " + projName);
		setMessage("These gradients are defined in config/gradients.ocg.",
				IMessageProvider.INFORMATION);
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
		for (Gradient gr : gkm.values())
			addGradientRef(container, gr);
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

	private void addGradientRef(Composite container, Gradient gradient) {
		Label gsp = new Label(container, SWT.FILL);
		gsp.setText("     ");
		Label gname = new Label(container, SWT.FILL | SWT.CENTER);
		gname.setText(gradient.getName() + " :  ");
		GridData gridata = new GridData();
		gridata.grabExcessHorizontalSpace = true;
		gridata.horizontalAlignment = GridData.FILL;
		GradientCanvas grad = new GradientCanvas(container, SWT.NONE, gradient);
	}

	protected class GradientCanvas extends Canvas {
		private Gradient gradient;

		public GradientCanvas(Composite parent, int style, Gradient gr) {
			super(parent, style);
			this.gradient = gr;
			addPaintListener(new PaintListener() {
				public void paintControl(PaintEvent pe) {
					List<fr.ocelet.runtime.ocltypes.Color> lc = gradient
							.toColorList(256);
					int i = 0;
					for (fr.ocelet.runtime.ocltypes.Color col : lc) {
						pe.gc.setForeground(new Color(pe.gc.getDevice(), col
								.getRed(), col.getGreen(), col.getBlue()));
						pe.gc.drawLine(i, 0, i, 32);
						i++;
					}
				}
			});
		}

		public Point computeSize(int wHint, int hHint, boolean changed) {
			return new Point(256, 32);
		}
	}

}
