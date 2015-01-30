package fr.ocelet.platform.handlers;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Shell;

@SuppressWarnings("restriction")
public class ShowColorDialog {

	@Execute
	public void execute(Shell shell) {
		final ColorDialog cdg = new ColorDialog(shell);
		cdg.open();

		RGB scol = cdg.getRGB();
		if (scol != null) {
			StringSelection cstring = new StringSelection("rgb("+scol.red+","+scol.green+","+scol.blue+")");
			Clipboard clipboard = Toolkit.getDefaultToolkit()
					.getSystemClipboard();
			clipboard.setContents(cstring, null);
		}
	}

	@CanExecute
	public boolean canExecute() {
		return true;
	}

}
