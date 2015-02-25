package fr.ocelet.p3x.filters;

import org.eclipse.jdt.internal.ui.filters.LibraryFilter;
import org.eclipse.jface.viewers.Viewer;

/**
 * Filters references to external jar dependencies and to
 * the JRE classpath container in the Project Explorer.
 *   
 * @author Pascal Degenne - Initial contribution
 *
 */
@SuppressWarnings("restriction")
public class ExtJarFilter extends LibraryFilter {

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if (element instanceof org.eclipse.jdt.internal.ui.packageview.ClassPathContainer)
			return false;
		else
			return super.select(viewer, parentElement, element);
	}
}
