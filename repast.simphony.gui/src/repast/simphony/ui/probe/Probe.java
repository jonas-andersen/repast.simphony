package repast.simphony.ui.probe;

import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.JPanel;

import simphony.util.ThreadUtilities;

import com.jgoodies.binding.PresentationModel;

/**
 * Encapsulates the gui representation and the model of a gui probe.S
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class Probe {

  private JPanel panel;
  private List<PresentationModel> models;
  private boolean buffered = false;
  private Updater updater;
  
  private class Updater implements Runnable{
  	public void run() {
  		for (PresentationModel model : models) {
  			((ProbeableBean) model.getBean()).update();
  		}
  	}  	
  }
  
  public Probe(List<PresentationModel> models, JPanel panel) {
    this(models, panel, false);
  }

  public Probe(List<PresentationModel> models, JPanel panel, boolean buffered) {
    this.panel = panel;
    this.models = models;
    this.buffered = buffered;
    updater = new Updater();
  }

  /**
   * A probe is buffered if changes to its values are not automatically
   * reflected in the probed object. Calling commit on a buffered probe
   * will commit the changes to the probed object.
   *
   * @return true if this Probe is buffered, false otherwise.
   */
  public boolean isBuffered() {
    return buffered;
  }

  /**
   * Updates the probe to show the latest values of the probed property.
   */
  public void update() {
  	// This must wait on the event dispatch thread otherwise the GUI may hang
  	//  when probes are updated very fast.
  	ThreadUtilities.runInEventThreadAndWait(updater);
  }

  /**
   * Flush any pending changes from the bean. Note that is only has an effect
   * if this is a buffered probe.
   */
  public void flush() {
    for (PresentationModel model : models) {
      model.triggerFlush();
    }
  }

  /**
   * Commit any pending changes to the bean. Note that is only has an effect
   * if this is a buffered probe.
   */
  public void commit() {
    for (PresentationModel model : models) {
      model.triggerCommit();
    }
  }

  /**
   * Gets the panel that displays the gui widgets for this probe.
   *
   * @return the panel that displays the gui widgets for this probe.
   */
  public JPanel getPanel() {
    return panel;
  }

  /**
   * Adds a property change listener to this Probe. This listener will be called
   * when the probed object is updated.
   *
   * @param listener
   */
  public void addPropertyChangeListener(PropertyChangeListener listener) {
    for (PresentationModel model : models) {
      ((ProbeableBean) model.getBean()).addPropertyChangeListener(listener);
    }
  }

  /**
   * Removes a property change listener from this Probe.
   *
   * @param listener
   */
  public void removePropertyChangeListener(PropertyChangeListener listener) {
    for (PresentationModel model : models) {
      ((ProbeableBean) model.getBean()).removePropertyChangeListener(listener);
    }
  }
}