<<<<<<< HEAD
/*
 * Created by JFormDesigner on Thu Mar 06 16:24:44 EST 2008
 */

package repast.simphony.dataLoader.ui.wizard;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;

import org.pietschy.wizard.InvalidStateException;
import org.pietschy.wizard.PanelWizardStep;
import org.pietschy.wizard.WizardModel;

import repast.simphony.util.ClassUtilities;
import saf.core.ui.util.FileChooserUtilities;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpec;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.Sizes;
import com.jidesoft.swing.CheckBoxList;
import com.thoughtworks.xstream.converters.Converter;

/**
 * @author User #2
 */
public class XMLFileChooserStep extends PanelWizardStep {

  private DataLoaderWizardModel model;

  public XMLFileChooserStep() {
    super("Select XML File",
	"Select the XML file to load from and optionally select any custom XStream "
	    + "converters.");
    initComponents();

    fileFld.getDocument().addDocumentListener(new DocumentListener() {

      public void insertUpdate(DocumentEvent e) {
	setComplete(fileFld.getText().trim().length() > 0);
      }

      public void removeUpdate(DocumentEvent e) {
	setComplete(fileFld.getText().trim().length() > 0);
      }

      public void changedUpdate(DocumentEvent e) {
      }
    });

    browseBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
	selectFile();
      }
    });

    converterList.setCellRenderer(new DefaultListCellRenderer() {
      @Override
      public Component getListCellRendererComponent(JList list, Object value, int index,
	  boolean isSelected, boolean cellHasFocus) {
	value = value != null ? value.getClass().getSimpleName() : value;
	return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
      }
    });
  }

  private void selectFile() {
    String fileText = fileFld.getText().trim();
    File file = null;
    if (fileText.length() > 0) {
      file = new File(fileText).getParentFile();
      if (file == null || !file.exists())
	file = null;
    }
    file = FileChooserUtilities.getOpenFile(file, new FileFilter() {

      public boolean accept(File f) {
	String name = f.getName();
	return f.isDirectory() || name.endsWith("xml") || name.endsWith("zip");
      }

      public String getDescription() {
	return "XML or zipped xml file (*.xml, *.zip)";
      }
    });
    if (file != null)
      fileFld.setText(file.getAbsolutePath());
  }

  public void init(WizardModel wizardModel) {
    super.init(wizardModel);
    this.model = (DataLoaderWizardModel) wizardModel;
    DefaultListModel listModel = new DefaultListModel();

    try {
      for (Class<?> clazz : model.getScenario().getContext().getClasspath().getClasses()) {

	if (Converter.class.isAssignableFrom(clazz)) {
	  Converter converter = (Converter) clazz.newInstance();
	  listModel.addElement(converter);
	}
      }

      converterList.setModel(listModel);

    } catch (IOException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } finally {
      ClassUtilities.doWarn = true;
    }
  }

  public void prepare() {
    XMLContextActionBuilder builder = (XMLContextActionBuilder) model.getBuilder();
    File file = builder.getXMLFile();
    if (file != null) {
      fileFld.setText(file.getAbsolutePath());
      setComplete(file.exists());
    }
    setComplete(false);

    /*
     * java.util.List<Integer> indices = new ArrayList<Integer>();
     * DefaultListModel listModel = (DefaultListModel)converterList.getModel();
     * for (Converter converter : model.converters()) { for (int i = 0; i <
     * listModel.size(); i++) { if
     * (converter.getClass().equals(listModel.elementAt(i).getClass())) {
     * indices.add(i); } } }
     * 
     * int[] sIndices = new int[indices.size()]; for (int i = 0; i <
     * indices.size(); i++) { sIndices[i] = indices.get(i); }
     * converterList.setCheckBoxListSelectedIndices(sIndices);
     */

  }

  public void applyState() throws InvalidStateException {
    super.applyState();
    XMLContextActionBuilder builder = (XMLContextActionBuilder) model.getBuilder();
    builder.setXMLFile(new File(fileFld.getText().trim()));
    builder.clearConverters();

    for (Object obj : converterList.getCheckBoxListSelectedValues()) {
      builder.addConverter((Converter) obj);
    }
  }

  private void initComponents() {
    // JFormDesigner - Component initialization - DO NOT MODIFY
    // //GEN-BEGIN:initComponents
    // Generated using JFormDesigner non-commercial license
    label1 = new JLabel();
    fileFld = new JTextField();
    browseBtn = new JButton();
    label2 = new JLabel();
    scrollPane1 = new JScrollPane();
    converterList = new CheckBoxList();
    CellConstraints cc = new CellConstraints();

    // ======== this ========
    setLayout(new FormLayout(new ColumnSpec[] { FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
	FormSpecs.LABEL_COMPONENT_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC,
	FormSpecs.LABEL_COMPONENT_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC,
	FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
	new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
	FormSpecs.LABEL_COMPONENT_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC }, new RowSpec[] {
	FormSpecs.DEFAULT_ROWSPEC, FormSpecs.LINE_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC,
	FormSpecs.LINE_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.LINE_GAP_ROWSPEC,
	new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW) }));

    // ---- label1 ----
    label1.setText("File:");
    add(label1, cc.xywh(1, 1, 7, 1));
    add(fileFld, cc.xywh(3, 3, 5, 1));

    // ---- browseBtn ----
    browseBtn.setText("Browse");
    add(browseBtn, cc.xy(9, 3));

    // ---- label2 ----
    label2.setText("Converters:");
    add(label2, cc.xywh(1, 5, 7, 1));

    // ======== scrollPane1 ========
    {
      scrollPane1.setViewportView(converterList);
    }
    add(scrollPane1, cc.xywh(3, 7, 7, 1));
    // JFormDesigner - End of component initialization //GEN-END:initComponents
  }

  // JFormDesigner - Variables declaration - DO NOT MODIFY //GEN-BEGIN:variables
  // Generated using JFormDesigner non-commercial license
  private JLabel label1;
  private JTextField fileFld;
  private JButton browseBtn;
  private JLabel label2;
  private JScrollPane scrollPane1;
  private CheckBoxList converterList;
  // JFormDesigner - End of variables declaration //GEN-END:variables
=======
/*
 * Created by JFormDesigner on Thu Mar 06 16:24:44 EST 2008
 */

package repast.simphony.dataLoader.ui.wizard;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;

import org.pietschy.wizard.InvalidStateException;
import org.pietschy.wizard.PanelWizardStep;
import org.pietschy.wizard.WizardModel;

import repast.simphony.util.ClassUtilities;
import saf.core.ui.util.FileChooserUtilities;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpec;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.Sizes;
import com.jidesoft.swing.CheckBoxList;
import com.thoughtworks.xstream.converters.Converter;

/**
 * @author User #2
 */
public class XMLFileChooserStep extends PanelWizardStep {

  private DataLoaderWizardModel model;

  public XMLFileChooserStep() {
    super("Select XML File",
	"Select the XML file to load from and optionally select any custom XStream "
	    + "converters.");
    initComponents();

    fileFld.getDocument().addDocumentListener(new DocumentListener() {

      public void insertUpdate(DocumentEvent e) {
	setComplete(fileFld.getText().trim().length() > 0);
      }

      public void removeUpdate(DocumentEvent e) {
	setComplete(fileFld.getText().trim().length() > 0);
      }

      public void changedUpdate(DocumentEvent e) {
      }
    });

    browseBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
	selectFile();
      }
    });

    converterList.setCellRenderer(new DefaultListCellRenderer() {
      @Override
      public Component getListCellRendererComponent(JList list, Object value, int index,
	  boolean isSelected, boolean cellHasFocus) {
	value = value != null ? value.getClass().getSimpleName() : value;
	return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
      }
    });
  }

  private void selectFile() {
    String fileText = fileFld.getText().trim();
    File file = null;
    if (fileText.length() > 0) {
      file = new File(fileText).getParentFile();
      if (file == null || !file.exists())
	file = null;
    }
    file = FileChooserUtilities.getOpenFile(file, new FileFilter() {

      public boolean accept(File f) {
	String name = f.getName();
	return f.isDirectory() || name.endsWith("xml") || name.endsWith("zip");
      }

      public String getDescription() {
	return "XML or zipped xml file (*.xml, *.zip)";
      }
    });
    if (file != null)
      fileFld.setText(file.getAbsolutePath());
  }

  public void init(WizardModel wizardModel) {
    super.init(wizardModel);
    this.model = (DataLoaderWizardModel) wizardModel;
    DefaultListModel listModel = new DefaultListModel();

    try {
      for (Class<?> clazz : model.getScenario().getContext().getClasspath().getClasses()) {

	if (Converter.class.isAssignableFrom(clazz)) {
	  Converter converter = (Converter) clazz.newInstance();
	  listModel.addElement(converter);
	}
      }

      converterList.setModel(listModel);

    } catch (IOException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } finally {
      ClassUtilities.doWarn = true;
    }
  }

  public void prepare() {
    XMLContextActionBuilder builder = (XMLContextActionBuilder) model.getBuilder();
    File file = builder.getXMLFile();
    if (file != null) {
      fileFld.setText(file.getAbsolutePath());
      setComplete(file.exists());
    }
    setComplete(false);

    /*
     * java.util.List<Integer> indices = new ArrayList<Integer>();
     * DefaultListModel listModel = (DefaultListModel)converterList.getModel();
     * for (Converter converter : model.converters()) { for (int i = 0; i <
     * listModel.size(); i++) { if
     * (converter.getClass().equals(listModel.elementAt(i).getClass())) {
     * indices.add(i); } } }
     * 
     * int[] sIndices = new int[indices.size()]; for (int i = 0; i <
     * indices.size(); i++) { sIndices[i] = indices.get(i); }
     * converterList.setCheckBoxListSelectedIndices(sIndices);
     */

  }

  public void applyState() throws InvalidStateException {
    super.applyState();
    XMLContextActionBuilder builder = (XMLContextActionBuilder) model.getBuilder();
    builder.setXMLFile(new File(fileFld.getText().trim()));
    builder.clearConverters();

    for (Object obj : converterList.getCheckBoxListSelectedValues()) {
      builder.addConverter((Converter) obj);
    }
  }

  private void initComponents() {
    // JFormDesigner - Component initialization - DO NOT MODIFY
    // //GEN-BEGIN:initComponents
    // Generated using JFormDesigner non-commercial license
    label1 = new JLabel();
    fileFld = new JTextField();
    browseBtn = new JButton();
    label2 = new JLabel();
    scrollPane1 = new JScrollPane();
    converterList = new CheckBoxList();
    CellConstraints cc = new CellConstraints();

    // ======== this ========
    setLayout(new FormLayout(new ColumnSpec[] { FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
	FormSpecs.LABEL_COMPONENT_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC,
	FormSpecs.LABEL_COMPONENT_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC,
	FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
	new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
	FormSpecs.LABEL_COMPONENT_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC }, new RowSpec[] {
	FormSpecs.DEFAULT_ROWSPEC, FormSpecs.LINE_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC,
	FormSpecs.LINE_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.LINE_GAP_ROWSPEC,
	new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW) }));

    // ---- label1 ----
    label1.setText("File:");
    add(label1, cc.xywh(1, 1, 7, 1));
    add(fileFld, cc.xywh(3, 3, 5, 1));

    // ---- browseBtn ----
    browseBtn.setText("Browse");
    add(browseBtn, cc.xy(9, 3));

    // ---- label2 ----
    label2.setText("Converters:");
    add(label2, cc.xywh(1, 5, 7, 1));

    // ======== scrollPane1 ========
    {
      scrollPane1.setViewportView(converterList);
    }
    add(scrollPane1, cc.xywh(3, 7, 7, 1));
    // JFormDesigner - End of component initialization //GEN-END:initComponents
  }

  // JFormDesigner - Variables declaration - DO NOT MODIFY //GEN-BEGIN:variables
  // Generated using JFormDesigner non-commercial license
  private JLabel label1;
  private JTextField fileFld;
  private JButton browseBtn;
  private JLabel label2;
  private JScrollPane scrollPane1;
  private CheckBoxList converterList;
  // JFormDesigner - End of variables declaration //GEN-END:variables
>>>>>>> branch 'system_dynamics' of ssh://bragen@git.code.sf.net/p/repast/repast-simphony
}
