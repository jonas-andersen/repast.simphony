package repast.simphony.statecharts.edit.parts;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.RotatableDecoration;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ITreeBranchEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.draw2d.ui.figures.PolylineConnectionEx;
import org.eclipse.gmf.runtime.notation.View;

import repast.simphony.statecharts.edit.policies.TransitionItemSemanticEditPolicy;
import repast.simphony.statecharts.policies.ShowPropsEditPolicy;

/**
 * @generated
 */
public class TransitionEditPart extends ConnectionNodeEditPart implements ITreeBranchEditPart {

  /**
   * @generated
   */
  public static final int VISUAL_ID = 4001;

  /**
   * @generated
   */
  public TransitionEditPart(View view) {
    super(view);
  }

  /**
   * @generated
   */
  protected void createDefaultEditPolicies() {
    super.createDefaultEditPolicies();
    installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE, new TransitionItemSemanticEditPolicy());
    installEditPolicy(ShowPropsEditPolicy.EDIT_POLICY, new ShowPropsEditPolicy());
  }

  /**
   * Creates figure for this edit part.
   * 
   * Body of this method does not depend on settings in generation model
   * so you may safely remove <i>generated</i> tag and modify it.
   * 
   * @generated
   */

  protected Connection createConnectionFigure() {
    return new TransitionFigure();
  }

  /**
   * @generated
   */
  public TransitionFigure getPrimaryShape() {
    return (TransitionFigure) getFigure();
  }

  /**
   * @generated
   */
  public class TransitionFigure extends PolylineConnectionEx {

    /**
     * @generated
     */
    public TransitionFigure() {
      this.setForegroundColor(ColorConstants.black);

      setTargetDecoration(createTargetDecoration());
    }

    /**
     * @generated
     */
    private RotatableDecoration createTargetDecoration() {
      PolygonDecoration df = new PolygonDecoration();
      df.setFill(true);
      return df;
    }

  }

}
