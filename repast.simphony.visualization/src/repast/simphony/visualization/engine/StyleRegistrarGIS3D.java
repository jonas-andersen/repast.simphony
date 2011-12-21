/**
 * 
 */
package repast.simphony.visualization.engine;

import repast.simphony.visualization.gisnew.StyleGIS;

/**
 * Style registrar for 3D GIS displays.
 * 
 * @author Nick Collier
 */
public class StyleRegistrarGIS3D extends StyleRegistrar<StyleGIS<?>> {

  /* (non-Javadoc)
   * @see repast.simphony.visualization.engine.StyleRegistrar#getEditedStyle(java.lang.String)
   */
  @SuppressWarnings("unchecked")
  @Override
  protected StyleGIS<?> createdEditedStyle(String editedStyleName) {
//    return new EditedStyleGIS3D(editedStyleName);
  	
  	return null;
  }
}