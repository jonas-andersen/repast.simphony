package repast.simphony.relogo.ide.plugin;

import org.codehaus.groovy.eclipse.core.builder.GroovyClasspathContainer;
import org.codehaus.groovy.eclipse.core.builder.GroovyClasspathContainerInitializer;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveRegistry;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWorkbenchPreferenceConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.intro.IIntroManager;
import org.eclipse.ui.intro.IIntroPart;

public class Startup implements IStartup {

	@Override
	public void earlyStartup() {
//		System.out.println("Hello from Startup");
//		System.out.println("The groovy version is: " + groovy.lang.GroovySystem.getVersion());
//		
		
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		// register the ReLogoResourceChangeListener
		workspace.addResourceChangeListener(new ReLogoResourceChangeListener());

		// We use the perspective bar extras to check for whether
		// the earlyStartup was run in this workspace before
		String extras = PlatformUI.getPreferenceStore().getString(
				IWorkbenchPreferenceConstants.PERSPECTIVE_BAR_EXTRAS);
		// If not, we open the ReLogo perspective and remove the Intro screen
		if (!extras.contains("repast.simphony.relogo.ide.relogoperspective")) {
			if (extras.equals("")) {
				extras = "repast.simphony.relogo.ide.relogoperspective";
			} else {
				extras = extras
						+ ",repast.simphony.relogo.ide.relogoperspective";
			}
			PlatformUI.getPreferenceStore().setValue(
					IWorkbenchPreferenceConstants.PERSPECTIVE_BAR_EXTRAS,
					extras);
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					IPerspectiveDescriptor[] openPersps = PlatformUI
							.getWorkbench().getActiveWorkbenchWindow()
							.getActivePage().getOpenPerspectives();
					boolean found = false;
					for (IPerspectiveDescriptor pd : openPersps) {
						if (pd.getId().equals(
								"repast.simphony.relogo.ide.relogoperspective")) {
							found = true;
							break;
						}
					}
					if (!found) {
						IPerspectiveRegistry reg = PlatformUI.getWorkbench()
								.getPerspectiveRegistry();
						IPerspectiveDescriptor relogoPersp = reg
								.findPerspectiveWithId("repast.simphony.relogo.ide.relogoperspective");
						PlatformUI.getWorkbench().getActiveWorkbenchWindow()
								.getActivePage().setPerspective(relogoPersp);
					}
					
					// Close the intro screen to avoid confusion.
					IIntroManager im = PlatformUI.getWorkbench()
							.getIntroManager();
					if (im != null){
						IIntroPart iip = im.getIntro();
						if (iip != null){
							im.closeIntro(iip);
						}
					}
				}
			});
		}

	}
}