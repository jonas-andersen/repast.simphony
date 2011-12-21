package repast.simphony.relogo.factories;

import java.lang.reflect.Method;
import java.util.List;

import repast.simphony.relogo.BasePatch;
import repast.simphony.relogo.Observer;
import repast.simphony.relogo.Patch;
import simphony.util.messages.MessageCenter;

public class PatchFactory {
	private static MessageCenter msgCenter = MessageCenter.getMessageCenter(PatchFactory.class);
	
	private Observer observer = null;
	
	private Class<? extends BasePatch> patchType;

	public PatchFactory(Class<? extends BasePatch> patchType){
		if (BasePatch.class.isAssignableFrom(patchType)){
			this.patchType = patchType;
		}
		else {
			throw new RuntimeException("Argument to PatchFactoryImpl constructor needs to extend BasePatch.");
		}
	}
	
	public void init(Observer observer) {
		//Set the observer for the patch factory
		this.observer = observer;
		
		/**
		 * The @Diffusible AST transformation creates a static method "getDiffusiblePatchVars" in the user patch class.
		 */
		
		try {
			Method getDiffusiblePatchVarsMethod = patchType.getMethod("getDiffusiblePatchVars");
			Object result = getDiffusiblePatchVarsMethod.invoke(null);
			if (result != null && result instanceof List<?>){
				List<?> patchVars = (List<?>) result;
				for (Object var : patchVars){
					if (var instanceof String){
						observer.createPatchVar((String)var);
					}
				}
			}
		} catch (Exception e1) {
		}
		
		
	}
	
	public Patch createPatch() {
		if (observer == null){
			throw(new RuntimeException("The PatchFactoryImpl init method needs to be called before instatiating Patches."));
		}
		BasePatch patch = null;
		try {
			patch = patchType.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		patch.setBasePatchProperties(observer, this);
		return patch;
	}
}