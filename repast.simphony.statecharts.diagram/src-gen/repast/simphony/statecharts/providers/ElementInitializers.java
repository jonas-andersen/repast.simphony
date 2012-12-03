package repast.simphony.statecharts.providers;

import repast.simphony.statecharts.expressions.StatechartAbstractExpression;
import repast.simphony.statecharts.expressions.StatechartOCLFactory;
import repast.simphony.statecharts.part.StatechartDiagramEditorPlugin;
import repast.simphony.statecharts.scmodel.CompositeState;
import repast.simphony.statecharts.scmodel.FinalState;
import repast.simphony.statecharts.scmodel.History;
import repast.simphony.statecharts.scmodel.PseudoState;
import repast.simphony.statecharts.scmodel.PseudoStateTypes;
import repast.simphony.statecharts.scmodel.State;
import repast.simphony.statecharts.scmodel.StatechartPackage;
import repast.simphony.statecharts.scmodel.Transition;
import repast.simphony.statecharts.util.StatechartsModelUtil;

/**
 * @generated
 */
public class ElementInitializers {

  protected ElementInitializers() {
    // use #getInstance to access cached instance
  }

  /**
   * @generated
   */
  public void init_State_2003(State instance) {
    try {
      Object value_0 = id_State_2003(instance);
      instance.setId((String) value_0);
    } catch (RuntimeException e) {
      StatechartDiagramEditorPlugin.getInstance().logError("Element initialization failed", e); //$NON-NLS-1$						
    }
  }

  /**
   * @generated
   */
  public void init_CompositeState_2004(CompositeState instance) {
    try {
      Object value_0 = id_CompositeState_2004(instance);
      instance.setId((String) value_0);
    } catch (RuntimeException e) {
      StatechartDiagramEditorPlugin.getInstance().logError("Element initialization failed", e); //$NON-NLS-1$						
    }
  }

  /**
   * @generated
   */
  public void init_PseudoState_2005(PseudoState instance) {
    try {
      Object value_0 = StatechartOCLFactory.getExpression(2,
          StatechartPackage.eINSTANCE.getPseudoState(), null).evaluate(instance);

      value_0 = StatechartAbstractExpression.performCast(value_0,
          StatechartPackage.eINSTANCE.getPseudoStateTypes());
      instance.setType((PseudoStateTypes) value_0);
      Object value_1 = id_PseudoState_2005(instance);
      instance.setId((String) value_1);
    } catch (RuntimeException e) {
      StatechartDiagramEditorPlugin.getInstance().logError("Element initialization failed", e); //$NON-NLS-1$						
    }
  }

  /**
   * @generated
   */
  public void init_PseudoState_2006(PseudoState instance) {
    try {
      Object value_0 = StatechartOCLFactory.getExpression(4,
          StatechartPackage.eINSTANCE.getPseudoState(), null).evaluate(instance);

      value_0 = StatechartAbstractExpression.performCast(value_0,
          StatechartPackage.eINSTANCE.getPseudoStateTypes());
      instance.setType((PseudoStateTypes) value_0);
      Object value_1 = id_PseudoState_2006(instance);
      instance.setId((String) value_1);
    } catch (RuntimeException e) {
      StatechartDiagramEditorPlugin.getInstance().logError("Element initialization failed", e); //$NON-NLS-1$						
    }
  }

  /**
   * @generated
   */
  public void init_PseudoState_2007(PseudoState instance) {
    try {
      Object value_0 = StatechartOCLFactory.getExpression(8,
          StatechartPackage.eINSTANCE.getPseudoState(), null).evaluate(instance);

      value_0 = StatechartAbstractExpression.performCast(value_0,
          StatechartPackage.eINSTANCE.getPseudoStateTypes());
      instance.setType((PseudoStateTypes) value_0);
      instance.setId("Entry State Pointer");
    } catch (RuntimeException e) {
      StatechartDiagramEditorPlugin.getInstance().logError("Element initialization failed", e); //$NON-NLS-1$						
    }
  }

  /**
   * @generated
   */
  public void init_FinalState_2008(FinalState instance) {
    try {
      Object value_0 = id_FinalState_2008(instance);
      instance.setId((String) value_0);
    } catch (RuntimeException e) {
      StatechartDiagramEditorPlugin.getInstance().logError("Element initialization failed", e); //$NON-NLS-1$						
    }
  }

  /**
   * @generated
   */
  public void init_State_3001(State instance) {
    try {
      Object value_0 = id_State_3001(instance);
      instance.setId((String) value_0);
    } catch (RuntimeException e) {
      StatechartDiagramEditorPlugin.getInstance().logError("Element initialization failed", e); //$NON-NLS-1$						
    }
  }

  /**
   * @generated
   */
  public void init_CompositeState_3002(CompositeState instance) {
    try {
      Object value_0 = id_CompositeState_3002(instance);
      instance.setId((String) value_0);
    } catch (RuntimeException e) {
      StatechartDiagramEditorPlugin.getInstance().logError("Element initialization failed", e); //$NON-NLS-1$						
    }
  }

  /**
   * @generated
   */
  public void init_PseudoState_3003(PseudoState instance) {
    try {
      Object value_0 = StatechartOCLFactory.getExpression(2,
          StatechartPackage.eINSTANCE.getPseudoState(), null).evaluate(instance);

      value_0 = StatechartAbstractExpression.performCast(value_0,
          StatechartPackage.eINSTANCE.getPseudoStateTypes());
      instance.setType((PseudoStateTypes) value_0);
      Object value_1 = id_PseudoState_3003(instance);
      instance.setId((String) value_1);
    } catch (RuntimeException e) {
      StatechartDiagramEditorPlugin.getInstance().logError("Element initialization failed", e); //$NON-NLS-1$						
    }
  }

  /**
   * @generated
   */
  public void init_PseudoState_3006(PseudoState instance) {
    try {
      Object value_0 = StatechartOCLFactory.getExpression(4,
          StatechartPackage.eINSTANCE.getPseudoState(), null).evaluate(instance);

      value_0 = StatechartAbstractExpression.performCast(value_0,
          StatechartPackage.eINSTANCE.getPseudoStateTypes());
      instance.setType((PseudoStateTypes) value_0);
      Object value_1 = id_PseudoState_3006(instance);
      instance.setId((String) value_1);
    } catch (RuntimeException e) {
      StatechartDiagramEditorPlugin.getInstance().logError("Element initialization failed", e); //$NON-NLS-1$						
    }
  }

  /**
   * @generated
   */
  public void init_FinalState_3007(FinalState instance) {
    try {
      Object value_0 = id_FinalState_3007(instance);
      instance.setId((String) value_0);
    } catch (RuntimeException e) {
      StatechartDiagramEditorPlugin.getInstance().logError("Element initialization failed", e); //$NON-NLS-1$						
    }
  }

  /**
   * @generated
   */
  public void init_History_3008(History instance) {
    try {
      instance.setShallow(true);
      Object value_1 = id_History_3008(instance);
      instance.setId((String) value_1);
    } catch (RuntimeException e) {
      StatechartDiagramEditorPlugin.getInstance().logError("Element initialization failed", e); //$NON-NLS-1$						
    }
  }

  /**
   * @generated
   */
  public void init_History_3009(History instance) {
    try {
      instance.setShallow(false);
      Object value_1 = id_History_3009(instance);
      instance.setId((String) value_1);
    } catch (RuntimeException e) {
      StatechartDiagramEditorPlugin.getInstance().logError("Element initialization failed", e); //$NON-NLS-1$						
    }
  }

  /**
   * @generated
   */
  public void init_Transition_4001(Transition instance) {
    try {
      Object value_0 = ID_Transition_4001(instance);
      instance.setID((String) value_0);
    } catch (RuntimeException e) {
      StatechartDiagramEditorPlugin.getInstance().logError("Element initialization failed", e); //$NON-NLS-1$						
    }
  }

  /**
   * @generated NOT
   */
  private String id_State_2003(State self) {
    try {
      return "State " + StatechartsModelUtil.getNextID(self);
    } catch (RuntimeException e) {
      StatechartDiagramEditorPlugin.getInstance().logError("Element initialization failed", e); //$NON-NLS-1$                                           
    }
    return "";
  }

  /**
   * @generated NOT
   */
  private String id_CompositeState_2004(CompositeState self) {
    try {
      return "Composite State " + StatechartsModelUtil.getNextID(self);
    } catch (RuntimeException e) {
      StatechartDiagramEditorPlugin.getInstance().logError("Element initialization failed", e); //$NON-NLS-1$                                           
    }
    return "";
  }

  /**
   * @generated NOT
   */
  private String id_PseudoState_2005(PseudoState self) {
    try {
      return "Initial State Pointer " + StatechartsModelUtil.getNextID(self);
    } catch (RuntimeException e) {
      StatechartDiagramEditorPlugin.getInstance().logError("Element initialization failed", e); //$NON-NLS-1$                                           
    }
    return "";
  }

  /**
   * @generated NOT
   */
  private String id_PseudoState_2006(PseudoState self) {
    try {
      return "Choice " + StatechartsModelUtil.getNextID(self);
    } catch (RuntimeException e) {
      StatechartDiagramEditorPlugin.getInstance().logError("Element initialization failed", e); //$NON-NLS-1$                                           
    }
    return "";
  }

  /**
   * @generated NOT
   */
  private String id_FinalState_2008(FinalState self) {
    try {
      return "Final State " + StatechartsModelUtil.getNextID(self);
    } catch (RuntimeException e) {
      StatechartDiagramEditorPlugin.getInstance().logError("Element initialization failed", e); //$NON-NLS-1$                                           
    }
    return "";
  }

  /**
   * @generated NOT
   */
  private String id_State_3001(State self) {
    try {
      return "State " + StatechartsModelUtil.getNextID(self);
    } catch (RuntimeException e) {
      StatechartDiagramEditorPlugin.getInstance().logError("Element initialization failed", e); //$NON-NLS-1$                                           
    }
    return "";
  }

  /**
   * @generated NOT
   */
  private String id_CompositeState_3002(CompositeState self) {
    try {
      return "Composite State " + StatechartsModelUtil.getNextID(self);
    } catch (RuntimeException e) {
      StatechartDiagramEditorPlugin.getInstance().logError("Element initialization failed", e); //$NON-NLS-1$                                           
    }
    return "";
  }

  /**
   * @generated NOT
   */
  private String id_PseudoState_3003(PseudoState self) {
    try {
      return "Initial State Pointer " + StatechartsModelUtil.getNextID(self);
    } catch (RuntimeException e) {
      StatechartDiagramEditorPlugin.getInstance().logError("Element initialization failed", e); //$NON-NLS-1$                                           
    }
    return "";
  }

  /**
   * @generated NOT
   */
  private String id_PseudoState_3006(PseudoState self) {
    try {
      return "Choice " + StatechartsModelUtil.getNextID(self);
    } catch (RuntimeException e) {
      StatechartDiagramEditorPlugin.getInstance().logError("Element initialization failed", e); //$NON-NLS-1$                                           
    }
    return "";
  }

  /**
   * @generated NOT
   */
  private String id_FinalState_3007(FinalState self) {
    try {
      return "Final State " + StatechartsModelUtil.getNextID(self);
    } catch (RuntimeException e) {
      StatechartDiagramEditorPlugin.getInstance().logError("Element initialization failed", e); //$NON-NLS-1$                                           
    }
    return "";
  }

  /**
   * @generated NOT
   */
  private String id_History_3008(History self) {
    try {
      return "Shallow History " + StatechartsModelUtil.getNextID(self);
    } catch (RuntimeException e) {
      StatechartDiagramEditorPlugin.getInstance().logError("Element initialization failed", e); //$NON-NLS-1$                                           
    }
    return "";
  }

  /**
   * @generated NOT
   */
  private String id_History_3009(History self) {
    try {
      return "Deep History " + StatechartsModelUtil.getNextID(self);
    } catch (RuntimeException e) {
      StatechartDiagramEditorPlugin.getInstance().logError("Element initialization failed", e); //$NON-NLS-1$                                           
    }
    return "";
  }

  /**
   * @generated NOT
   */
  private String ID_Transition_4001(Transition self) {
    try {
      return "Transition " + StatechartsModelUtil.getNextID(self);
    } catch (RuntimeException e) {
      StatechartDiagramEditorPlugin.getInstance().logError("Element initialization failed", e); //$NON-NLS-1$                                           
    }
    return "";
  }

  /**
   * @generated
   */
  public static ElementInitializers getInstance() {
    ElementInitializers cached = StatechartDiagramEditorPlugin.getInstance()
        .getElementInitializers();
    if (cached == null) {
      StatechartDiagramEditorPlugin.getInstance().setElementInitializers(
          cached = new ElementInitializers());
    }
    return cached;
  }
}
