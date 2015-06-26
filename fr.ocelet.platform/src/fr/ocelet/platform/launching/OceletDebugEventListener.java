package fr.ocelet.platform.launching;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IDebugEventSetListener;
import org.eclipse.debug.core.model.IProcess;

/**
 * Listener called when running a simulation. As the launch mechanism
 * is asynchronous, we had to create this listener to be notified when
 * the simulation has terminated so we can refresh the project view
 * (and the output folder).
 * @author Pascal Degenne - Initial contribution
 *
 */
public class OceletDebugEventListener implements IDebugEventSetListener {

	private IProject ipr;
	
	public OceletDebugEventListener(IProject project){
		this.ipr= project;
	}
	
	@Override
	public void handleDebugEvents(DebugEvent[] events) {
		for (int i = 0; i < events.length; i++) {
			DebugEvent event = events[i];
			Object source = event.getSource();
			if (source instanceof IProcess
					&& event.getKind() == DebugEvent.TERMINATE) {
				try {
					ipr.refreshLocal(IResource.DEPTH_INFINITE, null);
				} catch (CoreException e) {
					e.printStackTrace();
				}
				DebugPlugin.getDefault().removeDebugEventListener(this);
			}
		}
	}
}
