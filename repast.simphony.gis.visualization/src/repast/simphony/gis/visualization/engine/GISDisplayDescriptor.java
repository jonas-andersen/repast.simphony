package repast.simphony.gis.visualization.engine;

import java.util.HashMap;
import java.util.Map;

import repast.simphony.visualization.engine.BasicDisplayDescriptor;
import repast.simphony.visualization.engine.DisplayDescriptor;
import repast.simphony.visualization.gis3D.RepastStereoOptionSceneController.RenderQuality;

/**
 * Display descriptor for GIS displays.
 * 
 * @author Eric Tatara
 *
 * TODO GIS Network style (included with BasicDisplayDescriptor ?)
 */
public class GISDisplayDescriptor extends BasicDisplayDescriptor {

	public static enum VIEW_TYPE {FLAT("Flat"), GLOBE("Globe");
		String name;

		VIEW_TYPE(String name){
			this.name = name;
		}
		
		public String getName(){
			return name;
		}
		
		@Override
		public String toString(){
			return name;
		}
	}

	/**
	 * Render quality for surface objects
	 */
	protected RenderQuality renderQuality;
	
	/**
	 * The view type determines how the map is displayed, eg flat or round globe.
	 */
	private VIEW_TYPE viewType;
	
	/**
	 * If true, the display will zoom extent to always keep all agents in view.
	 */
	private boolean trackAgents = false;

	/**
	 * Globe layers are the default WWJ layers like the WMS background, stars,
	 * etc that can be optionally added to displays.
	 */
	protected Map<String,Boolean> globeLayers = new HashMap<String,Boolean>();
	
	/**
	 * Map of <file name, style> for static coverages.  Style can be null.
	 *
	 */
	protected Map<String,String> staticCoverages = new HashMap<String,String>();
	
	/**
	 * Map of <coverage name, style> for dynamic coverages.  Style can be null.
	 *
	 */
	protected Map<String,String> coverageLayers = new HashMap<String,String>();
	
  // TODO WWJ - handle multiple styles
//  private static Class<?>[] stylesGIS3D = new Class<?>[] { DefaultMarkStyle.class,
//      DefaultSurfaceShapeStyle.class };
	
	private static final long serialVersionUID = 8349240641245552328L;
	
	public GISDisplayDescriptor(DisplayDescriptor descriptor) {
    super(descriptor.getName());
    set(descriptor);    
  }

  public GISDisplayDescriptor(String name) {
    super(name);
  }

  @Override
  public void set(DisplayDescriptor descriptor) {
  	super.set(descriptor);
  	
  	getLayerOrders().clear();
  	 	
  	if (descriptor.agentClassLayerOrders() != null) {
  		for (String name : descriptor.agentClassLayerOrders()) {
  			addLayerOrder(name, descriptor.getLayerOrder(name));
  		}
  	}
  	
     // For backwards compatibility with DefaultDisplayDescriptor, check if setting
    //  new GIS specific settings is appropriate.
    if (descriptor instanceof GISDisplayDescriptor){
    	setViewType(((GISDisplayDescriptor)descriptor).getViewType());
    	setTrackAgents(((GISDisplayDescriptor)descriptor).getTrackAgents());
    	
    	Map<String,String> coverageMap = ((GISDisplayDescriptor)descriptor).getStaticCoverageMap();
    	
    	staticCoverages.clear();
    	for (String fileName : coverageMap.keySet()) {
    		addStaticCoverage(fileName, coverageMap.get(fileName));
    	}
    	
    	coverageLayers.clear();
    	for (String name : coverageLayers.keySet()) {
    		addCoverageLayer(name, coverageLayers.get(name));
    	}
    }
  }

  @Override
	public DisplayDescriptor makeCopy() {
  	return new GISDisplayDescriptor(this);
	}
  
	public VIEW_TYPE getViewType() {
		if (viewType == null)
			return VIEW_TYPE.FLAT;
		
		return viewType;
	}

	public void setViewType(VIEW_TYPE viewType) {
		this.viewType = viewType;
		scs.fireScenarioChanged(this, "viewType");
	}

	public RenderQuality getRenderQuality() {
		if (renderQuality == null)
			return RenderQuality.MEDIUM;
		
		return renderQuality;
	}

	public void setRenderQuality(RenderQuality renderQuality) {
		this.renderQuality = renderQuality;
	}

	public boolean getTrackAgents() {
		return trackAgents;
	}

	public void setTrackAgents(boolean trackAgents) {
		this.trackAgents = trackAgents;
		scs.fireScenarioChanged(this, "trackAgents");
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Map<String,Boolean> getGlobeLayersMap(){
		if (globeLayers == null)
			globeLayers = new HashMap<String,Boolean>();
		
		return globeLayers;
	}
	
	public void addGlobeLayer(String layerName, boolean enabled) {
		getGlobeLayersMap().put(layerName, enabled);
		scs.fireScenarioChanged(this, "globelayers");
	}
	
	/**
	 * 
	 * @return a Map of static coverages <filename, style>
	 */
	public Map<String, String> getStaticCoverageMap() {
		if (staticCoverages == null)
			staticCoverages = new HashMap<String,String>();
		
		return staticCoverages;
	}

	public void addStaticCoverage(String fileName, String style) {
		getStaticCoverageMap().put(fileName, style);
		scs.fireScenarioChanged(this, "staticCoverage");
	}
	
	/**
	 * 
	 * @return a Map of dynamic coverages <name, style>
	 */
	public Map<String, String> getCoverageLayers() {
		if (coverageLayers == null)
			coverageLayers = new HashMap<String,String>();
		
		return coverageLayers;
	}

	public void addCoverageLayer(String name, String style) {
		if (coverageLayers == null)
			coverageLayers = new HashMap<String,String>();
		
		coverageLayers.put(name, style);
		scs.fireScenarioChanged(this, "coverageLayers");
	}
}